package com.softure.api.application;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.api.domain.ApiVO;
import com.softure.api.domain.FieldVO;
import com.softure.document_execution.application.CallDocumentCRUD;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.java.domain.IdResponse;
import com.softure.java.dto.exception.ServerException;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;

@Service
public class ApiSendService implements IApiSendService {

	@Autowired DocumentoPlantillaSvc plantillaService;
	@Autowired CallDocumentCRUD saveDocumentService;
	
	@Override
	public IdResponse call(String token, ApiVO item) throws ServerException {
		validateItem(item);
		// Con el codigo de la plantilla consultar la plantilla completa
		DocumentoPlantillaDTO template = findTemplate(item.getTemplate(), token); 
		// crear el documento con todos los campos vacios
		PedidoVentaDTO document = createDocument(template);
		// Por cada campo con el codigo del campo colocar
		assignateValue(document, item.getFields());
		//Envio a guardar el documento
		document = saveDocumentService.save(document, token);
		return new IdResponse( document.getLlaveTabla());
	}

	private void validateItem(ApiVO item) throws ServerException {
		if(item.getTemplate() == null) throw new ServerException("El codigo de la plantilla es null, recuerda usar el campo template");
		if(item.getFields() == null) throw new ServerException("El documento no tiene campos, recuerda usar el tag fields");
		for (FieldVO fieldVO : item.getFields()) {
			if(fieldVO.getField()==null) throw new ServerException("Existe una campo que el valor FIELD ES VACIO, si no se envia valor no es necesario colocar el campo");
			if(fieldVO.getValue()==null) throw new ServerException("Existe una campo que el valor VALUE ES VACIO, si no se envia valor no es necesario colocar el campo");
		}
	}

	private void assignateValue(PedidoVentaDTO document, List<FieldVO> fields) throws ServerException {
		if(fields ==null || fields.isEmpty()) return;
		for (FieldVO fieldVO : fields) {
			for (PedidoVentaCaracteristicaDTO iCampo : document.getCaracteristicas()) {
				if(iCampo.getCampoDTO().getCodigo().compareTo(fieldVO.getField())==0) {
					switch (iCampo.getCampoDTO().getFormato()) {
					case DocumentoPlantillaCaracteristicaDTO.NUMERO : {
						iCampo.setValorNumero(transformNumber(fieldVO.getValue()));
						break;
					}
					case DocumentoPlantillaCaracteristicaDTO.FECHA : {
						iCampo.setValorFecha(transformDate(fieldVO.getValue()));
						break;
					}
					default:
						iCampo.setValorText(fieldVO.getValue());
					}
					break;
				}
			}
		}
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
		if(template.getCaracteristicas()==null) return document;
		document.setCaracteristicas(new ArrayList<>());
		for (DocumentoPlantillaCaracteristicaDTO iCampo : template.getCaracteristicas()) {
			PedidoVentaCaracteristicaDTO newField = new PedidoVentaCaracteristicaDTO();
			newField.setCampo(iCampo.getLlaveTabla());
			newField.setCampoDTO(iCampo);
			document.getCaracteristicas().add(newField);
		}
		return document;
	}

	private DocumentoPlantillaDTO findTemplate(String template, String token) throws ServerException{
		DocumentoPlantillaDTO templateDTO = plantillaService.consultarPorCodigo(template);
		if(templateDTO==null) throw new ServerException("La plantilla no se encuentra por el codigo " + template);
		return plantillaService.obtenerCampos(templateDTO, token);
	}

}
