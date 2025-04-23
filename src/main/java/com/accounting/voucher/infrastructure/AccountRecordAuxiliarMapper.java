package com.accounting.voucher.infrastructure;

import java.util.List;

import com.accounting.voucher.domain.AccountRecordAuxiliarDTO;
import com.accounting.voucher.domain.AccountRecordAuxiliarFilterDTO;
import com.softure.SoftureSqlConnMapper;

@SoftureSqlConnMapper(value = "AccountRecordAuxiliarAccountingMapper")
public interface AccountRecordAuxiliarMapper {

	AccountRecordAuxiliarDTO insert(AccountRecordAuxiliarDTO dto);

	AccountRecordAuxiliarDTO update(AccountRecordAuxiliarDTO dto);

	int count(AccountRecordAuxiliarFilterDTO filter);
	
	AccountRecordAuxiliarDTO getOne(AccountRecordAuxiliarFilterDTO filter);

	List<AccountRecordAuxiliarDTO> getMany(AccountRecordAuxiliarFilterDTO filter);

}