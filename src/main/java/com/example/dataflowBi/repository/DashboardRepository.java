package com.example.dataflowBi.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Map<String, Object>> findTableRows(String tableName,int count){
		String sql = "SELECT * FROM "+tableName+" LIMIT "+count;
		
		return jdbcTemplate.queryForList(sql);
	}
}
