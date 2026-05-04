package com.example.dataflowBi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dataflowBi.model.TableMetadata;
import com.example.dataflowBi.service.SchemaService;

@RestController
@RequestMapping("/tables")
public class SchemaController {

    @Autowired
    private SchemaService schemaService;

    @GetMapping
    public ResponseEntity<List<TableMetadata>> getTables() {
        try {
            return ResponseEntity.ok(schemaService.getSchema());
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
