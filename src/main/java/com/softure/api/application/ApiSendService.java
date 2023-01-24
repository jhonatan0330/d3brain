package com.softure.api.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.api.domain.DocumentVO;
import com.softure.api.domain.FieldVO;
import com.softure.api.domain.ProductVO;
import com.softure.document_execution.application.CallDocumentCRUD;
import com.softure.document_execution.application.DetallePedidoVentaSvc;
import com.softure.document_execution.domain.DetallePedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.inventory.application.ProductoSvc;
import com.softure.inventory.domain.ProductoDTO;
import com.softure.java.domain.IdResponse;
import com.softure.java.dto.exception.ServerException;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;

@Service
public class ApiSendService {

	@Autowired DocumentoPlantillaSvc plantillaService;
	@Autowired CallDocumentCRUD saveDocumentService;
	
	@Autowired private DetallePedidoVentaSvc detallePedidoVentaService;
	@Autowired private ProductoSvc productoService;

	public IdResponse call(String token, DocumentVO item) throws ServerException {
		validateItem(item);
		// Con el codigo de la plantilla consultar la plantilla completa
		DocumentoPlantillaDTO template = findTemplate(item.getTemplate(), token);
		// crear el documento con todos los campos vacios
		PedidoVentaDTO document = createDocument(template);
		// Por cada campo con el codigo del campo colocar
		assignateValue(document, item.getFields());
		// Envio a guardar el documento
		document = saveDocumentService.save(document, token);
		return new IdResponse(document.getLlaveTabla(), document.getNombre());
	}

	private void validateItem(DocumentVO item) throws ServerException {
		if (item.getTemplate() == null || item.getTemplate().isEmpty())
			throw new ServerException("El codigo de la plantilla es null, recuerda usar el campo template");
		if (item.getFields() == null)
			throw new ServerException("El documento no tiene campos, recuerda usar el tag fields");
		for (FieldVO fieldVO : item.getFields()) {
			if (fieldVO.getField() == null)
				throw new ServerException("Existe un campo " + fieldVO.getField()
						+ " que el valor FIELD ES VACIO, si no se envia valor no es necesario colocar el campo");
			if (fieldVO.getValue() == null && fieldVO.getProducts() == null)
				throw new ServerException("Existe un campo " + fieldVO.getField()
						+ " que no registra valores, si no se envia valor no es necesario colocar el campo");
		}
	}

	private void assignateValue(PedidoVentaDTO document, List<FieldVO> fields) throws ServerException {
		if (fields == null || fields.isEmpty())
			return;
		for (FieldVO fieldVO : fields) {
			for (PedidoVentaCaracteristicaDTO iCampo : document.getCaracteristicas()) {
				if (iCampo.getCampoDTO().getCodigo().compareTo(fieldVO.getField()) == 0) {
					chooseValueToField(fieldVO, iCampo);
				}
			}
		}
	}

	private void chooseValueToField(FieldVO fieldVO, PedidoVentaCaracteristicaDTO iCampo) throws ServerException {
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
			iCampo.setDetalles(assignateValueToProducts(fieldVO.getProducts()));
			break;
		}
		default: {
			iCampo.setValorText(fieldVO.getValue());
			break;
		}
		}
	}

	private boolean isUUID(String value) {
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

	private Date transformDate(String value) throws ServerException {
		try {
			return new SimpleDateFormat("yyyy-MM-dd@HH:mm:ss.SSSZ").parse(value);
		} catch (ParseException e) {
			throw new ServerException(e.getMessage());
		}
	}

	private BigDecimal transformNumber(String value) throws ServerException {
		try {
			return new BigDecimal(value);
		} catch (NumberFormatException e) {
			throw new ServerException(e.getMessage());
		}
	}

	private PedidoVentaDTO createDocument(DocumentoPlantillaDTO template) {
		PedidoVentaDTO document = new PedidoVentaDTO();
		document.setPlantilla(template.getLlaveTabla());
		if (template.getCaracteristicas() == null)
			return document;
		document.setCaracteristicas(new ArrayList<>());
		for (DocumentoPlantillaCaracteristicaDTO iCampo : template.getCaracteristicas()) {
			PedidoVentaCaracteristicaDTO newField = new PedidoVentaCaracteristicaDTO();
			newField.setCampo(iCampo.getLlaveTabla());
			newField.setCampoDTO(iCampo);
			document.getCaracteristicas().add(newField);
		}
		return document;
	}

	private DocumentoPlantillaDTO findTemplate(String template, String token) throws ServerException {
		DocumentoPlantillaDTO templateDTO = plantillaService.consultarPorCodigo(template);
		if (templateDTO == null)
			throw new ServerException("La plantilla no se encuentra por el codigo " + template);
		return plantillaService.obtenerCampos(templateDTO, token);
	}

	private List<DetallePedidoVentaDTO> assignateValueToProducts(List<ProductVO> products) throws ServerException {
		if (products == null || products.isEmpty())
			return null;
		List<DetallePedidoVentaDTO> result = new ArrayList<>();
		for (ProductVO iProductVO : products) {
			if(iProductVO.getCode()==null) throw new ServerException("Es necesario colocar el codigo del producto");
			DetallePedidoVentaDTO item = new DetallePedidoVentaDTO();
			item.setCantidad(iProductVO.getTotalQuantity());
			item.setCantidadTotal(iProductVO.getTotalQuantity());
			item.setProductoCodigo(iProductVO.getCode());
			item.setValorTotal(iProductVO.getTotalValue());
			if(iProductVO.getTotalValue()!=null) item.setValorUnitario(item.getValorTotal().divide(item.getCantidad(), 6, RoundingMode.CEILING));
			result.add(item);
		}
		// Consulto las propiedades de los productos
		List<ProductoDTO> productos = new ArrayList<>();
		for (DetallePedidoVentaDTO detalle : result) {
			ProductoDTO newProduct = productoService.filtrarPorCodigo(detalle.getProductoCodigo());
			if(newProduct==null) throw new ServerException("No se identifica un producto con el codigo " + detalle.getProductoCodigo());
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
					break;
				}
			}
			detallePedidoVentaService.createFieldsProduct(detalle);
		}
		
		return result;
	}
}
