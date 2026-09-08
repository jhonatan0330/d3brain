package d3.configuration;

import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
import d3.users.domain.ServidorDTO;
import d3.users.domain.ServidorFilterDTO;
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

	public ConfigController(
			@Lazy ConsecutivoSvc consecutivoService,
			@Lazy PropiedadValorDefinidoSvc propiedadValorDefinidoService,
			@Lazy OrganizacionSvc organizacionService,
			@Lazy ServidorSvc servidorService,
			@Lazy WebServiceSvc webServiceService,
			@Lazy WebServiceEjecucionSvc webServiceEjecucionService,
			@Lazy MensajeSvc mensajeService,
			@Lazy MensajePlantillaCorreoSvc mensajePlantillaCorreoService,
			@Lazy MailUserSendMessage mailUserSendMessage,
			@Lazy ProcesoTransicionAutomaticaSvc procesoTransicionAutomaticaService,
			@Lazy DocumentoPlantillaSvc documentoPlantillaService,
			@Lazy DocumentoPlantillaCaracteristicaSvc documentoPlantillaCaracteristicaService,
			@Lazy ReporteBaseSvc reporteBaseService,
			@Lazy ProcesoSvc procesoService,
			@Lazy ProcesoTransicionSvc procesoTransicionService,
			@Lazy PropiedadSvc propiedadService,
			@Lazy RelacionInternaSvc relacionInternaService,
			@Lazy ExportConfigurationFileService exportService,
			@Lazy ImportConfigurationFileService importService,
			@Lazy SincronizacionArbolSvc sincronizacionArbolService,
			@Lazy IndicatorService indicadorService) {
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
	}

	private void limpiarFiltro(Object filter) {
		if (filter == null) return;
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
	public ConsecutivoDTO guardarConsecutivo(@RequestBody ConsecutivoDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return consecutivoService.guardar(dto, token);
	}

	@PostMapping("/consecutives/update")
	public ConsecutivoDTO actualizarConsecutivo(@RequestBody ConsecutivoDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return consecutivoService.actualizar(dto, token);
	}

	@PostMapping("/consecutives/{key}/inactivate")
	public ConsecutivoDTO inactivarConsecutivo(@RequestBody ConsecutivoDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return consecutivoService.inactivar(dto, token);
	}

	@PostMapping("/consecutives/{key}/assign")
	public ConsecutivoDTO asignarConsecutivo(@RequestBody ConsecutivoDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return consecutivoService.asignarConsecutivo(dto, token);
	}

	// ==================== PROPERTY VALUES ====================

	@PostMapping("/property-values/list")
	public List<PropiedadValorDefinidoDTO> listarValoresDefinidos(
			@RequestBody PropiedadValorDefinidoFilterDTO filter) throws ServerException {
		limpiarFiltro(filter);
		return propiedadValorDefinidoService.listarConsulta(filter);
	}

	@PostMapping("/property-values/{key}")
	public PropiedadValorDefinidoDTO consultarValorDefinido(@PathVariable("key") String pKey) throws ServerException {
		return propiedadValorDefinidoService.consultaXId(pKey);
	}

	@PostMapping("/property-values/by-origen")
	public List<PropiedadValorDefinidoDTO> listarPorOrigen(
			@RequestBody PropiedadValorDefinidoFilterDTO filter) throws ServerException {
		limpiarFiltro(filter);
		return propiedadValorDefinidoService.listarConsulta(filter);
	}

	@PostMapping("/property-values/create")
	public PropiedadValorDefinidoDTO guardarValorDefinido(@RequestBody PropiedadValorDefinidoDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return propiedadValorDefinidoService.guardar(dto, token);
	}

	@PostMapping("/property-values/update")
	public PropiedadValorDefinidoDTO actualizarValorDefinido(@RequestBody PropiedadValorDefinidoDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return propiedadValorDefinidoService.actualizar(dto, token);
	}

	@PostMapping("/property-values/{key}/inactivate")
	public PropiedadValorDefinidoDTO inactivarValorDefinido(@RequestBody PropiedadValorDefinidoDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return propiedadValorDefinidoService.inactivar(dto, token);
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
	public OrganizacionDTO obtenerPrincipal(@RequestBody OrganizacionFilterDTO filter) throws ServerException {
		return organizacionService.obtenerPrincipal();
	}

	@PostMapping("/organizations/create")
	public OrganizacionDTO guardarOrganizacion(@RequestBody OrganizacionDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return organizacionService.guardar(dto, token);
	}

	@PostMapping("/organizations/update")
	public OrganizacionDTO actualizarOrganizacion(@RequestBody OrganizacionDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return organizacionService.actualizar(dto, token);
	}

	@PostMapping("/organizations/{key}/inactivate")
	public OrganizacionDTO inactivarOrganizacion(@RequestBody OrganizacionDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return organizacionService.inactivar(dto, token);
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
	public ServidorDTO guardarServidor(@RequestBody ServidorDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return servidorService.guardar(dto, token);
	}

	@PostMapping("/servers/update")
	public ServidorDTO actualizarServidor(@RequestBody ServidorDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return servidorService.actualizar(dto, token);
	}

	@PostMapping("/servers/{key}/inactivate")
	public ServidorDTO inactivarServidor(@RequestBody ServidorDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return servidorService.inactivar(dto, token);
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
	public WebServiceDTO guardarWebService(@RequestBody WebServiceDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return webServiceService.guardar(dto, token);
	}

	@PostMapping("/web-services/update")
	public WebServiceDTO actualizarWebService(@RequestBody WebServiceDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return webServiceService.actualizar(dto, token);
	}

	@PostMapping("/web-services/{key}/inactivate")
	public WebServiceDTO inactivarWebService(@RequestBody WebServiceDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return webServiceService.inactivar(dto, token);
	}

	public static class EjecutarWSRequest {
		private String parametros;
		public String getParametros() { return parametros; }
		public void setParametros(String parametros) { this.parametros = parametros; }
	}

	@PostMapping("/web-services/{key}/execute")
	public WebServiceEjecucionDTO ejecutarWebService(@PathVariable("key") String pKey,
			@RequestBody EjecutarWSRequest request,
			@RequestHeader("Authorization") String token) throws ServerException {
		WebServiceEjecucionDTO ejecucion = new WebServiceEjecucionDTO();
		ejecucion.setServicio(pKey);
		ejecucion.setParametros(request.getParametros());
		ejecucion.setSincrona("A");
		ejecucion = webServiceEjecucionService.guardar(ejecucion, token);

		WebServiceEjecucionFilterDTO filter = new WebServiceEjecucionFilterDTO();
		filter.setLlaveTabla(ejecucion.getLlaveTabla());
		filter.setSecurityToken(token);
		return webServiceEjecucionService.ejecutarAPI(filter);
	}

	@PostMapping("/web-services/executions")
	public List<WebServiceEjecucionDTO> listarEjecuciones(
			@RequestBody WebServiceEjecucionFilterDTO filter) throws ServerException {
		limpiarFiltro(filter);
		return webServiceEjecucionService.listarConsulta(filter);
	}

	@PostMapping("/web-services/{webServiceKey}/executions")
	public List<WebServiceEjecucionDTO> listarEjecucionesPorWebService(
			@PathVariable("webServiceKey") String pWebServiceKey,
			@RequestBody WebServiceEjecucionFilterDTO filter) throws ServerException {
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
		public String getEstado() { return estado; }
		public void setEstado(String estado) { this.estado = estado; }
		public Date getFechaDesde() { return fechaDesde; }
		public void setFechaDesde(Date fechaDesde) { this.fechaDesde = fechaDesde; }
		public Date getFechaHasta() { return fechaHasta; }
		public void setFechaHasta(Date fechaHasta) { this.fechaHasta = fechaHasta; }
		public String getEnviado() { return enviado; }
		public void setEnviado(String enviado) { this.enviado = enviado; }
		public String getUsuario() { return usuario; }
		public void setUsuario(String usuario) { this.usuario = usuario; }
		public String getTitulo() { return titulo; }
		public void setTitulo(String titulo) { this.titulo = titulo; }
		public Integer getPaginacionRegistroInicial() { return paginacionRegistroInicial; }
		public void setPaginacionRegistroInicial(Integer paginacionRegistroInicial) { this.paginacionRegistroInicial = paginacionRegistroInicial; }
		public Integer getPaginacionRegistroFinal() { return paginacionRegistroFinal; }
		public void setPaginacionRegistroFinal(Integer paginacionRegistroFinal) { this.paginacionRegistroFinal = paginacionRegistroFinal; }
	}

	@PostMapping("/messages/list")
	public List<MensajeDTO> listarMensajes(@RequestBody MensajesUsuarioRequest request) throws ServerException {
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
	public MensajeDTO reenviarMensaje(@PathVariable("key") String pKey,
			@RequestHeader("Authorization") String token) throws ServerException {
		MensajeFilterDTO filter = new MensajeFilterDTO();
		filter.setLlaveTabla(pKey);
		filter.setSecurityToken(token);
		return mailUserSendMessage.call(filter);
	}

	// ==================== MESSAGE TEMPLATES ====================

	@PostMapping("/message-templates/list")
	public List<MensajePlantillaCorreoDTO> listarPlantillasCorreo(
			@RequestBody MensajePlantillaCorreoFilterDTO filter) throws ServerException {
		limpiarFiltro(filter);
		return mensajePlantillaCorreoService.listarConsulta(filter);
	}

	@PostMapping("/message-templates/{key}")
	public MensajePlantillaCorreoDTO consultarPlantillaCorreo(@PathVariable("key") String pKey) throws ServerException {
		return mensajePlantillaCorreoService.consultaXId(pKey);
	}

	@PostMapping("/message-templates/create")
	public MensajePlantillaCorreoDTO guardarPlantillaCorreo(@RequestBody MensajePlantillaCorreoDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return mensajePlantillaCorreoService.guardar(dto, token);
	}

	@PostMapping("/message-templates/update")
	public MensajePlantillaCorreoDTO actualizarPlantillaCorreo(@RequestBody MensajePlantillaCorreoDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return mensajePlantillaCorreoService.actualizar(dto, token);
	}

	@PostMapping("/message-templates/{key}/inactivate")
	public MensajePlantillaCorreoDTO inactivarPlantillaCorreo(@RequestBody MensajePlantillaCorreoDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return mensajePlantillaCorreoService.inactivar(dto, token);
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
		public String getEstado() { return estado; }
		public void setEstado(String estado) { this.estado = estado; }
		public String getTransicion() { return transicion; }
		public void setTransicion(String transicion) { this.transicion = transicion; }
		public String getPlantilla() { return plantilla; }
		public void setPlantilla(String plantilla) { this.plantilla = plantilla; }
		public String getPlantillaNombre() { return plantillaNombre; }
		public void setPlantillaNombre(String plantillaNombre) { this.plantillaNombre = plantillaNombre; }
		public String getPropiedad() { return propiedad; }
		public void setPropiedad(String propiedad) { this.propiedad = propiedad; }
		public Date getFechaDesde() { return fechaDesde; }
		public void setFechaDesde(Date fechaDesde) { this.fechaDesde = fechaDesde; }
		public Date getFechaHasta() { return fechaHasta; }
		public void setFechaHasta(Date fechaHasta) { this.fechaHasta = fechaHasta; }
		public Boolean getActiva() { return activa; }
		public void setActiva(Boolean activa) { this.activa = activa; }
		public Integer getPaginacionRegistroInicial() { return paginacionRegistroInicial; }
		public void setPaginacionRegistroInicial(Integer paginacionRegistroInicial) { this.paginacionRegistroInicial = paginacionRegistroInicial; }
		public Integer getPaginacionRegistroFinal() { return paginacionRegistroFinal; }
		public void setPaginacionRegistroFinal(Integer paginacionRegistroFinal) { this.paginacionRegistroFinal = paginacionRegistroFinal; }
	}

	@PostMapping("/auto-tasks/list")
	public List<ProcesoTransicionAutomaticaDTO> listarTareasAutomaticas(
			@RequestBody AutoTaskFilterRequest request) throws ServerException {
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
	public ProcesoTransicionAutomaticaDTO consultarTareaAutomatica(@PathVariable("key") String pKey) throws ServerException {
		return procesoTransicionAutomaticaService.consultaXId(pKey);
	}

	@PostMapping("/auto-tasks/create")
	public ProcesoTransicionAutomaticaDTO guardarTareaAutomatica(
			@RequestBody ProcesoTransicionAutomaticaDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return procesoTransicionAutomaticaService.guardar(dto, token);
	}

	@PostMapping("/auto-tasks/update")
	public ProcesoTransicionAutomaticaDTO actualizarTareaAutomatica(
			@RequestBody ProcesoTransicionAutomaticaDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return procesoTransicionAutomaticaService.actualizar(dto, token);
	}

	@PostMapping("/auto-tasks/{key}/inactivate")
	public ProcesoTransicionAutomaticaDTO inactivarTareaAutomatica(
			@RequestBody ProcesoTransicionAutomaticaDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return procesoTransicionAutomaticaService.inactivar(dto, token);
	}

	public static class ProgramarRequest {
		private String tipo;
		private String cron;
		private Date fecha;
		public String getTipo() { return tipo; }
		public void setTipo(String tipo) { this.tipo = tipo; }
		public String getCron() { return cron; }
		public void setCron(String cron) { this.cron = cron; }
		public Date getFecha() { return fecha; }
		public void setFecha(Date fecha) { this.fecha = fecha; }
	}

	@PostMapping("/auto-tasks/{key}/schedule")
	public ProcesoTransicionAutomaticaDTO programarTareaAutomatica(
			@PathVariable("key") String pKey,
			@RequestBody ProgramarRequest request,
			@RequestHeader("Authorization") String token) throws ServerException {
		ProcesoTransicionAutomaticaDTO dto = new ProcesoTransicionAutomaticaDTO();
		dto.setLlaveTabla(pKey);
		dto.setPropiedad(request.getTipo());
		dto.setPlantilla(request.getCron());
		dto.setFecha(request.getFecha());
		return procesoTransicionAutomaticaService.programar(dto, token);
	}

	@PostMapping("/auto-tasks/{key}/execute")
	public ProcesoTransicionAutomaticaDTO ejecutarTareaAutomatica(
			@PathVariable("key") String pKey,
			@RequestHeader("Authorization") String token) throws ServerException {
		ProcesoTransicionAutomaticaDTO dto = new ProcesoTransicionAutomaticaDTO();
		dto.setLlaveTabla(pKey);
		return procesoTransicionAutomaticaService.ejecutar(dto, token);
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
	public List<TemplateDTO> listarPlantillasAdministrador(
			@RequestBody DocumentoPlantillaFilterDTO filter) throws ServerException {
		limpiarFiltro(filter);
		return documentoPlantillaService.consultaAdministrador(filter);
	}

	@PostMapping("/document-templates/create")
	public DocumentoPlantillaDTO guardarPlantilla(@RequestBody DocumentoPlantillaDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return documentoPlantillaService.guardar(dto, token);
	}

	@PostMapping("/document-templates/update")
	public DocumentoPlantillaDTO actualizarPlantilla(@RequestBody DocumentoPlantillaDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return documentoPlantillaService.actualizar(dto, token);
	}

	@PostMapping("/document-templates/{key}/inactivate")
	public DocumentoPlantillaDTO inactivarPlantilla(@RequestBody DocumentoPlantillaDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return documentoPlantillaService.inactivar(dto, token);
	}

	@PostMapping("/document-templates/{key}/duplicate")
	public DocumentoPlantillaDTO duplicarPlantilla(@PathVariable("key") String pKey,
			@RequestHeader("Authorization") String token) throws ServerException {
		DocumentoPlantillaDTO dto = new DocumentoPlantillaDTO();
		dto.setLlaveTabla(pKey);
		return documentoPlantillaService.duplicar(dto, token);
	}

	@PostMapping("/document-templates/{key}/fields-complete")
	public TemplateDTO obtenerCamposPlantilla(@PathVariable("key") String pKey,
			@RequestBody TemplateDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return documentoPlantillaService.obtenerCampos(dto, token, true);
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
	public DocumentoPlantillaCaracteristicaDTO guardarCampo(
			@RequestBody DocumentoPlantillaCaracteristicaDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return documentoPlantillaCaracteristicaService.guardar(dto, token);
	}

	@PostMapping("/document-templates/fields/{key}/update")
	public DocumentoPlantillaCaracteristicaDTO actualizarCampo(
			@RequestBody DocumentoPlantillaCaracteristicaDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return documentoPlantillaCaracteristicaService.actualizar(dto, token);
	}

	@PostMapping("/document-templates/fields/{key}/inactivate")
	public DocumentoPlantillaCaracteristicaDTO inactivarCampo(
			@RequestBody DocumentoPlantillaCaracteristicaDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return documentoPlantillaCaracteristicaService.inactivar(dto, token);
	}

	// --- Reports ---

	@PostMapping("/document-templates/{templateKey}/reports")
	public List<ReporteBaseDTO> listarReportesPlantilla(
			@PathVariable("templateKey") String pTemplateKey,
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
	public ReporteBaseDTO guardarReporte(@RequestBody ReporteBaseDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return reporteBaseService.guardar(dto, token);
	}

	@PostMapping("/document-templates/reports/{key}/update")
	public ReporteBaseDTO actualizarReporte(@RequestBody ReporteBaseDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return reporteBaseService.actualizar(dto, token);
	}

	@PostMapping("/document-templates/reports/{key}/inactivate")
	public ReporteBaseDTO inactivarReporte(@RequestBody ReporteBaseDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return reporteBaseService.inactivar(dto, token);
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
	public ProcesoDTO guardarProceso(@RequestBody ProcesoDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return procesoService.guardar(dto, token);
	}

	@PostMapping("/processes/update")
	public ProcesoDTO actualizarProceso(@RequestBody ProcesoDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return procesoService.actualizar(dto, token);
	}

	@PostMapping("/processes/{key}/inactivate")
	public ProcesoDTO inactivarProceso(@RequestBody ProcesoDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return procesoService.inactivar(dto, token);
	}

	// --- Transitions ---

	@PostMapping("/processes/{processKey}/transitions")
	public List<ProcesoTransicionDTO> listarTransiciones(
			@PathVariable("processKey") String pProcessKey,
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
	public ProcesoTransicionDTO guardarTransicion(@RequestBody ProcesoTransicionDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return procesoTransicionService.guardar(dto, token);
	}

	@PostMapping("/processes/transitions/{key}/update")
	public ProcesoTransicionDTO actualizarTransicion(@RequestBody ProcesoTransicionDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return procesoTransicionService.actualizar(dto, token);
	}

	@PostMapping("/processes/transitions/{key}/inactivate")
	public ProcesoTransicionDTO inactivarTransicion(@RequestBody ProcesoTransicionDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return procesoTransicionService.inactivar(dto, token);
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
		public String getEstado() { return estado; }
		public void setEstado(String estado) { this.estado = estado; }
		public String getCampo() { return campo; }
		public void setCampo(String campo) { this.campo = campo; }
		public String getTipo() { return tipo; }
		public void setTipo(String tipo) { this.tipo = tipo; }
		public String getNombre() { return nombre; }
		public void setNombre(String nombre) { this.nombre = nombre; }
		public String getValor() { return valor; }
		public void setValor(String valor) { this.valor = valor; }
		public Integer getPaginacionRegistroInicial() { return paginacionRegistroInicial; }
		public void setPaginacionRegistroInicial(Integer paginacionRegistroInicial) { this.paginacionRegistroInicial = paginacionRegistroInicial; }
		public Integer getPaginacionRegistroFinal() { return paginacionRegistroFinal; }
		public void setPaginacionRegistroFinal(Integer paginacionRegistroFinal) { this.paginacionRegistroFinal = paginacionRegistroFinal; }
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
		public String getCampo() { return campo; }
		public void setCampo(String campo) { this.campo = campo; }
		public String getEstado() { return estado; }
		public void setEstado(String estado) { this.estado = estado; }
	}

	@PostMapping("/properties/by-id")
	public PropiedadDTO consultarPropiedad(@RequestBody PropertyByIdRequest request,
			@RequestHeader("Authorization") String token) throws ServerException {
		return propiedadService.consultaXId(request.getCampo());
	}

	@PostMapping("/properties/create")
	public PropiedadDTO guardarPropiedad(@RequestBody PropiedadDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return propiedadService.guardar(dto, token);
	}

	@PostMapping("/properties/update")
	public PropiedadDTO actualizarPropiedad(@RequestBody PropiedadDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return propiedadService.actualizar(dto, token);
	}

	@PostMapping("/properties/inactivate")
	public PropiedadDTO inactivarPropiedad(@RequestBody PropiedadDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return propiedadService.inactivar(dto, token);
	}

	// --- Relations ---

	@PostMapping("/properties/{propiedad}/relations")
	public List<RelacionInternaDTO> listarRelaciones(@PathVariable("propiedad") String pPropiedad,
			@RequestBody RelacionInternaFilterDTO filter) throws ServerException {
		limpiarFiltro(filter);
		return relacionInternaService.relacionesPropiedad(pPropiedad);
	}

	@PostMapping("/properties/{propiedad}/relations/create")
	public RelacionInternaDTO guardarRelacion(@PathVariable("propiedad") String pPropiedad,
			@RequestBody RelacionInternaDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return relacionInternaService.guardar(dto, token);
	}

	@PostMapping("/properties/{propiedad}/relations/update")
	public RelacionInternaDTO actualizarRelacion(@PathVariable("propiedad") String pPropiedad,
			@RequestBody RelacionInternaDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return relacionInternaService.actualizar(dto, token);
	}

	@PostMapping("/properties/{propiedad}/relations/inactivate")
	public RelacionInternaDTO inactivarRelacion(@PathVariable("propiedad") String pPropiedad,
			@RequestBody RelacionInternaDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return relacionInternaService.inactivar(dto, token);
	}

	// ==================== CONFIGURATION IMPORT/EXPORT (antes /configuration/*) ====================

	@GetMapping("/configuration/export")
	private CargaArchivoDTO generateFileWithConfiguration(@RequestHeader("Authorization") String token) throws ServerException {
		return exportService.call(token);
	}

	@PostMapping("/configuration/module")
	private CargaArchivoDTO generateFileWithConfigurationModule(@RequestHeader("Authorization") String token,
			@RequestBody ExportListRequest modules) throws ServerException {
		return exportService.call(token, modules);
	}

	@PostMapping("/configuration/import")
	private CargaArchivoDTO loadConfiguration(@RequestHeader("Authorization") String token, @RequestBody CargaArchivoDTO file)
			throws ServerException {
		return importService.call(token, file);
	}

	@PostMapping("/configuration/compare")
	private CargaArchivoDTO compare(@RequestHeader("Authorization") String token, @RequestBody CargaArchivoDTO file)
			throws ServerException {
		return importService.compare(token, file);
	}

	// ==================== CONFIGURATION TREE (arbol generico) ====================

	@PostMapping("/tree")
	private TreeNodeDTO getConfigTree(@RequestHeader("Authorization") String token,
			@RequestBody ArbolConfiguracionFilterDTO filter) throws ServerException {
		return sincronizacionArbolService.obtenerArbol(token, filter);
	}

	@PostMapping("/tree/export")
	private CargaArchivoDTO exportConfigTree(@RequestHeader("Authorization") String token,
			@RequestBody ArbolConfiguracionFilterDTO filter) throws ServerException {
		return sincronizacionArbolService.exportarArbol(token, filter);
	}

	@PostMapping("/tree/compare")
	private List<DiferenciaDTO> compareTree(@RequestHeader("Authorization") String token,
			@RequestBody CompararArbolRequestDTO request) throws ServerException {
		return sincronizacionArbolService.compararArbol(token, request.getArbol(), request.getFilter());
	}

	@PostMapping("/tree/sync")
	private CargaArchivoDTO syncTree(@RequestHeader("Authorization") String token,
			@RequestBody SincronizacionSeleccionadaDTO request) throws ServerException {
		return sincronizacionArbolService.sincronizar(token, request);
	}

	// ==================== PROPERTY LOOKUP (antes /property/*) ====================

	@GetMapping("/property/{type}/{field}")
	public List<PropiedadDTO> getFullProperties(@RequestHeader("Authorization") String token,
			@PathVariable(name = "type") String pType, @PathVariable(name = "field") String pField)
			throws ServerException {
		PropiedadFilterDTO filter = new PropiedadFilterDTO();
		filter.setTipo(pType);
		filter.setCampo(pField);
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		return propiedadService.listarConsulta(filter);
	}

	@GetMapping("/property/type/{type}/{filterName}")
	public List<PropiedadValorDefinidoDTO> getTypeProperty(@RequestHeader("Authorization") String token,
			@PathVariable(name = "type") String pType, @PathVariable(name = "filterName") String pFilterName)
			throws ServerException {
		PropiedadValorDefinidoFilterDTO filter = new PropiedadValorDefinidoFilterDTO();
		filter.setOrigen(pType);
		filter.setFiltroParametro(pFilterName);
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		return propertyTypeService.listarConsulta(filter);
	}

	@GetMapping("/property/{key}")
	public PropiedadDTO getPropertyByKey(@RequestHeader("Authorization") String token,
			@PathVariable(name = "key") String pKey) throws ServerException {
		return propiedadService.consultaXId(pKey);
	}

	@PostMapping("/property/")
	public PropiedadDTO createProperty(@RequestHeader("Authorization") String token, @RequestBody PropiedadDTO property)
			throws ServerException {
		return propiedadService.guardar(property, token);
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
		public String getIndicadorId() { return indicadorId; }
		public void setIndicadorId(String indicadorId) { this.indicadorId = indicadorId; }
		public PeriodoDTO getPeriodo() { return periodo; }
		public void setPeriodo(PeriodoDTO periodo) { this.periodo = periodo; }
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

	@PostMapping("/indicators/{key}/inactivate")
	public IndicatorDTO inactivarIndicador(@PathVariable("key") String pKey, @RequestBody IndicatorDTO dto) throws ServerException {
		if (dto == null || dto.getKey() == null)
			throw new ServerException("La llave del indicador se encuentra vacia");
		return indicadorService.delete(dto.getKey());
	}
}
