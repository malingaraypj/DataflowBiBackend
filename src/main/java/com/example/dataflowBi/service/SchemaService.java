package com.example.dataflowBi.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dataflowBi.model.ColumnMetadata;
import com.example.dataflowBi.model.TableMetadata;

@Service
public class SchemaService {

    @Autowired
    private DataSource dataSource;

    public List<TableMetadata> getSchema() throws SQLException {
        List<TableMetadata> tables = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            // 1. Get all tables (Filter by 'TABLE' to ignore system views)
            ResultSet tableRes = metaData.getTables(connection.getCatalog(), null, "%", new String[]{"TABLE"});

            while (tableRes.next()) {
                String tableName = tableRes.getString("TABLE_NAME");
                List<ColumnMetadata> columns = new ArrayList<>();

                // 2. Get all columns for this table
                ResultSet columnRes = metaData.getColumns(connection.getCatalog(), null, tableName, "%");
                while (columnRes.next()) {
                    String colName = columnRes.getString("COLUMN_NAME");
                    String typeName = columnRes.getString("TYPE_NAME");
                    
                    columns.add(new ColumnMetadata(colName, typeName, mapToLogicalType(typeName)));
                }
                tables.add(new TableMetadata(tableName, columns));
            }
        }
        return tables;
    }

    // Basic logic to help the frontend decide on chart types later
    private String mapToLogicalType(String typeName) {
        String type = typeName.toUpperCase();
        if (type.contains("CHAR") || type.contains("TEXT")) return "STRING";
        if (type.contains("INT") || type.contains("DECIMAL") || type.contains("DOUBLE")) return "NUMBER";
        if (type.contains("DATE") || type.contains("TIME")) return "DATE";
        return "OTHER";
    }
}
