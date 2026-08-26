package d3.inventory.infrastructure;

import d3.D3SqlConnMapper;
import d3.inventory.domain.TrazabilidadProductoInventarioDTO;
import d3.inventory.domain.TrazabilidadProductoInventarioFilterDTO;
import d3.java.domain.IBasicMapper;

@D3SqlConnMapper(value = "TrazabilidadProductoInventarioMapper")
public interface TrazabilidadProductoInventarioMapper
		extends IBasicMapper<TrazabilidadProductoInventarioDTO, TrazabilidadProductoInventarioFilterDTO> {

}