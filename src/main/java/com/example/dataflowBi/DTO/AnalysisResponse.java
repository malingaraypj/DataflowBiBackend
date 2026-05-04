package com.example.dataflowBi.DTO;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalysisResponse {
    // The actual aggregated rows from the database
    private List<Map<String, Object>> data;
    
    // An intelligent list of what charts the frontend should offer the user
    private List<String> suggestedChartTypes;
}

