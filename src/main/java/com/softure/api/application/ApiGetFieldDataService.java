package com.softure.api.application;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.api.domain.FieldResponse;
import com.softure.api.domain.DataFieldRequest;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import com.softure.java.dto.exception.ServerException;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;

@Service
public class ApiGetFieldDataService {
	
	@Autowired private DocumentoPlantillaSvc templateService;
	@Autowired private PedidoVentaCaracteristicaSvc fieldService;

	public FieldResponse call(String token, DataFieldRequest filter) throws ServerException {
		validateFilter(token, filter);
		DocumentoPlantillaDTO templateBD = findTemplate(filter.getTemplate(), token);
		DocumentoPlantillaCaracteristicaDTO fieldBD = findField(filter.getCode(), templateBD);
		
		PedidoVentaCaracteristicaFilterDTO fieldFilter = new PedidoVentaCaracteristicaFilterDTO();
		fieldFilter.setCampo(fieldBD.getLlaveTabla());
		fieldFilter.setCampoDTO(fieldBD);
		if(filter.getPreconditions()!=null && !filter.getPreconditions().isEmpty()) {
			fieldFilter.setDependientes(new ArrayList<>());
			for (FieldResponse iPrecondition : filter.getPreconditions()) {
				DocumentoPlantillaCaracteristicaDTO fieldDependent = findField(iPrecondition.getField(), templateBD);
				PedidoVentaCaracteristicaDTO dependent = new PedidoVentaCaracteristicaDTO();
				dependent.setCampo(fieldDependent.getLlaveTabla());
				dependent.setCampoDTO(fieldDependent);
				fieldFilter.getDependientes().add(dependent);
			}
		}
		fieldService.completarDatosBase(fieldFilter);
		return null;
	}
	
	private DocumentoPlantillaCaracteristicaDTO findField(String code, DocumentoPlantillaDTO templateBD) throws ServerException {
		if(templateBD.getCaracteristicas()!=null && !templateBD.getCaracteristicas().isEmpty()) {
			for (DocumentoPlantillaCaracteristicaDTO iField : templateBD.getCaracteristicas()) {
				if(iField.getCodigo().compareTo(code)==0) return iField;
			}
		}
		throw new ServerException("No se identifica un campo de codigo " + code + " en la plantilla " + templateBD.getNombre());
	}

	private void validateFilter(String token, DataFieldRequest filter) throws ServerException {
		if(token==null || token.isEmpty()) throw new ServerException("Es obligatorio enviar un token valido");
		if (filter.getTemplate() == null || filter.getTemplate().isEmpty())
			throw new ServerException("El codigo de la plantilla es null, recuerda usar el campo template");
		if (filter.getCode() == null || filter.getCode().isEmpty())
			throw new ServerException("El codigo del campo es null, recuerda usar el campo code para colocar el codigo del campo a consultar");
	}

	private DocumentoPlantillaDTO findTemplate(String template, String token) throws ServerException {
		DocumentoPlantillaDTO templateDTO = templateService.consultarPorCodigo(template);
		if (templateDTO == null)
			throw new ServerException("La plantilla no se encuentra por el codigo " + template);
		return templateService.obtenerCampos(templateDTO, token);
	}

}
