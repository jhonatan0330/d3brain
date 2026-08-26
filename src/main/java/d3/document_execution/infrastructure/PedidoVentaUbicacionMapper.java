package d3.document_execution.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.document_execution.domain.PedidoVentaDTO;
import d3.document_execution.domain.PedidoVentaUbicacionDTO;
import d3.document_execution.domain.PedidoVentaUbicacionFilterDTO;
import d3.java.domain.IBasicMapper;

@D3SqlConnMapper(value = "PedidoVentaUbicacionMapper")
public interface PedidoVentaUbicacionMapper
		extends IBasicMapper<PedidoVentaUbicacionDTO, PedidoVentaUbicacionFilterDTO> {

	PedidoVentaUbicacionDTO consultaPorDocumento(@Param("idCampo") String idCampo,
			@Param("historico") Integer historico, @Param("ramdom") String ramdom);

	List<PedidoVentaUbicacionDTO> listar2DocumentoVisible(@Param("documentos") List<PedidoVentaDTO> documentos,
			@Param("historicos") List<PedidoVentaDTO> historicos);

	PedidoVentaUbicacionDTO insertarHistorico(PedidoVentaUbicacionDTO dto);

	PedidoVentaUbicacionDTO inactivarHistorico(@Param("idCampo") String idCampo, @Param("historico") String historico);
}