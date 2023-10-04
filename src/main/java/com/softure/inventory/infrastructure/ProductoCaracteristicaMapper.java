package com.softure.inventory.infrastructure;


// BEGIN region interImport  
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.softure.SoftureSqlConnMapper;
import com.softure.inventory.domain.ProductoCaracteristicaDTO;
import com.softure.inventory.domain.ProductoCaracteristicaFilterDTO;
import com.softure.inventory.domain.ProductoDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper("ProductoCaracteristicaMapper")
public interface ProductoCaracteristicaMapper extends IBasicMapper<ProductoCaracteristicaDTO, ProductoCaracteristicaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<ProductoCaracteristicaDTO> listarProductoSimplificado(@Param("productos") List<ProductoDTO> productos);
// END region aditionalMethods
}