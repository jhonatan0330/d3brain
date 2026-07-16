package com.softure.inventory.infrastructure;

import com.softure.SoftureSqlConnMapper;
import com.softure.inventory.domain.TrazabilidadProductoInventarioDTO;
import com.softure.inventory.domain.TrazabilidadProductoInventarioFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper(value = "TrazabilidadProductoInventarioMapper")
public interface TrazabilidadProductoInventarioMapper
		extends IBasicMapper<TrazabilidadProductoInventarioDTO, TrazabilidadProductoInventarioFilterDTO> {

}