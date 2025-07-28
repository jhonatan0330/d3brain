package com.softure.document_execution.infrastructure;


// BEGIN region interImport  
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.SoftureSqlConnMapper;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaUbicacionDTO;
import com.softure.document_execution.domain.PedidoVentaUbicacionFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper(value = "PedidoVentaUbicacionMapper")
public interface PedidoVentaUbicacionMapper extends IBasicMapper<PedidoVentaUbicacionDTO, PedidoVentaUbicacionFilterDTO>{
	

	PedidoVentaUbicacionDTO consultaPorDocumento(@Param("idCampo") String idCampo, @Param("historico") Integer historico, @Param("ramdom") String ramdom);
	List<PedidoVentaUbicacionDTO> listar2DocumentoVisible(@Param("documentos") List<PedidoVentaDTO> documentos, @Param("historicos") List<PedidoVentaDTO> historicos);
	PedidoVentaUbicacionDTO insertarHistorico(PedidoVentaUbicacionDTO dto);
	PedidoVentaUbicacionDTO inactivarHistorico(@Param("idCampo") String idCampo, @Param("historico") String historico);
}