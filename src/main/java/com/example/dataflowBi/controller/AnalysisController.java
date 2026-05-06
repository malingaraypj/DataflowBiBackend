package com.example.dataflowBi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dataflowBi.DTO.AnalysisRequest;
import com.example.dataflowBi.DTO.AnalysisResponse;
import com.example.dataflowBi.service.AnalysisService;

@RestController
@RequestMapping("/analyze")
@CrossOrigin(origins = "http://localhost:3000/")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    @PostMapping
    public ResponseEntity<?> performAnalysis(@RequestBody AnalysisRequest request) {
        try {
        	System.out.println(request);
            AnalysisResponse response = analysisService.executeAnalysis(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

}
