package d3.homologate.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.document_execution.application.CallDocumentCRUD;
import d3.document_execution.application.CallDocumentCommons;
import d3.document_execution.application.field.Propiedades;
import d3.document_execution.domain.PedidoVentaCaracteristicaDTO;
import d3.document_execution.domain.PedidoVentaDTO;
import d3.inventory.application.ProductoInventarioDescuentoSvc;
import d3.inventory.application.ProductoSvc;
import d3.inventory.domain.ProductoDTO;
import d3.inventory.domain.ProductoInventarioDescuentoDTO;
import d3.inventory.domain.ProductoInventarioDescuentoFilterDTO;
import d3.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import d3.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.property.application.PropiedadSvc;
import d3.property.domain.PropiedadValorDefinidoDTO;

@Component
public class HomologateProductStockDeduction {

	private final ProductoSvc productService;
	private final ProductoInventarioDescuentoSvc discountStockService;

	public HomologateProductStockDeduction(@Lazy ProductoSvc productService,
			@Lazy ProductoInventarioDescuentoSvc discountStockService) {
		this.productService = productService;
		this.discountStockService = discountStockService;
	}

	public void createFields(String templateId, String token, DocumentoPlantillaCaracteristicaSvc campoService,
			PropiedadSvc propertyService, CallDocumentCRUD crudService) throws ServerException {
		List<String> fieldsTemplate = new ArrayList<>();
		fieldsTemplate.add(campoService.createField(templateId, "PRODUCTO", DocumentoPlantillaCaracteristicaDTO.PROCESO,
				1, token));
		// Crear el campo tipo recurso nombre
		fieldsTemplate.add(campoService.createField(templateId, "DESCONTAR",
				DocumentoPlantillaCaracteristicaDTO.PROCESO, 2, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(1), Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);

		// mtar_valor numeric(18, 6) DEFAULT 0 NOT NULL,
		fieldsTemplate.add(
				campoService.createField(templateId, "CANTIDAD", DocumentoPlantillaCaracteristicaDTO.NUMERO, 3, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(2), Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);

		fieldsTemplate.add(campoService.createField(templateId, "CARACTERISTICA",
				DocumentoPlantillaCaracteristicaDTO.PROCESO, 4, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(3), Propiedades.PERMISO_CAMPO_RENDER, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(3), Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(3), Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);

		sincronize(templateId, fieldsTemplate, token, crudService);
	}

	private void sincronize(String templateId, List<String> fieldsTemplate, String token, CallDocumentCRUD crudService)
			throws ServerException {
		ProductoInventarioDescuentoFilterDTO filter = new ProductoInventarioDescuentoFilterDTO();
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		filter.setPaginacionRegistroFinal(20000);
		List<ProductoInventarioDescuentoDTO> pids = discountStockService.listarConsulta(filter);
		if (pids == null || pids.isEmpty())
			return;
		for (ProductoInventarioDescuentoDTO iPid : pids) {
			if (iPid.getDocumento() == null && iPid.getCantidadProductoDescontar().compareTo(BigDecimal.ZERO) != 0) {
				PedidoVentaDTO document = new PedidoVentaDTO();
				document.setPlantilla(templateId);
				document.setCaracteristicas(new ArrayList<>());

				PedidoVentaCaracteristicaDTO fieldProduct = new PedidoVentaCaracteristicaDTO();
				fieldProduct.setCampo(fieldsTemplate.get(0));
				fieldProduct.setValorOpcion(getKey(iPid.getProducto()));
				document.getCaracteristicas().add(fieldProduct);

				PedidoVentaCaracteristicaDTO fieldProductDiscount = new PedidoVentaCaracteristicaDTO();
				fieldProductDiscount.setCampo(fieldsTemplate.get(1));
				fieldProductDiscount.setValorOpcion(getKey(iPid.getProductoDescontar()));
				document.getCaracteristicas().add(fieldProductDiscount);

				PedidoVentaCaracteristicaDTO fieldCantidad = new PedidoVentaCaracteristicaDTO();
				fieldCantidad.setCampo(fieldsTemplate.get(2));
				fieldCantidad.setValorNumero(iPid.getCantidadProductoDescontar());
				document.getCaracteristicas().add(fieldCantidad);

				PedidoVentaCaracteristicaDTO fieldDim2 = new PedidoVentaCaracteristicaDTO();
				fieldDim2.setCampo(fieldsTemplate.get(3));
				if (iPid.getCaracteristica() != null)
					fieldDim2.setValorOpcion(iPid.getCaracteristica());
				document.getCaracteristicas().add(fieldDim2);

				document.setFuncionario(discountStockService.getUserFlex(token));
				document = crudService.saveWithoutTransaction(document, token, true);
				iPid.setDocumento(document.getLlaveTabla());
				discountStockService.update(iPid);
			}

		}
	}

	public void create(PedidoVentaDTO document) throws ServerException {
		ProductoInventarioDescuentoFilterDTO filter = new ProductoInventarioDescuentoFilterDTO();
		filter.setDocumento(document.getLlaveTabla());
		ProductoInventarioDescuentoDTO newItem = discountStockService.consultaUnica(filter);
		if (newItem == null) {
			newItem = new ProductoInventarioDescuentoDTO();
			newItem.setDocumento(document.getLlaveTabla());
			newItem.setProducto(getBase(CallDocumentCommons.getValueOption(document, "PRODUCTO")));
			newItem.setProductoDescontar(getBase(CallDocumentCommons.getValueOption(document, "DESCONTAR")));
			newItem.setCantidadProductoDescontar(CallDocumentCommons.getValueNumber(document, "CANTIDAD"));
			newItem.setCaracteristica(CallDocumentCommons.getValueOption(document, "CARACTERISTICA"));
			discountStockService.saveSimple(newItem);
		} else {
			if (document.getEstado().compareTo(SharedConstants.STATE_INACTIVE) == 0) {
				if (newItem.getEstado().compareTo(SharedConstants.STATE_INACTIVE) != 0) {
					newItem.setEstado(SharedConstants.STATE_INACTIVE);
					discountStockService.update(newItem);
				}
			} else {
				newItem.setProducto(getBase(CallDocumentCommons.getValueOption(document, "PRODUCTO")));
				newItem.setProductoDescontar(getBase(CallDocumentCommons.getValueOption(document, "DESCONTAR")));
				newItem.setCantidadProductoDescontar(CallDocumentCommons.getValueNumber(document, "CANTIDAD"));
				newItem.setCaracteristica(CallDocumentCommons.getValueOption(document, "CARACTERISTICA"));
				newItem.setEstado(SharedConstants.STATE_ACTIVE);
				discountStockService.update(newItem);
			}
		}
	}

	private String getBase(String valueOption) throws ServerException {
		if (valueOption == null)
			return null;
		ProductoDTO prod = productService.getProduct2Document(valueOption);
		if (prod == null)
			return null;
		return prod.getLlaveTabla();
	}

	private String getKey(String valueOption) throws ServerException {
		if (valueOption == null)
			return null;
		ProductoDTO prod = productService.consultaXId(valueOption);
		if (prod == null)
			return null;
		return prod.getDocumento();
	}
}
