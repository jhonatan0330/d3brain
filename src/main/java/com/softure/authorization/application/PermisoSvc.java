package com.softure.authorization.application;

import java.util.List;

// BEGIN region interImport
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.authorization.domain.PermisoDTO;
import com.softure.authorization.domain.PermisoFilterDTO;
import com.softure.authorization.infrastructure.PermisoMapper;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;

@Service("permisoService")
public class PermisoSvc extends BasicSvc<PermisoDTO, PermisoFilterDTO> {
	
	@Autowired
	private PermisoMapper permisoMapper;
	
	// BEGIN region servicesPermiso
	// END region servicesPermiso

	@Override
	public PermisoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. Permiso");
		PermisoFilterDTO dto = new PermisoFilterDTO();
		dto.setLlaveTabla(llave);
		return permisoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = permisoMapper;
	}
	
	@Override
	public PermisoDTO activar(PermisoDTO dto, String token) throws ServerException {
		// BEGIN Permiso_activar
		return super.activar(dto, token);
		// END Permiso_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PermisoDTO actualizar( PermisoDTO dto, String token) throws ServerException {
		// BEGIN Permiso_actualizar
		return super.actualizar(dto, token);
		// END Permiso_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PermisoDTO inactivar(PermisoDTO dto, String token) throws ServerException {
		// BEGIN Permiso_inactivar
		return super.inactivar(dto, token);
		// END Permiso_inactivar
	}
	
	@Override
	public PermisoDTO consultaUnica(PermisoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(PermisoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<PermisoDTO> listarConsulta(PermisoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PermisoDTO guardar(PermisoDTO dto, String token) throws ServerException {
		// BEGIN Permiso_guardar
		return super.guardar(dto, token);
		// END Permiso_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}