package com.example.dataflowBi.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.dataflowBi.DTO.KpiRequest;
import com.example.dataflowBi.DTO.KpiResponse;
import com.example.dataflowBi.repository.KpiRepository;

@Service
public class KpiService {

    private final KpiRepository kpiRepository;

    public KpiService(KpiRepository kpiRepository) {
        this.kpiRepository = kpiRepository;
    }

    public KpiResponse calculateKpi(KpiRequest request) {
        // 1. Determine Time Ranges (Example for "THIS_MONTH")
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime currentStart = now.withDayOfMonth(1).withHour(0).withMinute(0);
        LocalDateTime currentEnd = now;

        LocalDateTime previousStart = currentStart.minusMonths(1);
        LocalDateTime previousEnd = currentStart.minusSeconds(1);

        // 2. Fetch Data
        BigDecimal currentVal = kpiRepository.getValueForPeriod(request, currentStart, currentEnd);
        BigDecimal previousVal = kpiRepository.getValueForPeriod(request, previousStart, previousEnd);

        // 3. Calculate Change & Trend
        Double percentChange = calculatePercentageChange(currentVal, previousVal);
        String trend = determineTrend(percentChange);

        return new KpiResponse(
            request.getMeasureColumn() + " Status", 
            currentVal, 
            percentChange, 
            trend
        );
    }

    private Double calculatePercentageChange(BigDecimal current, BigDecimal previous) {
        if (previous.compareTo(BigDecimal.ZERO) == 0) {
            return current.compareTo(BigDecimal.ZERO) > 0 ? 100.0 : 0.0;
        }
        
        BigDecimal difference = current.subtract(previous);
        return difference
                .divide(previous, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }

    private String determineTrend(Double percentChange) {
        if (percentChange > 0) return "UP";
        if (percentChange < 0) return "DOWN";
        return "FLAT";
    }
}
