package com.accounting.plan.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.plan.application.base.AccountService;
import com.accounting.plan.domain.AccountConst;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.AccountFilterDTO;
import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;

@Service("PlanCreateAccountTemplateAccountingService")
public class PlanCreateAccountService {

	@Autowired
	private AccountService accountService;
	//@Autowired
	//private FormatVoucherService formatService;
	//@Autowired
	//private FormatLineService lineService;

	@Transactional(value = "accountingTransactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public AccountDTO call(AccountDTO account, String token) throws ServerException {
		if(account.getCatalog()==null) throw new ServerException("Es importatne asignar la cuenta a un catalogo");
		if(account.getParent()!=null && account.getParent().isEmpty()) account.setParent(null);
		if(account.getCode()!=null && account.getCode().isEmpty()) account.setCode(null);
		assignWBSNumber(account, token);
		if(account.getCode()==null) account.setCode(account.getWbs());
		accountService.save(account, token);
		return accountService.getById(account.getKey());
	}
	
	private void assignWBSNumber(AccountDTO account, String token) throws ServerException {
		
		AccountFilterDTO filter = new AccountFilterDTO();
		String prefixWBS = "";
		if(account.getParent()==null) {
			filter.setLevel(1);
			account.setLevel(1);
		} else {
			filter.setParent(account.getParent());
			AccountDTO parentAccount = accountService.getById(account.getParent());
			if(parentAccount == null) throw new ServerException("En la cuenta " +account.getName() +" el nodo principal " +account.getParent()+ " no se encuentra en la BD por su identificador");
			if(parentAccount.getState().compareTo(SharedConstants.STATE_ACTIVE)!=0) throw new ServerException("La cuenta" +parentAccount.getName() +" no se encuentra activa");
			prefixWBS = parentAccount.getWbs() + ".";
			account.setLevel(parentAccount.getLevel()+1);
			if(parentAccount.getType().compareTo(AccountConst.TYPE_OPERATIONAL)==0) {
				parentAccount.setType(AccountConst.TYPE_GROUP);
				accountService.update(parentAccount, token);
			}
		}
		filter.setState(SharedConstants.STATE_ACTIVE);
		int countAccount = accountService.count(filter);
		account.setWbs(prefixWBS + String.format("%1$4s", (countAccount+1)));
		account.setStatus(AccountConst.STATUS_PLANNING);
		account.setType(AccountConst.TYPE_OPERATIONAL);
		account.setOperation(AccountConst.OPERATION_ADD);
	}
/*
	@Transactional(value = "accountingTransactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public AccountDTO configurate(AccountDTO account, String token) throws ServerException {
		if (account.getType() != null && account.getType().compareTo(AccountConst.TYPE_GROUP) != 0
				&& account.getTemplate() != null) {
			FormatVoucherDTO format = new FormatVoucherDTO();
			format.setCatalog(account.getCatalog());
			format.setTemplate(account.getTemplate());
			format = formatService.save(format, token);
			FormatLineDTO line = new FormatLineDTO();
			line.setAccount(account.getKey());
			line.setFormat(format.getKey());
			if (account.getOperation() != null && account.getOperation().compareTo(AccountConst.OPERATION_MINUS) == 0) {
				line.setNegative("1");
			} else {
				line.setPositive("1");
			}
			lineService.save(line, token);
		}
		//createMatrix(account, token);
		return account;
	}*/

	

}