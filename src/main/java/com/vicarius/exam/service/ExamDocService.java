package com.vicarius.exam.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch.core.GetRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExamDocService {

    private final ElasticsearchClient elasticsearchClient;

    public ExamDocService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    public Object createIndex(Map<String, Object> indexConfig) {
        String indexName = (String) indexConfig.get("name");
        Map<String, Object> mappings = (Map<String, Object>) indexConfig.get("mappings");
        Map<String, Property> convertedMappings = mappings != null ? convertMappings(mappings) : null;
        CreateIndexRequest.Builder builder = new CreateIndexRequest.Builder()
                .index(indexName);
        if (convertedMappings != null) {
            builder.mappings(m -> m.properties(convertedMappings));
        }
        CreateIndexRequest request = builder.build();
        CreateIndexResponse response = null;
        try {
            response = elasticsearchClient.indices().create(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!response.acknowledged()) {
            throw new IllegalStateException("Failed to create index: " + indexName);
        }
        return response.index();
    }

    private Map<String, Property> convertMappings(Map<String, Object> mappings) {
        return mappings.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> {
                            if (!(e.getValue() instanceof Map)) {
                                throw new IllegalArgumentException("Field properties must be a Map");
                            }
                            @SuppressWarnings("unchecked")
                            Map<String, Object> fieldProperties = (Map<String, Object>) e.getValue();
                            return convertToProperty(fieldProperties);
                        }));
    }

    private Property convertToProperty(Map<String, Object> fieldProperties) {
        String type = (String) fieldProperties.get("type");
        if (type == null) {
            throw new IllegalArgumentException("Field type is missing");
        }
        return Property.of(p -> {
            switch (type) {
                case "text" -> p.text(t -> t.analyzer((String) fieldProperties.getOrDefault("analyzer", "standard")));
                case "date" -> p.date(d -> d.format((String) fieldProperties.getOrDefault("format", "strict_date_optional_time")));
            }
            return p;
        });
    }


    public String addDocument(String indexName, Map<String, Object> document) throws IOException {
        return elasticsearchClient.index(i -> i
                .index(indexName)
                .document(document)).id();
    }

    public Object getDocumentById(String indexName, String documentId) throws IOException {
        return elasticsearchClient.get(new GetRequest.Builder().index(indexName).id(documentId).build(), (Type) Map.class).source();
    }
}
