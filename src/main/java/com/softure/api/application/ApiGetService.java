package com.softure.api.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.api.domain.DocumentVO;
import com.softure.api.domain.FieldVO;
import com.softure.api.domain.FilterDocumentVO;
import com.softure.document_execution.application.CallDocumentListWithFilters;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaFilterDTO;
import com.softure.java.dto.exception.ServerException;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;

@Service
public class ApiGetService {

	@Autowired CallDocumentListWithFilters listService;
	@Autowired DocumentoPlantillaSvc templateService;
	@Autowired PedidoVentaSvc documentService;
	@Autowired private PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService;
	
	public List<DocumentVO> call(String token, FilterDocumentVO filter) throws ServerException {
		
		DocumentoPlantillaDTO templateBD = templateService.consultarPorCodigo(filter.getTemplate());
		if(templateBD==null) throw new ServerException("No se encontro una plantilla con el codigo " + filter.getTemplate());
		if(token==null || token.isEmpty()) throw new ServerException("Es obligatorio enviar un token valido");
		PedidoVentaFilterDTO filterDTO = new PedidoVentaFilterDTO();
		filterDTO.setPlantilla(templateBD.getLlaveTabla());
		filterDTO.setSecurityToken(token);
		filterDTO.setNombre(filter.getCode());
		filterDTO.setEstado(filter.getActive());
		filterDTO.setPaginacionRegistroInicial(filter.getPage()*filter.getSize());
		filterDTO.setPaginacionRegistroFinal((filter.getPage()+1)*filter.getSize());
		if(filter.getStates()!=null && !filter.getStates().isEmpty()) {
			filterDTO.setEstadoExpediente( String.join(";", filter.getStates()) );
		}
		List<PedidoVentaDTO> results = listService.listarAvanzado(filterDTO); 
		return transformPedidoVentaToDocument(results, token, templateBD);
	}

	private List<DocumentVO> transformPedidoVentaToDocument(List<PedidoVentaDTO> results, String token, DocumentoPlantillaDTO template) throws ServerException {
		List<DocumentVO> documents = new ArrayList<>();
		if(results==null) return documents;
		template = templateService.obtenerCampos(template, token);
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
			DocumentVO document = new DocumentVO();
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

	private List<FieldVO> generateFields(List<PedidoVentaCaracteristicaDTO> caracteristicas) {
		if(caracteristicas==null || caracteristicas.isEmpty()) return null;
		List<FieldVO> fields = new ArrayList<>();
		for (PedidoVentaCaracteristicaDTO iField: caracteristicas) {
			if(iField.getValorText()!=null && iField.getCampoDTO()!=null) {
				FieldVO field = new FieldVO();
				field.setField(iField.getCampoDTO().getNombre());
				field.setValue(iField.getValorText());
				field.setId(iField.getValorOpcion());
				fields.add(field);	
			}
		}
		return fields;
	}
}
