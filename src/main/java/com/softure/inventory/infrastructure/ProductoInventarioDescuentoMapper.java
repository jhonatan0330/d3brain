package com.softure.inventory.infrastructure;


import com.softure.SoftureSqlConnMapper;
import com.softure.inventory.domain.ProductoInventarioDescuentoDTO;
import com.softure.inventory.domain.ProductoInventarioDescuentoFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper("ProductoInventarioDescuentoMapper")
public interface ProductoInventarioDescuentoMapper extends IBasicMapper<ProductoInventarioDescuentoDTO, ProductoInventarioDescuentoFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}