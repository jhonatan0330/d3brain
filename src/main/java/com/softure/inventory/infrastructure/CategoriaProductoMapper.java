package com.softure.inventory.infrastructure;


import com.softure.inventory.domain.CategoriaProductoDTO;
import com.softure.inventory.domain.CategoriaProductoFilterDTO;
import com.softure.java.domain.IBasicMapper;

public interface CategoriaProductoMapper extends IBasicMapper<CategoriaProductoDTO, CategoriaProductoFilterDTO>{
	

// BEGIN region aditionalMethods  
	void ingresarInventarioFaltanteBodega();
// END region aditionalMethods
}