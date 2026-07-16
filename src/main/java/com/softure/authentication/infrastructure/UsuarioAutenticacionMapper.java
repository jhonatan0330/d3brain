package com.softure.authentication.infrastructure;

import com.softure.SoftureSqlConnMapper;
import com.softure.authentication.domain.UsuarioAutenticacionDTO;
import com.softure.authentication.domain.UsuarioAutenticacionFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper(value = "UsuarioAutenticacionMapper")
public interface UsuarioAutenticacionMapper
		extends IBasicMapper<UsuarioAutenticacionDTO, UsuarioAutenticacionFilterDTO> {

	String consultarValidez();

	String versionActual();

	String fechaMinima();

	int cantidadAsignaciones(String usuario);

}