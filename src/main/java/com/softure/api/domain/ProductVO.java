package com.softure.api.domain;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ProductVO {
	
	private BigDecimal totalQuantity;
	private BigDecimal totalValue;
	private String code;
	private List<FieldVO> features;
}
