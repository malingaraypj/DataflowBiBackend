package com.example.dataflowBi.repository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class KpiRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Object executeKpi(String table, String column, String function) {

        String sql;

        switch (function) {

            case "COUNT":
                sql = "SELECT COUNT(*) AS value FROM " + table;
                break;

            case "COUNT_DISTINCT":
                sql = "SELECT COUNT(DISTINCT `" + column + "`) AS value FROM " + table;
                break;

            case "NULL_COUNT":
                sql = "SELECT COUNT(*) - COUNT(`" + column + "`) AS value FROM " + table;
                break;

            case "NON_NULL_COUNT":
                sql = "SELECT COUNT(`" + column + "`) AS value FROM " + table;
                break;

            case "MOST_FREQUENT":
                sql = "SELECT `" + column + "` AS value " +
                      "FROM " + table +
                      " GROUP BY `" + column + "` " +
                      " ORDER BY COUNT(*) DESC " +
                      " LIMIT 1";
                break;

            case "LEAST_FREQUENT":
                sql = "SELECT `" + column + "` AS value " +
                      "FROM " + table +
                      " GROUP BY `" + column + "` " +
                      " ORDER BY COUNT(*) ASC " +
                      " LIMIT 1";
                break;

            default:
                // SUM, AVG, MIN, MAX
                sql = "SELECT " + function +
                      "(`" + column + "`) AS value FROM " + table;
        }
        
        System.out.println(sql);

        Map<String, Object> result = jdbcTemplate.queryForMap(sql);
        return result.get("value");
    }
}