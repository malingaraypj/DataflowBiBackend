package com.example.dataflowBi.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dataflowBi.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin
public class DashboardController {
	@Autowired
	private DashboardService dashboardService;
	
	@GetMapping("/table/{tableName}/{count}")
	public ResponseEntity<List<Map<String, Object>>> getTableRows(@PathVariable String tableName, @PathVariable int count){
		return new ResponseEntity<>(dashboardService.getTableRows(tableName, count),HttpStatus.OK);
	}

}
