package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.dto.ConsecutivoDTO;
import com.softure.logisticpymes.dto.CuentaDTO;
import com.softure.logisticpymes.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.dto.DocumentoPlantillaDTO;
import com.softure.logisticpymes.dto.DocumentoTransaccionDTO;
import com.softure.logisticpymes.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.dto.PedidoVentaDineroDTO;
import com.softure.logisticpymes.dto.PlantillaConsecutivoDTO;
import com.softure.logisticpymes.dto.ProcesoTransicionDTO;
import com.softure.logisticpymes.dto.PropiedadDTO;
import com.softure.logisticpymes.dto.PropiedadValorDefinidoDTO;
import com.softure.logisticpymes.dto.RolAccesoDTO;
import com.softure.logisticpymes.dto.UsuarioDTO;
import com.softure.logisticpymes.dto.UsuarioRolDTO;
import com.softure.logisticpymes.dto.filter.CuentaFilterDTO;
import com.softure.logisticpymes.dto.filter.DocumentoPlantillaFilterDTO;
import com.softure.logisticpymes.dto.filter.DocumentoPlantillaCaracteristicaFilterDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaCaracteristicaFilterDTO;
import com.softure.logisticpymes.dto.filter.PlantillaConsecutivoFilterDTO;
import com.softure.logisticpymes.dto.filter.RolAccesoFilterDTO;
import com.softure.logisticpymes.dto.filter.UsuarioFilterDTO;
import com.softure.logisticpymes.dto.filter.UsuarioRolFilterDTO;
import com.softure.logisticpymes.services.adapter.CampoAdaptador;
import com.softure.logisticpymes.services.adapter.Propiedades;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.PedidoVentaDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaFilterDTO;
import com.softure.logisticpymes.persistence.PedidoVentaMapper;

@Service("pedidoVentaService")
public class PedidoVentaSvc extends BasicSvc<PedidoVentaDTO, PedidoVentaFilterDTO> {
	
	@Autowired
	private PedidoVentaMapper pedidoVentaMapper;
	
	// BEGIN region servicesPedidoVenta

	@Autowired private BodegaSvc bodegaService;
	@Autowired private CampoAdaptador adaptador;
	@Autowired private ConsecutivoSvc consecutivoService;
	@Autowired private CuentaSvc cuentaService;
	
	@Autowired private DocumentoPlantillaSvc documentoPlantillaService;
	@Autowired private DocumentoPlantillaCaracteristicaSvc documentoPlantillaCaracteristicaService;
	@Autowired private DocumentoRelacionGestorSvc relacionGestorService;
	@Autowired private DocumentoTransaccionSvc transaccionSvc;
	
	@Autowired private MensajeSvc mensajeSvc;
	@Autowired private PlantillaConsecutivoSvc plantillaConsecutivoSvc;
	@Autowired private PedidoVentaDineroSvc dineroService;
	@Autowired private PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService;
	@Autowired private ProcesoEstadoSvc estadoService;
	@Autowired private ProcesoTransicionSvc transicionService;
	@Autowired private ProductoSvc productoService;
	@Autowired private PropiedadSvc propiedadService;
	@Autowired private UsuarioSvc usuarioService;
	@Autowired private UsuarioRolSvc usuarioRolService;
	@Autowired private RolAccesoSvc rolService;
	@Autowired private WebServiceEjecucionSvc apiService;
	// END region servicesPedidoVenta

	@Override
	public PedidoVentaDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. PedidoVenta");
		PedidoVentaFilterDTO dto = new PedidoVentaFilterDTO();
		dto.setLlaveTabla(llave);
		return pedidoVentaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = pedidoVentaMapper;
	}
	
	@Override
	public PedidoVentaDTO activar(PedidoVentaDTO dto, String token) throws ServerException {
		// BEGIN PedidoVenta_activar
		throw new ServerException("Un documento que fue inactivado no se puede volver a activar.");
		// END PedidoVenta_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaDTO actualizar( PedidoVentaDTO dto, String token) throws ServerException {
		// BEGIN PedidoVenta_actualizar
		DocumentoPlantillaFilterDTO plantillaFilter = new DocumentoPlantillaFilterDTO();
		PedidoVentaDTO bd = consultaXId(dto.getLlaveTabla());
		dto.setPlantilla(bd.getPlantilla());
		//if(dto.getTransaccion()!=null && dto.getTransaccion().compareTo(bd.getTransaccion())==0) dto.setTransaccion(null);
		plantillaFilter.setLlaveTabla(dto.getPlantilla());
		plantillaFilter.setSecurityToken(token);
		DocumentoPlantillaDTO plantilla = documentoPlantillaService.obtenerConfiguracionSinCampos(plantillaFilter, rolService.usuarioPermisosCompletos(token));
		plantilla = documentoPlantillaService.obtenerCampos(plantilla, token);
		if(Propiedades.obtenerValor(plantilla, Propiedades.PERMISO_PLANTILLA_MODIFICAR).isEmpty()) throw new ServerException("El usuario no tiene permisos para modificar un " + plantilla.getNombre());
		// PedidoVentaDTO pdv = consultaXId(dto.getLlaveTabla());
		if(bd.getEstadoExpediente()!=null) {
			if(dto.getEstadoExpediente()==null) throw new ServerException("El documento a actualizar debe traer el estado del expediente");
			if(bd.getEstadoExpediente().compareTo(dto.getEstadoExpediente())!=0) throw new ServerException("Revise porque el documento tiene un estado diferente.\nDocumento: " + bd.getNombre() + "\nEstado actual: " +bd.getEstadoNombre());
		}
		// Me aparecio un hz desde el historico, porque no tienen principal y nose como pasarlo pero tengo mis dudas con el guardar
		for (PedidoVentaCaracteristicaDTO iterador : dto.getCaracteristicas()) {
			iterador.setPrincipal(bd);
		}
		validarCaracteristicas(dto, plantilla, token);
		if(dto.getNombre()==null) {
			dto.setNombre(bd.getNombre());//Cuando envio modificar lo envio vacio
			dto.setConsecutivo(bd.getConsecutivo());
		}
		String campoDescripcion = Propiedades.obtenerValor(plantilla, Propiedades.DESCRIPCION);//Descripcion para los roles
		if(!campoDescripcion.isEmpty()) {
			for (PedidoVentaCaracteristicaDTO iterador : dto.getCaracteristicas()) {
				if(campoDescripcion.compareTo(iterador.getCampo())==0) {
					dto.setDescripcion(iterador.getValorText());
					break;
				}
			}
		}
		validarConsecutivo(dto, plantilla, token);
		dto.setFecha(bd.getFecha()); //Copio la fecha para que no me la modifiquen desde el cliente sin un campo
		validarFecha(dto, Propiedades.obtenerValor(plantilla, Propiedades.FECHA));
		validarCosto(dto, plantilla);
		String transaccion = dto.getTransaccion();
		boolean crearTraza = false;//Cuando un formulario modifica otro no debo crear traza ya que esta la del proceso
		if(transaccion ==null ||  bd.getTransaccion().compareTo(transaccion)==0) {//Si son diferetnes vienen de otro proceso
			transaccion = transaccionSvc.crear(token).getLlaveTabla();
			crearTraza = true;
		}
		if(dto.getEstado()==null) {
			if(dto.getEstadoExpediente()==null) {
				dto.setEstado(ConstantesGenerales.ESTADO_ACTIVO);//Viene de tipo proceso que lo coloca nulo
			}else {
				dto.setEstado(estadoService.consultaXId(dto.getEstadoExpediente()).getEstadoDocumento());
			}
		}
		dto.setFechaRegistro(bd.getFechaRegistro());//Siempre tiene que mantenerse la fecha de registro
		dto.setTransaccion(bd.getTransaccion());//Siempre tiene que mantenerse la transaccion de registro
		dto.setFuncionario(bd.getFuncionario());//Siempre tiene que mantenerse la funcionario de registro
		dto.setHistorico(bd.getHistorico());
		bd = update(dto);
		gestionarDinero(dto, token);
		for (PedidoVentaCaracteristicaDTO iterador : dto.getCaracteristicas()) {
			iterador.setTransaccionRegistro(transaccion);//Le quite el igual a null asumo que va a modificar los nuevos
			iterador.setPrincipal(bd);
		}
		dto.setCaracteristicas(gestionarCaracteristicas(dto, token));
		gestionarTipos(dto, plantilla, token);
		//Para los tipo cuenta al actualizar no estoy mirando los sobregiros
		if(crearTraza) relacionGestorService.trazar(dto.getLlaveTabla(), null, plantilla.getNombre(), dto.getEstadoExpediente(), dto.getEstadoExpediente(), 
				(dto.getDinero()==null)?null:dto.getDinero().getLlaveTabla(), null, token, null, dto.getHistorico(), transaccion);
		propiedadService.validarFuncionConsultandoPropiedad(plantilla, dto.getLlaveTabla(), null, dto.getFuncionario(), token);
		dto.setCaracteristicas(null);//Por error al serializar
		return dto;
		// END PedidoVenta_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaDTO inactivar(PedidoVentaDTO dto, String token) throws ServerException {
		// BEGIN PedidoVenta_inactivar
		throw new ServerException("Usa la transaccion de inactivar con documento para mantener la trazabilidad");
		// END PedidoVenta_inactivar
	}
	
	@Override
	public PedidoVentaDTO consultaUnica(PedidoVentaFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(PedidoVentaFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<PedidoVentaDTO> listarConsulta(PedidoVentaFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	public PedidoVentaDTO consultaCompleta(PedidoVentaFilterDTO dto)throws ServerException{
		// BEGIN region consultaCompleta
		if(dto.getLlaveTabla()==null) throw new ServerException("En el desarrollo se debe crear el objeto desde la plantilla");
		String securityToken = dto.getSecurityToken();
		PedidoVentaDTO bd = consultaXIdConDinero(dto.getLlaveTabla());
		if(bd == null) throw new ServerException("El identificador del DTO es incorrecto");
		//VAlido que el estado del pedido me permita modificaciones
		boolean modificable = true;
		if(bd.getEstadoExpediente()!=null){
			String usuarioToken = (securityToken==null)?null:getUserFlex(securityToken);
			modificable = (propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.ESTADO, bd.getEstadoExpediente(), 
					Propiedades.MODIFICABLE, usuarioToken)==null)?false:true;
		}else{
			if(bd.getEstado().compareTo(PedidoVentaDTO.ESTADO_ACTIVO)!=0) {
				modificable = false;
				if(bd.getEstado().compareTo(PedidoVentaDTO.ESTADO_FINALIZADO)==0) {
					bd.setEstadoNombre("FINALIZADO");
				}else {
					bd.setEstadoNombre("INACTIVO");
				}
			}else {
				bd.setEstadoNombre("ACTIVO");
			}
		}
		DocumentoPlantillaFilterDTO plantillaFilter = new DocumentoPlantillaFilterDTO();
		plantillaFilter.setLlaveTabla(bd.getPlantilla());
		plantillaFilter.setSecurityToken(securityToken);
		DocumentoPlantillaDTO plantilla = documentoPlantillaService.obtenerConfiguracionSinCampos(plantillaFilter, rolService.usuarioPermisosCompletos(securityToken));
		plantilla = documentoPlantillaService.obtenerCampos(plantilla, securityToken);
		if(plantilla.getCaracteristicas()!=null & plantilla.getCaracteristicas().size()!=0){
			List<PedidoVentaCaracteristicaDTO> caracteristicasActuales = pedidoVentaCaracteristicaService.listar2Documento(bd.getLlaveTabla(), bd.getHistorico());
			bd.setCaracteristicas(new ArrayList<PedidoVentaCaracteristicaDTO>());
			PedidoVentaCaracteristicaDTO uc = null;
			for (DocumentoPlantillaCaracteristicaDTO documentoCaracteristicaDTO : plantilla.getCaracteristicas()) {
				uc=null;
				for (PedidoVentaCaracteristicaDTO pedidoCaracteristica : caracteristicasActuales){
					if(pedidoCaracteristica.getCampo().compareTo(documentoCaracteristicaDTO.getLlaveTabla())==0){
						uc = pedidoCaracteristica;
					break;
					}
				}
				if(uc==null)uc= new PedidoVentaCaracteristicaDTO();
				uc.setCampo(documentoCaracteristicaDTO.getLlaveTabla());
				uc.setCampoDTO(documentoCaracteristicaDTO);
				uc.setDocumento(bd.getLlaveTabla());
				adaptador.cargarConsultaCampo(uc, securityToken);
				if(!modificable) {
					Propiedades.retirarPropiedad(uc.getCampoDTO(), Propiedades.PERMISO_CAMPO_MODIFICABLE);
					//Propiedades.retirarPropiedad(uc.getCampoDTO(), Propiedades.PERMISO_CAMPO_EDITABLE);
				}
				bd.getCaracteristicas().add(uc);
			}
		}
		return bd;
		// END region consultaCompleta
	}
	public List<PedidoVentaDTO> listarAvanzado(PedidoVentaFilterDTO dto)throws ServerException{
		// BEGIN region listarAvanzado
		System.out.println (new Date().toString() + " : ListarAvanzado");
		if(dto==null) throw new ServerException("Tronco de error");
		if(dto.getFiltroParametro()!=null && dto.getFiltroParametro().isEmpty()) dto.setFiltroParametro(null);
		if(dto.getFiltroParametro()!=null) dto.setFiltroParametro(SoftureUtil.formatFunction(dto.getFiltroParametro()).toUpperCase());//Yo tenia el normalize por BD pero no fue una buena practica porque consume mucha memoria
		if (dto.getNombre()!=null) dto.setNombre(dto.getNombre().toUpperCase()); // En los filtros se generaba error por las minusculas
		if (dto.getCampoPropiedad()!=null) {
			PropiedadDTO propiedadFuncion = propiedadService.consultaXId(dto.getCampoPropiedad());
			return listadoCompleto( listarExpedientesDisponiblesDocumentoFuncion(dto, propiedadFuncion.getLlaveTabla(), null), dto.getSecurityToken(), null );
		}
		//Filtros desde un campo
		if(dto.getCampoOrigen()!=null) {
			DocumentoPlantillaCaracteristicaDTO campoPlantilla = documentoPlantillaCaracteristicaService.consultaXId(dto.getCampoOrigen());
			if(campoPlantilla==null) throw new ServerException("Revise porque el campo enviado de filtro no es correcto");
			campoPlantilla = documentoPlantillaCaracteristicaService.cargarComplementos(campoPlantilla, dto.getSecurityToken());
			PropiedadDTO propiedadHeredable1 = Propiedades.obtenerParametro(campoPlantilla, Propiedades.CAMPO_HEREDADO_1);
			if(propiedadHeredable1!=null) {
				if(dto.getTextoFiltro()==null) throw new ServerException("Para los campos de herencia se debe colocar el id del documento base en el campo texto filtro");
				String tokenHeredable = dto.getSecurityToken();
				dto.setSecurityToken(null);//Se quito que solo viera los que tiene permiso
				List<String> estadosFiltro = organizarFiltros(dto);
				List<String> textoFiltroComas = organizarFiltroComas(dto);
				try {
					return listadoCompleto(
							pedidoVentaMapper.listarPermitidos(dto, estadosFiltro, 
									propiedadService.camposRelacionados(propiedadHeredable1),//Consulto las realaciones del campo para saber cuales campos heredan con la funcion de  
									dto.getTextoFiltro(), null, null, textoFiltroComas), tokenHeredable, null); 
				}catch (Exception e) {
					throw new ServerException(e.getMessage());
				}
			}else {
				String propiedadPlantilla = Propiedades.obtenerValor(campoPlantilla, Propiedades.PLANTILLA_AUXILIAR);
				String propiedadMultiple = Propiedades.obtenerValor(campoPlantilla, Propiedades.MULTIPLE);
				if(!propiedadMultiple.isEmpty()) {
					PropiedadDTO propiedadFuncion = Propiedades.obtenerParametro(campoPlantilla, Propiedades.PROCESO_FUNCION_SQL);
					if(propiedadFuncion!=null || propiedadPlantilla.isEmpty()) {
						String campoValor = Propiedades.obtenerValor(campoPlantilla, Propiedades.PROCESO_VALOR);
						if(campoValor.isEmpty() || campoValor.compareTo("1")==0 || campoValor.compareTo("2")==0) campoValor = null;
						List<PedidoVentaCaracteristicaDTO> parametros = null;
						if(dto.getLlaveTabla()!=null) { // asumo que viene el dependiente relacionado
							PedidoVentaCaracteristicaDTO param = new PedidoVentaCaracteristicaDTO();
							param.setValorOpcion(dto.getLlaveTabla());
							parametros = new ArrayList<PedidoVentaCaracteristicaDTO>();
							parametros.add(param);
						}
						return listadoCompleto( listarExpedientesDisponiblesDocumentoFuncion(dto, (propiedadFuncion==null)?null:propiedadFuncion.getLlaveTabla(), parametros), dto.getSecurityToken(), campoValor );
					} else {// hay casos que quiero que filtre solo por la fuente de datos
						if(dto.getLlaveTabla()!=null) { //el filtro viene en la llave tabla
							dto.setCaracteristicas(new ArrayList<PedidoVentaCaracteristicaDTO>());
							PedidoVentaCaracteristicaDTO pvc = new PedidoVentaCaracteristicaDTO();
							pvc.setValorOpcion(dto.getLlaveTabla());
							dto.getCaracteristicas().add(pvc);
							dto.setLlaveTabla(null);
						}
					}
				}
				dto.setPlantilla(propiedadPlantilla);
			}
		}
		
		//Filtros desde una lista
		String secToken =null;
		dto.setFuncionarioNombre(null);
		String token = dto.getSecurityToken();
		paginar(dto);
		DocumentoPlantillaDTO plantilla = null;//Es para almacenar las propiedades soloque tengo que pasar un BasicaPAram porque iba a pasar solo las propiedades
		//Consulto que la plantilla solicitada tenga permisos
		if(dto.getPlantilla()!=null) {// && dto.getLlaveTabla()==null){ OJO tengo que revisar poruqe tengo esto
			String campoFiltro = null;
			boolean verTodos = false;
			
			if(rolService.usuarioPermisosCompletos(dto.getSecurityToken())) {
				verTodos = true;
			}else {
				plantilla = new DocumentoPlantillaDTO();
				plantilla.setPropiedades( propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.PLANTILLA, dto.getPlantilla(), null, getUserFlex(token)) );		
				List<PropiedadDTO> propiedadesVerTodos = Propiedades.obtenerVariosParametro(plantilla, Propiedades.PERMISO_PLANTILLA_VER_TODOS);
				if(propiedadesVerTodos!=null && !propiedadesVerTodos.isEmpty()){
					verTodos = true;
				}else{
					//Si tiene funcion entonces se omite el resto de la parametrizacion
					PropiedadDTO propiedadFuncion = Propiedades.obtenerParametro(plantilla, Propiedades.PROCESO_FUNCION_SQL);
					if(propiedadFuncion!=null) {
						return listadoCompleto( listarExpedientesDisponiblesDocumentoFuncion(dto, propiedadFuncion.getLlaveTabla(), null), dto.getSecurityToken(), null );
					}
					
					List<PropiedadDTO> propiedadesFiltro = Propiedades.obtenerVariosParametro(plantilla, Propiedades.PERMISO_PLANTILLA_CAMPO_FILTRO);
					if(propiedadesFiltro!=null && !propiedadesFiltro.isEmpty()){
						campoFiltro = propiedadesFiltro.get(0).getValor();
					}
				}			
			}
			
			if(dto.getCaracteristicas()==null){
				if(verTodos ){
					secToken = null;
				}else{
					if(campoFiltro!=null){
						secToken = token;
						//Lo anterior al parecer servia para muchos, pero por el momento voy mirando solo uno
						PedidoVentaCaracteristicaDTO pvc = new PedidoVentaCaracteristicaDTO();
						pvc.setCampo(campoFiltro);
						dto.setCaracteristicas(new ArrayList<PedidoVentaCaracteristicaDTO>());
						dto.getCaracteristicas().add(pvc);
					}else{
						dto.setFuncionario(getUserFlex(dto.getSecurityToken()));
						//Coloco el filtro por el mismo
						dto.setCaracteristicas(new ArrayList<PedidoVentaCaracteristicaDTO>());
						PedidoVentaCaracteristicaDTO pvc = new PedidoVentaCaracteristicaDTO();
						dto.getCaracteristicas().add(pvc);
						//SE coloca esta linea debidoa que se debe filtrar por los permitidos por usuario cuando no tiene el check de vere todos
						secToken = token;
					}
				}
			}else{
				dto.setFuncionarioNombre(dto.getCaracteristicas().get(0).getValorOpcion());
				//Se coloca porque en un form pedido que lista por vendedor trae todos los vendedores
				if(dto.getFuncionarioNombre()==null && !verTodos) secToken = token;
			}
		}
		
		if(dto.getNombre()!=null){
			PedidoVentaFilterDTO filtro = new PedidoVentaFilterDTO();
			filtro.setNombre(dto.getNombre().toUpperCase());
			filtro.setPlantilla(dto.getPlantilla());
			filtro.setFuncionarioNombre(dto.getFuncionarioNombre());
			filtro.setFuncionario(dto.getFuncionario()); //No me encontraba una guia con el usuario
			filtro.setSecurityToken(secToken);
			filtro.setCaracteristicas(dto.getCaracteristicas());
			//filtro.setDocumentoFiltro(dto.getDocumentoFiltro());
			try {
				return listadoCompleto(pedidoVentaMapper.listarPermitidos(filtro, null, null, null, null, null, null), token, null); 
			}catch (Exception e) {
				throw new ServerException(e.getCause().getMessage());
			}
		}else{
			String orden = null;
			String ordenAscendente = null;
			//Esto filtra los resultados por estado, pero si va a consultar un solo registro mejor lo dejo solo para que sea consulta por id
			List<String> estadosFiltro = organizarFiltros(dto);
			if(dto.getLlaveTabla()==null){
				if(dto.getPlantilla()==null) { // Esto es para los procesos deben traer los estados
					if (estadosFiltro == null) throw new ServerException("Por favor revise porque el campo no tiene plantilla");
				} else {
					//DocumentoPlantillaDTO plantillaFiltro = documentoPlantillaService.consultaXId(dto.getPlantilla());
					//if(plantillaFiltro==null) throw new ServerException("Por favor revise el id de la plantilla porque no se encuentra");
					if(plantilla ==null) {
						plantilla = new DocumentoPlantillaDTO();
						plantilla.setPropiedades( propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.PLANTILLA, dto.getPlantilla(), null, getUserFlex(dto.getSecurityToken())));
					}
					PropiedadDTO filtroFechas = Propiedades.obtenerParametro(plantilla,  Propiedades.SOLICITAR_FECHAS);
					if(filtroFechas!=null) {
						if(dto.getFechaMin()==null) throw new ServerException("Por favor seleccione fecha de inicio para la consulta");
						if(dto.getFechaMax()==null) throw new ServerException("Por favor seleccione fecha de fin para la consulta");	
					}
					orden = Propiedades.obtenerValor(plantilla, Propiedades.ORDEN);
					if(orden.isEmpty())orden = null;
					ordenAscendente = Propiedades.obtenerValor(plantilla,  Propiedades.ORDEN_DESCENDENTE);
					if(ordenAscendente.isEmpty())ordenAscendente = null;
				}
				
			}else {
				dto.setFiltroParametro(null);
			}
			List<String> textoFiltroComas = organizarFiltroComas(dto);
			dto.setSecurityToken(secToken);
			try {
				System.out.println (new Date().toString() + " : Query avnazado");
				return listadoCompleto(
						pedidoVentaMapper.listarPermitidos(dto, estadosFiltro, null, null , orden, ordenAscendente, textoFiltroComas)
						, token, null); 
			}catch (Exception e) {
				throw new ServerException(e.getMessage());
			}
		}
		// END region listarAvanzado
	}
	public List<PedidoVentaDTO> listarUsuario(PedidoVentaFilterDTO dto)throws ServerException{
		// BEGIN region listarUsuario
		if(dto.getFuncionario()==null) dto.setFuncionario(getUserFlex(dto.getSecurityToken()));
		paginar(dto);
		try {
			return listadoCompleto(pedidoVentaMapper.listarUsuario(dto), dto.getSecurityToken(), null); 
		}catch (Exception e) {
			throw new ServerException(e.getMessage());
		}
		// END region listarUsuario
	}

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaDTO guardar(PedidoVentaDTO dto, String token) throws ServerException {
		// BEGIN PedidoVenta_guardar
		if(dto.getLlaveTabla()!=null) throw new ServerException("Envio un pedido a guardar con llave existente");
		dto.setFuncionario(getUserFlex(token));
		
		DocumentoPlantillaFilterDTO plantillaFilter = new DocumentoPlantillaFilterDTO();
		plantillaFilter.setLlaveTabla(dto.getPlantilla());
		plantillaFilter.setSecurityToken(token);
		DocumentoPlantillaDTO plantilla = documentoPlantillaService.obtenerConfiguracionSinCampos(plantillaFilter, rolService.usuarioPermisosCompletos(token));
		plantilla = documentoPlantillaService.obtenerCampos(plantilla, token);
		if(Propiedades.obtenerValor(plantilla, Propiedades.PERMISO_PLANTILLA_CREAR).isEmpty()) throw new ServerException("El usuario no tiene permisos para crear un " + plantilla.getNombre());

		validarCaracteristicas(dto, plantilla, token);	
		validarConsecutivo(dto, plantilla, token);
		validarFecha(dto, Propiedades.obtenerValor(plantilla, Propiedades.FECHA));
		validarCosto(dto, plantilla);
		if(dto.getTransaccion()==null) dto.setTransaccion( transaccionSvc.crear(token).getLlaveTabla() );
		dto.setFechaRegistro(new Date());
		dto.setHistorico(null);
		PedidoVentaDTO pedido = save(dto);
		dto.setLlaveTabla(pedido.getLlaveTabla());
		gestionarDinero(dto, token);
		pedido.setDinero(dto.getDinero());
		String campoDescripcion = Propiedades.obtenerValor(plantilla, Propiedades.DESCRIPCION);
		for (PedidoVentaCaracteristicaDTO iterador : dto.getCaracteristicas()) {
			//Principal se usa en inventarios, debe ir lleno
			iterador.setPrincipal(dto);
			iterador.setDocumento(pedido.getLlaveTabla());
			iterador.setTransaccionRegistro(pedido.getTransaccion());
			//Descripcion para los roles
			if(!campoDescripcion.isEmpty() && campoDescripcion.compareTo(iterador.getCampo())==0) {
				dto.setDescripcion(iterador.getValorText());
				pedido.setDescripcion(iterador.getValorText());
			}
		}
		pedido.setCaracteristicas(gestionarCaracteristicas(dto, token));
		if(dto.getDinero()!=null && pedido.getDinero() ==null ) pedido.setDinero(dto.getDinero());// Error al generar documentos en la iteracion que se borra
		gestionarEstado(pedido, plantilla.getNombre(), token, dto.getTransaccion());
		gestionarTipos(dto, plantilla, token);
		propiedadService.validarFuncionConsultandoPropiedad(plantilla, dto.getLlaveTabla(), null, dto.getFuncionario(), token);
		String api = Propiedades.obtenerValor(plantilla, Propiedades.API);
		if(!api.isEmpty()) apiService.ejecutar(api, dto, null, token);
		dto.setCaracteristicas(null);//Por error al serializar
		return pedido;
		// END PedidoVenta_guardar
	}

// BEGIN region aditionalMethods
	public PedidoVentaDTO actualizarBasico(PedidoVentaDTO dto) throws ServerException {
		validarDobleCodigoActivo(dto, dto.getNombre());
		return update(dto);
	}
	
	private void gestionarEstado(PedidoVentaDTO pedido, String plantillaNombre, String token, String transaccion) throws ServerException{
		ProcesoTransicionDTO inicial = transicionService.consultarTransaccionInicial(pedido.getPlantilla());
		if(inicial!=null) {
			transicionService.gestionarTransicion(inicial, pedido.getLlaveTabla(), pedido, 
					(pedido.getDinero()==null)?null:pedido.getDinero().getValorTotal(), 
					pedido.getDinero(), null, token, transaccion);
		}else {//Cuando son transacciones que no inician un proceso (aqui traza del documento en tipo proceso traza al proceso)
			// cundo son solo documetnos sin transciones se envian mensajes
			mensajeSvc.gestionarMensajes(pedido, null, usuarioService.consultaXId(pedido.getFuncionario()), pedido, token);
			//Pase aqui la traza ya que debo integrar
			relacionGestorService.trazar(pedido.getLlaveTabla(), null, plantillaNombre, null, pedido.getEstadoExpediente(), 
					(pedido.getDinero()==null)?null:pedido.getDinero().getLlaveTabla(), 
					null, token,null, pedido.getHistorico(), transaccion);
		}
		//return inicial;
	}

	private PedidoVentaDTO consultaPedidoInterfazVisual(PedidoVentaDTO dto, List<PedidoVentaCaracteristicaDTO> camposDocumentos) throws ServerException{
		if(dto ==null) return dto;
		//Coloco los estados de documentos sin maquina de estados
		if(dto.getEstadoExpediente()==null){
			switch(dto.getEstado()){
				case PedidoVentaDTO.ESTADO_ACTIVO:
					dto.setEstadoNombre("ACTIVO");
					break;
				case PedidoVentaDTO.ESTADO_INACTIVO:
					dto.setEstadoNombre("INACTIVO");
					break;
				case PedidoVentaDTO.ESTADO_FINALIZADO:
					dto.setEstadoNombre("FINALIZADO");
					break;
			}
		}
		if(camposDocumentos==null || camposDocumentos.isEmpty()) return dto;
		dto.setCaracteristicas(new ArrayList<PedidoVentaCaracteristicaDTO>());
		for (PedidoVentaCaracteristicaDTO pvrDTO : camposDocumentos){
			if(pvrDTO.getDocumento().compareTo(dto.getLlaveTabla())==0) {
				DocumentoPlantillaCaracteristicaDTO campo = new DocumentoPlantillaCaracteristicaDTO();
				campo.setNombre(pvrDTO.getCampo());
				campo.setPropiedades(new ArrayList<PropiedadDTO>());
				campo.getPropiedades().add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, null, Propiedades.PERMISO_CAMPO_RENDER, Propiedades.TRUE, null));
				campo.setLlaveTabla(pvrDTO.getTransaccionRegistro());
				pvrDTO.setTransaccionRegistro(null);
				pvrDTO.setCampoDTO(campo);
				dto.getCaracteristicas().add(pvrDTO);
				if(pvrDTO.getEstado().compareTo(DocumentoPlantillaCaracteristicaDTO.PROCESO)==0
						&& pvrDTO.getValorOpcion()!=null){
					PedidoVentaDTO filtroProceso = new PedidoVentaDTO();
					filtroProceso.setLlaveTabla(pvrDTO.getValorOpcion());
					for (PedidoVentaCaracteristicaDTO pvrDTO2 : camposDocumentos){
						if(pvrDTO2.getDocumento().compareTo(pvrDTO.getValorOpcion())==0) {
							DocumentoPlantillaCaracteristicaDTO campo2 = new DocumentoPlantillaCaracteristicaDTO();
							campo2.setNombre(pvrDTO2.getCampo());
							campo2.setPropiedades(new ArrayList<PropiedadDTO>());
							campo2.getPropiedades().add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, null, Propiedades.PERMISO_CAMPO_RENDER, Propiedades.TRUE, null));
							pvrDTO2.setCampoDTO(campo2);
							dto.getCaracteristicas().add(pvrDTO2);
						}
					}
				}
			}
		}
		return dto;
	}

	private void validarCosto(PedidoVentaDTO pedido, DocumentoPlantillaDTO plantilla) throws ServerException {
		String total = Propiedades.obtenerValor(plantilla, Propiedades.TOTAL);
		if(!total.isEmpty() ){
			PedidoVentaCaracteristicaDTO campoValor= obtenerValor(pedido.getCaracteristicas(), total);
			if(campoValor==null) throw new ServerException("Se debe colocar la caracteristica de valor TOTAL");
			PedidoVentaDineroDTO dineroCalculado = new PedidoVentaDineroDTO();
			dineroCalculado.setValorTotal(campoValor.getValorNumero());
			dineroCalculado.setSaldo(BigDecimal.ZERO);
			if(pedido.getLlaveTabla()!=null && pedido.getEstadoExpediente()!=null) {//Si es modificar debo actualizar el saldo//solo tienen saldo los que son de proceso
				PedidoVentaDineroDTO anterior = dineroService.consultaPorDocumento(pedido.getLlaveTabla(), pedido.getHistorico());
				if(anterior!=null) dineroCalculado.setSaldo(anterior.getSaldo());
				if(campoValor.getModificado()){
					ProcesoTransicionDTO inicial = transicionService.consultarTransaccionInicial(pedido.getPlantilla());
					if(inicial!=null && inicial.getAfectaSaldo()!=null) {
						if(anterior!=null) {
							BigDecimal diferencia = campoValor.getValorNumero().subtract(anterior.getValorTotal());
							dineroCalculado.setSaldo(dineroCalculado.getSaldo().add(diferencia));
						}else {
							dineroCalculado.setSaldo(campoValor.getValorNumero());
						}
					}
				}
			}
			pedido.setDinero(dineroCalculado);
		}else{
			pedido.setDinero(null);
		}
	}
	
	
	public PedidoVentaCaracteristicaDTO obtenerValor(List<PedidoVentaCaracteristicaDTO> caracteristicas, String campoValor) {
		if(caracteristicas==null || caracteristicas.size()==0) return null;
		for (PedidoVentaCaracteristicaDTO pvc : caracteristicas) {
			if(pvc.getCampo().compareTo(campoValor)==0){
				return pvc;
			}
		}
		return null;
	}
	
	private void validarCaracteristicas(PedidoVentaDTO dto, DocumentoPlantillaDTO plantilla, String token) throws ServerException {
		if(plantilla!=null && plantilla.getCaracteristicas()!=null && !plantilla.getCaracteristicas().isEmpty()){
			String filtroTexto = "";
			if(dto.getCaracteristicas()==null) throw new ServerException("Es necesesario registrar informacion adicional.");
			List<PedidoVentaCaracteristicaDTO> ordenadas = new ArrayList<PedidoVentaCaracteristicaDTO>();//En casos como generacion automatica vienen en desorden
			for (DocumentoPlantillaCaracteristicaDTO campoPlantilla : plantilla.getCaracteristicas()) {
				boolean campoEncontrado = false;
				//1 Coloco los campos DTO
				for (PedidoVentaCaracteristicaDTO campoDocumento : dto.getCaracteristicas()) {
					if(campoDocumento.getCampo().compareTo(campoPlantilla.getLlaveTabla())==0){
						campoDocumento.setCampoDTO(campoPlantilla);
						campoDocumento.setCampo(campoPlantilla.getLlaveTabla());
						campoDocumento.setDocumento(dto.getLlaveTabla());
						if(campoDocumento.getDocumento()==null) campoDocumento.setModificado(true);
						campoDocumento.setDependientes(null);
						ordenadas.add(campoDocumento);
						campoEncontrado=true;
						break;
					}
				}
				if(!campoEncontrado && campoPlantilla.getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.SECCION)!=0) 
					throw new ServerException("Revisa porque el campo " + campoPlantilla.getNombre() + " no viene registrado en el documento " + plantilla.getNombre());
			}
			dto.setCaracteristicas(ordenadas);
			if(dto.getLlaveTabla()!=null) {//Valido para actualizar que el campo si se pueda modiifcar
				if(!plantilla.getCaracteristicas().isEmpty()) {
					boolean iContadorModificadas = false;
					for (PedidoVentaCaracteristicaDTO iCampoDocumento : dto.getCaracteristicas()) {
						if(iCampoDocumento.getModificado()) {
							if(Propiedades.obtenerParametro(iCampoDocumento.getCampoDTO(), Propiedades.PERMISO_CAMPO_MODIFICABLE)==null) {
								String mensajeError = "El campo " + iCampoDocumento.getCampoDTO().getNombre();
								mensajeError = mensajeError + " de la plantilla " + iCampoDocumento.getCampoDTO().getPlantillaNombre() + " se envia a modificar pero el usuario ";
								mensajeError = mensajeError + usuarioService.consultaXId(getUserFlex(token)).getNombre() + " no tiene permisos de modificar ese campo";
								throw new ServerException(mensajeError);
							}
							iContadorModificadas= true;
						}
					}
					if(!iContadorModificadas) throw new ServerException("Se envia a modificar un documento sin cambios");
				}
			}
			//2. Coloco los dependientes//Actualizar dependencias despues de los camps para que queden completas asi el campo este despues en orden
			for (PedidoVentaCaracteristicaDTO campoDocumento : dto.getCaracteristicas()) {
				List<PropiedadDTO> codigoDepende = Propiedades.obtenerVariosParametro(campoDocumento.getCampoDTO(), Propiedades.DEPENDE);
				List<PropiedadDTO> modificarCampo = Propiedades.obtenerVariosParametro(campoDocumento.getCampoDTO(), Propiedades.MODIFICAR_CAMPO);
				if(codigoDepende!=null || modificarCampo!=null){
					List<PropiedadDTO> dependencias = new ArrayList<PropiedadDTO>();
					if(codigoDepende!=null) dependencias.addAll(codigoDepende);
					if(modificarCampo!=null) dependencias.addAll(modificarCampo);
					for (PropiedadDTO codigo: dependencias){
						for (PedidoVentaCaracteristicaDTO fieldExpediente: dto.getCaracteristicas()) {
							if(codigo.getValor().compareTo(fieldExpediente.getCampo()) == 0){
								if(campoDocumento.getDependientes()==null)campoDocumento.setDependientes(new ArrayList<PedidoVentaCaracteristicaDTO>());
								if(fieldExpediente.getModificado()) campoDocumento.setModificado(true);
								campoDocumento.getDependientes().add(fieldExpediente);
								break;
							}
						}
					}
				}
			}
			//3. valido cada campo
			for (PedidoVentaCaracteristicaDTO campoDocumento : dto.getCaracteristicas()) {
				adaptador.validarPrepararCampo(campoDocumento, token);
				if(campoDocumento.getValorText()!=null) {
					String filtro = Propiedades.obtenerValor(campoDocumento.getCampoDTO(), Propiedades.FILTRO);
					if(!filtro.isEmpty()) filtroTexto = filtroTexto + campoDocumento.getValorText() + ",";
					PropiedadDTO unique = Propiedades.obtenerParametro(campoDocumento.getCampoDTO(), Propiedades.UNIQUE);
					if(unique!=null) {
						String conicidencia =pedidoVentaCaracteristicaService.validarUnique(campoDocumento);
						if(conicidencia!=null) {
							PedidoVentaDTO coincidenciaDTO = consultaXId(conicidencia);
							throw new ServerException("Ya existe un documento que contiene en el campo " + campoDocumento.getCampoDTO().getNombre() + " el valor " + campoDocumento.getValorText() + "\n\n Documento: "+coincidenciaDTO.getNombre() +"\nFecha: " + coincidenciaDTO.getFecha());
						}
					}					
				}
			}
			if (filtroTexto.compareTo("")!=0) {
				dto.setTextoFiltro(SoftureUtil.formatSimpleFunction(filtroTexto).toUpperCase());
			}else {
				dto.setTextoFiltro(null);
			}
		}
	}
	
	public List<PedidoVentaDTO> listadoCompleto(List<PedidoVentaDTO> result, String securityToken
			, String campoValor) throws ServerException{
		if(result !=null && !result.isEmpty()){
			HashMap<String, String> hmapCamposEspeciales = new HashMap<String, String>();
			
			HashMap<String, String> hmap = new HashMap<String, String>();
			HashMap<String, String> hmapCampo = new HashMap<String, String>();
			List<PedidoVentaCaracteristicaDTO> base = pedidoVentaCaracteristicaService.listar2DocumentoVisible(result);
			List<PedidoVentaDTO> hijos = null;
			List<PedidoVentaDineroDTO> dineroDocumentos = null;
			if(base!=null && !base.isEmpty()) {
				hijos = pedidoVentaMapper.listarVisibleRenderNivel2(result);
				if(hijos!=null && !hijos.isEmpty()) {
					List<PedidoVentaCaracteristicaDTO> visibleHijos = pedidoVentaCaracteristicaService.listar2DocumentoVisible(hijos);
					if(visibleHijos!=null && !visibleHijos.isEmpty()) base.addAll(visibleHijos);
				}
			}
			for (int i =0 ; i<result.size(); i++){
				PedidoVentaDTO iterador = result.get(i);
				iterador = consultaPedidoInterfazVisual(iterador, base);
				if(campoValor==null || campoValor.compareTo("0")==0) {
					if(hmap.get(iterador.getPlantilla())==null){
						PropiedadDTO propiedadCuenta = propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.PLANTILLA, iterador.getPlantilla(), Propiedades.PLANTILLA_TIPO_CUENTA,  null);
						if(propiedadCuenta!=null) {
							hmap.put(iterador.getPlantilla(), "TIPO_CUENTA_VALOR");//Para que los tipo cuenta muestre el saldo
						}else {
							PropiedadDTO propiedad = propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.PLANTILLA, iterador.getPlantilla(), Propiedades.TOTAL, null);
							if(propiedad==null) {
								hmap.put(iterador.getPlantilla(), "");
							}else {
								hmap.put(iterador.getPlantilla(), propiedad.getLlaveTabla());
								if(dineroDocumentos==null) dineroDocumentos = dineroService.listar2DocumentoVisible(result);
							}
						}
					}
					if(!hmap.get(iterador.getPlantilla()).isEmpty()){
						
						if(hmap.get(iterador.getPlantilla()).compareTo("TIPO_CUENTA_VALOR")==0){
							CuentaFilterDTO cuentaFilter = new CuentaFilterDTO();
							cuentaFilter.setDocumento(iterador.getLlaveTabla());
							CuentaDTO cuenta = cuentaService.consultaUnica(cuentaFilter);
							if(cuenta!=null) {
								PedidoVentaDineroDTO dinero = new PedidoVentaDineroDTO();
								dinero.setDocumento(iterador.getLlaveTabla());
								dinero.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
								dinero.setValorTotal(cuenta.getSaldo());
								iterador.setDinero(dinero);	
							}
						}else {
							if(dineroDocumentos!=null && !dineroDocumentos.isEmpty()) {
								for (PedidoVentaDineroDTO iPedidoVentaDineroDTO : dineroDocumentos) {
									if(iterador.getLlaveTabla().compareTo(iPedidoVentaDineroDTO.getDocumento())==0) {
										iterador.setDinero(iPedidoVentaDineroDTO);
										break;
									}
								}
							}
						}
					}
				}else {
					String campoValorIterador = hmapCampo.get(iterador.getPlantilla());
					if(campoValorIterador==null){
						if(campoValor.compareTo("1")==0 || campoValor.compareTo("2")==0) {
							hmapCampo.put(iterador.getPlantilla(), campoValor);//Lo coloco con campo valor para que le asigne saldo o total
						}else {
							DocumentoPlantillaCaracteristicaDTO campo = null;
							if(!campoValor.isEmpty()) {
								DocumentoPlantillaCaracteristicaFilterDTO campoFilter = new DocumentoPlantillaCaracteristicaFilterDTO();
								campoFilter.setPlantilla(iterador.getPlantilla());
								campoFilter.setCodigo(campoValor);
								campoFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
								campo = documentoPlantillaCaracteristicaService.consultaUnica(campoFilter);	
							}
							if(campo==null){
								hmapCampo.put(iterador.getPlantilla(), "");//Lo coloco empty para que no lo busque de nuevo
							}else{
								hmapCampo.put(iterador.getPlantilla(), campo.getLlaveTabla());//Le coloco la llave del campo a buscar
							}
						}
						campoValorIterador = hmapCampo.get(iterador.getPlantilla());
					}
					if(!campoValorIterador.isEmpty()){
						if(campoValor.compareTo("1")==0 || campoValor.compareTo("2")==0) {
							iterador.setDinero(dineroService.consultaPorDocumento(iterador.getLlaveTabla(), iterador.getHistorico()));
						}else {
							PedidoVentaCaracteristicaFilterDTO valorCampoFilter = new PedidoVentaCaracteristicaFilterDTO();
							valorCampoFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
							valorCampoFilter.setCampo(campoValorIterador);
							valorCampoFilter.setDocumento(iterador.getLlaveTabla());
							PedidoVentaCaracteristicaDTO valorCampo = pedidoVentaCaracteristicaService.consultaUnica(valorCampoFilter);
							if(valorCampo!=null) {
								PedidoVentaDineroDTO dinero = new PedidoVentaDineroDTO();
								dinero.setDocumento(iterador.getLlaveTabla());
								dinero.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
								dinero.setValorTotal(valorCampo.getValorNumero());
								dinero.setSaldo(valorCampo.getValorNumero());
								iterador.setDinero(dinero);
							}
						}
					}
				}
				// Campos especiales de una lista
				if(hmapCamposEspeciales.get(iterador.getPlantilla())==null){
					PropiedadDTO propiedadRender = propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.PLANTILLA, iterador.getPlantilla(), Propiedades.PLANTILLA_RENDER_ESPECIAL_SQL,  null);
					if(propiedadRender==null) {
						hmapCamposEspeciales.put(iterador.getPlantilla(), "");
					}else {
						hmapCamposEspeciales.put(iterador.getPlantilla(), propiedadRender.getLlaveTabla());
					}
				}
				if(hmapCamposEspeciales.get(iterador.getPlantilla()).compareTo("")!=0) {
					try {
						List<PedidoVentaCaracteristicaDTO> camposEspeciales = pedidoVentaCaracteristicaService.camposEspecialesPlantilla(hmapCamposEspeciales.get(iterador.getPlantilla()), iterador.getLlaveTabla());
						if(camposEspeciales!=null && !camposEspeciales.isEmpty()) {
							for (PedidoVentaCaracteristicaDTO pvrDTO : camposEspeciales){
								DocumentoPlantillaCaracteristicaDTO campo = new DocumentoPlantillaCaracteristicaDTO();
								//campo.setNombre(pvrDTO.getCampo());
								campo.setPropiedades(new ArrayList<PropiedadDTO>());
								campo.getPropiedades().add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, null, Propiedades.PERMISO_CAMPO_RENDER, Propiedades.TRUE, null));
								pvrDTO.setCampoDTO(campo);
								iterador.getCaracteristicas().add(pvrDTO);
							}
						}	
					} catch (ServerException e) {
						throw new ServerException(e.getMessage());
					}	
				}
			}
		}
		return result;
	}
	

	private void validarConsecutivo(PedidoVentaDTO pedido, DocumentoPlantillaDTO plantilla, String token) throws ServerException {
		String codigoNuevo = null;
		String campoConsecutivo = Propiedades.obtenerValor(plantilla, Propiedades.CONSECUTIVO);
		if(!campoConsecutivo.isEmpty()){
			if(pedido.getCaracteristicas()==null || pedido.getCaracteristicas().size()==0) throw new ServerException("Se debe colocar la caracteristica nombre del documento");
			for (PedidoVentaCaracteristicaDTO pvc : pedido.getCaracteristicas()) {
				if(pvc.getCampo().compareTo(campoConsecutivo)==0){
					switch (pvc.getCampoDTO().getFormato()){
						case DocumentoPlantillaCaracteristicaDTO.NUMERO:
							pedido.setConsecutivo(pvc.getValorNumero());
							if(pedido.getConsecutivo().compareTo(BigDecimal.ZERO)==0) throw new ServerException("Se debe colocar el numero del documento");
							if(plantilla.getConsecutivo()==null) {
								pedido.setNombre(String.valueOf( pedido.getConsecutivo().longValue()));
								codigoNuevo = pedido.getNombre();
							}
							break;
						case DocumentoPlantillaCaracteristicaDTO.TEXTO:
							codigoNuevo = pvc.getValorText();
							break;
						case DocumentoPlantillaCaracteristicaDTO.PROCESO:
							if(pvc.getValorOpcion()==null) throw new ServerException("El valor no puede ser nulo, para asignar un consecutivo." + pvc.getCampoDTO().getNombre());
							PlantillaConsecutivoFilterDTO relacionConsecutivoFilter = new PlantillaConsecutivoFilterDTO();
							relacionConsecutivoFilter.setCaracteristica(pvc.getCampo());
							relacionConsecutivoFilter.setValorOpcion(pvc.getValorOpcion());
							relacionConsecutivoFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
							PlantillaConsecutivoDTO relacionConsecutivo = plantillaConsecutivoSvc.consultaUnica(relacionConsecutivoFilter);
							if(relacionConsecutivo==null){
								if(plantilla.getConsecutivo()==null) {
									throw new ServerException("No es posible crear el consecutivo, dado que no tenemos un consecutivo base para generar en el formulario, coloca el consecutivo base. "+ plantilla.getNombre());									
								}else{
									ConsecutivoDTO nuevo = consecutivoService.crear2Opcion(plantilla.getConsecutivo(), pvc.getCampo(), pvc.getValorOpcion(), token);
									relacionConsecutivo = new PlantillaConsecutivoDTO();
									relacionConsecutivo.setCaracteristica(pvc.getCampo());
									relacionConsecutivo.setValorOpcion(pvc.getValorOpcion());
									relacionConsecutivo.setConsecutivo(nuevo.getLlaveTabla());
									plantillaConsecutivoSvc.guardar(relacionConsecutivo, token);
									
									plantilla.setConsecutivo(nuevo.getLlaveTabla());
								}
							}else{
								plantilla.setConsecutivo(relacionConsecutivo.getConsecutivo());
							}
							break;
						default:
							throw new ServerException("El componente no es tipo texto o numero");
					}
					break;
				}
			}
		}else {
			//Creo el consecutivo y se lo asigno a la plantilla, si es rol no cuadro consecutivo
			if(plantilla.getConsecutivo()==null) {
				PropiedadDTO consecProperty = propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.PLANTILLA, plantilla.getLlaveTabla(), Propiedades.PLANTILLA_TIPO_ROL, getUserFlex(token));
				if(consecProperty ==null)consecutivoService.crear(plantilla, token);
			}
				
		}
		
		if(codigoNuevo==null) {//Lo hace para los automaticos y manuales de numero
			
			if(plantilla.getConsecutivo()==null) throw new ServerException("La plantilla no tiene consecutivo asignado");
			
			ConsecutivoDTO consecutivoManual = consecutivoService.consultaXId(plantilla.getConsecutivo());
			if(consecutivoManual.getManual() || (!consecutivoManual.getManual() && pedido.getLlaveTabla()==null)){
				ConsecutivoDTO consecutivo = new ConsecutivoDTO();
				consecutivo.setLlaveTabla(plantilla.getConsecutivo());
				consecutivo.setNumeroActual(pedido.getConsecutivo());
				consecutivo = consecutivoService.asignarConsecutivo(consecutivo, token);
				pedido.setConsecutivo(consecutivo.getNumeroActual());
				if(pedido.getConsecutivo().compareTo(new BigDecimal(9999999999999999.0))>0) 
					throw new ServerException("Se excedio del numero maximo para el consecutivo 1exp16");
				codigoNuevo = consecutivo.getConsecutivoActual();
			}else{
				codigoNuevo = pedido.getNombre();
			}
			if(pedido.getLlaveTabla()!=null){
				if(!consecutivoManual.getManual()){
					 if(pedido.getNombre()==null || pedido.getNombre().compareTo(codigoNuevo)!=0)
						 throw new ServerException("El consecutivo no puede ser modificado para automaticos");
				}
			}
			if(codigoNuevo==null) throw new ServerException("Se debe colocar el nombre del documento");
		}
		
		if(pedido.getLlaveTabla()==null || pedido.getNombre().compareTo(codigoNuevo)!=0){
			validarDobleCodigoActivo(pedido, codigoNuevo);
			pedido.setNombre(codigoNuevo);
		}
		
	}
	
	private List<PedidoVentaCaracteristicaDTO> gestionarCaracteristicas(PedidoVentaDTO dto, String token) throws ServerException{
		List<PedidoVentaCaracteristicaDTO> result = null;
		if(dto.getCaracteristicas()!=null){
			result = new ArrayList<PedidoVentaCaracteristicaDTO>();
			for (PedidoVentaCaracteristicaDTO iterable : dto.getCaracteristicas()) {
				//iterable.getModificado()!=null && 
				if(iterable.getModificado()){
					iterable.setDocumento(dto.getLlaveTabla());
					result.add(adaptador.guardarCampo(iterable, token));
				}else {//Antes solo devolvia las que iteraba pero no se porque
					result.add(iterable);
				}
			}
		}
		return result;
	}
	
	private void validarFecha(PedidoVentaDTO pedido, String caracteristicaFecha) throws ServerException {
		if(caracteristicaFecha.isEmpty()){
			if(pedido.getLlaveTabla()==null){
				pedido.setFecha(new Date());
			}
		}else{
			PedidoVentaCaracteristicaDTO campoFecha= obtenerValor(pedido.getCaracteristicas(), caracteristicaFecha);
			if(campoFecha==null) throw new ServerException("Se debe colocar la caracteristica de fecha fecha");
			if(campoFecha.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.FECHA)!=0) throw new ServerException("El componente visual no es tipo fecha");
			pedido.setFecha(campoFecha.getValorFecha());
			if(pedido.getFecha()==null) throw new ServerException("Se debe colocar la fecha");
		}
	}
	
	private PedidoVentaDineroDTO gestionarDinero(PedidoVentaDTO documento, String token) throws ServerException {
		if(documento!=null && documento.getDinero()!=null){
			PedidoVentaDineroDTO anterior = dineroService.consultaPorDocumento(documento.getLlaveTabla(), documento.getHistorico());
			if(anterior!=null){
				//Si todo es igual lo dejo quieto
				if(documento.getDinero().getValorTotal().compareTo(anterior.getValorTotal())==0 &&
						documento.getDinero().getSaldo().compareTo(anterior.getSaldo())==0 ) return null;
				anterior= dineroService.inactivarConHistorial(anterior, documento.getHistorico());
			}
			documento.getDinero().setDocumento(documento.getLlaveTabla());
			documento.setDinero( dineroService.guardarConHistorial(documento.getDinero(), documento.getHistorico()) );
			return documento.getDinero();
		}
		return null;
	}
	
	private void gestionarTipos(PedidoVentaDTO dto, DocumentoPlantillaDTO plantilla, String token) throws ServerException {
		//Viene de inactivar
		if(plantilla==null) {
			plantilla = new DocumentoPlantillaDTO();
			plantilla.setPropiedades(propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.PLANTILLA, dto.getPlantilla(), null,  null));
		}
		gestionarRol(dto, token);

		PropiedadDTO categoria = Propiedades.obtenerParametro(plantilla, Propiedades.PLANTILLA_TIPO_PRODUCTO);
		if(categoria!=null) productoService.crearDesdeDocumento(dto, categoria.getValor());
		if( Propiedades.obtenerParametro(plantilla, Propiedades.PLANTILLA_TIPO_BODEGA) !=null)bodegaService.crearDesdeDocumento(dto);
		//Queda pendiente que las cuentas contables se activen En cuenta auxiliar
	}
	
	public void gestionarRol(PedidoVentaDTO dto, String token) throws ServerException {
		//Valido que tenga relacion de plantilla
		RolAccesoFilterDTO dpiRolFilter = new RolAccesoFilterDTO();
		dpiRolFilter.setPlantilla(dto.getPlantilla());
		dpiRolFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		RolAccesoDTO dpiRol = rolService.consultaUnica(dpiRolFilter);
		if(dpiRol==null) return;
		
		if(dto.getEstado()==null || dto.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)==0){
			//Obtengo los valores de Id y nombre
			String usrNombre = dto.getDescripcion();
			if(usrNombre==null) throw new ServerException("revise la configuracion del nombre del recurso");
			String usrId = null;
			//En casos que el mismo usuario se coloque varias veces en un mismo formulario x ejemplo contactos de varios proyectos
			String campoConsecutivo = propiedadService.obtenerUnica(PropiedadValorDefinidoDTO.PLANTILLA, dto.getPlantilla(), Propiedades.CONSECUTIVO, getUserFlex(token));
			if(campoConsecutivo == null) throw new ServerException("Se debe configurar la propiedad consecutivo para obtener el id del usuario");
			//Cuando se gestiona el proceso para activar el usuario pasa que no vienen las caracteristicas
			if(dto.getCaracteristicas()==null) dto.setCaracteristicas(pedidoVentaCaracteristicaService.listar2Documento(dto.getLlaveTabla(), dto.getHistorico()));
			if(dto.getCaracteristicas().size()==0) throw new ServerException("Se debe colocar la caracteristica nombre del documento");
			for (PedidoVentaCaracteristicaDTO pvc : dto.getCaracteristicas()) {
				if(pvc.getCampo().compareTo(campoConsecutivo)==0){
					if(pvc.getCampoDTO()==null) pvc.setCampoDTO(documentoPlantillaCaracteristicaService.consultaXId(pvc.getCampo()));
					switch (pvc.getCampoDTO().getFormato()){
						case DocumentoPlantillaCaracteristicaDTO.NUMERO:
							usrId = String.valueOf(pvc.getValorNumero().longValue());
							break;
						case DocumentoPlantillaCaracteristicaDTO.TEXTO:
							usrId = pvc.getValorText();
							break;
						default:
							throw new ServerException("El componente no es tipo texto o numero");
					}
					break;
				}
			}
			if(usrId==null) throw new ServerException("revise la configuracion del id del recurso");
			//Consulto si el documento ya tiene una relacion con un rol
			UsuarioDTO usr = null;
			UsuarioRolFilterDTO urFilter = new UsuarioRolFilterDTO();
			urFilter.setDocumento(dto.getLlaveTabla());
			urFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			UsuarioRolDTO ur = usuarioRolService.consultaUnica(urFilter);
			if(ur == null){
				//Si no tengo relacion, busco usuario y creo relacion
				UsuarioFilterDTO usrFilter = new UsuarioFilterDTO();
				usrFilter.setIdentificacion(usrId);
				usr = usuarioService.consultaUnica(usrFilter);
				if(usr == null){
					usr = new UsuarioDTO();
					usr.setIdentificacion(usrId);
					usr.setNombre(usrNombre);
					usr.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
					usr = usuarioService.guardar(usr, token);
				}else {
					if(usr.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0) {
						usr.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
						usr = usuarioService.actualizar(usr, token);
					}
				}
				//Creo la reacion del rol con el documento
				ur = new UsuarioRolDTO();
				ur.setUsuario(usr.getLlaveTabla());
				ur.setRolAcceso(dpiRol.getLlaveTabla());
				ur.setDocumento(dto.getLlaveTabla());
				ur = usuarioRolService.guardar(ur, token);
			}else{
				usr = usuarioService.consultaXId(ur.getUsuario());
			}

			//3. actualizo nombre y el id
			if(usr.getNombre().compareTo(usrNombre)!=0 || usr.getIdentificacion().compareTo(usrId)!=0){
				UsuarioDTO usrActualizar = new UsuarioDTO();
				usrActualizar.setEstado(usr.getEstado());
				usrActualizar.setIdentificacion(usrId);
				usrActualizar.setNombre(usrNombre);
				usrActualizar.setLlaveTabla(usr.getLlaveTabla());
				usrActualizar.setImagen(usr.getImagen());
				usuarioService.actualizar(usrActualizar, token);
			}
		}else{
			UsuarioRolFilterDTO rolFilter = new UsuarioRolFilterDTO();
			rolFilter.setDocumento(dto.getLlaveTabla());
			rolFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			UsuarioRolDTO rol = usuarioRolService.consultaUnica(rolFilter);
			if(rol!=null){
				usuarioRolService.inactivar(rol, token);
			}
		}
	}
	
	
	
	public PedidoVentaDTO consultaXIdConDinero(String llave) throws ServerException {
		PedidoVentaDTO result = consultaXId(llave);
		if(result!=null){
			result.setDinero(dineroService.consultaPorDocumento(llave,  result.getHistorico()));
		}
		return result;
	}
	
	public List<PedidoVentaDTO> listarExpedientesPertenecenCampo(String dto, String token, String campoValor)throws ServerException{
		if(dto==null) return null;
		try {
			return listadoCompleto(pedidoVentaMapper.listarExpedientesPertenecenCampo(dto), token, campoValor); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public List<PedidoVentaDTO> listarExpedientesDisponiblesDocumentoFuncion(PedidoVentaFilterDTO dto, String funcionBusqueda, List<PedidoVentaCaracteristicaDTO> parametros)throws ServerException{
		paginar(dto);
		if(funcionBusqueda!=null) {// && funcionBusqueda.compareTo(ConstantesGenerales.OK)!=0) {
			try {
				List<String>filtrosEstado = organizarFiltros(dto);//Todo esto se hizo porque se null el valor opcion sucede que usabamos == y tocaba choose
				funcionBusqueda = SoftureUtil.formatFunction(funcionBusqueda);
				if(dto.getFiltroParametro()!=null) dto.setFiltroParametro(SoftureUtil.formatSimpleFunction(dto.getFiltroParametro()).toUpperCase());//Yo tenia el normalize por BD pero no fue una buena practica porque consume mucha memoria
				return pedidoVentaMapper.listarExpedientesDisponiblesDocumentoFuncion(dto, funcionBusqueda , filtrosEstado, parametros);
			}catch (Exception e) {
				throw new ServerException(e.getMessage());
			}
		}
		if(dto.getPlantilla()==null) throw new ServerException("Revise la plantilla para consultar los expedientes diponibles. Debe traer la plantilla actual para filtrar las transiciones validas");
		try {
			return pedidoVentaMapper.listarExpedientesDisponiblesDocumento(dto);
		}catch (Exception e) {
			throw new ServerException(e.getMessage());
		}
	}
	
	public PedidoVentaDTO obtenerCamposCompletos(PedidoVentaDTO pedido, String token)throws ServerException {
		//Caracteristicas
		if(pedido==null || pedido.getPlantilla()==null) throw new ServerException("Desarrollador el pedido y su plantilla no deben venir nulos");
		DocumentoPlantillaCaracteristicaFilterDTO rcDTOFilter = new DocumentoPlantillaCaracteristicaFilterDTO();
		rcDTOFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		rcDTOFilter.setPlantilla(pedido.getPlantilla());
		List<DocumentoPlantillaCaracteristicaDTO> camposBase =  documentoPlantillaCaracteristicaService.listarConsulta(rcDTOFilter);
		if(camposBase!=null & camposBase.size()!=0){
			List<PedidoVentaCaracteristicaDTO> caracteristicasActuales = pedidoVentaCaracteristicaService.listar2Documento(pedido.getLlaveTabla(), pedido.getHistorico());
			pedido.setCaracteristicas(new ArrayList<PedidoVentaCaracteristicaDTO>());
			PedidoVentaCaracteristicaDTO uc = null;
			for (DocumentoPlantillaCaracteristicaDTO documentoCaracteristicaDTO : camposBase) {
				uc=null;
				for (PedidoVentaCaracteristicaDTO pedidoCaracteristica : caracteristicasActuales){
					if(pedidoCaracteristica.getCampo().compareTo(documentoCaracteristicaDTO.getLlaveTabla())==0){
						uc = pedidoCaracteristica;
						break;
					}
				}
				if(uc==null)uc= new PedidoVentaCaracteristicaDTO();
				uc.setCampo(documentoCaracteristicaDTO.getLlaveTabla());
				//documentoCaracteristicaDTO.setRol(pedido.getRol());
				uc.setCampoDTO(documentoCaracteristicaDTO);
				uc.setDocumento(pedido.getLlaveTabla());
				adaptador.cargarConsultaCampo(uc, token);
				pedido.getCaracteristicas().add(uc);
			}
		}
		return pedido;
	}
	
	public void actualizarEstadosNuevoProceso(PedidoVentaDTO dto)throws ServerException{
		if(dto==null) return;
		try {
			pedidoVentaMapper.actualizarEstados(dto);
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public int listarEstadosNuevoProceso(PedidoVentaDTO dto)throws ServerException{
		if(dto==null) return 1000;
		try {
			return pedidoVentaMapper.contarEstados(dto);
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	private List<String> organizarFiltros(PedidoVentaFilterDTO dto) {
		List<String> estadosFiltro = null;
		if(dto.getEstadoExpediente()!=null && dto.getEstadoExpediente().contains(";")) {
			if(dto.getEstadoExpediente().startsWith(";"))dto.setEstadoExpediente(dto.getEstadoExpediente().substring(1));
			estadosFiltro = Arrays.asList(dto.getEstadoExpediente().split(";"));
			dto.setEstadoExpediente(null);
		}
		return estadosFiltro;
	}
	
	private List<String> organizarFiltroComas(PedidoVentaFilterDTO dto) {
		List<String> estadosFiltro = null;
		if(dto.getFiltroParametro()!=null && dto.getFiltroParametro().contains(",")) {
			if(dto.getFiltroParametro().startsWith(","))dto.setFiltroParametro(dto.getFiltroParametro().substring(1));
			estadosFiltro = Arrays.asList(dto.getFiltroParametro().split(","));
			dto.setFiltroParametro(null);
		}
		return estadosFiltro;
	}
	
	private void validarDobleCodigoActivo(PedidoVentaDTO pedido, String codigoNuevo)throws ServerException {
		PedidoVentaFilterDTO filtroNombreFilter = new PedidoVentaFilterDTO();//Valido que no existan documentos con el mismo nombre ni cerrados ni activos
		filtroNombreFilter.setNombre(codigoNuevo);
		filtroNombreFilter.setPlantilla(pedido.getPlantilla());
		List<PedidoVentaDTO> mismoNombre = listarConsulta(filtroNombreFilter);
		if(mismoNombre!=null && ! mismoNombre.isEmpty()){
			for(PedidoVentaDTO igualNombre : mismoNombre){
				if(pedido.getLlaveTabla()==null || pedido.getLlaveTabla().compareTo(igualNombre.getLlaveTabla())!=0){
					if(igualNombre.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO)!=0) {
						DocumentoPlantillaDTO plantilla = documentoPlantillaService.consultaXId(pedido.getPlantilla());						
						throw new ServerException("Ya existe un " + plantilla.getNombre() +" con el mismo codigo ("+ igualNombre.getNombre()+"). Creado el " + SoftureUtil.formatDateTime(igualNombre.getFechaRegistro()) + " con estado " + igualNombre.getEstado());	
					}
				}
			}
		}
	}
	
	public List<PedidoVentaDTO> iteracionesProceso(String sqlFuncionDecision, String llaveTablaDocumento, 
			String llaveTablaModificador) throws ServerException{
		List<PedidoVentaDTO> result = null;
		try {
			result = pedidoVentaMapper.iteracion(sqlFuncionDecision, llaveTablaDocumento, llaveTablaModificador);
		} catch (Exception e) {
			throw new ServerException(e.getMessage(), "Funcion de Iteracion con errores: ");
		}
		return result;
	}
	
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaDTO inactivateDocumentWithProcess(PedidoVentaDTO documentDTO, PedidoVentaDTO updaterDTO, String token) throws ServerException {
		// BEGIN PedidoVenta_inactivar
		PedidoVentaDTO bd = consultaXId(documentDTO.getLlaveTabla());
		if(bd.getEstadoExpediente()!=null)
			throw new ServerException("Para inactivar el expediente se debe usar un documento de transicion de estado");
		documentDTO = obtenerCamposCompletos(documentDTO, token);
		String transaccion = documentDTO.getTransaccion();
		if(transaccion == null) transaccion = transaccionSvc.crear(token).getLlaveTabla();
		for(PedidoVentaCaracteristicaDTO iterador: documentDTO.getCaracteristicas()){
			if(iterador.getCampoDTO()==null)iterador.setCampoDTO(documentoPlantillaCaracteristicaService.consultaXId(iterador.getCampo()));
			iterador.setTransaccionInactivo(transaccion);
			adaptador.inactivar(iterador, updaterDTO, token);
		}// El inactivar va e intenta gestionar los productos y proceso ()
	
		documentDTO = inactivate(documentDTO);
		gestionarTipos(documentDTO, null, token);
		/*if(Propiedades.obtenerParametro(dto.getPlantilla(), Propiedades.PLANTILLA_TIPO_CUENTA) !=null) {
			cuentaService.inactivarDocumento(dto);
		}*/
		return documentDTO;
		// END PedidoVenta_inactivar
	}
	
	//
	public List<PedidoVentaDTO> listarTareasOtroUsuario(String usuario)throws ServerException{
		// BEGIN region listarUsuario
		PedidoVentaFilterDTO filter = new PedidoVentaFilterDTO();
		filter.setFuncionario(usuario);
		paginar(filter);
		try {
			return pedidoVentaMapper.listarUsuario(filter); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		// END region listarUsuario
	}
	
	public List<PedidoVentaDTO> listar2Activity(List<String> ids, String token)throws ServerException{
		return listadoCompleto(pedidoVentaMapper.listar2Ids(ids), token, null);
	}
	
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaDTO guardarAPI(PedidoVentaDTO dto, String token) throws ServerException {
		DocumentoTransaccionDTO tran = transaccionSvc.crear(token);
		dto.setTransaccion( tran.getLlaveTabla() );
		PedidoVentaDTO result = guardar(dto, token);
		tran.setFechaFin(new Date());
		transaccionSvc.update(tran);
		return result;
	}
// END region aditionalMethods

}