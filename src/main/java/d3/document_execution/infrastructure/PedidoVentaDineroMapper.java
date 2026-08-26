package d3.document_execution.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.document_execution.domain.PedidoVentaDTO;
import d3.document_execution.domain.PedidoVentaDineroDTO;
import d3.document_execution.domain.PedidoVentaDineroFilterDTO;
import d3.java.domain.IBasicMapper;

@D3SqlConnMapper(value = "PedidoVentaDineroMapper")
public interface PedidoVentaDineroMapper extends IBasicMapper<PedidoVentaDineroDTO, PedidoVentaDineroFilterDTO> {

	PedidoVentaDineroDTO consultaPorDocumento(@Param("idCampo") String idCampo, @Param("historico") Integer historico,
			@Param("ramdom") String ramdom);

	List<PedidoVentaDineroDTO> listar2DocumentoVisible(@Param("documentos") List<PedidoVentaDTO> documentos,
			@Param("historicos") List<PedidoVentaDTO> historicos);

	PedidoVentaDineroDTO insertarHistorico(PedidoVentaDineroDTO dto);

	PedidoVentaDineroDTO inactivarHistorico(@Param("idCampo") String idCampo, @Param("historico") String historico);
}