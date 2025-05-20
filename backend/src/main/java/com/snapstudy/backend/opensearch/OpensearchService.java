package com.snapstudy.backend.opensearch;

import org.springframework.stereotype.Service;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.opensearch.client.opensearch._types.query_dsl.QueryBuilders;
import org.opensearch.client.transport.Transport;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.HttpHost;

@Service
public class OpensearchService {

    private final OpenSearchClient client;
    private final String indexName = "snapstudy-index";

    public OpensearchService() {
        // Configuración de conexión OpenSearch usando HTTPClient
        String hostname = "localhost";
        int port = 9200;
        String scheme = "http"; // Si usas HTTPS, cámbialo a "https"

        // Crear cliente HTTP básico
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // Crear cliente RestClient de Elasticsearch, utilizado para la conexión
        RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost(hostname, port, scheme));
        RestClient restClient = restClientBuilder.build();

        // Crear transporte de OpenSearch utilizando el RestClient y JacksonJsonpMapper
        Transport transport = new OpenSearchTransport(
            restClient, 
            new JacksonJsonpMapper()
        );

        // Crear cliente OpenSearch
        this.client = new OpenSearchClient(transport);
    }

    public String buscarPorIndex(String queryString) {
        // Crear la solicitud de búsqueda
        SearchRequest searchRequest = new SearchRequest.Builder()
            .index(indexName)  // Especificar el índice
            .query(q -> q
                .match(m -> m
                    .field("index")   // Campo 'index' en el índice
                    .query(queryString) // El valor a buscar
                )
            )
            .build();

        try {
            // Ejecutar la búsqueda
            SearchResponse<Object> response = client.search(searchRequest, Object.class);

            // Recorrer los resultados y devolver el primer encontrado
            for (Hit<Object> hit : response.hits().hits()) {
                return hit.source().toString();  // Devolver el primer resultado encontrado
            }

        } catch (Exception e) {
            e.printStackTrace();  // Manejo de error
        }

        return "No se encontró el documento con el valor: " + queryString;
    }
}
