package d3.configuration.application;

import java.util.ArrayList;
import java.util.HashMap;
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
import d3.configuration.domain.PropiedadDTO;
import d3.configuration.domain.RegistroArbolDTO;
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
	private final ArbolRemotoCache arbolRemotoCache;
	private final SynchronizePropertiesService sincronizePropertiesService;

	public SincronizacionArbolSvc(@Lazy ArbolConfiguracionSvc arbolConfiguracionSvc,
			@Lazy ComparacionArbolSvc comparacionArbolSvc, @Lazy ActualizacionEntidadSvc actualizacionEntidadSvc,
			@Lazy ImportConfigurationFileService importService, @Lazy UploadSvc uploadSvc, ObjectMapper mapper,
			@Lazy ArbolRemotoCache arbolRemotoCache,
			@Lazy SynchronizePropertiesService sincronizePropertiesService) {
		this.arbolConfiguracionSvc = arbolConfiguracionSvc;
		this.comparacionArbolSvc = comparacionArbolSvc;
		this.actualizacionEntidadSvc = actualizacionEntidadSvc;
		this.importService = importService;
		this.uploadSvc = uploadSvc;
		this.mapper = mapper;
		this.arbolRemotoCache = arbolRemotoCache;
		this.sincronizePropertiesService = sincronizePropertiesService;
	}

	public TreeNodeDTO obtenerArbol(ArbolConfiguracionFilterDTO filter) throws ServerException {
		return arbolConfiguracionSvc.construirArbolActual(filter);
	}

	public RegistroArbolDTO registrarArbolRemoto(Object payload) throws ServerException {
		if (payload == null)
			throw new ServerException("El archivo de configuracion esta vacio");
		String tenantOrigen = null;
		java.util.Date fechaExport = null;
		HierarchyExporterDTO hierarchy;
		TreeNodeDTO arbolCompleto;
		try {
			hierarchy = importService.parseHierarchy(payload);
			hierarchy.normalizar();
			ArbolConfiguracionFilterDTO filtro = filtroCompleto();
			filtro.setProfundidad(ArbolConfiguracionFilterDTO.PROFUNDIDAD_COMPLETA);
			arbolCompleto = arbolConfiguracionSvc.construirArbol(hierarchy, filtro);
			tenantOrigen = hierarchyTenantOrigen(payload);
			fechaExport = hierarchyFechaExport(payload);
		} catch (ServerException e) {
			TreeNodeDTO arbolRemoto = aArbolDesdePayload(payload);
			if (arbolRemoto == null)
				throw e;
			hierarchy = arbolConfiguracionSvc.convertirArbolAHierarchy(arbolRemoto);
			hierarchy.normalizar();
			arbolCompleto = arbolRemoto;
			tenantOrigen = textoExtraido(payload, "tenantOrigen");
			String fecha = textoExtraido(payload, "fechaExport");
			if (fecha != null && !fecha.isEmpty()) {
				try {
					fechaExport = new java.util.Date(java.time.Instant.parse(fecha).toEpochMilli());
				} catch (Exception ignore) {
					fechaExport = null;
				}
			}
		}
		String arbolId = arbolRemotoCache.registrar(hierarchy, arbolCompleto, tenantOrigen, fechaExport,
				usuarioActual());
		RegistroArbolDTO registro = new RegistroArbolDTO();
		registro.setArbolId(arbolId);
		registro.setArbol(aligerizar(arbolCompleto));
		registro.setTenantOrigen(tenantOrigen);
		registro.setFechaExport(fechaExport);
		registro.setTotalNodos(contarNodos(arbolCompleto));
		return registro;
	}

	private String usuarioActual() {
		try {
			return d3.shared.application.SessionContext.getCurrentUser();
		} catch (Exception e) {
			return null;
		}
	}

	private String hierarchyTenantOrigen(Object payload) {
		return textoExtraido(payload, "tenantOrigen");
	}

	private java.util.Date hierarchyFechaExport(Object payload) {
		String fecha = textoExtraido(payload, "fechaExport");
		if (fecha == null || fecha.isEmpty())
			return null;
		try {
			return new java.util.Date(java.time.Instant.parse(fecha).toEpochMilli());
		} catch (Exception e) {
			try {
				return new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(fecha);
			} catch (Exception ignore) {
				return null;
			}
		}
	}

	private String textoExtraido(Object payload, String campo) {
		try {
			@SuppressWarnings("unchecked")
			java.util.Map<String, Object> mapa = mapper.convertValue(payload, java.util.Map.class);
			if (mapa == null)
				return null;
			Object valor = mapa.get(campo);
			return valor != null ? String.valueOf(valor) : null;
		} catch (Exception e) {
			return null;
		}
	}

	private TreeNodeDTO aArbolDesdePayload(Object payload) {
		try {
			if (payload instanceof TreeNodeDTO)
				return (TreeNodeDTO) payload;
			if (payload instanceof ConfigurationExportDTO)
				return ((ConfigurationExportDTO) payload).getArbol();
			if (payload instanceof HierarchyExporterDTO)
				return null;
			Object material = payload instanceof java.util.Map ? mapper.valueToTree(payload) : payload;
			if (material instanceof com.fasterxml.jackson.databind.JsonNode) {
				com.fasterxml.jackson.databind.JsonNode nodo = (com.fasterxml.jackson.databind.JsonNode) material;
				if (nodo.has("hierarchy"))
					return null;
				if (nodo.has("arbol") && nodo.get("arbol").isObject())
					return mapper.convertValue(nodo.get("arbol"), TreeNodeDTO.class);
				if (nodo.has("tipo") && nodo.has("camino"))
					return mapper.convertValue(nodo, TreeNodeDTO.class);
			}
		} catch (Exception ignore) {
		}
		return null;
	}

	private TreeNodeDTO aligerizar(TreeNodeDTO origen) {
		if (origen == null)
			return null;
		TreeNodeDTO nodo = new TreeNodeDTO();
		nodo.setLlaveTabla(origen.getLlaveTabla());
		nodo.setEstado(origen.getEstado());
		nodo.setNombre(origen.getNombre());
		nodo.setCodigo(origen.getCodigo());
		nodo.setImagen(origen.getImagen());
		nodo.setTipo(origen.getTipo());
		nodo.setCamino(origen.getCamino());
		nodo.setTieneHijos(origen.getTieneHijos());
		nodo.setTotalHijos(origen.getTotalHijos());
		if (origen.getHijos() != null) {
			List<TreeNodeDTO> hijos = new ArrayList<>();
			for (TreeNodeDTO hijo : origen.getHijos())
				hijos.add(aligerizar(hijo));
			nodo.setHijos(hijos);
		}
		return nodo;
	}

	private int contarNodos(TreeNodeDTO nodo) {
		if (nodo == null)
			return 0;
		int total = 1;
		if (nodo.getHijos() != null) {
			for (TreeNodeDTO hijo : nodo.getHijos())
				total += contarNodos(hijo);
		}
		return total;
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

	public List<DiferenciaDTO> compararArbol(d3.configuration.domain.CompararArbolRequestDTO request)
			throws ServerException {
		if (request == null)
			throw new ServerException("Peticion de comparacion vacia");
		TreeNodeDTO remoto = null;
		if (request.getArbolId() != null && !request.getArbolId().isEmpty())
			remoto = arbolRemotoCache.obtener(request.getArbolId()).getArbol();
		if (remoto == null)
			remoto = request.getArbol();
		if (remoto == null)
			throw new ServerException("Se requiere un arbol remoto o un arbolId registrado");
		ArbolConfiguracionFilterDTO filtro = request.getFilter() != null ? request.getFilter()
				: new ArbolConfiguracionFilterDTO();
		filtro.setListarPropiedades(Boolean.TRUE);
		TreeNodeDTO local = arbolConfiguracionSvc.construirArbolActual(filtro);
		return comparacionArbolSvc.comparar(local, remoto);
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
		if (request == null)
			throw new ServerException("La peticion de sincronizacion es obligatoria");
		if (request.getAccion() == null || request.getAccion().isEmpty())
			throw new ServerException("La accion del nodo es obligatoria");
		String accion = request.getAccion();
		if (SincronizacionNodoDTO.OMITIR.equals(accion)) {
			resultado.setMensaje("Nodo omitido por el usuario");
			resultado.setLogs("OMITIDO " + request.getCamino());
			return resultado;
		}
		TreeNodeDTO arbolRemoto = arbolRemotoDeLaPeticion(request);
		String camino = request.getCamino() != null ? request.getCamino()
				: (request.getNodo() != null ? request.getNodo().getCamino() : null);
		TreeNodeDTO nodoRemoto = null;
		List<TreeNodeDTO> rutaRemota = null;
		if (arbolRemoto != null && camino != null) {
			rutaRemota = rutaAncestros(arbolRemoto, camino);
			if (!rutaRemota.isEmpty())
				nodoRemoto = rutaRemota.get(rutaRemota.size() - 1);
		}
		if (nodoRemoto == null)
			nodoRemoto = request.getNodo();
		if (nodoRemoto == null)
			throw new ServerException("El nodo a sincronizar es obligatorio");
		if (nodoRemoto.getDato() == null)
			throw new ServerException("El nodo " + (camino != null ? camino : nodoRemoto.getNombre())
					+ " no tiene datos de configuracion; registre primero el arbol remoto (POST /configuration/tree/register)");

		TreeNodeDTO local = arbolConfiguracionSvc.construirArbolActual(filtroCompleto());
		Map<String, TreeNodeDTO> indexLocal = indexar(local);

		if (SincronizacionNodoDTO.ACTUALIZAR.equals(accion)) {
			TreeNodeDTO localNodo = indexLocal.get(camino != null ? camino : nodoRemoto.getCamino());
			if (localNodo == null)
				throw new ServerException(
						"El nodo no existe localmente en el camino " + camino + "; use CREAR para crearlo");
			try {
				actualizacionEntidadSvc.actualizarNodo(nodoRemoto, localNodo, null,
						mapearLlaves(nodoRemoto, localNodo));
			} catch (Exception e) {
				resultado.setExito(false);
				resultado.setMensaje("Error al actualizar: " + e.getMessage());
				resultado.setLogs("ERROR ACTUALIZAR " + camino + " : " + e.getMessage());
				return resultado;
			}
			LogConfigurationDTO logsPropiedades = sincronizarPropiedadesDeNodo(nodoRemoto, localNodo);
			resultado.setMensaje("Nodo actualizado");
			resultado.setLogs("ACTUALIZADO " + camino + System.lineSeparator() + logsPropiedades.getLogs());
			return resultado;
		}

		if (!SincronizacionNodoDTO.CREAR.equals(accion))
			throw new ServerException("Accion no soportada: " + accion);

		boolean incluirHijos = Boolean.TRUE.equals(request.getIncluirHijos());
		HierarchyExporterDTO hierarchy = construirHierarchyParaNodo(nodoRemoto, rutaRemota, incluirHijos,
				resultado.getCreados());

		LogConfigurationDTO logs = new LogConfigurationDTO();
		try {
			logs = importService.sincronize(hierarchy);
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

	private TreeNodeDTO arbolRemotoDeLaPeticion(SincronizacionNodoDTO request) throws ServerException {
		if (request.getArbolId() == null || request.getArbolId().isEmpty())
			return null;
		return copiaProfunda(arbolRemotoCache.obtener(request.getArbolId()).getArbol());
	}

	private HierarchyExporterDTO construirHierarchyParaNodo(TreeNodeDTO nodo, List<TreeNodeDTO> rutaRemota,
			boolean incluirHijos, List<String> creados) throws ServerException {
		if (rutaRemota == null || rutaRemota.isEmpty())
			throw new ServerException("No se pudo resolver el ancestro remoto del nodo " + nodo.getCamino());
		HierarchyExporterDTO hierarchy = new HierarchyExporterDTO();
		List<PropiedadDTO> propiedades = new ArrayList<>();
		Map<String, TreeNodeDTO> vacio = new LinkedHashMap<>();
		for (TreeNodeDTO ancestro : rutaRemota) {
			agregarEntidadAHierarchy(ancestro, hierarchy, creados, vacio);
			acumularPropiedadesPropias(ancestro, propiedades);
		}
		if (incluirHijos && nodo.getHijos() != null) {
			for (TreeNodeDTO hijo : nodo.getHijos())
				agregarSubarbolConPropiedades(hijo, hierarchy, creados, vacio, propiedades);
		}
		hierarchy.setProperties(propiedades);
		hierarchy.normalizar();
		return hierarchy;
	}

	private void agregarSubarbolConPropiedades(TreeNodeDTO nodo, HierarchyExporterDTO hierarchy, List<String> creados,
			Map<String, TreeNodeDTO> indexLocal, List<PropiedadDTO> propiedades) throws ServerException {
		agregarEntidadAHierarchy(nodo, hierarchy, creados, indexLocal);
		acumularPropiedadesPropias(nodo, propiedades);
		if (nodo.getHijos() != null) {
			for (TreeNodeDTO hijo : nodo.getHijos())
				agregarSubarbolConPropiedades(hijo, hierarchy, creados, indexLocal, propiedades);
		}
	}

	private void acumularPropiedadesPropias(TreeNodeDTO nodo, List<PropiedadDTO> propiedades) {
		if (nodo.getPropiedades() == null || nodo.getPropiedades().isEmpty())
			return;
		for (PropiedadDTO propiedad : nodo.getPropiedades())
			propiedades.add(new PropiedadDTO(propiedad));
	}

	private LogConfigurationDTO sincronizarPropiedadesDeNodo(TreeNodeDTO nodoRemoto, TreeNodeDTO localNodo)
			throws ServerException {
		LogConfigurationDTO logs = new LogConfigurationDTO();
		if (nodoRemoto.getPropiedades() == null || nodoRemoto.getPropiedades().isEmpty())
			return logs;
		String tipoPropiedad = tipoPropiedadPorTipoNodo(nodoRemoto.getTipo());
		if (tipoPropiedad == null)
			return logs;
		HierarchyExporterDTO hierarchy = new HierarchyExporterDTO();
		List<PropiedadDTO> propiedades = new ArrayList<>();
		for (PropiedadDTO propiedad : nodoRemoto.getPropiedades())
			propiedades.add(new PropiedadDTO(propiedad));
		hierarchy.setProperties(propiedades);
		hierarchy.normalizar();
		sincronizePropertiesService.call(hierarchy, nodoRemoto.getLlaveTabla(), tipoPropiedad,
				localNodo.getLlaveTabla(), logs, false, true);
		return logs;
	}

	private String tipoPropiedadPorTipoNodo(String tipo) {
		if (TreeNodeDTO.ORGANIZACION.equals(tipo))
			return d3.configuration.domain.PropiedadValorDefinidoDTO.ORGANIZACION;
		if (TreeNodeDTO.PROCESO.equals(tipo) || TreeNodeDTO.PROCESO_MACRO.equals(tipo))
			return d3.configuration.domain.PropiedadValorDefinidoDTO.PROCESO;
		if (TreeNodeDTO.ESTADO.equals(tipo))
			return d3.configuration.domain.PropiedadValorDefinidoDTO.ESTADO;
		if (TreeNodeDTO.TRANSICION.equals(tipo))
			return d3.configuration.domain.PropiedadValorDefinidoDTO.TRANSICION;
		if (tipo != null && tipo.startsWith(TreeNodeDTO.PLANTILLA))
			return d3.configuration.domain.PropiedadValorDefinidoDTO.PLANTILLA;
		if (TreeNodeDTO.CAMPO.equals(tipo))
			return d3.configuration.domain.PropiedadValorDefinidoDTO.CAMPO;
		if (TreeNodeDTO.REPORTE.equals(tipo))
			return d3.configuration.domain.PropiedadValorDefinidoDTO.REPORTE;
		if (TreeNodeDTO.API.equals(tipo))
			return d3.configuration.domain.PropiedadValorDefinidoDTO.API_SERVICE;
		return null;
	}

	private Map<String, String> mapearLlaves(TreeNodeDTO remoto, TreeNodeDTO local) {
		Map<String, String> mapa = new HashMap<>();
		if (remoto == null || local == null)
			return mapa;
		mapa.put(remoto.getLlaveTabla(), local.getLlaveTabla());
		Map<String, TreeNodeDTO> indexLocal = indexar(local);
		acumularMapeo(remoto, indexLocal, mapa);
		return mapa;
	}

	private void acumularMapeo(TreeNodeDTO remoto, Map<String, TreeNodeDTO> indexLocal, Map<String, String> mapa) {
		if (remoto.getHijos() == null)
			return;
		for (TreeNodeDTO hijo : remoto.getHijos()) {
			TreeNodeDTO localHijo = indexLocal.get(hijo.getCamino());
			if (localHijo != null && hijo.getLlaveTabla() != null)
				mapa.put(hijo.getLlaveTabla(), localHijo.getLlaveTabla());
			acumularMapeo(hijo, indexLocal, mapa);
		}
	}

	private List<TreeNodeDTO> rutaAncestros(TreeNodeDTO raiz, String camino) {
		List<TreeNodeDTO> ruta = new ArrayList<>();
		if (raiz == null || camino == null)
			return ruta;
		if (buscardaRuta(raiz, camino, ruta))
			return ruta;
		return new ArrayList<>();
	}

	private boolean buscardaRuta(TreeNodeDTO nodo, String camino, List<TreeNodeDTO> ruta) {
		ruta.add(nodo);
		if (camino.equals(nodo.getCamino()))
			return true;
		if (nodo.getHijos() != null) {
			for (TreeNodeDTO hijo : nodo.getHijos()) {
				if (buscardaRuta(hijo, camino, ruta))
					return true;
			}
		}
		ruta.remove(ruta.size() - 1);
		return false;
	}

	private TreeNodeDTO copiaProfunda(TreeNodeDTO arbol) {
		if (arbol == null)
			return null;
		try {
			return mapper.readValue(mapper.writeValueAsString(arbol), TreeNodeDTO.class);
		} catch (Exception e) {
			return arbol;
		}
	}

	private void agregarEntidadAHierarchy(TreeNodeDTO nodo, HierarchyExporterDTO hierarchy, List<String> creados,
			Map<String, TreeNodeDTO> indexLocal)  {
		if (nodo == null || nodo.getTipo() == null || nodo.getDato() == null)
			return;
		String camino = nodo.getCamino();
		TreeNodeDTO local = indexLocal.get(camino);
		if (local != null)
			return;
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
