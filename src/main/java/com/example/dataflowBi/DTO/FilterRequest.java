package com.example.dataflowBi.DTO;

import java.util.List;

import lombok.Data;

@Data
public class FilterRequest {
	private String columnName;
	private String operator;  // "EQUALS", "GREATER_THAN", "LESS_THAN"
	private List<Object> values;   
}
