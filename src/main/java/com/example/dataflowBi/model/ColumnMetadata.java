package com.example.dataflowBi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ColumnMetadata {
	private String columnName;
	private String dataType;
	private String logicalType;
}
