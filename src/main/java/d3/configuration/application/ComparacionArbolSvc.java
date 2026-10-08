package d3.configuration.application;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import d3.configuration.domain.DetalleCampoDTO;
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
		Map<String, TreeNodeDTO> remotoHijos = indexar(remoto.getHijos());
		if (remoto.getHijos() != null) {
			for (TreeNodeDTO remotoHijo : remoto.getHijos()) {
				DiferenciaDTO diferencia = compararNodo(localHijos.get(remotoHijo.getCamino()), remotoHijo);
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

	private DiferenciaDTO compararNodo(TreeNodeDTO local, TreeNodeDTO remoto) {
		if (local == null) {
			DiferenciaDTO diferencia = new DiferenciaDTO();
			diferencia.setTipoDiferencia(DiferenciaDTO.CREAR);
			diferencia.setNodo(remoto);
			diferencia.setCamino(remoto.getCamino());
			diferencia.setCodigoRemoto(remoto.getCodigo());
			diferencia.setLlaveRemota(remoto.getLlaveTabla());
			if (remoto.getHijos() != null && !remoto.getHijos().isEmpty()) {
				List<DiferenciaDTO> hijos = new ArrayList<>();
				for (TreeNodeDTO hijo : remoto.getHijos()) {
					DiferenciaDTO hijoDif = compararNodo(null, hijo);
					if (hijoDif != null)
						hijos.add(hijoDif);
				}
				diferencia.setHijos(hijos);
			}
			return diferencia;
		}
		List<DetalleCampoDTO> detalles = detallesDiferentes(local, remoto);
		List<String> camposDiferentes = new ArrayList<>();
		for (DetalleCampoDTO detalle : detalles)
			camposDiferentes.add(detalle.getCampo());
		DiferenciaDTO diferencia = null;
		if (!camposDiferentes.isEmpty()) {
			diferencia = new DiferenciaDTO();
			diferencia.setTipoDiferencia(DiferenciaDTO.ACTUALIZAR);
			diferencia.setNodo(remoto);
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
				DiferenciaDTO hijoDif = compararNodo(localHijos.get(remotoHijo.getCamino()), remotoHijo);
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
				diferencia.setNodo(remoto);
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
		diferencia.setNodo(local);
		diferencia.setCamino(local.getCamino());
		diferencia.setCodigoLocal(local.getCodigo());
		diferencia.setLlaveLocal(local.getLlaveTabla());
		return diferencia;
	}

	private List<DetalleCampoDTO> detallesDiferentes(TreeNodeDTO local, TreeNodeDTO remoto) {
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
				if (!Objects.equals(valorLocal, valorRemoto))
					detalles.add(new DetalleCampoDTO(campo, valorLocal, valorRemoto));
			}
		}
		return detalles;
	}

	private List<String> camposPorTipo(String tipo) {
		switch (tipo) {
		case TreeNodeDTO.PROCESO_MACRO:
		case TreeNodeDTO.PROCESO:
			return List.of("objetivo", "prioridad", "tipo");
		case TreeNodeDTO.ESTADO:
			return List.of("estadoDocumento", "avance", "tipo");
		case TreeNodeDTO.TRANSICION:
			return List.of("afectaSaldo", "documentador", "rapida");
		case TreeNodeDTO.PLANTILLA:
		case TreeNodeDTO.PLANTILLA_MODIFICACION:
		case TreeNodeDTO.PLANTILLA_ANULACION:
		case TreeNodeDTO.PLANTILLA_ACTIVACION:
			return List.of("consecutivo", "tipo", "padre");
		case TreeNodeDTO.CAMPO:
			return List.of("formato", "orden", "objetivo");
		case TreeNodeDTO.REPORTE:
			return List.of("descripcion", "version", "servidor", "publico");
		case TreeNodeDTO.MENSAJE:
			return List.of("titulo", "texto", "servidor");
		case TreeNodeDTO.API:
			return List.of("nombre");
		case TreeNodeDTO.ROL:
			return List.of("nombre");
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
