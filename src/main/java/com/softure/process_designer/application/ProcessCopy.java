package com.softure.process_designer.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.SharedIdResponse;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.process_designer.domain.ProcesoDTO;
import com.softure.process_designer.domain.ProcesoEstadoDTO;
import com.softure.process_designer.domain.ProcesoEstadoFilterDTO;
import com.softure.process_designer.domain.ProcesoFilterDTO;
import com.softure.process_designer.domain.ProcesoTransicionDTO;
import com.softure.process_designer.domain.ProcesoTransicionFilterDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

@Component
public class ProcessCopy {

	@Autowired
	private ProcesoSvc processService;
	@Autowired
	private PropiedadSvc propiedadService;
	@Autowired 
	private ProcesoEstadoSvc estadoService;
	@Autowired 
	private ProcesoTransicionSvc transicionService;
	
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public SharedIdResponse call(String processId, String token) throws ServerException {

		ProcesoDTO process = processService.consultaXId(processId);
		if (process == null)
			throw new ServerException("El id del servicio no se encuentra en la BD." + processId);
		if (process.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO) != 0)
			throw new ServerException("El proceso " + process.getNombre() + " no se encuentra Activo." + processId);
		// Obtengo propiedades del servicio
		String userId = processService.getUserFlex(token);
		
		process = getFullProccessToCopy(process, userId);
		
		return new SharedIdResponse(guardarProceso(process, token).getLlaveTabla());
	}
	
	private ProcesoDTO getFullProccessToCopy(ProcesoDTO proceso, String userId) throws ServerException {
		proceso.setPropiedades(
				propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.PROCESO, proceso.getLlaveTabla(), null, userId));
		
		
        /*if(proceso.getLlaveTabla()!=null) {
        	DocumentoPlantillaFilterDTO filtro = new DocumentoPlantillaFilterDTO();
            filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);

            proceso.setPlantillas(plantillaService.listarConsulta(filtro)); 
            if(proceso.getPlantillas()!=null) {
            	for (DocumentoPlantillaDTO iPlantilla: proceso.getPlantillas()) {
        			escribirPlantilla(iPlantilla);
        		}	
            }
        }*/
        
        if(proceso.getTipo().compareTo(ProcesoDTO.EJECUTOR)==0) {
			ProcesoEstadoFilterDTO filtroEstadoDTO = new ProcesoEstadoFilterDTO();
			filtroEstadoDTO.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			filtroEstadoDTO.setProceso(proceso.getLlaveTabla());
			proceso.setEstados(estadoService.listarConsulta(filtroEstadoDTO));
			if(proceso.getEstados()!=null) {
	        	for (ProcesoEstadoDTO iEstado: proceso.getEstados()) {
	        		iEstado.setPropiedades(propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.ESTADO, iEstado.getLlaveTabla(), null, null));
	    		}	
	        }
			
			ProcesoTransicionFilterDTO filtroTransicionDTO = new ProcesoTransicionFilterDTO();
			filtroTransicionDTO.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			filtroTransicionDTO.setProceso(proceso.getLlaveTabla());
			proceso.setTransiciones(transicionService.listarConsulta(filtroTransicionDTO));
	        if(proceso.getTransiciones()!=null) {
	        	for (ProcesoTransicionDTO iTransicion: proceso.getTransiciones()) {
	        		iTransicion.setPropiedades(propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.TRANSICION, iTransicion.getLlaveTabla(), null, null));
	    		}	
	        }
			
		}else {
			ProcesoFilterDTO filtroHijos = new ProcesoFilterDTO();
			filtroHijos.setMacroproceso(proceso.getLlaveTabla());
			filtroHijos.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			List<ProcesoDTO> hijos = processService.listarConsulta(filtroHijos); 
			if(hijos!=null) {
				if(proceso.getLlaveTabla()==null) {
					proceso.setHijos(new ArrayList<ProcesoDTO>());
		        	for (ProcesoDTO iProceso : hijos) {
		        		if(iProceso.getMacroproceso()==null)proceso.getHijos().add(iProceso);
		        	}
				}else {
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
		
		if(proceso.getEstados()!=null) {
			for (ProcesoEstadoDTO iEstado : proceso.getEstados()) {
				iEstado.setLlaveTabla(null);
				iEstado.setProceso(newProcess.getLlaveTabla());
				iEstado.setLlaveTabla(estadoService.guardar(iEstado, token).getLlaveTabla());
			}
		}
		/*if(proceso.getPlantillas()!=null) {
			for (DocumentoPlantillaDTO iPlantilla : proceso.getPlantillas()) {
				iPlantilla.setLlaveTabla(null);
				//iPlantilla.setProceso(proceso.getLlaveTabla());
				iPlantilla.setConsecutivo(null);
				plantillaService.configurarInicioPlantilla(iPlantilla);
				iPlantilla.setLlaveTabla(plantillaService.save(iPlantilla).getLlaveTabla());
				//Esto ahora se gestiona con las propiedades				

				if(iPlantilla.getCaracteristicas()!=null) {
					for (DocumentoPlantillaCaracteristicaDTO iCampo : iPlantilla.getCaracteristicas()) {
						iCampo.setLlaveTabla(null);
						iCampo.setPlantilla(iPlantilla.getLlaveTabla());
						iCampo.setLlaveTabla(caracteristicaService.save(iCampo).getLlaveTabla());
					}
				}
				if(iPlantilla.getReportes()!=null) {
					for (ReporteBaseDTO iReporte : iPlantilla.getReportes()) {
						iReporte.setLlaveTabla(null);
						iReporte.setPlantilla(iPlantilla.getLlaveTabla());
						iReporte.setLlaveTabla(reporteService.guardar(iReporte, token).getLlaveTabla());
					}
				}
				appendInfo("Plantilla Creada " + iPlantilla.getNombre());
				plantillasSistema.add(iPlantilla);
			}
		}*/
		if(proceso.getHijos()!=null) {
			List<ProcesoDTO> newNodes = new ArrayList<>(); 
			for (ProcesoDTO iProceso : proceso.getHijos()) {
				iProceso.setMacroproceso(proceso.getLlaveTabla());
				newNodes.add(guardarProceso(iProceso, token));
			}
			proceso.setHijos(newNodes);
		}
		if(proceso.getTransiciones()!=null) {
        	for (ProcesoTransicionDTO iTransicion: proceso.getTransiciones()) {
        		iTransicion.setLlaveTabla(null);
				iTransicion.setProceso(newProcess.getLlaveTabla());
				if(iTransicion.getEstadoPartida()!=null) {
					for (ProcesoEstadoDTO iEstado : proceso.getEstados()) {
						if(iEstado.getNombre().compareTo(iTransicion.getEstadoPartidaNombre())==0) {
							iTransicion.setEstadoPartida(iEstado.getLlaveTabla());
							break;
						}
					}
				}
				if(iTransicion.getEstadoLLegada()!=null) {
					for (ProcesoEstadoDTO iEstado : proceso.getEstados()) {
						if(iEstado.getNombre().compareTo(iTransicion.getEstadoLlegadaNombre())==0) {
							iTransicion.setEstadoLLegada(iEstado.getLlaveTabla());
							break;
						}
					}
				}
				if(iTransicion.getEstadoPartida()!=null) iTransicion.setLlaveTabla(transicionService.guardar(iTransicion, token).getLlaveTabla());
    		}	
        }
		//Empiezo a gestionar propiedades al final para evitar errores de referencia	
		newProcess.setPropiedades(propiedadService.copiarPropiedades(proceso.getPropiedades(), newProcess.getLlaveTabla(), token));
		
		if(proceso.getEstados()!=null) {
			for (ProcesoEstadoDTO iEstado : proceso.getEstados()) {
				iEstado.setPropiedades(propiedadService.copiarPropiedades(iEstado.getPropiedades(), iEstado.getLlaveTabla(), token));
			}
		}
		if(proceso.getTransiciones()!=null) {
        	for (ProcesoTransicionDTO iTransicion: proceso.getTransiciones()) {
        		iTransicion.setPropiedades(propiedadService.copiarPropiedades(iTransicion.getPropiedades(), iTransicion.getLlaveTabla(), token));
    		}
        }
		return newProcess;
	}

}
