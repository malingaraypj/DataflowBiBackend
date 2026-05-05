package com.example.dataflowBi.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalysisRequest {
	public AnalysisRequest() {
		// TODO Auto-generated constructor stub
	}

	private String tableName;
	private List<String> dimensions;
	private List<String> measureColumns;
	private String aggregationType;  
	
	private List<FilterRequest> filters;

	
}
