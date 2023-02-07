package com.softure.api.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.api.domain.DocumentFilterRequest;
import com.softure.api.domain.DocumentResponse;
import com.softure.api.domain.FieldRequest;
import com.softure.document_execution.application.CallDocumentListFromFieldProcess;
import com.softure.document_execution.application.CallDocumentListWithFilters;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaFilterDTO;
import com.softure.java.cons.ConstantesGenerales;
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
			if(filter.getActive()==null) filter.setActive(ConstantesGenerales.ESTADO_ACTIVO);
			filterDTO.setEstado(filter.getActive());
			filterDTO.setPaginacionRegistroInicial(filter.getPage()*filter.getSize());
			filterDTO.setPaginacionRegistroFinal((filter.getPage()+1)*filter.getSize());
			filterDTO.setFechaMin(filter.getDateMin());
			filterDTO.setFechaMax(filter.getDateMax());
			filterDTO.setFiltroParametro(filter.getFilterText());
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
		return ApiCommon.transformPedidoVentaToDocument(token, pedidoVentaCaracteristicaService, results,  templateBD);
	}

	
	
	private PedidoVentaCaracteristicaFilterDTO getFieldValue(String token, FieldRequest fieldRequest, DocumentoPlantillaDTO template) throws ServerException {
		if(fieldRequest.getField()==null || fieldRequest.getField().isEmpty()) throw new ServerException("Existe un campo sin Field");
		if(fieldRequest.getValue()==null || fieldRequest.getValue().isEmpty()) throw new ServerException("El campo " + fieldRequest.getField()  + "no tienen value");
		PedidoVentaCaracteristicaFilterDTO result = new PedidoVentaCaracteristicaFilterDTO();
		for (DocumentoPlantillaCaracteristicaDTO fieldTemplate : template.getCaracteristicas()){
			if(fieldRequest.getField().compareTo(fieldTemplate.getCodigo())==0){
				result.setCampoDTO(fieldTemplate);
				result.setCampo(fieldTemplate.getLlaveTabla());
				if(fieldTemplate.getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.PROCESO)==0)
					result.setValorOpcion(ApiCommon.getValueOpctionFromText(token, listDocumentFromFieldProcessFunction, fieldRequest.getValue(), fieldTemplate));
				break;
			}
		}
		return result;
	}


}
