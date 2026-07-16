package com.softure.inventory.infrastructure;

import com.softure.SoftureSqlConnMapper;
import com.softure.inventory.domain.DeduccionProductoDTO;
import com.softure.inventory.domain.DeduccionProductoFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper(value = "DeduccionProductoMapper")
public interface DeduccionProductoMapper extends IBasicMapper<DeduccionProductoDTO, DeduccionProductoFilterDTO> {

}