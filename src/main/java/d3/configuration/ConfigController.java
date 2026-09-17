package d3.configuration;

import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import d3.accounting.application.base.IndicatorService;
import d3.accounting.domain.DatoTablaDTO;
import d3.accounting.domain.IndicatorDTO;
import d3.accounting.domain.IndicatorFilterDTO;
import d3.accounting.domain.IndicatorResultadoDTO;
import d3.accounting.domain.PeriodoDTO;
import d3.authentication.application.OrganizacionSvc;
import d3.authentication.domain.OrganizacionDTO;
import d3.authentication.domain.OrganizacionFilterDTO;
import d3.authorization.application.RolAccesoSvc;
import d3.authorization.domain.RolAccesoDTO;
import d3.authorization.domain.RolAccesoFilterDTO;
import d3.configuration.application.ExportConfigurationFileService;
import d3.configuration.application.ImportConfigurationFileService;
import d3.configuration.application.PropiedadSvc;
import d3.configuration.application.PropiedadValorDefinidoSvc;
import d3.configuration.application.RelacionInternaSvc;
import d3.configuration.application.SincronizacionArbolSvc;
import d3.configuration.domain.ArbolConfiguracionFilterDTO;
import d3.configuration.domain.CompararArbolRequestDTO;
import d3.configuration.domain.DiferenciaDTO;
import d3.configuration.domain.ExportListRequest;
import d3.configuration.domain.PropiedadDTO;
import d3.configuration.domain.PropiedadFilterDTO;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.configuration.domain.PropiedadValorDefinidoFilterDTO;
import d3.configuration.domain.RelacionInternaDTO;
import d3.configuration.domain.RelacionInternaFilterDTO;
import d3.configuration.domain.SincronizacionSeleccionadaDTO;
import d3.configuration.domain.TreeNodeDTO;
import d3.mail.application.MailUserSendMessage;
import d3.mail.application.MensajePlantillaCorreoSvc;
import d3.mail.application.MensajeSvc;
import d3.mail.domain.MensajeDTO;
import d3.mail.domain.MensajeFilterDTO;
import d3.mail.domain.MensajePlantillaCorreoDTO;
import d3.mail.domain.MensajePlantillaCorreoFilterDTO;
import d3.process.application.ConsecutivoSvc;
import d3.process.application.DocumentoPlantillaCaracteristicaSvc;
import d3.process.application.DocumentoPlantillaSvc;
import d3.process.application.ProcesoSvc;
import d3.process.application.ProcesoTransicionAutomaticaSvc;
import d3.process.application.ProcesoTransicionSvc;
import d3.process.domain.ConsecutivoDTO;
import d3.process.domain.ConsecutivoFilterDTO;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.process.domain.DocumentoPlantillaCaracteristicaFilterDTO;
import d3.process.domain.DocumentoPlantillaDTO;
import d3.process.domain.DocumentoPlantillaFilterDTO;
import d3.process.domain.ProcesoDTO;
import d3.process.domain.ProcesoFilterDTO;
import d3.process.domain.ProcesoTransicionAutomaticaDTO;
import d3.process.domain.ProcesoTransicionAutomaticaFilterDTO;
import d3.process.domain.ProcesoTransicionDTO;
import d3.process.domain.ProcesoTransicionFilterDTO;
import d3.process.domain.TemplateDTO;
import d3.report.application.ReporteBaseSvc;
import d3.report.domain.ReporteBaseDTO;
import d3.report.domain.ReporteBaseFilterDTO;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.upload.domain.CargaArchivoDTO;
import d3.users.application.ServidorSvc;
import d3.users.application.UsuarioSvc;
import d3.users.domain.ServidorDTO;
import d3.users.domain.ServidorFilterDTO;
import d3.users.domain.UsuarioDTO;
import d3.users.domain.UsuarioFilterDTO;
import d3.webservice.application.WebServiceEjecucionSvc;
import d3.webservice.application.WebServiceSvc;
import d3.webservice.domain.WebServiceDTO;
import d3.webservice.domain.WebServiceEjecucionDTO;
import d3.webservice.domain.WebServiceEjecucionFilterDTO;
import d3.webservice.domain.WebServiceFilterDTO;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

	private final ConsecutivoSvc consecutivoService;
	private final PropiedadValorDefinidoSvc propiedadValorDefinidoService;
	private final OrganizacionSvc organizacionService;
	private final ServidorSvc servidorService;
	private final WebServiceSvc webServiceService;
	private final WebServiceEjecucionSvc webServiceEjecucionService;
	private final MensajeSvc mensajeService;
	private final MensajePlantillaCorreoSvc mensajePlantillaCorreoService;
	private final MailUserSendMessage mailUserSendMessage;
	private final ProcesoTransicionAutomaticaSvc procesoTransicionAutomaticaService;
	private final DocumentoPlantillaSvc documentoPlantillaService;
	private final DocumentoPlantillaCaracteristicaSvc documentoPlantillaCaracteristicaService;
	private final ReporteBaseSvc reporteBaseService;
	private final ProcesoSvc procesoService;
	private final ProcesoTransicionSvc procesoTransicionService;
	private final PropiedadSvc propiedadService;
	private final RelacionInternaSvc relacionInternaService;
	private final ExportConfigurationFileService exportService;
	private final ImportConfigurationFileService importService;
	private final SincronizacionArbolSvc sincronizacionArbolService;
	private final PropiedadValorDefinidoSvc propertyTypeService;
	private final IndicatorService indicadorService;
	private final RolAccesoSvc rolAccesoService;
	private final UsuarioSvc usuarioService;

	public ConfigController(@Lazy ConsecutivoSvc consecutivoService,
			@Lazy PropiedadValorDefinidoSvc propiedadValorDefinidoService, @Lazy OrganizacionSvc organizacionService,
			@Lazy ServidorSvc servidorService, @Lazy WebServiceSvc webServiceService,
			@Lazy WebServiceEjecucionSvc webServiceEjecucionService, @Lazy MensajeSvc mensajeService,
			@Lazy MensajePlantillaCorreoSvc mensajePlantillaCorreoService,
			@Lazy MailUserSendMessage mailUserSendMessage,
			@Lazy ProcesoTransicionAutomaticaSvc procesoTransicionAutomaticaService,
			@Lazy DocumentoPlantillaSvc documentoPlantillaService,
			@Lazy DocumentoPlantillaCaracteristicaSvc documentoPlantillaCaracteristicaService,
			@Lazy ReporteBaseSvc reporteBaseService, @Lazy ProcesoSvc procesoService,
			@Lazy ProcesoTransicionSvc procesoTransicionService, @Lazy PropiedadSvc propiedadService,
			@Lazy RelacionInternaSvc relacionInternaService, @Lazy ExportConfigurationFileService exportService,
			@Lazy ImportConfigurationFileService importService, @Lazy SincronizacionArbolSvc sincronizacionArbolService,
			@Lazy IndicatorService indicadorService, @Lazy RolAccesoSvc rolAccesoService,
			@Lazy UsuarioSvc usuarioService) {
		this.consecutivoService = consecutivoService;
		this.propiedadValorDefinidoService = propiedadValorDefinidoService;
		this.organizacionService = organizacionService;
		this.servidorService = servidorService;
		this.webServiceService = webServiceService;
		this.webServiceEjecucionService = webServiceEjecucionService;
		this.mensajeService = mensajeService;
		this.mensajePlantillaCorreoService = mensajePlantillaCorreoService;
		this.mailUserSendMessage = mailUserSendMessage;
		this.procesoTransicionAutomaticaService = procesoTransicionAutomaticaService;
		this.documentoPlantillaService = documentoPlantillaService;
		this.documentoPlantillaCaracteristicaService = documentoPlantillaCaracteristicaService;
		this.reporteBaseService = reporteBaseService;
		this.procesoService = procesoService;
		this.procesoTransicionService = procesoTransicionService;
		this.propiedadService = propiedadService;
		this.relacionInternaService = relacionInternaService;
		this.exportService = exportService;
		this.importService = importService;
		this.sincronizacionArbolService = sincronizacionArbolService;
		this.propertyTypeService = propiedadValorDefinidoService;
		this.indicadorService = indicadorService;
		this.rolAccesoService = rolAccesoService;
		this.usuarioService = usuarioService;
	}

	private void limpiarFiltro(Object filter) {
		if (filter == null)
			return;
		try {
			for (java.lang.reflect.Field f : filter.getClass().getDeclaredFields()) {
				if (f.getType() == String.class) {
					f.setAccessible(true);
					Object val = f.get(filter);
					if (val != null && ((String) val).isEmpty()) {
						f.set(filter, null);
					}
				}
			}
			Class<?> superClass = filter.getClass().getSuperclass();
			while (superClass != null && superClass != Object.class) {
				for (java.lang.reflect.Field f : superClass.getDeclaredFields()) {
					if (f.getType() == String.class) {
						f.setAccessible(true);
						Object val = f.get(filter);
						if (val != null && ((String) val).isEmpty()) {
							f.set(filter, null);
						}
					}
				}
				superClass = superClass.getSuperclass();
			}
		} catch (Exception ignored) {
		}
	}

	// ==================== CONSECUTIVES ====================

	@PostMapping("/consecutives/list")
	public List<ConsecutivoDTO> listarConsecutivos(@RequestBody ConsecutivoFilterDTO filter) throws ServerException {
		limpiarFiltro(filter);
		return consecutivoService.listarConsulta(filter);
	}

	@PostMapping("/consecutives/{key}")
	public ConsecutivoDTO consultarConsecutivo(@PathVariable("key") String pKey) throws ServerException {
		return consecutivoService.consultaXId(pKey);
	}

	@PostMapping("/consecutives/create")
	public ConsecutivoDTO guardarConsecutivo(@RequestBody ConsecutivoDTO dto) throws ServerException {
		return consecutivoService.guardar(dto);
	}

	@PostMapping("/consecutives/update")
	public ConsecutivoDTO actualizarConsecutivo(@RequestBody ConsecutivoDTO dto) throws ServerException {
		return consecutivoService.actualizar(dto);
	}

	@PostMapping("/consecutives/{key}/inactivate")
	public ConsecutivoDTO inactivarConsecutivo(@RequestBody ConsecutivoDTO dto) throws ServerException {
		return consecutivoService.inactivar(dto);
	}

	@PostMapping("/consecutives/{key}/assign")
	public ConsecutivoDTO asignarConsecutivo(@RequestBody ConsecutivoDTO dto) throws ServerException {
		return consecutivoService.asignarConsecutivo(dto);
	}

	// ==================== PROPERTY VALUES ====================

	@PostMapping("/property-values/list")
	public List<PropiedadValorDefinidoDTO> listarValoresDefinidos(@RequestBody PropiedadValorDefinidoFilterDTO filter)
			throws ServerException {
		limpiarFiltro(filter);
		return propiedadValorDefinidoService.listarConsulta(filter);
	}

	@PostMapping("/property-values/{key}")
	public PropiedadValorDefinidoDTO consultarValorDefinido(@PathVariable("key") String pKey) throws ServerException {
		return propiedadValorDefinidoService.consultaXId(pKey);
	}

	@PostMapping("/property-values/by-origen")
	public List<PropiedadValorDefinidoDTO> listarPorOrigen(@RequestBody PropiedadValorDefinidoFilterDTO filter)
			throws ServerException {
		limpiarFiltro(filter);
		return propiedadValorDefinidoService.listarConsulta(filter);
	}

	@PostMapping("/property-values/create")
	public PropiedadValorDefinidoDTO guardarValorDefinido(@RequestBody PropiedadValorDefinidoDTO dto)
			throws ServerException {
		return propiedadValorDefinidoService.guardar(dto);
	}

	@PostMapping("/property-values/update")
	public PropiedadValorDefinidoDTO actualizarValorDefinido(@RequestBody PropiedadValorDefinidoDTO dto)
			throws ServerException {
		return propiedadValorDefinidoService.actualizar(dto);
	}

	@PostMapping("/property-values/{key}/inactivate")
	public PropiedadValorDefinidoDTO inactivarValorDefinido(@RequestBody PropiedadValorDefinidoDTO dto)
			throws ServerException {
		return propiedadValorDefinidoService.inactivar(dto);
	}

	// ==================== ORGANIZATIONS ====================

	@PostMapping("/organizations/list")
	public List<OrganizacionDTO> listarOrganizaciones(@RequestBody OrganizacionFilterDTO filter)
			throws ServerException {
		limpiarFiltro(filter);
		return organizacionService.listarConsulta(filter);
	}

	@PostMapping("/organizations/{key}")
	public OrganizacionDTO consultarOrganizacion(@PathVariable("key") String pKey) throws ServerException {
		return organizacionService.consultaXId(pKey);
	}

	@PostMapping("/organizations/principal")
	public OrganizacionDTO obtenerPrincipal() throws ServerException {
		return organizacionService.obtenerPrincipal();
	}

	@PostMapping("/organizations/create")
	public OrganizacionDTO guardarOrganizacion(@RequestBody OrganizacionDTO dto) throws ServerException {
		return organizacionService.guardar(dto);
	}

	@PostMapping("/organizations/update")
	public OrganizacionDTO actualizarOrganizacion(@RequestBody OrganizacionDTO dto) throws ServerException {
		return organizacionService.actualizar(dto);
	}

	@PostMapping("/organizations/{key}/inactivate")
	public OrganizacionDTO inactivarOrganizacion(@RequestBody OrganizacionDTO dto) throws ServerException {
		return organizacionService.inactivar(dto);
	}

	// ==================== SERVERS ====================

	@PostMapping("/servers/list")
	public List<ServidorDTO> listarServidores(@RequestBody ServidorFilterDTO filter) throws ServerException {
		limpiarFiltro(filter);
		return servidorService.listarConsulta(filter);
	}

	@PostMapping("/servers/{key}")
	public ServidorDTO consultarServidor(@PathVariable("key") String pKey) throws ServerException {
		return servidorService.consultaXId(pKey);
	}

	@PostMapping("/servers/create")
	public ServidorDTO guardarServidor(@RequestBody ServidorDTO dto) throws ServerException {
		return servidorService.guardar(dto);
	}

	@PostMapping("/servers/update")
	public ServidorDTO actualizarServidor(@RequestBody ServidorDTO dto) throws ServerException {
		return servidorService.actualizar(dto);
	}

	@PostMapping("/servers/{key}/inactivate")
	public ServidorDTO inactivarServidor(@RequestBody ServidorDTO dto) throws ServerException {
		return servidorService.inactivar(dto);
	}

	// ==================== WEB SERVICES ====================

	@PostMapping("/web-services/list")
	public List<WebServiceDTO> listarWebServices(@RequestBody WebServiceFilterDTO filter) throws ServerException {
		limpiarFiltro(filter);
		return webServiceService.listarConsulta(filter);
	}

	@PostMapping("/web-services/{key}")
	public WebServiceDTO consultarWebService(@PathVariable("key") String pKey) throws ServerException {
		return webServiceService.consultaXId(pKey);
	}

	@PostMapping("/web-services/create")
	public WebServiceDTO guardarWebService(@RequestBody WebServiceDTO dto) throws ServerException {
		return webServiceService.guardar(dto);
	}

	@PostMapping("/web-services/update")
	public WebServiceDTO actualizarWebService(@RequestBody WebServiceDTO dto) throws ServerException {
		return webServiceService.actualizar(dto);
	}

	@PostMapping("/web-services/{key}/inactivate")
	public WebServiceDTO inactivarWebService(@RequestBody WebServiceDTO dto) throws ServerException {
		return webServiceService.inactivar(dto);
	}

	public static class EjecutarWSRequest {
		private String parametros;

		public String getParametros() {
			return parametros;
		}

		public void setParametros(String parametros) {
			this.parametros = parametros;
		}
	}

	@PostMapping("/web-services/{key}/execute")
	public WebServiceEjecucionDTO ejecutarWebService(@PathVariable("key") String pKey,
			@RequestBody EjecutarWSRequest request) throws ServerException {
		WebServiceEjecucionDTO ejecucion = new WebServiceEjecucionDTO();
		ejecucion.setServicio(pKey);
		ejecucion.setParametros(request.getParametros());
		ejecucion.setSincrona("A");
		ejecucion = webServiceEjecucionService.guardar(ejecucion);

		WebServiceEjecucionFilterDTO filter = new WebServiceEjecucionFilterDTO();
		filter.setLlaveTabla(ejecucion.getLlaveTabla());
		return webServiceEjecucionService.ejecutarAPI(filter);
	}

	@PostMapping("/web-services/executions")
	public List<WebServiceEjecucionDTO> listarEjecuciones(@RequestBody WebServiceEjecucionFilterDTO filter)
			throws ServerException {
		if (filter.getFechaEjecucionMin() == null || filter.getFechaEjecucionMax() == null) {
			throw new ServerException("Las fechas Desde y Hasta son obligatorias para consultar ejecuciones");
		}
		limpiarFiltro(filter);
		return webServiceEjecucionService.listarConsulta(filter);
	}

	@PostMapping("/web-services/{webServiceKey}/executions")
	public List<WebServiceEjecucionDTO> listarEjecucionesPorWebService(
			@PathVariable("webServiceKey") String pWebServiceKey, @RequestBody WebServiceEjecucionFilterDTO filter)
			throws ServerException {
		limpiarFiltro(filter);
		filter.setServicio(pWebServiceKey);
		return webServiceEjecucionService.listarConsulta(filter);
	}

	// ==================== MESSAGES ====================

	public static class MensajesUsuarioRequest {
		private String estado;
		private Date fechaDesde;
		private Date fechaHasta;
		private String enviado;
		private String usuario;
		private String titulo;
		private Integer paginacionRegistroInicial;
		private Integer paginacionRegistroFinal;

		public String getEstado() {
			return estado;
		}

		public void setEstado(String estado) {
			this.estado = estado;
		}

		public Date getFechaDesde() {
			return fechaDesde;
		}

		public void setFechaDesde(Date fechaDesde) {
			this.fechaDesde = fechaDesde;
		}

		public Date getFechaHasta() {
			return fechaHasta;
		}

		public void setFechaHasta(Date fechaHasta) {
			this.fechaHasta = fechaHasta;
		}

		public String getEnviado() {
			return enviado;
		}

		public void setEnviado(String enviado) {
			this.enviado = enviado;
		}

		public String getUsuario() {
			return usuario;
		}

		public void setUsuario(String usuario) {
			this.usuario = usuario;
		}

		public String getTitulo() {
			return titulo;
		}

		public void setTitulo(String titulo) {
			this.titulo = titulo;
		}

		public Integer getPaginacionRegistroInicial() {
			return paginacionRegistroInicial;
		}

		public void setPaginacionRegistroInicial(Integer paginacionRegistroInicial) {
			this.paginacionRegistroInicial = paginacionRegistroInicial;
		}

		public Integer getPaginacionRegistroFinal() {
			return paginacionRegistroFinal;
		}

		public void setPaginacionRegistroFinal(Integer paginacionRegistroFinal) {
			this.paginacionRegistroFinal = paginacionRegistroFinal;
		}
	}

	@PostMapping("/messages/list")
	public List<MensajeDTO> listarMensajes(@RequestBody MensajesUsuarioRequest request) throws ServerException {
		if (request.getFechaDesde() == null || request.getFechaHasta() == null) {
			throw new ServerException("Las fechas Desde y Hasta son obligatorias para consultar mensajes");
		}
		limpiarFiltro(request);
		MensajeFilterDTO filter = new MensajeFilterDTO();
		filter.setEstado(request.getEstado());
		filter.setFechaMin(request.getFechaDesde());
		filter.setFechaMax(request.getFechaHasta());
		filter.setUsuario(request.getUsuario());
		filter.setTitulo(request.getTitulo());
		filter.setPaginacionRegistroInicial(request.getPaginacionRegistroInicial());
		filter.setPaginacionRegistroFinal(request.getPaginacionRegistroFinal());

		if ("1".equals(request.getEnviado())) {
			filter.setCorreoEnviadoMin(new Date(0));
		} else if ("0".equals(request.getEnviado())) {
			filter.setCorreoEnviadoMax(new Date(0));
		}
		return mensajeService.mensajesUsuario(filter);
	}

	@PostMapping("/messages/{key}")
	public MensajeDTO consultarMensaje(@PathVariable("key") String pKey) throws ServerException {
		return mensajeService.consultaXId(pKey);
	}

	@PostMapping("/messages/{key}/resend")
	public MensajeDTO reenviarMensaje(@PathVariable("key") String pKey) throws ServerException {
		MensajeFilterDTO filter = new MensajeFilterDTO();
		filter.setLlaveTabla(pKey);
		return mailUserSendMessage.call(filter);
	}

	// ==================== MESSAGE TEMPLATES ====================

	@PostMapping("/message-templates/list")
	public List<MensajePlantillaCorreoDTO> listarPlantillasCorreo(@RequestBody MensajePlantillaCorreoFilterDTO filter)
			throws ServerException {
		limpiarFiltro(filter);
		return mensajePlantillaCorreoService.listarConsulta(filter);
	}

	@PostMapping("/message-templates/{key}")
	public MensajePlantillaCorreoDTO consultarPlantillaCorreo(@PathVariable("key") String pKey) throws ServerException {
		return mensajePlantillaCorreoService.consultaXId(pKey);
	}

	@PostMapping("/message-templates/create")
	public MensajePlantillaCorreoDTO guardarPlantillaCorreo(@RequestBody MensajePlantillaCorreoDTO dto)
			throws ServerException {
		return mensajePlantillaCorreoService.guardar(dto);
	}

	@PostMapping("/message-templates/update")
	public MensajePlantillaCorreoDTO actualizarPlantillaCorreo(@RequestBody MensajePlantillaCorreoDTO dto)
			throws ServerException {
		return mensajePlantillaCorreoService.actualizar(dto);
	}

	@PostMapping("/message-templates/{key}/inactivate")
	public MensajePlantillaCorreoDTO inactivarPlantillaCorreo(@RequestBody MensajePlantillaCorreoDTO dto)
			throws ServerException {
		return mensajePlantillaCorreoService.inactivar(dto);
	}

	// ==================== AUTO TASKS ====================

	public static class AutoTaskFilterRequest {
		private String estado;
		private String transicion;
		private String plantilla;
		private String plantillaNombre;
		private String propiedad;
		private Date fechaDesde;
		private Date fechaHasta;
		private Boolean activa;
		private Integer paginacionRegistroInicial;
		private Integer paginacionRegistroFinal;

		public String getEstado() {
			return estado;
		}

		public void setEstado(String estado) {
			this.estado = estado;
		}

		public String getTransicion() {
			return transicion;
		}

		public void setTransicion(String transicion) {
			this.transicion = transicion;
		}

		public String getPlantilla() {
			return plantilla;
		}

		public void setPlantilla(String plantilla) {
			this.plantilla = plantilla;
		}

		public String getPlantillaNombre() {
			return plantillaNombre;
		}

		public void setPlantillaNombre(String plantillaNombre) {
			this.plantillaNombre = plantillaNombre;
		}

		public String getPropiedad() {
			return propiedad;
		}

		public void setPropiedad(String propiedad) {
			this.propiedad = propiedad;
		}

		public Date getFechaDesde() {
			return fechaDesde;
		}

		public void setFechaDesde(Date fechaDesde) {
			this.fechaDesde = fechaDesde;
		}

		public Date getFechaHasta() {
			return fechaHasta;
		}

		public void setFechaHasta(Date fechaHasta) {
			this.fechaHasta = fechaHasta;
		}

		public Boolean getActiva() {
			return activa;
		}

		public void setActiva(Boolean activa) {
			this.activa = activa;
		}

		public Integer getPaginacionRegistroInicial() {
			return paginacionRegistroInicial;
		}

		public void setPaginacionRegistroInicial(Integer paginacionRegistroInicial) {
			this.paginacionRegistroInicial = paginacionRegistroInicial;
		}

		public Integer getPaginacionRegistroFinal() {
			return paginacionRegistroFinal;
		}

		public void setPaginacionRegistroFinal(Integer paginacionRegistroFinal) {
			this.paginacionRegistroFinal = paginacionRegistroFinal;
		}
	}

	@PostMapping("/auto-tasks/list")
	public List<ProcesoTransicionAutomaticaDTO> listarTareasAutomaticas(@RequestBody AutoTaskFilterRequest request)
			throws ServerException {
		limpiarFiltro(request);
		ProcesoTransicionAutomaticaFilterDTO filter = new ProcesoTransicionAutomaticaFilterDTO();
		filter.setEstado(request.getEstado());
		filter.setTransicion(request.getTransicion());
		filter.setPlantilla(request.getPlantilla());
		filter.setPlantillaNombre(request.getPlantillaNombre());
		filter.setPropiedad(request.getPropiedad());
		filter.setFechaMin(request.getFechaDesde());
		filter.setFechaMax(request.getFechaHasta());
		filter.setPaginacionRegistroInicial(request.getPaginacionRegistroInicial());
		filter.setPaginacionRegistroFinal(request.getPaginacionRegistroFinal());
		return procesoTransicionAutomaticaService.listarConsulta(filter);
	}

	@PostMapping("/auto-tasks/{key}")
	public ProcesoTransicionAutomaticaDTO consultarTareaAutomatica(@PathVariable("key") String pKey)
			throws ServerException {
		return procesoTransicionAutomaticaService.consultaXId(pKey);
	}

	@PostMapping("/auto-tasks/create")
	public ProcesoTransicionAutomaticaDTO guardarTareaAutomatica(@RequestBody ProcesoTransicionAutomaticaDTO dto)
			throws ServerException {
		return procesoTransicionAutomaticaService.guardar(dto);
	}

	@PostMapping("/auto-tasks/update")
	public ProcesoTransicionAutomaticaDTO actualizarTareaAutomatica(@RequestBody ProcesoTransicionAutomaticaDTO dto)
			throws ServerException {
		return procesoTransicionAutomaticaService.actualizar(dto);
	}

	@PostMapping("/auto-tasks/{key}/inactivate")
	public ProcesoTransicionAutomaticaDTO inactivarTareaAutomatica(@RequestBody ProcesoTransicionAutomaticaDTO dto)
			throws ServerException {
		return procesoTransicionAutomaticaService.inactivar(dto);
	}

	public static class ProgramarRequest {
		private String tipo;
		private String cron;
		private Date fecha;

		public String getTipo() {
			return tipo;
		}

		public void setTipo(String tipo) {
			this.tipo = tipo;
		}

		public String getCron() {
			return cron;
		}

		public void setCron(String cron) {
			this.cron = cron;
		}

		public Date getFecha() {
			return fecha;
		}

		public void setFecha(Date fecha) {
			this.fecha = fecha;
		}
	}

	@PostMapping("/auto-tasks/{key}/schedule")
	public ProcesoTransicionAutomaticaDTO programarTareaAutomatica() throws ServerException {
		return procesoTransicionAutomaticaService.programar();
	}

	@PostMapping("/auto-tasks/{key}/execute")
	public ProcesoTransicionAutomaticaDTO ejecutarTareaAutomatica(@PathVariable("key") String pKey)
			throws ServerException {
		ProcesoTransicionAutomaticaDTO dto = new ProcesoTransicionAutomaticaDTO();
		dto.setLlaveTabla(pKey);
		return procesoTransicionAutomaticaService.ejecutar(dto);
	}

	// ==================== DOCUMENT TEMPLATES ====================

	@PostMapping("/document-templates/list")
	public List<DocumentoPlantillaDTO> listarPlantillas(@RequestBody DocumentoPlantillaFilterDTO filter)
			throws ServerException {
		limpiarFiltro(filter);
		return documentoPlantillaService.listarConsulta(filter);
	}

	@PostMapping("/document-templates/{key}")
	public DocumentoPlantillaDTO consultarPlantilla(@PathVariable("key") String pKey) throws ServerException {
		return documentoPlantillaService.consultaXId(pKey);
	}

	@PostMapping("/document-templates/admin")
	public List<DocumentoPlantillaDTO> listarPlantillasAdministrador(@RequestBody DocumentoPlantillaFilterDTO filter)
			throws ServerException {
		limpiarFiltro(filter);
		return documentoPlantillaService.listarConsulta(filter);
	}

	@PostMapping("/document-templates/create")
	public DocumentoPlantillaDTO guardarPlantilla(@RequestBody DocumentoPlantillaDTO dto) throws ServerException {
		return documentoPlantillaService.guardar(dto);
	}

	@PostMapping("/document-templates/update")
	public DocumentoPlantillaDTO actualizarPlantilla(@RequestBody DocumentoPlantillaDTO dto) throws ServerException {
		return documentoPlantillaService.actualizar(dto);
	}

	@PostMapping("/document-templates/{key}/inactivate")
	public DocumentoPlantillaDTO inactivarPlantilla(@RequestBody DocumentoPlantillaDTO dto) throws ServerException {
		return documentoPlantillaService.inactivar(dto);
	}

	@PostMapping("/document-templates/{key}/duplicate")
	public DocumentoPlantillaDTO duplicarPlantilla(@PathVariable("key") String pKey) throws ServerException {
		DocumentoPlantillaDTO dto = new DocumentoPlantillaDTO();
		dto.setLlaveTabla(pKey);
		return documentoPlantillaService.duplicar(dto);
	}

	@PostMapping("/document-templates/fields-complete")
	public TemplateDTO obtenerCamposPlantilla(@RequestBody TemplateDTO dto) throws ServerException {
		return documentoPlantillaService.obtenerCampos(dto, true);
	}

	// --- Fields (Caracteristicas) ---

	@PostMapping("/document-templates/{templateKey}/fields")
	public List<DocumentoPlantillaCaracteristicaDTO> listarCamposPlantilla(
			@PathVariable("templateKey") String pTemplateKey,
			@RequestBody DocumentoPlantillaCaracteristicaFilterDTO filter) throws ServerException {
		limpiarFiltro(filter);
		filter.setPlantilla(pTemplateKey);
		filter.setPaginacionRegistroFinal(500);
		return documentoPlantillaCaracteristicaService.listarConsulta(filter);
	}

	@PostMapping("/document-templates/fields/{key}")
	public DocumentoPlantillaCaracteristicaDTO consultarCampo(@PathVariable("key") String pKey) throws ServerException {
		return documentoPlantillaCaracteristicaService.consultaXId(pKey);
	}

	@PostMapping("/document-templates/fields")
	public DocumentoPlantillaCaracteristicaDTO guardarCampo(@RequestBody DocumentoPlantillaCaracteristicaDTO dto)
			throws ServerException {
		return documentoPlantillaCaracteristicaService.guardar(dto);
	}

	@PostMapping("/document-templates/fields/{key}/update")
	public DocumentoPlantillaCaracteristicaDTO actualizarCampo(@RequestBody DocumentoPlantillaCaracteristicaDTO dto)
			throws ServerException {
		return documentoPlantillaCaracteristicaService.actualizar(dto);
	}

	@PostMapping("/document-templates/fields/{key}/inactivate")
	public DocumentoPlantillaCaracteristicaDTO inactivarCampo(@RequestBody DocumentoPlantillaCaracteristicaDTO dto)
			throws ServerException {
		return documentoPlantillaCaracteristicaService.inactivar(dto);
	}

	// --- Reports ---

	@PostMapping("/document-templates/{templateKey}/reports")
	public List<ReporteBaseDTO> listarReportesPlantilla(@PathVariable("templateKey") String pTemplateKey,
			@RequestBody ReporteBaseFilterDTO filter) throws ServerException {
		limpiarFiltro(filter);
		filter.setPlantilla(pTemplateKey);
		return reporteBaseService.listarConsulta(filter);
	}

	@PostMapping("/document-templates/reports/{key}")
	public ReporteBaseDTO consultarReporte(@PathVariable("key") String pKey) throws ServerException {
		return reporteBaseService.consultaXId(pKey);
	}

	@PostMapping("/document-templates/reports")
	public ReporteBaseDTO guardarReporte(@RequestBody ReporteBaseDTO dto) throws ServerException {
		return reporteBaseService.guardar(dto);
	}

	@PostMapping("/document-templates/reports/{key}/update")
	public ReporteBaseDTO actualizarReporte(@RequestBody ReporteBaseDTO dto) throws ServerException {
		return reporteBaseService.actualizar(dto);
	}

	@PostMapping("/document-templates/reports/{key}/inactivate")
	public ReporteBaseDTO inactivarReporte(@RequestBody ReporteBaseDTO dto) throws ServerException {
		return reporteBaseService.inactivar(dto);
	}

	// ==================== PROCESSES ====================

	@PostMapping("/processes/list")
	public List<ProcesoDTO> listarProcesos(@RequestBody ProcesoFilterDTO filter) throws ServerException {
		limpiarFiltro(filter);
		return procesoService.listarConsulta(filter);
	}

	@PostMapping("/processes/{key}")
	public ProcesoDTO consultarProceso(@PathVariable("key") String pKey) throws ServerException {
		return procesoService.consultaXId(pKey);
	}

	@PostMapping("/processes/tree")
	public List<ProcesoDTO> consultarArbol(@RequestBody ProcesoFilterDTO filter) throws ServerException {
		limpiarFiltro(filter);
		return procesoService.consultarArbol(filter);
	}

	@PostMapping("/processes/{key}/graph")
	public ProcesoDTO obtenerProcesoParaGraficar(@PathVariable("key") String pKey) throws ServerException {
		ProcesoFilterDTO filter = new ProcesoFilterDTO();
		filter.setLlaveTabla(pKey);
		return procesoService.obtenerProcesoParaGraficar(filter);
	}

	@PostMapping("/processes/create")
	public ProcesoDTO guardarProceso(@RequestBody ProcesoDTO dto) throws ServerException {
		return procesoService.guardar(dto);
	}

	@PostMapping("/processes/update")
	public ProcesoDTO actualizarProceso(@RequestBody ProcesoDTO dto) throws ServerException {
		return procesoService.actualizar(dto);
	}

	@PostMapping("/processes/{key}/inactivate")
	public ProcesoDTO inactivarProceso(@RequestBody ProcesoDTO dto) throws ServerException {
		return procesoService.inactivar(dto);
	}

	// --- Transitions ---

	@PostMapping("/processes/{processKey}/transitions")
	public List<ProcesoTransicionDTO> listarTransiciones(@PathVariable("processKey") String pProcessKey,
			@RequestBody ProcesoTransicionFilterDTO filter) throws ServerException {
		limpiarFiltro(filter);
		filter.setProceso(pProcessKey);
		return procesoTransicionService.listarConsulta(filter);
	}

	@PostMapping("/processes/transitions/{key}")
	public ProcesoTransicionDTO consultarTransicion(@PathVariable("key") String pKey) throws ServerException {
		return procesoTransicionService.consultaXId(pKey);
	}

	@PostMapping("/processes/transitions")
	public ProcesoTransicionDTO guardarTransicion(@RequestBody ProcesoTransicionDTO dto) throws ServerException {
		return procesoTransicionService.guardar(dto);
	}

	@PostMapping("/processes/transitions/{key}/update")
	public ProcesoTransicionDTO actualizarTransicion(@RequestBody ProcesoTransicionDTO dto) throws ServerException {
		return procesoTransicionService.actualizar(dto);
	}

	@PostMapping("/processes/transitions/{key}/inactivate")
	public ProcesoTransicionDTO inactivarTransicion(@RequestBody ProcesoTransicionDTO dto) throws ServerException {
		return procesoTransicionService.inactivar(dto);
	}

	// ==================== PROPERTIES ====================

	public static class PropertyListRequest {
		private String estado;
		private String campo;
		private String tipo;
		private String nombre;
		private String valor;
		private Integer paginacionRegistroInicial;
		private Integer paginacionRegistroFinal;

		public String getEstado() {
			return estado;
		}

		public void setEstado(String estado) {
			this.estado = estado;
		}

		public String getCampo() {
			return campo;
		}

		public void setCampo(String campo) {
			this.campo = campo;
		}

		public String getTipo() {
			return tipo;
		}

		public void setTipo(String tipo) {
			this.tipo = tipo;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public String getValor() {
			return valor;
		}

		public void setValor(String valor) {
			this.valor = valor;
		}

		public Integer getPaginacionRegistroInicial() {
			return paginacionRegistroInicial;
		}

		public void setPaginacionRegistroInicial(Integer paginacionRegistroInicial) {
			this.paginacionRegistroInicial = paginacionRegistroInicial;
		}

		public Integer getPaginacionRegistroFinal() {
			return paginacionRegistroFinal;
		}

		public void setPaginacionRegistroFinal(Integer paginacionRegistroFinal) {
			this.paginacionRegistroFinal = paginacionRegistroFinal;
		}
	}

	@PostMapping("/properties/list")
	public List<PropiedadDTO> listarPropiedades(@RequestBody PropertyListRequest request) throws ServerException {
		limpiarFiltro(request);
		PropiedadFilterDTO filter = new PropiedadFilterDTO();
		filter.setEstado(request.getEstado());
		filter.setCampo(request.getCampo());
		filter.setTipo(request.getTipo());
		filter.setNombre(request.getNombre());
		filter.setValor(request.getValor());
		filter.setPaginacionRegistroInicial(request.getPaginacionRegistroInicial());
		filter.setPaginacionRegistroFinal(request.getPaginacionRegistroFinal());
		return propiedadService.listarConsulta(filter);
	}

	public static class PropertyByIdRequest {
		private String campo;
		private String estado;

		public String getCampo() {
			return campo;
		}

		public void setCampo(String campo) {
			this.campo = campo;
		}

		public String getEstado() {
			return estado;
		}

		public void setEstado(String estado) {
			this.estado = estado;
		}
	}

	@PostMapping("/properties/by-id")
	public PropiedadDTO consultarPropiedad(@RequestBody PropertyByIdRequest request) throws ServerException {
		return propiedadService.consultaXId(request.getCampo());
	}

	@PostMapping("/properties/create")
	public PropiedadDTO guardarPropiedad(@RequestBody PropiedadDTO dto) throws ServerException {
		return propiedadService.guardar(dto);
	}

	@PostMapping("/properties/update")
	public PropiedadDTO actualizarPropiedad(@RequestBody PropiedadDTO dto) throws ServerException {
		return propiedadService.actualizar(dto);
	}

	@PostMapping("/properties/inactivate")
	public PropiedadDTO inactivarPropiedad(@RequestBody PropiedadDTO dto) throws ServerException {
		return propiedadService.inactivar(dto);
	}

	// --- Relations ---

	@PostMapping("/properties/{propiedad}/relations")
	public List<RelacionInternaDTO> listarRelaciones(@PathVariable("propiedad") String pPropiedad,
			@RequestBody RelacionInternaFilterDTO filter) throws ServerException {
		limpiarFiltro(filter);
		return relacionInternaService.relacionesPropiedad(pPropiedad);
	}

	@PostMapping("/properties/relations/create")
	public RelacionInternaDTO guardarRelacion(@RequestBody RelacionInternaDTO dto) throws ServerException {
		return relacionInternaService.guardar(dto);
	}

	@PostMapping("/properties/relations/update")
	public RelacionInternaDTO actualizarRelacion(@RequestBody RelacionInternaDTO dto) throws ServerException {
		return relacionInternaService.actualizar(dto);
	}

	@PostMapping("/properties/relations/inactivate")
	public RelacionInternaDTO inactivarRelacion(@RequestBody RelacionInternaDTO dto) throws ServerException {
		return relacionInternaService.inactivar(dto);
	}

	// ==================== ROLES (acceso) ====================

	@PostMapping("/roles/list")
	public List<RolAccesoDTO> listarRoles(@RequestBody RolAccesoFilterDTO filter) throws ServerException {
		limpiarFiltro(filter);
		if (filter.getEstado() == null || filter.getEstado().isEmpty()) {
			filter.setEstado(SharedConstants.STATE_ACTIVE);
		}
		return rolAccesoService.listarConsulta(filter);
	}

	// ==================== USERS (búsqueda usuario propietario)
	// ====================

	@PostMapping("/users/search")
	public List<UsuarioDTO> listarUsuarios(@RequestBody UsuarioFilterDTO filter) throws ServerException {
		limpiarFiltro(filter);
		if (filter.getEstado() == null || filter.getEstado().isEmpty()) {
			filter.setEstado(SharedConstants.STATE_ACTIVE);
		}
		return usuarioService.listarConsulta(filter);
	}

	@PostMapping("/users/by-id")
	public UsuarioDTO consultarUsuario(@RequestBody String key) throws ServerException {
		return usuarioService.consultaXId(key);
	}

	// ==================== CONFIGURATION IMPORT/EXPORT (antes /configuration/*)
	// ====================

	@GetMapping("/configuration/export")
	private CargaArchivoDTO generateFileWithConfiguration() throws ServerException {
		return exportService.call();
	}

	@PostMapping("/configuration/module")
	private CargaArchivoDTO generateFileWithConfigurationModule(@RequestBody ExportListRequest modules)
			throws ServerException {
		return exportService.call(modules);
	}

	@PostMapping("/configuration/import")
	private CargaArchivoDTO loadConfiguration(@RequestBody CargaArchivoDTO file) throws ServerException {
		return importService.call(file);
	}

	@PostMapping("/configuration/compare")
	private CargaArchivoDTO compare(@RequestBody CargaArchivoDTO file) throws ServerException {
		return importService.compare(file);
	}

	// ==================== CONFIGURATION TREE (arbol generico) ====================

	@PostMapping("/tree")
	private TreeNodeDTO getConfigTree(@RequestBody ArbolConfiguracionFilterDTO filter) throws ServerException {
		return sincronizacionArbolService.obtenerArbol(filter);
	}

	@PostMapping("/tree/export")
	private CargaArchivoDTO exportConfigTree(@RequestBody ArbolConfiguracionFilterDTO filter) throws ServerException {
		return sincronizacionArbolService.exportarArbol(filter);
	}

	@PostMapping("/tree/compare")
	private List<DiferenciaDTO> compareTree(@RequestBody CompararArbolRequestDTO request) throws ServerException {
		return sincronizacionArbolService.compararArbol(request.getArbol(), request.getFilter());
	}

	@PostMapping("/tree/sync")
	private CargaArchivoDTO syncTree(@RequestBody SincronizacionSeleccionadaDTO request) throws ServerException {
		return sincronizacionArbolService.sincronizar(request);
	}

	// ==================== PROPERTY LOOKUP (antes /property/*) ====================

	@GetMapping("/property/{type}/{field}")
	public List<PropiedadDTO> getFullProperties(@PathVariable(name = "type") String pType,
			@PathVariable(name = "field") String pField) throws ServerException {
		PropiedadFilterDTO filter = new PropiedadFilterDTO();
		filter.setTipo(pType);
		filter.setCampo(pField);
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		return propiedadService.listarConsulta(filter);
	}

	@GetMapping("/property/type/{type}/{filterName}")
	public List<PropiedadValorDefinidoDTO> getTypeProperty(@PathVariable(name = "type") String pType,
			@PathVariable(name = "filterName") String pFilterName) throws ServerException {
		PropiedadValorDefinidoFilterDTO filter = new PropiedadValorDefinidoFilterDTO();
		filter.setOrigen(pType);
		filter.setFiltroParametro(pFilterName);
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		return propertyTypeService.listarConsulta(filter);
	}

	@GetMapping("/property/{key}")
	public PropiedadDTO getPropertyByKey(@PathVariable(name = "key") String pKey) throws ServerException {
		return propiedadService.consultaXId(pKey);
	}

	@PostMapping("/property/")
	public PropiedadDTO createProperty(@RequestBody PropiedadDTO property) throws ServerException {
		return propiedadService.guardar(property);
	}

	// ==================== INDICATORS ====================

	@PostMapping("/indicators/list")
	public List<IndicatorDTO> listarIndicadores(@RequestBody IndicatorFilterDTO filter) throws ServerException {
		limpiarFiltro(filter);
		return indicadorService.getMany(filter);
	}

	@PostMapping("/indicators/{key}")
	public IndicatorDTO consultarIndicador(@PathVariable("key") String pKey) throws ServerException {
		return indicadorService.getById(pKey);
	}

	public static class IndicadorResultadoRequest {
		private String indicadorId;
		private PeriodoDTO periodo;

		public String getIndicadorId() {
			return indicadorId;
		}

		public void setIndicadorId(String indicadorId) {
			this.indicadorId = indicadorId;
		}

		public PeriodoDTO getPeriodo() {
			return periodo;
		}

		public void setPeriodo(PeriodoDTO periodo) {
			this.periodo = periodo;
		}
	}

	@PostMapping("/indicators/result")
	public IndicatorResultadoDTO obtenerResultadoIndicador(@RequestBody IndicadorResultadoRequest request)
			throws ServerException {
		return indicadorService.getResultado(request.getIndicadorId(), request.getPeriodo());
	}

	@PostMapping("/indicators/{key}/table")
	public List<DatoTablaDTO> obtenerTablaIndicador(@PathVariable("key") String pKey) throws ServerException {
		return indicadorService.getTabla(pKey);
	}

	@PostMapping("/indicators/create")
	public IndicatorDTO crearIndicador(@RequestBody IndicatorDTO dto) throws ServerException {
		indicadorService.save(dto);
		return dto;
	}

	@PostMapping("/indicators/update")
	public IndicatorDTO actualizarIndicador(@RequestBody IndicatorDTO dto) throws ServerException {
		indicadorService.update(dto);
		return dto;
	}

	@PostMapping("/indicators/inactivate")
	public IndicatorDTO inactivarIndicador(@RequestBody IndicatorDTO dto) throws ServerException {
		if (dto == null || dto.getKey() == null)
			throw new ServerException("La llave del indicador se encuentra vacia");
		return indicadorService.delete(dto.getKey());
	}
}
