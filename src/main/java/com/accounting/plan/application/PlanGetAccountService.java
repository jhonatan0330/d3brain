package com.accounting.plan.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.accounting.plan.application.base.AccountService;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.AccountFilterDTO;
import com.shared.domain.SharedConstants;
import com.softure.java.services.SoftureUtil;
import com.shared.domain.ServerException;

@Service("PlanGetAccountAccountingService")
public class PlanGetAccountService {

	@Autowired @Lazy 
	private AccountService accountService;
	
	public List<AccountDTO> getActive(String catalogId, String filterText) throws ServerException{
		AccountFilterDTO filter = new AccountFilterDTO();
		filter.setCatalog(catalogId);
		filter.setFilter(filterText);
		filter.setState(SharedConstants.STATE_ACTIVE);
		filter.setEndRow(3000);
		return accountService.getMany(filter);
	}

	public AccountDTO getByCatalogAndId(String catalogId, String id) throws ServerException {
		AccountFilterDTO filter = new AccountFilterDTO();
		filter.setCatalog(catalogId);
		filter.setKey(id);
		return accountService.getOne(filter);
	}
	
	public AccountDTO findAccountByCode(String catalogId, String accountCode, String parentId) throws ServerException {
		AccountFilterDTO filterA = new AccountFilterDTO();
		if(SoftureUtil.isUUID(accountCode)) {
			filterA.setCatalog(catalogId);
			filterA.setParent(parentId);
			filterA.setState(SharedConstants.STATE_ACTIVE);
			filterA.setDocument(accountCode);
			AccountDTO _account = accountService.getOne(filterA);
			if(_account !=null) return _account;
		}
		
		filterA = new AccountFilterDTO();
		filterA.setCatalog(catalogId);
		filterA.setParent(parentId);
		filterA.setCode(accountCode.toUpperCase());
		filterA.setState(SharedConstants.STATE_ACTIVE);
		return accountService.getOne(filterA);
	}
	
	public AccountDTO findAccountByDocumentId(String catalogId, String documentId, String parentId) throws ServerException {
		AccountFilterDTO filterA = new AccountFilterDTO();
		filterA.setCatalog(catalogId);
		filterA.setParent(parentId);
		filterA.setDocument(documentId);
		filterA.setState(SharedConstants.STATE_ACTIVE);
		return accountService.getOne(filterA);
	}
	
	public AccountDTO getById( String id) throws ServerException {
		return accountService.getById(id);
	}
}
