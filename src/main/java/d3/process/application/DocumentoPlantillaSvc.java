package d3.process.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.authentication.application.UsuarioSesionSvc;
import d3.authorization.application.RolAccesoSvc;
import d3.authorization.domain.RolAccesoDTO;
import d3.authorization.domain.RolAccesoFilterDTO;
import d3.document.application.field.Propiedades;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.process.domain.DocumentoPlantillaDTO;
import d3.process.domain.DocumentoPlantillaFilterDTO;
import d3.process.domain.ProcesoDTO;
import d3.process.domain.ProcesoEstadoDTO;
import d3.process.domain.ProcesoEstadoFilterDTO;
import d3.process.domain.ProcesoTransicionDTO;
import d3.process.domain.ProcesoTransicionFilterDTO;
import d3.process.infrastructure.DocumentoPlantillaMapper;
import d3.property.application.PropertyGetWithCacheService;
import d3.property.application.PropiedadSvc;
import d3.property.domain.PropiedadDTO;
import d3.property.domain.PropiedadValorDefinidoDTO;
import d3.report.application.ReporteBaseSvc;
import d3.report.domain.ReporteBaseDTO;
import d3.shared.application.BasicSvc;
import d3.shared.application.D3Utils;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import jakarta.annotation.PostConstruct;

@Service("documentoPlantillaService")
public class DocumentoPlantillaSvc extends BasicSvc<DocumentoPlantillaDTO, DocumentoPlantillaFilterDTO> {

	private final DocumentoPlantillaMapper documentoPlantillaMapper;
	private final DocumentoPlantillaCaracteristicaSvc caracteristicaService;
	private final PropiedadSvc configuracionSvc;
	private final RolAccesoSvc rolService;
	private final ReporteBaseSvc reporteService;
	private final ProcesoSvc procesoService;
	private final ProcesoEstadoSvc estadoService;
	private final ProcesoTransicionSvc transicionService;
	private final PropertyGetWithCacheService cacheService;

	public DocumentoPlantillaSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy DocumentoPlantillaMapper documentoPlantillaMapper,
			@Lazy DocumentoPlantillaCaracteristicaSvc caracteristicaService, @Lazy PropiedadSvc configuracionSvc,
			@Lazy RolAccesoSvc rolService, @Lazy ReporteBaseSvc reporteService, @Lazy ProcesoSvc procesoService,
			@Lazy ProcesoEstadoSvc estadoService, @Lazy ProcesoTransicionSvc transicionService,
			@Lazy PropertyGetWithCacheService cacheService) {
		super(usuarioSesionService);
		this.documentoPlantillaMapper = documentoPlantillaMapper;
		this.caracteristicaService = caracteristicaService;
		this.configuracionSvc = configuracionSvc;
		this.rolService = rolService;
		this.reporteService = reporteService;
		this.procesoService = procesoService;
		this.estadoService = estadoService;
		this.transicionService = transicionService;
		this.cacheService = cacheService;
	}

	@Override
	public DocumentoPlantillaDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. DocumentoPlantilla");
		DocumentoPlantillaFilterDTO dto = new DocumentoPlantillaFilterDTO();
		dto.setLlaveTabla(llave);
		return documentoPlantillaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = documentoPlantillaMapper;
	}

	@Override
	public DocumentoPlantillaDTO activar(DocumentoPlantillaDTO dto, String token) throws ServerException {
		RolAccesoFilterDTO rolFilter = new RolAccesoFilterDTO();
		rolFilter.setPlantilla(dto.getLlaveTabla());
		rolFilter.setEstado(SharedConstants.STATE_INACTIVE);
		RolAccesoDTO rol = rolService.consultaUnica(rolFilter);
		if (rol != null)
			rolService.activar(rol, token);
		return super.activar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public DocumentoPlantillaDTO actualizar(DocumentoPlantillaDTO dto, String token) throws ServerException {
		dto.setCodigo(D3Utils.formatFunction(dto.getCodigo()).toUpperCase());
		dto = super.actualizar(dto, token);
		configuracionSvc.actualizarValorPropiedad(dto.getLlaveTabla(), dto.getNombre());
		return dto;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public DocumentoPlantillaDTO inactivar(DocumentoPlantillaDTO dto, String token) throws ServerException {
		ProcesoTransicionFilterDTO validar = new ProcesoTransicionFilterDTO();
		validar.setEstado(SharedConstants.STATE_ACTIVE);
		validar.setPlantilla(dto.getLlaveTabla());
		List<ProcesoTransicionDTO> pUsados = transicionService.listarConsulta(validar);
		if (pUsados == null || !pUsados.isEmpty()) {
			String mensaje = "La plantilla se esta usando en las siguientes transiciones : \n";
			for (ProcesoTransicionDTO iUsado : pUsados) {
				mensaje = mensaje + "Proceso: " + iUsado.getProcesoNombre() + "  -> Transicion: " + iUsado.getNombre()
						+ "\n";
			}
			throw new ServerException(mensaje);
		}
		RolAccesoFilterDTO rolFilter = new RolAccesoFilterDTO();
		rolFilter.setPlantilla(dto.getLlaveTabla());
		rolFilter.setEstado(SharedConstants.STATE_ACTIVE);
		RolAccesoDTO rol = rolService.consultaUnica(rolFilter);
		if (rol != null) {
			rolService.inactivar(rol, token);
		}
		return super.inactivar(dto, token);
	}

	public List<DocumentoPlantillaDTO> consultaUsuario(DocumentoPlantillaFilterDTO dto) throws ServerException {
		return listarPlantillasUsuario(dto, null);
	}

	public DocumentoPlantillaDTO obtenerCampos(DocumentoPlantillaDTO dto, String token, boolean external)
			throws ServerException {
		if (dto == null)
			return null;
		dto.setCaracteristicas(
				caracteristicaService.listarCamposPlantillaConComplementos(dto.getLlaveTabla(), token, external));
		int order = 0;
		boolean modificar = !Propiedades.obtenerValor(dto, Propiedades.PERMISO_PLANTILLA_MODIFICAR).isEmpty();
		// En caso que busca desde la interfaz
		if (dto.getPropiedades() == null) {
			modificar = true;
		}
		for (DocumentoPlantillaCaracteristicaDTO campo : dto.getCaracteristicas()) {
			order++;
			campo.setOrden(order);
			if (campo.getImagen() == null) {
				String imagenCampo = Propiedades.obtenerValor(campo, Propiedades.PLANTILLA_AUXILIAR);
				if (!imagenCampo.isEmpty()) {
					DocumentoPlantillaDTO plantillaAuxiliar = consultaXId(imagenCampo);
					if (plantillaAuxiliar != null)
						campo.setImagen(plantillaAuxiliar.getImagen());
				}
			}
			if (!modificar)
				Propiedades.retirarPropiedad(campo, Propiedades.PERMISO_CAMPO_MODIFICABLE);
		}
		return dto;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public DocumentoPlantillaDTO duplicar(DocumentoPlantillaDTO dto, String token) throws ServerException {
		DocumentoPlantillaDTO bd = consultaXId(dto.getLlaveTabla());
		// Copio plantilla
		DocumentoPlantillaDTO copy = new DocumentoPlantillaDTO();
		copy.setProceso(bd.getProceso());
		copy.setNombre("COPY_" + bd.getNombre());
		// copy.setImagen(bd.getImagen());
		copy.setObjetivo(".");

		configurarInicioPlantilla(copy);
		copy = super.save(copy);
		// Copio campos
		bd.setCaracteristicas(
				caracteristicaService.listarCamposPlantillaConComplementos(bd.getLlaveTabla(), null, false));
		copy.setCaracteristicas(new ArrayList<>());
		for (DocumentoPlantillaCaracteristicaDTO iCampo : bd.getCaracteristicas()) {
			DocumentoPlantillaCaracteristicaDTO newCampo = new DocumentoPlantillaCaracteristicaDTO();
			newCampo.setCodigo(iCampo.getCodigo());
			newCampo.setFormato(iCampo.getFormato());
			// newCampo.setImagen(iCampo.getImagen());
			newCampo.setNombre(iCampo.getNombre());
			// newCampo.setObjetivo(".");
			newCampo.setOrden(iCampo.getOrden());
			newCampo.setPlantilla(copy.getLlaveTabla());
			copy.getCaracteristicas().add(caracteristicaService.guardar(newCampo, token));
		}
		// Primero las propiedades de rol para evitar duplicar
		RolAccesoFilterDTO rolFiltroFilter = new RolAccesoFilterDTO();
		rolFiltroFilter.setEstado(SharedConstants.STATE_ACTIVE);
		rolFiltroFilter.setPlantilla(bd.getLlaveTabla());
		RolAccesoDTO rolFiltro = rolService.consultaUnica(rolFiltroFilter);
		if (rolFiltro != null) {
			RolAccesoDTO newRol = new RolAccesoDTO();
			newRol.setPlantilla(copy.getLlaveTabla());
			newRol = rolService.guardar(newRol, token);
			// rolFiltro.setPropiedades(
			// configuracionSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.ROL,
			// rolFiltro.getLlaveTabla(),null, null));
			// configuracionSvc.copiarPropiedades(rolFiltro.getPropiedades(),
			// newRol.getLlaveTabla(), token);
		}
		// Copio propiedades plantilla
		bd.setPropiedades(obtenerPropiedadesPlantilla(bd.getLlaveTabla(), null));
		copy.setPropiedades(configuracionSvc.copiarPropiedades(bd.getPropiedades(), copy.getLlaveTabla(), token));
		// Copio reportes
		bd.setReportes(reporteService.listarDisponiblesDocumento(bd.getLlaveTabla()));
		for (ReporteBaseDTO iReporte : bd.getReportes()) {
			ReporteBaseDTO newReporte = new ReporteBaseDTO();
			String newCode = copy.getCodigo() + "-" + iReporte.getCodigo();
			newReporte.setCodigo(newCode.substring(0, Math.min(newCode.length(), 16)));
			newReporte.setDescripcion(iReporte.getDescripcion());
			// newReporte.setJasperText(iReporte.getJasperText());
			String newName = copy.getCodigo() + "-" + iReporte.getNombre();
			newReporte.setNombre(newName);
			newReporte.setPlantilla(copy.getLlaveTabla());
			newReporte.setSoloExistente(iReporte.getSoloExistente());
			newReporte.setVariables(iReporte.getVariables());
			newReporte = reporteService.guardar(newReporte, token);
			configuracionSvc.copiarPropiedades(iReporte.getPropiedades(), newReporte.getLlaveTabla(), token);
		}
		for (DocumentoPlantillaCaracteristicaDTO iCampo : copy.getCaracteristicas()) {
			for (DocumentoPlantillaCaracteristicaDTO source : bd.getCaracteristicas()) {
				if (source.getCodigo().compareTo(iCampo.getCodigo()) == 0) {
					iCampo.setPropiedades(
							configuracionSvc.copiarPropiedades(source.getPropiedades(), iCampo.getLlaveTabla(), token));
					break;
				}
			}
		}
		return copy;
	}

	public List<DocumentoPlantillaDTO> consultaAdministrador(DocumentoPlantillaFilterDTO dto) throws ServerException {
		boolean todosPermisos = rolService.usuarioPermisosCompletos(dto.getSecurityToken());
		if (!todosPermisos)
			throw new ServerException(
					"En los roles que tienes asignados no tienes un rol que tenga permisos de consultar todas las plantillas");
		return listarPlantillasUsuario(dto, "ADMIN");
	}

	public List<DocumentoPlantillaDTO> consultaAuditor(DocumentoPlantillaFilterDTO dto) throws ServerException {
		boolean todosPermisos = rolService.usuarioPermisosAuditor(dto.getSecurityToken());
		if (!todosPermisos)
			throw new ServerException("En los roles que tienes asignados no tienes el permiso de auditor");
		return listarPlantillasUsuario(dto, "READER");
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public DocumentoPlantillaDTO guardar(DocumentoPlantillaDTO dto, String token) throws ServerException {
		configurarInicioPlantilla(dto);
		dto = super.guardar(dto, token);
		return dto;
	}

	public DocumentoPlantillaDTO consultarPorCodigo(String codigo) throws ServerException {
		if (codigo == null || codigo.isEmpty())
			throw new ServerException("Es obligatorio colocar la plantilla");
		DocumentoPlantillaFilterDTO filtro = new DocumentoPlantillaFilterDTO();
		filtro.setCodigo(codigo);
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		return consultaUnica(filtro);
	}

	public List<DocumentoPlantillaDTO> listarPlantillaRol(DocumentoPlantillaFilterDTO dto, boolean todosPermisos)
			throws ServerException {
		if (dto == null || dto.getSecurityToken() == null)
			throw new ServerException("Revise la configuracion del dto filtro");
		try {
			if (todosPermisos) {
				return documentoPlantillaMapper.getProcessBoardsToMenu(dto);
			} else {
				return documentoPlantillaMapper.listarMenu(dto);
			}
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public DocumentoPlantillaDTO obtenerConfiguracionSinCampos(DocumentoPlantillaFilterDTO dto, boolean fullPermisos)
			throws ServerException {
		if (dto.getLlaveTabla() == null)
			throw new ServerException("No se puede realizar la consulta sin id de la plantilla");
		DocumentoPlantillaDTO plantilla = consultaUnica(dto);
		if (plantilla == null)
			throw new ServerException("Consulta de la plantilla incorrecta");
		if (plantilla.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
			throw new ServerException("La plantilla " + plantilla.getNombre() + "se encuentra inactiva");
		// plantilla.setSecurityToken(dto.getSecurityToken());
		if (fullPermisos) {
			plantilla.setPropiedades(cacheService.obtenerEspecialFullPermisos(dto.getLlaveTabla()));
		} else {
			plantilla.setPropiedades(obtenerPropiedadesPlantilla(plantilla.getLlaveTabla(), dto.getSecurityToken()));
		}
		if (plantilla.getPropiedades() == null || plantilla.getPropiedades().isEmpty())
			throw new ServerException("El usuario no tiene permiso sobre el documento " + plantilla.getNombre());
		return plantilla;
	}

	public List<PropiedadDTO> obtenerPropiedadesPlantilla(String plantilla, String token) throws ServerException {
		// si el token es null que traiga todos principalmente para copiar
		String usuario = null;
		if (token != null)
			usuario = getUserFlex(token);
		List<PropiedadDTO> propiedades = cacheService.obtenerPropiedades(PropiedadValorDefinidoDTO.PLANTILLA, plantilla,
				null, usuario);
		if (propiedades == null)
			propiedades = new ArrayList<PropiedadDTO>();
		return propiedades;
	}

	public void configurarInicioPlantilla(DocumentoPlantillaDTO dto) throws ServerException {
		// Coloco una imagen por defecto
		if (dto.getProceso() == null)
			throw new ServerException("Falta el proceso al cual pertence la plantilla");
		if (dto.getImagen() == null)
			dto.setImagen(SharedConstants.LOGO);
		if (dto.getCodigo() == null) {

			ProcesoDTO _process = procesoService.consultaXId(dto.getProceso());
			if (_process == null)
				throw new ServerException("La plantilla tiene un id de proceso que no corresponde");

			DocumentoPlantillaFilterDTO filtroCantidad = new DocumentoPlantillaFilterDTO();
			int cantidadCampos = contarResultados(filtroCantidad) + 1;
			dto.setCodigo(_process.getCodigo().substring(0, 2) + cantidadCampos);
			filtroCantidad = new DocumentoPlantillaFilterDTO();
			filtroCantidad.setCodigo(dto.getCodigo());
			if (consultaUnica(filtroCantidad) != null) {
				dto.setCodigo("P" + cantidadCampos);
			}
		}
		dto.setCodigo(D3Utils.formatFunction(dto.getCodigo()).toUpperCase());
	}

	public DocumentoPlantillaDTO createDeleteTemplate(String templateReferenceId, String token, String action)
			throws ServerException {
		DocumentoPlantillaDTO principalTemplate = consultaXId(templateReferenceId);
		DocumentoPlantillaDTO templateDelete = new DocumentoPlantillaDTO();
		templateDelete.setProceso(principalTemplate.getProceso());
		templateDelete.setNombre(principalTemplate.getNombre() + " - " + action.toString());
		templateDelete = guardar(templateDelete, token);
		crearCampoProcesos(templateDelete.getLlaveTabla(), token);
		crearCampoNombre(templateDelete.getLlaveTabla(), token);
		return templateDelete;
	}

	public DocumentoPlantillaDTO createReportTemplate(String templateReferenceId, String token) throws ServerException {
		DocumentoPlantillaDTO principalTemplate = consultaXId(templateReferenceId);
		DocumentoPlantillaDTO templateNew = new DocumentoPlantillaDTO();
		templateNew.setProceso(principalTemplate.getProceso());
		templateNew.setNombre(principalTemplate.getNombre() + " - INFORME");
		// templateDelete.setObjetivo(".");
		templateNew = guardar(templateNew, token);
		PropiedadDTO prop = Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA, templateNew.getLlaveTabla(),
				Propiedades.PLANTILLA_TIPO_REPORTE, templateReferenceId, token);
		prop.setUsuarioExcluyenteNombre(principalTemplate.getLlaveTabla());
		configuracionSvc.guardar(prop, token);
		return templateNew;
	}

	public DocumentoPlantillaDTO createUpdateTemplate(String templateReferenceId, String token) throws ServerException {
		DocumentoPlantillaDTO principalTemplate = consultaXId(templateReferenceId);
		DocumentoPlantillaDTO templateUpdate = new DocumentoPlantillaDTO();
		templateUpdate.setProceso(principalTemplate.getProceso());
		templateUpdate.setNombre(principalTemplate.getNombre() + " - UPDATE");
		templateUpdate.setObjetivo(".");
		templateUpdate.setCodigo(principalTemplate.getCodigo() + "_U");
		templateUpdate = guardar(templateUpdate, token);

		DocumentoPlantillaCaracteristicaDTO campoProceso = new DocumentoPlantillaCaracteristicaDTO();
		campoProceso.setCodigo("DOCUMENTO_DIFF");
		campoProceso.setNombre("DOCUMENTO");
		campoProceso.setFormato(DocumentoPlantillaCaracteristicaDTO.PROCESO);
		campoProceso.setOrden(1);
		campoProceso.setPlantilla(templateUpdate.getLlaveTabla());
		campoProceso.setObjetivo(".");
		campoProceso = caracteristicaService.guardar(campoProceso, token);
		configuracionSvc.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				campoProceso.getLlaveTabla(), Propiedades.PLANTILLA_AUXILIAR, templateReferenceId, token), token);
		// Copio campos
		principalTemplate.setCaracteristicas(
				caracteristicaService.listarCamposPlantilla(principalTemplate.getLlaveTabla(), null));
		for (DocumentoPlantillaCaracteristicaDTO iCampo : principalTemplate.getCaracteristicas()) {
			caracteristicaService.createFieldDifference(iCampo, templateUpdate.getLlaveTabla(), token);
			// newCampo.setPropiedades(configuracionSvc.copiarPropiedades(iCampo.getPropiedades(),
			// newCampo.getLlaveTabla(), token));
		}

		PropiedadDTO historico = Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA,
				templateUpdate.getLlaveTabla(), Propiedades.PERIODO_LIMPIEZA_HISTORICO, "60", token);
		historico.setFechaInicial(new Date());
		historico.setMotivo("Historico " + templateUpdate.getNombre());
		historico.setTexto("00:00:07:00:00");
		configuracionSvc.guardar(historico, token);
		configuracionSvc.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA,
				templateUpdate.getLlaveTabla(), Propiedades.SOLICITAR_FECHAS, "1", token), token);
		return templateUpdate;
	}

	public String crearCampoProcesos(String plantilla, String token) throws ServerException {
		return caracteristicaService.crearCampoProcesos(plantilla, token);
	}

	private String crearCampoNombre(String plantilla, String token) throws ServerException {
		return caracteristicaService.crearCampoMotivo(plantilla, token);
	}

	private List<ProcesoEstadoDTO> crearEstadosBasicos() throws ServerException {
		List<ProcesoEstadoDTO> estados;
		ProcesoEstadoDTO activo = new ProcesoEstadoDTO();
		activo.setEstadoDocumento(SharedConstants.STATE_ACTIVE);
		activo.setNombre("ACTIVO");
		ProcesoEstadoDTO inactivo = new ProcesoEstadoDTO();
		inactivo.setEstadoDocumento(SharedConstants.STATE_INACTIVE);
		inactivo.setNombre("INACTIVO");
		estados = new ArrayList<ProcesoEstadoDTO>();
		estados.add(activo);
		estados.add(inactivo);
		return estados;
	}

	private List<DocumentoPlantillaDTO> listarPlantillasUsuario(DocumentoPlantillaFilterDTO pDTO, String pProfile)
			throws ServerException {

		String _user = null;
		if (pDTO.getSecurityToken() != null)
			_user = getUserFlex(pDTO.getSecurityToken());
		List<DocumentoPlantillaDTO> plantillasPermitidas = listarPlantillaRol(pDTO, (pProfile != null));
		List<DocumentoPlantillaDTO> _resultTemplates = new ArrayList<DocumentoPlantillaDTO>();
		boolean nuevaPlantilla = true;
		if (plantillasPermitidas != null && plantillasPermitidas.size() != 0) {
			// obtengo todo y didtribuyo para evitar tantas consultas a la BD y para
			// optimizar tiempo

			List<ReporteBaseDTO> _reports = reporteService.listarMenu();

			ProcesoEstadoFilterDTO filtroEstado = new ProcesoEstadoFilterDTO();
			filtroEstado.setEstado(SharedConstants.STATE_ACTIVE);
			filtroEstado.setTipo(ProcesoEstadoDTO.TIPO_ESTADO);
			filtroEstado.setPaginacionRegistroFinal(5000);
			List<ProcesoEstadoDTO> estados = estadoService.listarConsulta(filtroEstado);

			ProcesoTransicionFilterDTO filtroTransicion = new ProcesoTransicionFilterDTO();
			filtroTransicion.setSecurityToken((pProfile != null) ? null : pDTO.getSecurityToken());
			filtroTransicion.setEstado(SharedConstants.STATE_ACTIVE);

			List<ProcesoTransicionDTO> transiciones = transicionService.listarTransicionesRol(filtroTransicion);
			List<ProcesoTransicionDTO> transicionesIniciales = transicionService.listarTransaccionesIniciales(null,
					null);

			List<PropiedadDTO> todasPropiedadesEvitandoConsultaBD = null;
			// Aqui colqoue la restriccion, se supone que siempre viene perfil con un
			// usuario
			if (pProfile != null && _user != null) {
				todasPropiedadesEvitandoConsultaBD = cacheService
						.obtenerEspecialFullPermisosSimplificandoBD(plantillasPermitidas, pProfile, _user);
				todasPropiedadesEvitandoConsultaBD = configuracionSvc
						.clearResponseProperties(todasPropiedadesEvitandoConsultaBD);
			} else {
				todasPropiedadesEvitandoConsultaBD = cacheService.listarPlantillasSimplificar(plantillasPermitidas,
						_user);
				todasPropiedadesEvitandoConsultaBD = configuracionSvc
						.clearResponseProperties(todasPropiedadesEvitandoConsultaBD);
				List<PropiedadDTO> todasPropiedadesReportesOcultos = cacheService.obtenerPropiedadesSinEntidad(
						PropiedadValorDefinidoDTO.REPORTE, null, Propiedades.OCULTAR_REPORTE, _user);
				if (todasPropiedadesReportesOcultos != null && !todasPropiedadesReportesOcultos.isEmpty()) {
					for (PropiedadDTO propiedadDTO : todasPropiedadesReportesOcultos) {
						for (ReporteBaseDTO iReport : _reports) {
							if (iReport.getLlaveTabla().compareTo(propiedadDTO.getCampo()) == 0) {
								_reports.remove(iReport);
								break;
							}
						}
					}
				}
			}
			List<PropiedadDTO> todasPropiedadesEstados = cacheService
					.obtenerPropiedadesSinEntidad(PropiedadValorDefinidoDTO.ESTADO, null, null, _user);
			todasPropiedadesEstados = configuracionSvc.clearResponseProperties(todasPropiedadesEstados);
			List<PropiedadDTO> todasPropiedadesReportes = cacheService.obtenerPropiedadesSinEntidad(
					PropiedadValorDefinidoDTO.REPORTE, null, Propiedades.REP_VISIBLE_STATE, _user);
			todasPropiedadesReportes = configuracionSvc.clearResponseProperties(todasPropiedadesReportes);
			List<PropiedadDTO> propiedadesReporteAutoPrint = cacheService.obtenerPropiedadesSinEntidad(
					PropiedadValorDefinidoDTO.REPORTE, null, Propiedades.REP_AUTOPRINT, _user);
			if (propiedadesReporteAutoPrint != null && !propiedadesReporteAutoPrint.isEmpty())
				todasPropiedadesReportes.addAll(configuracionSvc.clearResponseProperties(propiedadesReporteAutoPrint));

			List<PropiedadDTO> _propAccountTemplate = cacheService.obtenerPropiedadesSinEntidad(
					PropiedadValorDefinidoDTO.API_SERVICE, null, Propiedades.TEMPLATE_VOUCHER, _user);
			_propAccountTemplate = configuracionSvc.clearResponseProperties(_propAccountTemplate);

			for (DocumentoPlantillaDTO iplantillaPermitida : plantillasPermitidas) {
				nuevaPlantilla = true;
				for (DocumentoPlantillaDTO iBD : _resultTemplates) {
					if (iplantillaPermitida.getLlaveTabla() == null
							|| iplantillaPermitida.getLlaveTabla().compareTo(iBD.getLlaveTabla()) == 0) {
						nuevaPlantilla = false;
						break;
					}
				}
				if (nuevaPlantilla) {
					// iplantillaPermitida.setSecurityToken(dto.getSecurityToken());
					if (iplantillaPermitida.getLlaveTabla() == null)
						throw new ServerException("No se puede realizar la consulta sin id de la plantilla");
					if (iplantillaPermitida.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
						throw new ServerException(
								"La plantilla " + iplantillaPermitida.getNombre() + "se encuentra inactiva");
					iplantillaPermitida.setPropiedades(new ArrayList<PropiedadDTO>());
					for (PropiedadDTO propiedadDTO : todasPropiedadesEvitandoConsultaBD) {
						if (propiedadDTO.getCampo().compareTo(iplantillaPermitida.getLlaveTabla()) == 0)
							iplantillaPermitida.getPropiedades().add(propiedadDTO);
					}

					for (PropiedadDTO _iProrTemplateVoucher : _propAccountTemplate) {
						if (_iProrTemplateVoucher.getValor().compareTo(iplantillaPermitida.getLlaveTabla()) == 0) {
							iplantillaPermitida.getPropiedades().add(_iProrTemplateVoucher);
						}
					}

					if (!iplantillaPermitida.getPropiedades().isEmpty()) {
						// throw new ServerException("El usuario no tiene permiso sobre el documento " +
						// iplantillaPermitida.getNombre());
						String procesoInicial = null;
						if (transicionesIniciales != null && !transicionesIniciales.isEmpty()) {
							for (ProcesoTransicionDTO procesoTransicionDTO : transicionesIniciales) {
								if (procesoTransicionDTO.getPlantilla()
										.compareTo(iplantillaPermitida.getLlaveTabla()) == 0) {
									procesoInicial = procesoTransicionDTO.getProceso();
									break;
								}
							}
						}
						if (procesoInicial != null) {
							statesFromProcess(estados, transiciones, todasPropiedadesEstados, iplantillaPermitida,
									procesoInicial);
							PropiedadDTO propertyTemplateStartProcess = new PropiedadDTO();
							propertyTemplateStartProcess.setKey(Propiedades.PLANTILLA_INICIA_PROCESO);
							iplantillaPermitida.getPropiedades().add(propertyTemplateStartProcess);
						}
						if (iplantillaPermitida.getEstados() == null)
							iplantillaPermitida.setEstados(crearEstadosBasicos());

						// iplantillaPermitida.setReportes(reporteService.listarDisponiblesDocumento(iplantillaPermitida.getLlaveTabla(),
						// false));
						if (_reports != null && !_reports.isEmpty()) {
							for (ReporteBaseDTO reporteBaseDTO : _reports) {
								if (reporteBaseDTO.getPlantilla().compareTo(iplantillaPermitida.getLlaveTabla()) == 0) {
									reporteBaseDTO.setPropiedades(new ArrayList<>());
									for (PropiedadDTO propiedadDTO : todasPropiedadesReportes) {
										if (propiedadDTO.getCampo().compareTo(reporteBaseDTO.getLlaveTabla()) == 0)
											reporteBaseDTO.getPropiedades().add(propiedadDTO);
									}
									if (iplantillaPermitida.getReportes() == null)
										iplantillaPermitida.setReportes(new ArrayList<ReporteBaseDTO>());
									iplantillaPermitida.getReportes().add(reporteBaseDTO);
								}
							}
						}
						_resultTemplates.add(iplantillaPermitida);
					}
				}
			}
			for (int i = plantillasPermitidas.size() - 1; i >= 0; i--) {
				DocumentoPlantillaDTO iplantillaPermitida = plantillasPermitidas.get(i);
				if (iplantillaPermitida.getLlaveTabla() == null) {
					statesFromProcess(estados, transiciones, todasPropiedadesEstados, iplantillaPermitida,
							iplantillaPermitida.getProceso());
					_resultTemplates.add(0, iplantillaPermitida);
				}
			}
		}
		System.out.format("%s \n[%s] Listando (%s) plantillas ", new Date(), _user, _resultTemplates.size());
		return _resultTemplates;
	}

	private void statesFromProcess(List<ProcesoEstadoDTO> estados, List<ProcesoTransicionDTO> transiciones,
			List<PropiedadDTO> todasPropiedadesEstados, DocumentoPlantillaDTO iplantillaPermitida,
			String procesoInicial) {
		// Los tableros de control por el momento no tienen estado
		if (procesoInicial == null)
			return;
		for (ProcesoEstadoDTO procesoEstadoDTO : estados) {
			if (procesoEstadoDTO.getProceso().compareTo(procesoInicial) == 0) {
				if (iplantillaPermitida.getEstados() == null)
					iplantillaPermitida.setEstados(new ArrayList<ProcesoEstadoDTO>());
				if (procesoEstadoDTO.getPropiedades() == null) {
					procesoEstadoDTO.setPropiedades(new ArrayList<PropiedadDTO>());
					for (PropiedadDTO propiedadDTO : todasPropiedadesEstados) {
						if (propiedadDTO.getCampo().compareTo(procesoEstadoDTO.getLlaveTabla()) == 0)
							procesoEstadoDTO.getPropiedades().add(propiedadDTO);
					}
				}
				if (procesoEstadoDTO.getTransiciones() == null) {
					procesoEstadoDTO.setTransiciones(new ArrayList<ProcesoTransicionDTO>());
					for (ProcesoTransicionDTO procesoTransicionDTO : transiciones) {
						if (procesoTransicionDTO.getEstadoPartida() != null && procesoTransicionDTO.getEstadoPartida()
								.compareTo(procesoEstadoDTO.getLlaveTabla()) == 0) {
							procesoEstadoDTO.getTransiciones().add(procesoTransicionDTO);
						}
					}
				}
				// Valida que quede empty no null, despues de validar borra
				iplantillaPermitida.getEstados().add(procesoEstadoDTO);
			}
		}
	}

	public List<DocumentoPlantillaDTO> getFullToSynchronize(List<String> process) {
		return documentoPlantillaMapper.getFullToSynchronize(process);
	}

	public List<DocumentoPlantillaDTO> getTemplateofCategoriesReplace() throws ServerException {
		return documentoPlantillaMapper.getTemplateofCategoriesReplace();
	}

}