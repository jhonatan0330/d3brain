package com.softure.process_designer.application;

import java.util.List;

// BEGIN region interImport
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.Calendar;

import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.mail.application.MailSendMessageToAdminService;
import com.softure.process_designer.domain.ProcesoTransicionAutomaticaDTO;
import com.softure.process_designer.domain.ProcesoTransicionAutomaticaFilterDTO;
import com.softure.process_designer.domain.ProcesoTransicionDTO;
import com.softure.process_designer.infrastructure.ProcesoTransicionAutomaticaMapper;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.application.RelacionInternaSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.property.domain.RelacionInternaDTO;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.softure.authentication.application.UsuarioAutenticacionSvc;
import com.softure.authentication.domain.UsuarioSesionDTO;
import com.softure.document_execution.application.CallDocumentListWithFilters;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaFilterDTO;
import com.softure.document_transition.application.CallDocumentNewFromAutomatic;

@Service("procesoTransicionAutomaticaService")
public class ProcesoTransicionAutomaticaSvc extends BasicSvc<ProcesoTransicionAutomaticaDTO, ProcesoTransicionAutomaticaFilterDTO> {
	
	@Autowired
	private ProcesoTransicionAutomaticaMapper procesoTransicionAutomaticaMapper;
	
	@Autowired private MailSendMessageToAdminService sendMessageToAdminSvc;
	@Autowired private PropiedadSvc propiedadService;
	@Autowired private CallDocumentNewFromAutomatic createDocumentSinceProperties;
	@Autowired private UsuarioAutenticacionSvc autenticacionService;
	@Autowired private RelacionInternaSvc relacionService;
	@Autowired private CallDocumentListWithFilters listDocumentWithFiltersFunction;

	@Override
	public ProcesoTransicionAutomaticaDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. ProcesoTransicionAutomatica");
		ProcesoTransicionAutomaticaFilterDTO dto = new ProcesoTransicionAutomaticaFilterDTO();
		dto.setLlaveTabla(llave);
		return procesoTransicionAutomaticaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = procesoTransicionAutomaticaMapper;
	}
	
	@Override
	public ProcesoTransicionAutomaticaDTO activar(ProcesoTransicionAutomaticaDTO dto, String token) throws ServerException {
		return super.activar(dto, token);
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProcesoTransicionAutomaticaDTO actualizar( ProcesoTransicionAutomaticaDTO dto, String token) throws ServerException {
		// BEGIN ProcesoTransicionAutomatica_actualizar
		return super.actualizar(dto, token);
		// END ProcesoTransicionAutomatica_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProcesoTransicionAutomaticaDTO inactivar(ProcesoTransicionAutomaticaDTO dto, String token) throws ServerException {
		// BEGIN ProcesoTransicionAutomatica_inactivar
		return super.inactivar(dto, token);
		// END ProcesoTransicionAutomatica_inactivar
	}
	
	@Override
	public ProcesoTransicionAutomaticaDTO consultaUnica(ProcesoTransicionAutomaticaFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(ProcesoTransicionAutomaticaFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<ProcesoTransicionAutomaticaDTO> listarConsulta(ProcesoTransicionAutomaticaFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	//@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProcesoTransicionAutomaticaDTO ejecutar(ProcesoTransicionAutomaticaDTO dto, String token)throws ServerException{
		// BEGIN region ejecutar
		ProcesoTransicionAutomaticaDTO bd = consultaXId(dto.getLlaveTabla());
		if(bd==null) throw new ServerException("Esta tarea autoamtica ya no se encuentra activo o valida en la bd");
		return gestionaEjecucion(bd);
		// END region ejecutar
	}
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProcesoTransicionAutomaticaDTO programar(ProcesoTransicionAutomaticaDTO dto, String token)throws ServerException{
		// BEGIN region programar
		programateAll();
		return null;
		// END region programar
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProcesoTransicionAutomaticaDTO guardar(ProcesoTransicionAutomaticaDTO dto, String token) throws ServerException {
		// BEGIN ProcesoTransicionAutomatica_guardar
		return super.guardar(dto, token);
		// END ProcesoTransicionAutomatica_guardar
	}

// BEGIN region aditionalMethods
	public void lanzarTransaccionesTemporizadas() {
		List<ProcesoTransicionAutomaticaDTO> transiciones = procesoTransicionAutomaticaMapper.consultarPendientes();
		if(transiciones ==null || transiciones.isEmpty()) return;
		List<ProcesoTransicionAutomaticaDTO> transaccionesEjecutadas = null;
		for (ProcesoTransicionAutomaticaDTO procesoTransicionAutomaticaDTO : transiciones) {
			ProcesoTransicionAutomaticaDTO previousValidate = containsTransicion(transaccionesEjecutadas, procesoTransicionAutomaticaDTO);
			procesoTransicionAutomaticaDTO.setEjecucion(new Date());
			if(previousValidate !=null) {
				procesoTransicionAutomaticaDTO.setMensaje("Ejecucion Anterior: " + previousValidate.getMensaje() + "\nFecha : " + SoftureUtil.formatDateTime(previousValidate.getEjecucion()) + "\nId: " + previousValidate.getLlaveTabla());
				try {
					procesoTransicionAutomaticaDTO = update(procesoTransicionAutomaticaDTO);	
				} catch (Exception e) {
				}
			}else {
				try {
					gestionaEjecucion(procesoTransicionAutomaticaDTO);
				} catch (ServerException e) {
					procesoTransicionAutomaticaDTO.setMensaje("ERROR : " + e.getMessage());
					try {
						try {
							sendMessageToAdminSvc.call("Error en ejecucion de transaccion " + procesoTransicionAutomaticaDTO.getPlantillaNombre(), e.getMessage()+ "\n\n(" +procesoTransicionAutomaticaDTO.getLlaveTabla() + ")");
						} catch (ServerException e1) {
						}
						procesoTransicionAutomaticaDTO = update(procesoTransicionAutomaticaDTO);
					} catch (ServerException e1) {
					}
				}
			}
			if(previousValidate == null) {
				if(transaccionesEjecutadas == null) transaccionesEjecutadas = new ArrayList<ProcesoTransicionAutomaticaDTO>();
				transaccionesEjecutadas.add(procesoTransicionAutomaticaDTO);
			}
		}
	}
	
	// Con el objetivo que no se duplique la ejecucion de una transicion se mira que ese tipo no se ejecutara antes
	// Como coloque unas sin transicion eas si no se validan
	private ProcesoTransicionAutomaticaDTO containsTransicion(List<ProcesoTransicionAutomaticaDTO> review, ProcesoTransicionAutomaticaDTO view) {
		if(review == null || review.isEmpty()) return null;
		for (ProcesoTransicionAutomaticaDTO procesoTransicionAutomaticaDTO : review) {
			if(view.getTransicion()!=null) {
				if(procesoTransicionAutomaticaDTO.getTransicion()!= null && 
						procesoTransicionAutomaticaDTO.getTransicion().compareTo(view.getTransicion())==0)
					return procesoTransicionAutomaticaDTO;
			} else {
				if(procesoTransicionAutomaticaDTO.getPropiedad()!= null && 
						procesoTransicionAutomaticaDTO.getPropiedad().compareTo(view.getPropiedad())==0)
					return procesoTransicionAutomaticaDTO;
			}
		}
		return null;
	}
	
	public void programateAll() throws ServerException {
		List<PropiedadDTO> faltantes = propiedadService.consultarTemporizadoresPendientes();
		if(faltantes==null || faltantes.isEmpty()) return;
		for (PropiedadDTO propiedadDTO : faltantes) {
			Date fechaProgramada = null;
			String error = null;
			if(propiedadDTO.getFechaInicial()==null) {
				error = "*****ERROR***** No tiene fecha inicial Fecha , revisa temporizador : (" + propiedadService.ubicarPropiedad(propiedadDTO) + " ) ";
			}else {
				if(propiedadDTO.getFechaInicial().compareTo(new Date())>0) {
					fechaProgramada = propiedadDTO.getFechaInicial();
				}else {
					Date ultimaEjecucion = procesoTransicionAutomaticaMapper.obtenerFechaUltimaEjecucion(propiedadDTO.getCampo());
					try {
						if(ultimaEjecucion==null) {
							fechaProgramada = calcularFecha(propiedadDTO.getFechaInicial(), propiedadDTO.getTexto());
						}else {
							fechaProgramada = calcularFecha(ultimaEjecucion, propiedadDTO.getTexto());
						}
					} catch (ServerException e) {
						error = "*****ERROR***** Calculando Fecha , revisa temporizador : (" + propiedadService.ubicarPropiedad(propiedadDTO) + " ) " + e.getMessage();
					} 
				}
			}
			
			ProcesoTransicionAutomaticaDTO programar = new ProcesoTransicionAutomaticaDTO();
			if(propiedadDTO.getKey().compareTo(Propiedades.TEMPORIZADOR)==0 && propiedadDTO.getTipo().compareTo(PropiedadValorDefinidoDTO.TRANSICION)==0) {
				programar.setTransicion(propiedadDTO.getCampo());	
			}
			programar.setFecha(fechaProgramada);
			if(error == null) {
				if(propiedadDTO.getMotivo()==null) {
					programar.setMensaje(propiedadDTO.getNombre());
				}else {
					programar.setMensaje(propiedadDTO.getMotivo());
				}
			} else {
				programar.setFecha(new Date());
				programar.setEjecucion(new Date());
				try {
					sendMessageToAdminSvc.call("TEMPORIZADOR ERROR", "Se ha presenstado error en el temporizador " + error + ". ( "+ propiedadService.ubicarPropiedad(propiedadDTO) + " )");					
				} catch (Exception e) {
					error = "*****ERROR SERVIDOR DE CORREO PARA MENSAJE***** revisa temporizador : (" + propiedadService.ubicarPropiedad(propiedadDTO) + "  -  " +  propiedadDTO.getTexto() + " ) " + e.getMessage() + " ERROR =" + error;
				}
				programar.setMensaje(error);
			}
			programar.setPropiedad(propiedadDTO.getLlaveTabla());
			save(programar);
		}
	}

	private Date calcularFecha(Date ultimaEjecucion, String valor) throws ServerException {
		if (valor==null) throw new ServerException("La propiedad no tiene el valor de temporizador");
		String[]temporizador = valor.split(":");
		Calendar fechaCalculada = new GregorianCalendar();
		fechaCalculada.setTime(ultimaEjecucion);
		try {
			int years = Integer.parseInt( temporizador[0]);
			int month = Integer.parseInt( temporizador[1]);
			int days = Integer.parseInt( temporizador[2]);
			int hours = Integer.parseInt( temporizador[3]);
			int minutes = Integer.parseInt( temporizador[4]);
			while (fechaCalculada.getTime().compareTo(new Date())<0) {
				if(years!=0)fechaCalculada.add(Calendar.YEAR, years);
				if(month!=0)fechaCalculada.add(Calendar.MONTH, month);
				if(days!=0)fechaCalculada.add(Calendar.DAY_OF_MONTH, days);
				if(hours!=0)fechaCalculada.add(Calendar.HOUR_OF_DAY, hours);
				if(minutes!=0)fechaCalculada.add(Calendar.MINUTE, minutes);			
			}
		} catch (Exception e) {
			throw new ServerException(e.getMessage());
		}
		return fechaCalculada.getTime();
	}
	
	public void inactivarPropiedad(String propiedad) throws ServerException {
		procesoTransicionAutomaticaMapper.inactivarPropiedad(propiedad);
	}
	
	public ProcesoTransicionAutomaticaDTO gestionaEjecucion(ProcesoTransicionAutomaticaDTO dto)throws ServerException{
		PropiedadDTO pTemporizador = propiedadService.consultaXId(dto.getPropiedad());
		if(Propiedades.validarBloqueo(pTemporizador)) {
			if(pTemporizador.getKey().compareTo(Propiedades.TEMPORIZADOR)==0) {
				List<PedidoVentaDTO> documentos = null;
				documentos = listDocumentWithFiltersFunction.listarExpedientesDisponiblesDocumentoFuncion(new PedidoVentaFilterDTO(), dto.getPropiedad(), null);
				if(documentos ==null || documentos.isEmpty()) {
					//Este mensaje va unido a el query de validacion, tener cuidado
					dto.setMensaje("Sin documentos a gestionar");
					if (dto.getTransicion()!=null && procesoTransicionAutomaticaMapper.countExecutionInLastMonth(dto.getTransicion(), dto.getPropiedad())==0) {
						try {
							sendMessageToAdminSvc.call("Proceso automatico que no se genera desde hace un mes " + dto.getPlantillaNombre(),
									"Proceso automatico que no se genera desde hace un mes" + "\n\n(" +dto.getLlaveTabla() + ")."
									+ " En caso de estar correcto te recomiendo que actualices la propiedad puede ser el motivo para que se vuelva a validar en un mes"
									+ "(Ubicacion: "+propiedadService.ubicarPropiedad(pTemporizador) +")");
						} catch (ServerException e1) {
						}
						dto.setMensaje("Sin documentos a gestionar y avisado al administrador por tiempo sin generar documentos");
					}
				}else {
					String campoDestino = procesoTransicionAutomaticaMapper.getFieldPlantilla(dto.getPropiedad());
					if(campoDestino==null) throw new ServerException("No se identifica el campo en donde se van a almacenar los documentos ( Ubicacion: "+ propiedadService.ubicarPropiedad(pTemporizador) + ")");
					UsuarioSesionDTO tokenSystem = autenticacionService.generateAdministratorToken();
					String propiedadMultiple = propiedadService.obtenerUnica(PropiedadValorDefinidoDTO.CAMPO, campoDestino, Propiedades.MULTIPLE, tokenSystem.getUsuario());
					ProcesoTransicionDTO transicion = new ProcesoTransicionDTO();//Esto lo hago para ahorrarme una consulta ala BD
					transicion.setLlaveTabla(dto.getTransicion());

					String transaccionDocumento = null;
					
					List<RelacionInternaDTO> relaciones = relacionService.relacionesPropiedad(dto.getPropiedad());
					if(relaciones==null || relaciones.isEmpty()|| relaciones.size()>1) {
						throw new ServerException("La transicion debe tener una unica (1) relacion que indique en que campo de la plantilla se va a guardar el documento resultado del temporizador. Revisar.  ( Ubicacion: "+ propiedadService.ubicarPropiedad(pTemporizador) + ")");
					}else {
						PedidoVentaCaracteristicaDTO campoPrinicipal = new PedidoVentaCaracteristicaDTO();
						campoPrinicipal.setCampo(relaciones.get(0).getCampo());
						transicion.setPlantilla(relaciones.get(0).getPlantilla());
						if(propiedadMultiple==null) {
							dto.setMensaje("");
							for (PedidoVentaDTO iPedido : documentos) {
								campoPrinicipal.setValorOpcion(iPedido.getLlaveTabla());
								PedidoVentaDTO nuevo = createDocumentSinceProperties.generateDocumentsFromAutomaticTask(transicion, null, iPedido, transaccionDocumento, tokenSystem.getLlaveTabla(), campoPrinicipal);
								transaccionDocumento = nuevo.getTransaccion();
								dto.setMensaje(dto.getMensaje() + nuevo.getNombre() + " ; ");
							}
						}else {
							campoPrinicipal.setExpedientes(documentos);
							PedidoVentaDTO nuevo = createDocumentSinceProperties.generateDocumentsFromAutomaticTask(transicion, null, null, transaccionDocumento, tokenSystem.getLlaveTabla(), campoPrinicipal);
							if(nuevo !=null) {
								dto.setMensaje(nuevo.getNombre());	
							}else {
								dto.setMensaje("Generar documentos no genera. Revisar");
							}
						}
					}
				}
			} else {
				if(pTemporizador.getKey().compareTo(Propiedades.PERIODO_LIMPIEZA_HISTORICO)==0) {
					try {
						int days = Integer.parseInt(pTemporizador.getValor());
						Calendar fechaCalculada = new GregorianCalendar();
						if(days!=0)fechaCalculada.add(Calendar.DAY_OF_MONTH, -days);
						int migrados =procesoTransicionAutomaticaMapper.funcionPasarTablaHistoricos(pTemporizador.getCampo(), fechaCalculada.getTime());
						dto.setMensaje(String.valueOf(migrados));
					} catch (Exception e) {
						dto.setMensaje("ERROR : " + e.getMessage());
						try {
							sendMessageToAdminSvc.call("Error en ejecucion de transaccion de limpieza de datos" + dto.getPlantillaNombre(), e.getMessage() + "\n\n(" +dto.getLlaveTabla() + ")");
						} catch (ServerException e1) {
						}
					}
				}
			}
		}else {
			dto.setMensaje("La propiedad temporizador esta bloqueada a esta horas");
		}
		dto.setEjecucion(new Date());
		return update(dto);
	}
	
// END region aditionalMethods

}