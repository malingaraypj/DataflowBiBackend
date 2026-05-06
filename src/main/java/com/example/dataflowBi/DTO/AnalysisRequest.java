package com.example.dataflowBi.DTO;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalysisRequest {
	private String tableName;
	private List<String> dimensions;
	private List<String> measureColumns;
	private String aggregationType; 
	private Map<String, String> dateColumn;  
	private List<FilterRequest> filters;
}
