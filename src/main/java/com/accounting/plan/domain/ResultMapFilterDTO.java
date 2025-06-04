package com.accounting.plan.domain;

import java.math.BigDecimal;

import org.apache.ibatis.type.Alias;

import com.shared.domain.SharedDataObjectFilter;

@Alias("ResultMapFilterDTO")
public class ResultMapFilterDTO extends SharedDataObjectFilter {

	private String account;
	private String accountName;
	private String accountCode;
	private String timeFrame;
	private String timeFrameName;
	private Integer quantity;
	private BigDecimal lastBalance;
	private BigDecimal nextBalance;
	private BigDecimal positive;
	private BigDecimal negative;
	private BigDecimal value;

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

	public String getTimeFrame() {
		return timeFrame;
	}

	public void setTimeFrame(String timeFrame) {
		this.timeFrame = timeFrame;
	}

	public String getTimeFrameName() {
		return timeFrameName;
	}

	public void setTimeFrameName(String timeFrameName) {
		this.timeFrameName = timeFrameName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getLastBalance() {
		return lastBalance;
	}

	public void setLastBalance(BigDecimal lastBalance) {
		this.lastBalance = lastBalance;
	}

	public BigDecimal getNextBalance() {
		return nextBalance;
	}

	public void setNextBalance(BigDecimal nextBalance) {
		this.nextBalance = nextBalance;
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

}