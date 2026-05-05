package com.example.dataflowBi.DTO;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class AnalysisResponse {
    private List<Map<String, Object>> data;
    private List<ChartSuggestion> suggestedCharts;
    private ChartMetaData metaData;
}
