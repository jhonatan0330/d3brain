package com.accounting.voucher.domain;

import java.util.List;

public class Voucher {

	private VoucherDTO header;
	private List<VoucherLine> records;
	
	public VoucherDTO getHeader() {
		return header;
	}
	public void setHeader(VoucherDTO voucher) {
		this.header = voucher;
	}
	public List<VoucherLine> getRecords() {
		return records;
	}
	public void setRecords(List<VoucherLine> records) {
		this.records = records;
	}
	
}
