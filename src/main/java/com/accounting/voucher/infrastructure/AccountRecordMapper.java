package com.accounting.voucher.infrastructure;

import java.util.List;

import com.accounting.AccountingConnMapper;
import com.accounting.voucher.domain.AccountRecordDTO;
import com.accounting.voucher.domain.AccountRecordFilterDTO;

@AccountingConnMapper("AccountRecordAccountingMapper")
public interface AccountRecordMapper {

	AccountRecordDTO insert(AccountRecordDTO dto);

	AccountRecordDTO update(AccountRecordDTO dto);

	int count(AccountRecordFilterDTO filter);
	
	AccountRecordDTO getOne(AccountRecordFilterDTO filter);

	List<AccountRecordDTO> getMany(AccountRecordFilterDTO filter);

}