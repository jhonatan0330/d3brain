package com.softure.authentication.application;

import java.util.List;

// BEGIN region interImport
import java.util.Date;

import com.softure.authentication.domain.UsuarioSesionDTO;
import com.softure.authentication.domain.UsuarioSesionFilterDTO;
import com.softure.authentication.infrastructure.UsuarioSesionMapper;
import com.softure.java.cons.ConstantesGenerales;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;

@Service("usuarioSesionService")
public class UsuarioSesionSvc extends BasicSvc<UsuarioSesionDTO, UsuarioSesionFilterDTO> {
	
	@Autowired
	private UsuarioSesionMapper usuarioSesionMapper;
	
	// BEGIN region servicesUsuarioSesion
	// END region servicesUsuarioSesion

	@Override
	public UsuarioSesionDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. UsuarioSesion");
		UsuarioSesionFilterDTO dto = new UsuarioSesionFilterDTO();
		dto.setLlaveTabla(llave);
		return usuarioSesionMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = usuarioSesionMapper;
	}
	
	@Override
	public UsuarioSesionDTO activar(UsuarioSesionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioSesion_activar
		return super.activar(dto, token);
		// END UsuarioSesion_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioSesionDTO actualizar( UsuarioSesionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioSesion_actualizar
		return super.actualizar(dto, token);
		// END UsuarioSesion_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioSesionDTO inactivar(UsuarioSesionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioSesion_inactivar
		dto.setFechaCierre(new Date());
		return super.inactivar(dto, token);
		// END UsuarioSesion_inactivar
	}
	
	@Override
	public UsuarioSesionDTO consultaUnica(UsuarioSesionFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(UsuarioSesionFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<UsuarioSesionDTO> listarConsulta(UsuarioSesionFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioSesionDTO guardar(UsuarioSesionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioSesion_guardar
		return super.save(dto);
		// END UsuarioSesion_guardar
	}

// BEGIN region aditionalMethods
	public String actualizarSesion(String token) throws ServerException {
		UsuarioSesionFilterDTO bdFilter = new UsuarioSesionFilterDTO();
		bdFilter.setSecurityToken(token);
		bdFilter.setUsuario(getUserFlex(token));
		int tiempo = usuarioSesionMapper.tiempoSesion(bdFilter.getUsuario());
		if(tiempo!=0) {
			UsuarioSesionDTO bd = consultaXId(token);
			bd.setFechaCierre(new Date(new Date().getTime() + (tiempo *60 * 1000)));
			update(bd);
			return bd.getUsuario();
		}
		return bdFilter.getUsuario();
	}
	
	public Date getFechaCierre(String usuario) throws ServerException {
		int tiempo = usuarioSesionMapper.tiempoSesion(usuario);
		if(tiempo!=0) {
			return new Date(new Date().getTime() + (tiempo *60 * 1000));
		}
		return null;
	}
	
	public UsuarioSesionDTO checkToken(String token)throws ServerException{
		UsuarioSesionDTO result = consultaXId(token);
		if(result !=null && result.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)==0  
				&& (result.getFechaCierre()== null || result.getFechaCierre().getTime() > new Date().getTime())) {
			return result;
		}
		return null;	
		
	}
// END region aditionalMethods

}