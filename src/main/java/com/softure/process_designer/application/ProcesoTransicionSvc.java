package com.softure.process_designer.application;

import java.util.List;

// BEGIN region interImport
import java.util.ArrayList;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaFilterDTO;
import com.softure.java.services.SoftureUtil;
import com.softure.process_designer.domain.ProcesoEstadoDTO;
import com.softure.process_designer.domain.ProcesoEstadoFilterDTO;
import com.softure.process_designer.domain.ProcesoTransicionDTO;
import com.softure.process_designer.domain.ProcesoTransicionFilterDTO;
import com.softure.process_designer.infrastructure.ProcesoTransicionMapper;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.property.domain.PropiedadDTO;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.logisticpymes.application.BasicSvc;

@Service("procesoTransicionService")
public class ProcesoTransicionSvc extends BasicSvc<ProcesoTransicionDTO, ProcesoTransicionFilterDTO> {
	
	@Autowired @Lazy 
	private ProcesoTransicionMapper procesoTransicionMapper;
	
	// BEGIN region servicesProcesoTransicion
	@Autowired @Lazy  private PedidoVentaSvc pedidoService;
	@Autowired @Lazy  private ProcesoEstadoSvc estadoService;
	@Autowired @Lazy  private DocumentoPlantillaSvc plantillaService;
	// END region servicesProcesoTransicion

	@Override
	public ProcesoTransicionDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. ProcesoTransicion");
		ProcesoTransicionFilterDTO dto = new ProcesoTransicionFilterDTO();
		dto.setLlaveTabla(llave);
		return procesoTransicionMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = procesoTransicionMapper;
	}
	
	@Override
	public ProcesoTransicionDTO activar(ProcesoTransicionDTO dto, String token) throws ServerException {
		// BEGIN ProcesoTransicion_activar
		return super.activar(dto, token);
		// END ProcesoTransicion_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProcesoTransicionDTO actualizar( ProcesoTransicionDTO dto, String token) throws ServerException {
		// BEGIN ProcesoTransicion_actualizar
		validarTransicion(dto);
		return super.update(dto);
		// END ProcesoTransicion_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProcesoTransicionDTO inactivar(ProcesoTransicionDTO dto, String token) throws ServerException {
		// BEGIN ProcesoTransicion_inactivar
		ProcesoTransicionDTO bd = consultaXId(dto.getLlaveTabla());
		if(bd.getEstadoPartida()==null) {
			PedidoVentaFilterDTO contar = new PedidoVentaFilterDTO();
			contar.setEstado(SharedConstants.STATE_ACTIVE);
			contar.setPlantilla(bd.getPlantilla());
			int cantidad = pedidoService.contarResultados(contar);
			if(cantidad != 0) {
				PedidoVentaDTO plantilla = pedidoService.consultaXId(bd.getPlantilla());
				throw new ServerException("Al intentar anular la transicion " + bd.getNombre() + " encontramos que existen " + cantidad + " registros de la plantilla " + plantilla.getNombre() + " todavia activos. Eliminar esta transicion puede generar una inconsistencia en la informacion, lo mejor es que finalices el ciclo de estos documentos. Recuerda estos estados del proceso siempre van a estar con tu documento si te equivocaste de plantilla lo mejor es iniciar una nueva plantilla");
			}
		}
		return super.inactivar(dto, token);
		// END ProcesoTransicion_inactivar
	}
	
	@Override
	public ProcesoTransicionDTO consultaUnica(ProcesoTransicionFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(ProcesoTransicionFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<ProcesoTransicionDTO> listarConsulta(ProcesoTransicionFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProcesoTransicionDTO guardar(ProcesoTransicionDTO dto, String token) throws ServerException {
		// BEGIN ProcesoTransicion_guardar
		if(dto.getEstadoLLegada()==null) dto.setEstadoLLegada(dto.getEstadoPartida());
		if(dto.getPlantilla()==null && dto.getDocumentador()) {
			ProcesoEstadoDTO inicial =null;
			if(dto.getEstadoPartida()!=null) inicial = estadoService.consultaXId(dto.getEstadoPartida());
			if(inicial==null || inicial.getTipo().compareTo(ProcesoEstadoDTO.TIPO_ESTADO)==0)dto.setPlantilla(crearPlantilla(dto, null, token));
		}
		validarTransicion(dto);
		dto = super.guardar(dto, token);
		return dto;
		// END ProcesoTransicion_guardar
	}

// BEGIN region aditionalMethods
	public List<ProcesoTransicionDTO> listarTransicionesRol(ProcesoTransicionFilterDTO dto)
			throws ServerException {
		return procesoTransicionMapper.listarTransicionesRol(dto);
	}
	
	public String consultarProceso(String plantilla)throws ServerException {
		ProcesoTransicionDTO filtro =consultarTransaccionInicial(plantilla);
		if(filtro!=null) return filtro.getProceso();
		return null;
	}
	
	public List<ProcesoTransicionDTO> listarTransaccionesIniciales(String plantilla, String proceso) throws ServerException {
		ProcesoTransicionFilterDTO filtro = new ProcesoTransicionFilterDTO();
		filtro.setPlantilla(plantilla);
		filtro.setProceso(proceso);
		return procesoTransicionMapper.listarTransaccionInicial(filtro);
	}
	
	public ProcesoTransicionDTO consultarTransaccionInicial(String plantilla) throws ServerException {
		List<ProcesoTransicionDTO> result = listarTransaccionesIniciales(plantilla, null);
		if(result!=null && !result.isEmpty()) {
			if(result.size()==1) {
				return result.get(0);
			}else {
				throw new ServerException("Revisar porq esta plantilla genera varios procesos.\n" + result.get(0).getPlantillaNombre());
			}
		}
		return null;
	}
	/*
	@Autowired @Lazy  ManageTransitionFunction manageTransition;
	
	public ProcesoTransicionDTO gestionarTransicion(
			ProcesoTransicionDTO dto, 
			String expediente, 
			PedidoVentaDTO documentoDTO, 
			BigDecimal valorModificador, 
			PedidoVentaDineroDTO dineroProcesado, 
			DocumentoRelacionGestorDTO relacionAnterior,
			String token,
			String transaccion) throws ServerException {
		
		return manageTransition.execute(dto, expediente, documentoDTO, valorModificador, dineroProcesado, relacionAnterior, token, transaccion);
	}*/
	
	private void validarTransicion(ProcesoTransicionDTO dto) throws ServerException {
		if(dto==null) throw new ServerException("Transicion nula");
		if(dto.getProceso()==null) throw new ServerException("Transicion sin maquina de estados");
		
		//YA no necesito estoi porque la llegada es obligatoria
		//if(dto.getEstadoPartida()==null && dto.getEstadoLLegada()==null && dto.getDecision()==null) throw new ServerException("Revise los estados de inicio o fin");
		//if(dto.getEstadoLLegada()==null && dto.getDecision()==null) dto.setEstadoLLegada(dto.getEstadoPartida());//Esto es para evitar un error que no encuentra estado de llegada
		ProcesoEstadoDTO estado;
		estado = estadoService.consultaXId(dto.getEstadoLLegada());
		if(estado.getProceso().compareTo(dto.getProceso())!=0)throw new ServerException("La plantilla del estado de llegada debe ser de la misma maquina de estados");
		if(dto.getEstadoPartida()!=null){
			estado = estadoService.consultaXId(dto.getEstadoPartida());
			if(estado.getProceso().compareTo(dto.getProceso())!=0)throw new ServerException("La plantilla del estado de partida debe ser de la misma maquina de estados");
			if(dto.getPlantilla()==null && estado.getTipo().compareTo(ProcesoEstadoDTO.TIPO_ESTADO)==0) throw new ServerException("Transicion sin formulario");
			if (estado.getTipo().compareTo(ProcesoEstadoDTO.TIPO_API)==0) {
				if(dto.getNombre().compareTo(SharedConstants.OK)!=0 && dto.getNombre().compareTo(SharedConstants.ERROR)!=0
						&& dto.getNombre().compareTo(SharedConstants.INCOMPLETE)!=0) {
					throw new ServerException("Las opciones que puede tener un Iterador son OK, ERROR o INCOMPLETE");
				}
			}
		}else {
			if(dto.getPlantilla()==null) throw new ServerException("Transicion sin formulario");
			// Validar que la transicion de inicio no se use en 2 procesos como inicial
			ProcesoTransicionFilterDTO filtroValidacion = new ProcesoTransicionFilterDTO();
			filtroValidacion.setEstado(SharedConstants.STATE_ACTIVE);
			filtroValidacion.setPlantilla(dto.getPlantilla());
			List<ProcesoTransicionDTO> filtradas = listarConsulta(filtroValidacion);
			if(filtradas!=null &&!filtradas.isEmpty()) {
				for (ProcesoTransicionDTO iTransicion : filtradas) {
					if(iTransicion.getEstadoPartida()==null) {
						if(dto.getLlaveTabla()==null || dto.getLlaveTabla().compareTo(iTransicion.getLlaveTabla())!=0 )
						 throw new ServerException("Esta plantilla esta siendo usada como inicio de un proceso diferente. " + iTransicion.getProcesoNombre() + "\n Nombre : " + iTransicion.getNombre());
					}
				}
			}
			//
			if(dto.getLlaveTabla()==null) {
				organizarEstadosNuevos(dto.getProceso(), dto.getPlantilla(), SharedConstants.STATE_ACTIVE, dto.getEstadoLLegada());
				organizarEstadosNuevos(dto.getProceso(), dto.getPlantilla(), SharedConstants.STATE_INACTIVE, null);
				organizarEstadosNuevos(dto.getProceso(), dto.getPlantilla(), SharedConstants.STATE_COMPLETE, null);
			}
			//dto.setDocumentador(true);
		}
		if (dto.getCodigo() == null) 
			dto.setCodigo((dto.getNombre().length()>50)?dto.getNombre().substring(0,49):dto.getNombre());
		dto.setCodigo(SoftureUtil.formatFunction(dto.getCodigo()).toUpperCase());
	}
	
	
	private void organizarEstadosNuevos(String proceso, String plantilla, String estadoBase, String procesoEstado) throws ServerException{
		PedidoVentaDTO contador = new PedidoVentaDTO();
		contador.setPlantilla(plantilla);
		contador.setEstado(estadoBase);
		int cantidad = pedidoService.listarEstadosNuevoProceso(contador);
		if(cantidad>0){
			if(procesoEstado ==null) {
				//Valido que si no tenia proceso le coloque valor a todos los documentos del proceso
				ProcesoEstadoFilterDTO estadoFiltro = new ProcesoEstadoFilterDTO();
				estadoFiltro.setEstado(SharedConstants.STATE_ACTIVE);
				estadoFiltro.setProceso(proceso);
				estadoFiltro.setEstadoDocumento(estadoBase);
				List<ProcesoEstadoDTO> estados = estadoService.listarConsulta(estadoFiltro);
				if(estados==null || estados.isEmpty()) throw new ServerException("El nuevo proceso debe tener estados para relacionar:" + estadoBase );
				if(estados.size()!=1) throw new ServerException("Todavia no esta la funcionalidad de cambio de estados, diferente a 1. Active un solo estado del proceso para continuar");
				procesoEstado = estados.get(0).getLlaveTabla();
			}
			contador.setEstadoExpediente(procesoEstado);
			pedidoService.actualizarEstadosNuevoProceso(contador);
		}
	}
	
	private String crearPlantilla(ProcesoTransicionDTO dto, String codigoFormulario, String token) throws ServerException {
		DocumentoPlantillaDTO plantilla = new DocumentoPlantillaDTO();

		plantilla.setProceso(dto.getProceso());
		plantilla.setCodigo(codigoFormulario);
		plantilla.setNombre(dto.getNombre());
		plantilla.setObjetivo(dto.getNombre());
		if(dto.getEstadoPartida()==null)plantilla.setPropiedades(new ArrayList<PropiedadDTO>());//Esta es la estrategia para que se cree listable el formularios
		plantilla = plantillaService.guardar(plantilla, token);
		if(dto.getEstadoPartida()!=null) plantillaService.crearCampoProcesos(plantilla.getLlaveTabla(), token);
		return plantilla.getLlaveTabla();
	}
	
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProcesoTransicionDTO guardarConCodigo(ProcesoTransicionDTO dto, String codigoFormulario, String plantilla, String token) throws ServerException {
		if(plantilla==null) {
			dto.setPlantilla(crearPlantilla(dto, codigoFormulario, token));			
		}else {
			dto.setPlantilla(plantilla);
		}
		validarTransicion(dto);
		return super.guardar(dto, token);
	}

	public List<ProcesoTransicionDTO> getFullToSynchronize(List<String> process) {
		return procesoTransicionMapper.getFullToSynchronize(process);
	}	
	
// END region aditionalMethods

}