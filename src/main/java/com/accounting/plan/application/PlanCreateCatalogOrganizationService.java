package com.accounting.plan.application;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.plan.application.base.CatalogService;
import com.accounting.plan.domain.AccountConst;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.CatalogFilterDTO;
import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.authentication.application.OrganizacionSvc;
import com.softure.authentication.domain.OrganizacionDTO;
import com.softure.process_designer.application.ProcesoSvc;
import com.softure.process_designer.domain.ProcesoDTO;
import com.softure.process_designer.domain.ProcesoFilterDTO;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.process_form.domain.DocumentoPlantillaFilterDTO;

@Service("CreateCatalogOrganizationAccountingService")
public class PlanCreateCatalogOrganizationService {

	@Autowired @Lazy 
	OrganizacionSvc organizationService;
	@Autowired @Lazy 
	private ProcesoSvc processService;
	@Autowired @Lazy 
	private DocumentoPlantillaSvc templateService;
	@Autowired @Lazy 
	private CatalogService catalogService;
	@Autowired @Lazy 
	private PlanCreateCatalogService createCatalogService;
	@Autowired @Lazy 
	private PlanCreateAccountService createAccountService;

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void call() throws ServerException {
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
			catalog = createCatalogService.call(catalog);
		}
			

		DocumentoPlantillaFilterDTO filterTemplate = new DocumentoPlantillaFilterDTO();
		filterTemplate.setEstado(SharedConstants.STATE_ACTIVE);
		filterTemplate.setPaginacionRegistroFinal(2000);
		List<DocumentoPlantillaDTO> templates = templateService.listarConsulta(filterTemplate);

		ProcesoFilterDTO filter = new ProcesoFilterDTO();
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		List<ProcesoDTO> procesos = processService.consultarArbol(filter);
		for (ProcesoDTO procesoDTO : procesos) {
			createAccountProcess(procesoDTO, catalog.getKey(), null, templates);
		}
	}

	private void createAccountProcess(ProcesoDTO procesoDTO, String catalogId, String accountParentId,
			List<DocumentoPlantillaDTO> templates) throws ServerException {
		AccountDTO account = new AccountDTO();
		account.setCatalog(catalogId);
		account.setCode(procesoDTO.getCodigo());
		account.setTemplate(procesoDTO.getLlaveTabla());
		account.setName(procesoDTO.getNombre());
		account.setParent(accountParentId);
		account.setType(AccountConst.TYPE_GROUP);
		account.setOperation(AccountConst.OPERATION_ADD);
		account = createAccountService.call(account);
		
		for (DocumentoPlantillaDTO itemplate : templates) {
			if(itemplate.getProceso()==null) {
				if(itemplate.getProceso().compareTo("NODO1476")==0)
					createAccountTemplate(itemplate, catalogId, account.getKey());
			}else {
				if(itemplate.getProceso().compareTo(procesoDTO.getLlaveTabla())==0) 
					createAccountTemplate(itemplate, catalogId, account.getKey());
			}
		}
		
		if (procesoDTO.getHijos() == null)
			return;
		for (ProcesoDTO iProcess : procesoDTO.getHijos()) {
			createAccountProcess(iProcess, catalogId, account.getKey(), templates);
		}	
	}

	private void createAccountTemplate(DocumentoPlantillaDTO itemplate, String catalogId, String accountParentId) throws ServerException {
		AccountDTO account = new AccountDTO();
		account.setCatalog(catalogId);
		account.setCode(itemplate.getCodigo());
		account.setTemplate(itemplate.getLlaveTabla());
		account.setName(itemplate.getNombre());
		account.setParent(accountParentId);
		account.setType(AccountConst.TYPE_OPERATIONAL);
		account.setOperation(AccountConst.OPERATION_ADD);
		account = createAccountService.call(account);
	}

}