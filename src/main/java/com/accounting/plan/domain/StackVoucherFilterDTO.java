package com.accounting.plan.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.shared.domain.SharedDataObjectFilter;

@Alias("StackVoucherFilterDTO")
public class StackVoucherFilterDTO extends SharedDataObjectFilter {

	private String voucher;
	private Date creationDateMin;
	private Date creationDateMax;
	private String action;

	public String getVoucher() {
		return voucher;
	}

	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}

	public Date getCreationDateMin() {
		return creationDateMin;
	}

	public void setCreationDateMin(Date creationDateMin) {
		this.creationDateMin = creationDateMin;
	}

	public Date getCreationDateMax() {
		return creationDateMax;
	}

	public void setCreationDateMax(Date creationDateMax) {
		this.creationDateMax = creationDateMax;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}