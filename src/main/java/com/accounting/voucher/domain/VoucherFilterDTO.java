package com.accounting.voucher.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("VoucherFilterDTO")
public class VoucherFilterDTO {
	private String key;
	private String state;
	private String filter;
	private Integer indexStart;
	private Integer indexEnd;
	private String catalog;
	private String catalogCode;
	private String code;
	private String concept;
	private Date factDate;
	private Date registerDate;
	private String registerUser;
	private BigDecimal positive;
	private BigDecimal negative;
	private BigDecimal value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public Integer getIndexStart() {
		return indexStart;
	}

	public void setIndexStart(Integer indexStart) {
		this.indexStart = indexStart;
	}

	public Integer getIndexEnd() {
		return indexEnd;
	}

	public void setIndexEnd(Integer indexEnd) {
		this.indexEnd = indexEnd;
	}

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

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public Date getFactDate() {
		return factDate;
	}

	public void setFactDate(Date factDate) {
		this.factDate = factDate;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public String getRegisterUser() {
		return registerUser;
	}

	public void setRegisterUser(String registerUser) {
		this.registerUser = registerUser;
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