package com.example.dataflowBi.DTO;

import lombok.Data;

@Data
public class SortRequest {
	private String columnName; 
	private String direction;  // ASC  or DESC
}
