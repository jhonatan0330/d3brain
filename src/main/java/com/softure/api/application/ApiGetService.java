package com.softure.api.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.api.domain.DocumentResponse;
import com.softure.api.domain.FieldRequest;
import com.softure.api.domain.FieldResponse;
import com.softure.api.domain.DocumentFilterRequest;
import com.softure.document_execution.application.CallDocumentListFromFieldProcess;
import com.softure.document_execution.application.CallDocumentListWithFilters;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaFilterDTO;
import com.softure.java.dto.exception.ServerException;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;

@Service
public class ApiGetService {

	@Autowired private CallDocumentListWithFilters listService;
	@Autowired private CallDocumentListFromFieldProcess listDocumentFromFieldProcessFunction;
	@Autowired private DocumentoPlantillaSvc templateService;
	@Autowired private PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService;
	
	public List<DocumentResponse> call(String token, DocumentFilterRequest filter) throws ServerException {
		
		if(token==null || token.isEmpty()) throw new ServerException("Es obligatorio enviar un token valido");
		DocumentoPlantillaDTO templateBD = templateService.consultarPorCodigo(filter.getTemplate());
		if(templateBD==null) throw new ServerException("No se encontro una plantilla con el codigo " + filter.getTemplate());
		templateBD = templateService.obtenerCampos(templateBD, token);
		PedidoVentaFilterDTO filterDTO = new PedidoVentaFilterDTO();
		filterDTO.setSecurityToken(token);
		if(filter.getId()==null) {
			filterDTO.setPlantilla(templateBD.getLlaveTabla());
			filterDTO.setNombre(filter.getCode());
			filterDTO.setEstado(filter.getActive());
			filterDTO.setPaginacionRegistroInicial(filter.getPage()*filter.getSize());
			filterDTO.setPaginacionRegistroFinal((filter.getPage()+1)*filter.getSize());
			filterDTO.setFechaMin(filter.getDateMin());
			filterDTO.setFechaMax(filter.getDateMax());
			filterDTO.setFechaRegistroMin(filter.getCreationDateMin());
			filterDTO.setFechaRegistroMax(filter.getCreationDateMax());
			if(filter.getStates()!=null && !filter.getStates().isEmpty()) {
				filterDTO.setEstadoExpediente( String.join(";", filter.getStates()) );
			}
			if(filter.getFilters()!=null && !filter.getFilters().isEmpty()) {
				filterDTO.setFiltersByFields(new ArrayList<>());
				for (FieldRequest iField : filter.getFilters()) {
					filterDTO.getFiltersByFields().add(getFieldValue(token, iField, templateBD));
				}
			}
		} else {
			filterDTO.setLlaveTabla(filter.getId());
		}
		List<PedidoVentaDTO> results = listService.listarAvanzado(filterDTO); 
		return transformPedidoVentaToDocument(results, token, templateBD);
	}

	private List<DocumentResponse> transformPedidoVentaToDocument(List<PedidoVentaDTO> results, String token, DocumentoPlantillaDTO template) throws ServerException {
		List<DocumentResponse> documents = new ArrayList<>();
		if(results==null) return documents;
		for (PedidoVentaDTO pedidoVentaDTO : results) {
			//pedidoVentaDTO = documentService.consultaXIdConDinero(pedidoVentaDTO.getLlaveTabla());
			pedidoVentaDTO.setCaracteristicas( pedidoVentaCaracteristicaService.listar2Documento(pedidoVentaDTO.getLlaveTabla(), pedidoVentaDTO.getHistorico()));
			for (PedidoVentaCaracteristicaDTO field : pedidoVentaDTO.getCaracteristicas()){
				for (DocumentoPlantillaCaracteristicaDTO fieldTemplate : template.getCaracteristicas()){
					if(field.getCampo().compareTo(fieldTemplate.getLlaveTabla())==0){
						field.setCampoDTO(fieldTemplate);
						break;
					}
				}
			}
			//pedidoVentaDTO = documentService.consultaCompleta(pedidoVentaDTO.getLlaveTabla(), token);
			DocumentResponse document = new DocumentResponse();
			document.setTemplate(pedidoVentaDTO.getPlantilla());
			document.setId(pedidoVentaDTO.getLlaveTabla());
			document.setCode(pedidoVentaDTO.getNombre());
			document.setActive(pedidoVentaDTO.getEstado());
			document.setStateId(pedidoVentaDTO.getEstadoExpediente());
			document.setStateName(pedidoVentaDTO.getEstadoNombre());
			document.setFields(generateFields(pedidoVentaDTO.getCaracteristicas()));
			documents.add(document);
		}
		return documents;
	}

	private List<FieldResponse> generateFields(List<PedidoVentaCaracteristicaDTO> caracteristicas) {
		if(caracteristicas==null || caracteristicas.isEmpty()) return null;
		List<FieldResponse> fields = new ArrayList<>();
		for (PedidoVentaCaracteristicaDTO iField: caracteristicas) {
			if(iField.getValorText()!=null && iField.getCampoDTO()!=null) {
				FieldResponse field = new FieldResponse();
				field.setField(iField.getCampoDTO().getNombre());
				field.setValue(iField.getValorText());
				// field.setId(iField.getValorOpcion());
				fields.add(field);	
			}
		}
		return fields;
	}
	
	private PedidoVentaCaracteristicaFilterDTO getFieldValue(String token, FieldRequest fieldRequest, DocumentoPlantillaDTO template) throws ServerException {
		if(fieldRequest.getField()==null || fieldRequest.getField().isEmpty()) throw new ServerException("Existe un campo sin Field");
		if(fieldRequest.getValue()==null || fieldRequest.getValue().isEmpty()) throw new ServerException("El campo " + fieldRequest.getField()  + "no tienen value");
		PedidoVentaCaracteristicaFilterDTO result = new PedidoVentaCaracteristicaFilterDTO();
		for (DocumentoPlantillaCaracteristicaDTO fieldTemplate : template.getCaracteristicas()){
			if(fieldRequest.getField().compareTo(fieldTemplate.getCodigo())==0){
				result.setCampoDTO(fieldTemplate);
				result.setCampo(fieldTemplate.getLlaveTabla());
				//Esto se hizo para las cargas masivas en caso que llegue un valor texto intentamos consultarlo
				// especialmente se hizo para los dependientes
				//Esta cpopiado en varias partes miestras analizo como colocarlo en alguna funcion
				PedidoVentaCaracteristicaFilterDTO filter = new PedidoVentaCaracteristicaFilterDTO();
				filter.setCampo(fieldTemplate.getLlaveTabla());
				filter.setCampoDTO(fieldTemplate);
				filter.setSecurityToken(token);
				//filter.setDependientes(pCampo.getDependientes());
				filter.setFiltroParametro(fieldRequest.getValue());
				PedidoVentaCaracteristicaFilterDTO resultField = listDocumentFromFieldProcessFunction.execute(filter, fieldTemplate);
				if(resultField == null || resultField.getCampoDTO()==null || resultField.getCampoDTO().getDocumentos() ==null || resultField.getCampoDTO().getDocumentos().isEmpty()) 
					throw new ServerException("Revisando el campo " + fieldTemplate.getNombre() +" No se encuentra el documento con codigo : " + fieldRequest.getValue());
				if(resultField.getCampoDTO().getDocumentos().size()>1)
					throw new ServerException("El campo " + fieldTemplate.getNombre() +" obtiene " + result.getCampoDTO().getDocumentos().size() +" resultados que concuerdan con el criterio : " + fieldRequest.getValue());
				result.setValorOpcion(resultField.getCampoDTO().getDocumentos().get(0).getLlaveTabla());
				break;
			}
		}
		return result;
	}
}
