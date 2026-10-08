package d3.configuration.application;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import d3.authentication.domain.OrganizacionDTO;
import d3.authorization.domain.RolAccesoDTO;
import d3.configuration.domain.ArbolConfiguracionFilterDTO;
import d3.configuration.domain.ConfigurationExportDTO;
import d3.configuration.domain.DiferenciaDTO;
import d3.configuration.domain.HierarchyExporterDTO;
import d3.configuration.domain.LogConfigurationDTO;
import d3.configuration.domain.SincronizacionNodoDTO;
import d3.configuration.domain.SincronizacionNodoResultDTO;
import d3.configuration.domain.SincronizacionSeleccionadaDTO;
import d3.configuration.domain.TreeNodeDTO;
import d3.mail.domain.MensajePlantillaCorreoDTO;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.process.domain.DocumentoPlantillaDTO;
import d3.process.domain.ProcesoDTO;
import d3.process.domain.ProcesoEstadoDTO;
import d3.process.domain.ProcesoTransicionDTO;
import d3.report.domain.ReporteBaseDTO;
import d3.shared.domain.ServerException;
import d3.upload.application.UploadSvc;
import d3.upload.domain.CargaArchivoDTO;
import d3.webservice.domain.WebServiceDTO;

@Service
public class SincronizacionArbolSvc {

	private final ArbolConfiguracionSvc arbolConfiguracionSvc;
	private final ComparacionArbolSvc comparacionArbolSvc;
	private final ActualizacionEntidadSvc actualizacionEntidadSvc;
	private final ImportConfigurationFileService importService;
	private final UploadSvc uploadSvc;
	private final ObjectMapper mapper;
	private final SynchronizeTypePropertiesService sincronizeTypeService;
	private final SynchronizeMessageService sincronizeMessageService;
	private final SynchronizeApiService sincronizeApiService;
	private final SynchronizeOrganizationService sincronizeOrganizationService;
	private final SynchronizeProcessService sincronizeProcessService;
	private final SynchronizeProcessStateService sincronizeProcessStateService;
	private final SynchronizeProcessTransitionService sincronizeProcessTransitionService;
	private final SynchronizeTemplateService sincronizeTemplateService;
	private final SynchronizeRolService sincronizeRolService;

	public SincronizacionArbolSvc(@Lazy ArbolConfiguracionSvc arbolConfiguracionSvc,
			@Lazy ComparacionArbolSvc comparacionArbolSvc, @Lazy ActualizacionEntidadSvc actualizacionEntidadSvc,
			@Lazy ImportConfigurationFileService importService, @Lazy UploadSvc uploadSvc, ObjectMapper mapper,
			@Lazy SynchronizeTypePropertiesService sincronizeTypeService,
			@Lazy SynchronizeMessageService sincronizeMessageService,
			@Lazy SynchronizeApiService sincronizeApiService,
			@Lazy SynchronizeOrganizationService sincronizeOrganizationService,
			@Lazy SynchronizeProcessService sincronizeProcessService,
			@Lazy SynchronizeProcessStateService sincronizeProcessStateService,
			@Lazy SynchronizeProcessTransitionService sincronizeProcessTransitionService,
			@Lazy SynchronizeTemplateService sincronizeTemplateService,
			@Lazy SynchronizeRolService sincronizeRolService) {
		this.arbolConfiguracionSvc = arbolConfiguracionSvc;
		this.comparacionArbolSvc = comparacionArbolSvc;
		this.actualizacionEntidadSvc = actualizacionEntidadSvc;
		this.importService = importService;
		this.uploadSvc = uploadSvc;
		this.mapper = mapper;
		this.sincronizeTypeService = sincronizeTypeService;
		this.sincronizeMessageService = sincronizeMessageService;
		this.sincronizeApiService = sincronizeApiService;
		this.sincronizeOrganizationService = sincronizeOrganizationService;
		this.sincronizeProcessService = sincronizeProcessService;
		this.sincronizeProcessStateService = sincronizeProcessStateService;
		this.sincronizeProcessTransitionService = sincronizeProcessTransitionService;
		this.sincronizeTemplateService = sincronizeTemplateService;
		this.sincronizeRolService = sincronizeRolService;
	}

	public TreeNodeDTO obtenerArbol(ArbolConfiguracionFilterDTO filter) throws ServerException {
		return arbolConfiguracionSvc.construirArbolActual(filter);
	}

	public CargaArchivoDTO exportarArbol(ArbolConfiguracionFilterDTO filter) throws ServerException {
		TreeNodeDTO arbol = obtenerArbol(filter);
		try {
			ConfigurationExportDTO wrapper = new ConfigurationExportDTO();
			wrapper.setArbol(arbol);
			wrapper.setFechaExport(new java.util.Date());
			wrapper.setTenantOrigen(obtenerTenant());
			byte[] bytes = mapper.writeValueAsBytes(wrapper);
			return uploadSvc.uploadFileDTO(bytes, nombreArchivoExport("ArbolConfiguracion"), "export", "private");
		} catch (Exception e) {
			throw new ServerException(e.getMessage());
		}
	}

	public List<DiferenciaDTO> compararArbol(TreeNodeDTO remoto, ArbolConfiguracionFilterDTO filter)
			throws ServerException {
		TreeNodeDTO local = arbolConfiguracionSvc.construirArbolActual(filter);
		return comparacionArbolSvc.comparar(local, remoto);
	}

	public CargaArchivoDTO sincronizar(SincronizacionSeleccionadaDTO request) throws ServerException {
		if (request == null || request.getArbol() == null)
			throw new ServerException("Árbol requerido");
		TreeNodeDTO local = arbolConfiguracionSvc.construirArbolActual(filtroCompleto());
		try {
			actualizacionEntidadSvc.actualizarArbol(request.getArbol(), local, request.getSelecciones());
		} catch (Exception e) {
			throw new ServerException(e.getMessage());
		}
		TreeNodeDTO filtrado = arbolConfiguracionSvc.filtrarArbolPorSeleccion(request.getArbol(),
				request.getSelecciones());
		HierarchyExporterDTO hierarchy = arbolConfiguracionSvc.convertirArbolAHierarchy(filtrado);
		LogConfigurationDTO logs = importService.sincronize(hierarchy);

		return uploadSvc.uploadFileDTO(logs.getLogs().getBytes(), "Sincronizacion.txt", "import", "private");
	}

	public SincronizacionNodoResultDTO sincronizarNodo(SincronizacionNodoDTO request) throws ServerException {
		SincronizacionNodoResultDTO resultado = new SincronizacionNodoResultDTO();
		resultado.setExito(true);
		resultado.setCreados(new ArrayList<>());
		if (request == null || request.getNodo() == null)
			throw new ServerException("El nodo a sincronizar es obligatorio");
		if (request.getAccion() == null || request.getAccion().isEmpty())
			throw new ServerException("La accion del nodo es obligatoria");
		String accion = request.getAccion();
		if (SincronizacionNodoDTO.OMITIR.equals(accion)) {
			resultado.setMensaje("Nodo omitido por el usuario");
			resultado.setLogs("OMITIDO " + request.getCamino());
			return resultado;
		}
		String camino = request.getCamino() != null ? request.getCamino() : request.getNodo().getCamino();
		TreeNodeDTO local = arbolConfiguracionSvc.construirArbolActual(filtroCompleto());
		Map<String, TreeNodeDTO> indexLocal = indexar(local);

		if (SincronizacionNodoDTO.ACTUALIZAR.equals(accion)) {
			TreeNodeDTO localNodo = indexLocal.get(camino);
			if (localNodo == null)
				throw new ServerException(
						"El nodo no existe localmente en el camino " + camino + "; use CREAR para crearlo");
			try {
				actualizacionEntidadSvc.actualizarNodo(request.getNodo(), localNodo);
			} catch (Exception e) {
				resultado.setExito(false);
				resultado.setMensaje("Error al actualizar: " + e.getMessage());
				resultado.setLogs("ERROR ACTUALIZAR " + camino + " : " + e.getMessage());
				return resultado;
			}
			resultado.setMensaje("Nodo actualizado");
			resultado.setLogs("ACTUALIZADO " + camino);
			return resultado;
		}

		if (!SincronizacionNodoDTO.CREAR.equals(accion))
			throw new ServerException("Accion no soportada: " + accion);

		boolean incluirHijos = Boolean.TRUE.equals(request.getIncluirHijos());
		boolean crearAncestros = request.getCrearAncestros() == null || Boolean.TRUE.equals(request.getCrearAncestros());
		HierarchyExporterDTO hierarchy = new HierarchyExporterDTO();
		if (request.getNodo().getPropiedades() != null && !request.getNodo().getPropiedades().isEmpty())
			hierarchy.setProperties(request.getNodo().getPropiedades());
		else
			hierarchy.setProperties(new ArrayList<>());

		agregarEntidadAHierarchy(request.getNodo(), hierarchy, resultado.getCreados(), indexLocal, crearAncestros);
		if (incluirHijos && request.getNodo().getHijos() != null) {
			for (TreeNodeDTO hijo : request.getNodo().getHijos()) {
				agregarSubarbol(hijo, hierarchy, resultado.getCreados(), indexLocal, crearAncestros);
			}
		}

		LogConfigurationDTO logs = new LogConfigurationDTO();
		try {
			ejecutarSincronizacionPorTipo(request.getNodo().getTipo(), hierarchy, logs);
		} catch (Exception e) {
			resultado.setExito(false);
			resultado.setMensaje("Error al sincronizar: " + e.getMessage());
			resultado.setLogs(logs.getLogs() + System.lineSeparator() + "ERROR: " + e.getMessage());
			return resultado;
		}
		resultado.setMensaje("Nodo sincronizado");
		resultado.setLogs(logs.getLogs());
		try {
			CargaArchivoDTO archivo = uploadSvc.uploadFileDTO(logs.getLogs().getBytes(),
					nombreArchivoExport("SyncNodo"), "import", "private");
			resultado.setUrl(archivo.getUrl());
		} catch (Exception e) {
			// el log ya esta en la respuesta
		}
		return resultado;
	}

	private void agregarSubarbol(TreeNodeDTO nodo, HierarchyExporterDTO hierarchy, List<String> creados,
			Map<String, TreeNodeDTO> indexLocal, boolean crearAncestros) throws ServerException {
		agregarEntidadAHierarchy(nodo, hierarchy, creados, indexLocal, crearAncestros);
		if (nodo.getHijos() != null) {
			for (TreeNodeDTO hijo : nodo.getHijos()) {
				agregarSubarbol(hijo, hierarchy, creados, indexLocal, crearAncestros);
			}
		}
	}

	private void agregarEntidadAHierarchy(TreeNodeDTO nodo, HierarchyExporterDTO hierarchy, List<String> creados,
			Map<String, TreeNodeDTO> indexLocal, boolean crearAncestros)  {
		if (nodo == null || nodo.getTipo() == null || nodo.getDato() == null)
			return;
		String camino = nodo.getCamino();
		TreeNodeDTO local = indexLocal.get(camino);
		if (local != null)
			return;
		if (crearAncestros)
			crearAncestrosFaltantes(nodo, hierarchy, creados, indexLocal);
		switch (nodo.getTipo()) {
		case TreeNodeDTO.ORGANIZACION: {
			OrganizacionDTO org = mapper.convertValue(nodo.getDato(), OrganizacionDTO.class);
			hierarchy.setOrganization(org);
			break;
		}
		case TreeNodeDTO.PROCESO_MACRO:
		case TreeNodeDTO.PROCESO: {
			ProcesoDTO proceso = mapper.convertValue(nodo.getDato(), ProcesoDTO.class);
			List<ProcesoDTO> process = hierarchy.getProcess() != null ? hierarchy.getProcess()
					: new ArrayList<ProcesoDTO>();
			process.add(proceso);
			hierarchy.setProcess(process);
			break;
		}
		case TreeNodeDTO.ESTADO: {
			ProcesoEstadoDTO estado = mapper.convertValue(nodo.getDato(), ProcesoEstadoDTO.class);
			List<ProcesoEstadoDTO> states = hierarchy.getStates() != null ? hierarchy.getStates()
					: new ArrayList<ProcesoEstadoDTO>();
			states.add(estado);
			hierarchy.setStates(states);
			break;
		}
		case TreeNodeDTO.TRANSICION: {
			ProcesoTransicionDTO transicion = mapper.convertValue(nodo.getDato(), ProcesoTransicionDTO.class);
			List<ProcesoTransicionDTO> transitions = hierarchy.getTransitions() != null ? hierarchy.getTransitions()
					: new ArrayList<ProcesoTransicionDTO>();
			transitions.add(transicion);
			hierarchy.setTransitions(transitions);
			break;
		}
		case TreeNodeDTO.PLANTILLA:
		case TreeNodeDTO.PLANTILLA_MODIFICACION:
		case TreeNodeDTO.PLANTILLA_ANULACION:
		case TreeNodeDTO.PLANTILLA_ACTIVACION: {
			DocumentoPlantillaDTO plantilla = mapper.convertValue(nodo.getDato(), DocumentoPlantillaDTO.class);
			List<DocumentoPlantillaDTO> templates = hierarchy.getTemplates() != null ? hierarchy.getTemplates()
					: new ArrayList<DocumentoPlantillaDTO>();
			templates.add(plantilla);
			hierarchy.setTemplates(templates);
			break;
		}
		case TreeNodeDTO.CAMPO: {
			DocumentoPlantillaCaracteristicaDTO campo = mapper.convertValue(nodo.getDato(),
					DocumentoPlantillaCaracteristicaDTO.class);
			List<DocumentoPlantillaCaracteristicaDTO> fields = hierarchy.getFields() != null ? hierarchy.getFields()
					: new ArrayList<DocumentoPlantillaCaracteristicaDTO>();
			fields.add(campo);
			hierarchy.setFields(fields);
			break;
		}
		case TreeNodeDTO.REPORTE: {
			ReporteBaseDTO reporte = mapper.convertValue(nodo.getDato(), ReporteBaseDTO.class);
			List<ReporteBaseDTO> reports = hierarchy.getReports() != null ? hierarchy.getReports()
					: new ArrayList<ReporteBaseDTO>();
			reports.add(reporte);
			hierarchy.setReports(reports);
			break;
		}
		case TreeNodeDTO.ROL: {
			RolAccesoDTO rol = mapper.convertValue(nodo.getDato(), RolAccesoDTO.class);
			List<RolAccesoDTO> roles = hierarchy.getRoles() != null ? hierarchy.getRoles()
					: new ArrayList<RolAccesoDTO>();
			roles.add(rol);
			hierarchy.setRoles(roles);
			break;
		}
		case TreeNodeDTO.API: {
			WebServiceDTO api = mapper.convertValue(nodo.getDato(), WebServiceDTO.class);
			List<WebServiceDTO> apis = hierarchy.getApis() != null ? hierarchy.getApis()
					: new ArrayList<WebServiceDTO>();
			apis.add(api);
			hierarchy.setApis(apis);
			break;
		}
		case TreeNodeDTO.MENSAJE: {
			MensajePlantillaCorreoDTO mensaje = mapper.convertValue(nodo.getDato(), MensajePlantillaCorreoDTO.class);
			List<MensajePlantillaCorreoDTO> messages = hierarchy.getMessages() != null ? hierarchy.getMessages()
					: new ArrayList<MensajePlantillaCorreoDTO>();
			messages.add(mensaje);
			hierarchy.setMessages(messages);
			break;
		}
		default:
			break;
		}
	}

	private void crearAncestrosFaltantes(TreeNodeDTO nodo, HierarchyExporterDTO hierarchy, List<String> creados,
			Map<String, TreeNodeDTO> indexLocal) {
		String tipo = nodo.getTipo();
		if (TreeNodeDTO.ESTADO.equals(tipo) || TreeNodeDTO.TRANSICION.equals(tipo)
				|| TreeNodeDTO.PLANTILLA.equals(tipo) || TreeNodeDTO.PLANTILLA_MODIFICACION.equals(tipo)
				|| TreeNodeDTO.PLANTILLA_ANULACION.equals(tipo) || TreeNodeDTO.PLANTILLA_ACTIVACION.equals(tipo)) {
			String procesoRemoto = extraerCampoDato(nodo, "proceso");
			if (procesoRemoto != null) {
				ProcesoDTO proceso = buscarProcesoRemoto(hierarchy, procesoRemoto);
				if (proceso == null) {
					ProcesoDTO localProc = buscarProcesoLocalPorLlaveOProc(indexLocal, procesoRemoto);
					if (localProc == null) {
						ProcesoDTO nuevo = new ProcesoDTO();
						nuevo.setLlaveTabla(procesoRemoto);
						nuevo.setCodigo(procesoRemoto);
						nuevo.setNombre("Proceso " + procesoRemoto);
						nuevo.setTipo(ProcesoDTO.EJECUTOR);
						List<ProcesoDTO> process = hierarchy.getProcess() != null ? hierarchy.getProcess()
								: new ArrayList<ProcesoDTO>();
						process.add(nuevo);
						hierarchy.setProcess(process);
						creados.add("PROCESO:" + procesoRemoto);
					}
				}
			}
		}
		if (TreeNodeDTO.CAMPO.equals(tipo)) {
			String plantillaRemota = extraerCampoDato(nodo, "plantilla");
			if (plantillaRemota != null && buscarPlantillaRemota(hierarchy, plantillaRemota) == null) {
				DocumentoPlantillaDTO plantilla = new DocumentoPlantillaDTO();
				plantilla.setLlaveTabla(plantillaRemota);
				plantilla.setCodigo(plantillaRemota);
				plantilla.setNombre("Plantilla " + plantillaRemota);
				List<DocumentoPlantillaDTO> templates = hierarchy.getTemplates() != null ? hierarchy.getTemplates()
						: new ArrayList<DocumentoPlantillaDTO>();
				templates.add(plantilla);
				hierarchy.setTemplates(templates);
				creados.add("PLANTILLA:" + plantillaRemota);
			}
		}
	}

	private ProcesoDTO buscarProcesoRemoto(HierarchyExporterDTO hierarchy, String llave) {
		if (hierarchy.getProcess() == null)
			return null;
		for (ProcesoDTO proceso : hierarchy.getProcess()) {
			if (llave.equals(proceso.getLlaveTabla()) || llave.equals(proceso.getCodigo()))
				return proceso;
		}
		return null;
	}

	private ProcesoDTO buscarProcesoLocalPorLlaveOProc(Map<String, TreeNodeDTO> indexLocal, String llave) {
		for (TreeNodeDTO nodo : indexLocal.values()) {
			if (!TreeNodeDTO.PROCESO.equals(nodo.getTipo()) && !TreeNodeDTO.PROCESO_MACRO.equals(nodo.getTipo()))
				continue;
			ProcesoDTO proceso = mapper.convertValue(nodo.getDato(), ProcesoDTO.class);
			if (proceso == null)
				continue;
			if (llave.equals(proceso.getLlaveTabla()) || llave.equals(proceso.getCodigo()))
				return proceso;
		}
		return null;
	}

	private DocumentoPlantillaDTO buscarPlantillaRemota(HierarchyExporterDTO hierarchy, String llave) {
		if (hierarchy.getTemplates() == null)
			return null;
		for (DocumentoPlantillaDTO plantilla : hierarchy.getTemplates()) {
			if (llave.equals(plantilla.getLlaveTabla()) || llave.equals(plantilla.getCodigo()))
				return plantilla;
		}
		return null;
	}

	private String extraerCampoDato(TreeNodeDTO nodo, String campo) {
		if (nodo.getDato() == null)
			return null;
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> mapa = mapper.convertValue(nodo.getDato(), Map.class);
			Object valor = mapa.get(campo);
			return valor != null ? String.valueOf(valor) : null;
		} catch (Exception e) {
			return null;
		}
	}

	private void ejecutarSincronizacionPorTipo(String tipo, HierarchyExporterDTO hierarchy, LogConfigurationDTO logs)
			throws ServerException {
		boolean compare = false;
		if (TreeNodeDTO.ORGANIZACION.equals(tipo)) {
			sincronizeTypeService.call(hierarchy, logs, compare);
			sincronizeOrganizationService.call(hierarchy, logs, compare);
			return;
		}
		if (TreeNodeDTO.PROCESO_MACRO.equals(tipo) || TreeNodeDTO.PROCESO.equals(tipo)) {
			sincronizeTypeService.call(hierarchy, logs, compare);
			sincronizeProcessService.call(hierarchy, logs, compare);
			return;
		}
		if (TreeNodeDTO.ESTADO.equals(tipo)) {
			sincronizeTypeService.call(hierarchy, logs, compare);
			sincronizeProcessService.call(hierarchy, logs, compare);
			sincronizeProcessStateService.call(hierarchy, logs, compare);
			sincronizeProcessStateService.callAfter(hierarchy, logs, compare);
			return;
		}
		if (TreeNodeDTO.TRANSICION.equals(tipo)) {
			sincronizeTypeService.call(hierarchy, logs, compare);
			sincronizeProcessService.call(hierarchy, logs, compare);
			sincronizeProcessStateService.call(hierarchy, logs, compare);
			sincronizeTemplateService.call(hierarchy, logs, compare);
			sincronizeProcessTransitionService.call(hierarchy, logs, compare);
			sincronizeProcessTransitionService.callAfterCreateAll(hierarchy, logs, compare);
			return;
		}
		if (tipo != null && tipo.startsWith(TreeNodeDTO.PLANTILLA)) {
			sincronizeTypeService.call(hierarchy, logs, compare);
			sincronizeProcessService.call(hierarchy, logs, compare);
			sincronizeTemplateService.call(hierarchy, logs, compare);
			sincronizeTemplateService.callAfterCreateAllTemplate(hierarchy, logs, compare);
			return;
		}
		if (TreeNodeDTO.CAMPO.equals(tipo)) {
			sincronizeTypeService.call(hierarchy, logs, compare);
			sincronizeProcessService.call(hierarchy, logs, compare);
			sincronizeTemplateService.call(hierarchy, logs, compare);
			sincronizeTemplateService.callAfterCreateAllTemplate(hierarchy, logs, compare);
			return;
		}
		if (TreeNodeDTO.REPORTE.equals(tipo)) {
			sincronizeTypeService.call(hierarchy, logs, compare);
			sincronizeProcessService.call(hierarchy, logs, compare);
			sincronizeTemplateService.call(hierarchy, logs, compare);
			sincronizeTemplateService.callAfterCreateAllTemplate(hierarchy, logs, compare);
			return;
		}
		if (TreeNodeDTO.ROL.equals(tipo)) {
			sincronizeTypeService.call(hierarchy, logs, compare);
			sincronizeRolService.call(hierarchy, new ArrayList<>(), logs);
			return;
		}
		if (TreeNodeDTO.API.equals(tipo)) {
			sincronizeTypeService.call(hierarchy, logs, compare);
			sincronizeApiService.call(hierarchy, logs, compare);
			return;
		}
		if (TreeNodeDTO.MENSAJE.equals(tipo)) {
			sincronizeTypeService.call(hierarchy, logs, compare);
			sincronizeMessageService.call(hierarchy, logs, compare);
			return;
		}
		throw new ServerException("Tipo de nodo no soportado para sincronizar: " + tipo);
	}

	private String nombreArchivoExport(String base) {
		String tenant = obtenerTenant();
		java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("yyyyMMdd-HHmm");
		return base + "-" + (tenant != null ? tenant : "tenant") + "-" + formato.format(new java.util.Date())
				+ ".json";
	}

	private String obtenerTenant() {
		try {
			return d3.shared.application.SessionContext.getCurrentUser();
		} catch (Exception e) {
			return null;
		}
	}

	private Map<String, TreeNodeDTO> indexar(TreeNodeDTO nodo) {
		Map<String, TreeNodeDTO> index = new LinkedHashMap<>();
		if (nodo.getCamino() != null)
			index.put(nodo.getCamino(), nodo);
		if (nodo.getHijos() != null) {
			for (TreeNodeDTO hijo : nodo.getHijos()) {
				index.putAll(indexar(hijo));
			}
		}
		return index;
	}

	private ArbolConfiguracionFilterDTO filtroCompleto() {
		ArbolConfiguracionFilterDTO filter = new ArbolConfiguracionFilterDTO();
		filter.setListarPropiedades(true);
		return filter;
	}

}
