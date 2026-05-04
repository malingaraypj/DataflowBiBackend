package com.example.dataflowBi.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChartMetaData {
	private String xAxisKey;
	private List<String> seriesKeys;
	private List<String> facetKeys;
	private String yAxisKey;	
}
