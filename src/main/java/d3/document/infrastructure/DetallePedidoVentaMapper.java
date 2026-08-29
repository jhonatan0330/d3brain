package d3.document.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.document.domain.DetallePedidoVentaDTO;
import d3.document.domain.DetallePedidoVentaFilterDTO;
import d3.shared.domain.IBasicMapper;

@D3SqlConnMapper(value = "DetallePedidoVentaMapper")
public interface DetallePedidoVentaMapper extends IBasicMapper<DetallePedidoVentaDTO, DetallePedidoVentaFilterDTO> {

	List<DetallePedidoVentaDTO> listar2Documento(@Param("documento") String pDocumento, @Param("campo") String pCampo);
}