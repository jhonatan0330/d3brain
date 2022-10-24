package com.softure.logisticpymes.services.adapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.domain.dto.CuentaDTO;
import com.softure.logisticpymes.domain.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.domain.dto.DocumentoPlantillaDTO;
import com.softure.logisticpymes.domain.dto.DocumentoRelacionExpedienteDTO;
import com.softure.logisticpymes.domain.dto.DocumentoRelacionGestorDTO;
import com.softure.logisticpymes.domain.dto.MovimientoDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaDineroDTO;
import com.softure.logisticpymes.domain.dto.ProcesoTransicionDTO;
import com.softure.logisticpymes.domain.dto.PropiedadDTO;
import com.softure.logisticpymes.domain.dto.PropiedadValorDefinidoDTO;
import com.softure.logisticpymes.domain.dto.RelacionInternaDTO;
import com.softure.logisticpymes.domain.dto.TurnoDTO;
import com.softure.logisticpymes.domain.filter.CuentaFilterDTO;
import com.softure.logisticpymes.domain.filter.DocumentoRelacionExpedienteFilterDTO;
import com.softure.logisticpymes.domain.filter.DocumentoRelacionGestorFilterDTO;
import com.softure.logisticpymes.domain.filter.MovimientoFilterDTO;
import com.softure.logisticpymes.domain.filter.PedidoVentaCaracteristicaFilterDTO;
import com.softure.logisticpymes.domain.filter.PedidoVentaFilterDTO;
import com.softure.logisticpymes.domain.filter.ProcesoTransicionFilterDTO;
import com.softure.logisticpymes.services.CuentaSvc;
import com.softure.logisticpymes.services.DocumentoPlantillaCaracteristicaSvc;
import com.softure.logisticpymes.services.DocumentoPlantillaSvc;
import com.softure.logisticpymes.services.DocumentoRelacionExpedienteSvc;
import com.softure.logisticpymes.services.DocumentoRelacionGestorSvc;
import com.softure.logisticpymes.services.MovimientoSvc;
import com.softure.logisticpymes.services.PedidoVentaDineroSvc;
import com.softure.logisticpymes.services.ProcesoTransicionSvc;
import com.softure.logisticpymes.services.PropiedadSvc;
import com.softure.logisticpymes.services.RelacionInternaSvc;
import com.softure.logisticpymes.services.TurnoSvc;
import com.softure.logisticpymes.services.refactor.CallListDocumentWithFilters;
import com.softure.logisticpymes.services.refactor.CallManageTransition;
import com.softure.logisticpymes.services.refactor.CallCRUDDocument;
import com.softure.logisticpymes.services.refactor.CallUpdateDocumentAutomatic;
import com.softure.logisticpymes.services.PedidoVentaCaracteristicaSvc;
import com.softure.logisticpymes.services.PedidoVentaSvc;
import com.softure.logisticpymes.services.ProcesoEstadoSvc;

@Component
public class TipoProceso {
	
	@Autowired private CuentaSvc cuentaService;
	@Autowired private PedidoVentaSvc pedidoService;
	@Autowired private CallCRUDDocument saveUpdateInactivateDocumentFunction;
	@Autowired private CallListDocumentWithFilters listDocumentWithFiltersFunction;
	@Autowired private PedidoVentaCaracteristicaSvc campoService;
	@Autowired private DocumentoPlantillaSvc plantillaService;
	@Autowired private DocumentoPlantillaCaracteristicaSvc caracteristicaService;
	@Autowired private DocumentoRelacionExpedienteSvc relacionExpedienteService;
	@Autowired private DocumentoRelacionGestorSvc relacionGestorService;
	@Autowired private MovimientoSvc movimientoService;
	@Autowired private ProcesoEstadoSvc estadoService;
	@Autowired private ProcesoTransicionSvc expedienteTransicionService;
	@Autowired private CallManageTransition manageTransitionFunction;
	@Autowired private CallUpdateDocumentAutomatic updateDocumentFunction;
	@Autowired private PropiedadSvc propiedadService;
	@Autowired private PedidoVentaDineroSvc dineroService;
	@Autowired private RelacionInternaSvc relacionService;
	@Autowired private TurnoSvc turnoService;
	
	@Autowired private AuxiliarProcesoBodega tipoBodega;

	public void cargarConsultaCampo(PedidoVentaCaracteristicaDTO pCampo) throws ServerException {
		if(pCampo.getValorOpcion()!=null) pCampo.setPrincipal(pedidoService.consultaXId(pCampo.getValorOpcion()));//Consulto el Id por proceso
	}

	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException{
		String campoHeredado1 = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.CAMPO_HEREDADO_1);
		if(campoHeredado1.isEmpty()){//Los heredados trabajan solos
			System.out.format("\n[%s - %s] Validando.....", pCampo.getCampoDTO().getPlantillaNombre(), pCampo.getCampoDTO().getNombre());
			String multiple = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.MULTIPLE);
			autosave(multiple, pCampo, token);
			if(!multiple.isEmpty()){
				validarMultiple(pCampo, token);
			}else{
				//DEsde las automaticas vienen un listado pero si es unico entonces debo agregarlo
				if(pCampo.getValorOpcion()==null && pCampo.getExpedientes()!=null && pCampo.getExpedientes().size()==1) pCampo.setValorOpcion( pCampo.getExpedientes().get(0).getLlaveTabla() );
				//Valido obligatoriedad
				if(Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL)==null && pCampo.getValorOpcion()==null) 
					throw new ServerException("Es necesario registrar el campo " + pCampo.getCampoDTO().getNombre());
				
				String bodega = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.BODEGA_FIJA);
				if(!bodega.isEmpty()) tipoBodega.validarPrepararCampo(pCampo, bodega);
				
				//Valido que el documento este activo y actualizo algunos valores
				if(pCampo.getValorOpcion()!=null){
					if(Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.BODEGA_MOVIMIENTO)!=null)
						tipoBodega.consultarBodegaDesdeDocumento(pCampo);
					loadActualOptionToDocumentList(pCampo);
					
					if(Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.CUENTA_MOVIMIENTO)!=null) {
						CuentaFilterDTO cajaFilter = new CuentaFilterDTO();
						cajaFilter.setDocumento(pCampo.getValorOpcion());
						CuentaDTO caja = cuentaService.consultaUnica(cajaFilter);
						if(caja==null) {
							PedidoVentaDTO cuentaDocumento = pedidoService.consultaXId(pCampo.getValorOpcion());
							PropiedadDTO propiedadCuenta = propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.PLANTILLA, cuentaDocumento.getPlantilla(), Propiedades.PLANTILLA_TIPO_CUENTA,  null);
							if(propiedadCuenta==null) {
								DocumentoPlantillaDTO plantillaError = plantillaService.consultaXId(cuentaDocumento.getPlantilla());
								throw new ServerException("El documento " + cuentaDocumento.getNombre() + " es de la plantilla "+ plantillaError.getNombre() + " y esta plantilla no tiene propiedad configurada la propiedad cuenta que le permite manejar un seguimiento a los movimientos");
							}else {
								caja = cuentaService.crearCuenta(cuentaDocumento, token);
								pCampo.setValorAuxiliar(caja.getLlaveTabla());
							}
						}else {
							if(caja.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0) throw new ServerException("La caja no esta activa");
							pCampo.setValorAuxiliar(caja.getLlaveTabla());
						}
					}
				}
			}
		} else {
			System.out.format("\n[%s - %s]  Campo heredado = %s, No se valida", pCampo.getCampoDTO().getPlantillaNombre(), pCampo.getCampoDTO().getNombre(), campoHeredado1);
		}
	}
	
	private void autosave(String multiple, PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		//Solo para los auload save
		if(pCampo.getValorOpcion()==null) {
			if(Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.AUTOLOAD_SAVE)!=null) {

				if(!multiple.isEmpty()){
					PropiedadDTO funcionConsulta = Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PROCESO_FUNCION_SQL);
					if(funcionConsulta ==null) throw new ServerException("Se debe definir la funcion para obtener los datos del autosave");
					pCampo.setExpedientes(
							consultarFuncionDocumentos(pCampo.getCampoDTO(), pCampo.getCampoDTO(), pCampo.getDependientes(), null, funcionConsulta, null, token)
							);
					if(pCampo.getModificado() 
							&& Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL)==null
							&& (pCampo.getExpedientes()==null || pCampo.getExpedientes().isEmpty()))
						throw new ServerException("Es necesario registrar el campo " + pCampo.getCampoDTO().getNombre());
				}else {
					PedidoVentaCaracteristicaFilterDTO filter = toFilter(pCampo, token);
					PedidoVentaCaracteristicaFilterDTO documentosFuncion = consultarDatosBase(filter);
					if(documentosFuncion.getCampoDTO().getDocumentos()!=null 
							&& !documentosFuncion.getCampoDTO().getDocumentos().isEmpty()) {
						pCampo.setValorOpcion(documentosFuncion.getCampoDTO().getDocumentos().get(0).getLlaveTabla());
					} else {
						if(pCampo.getModificado() 
								&& Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL)==null)
							throw new ServerException("Es necesario registrar el campo " + pCampo.getCampoDTO().getNombre());
					}
				}

			}
		}
	}

	private PedidoVentaCaracteristicaFilterDTO toFilter(PedidoVentaCaracteristicaDTO pCampo, String token) {
		PedidoVentaCaracteristicaFilterDTO filter = new PedidoVentaCaracteristicaFilterDTO();
		filter.setCampo(pCampo.getCampo());
		filter.setCampoDTO(pCampo.getCampoDTO());
		filter.setDependientes(pCampo.getDependientes());
		filter.setDocumento(pCampo.getDocumento());
		filter.setEstado(pCampo.getEstado());
		filter.setExpedientes(pCampo.getExpedientes());
		filter.setLlaveTabla(pCampo.getLlaveTabla());
		filter.setSecurityToken(token);
		filter.setValorAuxiliar(pCampo.getValorAuxiliar());
		filter.setValorOpcion(pCampo.getValorOpcion());
		//filter.setValorText(pCampo.getValorText());
		return filter;
	}

	private void loadActualOptionToDocumentList(PedidoVentaCaracteristicaDTO pCampo) throws ServerException {
		if(pCampo.getValorOpcion()==null) return;
		PedidoVentaDTO vActual = pedidoService.consultaXIdConDinero(pCampo.getValorOpcion());
		if(vActual==null) throw new ServerException("El documento no existe");
		//if(vActual.getEstado()!=null && vActual.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO)==0) throw new ServerException("El documento no se encuentra activo");			
		pCampo.setValorText((vActual.getDescripcion()==null)?vActual.getNombre():vActual.getDescripcion());
		if(vActual.getDinero()!=null) {
			String campoValor = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.PROCESO_VALOR);
			if(!campoValor.isEmpty() && campoValor.compareTo("1")==0) {
				pCampo.setValorNumero(vActual.getDinero().getValorTotal());
			}else {
				pCampo.setValorNumero(vActual.getDinero().getSaldo());
			}
		}
		vActual.setEstado(null);// no activo para que lo procese gestionar
		pCampo.setExpedientes(new ArrayList<PedidoVentaDTO>());
		pCampo.getExpedientes().add(vActual);
	}
	
	private void validarMultiple(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException{
		if(pCampo.getExpedientes()==null) pCampo.setExpedientes(new ArrayList<PedidoVentaDTO>());
		//Valido obligatoriedad
		if(pCampo.getModificado() 
				&& Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL)==null 
				&& pCampo.getExpedientes().isEmpty() 
				&& Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.AUTOLOAD_SAVE)==null) 
			throw new ServerException("Es necesario registrar el campo " + pCampo.getCampoDTO().getNombre());
		
		List<PedidoVentaDTO> procesosActuales = null;
		//Consulto los procesos que estan en BD
		if(pCampo.getDocumento()!= null) procesosActuales = listDocumentWithFiltersFunction.listarExpedientesPertenecenCampo(pCampo.getLlaveTabla(), token, null);
		if(procesosActuales==null) procesosActuales = new ArrayList<PedidoVentaDTO>();
		//En caso que sea modificacion comparo que proceso estan retirandose, cambiando
		if(pCampo.getModificado()){
			//Retiro de los actuales los que volvieron a enviar
			for (PedidoVentaDTO procesoDTO : pCampo.getExpedientes()) {
				procesoDTO.setEstado(null);
				for (PedidoVentaDTO procesoActivo : procesosActuales) {
					if(procesoActivo.getLlaveTabla().compareTo(procesoDTO.getLlaveTabla())==0){
						procesosActuales.remove(procesoActivo);
						procesoDTO.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
						break;
					}
				}
			}
			//Coloco estado de inactivo a los que no enviaron para borrarlos
			for (PedidoVentaDTO procesoInactivar : procesosActuales) {
				procesoInactivar.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
				pCampo.getExpedientes().add(procesoInactivar);
			}
		}else{
			pCampo.setExpedientes(procesosActuales);
		}
		
		pCampo.setValorOpcion(null);
		String campoValor = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.PROCESO_VALOR);
		if(!campoValor.isEmpty()) {
			//Quite esto pero para que lo tenia antes 
			//Por aqui no calculo con el dinero sino con el campo
			//Esto es para validar que los valores sean los que estan en la base de datos. Problema de concurrencia
			//Alguien cambia un valor de un expediente de un multiple y el valor no va a ser el mimsmo en los reportes
			for (PedidoVentaDTO expediente : pCampo.getExpedientes()) {
				if(expediente.getEstado()==null || expediente.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO)!=0){
					PedidoVentaDineroDTO valorActual = dineroService.consultaPorDocumento(expediente.getLlaveTabla(), expediente.getHistorico());
					if(valorActual==null) {
						if(expediente.getDinero()!=null) throw new ServerException("Revise porque el expediente tiene valor y en la base de datos no tiene. \nExpediente: " + expediente.getNombre());
					}else {
						if(expediente.getDinero()==null) throw new ServerException("Revise porque el expediente NO tiene valor y en la base de datos SI tiene. \nExpediente: " + expediente.getNombre() + "\nValor actual:" + valorActual.getValorTotal());
						if(valorActual.getValorTotal().compareTo(expediente.getDinero().getValorTotal())!=0) throw new ServerException("Revise porque los valores son diferentes. \nExpediente: " + expediente.getNombre() + "\nValor actual:" + valorActual.getValorTotal() + "\nValor enviado:" + expediente.getDinero().getValorTotal()+ ".\nRecomendacion actualice el documento posiblemente fue modificado.");
						if(valorActual.getSaldo().compareTo(expediente.getDinero().getSaldo())!=0) throw new ServerException("Revise porque los valores de los SALDOS son diferentes. \nExpediente: " + expediente.getNombre() + "\nSaldo actual:" + valorActual.getSaldo() + "\nValor enviado:" + expediente.getDinero().getSaldo() + ".\nRecomendacion actualice el documento posiblemente fue modificado.");
					}
				}
			}
			
		}
		PedidoVentaCaracteristicaFilterDTO calculado = calcularValoresTotalesCampo(toFilter(pCampo, token), campoValor);
		pCampo.setValorText(calculado.getValorText());
		pCampo.setValorNumero(calculado.getValorNumeroMax());
	}
	
	public PedidoVentaCaracteristicaDTO guardarCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException{
		String campoHeredado1 = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.CAMPO_HEREDADO_1);
		boolean modificacion = false;
		if(campoHeredado1.isEmpty()){
			PedidoVentaCaracteristicaDTO bd = campoService.buscarActivo(pCampo, pCampo.getPrincipal().getHistorico());
			String multiple = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.MULTIPLE);
			if(multiple.isEmpty()){
				if(bd!=null){
					bd.setCampoDTO(pCampo.getCampoDTO());
					if(pCampo.getValorOpcion()==null){
						bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
						if(bd.getLlaveTabla()!=null) {
							bd.setPrincipal(pCampo.getPrincipal());
							campoService.inactivar(bd, token);
						}
						return inactivar(bd, null, token);//Se inactiva el anterior, toca revisar el inactivar
					}else{
						if(bd.getValorOpcion()!=null && pCampo.getValorOpcion().compareTo(bd.getValorOpcion())==0){
							if(Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.BODEGA_MOVIMIENTO)!=null)
								tipoBodega.aplicarMovimientosBodega(pCampo, token);
							return pCampo;
						}else{
							bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
							if(bd.getLlaveTabla()!=null) {
								bd.setPrincipal(pCampo.getPrincipal());
								campoService.inactivar(bd, token);
							}
							inactivar(bd, null, token);//comentario anterior
							modificacion = true;
						}
					}
				}
				if(pCampo.getValorOpcion()==null){
					cerrarCaja(pCampo, token);
					return pCampo;
				}else{
					System.out.format("\n\n[%s (%s) - %s] START Guardando en bd %s ( %s )", pCampo.getCampoDTO().getPlantillaNombre(), pCampo.getPrincipal().getNombre(), pCampo.getCampoDTO().getNombre(), pCampo.getValorText(), pCampo.getValorOpcion());
					bd = campoService.guardar(pCampo, token);
					pCampo.setLlaveTabla(bd.getLlaveTabla());
					administrarExpedientes(pCampo, pCampo.getPrincipal(), modificacion, token);
					if(Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.CUENTA_ABRIR_CAJA)!=null){
						TurnoDTO turno = new TurnoDTO();
						//CuentaPermisoUsuarioDTO cuenta = cuentaPermisoUsuarioService.consultaXId(pCampo.getValorOpcion());
						turno.setCuenta(pCampo.getValorOpcion());
						turno.setUsuario(campoService.getUserFlex(token));
						turno.setDocumento(pCampo.getDocumento());
						turno = turnoService.iniciarTurno(turno, token);
					}
					relacionExternaDocumentos(pCampo, token);
					cerrarCaja(pCampo, token);
					generarPagos(pCampo, token);
					if(Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.BODEGA_MOVIMIENTO)!=null)
						tipoBodega.aplicarMovimientosBodega(pCampo, token);
					System.out.format("\n[%s (%s) - %s] END.. Guardando en bd %s ( %s )", pCampo.getCampoDTO().getPlantillaNombre(), pCampo.getPrincipal().getNombre(), pCampo.getCampoDTO().getNombre(), pCampo.getValorText(), pCampo.getValorOpcion());
					// throw new ServerException("Probando");
				}
			}else{
				System.out.format("\n[%s (%s) - %s] Campo Multiple] = %s", pCampo.getCampoDTO().getPlantillaNombre(), pCampo.getPrincipal().getNombre(), pCampo.getCampoDTO().getNombre(), multiple);
				if(bd==null){
					bd = campoService.guardar(pCampo, token);
					pCampo.setLlaveTabla(bd.getLlaveTabla());
				}else{
					bd.setValorNumero(pCampo.getValorNumero());
					bd.setValorText(pCampo.getValorText());
					bd = campoService.actualizar(bd, token);
					modificacion = true;
				}
				relacionarExpedientes(pCampo, token);
				administrarExpedientes(pCampo, pCampo.getPrincipal(), modificacion, token);
				relacionExternaDocumentos(pCampo, token);
			}
		}
		return pCampo;
	}
	
	
	public PedidoVentaCaracteristicaDTO inactivar(PedidoVentaCaracteristicaDTO pCampo, PedidoVentaDTO documentoModificadorDTO, String token) throws ServerException {
		if(pCampo.getCampoDTO().getPropiedades()==null || pCampo.getCampoDTO().getPropiedades().isEmpty()) 
			pCampo.setCampoDTO( caracteristicaService.cargarComplementos(pCampo.getCampoDTO(), token) );
		//if(pCampo.getLlaveTabla()!=null) campoService.inactivar(pCampo);
		// anularMovimiento(pCampo); //OJO esto qe como estoy haciendo para anular movimeintos
		System.out.format("\n%s Inactivando", pCampo.getCampoDTO().getNombre());
		if (pCampo.getExpedientes()!=null && pCampo.getExpedientes().size()!=0){
			for (PedidoVentaDTO procesoInactivar : pCampo.getExpedientes()) {
				procesoInactivar.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
			}
			relacionarExpedientes(pCampo, token);
		}else {
			if(documentoModificadorDTO!=null)loadActualOptionToDocumentList(pCampo);
		}
		return administrarExpedientes(pCampo, documentoModificadorDTO, true, token);
	}
	
	private void relacionarExpedientes(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException{
		if(pCampo.getExpedientes()==null || pCampo.getExpedientes().isEmpty()) return;
		for (PedidoVentaDTO procesoDTO : pCampo.getExpedientes()) {
			if(procesoDTO.getEstado()!=null && procesoDTO.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO)==0){
				retirarExpedienteDocumento(pCampo, procesoDTO, token);
			}else{
				relacionarExpedienteDocumento(pCampo, procesoDTO, token);
			}
		}
	}

	private void retirarExpedienteDocumento(PedidoVentaCaracteristicaDTO pCampo, PedidoVentaDTO procesoDTO, String token)throws ServerException {
		//Si es inactivo, busco la relacion del expediente y el campo
		DocumentoRelacionExpedienteFilterDTO filtroExpFilter = new DocumentoRelacionExpedienteFilterDTO();
		filtroExpFilter.setCampoMaestro(pCampo.getLlaveTabla());
		filtroExpFilter.setExpedienteDetalle(procesoDTO.getLlaveTabla());
		filtroExpFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		DocumentoRelacionExpedienteDTO filtroExp = relacionExpedienteService.consultaUnica(filtroExpFilter);
		if(filtroExp!=null){
			filtroExp.setTransaccionInactivo(pCampo.getTransaccionRegistro());
			relacionExpedienteService.inactivar(filtroExp, token);
		}
	}

	private void relacionarExpedienteDocumento(PedidoVentaCaracteristicaDTO pCampo, PedidoVentaDTO procesoDTO, String token)throws ServerException {
		//Creo una relacion entre el campo y los pedidos detalles, primero reviso si existe
		DocumentoRelacionExpedienteFilterDTO docExpedienteFilter = new DocumentoRelacionExpedienteFilterDTO();
		docExpedienteFilter.setCampoMaestro(pCampo.getLlaveTabla());
		docExpedienteFilter.setExpedienteDetalle(procesoDTO.getLlaveTabla());
		docExpedienteFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		DocumentoRelacionExpedienteDTO docExpediente = relacionExpedienteService.consultaUnica(docExpedienteFilter);
		if(docExpediente==null){
			docExpediente = new DocumentoRelacionExpedienteDTO();
			docExpediente.setCampoMaestro(pCampo.getLlaveTabla());
			docExpediente.setExpedienteDetalle(procesoDTO.getLlaveTabla());
			if(procesoDTO.getDinero()!=null)docExpediente.setValor(procesoDTO.getDinero().getSaldo());
			docExpediente.setTransaccionRegistro(pCampo.getTransaccionRegistro());
			docExpediente = relacionExpedienteService.guardar(docExpediente, token);
		}
	}

	private PedidoVentaCaracteristicaDTO administrarExpedientes(
			PedidoVentaCaracteristicaDTO pCampo, 
			PedidoVentaDTO updaterDTO, 
			boolean modificacion,
			String token) throws ServerException{
		if(pCampo.getExpedientes()!=null && !pCampo.getExpedientes().isEmpty()){
			List<PedidoVentaDTO> activos = new ArrayList<PedidoVentaDTO>();
			HashMap<String, String> hmap = new HashMap<String, String>();
			String maquinaEstados;
			for (PedidoVentaDTO procesoDTO : pCampo.getExpedientes()) {
				System.out.format("\n[%s (%s) - %s] INICIO Procesar expediente %s ( %s )", pCampo.getCampoDTO().getPlantillaNombre(), pCampo.getPrincipal().getNombre(), pCampo.getCampoDTO().getNombre(), procesoDTO.getNombre(), procesoDTO.getLlaveTabla());
				if(!hmap.containsKey(procesoDTO.getPlantilla())){
					hmap.put(procesoDTO.getPlantilla(), expedienteTransicionService.consultarProceso(procesoDTO.getPlantilla()));
				}
				maquinaEstados= hmap.get(procesoDTO.getPlantilla());
				if(procesoDTO.getEstado()== null){
					//Creo una relacion entre el campo y los pedidos detalles, primero reviso si existe
					if(maquinaEstados!=null){
						//Esto lo tuve que hacer en logimax para un cilo que se generaba de 
						modificacion = modificarDocumentoPrincipal(pCampo, procesoDTO, token);
						if( Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PROCESO_GESTIONAR_ESTADOS)!=null){
							System.out.format("\n[%s (%s) - %s] Maquina de estados BPM ( %s ) plantilla  ( %s )", pCampo.getCampoDTO().getPlantillaNombre(), pCampo.getPrincipal().getNombre(), pCampo.getCampoDTO().getNombre(), procesoDTO.getNombre(), maquinaEstados);
							System.out.format("\n[%s (%s) - %s] Calculando caminos BPM", pCampo.getCampoDTO().getPlantillaNombre(), pCampo.getPrincipal().getNombre(), pCampo.getCampoDTO().getNombre());
							List<String> caminosGestionar = getCaminos(pCampo);
							List<String> documentosGestionados = new ArrayList<String>();
							documentosGestionados.add(pCampo.getDocumento());
							BigDecimal saldoDoc = null;
							if(procesoDTO.getDinero()!=null) saldoDoc = procesoDTO.getDinero().getSaldo();
							gestionarExpedienteDependientes(procesoDTO, updaterDTO,
									token, saldoDoc, new ArrayList<String>(), caminosGestionar, documentosGestionados,
									pCampo.getTransaccionRegistro() ,!modificacion);
						}else{
							//Esto algun día lo voy a unir con el modificar
							if(Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PROCESO_DIVISION)!=null) {
								System.out.format("\n[%s (%s) - %s] Dividir documento...... %s", pCampo.getCampoDTO().getPlantillaNombre(), pCampo.getPrincipal().getNombre(), pCampo.getCampoDTO().getNombre(), procesoDTO.getNombre());
								dividirDocumento(procesoDTO, updaterDTO,  token, pCampo.getTransaccionRegistro());
								//Lo coloco aqui porque se relacionaba todo
								relacionarGestor(procesoDTO, updaterDTO, "Dividir documento", token);
							}
						}
					}else {
						if( Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PROCESO_GESTIONAR_ESTADOS)!=null){
							String usuarioToken = (token==null)?null:propiedadService.getUserFlex(token);
							PropiedadDTO prop = propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.PLANTILLA, 
									procesoDTO.getPlantilla(), Propiedades.PLANTILLA_ANULAR, usuarioToken);
							if(prop!=null && updaterDTO.getPlantilla().compareTo(prop.getValor())==0) {
								procesoDTO.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
								saveUpdateInactivateDocumentFunction.inactivateDocumentWithProcess(procesoDTO, updaterDTO, token);
								relacionarGestor(procesoDTO, updaterDTO, "ANULAR DOCUMENTO", token);
							}
						}
					}
					if( Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PROCESO_GESTIONAR_ESTADOS)==null){
						if(Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PROCESO_INCLUIR_TRAZA_PRINCIPAL)!=null) {
							System.out.format("\n[%s (%s) - %s] Incluir traza..... %s", pCampo.getCampoDTO().getPlantillaNombre(), pCampo.getPrincipal().getNombre(), pCampo.getCampoDTO().getNombre(), procesoDTO.getNombre());
							relacionarGestor(procesoDTO, updaterDTO, null, token);
						}
					}
					
					activos.add(procesoDTO);
				}else{
					if(procesoDTO.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO)==0){
						//Si tenia permisos, inactivo esos permisos
						if(maquinaEstados!=null){
							//BigDecimal saldoDoc = null;
							//if(updaterDTO.getDinero()!=null) saldoDoc = updaterDTO.getDinero().getSaldo();
							List<String> caminosGestionar = getCaminos(pCampo);
							revertirExpedienteDependiente(procesoDTO, updaterDTO, token,  caminosGestionar, true);
						}
					}else{
						activos.add(procesoDTO);
					}
				}
				System.out.format("\n[%s (%s) - %s] FIN... Procesar expediente %s ( %s )", pCampo.getCampoDTO().getPlantillaNombre(), pCampo.getPrincipal().getNombre(), pCampo.getCampoDTO().getNombre(), procesoDTO.getNombre(), procesoDTO.getLlaveTabla());
			}
		}
		return pCampo;
	}

	private boolean modificarDocumentoPrincipal(PedidoVentaCaracteristicaDTO pCampo, PedidoVentaDTO procesoDTO, String token)throws ServerException {
		//Modificar campos de plantilla principal
		List<PropiedadDTO> modificarCampo = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(), Propiedades.MODIFICAR_CAMPO);
		if(modificarCampo==null || modificarCampo.isEmpty()) return false;
		System.out.format("\n%s (Modificando documento principal..... %s)", pCampo.getCampoDTO().getNombre(), procesoDTO.getNombre());
		campoService.validarDependientes(pCampo.getCampoDTO(), pCampo.getDependientes());
		updateDocumentFunction.executeFromBPM(pCampo, procesoDTO, token, modificarCampo);
		return true;
	}

	private List<String> getCaminos(PedidoVentaCaracteristicaDTO pCampo) {
		List<String> caminosGestionar = new ArrayList<String>();
		List<PropiedadDTO> caminos  = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(), Propiedades.PROCESO_GESTIONAR_ESTADOS);
		if(caminos!=null) {
			for (PropiedadDTO iCamino : caminos) {
				if(iCamino.getValor().compareTo("*")==0) {
					caminosGestionar.add("*");
					System.out.format(" Camino (*)");
				}else {
					caminosGestionar.add(iCamino.getValor() + ";");
					System.out.format(", Camino (%s)", iCamino.getValor());
				}
			}
		}
		return caminosGestionar;
	}

	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(PedidoVentaCaracteristicaFilterDTO pCampo) throws ServerException {		
		DocumentoPlantillaCaracteristicaDTO pBase = caracteristicaService.consultaUnicaConComplementos(pCampo.getCampo(), pCampo.getSecurityToken());
		String campoHeredado1 = Propiedades.obtenerValor(pBase, Propiedades.CAMPO_HEREDADO_1);
		String multiple = Propiedades.obtenerValor(pBase, Propiedades.MULTIPLE);
		String campoValor = Propiedades.obtenerValor(pBase, Propiedades.PROCESO_VALOR);//Principalmente para los formularios que tengan valor
		PropiedadDTO funcionConsulta = Propiedades.obtenerParametro(pBase, Propiedades.PROCESO_FUNCION_SQL);
		List<PropiedadDTO> codigoDepende = Propiedades.obtenerVariosParametro(pBase, Propiedades.DEPENDE);
		List<PedidoVentaDTO> resultados = null;
		if(multiple.isEmpty() && campoHeredado1.isEmpty()){//Consulto opciones de combo
			//Esto es de los tipo bodega
			String bodegaFija = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.BODEGA_FIJA); 
			if(!bodegaFija.isEmpty()) pCampo.setValorOpcion( tipoBodega.consultarBodegaBaseFija(bodegaFija) );
			//Movi esto porque simpre que tenga opcion va a consultar uno creo que tengo un problema con los que dependen o algo asi
			if(pCampo.getValorOpcion()!=null){//Si tiene valor opcion es porque ya esta seleccionado
				PedidoVentaDTO documentoActual = pedidoService.consultaXId(pCampo.getValorOpcion());
				if(documentoActual==null) throw new ServerException("Documento opcion no se encuentra por llave." + pBase.getNombre());
				resultados = new ArrayList<PedidoVentaDTO>();
				resultados.add(documentoActual);
				if(!campoValor.isEmpty()) {
					//Coloco valores
					listDocumentWithFiltersFunction.listadoCompleto(resultados, pCampo.getSecurityToken(), (campoValor.isEmpty())?null:campoValor);
					//Para las cuentas les lleno el valor aqui
					if(campoValor.compareTo("0")==0 && resultados.get(0)!=null &&resultados.get(0).getDinero()!=null)
						pCampo.setValorNumeroMax(resultados.get(0).getDinero().getValorTotal());
				}
					
			}else {
				PedidoVentaFilterDTO entityFilter = new PedidoVentaFilterDTO();
				
				entityFilter.setSecurityToken(pCampo.getSecurityToken());
				entityFilter.setFiltroParametro(pCampo.getFiltroParametro());//Coloco los filtros necesarios
				if(entityFilter.getFiltroParametro()!=null && entityFilter.getFiltroParametro().compareTo("*")==0)entityFilter.setFiltroParametro(null);
				entityFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
				
				if(funcionConsulta == null) {
					String documentoAuxiliar = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.PLANTILLA_AUXILIAR);
					if(!documentoAuxiliar.isEmpty()){
						if(pBase!=null){//Esto aplica para autoload de los productos con ocion de seleccion
							if(codigoDepende!=null){//Coloco las dependencias
								if(codigoDepende.get(0).getValor().compareTo(ConstantesGenerales.USUARIO)!=0 
										&&  Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.BODEGA_MOVIMIENTO).isEmpty()){
									//Valido que la cantidad de dependientes este correcta
									if(pCampo.getDependientes()==null || pCampo.getDependientes().isEmpty())throw new ServerException("Revise los dependientes.\n " + pCampo.getCampoDTO().getNombre());
									if(pCampo.getDependientes().size()!=codigoDepende.size()) throw new ServerException("El numero de dependientes no concuerda. Tipo Expediente" + codigoDepende.size());
									//Al parecer solo funciona para un dependiente
									entityFilter.setCaracteristicas(new ArrayList<PedidoVentaCaracteristicaDTO>());
									entityFilter.getCaracteristicas().add(colocarFiltroDocumentoAuxiliar(pCampo.getDependientes().get(0).getValorOpcion()));
									//pCampo.setValorOpcion(null);//Creo que con esto soluciono las modificaciones de documentos de dependencia
								}
							}else{
								if(pCampo.getDocumento()!=null) {//Para que coloque esto
									entityFilter.setCaracteristicas(new ArrayList<PedidoVentaCaracteristicaDTO>());
									entityFilter.getCaracteristicas().add(colocarFiltroDocumentoAuxiliar(pCampo.getDocumento()));	
								}
							}
						}
						entityFilter.setPlantilla(documentoAuxiliar);
					}
					resultados = listDocumentWithFiltersFunction.listarAvanzado(entityFilter);
				}else {
					resultados = consultarFuncionDocumentos(pBase, pCampo.getCampoDTO(), pCampo.getDependientes(), entityFilter, funcionConsulta, campoValor, pCampo.getSecurityToken());
				}
			}
			if(pBase!=null){
				pBase.setDocumentos(resultados);
				pCampo.setCampoDTO(pBase);
			}else{//Esto aplica para autoload de los productos con ocion de seleccion
				pCampo.getCampoDTO().setDocumentos(resultados);
			}
			if(pCampo.getCampoDTO().getDocumentos()==null)throw new ServerException("Comuniquese con el desarrollador los documentos resultado de la consulta completa no pueden ser nulos");
			return pCampo;
		}else{ 
			if(pCampo.getDocumento()==null) {//Si es multiple y es nuevo no consulte nada
				
				pCampo.setExpedientes(new ArrayList<PedidoVentaDTO>());
			}else {//Aqui solo van los documentos actuales
				if(campoValor.isEmpty() || campoValor=="1" || campoValor == "2") campoValor = null;
				resultados = listDocumentWithFiltersFunction.listarExpedientesPertenecenCampo(pCampo.getLlaveTabla(), pCampo.getSecurityToken(), campoValor);
				pCampo.setExpedientes(resultados);
				calcularValoresTotalesCampo(pCampo, campoValor);
			}
			return pCampo;
		}
	}
	
	private List<PedidoVentaDTO> consultarFuncionDocumentos(
			DocumentoPlantillaCaracteristicaDTO pBase, 
			DocumentoPlantillaCaracteristicaDTO campo, 
			List<PedidoVentaCaracteristicaDTO> dependientes,
			PedidoVentaFilterDTO entityFilter,
			PropiedadDTO funcionConsulta, 
			String campoValor,
			String token) throws ServerException{
		//En caso que sea funcion y tenga una dependencia va a aenviar ese valor como llave tabla
		List<PropiedadDTO> codigoDepende = Propiedades.obtenerVariosParametro(pBase, Propiedades.DEPENDE);
		if(entityFilter==null) entityFilter = new PedidoVentaFilterDTO(); // en tipo proceos autoload no sabia que filtrar
		if(codigoDepende!=null){//Coloco las dependencias
			campoService.validarDependientes(campo, dependientes);
			dependientes = campoService.ordenarAlfabeticaDepende(dependientes);
			if(dependientes.get(0).getValorOpcion()!=null)//Se me perdia la referencia y no se porque
				entityFilter.setLlaveTabla(new String(dependientes.get(0).getValorOpcion()));
			List<PedidoVentaCaracteristicaDTO> expedientesMultiples = new ArrayList<PedidoVentaCaracteristicaDTO>();
			for (PedidoVentaCaracteristicaDTO iDependiente : dependientes) {
				if(iDependiente.getValorOpcion()==null && iDependiente.getExpedientes()!=null) {
					//Esto aplica para los campos multiples
					for (PedidoVentaDTO iExpediente : iDependiente.getExpedientes()) {
						PedidoVentaCaracteristicaDTO pd = new PedidoVentaCaracteristicaDTO();
						pd.setValorOpcion(iExpediente.getLlaveTabla());
						expedientesMultiples.add(pd);
					}
				}
			}
			if(expedientesMultiples.size()!=0) dependientes.addAll(expedientesMultiples);
		}
		//entityFilter.setDescripcion(funcionConsulta.getLlaveTabla());
		List<PedidoVentaDTO> result = listDocumentWithFiltersFunction.listarExpedientesDisponiblesDocumentoFuncion(entityFilter, 
				funcionConsulta.getLlaveTabla(), dependientes);
		return listDocumentWithFiltersFunction.listadoCompleto( result, token, campoValor);
	}
	
	private void gestionarExpedienteDependientes(
			PedidoVentaDTO procesoDTO, 
			PedidoVentaDTO documento, 
			String securityToken, 
			BigDecimal saldoDocumento, 
			List<String> plantillasRevisadas, 
			List<String> caminosGestionables, 
			List<String> documentosGestionados,
			String transaccion,
			boolean primerLlamado) throws ServerException{
		if(caminosGestionables==null || caminosGestionables.isEmpty()) return;
		if(caminosGestionables.size()==1 && caminosGestionables.get(0).isEmpty()) return;
		if(procesoDTO==null) return;
		List<String> caminosValidados = validarCamino(caminosGestionables, procesoDTO.getPlantilla());
		if(caminosValidados.size()==0) return;
		PedidoVentaDTO expediente = pedidoService.consultaXId(procesoDTO.getLlaveTabla());
		if(expediente == null) throw new ServerException("No se identifico el expediente");
		
		System.out.format("\n[%s] Gestionando por accion en documento: %s", expediente.getNombre(), documento.getNombre());
		if(procesoDTO.getEstadoExpediente()!=null) {
			ProcesoTransicionDTO transicion = consultarTransicion(documento.getPlantilla(), procesoDTO.getEstadoExpediente(), null);
			if(expediente.getEstadoExpediente()==null) throw new ServerException("Revise el estado del expediente que no es NULO : " + expediente.getNombre());
			if(expediente.getEstadoExpediente().compareTo(procesoDTO.getEstadoExpediente())!=0) 
				throw new ServerException("Revise el expediente " + procesoDTO.getNombre() + " el cual tiene un estado desactualizado");
			//Manejo de los saldos de los procesos
			if(transicion!=null) {
				manageTransitionFunction.execute(transicion, expediente.getLlaveTabla(), documento, saldoDocumento, null, null, securityToken, transaccion, null);
				if(documentosGestionados==null) documentosGestionados = new ArrayList<String>();//Para evitar que se generen ciclos validando los mismos documentos
				documentosGestionados.add(expediente.getLlaveTabla());
				saveUpdateInactivateDocumentFunction.saveRole(expediente, securityToken);
			}else {
				if(primerLlamado) {
					String mensajeFault = "Revisa porque este documento no genera ninguna transicion, el campo lo solicita. ( " + procesoDTO.getNombre() + " )" ;
					if(procesoDTO.getDescripcion()!=null)mensajeFault = mensajeFault + procesoDTO.getDescripcion();
					mensajeFault = mensajeFault + " ( desde el estado : " + estadoService.consultaXId(procesoDTO.getEstadoExpediente()).getNombre() + ")"; 
					throw new ServerException( mensajeFault) ;
				}
			}
		}
		plantillasRevisadas.add(procesoDTO.getPlantilla());
		List<PedidoVentaCaracteristicaDTO> gestionables = campoService.listarGestionables(expediente.getLlaveTabla());
		if(gestionables!=null && !gestionables.isEmpty()) {
			System.out.format("\n[%s] Gestionando documentos que esten relacionados", expediente.getNombre(), documento.getNombre());
			for(PedidoVentaCaracteristicaDTO campo : gestionables){
				System.out.format("\n[] Relacion %s ( %s )", campo.getCampo(), campo.getValorText());
				List<DocumentoRelacionExpedienteDTO> expedientesAnidados = null;
				DocumentoRelacionExpedienteDTO relacionExpediente = new DocumentoRelacionExpedienteDTO();
				if(campo.getValorOpcion()==null){//En caso que sean multiples
					if(campo.getLlaveTabla().compareTo(Propiedades.CAMPO_HEREDADO_1)==0){
						//Consulto los campos que se relacionan y gestiono el estado de esos procesos
						expedientesAnidados = relacionExpedienteService.listarHeredados(campo.getValorAuxiliar(), campo.getValorText(), procesoDTO.getLlaveTabla(), documento.getPlantilla(), plantillasRevisadas);
					}else{
						//Esto creo que se podria optimizar algun día y solo ahce un llamado por todos
						DocumentoRelacionExpedienteFilterDTO relacionExpedienteFilter = new DocumentoRelacionExpedienteFilterDTO();
						relacionExpedienteFilter.setCampoMaestro(campo.getLlaveTabla());
						relacionExpedienteFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
						relacionExpedienteFilter.setPaginacionRegistroFinal(5000);//Esto es para poder listar todos los relacionados
						expedientesAnidados = relacionExpedienteService.listarConsulta(relacionExpedienteFilter);
					}
				}else{
					expedientesAnidados = new ArrayList<DocumentoRelacionExpedienteDTO>();
					relacionExpediente.setExpedienteDetalle(campo.getValorOpcion());
					relacionExpediente.setValor(saldoDocumento);
					expedientesAnidados.add( relacionExpediente );
				}
				if(expedientesAnidados!=null && !expedientesAnidados.isEmpty()){//Aqui cambie el calculo de los saldos y no se como cuadralos
					if(documentosGestionados==null) documentosGestionados = new ArrayList<String>();//Para evitar que se generen ciclos validando los mismos documentos
					boolean validadoPreviamente = false;
					String expedienteId = null;
					for(DocumentoRelacionExpedienteDTO iExpediente : expedientesAnidados){
						expedienteId = iExpediente.getExpedienteDetalle();
						for (String iValidado : documentosGestionados) {//Para evitar que se generen ciclos validando los mismos documentos
							if(iValidado.compareTo(expedienteId)==0) {
								validadoPreviamente= true;
								break;
							}
						}
						if(!validadoPreviamente) {
							PedidoVentaDTO expAnidado = pedidoService.consultaXId(expedienteId);
							System.out.format("\n[%s] INICIA Procesar documento anidado ( %s )", expediente.getNombre(), expAnidado.getNombre());
							documentosGestionados.add(expedienteId);
							gestionarExpedienteDependientes(expAnidado, documento, securityToken, iExpediente.getValor(), plantillasRevisadas, caminosValidados, documentosGestionados, transaccion, false);
							System.out.format("\n[%s] FIN Procesar documento anidado ( %s )", expediente.getNombre(), expAnidado.getNombre());
						}
					}
				}
			}
		}
		
	}

	private List<String> validarCamino(List<String> caminosGestionables, String plantilla)throws ServerException {
		List<String> caminosValidados = new ArrayList<String>();
		String codigoDocumento = plantillaService.consultaXId(plantilla).getCodigo();
		for (String camino : caminosGestionables) {
			if(camino.compareTo("*")==0) {
				caminosValidados.add(camino);
			}else {
				if((camino+";").startsWith(codigoDocumento + ";")) {
					caminosValidados.add(camino.replaceFirst(codigoDocumento + ";", ""));
				}
			}
		}
		return caminosValidados;
	}
	
	private void revertirExpedienteDependiente(
			PedidoVentaDTO procesoDTO, 
			PedidoVentaDTO documento, 
			String securityToken, 
			List<String> caminosGestionables, 
			boolean primerLlamado) throws ServerException{
		//Consulto la relacion que genero el cambio de estado
		if(procesoDTO==null || procesoDTO.getEstadoExpediente()==null) return;
		List<String> caminosValidados = validarCamino(caminosGestionables, procesoDTO.getPlantilla());
		if(caminosValidados.size()==0) return;
		DocumentoRelacionGestorFilterDTO filtroGestor = new DocumentoRelacionGestorFilterDTO();
		filtroGestor.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		filtroGestor.setEstadoFinal(procesoDTO.getEstadoExpediente());
		filtroGestor.setDocumentoModificador(documento.getLlaveTabla());
		filtroGestor.setDocumentoPrincipal(procesoDTO.getLlaveTabla());
		List<DocumentoRelacionGestorDTO> gestores = relacionGestorService.listarConsulta(filtroGestor);
		
		if(gestores==null || gestores.isEmpty()) {
			if(primerLlamado) {
				StringBuilder error = new StringBuilder("El documento ");
				error.append(procesoDTO.getNombre());
				error.append(" no tiene elementos gestores con el estado actual ");
				error.append(procesoDTO.getEstadoNombre());
				error.append(" que permitan revertir el proceso.");
				throw new ServerException(error.toString());
			}
		}else {
			DocumentoRelacionGestorDTO ultimoGestor = gestores.get(0); //El query trae desc, escojo el primero para que es el ultimo
			ProcesoTransicionDTO transicion = consultarTransicion(documento.getPlantilla(), ultimoGestor.getEstadoInicial(), procesoDTO.getEstadoExpediente()); 
			if(transicion==null) return;//throw new ServerException("Existen documentos sin transicion para gestionar." + procesoDTO.getNombre());
			//Realizo validaciones de documento con estado
			PedidoVentaDTO expediente = pedidoService.consultaXId(procesoDTO.getLlaveTabla());
			if(expediente == null) throw new ServerException("No se identifico el expediente");
			if(expediente.getEstadoExpediente()==null) throw new ServerException("Revise el estado del expediente que no es NULO : " + expediente.getNombre());
			if(transicion.getEstadoLLegada().compareTo(procesoDTO.getEstadoExpediente())!=0) throw new ServerException("Revise e estado del proceso que no es acorde a la transcision");
			if(expediente.getEstadoExpediente().compareTo(procesoDTO.getEstadoExpediente())!=0) 
				throw new ServerException("Revise el expediente " + procesoDTO.getNombre() + " el cual tiene un estado desactualizado");
			manageTransitionFunction.gestionarTransicionReversa(transicion, expediente.getLlaveTabla(), documento, securityToken);
			saveUpdateInactivateDocumentFunction.saveRole(expediente, securityToken);
			List<PedidoVentaCaracteristicaDTO> gestionables = campoService.listarGestionables(expediente.getLlaveTabla());
			for(PedidoVentaCaracteristicaDTO campo : gestionables){
				List<DocumentoRelacionExpedienteDTO> expedientesAnidados;
				if(campo.getValorOpcion()==null){//En caso que sean multiples
					DocumentoRelacionExpedienteFilterDTO relacionExpediente = new DocumentoRelacionExpedienteFilterDTO();
					relacionExpediente.setCampoMaestro(campo.getLlaveTabla());
					relacionExpediente.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
					expedientesAnidados = relacionExpedienteService.listarConsulta(relacionExpediente);
				}else{
					expedientesAnidados = new ArrayList<DocumentoRelacionExpedienteDTO>();
					DocumentoRelacionExpedienteDTO relacionExpediente = new DocumentoRelacionExpedienteDTO();
					relacionExpediente.setExpedienteDetalle(campo.getValorOpcion());
					expedientesAnidados.add( relacionExpediente );
				}
				if(expedientesAnidados!=null && !expedientesAnidados.isEmpty()){
					//BigDecimal saldoDoc = saldoDocumento;
					for(DocumentoRelacionExpedienteDTO iExpediente : expedientesAnidados){
						PedidoVentaDTO expAnidado = pedidoService.consultaXId(iExpediente.getExpedienteDetalle());
						revertirExpedienteDependiente(expAnidado, documento, securityToken, caminosValidados, false);
					}
					//if(saldoDoc!=null && (saldoDoc.compareTo(documento.getDinero().getSaldo())!=0 && saldoDoc.compareTo(BigDecimal.ZERO)<0)) 
					//	throw new ServerException("Revise el proceso porque el saldo no puede ser negativo");//. (" + expediente.getNombre() + ")" + SoftureUtil.formatMoney(dinero.getValorTotal()) + " + (" + documento.getNombre() + ") " + SoftureUtil.formatMoney(saldoDocumento) + " = " + SoftureUtil.formatMoney(nuevo.getSaldo()));
				}
			}		
		}
		
		return;
	}
	
	
	private ProcesoTransicionDTO consultarTransicion(String plantilla, String estadoPartida, String estadoLlegada) throws ServerException{
		//Consulto la transicion del documento
		ProcesoTransicionFilterDTO transicion = new ProcesoTransicionFilterDTO();
		transicion.setPlantilla(plantilla);
		transicion.setEstadoPartida(estadoPartida);
		transicion.setEstadoLLegada(estadoLlegada);
		transicion.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		List<ProcesoTransicionDTO> transiciones = expedienteTransicionService.listarConsulta(transicion);
		if (transiciones.size()==0) return null;//throw new ServerException("Existen documentos sin transicion para gestionar." + procesoDTO.getNombre());
		if (transiciones.size()>1) {
			String message = "Existen muchas transiciones que cumplen con las condiciones del expediente.\n";
			message = message.concat("Plantilla : "  + transiciones.get(0).getPlantillaNombre() + "\n");
			if (estadoPartida==null) {
				message = message.concat("Estado Partida : NULL");
			}else {
				message = message.concat("Estado Partida : "+transiciones.get(0).getEstadoPartidaNombre());
			}
			message = message.concat((estadoLlegada==null)?"Estado Llegada : NULL":"Estado Llegada : "+ transiciones.get(0).getEstadoLlegadaNombre());
			throw new ServerException( message);
		}
		return transiciones.get(0);
	}

	
	private void relacionarGestor( PedidoVentaDTO anterior, PedidoVentaDTO nuevo, String motivo, String securityToken)
			throws ServerException {
		anterior = pedidoService.consultaXId(anterior.getLlaveTabla());
		if(motivo == null) {
			DocumentoPlantillaDTO plantillaNueva = plantillaService.consultaXId(nuevo.getPlantilla());
			motivo = plantillaNueva.getNombre();
		}
		System.out.format("\n(Colocar traza a documento...... %s)", anterior.getNombre());
		//Creo la relacion del documento Gestor
		relacionGestorService.trazar(
				anterior.getLlaveTabla(), nuevo.getLlaveTabla(), motivo, 
				anterior.getEstadoExpediente(), anterior.getEstadoExpediente(), 
				null, null, securityToken, null, anterior.getHistorico(),
				nuevo.getTransaccion());
	}
	

	private PedidoVentaCaracteristicaDTO colocarFiltroDocumentoAuxiliar(String documento){
		//Funcion creada para colocar los filtros
		PedidoVentaCaracteristicaDTO pvc = new PedidoVentaCaracteristicaDTO();
		pvc.setValorOpcion(documento);
		return pvc;
	}
	
	private PedidoVentaCaracteristicaFilterDTO calcularValoresTotalesCampo(PedidoVentaCaracteristicaFilterDTO pCampo, String valorTomar) throws ServerException{
		if(pCampo.getExpedientes()==null) pCampo.setExpedientes(new ArrayList<PedidoVentaDTO>());
		//Calculo el valor de los expedientes y la cantidad
		int cantidad = 0;
		//Esto llena los valores de la tabla relacion expediente
		BigDecimal valor =BigDecimal.ZERO;
		List<DocumentoRelacionExpedienteDTO> relaciones;
		if(pCampo.getLlaveTabla()!=null) {
			DocumentoRelacionExpedienteFilterDTO filtro = new DocumentoRelacionExpedienteFilterDTO();
			filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			filtro.setCampoMaestro(pCampo.getLlaveTabla());
			relaciones = relacionExpedienteService.listarConsulta(filtro);			
		}else {
			relaciones = new ArrayList<DocumentoRelacionExpedienteDTO>();	
		}
		boolean ValorNuevo = true;
		for (PedidoVentaDTO expediente : pCampo.getExpedientes()) {
			if((expediente.getEstado()==null || expediente.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO)!=0)) {
				cantidad ++;
				if(expediente.getDinero()!=null) {
					ValorNuevo = true;
					for (DocumentoRelacionExpedienteDTO iRelacion : relaciones) {
						if(iRelacion.getExpedienteDetalle().compareTo(expediente.getLlaveTabla())==0) {
							if(expediente.getDinero()!=null)expediente.getDinero().setValorCampo(iRelacion.getValor());
							ValorNuevo = false;
							break;
						}
					}
					if(ValorNuevo && valorTomar!=null && expediente.getDinero()!=null){
						if(valorTomar.compareTo("2")==0) {
							expediente.getDinero().setValorCampo( expediente.getDinero().getSaldo());
						}else {//Aqui falta que lo tome de la caracteristica
							expediente.getDinero().setValorCampo( expediente.getDinero().getValorTotal());
						}
					}
					valor = valor.add(expediente.getDinero().getValorCampo());
				}
			}
		}
		pCampo.setValorText(String.valueOf(cantidad));
		pCampo.setValorNumeroMax(valor);
		return pCampo;
	}
	
	
	
	public void dividirDocumento(PedidoVentaDTO anterior, PedidoVentaDTO nuevo, String securityToken, String transaccion) throws ServerException {
		//Se encarga de incluir el documento en los padres
		DocumentoRelacionExpedienteFilterDTO dre = new DocumentoRelacionExpedienteFilterDTO();
		dre.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		dre.setExpedienteDetalle(anterior.getLlaveTabla());
		List<DocumentoRelacionExpedienteDTO> cargues = relacionExpedienteService.listarConsulta(dre);
		if(cargues!=null && !cargues.isEmpty()){
			for(DocumentoRelacionExpedienteDTO relacion: cargues){
				DocumentoRelacionExpedienteDTO relacionCargueNuevo = new DocumentoRelacionExpedienteDTO();
				relacionCargueNuevo.setCampoMaestro(relacion.getCampoMaestro());
				relacionCargueNuevo.setExpedienteDetalle(nuevo.getLlaveTabla());
				relacionCargueNuevo.setTransaccionRegistro(transaccion);
				String  valorTomar = campoService.valueFieldProcessMultipleToPartialDivideDocument(relacion.getCampoMaestro());
				if(valorTomar!=null) {
					if(valorTomar.compareTo("2")==0) {
						if(nuevo.getDinero()!=null) relacionCargueNuevo.setValor(nuevo.getDinero().getSaldo());
						if(nuevo.getDinero()!=null) {
							relacion.setValor(anterior.getDinero().getSaldo());
							relacionExpedienteService.update(relacion);
						}
					}else {//Aqui falta que lo tome de la caracteristica
						if(nuevo.getDinero()!=null) relacionCargueNuevo.setValor(nuevo.getDinero().getValorTotal());
						if(nuevo.getDinero()!=null) {
							relacion.setValor(anterior.getDinero().getValorTotal());
							relacionExpedienteService.update(relacion);
						}
					}
				}
				relacionExpedienteService.guardar(relacionCargueNuevo, securityToken);
			}
		}
	}
	
	/*private PedidoVentaCaracteristicaDTO anularMovimiento(PedidoVentaCaracteristicaDTO pCampo) throws ServerException {
		// No se esto deberia ser por un proceso
		if(pCampo.getValorAuxiliar()!=null){
			MovimientoDTO movimiento = movimientoService.consultaXId(pCampo.getValorAuxiliar());
			if(movimiento!=null) { //El valor campo auxiliar lo uso en varios puntos y en un caso me afeca la bodega y la va a intentar anular
				//throw new ServerException("Por favor revise la consistencia de datos, el id del movimiento no se encuentra.");
				movimiento.setSecurityToken(pCampo.getSecurityToken());
				movimientoService.inactivar(movimiento);
			}
		}
		return pCampo;
	}*/

	private void cerrarCaja(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		String catalogoCierre = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.CUENTA_CERRAR_CAJA);
		if(!catalogoCierre.isEmpty()){
			TurnoDTO turno = new TurnoDTO();
			turno.setUsuario(campoService.getUserFlex(token));
			turno.setDocumento(pCampo.getValorOpcion());
			turno = turnoService.consultarTurnoActual(turno);
			if(turno==null) throw new ServerException("No se identifica el turno en ejecucion");
			CuentaDTO caja = cuentaService.consultaXId(turno.getCuenta());
			BigDecimal saldo = caja.getSaldo();
			if(saldo==null) saldo = BigDecimal.ZERO;
			if(pCampo.getPrincipal()!=null & pCampo.getPrincipal().getDinero()!=null && pCampo.getPrincipal().getDinero().getValorTotal().compareTo(BigDecimal.ZERO)!=0){
				if(pCampo.getValorOpcion()==null) throw new ServerException("Para este cierre con valor es neesario colocar la caja de destino");
				MovimientoDTO movimiento = new MovimientoDTO();
				movimiento.setFechaEvento(pCampo.getPrincipal().getFecha());
				movimiento.setTipo(MovimientoDTO.SALIDA_GASTO);
				movimiento.setCuenta(caja.getLlaveTabla());
				movimiento.setMonto(pCampo.getPrincipal().getDinero().getValorTotal());
				//movimiento.setCuentaPermisoUsuario(turno.getCuentaPermiso());
				movimiento.setDocumento(pCampo.getDocumento());
				movimiento = movimientoService.guardar(movimiento, token);
				saldo = saldo.add(movimiento.getMontoAplicado());
			}
			//if(permiso.getEstado().compareTo(TurnoDTO.ESTADO_ACTIVO)!=0) throw new ServerException("No tiene permisos sobre esa cuenta");
			/*if(caja.getValidarTurno()){
				if(caja.getCierreMaximo().compareTo(saldo)<0)
					throw new ServerException("Tiene como restriccion cerrar la caja con un valor maximo de " + caja.getCierreMaximo());
			}*/
			turno.setFechaEntrega(new Date());
			turno.setEstado(TurnoDTO.ESTADO_FINALIZADO);
			turno.setMontoFinal(saldo);
			turno = turnoService.actualizar(turno, token);
			caja.setFechaConciliacion(turno.getFechaEntrega());
			caja = cuentaService.actualizar(caja, token);
		}
	}
	
	private void generarPagos(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		String catalogoMovimiento = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.CUENTA_MOVIMIENTO);
		if(!catalogoMovimiento.isEmpty()){
			PedidoVentaDTO documento = pCampo.getPrincipal();
			if(documento.getDinero()==null)throw new ServerException("Los formularios con campos tipo cuenta deben tener el valor");
			/*if(documento.getDinero()==null){
				if(plantillaService.consultaXId(documento.getPlantilla()).getTipo().compareTo(DocumentoPlantillaDTO.REPORTE)==0){
					return;
				}else{
					throw new ServerException("Los formularios con campos tipo cuenta deben tener el valor");
				}
			}*/
			if(documento.getDinero().getValorTotal().compareTo(BigDecimal.ZERO)!=0){
				MovimientoDTO movimiento = new MovimientoDTO();
				movimiento.setFechaEvento(documento.getFecha());
				movimiento.setTipo(catalogoMovimiento);
				movimiento.setMonto(documento.getDinero().getValorTotal());
				movimiento.setCuenta(pCampo.getValorAuxiliar());
				movimiento.setDocumento(pCampo.getDocumento());
				movimiento = movimientoService.guardar(movimiento, token );
				//pedidoService.actualizarSaldo(pCampo.getDocumento(), movimiento.getMonto(), pCampo.getSecurityToken());
				pCampo.setValorAuxiliar( movimiento.getLlaveTabla() );
				pCampo.setValorFecha( movimiento.getFechaEvento() );
				pCampo.setValorNumero( movimiento.getMonto() );
			}
		}
		String anularMovimiento = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.CUENTA_ANULAR_MOVIMIENTO);
		if(anularMovimiento.isEmpty()) return;
		MovimientoFilterDTO movimiento = new MovimientoFilterDTO();
		movimiento.setDocumento(pCampo.getValorOpcion());
		movimiento.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		List<MovimientoDTO> movimientos = movimientoService.listarConsulta(movimiento);
		if(movimientos==null || movimientos.isEmpty()) throw new ServerException("Estas anulando un movimiento y no se encuentra en la tabla de movimientos");
		if(movimientos.size()!=1) throw new ServerException("Estas anulando un movimiento de un documento y este documento tiene muchos movimientos");
		MovimientoDTO result = movimientoService.inactivar(movimientos.get(0), token);
		pCampo.setValorAuxiliar( result.getLlaveTabla() );
		pCampo.setValorFecha( result.getFechaEvento() );
		pCampo.setValorNumero( result.getMonto() );
	}
	
	private void relacionExternaDocumentos(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		
		List<PropiedadDTO> relacionExternaAgregar = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(), Propiedades.RELACIONAR_DOCUMENTOS);
		List<PropiedadDTO> relacionExternaRetirar = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(), Propiedades.RETIRAR_DOCUMENTOS); 
		if(relacionExternaAgregar==null && relacionExternaRetirar==null) return;
		if(relacionExternaAgregar==null) relacionExternaAgregar = new ArrayList<PropiedadDTO>();
		if(relacionExternaRetirar!=null)relacionExternaAgregar.addAll( relacionExternaRetirar);
		if(pCampo.getDependientes()==null) throw new ServerException("relacionado o retirando documentos no esta relacionado el dependiente que contiene el campo proceso que vamos a afectar");
		campoService.validarDependientes(pCampo.getCampoDTO(), pCampo.getDependientes());
		for (PropiedadDTO propiedadDTO : relacionExternaAgregar) {
			for (PedidoVentaCaracteristicaDTO dependiente : pCampo.getDependientes()) {
				if(dependiente.getCampo().compareTo(propiedadDTO.getValor())==0) {
					List<RelacionInternaDTO> relaciones = relacionService.relacionesPropiedad(propiedadDTO.getLlaveTabla());
					if(relaciones ==null || relaciones.isEmpty()) throw new ServerException("Revisa las relaciones de la propiedad " + propiedadDTO.getNombre() + " del campo " + pCampo.getCampoDTO().getNombre());
					for (RelacionInternaDTO iRelacion : relaciones) {
						PedidoVentaCaracteristicaFilterDTO campoDestinoFilter = new PedidoVentaCaracteristicaFilterDTO();
						campoDestinoFilter.setDocumento(dependiente.getValorOpcion());
						campoDestinoFilter.setCampo(iRelacion.getCampo());
						PedidoVentaCaracteristicaDTO campoDestino = campoService.consultaUnica(campoDestinoFilter);
						// Aqui sucedio en colegios, la plantilla curso se creo sin campo estudiantes y se creo un curso, este no se asociaba porque no existia el campo destino. toca dejarlo asi porque hay casos donde se salta esta validacion.
						if(campoDestino!=null) {
							campoDestino.setTransaccionRegistro(pCampo.getTransaccionRegistro());
							for (PedidoVentaDTO iDocumentoRelacionar : pCampo.getExpedientes()) {
								if(propiedadDTO.getKey().compareTo(Propiedades.RELACIONAR_DOCUMENTOS)==0) {
									relacionarExpedienteDocumento(campoDestino, iDocumentoRelacionar, token);
								}else {
									retirarExpedienteDocumento(campoDestino, iDocumentoRelacionar, token);
								}
							}
						}
					}
					break;
				}
			}
		}	
		
	}	
}
