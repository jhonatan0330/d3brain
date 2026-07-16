package com.softure.document_execution.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.authorization.application.RolAccesoSvc;
import com.softure.document_execution.application.field.CampoAdaptador;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.DocumentMessage;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaDineroDTO;
import com.softure.document_execution.domain.PedidoVentaFilterDTO;
import com.softure.document_execution.infrastructure.PedidoVentaMapper;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaFilterDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.process_form.domain.DocumentoPlantillaFilterDTO;
import com.softure.property.application.PropertyGetWithCacheService;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

import jakarta.annotation.PostConstruct;
import com.softure.authentication.application.UsuarioSesionSvc;

@Service("pedidoVentaService")
public class PedidoVentaSvc extends BasicSvc<PedidoVentaDTO, PedidoVentaFilterDTO> {

	private final PedidoVentaMapper pedidoVentaMapper;
	private final CampoAdaptador adaptador;
	private final DocumentoPlantillaSvc documentoPlantillaService;
	private final DocumentoPlantillaCaracteristicaSvc documentoPlantillaCaracteristicaService;
	private final PedidoVentaDineroSvc dineroService;
	private final PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService;
	private final PropiedadSvc propiedadService;
	private final PropertyGetWithCacheService cacheService;
	private final RolAccesoSvc rolService;

	public PedidoVentaSvc(@Lazy UsuarioSesionSvc usuarioSesionService, @Lazy PedidoVentaMapper pedidoVentaMapper,
			@Lazy CampoAdaptador adaptador, @Lazy DocumentoPlantillaSvc documentoPlantillaService,
			@Lazy DocumentoPlantillaCaracteristicaSvc documentoPlantillaCaracteristicaService,
			@Lazy PedidoVentaDineroSvc dineroService,
			@Lazy PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService, @Lazy PropiedadSvc propiedadService,
			@Lazy PropertyGetWithCacheService cacheService, @Lazy RolAccesoSvc rolService) {
		super(usuarioSesionService);
		this.pedidoVentaMapper = pedidoVentaMapper;
		this.adaptador = adaptador;
		this.documentoPlantillaService = documentoPlantillaService;
		this.documentoPlantillaCaracteristicaService = documentoPlantillaCaracteristicaService;
		this.dineroService = dineroService;
		this.pedidoVentaCaracteristicaService = pedidoVentaCaracteristicaService;
		this.propiedadService = propiedadService;
		this.cacheService = cacheService;
		this.rolService = rolService;
	}

	@Override
	public PedidoVentaDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. PedidoVenta");
		PedidoVentaFilterDTO dto = new PedidoVentaFilterDTO();
		dto.setLlaveTabla(llave);
		return pedidoVentaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = pedidoVentaMapper;
	}

	@Override
	public PedidoVentaDTO activar(PedidoVentaDTO dto, String token) throws ServerException {
		throw new ServerException("Un documento que fue inactivado no se puede volver a activar.");
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaDTO actualizar(PedidoVentaDTO dto, String token) throws ServerException {
		throw new ServerException("Usa la funcion SaveUpdateInactivateDocumentFunction update");
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaDTO inactivar(PedidoVentaDTO dto, String token) throws ServerException {
		throw new ServerException("Usa la funcion SaveUpdateInactivateDocumentFunction inactivate");
	}

	public PedidoVentaDTO consultaCompleta(String documentId, String token) throws ServerException {
		if (documentId == null)
			throw new ServerException("En el desarrollo se debe crear el objeto desde la plantilla");
		String securityToken = token;
		PedidoVentaDTO bd = consultaXIdConDinero(documentId);
		if (bd == null)
			throw new ServerException("El identificador del DTO es incorrecto");
		// VAlido que el estado del pedido me permita modificaciones
		boolean modificable = true;
		if (bd.getEstadoExpediente() != null) {
			String usuarioToken = (securityToken == null) ? null : getUserFlex(securityToken);
			modificable = (cacheService.obtenerPropiedades(PropiedadValorDefinidoDTO.ESTADO, bd.getEstadoExpediente(),
					Propiedades.MODIFICABLE, usuarioToken) == null) ? false : true;
		} else {
			if (bd.getEstado().compareTo(PedidoVentaDTO.ESTADO_ACTIVO) != 0) {
				modificable = false;
				if (bd.getEstado().compareTo(PedidoVentaDTO.ESTADO_FINALIZADO) == 0) {
					bd.setEstadoNombre("FINALIZADO");
				} else {
					bd.setEstadoNombre("INACTIVO");
				}
			} else {
				bd.setEstadoNombre("ACTIVO");
			}
		}
		DocumentoPlantillaFilterDTO plantillaFilter = new DocumentoPlantillaFilterDTO();
		plantillaFilter.setLlaveTabla(bd.getPlantilla());
		plantillaFilter.setSecurityToken(securityToken);
		DocumentoPlantillaDTO plantilla = documentoPlantillaService.obtenerConfiguracionSinCampos(plantillaFilter,
				rolService.usuarioPermisosCompletos(securityToken));
		plantilla = documentoPlantillaService.obtenerCampos(plantilla, securityToken, false);
		if (plantilla.getCaracteristicas() != null & plantilla.getCaracteristicas().size() != 0) {
			List<PedidoVentaCaracteristicaDTO> caracteristicasActuales = pedidoVentaCaracteristicaService
					.listar2Documento(bd.getLlaveTabla(), bd.getHistorico());
			bd.setCaracteristicas(new ArrayList<PedidoVentaCaracteristicaDTO>());
			for (DocumentoPlantillaCaracteristicaDTO documentoCaracteristicaDTO : plantilla.getCaracteristicas()) {
				PedidoVentaCaracteristicaDTO uc = null;
				for (PedidoVentaCaracteristicaDTO pedidoCaracteristica : caracteristicasActuales) {
					if (pedidoCaracteristica.getCampo().compareTo(documentoCaracteristicaDTO.getLlaveTabla()) == 0) {
						uc = pedidoCaracteristica;
						break;
					}
				}
				if (uc == null)
					uc = new PedidoVentaCaracteristicaDTO();
				uc.setCampo(documentoCaracteristicaDTO.getLlaveTabla());
				uc.setCampoDTO(documentoCaracteristicaDTO);
				uc.setDocumento(bd.getLlaveTabla());

				if (!modificable) {
					Propiedades.retirarPropiedad(uc.getCampoDTO(), Propiedades.PERMISO_CAMPO_MODIFICABLE);
				}
				bd.getCaracteristicas().add(uc);
			}
			for (PedidoVentaCaracteristicaDTO _iField : bd.getCaracteristicas()) {
				List<PropiedadDTO> _propDepend = Propiedades.obtenerVariosParametro(_iField.getCampoDTO(),
						Propiedades.DEPENDENT_PROPS);
				if (_propDepend != null) {
					for (PropiedadDTO _iProp : _propDepend) {
						for (PedidoVentaCaracteristicaDTO _iFieldExpediente : bd.getCaracteristicas()) {
							if (_iProp.getValor().compareTo(_iFieldExpediente.getCampo()) == 0) {
								if (_iField.getDependientes() == null)
									_iField.setDependientes(new ArrayList<PedidoVentaCaracteristicaDTO>());
								if (_iFieldExpediente.getModificado())
									_iField.setModificado(true);
								_iField.getDependientes().add(_iFieldExpediente);
								break;
							}
						}
					}
					// Esto es muy riesgoso hacerlo toca despues con calma hacer pruebas
					// campoDocumento.setDependientes(pedidoVentaCaracteristicaService.ordenarAlfabeticaDepende(campoDocumento.getDependientes()));
				}
				adaptador.cargarConsultaCampo(_iField, securityToken);
			}
		}
		return bd;
	}

	public PedidoVentaDTO validateBeforeNew(PedidoVentaFilterDTO filter) throws ServerException {
		PedidoVentaDTO result = new PedidoVentaDTO();
		List<PropiedadDTO> prop = cacheService.obtenerPropiedades(PropiedadValorDefinidoDTO.PLANTILLA,
				filter.getPlantilla(), Propiedades.FUNCION_SQL_NEW_ANTES, filter.getSecurityToken());
		if (prop.isEmpty() || prop.size() != 1)
			return result;
		for (PropiedadDTO propiedadDTO : prop) {
			String resultString = propiedadService.validarFuncionSQL2(propiedadDTO, filter.getPlantilla(),
					filter.getSecurityToken());
			if (resultString != null && resultString.compareTo(SharedConstants.OK) != 0) {
				if (result.getMessages() == null)
					result.setMessages(new ArrayList<>());
				DocumentMessage message = new DocumentMessage();
				message.setType(SharedConstants.ERROR);
				message.setMessage(resultString);
				result.getMessages().add(message);
			}
		}
		return result;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaDTO guardar(PedidoVentaDTO dto, String token) throws ServerException {
		throw new ServerException("Usa la funcion SaveUpdateInactivateDocumentFunction save");
	}

	public PedidoVentaDTO consultaXIdConDinero(String llave) throws ServerException {
		PedidoVentaDTO result = consultaXId(llave);
		if (result == null)
			return result;
		// En softure en una iteracion al consultar por segunda vez no encontraba el
		// valor del dinero entonces para que se mantenga voy a decir que si es nulo
		// entonces use el que tiene
		PedidoVentaDineroDTO moneyDB = dineroService.consultaPorDocumento(llave, result.getHistorico(),
				result.getNombre());
		if (moneyDB != null)
			result.setDinero(moneyDB);

		return result;
	}

	public PedidoVentaDTO obtenerCamposCompletos(PedidoVentaDTO pedido, String token) throws ServerException {
		// Caracteristicas
		if (pedido == null || pedido.getPlantilla() == null)
			throw new ServerException("Desarrollador el pedido y su plantilla no deben venir nulos");
		DocumentoPlantillaCaracteristicaFilterDTO rcDTOFilter = new DocumentoPlantillaCaracteristicaFilterDTO();
		rcDTOFilter.setEstado(SharedConstants.STATE_ACTIVE);
		rcDTOFilter.setPlantilla(pedido.getPlantilla());
		List<DocumentoPlantillaCaracteristicaDTO> camposBase = documentoPlantillaCaracteristicaService
				.listarConsulta(rcDTOFilter);
		if (camposBase != null & camposBase.size() != 0) {
			List<PedidoVentaCaracteristicaDTO> caracteristicasActuales = pedidoVentaCaracteristicaService
					.listar2Documento(pedido.getLlaveTabla(), pedido.getHistorico());
			pedido.setCaracteristicas(new ArrayList<PedidoVentaCaracteristicaDTO>());
			if (caracteristicasActuales == null)
				caracteristicasActuales = new ArrayList<>();
			PedidoVentaCaracteristicaDTO uc = null;
			for (DocumentoPlantillaCaracteristicaDTO documentoCaracteristicaDTO : camposBase) {
				uc = null;
				for (PedidoVentaCaracteristicaDTO pedidoCaracteristica : caracteristicasActuales) {
					if (pedidoCaracteristica.getCampo().compareTo(documentoCaracteristicaDTO.getLlaveTabla()) == 0) {
						uc = pedidoCaracteristica;
						break;
					}
				}
				if (uc == null)
					uc = new PedidoVentaCaracteristicaDTO();
				uc.setCampo(documentoCaracteristicaDTO.getLlaveTabla());
				// documentoCaracteristicaDTO.setRol(pedido.getRol());
				uc.setCampoDTO(documentoCaracteristicaDTO);
				uc.setDocumento(pedido.getLlaveTabla());
				adaptador.cargarConsultaCampo(uc, token);
				pedido.getCaracteristicas().add(uc);
			}
		}
		return pedido;
	}

	public void actualizarEstadosNuevoProceso(PedidoVentaDTO dto) throws ServerException {
		if (dto == null)
			return;
		try {
			pedidoVentaMapper.actualizarEstados(dto);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public int listarEstadosNuevoProceso(PedidoVentaDTO dto) throws ServerException {
		if (dto == null)
			return 1000;
		try {
			return pedidoVentaMapper.contarEstados(dto);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public List<PedidoVentaDTO> iteracionesProceso(String sqlFuncionDecision, String llaveTablaDocumento,
			String llaveTablaModificador) throws ServerException {
		List<PedidoVentaDTO> result = null;
		try {
			// ramdom por problemas del framework se repetia la respuesta cuando iteraba
			result = pedidoVentaMapper.iteracion(sqlFuncionDecision, llaveTablaDocumento, llaveTablaModificador,
					generarLlave());
		} catch (Exception e) {
			throw new ServerException(e.getMessage(), "Funcion de Iteracion con errores: ");
		}
		return result;
	}

	public List<PedidoVentaDTO> listarTareasOtroUsuario(String usuario) throws ServerException {
		PedidoVentaFilterDTO filter = new PedidoVentaFilterDTO();
		filter.setFuncionario(usuario);
		paginar(filter);
		try {
			return pedidoVentaMapper.listarUsuario(filter);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	// Por el momento lo uso para tipo vinculo funcion
	public List<PedidoVentaDTO> listarExpedientesDisponiblesDocumentoFuncion(PedidoVentaFilterDTO dto,
			String funcionBusqueda, List<PedidoVentaCaracteristicaDTO> parametros) throws ServerException {
		if (funcionBusqueda == null)
			return null;
		if (dto == null) {
			dto = new PedidoVentaFilterDTO();
		}

		paginar(dto);
		try {
			funcionBusqueda = SoftureUtil.formatFunction(funcionBusqueda);
			if (dto.getFiltroParametro() != null)
				dto.setFiltroParametro(SoftureUtil.formatSimpleFunction(dto.getFiltroParametro()).toUpperCase());
			return pedidoVentaMapper.listarExpedientesDisponiblesDocumentoFuncion(dto, funcionBusqueda, null,
					parametros);
		} catch (Exception e) {
			throw new ServerException(e.getMessage(), e);
		}
	}

	public String getMessageToProcessField(String pProperty, String pValue, String pToken) throws ServerException {
		PropiedadDTO prop = cacheService.obtenerPropiedad(PropiedadValorDefinidoDTO.CAMPO, pProperty,
				Propiedades.HTML_DOCUMENT_SQL, pToken);
		if (prop == null)
			return null;
		return propiedadService.validarFuncionSQL2(prop, pValue, pToken);
	}

	public PedidoVentaDTO findByCode(String pName, String pTemplate) throws ServerException {
		if (pTemplate == null || pName == null)
			return null;
		PedidoVentaFilterDTO filter = new PedidoVentaFilterDTO();
		filter.setNombre(pName);
		filter.setPlantilla(pTemplate);
		try {
			return pedidoVentaMapper.consultar(filter);
		} catch (Exception e) {
			throw new ServerException(e.getMessage(), e);
		}
	}

	public void clearPedidoResponse(PedidoVentaDTO pDTO) {
		if (pDTO == null)
			return;

		for (PedidoVentaCaracteristicaDTO _iField : pDTO.getCaracteristicas()) {
			_iField.setCampoDTO(null);
			_iField.setDependientes(null);
			_iField.setTransaccionRegistro(null);
		}
	}

	// Creo que esto puede ir en un mapper diferente de solo el crud
	public List<PedidoVentaDTO> getByNameTemplateAndConsecutive(String pName, String pTemplate, String pConsecutive)
			throws ServerException {
		try {
			return pedidoVentaMapper.getByNameTemplateAndConsecutive(pName, pTemplate, pConsecutive);
		} catch (Exception e) {
			throw new ServerException(e.getMessage(), e);
		}
	}
}