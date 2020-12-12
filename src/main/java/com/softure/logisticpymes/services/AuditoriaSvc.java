package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.AuditoriaDTO;
import com.softure.logisticpymes.dto.filter.AuditoriaFilterDTO;
import com.softure.logisticpymes.persistence.AuditoriaMapper;

@Service("auditoriaService")
public class AuditoriaSvc extends BasicSvc<AuditoriaDTO, AuditoriaFilterDTO> {
	
	@Autowired
	private AuditoriaMapper auditoriaMapper;
	
	// BEGIN region servicesAuditoria
	// END region servicesAuditoria

	@Override
	public AuditoriaDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. Auditoria");
		AuditoriaFilterDTO dto = new AuditoriaFilterDTO();
		dto.setLlaveTabla(llave);
		return auditoriaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = auditoriaMapper;
	}
	
	@Override
	public AuditoriaDTO activar(AuditoriaDTO dto, String token) throws ServerException {
		// BEGIN Auditoria_activar
		return super.activar(dto, token);
		// END Auditoria_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public AuditoriaDTO actualizar( AuditoriaDTO dto, String token) throws ServerException {
		// BEGIN Auditoria_actualizar
		return super.actualizar(dto, token);
		// END Auditoria_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public AuditoriaDTO inactivar(AuditoriaDTO dto, String token) throws ServerException {
		// BEGIN Auditoria_inactivar
		return super.inactivar(dto, token);
		// END Auditoria_inactivar
	}
	
	@Override
	public AuditoriaDTO consultaUnica(AuditoriaFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(AuditoriaFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<AuditoriaDTO> listarConsulta(AuditoriaFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public AuditoriaDTO guardar(AuditoriaDTO dto, String token) throws ServerException {
		// BEGIN Auditoria_guardar
		return super.guardar(dto, token);
		// END Auditoria_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}