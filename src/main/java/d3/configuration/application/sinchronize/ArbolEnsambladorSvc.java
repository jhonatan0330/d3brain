package d3.configuration.application.sinchronize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.authentication.application.OrganizacionSvc;
import d3.authentication.domain.OrganizacionDTO;
import d3.configuration.domain.ArbolNodoRequestDTO;
import d3.configuration.domain.NodoArbolDTO;
import d3.configuration.domain.PropiedadDTO;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.configuration.domain.RelacionInternaDTO;
import d3.configuration.infrastructure.NodoHijoMapper;
import d3.process.application.ProcesoEstadoSvc;
import d3.process.domain.ProcesoEstadoDTO;
import d3.process.domain.ProcesoFilterDTO;
import d3.shared.domain.ServerException;

@Service
public class ArbolEnsambladorSvc {

	private final NodoHijoMapper nodoHijoMapper;
	private final OrganizacionSvc organizacionService;
	private final ProcesoEstadoSvc estadoService;

	public ArbolEnsambladorSvc(@Lazy NodoHijoMapper nodoHijoMapper, @Lazy OrganizacionSvc organizacionService, @Lazy ProcesoEstadoSvc estadoService) {
		this.nodoHijoMapper = nodoHijoMapper;
		this.organizacionService = organizacionService;
		this.estadoService = estadoService;
	}

	public List<NodoArbolDTO> cargarArboles() {
		return cargarArboles(null);
	}

	public List<NodoArbolDTO> cargarArboles(List<String> process) {
		List<NodoArbolDTO> nodos = cargarNodos(process);
		List<PropiedadDTO> propiedades = nodoHijoMapper.listarPropiedadesFull();
		adjuntarRelaciones(propiedades, nodoHijoMapper.listarRelacionesFull());
		NodoArbolDTO raiz = construirArbol(nodos, propiedades);
		List<NodoArbolDTO> arboles = new ArrayList<>();
		if (raiz != null)
			arboles.add(raiz);
		return arboles;
	}

	public NodoArbolDTO construirArbolProceso(String llaveProceso) {
		if (llaveProceso == null || llaveProceso.isEmpty())
			return null;
		List<NodoArbolDTO> arboles = cargarArboles(List.of(llaveProceso));
		for (NodoArbolDTO arbol : arboles) {
			NodoArbolDTO proceso = buscarNodo(arbol, llaveProceso);
			if (proceso != null)
				return proceso;
		}
		return null;
	}

	private NodoArbolDTO buscarNodo(NodoArbolDTO nodo, String llave) {
		if (nodo == null)
			return null;
		if (llave.equals(nodo.getLlaveTabla()))
			return nodo;
		if (nodo.getHijos() != null) {
			for (NodoArbolDTO hijo : nodo.getHijos()) {
				NodoArbolDTO encontrado = buscarNodo(hijo, llave);
				if (encontrado != null)
					return encontrado;
			}
		}
		return null;
	}

	public NodoArbolDTO construirArbolSimple() throws ServerException {
		NodoArbolDTO raiz = new NodoArbolDTO();
		raiz.setCategoria(PropiedadValorDefinidoDTO.ORGANIZACION);
		raiz.setCamino(PropiedadValorDefinidoDTO.ORGANIZACION);
		OrganizacionDTO org = organizacionService.obtenerPrincipal();
		if (org != null) {
			raiz.setLlaveTabla(org.getLlaveTabla());
			raiz.setEstado(org.getEstado());
			raiz.setNombre(org.getNombre());
			raiz.setCodigo(org.getCodigo());
			raiz.setImagen(org.getImagen());
			copiarBasicos(raiz, org);
		}
		raiz.setTieneHijos(true);
		List<NodoArbolDTO> hijos = construirHijosNivel(PropiedadValorDefinidoDTO.ORGANIZACION, raiz.getLlaveTabla(),
				PropiedadValorDefinidoDTO.ORGANIZACION);
		raiz.setHijos(hijos.isEmpty() ? null : hijos);
		raiz.setTotalHijos(hijos.size());
		return raiz;
	}

	public NodoArbolDTO construirHijos(ArbolNodoRequestDTO request) throws ServerException {
		if (request == null || request.getTipo() == null || request.getTipo().isEmpty())
			throw new ServerException("El tipo del nodo es obligatorio");
		String tipoPadre = request.getTipo();
		String llavePadre = request.getLlaveTabla();
		String caminoPadre = request.getCamino();
		if (caminoPadre == null || caminoPadre.isEmpty())
			caminoPadre = palabraCategoria(tipoPadre);
		List<NodoArbolDTO> hijos = construirHijosNivel(tipoPadre, llavePadre, caminoPadre);
		NodoArbolDTO nodo = new NodoArbolDTO();
		nodo.setCategoria(tipoPadre);
		nodo.setCamino(caminoPadre);
		nodo.setLlaveTabla(llavePadre);
		nodo.setHijos(hijos.isEmpty() ? null : hijos);
		nodo.setTieneHijos(admiteHijos(tipoPadre));
		nodo.setTotalHijos(hijos.size());
		return nodo;
	}

	private boolean admiteHijos(String categoria) {
		return PropiedadValorDefinidoDTO.ORGANIZACION.equals(categoria)
				|| PropiedadValorDefinidoDTO.PROCESO.equals(categoria)
				|| PropiedadValorDefinidoDTO.PLANTILLA.equals(categoria);
	}

	private List<NodoArbolDTO> construirHijosNivel(String categoriaPadre, String llavePadre, String caminoPadre) {
		List<NodoArbolDTO> hijos = new ArrayList<>();
		List<NodoArbolDTO> nodosHijos = nodoHijoMapper.listarHijos(categoriaPadre, llavePadre);
		if (nodosHijos != null) {
			for (NodoArbolDTO nodo : nodosHijos) {
				String categoria = nodo.getCategoria();
				String padre = (caminoPadre == null || caminoPadre.isEmpty())
						? PropiedadValorDefinidoDTO.ORGANIZACION
						: caminoPadre;
				nodo.setCamino(padre + "/" + palabraCategoria(categoria) + ":"
						+ etiqueta(nodo.getNombre(), nodo.getCodigo(), nodo.getLlaveTabla()));
				nodo.setTieneHijos(admiteHijos(categoria));

				if (PropiedadValorDefinidoDTO.PLANTILLA.equals(categoria)) {
					boolean tieneHijas = plantillaTieneHijos(nodo.getLlaveTabla());
					nodo.setTieneHijos(tieneHijas);
					nodo.setTotalHijos(tieneHijas ? 1 : 0);
				}

				hijos.add(nodo);
			}
		}
		return hijos;
	}

	private boolean plantillaTieneHijos(String llavePlantilla) {
		if (llavePlantilla == null)
			return false;
		List<NodoArbolDTO> subPlantillas = nodoHijoMapper.listarHijos(PropiedadValorDefinidoDTO.PLANTILLA,
				llavePlantilla);
		return subPlantillas != null && !subPlantillas.isEmpty();
	}

	private void copiarBasicos(NodoArbolDTO destino, OrganizacionDTO origen) {
		if (origen == null)
			return;
		destino.setNodoSuperior(origen.getNodoSuperior());
		destino.setNodoSuperiorNombre(origen.getNodoSuperiorNombre());
		destino.setPrioridad(origen.getPrioridad());
	}

	private String etiqueta(String nombre, String codigo, String llave) {
		if (codigo != null && !codigo.isEmpty())
			return codigo;
		if (nombre != null && !nombre.isEmpty())
			return nombre;
		return llave;
	}

	private List<NodoArbolDTO> cargarNodos(List<String> process) {
		List<NodoArbolDTO> nodos = new ArrayList<>();
		NodoArbolDTO organizacion = nodoHijoMapper.obtenerOrganizacionPrincipal();
		if (organizacion != null)
			nodos.add(organizacion);
		agregar(nodos, nodoHijoMapper.listarProcesosFull(process));
		agregar(nodos, nodoHijoMapper.listarApisFull(process));
		agregar(nodos, nodoHijoMapper.listarEstadosFull(process));
		agregar(nodos, nodoHijoMapper.listarPlantillasFull(process));
		agregar(nodos, nodoHijoMapper.listarTransicionesFull(process));
		agregar(nodos, nodoHijoMapper.listarReportesFull(process));
		agregar(nodos, nodoHijoMapper.listarCamposFull(process));
		agregar(nodos, nodoHijoMapper.listarIndicadoresFull(process));
		return nodos;
	}

	private void agregar(List<NodoArbolDTO> destino, List<? extends NodoArbolDTO> nodos) {
		if (nodos == null)
			return;
		for (NodoArbolDTO nodo : nodos) {
			if (nodo != null)
				destino.add(nodo);
		}
	}

	private NodoArbolDTO construirArbol(List<NodoArbolDTO> nodos, List<PropiedadDTO> propiedades) {
		if (nodos == null || nodos.isEmpty())
			return null;

		NodoArbolDTO raiz = null;
		Map<String, NodoArbolDTO> porLlave = new HashMap<>();
		for (NodoArbolDTO nodo : nodos) {
			if (raiz == null && PropiedadValorDefinidoDTO.ORGANIZACION.equals(nodo.getCategoria()))
				raiz = nodo;
			if (nodo.getLlaveTabla() != null)
				porLlave.put(nodo.getLlaveTabla(), nodo);
		}
		if (raiz == null) {
			raiz = new NodoArbolDTO();
			raiz.setCategoria(PropiedadValorDefinidoDTO.ORGANIZACION);
		}

		Map<NodoArbolDTO, NodoArbolDTO> padreAsignado = new IdentityHashMap<>();
		for (NodoArbolDTO nodo : nodos) {
			if (nodo == raiz)
				continue;
			NodoArbolDTO padre = resolverPadre(nodo, raiz, porLlave);
			if (padre == null || padre == nodo || esAncestro(nodo, padre, padreAsignado))
				padre = raiz;
			if (padre.getHijos() == null)
				padre.setHijos(new ArrayList<>());
			if (!padre.getHijos().contains(nodo))
				padre.getHijos().add(nodo);
			padreAsignado.put(nodo, padre);
		}

		Map<String, List<PropiedadDTO>> propiedadesIndex = indexarPropiedades(propiedades);
		asignarCamino(raiz, null, propiedadesIndex, new HashSet<>());
		return raiz;
	}

	private boolean esAncestro(NodoArbolDTO nodo, NodoArbolDTO posibleDescendiente,
			Map<NodoArbolDTO, NodoArbolDTO> padreAsignado) {
		NodoArbolDTO actual = posibleDescendiente;
		Set<NodoArbolDTO> visitados = new HashSet<>();
		while (actual != null && visitados.add(actual)) {
			if (actual == nodo)
				return true;
			actual = padreAsignado.get(actual);
		}
		return false;
	}

	private NodoArbolDTO resolverPadre(NodoArbolDTO nodo, NodoArbolDTO raiz, Map<String, NodoArbolDTO> porLlave) {
		String llaveSuperior = nodo.getNodoSuperior();
		if (llaveSuperior == null || llaveSuperior.isEmpty() || llaveSuperior.equals(nodo.getLlaveTabla()))
			return raiz;
		NodoArbolDTO padre = porLlave.get(llaveSuperior);
		if (padre == null) {
			String categoria = nodo.getCategoria();
			if (PropiedadValorDefinidoDTO.PLANTILLA.equals(categoria)) {
				String llaveProceso = nodo.getAtt1();
				padre = llaveProceso == null ? null : porLlave.get(llaveProceso);
				if (padre != null && !PropiedadValorDefinidoDTO.PROCESO.equals(padre.getCategoria()))
					padre = null;
			}
		}
		if (padre == null)
			return raiz;
		String categoriaPadre = padre.getCategoria();
		String categoria = nodo.getCategoria();
		if (PropiedadValorDefinidoDTO.PROCESO.equals(categoria)
				|| PropiedadValorDefinidoDTO.API_SERVICE.equals(categoria)) {
			if (!PropiedadValorDefinidoDTO.PROCESO.equals(categoriaPadre))
				return raiz;
		}
		if (PropiedadValorDefinidoDTO.ESTADO.equals(categoria)
				|| PropiedadValorDefinidoDTO.TRANSICION.equals(categoria)) {
			if (!PropiedadValorDefinidoDTO.PROCESO.equals(categoriaPadre))
				return raiz;
		}
		if (PropiedadValorDefinidoDTO.PLANTILLA.equals(categoria)) {
			if (!PropiedadValorDefinidoDTO.PROCESO.equals(categoriaPadre)
					&& !PropiedadValorDefinidoDTO.PLANTILLA.equals(categoriaPadre))
				return raiz;
		}
		if (PropiedadValorDefinidoDTO.CAMPO.equals(categoria)
				|| PropiedadValorDefinidoDTO.REPORTE.equals(categoria)) {
			if (!PropiedadValorDefinidoDTO.PLANTILLA.equals(categoriaPadre))
				return raiz;
		}
		if (PropiedadValorDefinidoDTO.INDICATOR.equals(categoria)) {
			if (!PropiedadValorDefinidoDTO.PROCESO.equals(categoriaPadre))
				return raiz;
		}
		return padre;
	}

	private void asignarCamino(NodoArbolDTO nodo, String caminoPadre,
			Map<String, List<PropiedadDTO>> propiedadesIndex, Set<NodoArbolDTO> visitados) {
		if (nodo == null || !visitados.add(nodo))
			return;
		String categoria = nodo.getCategoria();
		if (PropiedadValorDefinidoDTO.ORGANIZACION.equals(categoria) && caminoPadre == null)
			nodo.setCamino(PropiedadValorDefinidoDTO.ORGANIZACION);
		else
			nodo.setCamino((caminoPadre == null ? "" : caminoPadre + "/") + palabraCategoria(categoria) + ":"
					+ etiqueta(nodo));
		List<PropiedadDTO> propiedades = propiedadesIndex.get(categoria + "|" + nodo.getLlaveTabla());
		nodo.setPropiedades(propiedades == null || propiedades.isEmpty() ? null : propiedades);
		List<NodoArbolDTO> hijos = nodo.getHijos();
		nodo.setTotalHijos(hijos == null ? 0 : hijos.size());
		nodo.setTieneHijos(hijos != null && !hijos.isEmpty());
		if (hijos != null) {
			for (NodoArbolDTO hijo : hijos)
				asignarCamino(hijo, nodo.getCamino(), propiedadesIndex, visitados);
		}
	}

	private String etiqueta(NodoArbolDTO nodo) {
		if (nodo.getCodigo() != null && !nodo.getCodigo().isEmpty())
			return nodo.getCodigo();
		if (nodo.getNombre() != null && !nodo.getNombre().isEmpty())
			return nodo.getNombre();
		return nodo.getLlaveTabla();
	}

	private String palabraCategoria(String categoria) {
		if (PropiedadValorDefinidoDTO.ORGANIZACION.equals(categoria))
			return "ORGANIZACION";
		if (PropiedadValorDefinidoDTO.PROCESO.equals(categoria))
			return "PROCESO";
		if (PropiedadValorDefinidoDTO.ESTADO.equals(categoria))
			return "ESTADO";
		if (PropiedadValorDefinidoDTO.TRANSICION.equals(categoria))
			return "TRANSICION";
		if (PropiedadValorDefinidoDTO.PLANTILLA.equals(categoria))
			return "PLANTILLA";
		if (PropiedadValorDefinidoDTO.CAMPO.equals(categoria))
			return "CAMPO";
		if (PropiedadValorDefinidoDTO.REPORTE.equals(categoria))
			return "REPORTE";
		if (PropiedadValorDefinidoDTO.API_SERVICE.equals(categoria))
			return "API";
		if (PropiedadValorDefinidoDTO.INDICATOR.equals(categoria))
			return "INDICADOR";
		return categoria;
	}

	private Map<String, List<PropiedadDTO>> indexarPropiedades(List<PropiedadDTO> propiedades) {
		Map<String, List<PropiedadDTO>> index = new LinkedHashMap<>();
		if (propiedades == null)
			return index;
		for (PropiedadDTO propiedad : propiedades) {
			if (propiedad.getTipo() == null || propiedad.getCampo() == null)
				continue;
			index.computeIfAbsent(propiedad.getTipo() + "|" + propiedad.getCampo(), key -> new ArrayList<>())
					.add(propiedad);
		}
		return index;
	}

	private void adjuntarRelaciones(List<PropiedadDTO> propiedades, List<RelacionInternaDTO> relaciones) {
		if (propiedades == null || relaciones == null)
			return;
		Map<String, List<RelacionInternaDTO>> index = new HashMap<>();
		for (RelacionInternaDTO relacion : relaciones) {
			if (relacion.getPropiedad() == null)
				continue;
			index.computeIfAbsent(relacion.getPropiedad(), key -> new ArrayList<>()).add(relacion);
		}
		for (PropiedadDTO propiedad : propiedades) {
			List<RelacionInternaDTO> propias = index.get(propiedad.getLlaveTabla());
			if (propias != null && !propias.isEmpty()) {
				propiedad.setRelacionesInternas(propias);
				propiedad.setRelaciones(propias.size());
			}
		}
	}
	
	public NodoArbolDTO obtenerProcesoParaGraficar(ProcesoFilterDTO dto) throws ServerException {
		String llaveProceso = dto == null ? null : dto.getLlaveTabla();
		if (llaveProceso == null && dto != null && dto.getEstado() != null) {
			ProcesoEstadoDTO estado = estadoService.consultaXId(dto.getEstado());
			if (estado == null)
				throw new ServerException("El estado enviado no se identifica");
			llaveProceso = estado.getNodoSuperior();
		}
		if (llaveProceso == null || llaveProceso.isEmpty())
			throw new ServerException("No se identifica el proceso");
		NodoArbolDTO proceso = construirArbolProceso(llaveProceso);
		if (proceso == null)
			throw new ServerException("El proceso no se identifica");
		return proceso;
	}

}
