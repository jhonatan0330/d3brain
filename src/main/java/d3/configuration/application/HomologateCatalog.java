package d3.configuration.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import d3.accounting.application.PlanCreateCatalogService;
import d3.accounting.application.base.CatalogService;
import d3.accounting.domain.CatalogDTO;
import d3.accounting.domain.CatalogFilterDTO;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.document.application.CallDocumentCommons;
import d3.document.application.field.Propiedades;
import d3.document.domain.PedidoVentaDTO;
import d3.process.application.DocumentoPlantillaCaracteristicaSvc;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;

@Component
public class HomologateCatalog {

	private final CatalogService catalogService;
	private final PlanCreateCatalogService createCatalogService;

	public HomologateCatalog(@Lazy CatalogService catalogService, @Lazy PlanCreateCatalogService createCatalogService) {
		this.catalogService = catalogService;
		this.createCatalogService = createCatalogService;
	}

	public void createCatalogFields(String templateId, String token, DocumentoPlantillaCaracteristicaSvc campoService,
			PropiedadSvc propertyService) throws ServerException {
		List<String> fieldsTemplate = new ArrayList<>();
		fieldsTemplate.add(
				campoService.createField(templateId, "NOMBRE", DocumentoPlantillaCaracteristicaDTO.TEXTO, 1, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(0), Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA,
				templateId, Propiedades.DESCRIPCION, fieldsTemplate.get(0), token), token);
		// Crear el campo tipo recurso nombre
		fieldsTemplate.add(
				campoService.createField(templateId, "CODIGO", DocumentoPlantillaCaracteristicaDTO.TEXTO, 2, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(1), Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);

		// fecha inicial
		fieldsTemplate.add(campoService.createField(templateId, "FECHA_INICIAL",
				DocumentoPlantillaCaracteristicaDTO.FECHA, 3, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(2), Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		// fecha final
		fieldsTemplate.add(campoService.createField(templateId, "FECHA_FINAL",
				DocumentoPlantillaCaracteristicaDTO.FECHA, 4, token));
		// propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
		// fieldsTemplate.get(3),
		// Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(3), Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
	}

	public void createCatalog(PedidoVentaDTO document) throws ServerException {
		CatalogFilterDTO filter = new CatalogFilterDTO();
		filter.setDocument(document.getLlaveTabla());
		CatalogDTO newCatalog = catalogService.getOne(filter);
		if (newCatalog == null) {
			newCatalog = new CatalogDTO();
			newCatalog.setDocument(document.getLlaveTabla());
			newCatalog.setTemplate(document.getPlantilla());
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
