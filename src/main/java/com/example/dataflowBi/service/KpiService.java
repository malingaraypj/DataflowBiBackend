package com.example.dataflowBi.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dataflowBi.DTO.KpiRequest;
import com.example.dataflowBi.DTO.KpiResult;
import com.example.dataflowBi.DTO.KpiResponse;
import com.example.dataflowBi.model.ColumnMetadata;
import com.example.dataflowBi.model.TableMetadata;
import com.example.dataflowBi.repository.KpiRepository;

@Service
public class KpiService {

    @Autowired
    private SchemaService schemaService;

    @Autowired
    private KpiRepository kpiRepository;

    public KpiResponse generateKpis(KpiRequest request) throws RuntimeException, SQLException {

        //  Get table metadata
        TableMetadata table = schemaService.getSchema().stream()
            .filter(t -> t.getTableName().equalsIgnoreCase(request.getTableName()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Invalid table"));

        List<KpiResult> results = new ArrayList<>();

        //  Loop through selected columns
        for (ColumnMetadata column : table.getColumns()) {

            if (!request.getSelectedColumns().contains(column.getColumnName())) {
                continue;
            }

            //  Decide allowed functions
            List<String> functions = getFunctionsForType(column.getLogicalType());

            //  Execute each KPI
            for (String function : functions) {

                Object value = kpiRepository.executeKpi(
                        request.getTableName(),
                        column.getColumnName(),
                        function
                );

                results.add(
                    new KpiResult(column.getColumnName(), function, value)
                );
            }
        }

        return new KpiResponse(results);
    }

    //  KPI FUNCTION DECISION
    private List<String> getFunctionsForType(String logicalType) {

        List<String> functions = new ArrayList<>();
        
        System.out.println(logicalType);

        switch (logicalType) {

            case "NUMBER":
                functions.add("SUM");
                functions.add("AVG");
                functions.add("MIN");
                functions.add("MAX");
                functions.add("COUNT");
                break;

            case "STRING":
                functions.add("COUNT");
                functions.add("COUNT_DISTINCT");
                functions.add("MOST_FREQUENT");
                functions.add("LEAST_FREQUENT");
                functions.add("NULL_COUNT");
                functions.add("NON_NULL_COUNT");
                break;

            case "DATE":
                functions.add("MIN");
                functions.add("MAX");
                functions.add("COUNT");
                break;

            default:
                functions.add("COUNT");
        }

        return functions;
    }
}
