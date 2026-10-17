package d3.configuration.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import d3.configuration.domain.DetalleCampoDTO;
import d3.configuration.domain.DiferenciaDTO;
import d3.configuration.domain.PropiedadDTO;
import d3.configuration.domain.TreeNodeDTO;

@Service
public class ComparacionArbolSvc {

	private static final String SIN_VALOR = "—";

	private final ObjectMapper mapper;

	public ComparacionArbolSvc(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	public List<DiferenciaDTO> comparar(TreeNodeDTO actual, TreeNodeDTO remoto) {
		List<DiferenciaDTO> resultado = new ArrayList<>();
		if (actual == null || remoto == null)
			return resultado;
		ContextoComparacion contexto = new ContextoComparacion();
		contexto.llavesLocal = indexarLlaves(actual);
		contexto.llavesRemoto = indexarLlaves(remoto);
		Map<String, TreeNodeDTO> localHijos = indexar(actual.getHijos());
		Map<String, TreeNodeDTO> remotoHijos = indexar(remoto.getHijos());
		if (remoto.getHijos() != null) {
			for (TreeNodeDTO remotoHijo : remoto.getHijos()) {
				DiferenciaDTO diferencia = compararNodo(localHijos.get(remotoHijo.getCamino()), remotoHijo, contexto);
				if (diferencia != null)
					resultado.add(diferencia);
			}
		}
		if (actual.getHijos() != null) {
			for (TreeNodeDTO localHijo : actual.getHijos()) {
				if (!remotoHijos.containsKey(localHijo.getCamino())) {
					resultado.add(construirLocalSinRemoto(localHijo));
				}
			}
		}
		return resultado;
	}

	private DiferenciaDTO compararNodo(TreeNodeDTO local, TreeNodeDTO remoto, ContextoComparacion contexto) {
		if (local == null) {
			DiferenciaDTO diferencia = new DiferenciaDTO();
			diferencia.setTipoDiferencia(DiferenciaDTO.CREAR);
			diferencia.setNodo(nodoLigero(remoto));
			diferencia.setCamino(remoto.getCamino());
			diferencia.setCodigoRemoto(remoto.getCodigo());
			diferencia.setLlaveRemota(remoto.getLlaveTabla());
			if (remoto.getHijos() != null && !remoto.getHijos().isEmpty()) {
				List<DiferenciaDTO> hijos = new ArrayList<>();
				for (TreeNodeDTO hijo : remoto.getHijos()) {
					DiferenciaDTO hijoDif = compararNodo(null, hijo, contexto);
					if (hijoDif != null)
						hijos.add(hijoDif);
				}
				diferencia.setHijos(hijos);
			}
			return diferencia;
		}
		List<DetalleCampoDTO> detalles = detallesDiferentes(local, remoto, contexto);
		List<String> camposDiferentes = new ArrayList<>();
		for (DetalleCampoDTO detalle : detalles)
			camposDiferentes.add(detalle.getCampo());
		DiferenciaDTO diferencia = null;
		if (!camposDiferentes.isEmpty()) {
			diferencia = new DiferenciaDTO();
			diferencia.setTipoDiferencia(DiferenciaDTO.ACTUALIZAR);
			diferencia.setNodo(nodoLigero(remoto));
			diferencia.setCamino(remoto.getCamino());
			diferencia.setCamposDiferentes(camposDiferentes);
			diferencia.setDetalles(detalles);
			diferencia.setCodigoLocal(local.getCodigo());
			diferencia.setCodigoRemoto(remoto.getCodigo());
			diferencia.setLlaveLocal(local.getLlaveTabla());
			diferencia.setLlaveRemota(remoto.getLlaveTabla());
		}
		List<DiferenciaDTO> hijos = new ArrayList<>();
		if (remoto.getHijos() != null) {
			Map<String, TreeNodeDTO> localHijos = indexar(local.getHijos());
			Map<String, TreeNodeDTO> remotoHijos = indexar(remoto.getHijos());
			for (TreeNodeDTO remotoHijo : remoto.getHijos()) {
				DiferenciaDTO hijoDif = compararNodo(localHijos.get(remotoHijo.getCamino()), remotoHijo, contexto);
				if (hijoDif != null)
					hijos.add(hijoDif);
			}
			if (local.getHijos() != null) {
				for (TreeNodeDTO localHijo : local.getHijos()) {
					if (!remotoHijos.containsKey(localHijo.getCamino()))
						hijos.add(construirLocalSinRemoto(localHijo));
				}
			}
		}
		if (!hijos.isEmpty()) {
			if (diferencia == null) {
				diferencia = new DiferenciaDTO();
				diferencia.setTipoDiferencia(DiferenciaDTO.SIN_DIFERENCIA);
				diferencia.setNodo(nodoLigero(remoto));
				diferencia.setCamino(remoto.getCamino());
				diferencia.setCodigoLocal(local.getCodigo());
				diferencia.setCodigoRemoto(remoto.getCodigo());
				diferencia.setLlaveLocal(local.getLlaveTabla());
				diferencia.setLlaveRemota(remoto.getLlaveTabla());
			}
			diferencia.setHijos(hijos);
		}
		return diferencia;
	}

	private DiferenciaDTO construirLocalSinRemoto(TreeNodeDTO local) {
		DiferenciaDTO diferencia = new DiferenciaDTO();
		diferencia.setTipoDiferencia(DiferenciaDTO.LOCAL_SIN_REMOTO);
		diferencia.setNodo(nodoLigero(local));
		diferencia.setCamino(local.getCamino());
		diferencia.setCodigoLocal(local.getCodigo());
		diferencia.setLlaveLocal(local.getLlaveTabla());
		return diferencia;
	}

	private List<DetalleCampoDTO> detallesDiferentes(TreeNodeDTO local, TreeNodeDTO remoto,
			ContextoComparacion contexto) {
		List<DetalleCampoDTO> detalles = new ArrayList<>();
		if (!Objects.equals(local.getNombre(), remoto.getNombre()))
			detalles.add(new DetalleCampoDTO("nombre", local.getNombre(), remoto.getNombre()));
		if (!Objects.equals(local.getCodigo(), remoto.getCodigo()))
			detalles.add(new DetalleCampoDTO("codigo", local.getCodigo(), remoto.getCodigo()));
		if (!Objects.equals(local.getImagen(), remoto.getImagen()))
			detalles.add(new DetalleCampoDTO("imagen", local.getImagen(), remoto.getImagen()));
		if (!Objects.equals(local.getEstado(), remoto.getEstado()))
			detalles.add(new DetalleCampoDTO("estado", local.getEstado(), remoto.getEstado()));

		Map<String, Object> datoLocal = aMapa(local);
		Map<String, Object> datoRemoto = aMapa(remoto);
		if (datoLocal != null && datoRemoto != null) {
			for (String campo : camposPorTipo(remoto.getTipo())) {
				Object valorLocal = valorCampo(datoLocal, campo);
				Object valorRemoto = valorCampo(datoRemoto, campo);
				if (esLlaveForanea(campo)) {
					String codigoLocal = resolverCodigo(contexto.llavesLocal, valorLocal);
					String codigoRemoto = resolverCodigo(contexto.llavesRemoto, valorRemoto);
					if (!Objects.equals(codigoLocal, codigoRemoto))
						detalles.add(new DetalleCampoDTO(campo, codigoLocal, codigoRemoto));
				} else if (!Objects.equals(valorLocal, valorRemoto)) {
					detalles.add(new DetalleCampoDTO(campo, valorLocal, valorRemoto));
				}
			}
		}
		detalles.addAll(diferenciasPropiedades(local, remoto));
		return detalles;
	}

	private boolean esLlaveForanea(String campo) {
		return "plantilla".equals(campo) || "proceso".equals(campo) || "padre".equals(campo)
				|| "estadoPartida".equals(campo) || "estadoLLegada".equals(campo) || "macroproceso".equals(campo);
	}

	private String resolverCodigo(Map<String, String> llaves, Object llave) {
		if (llave == null)
			return null;
		String codigo = llaves.get(String.valueOf(llave));
		return codigo != null ? codigo : String.valueOf(llave);
	}

	private List<DetalleCampoDTO> diferenciasPropiedades(TreeNodeDTO local, TreeNodeDTO remoto) {
		List<DetalleCampoDTO> detalles = new ArrayList<>();
		List<PropiedadDTO> locales = local.getPropiedades() != null ? local.getPropiedades() : List.of();
		List<PropiedadDTO> remotas = remoto.getPropiedades() != null ? remoto.getPropiedades() : List.of();
		if (locales.isEmpty() && remotas.isEmpty())
			return detalles;
		List<PropiedadDTO> pendientes = new ArrayList<>(locales);
		for (PropiedadDTO remota : remotas) {
			PropiedadDTO encontrada = quitarPropiedadCoincidente(pendientes, remota);
			String etiqueta = etiquetaPropiedad(remota);
			if (encontrada == null) {
				detalles.add(new DetalleCampoDTO("propiedad:" + clavePropiedad(remota), SIN_VALOR, etiqueta));
			} else if (!Objects.equals(etiquetaPropiedad(encontrada), etiqueta)) {
				detalles.add(new DetalleCampoDTO("propiedad:" + clavePropiedad(remota), etiquetaPropiedad(encontrada),
						etiqueta));
			}
		}
		for (PropiedadDTO faltante : pendientes) {
			detalles.add(new DetalleCampoDTO("propiedad:" + clavePropiedad(faltante), etiquetaPropiedad(faltante),
					SIN_VALOR));
		}
		return detalles;
	}

	private PropiedadDTO quitarPropiedadCoincidente(List<PropiedadDTO> pendientes, PropiedadDTO remota) {
		for (int i = 0; i < pendientes.size(); i++) {
			if (mismoIdentificadorPropiedad(pendientes.get(i), remota))
				return pendientes.remove(i);
		}
		return null;
	}

	private boolean mismoIdentificadorPropiedad(PropiedadDTO local, PropiedadDTO remota) {
		if (!Objects.equals(local.getPropiedadValor(), remota.getPropiedadValor()))
			return false;
		if (!Objects.equals(local.getRol(), remota.getRol()))
			return false;
		if (!Objects.equals(local.getRolExcluyente(), remota.getRolExcluyente()))
			return false;
		return Objects.equals(local.getMotivo(), remota.getMotivo());
	}

	private String clavePropiedad(PropiedadDTO propiedad) {
		StringBuilder clave = new StringBuilder();
		clave.append(propiedad.getNombre() != null ? propiedad.getNombre() : propiedad.getPropiedadValor());
		if (propiedad.getRolNombre() != null)
			clave.append(" [rol: ").append(propiedad.getRolNombre()).append("]");
		else if (propiedad.getRol() != null)
			clave.append(" [rol: ").append(propiedad.getRol()).append("]");
		if (propiedad.getMotivo() != null)
			clave.append(" (").append(propiedad.getMotivo()).append(")");
		return clave.toString();
	}

	private String etiquetaPropiedad(PropiedadDTO propiedad) {
		if (propiedad.getTexto() != null)
			return propiedad.getTexto();
		if (propiedad.getValor() != null)
			return propiedad.getValor();
		return SIN_VALOR;
	}

	private List<String> camposPorTipo(String tipo) {
		switch (tipo) {
		case TreeNodeDTO.PROCESO_MACRO:
		case TreeNodeDTO.PROCESO:
			return List.of("objetivo", "prioridad", "tipo", "macroproceso");
		case TreeNodeDTO.ESTADO:
			return List.of("estadoDocumento", "avance", "tipo");
		case TreeNodeDTO.TRANSICION:
			return List.of("afectaSaldo", "documentador", "rapida", "plantilla", "estadoPartida", "estadoLLegada");
		case TreeNodeDTO.PLANTILLA:
		case TreeNodeDTO.PLANTILLA_MODIFICACION:
		case TreeNodeDTO.PLANTILLA_ANULACION:
		case TreeNodeDTO.PLANTILLA_ACTIVACION:
			return List.of("consecutivo", "tipo", "padre");
		case TreeNodeDTO.CAMPO:
			return List.of("formato", "orden", "objetivo");
		case TreeNodeDTO.REPORTE:
			return List.of("descripcion", "version", "servidor", "publico", "multiplesId", "soloExistente");
		case TreeNodeDTO.API:
			return List.of("nombre");
		case TreeNodeDTO.ROL:
			return List.of("nombre");
		default:
			return List.of();
		}
	}

	private TreeNodeDTO nodoLigero(TreeNodeDTO origen) {
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
		return nodo;
	}

	private Map<String, String> indexarLlaves(TreeNodeDTO raiz) {
		Map<String, String> index = new HashMap<>();
		if (raiz != null)
			indexarLlavesNodo(raiz, index);
		return index;
	}

	private void indexarLlavesNodo(TreeNodeDTO nodo, Map<String, String> index) {
		if (nodo.getLlaveTabla() != null && nodo.getCodigo() != null)
			index.put(nodo.getLlaveTabla(), nodo.getCodigo());
		if (nodo.getHijos() != null) {
			for (TreeNodeDTO hijo : nodo.getHijos())
				indexarLlavesNodo(hijo, index);
		}
	}

	@SuppressWarnings({ "unchecked" })
	private Map<String, Object> aMapa(TreeNodeDTO nodo) {
		if (nodo.getDato() == null)
			return null;
		try {
			return mapper.convertValue(nodo.getDato(), Map.class);
		} catch (Exception e) {
			return null;
		}
	}

	private Object valorCampo(Map<String, Object> dato, String campo) {
		return dato.get(campo);
	}

	private Map<String, TreeNodeDTO> indexar(List<TreeNodeDTO> hijos) {
		Map<String, TreeNodeDTO> index = new LinkedHashMap<>();
		if (hijos != null) {
			for (TreeNodeDTO hijo : hijos) {
				if (hijo.getCamino() != null)
					index.put(hijo.getCamino(), hijo);
			}
		}
		return index;
	}

	private static class ContextoComparacion {
		private Map<String, String> llavesLocal;
		private Map<String, String> llavesRemoto;
	}

}
