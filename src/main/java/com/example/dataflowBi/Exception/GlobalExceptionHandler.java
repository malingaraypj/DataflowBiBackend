package com.example.dataflowBi.Exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(TableNotFoundException.class)
	@ResponseBody
	public ResponseEntity<Object> handleTableNotFoundException(TableNotFoundException exception){
		Map<String, Object> errorBody = new HashMap<>();
		
		errorBody.put("status", 404);
		errorBody.put("message", exception.getMessage());
		
		return new ResponseEntity<>(errorBody, HttpStatus.NOT_FOUND);
	}
}
