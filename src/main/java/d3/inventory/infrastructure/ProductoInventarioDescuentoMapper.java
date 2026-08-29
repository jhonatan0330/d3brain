package d3.inventory.infrastructure;

import d3.D3SqlConnMapper;
import d3.inventory.domain.ProductoInventarioDescuentoDTO;
import d3.inventory.domain.ProductoInventarioDescuentoFilterDTO;
import d3.shared.domain.IBasicMapper;

@D3SqlConnMapper(value = "ProductoInventarioDescuentoMapper")
public interface ProductoInventarioDescuentoMapper
		extends IBasicMapper<ProductoInventarioDescuentoDTO, ProductoInventarioDescuentoFilterDTO> {

}