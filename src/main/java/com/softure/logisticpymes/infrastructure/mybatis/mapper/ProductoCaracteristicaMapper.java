package com.softure.logisticpymes.infrastructure.mybatis.mapper;


// BEGIN region interImport  
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.ProductoCaracteristicaDTO;
import com.softure.logisticpymes.domain.dto.ProductoDTO;
import com.softure.logisticpymes.domain.filter.ProductoCaracteristicaFilterDTO;

public interface ProductoCaracteristicaMapper extends IBasicMapper<ProductoCaracteristicaDTO, ProductoCaracteristicaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<ProductoCaracteristicaDTO> listarProductoSimplificado(@Param("productos") List<ProductoDTO> productos);
// END region aditionalMethods
}