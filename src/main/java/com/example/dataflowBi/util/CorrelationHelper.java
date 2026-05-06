package com.example.dataflowBi.util;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

public class CorrelationHelper {
    @Data
    @AllArgsConstructor
    public static class TopCorrelation {
        private String col1;
        private String col2;
        private double score;
    }

    public static TopCorrelation findStrongestCorrelation(List<String> measures, Map<String, double[]> dataMap) {
    	 if (dataMap == null || dataMap.size() < 2) {
    	        return new TopCorrelation(null, null, 0.0); 
    	    }
    	 
        PearsonsCorrelation pearson = new PearsonsCorrelation();
        TopCorrelation bestPair = new TopCorrelation(measures.get(0), measures.get(1), 0.0);
        double maxCorrelation = -1.0;

        // Iterate through all pairs to find the highest absolute correlation
        for (int i = 0; i < measures.size(); i++) {
            for (int j = i + 1; j < measures.size(); j++) {
                String m1 = measures.get(i);
                String m2 = measures.get(j);
                
                double[] array1 = dataMap.get(m1);
                double[] array2 = dataMap.get(m2);
                
                // Calculate correlation (-1.0 to 1.0)
                double correlation = Math.abs(pearson.correlation(array1, array2));
                
                if (correlation > maxCorrelation) {
                    maxCorrelation = correlation;
                    bestPair = new TopCorrelation(m1, m2, correlation);
                }
            }
        }
        return bestPair;
    }
}
