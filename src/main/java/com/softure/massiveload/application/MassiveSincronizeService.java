package com.softure.massiveload.application;


// Start of user code imports
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shared.domain.SharedIdResponse;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.java.dto.exception.ServerException;
import com.softure.massiveload.domain.MassiveMasterDTO;
import com.softure.massiveload.domain.MassiveItemDTO;

@Service
public class MassiveSincronizeService {

	@Autowired private MassiveCRUDMasterService cargaMasivaService;
	@Autowired private MassiveCRUDItemService cargaMasivaItemService;
	
	public SharedIdResponse call(String token, String fileUrl, String template) throws ServerException {
		MassiveMasterDTO newLoadMassive = new MassiveMasterDTO();
		newLoadMassive.setArchivo(fileUrl);
		newLoadMassive.setPlantilla(template);
		List<PedidoVentaDTO> documents = generateDocuments(fileUrl, template);
		newLoadMassive.setCreatedUser(token); // TODO : Colcoar el usuario
		if(documents==null || documents.isEmpty()) {
			newLoadMassive.setState(MassiveMasterDTO.ERROR);
			newLoadMassive.setMensaje("No se generaron items para cargar");
			newLoadMassive.setFecha(new Date());
			newLoadMassive = cargaMasivaService.saveAndFindById(newLoadMassive);	
		}else {
			newLoadMassive = cargaMasivaService.saveAndFindById(newLoadMassive);
			saveItems(token, newLoadMassive.getKey(), documents);
			newLoadMassive.setState(MassiveMasterDTO.SERIALIZADA);
		}
		return new SharedIdResponse(newLoadMassive.getKey());
	}

	private List<PedidoVentaDTO> generateDocuments(String fileUrl, String template) {
		return null;
	}
	
	private void saveItems(String token, String loadId, List<PedidoVentaDTO> documents) throws ServerException {
		for (PedidoVentaDTO pedidoVentaDTO : documents) {
			var item = new MassiveItemDTO();
			item.setCarga(loadId);
			item.setFechaSerializacion(new Date());
			ObjectMapper mapper = new ObjectMapper();
			try {
			  String json = mapper.writeValueAsString(pedidoVentaDTO);
			  item.setModelo(json);
			  item.setProgreso(MassiveItemDTO.SERIALIZADA);
			} catch (JsonProcessingException e) {
			  item.setNombre(loadId);
			  item.setProgreso(MassiveItemDTO.ERROR);
			}
			item.setCreatedUser(token);// TODO hay que colcoar el usuario correcto
			cargaMasivaItemService.save(item);	
		}
	}

}

