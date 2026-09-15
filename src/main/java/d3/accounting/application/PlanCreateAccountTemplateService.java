package d3.accounting.application;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.accounting.application.base.AccountService;
import d3.accounting.domain.AccountConst;
import d3.accounting.domain.AccountDTO;
import d3.process.application.DocumentoPlantillaSvc;
import d3.process.domain.DocumentoPlantillaDTO;
import d3.shared.domain.ServerException;

@Service("PlanCreateAccountTemplateAccountingService")
public class PlanCreateAccountTemplateService {

	private final AccountService accountService;
	private final PlanGetAccountService getAccountService;
	private final DocumentoPlantillaSvc templateService;

	public PlanCreateAccountTemplateService(@Lazy AccountService accountService,
			@Lazy DocumentoPlantillaSvc templateService, @Lazy PlanGetAccountService getAccountService) {
		this.accountService = accountService;
		this.getAccountService = getAccountService;
		this.templateService = templateService;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void call(String templateId) throws ServerException {

		AccountDTO accountDB = getAccountService.findAccountByTemplateId(templateId);
		if (accountDB != null)
			return;

		accountDB = new AccountDTO();

		DocumentoPlantillaDTO templateDTO = templateService.consultaXId(templateId);

		accountDB.setCode(templateDTO.getCodigo());
		accountDB.setName(templateDTO.getNombre());
		accountDB.setTemplate(templateId);
		accountDB.setWbs("1");
		accountDB.setType(AccountConst.TYPE_OPERATIONAL);
		accountDB.setOperation(AccountConst.OPERATION_ADD);
		accountService.save(accountDB);
	}

}