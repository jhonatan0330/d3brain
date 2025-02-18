package com.accounting.plan.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.plan.application.base.AccountService;
import com.accounting.plan.application.base.CatalogService;
import com.accounting.plan.domain.AccountConst;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.AccountFilterDTO;
import com.accounting.plan.domain.CatalogFilterDTO;
import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;

@Service("PlanCreateAccountTemplateAccountingService")
public class PlanCreateAccountService {

	@Autowired
	@Lazy
	private AccountService accountService;
	@Autowired
	@Lazy
	private CatalogService catalogService;

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public AccountDTO call(AccountDTO account) throws ServerException {
		if (account.getCatalogDocument() != null) {
			CatalogFilterDTO filter = new CatalogFilterDTO();
			filter.setDocument(account.getCatalogDocument());
			account.setCatalog(catalogService.getOne(filter).getKey());
		}
		if (account.getParentDocument() != null) {
			AccountFilterDTO filterParent = new AccountFilterDTO();
			filterParent.setDocument(account.getParentDocument());
			account.setParent(accountService.getOne(filterParent).getKey());
		}
		if (account.getCatalog() == null)
			throw new ServerException("Es importante asignar la cuenta a un catalogo");
		if (account.getParent() != null && account.getParent().isEmpty())
			account.setParent(null);
		if (account.getCode() != null && account.getCode().isEmpty())
			account.setCode(null);
		assignWBSNumber(account);
		if (account.getCode() == null)
			account.setCode(account.getWbs());
		accountService.save(account);
		return accountService.getById(account.getKey());
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public AccountDTO callUpdate(AccountDTO account) throws ServerException {
		AccountDTO bd = accountService.getById(account.getKey());
		if (bd == null)
			throw new ServerException("No se identifica la cuenta");
		if (bd.getCatalog().compareTo(account.getCatalog()) != 0)
			throw new ServerException("No se puede modificar el catalogo");
		if (account.getParent() != null && account.getParent().isEmpty())
			account.setParent(null);
		if (account.getParent() != null && account.getParent().compareTo(account.getKey()) == 0)
			throw new ServerException("La cuenta parent no puede ser la misma");
		if (account.getCode() != null && account.getCode().isEmpty())
			account.setCode(null);
		account.setState(bd.getState());
		if(account.getOperation()==null) account.setOperation(AccountConst.OPERATION_ADD);
		accountService.update(account);
		return accountService.getById(account.getKey());
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public AccountDTO callDelete(String accountId) throws ServerException {
		return accountService.delete(accountId);
	}

	private void assignWBSNumber(AccountDTO account) throws ServerException {

		AccountFilterDTO filter = new AccountFilterDTO();
		String prefixWBS = "";
		if (account.getParent() == null) {
			filter.setLevel(1);
			account.setLevel(1);
		} else {
			filter.setParent(account.getParent());
			AccountDTO parentAccount = accountService.getById(account.getParent());
			if (parentAccount == null)
				throw new ServerException("En la cuenta " + account.getName() + " el nodo principal "
						+ account.getParent() + " no se encuentra en la BD por su identificador");
			if (parentAccount.getState().compareTo(SharedConstants.STATE_ACTIVE) != 0)
				throw new ServerException("La cuenta" + parentAccount.getName() + " no se encuentra activa");
			prefixWBS = parentAccount.getWbs() + ".";
			account.setLevel(parentAccount.getLevel() + 1);
			if (parentAccount.getType().compareTo(AccountConst.TYPE_OPERATIONAL) == 0) {
				parentAccount.setType(AccountConst.TYPE_GROUP);
				accountService.update(parentAccount);
			}
		}
		filter.setState(SharedConstants.STATE_ACTIVE);
		int countAccount = accountService.count(filter);
		account.setWbs(prefixWBS + "%1$4s".formatted((countAccount + 1)));
		if(account.getType()==null)account.setType(AccountConst.TYPE_OPERATIONAL);
		if(account.getOperation()==null) account.setOperation(AccountConst.OPERATION_ADD);
	}

}