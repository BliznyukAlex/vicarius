package com.vicarius.exam.controllers;

import com.vicarius.exam.service.ExamDocService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
public class ElasticController {

    private final ExamDocService examDocService;

    public ElasticController(ExamDocService examDocService) {
        this.examDocService = examDocService;
    }

    @PostMapping("/index")
    public ResponseEntity<?> createIndex(@RequestBody Map<String, Object> indexConfig) {
        try {
            return ResponseEntity.ok(examDocService.createIndex(indexConfig));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating index");
        }
    }

    @PostMapping("/index/{indexName}/document")
    public ResponseEntity<?> addDocument(@PathVariable String indexName, @RequestBody Map<String, Object> document) {
        try {
            return ResponseEntity.ok(examDocService.addDocument(indexName, document));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding document: " + e.getMessage());
        }
    }

    @GetMapping("/index/{indexName}/document/{documentId}")
    public ResponseEntity getDocumentById(@PathVariable String indexName, @PathVariable String documentId) {
        try {
            return ResponseEntity.ok(examDocService.getDocumentById(indexName, documentId));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting document: " + e.getMessage());
        }
    }
}
