package com.softure.logisticpymes.persistence;

import java.util.List;

import com.softure.logisticpymes.dto.BasicDTO;
import com.softure.logisticpymes.dto.filter.BasicFilterDTO;


public interface IBasicMapper<T extends BasicDTO, TFilter extends BasicFilterDTO> {

	T insertar(T dto);
	
	int eliminar(T dto);

	T actualizar(T dto);
	
	int cantidadRegistros(TFilter dto);
	
	T consultar(TFilter dto);
	
	List<T> listar(TFilter dto);
}
