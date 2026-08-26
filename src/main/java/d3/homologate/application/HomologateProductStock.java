package d3.homologate.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.document_execution.application.CallDocumentCRUD;
import d3.document_execution.application.CallDocumentCommons;
import d3.document_execution.application.field.Propiedades;
import d3.document_execution.domain.PedidoVentaCaracteristicaDTO;
import d3.document_execution.domain.PedidoVentaDTO;
import d3.inventory.application.ProductoInventarioSvc;
import d3.inventory.application.ProductoSvc;
import d3.inventory.domain.ProductoDTO;
import d3.inventory.domain.ProductoInventarioDTO;
import d3.inventory.domain.ProductoInventarioFilterDTO;
import d3.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import d3.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.property.application.PropiedadSvc;
import d3.property.domain.PropiedadValorDefinidoDTO;
import org.springframework.context.annotation.Lazy;

@Component
public class HomologateProductStock {

	private final ProductoInventarioSvc stockService;
	private final ProductoSvc productService;

	public HomologateProductStock(@Lazy ProductoInventarioSvc stockService, @Lazy ProductoSvc productService) {
		this.stockService = stockService;
		this.productService = productService;
	}

	public void createFields(String templateId, String token, DocumentoPlantillaCaracteristicaSvc campoService,
			PropiedadSvc propertyService, CallDocumentCRUD crudService) throws ServerException {
		List<String> fieldsTemplate = new ArrayList<>();
		fieldsTemplate.add(campoService.createField(templateId, "PRODUCTO", DocumentoPlantillaCaracteristicaDTO.PROCESO,
				1, token));

		fieldsTemplate.add(
				campoService.createField(templateId, "BODEGA", DocumentoPlantillaCaracteristicaDTO.PROCESO, 2, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(1), Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);

		fieldsTemplate.add(
				campoService.createField(templateId, "MINIMA", DocumentoPlantillaCaracteristicaDTO.NUMERO, 3, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(2), Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(2), Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);

		fieldsTemplate.add(
				campoService.createField(templateId, "MAXIMA", DocumentoPlantillaCaracteristicaDTO.NUMERO, 4, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(3), Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(3), Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);

		sincronize(templateId, fieldsTemplate, token, crudService);
	}

	private void sincronize(String templateId, List<String> fieldsTemplate, String token, CallDocumentCRUD crudService)
			throws ServerException {
		ProductoInventarioFilterDTO filter = new ProductoInventarioFilterDTO();
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		filter.setPaginacionRegistroFinal(20000);
		List<ProductoInventarioDTO> pids = stockService.listarConsulta(filter);
		if (pids == null || pids.isEmpty())
			return;
		for (ProductoInventarioDTO iPid : pids) {
			if (iPid.getDocumento() == null) {
				PedidoVentaDTO document = new PedidoVentaDTO();
				document.setPlantilla(templateId);
				document.setCaracteristicas(new ArrayList<>());

				PedidoVentaCaracteristicaDTO fieldProduct = new PedidoVentaCaracteristicaDTO();
				fieldProduct.setCampo(fieldsTemplate.get(0));
				fieldProduct.setValorOpcion(getKey(iPid.getProducto()));
				document.getCaracteristicas().add(fieldProduct);

				PedidoVentaCaracteristicaDTO fieldProductDiscount = new PedidoVentaCaracteristicaDTO();
				fieldProductDiscount.setCampo(fieldsTemplate.get(1));
				fieldProductDiscount.setValorOpcion(iPid.getBodega());
				document.getCaracteristicas().add(fieldProductDiscount);

				PedidoVentaCaracteristicaDTO fieldCantidad = new PedidoVentaCaracteristicaDTO();
				fieldCantidad.setCampo(fieldsTemplate.get(2));
				fieldCantidad.setValorNumero(iPid.getCantidadMinima());
				document.getCaracteristicas().add(fieldCantidad);

				PedidoVentaCaracteristicaDTO fieldDim2 = new PedidoVentaCaracteristicaDTO();
				fieldDim2.setCampo(fieldsTemplate.get(3));
				fieldDim2.setValorNumero(iPid.getCantidadMaxima());
				document.getCaracteristicas().add(fieldDim2);

				document.setFuncionario(stockService.getUserFlex(token));
				document = crudService.saveWithoutTransaction(document, token, true);
				iPid.setDocumento(document.getLlaveTabla());
				stockService.update(iPid);
			}

		}
	}

	public void create(PedidoVentaDTO document, String token) throws ServerException {
		ProductoInventarioFilterDTO filter = new ProductoInventarioFilterDTO();
		filter.setDocumento(document.getLlaveTabla());
		ProductoInventarioDTO newItem = stockService.consultaUnica(filter);
		if (newItem == null) {
			newItem = new ProductoInventarioDTO();
			newItem.setDocumento(document.getLlaveTabla());
			newItem.setProducto(getBase(CallDocumentCommons.getValueOption(document, "PRODUCTO")));
			newItem.setBodega(CallDocumentCommons.getValueOption(document, "BODEGA"));
			newItem.setCantidadMinima(CallDocumentCommons.getValueNumber(document, "MINIMA"));
			newItem.setCantidadMaxima(CallDocumentCommons.getValueNumber(document, "MAXIMA"));
			stockService.guardar(newItem, token);
		} else {
			if (document.getEstado().compareTo(SharedConstants.STATE_INACTIVE) == 0) {
				if (newItem.getEstado().compareTo(SharedConstants.STATE_INACTIVE) != 0) {
					newItem.setEstado(SharedConstants.STATE_INACTIVE);
					stockService.update(newItem);
				}
			} else {
				newItem.setProducto(getBase(CallDocumentCommons.getValueOption(document, "PRODUCTO")));
				newItem.setBodega(CallDocumentCommons.getValueOption(document, "BODEGA"));
				newItem.setCantidadMinima(CallDocumentCommons.getValueNumber(document, "MINIMA"));
				newItem.setCantidadMaxima(CallDocumentCommons.getValueNumber(document, "MAXIMA"));
				newItem.setEstado(SharedConstants.STATE_ACTIVE);
				stockService.actualizar(newItem, token);
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
