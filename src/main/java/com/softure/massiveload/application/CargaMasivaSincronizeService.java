package com.softure.massiveload.application;


// Start of user code imports
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.java.domain.IdResponse;
import com.softure.java.dto.exception.ServerException;
import com.softure.massiveload.domain.CargaMasiva;
import com.softure.massiveload.domain.CargaMasivaDTO;
import com.softure.massiveload.domain.CargaMasivaItem;
import com.softure.massiveload.domain.CargaMasivaItemDTO;

@Service
public class CargaMasivaSincronizeService implements ICargaMasivaSincronizeService {

	// Start of user code autowired
	@Autowired private ICargaMasivaService cargaMasivaService;
	@Autowired private ICargaMasivaItemService cargaMasivaItemService;
	//End of user code	
	
	@Override
	public IdResponse call(String token, String fileUrl, String template) throws ServerException {
		// Start of user code MainMethod
		CargaMasiva newLoadMassive = new CargaMasiva();
		newLoadMassive.setArchivo(fileUrl);
		newLoadMassive.setPlantilla(template);
		List<PedidoVentaDTO> documents = generateDocuments(fileUrl, template);
		if(documents==null || documents.isEmpty()) {
			newLoadMassive.setEstado(CargaMasivaDTO.ERROR);
			newLoadMassive.setMensaje("No se generaron items para cargar");
			newLoadMassive.setFecha(new Date());
			newLoadMassive = cargaMasivaService.save(token, newLoadMassive);	
		}else {
			newLoadMassive = cargaMasivaService.save(token, newLoadMassive);
			saveItems(token, newLoadMassive.getCargaMasivaId(), documents);
			newLoadMassive.setEstado(CargaMasivaDTO.SERIALIZADA);
		}
		return new IdResponse(newLoadMassive.getCargaMasivaId());
 		// End of user code		
	}

	// Start of user code private methods
	private List<PedidoVentaDTO> generateDocuments(String fileUrl, String template) {
		return null;
	}
	
	private void saveItems(String token, String loadId, List<PedidoVentaDTO> documents) throws ServerException {
		for (PedidoVentaDTO pedidoVentaDTO : documents) {
			var item = new CargaMasivaItem();
			item.setCarga(loadId);
			item.setFechaSerializacion(new Date());
			ObjectMapper mapper = new ObjectMapper();
			try {
			  String json = mapper.writeValueAsString(pedidoVentaDTO);
			  item.setModelo(json);
			  item.setProgreso(CargaMasivaItemDTO.SERIALIZADA);
			} catch (JsonProcessingException e) {
			  item.setNombre(loadId);
			  item.setProgreso(CargaMasivaItemDTO.ERROR);
			}
			cargaMasivaItemService.save(token, item);	
		}
	}
	//End of user code

}

