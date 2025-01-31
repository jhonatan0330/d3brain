package com.accounting.api.domain;

import java.math.BigDecimal;

public class VoucherLineRequest {
	
	private String account;
	private BigDecimal debit;
	private BigDecimal credit;
	private String note;
	
	private String accountParent;
	private String accountName;
	private String accountDocument;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public BigDecimal getDebit() {
		return debit;
	}
	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}
	public BigDecimal getCredit() {
		return credit;
	}
	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	public String getAccountParent() {
		return accountParent;
	}
	public void setAccountParent(String accountParent) {
		this.accountParent = accountParent;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountDocument() {
		return accountDocument;
	}
	public void setAccountDocument(String accountDocument) {
		this.accountDocument = accountDocument;
	}
	
}
