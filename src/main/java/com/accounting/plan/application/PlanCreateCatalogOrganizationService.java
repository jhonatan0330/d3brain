package com.accounting.plan.application;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.plan.application.base.CatalogService;
import com.accounting.plan.domain.AccountConst;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.CatalogFilterDTO;
import com.softure.authentication.application.OrganizacionSvc;
import com.softure.authentication.domain.OrganizacionDTO;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.process_designer.application.ProcesoSvc;
import com.softure.process_designer.domain.ProcesoDTO;
import com.softure.process_designer.domain.ProcesoFilterDTO;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.process_form.domain.DocumentoPlantillaFilterDTO;

@Service("CreateCatalogOrganizationAccountingService")
public class PlanCreateCatalogOrganizationService {

	@Autowired
	OrganizacionSvc organizationService;
	@Autowired
	private ProcesoSvc processService;
	@Autowired
	private DocumentoPlantillaSvc templateService;
	@Autowired
	private CatalogService catalogService;
	@Autowired
	private PlanCreateCatalogService createCatalogService;
	@Autowired
	private PlanCreateAccountService createAccountService;

	@Transactional(value = "accountingTransactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void call(String token) throws ServerException {
		OrganizacionDTO organization = organizationService.obtenerPrincipal();
		if (organization.getCodigo() == null)
			throw new ServerException("No codigo organizacion");
		// Aqui debo tener el cuenta el año y acortarlos a 8
		String codeCatalogMain = organization.getCodigo() + "_2023";
		CatalogFilterDTO filterDTO = new CatalogFilterDTO();
		filterDTO.setCode(codeCatalogMain);
		CatalogDTO catalog = catalogService.getOne(filterDTO);
		if (catalog == null) {
			catalog = new CatalogDTO();
			catalog.setCode(codeCatalogMain);
			Calendar dates = Calendar.getInstance();
			dates.set(2023, 0, 0, 0, 0, 0);
			catalog.setInitialDate(dates.getTime());
			dates.add(Calendar.YEAR, 1);
			dates.add(Calendar.SECOND, -1);
			catalog.setFinalDate(dates.getTime());
			catalog.setName(organization.getNombre());
			catalog = createCatalogService.call(catalog, token);
		}
			

		DocumentoPlantillaFilterDTO filterTemplate = new DocumentoPlantillaFilterDTO();
		filterTemplate.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		filterTemplate.setPaginacionRegistroFinal(2000);
		List<DocumentoPlantillaDTO> templates = templateService.listarConsulta(filterTemplate);

		ProcesoFilterDTO filter = new ProcesoFilterDTO();
		filter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		List<ProcesoDTO> procesos = processService.consultarArbol(filter);
		for (ProcesoDTO procesoDTO : procesos) {
			createAccountProcess(procesoDTO, catalog.getKey(), null, templates, token);
		}
	}

	private void createAccountProcess(ProcesoDTO procesoDTO, String catalogId, String accountParentId,
			List<DocumentoPlantillaDTO> templates, String token) throws ServerException {
		AccountDTO account = new AccountDTO();
		account.setCatalog(catalogId);
		account.setCode(procesoDTO.getCodigo());
		account.setTemplate(procesoDTO.getLlaveTabla());
		account.setName(procesoDTO.getNombre());
		account.setParent(accountParentId);
		account.setType(AccountConst.TYPE_GROUP);
		account.setOperation(AccountConst.OPERATION_ADD);
		account = createAccountService.call(account, token);
		
		for (DocumentoPlantillaDTO itemplate : templates) {
			if(itemplate.getProceso()==null) {
				if(itemplate.getProceso().compareTo("NODO1476")==0)
					createAccountTemplate(itemplate, catalogId, account.getKey(), token);
			}else {
				if(itemplate.getProceso().compareTo(procesoDTO.getLlaveTabla())==0) 
					createAccountTemplate(itemplate, catalogId, account.getKey(), token);
			}
		}
		
		if (procesoDTO.getHijos() == null)
			return;
		for (ProcesoDTO iProcess : procesoDTO.getHijos()) {
			createAccountProcess(iProcess, catalogId, account.getKey(), templates, token);
		}	
	}

	private void createAccountTemplate(DocumentoPlantillaDTO itemplate, String catalogId, String accountParentId, String token) throws ServerException {
		AccountDTO account = new AccountDTO();
		account.setCatalog(catalogId);
		account.setCode(itemplate.getCodigo());
		account.setTemplate(itemplate.getLlaveTabla());
		account.setName(itemplate.getNombre());
		account.setParent(accountParentId);
		account.setType(AccountConst.TYPE_OPERATIONAL);
		account.setOperation(AccountConst.OPERATION_ADD);
		account = createAccountService.call(account, token);
	}

}