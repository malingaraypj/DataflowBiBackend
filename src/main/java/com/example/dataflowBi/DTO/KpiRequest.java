package com.example.dataflowBi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KpiRequest {
	 private String tableName;
	    private String measureColumn;
	    private String aggregationType;
	    private String dateColumn;
	    private String timeFrame; 
}
