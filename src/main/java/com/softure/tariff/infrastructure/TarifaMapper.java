package com.softure.tariff.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.SoftureSqlConnMapper;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.java.domain.IBasicMapper;
import com.softure.tariff.domain.TarifaDTO;
import com.softure.tariff.domain.TarifaFilterDTO;

@SoftureSqlConnMapper(value = "TarifaMapper")
public interface TarifaMapper extends IBasicMapper<TarifaDTO, TarifaFilterDTO>{
	
	List<TarifaDTO> obtenerTarifa(TarifaFilterDTO dto);
	List<TarifaDTO> obtenerTarifaFuncion(@Param("propiedad") String propiedad, @Param("producto") String producto, @Param("productoBase") String productoBase, @Param("parametros") List<PedidoVentaCaracteristicaDTO> parametros);
}