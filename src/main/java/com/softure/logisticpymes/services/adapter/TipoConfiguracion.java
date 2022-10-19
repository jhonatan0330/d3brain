package com.softure.logisticpymes.services.adapter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.domain.dto.BodegaDTO;
import com.softure.logisticpymes.domain.dto.CambioDTO;
import com.softure.logisticpymes.domain.dto.CategoriaProductoDTO;
import com.softure.logisticpymes.domain.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.domain.dto.DocumentoPlantillaDTO;
import com.softure.logisticpymes.domain.dto.EncuestaDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaDTO;
import com.softure.logisticpymes.domain.dto.ProcesoDTO;
import com.softure.logisticpymes.domain.dto.ProductoDTO;
import com.softure.logisticpymes.domain.dto.PropiedadDTO;
import com.softure.logisticpymes.domain.dto.RolAccesoDTO;
import com.softure.logisticpymes.domain.dto.TarifarioDTO;
import com.softure.logisticpymes.domain.filter.BodegaFilterDTO;
import com.softure.logisticpymes.domain.filter.CambioFilterDTO;
import com.softure.logisticpymes.domain.filter.CategoriaProductoFilterDTO;
import com.softure.logisticpymes.domain.filter.DocumentoPlantillaFilterDTO;
import com.softure.logisticpymes.domain.filter.EncuestaFilterDTO;
import com.softure.logisticpymes.domain.filter.PedidoVentaCaracteristicaFilterDTO;
import com.softure.logisticpymes.domain.filter.ProcesoFilterDTO;
import com.softure.logisticpymes.domain.filter.ProductoFilterDTO;
import com.softure.logisticpymes.domain.filter.RolAccesoFilterDTO;
import com.softure.logisticpymes.domain.filter.TarifarioFilterDTO;
import com.softure.logisticpymes.services.BodegaSvc;
import com.softure.logisticpymes.services.CambioSvc;
import com.softure.logisticpymes.services.CategoriaProductoSvc;
import com.softure.logisticpymes.services.DocumentoPlantillaCaracteristicaSvc;
import com.softure.logisticpymes.services.DocumentoPlantillaSvc;
import com.softure.logisticpymes.services.EncuestaSvc;
import com.softure.logisticpymes.services.PedidoVentaCaracteristicaSvc;
import com.softure.logisticpymes.services.ProcesoSvc;
import com.softure.logisticpymes.services.ProductoSvc;
import com.softure.logisticpymes.services.RolAccesoSvc;
import com.softure.logisticpymes.services.TarifarioSvc;

@Component
public class TipoConfiguracion {

	@Autowired
	private BodegaSvc bodegaService;
	@Autowired
	private CambioSvc cambioService;
	@Autowired
	private CategoriaProductoSvc categoriaProductoService;
	@Autowired
	private DocumentoPlantillaCaracteristicaSvc caracteristicaService;
	@Autowired
	private DocumentoPlantillaSvc plantillaService;
	@Autowired
	private EncuestaSvc encuestaService;
	@Autowired
	private PedidoVentaCaracteristicaSvc campoService;
	@Autowired
	private ProcesoSvc procesoService;
	@Autowired
	private ProductoSvc productoService;
	@Autowired
	private RolAccesoSvc rolService;
	@Autowired
	private TarifarioSvc tarifarioService;

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
						adaptado.setImagen(ConstantesGenerales.LOGO);
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
						adaptado.setImagen(ConstantesGenerales.LOGO);
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
						adaptado.setImagen(ConstantesGenerales.LOGO);
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
						adaptado.setImagen(ConstantesGenerales.LOGO);
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
					TarifarioDTO tarifario = tarifarioService.consultaXId(pCampo.getValorOpcion());
					if (tarifario == null) {
						throw new ServerException("No se identifica tarifario");
					} else {
						PedidoVentaDTO adaptado = new PedidoVentaDTO();
						adaptado.setLlaveTabla(tarifario.getLlaveTabla());
						adaptado.setImagen(ConstantesGenerales.LOGO);
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
			List<PropiedadDTO> visibleValueOK = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(), Propiedades.VISIBLE_VALOR_DEPENDIENTE); 
			if( visibleValueOK == null || pCampo.getDependientes()==null) {
				throw new ServerException("Es obligatorio registrar el campo " + pCampo.getCampoDTO().getNombre());				
			}else {
				String optionsToSelect = null;
				for (PropiedadDTO propiedadDTO : visibleValueOK) {
					if(optionsToSelect !=null) break;
					for (PedidoVentaCaracteristicaDTO iFieldDependent : pCampo.getDependientes()) {
						if(propiedadDTO.getValor().compareTo(iFieldDependent.getValorText())==0) {
							optionsToSelect = propiedadDTO.getValor();
							break;
						}
					}
				}
				if(optionsToSelect!=null)throw new ServerException("Es obligatorio seleccionar un valor en el campo " + pCampo.getCampoDTO().getNombre() + " cuando escoges la opcion " + optionsToSelect);
			}
		}
		if (pCampo.getValorOpcion() != null) {
			String valorConfiguracion = Propiedades.obtenerValor(pCampo.getCampoDTO(),
					Propiedades.CONFIGURACION_ENTIDAD);
			if (valorConfiguracion.isEmpty()) {
				// Ne bbx teniamos un campo de mas de 32 caracteres, no podia quitar el valos opcion asi que lo restringui
				// en futuras mejoras deberia que la propiedad tuviera un id y asi le puedo colocar este Id y usarlo
				if(pCampo.getValorOpcion().length()>32) 
					pCampo.setValorOpcion(pCampo.getValorOpcion().substring(0,32));
				if(pCampo.getValorOpcion().length()!=32) pCampo.setValorText(pCampo.getValorOpcion());
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
					TarifarioDTO tarifario = tarifarioService.consultaXId(pCampo.getValorOpcion());
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
				if (pCampo.getValorOpcion().compareTo(bd.getValorOpcion()) == 0) {
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
				if(adaptadoT.getLlaveTabla().length()>32) adaptadoT.setLlaveTabla(adaptadoT.getLlaveTabla().substring(0,32));
				adaptadoT.setNombre(iPropiedades.getValor());
				pBase.getDocumentos().add(adaptadoT);
			}
		} else {
			switch (Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.CONFIGURACION_ENTIDAD)) {
			case CATEGORIA_PRODUCTOS:
				CategoriaProductoFilterDTO categoria = new CategoriaProductoFilterDTO();
				categoria.setFiltroParametro(pCampo.getFiltroParametro());
				categoria.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
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
				proceso.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
				List<ProcesoDTO> procesos = procesoService.listarConsulta(proceso);
				if (procesos != null && !procesos.isEmpty()) {
					pBase.setDocumentos(new ArrayList<PedidoVentaDTO>());
					for (ProcesoDTO iProducto : procesos) {
						PedidoVentaDTO adaptado = new PedidoVentaDTO();
						adaptado.setLlaveTabla(iProducto.getLlaveTabla());
						adaptado.setImagen(ConstantesGenerales.LOGO);
						adaptado.setNombre(iProducto.getCodigo());
						adaptado.setDescripcion(iProducto.getNombre());
						pBase.getDocumentos().add(adaptado);
					}
				}
				break;
			case PRODUCTOS:
				ProductoFilterDTO producto = new ProductoFilterDTO();
				producto.setFiltroParametro(pCampo.getFiltroParametro());
				producto.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
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
				rolFiltro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
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
				cambio.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
				List<CambioDTO> cambios = cambioService.listarConsulta(cambio);
				if (cambios != null && !cambios.isEmpty()) {
					pBase.setDocumentos(new ArrayList<PedidoVentaDTO>());
					for (CambioDTO iCambio : cambios) {
						PedidoVentaDTO adaptado = new PedidoVentaDTO();
						adaptado.setLlaveTabla(iCambio.getLlaveTabla());
						adaptado.setImagen(ConstantesGenerales.LOGO);
						adaptado.setNombre(iCambio.getNombre());
						// adaptado.setDescripcion(iCategoria.getNombre());
						pBase.getDocumentos().add(adaptado);
					}
				}
				break;
			case ENCUESTAS:
				EncuestaFilterDTO encuesta = new EncuestaFilterDTO();
				encuesta.setFiltroParametro(pCampo.getFiltroParametro());
				encuesta.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
				List<EncuestaDTO> encuestas = encuestaService.listarConsulta(encuesta);
				if (encuestas != null && !encuestas.isEmpty()) {
					pBase.setDocumentos(new ArrayList<PedidoVentaDTO>());
					for (EncuestaDTO iEncuesta : encuestas) {
						PedidoVentaDTO adaptado = new PedidoVentaDTO();
						adaptado.setLlaveTabla(iEncuesta.getLlaveTabla());
						adaptado.setImagen(ConstantesGenerales.LOGO);
						adaptado.setNombre(iEncuesta.getNombre());
						// adaptado.setDescripcion(iCategoria.getNombre());
						pBase.getDocumentos().add(adaptado);
					}
				}
				break;
			case BODEGAS:
				BodegaFilterDTO bodega = new BodegaFilterDTO();
				bodega.setFiltroParametro(pCampo.getFiltroParametro());
				bodega.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
				List<BodegaDTO> bodegas = bodegaService.listarConsulta(bodega);
				if (bodegas != null && !bodegas.isEmpty()) {
					pBase.setDocumentos(new ArrayList<PedidoVentaDTO>());
					for (BodegaDTO iBodega : bodegas) {
						PedidoVentaDTO adaptado = new PedidoVentaDTO();
						adaptado.setLlaveTabla(iBodega.getLlaveTabla());
						adaptado.setImagen(ConstantesGenerales.LOGO);
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
				tarifario.setFiltroParametro(pCampo.getFiltroParametro());
				tarifario.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
				List<TarifarioDTO> tarifarios = tarifarioService.listarConsulta(tarifario);
				if (tarifarios != null && !tarifarios.isEmpty()) {
					pBase.setDocumentos(new ArrayList<PedidoVentaDTO>());
					for (TarifarioDTO itarifario : tarifarios) {
						PedidoVentaDTO adaptadoT = new PedidoVentaDTO();
						adaptadoT.setLlaveTabla(itarifario.getLlaveTabla());
						adaptadoT.setImagen(ConstantesGenerales.LOGO);
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
