package d3.accounting_plan.application;

import java.util.List;

import org.springframework.stereotype.Service;

import d3.accounting_plan.application.base.AccountService;
import d3.accounting_plan.domain.AccountDTO;
import d3.accounting_plan.domain.AccountFilterDTO;
import d3.shared.domain.SharedConstants;
import d3.java.services.D3Utils;
import d3.shared.domain.ServerException;
import org.springframework.context.annotation.Lazy;

@Service("PlanGetAccountAccountingService")
public class PlanGetAccountService {

	private final AccountService accountService;

	public PlanGetAccountService(@Lazy AccountService accountService) {
		this.accountService = accountService;
	}

	public List<AccountDTO> getActive(String catalogId, String filterText) throws ServerException {
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
		if (D3Utils.isUUID(accountCode)) {
			filterA.setCatalog(catalogId);
			filterA.setParent(parentId);
			filterA.setState(SharedConstants.STATE_ACTIVE);
			filterA.setDocument(accountCode);
			AccountDTO _account = accountService.getOne(filterA);
			if (_account != null)
				return _account;
		}

		filterA = new AccountFilterDTO();
		filterA.setCatalog(catalogId);
		filterA.setParent(parentId);
		filterA.setCode(accountCode.toUpperCase());
		filterA.setState(SharedConstants.STATE_ACTIVE);
		return accountService.getOne(filterA);
	}

	public AccountDTO findAccountByDocumentId(String catalogId, String documentId, String parentId)
			throws ServerException {
		AccountFilterDTO filterA = new AccountFilterDTO();
		filterA.setCatalog(catalogId);
		filterA.setParent(parentId);
		filterA.setDocument(documentId);
		filterA.setState(SharedConstants.STATE_ACTIVE);
		return accountService.getOne(filterA);
	}

	public AccountDTO getById(String id) throws ServerException {
		return accountService.getById(id);
	}
}
