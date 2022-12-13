package com.softure.document_execution.application.field;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softure.authorization.application.RolAccesoSvc;
import com.softure.authorization.domain.RolAccesoDTO;
import com.softure.authorization.domain.RolAccesoFilterDTO;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.process_designer.application.ProcesoEstadoSvc;
import com.softure.process_designer.application.ProcesoSvc;
import com.softure.process_designer.application.ProcesoTransicionSvc;
import com.softure.process_designer.domain.ProcesoDTO;
import com.softure.process_designer.domain.ProcesoEstadoDTO;
import com.softure.process_designer.domain.ProcesoEstadoFilterDTO;
import com.softure.process_designer.domain.ProcesoFilterDTO;
import com.softure.process_designer.domain.ProcesoTransicionDTO;
import com.softure.process_designer.domain.ProcesoTransicionFilterDTO;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.process_form.domain.DocumentoPlantillaFilterDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.report.application.ReporteBaseSvc;
import com.softure.report.domain.ReporteBaseDTO;

@Component
public class ProcesoHelperJson {
	
	@Autowired private DocumentoPlantillaSvc plantillaService;
	@Autowired private DocumentoPlantillaCaracteristicaSvc caracteristicaService;
	@Autowired private ProcesoSvc procesoService;
	@Autowired private ProcesoEstadoSvc estadoService;
	@Autowired private ProcesoTransicionSvc transicionService;
	@Autowired private PropiedadSvc propiedadService;
	@Autowired private ReporteBaseSvc reporteService;
	@Autowired private RolAccesoSvc rolService;
	
	private StringBuilder infoProceso;
	private List<RolAccesoDTO> roles;
	private List<DocumentoPlantillaDTO> plantillasSistema;
	
	
	public String generarXML(ProcesoFilterDTO proceso) throws ServerException{
		ProcesoDTO filtro = new ProcesoDTO();
		if(proceso.getLlaveTabla().compareTo("NODO1476")==0) {
			filtro.setTipo(ProcesoDTO.AGRUPADOR);
			filtro.setNombre("MAPA DE PROCESOS");
			filtro.setCodigo("MAPA");
			filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		}else {
			filtro.setLlaveTabla(proceso.getLlaveTabla());
			filtro = procesoService.consultaXId(proceso.getLlaveTabla());
		}		
		StringBuilder root = new StringBuilder();
		ObjectMapper mapper = new ObjectMapper();
		try {
			filtro = escribirProceso(filtro);
			root.append( mapper.writeValueAsString(filtro) );
		} catch (JsonProcessingException e) {
			new ServerException(e.getMessage());
		}
		
		return root.toString();
	}


	private ProcesoDTO escribirProceso(ProcesoDTO proceso) throws ServerException {
		proceso.setPropiedades(propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.PROCESO, proceso.getLlaveTabla(), null, null));
        if(proceso.getLlaveTabla()!=null) {
        	DocumentoPlantillaFilterDTO filtro = new DocumentoPlantillaFilterDTO();
            filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);

            proceso.setPlantillas(plantillaService.listarConsulta(filtro)); 
            if(proceso.getPlantillas()!=null) {
            	for (DocumentoPlantillaDTO iPlantilla: proceso.getPlantillas()) {
        			escribirPlantilla(iPlantilla);
        		}	
            }
        }
        
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
			List<ProcesoDTO> hijos = procesoService.listarConsulta(filtroHijos); 
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
					escribirProceso(procesoDTO);
				}
			}	
		}
        return proceso;
    }
	
	private void escribirPlantilla(DocumentoPlantillaDTO plantilla) throws ServerException {
        plantilla.setPropiedades(propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.PLANTILLA, plantilla.getLlaveTabla(), null, null));
        plantilla.setCaracteristicas(caracteristicaService.listarCamposPlantillaConComplementos(plantilla.getLlaveTabla(), null));
        if(plantilla.getCaracteristicas()!=null) {
        	for (DocumentoPlantillaCaracteristicaDTO iCampo : plantilla.getCaracteristicas()) {
        		iCampo.setPropiedades(propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.CAMPO, iCampo.getLlaveTabla(), null, null));
        	}
        }
        plantilla.setReportes(reporteService.listarDisponiblesDocumento(plantilla.getLlaveTabla()));
    }
	
	public String convertXML(String xmlString, String token) throws ServerException {
		ObjectMapper mapper = new ObjectMapper();
		ProcesoDTO obj = null;
		try {
			obj = mapper.readValue(xmlString, ProcesoDTO.class);
		} catch (Exception e) {
			throw new ServerException(e.getMessage());
		}
		//obj.setSecurityToken(token);
		infoProceso = new StringBuilder();
		roles = rolService.listarConsulta(new RolAccesoFilterDTO());
		DocumentoPlantillaFilterDTO filtro = new DocumentoPlantillaFilterDTO();
		filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		plantillasSistema = plantillaService.listarConsulta(filtro);
		guardarProceso(obj, token);
		return infoProceso.toString();
    }
	
	public void guardarProceso(ProcesoDTO proceso, String token) throws ServerException {
		if(proceso.getLlaveTabla()!=null) {
			proceso.setLlaveTabla(null);
			procesoService.preConfigurar(proceso);
			proceso.setLlaveTabla(procesoService.save(proceso).getLlaveTabla());
			appendInfo("Proceso creado: " +proceso.getNombre());
		}
		if(proceso.getEstados()!=null) {
			for (ProcesoEstadoDTO iEstado : proceso.getEstados()) {
				iEstado.setLlaveTabla(null);
				iEstado.setProceso(proceso.getLlaveTabla());
				iEstado.setLlaveTabla(estadoService.guardar(iEstado, token).getLlaveTabla());
			}
		}
		if(proceso.getPlantillas()!=null) {
			for (DocumentoPlantillaDTO iPlantilla : proceso.getPlantillas()) {
				iPlantilla.setLlaveTabla(null);
				//iPlantilla.setProceso(proceso.getLlaveTabla());
				iPlantilla.setConsecutivo(null);
				plantillaService.configurarInicioPlantilla(iPlantilla);
				iPlantilla.setLlaveTabla(plantillaService.save(iPlantilla).getLlaveTabla());
				//Esto ahora se gestiona con las propiedades				
				/*if(iPlantilla.getTipo().compareTo(DocumentoPlantillaDTO.ROL)==0) {
					RolAccesoDTO nuevo = new RolAccesoDTO();
					nuevo.setSecurityToken(iPlantilla.getSecurityToken());
					nuevo.setPlantilla(iPlantilla.getLlaveTabla());
					nuevo = rolService.guardar(nuevo);
					roles.add(nuevo);
				}*/
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
		}
		if(proceso.getHijos()!=null) {
			for (ProcesoDTO iProceso : proceso.getHijos()) {
				iProceso.setMacroproceso(proceso.getLlaveTabla());
				guardarProceso(iProceso, token);
			}
		}
		if(proceso.getTransiciones()!=null) {
        	for (ProcesoTransicionDTO iTransicion: proceso.getTransiciones()) {
				if(iTransicion.getPlantilla()!=null) {
					iTransicion.setPlantilla(null);
					for (DocumentoPlantillaDTO iPlantilla : plantillasSistema) {
						if(iPlantilla.getNombre().compareTo(iTransicion.getPlantillaNombre())==0) {
							iTransicion.setPlantilla(iPlantilla.getLlaveTabla());
							break;
						}
					}
				}
				if(iTransicion.getPlantilla()==null) {
					appendNotice("La transicion ha sido omitida debido a que no se encuentra la plantilla. " + iTransicion.getNombre());
				}else {
					iTransicion.setLlaveTabla(null);
					iTransicion.setProceso(proceso.getLlaveTabla());
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
					iTransicion.setLlaveTabla(transicionService.guardar(iTransicion, token).getLlaveTabla());
				}
    		}	
        }
		//Empiezo a gestionar propiedades al final para evitar errores de referencia		
		gestionarPropiedades("Proceso " + proceso.getNombre(), proceso.getPropiedades(), proceso.getLlaveTabla(), token);
		if(proceso.getEstados()!=null) {
			for (ProcesoEstadoDTO iEstado : proceso.getEstados()) {
				gestionarPropiedades("Estado " + iEstado.getNombre(),iEstado.getPropiedades(), iEstado.getLlaveTabla(), token);
			}
		}
		if(proceso.getPlantillas()!=null) {
			for (DocumentoPlantillaDTO iPlantilla : proceso.getPlantillas()) {
				if(iPlantilla.getCaracteristicas()!=null) {
					for (DocumentoPlantillaCaracteristicaDTO iCampo : iPlantilla.getCaracteristicas()) {
						gestionarPropiedades("Campo " + iCampo.getNombre(), iCampo.getPropiedades(), iCampo.getLlaveTabla(), token);
					}
				}
				if(iPlantilla.getReportes()!=null) {
					for (ReporteBaseDTO iReporte : iPlantilla.getReportes()) {
						gestionarPropiedades("Reporte " + iReporte.getNombre(), iReporte.getPropiedades(), iReporte.getLlaveTabla(), token);
					}
				}
				gestionarPropiedades("Plantilla " + iPlantilla.getNombre(),iPlantilla.getPropiedades(), iPlantilla.getLlaveTabla(), token);
			}
		}
		if(proceso.getTransiciones()!=null) {
        	for (ProcesoTransicionDTO iTransicion: proceso.getTransiciones()) {
        		gestionarPropiedades("Transicion " + iTransicion.getNombre(),iTransicion.getPropiedades(), iTransicion.getLlaveTabla(), token);
    		}	
        }
	}

	private void gestionarPropiedades(String padre, List<PropiedadDTO> propiedades, String campo, String token) throws ServerException {
		if(propiedades==null) return; //Existe algo parecido para copiar plantillas
		for (PropiedadDTO iPropiedad : propiedades) {
			if(iPropiedad.getUsuario()==null) {
				if(iPropiedad.getRolNombre()!=null) {
					iPropiedad.setRol(null);
					for (RolAccesoDTO iRol: roles) {
						if(iRol.getNombre().compareTo(iPropiedad.getRolNombre())==0) {
							iPropiedad.setRol(iRol.getLlaveTabla());
							break;
						}
					}
				}
				if(iPropiedad.getRol()==null && iPropiedad.getRolNombre()!=null) {
					appendNotice("(" + padre + ") Propiedad Omitida porque no se identifica el rol " + iPropiedad.getRolNombre());
				}else {
					iPropiedad.setLlaveTabla(null);
					iPropiedad.setCampo(campo);
					
					String oldValue = iPropiedad.getValor();
					iPropiedad.setValor(iPropiedad.getTexto());//Sucede que los campos de una plantilla los copiaba mal referenciados
					if(iPropiedad.getValor()==null) iPropiedad.setValor(oldValue);
					iPropiedad.setLlaveTabla(propiedadService.guardar(iPropiedad, token).getLlaveTabla());
				}
			}else {
				appendNotice("(" + padre + ") Propiedad Omitida porque tenia usuario " + iPropiedad.getNombre());
			}
		}
	}
	
	private void appendInfo(String value) {
		infoProceso.append("OK : " + value + "\n");
	}
	
	private void appendNotice(String value) {
		infoProceso.append("NOTICE  : " + value+ "\n");
	}
	
}
