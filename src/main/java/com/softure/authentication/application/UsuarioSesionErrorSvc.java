package com.softure.authentication.application;

import java.util.List;

// BEGIN region interImport
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.authentication.domain.UsuarioSesionErrorDTO;
import com.softure.authentication.domain.UsuarioSesionErrorFilterDTO;
import com.softure.authentication.infrastructure.UsuarioSesionErrorMapper;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;

@Service("usuarioSesionErrorService")
public class UsuarioSesionErrorSvc extends BasicSvc<UsuarioSesionErrorDTO, UsuarioSesionErrorFilterDTO> {
	
	@Autowired
	private UsuarioSesionErrorMapper usuarioSesionErrorMapper;
	
	// BEGIN region servicesUsuarioSesionError
	// END region servicesUsuarioSesionError

	@Override
	public UsuarioSesionErrorDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. UsuarioSesionError");
		UsuarioSesionErrorFilterDTO dto = new UsuarioSesionErrorFilterDTO();
		dto.setLlaveTabla(llave);
		return usuarioSesionErrorMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = usuarioSesionErrorMapper;
	}
	
	@Override
	public UsuarioSesionErrorDTO activar(UsuarioSesionErrorDTO dto, String token) throws ServerException {
		// BEGIN UsuarioSesionError_activar
		return super.activar(dto, token);
		// END UsuarioSesionError_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioSesionErrorDTO actualizar( UsuarioSesionErrorDTO dto, String token) throws ServerException {
		// BEGIN UsuarioSesionError_actualizar
		return super.actualizar(dto, token);
		// END UsuarioSesionError_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioSesionErrorDTO inactivar(UsuarioSesionErrorDTO dto, String token) throws ServerException {
		// BEGIN UsuarioSesionError_inactivar
		return super.inactivar(dto, token);
		// END UsuarioSesionError_inactivar
	}
	
	@Override
	public UsuarioSesionErrorDTO consultaUnica(UsuarioSesionErrorFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(UsuarioSesionErrorFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<UsuarioSesionErrorDTO> listarConsulta(UsuarioSesionErrorFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioSesionErrorDTO guardar(UsuarioSesionErrorDTO dto, String token) throws ServerException {
		// BEGIN UsuarioSesionError_guardar
		return super.guardar(dto, token);
		// END UsuarioSesionError_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}