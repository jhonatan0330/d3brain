package com.accounting.plan.infrastructure;

import java.util.List;

import com.accounting.AccountingSqlConnMapper;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.AccountFilterDTO;

@AccountingSqlConnMapper("AccountAccountingMapper")
public interface AccountMapper {

	AccountDTO insert(AccountDTO dto);

	AccountDTO update(AccountDTO dto);

	int count(AccountFilterDTO filter);
	
	AccountDTO getOne(AccountFilterDTO filter);

	List<AccountDTO> getMany(AccountFilterDTO filter);

}