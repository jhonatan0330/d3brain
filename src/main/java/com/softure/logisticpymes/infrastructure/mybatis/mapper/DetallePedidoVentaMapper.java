package com.softure.logisticpymes.infrastructure.mybatis.mapper;


// BEGIN region interImport  
import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.DetallePedidoVentaDTO;
import com.softure.logisticpymes.domain.filter.DetallePedidoVentaFilterDTO;

public interface DetallePedidoVentaMapper extends IBasicMapper<DetallePedidoVentaDTO, DetallePedidoVentaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<DetallePedidoVentaDTO> listar2Documento(String documento);
// END region aditionalMethods
}