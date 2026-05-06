package com.example.dataflowBi.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dataflowBi.DTO.*;
import com.example.dataflowBi.repository.AnalysisRepository;
import com.example.dataflowBi.util.CorrelationHelper;

@Service
public class AnalysisService {

    @Autowired
    AnalysisRepository analysisRepository;

    public AnalysisResponse executeAnalysis(AnalysisRequest request) {
    	
        List<Map<String, Object>> rawData;
        
        if(request.getDateColumn() !=null && !request.getDateColumn().isEmpty()) {
        	rawData = analysisRepository.fetchAnalysedDataByDate(request);
        }else {
        	rawData = analysisRepository.fetchAnalysedData(request);
        }
        
        
        //  Prepare numeric data for correlation calculation
        Map<String, double[]> correlationData = prepareCorrelationData(request.getMeasureColumns(), rawData);

        //  Get suggestions (now passing the converted map)
        List<ChartSuggestion> suggestedCharts = suggestCharts(request, correlationData);

        //  Generate metadata for the "default" view
        ChartMetaData metadata = generateMetadata(request, suggestedCharts);

        return new AnalysisResponse(rawData, suggestedCharts, metadata);
    }

  
    private Map<String, double[]> prepareCorrelationData(List<String> measures, List<Map<String, Object>> rawData) {
        Map<String, double[]> dataMap = new HashMap<>();
        if (measures == null || rawData == null || rawData.isEmpty()) return dataMap;

        for (String measure : measures) {
            double[] values = rawData.stream()
                .mapToDouble(row -> {
                    Object val = row.get(measure);
                    return (val instanceof Number) ? ((Number) val).doubleValue() : 0.0;
                })
                .toArray();
            dataMap.put(measure, values);
        }
        return dataMap;
    }

    private ChartMetaData generateMetadata(AnalysisRequest request, List<ChartSuggestion> suggestions) {
        
    	
    	// If we found a strong correlation, we should use those columns as default axes!
        if (!suggestions.isEmpty() && suggestions.get(0).getChartType().equals("SCATTER_PLOT")) {
            ChartSuggestion top = suggestions.get(0);
            return new ChartMetaData(
                top.getXAxisColumn(), 
                new ArrayList<>(), 
                new ArrayList<>(), 
                top.getYAxisColumn()
            );
        }

        List<String> dims = request.getDimensions();
        List<String> measures = request.getMeasureColumns();
        String xAxisKey = (dims != null && !dims.isEmpty()) ? dims.get(0) : null;
        String yAxisKey = (measures != null && !measures.isEmpty()) ? measures.get(0) : "value";
        
        return new ChartMetaData(xAxisKey, new ArrayList<>(), new ArrayList<>(), yAxisKey);
    }

    private List<ChartSuggestion> suggestCharts(AnalysisRequest request, Map<String, double[]> fetchedData) {
    List<ChartSuggestion> suggestions = new ArrayList<>();

    int dimensionCount = request.getDimensions() == null ? 0 : request.getDimensions().size();
    int measureCount = request.getMeasureColumns() == null ? 0 : request.getMeasureColumns().size();

    // 1. Handle Measures (Scatter & Bubble) with Correlation
    if (measureCount >= 2 && fetchedData != null && !fetchedData.isEmpty()) {
        // Find the best correlated pair using our helper
        CorrelationHelper.TopCorrelation topPair =
            CorrelationHelper.findStrongestCorrelation(request.getMeasureColumns(), fetchedData);

        String msg = String.format("Suggested based on high correlation (%.2f)", topPair.getScore());

        // Suggest Scatter Plot for the best pair
        suggestions.add(new ChartSuggestion(
            "SCATTER_PLOT", topPair.getCol1(), topPair.getCol2(), null, msg
        ));

        // If >= 3 measures, add Bubble Chart using the 3rd column as size
        if (measureCount >= 3) {
            String sizeCol = request.getMeasureColumns().stream()
                .filter(col -> !col.equals(topPair.getCol1()) && !col.equals(topPair.getCol2()))
                .findFirst()
                .orElse(request.getMeasureColumns().get(2)); // fallback

            suggestions.add(new ChartSuggestion(
                "BUBBLE_CHART", topPair.getCol1(), topPair.getCol2(), sizeCol,
                "Bubble size based on " + sizeCol
            ));

            // Bonus: Suggest a correlation matrix/heatmap for ALL measures
            suggestions.add(new ChartSuggestion(
                "CORRELATION_MATRIX", null, null, null, "View all correlations"
            ));
        }
    }

    // Determine the main measure to use for the Y-Axis in standard charts
    String mainMeasure = measureCount > 0 ? request.getMeasureColumns().get(0) : null;
    


    // 2. Handle Dimensions (Line, Bar, Pie, etc.)
    if (dimensionCount == 0 && measureCount < 2) {
//        suggestions.add(new ChartSuggestion("KPI_CARD", null, mainMeasure, null, ""));
//        suggestions.add(new ChartSuggestion("GAUGE", null, mainMeasure, null, ""));
        
    } 
    
    if(request.getDateColumn() != null && !request.getDateColumn().isEmpty()) {
    	suggestions.add(new ChartSuggestion("LINE", request.getDateColumn().get("columnName"), mainMeasure, null, "Trend over time"));
        suggestions.add(new ChartSuggestion("AREA", request.getDateColumn().get("columnName"), mainMeasure, null, "Volume over time"));
    }
    
    if (dimensionCount == 1) {
        String dimName = request.getDimensions().get(0);

         suggestions.add(new ChartSuggestion("BAR", dimName, mainMeasure, null, "Categorical comparison"));
         suggestions.add(new ChartSuggestion("PIE", dimName, mainMeasure, null, "Part-to-whole relationship"));
         suggestions.add(new ChartSuggestion("DONUT", dimName, mainMeasure, null, ""));
        
        
        if (measureCount > 1) {
            suggestions.add(new ChartSuggestion(
                "GROUPED_BAR", dimName, null, null, "Compare multiple measures across stores"
            ));
            suggestions.add(new ChartSuggestion(
                "STACKED_BAR", dimName, null, null, "Total volume of measures per store"
            ));
        }
        
    }else if (dimensionCount >= 2) {
        String primaryDim = request.getDimensions().get(0);
        suggestions.add(new ChartSuggestion("STACKED_BAR", primaryDim, mainMeasure, null, ""));
        suggestions.add(new ChartSuggestion("GROUPED_BAR", primaryDim, mainMeasure, null, ""));
        
    } 
    
    if(dimensionCount == 2 && measureCount == 1) {
    	String primaryDim = request.getDimensions().get(0);
    	suggestions.add(new ChartSuggestion("HEATMAP", primaryDim, mainMeasure, null, ""));
    } 

    return suggestions;
}

}
