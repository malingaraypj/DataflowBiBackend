package com.example.dataflowBi.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.dataflowBi.DTO.KpiRequest;

@Repository
public class KpiRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public BigDecimal getValueForPeriod(KpiRequest request, LocalDateTime start, LocalDateTime end) {
        String sql = String.format(
            "SELECT %s(%s) FROM %s WHERE %s >= ? AND %s <= ?",
            request.getAggregationType(),
            request.getMeasureColumn(),
            request.getTableName(),
            request.getDateColumn(),
            request.getDateColumn()
        );

        // Using PreparedStatement parameters (?) for the date values for security
        Double result = jdbcTemplate.queryForObject(sql, Double.class, start, end);
        return result != null ? BigDecimal.valueOf(result) : BigDecimal.ZERO;
	}
}
