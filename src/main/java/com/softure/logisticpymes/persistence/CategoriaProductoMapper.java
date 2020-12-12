package com.softure.logisticpymes.persistence;


// BEGIN region interImport  
// END region interImport
import com.softure.logisticpymes.dto.CategoriaProductoDTO;
import com.softure.logisticpymes.dto.filter.CategoriaProductoFilterDTO;

public interface CategoriaProductoMapper extends IBasicMapper<CategoriaProductoDTO, CategoriaProductoFilterDTO>{
	

// BEGIN region aditionalMethods  
	void ingresarInventarioFaltanteBodega();
// END region aditionalMethods
}