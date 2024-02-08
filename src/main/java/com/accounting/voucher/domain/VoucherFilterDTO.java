package com.accounting.voucher.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.shared.domain.SharedDataObjectFilter;

@Alias("VoucherFilterDTO")
public class VoucherFilterDTO extends SharedDataObjectFilter {

	private String catalog;
	private String catalogCode;
	private String code;
	private String type;
	private String typeName;
	private String concept;
	private Date factDateMin;
	private Date factDateMax;
	private BigDecimal positive;
	private BigDecimal negative;
	private BigDecimal value;

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getCatalogCode() {
		return catalogCode;
	}

	public void setCatalogCode(String catalogCode) {
		this.catalogCode = catalogCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public Date getFactDateMin() {
		return factDateMin;
	}

	public void setFactDateMin(Date factDateMin) {
		this.factDateMin = factDateMin;
	}

	public Date getFactDateMax() {
		return factDateMax;
	}

	public void setFactDateMax(Date factDateMax) {
		this.factDateMax = factDateMax;
	}

	public BigDecimal getPositive() {
		return positive;
	}

	public void setPositive(BigDecimal positive) {
		this.positive = positive;
	}

	public BigDecimal getNegative() {
		return negative;
	}

	public void setNegative(BigDecimal negative) {
		this.negative = negative;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

}