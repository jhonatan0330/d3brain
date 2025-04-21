package com.softure.authentication.infrastructure;


import org.apache.ibatis.annotations.Param;

import com.softure.SoftureSqlConnMapper;
import com.softure.authentication.domain.UsuarioSesionDTO;
import com.softure.authentication.domain.UsuarioSesionFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper(value = "UsuarioSesionMapper")
public interface UsuarioSesionMapper extends IBasicMapper<UsuarioSesionDTO, UsuarioSesionFilterDTO>{

// BEGIN region aditionalMethods  
	int tiempoSesion(String usuario);
	void closeAllSession(@Param("userId")String userId, @Param("token")String token);
// END region aditionalMethods
}