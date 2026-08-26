package d3.inventory.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.document_execution.domain.PedidoVentaCaracteristicaDTO;
import d3.inventory.domain.ProductoDTO;
import d3.inventory.domain.ProductoFilterDTO;
import d3.java.domain.IBasicMapper;

@D3SqlConnMapper(value = "ProductoMapper")
public interface ProductoMapper extends IBasicMapper<ProductoDTO, ProductoFilterDTO> {

	List<ProductoDTO> listarProductoCampo(@Param("campo") String campo, @Param("filtro") String filtro);

	List<ProductoDTO> listarProductoPlantillaResponsable(ProductoFilterDTO dto);

	List<ProductoDTO> listarProductoDisponible(ProductoFilterDTO dto);

	List<ProductoDTO> listarProductoFuncion(@Param("funcion") String funcion, @Param("documento") String documento,
			@Param("filtro") String filtro, @Param("token") String token,
			@Param("parametros") List<PedidoVentaCaracteristicaDTO> parametros);

	List<ProductoDTO> listarProductoSimplificado(@Param("productos") List<ProductoDTO> productos);

	ProductoDTO filtrarPorCodigo(@Param("codigo") String codigo);
}