package com.accounting.plan.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accounting.plan.application.base.AccountService;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.AccountFilterDTO;
import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;

@Service("PlanGetAccountAccountingService")
public class PlanGetAccountService {

	@Autowired
	private AccountService accountService;
	
	public List<AccountDTO> getActive(String catalogId, String filterText) throws ServerException{
		AccountFilterDTO filter = new AccountFilterDTO();
		filter.setCatalog(catalogId);
		filter.setFilter(filterText);
		filter.setState(SharedConstants.STATE_ACTIVE);
		filter.setEndRow(3000);
		return accountService.getMany(filter);
	}

	public AccountDTO getById(String catalogId, String id) throws ServerException {
		AccountFilterDTO filter = new AccountFilterDTO();
		filter.setCatalog(catalogId);
		filter.setKey(id);
		return accountService.getOne(filter);
	}
}
