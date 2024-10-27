package com.Elastickserch_code_crud.elastick.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport; // Remove this import
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        /**
         *RestClientBuilder is configured to connect to the Elasticsearch server at localhost on port 9200.
         * HttpHost("localhost", 9200) specifies the hostname and port where Elasticsearch is running
         */
        RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost("localhost", 9200));
        /**
         * This creates a RestClient instance from the RestClientBuilder, which will handle HTTP communication with Elasticsearch.
         * The RestClient is a low-level HTTP client that manages the actual connection.
         */
        RestClient restClient = restClientBuilder.build();

        /**
         * Once ElasticsearchTransport is configured with RestClient and a JSON mapper, it can be reused by ElasticsearchClient throughout the application, ensuring consistency in data serialization, request handling, and error management.
         * JacksonJsonpMapper is specified to handle JSON serialization and deserialization for Elasticsearch requests and responses.
         * This setup enables the ElasticsearchClient to work with structured data in Java, rather than raw JSON.
         */
        // Create ElasticsearchTransport using RestClient and JSON codec
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new co.elastic.clients.json.jackson.JacksonJsonpMapper());
/**
 * ElasticsearchClient is created using the ElasticsearchTransport. This high-level client provides structured methods for Elasticsearch operations like indexing, retrieving, and deleting documents.
 */
        return new ElasticsearchClient(transport);
    }
}