package com.example.dataflowBi.DTO;

import lombok.Data;

@Data
public class Aggregation{
	private String column;
	private String functions;
	private String alias;
}