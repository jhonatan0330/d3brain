package com.configuration.homologate.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.accounting.plan.application.PlanCreateAccountService;
import com.accounting.plan.application.base.AccountService;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.AccountFilterDTO;
import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.document_execution.application.CallDocumentCommons;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

@Component
public class HomologateAccount {

	private final AccountService accountService;
	private final PlanCreateAccountService createAccountService;

	public HomologateAccount(@Lazy AccountService accountService, @Lazy PlanCreateAccountService createAccountService) {
		this.accountService = accountService;
		this.createAccountService = createAccountService;
	}

	public void createAccountFields(String templateId, String token, DocumentoPlantillaCaracteristicaSvc campoService,
			PropiedadSvc propertyService) throws ServerException {
		List<String> fieldsTemplate = new ArrayList<>();

		fieldsTemplate.add(campoService.createField(templateId, "CATALOGO", DocumentoPlantillaCaracteristicaDTO.PROCESO,
				1, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(0), Propiedades.PERMISO_CAMPO_RENDER, "1", token), token);

		fieldsTemplate.add(
				campoService.createField(templateId, "CODIGO", DocumentoPlantillaCaracteristicaDTO.TEXTO, 2, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(1), Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);

		fieldsTemplate.add(
				campoService.createField(templateId, "NOMBRE", DocumentoPlantillaCaracteristicaDTO.TEXTO, 3, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(2), Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA,
				templateId, Propiedades.DESCRIPCION, fieldsTemplate.get(2), token), token);

		fieldsTemplate.add(
				campoService.createField(templateId, "PARENT", DocumentoPlantillaCaracteristicaDTO.PROCESO, 4, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(3), Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(3), Propiedades.PLANTILLA_AUXILIAR, templateId, token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(3), Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);

		fieldsTemplate.add(campoService.createField(templateId, "NATURALEZA",
				DocumentoPlantillaCaracteristicaDTO.CONFIGURACION, 5, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(4), Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(4), Propiedades.OPCIONES, "C", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(4), Propiedades.OPCIONES, "D", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(4), Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
	}

	public void createAccount(PedidoVentaDTO document) throws ServerException {
		AccountFilterDTO filter = new AccountFilterDTO();
		filter.setDocument(document.getLlaveTabla());
		AccountDTO newAccount = accountService.getOne(filter);
		if (newAccount == null) {
			newAccount = new AccountDTO();
			newAccount.setDocument(document.getLlaveTabla());
			newAccount.setCatalogDocument(CallDocumentCommons.getValueOption(document, "CATALOGO"));
			newAccount.setCode(CallDocumentCommons.getValueText(document, "CODIGO"));
			newAccount.setName(CallDocumentCommons.getValueText(document, "NOMBRE"));
			newAccount.setParentDocument(CallDocumentCommons.getValueOption(document, "PARENT"));
			newAccount.setOperation(CallDocumentCommons.getValueOption(document, "NATURALEZA"));
			createAccountService.call(newAccount);
		} else {
			if (document.getEstado().compareTo(SharedConstants.STATE_INACTIVE) == 0) {
				if (newAccount.getState().compareTo(SharedConstants.STATE_INACTIVE) != 0) {
					newAccount.setState(SharedConstants.STATE_INACTIVE);
					createAccountService.callDelete(newAccount.getKey());
				}
			} else {
				newAccount.setCatalogDocument(CallDocumentCommons.getValueOption(document, "CATALOGO"));
				newAccount.setCode(CallDocumentCommons.getValueText(document, "CODIGO"));
				newAccount.setName(CallDocumentCommons.getValueText(document, "NOMBRE"));
				newAccount.setParentDocument(CallDocumentCommons.getValueOption(document, "PARENT"));
				newAccount.setOperation(CallDocumentCommons.getValueOption(document, "NATURALEZA"));
				newAccount.setState(SharedConstants.STATE_ACTIVE);
				createAccountService.callUpdate(newAccount);
			}
		}
	}

}
