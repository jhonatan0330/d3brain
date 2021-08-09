package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.math.BigDecimal;
import java.util.ArrayList;

import com.softure.logisticpymes.dto.ActividadDTO;
import com.softure.logisticpymes.dto.DocumentoPlantillaDTO;
import com.softure.logisticpymes.dto.DocumentoRelacionGestorDTO;
import com.softure.logisticpymes.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.dto.PedidoVentaDTO;
import com.softure.logisticpymes.dto.PedidoVentaDineroDTO;
import com.softure.logisticpymes.dto.ProcesoEstadoDTO;
import com.softure.logisticpymes.dto.PropiedadDTO;
import com.softure.logisticpymes.dto.PropiedadValorDefinidoDTO;
import com.softure.logisticpymes.dto.RelacionInternaDTO;
import com.softure.logisticpymes.dto.UsuarioDTO;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.services.adapter.Propiedades;
import com.softure.logisticpymes.dto.filter.ProcesoEstadoFilterDTO;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.ProcesoTransicionDTO;
import com.softure.logisticpymes.dto.filter.ProcesoTransicionFilterDTO;
import com.softure.logisticpymes.persistence.ProcesoTransicionMapper;

@Service("procesoTransicionService")
public class ProcesoTransicionSvc extends BasicSvc<ProcesoTransicionDTO, ProcesoTransicionFilterDTO> {
	
	@Autowired
	private ProcesoTransicionMapper procesoTransicionMapper;
	
	// BEGIN region servicesProcesoTransicion
	@Autowired private ActividadSvc actividadService;
	@Autowired private MensajeSvc mensajeSvc;
	@Autowired private PedidoVentaSvc pedidoService;
	@Autowired private PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService;
	@Autowired private PedidoVentaDineroSvc dineroService;
	@Autowired private ProcesoEstadoSvc estadoService;
	@Autowired private PropiedadSvc propiedadService;
	@Autowired private RelacionInternaSvc relacionService;
	@Autowired private DocumentoPlantillaSvc plantillaService;
	@Autowired private DocumentoRelacionGestorSvc relacionGestorService;
	@Autowired private WebServiceEjecucionSvc apiService;
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
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProcesoTransicionDTO actualizar( ProcesoTransicionDTO dto, String token) throws ServerException {
		// BEGIN ProcesoTransicion_actualizar
		validarTransicion(dto);
		return super.update(dto);
		// END ProcesoTransicion_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProcesoTransicionDTO inactivar(ProcesoTransicionDTO dto, String token) throws ServerException {
		// BEGIN ProcesoTransicion_inactivar
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
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
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
	
	public List<ProcesoTransicionDTO> listarTransaccionesIniciales(String plantilla) throws ServerException {
		ProcesoTransicionFilterDTO filtro = new ProcesoTransicionFilterDTO();
		filtro.setPlantilla(plantilla);
		return procesoTransicionMapper.listarTransaccionInicial(filtro);
	}
	
	public ProcesoTransicionDTO consultarTransaccionInicial(String plantilla) throws ServerException {
		List<ProcesoTransicionDTO> result = listarTransaccionesIniciales(plantilla);
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
	 * El objetivo es obtener la ultima transciion para llegar a un estado, normalemente se hace solo una iteracion
	 * esto cambia cuando hay 2 decisiones unidas
	 */
	public ProcesoTransicionDTO gestionarTransicion(
			ProcesoTransicionDTO dto, 
			String expediente, 
			PedidoVentaDTO documentoDTO, 
			BigDecimal valorModificador, 
			PedidoVentaDineroDTO dineroProcesado, 
			DocumentoRelacionGestorDTO relacionAnterior,
			String token) throws ServerException {

		// Aqui lleno las propiedades del dto asi no falla api
		propiedadService.validarFuncionConsultandoPropiedad(dto, PropiedadValorDefinidoDTO.TRANSICION, expediente, documentoDTO.getLlaveTabla(), getUserFlex(token));
		ProcesoTransicionDTO respuesta = dto;
		PedidoVentaDTO expedienteDTO = pedidoService.consultaXId(expediente);
		ProcesoEstadoDTO filtroEstado = estadoService.consultaXId(dto.getEstadoLLegada());
		ProcesoEstadoDTO anteriorEstado = null;
		if(dto.getEstadoPartida()!=null) anteriorEstado = estadoService.consultaXId(dto.getEstadoPartida());
		if(filtroEstado==null)throw new ServerException("No se encuentra estado de llegada, en caso que no se modifiquen coloque el mismo estado.\n" + expedienteDTO.getNombre() +  " - " + expedienteDTO.getDescripcion());
		System.out.format("\n\n[%s] Procesando transicion (%s) del proceso (%s)", expedienteDTO.getNombre(), dto.getNombre(), dto.getProcesoNombre());
		String modificadorId = null;
		PedidoVentaDineroDTO afectado = null;
		if(anteriorEstado!=null && anteriorEstado.getTipo().compareTo(ProcesoEstadoDTO.TIPO_ITERADOR)==0) {
			 iteracion(respuesta, expedienteDTO, documentoDTO, token, relacionAnterior);
		}else {
			String ubicacion = obtenerUbicacion(documentoDTO, dto.getLlaveTabla(), token);
			System.out.format("\n[%s] Afectando saldos con parametro de la transicion %s", expedienteDTO.getNombre(), dto.getAfectaSaldo());
			afectado = afectarSaldos(expediente, token, dto, valorModificador, dineroProcesado);
			//Genero documento en caso que toque
			if(dto.getPlantilla()!=null) {
				modificadorId = documentoDTO.getLlaveTabla();
				PedidoVentaDTO automatico = generarDocumentosTransicion(dto, documentoDTO, expedienteDTO, documentoDTO.getTransaccion(), token, null);
				if(automatico!=null && automatico.getPlantilla().compareTo(dto.getPlantilla())==0)//Por si es la transicion inicial no  le quite el poder del documento que genero  
					modificadorId = automatico.getLlaveTabla();
			}
			System.out.format("\n[%s] Envia a motor de traza por modificador ( %s ) ", expedienteDTO.getNombre(), documentoDTO.getNombre());
			//Creo la relacion del documento Gestor
			relacionAnterior = relacionGestorService.trazar(expedienteDTO.getLlaveTabla(), 
					modificadorId,
					dto.getNombre(), dto.getEstadoPartida(), dto.getEstadoLLegada(), 
					(afectado==null)?null:afectado.getLlaveTabla(), 
					ubicacion, token, relacionAnterior, expedienteDTO.getHistorico());
		}
		//Se actualiza pedido
		// si son los mismo creo que no necesito update ???????????
		System.out.format("\n[%s] Se actualiza estado del documento de ( %s ) a ( %s )", expedienteDTO.getNombre(), expedienteDTO.getEstadoNombre(), filtroEstado.getNombre());
		expedienteDTO.setEstadoExpediente(filtroEstado.getLlaveTabla());
		expedienteDTO.setEstado(filtroEstado.getEstadoDocumento());//No se porque tenia esta linea ->//anterior.setEstadoNombre(filtroEstado.getNombre());
		pedidoService.update(expedienteDTO);
		String api = Propiedades.obtenerValor(dto, Propiedades.API_TRANSACCION);
		if(!api.isEmpty()) apiService.ejecutar(api, expedienteDTO, documentoDTO, token);
		if(dto.getEstadoLlegadaTipo().compareTo(ProcesoEstadoDTO.TIPO_DECISION)==0) {
			respuesta= decision(dto.getEstadoLLegada(), expediente, documentoDTO.getLlaveTabla(), token);
			respuesta = gestionarTransicion(respuesta, expediente, documentoDTO, valorModificador, afectado, relacionAnterior, token);
		}else {
			if(dto.getEstadoLlegadaTipo().compareTo(ProcesoEstadoDTO.TIPO_ITERADOR)==0) {
				ProcesoTransicionFilterDTO solucion = new ProcesoTransicionFilterDTO();
				solucion.setEstadoPartida(dto.getEstadoLLegada());
				solucion.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
				respuesta = super.consultaUnica(solucion);
				if(respuesta==null) throw new ServerException(dto.getEstadoLlegadaNombre() + "\nNo se encuentra una transicion ligada a la iteracion ");
								
				respuesta = gestionarTransicion(respuesta, expediente, documentoDTO, valorModificador, afectado, relacionAnterior, token);//Por si siguen decisiones
				mensajeSvc.gestionarMensajes(expedienteDTO, dto, null, documentoDTO, token);//Aqui tambien gestiona mensajes se duplica porque no evalue bien que eimpato tiene ponerlo antes o despues  
			}else {//Solo gestiono responsable y mensajes al finalizar la transicion
				UsuarioDTO responsable = gestionarResponsable(expediente,filtroEstado.getLlaveTabla(), filtroEstado.getNombre(), documentoDTO.getLlaveTabla(), token);
				mensajeSvc.gestionarMensajes(expedienteDTO, dto, responsable, documentoDTO, token);
			}
		}
		
		return respuesta;
	}
	
	
	/*
	 * Esto es lo mimo de la normal pero vuelve al estado incial, tengo que ver como cambio esto
	 */
	public ProcesoTransicionDTO gestionarTransicionReversa(
			ProcesoTransicionDTO dto, 
			String expediente, 
			PedidoVentaDTO documento, 
			String token) throws ServerException {
		ProcesoTransicionDTO respuesta = dto;
		PedidoVentaDTO anterior = pedidoService.consultaXId(expediente);
		ProcesoEstadoDTO filtroEstado = estadoService.consultaXId(dto.getEstadoPartida());
		if(filtroEstado==null)throw new ServerException("No se encuentra estado de partida, en caso que no se modifiquen coloque el mismo estado.\n" + anterior.getNombre() +  " - " + anterior.getDescripcion());
		if(filtroEstado.getTipo().compareTo(ProcesoEstadoDTO.TIPO_ESTADO)!=0) throw new ServerException("No se puede devolver a una decision");
		String ubicacion = obtenerUbicacion(documento, dto.getLlaveTabla(), token);
		BigDecimal valorModificador = null;
		if(dto.getAfectaSaldo()!=null) {
			if(dto.getAfectaSaldo().compareTo(ProcesoTransicionDTO.RESTANDO)==0) {
				dto.setAfectaSaldo(ProcesoTransicionDTO.SUMANDO);
			}else {
				dto.setAfectaSaldo(ProcesoTransicionDTO.RESTANDO);
			}
			valorModificador = procesoTransicionMapper.valorEntransicionParaRevertir(documento.getLlaveTabla(), expediente);
		}
		PedidoVentaDineroDTO nuevoValor = afectarSaldos(expediente, token, dto, valorModificador, null);//aqui es nulo porque ya existe
		//Creo la relacion del documento Gestor
		relacionGestorService.trazar(anterior.getLlaveTabla(), documento.getLlaveTabla(), dto.getNombre(), dto.getEstadoLLegada(), 
				dto.getEstadoPartida(), (nuevoValor==null)?null:nuevoValor.getLlaveTabla(), ubicacion, token, null, anterior.getHistorico());
		//Se actualiza pedido
		System.out.println(anterior.getNombre() + " : " + filtroEstado.getNombre() + "(" +anterior.getEstadoNombre() + ")");
		anterior.setEstadoExpediente(filtroEstado.getLlaveTabla());
		anterior.setEstado(filtroEstado.getEstadoDocumento());
		//No se porque tenia esta linea//anterior.setEstadoNombre(filtroEstado.getNombre());
		pedidoService.update(anterior);
		gestionarResponsable(expediente,filtroEstado.getLlaveTabla(), filtroEstado.getNombre(), documento.getLlaveTabla(), token);
		//Por el momento asumo que no tuvo preguntas
		/*
		if(dto.getEstadoPartid().compareTo(ProcesoEstadoDTO.TIPO_DECISION)==0) {
			respuesta= decision(dto.getEstadoLLegada(), expediente, documento.getLlaveTabla());
			//Aqui coloco la traza de las decisiones me falta unirlas, las otras se gestionan en cada parte por el dinero
			//relacionGestorService.trazar(documento, expediente, dto.getEstadoLLegada(), respuesta.getEstadoPartida(), nuevoValor, ubicacion, dto.getSecurityToken());
			respuesta = gestionarTransicion(respuesta, expediente, documento, nuevoValor);
		}else {
			
			//Quito los mensajes se supone que devuelve
			//mensajeSvc.gestionarMensajes(anterior, dto, responsable, documento);
		}*/
		return respuesta;
	}
	
	
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
		}else {
			if(dto.getPlantilla()==null) throw new ServerException("Transicion sin formulario");
			// Validar que la transicion de inicio no se use en 2 procesos como inicial
			ProcesoTransicionFilterDTO filtroValidacion = new ProcesoTransicionFilterDTO();
			filtroValidacion.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
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
				organizarEstadosNuevos(dto.getProceso(), dto.getPlantilla(), ConstantesGenerales.ESTADO_ACTIVO, dto.getEstadoLLegada());
				organizarEstadosNuevos(dto.getProceso(), dto.getPlantilla(), ConstantesGenerales.ESTADO_INACTIVO, null);
				organizarEstadosNuevos(dto.getProceso(), dto.getPlantilla(), ConstantesGenerales.ESTADO_FINALIZADO, null);
			}
			dto.setDocumentador(true);
			
		}
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
				estadoFiltro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
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
	
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProcesoTransicionDTO guardarConCodigo(ProcesoTransicionDTO dto, String codigoFormulario, String plantilla, String token) throws ServerException {
		if(plantilla==null) {
			dto.setPlantilla(crearPlantilla(dto, codigoFormulario, token));			
		}else {
			dto.setPlantilla(plantilla);
		}
		validarTransicion(dto);
		return super.guardar(dto, token);
	}
	
	public ProcesoTransicionDTO decision(String decision, String llaveTablaDocumento, String llaveModificador, String token) throws ServerException {
		ProcesoEstadoDTO decisionDTO = estadoService.consultaXId(decision);
		if(decisionDTO.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0) 
			throw new ServerException("La decision "+decisionDTO.getNombre()+" esta inactiva");
		PropiedadDTO propiedadFuncion = propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.ESTADO, decision, Propiedades.DECISION_SQL, getUserFlex(token));
		if(propiedadFuncion==null) throw new ServerException("La decision "+decisionDTO.getNombre()+" no tiene definida la funcion SQL");
		String resultado = null;
		try {
			resultado = procesoTransicionMapper.decision(SoftureUtil.formatFunction(propiedadFuncion.getLlaveTabla()) , llaveTablaDocumento, llaveModificador);
		} catch (Exception e) {
			throw new ServerException(e.getMessage(), "Decision : " + decisionDTO.getNombre());
		}
		if(resultado==null)  throw new ServerException("El resultado ha sido nulo\nDecision : " + decisionDTO.getNombre());
		ProcesoTransicionFilterDTO solucionFilter = new ProcesoTransicionFilterDTO();
		solucionFilter.setEstadoPartida(decisionDTO.getLlaveTabla());
		solucionFilter.setNombre(resultado);
		solucionFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		ProcesoTransicionDTO solucion = super.consultaUnica(solucionFilter);
		if(solucion==null) throw new ServerException(decisionDTO.getNombre()+"\nNo se encuentra una transicion con el nombre para  esta respuesta: " + resultado);
		return solucion;
	}
	
	public void iteracion(
			ProcesoTransicionDTO transicionIteracion, // Estado que contine la iteracion y donde vamos a buscar al funcion
			PedidoVentaDTO expediente, // Documento Proceso que estamos afectando 
			PedidoVentaDTO documentoModificador, // Documento que realizo la acción y disparo la transicion
			String token, // Codigo de seguridad de la transaccion
			DocumentoRelacionGestorDTO relacionAnterior //SE necesita para la traza :(
			) throws ServerException {
		
		ProcesoEstadoDTO pEstadoDTO = estadoService.consultaXId(transicionIteracion.getEstadoPartida());
		if(pEstadoDTO.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0) 
			throw new ServerException("La iteracion "+ pEstadoDTO.getNombre()+" esta inactiva");
		PropiedadDTO propiedadFuncion = propiedadService.obtenerPropiedad(
				PropiedadValorDefinidoDTO.ESTADO, 
				pEstadoDTO.getLlaveTabla(), 
				Propiedades.ITERACION_SQL, 
				null);
		if(propiedadFuncion==null) throw new ServerException("La iteracion "+ pEstadoDTO.getNombre() +" no tiene definida la funcion SQL");
		
		List<PedidoVentaDTO> resultado = null;
		try {
			resultado = pedidoService.iteracionesProceso(SoftureUtil.formatFunction(propiedadFuncion.getLlaveTabla()) , 
					expediente.getLlaveTabla(), 
					(documentoModificador==null)?null:documentoModificador.getLlaveTabla());
		} catch (Exception e) {
			throw new ServerException(e.getMessage(), "Iteracion : " + pEstadoDTO.getNombre());
		}
		if(resultado!=null && !resultado.isEmpty()) {
			for (PedidoVentaDTO iDocumentoIterar : resultado) {
				iDocumentoIterar.setCaracteristicas(pedidoVentaCaracteristicaService.listar2Documento(iDocumentoIterar.getLlaveTabla(), iDocumentoIterar.getHistorico()));
				//Aqui al parecer el expediednte principal es el modificador pero no me parece que sea asi, deberia ser el expediente??, o talvez todos
				PedidoVentaDTO acabdoCrear = generarDocumentosTransicion(transicionIteracion, iDocumentoIterar, documentoModificador, iDocumentoIterar.getTransaccion(), token, null);
				//Creo la relacion del documento Gestor
				relacionGestorService.trazar(expediente.getLlaveTabla(), 
						(acabdoCrear==null)?null:acabdoCrear.getLlaveTabla(),
						transicionIteracion.getNombre(), expediente.getEstadoExpediente(), expediente.getEstadoExpediente(), 
						null, null, token, relacionAnterior, expediente.getHistorico());
			}
		}
	}
	
	public String obtenerUbicacion(PedidoVentaDTO pedido, String transicion, String token) throws ServerException {
		if(transicion==null) return null;
		PropiedadDTO ubicacion = propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.TRANSICION, transicion, Propiedades.UBICACION, getUserFlex(token));
		if(ubicacion==null) return null;
		System.out.format("\n......Buscando ubicacion del documento %s", pedido.getNombre());
		PedidoVentaCaracteristicaDTO campoValor= pedidoService.obtenerValor(pedido.getCaracteristicas(), ubicacion.getValor());
		if(campoValor ==null) throw new ServerException("Revisa la configuracion de ubicacion, el campo ya no esta disponible. " + ubicacion.getTexto());
		return campoValor.getValorOpcion();
	}
	
	
	public UsuarioDTO gestionarResponsable(String pedido, String estado, String estadoNombre, String modificador,String token) throws ServerException {//, DocumentoPlantillaDTO plantilla
		if(estado==null) return null;
		ActividadDTO responsable = new ActividadDTO();
		PropiedadDTO propiedadFuncion = propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.ESTADO, estado, Propiedades.FUNCION_SQL_ESTADO_ASIGNAR, getUserFlex(token)); 
		if(propiedadFuncion !=null){
			responsable.setResponsable(estadoService.obtenerResponsable(propiedadFuncion, pedido, modificador));
		}else{
			propiedadFuncion = propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.ESTADO, estado, Propiedades.ESTADO_ASIGNAR, getUserFlex(token));
			if(propiedadFuncion !=null){
				responsable.setResponsable(propiedadFuncion.getValor());
			}else{
				//retire la plantilla
				/*
				String campoResponsable = "";
				if(plantilla!=null) campoResponsable = Propiedades.obtenerValor(plantilla, Propiedades.RESPONSABLE);
				if(!campoResponsable.isEmpty()){
					PedidoVentaCaracteristicaDTO campoValor= pedidoService.obtenerValor(pedido.getCaracteristicas(), campoResponsable);
					if(campoValor==null) throw new ServerException("Se debe colocar la caracteristica de responsable");

					responsable.setResponsable(obtenerUsuarioDocumento(campoValor.getValorOpcion()));
				}else{
					responsable.setResponsable(null);
				}
				*/
			}
		}
		responsable.setDocumento(pedido);
		responsable.setComentario(estadoNombre);
		return actividadService.crearActividad(responsable, token);
	}


	/*
	private String obtenerUsuarioDocumento( String documento)throws ServerException {
		UsuarioRolDTO erl = new UsuarioRolDTO();
		erl.setDocumento(documento);
		erl.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		erl = usuarioRolService.consultaUnica(erl);
		if(erl ==null) throw new ServerException("Revise porque el responsable no esta registrado como usuario en el sistema");
		return erl.getUsuario();
	}
	*/
	
	/*
	 * 
	 */
	private PedidoVentaDineroDTO afectarSaldos(String expediente, String securityToken, ProcesoTransicionDTO transicion, 
			BigDecimal saldoDocumento, PedidoVentaDineroDTO dineroDocumentoInicial) throws ServerException{
		PedidoVentaDineroDTO dinero = dineroDocumentoInicial;
		PedidoVentaDTO pExpediente = pedidoService.consultaXId(expediente);
		if(dinero==null) {
			dinero = dineroService.consultaPorDocumento(expediente, pExpediente.getHistorico());
		}
		
		if(transicion.getAfectaSaldo()==null) return dinero;
		if(dinero==null) {
			throw new ServerException("Revise el documento " + pExpediente.getNombre() + " porque no tiene ningun registro de valores de saldos");
		}
		if(saldoDocumento==null) throw new ServerException("Revise porque el documento no tiene saldo");
		
		BigDecimal factor = BigDecimal.ONE;
		if(transicion.getAfectaSaldo().compareTo(ProcesoTransicionDTO.RESTANDO)==0) factor = factor.negate();
		
		System.out.format("\n[%s] Afectando saldos con factor %s", dinero.getDocumento(), factor.toString());
		if(transicion.getEstadoPartida()==null) { //Para los documentos iniciales
			if(transicion.getAfectaSaldo().compareTo(ProcesoTransicionDTO.SUMANDO)!=0) throw new ServerException("No es logico que inicie in proceso restando");
			dinero.setSaldo(dinero.getSaldo().add(saldoDocumento.multiply(factor)));
			System.out.format("\n" + transicion.getNombre() + " (" + pExpediente.getNombre()  + " : " +dinero.getValorTotal() + ")" + dinero.getSaldo() + " - " + saldoDocumento + " = " + dinero.getSaldo());
			if(dinero.getSaldo().compareTo(BigDecimal.ZERO) < 0){
				dinero.setSaldo(BigDecimal.ZERO);
				saldoDocumento = saldoDocumento.add(dinero.getSaldo().negate());
			}else{
				saldoDocumento = BigDecimal.ZERO;
			}
			if(dinero.getSaldo().compareTo(dinero.getValorTotal())>0) {
				throw new ServerException("Revise porque el saldo del documento es mayor al valor total.\nDocumento: " + pExpediente.getNombre()+ "\nSaldo: " + SoftureUtil.formatMoney(dinero.getSaldo()) + "\nTotal: " + SoftureUtil.formatMoney(dinero.getValorTotal()));
			}
			dineroService.update(dinero);// Se acaba de crear siempre va a ser tabla productiva
			return dinero;
		}
		dineroService.inactivar(dinero, securityToken);
		PedidoVentaDineroDTO nuevo = new PedidoVentaDineroDTO();
		nuevo.setSaldo(dinero.getSaldo().add(saldoDocumento.multiply(factor)));
		System.out.format("\n" + transicion.getNombre() + " (" + pExpediente.getNombre()  + " : " +dinero.getValorTotal() + ")" + dinero.getSaldo() + " - " + saldoDocumento + " = " + nuevo.getSaldo());
		if(nuevo.getSaldo().compareTo(BigDecimal.ZERO) < 0){
			nuevo.setSaldo(BigDecimal.ZERO);
			saldoDocumento = saldoDocumento.add(dinero.getSaldo().negate());
		}else{
			saldoDocumento = BigDecimal.ZERO;
		}
		nuevo.setDocumento(dinero.getDocumento());

		nuevo.setValorTotal(dinero.getValorTotal());
		if(nuevo.getSaldo().compareTo(nuevo.getValorTotal())>0) {
			throw new ServerException("Revise porque el saldo del documento es mayor al valor total.\nDocumento: " + pExpediente.getNombre()+ "\nSaldo: " + SoftureUtil.formatMoney(nuevo.getSaldo()) + "\nTotal: " + SoftureUtil.formatMoney(nuevo.getValorTotal()));
		}
		return dineroService.guardar(nuevo, securityToken);
	}
	
	public PedidoVentaDTO generarDocumentosTransicion(
			ProcesoTransicionDTO transicion, 
			PedidoVentaDTO documento, 		// Documento modificador que realiza la accion sobre el documetnto base
			PedidoVentaDTO expedienteDTO, 	// Documento base que se esta fafectando con el proceso
			String transaccion,				// Como reuso esto en los temporizadores automaticos entonces no viene transaccion
			String token,
			PedidoVentaCaracteristicaDTO vieneAutomatica)throws ServerException{ // Machetazo 
		List<PedidoVentaCaracteristicaDTO> camposNuevos = new ArrayList<PedidoVentaCaracteristicaDTO>();
		if (vieneAutomatica !=null) {
			camposNuevos.add(vieneAutomatica);
		}else {
			if(transicion.getPlantilla()==null) return null;
			String user = getUserFlex(token);
			List<PropiedadDTO>camposGenerar = propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.TRANSICION, transicion.getLlaveTabla(), Propiedades.GENERA_DOCUMENTO_CAMPO, user);
			if(camposGenerar==null) camposGenerar = new ArrayList<>();
			camposGenerar.addAll(propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.TRANSICION, transicion.getLlaveTabla(), Propiedades.GENERA_DOCUMENTO_FUNCION_SQL, user));
			if(camposGenerar==null || camposGenerar.isEmpty())	return null;
			//tengo que revisar cada propiedad y ver el campo que pide
			for (PropiedadDTO iPropiedadDTO : camposGenerar) {
				List<RelacionInternaDTO> relaciones = relacionService.relacionesPropiedad(iPropiedadDTO.getLlaveTabla());
				if(relaciones==null || relaciones.isEmpty()) {	//Este es un campo donde va principal
					PedidoVentaCaracteristicaDTO campoPrincipal = copiar( null ,iPropiedadDTO.getValor());
					if(documento!=null) {
						campoPrincipal.setValorOpcion(documento.getLlaveTabla());
						if(documento.getDinero()!=null)campoPrincipal.setValorNumero(documento.getDinero().getValorTotal());//Importante para que coja valor porque va a consultar po BD y no tiene
						campoPrincipal.setPrincipal(documento);
					}else {
						campoPrincipal.setValorOpcion(expedienteDTO.getLlaveTabla());
						if(expedienteDTO.getDinero()!=null)campoPrincipal.setValorNumero(expedienteDTO.getDinero().getValorTotal());//Importante para que coja valor porque va a consultar po BD y no tiene
						campoPrincipal.setPrincipal(expedienteDTO);
					}
					camposNuevos.add(campoPrincipal);
				}else {
					if(iPropiedadDTO.getKey().compareTo(Propiedades.GENERA_DOCUMENTO_CAMPO)==0) {
						//Este campo debe sumarse
						for (RelacionInternaDTO iRelacion : relaciones) {
							if(documento!=null && iRelacion.getPlantilla().compareTo(documento.getPlantilla())==0) {
								camposNuevos.add(copiar( pedidoService.obtenerValor(documento.getCaracteristicas(), iRelacion.getCampo()), iPropiedadDTO.getValor()));
							} else {
								if(expedienteDTO!=null && expedienteDTO.getPlantilla() != null && iRelacion.getPlantilla().compareTo(expedienteDTO.getPlantilla())==0) {
									// Solo consulto el documento cuando en realidad lo necesito, en general no veien las caracteristicas
									if(expedienteDTO.getCaracteristicas()==null) expedienteDTO.setCaracteristicas(pedidoVentaCaracteristicaService.listar2Documento(expedienteDTO.getLlaveTabla(), expedienteDTO.getHistorico()));
									camposNuevos.add(copiar( pedidoService.obtenerValor(expedienteDTO.getCaracteristicas(), iRelacion.getCampo()), iPropiedadDTO.getValor()));
								}
							}
						}	
					}else {
						PedidoVentaCaracteristicaDTO campoGenerado = pedidoVentaCaracteristicaService.consultarSQLCampoGenerarDocumento(iPropiedadDTO.getLlaveTabla(), (expedienteDTO!=null)?expedienteDTO.getLlaveTabla():null, (documento!=null)?documento.getLlaveTabla():null);
						camposNuevos.add(copiar(campoGenerado, relaciones.get(0).getCampo()));
					}
				}
			}
		}
		
		if(!camposNuevos.isEmpty()) {
			PedidoVentaDTO nuevo = new PedidoVentaDTO();
			nuevo.setCaracteristicas(new ArrayList<PedidoVentaCaracteristicaDTO>());
			nuevo.setPlantilla(transicion.getPlantilla());
			for (PedidoVentaCaracteristicaDTO iCampoCopiar : camposNuevos) {
				nuevo.getCaracteristicas().add(copiar(iCampoCopiar, iCampoCopiar.getCampo()));
			}
			nuevo.setLlaveTabla(null);
			nuevo.setTransaccion(transaccion);
			return pedidoService.guardar(nuevo, token);
		}else {
			return null;
		}
		
	}
	
	
	private PedidoVentaCaracteristicaDTO copiar(PedidoVentaCaracteristicaDTO actual, String campoId) {
		PedidoVentaCaracteristicaDTO nueva = new PedidoVentaCaracteristicaDTO();
		nueva.setCampo(campoId);
		if(actual!=null) {
			nueva.setValorAuxiliar(actual.getValorAuxiliar());
			nueva.setValorFecha(actual.getValorFecha());
			nueva.setValorNumero(actual.getValorNumero());
			nueva.setValorOpcion(actual.getValorOpcion());
			nueva.setValorText(actual.getValorText());
			nueva.setExpedientes(actual.getExpedientes());
		}
		return nueva;
	}
	
// END region aditionalMethods

}