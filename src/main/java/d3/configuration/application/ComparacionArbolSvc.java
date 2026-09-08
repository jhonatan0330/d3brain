package d3.configuration.application;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import d3.configuration.domain.DiferenciaDTO;
import d3.configuration.domain.TreeNodeDTO;

@Service
public class ComparacionArbolSvc {

	private final ObjectMapper mapper;

	public ComparacionArbolSvc(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	public List<DiferenciaDTO> comparar(TreeNodeDTO actual, TreeNodeDTO remoto) {
		List<DiferenciaDTO> resultado = new ArrayList<>();
		if (actual == null || remoto == null)
			return resultado;
		Map<String, TreeNodeDTO> localHijos = indexar(actual.getHijos());
		if (remoto.getHijos() != null) {
			for (TreeNodeDTO remotoHijo : remoto.getHijos()) {
				compararNodo(localHijos.get(remotoHijo.getCamino()), remotoHijo, resultado);
			}
		}
		return resultado;
	}

	private void compararNodo(TreeNodeDTO local, TreeNodeDTO remoto, List<DiferenciaDTO> resultado) {
		if (local == null) {
			DiferenciaDTO diferencia = new DiferenciaDTO();
			diferencia.setTipoDiferencia(DiferenciaDTO.CREAR);
			diferencia.setNodo(remoto);
			diferencia.setCamino(remoto.getCamino());
			resultado.add(diferencia);
			return;
		}
		List<String> camposDiferentes = camposDiferentes(local, remoto);
		if (!camposDiferentes.isEmpty()) {
			DiferenciaDTO diferencia = new DiferenciaDTO();
			diferencia.setTipoDiferencia(DiferenciaDTO.ACTUALIZAR);
			diferencia.setNodo(remoto);
			diferencia.setCamino(remoto.getCamino());
			diferencia.setCamposDiferentes(camposDiferentes);
			resultado.add(diferencia);
		}
		if (remoto.getHijos() != null) {
			Map<String, TreeNodeDTO> localHijos = indexar(local.getHijos());
			for (TreeNodeDTO remotoHijo : remoto.getHijos()) {
				compararNodo(localHijos.get(remotoHijo.getCamino()), remotoHijo, resultado);
			}
		}
	}

	private List<String> camposDiferentes(TreeNodeDTO local, TreeNodeDTO remoto) {
		List<String> campos = new ArrayList<>();
		if (!Objects.equals(local.getNombre(), remoto.getNombre()))
			campos.add("nombre");
		if (!Objects.equals(local.getCodigo(), remoto.getCodigo()))
			campos.add("codigo");
		if (!Objects.equals(local.getImagen(), remoto.getImagen()))
			campos.add("imagen");
		if (!Objects.equals(local.getEstado(), remoto.getEstado()))
			campos.add("estado");

		Map<String, Object> datoLocal = aMapa(local);
		Map<String, Object> datoRemoto = aMapa(remoto);
		if (datoLocal != null && datoRemoto != null) {
			for (String campo : camposPorTipo(remoto.getTipo())) {
				if (!Objects.equals(valorCampo(datoLocal, campo), valorCampo(datoRemoto, campo)))
					campos.add(campo);
			}
		}
		return campos;
	}

	private List<String> camposPorTipo(String tipo) {
		switch (tipo) {
		case TreeNodeDTO.PROCESO_MACRO:
		case TreeNodeDTO.PROCESO:
			return List.of("objetivo", "prioridad");
		case TreeNodeDTO.ESTADO:
			return List.of("estadoDocumento", "avance");
		case TreeNodeDTO.PLANTILLA:
		case TreeNodeDTO.PLANTILLA_MODIFICACION:
		case TreeNodeDTO.PLANTILLA_ANULACION:
		case TreeNodeDTO.PLANTILLA_ACTIVACION:
			return List.of("consecutivo");
		case TreeNodeDTO.CAMPO:
			return List.of("formato", "orden", "objetivo");
		case TreeNodeDTO.REPORTE:
			return List.of("descripcion", "version");
		case TreeNodeDTO.MENSAJE:
			return List.of("titulo", "texto", "servidor");
		default:
			return List.of();
		}
	}

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

}