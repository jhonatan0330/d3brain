package com.softure.property.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.BindingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.plan.application.base.CatalogService;
import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.CatalogFilterDTO;
import com.configuration.homologate.application.HomologateAdapterService;
import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.authorization.application.RolAccesoSvc;
import com.softure.authorization.domain.RolAccesoDTO;
import com.softure.authorization.domain.RolAccesoFilterDTO;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.inventory.application.CategoriaProductoSvc;
import com.softure.inventory.application.ProductoSvc;
import com.softure.inventory.domain.CategoriaProductoDTO;
import com.softure.inventory.domain.CategoriaProductoFilterDTO;
import com.softure.inventory.domain.ProductoDTO;
import com.softure.java.services.ProcessTemplate;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.logisticpymes.application.CambioSvc;
import com.softure.logisticpymes.application.UsuarioSvc;
import com.softure.logisticpymes.domain.BasicParamDTO;
import com.softure.logisticpymes.domain.UsuarioDTO;
import com.softure.logisticpymes.domain.UsuarioFilterDTO;
import com.softure.mail.application.MensajePlantillaCorreoSvc;
import com.softure.mail.domain.MensajePlantillaCorreoDTO;
import com.softure.mail.domain.MensajePlantillaCorreoFilterDTO;
import com.softure.process_designer.application.ProcesoEstadoSvc;
import com.softure.process_designer.application.ProcesoSvc;
import com.softure.process_designer.application.ProcesoTransicionAutomaticaSvc;
import com.softure.process_designer.application.ProcesoTransicionSvc;
import com.softure.process_designer.domain.ProcesoDTO;
import com.softure.process_designer.domain.ProcesoEstadoDTO;
import com.softure.process_designer.domain.ProcesoEstadoFilterDTO;
import com.softure.process_designer.domain.ProcesoFilterDTO;
import com.softure.process_designer.domain.ProcesoTransicionDTO;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaFilterDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.process_form.domain.DocumentoPlantillaFilterDTO;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadFilterDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.property.domain.PropiedadValorDefinidoFilterDTO;
import com.softure.property.domain.RelacionInternaDTO;
import com.softure.property.infrastructure.PropiedadMapper;
import com.softure.report.application.JasperReportCache;
import com.softure.report.application.ReporteBaseSvc;
import com.softure.report.domain.ReporteBaseDTO;
import com.softure.report.domain.ReporteBaseFilterDTO;
import com.softure.tariff.application.base.TarifarioService;
import com.softure.tariff.domain.TarifarioDTO;
import com.softure.tariff.domain.TarifarioFilterDTO;
import com.softure.webservice.application.WebServiceSvc;
import com.softure.webservice.domain.WebServiceDTO;
import com.softure.webservice.domain.WebServiceFilterDTO;

import jakarta.annotation.PostConstruct;

@Service("propiedadService")
public class PropiedadSvc extends BasicSvc<PropiedadDTO, PropiedadFilterDTO> {

	private static Logger log = LoggerFactory.getLogger(PropiedadSvc.class);

	@Autowired @Lazy 
	private PropiedadMapper propiedadMapper;

	@Autowired @Lazy 
	private CambioSvc cambioService;
	@Autowired @Lazy 
	private CatalogService catalogService;
	@Autowired @Lazy 
	private CategoriaProductoSvc categoriaProductoService;
	@Autowired @Lazy 
	private DocumentoPlantillaCaracteristicaSvc campoService;
	@Autowired @Lazy 
	private DocumentoPlantillaSvc plantillaService;
	@Autowired @Lazy 
	private MensajePlantillaCorreoSvc mensajeService;
	@Autowired @Lazy 
	private ProcesoSvc procesoService;
	@Autowired @Lazy 
	private ProcesoEstadoSvc estadoService;
	@Autowired @Lazy 
	private ProcesoTransicionSvc transicionService;
	@Autowired @Lazy 
	private ProcesoTransicionAutomaticaSvc automatizadorService;
	@Autowired @Lazy 
	private ProductoSvc productoService;
	@Autowired @Lazy 
	private PropiedadValorDefinidoSvc valorDefinidoService;
	@Autowired @Lazy 
	private TarifarioService tarifarioService;
	@Autowired @Lazy 
	private ReporteBaseSvc reporteService;
	@Autowired @Lazy 
	private RolAccesoSvc rolService;
	@Autowired @Lazy 
	private RelacionInternaSvc relacionService;
	@Autowired @Lazy 
	private UsuarioSvc usuarioService;
	@Autowired @Lazy 
	private WebServiceSvc apiService;

	@Autowired @Lazy 
	private HomologateAdapterService homologateService;
	
	@Autowired @Lazy 
	private JasperReportCache reportCacheService;
	@Autowired @Lazy 
	private ProcessTemplate templatesService;
	
	@Override
	public PropiedadDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Propiedad");
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
		throw new ServerException("No se permite activar las propiedades");
		// END Propiedad_activar
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PropiedadDTO actualizar(PropiedadDTO dto, String token) throws ServerException {
		// BEGIN Propiedad_actualizar
		String llaveTabla = dto.getLlaveTabla();
		dto = guardar(dto, token);

		relacionService.copyFromProperty(llaveTabla, dto.getLlaveTabla(), token, dto.getCambioCreacion(), true);
		/*
		 * List<RelacionInternaDTO> relaciones =
		 * relacionService.relacionesPropiedad(llaveTabla); if (relaciones != null &&
		 * !relaciones.isEmpty()) { for (RelacionInternaDTO relacionInternaDTO :
		 * relaciones) { if (dto.getValor().compareTo(relacionInternaDTO.getCampo()) !=
		 * 0) { RelacionInternaDTO nueva = new RelacionInternaDTO();
		 * nueva.setPropiedad(dto.getLlaveTabla());
		 * nueva.setPlantilla(relacionInternaDTO.getPlantilla());
		 * nueva.setCampo(relacionInternaDTO.getCampo());
		 * nueva.setFechaInicio(relacionInternaDTO.getFechaInicio());
		 * nueva.setCambioCreacion(relacionInternaDTO.getCambioCreacion());
		 * relacionService.guardar(nueva, token); } } }
		 */
		PropiedadDTO inactivo = new PropiedadDTO();
		inactivo.setLlaveTabla(llaveTabla);
		inactivar(inactivo, token);
		return dto;
		// END Propiedad_actualizar
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PropiedadDTO inactivar(PropiedadDTO dto, String token) throws ServerException {
		// BEGIN Propiedad_inactivar
		PropiedadDTO bd = consultaXId(dto.getLlaveTabla());
		bd.setCambioEliminacion(cambioService.obtenerCambioGrabando(token).getLlaveTabla());
		if (bd.getKey() == null) {
			PropiedadValorDefinidoDTO valorDefinido = valorDefinidoService.consultaXId(bd.getPropiedadValor());
			bd.setTipo(valorDefinido.getOrigen());
			bd.setKey(valorDefinido.getCodigo());
		}
		bd.setEstado(SharedConstants.STATE_INACTIVE);
		bd = super.update(bd);
		if (bd.getKey().contains("SQL")) {
			bd.setLlaveTabla(SoftureUtil.formatFunction(bd.getLlaveTabla()));
			switch (bd.getKey()) {
			case Propiedades.TABLERO_CONTROL_SQL:
			case Propiedades.VINCULO_FIELD_SQL:
			case Propiedades.VINCULO_GET_PREVIOUS_SQL:
			case Propiedades.PROCESO_FUNCION_SQL:
				propiedadMapper.eliminarFuncionFiltros(bd);
				break;
			case Propiedades.PRODUCTOS_FUNCION_SQL:
				propiedadMapper.eliminarFuncionProductos(dto);
				break;
			case Propiedades.ITERACION_SQL:
			case Propiedades.API_SQL:
			case Propiedades.DECISION_SQL:
				propiedadMapper.eliminarFuncionDecision(dto);
				break;
			case Propiedades.DETALLE_TARIFARIO_SQL:
				propiedadMapper.eliminarFuncionTarifas(dto);
				break;
			case Propiedades.DISPONIBILIDAD_FUNCION_SQL:
			case Propiedades.FECHA_FUNCION_SQL:
			case Propiedades.SECCION_FUNCION_SQL:	
			case Propiedades.NUMERO_FUNCION_SQL:
				if(Propiedades.isFunctionNotFreeMarker(dto.getValor())) {
					propiedadMapper.eliminarFuncionNumerica(dto);					
				}
				break;
			case Propiedades.GENERA_DOCUMENTO_FUNCION_SQL:
				propiedadMapper.eliminarFuncionCampoGenerar(dto);
				break;
			case Propiedades.PLANTILLA_RENDER_ESPECIAL_SQL:
				propiedadMapper.eliminarFuncionCamposEspecialesPlantilla(dto);
				break;
			case Propiedades.FUNCION_SQL_PREVALIDATE_API:
			case Propiedades.FUNCION_SQL_VALIDAR_ANTES:
			case Propiedades.TEMPLATE_MESSAGE_SQL:
			case Propiedades.FUNCION_SQL_NEW_ANTES:
				if(Propiedades.isFunctionNotFreeMarker(dto.getValor())) {
					propiedadMapper.eliminarFuncionPrevalidacion(dto);
				}
				break;
			default:
				propiedadMapper.eliminarFuncion(bd);
				break;
			}
		}
		if (bd.getKey().compareTo(Propiedades.FILTRO) == 0)
			campoService.actualizarFiltros(dto.getCampo());
		if (bd.getKey().compareTo(Propiedades.DESCRIPCION) == 0)
			campoService.actualizarDescripcion(dto.getCampo(), null);
		if (bd.getKey().compareTo(Propiedades.TEMPORIZADOR) == 0) {
			automatizadorService.inactivarPropiedad(bd.getLlaveTabla());
			propiedadMapper.eliminarFuncionFiltros(bd);
		}
		List<RelacionInternaDTO> relaciones = relacionService.relacionesPropiedad(bd.getLlaveTabla());
		if (relaciones != null && !relaciones.isEmpty()) {
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
	public List<PropiedadDTO> listarConsulta(PropiedadFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	private String getLocationError(String type, String fieldId) throws ServerException {
		switch (type) {
		case PropiedadValorDefinidoDTO.PLANTILLA: {
			return " en la plantilla " + plantillaService.consultaXId(fieldId).getNombre();
		}
		case PropiedadValorDefinidoDTO.CAMPO: {
			DocumentoPlantillaCaracteristicaDTO campo = campoService.consultaXId(fieldId);
			return " en el campo " + campo.getNombre() + " la plantilla " + campo.getPlantillaNombre();
		}
		case PropiedadValorDefinidoDTO.REPORTE: {
			ReporteBaseDTO campo = reporteService.consultaXId(fieldId);
			return " en el reporte " + campo.getNombre() + " de la plantilla " + campo.getPlantillaNombre();
		}
		default:
			return "(" + fieldId + ")";
		}
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PropiedadDTO guardar(PropiedadDTO dto, String token) throws ServerException {
		// BEGIN Propiedad_guardar
		if (dto.getUsuarioExcluyente() != null && (dto.getUsuario() != null || dto.getRol() != null))
			throw new ServerException("Cuando colocas USUARIO Excluyente no puedes colocar usuario o rol");
		if (dto.getRolExcluyente() != null && (dto.getUsuario() != null || dto.getRol() != null))
			throw new ServerException("Cuando colocas ROL Excluyente no puedes colocar usuario o rol");
		if (dto.getPropiedadValor() == null)
			dto.setPropiedadValor(consultarValorDefinido(dto.getTipo(), dto.getKey()).getLlaveTabla());
		PropiedadValorDefinidoDTO valorDefinido = valorDefinidoService.consultaXId(dto.getPropiedadValor());
		if (valorDefinido == null)
			throw new ServerException("No se encuentra la propiedad con Id " + dto.getPropiedadValor());
		dto.setTipo(valorDefinido.getOrigen());
		dto.setKey(valorDefinido.getCodigo());
		if (dto.getValor() != null)
			dto.setValor(SoftureUtil.cleanStartEndSpaces(dto.getValor()));
		dto.setCambioCreacion(cambioService.obtenerCambioGrabando(token).getLlaveTabla());
		if (!valorDefinido.getNecesitaDesarrollo())
			dto.setFechaImplementacion(new Date());
		if(dto.getMotivo()!=null && dto.getMotivo().isEmpty()) dto.setMotivo(null);
		if (valorDefinido.getMultiple()) {
			PropiedadFilterDTO existeFilter = new PropiedadFilterDTO();
			existeFilter.setCampo(dto.getCampo());
			existeFilter.setPropiedadValor(dto.getPropiedadValor());
			existeFilter.setEstado(SharedConstants.STATE_ACTIVE);
			existeFilter.setRol(dto.getRol());
			existeFilter.setValor(dto.getValor());
			existeFilter.setRolExcluyente(dto.getRolExcluyente());
			existeFilter.setUsuario(dto.getUsuario());
			existeFilter.setUsuarioExcluyente(dto.getUsuarioExcluyente());
			existeFilter.setMotivo(dto.getMotivo());
			List<PropiedadDTO> existe = listarConsulta(existeFilter);
			if (existe != null && existe.size()>1)
				throw new ServerException(
						"Cuando se repiten propiedades multiples se necesita tener un motivo que la describa "
								+ existe.get(0).getNombre() + getLocationError(existe.get(0).getTipo(), existe.get(0).getCampo()));
		}
		if (valorDefinido.getSolicitaMotivo() && dto.getMotivo() == null)
			throw new ServerException("La propiedad necesita tener motivo. \n" + valorDefinido.getNombre()
					+ getLocationError(dto.getTipo(), dto.getCampo()));
		if (!valorDefinido.getMultiple() && dto.getLlaveTabla() == null) {// Por el momento solo valida las nuevas
			PropiedadFilterDTO existeFilter = new PropiedadFilterDTO();
			existeFilter.setCampo(dto.getCampo());
			existeFilter.setPropiedadValor(dto.getPropiedadValor());
			existeFilter.setEstado(SharedConstants.STATE_ACTIVE);
			existeFilter.setRol(dto.getRol());
			existeFilter.setRolExcluyente(dto.getRolExcluyente());
			existeFilter.setUsuario(dto.getUsuario());
			existeFilter.setUsuarioExcluyente(dto.getUsuarioExcluyente());
			List<PropiedadDTO> existe = listarConsulta(existeFilter);
			if (existe != null && !existe.isEmpty())
				throw new ServerException("Esta propiedad ya fue definida " + existe.get(0).getNombre()
						+ getLocationError(existe.get(0).getTipo(), existe.get(0).getCampo()));

		} else {
			dto.setLlaveTabla(null);
			// Falta validar que venga el mismo tipo para que no nos hagan gol
		}
		if (validar(dto, token))
			return null;
		// Esto tuve que hacerlo para evitar un ciclo infinito al sincronizar los datos
		// actuales y crear los documentos nuevos que se homologan
		if (dto.getKey().contains("PLANTILLA_TIPO")) {
			homologateService.call(dto, token);
		}
		dto.setFechaDefinicion(new Date());
		dto = super.guardar(dto, token);
		try {
			if (dto.getKey().contains("SQL")) {
				dto.setLlaveTabla(SoftureUtil.formatFunction(dto.getLlaveTabla()));
				switch (dto.getKey()) {
				case Propiedades.TABLERO_CONTROL_SQL:
				case Propiedades.VINCULO_FIELD_SQL:
				case Propiedades.VINCULO_GET_PREVIOUS_SQL:
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
				case Propiedades.API_SQL:
					propiedadMapper.crearFuncionDecision(dto);
					break;
				case Propiedades.ITERACION_SQL:
					propiedadMapper.crearFuncionIteracion(dto);
					break;
				case Propiedades.DETALLE_TARIFARIO_SQL:
					propiedadMapper.crearFuncionTarifas(dto);
					break;
				case Propiedades.SECCION_FUNCION_SQL:
				case Propiedades.NUMERO_FUNCION_SQL:
					if(Propiedades.isFunctionNotFreeMarker(dto.getValor())) {
						propiedadMapper.crearFuncionNumerica(dto);	
					}
					break;
				case Propiedades.FECHA_FUNCION_SQL:
					propiedadMapper.crearFuncionFecha(dto);
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
				case Propiedades.FUNCION_SQL_VALIDAR_ANTES:
					propiedadMapper.crearFuncionPrevalidacion(dto);
					break;
				case Propiedades.TEMPLATE_MESSAGE_SQL:
				case Propiedades.HTML_DOCUMENT_SQL:
				case Propiedades.FUNCION_SQL_NEW_ANTES:
					propiedadMapper.crearFuncionPrevalidacionReturnString(dto);
					break;
				case Propiedades.FUNCION_SQL_PREVALIDATE_API:
					if(Propiedades.isFunctionNotFreeMarker(dto.getValor())) {
						propiedadMapper.crearFuncionPrevalidateAPI(dto);	
					}
					break;
				default:
					propiedadMapper.crearFuncion(dto);
					break;
				}
			}
			if (dto.getKey().compareTo(Propiedades.TEMPORIZADOR) == 0)
				propiedadMapper.crearFuncionFiltros(dto);
			//if (dto.getKey().compareTo(Propiedades.PLANTILLA_MONITOR) == 0)
				//createAccountService.call(dto.getValor(), dto.getCampo(), null);
		} catch (Exception e) {
			throw new ServerException(e.getMessage(), "Funcion de SQL : " + dto.getMotivo());
		}
		if (dto.getKey().compareTo(Propiedades.FILTRO) == 0)
			campoService.actualizarFiltros(dto.getCampo());
		if (dto.getKey().compareTo(Propiedades.DESCRIPCION) == 0)
			campoService.actualizarDescripcion(dto.getCampo(), dto.getValor());
		// relacionarCampo(dto, token);
		return dto;
		// END Propiedad_guardar
	}

// BEGIN region aditionalMethods

	public PropiedadValorDefinidoDTO consultarValorDefinido(String tipo, String key) throws ServerException {
		PropiedadValorDefinidoFilterDTO valorDefinidoFilter = new PropiedadValorDefinidoFilterDTO();
		valorDefinidoFilter.setCodigo(key);
		valorDefinidoFilter.setOrigen(tipo);
		valorDefinidoFilter.setEstado(SharedConstants.STATE_ACTIVE);
		PropiedadValorDefinidoDTO valorDefinido = valorDefinidoService.consultaUnica(valorDefinidoFilter);
		if (valorDefinido == null)
			throw new ServerException("No se encontro la propiedad " + key + " del tipo " + tipo);
		return valorDefinido;
	}

	private void identificadorRol(PropiedadDTO dto, String token) throws ServerException {
		RolAccesoDTO rol = rolService.consultaXId(dto.getValor());
		if (rol == null) {
			identificadorPlantilla(dto, token);
			RolAccesoFilterDTO rolFilter = new RolAccesoFilterDTO();
			rolFilter.setPlantilla(dto.getValor());
			rolFilter.setEstado(SharedConstants.STATE_ACTIVE);
			rol = rolService.consultaUnica(rolFilter);
			if (rol == null)
				throw new ServerException("No se encontro rol con Id, nombre o Codigo que concuerde con el Rol");
		}
		dto.setValor(rol.getLlaveTabla());
		dto.setTexto(rol.getNombre());
	}

	private void identificadorPlantilla(PropiedadDTO dto, String token) throws ServerException {
		boolean createDocument = false;// Es una ayudita porque se creaban 2 veces los campos diferencias
		if (dto.getValor().compareTo("*") == 0) {
			if (dto.getKey().compareTo(Propiedades.PLANTILLA_ANULAR) == 0) {
				DocumentoPlantillaDTO plantilla = plantillaService.createDeleteTemplate(dto.getCampo(), token, "DELETE");
				dto.setValor(plantilla.getLlaveTabla());
			}
			if (dto.getKey().compareTo(Propiedades.PLANTILLA_ACTIVAR) == 0) {
				DocumentoPlantillaDTO plantilla = plantillaService.createDeleteTemplate(dto.getCampo(), token, "ACTIVATE");
				dto.setValor(plantilla.getLlaveTabla());
			}
			if (dto.getKey().compareTo(Propiedades.REPORT_MODULE_REFERENCE) == 0) {
				DocumentoPlantillaDTO plantilla = plantillaService.createReportTemplate(dto.getCampo(), token);
				dto.setValor(plantilla.getLlaveTabla());
			}
			if (dto.getKey().compareTo(Propiedades.PLANTILLA_DIFERENCIAS) == 0) {
				DocumentoPlantillaDTO plantilla = plantillaService.createUpdateTemplate(dto.getCampo(), token);
				dto.setValor(plantilla.getLlaveTabla());
				createDocument = true;
			}
		}
		DocumentoPlantillaDTO plantilla = buscarPlantilla(dto.getValor());
		if (plantilla == null)
			throw new ServerException(
					"No se encontro plantilla con Id, nombre o Codigo que concuerde." + dto.getValor());
		dto.setValor(plantilla.getLlaveTabla());
		dto.setTexto(plantilla.getNombre());

		if (!createDocument && dto.getKey().compareTo(Propiedades.PLANTILLA_DIFERENCIAS) == 0) {
			if (dto.getCampo().compareTo(plantilla.getLlaveTabla()) == 0) {
				throw new ServerException("No se puede crear la plantilla de diferencias para la misma plantilla");
			}
			List<DocumentoPlantillaCaracteristicaDTO> fields = campoService
					.listarCamposPlantillaConComplementos(dto.getCampo(), null, false);
			for (DocumentoPlantillaCaracteristicaDTO iCampo : fields) {
				if (Propiedades.obtenerParametro(plantilla, Propiedades.CAMPO_DIFERENCIAS) == null)
					campoService.createFieldDifference(iCampo, plantilla.getLlaveTabla(), token);
				// newCampo.setPropiedades(configuracionSvc.copiarPropiedades(iCampo.getPropiedades(),
				// newCampo.getLlaveTabla(), token));
			}
		}

	}

	private DocumentoPlantillaDTO buscarPlantilla(String valor) throws ServerException {
		DocumentoPlantillaDTO plantilla = plantillaService.consultaXId(valor);
		if (plantilla == null) {// Consulto por nombre
			DocumentoPlantillaFilterDTO plantillaFilter = new DocumentoPlantillaFilterDTO();
			plantillaFilter.setCodigo(valor.toUpperCase());
			plantillaFilter.setEstado(SharedConstants.STATE_ACTIVE);
			plantilla = plantillaService.consultaUnica(plantillaFilter);
			if (plantilla == null) {// Consulto por codigo
				plantillaFilter = new DocumentoPlantillaFilterDTO();
				plantillaFilter.setEstado(SharedConstants.STATE_ACTIVE);
				plantillaFilter.setNombre(valor.toUpperCase());
				try {
					plantilla = plantillaService.consultaUnica(plantillaFilter);
				} catch (Exception e) {
					throw new ServerException(
							"Existen varias plantillas con el nombre " + valor.toUpperCase() + ". " + e.getMessage());
				}
			}
		}
		return plantilla;
	}

	private boolean identificadorCampo(PropiedadDTO dto, String token) throws ServerException {
		DocumentoPlantillaCaracteristicaDTO campo = null;
		if (dto.getValor().compareTo("*") == 0) {// Si viene en cero se crea el campo
			switch (dto.getKey()) {
			case Propiedades.DESCRIPCION: {
				dto.setValor(campoService.crearCampoNombre(dto.getCampo(), token));
				break;
			}
			case Propiedades.TOTAL: {
				dto.setValor(campoService.crearCampoValor(dto.getCampo(), token));
				break;
			}
			case Propiedades.CONSECUTIVO: {
				dto.setValor(campoService.crearCampoIdentificacion(dto.getCampo(), token));
				break;
			}
			case Propiedades.CORREO_ROL: {
				dto.setValor(campoService.crearCampoCorreo(dto.getCampo(), token));
				break;
			}
			case Propiedades.CELULAR_ROL: {
				dto.setValor(campoService.crearCampoTelefono(dto.getCampo(), token));
				break;
			}
			case Propiedades.FECHA: {
				dto.setValor(campoService.crearCampoTiempoReporte(dto.getCampo(), token, false));
				break;
			}
			default: {
				throw new ServerException("Este campo no tiene opcion de crear el campo");
			}
			}
		}
		if (campo == null)
			campo = campoService.consultaXId(dto.getValor());
		// Si es actualizar valido por el id
		if (campo == null) {
			String plantillaId = null;
			if (dto.getTipo().compareTo(PropiedadValorDefinidoDTO.CAMPO) == 0) {
				if (dto.getKey().compareTo(Propiedades.DEPENDE) == 0
						|| dto.getKey().compareTo(Propiedades.MODIFICAR_CAMPO) == 0
						|| dto.getKey().compareTo(Propiedades.INFORMATIVE_DATA) == 0
						|| dto.getKey().compareTo(Propiedades.PRODUCTOS_FUNCION_CAMPO) == 0
						|| dto.getKey().compareTo(Propiedades.RELACIONAR_DOCUMENTOS) == 0
						|| dto.getKey().compareTo(Propiedades.DISPONIBILIDAD_CROQUIS) == 0
						|| dto.getKey().compareTo(Propiedades.UPDATE_INFORMATIVE_FIELD) == 0
						|| dto.getKey().compareTo(Propiedades.FECHA_MAXIMA_CAMPO) == 0
						|| dto.getKey().compareTo(Propiedades.FECHA_MINIMA_CAMPO) == 0
						|| dto.getKey().compareTo(Propiedades.RETIRAR_DOCUMENTOS) == 0) {
					DocumentoPlantillaCaracteristicaDTO filtro = campoService.consultaXId(dto.getCampo());
					if (filtro == null) {
						throw new ServerException("Este campo no tiene configurada la plantilla");
					} else {
						plantillaId = filtro.getPlantilla();
					}
				} else {
					
					switch(dto.getKey()) {
					case Propiedades.CAMPO_DIFERENCIAS: {
						// Obtengo la plantilla para que la busqueda sea correcta
						PropiedadDTO filtroPlantilla = getPropertyDifferenceTemplate(
								campoService.consultaXId(dto.getCampo()).getPlantilla());
						if (filtroPlantilla == null)
							throw new ServerException(
									"Este campo no tiene una plantilla de diferencias y validar el campo.\nValor : "
											+ dto.getValor() + "\nMotivo: " + dto.getMotivo());
						plantillaId = filtroPlantilla.getValor();
						break;
					}
					default: {
						// Obtengo la plantilla para que la busqueda sea correcta
						PropiedadFilterDTO filtro = new PropiedadFilterDTO();
						filtro.setTipo(PropiedadValorDefinidoDTO.CAMPO);
						filtro.setCampo(dto.getCampo());
						filtro.setPropiedadValor("PROP_19");
						filtro.setEstado(SharedConstants.STATE_ACTIVE);
						PropiedadDTO filtroPlantilla = consultaUnica(filtro);
						if (filtroPlantilla == null)
							throw new ServerException(
									"Este campo no tiene una fuente de datos para obtener la plantilla y validar el campo.\nValor : "
											+ dto.getValor() + "\nMotivo: " + dto.getMotivo());
						plantillaId = filtroPlantilla.getValor();
					}
					}
				}

			}
			if (dto.getTipo().compareTo(PropiedadValorDefinidoDTO.PLANTILLA) == 0) {
				switch(dto.getKey()) {
				case Propiedades.PRODUCTO_CAMPO_VALOR_MINIMO:
				case Propiedades.PRODUCTO_CAMPO_VALOR_UNITARIO:
				case Propiedades.PRODUCTO_CAMPO_CANTIDAD:
				case Propiedades.PRODUCTO_CAMPO_TOTAL: {
					// Obtengo la plantilla para que la busqueda sea correcta
					PropiedadDTO filtroPlantilla = getProductTemplate(dto.getCampo());
					if (filtroPlantilla == null)
						throw new ServerException(
								"Este campo no tiene una plantilla de TIPO PRODCTO FORMULARIO DETALLADO.\nValor : "
										+ dto.getValor() + "\nMotivo: " + dto.getMotivo());
					plantillaId = filtroPlantilla.getValor();
					break;
				}
				default: {
					plantillaId = dto.getCampo();
					break;
				}
				}
			}
				
			if (dto.getTipo().compareTo(PropiedadValorDefinidoDTO.TRANSICION) == 0)
				plantillaId = transicionService.consultaXId(dto.getCampo()).getPlantilla();
			if (plantillaId == null)
				throw new ServerException("Se va validar un campo pero no se identifica el id de la plantilla");
			DocumentoPlantillaDTO plantilla = plantillaService.consultaXId(plantillaId);
			if (plantilla == null) {
				ProductoDTO producto = productoService.consultaXId(plantillaId);
				if (producto == null) {
					throw new ServerException("ID de la plantilla configurado en el campo no es valido");
				} else {
					//ProductoCaracteristicaFilterDTO campoProductoFilter = new ProductoCaracteristicaFilterDTO();
					//campoProductoFilter.setEstado(SharedConstants.STATE_ACTIVE);
					//campoProductoFilter.setBase(producto.getLlaveTabla());
					//campoProductoFilter.setCodigo(dto.getValor().toUpperCase());
					//ProductoCaracteristicaDTO campoProducto = productoCaracteristicaService
					//		.consultaUnica(campoProductoFilter);
					//if (campoProducto == null)
						throw new ServerException("El campo " + dto.getTexto() + " no fue reconocido en el producto "
								+ producto.getNombre() + "\nKey : " + dto.getKey() + "\nValue Code : "
								+ dto.getValor());
						//dto.setValor(campoProducto.getLlaveTabla());
						//dto.setTexto(campoProducto.getNombre());
						//return false;
				}
			}
			// VAlido por el nombre
			DocumentoPlantillaCaracteristicaFilterDTO campoFilter = new DocumentoPlantillaCaracteristicaFilterDTO();
			campoFilter.setNombre(dto.getValor().toUpperCase());
			campoFilter.setPlantilla(plantilla.getLlaveTabla());
			campoFilter.setEstado(SharedConstants.STATE_ACTIVE);
			campo = campoService.consultaUnica(campoFilter);
			if (campo == null) {
				campoFilter = new DocumentoPlantillaCaracteristicaFilterDTO();
				campoFilter.setCodigo(dto.getValor().toUpperCase());
				campoFilter.setPlantilla(plantilla.getLlaveTabla());
				campoFilter.setEstado(SharedConstants.STATE_ACTIVE);
				campo = campoService.consultaUnica(campoFilter);
				if (campo == null)
					throw new ServerException("El campo " + dto.getTexto() + " no fue reconocido en la plantilla "
							+ plantilla.getNombre() + "\nKey : " + dto.getKey() + "\nValue : " + dto.getValor());
			}
		}
		dto.setValor(campo.getLlaveTabla());
		dto.setTexto(campo.getNombre());
		return false;
	}

	public PropiedadDTO getPropertyDifferenceTemplate(String templateId) throws ServerException {
		PropiedadFilterDTO filtro = new PropiedadFilterDTO();
		filtro.setTipo(PropiedadValorDefinidoDTO.PLANTILLA);
		filtro.setCampo(templateId);
		filtro.setPropiedadValor("PROP_242");
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		return consultaUnica(filtro);
	}
	
	public PropiedadDTO getProductTemplate(String templateId) throws ServerException {
		PropiedadFilterDTO filtro = new PropiedadFilterDTO();
		filtro.setTipo(PropiedadValorDefinidoDTO.PLANTILLA);
		filtro.setCampo(templateId);
		filtro.setPropiedadValor("PROP_266");
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		return consultaUnica(filtro);
	}

	public PropiedadDTO getPropertyDifferenceField(String field) throws ServerException {
		PropiedadFilterDTO filtro = new PropiedadFilterDTO();
		filtro.setTipo(PropiedadValorDefinidoDTO.CAMPO);
		filtro.setCampo(field);
		filtro.setPropiedadValor("PROP_243");
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		PropiedadDTO filtroPlantilla = consultaUnica(filtro);
		return filtroPlantilla;
	}


	private void identificadorProducto(PropiedadDTO dto) throws ServerException {
		ProductoDTO producto = productoService.consultaXId(dto.getValor());
		if (producto == null) {
			producto = productoService.filtrarPorCodigo(dto.getValor());
			if (producto == null)
				throw new ServerException("No se encontro un producto con nombre o Codigo que concuerde");
		}
		dto.setValor(producto.getLlaveTabla());
		dto.setTexto(producto.getNombre());
	}

	private void identificadorCatalogo(PropiedadDTO dto) throws ServerException {
		CatalogDTO catalog = catalogService.getById(dto.getValor());
		if (catalog == null) {
			CatalogFilterDTO filter = new CatalogFilterDTO();
			filter.setCode(dto.getValor().toUpperCase());
			catalog = catalogService.getOne(filter);
			if (catalog == null)
				throw new ServerException("No se encontro un catalogo con nombre o Codigo que concuerde");
		}
		if (catalog.getState().compareTo(SharedConstants.STATE_ACTIVE) != 0)
			throw new ServerException("El catalogo no se encuentra ACTIVO");
		dto.setValor(catalog.getKey());
		dto.setTexto(catalog.getName());
	}

	private void identificadorCategoriaProducto(PropiedadDTO dto, String token) throws ServerException {
		if (dto.getValor().compareTo("*") == 0) {// Si viene en cero se crea el campo
			DocumentoPlantillaDTO plantillaPrincipal = plantillaService.consultaXId(dto.getCampo());
			CategoriaProductoDTO nuevaCategoria = new CategoriaProductoDTO();
			nuevaCategoria.setNombre(plantillaPrincipal.getNombre());
			nuevaCategoria.setImagen(plantillaPrincipal.getImagen());
			nuevaCategoria.setPlantilla(plantillaPrincipal.getLlaveTabla());
			nuevaCategoria = categoriaProductoService.guardar(nuevaCategoria, token);
			dto.setValor(nuevaCategoria.getLlaveTabla());
		}
		CategoriaProductoDTO categoria = categoriaProductoService.consultaXId(dto.getValor());
		if (categoria == null) {
			CategoriaProductoFilterDTO categoriaFilter = new CategoriaProductoFilterDTO();
			categoriaFilter.setNombre(dto.getValor().toUpperCase());
			categoriaFilter.setEstado(SharedConstants.STATE_ACTIVE);
			categoria = categoriaProductoService.consultaUnica(categoriaFilter);
			if (categoria == null)
				throw new ServerException("No se encontro categoria con Id, nombre o Codigo que concuerde");
		}
		dto.setValor(categoria.getLlaveTabla());
		dto.setTexto(categoria.getNombre());
	}

	private void identificadorUsuario(PropiedadDTO dto) throws ServerException {
		if (dto.getValor().compareTo("*") == 0)
			return;
		UsuarioDTO usuario = usuarioService.consultaXId(dto.getValor());
		if (usuario == null) {
			UsuarioFilterDTO usuarioFilter = new UsuarioFilterDTO();
			usuarioFilter.setIdentificacion(dto.getValor());
			usuarioFilter.setEstado(SharedConstants.STATE_ACTIVE);
			usuario = usuarioService.consultaUnica(usuarioFilter);
			if (usuario == null) {// Consulto por codigo
				usuarioFilter = new UsuarioFilterDTO();
				usuarioFilter.setNombre(dto.getValor().toUpperCase());
				usuarioFilter.setEstado(SharedConstants.STATE_ACTIVE);
				usuario = usuarioService.consultaUnica(usuarioFilter);
				if (usuario == null)
					throw new ServerException("No se encontro usuario con Id, nombre o Codigo que concuerde");
			}
		}
		dto.setValor(usuario.getLlaveTabla());
		dto.setTexto(usuario.getNombre());
	}

	private void identificadorTarifario(PropiedadDTO dto) throws ServerException {
		TarifarioDTO tarifario = tarifarioService.getById(dto.getValor());
		if (tarifario == null) {
			TarifarioFilterDTO tarifarioFilter = new TarifarioFilterDTO();
			tarifarioFilter.setNombre(dto.getValor().toUpperCase());
			tarifarioFilter.setState(SharedConstants.STATE_ACTIVE);
			tarifario = tarifarioService.getOne(tarifarioFilter);
			if (tarifario == null)
				throw new ServerException(
						"No se encontro catalogo con Id, nombre o Codigo que concuerde cone el tarifario");
		}
		dto.setValor(tarifario.getKey());
		dto.setTexto(tarifario.getNombre());
	}

	private void identificadorProceso(PropiedadDTO dto) throws ServerException {
		if (dto.getValor().compareTo("*") == 0) {
			DocumentoPlantillaDTO plantillaPrincipal = plantillaService.consultaXId(dto.getCampo());
			dto.setValor(plantillaPrincipal.getProceso());
		}
		ProcesoDTO _process = procesoService.consultaXId(dto.getValor());
		if (_process == null) {
			ProcesoFilterDTO _processFilter = new ProcesoFilterDTO();
			_processFilter.setNombre(dto.getValor().toUpperCase());
			_processFilter.setEstado(SharedConstants.STATE_ACTIVE);
			_process = procesoService.consultaUnica(_processFilter);
			if (_process == null)
				throw new ServerException(
						"No se encontro proceso con Id, nombre o Codigo que concuerde");
		}
		dto.setValor(_process.getLlaveTabla());
		dto.setTexto(_process.getNombre());
	}
	
	private void identificadorReporte(PropiedadDTO dto) throws ServerException {
		ReporteBaseDTO reporte = reporteService.consultaXId(dto.getValor());
		if (reporte == null) {
			ReporteBaseFilterDTO reporteFilter = new ReporteBaseFilterDTO();
			reporteFilter.setCodigo(dto.getValor().toUpperCase());
			// Busco los activos porque los que son subreportes no se muestran
			reporteFilter.setEstado(SharedConstants.STATE_ACTIVE);
			reporte = reporteService.consultaUnica(reporteFilter);

			if (reporte == null) {// Consulto por codigo
				reporteFilter = new ReporteBaseFilterDTO();
				reporteFilter.setNombre(dto.getValor().toUpperCase());
				reporteFilter.setEstado(SharedConstants.STATE_ACTIVE);
				try {
					reporte = reporteService.consultaUnica(reporteFilter);
				} catch (Exception e) {
					throw new ServerException("Existen varios reportes con nombre " + dto.getValor().toUpperCase()
							+ ". " + e.getMessage());
				}
				if (reporte == null)
					throw new ServerException("No se encontro reporte con Id, nombre o Codigo que concuerde "
							+ dto.getValor().toUpperCase());
			}
		}
		dto.setValor(reporte.getLlaveTabla());
		dto.setTexto(reporte.getNombre());
	}

	private void identificadorEstadoReporte(PropiedadDTO dto) throws ServerException {
		ProcesoEstadoDTO estado = estadoService.consultaXId(dto.getValor());
		if (estado == null) {
			ReporteBaseDTO reporte = reporteService.consultaXId(dto.getCampo());
			if (reporte == null)
				throw new ServerException("Revisa porque esta propiedad no pertenece a un reporte");
			DocumentoPlantillaDTO plantilla = plantillaService.consultaXId(reporte.getPlantilla());
			if (plantilla == null)
				throw new ServerException(
						"Revisa porque el reporte " + reporte.getNombre() + " no esta asociado a una plantilla");
			ProcesoEstadoFilterDTO estadoFiltro = new ProcesoEstadoFilterDTO();
			estadoFiltro.setProceso(plantilla.getProceso());
			estadoFiltro.setNombre(dto.getValor().toUpperCase());
			estadoFiltro.setEstado(SharedConstants.STATE_ACTIVE);
			estado = estadoService.consultaUnica(estadoFiltro);
			if (estado == null) {
				ProcesoDTO proceso = procesoService.consultaXId(plantilla.getProceso());
				if (proceso == null) {
					throw new ServerException("Comienza revisando el proceso de la plantilla del reporte");
				} else {
					throw new ServerException("No se encontro estado con el nombre " + dto.getValor()
							+ " en el proceso " + proceso.getNombre());
				}
			}
		}
		dto.setValor(estado.getLlaveTabla());
		dto.setTexto(estado.getNombre());
	}

	private boolean identificadorPlantillasGestion(PropiedadDTO dto) throws ServerException {
		if (dto.getValor().compareTo("TODOS") == 0)
			dto.setValor("*");// Esto es para evitar error al copiar
		if (dto.getValor().compareTo("PUENTE") == 0)
			dto.setValor("+");// Esto es para evitar error al copiar
		if (dto.getValor().compareTo("*") == 0) {
			dto.setTexto("TODOS");
		} else {
			if (dto.getValor().compareTo("+") == 0) {
				dto.setTexto("PUENTE");
			}else {
				dto.setTexto(null);
				String[] plantillas = dto.getValor().split(";");
				String valorFinal = null;
				for (String iPlantilla : plantillas) {
					DocumentoPlantillaDTO filtro = buscarPlantilla(iPlantilla);
					if (filtro == null)
						throw new ServerException("Codigo de la plantilla configurado en el campo no es valido.\nNombre : "
								+ iPlantilla + "\nPropiedad : " + dto.getKey());
					if (dto.getTexto() == null) {
						dto.setTexto(filtro.getNombre());
						valorFinal = filtro.getCodigo();
					} else {
						dto.setTexto(dto.getTexto() + ";" + filtro.getNombre());
						valorFinal = valorFinal + ";" + filtro.getCodigo();
					}
				}
				dto.setValor(valorFinal);	
			}			
		}
		return false;
	}

	private void identificarColor(PropiedadDTO dto) throws ServerException {
		if (dto.getValor().length() != 7)
			throw new ServerException("El color debe tener 7 caracteres y el primero es #");
		if (!dto.getValor().startsWith("#"))
			throw new ServerException("El color debe tener 7 caracteres y el primero es #");
	}

	private boolean validar(PropiedadDTO dto, String token) throws ServerException {
		switch (dto.getKey()) {
		case Propiedades.PROCESO_ACCIONES:
		case Propiedades.PLANTILLA_AUXILIAR:
		case Propiedades.API_NEW_DOCUMENT:
		case Propiedades.API_SECONDARY_DOCUMENT:
		case Propiedades.PLANTILLA_DIFERENCIAS:
		case Propiedades.REPORT_MODULE_REFERENCE:
		case Propiedades.COVERAGE_TEMPLATE:
		case Propiedades.TIPO_PRODUCTO_FORMULARIO_DETALLADO:
		case Propiedades.PLANTILLA_NUEVO_USUARIO:
		case Propiedades.PLANTILLA_ACTIVAR:
		case Propiedades.VINCULO_DATA:
		case Propiedades.VINCULO_DELETE:
		case Propiedades.PLANTILLA_ANULAR: {
			identificadorPlantilla(dto, token);
			break;
		}
		case Propiedades.PROCESO_GESTIONAR_ESTADOS: {
			identificadorPlantillasGestion(dto);
			break;
		}
		case Propiedades.UPDATE_INFORMATIVE_FIELD:
		case Propiedades.CAMPO_DIFERENCIAS:
		case Propiedades.DISPONIBILIDAD_CROQUIS:
		case Propiedades.MODIFICAR_CAMPO:
		case Propiedades.DEPENDE: 
		case Propiedades.FECHA_MAXIMA_CAMPO:
		case Propiedades.FECHA_MINIMA_CAMPO:
		case Propiedades.GENERA_DOCUMENTO_CAMPO:
		case Propiedades.GENERA_DOCUMENTO_CAMPO_FROM_EXPEDIENTE:
		case Propiedades.GENERA_DOCUMENTO_DEL_RESULTADO_ITERACION:
		case Propiedades.GENERA_DOCUMENTO_CAMPO_FROM_GENERADOR:
		case Propiedades.RELACIONAR_DOCUMENTOS:
		case Propiedades.RETIRAR_DOCUMENTOS:
		case Propiedades.PLANTILLA_CARGA_MASIVA_MULTIPLE:
		case Propiedades.TERCERO:
		case Propiedades.PERMISO_PLANTILLA_INICIO_RAPIDO:
		case Propiedades.DESCRIPCION:
		case Propiedades.CAMPO_EVIDENCIA:
		case Propiedades.DESCRIPCION_NIVEL2:
		case Propiedades.TOTAL:
		case Propiedades.CONSECUTIVO:
		case Propiedades.FECHA:
		case Propiedades.PLANTILLA_FECHA_FINAL:
		case Propiedades.PLANTILLA_FECHA_INICIO:
		case Propiedades.PLANTILLA_IMAGEN:
		case Propiedades.RESPONSABLE: 
		case Propiedades.INFORMATIVE_DATA: {
			return identificadorCampo(dto, token);
		}
		case Propiedades.PROCESO_VALOR: {
			identificadorValorProceso(dto, token);
			break;
		}
		case Propiedades.ENCABEZADO: {
			break;
		}
		case Propiedades.ORDEN: {
			if (dto.getValor().compareTo("D") == 0) {
				dto.setTexto("POR DESCRIPCION");
			} else {
				dto.setValor("N");
				dto.setTexto("POR NOMBRE");
			}
			return false;
		}
		case Propiedades.CUENTA_SOBREGIRO:
		case Propiedades.PRODUCTOS_FUNCION_CAMPO:
		case Propiedades.PRODUCTOS_TERCERO: {
			return identificadorCampo(dto, token);
		}
		case Propiedades.PRODUCTO_PUESTO: {
			identificadorProducto(dto);
			break;
		}
		case Propiedades.PRODUCTO_CAMPO_VALOR_MINIMO:
		case Propiedades.PRODUCTO_CAMPO_VALOR_UNITARIO:
		case Propiedades.PRODUCTO_CAMPO_CANTIDAD:
		case Propiedades.PRODUCTO_CAMPO_TOTAL:
		case Propiedades.PERMISO_PLANTILLA_CAMPO_FILTRO: {
			return identificadorCampo(dto, token);
		}
		case Propiedades.PLANTILLA_TIPO_PRODUCTO:
		case Propiedades.DETALLE_CATEGORIA: {
			identificadorCategoriaProducto(dto, token);
			break;
		}
		case Propiedades.REPORTE_ENCABEZADO:
		case Propiedades.REPORTE_PIE_PAGINA:
		case Propiedades.REPORTE_EXCEL:
		case Propiedades.P_SUBREPORT_:
		case Propiedades.MENSAJE_REPORTE:
		case Propiedades.REPORTE_ENCABEZADO_EXCEL: {
			identificadorReporte(dto);
			break;
		}
		case Propiedades.REP_VISIBLE_STATE: {
			identificadorEstadoReporte(dto);
			break;
		}
		case Propiedades.REPORTE_JRXML: {
			identificadorJRXML(dto);
			break;
		}
		case Propiedades.REPORTE_IMAGEN: {
			vaildarBase64(dto);
			break;
		}

		case Propiedades.MENSAJE: {
			identificadorMensaje(dto);
			break;
		}
		case Propiedades.API_AUTHENTICATION:
		case Propiedades.API_BASE:
		case Propiedades.API_TRANSACCION:
		case Propiedades.TEMPLATE_VOUCHER:
		case Propiedades.API: {
			identificadorApi(dto, token);
			break;
		}
		case Propiedades.MENSAJE_DESTINATARIO:
		case Propiedades.PUBLIC_USER:
		case Propiedades.ESTADO_ASIGNAR: {
			identificadorUsuario(dto);
			break;
		}

		case Propiedades.ROL: {
			identificadorRol(dto, token);
			break;
		}
		case Propiedades.CELULAR_ROL:
		case Propiedades.CORREO_ROL: {
			return identificadorCampo(dto, token);
		}
		case Propiedades.COLOR: {
			identificarColor(dto);
			break;
		}

		case Propiedades.DETALLE_TARIFARIO: {
			identificadorTarifario(dto);
			break;
		}
		case Propiedades.PERMISO_PLANTILLA_MODIFICAR: {
			validateTemplateDifference(dto, token);
			break;
		}
		case Propiedades.PERIODO_LIMPIEZA_HISTORICO:
		case Propiedades.TEMPORIZADOR: {
			validarTemporizador(dto);
			break;
		}
		case Propiedades.API_ACCOUNT_CATALOG:
		case Propiedades.PLANTILLA_MONITOR: {
			identificadorCatalogo(dto);
			break;
		}
		case Propiedades.PERMISO_PLANTILLA_LISTAR_MENU: {
			identificadorProceso(dto);
			break;
		}
		}
		return false;
	}

	private void validateTemplateDifference(PropiedadDTO dto, String token) throws ServerException {
		if (getPropertyDifferenceTemplate(dto.getCampo()) == null)
			guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA, dto.getCampo(),
					Propiedades.PLANTILLA_DIFERENCIAS, "*", token), token);
		try {
			List<PropiedadDTO> pendientes = propiedadMapper.getTemplateWithoutUpdate();
			if (pendientes == null || pendientes.isEmpty())
				return;
			for (PropiedadDTO propiedadDTO : pendientes) {
				guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA, propiedadDTO.getCampo(),
						Propiedades.PLANTILLA_DIFERENCIAS, "*", token), token);
			}
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}

	}

	private void validarTemporizador(PropiedadDTO dto) throws ServerException {
		if (dto.getFechaInicial() == null)
			dto.setFechaInicial(new Date());
		if (dto.getTexto() == null)
			throw new ServerException(
					"Cuando registras una propiedad de temporizador debes colocar en el texto la clave de tiempo de repeticion. Observa la ayuda");
	}

	/*
	 * private void relacionarCampo(PropiedadDTO dto, String token) throws
	 * ServerException { switch (dto.getKey()) { case Propiedades.TERCERO: { break;
	 * } case Propiedades.DESCRIPCION: { break; } case
	 * Propiedades.DESCRIPCION_NIVEL2: { break; } case Propiedades.TOTAL: { break; }
	 * case Propiedades.CONSECUTIVO: { break; } case Propiedades.FECHA: { break; }
	 * case Propiedades.RESPONSABLE: { break; } case Propiedades.DEPENDE: { break; }
	 * case Propiedades.MODIFICAR_CAMPO: { break; } case
	 * Propiedades.INFORMATIVE_DATA: { break; } default: { return; } }
	 * RelacionInternaDTO relacion = new RelacionInternaDTO();
	 * relacion.setPropiedad(dto.getLlaveTabla());
	 * relacion.setCampo(dto.getValor()); relacionService.guardar(relacion, token);
	 * }
	 */

	public String obtenerUnica(String tipo, String plantilla, String key, String usuario) throws ServerException {
		PropiedadDTO filtroOrden = obtenerPropiedad(tipo, plantilla, key, usuario);
		if (filtroOrden == null)
			return null;
		return filtroOrden.getValor();
	}

	public PropiedadDTO obtenerPropiedad(String tipo, String plantilla, String key, String usuario)
			throws ServerException {
		if (plantilla == null)
			throw new ServerException("El campo esta nulo");
		List<PropiedadDTO> propiedades = obtenerPropiedades(tipo, plantilla, key, usuario);
		if (propiedades == null || propiedades.isEmpty())
			return null;
		return propiedades.get(0);
	}

	public List<PropiedadDTO> obtenerPropiedades(String tipo, String entidad, String key, String usuario)
			throws ServerException {
		return obtenerPropiedades(tipo, entidad, key, usuario, null);
	}
	
	public List<PropiedadDTO> obtenerPropiedades(String tipo, String entidad, String key, String usuario, Boolean privada)
			throws ServerException {
		if (entidad == null)
			throw new ServerException("El campo esta nulo");
		return obtenerPropiedadesSinEntidad(tipo, entidad, key, usuario, privada);
	}

	public List<PropiedadDTO> obtenerPropiedadesSinEntidad(String tipo, String entidad, String key, String usuario)
			throws ServerException {
		return obtenerPropiedadesSinEntidad(tipo, entidad, key, usuario, null);
	}
	
	// Los dividi oara optimizar el menu de usuario y asi consultar los estados
	// todas las propiedades
	public List<PropiedadDTO> obtenerPropiedadesSinEntidad(String tipo, String entidad, String key, String usuario, Boolean privada)
			throws ServerException {
		PropiedadFilterDTO filtroOrden = new PropiedadFilterDTO();
		filtroOrden.setTipo(tipo);
		filtroOrden.setCampo(entidad);
		if (key != null) {
			PropiedadValorDefinidoDTO valorDefinido = consultarValorDefinido(tipo, key);
			filtroOrden.setPropiedadValor(valorDefinido.getLlaveTabla());
		}
		List<PropiedadDTO> consultadas = propiedadMapper.consultarRol(filtroOrden, usuario, new Date(), privada);
		if (usuario != null) {
			return cleanPropertiesFromTimeAndExclusion(consultadas);
		}
		return consultadas;
	}
	
	public List<PropiedadDTO> getToUser(String usuario)
			throws ServerException {
		return propiedadMapper.consultarPermisosUsuario(usuario);
	}

	private List<PropiedadDTO> cleanPropertiesFromTimeAndExclusion(List<PropiedadDTO> consultadas) {

		List<PropiedadDTO> validadas = new ArrayList<PropiedadDTO>();
		List<PropiedadDTO> excluidas = new ArrayList<PropiedadDTO>();

		if (!consultadas.isEmpty()) {
			// Valido bloqueo por exclusion
			for (PropiedadDTO iPropiedadDTO : consultadas) {
				if (iPropiedadDTO.getUsuarioExcluyente() != null || iPropiedadDTO.getRolExcluyente() != null)
					excluidas.add(iPropiedadDTO);
			}
			if (!excluidas.isEmpty()) {
				for (PropiedadDTO iPropiedadDTO : excluidas) {
					// Aqui me di cuenta que estaba borrando todas las propiedades de la plantilla
					// Filtro el valor: en las opciones de una lista (multiple) se puede dejar de un
					// tipo y mostrar el otro
					consultadas.removeIf(x -> (x.getPropiedadValor().compareTo(iPropiedadDTO.getPropiedadValor()) == 0
							&& x.getCampo().compareTo(iPropiedadDTO.getCampo()) == 0
							&& x.getValor().compareTo(iPropiedadDTO.getValor()) == 0));
				}
			}
			// Valido bloqueo por tiempo
			for (PropiedadDTO iPropiedadDTO : consultadas) {
				if (Propiedades.validarBloqueo(iPropiedadDTO))
					validadas.add(iPropiedadDTO);
			}
		}

		return validadas;

	}

	public List<PropiedadDTO> obtenerEspecialFullPermisos(String plantilla) throws ServerException {
		PropiedadDTO filtroOrden = new PropiedadDTO();
		filtroOrden.setTipo(PropiedadValorDefinidoDTO.PLANTILLA);
		filtroOrden.setCampo(plantilla);
		return propiedadMapper.consultarPermisosFullPlantilla(filtroOrden);
	}

	public List<PropiedadDTO> obtenerEspecialFullPermisosSimplificandoBD(List<DocumentoPlantillaDTO> plantillas)
			throws ServerException {
		return propiedadMapper.obtenerEspecialFullPermisosSimplificandoBD(plantillas);
	}

	private void identificadorMensaje(PropiedadDTO dto) throws ServerException {
		MensajePlantillaCorreoDTO bd = mensajeService.consultaXId(dto.getValor());
		// Si es actualizar valido por el id
		if (bd == null) {
			// VAlido por el nombre
			MensajePlantillaCorreoFilterDTO bdFilter = new MensajePlantillaCorreoFilterDTO();
			bdFilter.setNombre(dto.getValor().toUpperCase());
			bdFilter.setEstado(SharedConstants.STATE_ACTIVE);
			bd = mensajeService.consultaUnica(bdFilter);
			if (bd == null)
				throw new ServerException("El mensaje no fue reconocido");
		}
		dto.setValor(bd.getLlaveTabla());
		dto.setTexto(bd.getNombre());
	}

	private void identificadorApi(PropiedadDTO dto, String token) throws ServerException {
		
		WebServiceDTO bd = apiService.consultaXId(dto.getValor());
		// Si es actualizar valido por el id
		if (bd == null) {
			// VAlido por el nombre
			WebServiceFilterDTO bdFilter = new WebServiceFilterDTO();
			bdFilter.setNombre(dto.getValor().toUpperCase());
			bdFilter.setEstado(SharedConstants.STATE_ACTIVE);
			bd = apiService.consultaUnica(bdFilter);
			if (bd == null) {// Consulto por codigo
				bdFilter = new WebServiceFilterDTO();
				bdFilter.setCodigo(dto.getValor().toUpperCase());
				bdFilter.setEstado(SharedConstants.STATE_ACTIVE);
				bd = apiService.consultaUnica(bdFilter);
			}
			if (bd == null) {
				if (dto.getKey().compareTo(Propiedades.TEMPLATE_VOUCHER) == 0) {
					if (dto.getValor().compareTo("*") == 0) {
					
						DocumentoPlantillaDTO _template = plantillaService.consultaXId(dto.getCampo());
						if(_template ==null) throw new ServerException("Al crear el api no se identifico plantilla");
						
						bd = apiService.createVocherTemplate(token, _template.getNombre(), _template.getCodigo());
					} else {
						throw new ServerException("El api " + dto.getValor().toUpperCase()
								+ " no fue reconocido para la propiedad " + dto.getKey());
					}
				} else {
					throw new ServerException("El api " + dto.getValor().toUpperCase()
							+ " no fue reconocido para la propiedad " + dto.getKey());
				}
			}
		}
		dto.setValor(bd.getLlaveTabla());
		dto.setTexto(bd.getNombre());
	}

	public void validarFuncionConsultandoPropiedad(BasicParamDTO dto, String tipo, String documento, String modificador,
			String usuario, String token) throws ServerException {
		dto.setPropiedades(obtenerPropiedades(tipo, dto.getLlaveTabla(), null, usuario));
		if (dto.getPropiedades() == null)
			throw new ServerException("NO se logro consultar las propiedades del documento");
		validarFuncionConsultandoPropiedad(dto, documento, modificador, usuario, token);
	}

	public void validarFuncionConsultandoPropiedad(BasicParamDTO dto, String documento, String modificador,
			String usuario, String token) throws ServerException {
		List<PropiedadDTO> validaciones = Propiedades.obtenerVariosParametro(dto, Propiedades.FUNCION_SQL_VALIDAR);
		if (validaciones == null || validaciones.isEmpty())
			return;
		for (PropiedadDTO pPropiedad : validaciones) {
			System.out.format("\nValidando funcion SQL (%s)", pPropiedad.getMotivo());
			validarFuncion(pPropiedad, documento, modificador, token);
		}
	}
	
	public String templateNotifications(String pPropertyId, List<PedidoVentaCaracteristicaDTO> campos, String documento,
			String token) throws ServerException {
		String _result = null;
		try {
			_result =  propiedadMapper.funcionPrevalidacionPlantillaReturnString(SoftureUtil.formatFunction(pPropertyId),
					documento, token, campos);
		} catch (Exception e) {
			throw new ServerException(e.getMessage());
		}
		if (_result.compareTo("OK") == 0)
			_result = null;
		return _result;
	}

	public void prevalidate(BasicParamDTO dto, List<PedidoVentaCaracteristicaDTO> campos, String documento,
			String token) throws ServerException {
		List<PropiedadDTO> validaciones = Propiedades.obtenerVariosParametro(dto,
				Propiedades.FUNCION_SQL_VALIDAR_ANTES);
		if (validaciones == null || validaciones.isEmpty())
			return;
		for (PropiedadDTO pPropiedad : validaciones) {
			log.debug("\nPre validando funcion SQL (%s)", pPropiedad.getMotivo());
			try {
				propiedadMapper.funcionPrevalidacionPlantilla(SoftureUtil.formatFunction(pPropiedad.getLlaveTabla()),
						documento, token, campos);
			} catch (Exception e) {
				throw new ServerException(e.getMessage());
			}
		}
	}

	// Esto es importante para que cuando falle la transaccion no se bloquee
	// https://medium.com/geekculture/spring-transactional-rollback-handling-741fcad043c6
	@Transactional(value = "transactionManager", propagation = Propagation.REQUIRES_NEW)
	public String prevalidateAPI(BasicParamDTO dto, String document, String editor, String extractions) {
		List<PropiedadDTO> validaciones = Propiedades.obtenerVariosParametro(dto,
				Propiedades.FUNCION_SQL_PREVALIDATE_API);
		if (validaciones == null || validaciones.isEmpty())
			return null;
		for (PropiedadDTO pPropiedad : validaciones) {
			log.debug("Pre validando APIfuncion SQL (%s)", pPropiedad.getMotivo());
			try {
				String extractionsWithEnd = extractions;
				if (extractionsWithEnd != null)
					extractionsWithEnd = extractionsWithEnd + ";;";
				if(Propiedades.isFunctionNotFreeMarker(pPropiedad.getValor())) {
					propiedadMapper.funcionPrevalidateAPI(SoftureUtil.formatFunction(pPropiedad.getLlaveTabla()), document,
							editor, extractionsWithEnd);	
				} else {
					String parameters = extractionsWithEnd;
					/*for (PedidoVentaCaracteristicaDTO iProp : extractionsWithEnd) {
						parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE + "R_" + iProp.getCampoDTO().getCodigo() + SharedConstants.IGUAL;
						
						if(iProp.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.NUMERO)==0) {
							parameters = parameters + ((iProp.getValorNumero()==null)?"0":iProp.getValorNumero().toString());
						}else {
							parameters = parameters + ((iProp.getValorText()==null)?"0":iProp.getValorText());
						}
					}*/
					String _result = templatesService.generateOutputFile(pPropiedad.getValor(), parameters);
					if (_result!= null && _result.isEmpty()) return null;
					return _result;
				}
			} catch (Exception se) {
				if (se.getCause() != null) {
					return se.getCause().getMessage();
				} else {
					return se.getMessage();
				}
			}
		}
		return null;
	}

	public void validarFuncion(PropiedadDTO dto, String documento, String modificador, String token)
			throws ServerException {
		String respuestaValidacion = null;
		try {
			respuestaValidacion = propiedadMapper.funcionAsignacion(SoftureUtil.formatFunction(dto.getLlaveTabla()),
					documento, modificador, token);
		} catch (Exception e) {
			throw new ServerException(e.getMessage(),
					" Motivo: " + dto.getMotivo() + " Propiedad : " + dto.getNombre());
		}
		if (respuestaValidacion == null)
			throw new ServerException("El resultado ha sido nulo de la validacion\nDecision : " + dto.getMotivo());
		if (respuestaValidacion.compareTo("S") != 0)
			throw new ServerException(respuestaValidacion, " Motivo: " + dto.getMotivo());
	}
	
	public String validarFuncionSQL2(PropiedadDTO dto, String template, String token)throws ServerException {
		String respuestaValidacion = null;
		try {
			respuestaValidacion = propiedadMapper.funcionPrevalidacionPlantillaReturnString(SoftureUtil.formatFunction(dto.getLlaveTabla()),
					template, token, null);
		} catch (UncategorizedSQLException e) {
			throw new ServerException(e.getLocalizedMessage());
		} catch (Exception e) {
			throw new ServerException(e.getMessage());
		}
		return respuestaValidacion;		
		
	}

	public List<PropiedadDTO> copiarPropiedades(List<PropiedadDTO> propiedadedBase, String entidad, String token)
			throws ServerException {
		List<PropiedadDTO> result = new ArrayList<PropiedadDTO>();// Existe otroparecido en helperjosn
		for (PropiedadDTO propiedadDTO : propiedadedBase) {
			boolean agregar = true;
			if (propiedadDTO.getKey().compareTo(Propiedades.TEMPORIZADOR) == 0)
				agregar = false; // Cuando copio esta propiedad queda mal y duplicada
			if (propiedadDTO.getKey().compareTo(Propiedades.PERIODO_LIMPIEZA_HISTORICO) == 0)
				agregar = false; // Cuando copio esta propiedad queda mal y duplicada
			if (propiedadDTO.getKey().compareTo(Propiedades.CAMPO_DIFERENCIAS) == 0)
				agregar = false; // Cuando copio esta propiedad queda mal y duplicada
			if (propiedadDTO.getKey().compareTo(Propiedades.PLANTILLA_DIFERENCIAS) == 0)
				agregar = false; // Cuando copio esta propiedad queda mal y duplicada
			if (propiedadDTO.getKey().compareTo(Propiedades.PERMISO_PLANTILLA_MODIFICAR) == 0)
				agregar = false; // Cuando copio esta propiedad se crea la plantilla de modificar y no es la idea
			if (propiedadDTO.getKey().compareTo(Propiedades.PLANTILLA_ANULAR) == 0)
				agregar = false; // CAsi siempre se borran estos permisos
			if (propiedadDTO.getKey().compareTo(Propiedades.PLANTILLA_ACTIVAR) == 0)
				agregar = false; // CAsi siempre se borran estos permisos
			if (agregar) {
				PropiedadDTO newPropiedad = new PropiedadDTO();
				newPropiedad.setCampo(entidad);
				// newPropiedad.setCodigo(propiedadDTO.getCodigo());
				newPropiedad.setKey(propiedadDTO.getKey());
				newPropiedad.setMotivo(propiedadDTO.getMotivo());
				// newPropiedad.setNecesario(propiedadDTO.getNecesario());
				newPropiedad.setNombre(propiedadDTO.getNombre());
				newPropiedad.setPropiedadValor(propiedadDTO.getPropiedadValor());
				newPropiedad.setTipo(propiedadDTO.getTipo());
				newPropiedad.setRol(propiedadDTO.getRol());
				newPropiedad.setUsuario(propiedadDTO.getUsuario());
				newPropiedad.setFechaInicial(propiedadDTO.getFechaInicial());
				newPropiedad.setFechaFinal(propiedadDTO.getFechaFinal());
				if (propiedadDTO.getKey().compareTo(Propiedades.REPORTE_IMAGEN) == 0
						|| propiedadDTO.getKey().compareTo(Propiedades.OPCIONES) == 0
						|| propiedadDTO.getKey().compareTo(Propiedades.API_CODE_REPLACE) == 0
						|| propiedadDTO.getKey().compareTo(Propiedades.API_CODE_ESPECIAL) == 0
						|| propiedadDTO.getKey().compareTo(Propiedades.API_EXTRACTION) == 0
						|| propiedadDTO.getKey().compareTo(Propiedades.API_EXTRACTION_NO_ERROR) == 0
						|| propiedadDTO.getKey().compareTo(Propiedades.API_EXTRACTION_TO_BASE_64) == 0) {
					newPropiedad.setTexto(propiedadDTO.getTexto());
					newPropiedad.setValor(propiedadDTO.getValor());
				} else {
					// Sucede que los campos de una plantilla los copiaba mal referenciados
					newPropiedad.setValor(propiedadDTO.getTexto());
					if (newPropiedad.getValor() == null) {
						newPropiedad.setValor(propiedadDTO.getValor());
					}
				}
				newPropiedad = guardar(newPropiedad, token);
				result.add(newPropiedad);
				relacionService.copyFromProperty(propiedadDTO.getLlaveTabla(), newPropiedad.getLlaveTabla(), token,
						propiedadDTO.getCambioCreacion(), false);
			}
		}
		return result;
	}

	private void identificadorValorProceso(PropiedadDTO dto, String token) throws ServerException {
		if (dto.getValor().compareTo("1") == 0)
			return;
		if (dto.getValor().compareTo("2") == 0)
			return;
		if (dto.getValor().compareTo("0") == 0)
			return;
		identificadorCampo(dto, token);
	}

	private void identificadorJRXML(PropiedadDTO dto) throws ServerException {
		if (dto.getValor().contains("language=\"groovy\""))
			throw new ServerException("El lenguaje del reporte debe ser java.");
		int imageCount = StringUtils.countMatches(dto.getValor(), "<image ");
		if (imageCount != 0) {
			if (StringUtils.countMatches(dto.getValor(), "onErrorType=\"Blank\"") < imageCount)
				throw new ServerException(
						"Todas las imagenes del reporte deben tener la propiedad On Error Type con valor = Blank.");
			if (StringUtils.countMatches(dto.getValor(), "imageExpression><![CDATA[$") + StringUtils
					.countMatches(dto.getValor(), "imageExpression><![CDATA[new ByteArrayInputStream") < imageCount) {
				ReporteBaseDTO report = reporteService.consultaXId(dto.getCampo());
				if (report.getVariables() != null && report.getVariables().toUpperCase().contains("HTML")) {
					if (StringUtils.countMatches(dto.getValor(), "isLazy=\"true\"") < imageCount)
						throw new ServerException(
								"Todas las imagenes de un reporte deben tener la propiedad isLazy con valor = true.");
				} else {
					// throw new ServerException(
					// "Todas las imagenes del reporte deben instanciarse como una propiedad REPORTE
					// IMAGEN en el reporte "
					// + report.getNombre() + " de la plantilla " + report.getPlantillaNombre());
				}
			}
		}
		reportCacheService.clearCache();
	}

	private void vaildarBase64(PropiedadDTO dto) throws ServerException {
		int imageCount = StringUtils.countMatches(dto.getValor(), ",");
		if (imageCount != 1)
			throw new ServerException("Incluya la parte inicial del codigo base 64");
		if (dto.getTexto() == null || dto.getTexto().isEmpty())
			throw new ServerException("No olvides colocar el nombre del parametro");
	}

	public void actualizarValorPropiedad(String key, String name) throws ServerException {
		PropiedadDTO filter = new PropiedadDTO();
		filter.setValor(key);
		filter.setTexto(name);
		propiedadMapper.actualizarValorPropiedad(filter);
	}

	/*
	 * La uso en pedidoventa para listar los campos que se realacionan en un
	 * heredable de muchas plantillas lo empeza a usar en campo filtro para
	 * simplificar el paso de las propiedades
	 */
	public List<String> camposRelacionados(PropiedadDTO propiedad) throws ServerException {
		List<String> result = null;
		List<RelacionInternaDTO> relaciones = relacionService.relacionesPropiedad(propiedad.getLlaveTabla());
		if (relaciones == null || relaciones.isEmpty())
			return null;
		result = new ArrayList<String>();
		for (RelacionInternaDTO relacionInternaDTO : relaciones) {
			result.add(relacionInternaDTO.getCampo());
		}
		return result;
	}

	/*
	 * La uso para programar tareas automaticas
	 */
	public List<PropiedadDTO> consultarTemporizadoresPendientes() throws ServerException {
		return propiedadMapper.consultarTemporizadoresPendientes();
	}

	public List<PropiedadDTO> listarProductoSimplificar(List<ProductoDTO> productos) throws ServerException {
		if (productos == null || productos.isEmpty())
			return new ArrayList<PropiedadDTO>();
		return propiedadMapper.listarProductoSimplificado(productos);
	}

	public List<PropiedadDTO> getFullPropertiesToConfiguration() throws ServerException {
		return propiedadMapper.getFullPropertiesToConfiguration();
	}

	public List<PropiedadDTO> listarPlantillasSimplificar(List<DocumentoPlantillaDTO> plantillas, String usuario)
			throws ServerException {

		List<PropiedadDTO> consultadas = propiedadMapper.listarPlantillasSimplificar(plantillas, usuario, new Date());
		return cleanPropertiesFromTimeAndExclusion(consultadas);
	}

	public String ubicarPropiedad(PropiedadDTO propiedad) throws ServerException {
		if (propiedad == null || propiedad.getTipo() == null)
			throw new ServerException("Los datos de la propiedad estan nulos");
		switch (propiedad.getTipo()) {
		case PropiedadValorDefinidoDTO.API_SERVICE:
			return "SERVICIO API";
		case PropiedadValorDefinidoDTO.CAMPO:
			return "CAMPO";
		case PropiedadValorDefinidoDTO.ESTADO:
			return "ESTADO";
		case PropiedadValorDefinidoDTO.ORGANIZACION:
			return "ORGANIZACION";
		case PropiedadValorDefinidoDTO.PLANTILLA:
			DocumentoPlantillaDTO plantillaBD = plantillaService.consultaXId(propiedad.getCampo());
			return "PLANTILLA : " + plantillaBD.getNombre().toLowerCase();
		case PropiedadValorDefinidoDTO.PROCESO:
			return "PROCESO";
		case PropiedadValorDefinidoDTO.REPORTE:
			return "REPORTE";
		case PropiedadValorDefinidoDTO.SERVIDOR:
			return "SERVIDOR";
		case PropiedadValorDefinidoDTO.TRANSICION:
			ProcesoTransicionDTO bdTR = transicionService.consultaXId(propiedad.getCampo());
			return "TRANSICION : " + bdTR.getNombre().toLowerCase() + " \n PROCESO: "
					+ bdTR.getProcesoNombre().toLowerCase() + " \nPLANTILLA : "
					+ bdTR.getPlantillaNombre().toLowerCase() + "\n\n";
		}
		return "";
	}

	public List<PropiedadDTO> clearResponseProperties(List<PropiedadDTO> pProperties) {
		if (pProperties == null)
			return new ArrayList<>();
		for (PropiedadDTO propiedadDTO : pProperties) {
			if (propiedadDTO.getKey().contains("SQL"))
				propiedadDTO.setValor("");
			propiedadDTO.setUsuario(null);
			propiedadDTO.setUsuarioExcluyente(null);
			propiedadDTO.setUsuarioExcluyenteNombre(null);
			propiedadDTO.setUsuarioNombre(null);
			propiedadDTO.setRol(null);
			propiedadDTO.setRolExcluyente(null);
			propiedadDTO.setRolExcluyenteNombre(null);
			propiedadDTO.setRolNombre(null);
			propiedadDTO.setCambioCreacion(null);
			propiedadDTO.setCambioEliminacion(null);
			propiedadDTO.setFechaDefinicion(null);
			propiedadDTO.setFechaImplementacion(null);
			propiedadDTO.setFechaFinal(null);
			propiedadDTO.setFechaInicial(null);
			propiedadDTO.setBloqueo(null);
		}
		return pProperties;
	}

	public void guardarEnCasoQueNoExista(PropiedadDTO dto, String token) throws ServerException {
		// Lo copie de guardar depronto lo puedo refacorizar
		PropiedadFilterDTO existeFilter = new PropiedadFilterDTO();
		existeFilter.setCampo(dto.getCampo());
		if (dto.getPropiedadValor() == null)
			existeFilter.setPropiedadValor(
					consultarValorDefinido(dto.getTipo(), dto.getKey()).getLlaveTabla());
		existeFilter.setEstado(SharedConstants.STATE_ACTIVE);
		existeFilter.setKey(dto.getKey());
		existeFilter.setTipo(dto.getTipo());
		List<PropiedadDTO> _actualProperties = listarConsulta(existeFilter);
		if (_actualProperties == null || _actualProperties.isEmpty()) {
			guardar(dto, token);
		}else {
			//No se si crear la validacion
			//if(_actualProperties.size()>1)
				//throw new ServerException("Esta propiedad esta doble" + dto.getKey() );
		}
		/*PropiedadDTO existe = consultaUnica(existeFilter);
		if (existe == null)
			guardar(dto, token);*/
	}

}