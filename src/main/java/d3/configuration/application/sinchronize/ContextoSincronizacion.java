package d3.configuration.application.sinchronize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import d3.configuration.domain.NodoArbolDTO;
import d3.configuration.domain.PropiedadDTO;

public class ContextoSincronizacion {

	private final List<NodoArbolDTO> nodos = new ArrayList<>();
	private final Map<NodoArbolDTO, NodoArbolDTO> padres = new IdentityHashMap<>();
	private final Map<String, NodoArbolDTO> nodosPorLlave = new HashMap<>();
	private final Map<String, Map<String, String>> llavesLocales = new HashMap<>();
	private final Map<String, Map<String, String>> estadosLocales = new HashMap<>();
	private List<PropiedadDTO> propiedades = new ArrayList<>();

	private ContextoSincronizacion() {
	}

	public static ContextoSincronizacion crear(List<NodoArbolDTO> arboles) {
		ContextoSincronizacion contexto = new ContextoSincronizacion();
		if (arboles == null)
			return contexto;
		for (NodoArbolDTO arbol : arboles)
			contexto.recorrer(arbol, null);
		return contexto;
	}

	private void recorrer(NodoArbolDTO nodo, NodoArbolDTO padre) {
		if (nodo == null)
			return;
		nodos.add(nodo);
		if (padre != null)
			padres.put(nodo, padre);
		if (nodo.getLlaveTabla() != null)
			nodosPorLlave.put(nodo.getLlaveTabla(), nodo);
		if (nodo.getPropiedades() != null)
			propiedades.addAll(nodo.getPropiedades());
		if (nodo.getHijos() != null) {
			for (NodoArbolDTO hijo : nodo.getHijos())
				recorrer(hijo, nodo);
		}
	}

	public List<NodoArbolDTO> getNodos() {
		return nodos;
	}

	public List<PropiedadDTO> getPropiedades() {
		return propiedades;
	}

	public void setPropiedades(List<PropiedadDTO> propiedades) {
		this.propiedades = propiedades == null ? new ArrayList<>() : propiedades;
	}

	public List<NodoArbolDTO> porCategoria(String categoria) {
		List<NodoArbolDTO> resultado = new ArrayList<>();
		for (NodoArbolDTO nodo : nodos) {
			if (categoria != null && categoria.equals(nodo.getCategoria()))
				resultado.add(nodo);
		}
		return resultado;
	}

	public NodoArbolDTO porLlave(String llave) {
		return llave == null ? null : nodosPorLlave.get(llave);
	}

	public NodoArbolDTO padreDe(NodoArbolDTO nodo) {
		return nodo == null ? null : padres.get(nodo);
	}

	public void registrarLocal(NodoArbolDTO remoto, String llaveLocal) {
		if (remoto == null || remoto.getCategoria() == null || remoto.getLlaveTabla() == null
				|| llaveLocal == null)
			return;
		llavesLocales.computeIfAbsent(remoto.getCategoria(), key -> new HashMap<>()).put(remoto.getLlaveTabla(),
				llaveLocal);
	}

	public String local(NodoArbolDTO remoto) {
		if (remoto == null || remoto.getCategoria() == null || remoto.getLlaveTabla() == null)
			return null;
		Map<String, String> porLlave = llavesLocales.get(remoto.getCategoria());
		return porLlave == null ? null : porLlave.get(remoto.getLlaveTabla());
	}

	public String localPorCategoria(String categoria, String llaveRemota) {
		if (categoria == null || llaveRemota == null)
			return null;
		Map<String, String> porLlave = llavesLocales.get(categoria);
		return porLlave == null ? null : porLlave.get(llaveRemota);
	}

	public String localPadre(NodoArbolDTO remoto) {
		NodoArbolDTO padre = padreDe(remoto);
		return padre == null ? null : local(padre);
	}

	public String categoriaPadre(NodoArbolDTO remoto) {
		NodoArbolDTO padre = padreDe(remoto);
		return padre == null ? null : padre.getCategoria();
	}

	public void registrarEstado(String llaveProcesoLocal, String codigo, String llaveEstadoLocal) {
		if (llaveProcesoLocal == null || codigo == null || llaveEstadoLocal == null)
			return;
		estadosLocales.computeIfAbsent(llaveProcesoLocal, key -> new HashMap<>()).put(codigo, llaveEstadoLocal);
	}

	public String estadoLocal(String llaveProcesoLocal, String codigo) {
		if (llaveProcesoLocal == null || codigo == null)
			return null;
		Map<String, String> porCodigo = estadosLocales.get(llaveProcesoLocal);
		return porCodigo == null ? null : porCodigo.get(codigo);
	}

}
