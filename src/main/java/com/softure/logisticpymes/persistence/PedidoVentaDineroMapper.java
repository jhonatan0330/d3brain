package com.softure.logisticpymes.persistence;


// BEGIN region interImport  
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.logisticpymes.dto.PedidoVentaDTO;
// END region interImport
import com.softure.logisticpymes.dto.PedidoVentaDineroDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaDineroFilterDTO;

public interface PedidoVentaDineroMapper extends IBasicMapper<PedidoVentaDineroDTO, PedidoVentaDineroFilterDTO>{
	

// BEGIN region aditionalMethods  
	PedidoVentaDineroDTO consultaPorDocumento(@Param("idCampo") String idCampo, @Param("historico") String historico);
	List<PedidoVentaDineroDTO> listar2DocumentoVisible(@Param("documentos") List<PedidoVentaDTO> documentos);
	PedidoVentaDineroDTO insertarHistorico(PedidoVentaDineroDTO dto);
	PedidoVentaDineroDTO inactivarHistorico(@Param("idCampo") String idCampo, @Param("historico") String historico);
// END region aditionalMethods
}