package com.softure.api.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shared.domain.ServerException;
import com.softure.api.domain.DocumentResponse;
import com.softure.api.domain.FieldRequest;
import com.softure.api.domain.FieldResponse;
import com.softure.api.domain.ProductRequest;
import com.softure.document_execution.application.DetallePedidoVentaSvc;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.domain.DetallePedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.inventory.application.ProductoSvc;
import com.softure.inventory.domain.ProductoDTO;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;

public class ApiCommon {

	public static void chooseValueToField(FieldRequest fieldVO, PedidoVentaCaracteristicaDTO iCampo,
			ProductoSvc productoService, DetallePedidoVentaSvc detallePedidoVentaService) throws ServerException {
		switch (iCampo.getCampoDTO().getFormato()) {
		case DocumentoPlantillaCaracteristicaDTO.NUMERO: {
			iCampo.setValorNumero(transformNumber(fieldVO.getValue()));
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.FECHA: {
			iCampo.setValorFecha(transformDate(fieldVO.getValue()));
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.PROCESO: {
			if (isUUID(fieldVO.getValue()))
				iCampo.setValorOpcion(fieldVO.getValue());
			iCampo.setValorText(fieldVO.getValue());
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.PRODUCTO: {
			iCampo.setDetalles(
					assignateValueToProducts(fieldVO.getProducts(), productoService, detallePedidoVentaService, null));
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.CONFIGURACION: {
			iCampo.setValorOpcion(fieldVO.getValue());
			break;
		}
		default: {
			iCampo.setValorText(fieldVO.getValue());
			break;
		}
		}
	}

	public static boolean isUUID(String value) {
		if (value == null)
			return false;
		if (value.length() != 32)
			return false;
		if (value.contains(" "))
			return false;
		if (value.contains("-"))
			return false;
		return true;
	}

	private static Date transformDate(String value) throws ServerException {
		try {
			return new SimpleDateFormat("yyyy-MM-dd@HH:mm:ss.SSSZ").parse(value);
		} catch (ParseException e) {
			throw new ServerException(e.getMessage());
		}
	}

	private static BigDecimal transformNumber(String value) throws ServerException {
		try {
			return new BigDecimal(value);
		} catch (NumberFormatException e) {
			throw new ServerException(e.getMessage());
		}
	}

	private static List<DetallePedidoVentaDTO> assignateValueToProducts(List<ProductRequest> products,
			ProductoSvc productoService, DetallePedidoVentaSvc detallePedidoVentaService, String token) throws ServerException {
		if (products == null || products.isEmpty())
			return null;
		List<DetallePedidoVentaDTO> result = new ArrayList<>();
		for (ProductRequest iProductVO : products) {
			if (iProductVO.getCode() == null)
				throw new ServerException("Es necesario colocar el codigo del producto");
			DetallePedidoVentaDTO item = new DetallePedidoVentaDTO();
			item.setCantidad(iProductVO.getTotalQuantity());
			item.setCantidadTotal(iProductVO.getTotalQuantity());
			item.setProductoCodigo(iProductVO.getCode());
			item.setValorTotal(iProductVO.getTotalValue());
			if (iProductVO.getTotalValue() != null)
				item.setValorUnitario(item.getValorTotal().divide(item.getCantidad(), 6, RoundingMode.CEILING));
			result.add(item);
		}
		// Consulto las propiedades de los productos
		List<ProductoDTO> productos = new ArrayList<>();
		for (DetallePedidoVentaDTO detalle : result) {
			ProductoDTO newProduct = productoService.filtrarPorCodigo(detalle.getProductoCodigo());
			if (newProduct == null)
				throw new ServerException("No se identifica un producto con el codigo " + detalle.getProductoCodigo());
			detalle.setProducto(newProduct.getLlaveTabla());
			detalle.setProductoImagen(newProduct.getImagen());
			productos.add(newProduct);
		}
		productos = detallePedidoVentaService.simplificarConsultaBDProductos(productos);
		// Agrupo los detalles por producto
		for (DetallePedidoVentaDTO detalle : result) {
			for (ProductoDTO iProducto : productos) {
				if (iProducto.getLlaveTabla().compareTo(detalle.getProducto()) == 0) {
					detalle.setPropiedades(iProducto.getPropiedades());
					detalle.setPlantillaDetalle(iProducto.getTemplateFields());
					break;
				}
			}
			detallePedidoVentaService.createFieldsProduct(detalle, token);
		}

		return result;
	}

	

	public static List<DocumentResponse> transformPedidoVentaToDocument(String token,
			PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService, List<PedidoVentaDTO> results,
			DocumentoPlantillaDTO template) throws ServerException {
		List<DocumentResponse> documents = new ArrayList<>();
		if (results == null)
			return documents;
		for (PedidoVentaDTO pedidoVentaDTO : results) {
			if(template!=null && pedidoVentaDTO.getPlantilla()!=null) {
				pedidoVentaDTO.setCaracteristicas(pedidoVentaCaracteristicaService
						.listar2Documento(pedidoVentaDTO.getLlaveTabla(), pedidoVentaDTO.getHistorico()));
				for (PedidoVentaCaracteristicaDTO field : pedidoVentaDTO.getCaracteristicas()) {
					for (DocumentoPlantillaCaracteristicaDTO fieldTemplate : template.getCaracteristicas()) {
						if (field.getCampo().compareTo(fieldTemplate.getLlaveTabla()) == 0) {
							field.setCampoDTO(fieldTemplate);
							break;
						}
					}
				}	
			}
			DocumentResponse document = new DocumentResponse();
			document.setTemplate(pedidoVentaDTO.getPlantilla());
			document.setId(pedidoVentaDTO.getLlaveTabla());
			document.setCode(pedidoVentaDTO.getNombre());
			document.setActive(pedidoVentaDTO.getEstado());
			document.setStateId(pedidoVentaDTO.getEstadoExpediente());
			document.setStateName(pedidoVentaDTO.getEstadoNombre());
			document.setFields(generateFields(pedidoVentaDTO.getCaracteristicas()));
			if(pedidoVentaDTO.getDinero() != null) {
				if(pedidoVentaDTO.getDinero().getValorTotal()!=null && pedidoVentaDTO.getDinero().getValorTotal().compareTo(BigDecimal.ZERO)!=0)
					document.setFullValue(pedidoVentaDTO.getDinero().getValorTotal());
				if(pedidoVentaDTO.getDinero().getSaldo()!=null && pedidoVentaDTO.getDinero().getSaldo().compareTo(BigDecimal.ZERO)!=0)
					document.setPendingValue(pedidoVentaDTO.getDinero().getSaldo());
			}
			documents.add(document);
		}
		return documents;
	}

	private static List<FieldResponse> generateFields(List<PedidoVentaCaracteristicaDTO> caracteristicas) {
		if (caracteristicas == null || caracteristicas.isEmpty())
			return null;
		List<FieldResponse> fields = new ArrayList<>();
		for (PedidoVentaCaracteristicaDTO iField : caracteristicas) {
			if (iField.getValorText() != null && !iField.getValorText().isEmpty() && iField.getCampoDTO() != null) {
				FieldResponse field = new FieldResponse();
				field.setName(iField.getCampoDTO().getNombre());
				field.setCode(iField.getCampoDTO().getCodigo());
				field.setValue(iField.getValorText());
				field.setInternalId(iField.getValorOpcion());
				fields.add(field);
			}
		}
		return fields;
	}
}
