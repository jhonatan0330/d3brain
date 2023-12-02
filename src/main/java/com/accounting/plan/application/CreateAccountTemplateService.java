package com.accounting.plan.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accounting.plan.domain.AccountDTO;
import com.shared.domain.ServerException;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;

@Service("CreateAccountTemplateAccountingService")
public class CreateAccountTemplateService {

	@Autowired
	private DocumentoPlantillaSvc templateService;
	@Autowired
	private DocumentoPlantillaCaracteristicaSvc fieldService;
	@Autowired
	private PlanCreateAccountService createAccountService;

	public void call(String catalogId, String templateId, String fieldId,  String token)
			throws ServerException {
		AccountDTO account = new AccountDTO();
		if (fieldId != null) {
			DocumentoPlantillaCaracteristicaDTO field = fieldService.consultaXId(fieldId);
			account.setField(field.getLlaveTabla());
			account.setCode(field.getCodigo());
			account.setName(field.getNombre());
			templateId = field.getPlantilla();
		}
		DocumentoPlantillaDTO template = templateService.consultaXId(templateId);
		account.setTemplate(template.getLlaveTabla());
		if (account.getName() == null)
			account.setName(template.getNombre());
		account.setCode(template.getCodigo());
		account.setCatalog(catalogId);
		if (account.getCode() == null) {
			account.setCode(template.getCodigo());
		} else {
			account.setCode(account.getCode() + "." + template.getCodigo());
		}
		createAccountService.call(account, token);
	}

}