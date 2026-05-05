package com.example.dataflowBi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KpiResult {
    private String columnName;
    private String function;
    private Object value;
}