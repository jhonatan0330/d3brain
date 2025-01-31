package com.accounting.plan.domain;


import com.shared.domain.SharedDataObject;
import org.apache.ibatis.type.Alias;

@Alias("StackVoucherDTO")
public class StackVoucherDTO extends SharedDataObject{

	private String voucher;

	public String getVoucher() {
		return voucher;
	}

	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}

}