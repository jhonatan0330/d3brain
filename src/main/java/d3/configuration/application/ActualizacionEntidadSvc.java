package d3.configuration.application;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import d3.configuration.domain.TreeNodeDTO;
import d3.shared.domain.ServerException;

@Service
public class ActualizacionEntidadSvc {

	/*private final ObjectMapper mapper;
	private final ProcesoSvc procesoSvc;
	private final ProcesoEstadoSvc procesoEstadoSvc;
	private final ProcesoTransicionSvc procesoTransicionSvc;
	private final DocumentoPlantillaSvc documentoPlantillaSvc;
	private final DocumentoPlantillaCaracteristicaSvc documentoPlantillaCaracteristicaSvc;
	private final ReporteBaseSvc reporteBaseSvc;
	private final WebServiceSvc webServiceSvc;*/

	public ActualizacionEntidadSvc() {
			/*	ObjectMapper mapper, ProcesoSvc procesoSvc, ProcesoEstadoSvc procesoEstadoSvc,
			ProcesoTransicionSvc procesoTransicionSvc, DocumentoPlantillaSvc documentoPlantillaSvc,
			DocumentoPlantillaCaracteristicaSvc documentoPlantillaCaracteristicaSvc, ReporteBaseSvc reporteBaseSvc,
			WebServiceSvc webServiceSvc) {
		/*this.mapper = mapper;
		this.procesoSvc = procesoSvc;
		this.procesoEstadoSvc = procesoEstadoSvc;
		this.procesoTransicionSvc = procesoTransicionSvc;
		this.documentoPlantillaSvc = documentoPlantillaSvc;
		this.documentoPlantillaCaracteristicaSvc = documentoPlantillaCaracteristicaSvc;
		this.reporteBaseSvc = reporteBaseSvc;
		this.webServiceSvc = webServiceSvc;*/
	}

	public void actualizarArbol(TreeNodeDTO remoto, TreeNodeDTO local) throws ServerException {
		actualizarArbol(remoto, local, null);
	}

	public void actualizarArbol(TreeNodeDTO remoto, TreeNodeDTO local,
			List<d3.configuration.domain.SeleccionSincronizacionDTO> selecciones) throws ServerException {
		if (remoto == null || local == null)
			return;
		Map<String, TreeNodeDTO> index = indexar(local);
		actualizarArbol(remoto, index.get(remoto.getCamino()) != null ? index.get(remoto.getCamino()) : local,
				selecciones, mapearLlaves(remoto, local));
	}

	private void actualizarArbol(TreeNodeDTO remoto, TreeNodeDTO local,
			List<d3.configuration.domain.SeleccionSincronizacionDTO> selecciones, Map<String, String> mapeoLlaves)
			throws ServerException {
		if (remoto == null || local == null)
			return;
		Map<String, TreeNodeDTO> index = indexar(local);
		actualizarNodo(remoto, index.get(remoto.getCamino()), selecciones, mapeoLlaves);
		if (remoto.getHijos() != null) {
			for (TreeNodeDTO hijo : remoto.getHijos()) {
				actualizarArbol(hijo, local, selecciones, mapeoLlaves);
			}
		}
	}

	public void actualizarNodo(TreeNodeDTO remoto, TreeNodeDTO local) {
		actualizarNodo(remoto, local, null, mapearLlaves(remoto, local));
	}

	public void actualizarNodo(TreeNodeDTO remoto, TreeNodeDTO local, Map<String, String> mapeoLlaves)
			 {
		actualizarNodo(remoto, local, null, mapeoLlaves);
	}

	public void actualizarNodo(TreeNodeDTO remoto, TreeNodeDTO local,
			List<d3.configuration.domain.SeleccionSincronizacionDTO> selecciones, Map<String, String> mapeoLlaves)
			{
		if (local == null || remoto == null)// || remoto.getDato() == null || local.getDato() == null)
			return;
		if (selecciones != null) {
			String camino = remoto.getCamino() != null ? remoto.getCamino() : local.getCamino();
			for (d3.configuration.domain.SeleccionSincronizacionDTO seleccion : selecciones) {
				if (seleccion.getCamino() != null && seleccion.getCamino().equals(camino)) {
					if (d3.configuration.domain.SeleccionSincronizacionDTO.OMITIR.equals(seleccion.getAccion()))
						return;
					if (d3.configuration.domain.SeleccionSincronizacionDTO.CREAR.equals(seleccion.getAccion()))
						return;
				}
			}
		}
		if (mapeoLlaves == null)
			mapeoLlaves = mapearLlaves(remoto, local);
		/*switch (remoto.getTipo()) {
		case TreeNodeDTO.PROCESO:
			actualizarProceso(remoto, local, mapeoLlaves);
			break;
		case TreeNodeDTO.ESTADO:
			actualizarEstado(remoto, local, mapeoLlaves);
			break;
		case TreeNodeDTO.TRANSICION:
			actualizarTransicion(remoto, local, mapeoLlaves);
			break;
		case TreeNodeDTO.PLANTILLA:
			actualizarPlantilla(remoto, local, mapeoLlaves);
			break;
		case TreeNodeDTO.CAMPO:
			actualizarCampo(remoto, local, mapeoLlaves);
			break;
		case TreeNodeDTO.REPORTE:
			actualizarReporte(remoto, local, mapeoLlaves);
			break;
		case TreeNodeDTO.API:
			actualizarApi(remoto, local, mapeoLlaves);
			break;
		default:
			break;
		}*/
	}

	/*private static final List<String> CAMPOS_LLAVE_FORANEA = List.of("proceso", "plantilla", "padre",
			"macroproceso", "estadoPartida", "estadoLLegada");

	@SuppressWarnings("unchecked")
	private <T> T datosRemotos(TreeNodeDTO remoto, Class<T> tipo, Map<String, String> mapeoLlaves) {
		if (remoto.getDato() == null)
			return null;
		Map<String, Object> mapa = mapper.convertValue(remoto.getDato(), Map.class);
		if (mapeoLlaves != null) {
			for (String campo : CAMPOS_LLAVE_FORANEA) {
				Object valor = mapa.get(campo);
				if (valor == null)
					continue;
				String local = mapeoLlaves.get(String.valueOf(valor));
				if (local != null)
					mapa.put(campo, local);
			}
		}
		return mapper.convertValue(mapa, tipo);
	}*/

	/*
	private void actualizarProceso(TreeNodeDTO remoto, TreeNodeDTO local, Map<String, String> mapeoLlaves)
			throws ServerException {
		ProcesoDTO entidad = mapper.convertValue(local.getDato(), ProcesoDTO.class);
		ProcesoDTO datos = datosRemotos(remoto, ProcesoDTO.class, mapeoLlaves);
		entidad.setNombre(datos.getNombre());
		entidad.setImagen(datos.getImagen());
		entidad.setObjetivo(datos.getObjetivo());
		entidad.setPrioridad(datos.getPrioridad());
		entidad.setTipo(datos.getTipo());
		if (datos.getMacroproceso() != null)
			entidad.setMacroproceso(datos.getMacroproceso());
		updateProceso(entidad);
	}

	private void actualizarEstado(TreeNodeDTO remoto, TreeNodeDTO local, Map<String, String> mapeoLlaves)
			throws ServerException {
		ProcesoEstadoDTO entidad = mapper.convertValue(local.getDato(), ProcesoEstadoDTO.class);
		ProcesoEstadoDTO datos = datosRemotos(remoto, ProcesoEstadoDTO.class, mapeoLlaves);
		entidad.setNombre(datos.getNombre());
		entidad.setEstadoDocumento(datos.getEstadoDocumento());
		entidad.setAvance(datos.getAvance());
		entidad.setTipo(datos.getTipo());
		if (datos.getProceso() != null)
			entidad.setProceso(datos.getProceso());
		updateEstado(entidad);
	}

	private void actualizarTransicion(TreeNodeDTO remoto, TreeNodeDTO local, Map<String, String> mapeoLlaves)
			throws ServerException {
		ProcesoTransicionDTO entidad = mapper.convertValue(local.getDato(), ProcesoTransicionDTO.class);
		ProcesoTransicionDTO datos = datosRemotos(remoto, ProcesoTransicionDTO.class, mapeoLlaves);
		entidad.setNombre(datos.getNombre());
		entidad.setAfectaSaldo(datos.getAfectaSaldo());
		entidad.setDocumentador(datos.getDocumentador());
		entidad.setRapida(datos.getRapida());
		entidad.setImagen(datos.getImagen());
		if (datos.getProceso() != null)
			entidad.setProceso(datos.getProceso());
		if (datos.getPlantilla() != null)
			entidad.setPlantilla(datos.getPlantilla());
		if (datos.getEstadoPartida() != null)
			entidad.setEstadoPartida(datos.getEstadoPartida());
		if (datos.getEstadoLLegada() != null)
			entidad.setEstadoLLegada(datos.getEstadoLLegada());
		updateTransicion(entidad);
	}

	private void actualizarPlantilla(TreeNodeDTO remoto, TreeNodeDTO local, Map<String, String> mapeoLlaves)
			throws ServerException {
		DocumentoPlantillaDTO entidad = mapper.convertValue(local.getDato(), DocumentoPlantillaDTO.class);
		DocumentoPlantillaDTO datos = datosRemotos(remoto, DocumentoPlantillaDTO.class, mapeoLlaves);
		entidad.setNombre(datos.getNombre());
		entidad.setImagen(datos.getImagen());
		entidad.setConsecutivo(datos.getConsecutivo());
		entidad.setTipo(datos.getTipo());
		if (datos.getProceso() != null)
			entidad.setProceso(datos.getProceso());
		if (datos.getPadre() != null)
			entidad.setPadre(datos.getPadre());
		updatePlantilla(entidad);
	}

	private void actualizarCampo(TreeNodeDTO remoto, TreeNodeDTO local, Map<String, String> mapeoLlaves)
			throws ServerException {
		DocumentoPlantillaCaracteristicaDTO entidad = mapper.convertValue(local.getDato(),
				DocumentoPlantillaCaracteristicaDTO.class);
		DocumentoPlantillaCaracteristicaDTO datos = datosRemotos(remoto, DocumentoPlantillaCaracteristicaDTO.class,
				mapeoLlaves);
		entidad.setNombre(datos.getNombre());
		entidad.setImagen(datos.getImagen());
		entidad.setFormato(datos.getFormato());
		entidad.setObjetivo(datos.getObjetivo());
		entidad.setOrden(datos.getOrden());
		if (datos.getPlantilla() != null)
			entidad.setPlantilla(datos.getPlantilla());
		updateCampo(entidad);
	}

	private void actualizarReporte(TreeNodeDTO remoto, TreeNodeDTO local, Map<String, String> mapeoLlaves)
			throws ServerException {
		ReporteBaseDTO entidad = mapper.convertValue(local.getDato(), ReporteBaseDTO.class);
		ReporteBaseDTO datos = datosRemotos(remoto, ReporteBaseDTO.class, mapeoLlaves);
		entidad.setNombre(datos.getNombre());
		entidad.setDescripcion(datos.getDescripcion());
		entidad.setVersion(datos.getVersion());
		entidad.setVariables(datos.getVariables());
		entidad.setServidor(datos.getServidor());
		entidad.setMultiplesId(datos.getMultiplesId());
		entidad.setServidorUrl(datos.getServidorUrl());
		entidad.setPublico(datos.getPublico());
		entidad.setSoloExistente(datos.getSoloExistente());
		if (datos.getPlantilla() != null)
			entidad.setPlantilla(datos.getPlantilla());
		updateReporte(entidad);
	}

	private void actualizarApi(TreeNodeDTO remoto, TreeNodeDTO local, Map<String, String> mapeoLlaves)
			throws ServerException {
		WebServiceDTO entidad = mapper.convertValue(local.getDato(), WebServiceDTO.class);
		WebServiceDTO datos = datosRemotos(remoto, WebServiceDTO.class, mapeoLlaves);
		entidad.setNombre(datos.getNombre());
		if (datos.getProceso() != null)
			entidad.setProceso(datos.getProceso());
		updateApi(entidad);
	}*/

	private Map<String, String> mapearLlaves(TreeNodeDTO remoto, TreeNodeDTO local) {
		Map<String, String> mapa = new LinkedHashMap<>();
		if (remoto == null || local == null)
			return mapa;
		if (remoto.getLlaveTabla() != null && local.getLlaveTabla() != null)
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
/*
	private void updateProceso(ProcesoDTO entidad) throws ServerException {
		procesoSvc.update(entidad);
	}

	private void updateEstado(ProcesoEstadoDTO entidad) throws ServerException {
		procesoEstadoSvc.update(entidad);
	}

	private void updateTransicion(ProcesoTransicionDTO entidad) throws ServerException {
		procesoTransicionSvc.update(entidad);
	}

	private void updatePlantilla(DocumentoPlantillaDTO entidad) throws ServerException {
		documentoPlantillaSvc.update(entidad);
	}

	private void updateCampo(DocumentoPlantillaCaracteristicaDTO entidad) throws ServerException {
		documentoPlantillaCaracteristicaSvc.update(entidad);
	}

	private void updateReporte(ReporteBaseDTO entidad) throws ServerException {
		reporteBaseSvc.update(entidad);
	}

	private void updateApi(WebServiceDTO entidad) throws ServerException {
		webServiceSvc.update(entidad);
	}*/


}
