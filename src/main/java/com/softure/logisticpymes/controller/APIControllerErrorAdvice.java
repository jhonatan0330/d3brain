package com.softure.logisticpymes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    		        .withMessage(e.getLocalizedMessage()).build();

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
    /*
	@ExceptionHandler(ServerException.class)
	protected ResponseEntity<Object> handleCustomAPIException(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
	  
	   ApiErrorResponse response =new ApiErrorResponse.ApiErrorResponseBuilder()
	         .withStatus(status)
	         .withError_code(HttpStatus.NOT_FOUND.name())
	         .withMessage(ex.getLocalizedMessage())
	         .withDetail(ex.getMessage())
	         .build();
	        return new ResponseEntity<>(response, response.getStatus());
	 }*/
}
