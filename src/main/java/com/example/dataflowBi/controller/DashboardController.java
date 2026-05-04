package com.example.dataflowBi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dataflowBi.DTO.KpiRequest;
import com.example.dataflowBi.DTO.KpiResponse;
import com.example.dataflowBi.service.KpiService;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
	@Autowired
	private KpiService kpiService;
	
	@PostMapping("/kpi")
	public ResponseEntity<KpiResponse> getKpiData(@RequestBody KpiRequest request){
		KpiResponse response = kpiService.calculateKpi(request);
		
		return ResponseEntity.ok(response);
	}

}
