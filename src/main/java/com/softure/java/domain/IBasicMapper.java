package com.softure.java.domain;

import java.util.List;


public interface IBasicMapper<T extends BasicDTO, TFilter extends BasicFilterDTO> {

	T insertar(T dto);
	
	int eliminar(T dto);

	T actualizar(T dto);
	
	int cantidadRegistros(TFilter dto);
	
	T consultar(TFilter dto);
	
	List<T> listar(TFilter dto);
	
}
