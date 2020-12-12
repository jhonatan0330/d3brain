package com.softure.logisticpymes.persistence;


// BEGIN region interImport  
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.softure.logisticpymes.dto.PedidoVentaCaracteristicaDTO;
// END region interImport
import com.softure.logisticpymes.dto.TarifaDTO;
import com.softure.logisticpymes.dto.filter.TarifaFilterDTO;

public interface TarifaMapper extends IBasicMapper<TarifaDTO, TarifaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<TarifaDTO> obtenerTarifa(TarifaFilterDTO dto);
	List<TarifaDTO> obtenerTarifaFuncion(@Param("propiedad") String propiedad, @Param("producto") String producto, @Param("productoBase") String productoBase, @Param("parametros") List<PedidoVentaCaracteristicaDTO> parametros);
// END region aditionalMethods
}