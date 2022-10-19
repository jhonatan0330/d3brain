package com.softure.logisticpymes.infrastructure.mybatis.mapper;


// BEGIN region interImport  
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.domain.dto.TarifaDTO;
import com.softure.logisticpymes.domain.filter.TarifaFilterDTO;

public interface TarifaMapper extends IBasicMapper<TarifaDTO, TarifaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<TarifaDTO> obtenerTarifa(TarifaFilterDTO dto);
	List<TarifaDTO> obtenerTarifaFuncion(@Param("propiedad") String propiedad, @Param("producto") String producto, @Param("productoBase") String productoBase, @Param("parametros") List<PedidoVentaCaracteristicaDTO> parametros);
// END region aditionalMethods
}