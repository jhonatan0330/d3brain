package com.accounting.voucher.infrastructure;

import java.util.List;

import com.accounting.AccountingConnMapper;
import com.accounting.voucher.domain.VoucherDTO;
import com.accounting.voucher.domain.VoucherFilterDTO;

@AccountingConnMapper("VoucherAccountingMapper")
public interface VoucherMapper {

	VoucherDTO insert(VoucherDTO dto);

	VoucherDTO update(VoucherDTO dto);

	int count(VoucherFilterDTO filter);
	
	VoucherDTO getOne(VoucherFilterDTO filter);

	List<VoucherDTO> getMany(VoucherFilterDTO filter);

}