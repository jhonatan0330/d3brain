package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.util.Date;

import com.softure.java.cons.ConstantesGenerales;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.domain.dto.RolAccesoDTO;
import com.softure.logisticpymes.domain.dto.UsuarioDTO;
import com.softure.logisticpymes.domain.dto.UsuarioRolDTO;
import com.softure.logisticpymes.domain.filter.UsuarioRolFilterDTO;
import com.softure.logisticpymes.infrastructure.mybatis.mapper.UsuarioRolMapper;

@Service("usuarioRolService")
public class UsuarioRolSvc extends BasicSvc<UsuarioRolDTO, UsuarioRolFilterDTO> {
	
	@Autowired
	private UsuarioRolMapper usuarioRolMapper;
	
	// BEGIN region servicesUsuarioRol
	@Autowired private UsuarioSvc usuarioService;
	@Autowired private RolAccesoSvc rolService;
	@Autowired private UsuarioAutenticacionSvc autenticacionService;
	// END region servicesUsuarioRol

	@Override
	public UsuarioRolDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. UsuarioRol");
		UsuarioRolFilterDTO dto = new UsuarioRolFilterDTO();
		dto.setLlaveTabla(llave);
		return usuarioRolMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = usuarioRolMapper;
	}
	
	@Override
	public UsuarioRolDTO activar(UsuarioRolDTO dto, String token) throws ServerException {
		// BEGIN UsuarioRol_activar
		return super.activar(dto, token);
		// END UsuarioRol_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioRolDTO actualizar( UsuarioRolDTO dto, String token) throws ServerException {
		// BEGIN UsuarioRol_actualizar
		throw new ServerException("Lo que se debe actualizar es el documento");
		// END UsuarioRol_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioRolDTO inactivar(UsuarioRolDTO dto, String token) throws ServerException {
		// BEGIN UsuarioRol_inactivar
		dto.setFechaFinal(new Date());
		dto.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
		dto= super.update(dto);
		UsuarioRolFilterDTO filtro = new UsuarioRolFilterDTO();
		filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		filtro.setUsuario(dto.getUsuario());
		int cont = contarResultados(filtro);
		if(cont==0) {
			UsuarioDTO usuario = usuarioService.consultaXId(dto.getUsuario());
			if(usuario.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO)!=0)
				usuarioService.inactivar(usuario, token);
		}
		return dto;
		// END UsuarioRol_inactivar
	}
	
	@Override
	public UsuarioRolDTO consultaUnica(UsuarioRolFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(UsuarioRolFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<UsuarioRolDTO> listarConsulta(UsuarioRolFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioRolDTO guardar(UsuarioRolDTO dto, String token) throws ServerException {
		// BEGIN UsuarioRol_guardar
		dto.setFechaInicial(new Date());
		dto = super.guardar(dto, token);
		autenticacionService.crearAutenticacion(dto.getUsuario(), token);
		return dto;
		// END UsuarioRol_guardar
	}

// BEGIN region aditionalMethods
	
	public UsuarioRolDTO consultaValidandoCaracteristicas(UsuarioRolDTO dto)throws ServerException{
		if(dto.getRolAcceso()==null)throw new ServerException("Es necesario indicar el rol");
		if(dto.getUsuario()==null)throw new ServerException("Es necesario indicar el usuario");
		RolAccesoDTO rol = rolService.consultaXId(dto.getRolAcceso());
		if(rol==null)throw new ServerException("No se identifico rol");
		if(rol.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0)throw new ServerException("Rol inactivo.\n" + rol.getNombre());
		
		UsuarioDTO usuario = usuarioService.consultaXId(dto.getUsuario());
		if(usuario==null)throw new ServerException("No se identifico usuario");
		if(usuario.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0)throw new ServerException("Usuario inactivo.\n" + usuario.getNombre());
		UsuarioRolFilterDTO filter = new UsuarioRolFilterDTO();
		filter.setRolAcceso(dto.getRolAcceso());
		filter.setUsuario(dto.getUsuario());
		filter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		dto = consultaUnica(filter);
		
		if(dto == null)throw new ServerException("No se identifico usuarioRol.\n Usuario: "+ usuario.getNombre() + ".\nRol : " + rol.getNombre());
		return dto;
	}
	
	
// END region aditionalMethods

}