package com.softure.shared.application;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.authentication.domain.UsuarioSesionDTO;
import com.softure.authentication.domain.UsuarioSesionFilterDTO;
import com.softure.authentication.infrastructure.UsuarioSesionMapper;
import com.softure.java.dto.exception.ServerException;

@Service
public class SharedValidateTokenService {
	
	@Autowired private UsuarioSesionMapper usuarioSesionMapper;
	
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
}