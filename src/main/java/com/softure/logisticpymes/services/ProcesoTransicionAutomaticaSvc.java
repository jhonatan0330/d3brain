package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.Calendar;

import com.softure.logisticpymes.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.dto.PedidoVentaDTO;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.dto.PropiedadDTO;
import com.softure.logisticpymes.dto.PropiedadValorDefinidoDTO;
import com.softure.logisticpymes.dto.RelacionInternaDTO;
import com.softure.logisticpymes.dto.UsuarioSesionDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaFilterDTO;
import com.softure.logisticpymes.services.adapter.Propiedades;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.ProcesoTransicionAutomaticaDTO;
import com.softure.logisticpymes.dto.ProcesoTransicionDTO;
import com.softure.logisticpymes.dto.filter.ProcesoTransicionAutomaticaFilterDTO;
import com.softure.logisticpymes.persistence.ProcesoTransicionAutomaticaMapper;

@Service("procesoTransicionAutomaticaService")
public class ProcesoTransicionAutomaticaSvc extends BasicSvc<ProcesoTransicionAutomaticaDTO, ProcesoTransicionAutomaticaFilterDTO> {
	
	@Autowired
	private ProcesoTransicionAutomaticaMapper procesoTransicionAutomaticaMapper;
	
	// BEGIN region servicesProcesoTransicionAutomatica
	@Autowired private PedidoVentaSvc documentoService;
	@Autowired private PropiedadSvc propiedadService;
	@Autowired private ProcesoTransicionSvc transicionService;
	@Autowired private UsuarioAutenticacionSvc autenticacionService;
	@Autowired private RelacionInternaSvc relacionService;
	// END region servicesProcesoTransicionAutomatica

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
		// BEGIN ProcesoTransicionAutomatica_activar
		return super.activar(dto, token);
		// END ProcesoTransicionAutomatica_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProcesoTransicionAutomaticaDTO actualizar( ProcesoTransicionAutomaticaDTO dto, String token) throws ServerException {
		// BEGIN ProcesoTransicionAutomatica_actualizar
		return super.actualizar(dto, token);
		// END ProcesoTransicionAutomatica_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
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
	
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProcesoTransicionAutomaticaDTO ejecutar(ProcesoTransicionAutomaticaDTO dto, String token)throws ServerException{
		// BEGIN region ejecutar
		ProcesoTransicionAutomaticaDTO bd = consultaXId(dto.getLlaveTabla());
		return gestionaEjecucion(bd);
		// END region ejecutar
	}
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProcesoTransicionAutomaticaDTO programar(ProcesoTransicionAutomaticaDTO dto, String token)throws ServerException{
		// BEGIN region programar
		programateAll();
		return null;
		// END region programar
	}

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
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
	
	private ProcesoTransicionAutomaticaDTO containsTransicion(List<ProcesoTransicionAutomaticaDTO> review, ProcesoTransicionAutomaticaDTO view) {
		if(review == null || review.isEmpty()) return null;
		for (ProcesoTransicionAutomaticaDTO procesoTransicionAutomaticaDTO : review) {
			if(procesoTransicionAutomaticaDTO.getTransicion().compareTo(view.getTransicion())==0)
				return procesoTransicionAutomaticaDTO;
		}
		return null;
	}
	
	public void programateAll() throws ServerException{
		List<PropiedadDTO> faltantes = propiedadService.consultarTemporizadoresPendientes();
		if(faltantes==null || faltantes.isEmpty()) return;
		Date fechaProgramada;
		for (PropiedadDTO propiedadDTO : faltantes) {
			if(propiedadDTO.getFechaInicial().compareTo(new Date())>0) {
				fechaProgramada = propiedadDTO.getFechaInicial();
			}else {
				Date ultimaEjecucion = procesoTransicionAutomaticaMapper.obtenerFechaUltimaEjecucion(propiedadDTO.getCampo());
				if(ultimaEjecucion==null) {
					fechaProgramada = calcularFecha(propiedadDTO.getFechaInicial(), propiedadDTO.getTexto()); 
				}else {
					fechaProgramada = calcularFecha(ultimaEjecucion, propiedadDTO.getTexto());
				}
			}
			ProcesoTransicionAutomaticaDTO programar = new ProcesoTransicionAutomaticaDTO();
			programar.setTransicion(propiedadDTO.getCampo());
			programar.setFecha(fechaProgramada);
			if(propiedadDTO.getMotivo()==null) {
				programar.setMensaje(propiedadDTO.getNombre());
			}else {
				programar.setMensaje(propiedadDTO.getMotivo());
			}
			programar.setPropiedad(propiedadDTO.getLlaveTabla());
			save(programar);
		}
	}

	private Date calcularFecha(Date ultimaEjecucion, String valor) {
		String[]temporizador = valor.split(":");
		int years = Integer.parseInt( temporizador[0]);
		int month = Integer.parseInt( temporizador[1]);
		int days = Integer.parseInt( temporizador[2]);
		int hours = Integer.parseInt( temporizador[3]);
		int minutes = Integer.parseInt( temporizador[4]);
		Calendar fechaCalculada = new GregorianCalendar();
		fechaCalculada.setTime(ultimaEjecucion);
		while (fechaCalculada.getTime().compareTo(new Date())<0) {
			if(years!=0)fechaCalculada.add(Calendar.YEAR, years);
			if(month!=0)fechaCalculada.add(Calendar.MONTH, month);
			if(days!=0)fechaCalculada.add(Calendar.DAY_OF_MONTH, days);
			if(hours!=0)fechaCalculada.add(Calendar.HOUR_OF_DAY, hours);
			if(minutes!=0)fechaCalculada.add(Calendar.MINUTE, minutes);			
		}
		return fechaCalculada.getTime();
	}
	
	public void inactivarPropiedad(String propiedad) throws ServerException {
		procesoTransicionAutomaticaMapper.inactivarPropiedad(propiedad);
	}
	
	public ProcesoTransicionAutomaticaDTO gestionaEjecucion(ProcesoTransicionAutomaticaDTO dto)throws ServerException{
		PropiedadDTO pTemporizador = propiedadService.consultaXId(dto.getPropiedad());
		if(Propiedades.validarBloqueo(pTemporizador)) {
			List<PedidoVentaDTO> documentos = null;
			documentos = documentoService.listarExpedientesDisponiblesDocumentoFuncion(new PedidoVentaFilterDTO(), dto.getPropiedad(), null);
			if(documentos ==null || documentos.isEmpty()) {
				dto.setMensaje("Sin documentos a gestionar");
			}else {
				String campoDestino = procesoTransicionAutomaticaMapper.getFieldPlantilla(dto.getPropiedad());
				if(campoDestino==null) throw new ServerException("No se identifica el campo en donde se van a almacenar los documentos");
				UsuarioSesionDTO tokenSystem = autenticacionService.generateAdministratorToken();
				String propiedadMultiple = propiedadService.obtenerUnica(PropiedadValorDefinidoDTO.CAMPO, campoDestino, Propiedades.MULTIPLE, tokenSystem.getUsuario());
				ProcesoTransicionDTO transicion = new ProcesoTransicionDTO();//Esto lo hago para ahorrarme una consulta ala BD
				transicion.setLlaveTabla(dto.getTransicion());
				transicion.setPlantilla(dto.getPlantilla());

				String transaccionDocumento = null;
				
				List<RelacionInternaDTO> relaciones = relacionService.relacionesPropiedad(dto.getPropiedad());
				if(relaciones==null || relaciones.isEmpty()|| relaciones.size()>1) {
					dto.setMensaje("La transicion debe tener una relacion. Revisar. " + transicion.getNombre());
				}else {
					PedidoVentaCaracteristicaDTO campoPrinicipal = new PedidoVentaCaracteristicaDTO();
					campoPrinicipal.setCampo(relaciones.get(0).getCampo());
					
					if(propiedadMultiple==null) {
						dto.setMensaje("");
						;
						for (PedidoVentaDTO iPedido : documentos) {
							campoPrinicipal.setValorOpcion(iPedido.getLlaveTabla());
							PedidoVentaDTO nuevo = transicionService.generarDocumentosTransicion(transicion, null, iPedido, transaccionDocumento, tokenSystem.getLlaveTabla(), campoPrinicipal);
							transaccionDocumento = nuevo.getTransaccion();
							dto.setMensaje(dto.getMensaje() + nuevo.getNombre() + " ; ");
						}
					}else {
						campoPrinicipal.setExpedientes(documentos);
						PedidoVentaDTO nuevo = transicionService.generarDocumentosTransicion(transicion, null, null, transaccionDocumento, tokenSystem.getLlaveTabla(), campoPrinicipal);
						if(nuevo !=null) {
							dto.setMensaje(nuevo.getNombre());	
						}else {
							dto.setMensaje("Generar documentos no genera. Revisar");
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