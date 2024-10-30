package com.configuration.homologate.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.accounting.plan.application.PlanCreateCatalogService;
import com.accounting.plan.application.base.CatalogService;
import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.CatalogFilterDTO;
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
public class HomologateCatalog {

	@Autowired @Lazy CatalogService catalogService;
	@Autowired @Lazy PlanCreateCatalogService createCatalogService;
	
	public void createCatalogFields(String templateId, String token, DocumentoPlantillaCaracteristicaSvc campoService, PropiedadSvc propertyService) throws ServerException {
		List<String> fieldsTemplate = new ArrayList<>();
		fieldsTemplate.add(
				campoService.createField(templateId, "NOMBRE", DocumentoPlantillaCaracteristicaDTO.TEXTO, 1, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(0),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA, templateId,
				Propiedades.DESCRIPCION, fieldsTemplate.get(0), token), token);
		// Crear el campo tipo recurso nombre
		fieldsTemplate.add(campoService.createField(templateId, "CODIGO",
						DocumentoPlantillaCaracteristicaDTO.TEXTO, 2, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(1),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
	
		// fecha inicial
		fieldsTemplate.add(campoService.createField(templateId, "FECHA_INICIAL",
				DocumentoPlantillaCaracteristicaDTO.FECHA, 3, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(2),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		// fecha final
		fieldsTemplate.add(campoService.createField(templateId, "FECHA_FINAL",
				DocumentoPlantillaCaracteristicaDTO.FECHA, 4, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(3),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(3),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
	}
	
	

	public void createCatalog(PedidoVentaDTO document) throws ServerException {
		CatalogFilterDTO filter = new CatalogFilterDTO();
		filter.setDocument(document.getLlaveTabla());
		CatalogDTO newCatalog = catalogService.getOne(filter);
		if (newCatalog == null) {
			newCatalog = new CatalogDTO();
			newCatalog.setDocument(document.getLlaveTabla());
			newCatalog.setFinalDate(CallDocumentCommons.getValueDate(document, "FECHA_FINAL"));
			newCatalog.setInitialDate(CallDocumentCommons.getValueDate(document, "FECHA_INICIAL"));
			newCatalog.setName(CallDocumentCommons.getValueText(document, "NOMBRE"));
			newCatalog.setCode(CallDocumentCommons.getValueText(document, "CODIGO"));
			createCatalogService.call(newCatalog);
		} else {
			if (document.getEstado().compareTo(SharedConstants.STATE_INACTIVE) == 0) {
				if (newCatalog.getState().compareTo(SharedConstants.STATE_INACTIVE) != 0) {
					newCatalog.setState(SharedConstants.STATE_INACTIVE);
					createCatalogService.call(newCatalog);
				}
			} else {
				newCatalog.setFinalDate(CallDocumentCommons.getValueDate(document, "FECHA_FINAL"));
				newCatalog.setInitialDate(CallDocumentCommons.getValueDate(document, "FECHA_INICIAL"));
				newCatalog.setName(CallDocumentCommons.getValueText(document, "NOMBRE"));
				newCatalog.setCode(CallDocumentCommons.getValueText(document, "CODIGO"));
				newCatalog.setState(SharedConstants.STATE_ACTIVE);
				createCatalogService.call(newCatalog);
			}
		}
	}
}
