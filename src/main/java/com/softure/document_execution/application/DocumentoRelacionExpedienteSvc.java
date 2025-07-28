package com.softure.document_execution.application;

import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.document_execution.domain.DocumentoRelacionExpedienteDTO;
import com.softure.document_execution.domain.DocumentoRelacionExpedienteFilterDTO;
import com.softure.document_execution.infrastructure.DocumentoRelacionExpedienteMapper;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.logisticpymes.application.BasicSvc;

import jakarta.annotation.PostConstruct;

@Service("documentoRelacionExpedienteService")
public class DocumentoRelacionExpedienteSvc extends BasicSvc<DocumentoRelacionExpedienteDTO, DocumentoRelacionExpedienteFilterDTO> {
	
	@Autowired @Lazy 
	private DocumentoRelacionExpedienteMapper documentoRelacionExpedienteMapper;
	

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
		return super.activar(dto, token);
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DocumentoRelacionExpedienteDTO actualizar( DocumentoRelacionExpedienteDTO dto, String token) throws ServerException {
		return super.actualizar(dto, token);
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DocumentoRelacionExpedienteDTO inactivar(DocumentoRelacionExpedienteDTO dto, String token) throws ServerException {
		dto.setEstado(SharedConstants.STATE_INACTIVE);
		return super.actualizar(dto, token);
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
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DocumentoRelacionExpedienteDTO guardar(DocumentoRelacionExpedienteDTO dto, String token) throws ServerException {
		return super.guardar(dto, token);
	}

	public List<DocumentoRelacionExpedienteDTO> listarHeredados(String plantilla, String campoMaestro, String llaveOpcion, String plantillaTransicion, List<String> plantillasGestionadas) throws ServerException {
		if(plantillasGestionadas!=null && !plantillasGestionadas.isEmpty()) {
			for (String iPlantilla : plantillasGestionadas) {
				if(iPlantilla.compareTo(plantilla)==0) return new ArrayList<DocumentoRelacionExpedienteDTO>();
			}
		}
		return documentoRelacionExpedienteMapper.listarHeredados(plantilla, campoMaestro, llaveOpcion, plantillaTransicion);
	}
	
	public List<DocumentoRelacionExpedienteDTO> listByField(String field)
			throws ServerException {
		DocumentoRelacionExpedienteFilterDTO filter = new DocumentoRelacionExpedienteFilterDTO();
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		filter.setCampoMaestro(field);
		filter.setPaginacionRegistroFinal(5000);
		return listarConsulta(filter);
	}
	
	public boolean relacionarExpedienteDocumento(String pFieldId, String pProcessId,
			String token, String pFieldName, BigDecimal pProcessValue, String pKeyDocumentRelation) throws ServerException {
		if (pProcessId == null)
			throw new ServerException(
					"Por favor valida el motivo por el cual no se identifica la llave del expediente en el campo "
							+ pFieldName);
		// Creo una relacion entre el campo y los pedidos detalles, primero reviso si
		// existe
		DocumentoRelacionExpedienteFilterDTO _filter = new DocumentoRelacionExpedienteFilterDTO();
		_filter.setCampoMaestro(pFieldId);
		_filter.setExpedienteDetalle(pProcessId);
		_filter.setEstado(SharedConstants.STATE_ACTIVE);
		DocumentoRelacionExpedienteDTO _relation = consultaUnica(_filter);
		if (_relation == null) {
			_relation = new DocumentoRelacionExpedienteDTO();
			_relation.setCampoMaestro(pFieldId);
			_relation.setExpedienteDetalle(pProcessId);
			_relation.setValor(pProcessValue);
			//if (procesoDTO.getDinero() != null)
			//	docExpediente.setValor(procesoDTO.getDinero().getSaldo());
			_relation.setDocumentoRegistro(pKeyDocumentRelation);
			_relation = guardar(_relation, token);
			return true;
		}
		return false;
	}

// END region aditionalMethods

}