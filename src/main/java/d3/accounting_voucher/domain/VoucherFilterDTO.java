package d3.accounting_voucher.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import d3.shared.domain.SharedDataObjectFilter;

@Alias("VoucherFilterDTO")
public class VoucherFilterDTO extends SharedDataObjectFilter {

	private String catalog;
	private String catalogCode;
	private String code;
	private String type;
	private String concept;
	private Date factDateMin;
	private Date factDateMax;
	private BigDecimal value;
	private String document;
	private String mainDocument;
	private Date deleteDateMin;
	private Date deleteDateMax;
	private Date creationDateMin;
	private Date creationDateMax;

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getCatalogCode() {
		return catalogCode;
	}

	public void setCatalogCode(String catalogCode) {
		this.catalogCode = catalogCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public Date getFactDateMin() {
		return factDateMin;
	}

	public void setFactDateMin(Date factDateMin) {
		this.factDateMin = factDateMin;
	}

	public Date getFactDateMax() {
		return factDateMax;
	}

	public void setFactDateMax(Date factDateMax) {
		this.factDateMax = factDateMax;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getMainDocument() {
		return mainDocument;
	}

	public void setMainDocument(String mainDocument) {
		this.mainDocument = mainDocument;
	}

	public Date getDeleteDateMin() {
		return deleteDateMin;
	}

	public void setDeleteDateMin(Date deleteDateMin) {
		this.deleteDateMin = deleteDateMin;
	}

	public Date getDeleteDateMax() {
		return deleteDateMax;
	}

	public void setDeleteDateMax(Date deleteDateMax) {
		this.deleteDateMax = deleteDateMax;
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

}