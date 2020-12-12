package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.util.Date;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.DocumentoTransaccionDTO;
import com.softure.logisticpymes.dto.filter.DocumentoTransaccionFilterDTO;
import com.softure.logisticpymes.persistence.DocumentoTransaccionMapper;

@Service("documentoTransaccionService")
public class DocumentoTransaccionSvc extends BasicSvc<DocumentoTransaccionDTO, DocumentoTransaccionFilterDTO> {
	
	@Autowired
	private DocumentoTransaccionMapper documentoTransaccionMapper;
	
	// BEGIN region servicesDocumentoTransaccion
	@Autowired private UsuarioSesionSvc sesionSvc;
	// END region servicesDocumentoTransaccion

	@Override
	public DocumentoTransaccionDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. DocumentoTransaccion");
		DocumentoTransaccionFilterDTO dto = new DocumentoTransaccionFilterDTO();
		dto.setLlaveTabla(llave);
		return documentoTransaccionMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = documentoTransaccionMapper;
	}
	
	@Override
	public DocumentoTransaccionDTO activar(DocumentoTransaccionDTO dto, String token) throws ServerException {
		// BEGIN DocumentoTransaccion_activar
		return super.activar(dto, token);
		// END DocumentoTransaccion_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DocumentoTransaccionDTO actualizar( DocumentoTransaccionDTO dto, String token) throws ServerException {
		// BEGIN DocumentoTransaccion_actualizar
		return super.actualizar(dto, token);
		// END DocumentoTransaccion_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DocumentoTransaccionDTO inactivar(DocumentoTransaccionDTO dto, String token) throws ServerException {
		// BEGIN DocumentoTransaccion_inactivar
		return super.inactivar(dto, token);
		// END DocumentoTransaccion_inactivar
	}
	
	@Override
	public DocumentoTransaccionDTO consultaUnica(DocumentoTransaccionFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(DocumentoTransaccionFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<DocumentoTransaccionDTO> listarConsulta(DocumentoTransaccionFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DocumentoTransaccionDTO guardar(DocumentoTransaccionDTO dto, String token) throws ServerException {
		// BEGIN DocumentoTransaccion_guardar
		return super.guardar(dto, token);
		// END DocumentoTransaccion_guardar
	}

// BEGIN region aditionalMethods
	public String crear(String token) throws ServerException {
		DocumentoTransaccionDTO nuevo = new DocumentoTransaccionDTO();
		nuevo.setUsuario(sesionSvc.actualizarSesion(token));
		nuevo.setFecha(new Date());
		return save(nuevo).getLlaveTabla();
	}
// END region aditionalMethods

}