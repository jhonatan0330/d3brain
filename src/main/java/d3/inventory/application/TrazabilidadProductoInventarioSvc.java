package d3.inventory.application;

import java.util.List;

import java.math.BigDecimal;
import java.util.Date;

import d3.shared.domain.SharedConstants;
import d3.shared.domain.ServerException;
import d3.inventory.domain.ProductoDTO;
import d3.inventory.domain.ProductoInventarioDTO;
import d3.inventory.domain.ProductoInventarioFilterDTO;
import d3.inventory.domain.TrazabilidadProductoInventarioDTO;
import d3.inventory.domain.TrazabilidadProductoInventarioFilterDTO;
import d3.inventory.infrastructure.TrazabilidadProductoInventarioMapper;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.logisticpymes.application.BasicSvc;

import jakarta.annotation.PostConstruct;
import d3.authentication.application.UsuarioSesionSvc;

@Service("trazabilidadProductoInventarioService")
public class TrazabilidadProductoInventarioSvc
		extends BasicSvc<TrazabilidadProductoInventarioDTO, TrazabilidadProductoInventarioFilterDTO> {

	private final TrazabilidadProductoInventarioMapper trazabilidadProductoInventarioMapper;
	private final ProductoInventarioSvc productoInventarioService;
	private final ProductoSvc productoService;

	public TrazabilidadProductoInventarioSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy TrazabilidadProductoInventarioMapper trazabilidadProductoInventarioMapper,
			@Lazy ProductoInventarioSvc productoInventarioService, @Lazy ProductoSvc productoService) {
		super(usuarioSesionService);
		this.trazabilidadProductoInventarioMapper = trazabilidadProductoInventarioMapper;
		this.productoInventarioService = productoInventarioService;
		this.productoService = productoService;
	}

	@Override
	public TrazabilidadProductoInventarioDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. TrazabilidadProductoInventario");
		TrazabilidadProductoInventarioFilterDTO dto = new TrazabilidadProductoInventarioFilterDTO();
		dto.setLlaveTabla(llave);
		return trazabilidadProductoInventarioMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = trazabilidadProductoInventarioMapper;
	}

	@Override
	public TrazabilidadProductoInventarioDTO activar(TrazabilidadProductoInventarioDTO dto, String token)
			throws ServerException {
		return super.activar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public TrazabilidadProductoInventarioDTO actualizar(TrazabilidadProductoInventarioDTO dto, String token)
			throws ServerException {
		return super.actualizar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public TrazabilidadProductoInventarioDTO inactivar(TrazabilidadProductoInventarioDTO dto, String token)
			throws ServerException {
		return super.inactivar(dto, token);
	}

	@Override
	public TrazabilidadProductoInventarioDTO consultaUnica(TrazabilidadProductoInventarioFilterDTO dto)
			throws ServerException {
		return super.consultaUnica(dto);
	}

	@Override
	public int contarResultados(TrazabilidadProductoInventarioFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}

	@Override
	public List<TrazabilidadProductoInventarioDTO> listarConsulta(TrazabilidadProductoInventarioFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}

	@Override
	public TrazabilidadProductoInventarioDTO guardar(TrazabilidadProductoInventarioDTO dto, String token)
			throws ServerException {
		TrazabilidadProductoInventarioDTO trazabilidad = dto;
		if (trazabilidad.getCantidad() == null)
			throw new ServerException("La cantidad del movimiento de producto no puede ser null");
		if (trazabilidad.getFecha() == null)
			trazabilidad.setFecha(new Date());

		ProductoInventarioFilterDTO inventarioFilter = new ProductoInventarioFilterDTO();
		inventarioFilter.setEstado(SharedConstants.STATE_ACTIVE);
		inventarioFilter.setProducto(dto.getProducto());
		inventarioFilter.setBodega(dto.getBodega());
		// En aprobar estimacion se tiene que crear la bodega en el auxiliar
		// de bodegas del tipo campo, por motivos desconocidos si hago una consultar
		// unica no me trae el inventario acabado de crear tocaba con el listar
		List<ProductoInventarioDTO> inventarios = productoInventarioService.listarConsulta(inventarioFilter);
		if (inventarios == null || inventarios.size() == 0)
			return trazabilidad;
		// ProductoInventarioDTO inventario =
		// productoInventarioService.consultaUnica(inventarioFilter);
		ProductoInventarioDTO inventario = inventarios.get(0);
		// LOGICA DE INVENTARIOS
		if (inventario != null) {
			ProductoDTO producto = productoService.consultaXId(inventario.getProducto());
			if (producto.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
				throw new ServerException("El producto no se encuentra activo: " + producto.getNombre());
			trazabilidad.setCantidadInicial(inventario.getCantidadActual());
			trazabilidad.setCantidadFinal(trazabilidad.getCantidadInicial().add(trazabilidad.getCantidad()));
			// Valido los tamanos de inventario catindad maxima y el cero
			if (trazabilidad.getCantidadFinal().compareTo(BigDecimal.ZERO) < 0)
				throw new ServerException("No se puede deducir mas cantidad a la actual del producto.\nProducto:"
						+ inventario.getNombre() + "\nCant:" + inventario.getCantidadActual() + "\nSolicitado: "
						+ trazabilidad.getCantidad() + "\nBodega: " + inventario.getNombreBodega());
			if (trazabilidad.getCantidadFinal().compareTo(inventario.getCantidadMaxima()) > 0)
				throw new ServerException("Esta superando la cantidad maxima estimada del producto.\nProducto:"
						+ inventario.getNombre() + "\nCant Maxima:" + inventario.getCantidadMaxima() + "\nCant Final:"
						+ trazabilidad.getCantidadFinal());
			// Solo se acualiza hasta el final el produto para realizar los calculos en la
			// trazabilidad
			inventario.setCantidadModificar(trazabilidad.getCantidad());
			trazabilidad.setResponsable(getUserFlex(token));
			trazabilidad = super.guardar(trazabilidad, token);
			inventario = productoInventarioService.actualizar(inventario, token);
		}
		return trazabilidad;
	}


}