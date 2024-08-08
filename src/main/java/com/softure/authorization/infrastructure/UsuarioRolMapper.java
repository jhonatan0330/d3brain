package com.softure.authorization.infrastructure;


import com.softure.SoftureSqlConnMapper;
import com.softure.authorization.domain.UsuarioRolDTO;
import com.softure.authorization.domain.UsuarioRolFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper(value = "UsuarioRolMapper")
public interface UsuarioRolMapper extends IBasicMapper<UsuarioRolDTO, UsuarioRolFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}