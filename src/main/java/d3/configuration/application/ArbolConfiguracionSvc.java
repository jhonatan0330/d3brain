package d3.configuration.application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.authentication.application.OrganizacionSvc;
import d3.authentication.domain.OrganizacionDTO;
import d3.configuration.domain.ArbolConfiguracionFilterDTO;
import d3.configuration.domain.ArbolNodoRequestDTO;
import d3.configuration.domain.HierarchyExporterDTO;
import d3.configuration.domain.PropiedadDTO;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.configuration.domain.SeleccionSincronizacionDTO;
import d3.configuration.domain.TreeNodeDTO;
import d3.configuration.infrastructure.NodoHijoMapper;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.process.domain.DocumentoPlantillaDTO;
import d3.process.domain.ProcesoDTO;
import d3.process.domain.ProcesoEstadoDTO;
import d3.process.domain.ProcesoTransicionDTO;
import d3.report.domain.ReporteBaseDTO;
import d3.shared.domain.ServerException;
import d3.webservice.domain.WebServiceDTO;

@Service
public class ArbolConfiguracionSvc {


	private final ExportConfigurationFileService exportService;
	private final OrganizacionSvc organizacionService;
	private final NodoHijoMapper nodoHijoMapper;

	public ArbolConfiguracionSvc(@Lazy ExportConfigurationFileService exportService,
			@Lazy OrganizacionSvc organizacionService, @Lazy NodoHijoMapper nodoHijoMapper) {
		this.exportService = exportService;
		this.organizacionService = organizacionService;
		this.nodoHijoMapper = nodoHijoMapper;
	}

	public TreeNodeDTO construirArbolActual( ArbolConfiguracionFilterDTO filter) throws ServerException {
		if (filter != null && ArbolConfiguracionFilterDTO.PROFUNDIDAD_SIMPLE.equals(filter.getProfundidad()))
			return construirArbolSimple(filter);
		return construirArbol(exportService.construirHierarchy(), filter);
	}

	public TreeNodeDTO construirArbolSimple(ArbolConfiguracionFilterDTO filter) throws ServerException {
		boolean listarPropiedades = filter != null && Boolean.TRUE.equals(filter.getListarPropiedades());

		TreeNodeDTO raiz = new TreeNodeDTO();
		raiz.setTipo(TreeNodeDTO.ORGANIZACION);
		raiz.setCamino(TreeNodeDTO.ORGANIZACION);
		OrganizacionDTO org = organizacionService.obtenerPrincipal();
		if (org != null) {
			raiz.setLlaveTabla(org.getLlaveTabla());
			raiz.setEstado(org.getEstado());
			raiz.setNombre(org.getNombre());
			raiz.setCodigo(org.getCodigo());
			raiz.setImagen(org.getImagen());
			
		}
		raiz.setTieneHijos(true);
		List<TreeNodeDTO> hijos = construirHijosNivel(TreeNodeDTO.ORGANIZACION, raiz.getLlaveTabla(),
				TreeNodeDTO.ORGANIZACION, listarPropiedades);
		raiz.setHijos(hijos.isEmpty() ? null : hijos);
		raiz.setTotalHijos(hijos.size());
		return raiz;
	}

	public TreeNodeDTO construirHijos(ArbolNodoRequestDTO request) throws ServerException {
		if (request == null || request.getTipo() == null || request.getTipo().isEmpty())
			throw new ServerException("El tipo del nodo es obligatorio");
		boolean listarPropiedades = Boolean.TRUE.equals(request.getListarPropiedades());
		
		String tipoPadre = request.getTipo();
		String llavePadre = request.getLlaveTabla();
		String caminoPadre = request.getCamino();
		if (caminoPadre == null || caminoPadre.isEmpty())
			caminoPadre = tipoPadre;
		List<TreeNodeDTO> hijos = construirHijosNivel(tipoPadre, llavePadre, caminoPadre, listarPropiedades);
		TreeNodeDTO nodo = new TreeNodeDTO();
		nodo.setTipo(tipoPadre);
		nodo.setCamino(caminoPadre);
		nodo.setLlaveTabla(llavePadre);
		nodo.setHijos(hijos.isEmpty() ? null : hijos);
		nodo.setTieneHijos(admiteHijos(tipoPadre));
		nodo.setTotalHijos(hijos.size());
		return nodo;
	}

	private boolean admiteHijos(String tipo) {
		return TreeNodeDTO.ORGANIZACION.equals(tipo) 
				|| TreeNodeDTO.PROCESO.equals(tipo) || TreeNodeDTO.PLANTILLA.equals(tipo);
	}

	private boolean esRaiz(ProcesoDTO proceso) {
		String macro = proceso.getNodoSuperior();
		return macro == null || macro.isEmpty() || macro.equals(proceso.getLlaveTabla());
	}


	private List<TreeNodeDTO> construirHijosNivel(String tipoPadre, String llavePadre, String caminoPadre,
			boolean listarPropiedades) throws ServerException {
		List<TreeNodeDTO> hijos = new ArrayList<>();
		try {
			List<TreeNodeDTO> nodosHijos = nodoHijoMapper.listarHijos(tipoPadre, llavePadre);
			if (nodosHijos != null) {
				for (TreeNodeDTO nodoHijo : nodosHijos) {
					String tipoNodo = nodoHijo.getTipo();
					String nombre = nodoHijo.getNombre();
					String codigo = nodoHijo.getCodigo();
					String imagen = nodoHijo.getImagen();
					String categoria = nodoHijo.getCategoria();

					TreeNodeDTO nodo = nodoConfig(nodoHijo.getLlaveTabla(), nodoHijo.getEstado(), nombre,
							codigo, imagen, tipoNodo, caminoPadre, listarPropiedades, null, categoria);

					if (TreeNodeDTO.PLANTILLA.equals(tipoNodo)) {
						boolean tieneHijas = plantillaTieneHijos(nodoHijo.getLlaveTabla(), llavePadre);
						nodo.setTieneHijos(tieneHijas);
						nodo.setTotalHijos(tieneHijas ? 1 : 0);
					}

					hijos.add(nodo);
				}
			}
		} catch (ServerException e) {
			// mantener comportamiento original: devolver lista vacía ante error
		}
		return hijos;
	}

	private boolean plantillaTieneHijos(String llavePlantilla, String llavePadreProceso) throws ServerException {
		if (llavePlantilla == null)
			return false;
		List<TreeNodeDTO> subPlantillas = nodoHijoMapper.listarHijos(TreeNodeDTO.PLANTILLA, llavePlantilla);
		return subPlantillas != null && !subPlantillas.isEmpty();
	}

	private TreeNodeDTO nodoConfig(String llave, String estado, String nombre, String codigo, String imagen,
			String tipoNodo, String caminoPadre, boolean listarPropiedades,
			List<PropiedadDTO> propiedades, String categoria) {
		TreeNodeDTO nodo = new TreeNodeDTO();
		nodo.setLlaveTabla(llave);
		nodo.setEstado(estado);
		nodo.setNombre(nombre);
		nodo.setCodigo(codigo);
		nodo.setImagen(imagen);
		nodo.setTipo(tipoNodo);
		nodo.setCategoria(categoria);
		String padre = (caminoPadre == null || caminoPadre.isEmpty()) ? TreeNodeDTO.ORGANIZACION : caminoPadre;
		nodo.setCamino(padre + "/" + tipoNodo + ":" + etiqueta(nombre, codigo, llave));
		//nodo.setDato(dato);
		nodo.setTieneHijos(admiteHijos(tipoNodo));
		if (listarPropiedades && propiedades != null)
			nodo.setPropiedades(propiedades);
		return nodo;
	}

	public TreeNodeDTO construirArbol(HierarchyExporterDTO hierarchy) {
		return construirArbol(hierarchy, null);
	}

	public TreeNodeDTO construirArbol(HierarchyExporterDTO hierarchy, ArbolConfiguracionFilterDTO filter) {
		boolean listarPropiedades = filter != null && Boolean.TRUE.equals(filter.getListarPropiedades());
		Map<String, List<PropiedadDTO>> propiedadesIndex = indexarPropiedades(hierarchy.getProperties());

		TreeNodeDTO raiz = new TreeNodeDTO();
		raiz.setTipo(TreeNodeDTO.ORGANIZACION);
		raiz.setCamino(TreeNodeDTO.ORGANIZACION);
		if (hierarchy.getOrganization() != null) {
			OrganizacionDTO org = hierarchy.getOrganization();
			raiz.setLlaveTabla(org.getLlaveTabla());
			raiz.setEstado(org.getEstado());
			raiz.setNombre(org.getNombre());
			raiz.setCodigo(org.getCodigo());
			raiz.setImagen(org.getImagen());
			//raiz.setDato(org);
			if (listarPropiedades)
				raiz.setPropiedades(propiedadesIndex.get(PropiedadValorDefinidoDTO.ORGANIZACION + "|" + org.getLlaveTabla()));
		}

		List<TreeNodeDTO> hijos = new ArrayList<>();

		if (hierarchy.getProcess() != null && !hierarchy.getProcess().isEmpty()) {
			Map<String, List<ProcesoDTO>> procesosHijos = new LinkedHashMap<>();
			for (ProcesoDTO proceso : hierarchy.getProcess()) {
				if (esRaiz(proceso))
					continue;
				procesosHijos.computeIfAbsent(proceso.getNodoSuperior(), key -> new ArrayList<>()).add(proceso);
			}
			for (ProcesoDTO proceso : hierarchy.getProcess()) {
				if (esRaiz(proceso)) {
					TreeNodeDTO nodoProceso = construirNodoProceso(proceso, hierarchy, procesosHijos, listarPropiedades,
							propiedadesIndex, raiz.getCamino());
					hijos.add(nodoProceso);
				}
			}
		}

		Set<String> llavesProcesos = new HashSet<>();
		if (hierarchy.getProcess() != null) {
			for (ProcesoDTO proceso : hierarchy.getProcess())
				llavesProcesos.add(proceso.getLlaveTabla());
		}

		if (hierarchy.getApis() != null) {
			for (WebServiceDTO api : hierarchy.getApis()) {
				String procesoApi = api.getNodoSuperior();
				if (procesoApi != null && !procesoApi.isEmpty() && llavesProcesos.contains(procesoApi))
					continue;
				hijos.add(nodoBase(api.getLlaveTabla(), api.getEstado(), api.getNombre(), api.getCodigo(), null,
						TreeNodeDTO.API, raiz.getCamino(),  listarPropiedades,
						propiedadesIndex.get(PropiedadValorDefinidoDTO.API_SERVICE + "|" + api.getLlaveTabla())));
			}
		}

		raiz.setHijos(hijos.isEmpty() ? null : hijos);
		return raiz;
	}

	private TreeNodeDTO construirNodoProceso(ProcesoDTO proceso, HierarchyExporterDTO hierarchy,
			Map<String, List<ProcesoDTO>> procesosHijos, boolean listarPropiedades,
			Map<String, List<PropiedadDTO>> propiedadesIndex, String caminoPadre) {

		TreeNodeDTO nodo = nodoBase(proceso.getLlaveTabla(), proceso.getEstado(), proceso.getNombre(),
				proceso.getCodigo(), proceso.getImagen(), TreeNodeDTO.PROCESO, caminoPadre,  listarPropiedades,
				propiedadesIndex.get(PropiedadValorDefinidoDTO.PROCESO + "|" + proceso.getLlaveTabla()));

		List<TreeNodeDTO> hijos = new ArrayList<>();

		List<ProcesoDTO> hijosProceso = procesosHijos.get(proceso.getLlaveTabla());
		if (hijosProceso != null) {
			for (ProcesoDTO hijoProceso : hijosProceso) {
				hijos.add(construirNodoProceso(hijoProceso, hierarchy, procesosHijos, listarPropiedades,
						propiedadesIndex, nodo.getCamino()));
			}
		}
		if (hierarchy.getStates() != null) {
			for (ProcesoEstadoDTO estado : hierarchy.getStates()) {
				if (proceso.getLlaveTabla().equals(estado.getNodoSuperior())) {
					hijos.add(nodoBase(estado.getLlaveTabla(), estado.getEstado(), estado.getNombre(),
							estado.getCodigo(), null, TreeNodeDTO.ESTADO, nodo.getCamino(), 
							listarPropiedades,
							propiedadesIndex.get(PropiedadValorDefinidoDTO.ESTADO + "|" + estado.getLlaveTabla())));
				}
			}
		}
		if (hierarchy.getTransitions() != null) {
			for (ProcesoTransicionDTO transicion : hierarchy.getTransitions()) {
				if (proceso.getLlaveTabla().equals(transicion.getNodoSuperior())) {
					hijos.add(nodoBase(transicion.getLlaveTabla(), transicion.getEstado(), transicion.getNombre(),
							transicion.getCodigo(), transicion.getImagen(), TreeNodeDTO.TRANSICION,
							nodo.getCamino(),  listarPropiedades,
							propiedadesIndex.get(PropiedadValorDefinidoDTO.TRANSICION + "|"
									+ transicion.getLlaveTabla())));
				}
			}
		}
		hijos.addAll(construirNodosPlantillas(hierarchy, proceso.getLlaveTabla(), listarPropiedades,
				propiedadesIndex, nodo.getCamino()));

		if (hierarchy.getApis() != null) {
			for (WebServiceDTO api : hierarchy.getApis()) {
				if (!proceso.getLlaveTabla().equals(api.getNodoSuperior()))
					continue;
				hijos.add(nodoBase(api.getLlaveTabla(), api.getEstado(), api.getNombre(), api.getCodigo(),
						null, TreeNodeDTO.API, nodo.getCamino(),  false, null));
			}
		}

		nodo.setHijos(hijos.isEmpty() ? null : hijos);
		return nodo;
	}

	private List<TreeNodeDTO> construirNodosPlantillas(HierarchyExporterDTO hierarchy, String llaveProceso,
			boolean listarPropiedades, Map<String, List<PropiedadDTO>> propiedadesIndex, String caminoPadre) {
		List<TreeNodeDTO> nodos = new ArrayList<>();
		if (hierarchy.getTemplates() == null)
			return nodos;
		
		Map<String, List<DocumentoPlantillaDTO>> porPadre = new LinkedHashMap<>();
		
		for (DocumentoPlantillaDTO plantilla : hierarchy.getTemplates()) {
			if (llaveProceso.equals(plantilla.getProceso())) {
				porPadre.computeIfAbsent(plantilla.getNodoSuperior(), k -> new ArrayList<>()).add(plantilla);
			}
		}

		for (DocumentoPlantillaDTO plantilla : hierarchy.getTemplates()) {
			if (!llaveProceso.equals(plantilla.getProceso()) || plantilla.getTipo() == null
					|| !plantilla.getTipo().equals("P"))
				continue;
			TreeNodeDTO nodoPlantilla = nodoBase(plantilla.getLlaveTabla(), plantilla.getEstado(),
					plantilla.getNombre(), plantilla.getCodigo(), plantilla.getImagen(), TreeNodeDTO.PLANTILLA,
					caminoPadre,  listarPropiedades,
					propiedadesIndex.get(PropiedadValorDefinidoDTO.PLANTILLA + "|" + plantilla.getLlaveTabla()));

			List<TreeNodeDTO> hijos = new ArrayList<>();
			if (hierarchy.getFields() != null) {
				for (DocumentoPlantillaCaracteristicaDTO campo : hierarchy.getFields()) {
					if (plantilla.getLlaveTabla().equals(campo.getNodoSuperior())) {
						hijos.add(nodoBase(campo.getLlaveTabla(), campo.getEstado(), campo.getNombre(), campo.getCodigo(),
								campo.getImagen(), TreeNodeDTO.CAMPO, nodoPlantilla.getCamino(), 
								listarPropiedades,
								propiedadesIndex.get(PropiedadValorDefinidoDTO.CAMPO + "|" + campo.getLlaveTabla())));
					}
				}
			}
			if (hierarchy.getReports() != null) {
				for (ReporteBaseDTO reporte : hierarchy.getReports()) {
					if (plantilla.getLlaveTabla().equals(reporte.getNodoSuperior())) {
						hijos.add(nodoBase(reporte.getLlaveTabla(), reporte.getEstado(), reporte.getNombre(),
								reporte.getCodigo(), null, TreeNodeDTO.REPORTE, nodoPlantilla.getCamino(), 
								listarPropiedades,
								propiedadesIndex.get(PropiedadValorDefinidoDTO.REPORTE + "|" + reporte.getLlaveTabla())));
					}
				}
			}
			hijos.addAll(susPlantillas(porPadre.get(plantilla.getLlaveTabla()), plantilla.getTipo(),
					nodoPlantilla.getCamino(), listarPropiedades, propiedadesIndex));
			nodoPlantilla.setHijos(hijos.isEmpty() ? null : hijos);
			nodos.add(nodoPlantilla);
		}
		return nodos;
	}

	private List<TreeNodeDTO> susPlantillas(List<DocumentoPlantillaDTO> plantillas, String tipoNodo, String caminoPadre,
			boolean listarPropiedades, Map<String, List<PropiedadDTO>> propiedadesIndex) {
		List<TreeNodeDTO> nodos = new ArrayList<>();
		if (plantillas == null)
			return nodos;
		for (DocumentoPlantillaDTO plantilla : plantillas) {
			nodos.add(nodoBase(plantilla.getLlaveTabla(), plantilla.getEstado(), plantilla.getNombre(),
					plantilla.getCodigo(), plantilla.getImagen(), tipoNodo, caminoPadre,  listarPropiedades,
					propiedadesIndex.get(PropiedadValorDefinidoDTO.PLANTILLA + "|" + plantilla.getLlaveTabla())));
		}
		return nodos;
	}

	private TreeNodeDTO nodoBase(String llave, String estado, String nombre, String codigo, String imagen,
			String tipoNodo, String caminoPadre,  boolean listarPropiedades, List<PropiedadDTO> propiedades) {
		TreeNodeDTO nodo = new TreeNodeDTO();
		nodo.setLlaveTabla(llave);
		nodo.setEstado(estado);
		nodo.setNombre(nombre);
		nodo.setCodigo(codigo);
		nodo.setImagen(imagen);
		nodo.setTipo(tipoNodo);
		nodo.setCamino(caminoPadre + "/" + tipoNodo + ":" + etiqueta(nombre, codigo, llave));
		//nodo.setDato(dato);
		if (listarPropiedades)
			nodo.setPropiedades(propiedades);
		return nodo;
	}

	private String etiqueta(String nombre, String codigo, String llave) {
		if (codigo != null && !codigo.isEmpty())
			return codigo;
		if (nombre != null && !nombre.isEmpty())
			return nombre;
		return llave;
	}

	private Map<String, List<PropiedadDTO>> indexarPropiedades(List<PropiedadDTO> properties) {
		Map<String, List<PropiedadDTO>> index = new LinkedHashMap<>();
		if (properties == null)
			return index;
		for (PropiedadDTO property : properties) {
			if (property.getTipo() == null || property.getCampo() == null)
				continue;
			index.computeIfAbsent(property.getTipo() + "|" + property.getCampo(), k -> new ArrayList<>()).add(property);
		}
		return index;
	}

	public TreeNodeDTO filtrarArbolPorSeleccion(TreeNodeDTO raiz, List<SeleccionSincronizacionDTO> selecciones) {
		if (raiz == null)
			return null;
		Map<String, SeleccionSincronizacionDTO> mapa = new LinkedHashMap<>();
		if (selecciones != null) {
			for (SeleccionSincronizacionDTO seleccion : selecciones) {
				if (seleccion.getCamino() != null)
					mapa.put(seleccion.getCamino(), seleccion);
			}
		}
		TreeNodeDTO resultado = new TreeNodeDTO();
		copiarNodo(raiz, resultado);
		List<TreeNodeDTO> hijos = new ArrayList<>();
		if (raiz.getHijos() != null) {
			for (TreeNodeDTO hijo : raiz.getHijos()) {
				TreeNodeDTO filtrado = filtrarNodo(hijo, mapa, false);
				if (filtrado != null)
					hijos.add(filtrado);
			}
		}
		resultado.setHijos(hijos.isEmpty() ? null : hijos);
		return resultado;
	}

	private TreeNodeDTO filtrarNodo(TreeNodeDTO nodo, Map<String, SeleccionSincronizacionDTO> mapa,
			boolean forzarIncluirHijos) {
		SeleccionSincronizacionDTO seleccion = mapa.get(nodo.getCamino());
		boolean incluirNodo;
		if (forzarIncluirHijos) {
			incluirNodo = true;
		} else if (seleccion != null && !SeleccionSincronizacionDTO.OMITIR.equals(seleccion.getAccion())) {
			incluirNodo = true;
		} else {
			incluirNodo = false;
		}
		if (!incluirNodo)
			return null;
		boolean incluirHijos = forzarIncluirHijos
				|| (seleccion != null && Boolean.TRUE.equals(seleccion.getIncluirHijos()));
		TreeNodeDTO resultado = new TreeNodeDTO();
		copiarNodo(nodo, resultado);
		List<TreeNodeDTO> hijos = new ArrayList<>();
		if (nodo.getHijos() != null) {
			for (TreeNodeDTO hijo : nodo.getHijos()) {
				TreeNodeDTO filtrado = filtrarNodo(hijo, mapa, incluirHijos);
				if (filtrado != null)
					hijos.add(filtrado);
			}
		}
		resultado.setHijos(hijos.isEmpty() ? null : hijos);
		return resultado;
	}

	private void copiarNodo(TreeNodeDTO origen, TreeNodeDTO destino) {
		destino.setLlaveTabla(origen.getLlaveTabla());
		destino.setEstado(origen.getEstado());
		destino.setNombre(origen.getNombre());
		destino.setCodigo(origen.getCodigo());
		destino.setImagen(origen.getImagen());
		destino.setTipo(origen.getTipo());
		destino.setCamino(origen.getCamino());
		//destino.setDato(origen.getDato());
		destino.setPropiedades(origen.getPropiedades());
	}

	public HierarchyExporterDTO convertirArbolAHierarchy(TreeNodeDTO raiz) throws ServerException {
		HierarchyExporterDTO hierarchy = new HierarchyExporterDTO();
		List<ProcesoDTO> process = new ArrayList<>();
		List<ProcesoEstadoDTO> states = new ArrayList<>();
		List<ProcesoTransicionDTO> transitions = new ArrayList<>();
		List<DocumentoPlantillaDTO> templates = new ArrayList<>();
		List<DocumentoPlantillaCaracteristicaDTO> fields = new ArrayList<>();
		List<ReporteBaseDTO> reports = new ArrayList<>();
		List<WebServiceDTO> apis = new ArrayList<>();
		List<PropiedadDTO> properties = new ArrayList<>();
		agregarDatos(raiz, hierarchy, process, states, transitions, templates, fields, reports,  apis, 
				properties);
		hierarchy.setProcess(process.isEmpty() ? null : process);
		hierarchy.setStates(states.isEmpty() ? null : states);
		hierarchy.setTransitions(transitions.isEmpty() ? null : transitions);
		hierarchy.setTemplates(templates.isEmpty() ? null : templates);
		hierarchy.setFields(fields.isEmpty() ? null : fields);
		hierarchy.setReports(reports.isEmpty() ? null : reports);
		hierarchy.setApis(apis.isEmpty() ? null : apis);
		hierarchy.setProperties(properties.isEmpty() ? null : properties);
		return hierarchy;
	}

	private void agregarDatos(TreeNodeDTO nodo, HierarchyExporterDTO hierarchy, List<ProcesoDTO> process,
			List<ProcesoEstadoDTO> states, List<ProcesoTransicionDTO> transitions, List<DocumentoPlantillaDTO> templates,
			List<DocumentoPlantillaCaracteristicaDTO> fields, List<ReporteBaseDTO> reports, 
			List<WebServiceDTO> apis,  List<PropiedadDTO> properties)
			throws ServerException {
		if (nodo.getPropiedades() != null) {
			properties.addAll(nodo.getPropiedades());
		}
		/*if (nodo.getDato() != null) {
			switch (nodo.getTipo()) {
			case TreeNodeDTO.ORGANIZACION:
				hierarchy.setOrganization(mapper.convertValue(nodo.getDato(), OrganizacionDTO.class));
				break;
			case TreeNodeDTO.PROCESO:
				process.add(mapper.convertValue(nodo.getDato(), ProcesoDTO.class));
				break;
			case TreeNodeDTO.ESTADO:
				states.add(mapper.convertValue(nodo.getDato(), ProcesoEstadoDTO.class));
				break;
			case TreeNodeDTO.TRANSICION:
				transitions.add(mapper.convertValue(nodo.getDato(), ProcesoTransicionDTO.class));
				break;
			case TreeNodeDTO.PLANTILLA:
				templates.add(mapper.convertValue(nodo.getDato(), DocumentoPlantillaDTO.class));
				break;
			case TreeNodeDTO.CAMPO:
				fields.add(mapper.convertValue(nodo.getDato(), DocumentoPlantillaCaracteristicaDTO.class));
				break;
			case TreeNodeDTO.REPORTE:
				reports.add(mapper.convertValue(nodo.getDato(), ReporteBaseDTO.class));
				break;
			case TreeNodeDTO.API:
				apis.add(mapper.convertValue(nodo.getDato(), WebServiceDTO.class));
				break;
			default:
				break;
			}
		}*/
		if (nodo.getHijos() != null) {
			for (TreeNodeDTO hijo : nodo.getHijos()) {
				agregarDatos(hijo, hierarchy, process, states, transitions, templates, fields, reports,  apis,
						 properties);
			}
		}
	}

}