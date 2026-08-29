package d3.inventory.infrastructure;

import d3.D3SqlConnMapper;
import d3.inventory.domain.DeduccionProductoDTO;
import d3.inventory.domain.DeduccionProductoFilterDTO;
import d3.shared.domain.IBasicMapper;

@D3SqlConnMapper(value = "DeduccionProductoMapper")
public interface DeduccionProductoMapper extends IBasicMapper<DeduccionProductoDTO, DeduccionProductoFilterDTO> {

}