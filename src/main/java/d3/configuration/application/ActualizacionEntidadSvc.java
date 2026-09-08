package d3.configuration.application;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import d3.authorization.application.RolAccesoSvc;
import d3.authorization.domain.RolAccesoDTO;
import d3.configuration.domain.TreeNodeDTO;
import d3.mail.application.MensajePlantillaCorreoSvc;
import d3.mail.domain.MensajePlantillaCorreoDTO;
import d3.process.application.DocumentoPlantillaCaracteristicaSvc;
import d3.process.application.DocumentoPlantillaSvc;
import d3.process.application.ProcesoEstadoSvc;
import d3.process.application.ProcesoSvc;
import d3.process.application.ProcesoTransicionSvc;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.process.domain.DocumentoPlantillaDTO;
import d3.process.domain.ProcesoDTO;
import d3.process.domain.ProcesoEstadoDTO;
import d3.process.domain.ProcesoTransicionDTO;
import d3.report.application.ReporteBaseSvc;
import d3.report.domain.ReporteBaseDTO;
import d3.shared.domain.ServerException;
import d3.webservice.application.WebServiceSvc;
import d3.webservice.domain.WebServiceDTO;

@Service
public class ActualizacionEntidadSvc {

	private final ObjectMapper mapper;
	private final ProcesoSvc procesoSvc;
	private final ProcesoEstadoSvc procesoEstadoSvc;
	private final ProcesoTransicionSvc procesoTransicionSvc;
	private final DocumentoPlantillaSvc documentoPlantillaSvc;
	private final DocumentoPlantillaCaracteristicaSvc documentoPlantillaCaracteristicaSvc;
	private final ReporteBaseSvc reporteBaseSvc;
	private final RolAccesoSvc rolAccesoSvc;
	private final WebServiceSvc webServiceSvc;
	private final MensajePlantillaCorreoSvc mensajePlantillaCorreoSvc;

	public ActualizacionEntidadSvc(ObjectMapper mapper, ProcesoSvc procesoSvc, ProcesoEstadoSvc procesoEstadoSvc,
			ProcesoTransicionSvc procesoTransicionSvc, DocumentoPlantillaSvc documentoPlantillaSvc,
			DocumentoPlantillaCaracteristicaSvc documentoPlantillaCaracteristicaSvc, ReporteBaseSvc reporteBaseSvc,
			RolAccesoSvc rolAccesoSvc, WebServiceSvc webServiceSvc,
			MensajePlantillaCorreoSvc mensajePlantillaCorreoSvc) {
		this.mapper = mapper;
		this.procesoSvc = procesoSvc;
		this.procesoEstadoSvc = procesoEstadoSvc;
		this.procesoTransicionSvc = procesoTransicionSvc;
		this.documentoPlantillaSvc = documentoPlantillaSvc;
		this.documentoPlantillaCaracteristicaSvc = documentoPlantillaCaracteristicaSvc;
		this.reporteBaseSvc = reporteBaseSvc;
		this.rolAccesoSvc = rolAccesoSvc;
		this.webServiceSvc = webServiceSvc;
		this.mensajePlantillaCorreoSvc = mensajePlantillaCorreoSvc;
	}

	public void actualizarArbol(TreeNodeDTO remoto, TreeNodeDTO local) throws ServerException {
		if (remoto == null || local == null)
			return;
		Map<String, TreeNodeDTO> index = indexar(local);
		actualizarNodo(remoto, index.get(remoto.getCamino()));
		if (remoto.getHijos() != null) {
			for (TreeNodeDTO hijo : remoto.getHijos()) {
				actualizarArbol(hijo, local);
			}
		}
	}

	private void actualizarNodo(TreeNodeDTO remoto, TreeNodeDTO local) throws ServerException {
		if (local == null || remoto.getDato() == null || local.getDato() == null)
			return;
		switch (remoto.getTipo()) {
		case TreeNodeDTO.PROCESO_MACRO:
		case TreeNodeDTO.PROCESO:
			actualizarProceso(remoto, local);
			break;
		case TreeNodeDTO.ESTADO:
			actualizarEstado(remoto, local);
			break;
		case TreeNodeDTO.TRANSICION:
			actualizarTransicion(remoto, local);
			break;
		case TreeNodeDTO.PLANTILLA:
		case TreeNodeDTO.PLANTILLA_MODIFICACION:
		case TreeNodeDTO.PLANTILLA_ANULACION:
		case TreeNodeDTO.PLANTILLA_ACTIVACION:
			actualizarPlantilla(remoto, local);
			break;
		case TreeNodeDTO.CAMPO:
			actualizarCampo(remoto, local);
			break;
		case TreeNodeDTO.REPORTE:
			actualizarReporte(remoto, local);
			break;
		case TreeNodeDTO.ROL:
			actualizarRol(remoto, local);
			break;
		case TreeNodeDTO.API:
			actualizarApi(remoto, local);
			break;
		case TreeNodeDTO.MENSAJE:
			actualizarMensaje(remoto, local);
			break;
		default:
			break;
		}
	}

	private void actualizarProceso(TreeNodeDTO remoto, TreeNodeDTO local) throws ServerException {
		ProcesoDTO entidad = mapper.convertValue(local.getDato(), ProcesoDTO.class);
		ProcesoDTO datos = mapper.convertValue(remoto.getDato(), ProcesoDTO.class);
		entidad.setNombre(datos.getNombre());
		entidad.setImagen(datos.getImagen());
		entidad.setObjetivo(datos.getObjetivo());
		entidad.setPrioridad(datos.getPrioridad());
		updateProceso(entidad);
	}

	private void actualizarEstado(TreeNodeDTO remoto, TreeNodeDTO local) throws ServerException {
		ProcesoEstadoDTO entidad = mapper.convertValue(local.getDato(), ProcesoEstadoDTO.class);
		ProcesoEstadoDTO datos = mapper.convertValue(remoto.getDato(), ProcesoEstadoDTO.class);
		entidad.setNombre(datos.getNombre());
		entidad.setEstadoDocumento(datos.getEstadoDocumento());
		entidad.setAvance(datos.getAvance());
		updateEstado(entidad);
	}

	private void actualizarTransicion(TreeNodeDTO remoto, TreeNodeDTO local) throws ServerException {
		ProcesoTransicionDTO entidad = mapper.convertValue(local.getDato(), ProcesoTransicionDTO.class);
		ProcesoTransicionDTO datos = mapper.convertValue(remoto.getDato(), ProcesoTransicionDTO.class);
		entidad.setNombre(datos.getNombre());
		entidad.setAfectaSaldo(datos.getAfectaSaldo());
		entidad.setDocumentador(datos.getDocumentador());
		entidad.setRapida(datos.getRapida());
		entidad.setImagen(datos.getImagen());
		updateTransicion(entidad);
	}

	private void actualizarPlantilla(TreeNodeDTO remoto, TreeNodeDTO local) throws ServerException {
		DocumentoPlantillaDTO entidad = mapper.convertValue(local.getDato(), DocumentoPlantillaDTO.class);
		DocumentoPlantillaDTO datos = mapper.convertValue(remoto.getDato(), DocumentoPlantillaDTO.class);
		entidad.setNombre(datos.getNombre());
		entidad.setImagen(datos.getImagen());
		entidad.setConsecutivo(datos.getConsecutivo());
		updatePlantilla(entidad);
	}

	private void actualizarCampo(TreeNodeDTO remoto, TreeNodeDTO local) throws ServerException {
		DocumentoPlantillaCaracteristicaDTO entidad = mapper.convertValue(local.getDato(),
				DocumentoPlantillaCaracteristicaDTO.class);
		DocumentoPlantillaCaracteristicaDTO datos = mapper.convertValue(remoto.getDato(),
				DocumentoPlantillaCaracteristicaDTO.class);
		entidad.setNombre(datos.getNombre());
		entidad.setImagen(datos.getImagen());
		entidad.setFormato(datos.getFormato());
		entidad.setObjetivo(datos.getObjetivo());
		entidad.setOrden(datos.getOrden());
		updateCampo(entidad);
	}

	private void actualizarReporte(TreeNodeDTO remoto, TreeNodeDTO local) throws ServerException {
		ReporteBaseDTO entidad = mapper.convertValue(local.getDato(), ReporteBaseDTO.class);
		ReporteBaseDTO datos = mapper.convertValue(remoto.getDato(), ReporteBaseDTO.class);
		entidad.setNombre(datos.getNombre());
		entidad.setDescripcion(datos.getDescripcion());
		entidad.setVersion(datos.getVersion());
		entidad.setVariables(datos.getVariables());
		entidad.setServidor(datos.getServidor());
		entidad.setMultiplesId(datos.getMultiplesId());
		entidad.setServidorUrl(datos.getServidorUrl());
		entidad.setPublico(datos.getPublico());
		entidad.setSoloExistente(datos.getSoloExistente());
		updateReporte(entidad);
	}

	private void actualizarRol(TreeNodeDTO remoto, TreeNodeDTO local) throws ServerException {
		RolAccesoDTO entidad = mapper.convertValue(local.getDato(), RolAccesoDTO.class);
		RolAccesoDTO datos = mapper.convertValue(remoto.getDato(), RolAccesoDTO.class);
		entidad.setNombre(datos.getNombre());
		entidad.setImagen(datos.getImagen());
		updateRol(entidad);
	}

	private void actualizarApi(TreeNodeDTO remoto, TreeNodeDTO local) throws ServerException {
		WebServiceDTO entidad = mapper.convertValue(local.getDato(), WebServiceDTO.class);
		WebServiceDTO datos = mapper.convertValue(remoto.getDato(), WebServiceDTO.class);
		entidad.setNombre(datos.getNombre());
		updateApi(entidad);
	}

	private void actualizarMensaje(TreeNodeDTO remoto, TreeNodeDTO local) throws ServerException {
		MensajePlantillaCorreoDTO entidad = mapper.convertValue(local.getDato(), MensajePlantillaCorreoDTO.class);
		MensajePlantillaCorreoDTO datos = mapper.convertValue(remoto.getDato(), MensajePlantillaCorreoDTO.class);
		entidad.setNombre(datos.getNombre());
		entidad.setTitulo(datos.getTitulo());
		entidad.setTexto(datos.getTexto());
		entidad.setServidor(datos.getServidor());
		updateMensaje(entidad);
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

	private void updateRol(RolAccesoDTO entidad) throws ServerException {
		rolAccesoSvc.update(entidad);
	}

	private void updateApi(WebServiceDTO entidad) throws ServerException {
		webServiceSvc.update(entidad);
	}

	private void updateMensaje(MensajePlantillaCorreoDTO entidad) throws ServerException {
		mensajePlantillaCorreoSvc.update(entidad);
	}

}
