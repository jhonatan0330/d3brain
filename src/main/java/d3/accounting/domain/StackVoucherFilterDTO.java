package d3.accounting.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import d3.shared.domain.SharedDataObjectFilter;

@Alias("StackVoucherFilterDTO")
public class StackVoucherFilterDTO extends SharedDataObjectFilter {

	private String voucher;
	private Date creationDateMin;
	private Date creationDateMax;
	private Date executionDateMin;
	private Date executionDateMax;
	private Date finishDateMin;
	private Date finishDateMax;
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

	public Date getExecutionDateMin() {
		return executionDateMin;
	}

	public void setExecutionDateMin(Date executionDateMin) {
		this.executionDateMin = executionDateMin;
	}

	public Date getExecutionDateMax() {
		return executionDateMax;
	}

	public void setExecutionDateMax(Date executionDateMax) {
		this.executionDateMax = executionDateMax;
	}

	public Date getFinishDateMin() {
		return finishDateMin;
	}

	public void setFinishDateMin(Date finishDateMin) {
		this.finishDateMin = finishDateMin;
	}

	public Date getFinishDateMax() {
		return finishDateMax;
	}

	public void setFinishDateMax(Date finishDateMax) {
		this.finishDateMax = finishDateMax;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}