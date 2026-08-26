package d3.inventory.application;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.inventory.domain.ProductoDTO;
import d3.inventory.domain.ProductoInventarioDTO;
import d3.inventory.domain.ProductoInventarioFilterDTO;
import d3.inventory.infrastructure.ProductoInventarioMapper;
import d3.logisticpymes.application.BasicSvc;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import d3.authentication.application.UsuarioSesionSvc;

@Service("productoInventarioService")
public class ProductoInventarioSvc extends BasicSvc<ProductoInventarioDTO, ProductoInventarioFilterDTO> {

	private final ProductoInventarioMapper productoInventarioMapper;
	private final ProductoSvc productService;

	public ProductoInventarioSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy ProductoInventarioMapper productoInventarioMapper, @Lazy ProductoSvc productService) {
		super(usuarioSesionService);
		this.productoInventarioMapper = productoInventarioMapper;
		this.productService = productService;
	}

	@Override
	public ProductoInventarioDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. ProductoInventario");
		ProductoInventarioFilterDTO dto = new ProductoInventarioFilterDTO();
		dto.setLlaveTabla(llave);
		return productoInventarioMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = productoInventarioMapper;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProductoInventarioDTO actualizar(ProductoInventarioDTO dto, String token) throws ServerException {
		ProductoInventarioDTO bd = consultaXId(dto.getLlaveTabla());
		if (dto.getCantidadModificar() != null && dto.getCantidadModificar().compareTo(BigDecimal.ZERO) != 0) {
			bd.setCantidadActual(bd.getCantidadActual().add(dto.getCantidadModificar()));
			return super.actualizar(bd, token);
		} else {
			if (dto.getCantidadMaxima().compareTo(bd.getCantidadMaxima()) != 0
					|| dto.getCantidadMinima().compareTo(bd.getCantidadMinima()) != 0) {

				bd.setCantidadMaxima(dto.getCantidadMaxima());
				bd.setCantidadMinima(dto.getCantidadMinima());
				if (bd.getCantidadMinima().compareTo(bd.getCantidadMaxima()) > 0)
					throw new ServerException("La cantidad minima no puede ser mayor a la maxima");
				return super.actualizar(bd, token);
			}
		}
		return bd;
	}

	@Override
	public ProductoInventarioDTO guardar(ProductoInventarioDTO dto, String token) throws ServerException {
		if (dto.getDocumento() == null)
			throw new ServerException("Al crear un inventario de producto debe teenr un documento.");
		ProductoDTO product = productService.consultaXId(dto.getProducto());

		ProductoInventarioFilterDTO unicoFilter = new ProductoInventarioFilterDTO();
		unicoFilter.setBodega(dto.getBodega());
		unicoFilter.setProducto(dto.getProducto());
		ProductoInventarioDTO unico = consultaUnica(unicoFilter);
		if (unico != null) {
			if (unico.getEstado().compareTo(SharedConstants.STATE_ACTIVE) == 0) {
				throw new ServerException("Este producto " + product.getNombre()
						+ " ya se encuentra referenciado para controlar en esta bodega ");
			} else {
				throw new ServerException("Este producto " + product.getNombre()
						+ " se encuentra inactivo para manejo de inventarios en esta bodega ");
			}
		}
		if (dto.getCantidadMinima() == null)
			dto.setCantidadMinima(BigDecimal.ZERO);
		if (dto.getCantidadMaxima() == null)
			dto.setCantidadMaxima(BigDecimal.valueOf(1000));
		if (dto.getCantidadMinima().compareTo(dto.getCantidadMaxima()) >= 0)
			throw new ServerException("La cantidad minima no puede ser mayor o igual a la maxima");

		dto.setCantidadActual(BigDecimal.ZERO);
		dto.setFechaInicial(new Date());
		return super.save(dto);
	}

	public List<ProductoInventarioDTO> getByProducto(String id) throws ServerException {
		ProductoInventarioFilterDTO filter = new ProductoInventarioFilterDTO();
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		filter.setProducto(id);
		return super.listarConsulta(filter);
	}

}