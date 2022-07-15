package com.softure.logisticpymes.controller;

import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.softure.java.dto.exception.ApiErrorResponse;
import com.softure.java.dto.exception.FlexException;
import com.softure.java.dto.exception.ServerException;

@ControllerAdvice
public class APIControllerErrorAdvice {

	
    @ExceptionHandler({ServerException.class})
    public ResponseEntity<ApiErrorResponse> handle(ServerException e) {
    	 ApiErrorResponse response =new ApiErrorResponse.ApiErrorResponseBuilder()
    		        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    		        .withError_code(HttpStatus.INTERNAL_SERVER_ERROR.name())
    		        .withMessage(e.getLocalizedMessage())
    		        .withDetail(e.getOrigen())
    		        .build();
    		        return new ResponseEntity<ApiErrorResponse>(response, response.getStatus());
	}
    
    @ExceptionHandler({FlexException.class})
    public ResponseEntity<ApiErrorResponse> handle(FlexException e) {
    	 ApiErrorResponse response =new ApiErrorResponse.ApiErrorResponseBuilder()
    		        .withStatus(HttpStatus.OK)
    		        .withError_code(HttpStatus.INTERNAL_SERVER_ERROR.name())
    		        .withMessage(e.getLocalizedMessage()).build();

    		        return new ResponseEntity<ApiErrorResponse>(response, response.getStatus());
	}
    
	@ExceptionHandler(PSQLException.class)
	protected ResponseEntity<ApiErrorResponse> handleCustomAPIException(PSQLException e) {
	   ApiErrorResponse response =new ApiErrorResponse.ApiErrorResponseBuilder()
	         .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	         .withError_code(HttpStatus.INTERNAL_SERVER_ERROR.name())
	         .withMessage("PSQLException")
	         .withDetail(e.getMessage())
	         .build();
	        return new ResponseEntity<>(response, response.getStatus());
	 }
	
	@ExceptionHandler(BadSqlGrammarException.class)
	protected ResponseEntity<ApiErrorResponse> handleCustomAPIException(BadSqlGrammarException e) {
	   ApiErrorResponse response =new ApiErrorResponse.ApiErrorResponseBuilder()
	         .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	         .withError_code(HttpStatus.INTERNAL_SERVER_ERROR.name())
	         .withMessage("BadSqlGrammarException")
	         .withDetail(e.getCause().getMessage())
	         .build();
	        return new ResponseEntity<>(response, response.getStatus());
	 }
	
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ApiErrorResponse> handleCustomAPIException(Exception e) {
	   ApiErrorResponse response =new ApiErrorResponse.ApiErrorResponseBuilder()
	         .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	         .withError_code(HttpStatus.INTERNAL_SERVER_ERROR.name())
	         .withMessage(e.getMessage())
	         .withDetail(e.getStackTrace()[0].toString())
	         .build();
	        return new ResponseEntity<>(response, response.getStatus());
	 }
}
