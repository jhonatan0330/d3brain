package com.accounting.voucher.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.shared.domain.SharedDataObjectFilter;

@Alias("AccountRecordFilterDTO")
public class AccountRecordFilterDTO extends SharedDataObjectFilter {

	private String catalogCode;
	private String voucher;
	private String account;
	private String accountName;
	private String accountCode;
	private String note;
	private Date factDateMin;
	private Date factDateMax;
	private BigDecimal positive;
	private BigDecimal negative;
	private BigDecimal value;
	private String mainDocument;
	private String accountLink;
	private String type;

	public String getCatalogCode() {
		return catalogCode;
	}

	public void setCatalogCode(String catalogCode) {
		this.catalogCode = catalogCode;
	}

	public String getVoucher() {
		return voucher;
	}

	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public String getMainDocument() {
		return mainDocument;
	}

	public void setMainDocument(String mainDocument) {
		this.mainDocument = mainDocument;
	}

	public String getAccountLink() {
		return accountLink;
	}

	public void setAccountLink(String accountLink) {
		this.accountLink = accountLink;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}