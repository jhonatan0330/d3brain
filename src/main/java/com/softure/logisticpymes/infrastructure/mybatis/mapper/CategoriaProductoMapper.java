package com.softure.logisticpymes.infrastructure.mybatis.mapper;


import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.CategoriaProductoDTO;
import com.softure.logisticpymes.domain.filter.CategoriaProductoFilterDTO;

public interface CategoriaProductoMapper extends IBasicMapper<CategoriaProductoDTO, CategoriaProductoFilterDTO>{
	

// BEGIN region aditionalMethods  
	void ingresarInventarioFaltanteBodega();
// END region aditionalMethods
}