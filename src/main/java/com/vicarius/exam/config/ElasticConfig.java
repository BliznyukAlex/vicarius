package com.vicarius.exam.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticConfig {

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        RestClient restClient = RestClient
                .builder(HttpHost.create("https://3e4034e89a05419ebd25f2de30060ac8.us-east-2.aws.elastic-cloud.com"))
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization", "ApiKey " + "eGJ1d0M0d0JneDI2eVNOdVBrdWU6Q1Z6MVFMekxRbi1EM05vV1F0RXNlUQ==")
                })
                .build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }
}
