package com.accounting.plan.domain;


import org.apache.ibatis.type.Alias;

import com.shared.domain.SharedDataObjectFilter;

@Alias("StackVoucherFilterDTO")
public class StackVoucherFilterDTO extends SharedDataObjectFilter {

	private String voucher;

	public String getVoucher() {
		return voucher;
	}

	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}

}