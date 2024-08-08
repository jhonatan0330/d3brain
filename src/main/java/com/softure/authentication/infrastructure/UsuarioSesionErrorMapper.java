package com.softure.authentication.infrastructure;


import com.softure.SoftureSqlConnMapper;
import com.softure.authentication.domain.UsuarioSesionErrorDTO;
import com.softure.authentication.domain.UsuarioSesionErrorFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper(value = "UsuarioSesionErrorMapper")
public interface UsuarioSesionErrorMapper extends IBasicMapper<UsuarioSesionErrorDTO, UsuarioSesionErrorFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}