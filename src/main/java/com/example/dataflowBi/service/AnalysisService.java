package com.example.dataflowBi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dataflowBi.DTO.AnalysisRequest;
import com.example.dataflowBi.DTO.AnalysisResponse;
import com.example.dataflowBi.repository.AnalysisRepository;

@Service
public class AnalysisService {

    @Autowired
    AnalysisRepository analysisRepository;
    
    public AnalysisResponse executeAnalysis(AnalysisRequest request) {
    
    
    List<Map<String, Object>> rawData = analysisRepository.fetchAnalysedData(request);
    List<String> suggestedCharts = suggestCharts(request);

    return new AnalysisResponse(rawData, suggestedCharts);
}


    private List<String> suggestCharts(AnalysisRequest request) {
        List<String> suggestions = new ArrayList<>();
        int dimensionCount = request.getDimensions() == null ? 0 : request.getDimensions().size();

        if (dimensionCount == 0) {
            // No dimensions, just a single aggregated number
            suggestions.add("KPI_CARD");
            suggestions.add("GAUGE");
        } 
        else if (dimensionCount == 1) {
            String dimName = request.getDimensions().get(0).toLowerCase();
            // If the column name implies time, suggest a Line Chart
            if (dimName.contains("date") || dimName.contains("time") || dimName.contains("month") || dimName.contains("year")) {
                suggestions.add("LINE");
                suggestions.add("AREA");
                suggestions.add("BAR"); 
            } else {
                // Standard categorical data
                suggestions.add("BAR");
                suggestions.add("PIE");
                suggestions.add("DONUT");
            }
        } 
        else if (dimensionCount >= 2) {
            // Multiple dimensions
            suggestions.add("STACKED_BAR");
            suggestions.add("GROUPED_BAR");
            suggestions.add("HEATMAP");
        }

        return suggestions;
    }
}
