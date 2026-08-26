package d3.document_execution.infrastructure;

import d3.D3SqlConnMapper;
import d3.document_execution.domain.PedidoVentaTiempoDTO;
import d3.document_execution.domain.PedidoVentaTiempoFilterDTO;
import d3.java.domain.IBasicMapper;

@D3SqlConnMapper(value = "PedidoVentaTiempoMapper")
public interface PedidoVentaTiempoMapper extends IBasicMapper<PedidoVentaTiempoDTO, PedidoVentaTiempoFilterDTO> {

}