package com.softure.document_execution.application.field;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.authorization.application.RolAccesoSvc;
import com.softure.authorization.domain.RolAccesoDTO;
import com.softure.authorization.domain.RolAccesoFilterDTO;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.inventory.application.BodegaSvc;
import com.softure.inventory.application.CategoriaProductoSvc;
import com.softure.inventory.application.ProductoSvc;
import com.softure.inventory.domain.BodegaDTO;
import com.softure.inventory.domain.BodegaFilterDTO;
import com.softure.inventory.domain.CategoriaProductoDTO;
import com.softure.inventory.domain.CategoriaProductoFilterDTO;
import com.softure.inventory.domain.ProductoDTO;
import com.softure.inventory.domain.ProductoFilterDTO;
import com.softure.logisticpymes.application.CambioSvc;
import com.softure.logisticpymes.domain.CambioDTO;
import com.softure.logisticpymes.domain.CambioFilterDTO;
import com.softure.process_designer.application.ProcesoSvc;
import com.softure.process_designer.domain.ProcesoDTO;
import com.softure.process_designer.domain.ProcesoFilterDTO;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.process_form.domain.DocumentoPlantillaFilterDTO;
import com.softure.property.domain.PropiedadDTO;
import com.softure.survey.application.EncuestaSvc;
import com.softure.survey.domain.EncuestaDTO;
import com.softure.survey.domain.EncuestaFilterDTO;
import com.softure.tariff.application.base.TarifarioService;
import com.softure.tariff.domain.TarifarioDTO;
import com.softure.tariff.domain.TarifarioFilterDTO;

@Component
public class TipoConfiguracion {

	@Autowired @Lazy 
	private BodegaSvc bodegaService;
	@Autowired @Lazy 
	private CambioSvc cambioService;
	@Autowired @Lazy 
	private CategoriaProductoSvc categoriaProductoService;
	@Autowired @Lazy 
	private DocumentoPlantillaCaracteristicaSvc caracteristicaService;
	@Autowired @Lazy 
	private DocumentoPlantillaSvc plantillaService;
	@Autowired @Lazy 
	private EncuestaSvc encuestaService;
	@Autowired @Lazy 
	private PedidoVentaCaracteristicaSvc campoService;
	@Autowired @Lazy 
	private ProcesoSvc procesoService;
	@Autowired @Lazy 
	private ProductoSvc productoService;
	@Autowired @Lazy 
	private RolAccesoSvc rolService;
	@Autowired @Lazy 
	private TarifarioService tarifarioService;

	public static final String CATEGORIA_PRODUCTOS = "CATEGORIA_PRODUCTOS";
	public static final String PROCESO = "PROCESO";
	public static final String PRODUCTOS = "PRODUCTOS";
	public static final String PLANTILLAS = "PLANTILLAS";
	public static final String ROLES = "ROLES";
	public static final String ENCUESTAS = "ENCUESTAS";
	public static final String TARIFARIO = "TARIFARIO";
	public static final String BODEGAS = "BODEGAS";
	public static final String FORMATO_EXPORTAR = "FORMATO_EXPORTAR";
	public static final String CAMBIO = "CAMBIO";
	public static final String REQUERIMIENTO = "REQUERIMIENTO";

	public void cargarConsultaCampo(PedidoVentaCaracteristicaDTO pCampo) throws ServerException {
		if (pCampo.getValorOpcion() != null) {
			String valorConfiguracion = Propiedades.obtenerValor(pCampo.getCampoDTO(),
					Propiedades.CONFIGURACION_ENTIDAD);
			if (valorConfiguracion.isEmpty()) {
				PedidoVentaDTO adaptado = new PedidoVentaDTO();
				adaptado.setLlaveTabla(pCampo.getValorOpcion());
				adaptado.setNombre(pCampo.getValorText());
				pCampo.setPrincipal(adaptado);
			} else {
				switch (valorConfiguracion) {
				case CATEGORIA_PRODUCTOS:
					CategoriaProductoDTO categoria = categoriaProductoService.consultaXId(pCampo.getValorOpcion());
					if (categoria == null) {
						throw new ServerException("No se identifica el categoria");
					} else {
						PedidoVentaDTO adaptado = new PedidoVentaDTO();
						adaptado.setLlaveTabla(categoria.getLlaveTabla());
						adaptado.setImagen(categoria.getImagen());
						adaptado.setNombre("");
						adaptado.setDescripcion(categoria.getNombre());
						pCampo.setPrincipal(adaptado);
					}
					break;
				case PROCESO:
					ProcesoDTO proceso = procesoService.consultaXId(pCampo.getValorOpcion());
					if (proceso == null) {
						throw new ServerException("No se identifica el categoria");
					} else {
						PedidoVentaDTO adaptado = new PedidoVentaDTO();
						adaptado.setLlaveTabla(proceso.getLlaveTabla());
						adaptado.setImagen(SharedConstants.LOGO);
						adaptado.setNombre(proceso.getCodigo());
						adaptado.setDescripcion(proceso.getNombre());
						pCampo.setPrincipal(adaptado);
					}
					break;
				case PRODUCTOS:
					ProductoDTO producto = productoService.consultaXId(pCampo.getValorOpcion());
					if (producto == null) {
						throw new ServerException("No se identifica el producto");
					} else {
						PedidoVentaDTO adaptado = new PedidoVentaDTO();
						adaptado.setLlaveTabla(producto.getLlaveTabla());
						adaptado.setImagen(producto.getImagen());
						adaptado.setNombre(producto.getCodigo());
						adaptado.setDescripcion(producto.getNombre());
						pCampo.setPrincipal(adaptado);
					}
					break;
				case PLANTILLAS:
					DocumentoPlantillaDTO plantilla = plantillaService.consultaXId(pCampo.getValorOpcion());
					if (plantilla == null) {
						throw new ServerException("No se identifica el categoria");
					} else {
						PedidoVentaDTO adaptado = new PedidoVentaDTO();
						adaptado.setLlaveTabla(plantilla.getLlaveTabla());
						adaptado.setImagen(plantilla.getImagen());
						adaptado.setNombre(plantilla.getCodigo());
						adaptado.setDescripcion(plantilla.getNombre());
						pCampo.setPrincipal(adaptado);
					}
					break;
				case ROLES:
					RolAccesoDTO rolAcceso = rolService.consultaXId(pCampo.getValorOpcion());
					if (rolAcceso == null) {
						throw new ServerException("No se identifica el rol");
					} else {
						PedidoVentaDTO adaptado = new PedidoVentaDTO();
						adaptado.setLlaveTabla(rolAcceso.getLlaveTabla());
						adaptado.setImagen(rolAcceso.getImagen());
						adaptado.setNombre(rolAcceso.getCodigo());
						adaptado.setDescripcion(rolAcceso.getNombre());
						pCampo.setPrincipal(adaptado);
					}
					break;
				case CAMBIO:
					CambioDTO cambio = cambioService.consultaXId(pCampo.getValorOpcion());
					if (cambio == null) {
						throw new ServerException("No se identifica el cambio");
					} else {
						PedidoVentaDTO adaptado = new PedidoVentaDTO();
						adaptado.setLlaveTabla(cambio.getLlaveTabla());
						adaptado.setImagen(SharedConstants.LOGO);
						adaptado.setNombre(cambio.getNombre());
						adaptado.setDescripcion(cambio.getMotivo());
						pCampo.setPrincipal(adaptado);
					}
					break;
				case ENCUESTAS:
					EncuestaDTO encuesta = encuestaService.consultaXId(pCampo.getValorOpcion());
					if (encuesta == null) {
						throw new ServerException("No se identifica la encuesta");
					} else {
						PedidoVentaDTO adaptado = new PedidoVentaDTO();
						adaptado.setLlaveTabla(encuesta.getLlaveTabla());
						adaptado.setImagen(SharedConstants.LOGO);
						adaptado.setNombre(encuesta.getNombre());
						// adaptado.setDescripcion(encuesta.getNombre());
						pCampo.setPrincipal(adaptado);
					}
					break;
				case BODEGAS:
					BodegaDTO bodega = bodegaService.consultaXId(pCampo.getValorOpcion());
					if (bodega == null) {
						throw new ServerException("No se identifica bodega");
					} else {
						PedidoVentaDTO adaptado = new PedidoVentaDTO();
						adaptado.setLlaveTabla(bodega.getLlaveTabla());
						adaptado.setImagen(SharedConstants.LOGO);
						adaptado.setNombre(bodega.getNombre());
						adaptado.setDescripcion(bodega.getNombre());
						pCampo.setPrincipal(adaptado);
					}
					break;
				case FORMATO_EXPORTAR:
					PedidoVentaDTO adaptadoPDF = new PedidoVentaDTO();
					adaptadoPDF.setLlaveTabla("PDF");
					// adaptadoPDF.setImagen(plantilla.getImagen());
					adaptadoPDF.setNombre("PDF");
					// adaptadoPDF.setDescripcion(plantilla.getNombre());
					pCampo.setPrincipal(adaptadoPDF);

					PedidoVentaDTO adaptadoXLS = new PedidoVentaDTO();
					adaptadoXLS.setLlaveTabla("XLS");
					// adaptadoXLS.setImagen(plantilla.getImagen());
					adaptadoXLS.setNombre("XLS");
					// adaptadoXLS.setDescripcion(plantilla.getNombre());
					pCampo.setPrincipal(adaptadoXLS);
					break;
				case TARIFARIO:
					TarifarioDTO tarifario = tarifarioService.getById(pCampo.getValorOpcion());
					if (tarifario == null) {
						throw new ServerException("No se identifica tarifario");
					} else {
						PedidoVentaDTO adaptado = new PedidoVentaDTO();
						adaptado.setLlaveTabla(tarifario.getKey());
						adaptado.setImagen(SharedConstants.LOGO);
						adaptado.setNombre(tarifario.getNombre());
						// adaptado.setDescripcion(encuesta.getNombre());
						pCampo.setPrincipal(adaptado);
					}
					break;
				default:
					break;
				}
			}
		}
	}

	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL) == null
				&& (pCampo.getValorOpcion() == null || pCampo.getValorOpcion().isEmpty())) {
			List<PropiedadDTO> visibleValueOK = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(),
					Propiedades.VISIBLE_VALOR_DEPENDIENTE);
			if (visibleValueOK == null || pCampo.getDependientes() == null) {
				throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre()
						+ " es obligatorio registrar el campo " + pCampo.getCampoDTO().getNombre() + ")");
			} else {
				String optionsToSelect = null;
				for (PropiedadDTO propiedadDTO : visibleValueOK) {
					if (optionsToSelect != null)
						break;
					for (PedidoVentaCaracteristicaDTO iFieldDependent : pCampo.getDependientes()) {
						if (propiedadDTO.getValor().compareTo(iFieldDependent.getValorText()) == 0) {
							optionsToSelect = propiedadDTO.getValor();
							break;
						}
					}
				}
				if (optionsToSelect != null)
					throw new ServerException("Es obligatorio seleccionar un valor en el campo "
							+ pCampo.getCampoDTO().getNombre() + " cuando escoges la opcion " + optionsToSelect);
			}
		}
		if (pCampo.getValorOpcion() != null) {
			String valorConfiguracion = Propiedades.obtenerValor(pCampo.getCampoDTO(),
					Propiedades.CONFIGURACION_ENTIDAD);
			if (valorConfiguracion.isEmpty()) {
				List<PropiedadDTO> options = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(), Propiedades.OPCIONES);
				PropiedadDTO option =  null;
				if(options!=null) {
					for (PropiedadDTO propiedadDTO : options) {
						if(propiedadDTO.getValor().compareTo(pCampo.getValorOpcion())==0) {
							option = propiedadDTO;
							pCampo.setValorText(option.getTexto());
							break;
						}
					}	
				}
				if(option ==null) throw new ServerException("En el campo " + pCampo.getCampoDTO().getNombre() + " de la plantilla " + pCampo.getCampoDTO().getPlantillaNombre() +  " No se identifico la opcion a seleccionar "+ pCampo.getValorOpcion());
				// Ne bbx teniamos un campo de mas de 32 caracteres, no podia quitar el valos
				// opcion asi que lo restringui
				if (pCampo.getValorOpcion().length() > 32)
					pCampo.setValorOpcion(pCampo.getValorOpcion().substring(0, 32));
				//if (pCampo.getValorOpcion().length() != 32)
				if(pCampo.getValorText()==null) pCampo.setValorText(option.getValor());
			} else {
				switch (valorConfiguracion) {
				case CATEGORIA_PRODUCTOS:
					CategoriaProductoDTO categoria = categoriaProductoService.consultaXId(pCampo.getValorOpcion());
					if (categoria == null) {
						throw new ServerException("No se identifica el categoria");
					} else {
						pCampo.setValorText(categoria.getNombre());
					}
					break;
				case PROCESO:
					ProcesoDTO proceso = procesoService.consultaXId(pCampo.getValorOpcion());
					if (proceso == null) {
						throw new ServerException("No se identifica el procep");
					} else {
						pCampo.setValorText(proceso.getNombre());
					}
					break;
				case PRODUCTOS:
					ProductoDTO producto = productoService.consultaXId(pCampo.getValorOpcion());
					if (producto == null) {
						throw new ServerException("No se identifica el producto");
					} else {
						pCampo.setValorText(producto.getNombre());
					}
					break;
				case PLANTILLAS:
					DocumentoPlantillaDTO plantilla = plantillaService.consultaXId(pCampo.getValorOpcion());
					if (plantilla == null) {
						throw new ServerException("No se identifica el categoria");
					} else {
						pCampo.setValorText(plantilla.getNombre());
					}
					break;
				case ROLES:
					RolAccesoDTO rol = rolService.consultaXId(pCampo.getValorOpcion());
					if (rol == null) {
						throw new ServerException("No se identifica el categoria");
					} else {
						pCampo.setValorText(rol.getNombre());
					}
					break;
				case CAMBIO:
					CambioDTO cambio = cambioService.consultaXId(pCampo.getValorOpcion());
					if (cambio == null) {
						throw new ServerException("No se identifica el categoria");
					} else {
						pCampo.setValorText(cambio.getNombre());
					}
					break;
				case ENCUESTAS:
					EncuestaDTO encuesta = encuestaService.consultaXId(pCampo.getValorOpcion());
					if (encuesta == null) {
						throw new ServerException("No se identifica la encuesta");
					} else {
						pCampo.setValorText(encuesta.getNombre());
					}
					break;
				case BODEGAS:
					BodegaDTO bodega = bodegaService.consultaXId(pCampo.getValorOpcion());
					if (bodega == null) {
						throw new ServerException("No se identifica el bodega");
					} else {
						pCampo.setValorText(bodega.getNombre());
					}
					break;
				case FORMATO_EXPORTAR:
					pCampo.setValorText(pCampo.getValorOpcion());
					break;
				case TARIFARIO:
					TarifarioDTO tarifario = tarifarioService.getById(pCampo.getValorOpcion());
					if (tarifario == null) {
						throw new ServerException("No se identifica el tarifario");
					} else {
						pCampo.setValorText(tarifario.getNombre());
					}
					break;

				default:
					break;
				}
			}
		}
	}

	public PedidoVentaCaracteristicaDTO guardarCampo(PedidoVentaCaracteristicaDTO pCampo, String token)
			throws ServerException {
		PedidoVentaCaracteristicaDTO bd = campoService.buscarActivo(pCampo, pCampo.getPrincipal().getHistorico());
		if (bd != null) {
			if (pCampo.getValorOpcion() == null) {
				bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
				bd.setPrincipal(pCampo.getPrincipal());
				campoService.inactivar(bd, token);
				return pCampo;
			} else {
				if (bd.getValorOpcion()!=null && pCampo.getValorOpcion().compareTo(bd.getValorOpcion()) == 0) {
					return pCampo;
				} else {
					bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
					bd.setPrincipal(pCampo.getPrincipal());
					campoService.inactivar(bd, token);
				}
			}
		}
		if (pCampo.getValorOpcion() == null) {
			return pCampo;
		} else {
			return campoService.guardar(pCampo, token);
		}
	}

	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(PedidoVentaCaracteristicaFilterDTO pCampo)
			throws ServerException {
		DocumentoPlantillaCaracteristicaDTO pBase = caracteristicaService
				.consultaUnicaConComplementos(pCampo.getCampo(), pCampo.getSecurityToken());
		List<PropiedadDTO> campos = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(), Propiedades.OPCIONES);
		if (campos != null && !campos.isEmpty()) {
			pBase.setDocumentos(new ArrayList<PedidoVentaDTO>());
			for (PropiedadDTO iPropiedades : campos) {
				PedidoVentaDTO adaptadoT = new PedidoVentaDTO();
				adaptadoT.setLlaveTabla(iPropiedades.getValor());
				adaptadoT.setNombre(iPropiedades.getValor());
				if(iPropiedades.getTexto()!=null)
					adaptadoT.setDescripcion(iPropiedades.getTexto());
				if (adaptadoT.getNombre().length() > 32)
					adaptadoT.setNombre(adaptadoT.getNombre().substring(0, 32));
				//adaptadoT.setNombre(iPropiedades.getValor());
				pBase.getDocumentos().add(adaptadoT);
			}
		} else {
			switch (Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.CONFIGURACION_ENTIDAD)) {
			case CATEGORIA_PRODUCTOS:
				CategoriaProductoFilterDTO categoria = new CategoriaProductoFilterDTO();
				categoria.setFiltroParametro(pCampo.getFiltroParametro());
				categoria.setEstado(SharedConstants.STATE_ACTIVE);
				List<CategoriaProductoDTO> categorias = categoriaProductoService.listarConsulta(categoria);
				if (categorias != null && !categorias.isEmpty()) {
					pBase.setDocumentos(new ArrayList<PedidoVentaDTO>());
					for (CategoriaProductoDTO iCategoria : categorias) {
						PedidoVentaDTO adaptado = new PedidoVentaDTO();
						adaptado.setLlaveTabla(iCategoria.getLlaveTabla());
						adaptado.setImagen(iCategoria.getImagen());
						adaptado.setNombre(iCategoria.getNombre());
						// adaptado.setDescripcion(iCategoria.getNombre());
						pBase.getDocumentos().add(adaptado);
					}
				}
				break;
			case PROCESO:
				ProcesoFilterDTO proceso = new ProcesoFilterDTO();
				proceso.setFiltroParametro(pCampo.getFiltroParametro());
				proceso.setEstado(SharedConstants.STATE_ACTIVE);
				List<ProcesoDTO> procesos = procesoService.listarConsulta(proceso);
				if (procesos != null && !procesos.isEmpty()) {
					pBase.setDocumentos(new ArrayList<PedidoVentaDTO>());
					for (ProcesoDTO iProducto : procesos) {
						PedidoVentaDTO adaptado = new PedidoVentaDTO();
						adaptado.setLlaveTabla(iProducto.getLlaveTabla());
						adaptado.setImagen(SharedConstants.LOGO);
						adaptado.setNombre(iProducto.getCodigo());
						adaptado.setDescripcion(iProducto.getNombre());
						pBase.getDocumentos().add(adaptado);
					}
				}
				break;
			case PRODUCTOS:
				ProductoFilterDTO producto = new ProductoFilterDTO();
				producto.setFiltroParametro(pCampo.getFiltroParametro());
				producto.setEstado(SharedConstants.STATE_ACTIVE);
				List<ProductoDTO> productos = productoService.listarConsulta(producto);
				if (productos != null && !productos.isEmpty()) {
					pBase.setDocumentos(new ArrayList<PedidoVentaDTO>());
					for (ProductoDTO iProducto : productos) {
						PedidoVentaDTO adaptado = new PedidoVentaDTO();
						adaptado.setLlaveTabla(iProducto.getLlaveTabla());
						adaptado.setImagen(iProducto.getImagen());
						adaptado.setNombre(iProducto.getCodigo());
						adaptado.setDescripcion(iProducto.getNombre());
						pBase.getDocumentos().add(adaptado);
					}
				}
				break;
			case PLANTILLAS:
				if (pCampo.getCampoDTO() == null)
					throw new ServerException("valide el rol para consultar las plantillas");
				DocumentoPlantillaFilterDTO plantilla = new DocumentoPlantillaFilterDTO();
				plantilla.setFiltroParametro(pCampo.getFiltroParametro());
				plantilla.setSecurityToken(pCampo.getSecurityToken());
				// Pienso que se puede colocar una funcion para traer las plantillas del tipo y
				// mejorarlas
				// Tambien pienso que deberia tener un parametro de plantilla y agregar las
				// plantillas que queremos
				/*
				 * if( Propiedades.obtenerParametro(pBase,
				 * Propiedades.CONFIGURACION_PLANTILLA_TIPO)!=null){
				 * plantilla.setTipo(Propiedades.obtenerValor(pBase,
				 * Propiedades.CONFIGURACION_PLANTILLA_TIPO)); }
				 */
				List<DocumentoPlantillaDTO> plantillas = plantillaService.listarPlantillaRol(plantilla);
				if (plantillas != null && !plantillas.isEmpty()) {
					pBase.setDocumentos(new ArrayList<PedidoVentaDTO>());
					for (DocumentoPlantillaDTO iPlantilla : plantillas) {
						PedidoVentaDTO adaptado = new PedidoVentaDTO();
						adaptado.setLlaveTabla(iPlantilla.getLlaveTabla());
						adaptado.setImagen(iPlantilla.getImagen());
						adaptado.setNombre(iPlantilla.getCodigo());
						adaptado.setDescripcion(iPlantilla.getNombre());
						pBase.getDocumentos().add(adaptado);
					}
				}
				break;
			case ROLES:
				RolAccesoFilterDTO rolFiltro = new RolAccesoFilterDTO();
				rolFiltro.setNombre(pCampo.getFiltroParametro());
				rolFiltro.setEstado(SharedConstants.STATE_ACTIVE);
				List<RolAccesoDTO> roles = rolService.listarConsulta(rolFiltro);
				if (roles != null && !roles.isEmpty()) {
					pBase.setDocumentos(new ArrayList<PedidoVentaDTO>());
					for (RolAccesoDTO iCambio : roles) {
						PedidoVentaDTO adaptado = new PedidoVentaDTO();
						adaptado.setLlaveTabla(iCambio.getLlaveTabla());
						adaptado.setImagen(iCambio.getImagen());
						adaptado.setNombre(iCambio.getCodigo());
						adaptado.setDescripcion(iCambio.getNombre());
						pBase.getDocumentos().add(adaptado);
					}
				}
				break;
			case CAMBIO:
				CambioFilterDTO cambio = new CambioFilterDTO();
				cambio.setNombre(pCampo.getFiltroParametro());
				cambio.setEstado(SharedConstants.STATE_ACTIVE);
				List<CambioDTO> cambios = cambioService.listarConsulta(cambio);
				if (cambios != null && !cambios.isEmpty()) {
					pBase.setDocumentos(new ArrayList<PedidoVentaDTO>());
					for (CambioDTO iCambio : cambios) {
						PedidoVentaDTO adaptado = new PedidoVentaDTO();
						adaptado.setLlaveTabla(iCambio.getLlaveTabla());
						adaptado.setImagen(SharedConstants.LOGO);
						adaptado.setNombre(iCambio.getNombre());
						// adaptado.setDescripcion(iCategoria.getNombre());
						pBase.getDocumentos().add(adaptado);
					}
				}
				break;
			case ENCUESTAS:
				EncuestaFilterDTO encuesta = new EncuestaFilterDTO();
				encuesta.setFiltroParametro(pCampo.getFiltroParametro());
				encuesta.setEstado(SharedConstants.STATE_ACTIVE);
				List<EncuestaDTO> encuestas = encuestaService.listarConsulta(encuesta);
				if (encuestas != null && !encuestas.isEmpty()) {
					pBase.setDocumentos(new ArrayList<PedidoVentaDTO>());
					for (EncuestaDTO iEncuesta : encuestas) {
						PedidoVentaDTO adaptado = new PedidoVentaDTO();
						adaptado.setLlaveTabla(iEncuesta.getLlaveTabla());
						adaptado.setImagen(SharedConstants.LOGO);
						adaptado.setNombre(iEncuesta.getNombre());
						// adaptado.setDescripcion(iCategoria.getNombre());
						pBase.getDocumentos().add(adaptado);
					}
				}
				break;
			case BODEGAS:
				BodegaFilterDTO bodega = new BodegaFilterDTO();
				bodega.setFiltroParametro(pCampo.getFiltroParametro());
				bodega.setEstado(SharedConstants.STATE_ACTIVE);
				List<BodegaDTO> bodegas = bodegaService.listarConsulta(bodega);
				if (bodegas != null && !bodegas.isEmpty()) {
					pBase.setDocumentos(new ArrayList<PedidoVentaDTO>());
					for (BodegaDTO iBodega : bodegas) {
						PedidoVentaDTO adaptado = new PedidoVentaDTO();
						adaptado.setLlaveTabla(iBodega.getLlaveTabla());
						adaptado.setImagen(SharedConstants.LOGO);
						adaptado.setNombre(iBodega.getCodigo());
						adaptado.setDescripcion(iBodega.getNombre());
						pBase.getDocumentos().add(adaptado);
					}
				}
				break;
			case FORMATO_EXPORTAR:
				pBase.setDocumentos(new ArrayList<PedidoVentaDTO>());
				PedidoVentaDTO adaptadoPDF = new PedidoVentaDTO();
				adaptadoPDF.setLlaveTabla("PDF");
				// adaptadoPDF.setImagen(plantilla.getImagen());
				adaptadoPDF.setNombre("PDF");
				// adaptadoPDF.setDescripcion(plantilla.getNombre());
				pBase.getDocumentos().add(adaptadoPDF);

				PedidoVentaDTO adaptadoXLS = new PedidoVentaDTO();
				adaptadoXLS.setLlaveTabla("XLS");
				// adaptadoXLS.setImagen(plantilla.getImagen());
				adaptadoXLS.setNombre("XLS");
				// adaptadoXLS.setDescripcion(plantilla.getNombre());
				pBase.getDocumentos().add(adaptadoXLS);
				break;
			case TARIFARIO:
				TarifarioFilterDTO tarifario = new TarifarioFilterDTO();
				tarifario.setFilter(pCampo.getFiltroParametro());
				tarifario.setState(SharedConstants.STATE_ACTIVE);
				List<TarifarioDTO> tarifarios = tarifarioService.getMany(tarifario);
				if (tarifarios != null && !tarifarios.isEmpty()) {
					pBase.setDocumentos(new ArrayList<PedidoVentaDTO>());
					for (TarifarioDTO itarifario : tarifarios) {
						PedidoVentaDTO adaptadoT = new PedidoVentaDTO();
						adaptadoT.setLlaveTabla(itarifario.getKey());
						adaptadoT.setImagen(SharedConstants.LOGO);
						adaptadoT.setNombre(itarifario.getNombre());
						// adaptado.setDescripcion(iCategoria.getNombre());
						pBase.getDocumentos().add(adaptadoT);
					}
				}
				break;
			default:
				break;
			}
		}
		if (pBase.getDocumentos() == null)
			pBase.setDocumentos(new ArrayList<PedidoVentaDTO>());// Esto evita un ciclo infinito en el cliente
		pCampo.setCampoDTO(pBase);
		return pCampo;
	}
}
