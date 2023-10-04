package com.softure.inventory.infrastructure;


import com.softure.SoftureSqlConnMapper;
import com.softure.inventory.domain.DetalleCaracteristicaProductoDTO;
import com.softure.inventory.domain.DetalleCaracteristicaProductoFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper("DetalleCaracteristicaProductoMapper")
public interface DetalleCaracteristicaProductoMapper extends IBasicMapper<DetalleCaracteristicaProductoDTO, DetalleCaracteristicaProductoFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}