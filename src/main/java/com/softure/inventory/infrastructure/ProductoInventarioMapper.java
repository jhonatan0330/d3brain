package com.softure.inventory.infrastructure;

import com.softure.SoftureSqlConnMapper;
import com.softure.inventory.domain.ProductoInventarioDTO;
import com.softure.inventory.domain.ProductoInventarioFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper(value = "ProductoInventarioMapper")
public interface ProductoInventarioMapper extends IBasicMapper<ProductoInventarioDTO, ProductoInventarioFilterDTO> {

}