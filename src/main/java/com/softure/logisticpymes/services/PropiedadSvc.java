package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.util.ArrayList;
import java.util.Date;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.dto.BasicParamDTO;
import com.softure.logisticpymes.dto.BodegaDTO;
import com.softure.logisticpymes.dto.CategoriaProductoDTO;
import com.softure.logisticpymes.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.dto.DocumentoPlantillaDTO;
import com.softure.logisticpymes.dto.MensajePlantillaCorreoDTO;
import com.softure.logisticpymes.dto.ProcesoTransicionDTO;
import com.softure.logisticpymes.dto.ProductoCaracteristicaDTO;
import com.softure.logisticpymes.dto.ProductoDTO;
import com.softure.logisticpymes.dto.TarifarioDTO;
import com.softure.logisticpymes.dto.UsuarioDTO;
import com.softure.logisticpymes.dto.WebServiceDTO;
import com.softure.logisticpymes.dto.PropiedadValorDefinidoDTO;
import com.softure.logisticpymes.dto.RelacionInternaDTO;
import com.softure.logisticpymes.dto.ReporteBaseDTO;
import com.softure.logisticpymes.dto.RolAccesoDTO;
import com.softure.logisticpymes.dto.filter.BodegaFilterDTO;
import com.softure.logisticpymes.dto.filter.CategoriaProductoFilterDTO;
import com.softure.logisticpymes.dto.filter.DocumentoPlantillaFilterDTO;
import com.softure.logisticpymes.dto.filter.DocumentoPlantillaCaracteristicaFilterDTO;
import com.softure.logisticpymes.dto.filter.MensajePlantillaCorreoFilterDTO;
import com.softure.logisticpymes.dto.filter.ReporteBaseFilterDTO;
import com.softure.logisticpymes.dto.filter.ProductoCaracteristicaFilterDTO;
import com.softure.logisticpymes.dto.filter.PropiedadValorDefinidoFilterDTO;
import com.softure.logisticpymes.dto.filter.RolAccesoFilterDTO;
import com.softure.logisticpymes.dto.filter.TarifarioFilterDTO;
import com.softure.logisticpymes.dto.filter.UsuarioFilterDTO;
import com.softure.logisticpymes.dto.filter.WebServiceFilterDTO;
import com.softure.logisticpymes.services.adapter.Propiedades;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.PropiedadDTO;
import com.softure.logisticpymes.dto.filter.PropiedadFilterDTO;
import com.softure.logisticpymes.persistence.PropiedadMapper;

@Service("propiedadService")
public class PropiedadSvc extends BasicSvc<PropiedadDTO, PropiedadFilterDTO> {
	
	@Autowired
	private PropiedadMapper propiedadMapper;
	
	// BEGIN region servicesPropiedad
	@Autowired private BodegaSvc bodegaService;
	@Autowired private CambioSvc cambioService;
	@Autowired private CategoriaProductoSvc categoriaProductoService;
	@Autowired private DocumentoPlantillaCaracteristicaSvc campoService;
	@Autowired private DocumentoPlantillaSvc plantillaService;
	@Autowired private MensajePlantillaCorreoSvc mensajeService;
	@Autowired private ProcesoTransicionSvc transicionService;
	@Autowired private ProcesoTransicionAutomaticaSvc automatizadorService;
	@Autowired private ProductoSvc productoService;
	@Autowired private ProductoCaracteristicaSvc productoCaracteristicaService;
	@Autowired private PropiedadValorDefinidoSvc valorDefinidoService;
	@Autowired private TarifarioSvc tarifarioService;
	@Autowired private ReporteBaseSvc reporteService;
	@Autowired private RolAccesoSvc rolService;
	@Autowired private RelacionInternaSvc relacionService;
	@Autowired private UsuarioSvc usuarioService;
	@Autowired private WebServiceSvc apiService;
	// END region servicesPropiedad

	@Override
	public PropiedadDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. Propiedad");
		PropiedadFilterDTO dto = new PropiedadFilterDTO();
		dto.setLlaveTabla(llave);
		return propiedadMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = propiedadMapper;
	}
	
	@Override
	public PropiedadDTO activar(PropiedadDTO dto, String token) throws ServerException {
		// BEGIN Propiedad_activar
		return super.activar(dto, token);
		// END Propiedad_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PropiedadDTO actualizar( PropiedadDTO dto, String token) throws ServerException {
		// BEGIN Propiedad_actualizar
		String llaveTabla = dto.getLlaveTabla();
		dto = guardar(dto, token);
		List<RelacionInternaDTO> relaciones = relacionService.relacionesPropiedad(llaveTabla);
		if(relaciones!=null && !relaciones.isEmpty()) {
			for (RelacionInternaDTO relacionInternaDTO : relaciones) {
				if(dto.getValor().compareTo(relacionInternaDTO.getCampo())!=0) {
					RelacionInternaDTO nueva = new RelacionInternaDTO();
					nueva.setPropiedad(dto.getLlaveTabla());
					nueva.setPlantilla(relacionInternaDTO.getPlantilla());
					nueva.setCampo(relacionInternaDTO.getCampo());
					relacionService.guardar(nueva, token);
				}
			}
		}
		PropiedadDTO inactivo = new PropiedadDTO();
		inactivo.setLlaveTabla(llaveTabla);
		inactivar(inactivo, token);
		return dto;
		// END Propiedad_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PropiedadDTO inactivar(PropiedadDTO dto, String token) throws ServerException {
		// BEGIN Propiedad_inactivar
		PropiedadDTO bd = consultaXId(dto.getLlaveTabla());
		bd.setCambioEliminacion(cambioService.obtenerCambioGrabando(token).getLlaveTabla());
		if(bd.getKey()==null) {
			PropiedadValorDefinidoDTO valorDefinido = valorDefinidoService.consultaXId(bd.getPropiedadValor());
			bd.setTipo(valorDefinido.getOrigen());
			bd.setKey(valorDefinido.getCodigo());
		}
		bd.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
		bd = super.update(bd);
		if(bd.getKey().contains("SQL")) {
			bd.setLlaveTabla(SoftureUtil.formatFunction(bd.getLlaveTabla()));
			switch (bd.getKey()) {
			case Propiedades.TABLERO_CONTROL_SQL:
			case Propiedades.PROCESO_FUNCION_SQL:
				propiedadMapper.eliminarFuncionFiltros(bd);
				break;
			case Propiedades.PRODUCTOS_FUNCION_SQL:
				propiedadMapper.eliminarFuncionProductos(dto);
				break;
			case Propiedades.DECISION_SQL:
				propiedadMapper.eliminarFuncionDecision(dto);
				break;
			case Propiedades.ITERACION_SQL:
				propiedadMapper.eliminarFuncionDecision(dto);
				break;
			case Propiedades.DETALLE_TARIFARIO_SQL:
				propiedadMapper.eliminarFuncionTarifas(dto);
				break;
			case Propiedades.NUMERO_FUNCION_SQL:
				propiedadMapper.eliminarFuncionNumerica(dto);
				break;
			case Propiedades.GENERA_DOCUMENTO_FUNCION_SQL:
				propiedadMapper.eliminarFuncionCampoGenerar(dto);
				break;
			case Propiedades.PLANTILLA_RENDER_ESPECIAL_SQL:
				propiedadMapper.eliminarFuncionCamposEspecialesPlantilla(dto);
				break;
			case Propiedades.DISPONIBILIDAD_FUNCION_SQL:
				propiedadMapper.eliminarFuncionNumerica(dto);
				break;
			default:
				propiedadMapper.eliminarFuncion(bd);
				break;
			}
		}
		if(bd.getKey().compareTo(Propiedades.FILTRO)==0)
			campoService.actualizarFiltros(dto.getCampo());
		if(bd.getKey().compareTo(Propiedades.TEMPORIZADOR)==0) {
			automatizadorService.inactivarPropiedad(bd.getLlaveTabla());
			propiedadMapper.eliminarFuncionFiltros(bd);
		}
		List<RelacionInternaDTO> relaciones = relacionService.relacionesPropiedad(bd.getLlaveTabla());
		if(relaciones!=null && !relaciones.isEmpty()) {
			for (RelacionInternaDTO relacionInternaDTO : relaciones) {
				relacionService.inactivar(relacionInternaDTO, token);
			}
		}
		return bd;
		// END Propiedad_inactivar
	}
	
	@Override
	public PropiedadDTO consultaUnica(PropiedadFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(PropiedadFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<PropiedadDTO> listarConsulta(PropiedadFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PropiedadDTO guardar(PropiedadDTO dto, String token) throws ServerException {
		// BEGIN Propiedad_guardar
		if(dto.getPropiedadValor()==null)
			dto.setPropiedadValor(consultarValorDefinido(dto.getTipo(), dto.getKey()).getLlaveTabla());
		PropiedadValorDefinidoDTO valorDefinido = valorDefinidoService.consultaXId(dto.getPropiedadValor());
		if(valorDefinido==null) throw new ServerException("No se encuentra la propiedad con Id " + dto.getPropiedadValor());
		dto.setTipo(valorDefinido.getOrigen());
		dto.setKey(valorDefinido.getCodigo());
		if(dto.getValor().compareTo("-help")==0) throw new ServerException("Ayuda de " + dto.getKey() + "\n\n\n" + Propiedades.instrucciones(dto.getKey()));
		dto.setCambioCreacion(cambioService.obtenerCambioGrabando(token).getLlaveTabla());
		if(!valorDefinido.getNecesitaDesarrollo())dto.setFechaImplementacion(new Date());
		if(valorDefinido.getSolicitaMotivo() && dto.getMotivo()==null) throw new ServerException("La propiedad necesita tener motivo. \n" + valorDefinido.getNombre() );
		if(!valorDefinido.getMultiple() && dto.getLlaveTabla()==null) {//Por el momento solo valida las nuevas
			PropiedadFilterDTO existeFilter = new PropiedadFilterDTO();
			existeFilter.setCampo(dto.getCampo());
			existeFilter.setPropiedadValor(dto.getPropiedadValor());
			existeFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			existeFilter.setRol(dto.getRol());
			existeFilter.setRolExcluyente(dto.getRolExcluyente());
			existeFilter.setUsuario(dto.getUsuario());
			existeFilter.setUsuarioExcluyente(dto.getUsuarioExcluyente());
			PropiedadDTO existe = consultaUnica(existeFilter);
			if(existe!=null) throw new ServerException("Esta propiedad ya fue definida");
		}else {
			dto.setLlaveTabla(null);
			//Falta validar que venga el mismo tipo para que no nos hagan gol
		}
		if(validar(dto, token))return null;
		dto.setFechaDefinicion(new Date());
		dto = super.guardar(dto, token);
		try {
			if(dto.getKey().contains("SQL")) {
				dto.setLlaveTabla(SoftureUtil.formatFunction(dto.getLlaveTabla()));
				switch (dto.getKey()) {
					case Propiedades.TABLERO_CONTROL_SQL:
					case Propiedades.PROCESO_FUNCION_SQL:
						propiedadMapper.crearFuncionFiltros(dto);
						break;
					case Propiedades.MENSAJE_DESTINATARIOS_SQL:
						propiedadMapper.crearFuncionMail(dto);
						break;
					case Propiedades.PRODUCTOS_FUNCION_SQL:
						propiedadMapper.crearFuncionProductos(dto);
						break;
					case Propiedades.DECISION_SQL:
						propiedadMapper.crearFuncionDecision(dto);
						break;
					case Propiedades.ITERACION_SQL:
						propiedadMapper.crearFuncionIteracion(dto);
						break;
					case Propiedades.DETALLE_TARIFARIO_SQL:
						propiedadMapper.crearFuncionTarifas(dto);
						break;
					case Propiedades.NUMERO_FUNCION_SQL:
						propiedadMapper.crearFuncionNumerica(dto);
						break;
					case Propiedades.GENERA_DOCUMENTO_FUNCION_SQL:
						propiedadMapper.crearFuncionCampoGenerar(dto);
						break;
					case Propiedades.PLANTILLA_RENDER_ESPECIAL_SQL:
						propiedadMapper.crearFuncionCamposEspecialesPlantilla(dto);
						break;
					case Propiedades.DISPONIBILIDAD_FUNCION_SQL:
						propiedadMapper.crearFuncionParametros(dto);
						break;
					default:
						propiedadMapper.crearFuncion(dto);
						break;
				}
			}
			if(dto.getKey().compareTo(Propiedades.TEMPORIZADOR)==0)propiedadMapper.crearFuncionFiltros(dto);
		}catch (Exception e) {
			throw new ServerException(e.getMessage(), "Funcion de SQL : " + dto.getMotivo());
		}
		if(dto.getKey().compareTo(Propiedades.FILTRO)==0)campoService.actualizarFiltros(dto.getCampo());
		if(dto.getKey().contains("PLANTILLA_TIPO")) {
			DocumentoPlantillaDTO plantillaPrincipal = plantillaService.consultaXId(dto.getCampo());
			switch (dto.getKey()) {
				case Propiedades.PLANTILLA_TIPO_BODEGA:
					break;
				case Propiedades.PLANTILLA_TIPO_CUENTA:
					break;
				case Propiedades.PLANTILLA_TIPO_PRODUCTO:
					break;
				case Propiedades.PLANTILLA_TIPO_REPORTE:
					ReporteBaseFilterDTO reporteFilter = new ReporteBaseFilterDTO();
					reporteFilter.setPlantilla(plantillaPrincipal.getLlaveTabla());
					if(reporteService.contarResultados(reporteFilter)==0) {
						ReporteBaseDTO reporte = new ReporteBaseDTO();
						reporte.setCodigo(plantillaPrincipal.getCodigo());
						reporte.setDescripcion("PENDIENTE");
						reporte.setNombre(plantillaPrincipal.getNombre());
						reporte.setPlantilla(plantillaPrincipal.getLlaveTabla());
						reporteService.guardar(reporte, token);
					}
					campoService.crearCampoTiempoReporte(plantillaPrincipal.getLlaveTabla(), token, true);
					PropiedadDTO historico = Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA, plantillaPrincipal.getLlaveTabla(), 
							Propiedades.PERIODO_LIMPIEZA_HISTORICO, "15", token);
					historico.setFechaInicial(new Date());
					historico.setMotivo("Pasar a tabla historico");
					historico.setTexto("00:00:07:00:00");
					guardar(historico , token);
					break;
				case Propiedades.PLANTILLA_TIPO_ROL:
					RolAccesoFilterDTO rolFiltroFilter = new RolAccesoFilterDTO();
					rolFiltroFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
					rolFiltroFilter.setPlantilla(plantillaPrincipal.getLlaveTabla());
					RolAccesoDTO rolFiltro = rolService.consultaUnica(rolFiltroFilter);
					if(rolFiltro==null) {// Si la propiedad ya se genero no hay que duplicar
						RolAccesoDTO nuevo = new RolAccesoDTO();
						nuevo.setPlantilla(plantillaPrincipal.getLlaveTabla());
						nuevo = rolService.guardar(nuevo, token);
						guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA, plantillaPrincipal.getLlaveTabla(), 
								Propiedades.ORDEN, "N", token), token);
						guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA, plantillaPrincipal.getLlaveTabla(), 
								Propiedades.DESCRIPCION, "*", token), token);
						guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA, plantillaPrincipal.getLlaveTabla(), 
								Propiedades.CONSECUTIVO, "*", token), token);						
					}
					break;
			}
		}
		relacionarCampo(dto, token);
		return dto;
		// END Propiedad_guardar
	}

// BEGIN region aditionalMethods
	
	private PropiedadValorDefinidoDTO consultarValorDefinido(String tipo, String key) throws ServerException {
		PropiedadValorDefinidoFilterDTO valorDefinidoFilter = new PropiedadValorDefinidoFilterDTO();
		valorDefinidoFilter.setCodigo(key);
		valorDefinidoFilter.setOrigen(tipo);
		PropiedadValorDefinidoDTO valorDefinido = valorDefinidoService.consultaUnica(valorDefinidoFilter);
		if(valorDefinido==null) throw new ServerException("No se encontro la propiedad " + key + " del tipo " + tipo);
		return valorDefinido;
	}

	private void identificadorRol(PropiedadDTO dto, String token) throws ServerException {
		RolAccesoDTO rol = rolService.consultaXId(dto.getValor());
		if(rol==null){
			identificadorPlantilla(dto, token);
			RolAccesoFilterDTO rolFilter = new RolAccesoFilterDTO();
			rolFilter.setPlantilla(dto.getValor());
			rolFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			rol = rolService.consultaUnica(rolFilter);
			if(rol==null) throw new ServerException("No se encontro rol con Id, nombre o Codigo que concuerde con el Rol");
		}
		dto.setValor( rol.getLlaveTabla() );
		dto.setTexto( rol.getNombre() );
	}
	
	
	private void identificadorPlantilla(PropiedadDTO dto, String token) throws ServerException {
		if(dto.getValor().compareTo("*")==0) {
			if(dto.getKey().compareTo(Propiedades.PLANTILLA_ANULAR)==0) {
				DocumentoPlantillaDTO plantillaPrincipal = plantillaService.consultaXId(dto.getCampo());
				DocumentoPlantillaDTO plantilla = new DocumentoPlantillaDTO();
				plantilla.setImagen(plantillaPrincipal.getImagen());
				plantilla.setNombre(plantillaPrincipal.getNombre() + " - ANULAR ");
				plantilla.setObjetivo(dto.getMotivo());
				plantilla = plantillaService.guardar(plantilla, token);
				plantillaService.crearCampoProcesos(plantilla.getLlaveTabla(), token);
				dto.setValor(plantilla.getLlaveTabla());
			}
		}
		DocumentoPlantillaDTO plantilla = buscarPlantilla(dto.getValor());
		if(plantilla==null) throw new ServerException("No se encontro plantilla con Id, nombre o Codigo que concuerde." + dto.getValor());
		dto.setValor( plantilla.getLlaveTabla() );
		dto.setTexto( plantilla.getNombre() );
	}
	
	private DocumentoPlantillaDTO buscarPlantilla(String valor) throws ServerException {
		DocumentoPlantillaDTO plantilla = plantillaService.consultaXId(valor);
		if(plantilla==null){//Consulto por nombre
			DocumentoPlantillaFilterDTO plantillaFilter = new DocumentoPlantillaFilterDTO();
			plantillaFilter.setNombre(valor.toUpperCase());
			plantillaFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			plantilla = plantillaService.consultaUnica(plantillaFilter);
			if(plantilla==null){//Consulto por codigo
				plantillaFilter = new DocumentoPlantillaFilterDTO();
				plantillaFilter.setCodigo(valor.toUpperCase());
				plantillaFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
				plantilla = plantillaService.consultaUnica(plantillaFilter);
			}			
		}
		return plantilla;
	}
	
	private boolean identificadorCampo(PropiedadDTO dto, String token) throws ServerException{
		DocumentoPlantillaCaracteristicaDTO campo = null; 
		if(dto.getValor().compareTo("*")==0) {//Si viene en cero se crea el campo
			switch (dto.getKey()){
				case Propiedades.DESCRIPCION : {dto.setValor( campoService.crearCampoNombre(dto.getCampo(), token) );break;}
				case Propiedades.TOTAL : {dto.setValor( campoService.crearCampoValor(dto.getCampo(), token) );break;}
				case Propiedades.CONSECUTIVO : {dto.setValor( campoService.crearCampoIdentificacion(dto.getCampo(), token) );break;}
				case Propiedades.FECHA : {dto.setValor( campoService.crearCampoTiempoReporte(dto.getCampo(), token, false) );break;}
				default : {throw new ServerException("Este campo no tiene opcion de crear el campo");}
			}
		}
		if(campo ==null)campo = campoService.consultaXId(dto.getValor());
		//Si es actualizar valido por el id
		if(campo==null) {
			String plantillaId = null;
			if(dto.getTipo().compareTo(PropiedadValorDefinidoDTO.CAMPO)==0) {
				if(dto.getKey().compareTo(Propiedades.DEPENDE)==0 
						|| dto.getKey().compareTo(Propiedades.MODIFICAR_CAMPO)==0 
						|| dto.getKey().compareTo(Propiedades.PRODUCTOS_FUNCION_CAMPO)==0
						|| dto.getKey().compareTo(Propiedades.RELACIONAR_DOCUMENTOS)==0
						|| dto.getKey().compareTo(Propiedades.DISPONIBILIDAD_CROQUIS)==0
						|| dto.getKey().compareTo(Propiedades.RETIRAR_DOCUMENTOS)==0) {
					DocumentoPlantillaCaracteristicaDTO filtro = campoService.consultaXId(dto.getCampo());
					if(filtro==null) {
						ProductoCaracteristicaDTO filtroProducto =  productoCaracteristicaService.consultaXId(dto.getCampo());
						if(filtroProducto==null) {
							throw new ServerException("Este campo no tiene configurada la plantilla");
						}else {
							plantillaId = filtroProducto.getBase();
						}
					}else {
						plantillaId = filtro.getPlantilla();
					}
				}else {
					//Obtengo la plantilla para que la busqueda sea correcta
					PropiedadFilterDTO filtro = new PropiedadFilterDTO();
					filtro.setTipo(PropiedadValorDefinidoDTO.CAMPO);
					filtro.setCampo(dto.getCampo());
					filtro.setPropiedadValor("PROP_19");
					filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
					PropiedadDTO filtroPlantilla = consultaUnica(filtro);
					if(filtroPlantilla==null) throw new ServerException("Este campo no tiene una fuente de datos para obtener la plantilla y validar el campo.\nValor : " + dto.getValor() + "\nMotivo: " + dto.getMotivo());
					plantillaId = filtroPlantilla.getValor();
				}
				
			}
			if(dto.getTipo().compareTo(PropiedadValorDefinidoDTO.PLANTILLA)==0) plantillaId = dto.getCampo();
			if(dto.getTipo().compareTo(PropiedadValorDefinidoDTO.TRANSICION)==0) plantillaId = transicionService.consultaXId(dto.getCampo()).getPlantilla();
			if(plantillaId==null) throw new ServerException("Se va validar un campo pero no se identifica el id de la plantilla");
			DocumentoPlantillaDTO plantilla = plantillaService.consultaXId(plantillaId);
			if(plantilla==null) {
				ProductoDTO producto = productoService.consultaXId(plantillaId);
				if(producto==null) {
					 throw new ServerException("ID de la plantilla configurado en el campo no es valido");							
				}else {
					ProductoCaracteristicaFilterDTO campoProductoFilter = new ProductoCaracteristicaFilterDTO();
					campoProductoFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
					campoProductoFilter.setBase(producto.getLlaveTabla());
					campoProductoFilter.setCodigo(dto.getValor().toUpperCase());
					ProductoCaracteristicaDTO campoProducto = productoCaracteristicaService.consultaUnica(campoProductoFilter);
					if(campoProducto==null) throw new ServerException("El campo " + dto.getTexto() + " no fue reconocido en el producto " + producto.getNombre() +"\nKey : "+ dto.getKey() +"\nValue Code : "+ dto.getValor());
					dto.setValor(campoProducto.getLlaveTabla());
					dto.setTexto(campoProducto.getNombre());
					return false;
				}
			}
			//VAlido por el nombre
			DocumentoPlantillaCaracteristicaFilterDTO campoFilter = new DocumentoPlantillaCaracteristicaFilterDTO();
			campoFilter.setNombre(dto.getValor().toUpperCase());
			campoFilter.setPlantilla(plantilla.getLlaveTabla());
			campoFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			campo = campoService.consultaUnica(campoFilter);
			if(campo==null){
				campoFilter = new DocumentoPlantillaCaracteristicaFilterDTO();
				campoFilter.setCodigo(dto.getValor().toUpperCase());
				campoFilter.setPlantilla(plantilla.getLlaveTabla());
				campoFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
				campo = campoService.consultaUnica(campoFilter);
				if(campo==null) throw new ServerException("El campo " + dto.getTexto() + " no fue reconocido en la plantilla " + plantilla.getNombre() +"\nKey : "+ dto.getKey() +"\nValue : "+ dto.getValor());
			}
		}
		dto.setValor(campo.getLlaveTabla());
		dto.setTexto(campo.getNombre());
		return false;
	}
	
	private void identificadorBodega(PropiedadDTO dto) throws ServerException {
		BodegaDTO bodega = bodegaService.consultaXId(dto.getValor());
		if(bodega==null){
			BodegaFilterDTO bodegaFilter = new BodegaFilterDTO();
			bodegaFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			List<BodegaDTO> bodegas = bodegaService.listarConsulta(bodegaFilter);
			if(bodegas ==null || bodegas.isEmpty()) throw new ServerException("No se tienen bodegas creadas");
			bodega = null;
			for (BodegaDTO bodegaDTO : bodegas) {
				if(bodegaDTO.getNombre().compareTo(dto.getValor().toUpperCase())==0 || bodegaDTO.getCodigo().compareTo(dto.getValor().toUpperCase())==0) {
					bodega = bodegaDTO;
					break;
				}
			}
			if(bodega==null) throw new ServerException("No se encontro bodega con Id, nombre o Codigo que concuerde");
		}
		dto.setValor( bodega.getLlaveTabla() );
		dto.setTexto( bodega.getNombre() );
	}
	
	private void identificadorCategoriaProducto(PropiedadDTO dto, String token) throws ServerException {
		if(dto.getValor().compareTo("*")==0) {//Si viene en cero se crea el campo
			DocumentoPlantillaDTO plantillaPrincipal = plantillaService.consultaXId(dto.getCampo());
			CategoriaProductoDTO nuevaCategoria= new CategoriaProductoDTO();
			nuevaCategoria.setNombre(plantillaPrincipal.getNombre());
			nuevaCategoria.setImagen(plantillaPrincipal.getImagen());
			nuevaCategoria = categoriaProductoService.guardar(nuevaCategoria, token);
			dto.setValor(nuevaCategoria.getLlaveTabla());
		}
		CategoriaProductoDTO categoria = categoriaProductoService.consultaXId(dto.getValor());
		if(categoria==null){
			CategoriaProductoFilterDTO categoriaFilter = new CategoriaProductoFilterDTO();
			categoriaFilter.setNombre(dto.getValor().toUpperCase());
			categoriaFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			categoria = categoriaProductoService.consultaUnica(categoriaFilter);
			if(categoria==null) throw new ServerException("No se encontro categoria con Id, nombre o Codigo que concuerde");
		}
		dto.setValor( categoria.getLlaveTabla() );
		dto.setTexto( categoria.getNombre() );
	}
	
	private void identificadorUsuario(PropiedadDTO dto) throws ServerException {
		if(dto.getValor().compareTo("*")==0) return;
		UsuarioDTO usuario = usuarioService.consultaXId(dto.getValor());
		if(usuario==null){
			UsuarioFilterDTO usuarioFilter = new UsuarioFilterDTO();
			usuarioFilter.setIdentificacion(dto.getValor());
			usuarioFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			usuario = usuarioService.consultaUnica(usuarioFilter);
			if(usuario==null){//Consulto por codigo
				usuarioFilter = new UsuarioFilterDTO();
				usuarioFilter.setNombre(dto.getValor().toUpperCase());
				usuarioFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
				usuario = usuarioService.consultaUnica(usuarioFilter);
				if(usuario==null) throw new ServerException("No se encontro usuario con Id, nombre o Codigo que concuerde");
			}
		}
		dto.setValor( usuario.getLlaveTabla() );
		dto.setTexto( usuario.getNombre() );
	}
	
	private void identificadorTarifario(PropiedadDTO dto) throws ServerException {
		TarifarioDTO tarifario = tarifarioService.consultaXId(dto.getValor());
		if(tarifario==null){
			TarifarioFilterDTO tarifarioFilter = new TarifarioFilterDTO();
			tarifarioFilter.setNombre(dto.getValor().toUpperCase());
			tarifarioFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			tarifario = tarifarioService.consultaUnica(tarifarioFilter);
			if(tarifario==null) throw new ServerException("No se encontro catalogo con Id, nombre o Codigo que concuerde cone el tarifario");
		}
		dto.setValor( tarifario.getLlaveTabla() );
		dto.setTexto( tarifario.getNombre() );
	}
	
	private void identificadorReporte(PropiedadDTO dto) throws ServerException {
		ReporteBaseDTO reporte = reporteService.consultaXId(dto.getValor());
		if(reporte==null){
			ReporteBaseFilterDTO reporteFilter = new ReporteBaseFilterDTO();
			reporteFilter.setNombre(dto.getValor().toUpperCase());
			reporteFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);//Busco los activos porque los que son subreportes nos e muestran
			reporte = reporteService.consultaUnica(reporteFilter);
			if(reporte==null){//Consulto por codigo
				reporteFilter = new ReporteBaseFilterDTO();
				reporteFilter.setCodigo(dto.getValor().toUpperCase());
				reporteFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
				reporte = reporteService.consultaUnica(reporteFilter);
				if(reporte==null) throw new ServerException("No se encontro reporte con Id, nombre o Codigo que concuerde");
			}
		}
		dto.setValor(reporte.getLlaveTabla() );
		dto.setTexto(reporte.getNombre() );
	}
	
	private boolean identificadorPlantillasGestion(PropiedadDTO dto) throws ServerException{
		if(dto.getValor().compareTo("TODOS")==0)dto.setValor("*");//Esto es para evitar error al copiar
		if(dto.getValor().compareTo("*")==0) {
			dto.setTexto("TODOS");
		}else {
			dto.setTexto(null);
			String[] plantillas = dto.getValor().split(";");
			String valorFinal = null;
			for (String iPlantilla : plantillas) {
				DocumentoPlantillaDTO filtro = buscarPlantilla(iPlantilla);
				if(filtro==null) throw new ServerException("Codigo de la plantilla configurado en el campo no es valido.\nNombre : " + iPlantilla +  "\nPropiedad : " +dto.getKey());
				if(dto.getTexto()==null) {
					dto.setTexto(filtro.getNombre());
					valorFinal =  filtro.getCodigo();
				}else {
					dto.setTexto(dto.getTexto() + ";"+filtro.getNombre());
					valorFinal = valorFinal + ";" + filtro.getCodigo();
				}
			}
			dto.setValor(valorFinal);
		}
		return false;
	}
	
	private void identificarColor(PropiedadDTO dto) throws ServerException {
		if(dto.getValor().length()!=7) throw new ServerException("El color debe tener 7 caracteres y el primero es #");
		if(!dto.getValor().startsWith("#")) throw new ServerException("El color debe tener 7 caracteres y el primero es #");
	}
	
	private boolean validar(PropiedadDTO dto, String token)throws ServerException {
		switch (dto.getKey()){
			case Propiedades.PROCESO_ACCIONES : {identificadorPlantilla(dto, token);break;}
			case Propiedades.PLANTILLA_AUXILIAR : {identificadorPlantilla(dto, token);break;}
			case Propiedades.API_NEW_DOCUMENT : {identificadorPlantilla(dto, token);break;}
			case Propiedades.API_SECONDARY_DOCUMENT : {identificadorPlantilla(dto, token);break;}
			case Propiedades.PLANTILLA_ANULAR : {identificadorPlantilla(dto, token);break;}
			case Propiedades.PROCESO_GESTIONAR_ESTADOS : {identificadorPlantillasGestion(dto);break;}
			case Propiedades.BODEGA_FIJA : {identificadorBodega(dto);break;}
			case Propiedades.DEPENDE : {return identificadorCampo(dto, token);}
			case Propiedades.DISPONIBILIDAD_CROQUIS: {return identificadorCampo(dto, token);}
			case Propiedades.MODIFICAR_CAMPO : {return identificadorCampo(dto, token);}
			case Propiedades.PROCESO_VALOR: {identificadorValorProceso(dto, token);break;}
			case Propiedades.UBICACION : {return identificadorCampo(dto, token);}
			case Propiedades.GENERA_DOCUMENTO_CAMPO : {return identificadorCampo(dto, token);}
			case Propiedades.RELACIONAR_DOCUMENTOS : {return identificadorCampo(dto, token);}
			case Propiedades.RETIRAR_DOCUMENTOS : {return identificadorCampo(dto, token);}
			
			case Propiedades.TERCERO : {return identificadorCampo(dto, token);}
			case Propiedades.PERMISO_PLANTILLA_INICIO_RAPIDO : {return identificadorCampo(dto, token);}
			case Propiedades.DESCRIPCION : {return identificadorCampo(dto, token);}
			case Propiedades.CAMPO_EVIDENCIA : {return identificadorCampo(dto, token);}
			case Propiedades.DESCRIPCION_NIVEL2 : {return identificadorCampo(dto, token);}
			case Propiedades.TOTAL : {return identificadorCampo(dto, token);}
			case Propiedades.CONSECUTIVO : {return identificadorCampo(dto, token);}
			case Propiedades.FECHA : {return identificadorCampo(dto, token);}
			case Propiedades.RESPONSABLE : {return identificadorCampo(dto, token);}
			case Propiedades.ENCABEZADO : {break;}
			case Propiedades.ORDEN : {break;}
			case Propiedades.CUENTA_SOBREGIRO : {return identificadorCampo(dto, token);}
			
			case Propiedades.PRODUCTOS_FUNCION_CAMPO : {return identificadorCampo(dto, token);}
			case Propiedades.PRODUCTOS_TERCERO : {return identificadorCampo(dto, token);}
			case Propiedades.PRODUCTO_CAMPO_VALOR_MINIMO : {return identificadorCampo(dto, token);}
			case Propiedades.PRODUCTO_CAMPO_VALOR_UNITARIO : {return identificadorCampo(dto, token);}
			case Propiedades.PRODUCTO_CAMPO_CANTIDAD: {return identificadorCampo(dto, token);}
			case Propiedades.PRODUCTO_CAMPO_TOTAL: {return identificadorCampo(dto, token);}
			case Propiedades.PERMISO_PLANTILLA_CAMPO_FILTRO : {return identificadorCampo(dto, token);}
			
			case Propiedades.DETALLE_CATEGORIA : {identificadorCategoriaProducto(dto, token);break;}
			case Propiedades.PLANTILLA_TIPO_PRODUCTO : {identificadorCategoriaProducto(dto, token);break;}
			
			case Propiedades.REPORTE_ENCABEZADO : 
			case Propiedades.REPORTE_PIE_PAGINA : 
			case Propiedades.REPORTE_EXCEL : 
			case Propiedades.P_SUBREPORT_ : 
			case Propiedades.MENSAJE_REPORTE :
			case Propiedades.REPORTE_ENCABEZADO_EXCEL : {identificadorReporte(dto);break;}
			
			case Propiedades.REPORTE_JRXML : {identificadorJRXML(dto);break;}
			
			case Propiedades.MENSAJE : {identificadorMensaje(dto);break;}
			
			case Propiedades.API_TRANSACCION :
			case Propiedades.API : {identificadorApi(dto);break;}
			
			case Propiedades.MENSAJE_DESTINATARIO : 
			case Propiedades.ESTADO_ASIGNAR : {identificadorUsuario(dto);break;}
			
			case Propiedades.ROL : {identificadorRol(dto, token);break;}
			
			case Propiedades.COLOR : {identificarColor(dto);break;}
			
			case Propiedades.DETALLE_TARIFARIO : {identificadorTarifario(dto);break;}
		}
		return false;
	}
	

	private void relacionarCampo(PropiedadDTO dto, String token)throws ServerException {
		switch (dto.getKey()){
			case Propiedades.TERCERO : {break;}
			case Propiedades.DESCRIPCION : {break;}
			case Propiedades.DESCRIPCION_NIVEL2 : {break;}
			case Propiedades.TOTAL : {break;}
			case Propiedades.CONSECUTIVO : {break;}
			case Propiedades.FECHA : {break;}
			case Propiedades.RESPONSABLE : {break;}
			case Propiedades.DEPENDE : {break;}
			case Propiedades.MODIFICAR_CAMPO : {break;}
			default: {return;} 
		}
		RelacionInternaDTO relacion = new RelacionInternaDTO();
		relacion.setPropiedad(dto.getLlaveTabla());
		relacion.setCampo(dto.getValor());
		relacionService.guardar(relacion, token);
	}
	
	public String obtenerUnica(String tipo, String plantilla, String key, String usuario) throws ServerException{
		PropiedadDTO filtroOrden = obtenerPropiedad(tipo, plantilla, key, usuario);
		if(filtroOrden==null) return null;
		return filtroOrden.getValor();
	}

	public PropiedadDTO obtenerPropiedad(String tipo, String plantilla, String key, String usuario) throws ServerException{
		if(plantilla ==null) throw new ServerException("El campo esta nulo");
		List<PropiedadDTO> propiedades = obtenerPropiedades(tipo, plantilla, key, usuario);
		if(propiedades==null || propiedades.isEmpty()) return null;
		return propiedades.get(0);
	}
	
	public List<PropiedadDTO> obtenerPropiedades(String tipo, String entidad, String key, String usuario) throws ServerException{
		if(entidad ==null ) throw new ServerException("El campo esta nulo");
		return obtenerPropiedadesSinEntidad(tipo, entidad, key, usuario);
	}
	
	//Los dividi oara optimizar el menu de usuario y asi consultar los estados todas las propiedades
	public List<PropiedadDTO> obtenerPropiedadesSinEntidad(String tipo, String entidad, String key, String usuario) throws ServerException{
		PropiedadFilterDTO filtroOrden = new PropiedadFilterDTO();
		filtroOrden.setTipo(tipo);
		filtroOrden.setCampo(entidad);
		if(key!=null) {
			PropiedadValorDefinidoDTO valorDefinido = consultarValorDefinido(tipo, key);
			filtroOrden.setPropiedadValor(valorDefinido.getLlaveTabla());	
		}
		List<PropiedadDTO> consultadas = propiedadMapper.consultarRol(filtroOrden, usuario, new Date());
		if(usuario !=null) {
			return cleanPropertiesFromTimeAndExclusion(consultadas);
		}
		return consultadas;
	}

	private List<PropiedadDTO> cleanPropertiesFromTimeAndExclusion(List<PropiedadDTO> consultadas) {
		
		List<PropiedadDTO> validadas = new ArrayList<PropiedadDTO>();
		List<PropiedadDTO> excluidas = new ArrayList<PropiedadDTO>();
		
		if(!consultadas.isEmpty()) {
			//Valido bloqueo por exclusion
			for (PropiedadDTO iPropiedadDTO : consultadas) {
				if(iPropiedadDTO.getUsuarioExcluyente()!=null || iPropiedadDTO.getRolExcluyente()!=null)excluidas.add(iPropiedadDTO);
			}
			if(!excluidas.isEmpty()) {
				for (PropiedadDTO iPropiedadDTO : excluidas) {
					consultadas.removeIf(x -> (x.getTipo().compareTo(iPropiedadDTO.getTipo()) ==0 && x.getCampo().compareTo(iPropiedadDTO.getCampo())==0));
				}	
			}
			//Valido bloqueo por tiempo
			for (PropiedadDTO iPropiedadDTO : consultadas) {
				if(Propiedades.validarBloqueo(iPropiedadDTO))validadas.add(iPropiedadDTO);
			}
		}
		
		return validadas;
		
	}
	
	public List<PropiedadDTO> obtenerEspecialFullPermisos(String plantilla) throws ServerException{
		PropiedadDTO filtroOrden = new PropiedadDTO();
		filtroOrden.setTipo(PropiedadValorDefinidoDTO.PLANTILLA);
		filtroOrden.setCampo(plantilla);
		return propiedadMapper.consultarPermisosFullPlantilla(filtroOrden);
	}
	
	public List<PropiedadDTO> obtenerEspecialFullPermisosSimplificandoBD(List<DocumentoPlantillaDTO> plantillas) throws ServerException{
		return propiedadMapper.obtenerEspecialFullPermisosSimplificandoBD(plantillas);
	}
	
	private void identificadorMensaje(PropiedadDTO dto) throws ServerException{
		MensajePlantillaCorreoDTO bd = mensajeService.consultaXId(dto.getValor());
		//Si es actualizar valido por el id
		if(bd== null) {
			//VAlido por el nombre
			MensajePlantillaCorreoFilterDTO bdFilter = new MensajePlantillaCorreoFilterDTO();
			bdFilter.setNombre(dto.getValor().toUpperCase());
			bdFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			bd  = mensajeService.consultaUnica(bdFilter);
			if(bd==null) throw new ServerException("El mensaje no fue reconocido");
		}
		dto.setValor(bd.getLlaveTabla());
		dto.setTexto(bd.getNombre());
	}
	
	private void identificadorApi(PropiedadDTO dto) throws ServerException{
		WebServiceDTO bd = apiService.consultaXId(dto.getValor());
		//Si es actualizar valido por el id
		if(bd== null) {
			//VAlido por el nombre
			WebServiceFilterDTO bdFilter = new WebServiceFilterDTO();
			bdFilter.setNombre(dto.getValor().toUpperCase());
			bdFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			bd = apiService.consultaUnica(bdFilter);
			if(bd==null) throw new ServerException("El api no fue reconocido");
		}
		dto.setValor(bd.getLlaveTabla());
		dto.setTexto(bd.getNombre());
	}
	
	public void validarFuncionConsultandoPropiedad(BasicParamDTO dto, String tipo, String documento, String modificador, String usuario, String token) throws ServerException {
		dto.setPropiedades(obtenerPropiedades(tipo, dto.getLlaveTabla(), null, usuario));
		if(dto.getPropiedades()==null ) throw new ServerException("NO se logro consultar las propiedades del documento"); 
		validarFuncionConsultandoPropiedad(dto, documento, modificador, usuario, token);
	}

	public void validarFuncionConsultandoPropiedad(BasicParamDTO dto, String documento, String modificador, String usuario, String token) throws ServerException {
		List<PropiedadDTO> validaciones = Propiedades.obtenerVariosParametro(dto, Propiedades.FUNCION_SQL_VALIDAR);
		if(validaciones == null || validaciones.isEmpty()) return ;
		for (PropiedadDTO pPropiedad : validaciones) {
			System.out.format("\nValidando funcion SQL (%s)",pPropiedad.getMotivo() );
			validarFuncion(pPropiedad, documento, modificador, token);
		}
	}
	
	public void validarFuncion(PropiedadDTO dto, String documento, String modificador, String token) throws ServerException {
		String respuestaValidacion = null;
		try {
			respuestaValidacion = propiedadMapper.funcionAsignacion(SoftureUtil.formatFunction(dto.getLlaveTabla()), documento, modificador, token);
		} catch (Exception e) {
			throw new ServerException(e.getMessage(), " Motivo: " + dto.getMotivo() + " Propiedad : " + dto.getNombre());
		}
		if(respuestaValidacion==null)  throw new ServerException("El resultado ha sido nulo de la validacion\nDecision : " + dto.getMotivo());
		if(respuestaValidacion.compareTo("S")!=0)
			throw new ServerException(respuestaValidacion, " Motivo: " + dto.getMotivo());
	}
	
	public List<PropiedadDTO> copiarPropiedades(List<PropiedadDTO> propiedadedBase, String entidad, String token) throws ServerException{
		List<PropiedadDTO> result = new ArrayList<PropiedadDTO>();//Existe otroparecido en helperjosn
		for (PropiedadDTO propiedadDTO : propiedadedBase) {
			PropiedadDTO newPropiedad = new PropiedadDTO();
			newPropiedad.setCampo(entidad);
			//newPropiedad.setCodigo(propiedadDTO.getCodigo());
			newPropiedad.setKey(propiedadDTO.getKey());
			newPropiedad.setMotivo(propiedadDTO.getMotivo());
			//newPropiedad.setNecesario(propiedadDTO.getNecesario());
			newPropiedad.setNombre(propiedadDTO.getNombre());
			newPropiedad.setPropiedadValor(propiedadDTO.getPropiedadValor());
			newPropiedad.setTipo(propiedadDTO.getTipo());
			newPropiedad.setRol(propiedadDTO.getRol());
			newPropiedad.setUsuario(propiedadDTO.getUsuario());
			newPropiedad.setFechaInicial(propiedadDTO.getFechaInicial());
			newPropiedad.setFechaFinal(propiedadDTO.getFechaFinal());
			newPropiedad.setValor(propiedadDTO.getTexto());//Sucede que los campos de una plantilla los copiaba mal referenciados
			if(newPropiedad.getValor()==null) {
				newPropiedad.setValor(propiedadDTO.getValor());
			}
			result.add( guardar(newPropiedad, token) );
		}
		return result;
	}
	
	private void identificadorValorProceso(PropiedadDTO dto, String token) throws ServerException{
		if(dto.getValor().compareTo("1")==0) return;
		if(dto.getValor().compareTo("2")==0) return;
		if(dto.getValor().compareTo("0")==0) return;
		identificadorCampo(dto, token);
	}
	
	private void identificadorJRXML(PropiedadDTO dto) throws ServerException{
		if(dto.getValor().contains("language=\"groovy\"")) throw new ServerException("El lenguaje del reporte debe ser java.");
	}
	
	public void actualizarValorPropiedad(PropiedadDTO dto) throws ServerException{
		propiedadMapper.actualizarValorPropiedad(dto);
	}
	
	/*
	 * La uso en pedidoventa para listar los campos que se realacionan en un heredable de muchas plantillas
	 */
	public List<String> camposRelacionados(PropiedadDTO propiedad) throws ServerException{
		List<String> result = null;
		List<RelacionInternaDTO> relaciones = relacionService.relacionesPropiedad(propiedad.getLlaveTabla());
		if(relaciones!=null && !relaciones.isEmpty()) {
			result = new ArrayList<String>();
			for (RelacionInternaDTO relacionInternaDTO : relaciones) {
				result.add( relacionInternaDTO.getCampo());
			}
		}else {
			throw new ServerException("Este campo de heredable no tiene relaciones de campos");
		}
		return result;
	}
	
	/*
	 * La uso para programar tareas automaticas
	 */
	public List<PropiedadDTO> consultarTemporizadoresPendientes() throws ServerException{
		return propiedadMapper.consultarTemporizadoresPendientes();
	}
	
	public List<PropiedadDTO> listarProductoSimplificar(List<ProductoDTO> productos) throws ServerException{
		if(productos==null || productos.isEmpty()) return new ArrayList<PropiedadDTO>();
		return propiedadMapper.listarProductoSimplificado(productos);
	}
	
	public List<PropiedadDTO> listarPlantillasSimplificar(List<DocumentoPlantillaDTO> plantillas, String usuario) throws ServerException{
		 
		List<PropiedadDTO> consultadas = propiedadMapper.listarPlantillasSimplificar(plantillas, usuario, new Date());
		return cleanPropertiesFromTimeAndExclusion(consultadas);
	}
	
	public String ubicarPropiedad(PropiedadDTO propiedad) throws ServerException {
		if(propiedad==null || propiedad.getTipo()==null) throw new ServerException("Los datos de la propiedad estan nulos");
		switch (propiedad.getTipo()) {
		case PropiedadValorDefinidoDTO.API_SERVICE:
			return "SERVICIO API";
		case PropiedadValorDefinidoDTO.CAMPO:
			return"CAMPO";
		case PropiedadValorDefinidoDTO.ESTADO:
			return"ESTADO";
		case PropiedadValorDefinidoDTO.ORGANIZACION:
			return"ORGANIZACION";
		case PropiedadValorDefinidoDTO.PLANTILLA:
			return"PLANTILLA";
		case PropiedadValorDefinidoDTO.PROCESO:
			return"PROCESO";
		case PropiedadValorDefinidoDTO.REPORTE:
			return"REPORTE";
		case PropiedadValorDefinidoDTO.ROL:
			return"ROL";
		case PropiedadValorDefinidoDTO.SERVIDOR:
			return"SERVIDOR";
		case PropiedadValorDefinidoDTO.TRANSICION:
			ProcesoTransicionDTO bdTR = transicionService.consultaXId(propiedad.getCampo());
			return "TRANSICION : " + bdTR.getNombre().toLowerCase()+ " \n PROCESO: "+ bdTR.getProcesoNombre().toLowerCase()
				+" \nPLANTILLA : "+ bdTR.getPlantillaNombre().toLowerCase()+ "\n\n";
		}
		return "";
	}
// END region aditionalMethods

}