package com.accounting.plan.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accounting.plan.application.base.AccountService;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.AccountFilterDTO;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;

@Service("PlanGetAccountAccountingService")
public class PlanGetAccountService {

	@Autowired
	private AccountService accountService;
	
	public List<AccountDTO> getActive(String catalogId) throws ServerException{
		AccountFilterDTO filter = new AccountFilterDTO();
		filter.setCatalog(catalogId);
		filter.setState(ConstantesGenerales.ESTADO_ACTIVO);
		return accountService.getMany(filter);
	}

	public AccountDTO getById(String catalogId, String id) throws ServerException {
		AccountFilterDTO filter = new AccountFilterDTO();
		filter.setCatalog(catalogId);
		filter.setKey(id);
		return accountService.getOne(filter);
	}
}
