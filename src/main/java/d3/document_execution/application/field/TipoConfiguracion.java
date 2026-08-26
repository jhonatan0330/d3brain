package d3.document_execution.application.field;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.authorization.application.RolAccesoSvc;
import d3.authorization.domain.RolAccesoDTO;
import d3.authorization.domain.RolAccesoFilterDTO;
import d3.document_execution.application.CallDocumentCommons;
import d3.document_execution.application.PedidoVentaCaracteristicaSvc;
import d3.document_execution.domain.PedidoVentaCaracteristicaDTO;
import d3.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import d3.document_execution.domain.PedidoVentaDTO;
import d3.inventory.application.ProductoSvc;
import d3.inventory.domain.ProductoDTO;
import d3.inventory.domain.ProductoFilterDTO;
import d3.process_designer.application.ProcesoSvc;
import d3.process_designer.domain.ProcesoDTO;
import d3.process_designer.domain.ProcesoFilterDTO;
import d3.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import d3.process_form.application.DocumentoPlantillaSvc;
import d3.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.process_form.domain.DocumentoPlantillaDTO;
import d3.process_form.domain.DocumentoPlantillaFilterDTO;
import d3.property.domain.PropiedadDTO;
import d3.tariff.application.base.TarifarioService;
import d3.tariff.domain.TarifarioDTO;
import d3.tariff.domain.TarifarioFilterDTO;

@Component
public class TipoConfiguracion {

	private final DocumentoPlantillaCaracteristicaSvc caracteristicaService;
	private final DocumentoPlantillaSvc plantillaService;
	private final PedidoVentaCaracteristicaSvc campoService;
	private final ProcesoSvc procesoService;
	private final ProductoSvc productoService;
	private final RolAccesoSvc rolService;
	private final TarifarioService tarifarioService;

	public TipoConfiguracion(@Lazy DocumentoPlantillaCaracteristicaSvc caracteristicaService,
			@Lazy DocumentoPlantillaSvc plantillaService, @Lazy PedidoVentaCaracteristicaSvc campoService,
			@Lazy ProcesoSvc procesoService, @Lazy ProductoSvc productoService, @Lazy RolAccesoSvc rolService,
			@Lazy TarifarioService tarifarioService) {
		this.caracteristicaService = caracteristicaService;
		this.plantillaService = plantillaService;
		this.campoService = campoService;
		this.procesoService = procesoService;
		this.productoService = productoService;
		this.rolService = rolService;
		this.tarifarioService = tarifarioService;
	}

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
				case CATEGORIA_PRODUCTOS:
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

	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token, boolean isUpdateAutomatic)
			throws ServerException {
		// Para las transiciones ponemos el texto
		if (pCampo.getValorOpcion() == null && pCampo.getValorText() != null) {
			List<PropiedadDTO> options = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(), Propiedades.OPCIONES);
			if (options != null) {
				for (PropiedadDTO propiedadDTO : options) {
					if (propiedadDTO.getValor().compareTo(pCampo.getValorText()) == 0
							|| (propiedadDTO.getTexto() != null
									&& propiedadDTO.getTexto().compareTo(pCampo.getValorText()) == 0)) {
						pCampo.setValorOpcion(propiedadDTO.getValor());
						pCampo.setValorText(propiedadDTO.getTexto());
						break;
					}
				}
			}
		}
		// Aqui sigo normal
		if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL) == null
				&& (pCampo.getValorOpcion() == null || pCampo.getValorOpcion().isEmpty())) {
			List<PropiedadDTO> visibleValueOK = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(),
					Propiedades.VISIBLE_VALOR_DEPENDIENTE);
			if (visibleValueOK == null || pCampo.getDependientes() == null) {
				if (isUpdateAutomatic) {
					CallDocumentCommons.addMessageError(pCampo.getPrincipal(),
							"Es necesario registrar el campo " + pCampo.getCampoDTO().getNombre() + " de la plantilla "
									+ pCampo.getCampoDTO().getPlantillaNombre());
				} else {
					throw new ServerException("Es necesario registrar el campo " + pCampo.getCampoDTO().getNombre()
							+ " de la plantilla " + pCampo.getCampoDTO().getPlantillaNombre());
				}

			} else {
				String optionsToSelect = null;
				for (PropiedadDTO propiedadDTO : visibleValueOK) {
					if (optionsToSelect != null)
						break;
					for (PedidoVentaCaracteristicaDTO iFieldDependent : pCampo.getDependientes()) {
						if (iFieldDependent.getValorText() != null
								&& propiedadDTO.getValor().compareTo(iFieldDependent.getValorText()) == 0) {
							optionsToSelect = propiedadDTO.getValor();
							break;
						}
					}
				}
				if (optionsToSelect != null) {
					if (isUpdateAutomatic) {
						CallDocumentCommons.addMessageError(pCampo.getPrincipal(),
								"Es obligatorio seleccionar un valor en el campo " + pCampo.getCampoDTO().getNombre()
										+ " cuando escoges la opcion " + optionsToSelect);
					} else {
						throw new ServerException("Es obligatorio seleccionar un valor en el campo "
								+ pCampo.getCampoDTO().getNombre() + " cuando escoges la opcion " + optionsToSelect);
					}
				}
			}
		}
		if (pCampo.getValorOpcion() != null) {
			String valorConfiguracion = Propiedades.obtenerValor(pCampo.getCampoDTO(),
					Propiedades.CONFIGURACION_ENTIDAD);
			if (valorConfiguracion.isEmpty()) {
				List<PropiedadDTO> options = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(),
						Propiedades.OPCIONES);
				PropiedadDTO option = null;
				if (options != null) {
					for (PropiedadDTO propiedadDTO : options) {
						if (propiedadDTO.getValor().compareTo(pCampo.getValorOpcion()) == 0
								|| (propiedadDTO.getTexto() != null
										&& propiedadDTO.getTexto().compareTo(pCampo.getValorOpcion()) == 0)) {
							option = propiedadDTO;
							pCampo.setValorOpcion(option.getValor());
							pCampo.setValorText(option.getTexto());
							break;
						}
					}
				}
				if (option == null)
					throw new ServerException("En el campo " + pCampo.getCampoDTO().getNombre() + " de la plantilla "
							+ pCampo.getCampoDTO().getPlantillaNombre() + " No se identifico la opcion a seleccionar "
							+ pCampo.getValorOpcion());
				// Ne bbx teniamos un campo de mas de 32 caracteres, no podia quitar el valos
				// opcion asi que lo restringui
				if (pCampo.getValorOpcion().length() > 32)
					pCampo.setValorOpcion(pCampo.getValorOpcion().substring(0, 32));
				// if (pCampo.getValorOpcion().length() != 32)
				if (pCampo.getValorText() == null)
					pCampo.setValorText(option.getValor());
			} else {
				switch (valorConfiguracion) {

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
				case CATEGORIA_PRODUCTOS:
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
				if (bd.getValorOpcion() != null && pCampo.getValorOpcion().compareTo(bd.getValorOpcion()) == 0) {
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
				if (iPropiedades.getTexto() != null)
					adaptadoT.setDescripcion(iPropiedades.getTexto());
				if (adaptadoT.getNombre().length() > 32)
					adaptadoT.setNombre(adaptadoT.getNombre().substring(0, 32));
				// adaptadoT.setNombre(iPropiedades.getValor());
				pBase.getDocumentos().add(adaptadoT);
			}
		} else {
			switch (Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.CONFIGURACION_ENTIDAD)) {
			case CATEGORIA_PRODUCTOS:
				List<DocumentoPlantillaDTO> categorias = plantillaService.getTemplateofCategoriesReplace();
				if (categorias != null && !categorias.isEmpty()) {
					pBase.setDocumentos(new ArrayList<PedidoVentaDTO>());
					for (DocumentoPlantillaDTO iPlantilla : categorias) {
						PedidoVentaDTO adaptado = new PedidoVentaDTO();
						adaptado.setLlaveTabla(iPlantilla.getLlaveTabla());
						adaptado.setImagen(iPlantilla.getImagen());
						adaptado.setNombre(iPlantilla.getCodigo());
						adaptado.setDescripcion(iPlantilla.getNombre());
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
				List<DocumentoPlantillaDTO> plantillas = plantillaService.listarPlantillaRol(plantilla, false);
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
