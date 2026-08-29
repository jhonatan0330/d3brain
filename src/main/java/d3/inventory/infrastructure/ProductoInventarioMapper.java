package d3.inventory.infrastructure;

import d3.D3SqlConnMapper;
import d3.inventory.domain.ProductoInventarioDTO;
import d3.inventory.domain.ProductoInventarioFilterDTO;
import d3.shared.domain.IBasicMapper;

@D3SqlConnMapper(value = "ProductoInventarioMapper")
public interface ProductoInventarioMapper extends IBasicMapper<ProductoInventarioDTO, ProductoInventarioFilterDTO> {

}