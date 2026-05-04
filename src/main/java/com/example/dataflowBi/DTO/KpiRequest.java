package com.example.dataflowBi.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KpiRequest {
	 private String tableName;
	 private List<Aggregation> aggregations;
	 private List<String> groupByColumns;
}


