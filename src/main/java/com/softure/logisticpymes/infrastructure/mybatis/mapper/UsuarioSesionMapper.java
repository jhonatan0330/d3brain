package com.softure.logisticpymes.infrastructure.mybatis.mapper;


import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.UsuarioSesionDTO;
import com.softure.logisticpymes.domain.filter.UsuarioSesionFilterDTO;

public interface UsuarioSesionMapper extends IBasicMapper<UsuarioSesionDTO, UsuarioSesionFilterDTO>{
	

// BEGIN region aditionalMethods  
	int tiempoSesion(String usuario);
// END region aditionalMethods
}