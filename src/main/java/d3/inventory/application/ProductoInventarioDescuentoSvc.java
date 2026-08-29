package d3.inventory.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.domain.ServerException;
import d3.inventory.domain.ProductoInventarioDescuentoDTO;
import d3.inventory.domain.ProductoInventarioDescuentoFilterDTO;
import d3.inventory.infrastructure.ProductoInventarioDescuentoMapper;
import d3.shared.application.BasicSvc;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import d3.authentication.application.UsuarioSesionSvc;

@Service("productoInventarioDescuentoService")
public class ProductoInventarioDescuentoSvc
		extends BasicSvc<ProductoInventarioDescuentoDTO, ProductoInventarioDescuentoFilterDTO> {

	private final ProductoInventarioDescuentoMapper productoInventarioDescuentoMapper;

	public ProductoInventarioDescuentoSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy ProductoInventarioDescuentoMapper productoInventarioDescuentoMapper) {
		super(usuarioSesionService);
		this.productoInventarioDescuentoMapper = productoInventarioDescuentoMapper;
	}

	@Override
	public ProductoInventarioDescuentoDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. ProductoInventarioDescuento");
		ProductoInventarioDescuentoFilterDTO dto = new ProductoInventarioDescuentoFilterDTO();
		dto.setLlaveTabla(llave);
		return productoInventarioDescuentoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = productoInventarioDescuentoMapper;
	}

	@Override
	public ProductoInventarioDescuentoDTO activar(ProductoInventarioDescuentoDTO dto, String token)
			throws ServerException {
		return super.activar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProductoInventarioDescuentoDTO actualizar(ProductoInventarioDescuentoDTO dto, String token)
			throws ServerException {
		if (dto.getProducto().compareTo(dto.getProductoDescontar()) == 0)
			throw new ServerException("No se puede generar una composición del mismo producto");
		return super.actualizar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProductoInventarioDescuentoDTO inactivar(ProductoInventarioDescuentoDTO dto, String token)
			throws ServerException {
		return super.inactivar(dto, token);
	}

	@Override
	public ProductoInventarioDescuentoDTO consultaUnica(ProductoInventarioDescuentoFilterDTO dto)
			throws ServerException {
		return super.consultaUnica(dto);
	}

	@Override
	public int contarResultados(ProductoInventarioDescuentoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}

	@Override
	public List<ProductoInventarioDescuentoDTO> listarConsulta(ProductoInventarioDescuentoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProductoInventarioDescuentoDTO guardar(ProductoInventarioDescuentoDTO dto, String token)
			throws ServerException {
		dto = super.guardar(dto, token);
		if (dto.getProducto().compareTo(dto.getProductoDescontar()) == 0)
			throw new ServerException("No se puede generar una composición del mismo producto");
		return dto;
	}


}