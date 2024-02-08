package com.softure.document_execution.application.field;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.document_execution.application.CallDocumentCRUD;
import com.softure.document_execution.application.DetallePedidoVentaSvc;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.domain.DetallePedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.inventory.application.CategoriaProductoSvc;
import com.softure.inventory.application.ProductoSvc;
import com.softure.inventory.domain.CategoriaProductoDTO;
import com.softure.inventory.domain.ProductoDTO;
import com.softure.inventory.domain.ProductoFilterDTO;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

@Component
public class TipoDetallePedido {

	@Autowired
	private DocumentoPlantillaCaracteristicaSvc caracteristicaService;
	@Autowired
	private CategoriaProductoSvc categoriaProductoService;
	@Autowired
	private DetallePedidoVentaSvc detallePedidoVentaService;
	@Autowired
	private ProductoSvc productoService;
	@Autowired
	private PedidoVentaSvc pedidoService;
	@Autowired
	private CallDocumentCRUD saveUpdateInactivateDocumentFunction;
	@Autowired
	private PedidoVentaCaracteristicaSvc campoService;
	@Autowired
	private PropiedadSvc configuracionSvc;
	@Autowired
	private CallProductValidateAndSave validateAndSave;

	public void cargarConsultaCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		if (pCampo.getDocumento() != null) {
			List<PropiedadDTO> tarifario = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(),
					Propiedades.DETALLE_TARIFARIO);
			if (tarifario != null && tarifario.isEmpty())
				tarifario = null;

			if (!pCampo.getModificado())
				pCampo.setDetalles(
						detallePedidoVentaService.listarCompleto(pCampo.getDocumento(), tarifario, null, null, token));
			if (pCampo.getDetalles() != null && !pCampo.getDetalles().isEmpty()) {
				pCampo.setValorNumero(BigDecimal.ZERO);
				for (DetallePedidoVentaDTO detalle : pCampo.getDetalles()) {
					pCampo.setValorNumero(pCampo.getValorNumero().add(detalle.getValorTotal()));
				}
			}
			String unico = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.UNICO_PRODUCTO);
			if (!unico.isEmpty() && pCampo.getDetalles() != null && pCampo.getDetalles().size() != 0) {
				pCampo.setValorText(pCampo.getDetalles().get(0).getProductoCodigo() + " - "
						+ pCampo.getDetalles().get(0).getNombre());
			}
		}
	}

	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {

		if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL) == null
				&& (pCampo.getDetalles() == null || pCampo.getDetalles().size() == 0))
			throw new ServerException("Es obligatorio colocar el campo " + pCampo.getCampoDTO().getNombre());

		List<DetallePedidoVentaDTO> agrupados = new ArrayList<DetallePedidoVentaDTO>();
		if (pCampo.getDetalles() != null && !pCampo.getDetalles().isEmpty()) {
			String formula = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.DETALLE_FORMULA);
			if (!formula.isEmpty()) {
				if (pCampo.getDependientes() == null)
					throw new ServerException("Revise los dependientes del tipo detalle");
				if (pCampo.getDependientes().size() != 1)
					throw new ServerException("Los tipo bodega permiten solo 1 dependientes para sumar o restar");
				novedadParcial(pCampo, token);
			}
			agrupados = validateAndSave.orderToValidate(pCampo.getDetalles());
			pCampo.setDetalles(agrupados);
			String unico = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.UNICO_PRODUCTO);
			if (!unico.isEmpty()) {
				if (pCampo.getDetalles() != null && pCampo.getDetalles().size() != 1)
					throw new ServerException(
							"Cuando un producto tiene la propiedad unica solo puede venir un producto a guardar");
				pCampo.getDetalles().get(0).setCantidadTotal(BigDecimal.ONE);
				pCampo.getDetalles().get(0).setCantidad(BigDecimal.ONE);
				pCampo.getDetalles().get(0).setCantidadPromocion(0);
				pCampo.getDetalles().get(0).setCantidadPromocionBase(0);
				pCampo.setValorText(pCampo.getDetalles().get(0).getProductoCodigo() + " - "
						+ pCampo.getDetalles().get(0).getNombre());
			} else {
				String texto = "";
				for (DetallePedidoVentaDTO detalle : pCampo.getDetalles()) {
					texto = texto + "(" + detalle.getCantidad() + ") " + detalle.getNombre() + " ";
				}
				pCampo.setValorText(texto);
			}
		}

		if (pCampo.getDocumento() != null) {
			List<PropiedadDTO> tarifario = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(),
					Propiedades.DETALLE_TARIFARIO);
			if (tarifario != null && tarifario.isEmpty())
				tarifario = null;
			pCampo.setDetalles(
					validateAndSave.validateWithExistProducts(agrupados, pCampo.getDocumento(), tarifario, token));
		} else {
			pCampo.setDetalles(agrupados);
		}

		// Organizo los valores y las promociones
		if (pCampo.getDetalles() != null && !pCampo.getDetalles().isEmpty()) {
			pCampo.setValorNumero(BigDecimal.ZERO);
			for (DetallePedidoVentaDTO detalle : pCampo.getDetalles()) {
				if (detalle.getEstado() == null || detalle.getEstado().compareTo(SharedConstants.STATE_ACTIVE) == 0) {
					// Valido que el producto se pueda guardar en el tiempo

					ProductoDTO pd = productoService.consultaXId(detalle.getProducto());
					if (pd == null)
						throw new ServerException("Revise todo el producto no existe" + detalle.getProducto());
					if (pd.getEstado().compareTo(SharedConstants.STATE_INACTIVE) == 0)
						throw new ServerException("El producto esta inactivo" + pd.getNombre());

					// Calculo la
					// cantidad--//detalle.getCantidad();--//detalle.getCantidadPromocionBase();//detalle.setCantidadPromocion(0);

					if (detalle.getCantidadPromocion() != null && detalle.getCantidadPromocion() > 0) {
						if (detalle.getCantidadPromocionBase().compareTo(0) == 0)
							throw new ServerException("El valor base de promocion no puede ser cero");
						long cantPromo = 0;
						cantPromo = Math.round(
								Math.floor(detalle.getCantidad().intValue() / detalle.getCantidadPromocionBase()));
						cantPromo = cantPromo * detalle.getCantidadPromocion();
						// detalle.setCantidadPromocion((int)cantPromo);
						detalle.setCantidadTotal(detalle.getCantidad().add(new BigDecimal(cantPromo)));
					} else {
						detalle.setCantidadTotal(detalle.getCantidad());
					}
					/*
					 * if(detalle.getValorUnitario().compareTo(detalle.getValorMinimo())<0) throw
					 * new ServerException("El valor unitario es menor al minimo.");
					 * if(detalle.getValorUnitario().compareTo(detalle.getValorMaximo())>0) throw
					 * new ServerException("El valor unitario es mayor al maximo.");
					 */
					// Coloco el valor subtotal
					if (detalle.getValorUnitario() == null)
						detalle.setValorUnitario(BigDecimal.ZERO);
					PropiedadDTO campoTotal = Propiedades.obtenerParametro(detalle, Propiedades.PRODUCTO_CAMPO_TOTAL);
					if (campoTotal == null) {
						detalle.setValorSubtotal(detalle.getValorUnitario().multiply(detalle.getCantidad()));
					} else {
						for (PedidoVentaCaracteristicaDTO iFieldCantidad : detalle.getCaracteristicas()) {
							if (iFieldCantidad.getCampo().compareTo(campoTotal.getValor()) == 0) {
								detalle.setValorSubtotal(iFieldCantidad.getValorNumero());
								break;
							}
						}

					}
					detalle.setValorTotal(detalle.getValorSubtotal().setScale(0, RoundingMode.CEILING));
					pCampo.setValorNumero(pCampo.getValorNumero().add(detalle.getValorTotal()));
				}
			}
		}
	}

	public PedidoVentaCaracteristicaDTO guardarCampo(PedidoVentaCaracteristicaDTO pCampo, String token)
			throws ServerException {

		if (pCampo.getDetalles() != null && !pCampo.getDetalles().isEmpty()) {

			pCampo.setDetalles(validateAndSave.save(pCampo.getDetalles(), token, pCampo.getDocumento(),
					pCampo.getCampoDTO().getPlantilla(), pCampo.getTransaccionRegistro(), pCampo.getLlaveTabla()));
			PedidoVentaCaracteristicaDTO bd = campoService.buscarActivo(pCampo, pCampo.getPrincipal().getHistorico());
			if (bd != null) {
				if (pCampo.getValorText() == null) {
					return pCampo;
				} else {
					if (pCampo.getValorText().compareTo(bd.getValorText()) == 0
							&& ((bd.getValorNumero() == null && pCampo.getValorNumero().compareTo(BigDecimal.ZERO) == 0)
									|| (bd.getValorNumero() != null
											&& pCampo.getValorNumero().compareTo(bd.getValorNumero()) == 0))) {
						return bd;
					} else {
						bd.setValorText(pCampo.getValorText());
						bd.setValorNumero(pCampo.getValorNumero());
						return campoService.actualizar(bd, token);
					}
				}
			}
			if (pCampo.getValorText() == null) {
				return pCampo;
			} else {
				bd = campoService.guardar(pCampo, token);
				pCampo.setLlaveTabla(bd.getLlaveTabla());
			}
		}

		return pCampo;
	}

	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(PedidoVentaCaracteristicaFilterDTO pCampo)
			throws ServerException {
		System.out.println("Consulta DB Detalle Start:" + new Date());
		if (pCampo == null || pCampo.getCampo() == null)
			throw new ServerException("Revise la parametro del metodo");
		if (pCampo.getFiltroParametro() != null && pCampo.getFiltroParametro().length() == 0)
			pCampo.setFiltroParametro(null);
		DocumentoPlantillaCaracteristicaDTO pBase = caracteristicaService
				.consultaUnicaConComplementos(pCampo.getCampo(), pCampo.getSecurityToken());
		if (pBase == null)
			throw new ServerException("Error en el identificador de la caracteristica");
		String tercero = null;
		PropiedadDTO pTercero = Propiedades.obtenerParametro(pBase, Propiedades.PRODUCTOS_TERCERO);
		if (pTercero != null) {
			if (pCampo.getDependientes() == null || pCampo.getDependientes().isEmpty())
				throw new ServerException("Revise los dependientes");
			for (PedidoVentaCaracteristicaDTO iDepende : pCampo.getDependientes()) {
				if (iDepende.getCampo().compareTo(pTercero.getValor()) == 0) {
					tercero = iDepende.getValorOpcion();
					break;
				}
			}
			if (tercero == null)
				throw new ServerException("Debe seleccionar un " + pTercero.getTexto());
		}
		PropiedadDTO funcionProductos = Propiedades.obtenerParametro(pBase, Propiedades.PRODUCTOS_FUNCION_SQL);
		if (funcionProductos == null) {
			if (tercero != null) {
				ProductoFilterDTO entityFilter = new ProductoFilterDTO();
				// entityFilter.setPlantilla(pBase.getPlantilla());
				entityFilter.setEstado(SharedConstants.STATE_ACTIVE);
				entityFilter.setFiltroParametro(pCampo.getFiltroParametro());
				entityFilter.setUsuarioRol(tercero);
				pBase.setProductos(productoService.listarProductoPlantillaResponsable(entityFilter));
			} else {
				pBase.setProductos(
						productoService.listarProductoCampo(pBase.getLlaveTabla(), pCampo.getFiltroParametro()));
			}
		} else {
			PropiedadDTO pCampoFuncion = Propiedades.obtenerParametro(pCampo.getCampoDTO(),
					Propiedades.PRODUCTOS_FUNCION_CAMPO);
			String valorCampo = pCampo.getDocumento();
			if (pCampoFuncion != null) {
				if (pCampo.getDependientes() == null || pCampo.getDependientes().isEmpty())
					throw new ServerException("Revise los dependientes");
				for (PedidoVentaCaracteristicaDTO iDepende : pCampo.getDependientes()) {
					if (iDepende.getCampo().compareTo(pCampoFuncion.getValor()) == 0) {
						valorCampo = iDepende.getValorOpcion();
						break;
					}
				}
			}
			if (valorCampo == null)
				throw new ServerException("Revise la configuracion debe tener un campo relacionado para la funcion"
						+ pCampoFuncion.getTexto());
			pBase.setProductos(productoService.listarProductoFuncion(funcionProductos.getLlaveTabla(), valorCampo,
					pCampo.getFiltroParametro(), pCampo.getSecurityToken()));
		}
		if (pBase.getProductos() != null && !pBase.getProductos().isEmpty()) {

			PropiedadDTO tarifarioFuncion = Propiedades.obtenerParametro(pBase, Propiedades.DETALLE_TARIFARIO_SQL);
			if (tarifarioFuncion != null) {
				campoService.validarDependientes(pCampo.getCampoDTO(), pCampo.getDependientes());
				pCampo.setDependientes(campoService.ordenarAlfabeticaDepende(pCampo.getDependientes()));
			}
			pBase.setProductos(detallarProductos2Plantilla(pBase.getProductos(), pCampo.getCampoDTO(), null, tercero,
					(tarifarioFuncion != null) ? tarifarioFuncion.getLlaveTabla() : null, pCampo.getDependientes(),
					pCampo.getSecurityToken()));
			List<CategoriaProductoDTO> categorias = new ArrayList<CategoriaProductoDTO>();
			for (ProductoDTO productoDTO : pBase.getProductos()) {
				boolean existeCategoria = false;
				for (CategoriaProductoDTO catPlantilla : categorias) {
					if (catPlantilla.getLlaveTabla().compareTo(productoDTO.getCategoria()) == 0) {
						existeCategoria = true;
						productoDTO.getDetallePlantilla().getPropiedades().addAll(catPlantilla.getPropiedades());
						break;
					}
				}
				if (!existeCategoria) {
					CategoriaProductoDTO categoria = categoriaProductoService.consultaXId(productoDTO.getCategoria());
					categoria.setPropiedades(configuracionSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.PLANTILLA,
							categoria.getLlaveTabla(), null, configuracionSvc.getUserFlex(pCampo.getSecurityToken())));
					productoDTO.getDetallePlantilla().getPropiedades().addAll(categoria.getPropiedades());
					categorias.add(categoria);
				}
			}
			pBase.setCategorias(ordenar(categorias).getHijos());
		}
		pCampo.setCampoDTO(pBase);
		System.out.println("Consulta DB Detalle FIN :" + new Date());
		return pCampo;
	}

	private CategoriaProductoDTO ordenar(List<CategoriaProductoDTO> categorias) throws ServerException {
		if (categorias == null)
			categorias = new ArrayList<CategoriaProductoDTO>();
		CategoriaProductoDTO nodoPrincipal = new CategoriaProductoDTO();
		nodoPrincipal.setLlaveTabla("NODO1476");
		categorias.add(0, nodoPrincipal);
		while (categorias.size() > 1) {
			CategoriaProductoDTO ultimo = categorias.get(categorias.size() - 1);
			if (ultimo.getNodoSuperior() == null)
				ultimo.setNodoSuperior("NODO1476");
			CategoriaProductoDTO padre = null;
			for (int i = categorias.size() - 2; i >= 0; i--) {
				padre = esPadre(categorias.get(i), ultimo.getNodoSuperior());
				if (padre != null)
					break;
			}
			if (padre == null) {
				CategoriaProductoDTO categoria = categoriaProductoService.consultaXId(ultimo.getNodoSuperior());
				if (categoria == null)
					throw new ServerException("No se encuentra la categoria principal. " + ultimo.getNodoSuperior());
				categorias.add(categoria);
			} else {
				if (padre.getHijos() == null)
					padre.setHijos(new ArrayList<CategoriaProductoDTO>());
				int j = 0;// Ordenar alfabeticamenta
				while (j < padre.getHijos().size()
						&& padre.getHijos().get(j).getNombre().compareTo(ultimo.getNombre()) < 0) {
					j++;
				}
				padre.getHijos().add(j, ultimo);
				categorias.remove(ultimo);
			}
		}
		return nodoPrincipal;
	}

	private CategoriaProductoDTO esPadre(CategoriaProductoDTO categoria, String llavePadre) {
		if (categoria.getLlaveTabla().compareTo(llavePadre) == 0) {
			return categoria;
		} else {
			if (categoria.getHijos() == null)
				return null;
			for (CategoriaProductoDTO iCategoria : categoria.getHijos()) {
				CategoriaProductoDTO busqueda = esPadre(iCategoria, llavePadre);
				if (busqueda != null)
					return busqueda;
			}
		}
		return null;
	}

	private List<ProductoDTO> detallarProductos2Plantilla(List<ProductoDTO> productos,
			DocumentoPlantillaCaracteristicaDTO pCampo, CategoriaProductoDTO categoria, String tercero,
			String propiedadFuncionTarifario, List<PedidoVentaCaracteristicaDTO> parametrosFuncionTarifario,
			String token) throws ServerException {
		if (productos != null && !productos.isEmpty()) {
			List<PropiedadDTO> tarifario = Propiedades.obtenerVariosParametro(pCampo, Propiedades.DETALLE_TARIFARIO);
			String imagenes = Propiedades.obtenerValor(pCampo, Propiedades.DETALLE_OCULTAR_IMAGENES);
			if (tarifario != null && tarifario.isEmpty())
				tarifario = null;
			if (imagenes.isEmpty())
				imagenes = null;
			List<ProductoDTO> productosSimplificados = detallePedidoVentaService
					.simplificarConsultaBDProductos(productos);
			for (ProductoDTO productoDTO : productos) {
				DetallePedidoVentaDTO filtroPlantilla = new DetallePedidoVentaDTO();
				filtroPlantilla.setPlantilla(pCampo.getPlantilla());
				filtroPlantilla.setProducto(productoDTO.getLlaveTabla());
				productoDTO.setDetallePlantilla(detallePedidoVentaService.consultaCompleta(filtroPlantilla, tarifario,
						tercero, propiedadFuncionTarifario, parametrosFuncionTarifario, productosSimplificados, token));
				if (imagenes == null) {
					if (productoDTO.getImagen() == null && categoria != null)
						productoDTO.setImagen(categoria.getImagen());
				} else {
					productoDTO.setImagen(null);
				}
				productoDTO.getDetallePlantilla().setProductoImagen(productoDTO.getImagen());
				productoDTO.getDetallePlantilla().setProductoDocumento(productoDTO.getDocumento());
				// productoDTO.setPropiedades(configuracionSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.PLANTILLA,
				// productoDTO.getLlaveTabla(), null, pCampo.getSecurityToken()));
			}
		}
		return productos;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaCaracteristicaDTO inactivar(PedidoVentaCaracteristicaDTO pCampo, String token)
			throws ServerException {
		if (pCampo.getDetalles() != null && !pCampo.getDetalles().isEmpty()) {
			for (DetallePedidoVentaDTO detalle : pCampo.getDetalles()) {
				detalle.setTransaccionInactivo(pCampo.getTransaccionRegistro());
				detallePedidoVentaService.inactivar(detalle, token);
			}
		}
		return pCampo;
	}

	public void novedadParcial(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		// al expediente anterior modificarle el documento quitandole las diferencias
		if (pCampo != null) {
			// Obtengo los detalles del documento y del expediente
			List<DetallePedidoVentaDTO> detallesDocumento = pCampo.getDetalles();
			PedidoVentaDTO documentoPrincipal = pCampo.getDependientes().get(0).getPrincipal();
			if (documentoPrincipal == null)
				documentoPrincipal = pedidoService.consultaXId(pCampo.getDependientes().get(0).getValorOpcion());
			PedidoVentaDTO expediente = pedidoService.obtenerCamposCompletos(documentoPrincipal, token);
			List<DetallePedidoVentaDTO> detallesExpediente = detallePedidoVentaService
					.listar2Documento(expediente.getLlaveTabla());
			// Expediente: Como queda el expediente
			List<DetallePedidoVentaDTO> detallesFinalExpediente = new ArrayList<DetallePedidoVentaDTO>();
			// Nuevo : Como queda el expediente actual
			List<DetallePedidoVentaDTO> detallesFinalNuevo = new ArrayList<DetallePedidoVentaDTO>();

			for (DetallePedidoVentaDTO detalleDocumento : detallesDocumento) {
				for (DetallePedidoVentaDTO detalleExpediente : detallesExpediente) {

					if (detalleDocumento.getProducto().compareTo(detalleExpediente.getProducto()) == 0) {
						if (detalleDocumento.getCantidad().compareTo(detalleExpediente.getCantidad()) == 0) {
							detallePedidoVentaService.createFieldsProduct(detalleExpediente);
							detallesFinalExpediente.add(detalleExpediente);
							detallesExpediente.remove(detalleExpediente);
						} else {
							BigDecimal nuevoTotal = detalleExpediente.getCantidad()
									.add(detalleDocumento.getCantidad().negate());
							if (nuevoTotal.compareTo(BigDecimal.ZERO) < 0)
								throw new ServerException(
										"En una devolución no se puede colocar una cantidad mayor a la inicial de "
												+ detalleExpediente.getCantidad());
							// Cuadra los inventarios con promocion
							if (detalleDocumento.getCantidadPromocion().compareTo(0) != 0) {
								BigDecimal cantidadRestanteTotales = detalleExpediente.getCantidadTotal()
										.add(detalleDocumento.getCantidadTotal().negate());
								detalleDocumento.setCantidadPromocion(
										cantidadRestanteTotales.add(nuevoTotal.negate()).intValue());
								detalleDocumento.setCantidadPromocionBase(nuevoTotal.intValue());
							}
							detalleDocumento.setCantidad(nuevoTotal);
							detalleDocumento.setValorUnitario(detalleExpediente.getValorUnitario());
							detalleDocumento.setValorMaximo(detalleExpediente.getValorMaximo());
							detalleDocumento.setValorMinimo(detalleExpediente.getValorMinimo());
							detalleDocumento.setValorSubtotal(
									detalleDocumento.getValorUnitario().multiply(detalleDocumento.getCantidad()));
							detalleDocumento.setValorTotal(detalleDocumento.getValorSubtotal());
							// Con el tema de campos basicos toca es actualizar los campos basicos
							detallePedidoVentaService.actualizarCamposNovedadParcial(detalleDocumento);
							detallesFinalNuevo.add(detalleDocumento);
							detalleExpediente.setCantidad(detalleExpediente.getCantidad().add(nuevoTotal.negate()));
							// Aqui porque antes no me actualiza la cantidad en las novedades parciales
							detallePedidoVentaService.createFieldsProduct(detalleExpediente);
							detallesFinalExpediente.add(detalleExpediente);
							detallesExpediente.remove(detalleExpediente);
						}
						break;
					}
				}
			}
			if (!detallesExpediente.isEmpty()) {
				for (DetallePedidoVentaDTO detalleExpediente : detallesExpediente) {
					DetallePedidoVentaDTO detalleDocumento = new DetallePedidoVentaDTO();
					detalleDocumento.setProducto(detalleExpediente.getProducto());
					detalleDocumento.setProductoTercero(detalleExpediente.getProductoTercero());
					detalleDocumento.setProductoCodigo(detalleExpediente.getProductoCodigo());
					detalleDocumento.setProductoImagen(detalleExpediente.getProductoImagen());
					detalleDocumento.setProductoDocumento(detalleExpediente.getProductoDocumento());
					detalleDocumento.setNombre(detalleExpediente.getNombre());
					detalleDocumento.setCantidad(detalleExpediente.getCantidad());
					detalleDocumento.setCantidadPromocion(detalleExpediente.getCantidadPromocion());
					detalleDocumento.setCantidadPromocionBase(detalleExpediente.getCantidadPromocionBase());
					detalleDocumento.setCantidadTotal(detalleExpediente.getCantidadTotal());
					detalleDocumento.setValorUnitario(detalleExpediente.getValorUnitario());
					detalleDocumento.setValorMaximo(detalleExpediente.getValorMaximo());
					detalleDocumento.setValorMinimo(detalleExpediente.getValorMinimo());
					detalleDocumento.setValorTotal(detalleExpediente.getValorMinimo());
					detalleDocumento.setPlantilla(detalleExpediente.getPlantilla());
					detallePedidoVentaService.createFieldsProduct(detalleDocumento);
					detallesFinalNuevo.add(detalleDocumento);
				}
			}
			if (detallesFinalNuevo.isEmpty()) {
				throw new ServerException("se debe modificar la cantidad de productos");
			} else {
				pCampo.setDetalles(detallesFinalNuevo);
			}

			String fieldWithDetails = null;
			for (PedidoVentaCaracteristicaDTO iCampoExpediente : expediente.getCaracteristicas()) {
				if (iCampoExpediente.getCampoDTO().getCodigo().compareTo(pCampo.getCampoDTO().getCodigo()) == 0) {
					iCampoExpediente.setDetalles(detallesFinalExpediente);
					iCampoExpediente.setModificado(true);
					fieldWithDetails = iCampoExpediente.getCampo();
					break;
				}

			}
			//Esto e hizo por nuestro querido brandingo box, formulario pedidos y valor total
			if (fieldWithDetails != null) {
				for (PedidoVentaCaracteristicaDTO iCampoExpediente : expediente.getCaracteristicas()) {
					if (iCampoExpediente.getCampoDTO().getFormato()
							.compareTo(DocumentoPlantillaCaracteristicaDTO.NUMERO) == 0) {
						iCampoExpediente.setCampoDTO(
								caracteristicaService.cargarComplementos(iCampoExpediente.getCampoDTO(), token));
						List<PropiedadDTO> propsDependent = Propiedades
								.obtenerVariosParametro(iCampoExpediente.getCampoDTO(), Propiedades.DEPENDE);
						if (propsDependent != null && !propsDependent.isEmpty()
								&& Propiedades.obtenerValor(iCampoExpediente.getCampoDTO(),
										Propiedades.PERMISO_CAMPO_BLOQUEAR) != null) {
							for (PropiedadDTO iDep : propsDependent) {
								if (iDep.getValor().compareTo(fieldWithDetails) == 0) {
									iCampoExpediente.setValorNumero(null);
									iCampoExpediente.setModificado(true);
									break;
								}
							}
						}
					}
				}
			}

			// Sucede que aqui llega nulo porque previamente se a validado una
			// carcateristica
			if (expediente.getEstado() == null)
				expediente.setEstado(SharedConstants.STATE_ACTIVE);
			// Cuando revise lo del documento modificador veo como arreglo esto
			saveUpdateInactivateDocumentFunction.updateWithoutTransaction(expediente, pCampo.getDocumento(), token,
					true);
			// Necesito que el expediente quede en estado null para que se tramite
			expediente.setEstado(null);// Esto es crazy pero me toca hacerlo por el momento
		}
	}
}
