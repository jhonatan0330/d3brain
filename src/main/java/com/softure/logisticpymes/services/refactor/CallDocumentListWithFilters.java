package com.softure.logisticpymes.services.refactor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.domain.dto.CuentaDTO;
import com.softure.logisticpymes.domain.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.domain.dto.DocumentoPlantillaDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaDineroDTO;
import com.softure.logisticpymes.domain.dto.PropiedadDTO;
import com.softure.logisticpymes.domain.dto.PropiedadValorDefinidoDTO;
import com.softure.logisticpymes.domain.filter.CuentaFilterDTO;
import com.softure.logisticpymes.domain.filter.DocumentoPlantillaCaracteristicaFilterDTO;
import com.softure.logisticpymes.domain.filter.PedidoVentaCaracteristicaFilterDTO;
import com.softure.logisticpymes.domain.filter.PedidoVentaFilterDTO;
import com.softure.logisticpymes.infrastructure.mybatis.mapper.PedidoVentaMapper;
import com.softure.logisticpymes.services.CuentaSvc;
import com.softure.logisticpymes.services.DocumentoPlantillaCaracteristicaSvc;
import com.softure.logisticpymes.services.PedidoVentaCaracteristicaSvc;
import com.softure.logisticpymes.services.PedidoVentaDineroSvc;
import com.softure.logisticpymes.services.PedidoVentaSvc;
import com.softure.logisticpymes.services.PropiedadSvc;
import com.softure.logisticpymes.services.RolAccesoSvc;
import com.softure.logisticpymes.services.adapter.Propiedades;

@Component
public class CallDocumentListWithFilters {

	@Autowired private PedidoVentaMapper pedidoVentaMapper;
	@Autowired private PropiedadSvc propiedadService;
	@Autowired private PedidoVentaSvc pedidoVentaService;
	@Autowired private DocumentoPlantillaCaracteristicaSvc documentoPlantillaCaracteristicaService;
	@Autowired private PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService;
	@Autowired private RolAccesoSvc rolService;
	@Autowired private CuentaSvc cuentaService;
	@Autowired private PedidoVentaDineroSvc dineroService;
	
	public List<PedidoVentaDTO> listarAvanzado(PedidoVentaFilterDTO dto)throws ServerException{
		if(dto==null) throw new ServerException("Tronco de error");
		System.out.println (new Date().toString() + " : ListarAvanzado (" + dto.getPlantilla() +"), llave (" + dto.getLlaveTabla() + "), filtro ( " + dto.getFiltroParametro() + "), nombre ( " + dto.getNombre() + ")");
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
				List<PropiedadDTO> auxiliaresPlantilla = Propiedades.obtenerVariosParametro(campoPlantilla, Propiedades.PLANTILLA_AUXILIAR);
				String propiedadMultiple = Propiedades.obtenerValor(campoPlantilla, Propiedades.MULTIPLE);
				if(!propiedadMultiple.isEmpty()) {
					PropiedadDTO propiedadFuncion = Propiedades.obtenerParametro(campoPlantilla, Propiedades.PROCESO_FUNCION_SQL);
					if(propiedadFuncion!=null || (auxiliaresPlantilla==null || auxiliaresPlantilla.isEmpty())) {
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
				if(auxiliaresPlantilla!=null && !auxiliaresPlantilla.isEmpty()){
					List<PedidoVentaDTO> resultManyTemplates = new ArrayList<>(); 
					for (PropiedadDTO iProp : auxiliaresPlantilla) {
						resultManyTemplates.addAll( readResultByTemplate(dto, iProp.getValor()) );
					}
					return resultManyTemplates;
				}
			}
		}
		return readResultByTemplate(dto, dto.getPlantilla());
	}

	private List<PedidoVentaDTO> readResultByTemplate(PedidoVentaFilterDTO dtoFilter, String templateFilter) throws ServerException {
		PedidoVentaFilterDTO filterDTO = new PedidoVentaFilterDTO();
		filterDTO.setFiltroParametro(dtoFilter.getFiltroParametro());
		filterDTO.setEstado(dtoFilter.getEstado());
		filterDTO.setCampoOrigen(dtoFilter.getCampoOrigen());
		String secToken =null;
		String campoFiltro = null;
		filterDTO.setPlantilla(templateFilter);
		filterDTO.setFuncionarioNombre(null);
		// Aqui cometi un error en los campos dependientes asi que toca copiar las caracteristicas como vienen
		// Algun dia mejorare esos dependientes
		filterDTO.setCaracteristicas(dtoFilter.getCaracteristicas());
		String token = dtoFilter.getSecurityToken();
		
		filterDTO.setPaginacionRegistroInicial(dtoFilter.getPaginacionRegistroInicial());
		filterDTO.setPaginacionRegistroFinal(dtoFilter.getPaginacionRegistroFinal());
		pedidoVentaService.paginar(filterDTO);
		DocumentoPlantillaDTO plantilla = null;//Es para almacenar las propiedades soloque tengo que pasar un BasicaPAram porque iba a pasar solo las propiedades
		//Consulto que la plantilla solicitada tenga permisos
		if(templateFilter!=null) {// && dto.getLlaveTabla()==null){ OJO tengo que revisar poruqe tengo esto
			boolean verTodos = false;
			if(rolService.usuarioPermisosCompletos(token)) {
				verTodos = true;
			}else {
				plantilla = new DocumentoPlantillaDTO();
				plantilla.setPropiedades( propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.PLANTILLA, templateFilter, null, pedidoVentaService.getUserFlex(token)) );		
				List<PropiedadDTO> propiedadesVerTodos = Propiedades.obtenerVariosParametro(plantilla, Propiedades.PERMISO_PLANTILLA_VER_TODOS);
				if(propiedadesVerTodos!=null && !propiedadesVerTodos.isEmpty()){
					verTodos = true;
				}else{
					//Si tiene funcion entonces se omite el resto de la parametrizacion
					PropiedadDTO propiedadFuncion = Propiedades.obtenerParametro(plantilla, Propiedades.PROCESO_FUNCION_SQL);
					if(propiedadFuncion!=null) {
						return listadoCompleto( listarExpedientesDisponiblesDocumentoFuncion(filterDTO, propiedadFuncion.getLlaveTabla(), null), token, null );
					}
					
					List<PropiedadDTO> propiedadesFiltro = Propiedades.obtenerVariosParametro(plantilla, Propiedades.PERMISO_PLANTILLA_CAMPO_FILTRO);
					if(propiedadesFiltro!=null && !propiedadesFiltro.isEmpty()){
						campoFiltro = propiedadesFiltro.get(0).getValor();
					}
				}			
			}
			
			if(dtoFilter.getCaracteristicas()==null){
				if(verTodos ){
					secToken = null;
				}else{
					if(campoFiltro==null){
						filterDTO.setFuncionario(pedidoVentaService.getUserFlex(token));
						//Coloco el filtro por el mismo
						filterDTO.setCaracteristicas(new ArrayList<PedidoVentaCaracteristicaDTO>());
						PedidoVentaCaracteristicaDTO pvc = new PedidoVentaCaracteristicaDTO();
						filterDTO.getCaracteristicas().add(pvc);
						//SE coloca esta linea debidoa que se debe filtrar por los permitidos por usuario cuando no tiene el check de vere todos
						secToken = token;
					}
				}
			}else{
				filterDTO.setFuncionarioNombre(dtoFilter.getCaracteristicas().get(0).getValorOpcion());
				//Se coloca porque en un form pedido que lista por vendedor trae todos los vendedores
				if(dtoFilter.getFuncionarioNombre()==null && !verTodos) secToken = token;
			}
		}
		
		if(dtoFilter.getNombre()!=null){
			PedidoVentaFilterDTO filtro = new PedidoVentaFilterDTO();
			filtro.setNombre(dtoFilter.getNombre().toUpperCase());
			filtro.setPlantilla(templateFilter);
			filtro.setFuncionarioNombre(dtoFilter.getFuncionarioNombre());
			filtro.setFuncionario(filterDTO.getFuncionario()); //No me encontraba una guia con el usuario
			filtro.setSecurityToken(secToken);
			filtro.setCaracteristicas(filterDTO.getCaracteristicas());
			try {
				return listadoCompleto(pedidoVentaMapper.listarPermitidos(filtro, null, null, null, null, null, null), token, null); 
			}catch (Exception e) {
				throw new ServerException(e.getCause().getMessage());
			}
		}else{
			String orden = null;
			String ordenAscendente = null;
			//Esto filtra los resultados por estado, pero si va a consultar un solo registro mejor lo dejo solo para que sea consulta por id
			List<String> estadosFiltro = organizarFiltros(dtoFilter);
			if(dtoFilter.getLlaveTabla()==null){
				if(templateFilter==null) { // Esto es para los procesos deben traer los estados
					if (estadosFiltro == null) throw new ServerException("Por favor revise porque el campo no tiene plantilla");
				} else {
					//DocumentoPlantillaDTO plantillaFiltro = documentoPlantillaService.consultaXId(templateFilter);
					//if(plantillaFiltro==null) throw new ServerException("Por favor revise el id de la plantilla porque no se encuentra");
					if(plantilla ==null) {
						plantilla = new DocumentoPlantillaDTO();
						plantilla.setPropiedades( propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.PLANTILLA, templateFilter, null, pedidoVentaService.getUserFlex(token)));
					}
					PropiedadDTO filtroFechas = Propiedades.obtenerParametro(plantilla,  Propiedades.SOLICITAR_FECHAS);
					filterDTO.setFechaMax(dtoFilter.getFechaMax());
					filterDTO.setFechaMin(dtoFilter.getFechaMin());
					if(filtroFechas!=null) {
						if(filterDTO.getFechaMin()==null) throw new ServerException("Por favor seleccione fecha de inicio para la consulta");
						if(filterDTO.getFechaMax()==null) throw new ServerException("Por favor seleccione fecha de fin para la consulta");	
					}
					orden = Propiedades.obtenerValor(plantilla, Propiedades.ORDEN);
					if(orden.isEmpty())orden = null;
					ordenAscendente = Propiedades.obtenerValor(plantilla,  Propiedades.ORDEN_DESCENDENTE);
					if(ordenAscendente.isEmpty())ordenAscendente = null;
				}
				
			}else {
				filterDTO.setLlaveTabla(dtoFilter.getLlaveTabla());
				filterDTO.setFiltroParametro(null);
			}
			filterDTO.setEstadoExpediente(dtoFilter.getEstadoExpediente());
			List<String> textoFiltroComas = organizarFiltroComas(dtoFilter);
			filterDTO.setSecurityToken(secToken);
			if(campoFiltro !=null ) {
				return listadoCompleto(
						pedidoVentaMapper.listarPermitidosPorCampoFiltro(filterDTO, estadosFiltro, orden, ordenAscendente, textoFiltroComas, pedidoVentaService.getUserFlex(token), campoFiltro)
						, token, null);	
			}
			return listadoCompleto(
					pedidoVentaMapper.listarPermitidos(filterDTO, estadosFiltro, null, null , orden, ordenAscendente, textoFiltroComas)
					, token, null); 
		}
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
	
	public List<PedidoVentaDTO> listarExpedientesDisponiblesDocumentoFuncion(PedidoVentaFilterDTO dto, String funcionBusqueda, List<PedidoVentaCaracteristicaDTO> parametros)throws ServerException{
		pedidoVentaService.paginar(dto);
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
	
	private List<String> organizarFiltros(PedidoVentaFilterDTO dto) {
		List<String> estadosFiltro = null;
		if(dto.getEstadoExpediente()!=null && dto.getEstadoExpediente().contains(";")) {
			if(dto.getEstadoExpediente().startsWith(";"))dto.setEstadoExpediente(dto.getEstadoExpediente().substring(1));
			estadosFiltro = Arrays.asList(dto.getEstadoExpediente().split(";"));
			dto.setEstadoExpediente(null);
		}
		return estadosFiltro;
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
					PropiedadDTO propiedadRender = propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.PLANTILLA, iterador.getPlantilla(), Propiedades.PLANTILLA_RENDER_ESPECIAL_SQL, pedidoVentaService.getUserFlex(securityToken));
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
	
	public List<PedidoVentaDTO> listarExpedientesPertenecenCampo(String dto, String token, String campoValor)throws ServerException{
		if(dto==null) return null;
		try {
			return listadoCompleto(pedidoVentaMapper.listarExpedientesPertenecenCampo(dto), token, campoValor); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public List<PedidoVentaDTO> listar2Activity(List<String> ids, String token)throws ServerException{
		return listadoCompleto(pedidoVentaMapper.listar2Ids(ids), token, null);
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

	public List<PedidoVentaDTO> listarUsuario(PedidoVentaFilterDTO dto)throws ServerException{
		if(dto.getFuncionario()==null) dto.setFuncionario(pedidoVentaService.getUserFlex(dto.getSecurityToken()));
		pedidoVentaService.paginar(dto);
		try {
			return listadoCompleto(pedidoVentaMapper.listarUsuario(dto), dto.getSecurityToken(), null); 
		}catch (Exception e) {
			throw new ServerException(e.getMessage());
		}
	}

}
