package com.accounting.plan.infrastructure;

import java.util.List;

import com.accounting.AccountingConnMapper;
import com.accounting.plan.domain.FormatVoucherDTO;
import com.accounting.plan.domain.FormatVoucherFilterDTO;

@AccountingConnMapper("FormatVoucherAccountingMapper")
public interface FormatVoucherMapper {

	FormatVoucherDTO insert(FormatVoucherDTO dto);

	FormatVoucherDTO update(FormatVoucherDTO dto);

	int count(FormatVoucherFilterDTO filter);
	
	FormatVoucherDTO getOne(FormatVoucherFilterDTO filter);

	List<FormatVoucherDTO> getMany(FormatVoucherFilterDTO filter);

}