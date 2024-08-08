package com.softure.authentication.infrastructure;


import com.softure.SoftureSqlConnMapper;
import com.softure.authentication.domain.UsuarioAutenticacionAutorizacionDTO;
import com.softure.authentication.domain.UsuarioAutenticacionAutorizacionFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper(value = "UsuarioAutenticacionAutorizacionMapper")
public interface UsuarioAutenticacionAutorizacionMapper extends IBasicMapper<UsuarioAutenticacionAutorizacionDTO, UsuarioAutenticacionAutorizacionFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}