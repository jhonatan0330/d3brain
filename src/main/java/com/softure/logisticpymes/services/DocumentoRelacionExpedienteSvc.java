package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.util.ArrayList;
import com.softure.java.cons.ConstantesGenerales;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.DocumentoRelacionExpedienteDTO;
import com.softure.logisticpymes.dto.filter.DocumentoRelacionExpedienteFilterDTO;
import com.softure.logisticpymes.persistence.DocumentoRelacionExpedienteMapper;

@Service("documentoRelacionExpedienteService")
public class DocumentoRelacionExpedienteSvc extends BasicSvc<DocumentoRelacionExpedienteDTO, DocumentoRelacionExpedienteFilterDTO> {
	
	@Autowired
	private DocumentoRelacionExpedienteMapper documentoRelacionExpedienteMapper;
	
	// BEGIN region servicesDocumentoRelacionExpediente
	// END region servicesDocumentoRelacionExpediente

	@Override
	public DocumentoRelacionExpedienteDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. DocumentoRelacionExpediente");
		DocumentoRelacionExpedienteFilterDTO dto = new DocumentoRelacionExpedienteFilterDTO();
		dto.setLlaveTabla(llave);
		return documentoRelacionExpedienteMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = documentoRelacionExpedienteMapper;
	}
	
	@Override
	public DocumentoRelacionExpedienteDTO activar(DocumentoRelacionExpedienteDTO dto, String token) throws ServerException {
		// BEGIN DocumentoRelacionExpediente_activar
		return super.activar(dto, token);
		// END DocumentoRelacionExpediente_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DocumentoRelacionExpedienteDTO actualizar( DocumentoRelacionExpedienteDTO dto, String token) throws ServerException {
		// BEGIN DocumentoRelacionExpediente_actualizar
		return super.actualizar(dto, token);
		// END DocumentoRelacionExpediente_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DocumentoRelacionExpedienteDTO inactivar(DocumentoRelacionExpedienteDTO dto, String token) throws ServerException {
		// BEGIN DocumentoRelacionExpediente_inactivar
		dto.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
		return super.actualizar(dto, token);
		// END DocumentoRelacionExpediente_inactivar
	}
	
	@Override
	public DocumentoRelacionExpedienteDTO consultaUnica(DocumentoRelacionExpedienteFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(DocumentoRelacionExpedienteFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<DocumentoRelacionExpedienteDTO> listarConsulta(DocumentoRelacionExpedienteFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DocumentoRelacionExpedienteDTO guardar(DocumentoRelacionExpedienteDTO dto, String token) throws ServerException {
		// BEGIN DocumentoRelacionExpediente_guardar
		return super.guardar(dto, token);
		// END DocumentoRelacionExpediente_guardar
	}

// BEGIN region aditionalMethods
	public List<DocumentoRelacionExpedienteDTO> listarHeredados(String plantilla, String campoMaestro, String llaveOpcion, String plantillaTransicion, List<String> plantillasGestionadas) throws ServerException {
		if(plantillasGestionadas!=null && !plantillasGestionadas.isEmpty()) {
			for (String iPlantilla : plantillasGestionadas) {
				if(iPlantilla.compareTo(plantilla)==0) return new ArrayList<DocumentoRelacionExpedienteDTO>();
			}
		}
		return documentoRelacionExpedienteMapper.listarHeredados(plantilla, campoMaestro, llaveOpcion, plantillaTransicion);
	}
// END region aditionalMethods

}