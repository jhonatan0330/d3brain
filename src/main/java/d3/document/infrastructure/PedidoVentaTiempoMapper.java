package d3.document.infrastructure;

import d3.D3SqlConnMapper;
import d3.document.domain.PedidoVentaTiempoDTO;
import d3.document.domain.PedidoVentaTiempoFilterDTO;
import d3.shared.domain.IBasicMapper;

@D3SqlConnMapper(value = "PedidoVentaTiempoMapper")
public interface PedidoVentaTiempoMapper extends IBasicMapper<PedidoVentaTiempoDTO, PedidoVentaTiempoFilterDTO> {

}