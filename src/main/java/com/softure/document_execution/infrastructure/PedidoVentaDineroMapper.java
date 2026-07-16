package com.softure.document_execution.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.SoftureSqlConnMapper;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaDineroDTO;
import com.softure.document_execution.domain.PedidoVentaDineroFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper(value = "PedidoVentaDineroMapper")
public interface PedidoVentaDineroMapper extends IBasicMapper<PedidoVentaDineroDTO, PedidoVentaDineroFilterDTO> {

	PedidoVentaDineroDTO consultaPorDocumento(@Param("idCampo") String idCampo, @Param("historico") Integer historico,
			@Param("ramdom") String ramdom);

	List<PedidoVentaDineroDTO> listar2DocumentoVisible(@Param("documentos") List<PedidoVentaDTO> documentos,
			@Param("historicos") List<PedidoVentaDTO> historicos);

	PedidoVentaDineroDTO insertarHistorico(PedidoVentaDineroDTO dto);

	PedidoVentaDineroDTO inactivarHistorico(@Param("idCampo") String idCampo, @Param("historico") String historico);
}