package com.softure.tariff.infrastructure;


// BEGIN region interImport  
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.java.domain.IBasicMapper;
import com.softure.tariff.domain.TarifaDTO;
import com.softure.tariff.domain.TarifaFilterDTO;

public interface TarifaMapper extends IBasicMapper<TarifaDTO, TarifaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<TarifaDTO> obtenerTarifa(TarifaFilterDTO dto);
	List<TarifaDTO> obtenerTarifaFuncion(@Param("propiedad") String propiedad, @Param("producto") String producto, @Param("productoBase") String productoBase, @Param("parametros") List<PedidoVentaCaracteristicaDTO> parametros);
// END region aditionalMethods
}