package com.softure.logisticpymes.controller;

import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.shared.domain.SharedApiErrorResponse;
import com.shared.domain.ServerException;
import com.softure.java.dto.exception.FlexException;

@ControllerAdvice
public class APIControllerErrorAdvice {

	
    @ExceptionHandler({ServerException.class})
    public ResponseEntity<SharedApiErrorResponse> handle(ServerException e) {
    	 SharedApiErrorResponse response =new SharedApiErrorResponse.ApiErrorResponseBuilder()
    		        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    		        .withError_code(HttpStatus.INTERNAL_SERVER_ERROR.name())
    		        .withMessage(e.getTextMessage())
    		        .withDetail(e.getOrigen())
    		        .build();
    		        return new ResponseEntity<SharedApiErrorResponse>(response, response.getStatus());
	}
    
    @ExceptionHandler({FlexException.class})
    public ResponseEntity<SharedApiErrorResponse> handle(FlexException e) {
    	 SharedApiErrorResponse response =new SharedApiErrorResponse.ApiErrorResponseBuilder()
    		        .withStatus(HttpStatus.OK)
    		        .withError_code(HttpStatus.INTERNAL_SERVER_ERROR.name())
    		        .withMessage(e.getLocalizedMessage()).build();

    		        return new ResponseEntity<SharedApiErrorResponse>(response, response.getStatus());
	}
    
	@ExceptionHandler(PSQLException.class)
	protected ResponseEntity<SharedApiErrorResponse> handleCustomAPIException(PSQLException e) {
	   SharedApiErrorResponse response =new SharedApiErrorResponse.ApiErrorResponseBuilder()
	         .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	         .withError_code(HttpStatus.INTERNAL_SERVER_ERROR.name())
	         .withMessage("PSQLException")
	         .withDetail(e.getMessage())
	         .build();
	        return new ResponseEntity<>(response, response.getStatus());
	 }
	
	@ExceptionHandler(BadSqlGrammarException.class)
	protected ResponseEntity<SharedApiErrorResponse> handleCustomAPIException(BadSqlGrammarException e) {
	   SharedApiErrorResponse response =new SharedApiErrorResponse.ApiErrorResponseBuilder()
	         .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	         .withError_code(HttpStatus.INTERNAL_SERVER_ERROR.name())
	         .withMessage("BadSqlGrammarException")
	         .withDetail(e.getCause().getMessage())
	         .build();
	        return new ResponseEntity<>(response, response.getStatus());
	 }
	
	@ExceptionHandler(NullPointerException.class)
	protected ResponseEntity<SharedApiErrorResponse> handleCustomAPIException(NullPointerException e) {
	   SharedApiErrorResponse response =new SharedApiErrorResponse.ApiErrorResponseBuilder()
	         .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	         .withError_code(HttpStatus.INTERNAL_SERVER_ERROR.name())
	         .withMessage("NullPointerException")
	         .build();
	        return new ResponseEntity<>(response, response.getStatus());
	 }
	
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<SharedApiErrorResponse> handleCustomAPIException(Exception e) {
	   SharedApiErrorResponse response =new SharedApiErrorResponse.ApiErrorResponseBuilder()
	         .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	         .withError_code(HttpStatus.INTERNAL_SERVER_ERROR.name())
	         .withMessage(e.getMessage())
	         .withDetail(e.getStackTrace()[0].toString())
	         .build();
	        return new ResponseEntity<>(response, response.getStatus());
	 }
}
