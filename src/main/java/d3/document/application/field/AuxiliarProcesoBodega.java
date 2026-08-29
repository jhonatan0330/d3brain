package d3.document.application.field;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.configuration.application.RelacionInternaSvc;
import d3.configuration.domain.PropiedadDTO;
import d3.configuration.domain.RelacionInternaDTO;
import d3.document.application.CallDocumentListWithFilters;
import d3.document.application.PedidoVentaSvc;
import d3.document.domain.DetallePedidoVentaDTO;
import d3.document.domain.PedidoVentaCaracteristicaDTO;
import d3.document.domain.PedidoVentaDTO;
import d3.inventory.application.DeduccionProductoSvc;
import d3.inventory.application.ProductoInventarioDescuentoSvc;
import d3.inventory.application.ProductoInventarioSvc;
import d3.inventory.application.ProductoSvc;
import d3.inventory.domain.DeduccionProductoDTO;
import d3.inventory.domain.ProductoDTO;
import d3.inventory.domain.ProductoInventarioDTO;
import d3.inventory.domain.ProductoInventarioDescuentoDTO;
import d3.inventory.domain.ProductoInventarioDescuentoFilterDTO;
import d3.inventory.domain.ProductoInventarioFilterDTO;
import d3.process.application.DocumentoPlantillaCaracteristicaSvc;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;

import org.springframework.context.annotation.Lazy;

@Component
public class AuxiliarProcesoBodega {

	private final DeduccionProductoSvc deduccionProductoService;
	private final DocumentoPlantillaCaracteristicaSvc caracteristicaService;
	private final PedidoVentaSvc pedidoService;
	private final CallDocumentListWithFilters listDocumentWithFiltersFunction;
	private final ProductoSvc productoService;
	private final ProductoInventarioSvc productoInventarioService;
	private final ProductoInventarioDescuentoSvc productoInventarioDescuentoService;
	private final RelacionInternaSvc relacionService;

	public AuxiliarProcesoBodega(@Lazy DeduccionProductoSvc deduccionProductoService,
			@Lazy DocumentoPlantillaCaracteristicaSvc caracteristicaService, @Lazy PedidoVentaSvc pedidoService,
			@Lazy CallDocumentListWithFilters listDocumentWithFiltersFunction, @Lazy ProductoSvc productoService,
			@Lazy ProductoInventarioSvc productoInventarioService,
			@Lazy ProductoInventarioDescuentoSvc productoInventarioDescuentoService,
			@Lazy RelacionInternaSvc relacionService) {
		this.deduccionProductoService = deduccionProductoService;
		this.caracteristicaService = caracteristicaService;
		this.pedidoService = pedidoService;
		this.listDocumentWithFiltersFunction = listDocumentWithFiltersFunction;
		this.productoService = productoService;
		this.productoInventarioService = productoInventarioService;
		this.productoInventarioDescuentoService = productoInventarioDescuentoService;
		this.relacionService = relacionService;
	}

	public void aplicarMovimientosBodega(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		// OJO: Primero tiene que estar el campo de bodega y despues del de prouctos
		gestionarInventario(validarInventario(pCampo, token), token);
	}

	public List<DeduccionProductoDTO> validarInventario(PedidoVentaCaracteristicaDTO pCampo, String token)
			throws ServerException {
		if (pCampo.getValorOpcion() == null)
			throw new ServerException("Por favor revise la configuracion de inventarios");
		List<DeduccionProductoDTO> result = null;

		List<PropiedadDTO> movimientos = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(),
				Propiedades.BODEGA_MOVIMIENTO);
		if (movimientos == null)
			throw new ServerException("Revise la configuracion de la bodega ya que no tiene movimientos. Campo: "
					+ pCampo.getCampoDTO().getNombre());
		;
		for (PropiedadDTO iParam : movimientos) {
			if (iParam.getTexto() == null)
				throw new ServerException(
						"El texto de la propiedad movimiento de bodega debe tener el codigo del campo que vamos a gestionar. Campo: "
								+ pCampo.getCampoDTO().getNombre());
			PedidoVentaCaracteristicaDTO dependiente = null;
			if (pCampo.getDependientes() != null) {
				for (PedidoVentaCaracteristicaDTO iDep : pCampo.getDependientes()) {
					if (iDep.getCampoDTO().getCodigo().compareTo(iParam.getTexto()) == 0) {
						dependiente = iDep;
						break;
					}
				}
			}

			if (dependiente == null)
				throw new ServerException("El campo bodega no logra obtener el campo dependiente." + iParam.getTexto());

			List<DeduccionProductoDTO> acumulado = inventarioDirecto(dependiente, iParam.getValor(),
					pCampo.getValorOpcion(), pCampo.getDocumento(),
					relacionService.relacionesPropiedad(iParam.getLlaveTabla()), token);

			if (acumulado != null && !acumulado.isEmpty()) {
				if (result == null) {
					result = new ArrayList<DeduccionProductoDTO>();
					result.addAll(acumulado);
				} else {
					for (DeduccionProductoDTO iDeduccion : acumulado) {
						result = adicionarDeduccion(result, iDeduccion);
					}
				}
			}

		}
		return result;
	}

	public List<DeduccionProductoDTO> inventarioDirecto(PedidoVentaCaracteristicaDTO pCampo, String operacion,
			String pStoreId, String documentoInicial, List<RelacionInternaDTO> relaciones, String token)
			throws ServerException {
		if (pCampo.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.PRODUCTO) == 0) {
			return inventariarDetalle(pCampo, operacion, pStoreId, documentoInicial, token);
		} else {
			if (pCampo.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.PROCESO) == 0) {
				if (relaciones == null || relaciones.isEmpty())
					throw new ServerException(
							"Coloca el camino de profundidad de consulta de inventario en las relaciones de la propiedad"
									+ "\nPlantilla: " + pCampo.getCampoDTO().getPlantillaNombre() + "\nCampo: "
									+ pCampo.getCampoDTO().getNombre());
				return inventariarProceso(pCampo, operacion, pStoreId, documentoInicial, relaciones, token);
			} else {
				if (pCampo.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.NUMERO) == 0) {
					List<PropiedadDTO> codigoDepende = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(),
							Propiedades.DEPENDE);
					if (codigoDepende != null) {
						if (pCampo.getDependientes() == null || pCampo.getDependientes().isEmpty())
							throw new ServerException(
									"Por favor revise la configuracion del dependiente de tipo numero "
											+ pCampo.getCampoDTO().getNombre());
						List<DeduccionProductoDTO> acumulado = inventarioDirecto(pCampo.getDependientes().get(0),
								operacion, pStoreId, documentoInicial, relaciones, token);
						if (acumulado != null && !acumulado.isEmpty()) {
							for (DeduccionProductoDTO iDeduccion : acumulado) {
								iDeduccion.setCantidad(iDeduccion.getCantidad().multiply(pCampo.getValorNumero()));
							}
						}
						return acumulado;
					}
				}
			}
		}
		return null;
	}

	private void gestionarInventario(List<DeduccionProductoDTO> deduccionesFinales, String securityToken)
			throws ServerException {
		if (deduccionesFinales != null && !deduccionesFinales.isEmpty()) {
			for (DeduccionProductoDTO deduccion : deduccionesFinales) {
				if (deduccion.getCantidad().compareTo(BigDecimal.ZERO) != 0) {
					deduccion = deduccionProductoService.guardar(deduccion, securityToken);
				}
			}
		}
	}

	public List<DeduccionProductoDTO> inventariarProceso(PedidoVentaCaracteristicaDTO pCampo, String operacion,
			String pStoreId, String documentoInicial, List<RelacionInternaDTO> relaciones, String token)
			throws ServerException {
		if (pCampo.getExpedientes() != null && !pCampo.getExpedientes().isEmpty()) {
			List<DeduccionProductoDTO> result = new ArrayList<DeduccionProductoDTO>();
			List<DeduccionProductoDTO> acumulado = null;

			for (PedidoVentaDTO expediente : pCampo.getExpedientes()) {
				// El tipo proceso cuando gestiona me lo envia vacio
				expediente = pedidoService.obtenerCamposCompletos(expediente, token);
				for (PedidoVentaCaracteristicaDTO campoExpediente : expediente.getCaracteristicas()) {
					for (RelacionInternaDTO rit : relaciones) {
						if (campoExpediente.getCampo().compareTo(rit.getCampo()) == 0) {
							System.out.format(
									"\n[%s (%s) - %s] Inventario anidado de documento operacion(%s) iniciando en campo interno ( %s )",
									pCampo.getCampoDTO().getPlantillaNombre(), expediente.getNombre(),
									pCampo.getCampoDTO().getNombre(), operacion,
									campoExpediente.getCampoDTO().getNombre());
							// La siguiente linea es redundante ya que lo consulte el campo completo
							// //campo.setCampoDTO(caracteristicaService.consultaXId(campo.getCampo()));
							if (campoExpediente.getCampoDTO().getFormato()
									.compareTo(DocumentoPlantillaCaracteristicaDTO.PROCESO) == 0
									&& campoExpediente.getLlaveTabla() != null) {
								campoExpediente.setCampoDTO(
										caracteristicaService.cargarComplementos(campoExpediente.getCampoDTO(), token));
								if (Propiedades.obtenerParametro(campoExpediente.getCampoDTO(),
										Propiedades.MULTIPLE) == null) {
									campoExpediente.setExpedientes(new ArrayList<PedidoVentaDTO>());
									campoExpediente.getExpedientes()
											.add(pedidoService.consultaXId(campoExpediente.getValorOpcion()));
								} else {
									campoExpediente.setExpedientes(
											listDocumentWithFiltersFunction.listarExpedientesPertenecenCampo(
													campoExpediente.getLlaveTabla(), token, null));
								}
							}
							acumulado = inventarioDirecto(campoExpediente, operacion, pStoreId, documentoInicial,
									relaciones, token);
							if (acumulado != null) {
								for (DeduccionProductoDTO iAcumulado : acumulado) {
									adicionarDeduccion(result, iAcumulado);
								}
							}
						}
					}

				}
			}
			return result;
		}
		return null;
	}

	public List<DeduccionProductoDTO> inventariarDetalle(PedidoVentaCaracteristicaDTO pCampo, String operacion,
			String pStoreId, String documentoInicial, String token) throws ServerException {
		if (operacion == null)
			throw new ServerException("La operacion de inventarios no puede ser vacia");
		BigDecimal factor = null;

		if (operacion.contains("E"))
			factor = BigDecimal.ONE;
		if (operacion.contains("S"))
			factor = BigDecimal.ONE.negate();
		if (operacion.compareTo("T") == 0)
			factor = BigDecimal.ONE;

		if (factor == null)
			throw new ServerException("Revise la operacion de la bodega, no se identifica el factor");

		List<DeduccionProductoDTO> result = null;
		System.out.format("\n[%s - %s] Gestionando inventario operacion", pCampo.getCampoDTO().getPlantillaNombre(),
				pCampo.getCampoDTO().getNombre());

		if (pCampo.getDetalles() != null && !pCampo.getDetalles().isEmpty()) {
			for (DetallePedidoVentaDTO detalle : pCampo.getDetalles()) {
				// Si principal viene nulo viene de tipo proceso
				if (pCampo.getPrincipal() == null || detalle.getLlaveTabla() == null || detalle.getEstado() == null
						|| detalle.getEstado().compareTo(SharedConstants.STATE_INACTIVE) == 0) {
					boolean deducirComposicion = false;// En transformacion se hacen las 2 operaciones
					System.out.format("\n[%s - %s] Revisando producto %s", pCampo.getCampoDTO().getPlantillaNombre(),
							pCampo.getCampoDTO().getNombre(), detalle.getNombre());
					if (!operacion.contains("C")) {
						ProductoInventarioFilterDTO productoFilter = new ProductoInventarioFilterDTO();
						// En box tenian varios inactivos y esto generaba errores
						// productoFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
						productoFilter.setProducto(detalle.getProducto());
						productoFilter.setBodega(pStoreId);
						ProductoInventarioDTO producto = productoInventarioService.consultaUnica(productoFilter);
						// En algunos casos es obligatorio crear la relacion de bodega de inventario del
						// producto
						if (producto != null && producto.getEstado().compareTo(SharedConstants.STATE_ACTIVE) == 0) {
							DeduccionProductoDTO salida = new DeduccionProductoDTO();
							salida.setBodega(pStoreId);
							salida.setCantidad(detalle.getCantidadTotal().multiply(factor));
							if (detalle.getEstado() != null
									&& detalle.getEstado().compareTo(SharedConstants.STATE_INACTIVE) == 0)
								salida.setCantidad(salida.getCantidad().negate());
							salida.setProducto(detalle.getProducto());
							salida.setDocumento(documentoInicial);
							result = adicionarDeduccion(result, salida);
							deducirComposicion = false;
							if (operacion.compareTo("T") == 0)
								deducirComposicion = true;
						} else {
							if (operacion.compareTo("T") == 0)
								throw new ServerException(
										"Para una transformacion es necesario que el producto maneje inventarios."
												+ detalle.getNombre());
							if (!operacion.contains("D"))
								deducirComposicion = true;
						}
					} else {
						deducirComposicion = true;
					}

					if (deducirComposicion) {
						ProductoInventarioDescuentoFilterDTO filtro = new ProductoInventarioDescuentoFilterDTO();
						filtro.setProducto(detalle.getProducto());
						filtro.setEstado(SharedConstants.STATE_ACTIVE);
						List<ProductoInventarioDescuentoDTO> descuentos = productoInventarioDescuentoService
								.listarConsulta(filtro);
						if (descuentos == null || descuentos.size() == 0) {
							ProductoDTO productoDTO = productoService.consultaXId(detalle.getProducto());
							if (productoDTO.getProductoBase() != null) {
								filtro.setProducto(productoDTO.getProductoBase());
								descuentos = productoInventarioDescuentoService.listarConsulta(filtro);
								if (descuentos == null || descuentos.isEmpty()) {// En caso que no tenda composicion
																					// miramos que sea el mismo
									ProductoInventarioFilterDTO inventarioPFilter = new ProductoInventarioFilterDTO();
									inventarioPFilter.setEstado(SharedConstants.STATE_ACTIVE);
									inventarioPFilter.setProducto(productoDTO.getProductoBase());
									inventarioPFilter.setBodega(pStoreId);
									ProductoInventarioDTO inventarioP = productoInventarioService
											.consultaUnica(inventarioPFilter);
									if (inventarioP != null) {
										ProductoInventarioDescuentoDTO salidaMismoProducto = new ProductoInventarioDescuentoDTO();
										salidaMismoProducto.setCantidadProductoDescontar(BigDecimal.ONE);
										salidaMismoProducto.setProducto(productoDTO.getLlaveTabla());
										salidaMismoProducto.setProductoDescontar(inventarioP.getProducto());
										descuentos = new ArrayList<ProductoInventarioDescuentoDTO>();
										descuentos.add(salidaMismoProducto);
									}
								}
							}
						}
						if (descuentos != null && descuentos.size() != 0) {
							if (operacion.compareTo("T") == 0)
								factor = factor.negate();
							for (ProductoInventarioDescuentoDTO descuento : descuentos) {
								DeduccionProductoDTO salida = new DeduccionProductoDTO();
								salida.setProducto(descuento.getProductoDescontar());
								if (descuento.getCaracteristica() != null) {
									salida.setProducto(null);
									if (detalle.getDocumentoDetalle() != null
											&& detalle.getDocumentoDetalle().getCaracteristicas() != null
											&& !detalle.getDocumentoDetalle().getCaracteristicas().isEmpty()) {
										for (PedidoVentaCaracteristicaDTO iterador : detalle.getDocumentoDetalle()
												.getCaracteristicas()) {
											if (iterador.getValorOpcion() != null) {
												if (iterador.getValorOpcion()
														.compareTo(descuento.getCaracteristica()) == 0) {
													salida.setProducto(descuento.getProductoDescontar());
													break;
												}
											}
										}
									}
								}
								if (salida.getProducto() != null) {
									salida.setCantidad(descuento.getCantidadProductoDescontar()
											.multiply(detalle.getCantidadTotal().multiply(factor)));
									if (detalle.getEstado() != null
											&& detalle.getEstado().compareTo(SharedConstants.STATE_INACTIVE) == 0)
										salida.setCantidad(salida.getCantidad().negate());
									salida.setBodega(pStoreId);
									salida.setDocumento(documentoInicial);
									result = adicionarDeduccion(result, salida);
								}
							}
						} else {
							if (operacion.compareTo("T") == 0) {
								ProductoDTO productoDTO = productoService.consultaXId(detalle.getProducto());
								if (productoDTO.getProductoBase() != null)
									productoDTO = productoService.consultaXId(productoDTO.getProductoBase());
								throw new ServerException(
										"Para una transformacion es necesario que el producto maneje inventarios de composicion."
												+ productoDTO.getNombre());
							}
						}
					}

				}
			}
		}
		return result;
	}

	public List<DeduccionProductoDTO> adicionarDeduccion(List<DeduccionProductoDTO> deduccionesFinales,
			DeduccionProductoDTO salida) {
		if (deduccionesFinales == null)
			deduccionesFinales = new ArrayList<DeduccionProductoDTO>();
		if (!deduccionesFinales.isEmpty()) {
			for (DeduccionProductoDTO deduccion : deduccionesFinales) {
				if (deduccion.getProducto().compareTo(salida.getProducto()) == 0) {
					deduccion.setCantidad(deduccion.getCantidad().add(salida.getCantidad()));
					return deduccionesFinales;
				}
			}
		}
		deduccionesFinales.add(salida);
		return deduccionesFinales;
	}
}
