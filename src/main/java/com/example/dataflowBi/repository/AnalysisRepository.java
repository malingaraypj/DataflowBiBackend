package com.example.dataflowBi.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.dataflowBi.DTO.AnalysisRequest;
import com.example.dataflowBi.DTO.FilterRequest;

@Repository
public class AnalysisRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> fetchAnalysedData(AnalysisRequest request) {
        StringBuilder sql = new StringBuilder("SELECT ");
        List<Object> queryArgs = new ArrayList<>();

        // Handle Dimensions
        if (request.getDimensions() != null && !request.getDimensions().isEmpty()) {
            for (String dim : request.getDimensions()) {
                sql.append(dim).append(", ");
            }
        }
        


        // Handle Multiple Measures (Crucial for Scatter Plots)
        List<String> measures = request.getMeasureColumns();
        if (measures != null && !measures.isEmpty()) {
            for (int i = 0; i < measures.size(); i++) {
                String measure = measures.get(i);
                sql.append(request.getAggregationType())
                   .append("(")
                   .append(measure)
                   .append(")");

                if (measures.size() == 1) {
                    sql.append(" as value ");
                } else {
                    sql.append(" as ").append(measure).append(" ");
                }

                if (i < measures.size() - 1) {
                    sql.append(", ");
                }
            }
        }

        //  Add FROM Table
        sql.append(" FROM ").append(request.getTableName());
   
        // Add GROUP BY
        if (request.getDimensions() != null && !request.getDimensions().isEmpty()) {
            sql.append(" GROUP BY ").append(String.join(", ", request.getDimensions()));
        }

        return jdbcTemplate.queryForList(sql.toString(), queryArgs.toArray());
    }
    
    public List<Map<String, Object>> fetchAnalysedDataByDate(AnalysisRequest request) {
        StringBuilder sql = new StringBuilder("SELECT ");
        List<Object> queryArgs = new ArrayList<>();
        Map<String, String> dateInfo = request.getDateColumn();
        
        String colName = dateInfo.get("columnName").trim();
        String granularity = dateInfo.get("by").trim().toUpperCase();

        String dateExpr = getDateSqlExpression(colName, granularity);
        
        sql.append(dateExpr).append(" AS date_period");

        // 3. Handle Measures
        List<String> measures = request.getMeasureColumns();
        if (measures != null && !measures.isEmpty()) {
            sql.append(", "); 
            for (int i = 0; i < measures.size(); i++) {
                String measure = measures.get(i);
                sql.append(request.getAggregationType())
                   .append("(")
                   .append(measure)
                   .append(")");

                // Alias handling
                if (measures.size() == 1) {
                    sql.append(" AS value");
                } else {
                    sql.append(" AS ").append(measure);
                }

                if (i < measures.size() - 1) {
                    sql.append(", ");
                }
            }
        }

        // 4. FROM and GROUP BY
        sql.append(" FROM ").append(request.getTableName());
        sql.append(" GROUP BY ").append(dateExpr); 
        sql.append(" ORDER BY ").append(dateExpr).append(" ASC");
        

        return jdbcTemplate.queryForList(sql.toString(), queryArgs.toArray());
    }


    private String getDateSqlExpression(String column, String granularity) {
        switch (granularity) {
            case "YEAR":
                return "YEAR(" + column + ")";
            case "MONTH":
                return "DATE_FORMAT(" + column + ", '%Y-%m')";
            case "DAY":
                return "DATE(" + column + ")";
            default:
                return column;
        }
    }

    
}



// 4. Add WHERE (Filters)
//if (request.getFilters() != null && !request.getFilters().isEmpty()) {
//  sql.append(" WHERE ");
//  for (int i = 0; i < request.getFilters().size(); i++) {
//      FilterRequest filter = request.getFilters().get(i);
//      if (i > 0) sql.append(" AND ");
//
//      String operator = filter.getOperator().toUpperCase();
//      List<Object> values = filter.getValues();
//      sql.append(filter.getColumnName());
//
//      switch (operator) {
//          case "EQUALS": 
//          	sql.append(" = ? "); 
//          	queryArgs.add(values.get(0)); 
//          	break;
//          case "CONTAINS": 
//          	sql.append(" LIKE ?"); 
//          	queryArgs.add("%" + values.get(0) + "%");
//          	break;
//          case "BETWEEN": 
//          	sql.append(" BETWEEN ? AND ?");
//          	queryArgs.add(values.get(0)); 
//          	queryArgs.add(values.get(1));
//          	break;
//          case "GREATER_THAN": 
//          	sql.append(" > ? "); 
//          	queryArgs.add(values.get(0));
//          	break;
//          case "LESS_THAN": 
//          	sql.append(" < ? "); 
//          	queryArgs.add(values.get(0)); 
//          	break;
//          case "NOT_EQUALS": 
//          	sql.append(" != ? ");
//          	queryArgs.add(values.get(0));
//          	break;
//          default:
//          	sql.append(" = ? "); 
//          	queryArgs.add(values.get(0));
//      }
//  }
//}

