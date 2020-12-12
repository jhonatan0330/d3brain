package com.softure.logisticpymes.persistence;


import java.util.List;

import org.apache.ibatis.annotations.Param;

// BEGIN region interImport  
import com.softure.logisticpymes.dto.PedidoVentaDTO;
// END region interImport
import com.softure.logisticpymes.dto.PedidoVentaDineroDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaDineroFilterDTO;

public interface PedidoVentaDineroMapper extends IBasicMapper<PedidoVentaDineroDTO, PedidoVentaDineroFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<PedidoVentaDineroDTO> listar2DocumentoVisible(@Param("documentos") List<PedidoVentaDTO> documentos);
// END region aditionalMethods
}