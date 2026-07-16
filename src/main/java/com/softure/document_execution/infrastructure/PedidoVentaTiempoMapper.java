package com.softure.document_execution.infrastructure;

import com.softure.SoftureSqlConnMapper;
import com.softure.document_execution.domain.PedidoVentaTiempoDTO;
import com.softure.document_execution.domain.PedidoVentaTiempoFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper(value = "PedidoVentaTiempoMapper")
public interface PedidoVentaTiempoMapper extends IBasicMapper<PedidoVentaTiempoDTO, PedidoVentaTiempoFilterDTO> {

}