package com.accounting.voucher.domain;

import java.util.List;

public class Voucher {

	private VoucherDTO header;
	private List<AccountRecordDTO> records;
	
	public VoucherDTO getHeader() {
		return header;
	}
	public void setHeader(VoucherDTO voucher) {
		this.header = voucher;
	}
	public List<AccountRecordDTO> getRecords() {
		return records;
	}
	public void setRecords(List<AccountRecordDTO> records) {
		this.records = records;
	}
	
}
