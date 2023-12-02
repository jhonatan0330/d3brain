package com.softure.api.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.shared.domain.SharedIdResponse;
import com.softure.api.domain.DocumentRequest;
import com.softure.api.domain.FieldRequest;
import com.softure.document_execution.application.CallDocumentCRUD;
import com.softure.document_execution.application.DetallePedidoVentaSvc;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.inventory.application.ProductoSvc;
import com.softure.process_form.application.CallSearchProcessFromText;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;

@Service
public class ApiSendService {

	@Autowired DocumentoPlantillaSvc plantillaService;
	@Autowired CallDocumentCRUD saveDocumentService;
	
	@Autowired private DetallePedidoVentaSvc detallePedidoVentaService;
	@Autowired private ProductoSvc productoService;
	@Autowired private CallSearchProcessFromText searchProcessFromText;

	public SharedIdResponse call(String token, DocumentRequest item) throws ServerException {
		validateItem(item);
		// Con el codigo de la plantilla consultar la plantilla completa
		DocumentoPlantillaDTO template = findTemplate(item.getTemplate(), token);
		// crear el documento con todos los campos vacios
		PedidoVentaDTO document = createDocument(template);
		// Por cada campo con el codigo del campo colocar
		assignateValue(document, item.getFields(), token);
		// Envio a guardar el documento
		document = saveDocumentService.save(document, token, null);
		return new SharedIdResponse(document.getLlaveTabla(), document.getNombre());
	}

	private void validateItem(DocumentRequest item) throws ServerException {
		if (item.getTemplate() == null || item.getTemplate().isEmpty())
			throw new ServerException("El codigo de la plantilla es null, recuerda usar el campo template");
		if (item.getFields() == null)
			throw new ServerException("El documento no tiene campos, recuerda usar el tag fields");
		for (FieldRequest fieldVO : item.getFields()) {
			if (fieldVO.getField() == null)
				throw new ServerException("Existe un campo " + fieldVO.getField()
						+ " que el valor FIELD ES VACIO, si no se envia valor no es necesario colocar el campo");
			if (fieldVO.getValue() == null && fieldVO.getProducts() == null)
				throw new ServerException("Existe un campo " + fieldVO.getField()
						+ " que no registra valores, si no se envia valor no es necesario colocar el campo");
		}
	}

	private void assignateValue(PedidoVentaDTO document, List<FieldRequest> fields, String token) throws ServerException {
		if (fields == null || fields.isEmpty())
			return;
		for (FieldRequest fieldVO : fields) {
			for (PedidoVentaCaracteristicaDTO iCampo : document.getCaracteristicas()) {
				if (iCampo.getCampoDTO().getCodigo().compareTo(fieldVO.getField()) == 0) {
					ApiCommon.chooseValueToField(fieldVO, iCampo, productoService, detallePedidoVentaService);
					if (iCampo.getValorOpcion()==null && iCampo.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.PROCESO) == 0) {
						if(fieldVO.getParentDocument()!=null) {
							String keyExists =  searchProcessFromText.findOptionFromText(token, iCampo.getValorText(), iCampo.getCampoDTO());
							if(keyExists ==null) {
								SharedIdResponse responseId =  call(token, fieldVO.getParentDocument());
								iCampo.setValorOpcion(responseId.getId());
							}
						}
					}
				}
			}
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

}
