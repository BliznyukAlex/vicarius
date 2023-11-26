package com.vicarius.exam.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetRequest;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Map;

@Service
public class ExamDocService {

    private final ElasticsearchClient elasticsearchClient;

    public ExamDocService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    public Object createIndex(String indexName) throws Exception {
        CreateIndexRequest.Builder builder = new CreateIndexRequest.Builder()
                .index(indexName);
        CreateIndexRequest request = builder.build();
        CreateIndexResponse response = null;
        try {
            response = elasticsearchClient.indices().create(request);
        } catch (Exception e) {
            throw new Exception("Failed to create index: " + indexName);
        }
        return response.index();
    }

    public String addDocument(String indexName, Map<String, Object> document) throws Exception {
        if (elasticsearchClient.indices().exists(e -> e.index(indexName)).value()) {
            return elasticsearchClient.index(i -> i
                    .index(indexName)
                    .document(document)).id();
        } else {
            throw new Exception("Index does not exist: " + indexName);
        }
    }

    public Object getDocumentById(String indexName, String documentId) throws Exception {
        elasticsearchClient.indices().exists(e -> e.index(indexName));
        if (!elasticsearchClient.indices().exists(e -> e.index(indexName)).value()) {
            throw new Exception("Index does not exist: " + indexName);
        }
        GetResponse<Object> response = elasticsearchClient.get(new GetRequest.Builder().index(indexName).id(documentId).build(), (Type) Map.class);
        if (response.found()) {
            return response.source();
        } else {
            throw new Exception("Document: " + documentId + " does not exist in index: " + indexName);
        }
    }
}
