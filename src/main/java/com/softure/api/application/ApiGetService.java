package com.softure.api.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.api.domain.DocumentVO;
import com.softure.api.domain.FieldVO;
import com.softure.api.domain.FilterDocumentVO;
import com.softure.document_execution.application.CallDocumentListWithFilters;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaFilterDTO;
import com.softure.java.dto.exception.ServerException;

@Service
public class ApiGetService {

	@Autowired CallDocumentListWithFilters listService;
	
	public List<DocumentVO> call(String token, FilterDocumentVO filter) throws ServerException {
		if(filter.getTemplate()==null ||filter.getTemplate().isEmpty()) throw new ServerException("Es obligatorio colocar la plantilla");
		if(filter.getTemplate()==null ||filter.getTemplate().isEmpty()) throw new ServerException("Es obligatorio enviar un token valido");
		PedidoVentaFilterDTO filterDTO = new PedidoVentaFilterDTO();
		filterDTO.setPlantilla(filter.getTemplate());
		filterDTO.setSecurityToken(token);
		filterDTO.setNombre(filter.getCode());
		filterDTO.setEstado(filter.getActive());
		filterDTO.setPaginacionRegistroInicial(filter.getPage()*filter.getSize());
		filterDTO.setPaginacionRegistroFinal((filter.getPage()+1)*filter.getSize());
		if(filter.getStates()!=null && !filter.getStates().isEmpty()) {
			filterDTO.setEstadoExpediente( String.join(";", filter.getStates()) );
		}
		List<PedidoVentaDTO> results = listService.listarAvanzado(filterDTO); 
		return transformPedidoVentaToDocument(results);
	}

	private List<DocumentVO> transformPedidoVentaToDocument(List<PedidoVentaDTO> results) {
		if(results==null || results.isEmpty()) return null;
		List<DocumentVO> documents = new ArrayList<>();
		for (PedidoVentaDTO pedidoVentaDTO : results) {
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
			FieldVO field = new FieldVO();
			field.setField(iField.getCampoDTO().getNombre());
			field.setValue(iField.getValorText());
			field.setId(iField.getValorOpcion());
			fields.add(field);
		}
		return fields;
	}
}
