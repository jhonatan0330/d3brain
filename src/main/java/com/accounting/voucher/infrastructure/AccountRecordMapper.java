package com.accounting.voucher.infrastructure;

import java.util.List;

import com.accounting.voucher.domain.AccountRecordDTO;
import com.accounting.voucher.domain.AccountRecordFilterDTO;
import com.softure.SoftureSqlConnMapper;

@SoftureSqlConnMapper(value = "AccountRecordAccountingMapper")
public interface AccountRecordMapper {

	AccountRecordDTO insert(AccountRecordDTO dto);

	AccountRecordDTO update(AccountRecordDTO dto);

	int count(AccountRecordFilterDTO filter);
	
	AccountRecordDTO getOne(AccountRecordFilterDTO filter);

	List<AccountRecordDTO> getMany(AccountRecordFilterDTO filter);

}