package com.example.dataflowBi.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dataflowBi.repository.DashboardRepository;

@Service
public class DashboardService {
	
	@Autowired
	private DashboardRepository dashboardRepository;
	
	public List<Map<String, Object>> getTableRows(String tableName,int count){
		return dashboardRepository.findTableRows(tableName, count);
	}
}
