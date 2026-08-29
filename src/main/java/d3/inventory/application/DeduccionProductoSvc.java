package d3.inventory.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.document.application.PedidoVentaSvc;
import d3.document.application.field.AuxiliarProcesoBodega;
import d3.document.application.field.Propiedades;
import d3.document.domain.PedidoVentaCaracteristicaDTO;
import d3.document.domain.PedidoVentaDTO;
import d3.inventory.domain.DeduccionProductoDTO;
import d3.inventory.domain.DeduccionProductoFilterDTO;
import d3.inventory.domain.TrazabilidadProductoInventarioDTO;
import d3.inventory.domain.TrazabilidadProductoInventarioFilterDTO;
import d3.inventory.infrastructure.DeduccionProductoMapper;
import d3.shared.application.BasicSvc;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import d3.authentication.application.UsuarioSesionSvc;
import d3.configuration.application.PropertyGetWithCacheService;
import d3.configuration.domain.PropiedadDTO;
import d3.configuration.domain.PropiedadValorDefinidoDTO;

@Service("deduccionProductoService")
public class DeduccionProductoSvc extends BasicSvc<DeduccionProductoDTO, DeduccionProductoFilterDTO> {

	private final DeduccionProductoMapper deduccionProductoMapper;
	private final TrazabilidadProductoInventarioSvc trazabilidadProductoInventarioService;
	private final PedidoVentaSvc pedidoService;
	private final AuxiliarProcesoBodega tipoBodega;
	private final PropertyGetWithCacheService cacheService;

	public DeduccionProductoSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy DeduccionProductoMapper deduccionProductoMapper,
			@Lazy TrazabilidadProductoInventarioSvc trazabilidadProductoInventarioService,
			@Lazy PedidoVentaSvc pedidoService, @Lazy AuxiliarProcesoBodega tipoBodega,
			@Lazy PropertyGetWithCacheService cacheService) {
		super(usuarioSesionService);
		this.deduccionProductoMapper = deduccionProductoMapper;
		this.trazabilidadProductoInventarioService = trazabilidadProductoInventarioService;
		this.pedidoService = pedidoService;
		this.tipoBodega = tipoBodega;
		this.cacheService = cacheService;
	}

	@Override
	public DeduccionProductoDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. DeduccionProducto");
		DeduccionProductoFilterDTO dto = new DeduccionProductoFilterDTO();
		dto.setLlaveTabla(llave);
		return deduccionProductoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = deduccionProductoMapper;
	}

	@Override
	public DeduccionProductoDTO activar(DeduccionProductoDTO dto, String token) throws ServerException {
		return super.activar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public DeduccionProductoDTO actualizar(DeduccionProductoDTO dto, String token) throws ServerException {
		return super.actualizar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public DeduccionProductoDTO inactivar(DeduccionProductoDTO dto, String token) throws ServerException {
		dto = super.inactivar(dto, token);
		TrazabilidadProductoInventarioFilterDTO trazabilidadFilter = new TrazabilidadProductoInventarioFilterDTO();
		trazabilidadFilter.setDeduccionProducto(dto.getLlaveTabla());
		List<TrazabilidadProductoInventarioDTO> trazas = trazabilidadProductoInventarioService
				.listarConsulta(trazabilidadFilter);
		if (trazas != null && trazas.size() != 0) {
			TrazabilidadProductoInventarioDTO trazabilidad = new TrazabilidadProductoInventarioDTO();
			trazabilidad.setProducto(dto.getProducto());
			trazabilidad.setCantidad(BigDecimal.ZERO);
			trazabilidad.setBodega(dto.getBodega());
			for (TrazabilidadProductoInventarioDTO trazabilidadProductoInventarioDTO : trazas) {
				trazabilidad.setCantidad(
						trazabilidad.getCantidad().add(trazabilidadProductoInventarioDTO.getCantidad().negate()));
			}
			trazabilidad.setDeduccionProducto(dto.getLlaveTabla());
			trazabilidadProductoInventarioService.guardar(trazabilidad, token);
		}
		return dto;
	}

	@Override
	public DeduccionProductoDTO consultaUnica(DeduccionProductoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}

	@Override
	public int contarResultados(DeduccionProductoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}

	@Override
	public List<DeduccionProductoDTO> listarConsulta(DeduccionProductoFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	@Override
	public DeduccionProductoDTO guardar(DeduccionProductoDTO dto, String token) throws ServerException {
		if (dto.getFecha() == null)
			dto.setFecha(new Date());
		if (dto.getCantidad() == null || dto.getCantidad().compareTo(BigDecimal.ZERO) == 0)
			throw new ServerException("No se puede realizar una deduccion sin cantidad");
		dto = super.guardar(dto, token);
		TrazabilidadProductoInventarioDTO trazabilidad = new TrazabilidadProductoInventarioDTO();
		trazabilidad.setProducto(dto.getProducto());
		trazabilidad.setCantidad(dto.getCantidad());
		trazabilidad.setBodega(dto.getBodega());
		trazabilidad.setDeduccionProducto(dto.getLlaveTabla());
		trazabilidad = trazabilidadProductoInventarioService.guardar(trazabilidad, token);
		return dto;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void recalcularInventarioDocumento(String documento, String token) throws ServerException {
		PedidoVentaDTO expediente = pedidoService.obtenerCamposCompletos(pedidoService.consultaXId(documento), token);
		// 2. Coloco los dependientes//Actualizar dependencias despues de los camps para
		// que queden completas asi el campo este despues en orden
		for (PedidoVentaCaracteristicaDTO campoDocumento : expediente.getCaracteristicas()) {
			campoDocumento.getCampoDTO().setPropiedades(cacheService.obtenerPropiedades(PropiedadValorDefinidoDTO.CAMPO,
					campoDocumento.getCampo(), null, null));
			List<PropiedadDTO> codigoDepende = Propiedades.obtenerVariosParametro(campoDocumento.getCampoDTO(),
					Propiedades.DEPENDE);
			if (codigoDepende != null) {
				for (PropiedadDTO codigo : codigoDepende) {
					for (PedidoVentaCaracteristicaDTO fieldExpediente : expediente.getCaracteristicas()) {
						if (codigo.getValor().compareTo(fieldExpediente.getCampo()) == 0) {
							if (fieldExpediente.getCampoDTO().getFormato()
									.compareTo(DocumentoPlantillaCaracteristicaDTO.PROCESO) == 0
									&& fieldExpediente.getValorOpcion() != null
									&& fieldExpediente.getExpedientes() == null) {
								fieldExpediente.setExpedientes(new ArrayList<PedidoVentaDTO>());
								fieldExpediente.getExpedientes()
										.add(pedidoService.consultaXId(fieldExpediente.getValorOpcion()));
							}
							if (campoDocumento.getDependientes() == null)
								campoDocumento.setDependientes(new ArrayList<PedidoVentaCaracteristicaDTO>());
							campoDocumento.getDependientes().add(fieldExpediente);

							break;
						}
					}
				}
			}
		}
		DeduccionProductoFilterDTO filter = new DeduccionProductoFilterDTO();
		filter.setDocumento(documento);
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		List<DeduccionProductoDTO> deduccionesActuales = listarConsulta(filter);
		if (deduccionesActuales == null)
			deduccionesActuales = new ArrayList<DeduccionProductoDTO>();
		for (DeduccionProductoDTO iDeduccion : deduccionesActuales) {
			iDeduccion.setCantidad(iDeduccion.getCantidad().negate());
		}
		List<DeduccionProductoDTO> deduccionesFinales = new ArrayList<>();
		for (PedidoVentaCaracteristicaDTO iCampo : expediente.getCaracteristicas()) {
			// Identificar los campos bodega
			if (Propiedades.obtenerParametro(iCampo.getCampoDTO(), Propiedades.BODEGA_MOVIMIENTO) != null) {
				// Guardar
				deduccionesFinales = tipoBodega.validarInventario(iCampo, token);
				for (DeduccionProductoDTO iDeduccion : deduccionesFinales) {
					deduccionesActuales = tipoBodega.adicionarDeduccion(deduccionesActuales, iDeduccion);
				}
			}
		}

		if (deduccionesActuales != null && !deduccionesActuales.isEmpty()) {
			for (DeduccionProductoDTO iDeduccion : deduccionesActuales) {
				if (iDeduccion.getCantidad().compareTo(BigDecimal.ZERO) != 0) {
					iDeduccion = guardar(iDeduccion, token);
				}
			}
		}
	}

}