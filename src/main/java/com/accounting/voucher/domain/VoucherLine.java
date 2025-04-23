package com.accounting.voucher.domain;

import java.util.List;

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
