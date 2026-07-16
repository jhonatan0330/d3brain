package com.accounting.voucher.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class VoucherLine {

	private AccountRecordDTO line;
	private List<AccountRecordAuxiliarDTO> references;

	public AccountRecordDTO getLine() {
		return line;
	}

	public void setLine(AccountRecordDTO line) {
		this.line = line;
	}

	public List<AccountRecordAuxiliarDTO> getReferences() {
		return references;
	}

	public void setReferences(List<AccountRecordAuxiliarDTO> references) {
		this.references = references;
	}

}
