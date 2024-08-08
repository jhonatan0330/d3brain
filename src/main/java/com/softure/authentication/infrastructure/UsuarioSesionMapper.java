package com.softure.authentication.infrastructure;


import com.softure.SoftureSqlConnMapper;
import com.softure.authentication.domain.UsuarioSesionDTO;
import com.softure.authentication.domain.UsuarioSesionFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper(value = "UsuarioSesionMapper")
public interface UsuarioSesionMapper extends IBasicMapper<UsuarioSesionDTO, UsuarioSesionFilterDTO>{

// BEGIN region aditionalMethods  
	int tiempoSesion(String usuario);
// END region aditionalMethods
}