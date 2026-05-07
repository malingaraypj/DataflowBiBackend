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
    // Basic check for map size or insufficient measures
    if (dataMap == null || measures == null || measures.size() < 2) {
        return new TopCorrelation(null, null, 0.0);
    }

    PearsonsCorrelation pearson = new PearsonsCorrelation();
    TopCorrelation bestPair = null;
    double maxCorrelation = -1.0;

    for (int i = 0; i < measures.size(); i++) {
        for (int j = i + 1; j < measures.size(); j++) {
            String m1 = measures.get(i);
            String m2 = measures.get(j);

            double[] array1 = dataMap.get(m1);
            double[] array2 = dataMap.get(m2);

            if (array1 != null && array2 != null && 
                array1.length >= 2 && array2.length >= 2 && 
                array1.length == array2.length) {

                try {
                    double correlation = Math.abs(pearson.correlation(array1, array2));

                    if (!Double.isNaN(correlation) && correlation > maxCorrelation) {
                        maxCorrelation = correlation;
                        bestPair = new TopCorrelation(m1, m2, correlation);
                    }
                } catch (Exception e) {
                    // Log or handle unexpected calculation errors
                	System.out.println(e);
                }
            }
        }
    }

    // Return bestPair or a default if no valid correlations were found
    return bestPair != null ? bestPair : new TopCorrelation(null, null, 0.0);
}

}
