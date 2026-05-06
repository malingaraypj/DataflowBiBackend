package com.example.dataflowBi.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TableMetadata {
	private String tableName;
	private List<ColumnMetadata> columns;
	private long rowCount;
}
