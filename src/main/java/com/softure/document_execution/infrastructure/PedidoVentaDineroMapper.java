package com.softure.document_execution.infrastructure;


// BEGIN region interImport  
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaDineroDTO;
import com.softure.document_execution.domain.PedidoVentaDineroFilterDTO;
import com.softure.java.domain.IBasicMapper;

public interface PedidoVentaDineroMapper extends IBasicMapper<PedidoVentaDineroDTO, PedidoVentaDineroFilterDTO>{
	

// BEGIN region aditionalMethods  
	PedidoVentaDineroDTO consultaPorDocumento(@Param("idCampo") String idCampo, @Param("historico") String historico);
	List<PedidoVentaDineroDTO> listar2DocumentoVisible(@Param("documentos") List<PedidoVentaDTO> documentos, @Param("historicos") List<PedidoVentaDTO> historicos);
	PedidoVentaDineroDTO insertarHistorico(PedidoVentaDineroDTO dto);
	PedidoVentaDineroDTO inactivarHistorico(@Param("idCampo") String idCampo, @Param("historico") String historico);
// END region aditionalMethods
}