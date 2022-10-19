package com.softure.logisticpymes.infrastructure.mybatis.mapper;


// BEGIN region interImport  
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.PedidoVentaDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaDineroDTO;
import com.softure.logisticpymes.domain.filter.PedidoVentaDineroFilterDTO;

public interface PedidoVentaDineroMapper extends IBasicMapper<PedidoVentaDineroDTO, PedidoVentaDineroFilterDTO>{
	

// BEGIN region aditionalMethods  
	PedidoVentaDineroDTO consultaPorDocumento(@Param("idCampo") String idCampo, @Param("historico") String historico);
	List<PedidoVentaDineroDTO> listar2DocumentoVisible(@Param("documentos") List<PedidoVentaDTO> documentos, @Param("historicos") List<PedidoVentaDTO> historicos);
	PedidoVentaDineroDTO insertarHistorico(PedidoVentaDineroDTO dto);
	PedidoVentaDineroDTO inactivarHistorico(@Param("idCampo") String idCampo, @Param("historico") String historico);
// END region aditionalMethods
}