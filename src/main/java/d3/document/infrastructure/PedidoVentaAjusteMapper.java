package d3.document.infrastructure;

import d3.D3SqlConnMapper;
import d3.document.domain.PedidoVentaAjusteDTO;
import d3.document.domain.PedidoVentaAjusteFilterDTO;
import d3.shared.domain.IBasicMapper;

@D3SqlConnMapper(value = "PedidoVentaAjusteMapper")
public interface PedidoVentaAjusteMapper extends IBasicMapper<PedidoVentaAjusteDTO, PedidoVentaAjusteFilterDTO> {

}