package com.snapstudy.backend.opensearch;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.snapstudy.backend.model.SearchResult;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.signer.Aws4Signer;
import software.amazon.awssdk.http.SdkHttpFullRequest;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.regions.Region;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OpenSearchService {

    private final Region region = Region.EU_WEST_1;
    private String opensearchEndpoint = System.getenv("AWS_OPENSEARCH_ENDPOINT");
    private String opensearchIndex = "snapstudy-index";

    public List<SearchResult> search(String query) {
        try {
            String queryJson = buildQuery(query);

            String url = String.format("%s/%s/_search", opensearchEndpoint, opensearchIndex);

            // Construir la request sin firma
            SdkHttpFullRequest unsignedRequest = SdkHttpFullRequest.builder()
                    .method(SdkHttpMethod.POST)
                    .uri(URI.create(url))
                    .putHeader("Content-Type", "application/json")
                    .contentStreamProvider(() -> new ByteArrayInputStream(queryJson.getBytes(StandardCharsets.UTF_8)))
                    .build();

            // Firmar con SigV4
            Aws4Signer signer = Aws4Signer.create();
            SdkHttpFullRequest signedRequest = signer.sign(unsignedRequest,
                    software.amazon.awssdk.auth.signer.params.Aws4SignerParams.builder()
                            .signingRegion(region)
                            .signingName("es")
                            .awsCredentials(DefaultCredentialsProvider.create().resolveCredentials())
                            .build());

            // Construir HttpRequest de Java con headers firmados (excepto Host)
            HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .POST(HttpRequest.BodyPublishers.ofString(queryJson));

            for (Map.Entry<String, java.util.List<String>> headerEntry : signedRequest.headers().entrySet()) {
                String headerName = headerEntry.getKey();
                if (!headerName.equalsIgnoreCase("host")) {
                    for (String value : headerEntry.getValue()) {
                        httpRequestBuilder.header(headerName, value);
                    }
                }
            }

            HttpRequest httpRequest = httpRequestBuilder.build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            List<SearchResult> result = trans(response.body());

            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String buildQuery(String query) {
        return """
                {
                  "query": {
                    "query_string": {
                      "query": "%s",
                      "fields": ["title^2", "content^1"],
                      "fuzziness": "AUTO"
                    }
                  }
                }
                """.formatted(query);
    }

    public List<SearchResult> trans(String response) {
        String jsonResponse = response; // Aquí pones la respuesta JSON como String

        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray hitsArray = jsonObject.getJSONObject("hits").getJSONArray("hits");

        List<SearchResult> searchResults = new ArrayList<>();

        for (int i = 0; i < hitsArray.length(); i++) {
            JSONObject hit = hitsArray.getJSONObject(i);
            JSONObject source = hit.getJSONObject("_source");

            Long index = Long.parseLong(source.getString("index"));
            String title = source.getString("title");

            searchResults.add(new SearchResult(index, title));
        }

        return searchResults;
    }

}