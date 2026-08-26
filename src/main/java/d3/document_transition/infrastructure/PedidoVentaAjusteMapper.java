package d3.document_transition.infrastructure;

import d3.D3SqlConnMapper;
import d3.document_transition.domain.PedidoVentaAjusteDTO;
import d3.document_transition.domain.PedidoVentaAjusteFilterDTO;
import d3.java.domain.IBasicMapper;

@D3SqlConnMapper(value = "PedidoVentaAjusteMapper")
public interface PedidoVentaAjusteMapper extends IBasicMapper<PedidoVentaAjusteDTO, PedidoVentaAjusteFilterDTO> {

}