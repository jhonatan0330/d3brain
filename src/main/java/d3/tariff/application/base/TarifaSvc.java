package d3.tariff.application.base;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.authentication.application.UsuarioSesionSvc;
import d3.document.domain.PedidoVentaCaracteristicaDTO;
import d3.inventory.application.ProductoSvc;
import d3.inventory.domain.ProductoDTO;
import d3.inventory.domain.ProductoFilterDTO;
import d3.shared.application.D3Utils;
import d3.shared.application.BasicSvc;
import d3.tariff.domain.TarifaDTO;
import d3.tariff.domain.TarifaFilterDTO;
import d3.tariff.domain.TarifarioDTO;
import d3.tariff.domain.TarifarioFilterDTO;
import d3.tariff.infrastructure.TarifaMapper;

import jakarta.annotation.PostConstruct;

@Service("tarifaService")
public class TarifaSvc extends BasicSvc<TarifaDTO, TarifaFilterDTO> {

	private final TarifaMapper tarifaMapper;
	private final TarifarioService tarifarioService;
	private final ProductoSvc productoService;

	public TarifaSvc(@Lazy UsuarioSesionSvc usuarioSesionService, @Lazy TarifaMapper tarifaMapper,
			@Lazy TarifarioService tarifarioService, @Lazy ProductoSvc productoService) {
		super(usuarioSesionService);
		this.tarifaMapper = tarifaMapper;
		this.tarifarioService = tarifarioService;
		this.productoService = productoService;
	}

	@Override
	public TarifaDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Tarifa");
		TarifaFilterDTO dto = new TarifaFilterDTO();
		dto.setLlaveTabla(llave);
		return tarifaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = tarifaMapper;
	}

	@Override
	public TarifaDTO activar(TarifaDTO dto, String token) throws ServerException {
		return super.activar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public TarifaDTO actualizar(TarifaDTO dto, String token) throws ServerException {
		clean(dto);
		if (dto.getLlaveTabla() == null)
			throw new ServerException("No podemos actualizar una tarifa sin su id");
		TarifaDTO existe = validarTarifa(dto);
		if (existe != null && existe.getLlaveTabla().compareTo(dto.getLlaveTabla()) != 0)
			throw new ServerException(
					"Existe una tarifa con las mimsas condiciones de tarifario, origen y destino activa, por favor revise su configuracion");

		TarifaDTO tariff = consultaXId(dto.getLlaveTabla());
		tariff.setEstado(SharedConstants.STATE_INACTIVE);
		tariff.setUpdatedAt(new Date());
		tariff.setUpdatedUser(getUserFlex(token));
		super.actualizar(tariff, token);
		// Para el historial
		dto.setLlaveTabla(null);
		dto.setCreatedUser(getUserFlex(token));
		return super.guardar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public TarifaDTO inactivar(TarifaDTO dto, String token) throws ServerException {
		return super.inactivar(dto, token);
	}

	@Override
	public TarifaDTO consultaUnica(TarifaFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}

	@Override
	public int contarResultados(TarifaFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}

	@Override
	public List<TarifaDTO> listarConsulta(TarifaFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public TarifaDTO guardar(TarifaDTO dto, String token) throws ServerException {
		clean(dto);
		if (dto.getTarifario() == null)
			dto.setTarifario(getByDocumentService(dto.getTarifarioDocumento()).getKey());

		if (dto.getProducto() == null && dto.getProductoDocumento() != null) {
			ProductoFilterDTO filterOne = new ProductoFilterDTO();
			filterOne.setDocumento(dto.getProductoDocumento());
			ProductoDTO productoById = productoService.consultaUnica(filterOne);
			if (productoById != null)
				dto.setProducto(productoById.getLlaveTabla());
		}
		// Para las cargas valido los codigos y el recurso
		if (dto.getProducto() == null && dto.getProductoNombre() != null) {
			ProductoDTO filtroProducto = productoService.filtrarPorCodigo(dto.getProductoNombre());
			if (filtroProducto == null)
				throw new ServerException("No se identifica producto con el codigo : " + dto.getProductoNombre());
			dto.setProducto(filtroProducto.getLlaveTabla());
		} else {
			if (dto.getProducto() != null) {
				ProductoDTO productoById = productoService.consultaXId(dto.getProducto());
				if (productoById == null) {
					ProductoFilterDTO filterOne = new ProductoFilterDTO();
					filterOne.setDocumento(dto.getProducto());
					productoById = productoService.consultaUnica(filterOne);
					if (productoById != null)
						dto.setProducto(productoById.getLlaveTabla());
				}
			}
		}

		if (validarTarifa(dto) != null)
			throw new ServerException(
					"Existe una tarifa con las mismas condiciones de tarifario, origen y destino activa, por favor revise su configuracion");

		dto.setCreatedUser(getUserFlex(token));
		return super.guardar(dto, token);

	}

	private void clean(TarifaDTO dto) {
		if (dto.getCantidadMaxima() == null)
			dto.setCantidadMaxima(0);
		if (dto.getCantidadMinima() == null)
			dto.setCantidadMinima(0);
		if (dto.getValorMaximo() == null)
			dto.setValorMaximo(BigDecimal.ZERO);
		if (dto.getValorMinimo() == null)
			dto.setValorMinimo(BigDecimal.ZERO);
		if (dto.getValor() == null)
			dto.setValor(BigDecimal.ZERO);
		if (dto.getTotalMinimo() == null)
			dto.setTotalMinimo(BigDecimal.ZERO);
		if (dto.getProducto() != null && dto.getProducto().isEmpty())
			dto.setProducto(null);
		if (dto.getRecurso() != null && dto.getRecurso().isEmpty())
			dto.setRecurso(null);
		if (dto.getDimension2() != null && dto.getDimension2().isEmpty())
			dto.setDimension2(null);
		if (dto.getDimension3() != null && dto.getDimension3().isEmpty())
			dto.setDimension3(null);
		if (dto.getDimension4() != null && dto.getDimension4().isEmpty())
			dto.setDimension4(null);
		if (dto.getProductoNombre() != null && dto.getProductoNombre().isEmpty())
			dto.setProductoNombre(null);

	}

	public List<TarifaDTO> obtenerTarifa(TarifaFilterDTO dto) throws ServerException {
		List<TarifaDTO> resultado = null;
		if (dto.getRecurso() != null) {
			resultado = tarifaMapper.obtenerTarifa(dto);
			dto.setRecurso(null);
		}
		if (resultado == null) {
			resultado = tarifaMapper.obtenerTarifa(dto);
		} else {
			resultado.addAll(tarifaMapper.obtenerTarifa(dto));
		}
		return resultado;
	}

	public TarifaDTO validarTarifa(TarifaDTO dto) throws ServerException {
		// Por el momento solo valido que existaq en crear y actualizar
		TarifaFilterDTO existe = new TarifaFilterDTO();
		existe.setTarifario(dto.getTarifario());
		existe.setProducto(dto.getProducto());

		existe.setCantidadMinima(dto.getCantidadMinima());
		existe.setCantidadMaxima(dto.getCantidadMaxima());
		if (existe.getCantidadMaxima().compareTo(1) == 0
				&& existe.getCantidadMaxima().compareTo(existe.getCantidadMinima()) == 0) {
			existe.setCantidadMinima(0);
			existe.setCantidadMaxima(0);
		}
		if (existe.getCantidadMaxima().compareTo(0) == 0)
			existe.setCantidadMaxima(Integer.MAX_VALUE);

		existe.setRecurso(dto.getRecurso());
		existe.setDimension2(dto.getDimension2());
		existe.setDimension3(dto.getDimension3());
		existe.setDimension4(dto.getDimension4());
		existe.setEstado(SharedConstants.STATE_ACTIVE);
		List<TarifaDTO> tarifa = tarifaMapper.obtenerTarifa(existe);
		if (tarifa == null || tarifa.isEmpty())
			return null;
		if (tarifa.size() > 1)
			throw new ServerException("Revisa porque al consultar la tarifa, se consultan varias respuestas");
		return tarifa.get(0);
	}

	public List<TarifaDTO> obtenerTarifaFuncion(String propiedad, ProductoDTO producto,
			List<PedidoVentaCaracteristicaDTO> parametros) throws ServerException {
		try {
			return tarifaMapper.obtenerTarifaFuncion(D3Utils.formatFunction(propiedad), producto.getLlaveTabla(),
					producto.getProductoBase(), parametros);
		} catch (Exception e) {
			throw new ServerException(e.getMessage(), "Funcion de tarifas : " + producto.getNombre());
		}
	}

	public List<TarifaDTO> getTarifas2Product(String productId) throws ServerException {
		TarifaFilterDTO t = new TarifaFilterDTO();
		t.setEstado(SharedConstants.STATE_ACTIVE);
		t.setProducto(productId);
		return listarConsulta(t);
	}

	// Esto lo copie de TarrifGetByDocument, la idea es unificarlo y posiblemten
	// quitar todo lo de fees
	public TarifarioDTO getByDocumentService(String documentId) throws ServerException {
		if (documentId == null)
			throw new ServerException("Se debe seleccionar un tarifario");
		TarifarioFilterDTO tariffFilter = new TarifarioFilterDTO();
		tariffFilter.setDocumento(documentId);
		TarifarioDTO tariffDTO = tarifarioService.getOne(tariffFilter);
		if (tariffDTO == null)
			throw new ServerException("El tarifario no existe con ese identificador");
		return tariffDTO;
	}

}