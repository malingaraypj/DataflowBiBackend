package com.example.dataflowBi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.dataflowBi.DTO.KpiRequest;
import com.example.dataflowBi.DTO.KpiResponse;
import com.example.dataflowBi.service.KpiService;

@RestController
@RequestMapping("/kpi")
@CrossOrigin
public class KpiController {

    @Autowired
    private KpiService kpiService;

    @PostMapping
    public ResponseEntity<KpiResponse> getKpis(@RequestBody KpiRequest request) throws Exception {
    	System.out.println(request);
        return ResponseEntity.ok(kpiService.generateKpis(request));
    }
}