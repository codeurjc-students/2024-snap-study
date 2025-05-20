package com.snapstudy.backend.opensearch;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

@Service
public class OpenSearchService {

    private final Region region = Region.EU_WEST_1;

    public void search(String domainEndpoint, String indexName, String queryJson) {
        try {
            String url = String.format("https://%s/%s/_search", domainEndpoint, indexName);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(queryJson))
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("--------------------------------------------------");
            System.out.println("--------------------------------------------------");
            System.out.println("Search response:");
            System.out.println(response.body());
            System.out.println("--------------------------------------------------");
            System.out.println("--------------------------------------------------");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}