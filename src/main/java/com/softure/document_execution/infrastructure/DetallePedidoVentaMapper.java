package com.softure.document_execution.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.SoftureSqlConnMapper;
import com.softure.document_execution.domain.DetallePedidoVentaDTO;
import com.softure.document_execution.domain.DetallePedidoVentaFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper(value = "DetallePedidoVentaMapper")
public interface DetallePedidoVentaMapper extends IBasicMapper<DetallePedidoVentaDTO, DetallePedidoVentaFilterDTO>{
	
	List<DetallePedidoVentaDTO> listar2Documento(@Param("documento")String pDocumento, @Param("campo")String pCampo);
}