package d3.process.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.domain.SharedConstants;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedIdResponse;
import d3.configuration.application.PropertyGetWithCacheService;
import d3.configuration.application.PropiedadSvc;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.process.domain.ProcesoDTO;
import d3.process.domain.ProcesoEstadoDTO;
import d3.process.domain.ProcesoEstadoFilterDTO;
import d3.process.domain.ProcesoFilterDTO;
import d3.process.domain.ProcesoTransicionDTO;
import d3.process.domain.ProcesoTransicionFilterDTO;

import org.springframework.context.annotation.Lazy;

@Component
public class ProcessCopy {

	private final ProcesoSvc processService;
	private final PropiedadSvc propiedadService;
	private final PropertyGetWithCacheService cacheService;
	private final ProcesoEstadoSvc estadoService;
	private final ProcesoTransicionSvc transicionService;

	public ProcessCopy(@Lazy ProcesoSvc processService, @Lazy PropiedadSvc propiedadService,
			@Lazy PropertyGetWithCacheService cacheService, @Lazy ProcesoEstadoSvc estadoService,
			@Lazy ProcesoTransicionSvc transicionService) {
		this.processService = processService;
		this.propiedadService = propiedadService;
		this.cacheService = cacheService;
		this.estadoService = estadoService;
		this.transicionService = transicionService;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SharedIdResponse call(String processId, String token) throws ServerException {

		ProcesoDTO process = processService.consultaXId(processId);
		if (process == null)
			throw new ServerException("El id del servicio no se encuentra en la BD." + processId);
		if (process.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
			throw new ServerException("El proceso " + process.getNombre() + " no se encuentra Activo." + processId);
		// Obtengo propiedades del servicio
		String userId = processService.getUserFlex(token);

		process = getFullProccessToCopy(process, userId);

		return new SharedIdResponse(guardarProceso(process, token).getLlaveTabla());
	}

	private ProcesoDTO getFullProccessToCopy(ProcesoDTO proceso, String userId) throws ServerException {
		proceso.setPropiedades(cacheService.obtenerPropiedades(PropiedadValorDefinidoDTO.PROCESO,
				proceso.getLlaveTabla(), null, userId));

		/*
		 * if(proceso.getLlaveTabla()!=null) { DocumentoPlantillaFilterDTO filtro = new
		 * DocumentoPlantillaFilterDTO();
		 * filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		 * 
		 * proceso.setPlantillas(plantillaService.listarConsulta(filtro));
		 * if(proceso.getPlantillas()!=null) { for (DocumentoPlantillaDTO iPlantilla:
		 * proceso.getPlantillas()) { escribirPlantilla(iPlantilla); } } }
		 */

		if (proceso.getTipo().compareTo(ProcesoDTO.EJECUTOR) == 0) {
			ProcesoEstadoFilterDTO filtroEstadoDTO = new ProcesoEstadoFilterDTO();
			filtroEstadoDTO.setEstado(SharedConstants.STATE_ACTIVE);
			filtroEstadoDTO.setProceso(proceso.getLlaveTabla());
			proceso.setEstados(estadoService.listarConsulta(filtroEstadoDTO));
			if (proceso.getEstados() != null) {
				for (ProcesoEstadoDTO iEstado : proceso.getEstados()) {
					iEstado.setPropiedades(cacheService.obtenerPropiedades(PropiedadValorDefinidoDTO.ESTADO,
							iEstado.getLlaveTabla(), null, null));
				}
			}

			ProcesoTransicionFilterDTO filtroTransicionDTO = new ProcesoTransicionFilterDTO();
			filtroTransicionDTO.setEstado(SharedConstants.STATE_ACTIVE);
			filtroTransicionDTO.setProceso(proceso.getLlaveTabla());
			proceso.setTransiciones(transicionService.listarConsulta(filtroTransicionDTO));
			if (proceso.getTransiciones() != null) {
				for (ProcesoTransicionDTO iTransicion : proceso.getTransiciones()) {
					iTransicion.setPropiedades(cacheService.obtenerPropiedades(PropiedadValorDefinidoDTO.TRANSICION,
							iTransicion.getLlaveTabla(), null, null));
				}
			}

		} else {
			ProcesoFilterDTO filtroHijos = new ProcesoFilterDTO();
			filtroHijos.setMacroproceso(proceso.getLlaveTabla());
			filtroHijos.setEstado(SharedConstants.STATE_ACTIVE);
			List<ProcesoDTO> hijos = processService.listarConsulta(filtroHijos);
			if (hijos != null) {
				if (proceso.getLlaveTabla() == null) {
					proceso.setHijos(new ArrayList<ProcesoDTO>());
					for (ProcesoDTO iProceso : hijos) {
						if (iProceso.getMacroproceso() == null)
							proceso.getHijos().add(iProceso);
					}
				} else {
					proceso.setHijos(hijos);
				}
				for (ProcesoDTO procesoDTO : proceso.getHijos()) {
					getFullProccessToCopy(procesoDTO, userId);
				}
			}
		}
		return proceso;
	}

	private ProcesoDTO guardarProceso(ProcesoDTO proceso, String token) throws ServerException {

		ProcesoDTO newProcess = new ProcesoDTO();
		newProcess.setCodigo(proceso.getCodigo() + "COPY");
		newProcess.setNombre(proceso.getNombre() + "COPY");
		newProcess.setImagen(proceso.getImagen());
		newProcess.setObjetivo(proceso.getObjetivo());
		newProcess.setMacroproceso(proceso.getMacroproceso());
		newProcess.setTipo(proceso.getTipo());
		newProcess = processService.save(newProcess);

		if (proceso.getEstados() != null) {
			for (ProcesoEstadoDTO iEstado : proceso.getEstados()) {
				iEstado.setLlaveTabla(null);
				iEstado.setProceso(newProcess.getLlaveTabla());
				iEstado.setLlaveTabla(estadoService.guardar(iEstado, token).getLlaveTabla());
			}
		}
		/*
		 * if(proceso.getPlantillas()!=null) { for (DocumentoPlantillaDTO iPlantilla :
		 * proceso.getPlantillas()) { iPlantilla.setLlaveTabla(null);
		 * //iPlantilla.setProceso(proceso.getLlaveTabla());
		 * iPlantilla.setConsecutivo(null);
		 * plantillaService.configurarInicioPlantilla(iPlantilla);
		 * iPlantilla.setLlaveTabla(plantillaService.save(iPlantilla).getLlaveTabla());
		 * //Esto ahora se gestiona con las propiedades
		 * 
		 * if(iPlantilla.getCaracteristicas()!=null) { for
		 * (DocumentoPlantillaCaracteristicaDTO iCampo :
		 * iPlantilla.getCaracteristicas()) { iCampo.setLlaveTabla(null);
		 * iCampo.setPlantilla(iPlantilla.getLlaveTabla());
		 * iCampo.setLlaveTabla(caracteristicaService.save(iCampo).getLlaveTabla()); } }
		 * if(iPlantilla.getReportes()!=null) { for (ReporteBaseDTO iReporte :
		 * iPlantilla.getReportes()) { iReporte.setLlaveTabla(null);
		 * iReporte.setPlantilla(iPlantilla.getLlaveTabla());
		 * iReporte.setLlaveTabla(reporteService.guardar(iReporte,
		 * token).getLlaveTabla()); } } appendInfo("Plantilla Creada " +
		 * iPlantilla.getNombre()); plantillasSistema.add(iPlantilla); } }
		 */
		if (proceso.getHijos() != null) {
			List<ProcesoDTO> newNodes = new ArrayList<>();
			for (ProcesoDTO iProceso : proceso.getHijos()) {
				iProceso.setMacroproceso(proceso.getLlaveTabla());
				newNodes.add(guardarProceso(iProceso, token));
			}
			proceso.setHijos(newNodes);
		}
		if (proceso.getTransiciones() != null) {
			for (ProcesoTransicionDTO iTransicion : proceso.getTransiciones()) {
				iTransicion.setLlaveTabla(null);
				iTransicion.setProceso(newProcess.getLlaveTabla());
				if (iTransicion.getEstadoPartida() != null) {
					for (ProcesoEstadoDTO iEstado : proceso.getEstados()) {
						if (iEstado.getNombre().compareTo(iTransicion.getEstadoPartidaNombre()) == 0) {
							iTransicion.setEstadoPartida(iEstado.getLlaveTabla());
							break;
						}
					}
				}
				if (iTransicion.getEstadoLLegada() != null) {
					for (ProcesoEstadoDTO iEstado : proceso.getEstados()) {
						if (iEstado.getNombre().compareTo(iTransicion.getEstadoLlegadaNombre()) == 0) {
							iTransicion.setEstadoLLegada(iEstado.getLlaveTabla());
							break;
						}
					}
				}
				if (iTransicion.getEstadoPartida() != null)
					iTransicion.setLlaveTabla(transicionService.guardar(iTransicion, token).getLlaveTabla());
			}
		}
		// Empiezo a gestionar propiedades al final para evitar errores de referencia
		newProcess.setPropiedades(
				propiedadService.copiarPropiedades(proceso.getPropiedades(), newProcess.getLlaveTabla(), token));

		if (proceso.getEstados() != null) {
			for (ProcesoEstadoDTO iEstado : proceso.getEstados()) {
				iEstado.setPropiedades(
						propiedadService.copiarPropiedades(iEstado.getPropiedades(), iEstado.getLlaveTabla(), token));
			}
		}
		if (proceso.getTransiciones() != null) {
			for (ProcesoTransicionDTO iTransicion : proceso.getTransiciones()) {
				iTransicion.setPropiedades(propiedadService.copiarPropiedades(iTransicion.getPropiedades(),
						iTransicion.getLlaveTabla(), token));
			}
		}
		return newProcess;
	}

}
