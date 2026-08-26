package d3.accounting_api.domain;

import java.math.BigDecimal;
import java.util.ArrayList;

public class VoucherLineRequest {

	private String account;
	private BigDecimal debit;
	private BigDecimal credit;
	private String note;
	private String type;
	private String accountLink;
	private String mainDocument;
	private ArrayList<VoucherLineDimensionRequest> references;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public BigDecimal getDebit() {
		return debit;
	}

	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<VoucherLineDimensionRequest> getReferences() {
		return references;
	}

	public void setReferences(ArrayList<VoucherLineDimensionRequest> references) {
		this.references = references;
	}

	public String getMainDocument() {
		return mainDocument;
	}

	public void setDocumentMain(String mainDocument) {
		this.mainDocument = mainDocument;
	}

	public String getAccountLink() {
		return accountLink;
	}

	public void setAccountLink(String accountLink) {
		this.accountLink = accountLink;
	}

}
