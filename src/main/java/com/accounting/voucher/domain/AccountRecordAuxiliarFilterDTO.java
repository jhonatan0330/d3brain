package com.accounting.voucher.domain;


import org.apache.ibatis.type.Alias;

import com.shared.domain.SharedDataObjectFilter;

@Alias("AccountRecordAuxiliarFilterDTO")
public class AccountRecordAuxiliarFilterDTO extends SharedDataObjectFilter {

	private String voucher;
	private String recordLine;
	private String account;
	private String auxiliarType;
	private String auxiliarDocumentId;
	private String auxiliarCode;
	private String auxiliarName;

	public String getVoucher() {
		return voucher;
	}

	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}

	public String getRecordLine() {
		return recordLine;
	}

	public void setRecordLine(String recordLine) {
		this.recordLine = recordLine;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAuxiliarType() {
		return auxiliarType;
	}

	public void setAuxiliarType(String auxiliarType) {
		this.auxiliarType = auxiliarType;
	}

	public String getAuxiliarDocumentId() {
		return auxiliarDocumentId;
	}

	public void setAuxiliarDocumentId(String auxiliarDocumentId) {
		this.auxiliarDocumentId = auxiliarDocumentId;
	}

	public String getAuxiliarCode() {
		return auxiliarCode;
	}

	public void setAuxiliarCode(String auxiliarCode) {
		this.auxiliarCode = auxiliarCode;
	}

	public String getAuxiliarName() {
		return auxiliarName;
	}

	public void setAuxiliarName(String auxiliarName) {
		this.auxiliarName = auxiliarName;
	}

}