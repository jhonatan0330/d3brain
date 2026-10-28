package d3.configuration;

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
import d3.configuration.application.ArbolConfiguracionSvc;
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
import d3.configuration.domain.ProcesoGraphDTO;
import d3.configuration.domain.PropiedadDTO;
import d3.configuration.domain.PropiedadFilterDTO;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.configuration.domain.PropiedadValorDefinidoFilterDTO;
import d3.configuration.domain.RelacionInternaDTO;
import d3.configuration.domain.RelacionInternaFilterDTO;
import d3.configuration.domain.SincronizacionSeleccionadaDTO;
import d3.configuration.domain.TreeNodeDTO;
import d3.process.application.ConsecutivoSvc;
import d3.process.application.DocumentoPlantillaCaracteristicaSvc;
import d3.process.application.DocumentoPlantillaSvc;
import d3.process.application.ProcesoEstadoSvc;
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
import d3.process.domain.ProcesoEstadoDTO;
import d3.process.domain.ProcesoEstadoFilterDTO;
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
import d3.webservice.application.MailUserSendMessage;
import d3.webservice.application.MensajeSvc;
import d3.webservice.application.WebServiceCopyAPI;
import d3.webservice.application.WebServiceEjecucionSvc;
import d3.webservice.application.WebServiceSvc;
import d3.webservice.domain.MensajeDTO;
import d3.webservice.domain.MensajeFilterDTO;
import d3.webservice.domain.WebServiceDTO;
import d3.webservice.domain.WebServiceEjecucionDTO;
import d3.webservice.domain.WebServiceEjecucionFilterDTO;
import d3.webservice.domain.WebServiceFilterDTO;

@RestController
@RequestMapping("/configuration")
public class ConfigurationController {

	private final ConsecutivoSvc consecutivoService;
	private final PropiedadValorDefinidoSvc propiedadValorDefinidoService;
	private final OrganizacionSvc organizacionService;
	private final ServidorSvc servidorService;
	private final WebServiceSvc webServiceService;
	private final WebServiceCopyAPI webServiceCopyAPI;
	private final WebServiceEjecucionSvc webServiceEjecucionService;
	private final MensajeSvc mensajeService;
	private final MailUserSendMessage mailUserSendMessage;
	private final ProcesoTransicionAutomaticaSvc procesoTransicionAutomaticaService;
	private final DocumentoPlantillaSvc documentoPlantillaService;
	private final DocumentoPlantillaCaracteristicaSvc documentoPlantillaCaracteristicaService;
	private final ReporteBaseSvc reporteBaseService;
	private final ProcesoSvc procesoService;
	private final ProcesoEstadoSvc procesoEstadoService;
	private final ProcesoTransicionSvc procesoTransicionService;
	private final PropiedadSvc propiedadService;
	private final RelacionInternaSvc relacionInternaService;
	private final ExportConfigurationFileService exportService;
	private final ImportConfigurationFileService importService;
	private final SincronizacionArbolSvc sincronizacionArbolService;
	private final ArbolConfiguracionSvc arbolConfiguracionService;
	private final PropiedadValorDefinidoSvc propertyTypeService;
	private final IndicatorService indicadorService;
	private final RolAccesoSvc rolAccesoService;
	private final UsuarioSvc usuarioService;

	public ConfigurationController(@Lazy ConsecutivoSvc consecutivoService,
			@Lazy PropiedadValorDefinidoSvc propiedadValorDefinidoService, @Lazy OrganizacionSvc organizacionService,
			@Lazy ServidorSvc servidorService, @Lazy WebServiceSvc webServiceService,
			@Lazy WebServiceCopyAPI webServiceCopyAPI,
			@Lazy WebServiceEjecucionSvc webServiceEjecucionService, @Lazy MensajeSvc mensajeService,
			@Lazy MailUserSendMessage mailUserSendMessage,
			@Lazy ProcesoTransicionAutomaticaSvc procesoTransicionAutomaticaService,
			@Lazy DocumentoPlantillaSvc documentoPlantillaService,
			@Lazy DocumentoPlantillaCaracteristicaSvc documentoPlantillaCaracteristicaService,
			@Lazy ReporteBaseSvc reporteBaseService, @Lazy ProcesoSvc procesoService,
			@Lazy ProcesoEstadoSvc procesoEstadoService,
			@Lazy ProcesoTransicionSvc procesoTransicionService, @Lazy PropiedadSvc propiedadService,
			@Lazy RelacionInternaSvc relacionInternaService, @Lazy ExportConfigurationFileService exportService,
			@Lazy ImportConfigurationFileService importService, @Lazy SincronizacionArbolSvc sincronizacionArbolService,
			@Lazy ArbolConfiguracionSvc arbolConfiguracionService,
			@Lazy IndicatorService indicadorService, @Lazy RolAccesoSvc rolAccesoService,
			@Lazy UsuarioSvc usuarioService) {
		this.consecutivoService = consecutivoService;
		this.propiedadValorDefinidoService = propiedadValorDefinidoService;
		this.organizacionService = organizacionService;
		this.servidorService = servidorService;
		this.webServiceService = webServiceService;
		this.webServiceCopyAPI = webServiceCopyAPI;
		this.webServiceEjecucionService = webServiceEjecucionService;
		this.mensajeService = mensajeService;
		this.mailUserSendMessage = mailUserSendMessage;
		this.procesoTransicionAutomaticaService = procesoTransicionAutomaticaService;
		this.documentoPlantillaService = documentoPlantillaService;
		this.documentoPlantillaCaracteristicaService = documentoPlantillaCaracteristicaService;
		this.reporteBaseService = reporteBaseService;
		this.procesoService = procesoService;
		this.procesoEstadoService = procesoEstadoService;
		this.procesoTransicionService = procesoTransicionService;
		this.propiedadService = propiedadService;
		this.relacionInternaService = relacionInternaService;
		this.exportService = exportService;
		this.importService = importService;
		this.sincronizacionArbolService = sincronizacionArbolService;
		this.arbolConfiguracionService = arbolConfiguracionService;
		this.propertyTypeService = propiedadValorDefinidoService;
		this.indicadorService = indicadorService;
		this.rolAccesoService = rolAccesoService;
		this.usuarioService = usuarioService;
	}


	// ==================== CONSECUTIVES ====================

	@PostMapping("/consecutives/list")
	public List<ConsecutivoDTO> listarConsecutivos(@RequestBody ConsecutivoFilterDTO filter) throws ServerException {
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

	@PostMapping("/consecutives/{key}/activate")
	public ConsecutivoDTO activarConsecutivo(@RequestBody ConsecutivoDTO dto) throws ServerException {
		return consecutivoService.activar(dto);
	}

	@PostMapping("/consecutives/{key}/assign")
	public ConsecutivoDTO asignarConsecutivo(@RequestBody ConsecutivoDTO dto) throws ServerException {
		return consecutivoService.asignarConsecutivo(dto);
	}

	// ==================== PROPERTY VALUES ====================

	@PostMapping("/property-values/list")
	public List<PropiedadValorDefinidoDTO> listarValoresDefinidos(@RequestBody PropiedadValorDefinidoFilterDTO filter)
			throws ServerException {
		
		return propiedadValorDefinidoService.listarConsulta(filter);
	}

	@PostMapping("/property-values/{key}")
	public PropiedadValorDefinidoDTO consultarValorDefinido(@PathVariable("key") String pKey) throws ServerException {
		return propiedadValorDefinidoService.consultaXId(pKey);
	}

	@PostMapping("/property-values/by-origen")
	public List<PropiedadValorDefinidoDTO> listarPorOrigen(@RequestBody PropiedadValorDefinidoFilterDTO filter){
		
		return propiedadValorDefinidoService.listarPorOrigen(filter);
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

	@PostMapping("/property-values/{key}/activate")
	public PropiedadValorDefinidoDTO activarValorDefinido(@RequestBody PropiedadValorDefinidoDTO dto)
			throws ServerException {
		return propiedadValorDefinidoService.activar(dto);
	}

	// ==================== ORGANIZATIONS ====================

	@PostMapping("/organizations/list")
	public List<OrganizacionDTO> listarOrganizaciones(@RequestBody OrganizacionFilterDTO filter)
			throws ServerException {
		
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

	@PostMapping("/organizations/{key}/activate")
	public OrganizacionDTO activarOrganizacion(@RequestBody OrganizacionDTO dto) throws ServerException {
		return organizacionService.activar(dto);
	}

	// ==================== SERVERS ====================

	@PostMapping("/servers/list")
	public List<ServidorDTO> listarServidores(@RequestBody ServidorFilterDTO filter) throws ServerException {
		
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

	@PostMapping("/servers/{key}/activate")
	public ServidorDTO activarServidor(@RequestBody ServidorDTO dto) throws ServerException {
		return servidorService.activar(dto);
	}

	// ==================== WEB SERVICES ====================

	@PostMapping("/web-services/list")
	public List<WebServiceDTO> listarWebServices(@RequestBody WebServiceFilterDTO filter) throws ServerException {
		
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

	@PostMapping("/web-services/{key}/activate")
	public WebServiceDTO activarWebService(@RequestBody WebServiceDTO dto) throws ServerException {
		return webServiceService.activar(dto);
	}

	@PostMapping("/web-services/{key}/copy")
	public d3.shared.domain.SharedIdResponse copiarWebService(@PathVariable("key") String pKey)
			throws ServerException {
		return webServiceCopyAPI.call(pKey);
	}



	@PostMapping("/web-services/{key}/execute")
	public WebServiceEjecucionDTO ejecutarWebService(@PathVariable("key") String pKey) throws ServerException {
		WebServiceEjecucionFilterDTO filter = new WebServiceEjecucionFilterDTO();
		filter.setLlaveTabla(pKey);
		return webServiceEjecucionService.ejecutarAPI(filter);
	}

	@PostMapping("/web-services/executions")
	public List<WebServiceEjecucionDTO> listarEjecuciones(@RequestBody WebServiceEjecucionFilterDTO filter)
			throws ServerException {
		if (filter.getFechaEjecucionMin() == null || filter.getFechaEjecucionMax() == null) {
			throw new ServerException("Las fechas Desde y Hasta son obligatorias para consultar ejecuciones");
		}
		
		return webServiceEjecucionService.listarConsulta(filter);
	}

	@PostMapping("/web-services/{webServiceKey}/executions")
	public List<WebServiceEjecucionDTO> listarEjecucionesPorWebService(
			@PathVariable("webServiceKey") String pWebServiceKey, @RequestBody WebServiceEjecucionFilterDTO filter)
			throws ServerException {
		
		filter.setServicio(pWebServiceKey);
		return webServiceEjecucionService.listarConsulta(filter);
	}

	// ==================== MESSAGES ====================

	

	@PostMapping("/messages/list")
	public List<MensajeDTO> listarMensajes(@RequestBody MensajeFilterDTO request) throws ServerException {
		if (request.getFechaMin() == null || request.getFechaMax() == null) {
			throw new ServerException("Las fechas Desde y Hasta son obligatorias para consultar mensajes");
		}
	
		return mensajeService.mensajesUsuario(request);
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


	// ==================== AUTO TASKS ====================

	

	@PostMapping("/auto-tasks/list")
	public List<ProcesoTransicionAutomaticaDTO> listarTareasAutomaticas(@RequestBody ProcesoTransicionAutomaticaFilterDTO request)
			throws ServerException {
		return procesoTransicionAutomaticaService.listarConsulta(request);
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

	@PostMapping("/auto-tasks/{key}/activate")
	public ProcesoTransicionAutomaticaDTO activarTareaAutomatica(@RequestBody ProcesoTransicionAutomaticaDTO dto)
			throws ServerException {
		return procesoTransicionAutomaticaService.activar(dto);
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
		
		return documentoPlantillaService.listarConsulta(filter);
	}

	@PostMapping("/document-templates/{key}")
	public DocumentoPlantillaDTO consultarPlantilla(@PathVariable("key") String pKey) throws ServerException {
		return documentoPlantillaService.consultaXId(pKey);
	}

	@PostMapping("/document-templates/admin")
	public List<DocumentoPlantillaDTO> listarPlantillasAdministrador(@RequestBody DocumentoPlantillaFilterDTO filter)
			throws ServerException {
		
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

	@PostMapping("/document-templates/{key}/activate")
	public DocumentoPlantillaDTO activarPlantilla(@RequestBody DocumentoPlantillaDTO dto) throws ServerException {
		return documentoPlantillaService.activar(dto);
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
		
		filter.setNodoSuperior(pTemplateKey);
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

	@PostMapping("/document-templates/fields/{key}/activate")
	public DocumentoPlantillaCaracteristicaDTO activarCampo(@RequestBody DocumentoPlantillaCaracteristicaDTO dto)
			throws ServerException {
		return documentoPlantillaCaracteristicaService.activar(dto);
	}

	// --- Reports ---

	@PostMapping("/document-templates/{templateKey}/reports")
	public List<ReporteBaseDTO> listarReportesPlantilla(@PathVariable("templateKey") String pTemplateKey,
			@RequestBody ReporteBaseFilterDTO filter) throws ServerException {
		
		filter.setNodoSuperior(pTemplateKey);
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

	@PostMapping("/document-templates/reports/{key}/activate")
	public ReporteBaseDTO activarReporte(@RequestBody ReporteBaseDTO dto) throws ServerException {
		return reporteBaseService.activar(dto);
	}

	@PostMapping("/document-templates/{templateKey}/transitions")
	public List<ProcesoTransicionDTO> listarTransicionesPlantilla(
			@PathVariable("templateKey") String pTemplateKey,
			@RequestBody ProcesoTransicionFilterDTO filter) throws ServerException {

		filter.setPlantilla(pTemplateKey);
		return procesoTransicionService.listarConsulta(filter);
	}

	// ==================== PROCESSES ====================

	@PostMapping("/processes/list")
	public List<ProcesoDTO> listarProcesos(@RequestBody ProcesoFilterDTO filter) throws ServerException {
		
		return procesoService.listarConsulta(filter);
	}

	@PostMapping("/processes/{key}")
	public ProcesoDTO consultarProceso(@PathVariable("key") String pKey) throws ServerException {
		return procesoService.consultaXId(pKey);
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

	@PostMapping("/processes/{key}/activate")
	public ProcesoDTO activarProceso(@RequestBody ProcesoDTO dto) throws ServerException {
		return procesoService.activar(dto);
	}

	// --- Transitions ---

	@PostMapping("/processes/{processKey}/transitions")
	public List<ProcesoTransicionDTO> listarTransiciones(@PathVariable("processKey") String pProcessKey,
			@RequestBody ProcesoTransicionFilterDTO filter) throws ServerException {
		
		filter.setNodoSuperior(pProcessKey);
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

	@PostMapping("/processes/transitions/{key}/activate")
	public ProcesoTransicionDTO activarTransicion(@RequestBody ProcesoTransicionDTO dto) throws ServerException {
		return procesoTransicionService.activar(dto);
	}

	// --- States ---

	@PostMapping("/processes/{processKey}/states")
	public List<ProcesoEstadoDTO> listarEstados(@PathVariable("processKey") String pProcessKey,
			@RequestBody ProcesoEstadoFilterDTO filter) throws ServerException {
		
		filter.setNodoSuperior(pProcessKey);
		return procesoEstadoService.listarConsulta(filter);
	}

	@PostMapping("/processes/states/{key}")
	public ProcesoEstadoDTO consultarEstado(@PathVariable("key") String pKey) throws ServerException {
		return procesoEstadoService.consultaXId(pKey);
	}

	@PostMapping("/processes/states")
	public ProcesoEstadoDTO guardarEstado(@RequestBody ProcesoEstadoDTO dto) throws ServerException {
		return procesoEstadoService.guardar(dto);
	}

	@PostMapping("/processes/states/{key}/update")
	public ProcesoEstadoDTO actualizarEstado(@RequestBody ProcesoEstadoDTO dto) throws ServerException {
		return procesoEstadoService.actualizar(dto);
	}

	@PostMapping("/processes/states/{key}/inactivate")
	public ProcesoEstadoDTO inactivarEstado(@RequestBody ProcesoEstadoDTO dto) throws ServerException {
		return procesoEstadoService.inactivar(dto);
	}

	@PostMapping("/processes/states/{key}/activate")
	public ProcesoEstadoDTO activarEstado(@RequestBody ProcesoEstadoDTO dto) throws ServerException {
		return procesoEstadoService.activar(dto);
	}

	// ==================== PROPERTIES ====================

	

	@PostMapping("/properties/list")
	public List<PropiedadDTO> listarPropiedades(@RequestBody PropiedadFilterDTO request) throws ServerException {

		return propiedadService.listarConsulta(request);
	}

	

	@PostMapping("/properties/by-id/{key}")
	public PropiedadDTO consultarPropiedad(@PathVariable("key") String pKey) throws ServerException {
		return propiedadService.consultaXId(pKey);
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

	@PostMapping("/properties/reactivate")
	public PropiedadDTO activarPropiedad(@RequestBody PropiedadDTO dto) throws ServerException {
		return propiedadService.activar(dto);
	}

	// --- Relations ---

	@PostMapping("/properties/{propiedad}/relations")
	public List<RelacionInternaDTO> listarRelaciones(@PathVariable("propiedad") String pPropiedad) throws ServerException {
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
		
		if (filter.getEstado() == null || filter.getEstado().isEmpty()) {
			filter.setEstado(SharedConstants.STATE_ACTIVE);
		}
		return rolAccesoService.listarConsulta(filter);
	}

	// ==================== USERS (búsqueda usuario propietario)
	// ====================

	@PostMapping("/users/search")
	public List<UsuarioDTO> listarUsuarios(@RequestBody UsuarioFilterDTO filter) throws ServerException {
		
		if (filter.getEstado() == null || filter.getEstado().isEmpty()) {
			filter.setEstado(SharedConstants.STATE_ACTIVE);
		}
		return usuarioService.listarConsulta(filter);
	}

	@PostMapping("/users/by-id")
	public UsuarioDTO consultarUsuario(@RequestBody String key) throws ServerException {
		return usuarioService.consultaXId(key);
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

	@PostMapping("/indicators/activate")
	public IndicatorDTO activarIndicador(@RequestBody IndicatorDTO dto) throws ServerException {
		if (dto == null || dto.getKey() == null)
			throw new ServerException("La llave del indicador se encuentra vacia");
		return indicadorService.activar(dto);
	}
	
	@PostMapping(value = "/getPropertyRelations")
	public List<RelacionInternaDTO> getPropertyRelations(@RequestBody RelacionInternaFilterDTO filter)
			throws ServerException {
		return relacionInternaService.listarConsulta(filter);
	}
	
	// ==================== CONFIGURATION IMPORT/EXPORT (antes /configuration/*)

		@GetMapping("/export")
		private CargaArchivoDTO generateFileWithConfiguration() throws ServerException {
			return exportService.call();
		}

		@PostMapping("/module")
		private CargaArchivoDTO generateFileWithConfigurationModule(@RequestBody ExportListRequest modules)
				throws ServerException {
			return exportService.call(modules);
		}

		@PostMapping("/import")
		private CargaArchivoDTO loadConfiguration(@RequestBody CargaArchivoDTO file) throws ServerException {
			return importService.call(file);
		}

		@PostMapping("/compare")
		private CargaArchivoDTO compare(@RequestBody CargaArchivoDTO file) throws ServerException {
			return importService.compare(file);
		}

		@PostMapping("/processes/{key}/graph")
		public ProcesoGraphDTO obtenerProcesoParaGraficar(@PathVariable("key") String pKey) throws ServerException {
			ProcesoFilterDTO filter = new ProcesoFilterDTO();
			filter.setLlaveTabla(pKey);
			return sincronizacionArbolService.obtenerProcesoParaGraficar(filter);
		}
		
		@PostMapping("/tree")
		private TreeNodeDTO getConfigTree(@RequestBody ArbolConfiguracionFilterDTO filter) throws ServerException {
			return sincronizacionArbolService.obtenerArbol(filter);
		}
		
		@PostMapping("/tree/node")
		private TreeNodeDTO getTreeNode(@RequestBody d3.configuration.domain.ArbolNodoRequestDTO request)
				throws ServerException {
			return arbolConfiguracionService.construirHijos(request);
		}

		@PostMapping("/tree/export")
		private CargaArchivoDTO exportConfigTree(@RequestBody ArbolConfiguracionFilterDTO filter) throws ServerException {
			return sincronizacionArbolService.exportarArbol(filter);
		}


		@PostMapping("/tree/register")
		private d3.configuration.domain.RegistroArbolDTO registerRemoteTree(@RequestBody Object payload)
				throws ServerException {
			return sincronizacionArbolService.registrarArbolRemoto(payload);
		}

		@PostMapping("/tree/compare")
		private List<DiferenciaDTO> compareTree(@RequestBody CompararArbolRequestDTO request) throws ServerException {
			return sincronizacionArbolService.compararArbol(request);
		}

		@PostMapping("/tree/sync")
		private CargaArchivoDTO syncTree(@RequestBody SincronizacionSeleccionadaDTO request) throws ServerException {
			return sincronizacionArbolService.sincronizar(request);
		}

		@PostMapping("/tree/sync-node")
		private d3.configuration.domain.SincronizacionNodoResultDTO syncTreeNode(
				@RequestBody d3.configuration.domain.SincronizacionNodoDTO request) throws ServerException {
			return sincronizacionArbolService.sincronizarNodo(request);
		}
}
