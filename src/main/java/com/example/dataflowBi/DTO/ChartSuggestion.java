package com.example.dataflowBi.DTO;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChartSuggestion {
    private String chartType;
    private String xAxisColumn;
    private String yAxisColumn;
    private String sizeColumn; // Specifically for Bubble Charts
    private String insightMessage; // E.g., "Strongest correlation found"
}
