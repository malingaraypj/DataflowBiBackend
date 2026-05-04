package com.example.dataflowBi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dataflowBi.DTO.AnalysisRequest;
import com.example.dataflowBi.DTO.AnalysisResponse;
import com.example.dataflowBi.DTO.ChartMetaData;
import com.example.dataflowBi.repository.AnalysisRepository;

@Service
public class AnalysisService {

    @Autowired
    AnalysisRepository analysisRepository;
    
    public AnalysisResponse executeAnalysis(AnalysisRequest request) {
    
    
    List<Map<String, Object>> rawData = analysisRepository.fetchAnalysedData(request);
    List<String> suggestedCharts = suggestCharts(request);
    
    ChartMetaData metadata = generateMetadata(request);

    return new AnalysisResponse(rawData, suggestedCharts,metadata);
}


  private ChartMetaData generateMetadata(AnalysisRequest request) {
	  	List<String> dims = request.getDimensions();
	  	List<String> measures = request.getMeasureColumns();
	  	
		String xAxisKey = null;
		String yAxisKey = null;
		List<String> seriesKeys = new ArrayList<>();
		List<String> facetKeys = new ArrayList<>();
		
		int dimCount = (dims != null) ? dims.size(): 0;
		int measureCount = (measures != null) ? measures.size(): 0;
		
		// Scatter plot configuration (2 or more measure columns)
		if(measureCount>=2) {
			xAxisKey = measures.get(0);
			yAxisKey = measures.get(1);
			
			if(dimCount >0) {
				seriesKeys.add(dims.get(0));
			}
			if(dimCount>1) {
				facetKeys.addAll(dims.subList(1, dimCount));
			}
		}else {
			yAxisKey = (measureCount ==1) ? measures.get(0) : "value";
			
			if(dimCount>0) {
				xAxisKey = dims.get(0);
			}
			if(dimCount>=2) {
				seriesKeys.add(dims.get(1));
			}
			if(dimCount>2) {
				facetKeys.addAll(dims.subList(2, dimCount));
			}
		}
		
		
		return new ChartMetaData(xAxisKey, seriesKeys,facetKeys, yAxisKey);
	}


   private List<String> suggestCharts(AnalysisRequest request) {
        List<String> suggestions = new ArrayList<>();
        int dimensionCount = request.getDimensions() == null ? 0 : request.getDimensions().size();
        int measureCount = request.getMeasureColumns() == null ? 0 : request.getMeasureColumns().size();

        // Check if a Scatter Plot is possible
        if (measureCount >= 2) {
            suggestions.add("SCATTER_PLOT");
            
            if (measureCount >= 3) {
                suggestions.add("BUBBLE_CHART"); 
            }
            return suggestions; 
        }

        
        if (dimensionCount == 0) {
            suggestions.add("KPI_CARD");
            suggestions.add("GAUGE");
        } else if (dimensionCount == 1) {
            String dimName = request.getDimensions().get(0).toLowerCase();
            if (dimName.contains("date") || dimName.contains("time") || dimName.contains("month") || dimName.contains("year")) {
                suggestions.add("LINE");
                suggestions.add("AREA");
                suggestions.add("BAR");
            } else {
                suggestions.add("BAR");
                suggestions.add("PIE");
                suggestions.add("DONUT");
            }
        } else if (dimensionCount >= 2) {
            suggestions.add("STACKED_BAR");
            suggestions.add("GROUPED_BAR");
            suggestions.add("HEATMAP");
        }

        return suggestions;
    }
}
