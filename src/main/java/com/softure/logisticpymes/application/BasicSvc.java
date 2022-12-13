package com.softure.logisticpymes.application;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.authentication.domain.UsuarioSesionDTO;
import com.softure.authentication.domain.UsuarioSesionFilterDTO;
import com.softure.authentication.infrastructure.UsuarioSesionMapper;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.domain.BasicDTO;
import com.softure.java.domain.BasicFilterDTO;
import com.softure.java.domain.IBasicMapper;
import com.softure.java.dto.exception.ServerException;

public class BasicSvc<T extends BasicDTO, TFilter extends BasicFilterDTO> {
	
	protected IBasicMapper<T, TFilter> mapper;
	
	@Autowired private UsuarioSesionMapper usuarioSesionMapper;
	
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public T actualizar(T dto, String token) throws ServerException {
		getUserFlex(token);
		try {
			mapper.actualizar(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}

	public T consultaUnica(TFilter dto) throws ServerException {
		T result = null;
		try {
			result = mapper.consultar(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return result;
	}

	public T consultaXId(String llave) throws ServerException {
		throw new ServerException("Este metodo debe ser sobreescrito en cada servicio");
	}

	public int contarResultados(TFilter dto) throws ServerException {
		try {
			return mapper.cantidadRegistros(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public T inactivar(T dto, String token) throws ServerException {
		getUserFlex(token);
		dto = consultaXId(dto.getLlaveTabla());
		if(dto==null) throw new ServerException("No se identifica el objeto a inactivar");
		if(dto.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO)==0) throw new ServerException("Este objeto ya se encuentra inactivo");
		dto.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
		try {
			mapper.actualizar(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}
	
	public T activar(T dto, String token) throws ServerException {
		getUserFlex(token);
		dto = consultaXId(dto.getLlaveTabla());
		if(dto==null) throw new ServerException("No se identifica el objeto a Activar");
		if(dto.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)==0) throw new ServerException("Este objeto ya se encuentra Activo");
		dto.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		try {
			mapper.actualizar(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}
	
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public T guardar(T dto, String token) throws ServerException {
		getUserFlex(token);
		dto.setLlaveTabla(generarLlave());
		try {
			mapper.insertar( dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		dto = consultaXId(dto.getLlaveTabla());
		return dto;
	}

	public List<T> listarConsulta(TFilter dto) throws ServerException {
		paginar(dto);
		try {
			return mapper.listar(dto); 
		}catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public String getUserFlex(String token) throws ServerException{
		if(token!=null){
			UsuarioSesionFilterDTO filter = new UsuarioSesionFilterDTO();
			filter.setLlaveTabla(token);
			UsuarioSesionDTO sesion = usuarioSesionMapper.consultar(filter);
			if(sesion==null) throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario" );
			if(sesion.getFechaCierre()!=null && sesion.getFechaCierre().compareTo(new Date())<0) throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario" );
			return sesion.getUsuario();
		}
		throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario" );
	}
	
	public void paginar(TFilter dto){
		if(dto.getPaginacionRegistroInicial()==null) dto.setPaginacionRegistroInicial(0);
		if(dto.getPaginacionRegistroFinal()==null || dto.getPaginacionRegistroFinal()==0) dto.setPaginacionRegistroFinal(200);
	}
	
	public String generarLlave(){
		UUID uuid = UUID.randomUUID();
		String gen = uuid.toString();
		gen = gen.replaceAll("-", "");
		return gen;
	}
	
	
	
	public T inactivate(T dto) throws ServerException {
		dto = consultaXId(dto.getLlaveTabla());
		if(dto==null) throw new ServerException("No se identifica el objeto a inactivar");
		if(dto.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO)==0) throw new ServerException("Este objeto ya se encuentra inactivo");
		dto.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
		try {
			mapper.actualizar(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}
	
	public T activate(T dto) throws ServerException {
		dto = consultaXId(dto.getLlaveTabla());
		if(dto==null) throw new ServerException("No se identifica el objeto a Activar");
		if(dto.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)==0) throw new ServerException("Este objeto ya se encuentra Activo");
		dto.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		try {
			mapper.actualizar(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}
	
	// @Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public T save(T dto) throws ServerException {
		dto.setLlaveTabla(generarLlave());
		try {
			mapper.insertar(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		dto = consultaXId(dto.getLlaveTabla());
		return dto;
	}
	
	// @Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public T update(T dto) throws ServerException {
		try {
			mapper.actualizar(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}
}