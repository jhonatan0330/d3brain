package com.softure.logisticpymes.infrastructure.mybatis.mapper;


import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.UsuarioAutenticacionDTO;
import com.softure.logisticpymes.domain.filter.UsuarioAutenticacionFilterDTO;

public interface UsuarioAutenticacionMapper extends IBasicMapper<UsuarioAutenticacionDTO, UsuarioAutenticacionFilterDTO>{
	

// BEGIN region aditionalMethods  
	String consultarValidez();
	String versionActual();
	String fechaMinima();
	
	int cantidadAsignaciones(String usuario);
	int ocultarLicencia(String usuario);

// END region aditionalMethods
}