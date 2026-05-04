package com.example.dataflowBi.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalysisRequest {
	private String tableName;
	private List<String> dimensions;
	private String measureColumn;
	private String aggregationType;  
	
	private List<FilterRequest> filters;
	private List<SortRequest> sorts;
}
