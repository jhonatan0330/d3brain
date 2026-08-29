package d3.accounting.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
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
