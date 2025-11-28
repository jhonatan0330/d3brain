package com.softure.authorization.application;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.CacheManager;
import com.softure.authentication.application.UsuarioAutenticacionSvc;
import com.softure.authorization.domain.UsuarioRolDTO;
import com.softure.authorization.domain.UsuarioRolFilterDTO;
import com.softure.authorization.infrastructure.UsuarioRolMapper;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.logisticpymes.application.UsuarioSvc;
import com.softure.logisticpymes.domain.UsuarioDTO;

import jakarta.annotation.PostConstruct;

@Service("usuarioRolService")
public class UsuarioRolSvc extends BasicSvc<UsuarioRolDTO, UsuarioRolFilterDTO> {
	
	@Autowired @Lazy 
	private UsuarioRolMapper usuarioRolMapper;
	
	@Autowired @Lazy private UsuarioSvc usuarioService;
	@Autowired @Lazy private UsuarioAutenticacionSvc autenticacionService;
	@Autowired @Lazy private CacheManager cacheService;

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
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioRolDTO actualizar( UsuarioRolDTO dto, String token) throws ServerException {
		throw new ServerException("Lo que se debe actualizar es el documento");
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioRolDTO inactivar(UsuarioRolDTO dto, String token) throws ServerException {
		dto.setFechaFinal(new Date());
		dto.setEstado(SharedConstants.STATE_INACTIVE);
		dto= super.update(dto);
		UsuarioRolFilterDTO filtro = new UsuarioRolFilterDTO();
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		filtro.setUsuario(dto.getUsuario());
		int cont = contarResultados(filtro);
		if(cont==0) {
			UsuarioDTO usuario = usuarioService.consultaXId(dto.getUsuario());
			if(usuario.getEstado().compareTo(SharedConstants.STATE_INACTIVE)!=0)
				usuarioService.inactivar(usuario, token);
		}
		cacheService.clearUserRoleMap();
		return dto;
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioRolDTO guardar(UsuarioRolDTO dto, String token) throws ServerException {
		dto.setFechaInicial(new Date());
		dto = super.guardar(dto, token);
		autenticacionService.crearAutenticacion(dto.getUsuario(), token);
		cacheService.clearUserRoleMap();
		return dto;
	}
	

}