package com.softure.document_execution.application;

import java.util.List;

import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.authorization.application.UsuarioRolProductoSvc;
import com.softure.authorization.domain.UsuarioRolProductoDTO;
import com.softure.authorization.domain.UsuarioRolProductoFilterDTO;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.DetallePedidoVentaDTO;
import com.softure.document_execution.domain.DetallePedidoVentaFilterDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.infrastructure.DetallePedidoVentaMapper;
import com.softure.inventory.application.ProductoSvc;
import com.softure.inventory.domain.ProductoDTO;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.tariff.application.base.TarifaSvc;
import com.softure.tariff.domain.TarifaDTO;
import com.softure.tariff.domain.TarifaFilterDTO;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.logisticpymes.application.BasicSvc;

@Service("detallePedidoVentaService")
public class DetallePedidoVentaSvc extends BasicSvc<DetallePedidoVentaDTO, DetallePedidoVentaFilterDTO> {

	@Autowired @Lazy 
	private DetallePedidoVentaMapper detallePedidoVentaMapper;
	@Autowired @Lazy 
	private ProductoSvc productoService;
	@Autowired @Lazy 
	private PedidoVentaSvc documentoService;
	@Autowired @Lazy 
	private UsuarioRolProductoSvc usuarioRolProductoService;
	@Autowired @Lazy 
	private TarifaSvc tarifaService;
	@Autowired @Lazy 
	private PropiedadSvc configuracionSvc;
	@Autowired @Lazy 
	private CallDocumentCRUD crudservice;

	@Override
	public DetallePedidoVentaDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. DetallePedidoVenta");
		DetallePedidoVentaFilterDTO dto = new DetallePedidoVentaFilterDTO();
		dto.setLlaveTabla(llave);
		return detallePedidoVentaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = detallePedidoVentaMapper;
	}

	@Override
	public DetallePedidoVentaDTO activar(DetallePedidoVentaDTO dto, String token) throws ServerException {
		// BEGIN DetallePedidoVenta_activar
		return activate(dto);
		// END DetallePedidoVenta_activar
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DetallePedidoVentaDTO actualizar(DetallePedidoVentaDTO dto, String token) throws ServerException {
		if(dto.getPlantillaDetalle()!=null) {
			dto.getDocumentoDetalle().setFuncionario(getUserFlex(token));
			dto.getDocumentoDetalle().setPlantilla(dto.getPlantillaDetalle());
			crudservice.saveWithoutTransaction(dto.getDocumentoDetalle(), token, false);
		}
		dto = update(dto);
		return dto;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DetallePedidoVentaDTO inactivar(DetallePedidoVentaDTO dto, String token) throws ServerException {
		// BEGIN DetallePedidoVenta_inactivar
		if (dto.getTransaccionInactivo() == null)
			throw new ServerException("Ingrese la transaccion de inactivar");
		DetallePedidoVentaDTO bd = consultaXId(dto.getLlaveTabla());
		bd.setEstado(SharedConstants.STATE_INACTIVE);
		bd.setTransaccionInactivo(dto.getTransaccionInactivo());
		return update(bd);
		// END DetallePedidoVenta_inactivar
	}

	@Override
	public DetallePedidoVentaDTO consultaUnica(DetallePedidoVentaFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}

	@Override
	public int contarResultados(DetallePedidoVentaFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}

	@Override
	public List<DetallePedidoVentaDTO> listarConsulta(DetallePedidoVentaFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DetallePedidoVentaDTO guardar(DetallePedidoVentaDTO dto, String token) throws ServerException {
		
		// if(dto.getCantidad().multiply(dto.getValorUnitario()).add(dto.getValorTotal().negate()).abs().longValue()
		// > 1)
		// throw new ServerException("El valor total (" +
		// SoftureUtil.formatMoney(dto.getValorTotal()) +") no concuerda con la cantidad
		// (" + SoftureUtil.formatNumber(dto.getCantidad()) +") x valor unitario (" +
		// SoftureUtil.formatMoney(dto.getValorUnitario()) +") =" +
		// SoftureUtil.formatMoney(dto.getCantidad().multiply(dto.getValorUnitario())));
		if(dto.getPlantillaDetalle()!=null) {
			dto.getDocumentoDetalle().setFuncionario(getUserFlex(token));
			dto.getDocumentoDetalle().setPlantilla(dto.getPlantillaDetalle());
			if(dto.getDetalleId()==null) {
				dto.setDocumentoDetalle(crudservice.saveWithoutTransaction(dto.getDocumentoDetalle(), token, false));
				dto.setDetalleId(dto.getDocumentoDetalle().getLlaveTabla());	
			} else {
				//aqui debo mejorar para que no se hagan procesos si no tuvieron modificaciones
					boolean iContadorModificadas = false;
					for (PedidoVentaCaracteristicaDTO iCampoDocumento : dto.getDocumentoDetalle().getCaracteristicas()) {
						if (iCampoDocumento.getModificado()) {
							iContadorModificadas = true;
							break;
						}
					}
					if (iContadorModificadas) {
						dto.getDocumentoDetalle().setLlaveTabla(dto.getDetalleId());
						dto.setDocumentoDetalle(crudservice.updateWithoutTransaction(dto.getDocumentoDetalle(), dto.getDetalleId(), token, false));		
					}
			}
			
		}
		dto = save(dto);
		return dto;
		// END DetallePedidoVenta_guardar
	}

// BEGIN region aditionalMethods
	public List<DetallePedidoVentaDTO> listarCompleto(String documento, List<PropiedadDTO> tarifario,
			String propiedadFuncion, List<PedidoVentaCaracteristicaDTO> parametrosFuncionTarifario, String token, String newOnlyFormProcess)
			throws ServerException {
		List<DetallePedidoVentaDTO> base = detallePedidoVentaMapper.listar2Documento(documento);
		if (base == null || base.isEmpty())
			return new ArrayList<DetallePedidoVentaDTO>();
		List<DetallePedidoVentaDTO> result = new ArrayList<DetallePedidoVentaDTO>();
		List<ProductoDTO> productosSimplificados = new ArrayList<ProductoDTO>();
		for (DetallePedidoVentaDTO iDetalle : base) {
			ProductoDTO iProducto = new ProductoDTO();
			iProducto.setLlaveTabla(iDetalle.getProducto());
			productosSimplificados.add(iProducto);
		}
		productosSimplificados = simplificarConsultaBDProductos(productosSimplificados);
		for (DetallePedidoVentaDTO detallePedidoVentaDTO : base) {
			result.add(consultaCompleta(detallePedidoVentaDTO, tarifario, null, propiedadFuncion,
					parametrosFuncionTarifario, productosSimplificados, token, newOnlyFormProcess));
		}
		return result;
	}

	public List<DetallePedidoVentaDTO> listar2Documento(String documento) throws ServerException {
		return detallePedidoVentaMapper.listar2Documento(documento);
	}


	public List<ProductoDTO> simplificarConsultaBDProductos(List<ProductoDTO> productos) throws ServerException {
		List<ProductoDTO> result = productoService.listarProductoSimplificar(productos);
		List<ProductoDTO> bases = new ArrayList<ProductoDTO>();
		for (ProductoDTO productoDTO : result) {
			if (productoDTO.getProductoBase() != null) {
				ProductoDTO iBase = new ProductoDTO();
				iBase.setLlaveTabla(productoDTO.getProductoBase());
				iBase.setNombre(productoDTO.getNombre());
				iBase.setCategoriaPlantilla(productoDTO.getCategoriaPlantilla());
				bases.add(iBase);
			}
		}
		if (bases != null && bases.size() != 0) {
			List<PropiedadDTO> propiedadesBases = configuracionSvc.listarProductoSimplificar(bases);
			for (ProductoDTO iProductoDTO : bases) {
				iProductoDTO.setPropiedades(new ArrayList<PropiedadDTO>());
				if (propiedadesBases != null && !propiedadesBases.isEmpty()) {
					for (PropiedadDTO propiedadDTO : propiedadesBases) {
						if(iProductoDTO.getCategoriaPlantilla()==null)
							throw new ServerException("El producto " +  iProductoDTO.getNombre() + " tiene una categoria plantilla nula");
						if (propiedadDTO.getCampo().compareTo(iProductoDTO.getCategoriaPlantilla()) == 0)
							iProductoDTO.getPropiedades().add(propiedadDTO);
					}
				}
			}
		}
		List<PropiedadDTO> propiedadesProducto = configuracionSvc.listarProductoSimplificar(productos);
		for (ProductoDTO productoDTO : result) {
			productoDTO.setPropiedades(new ArrayList<PropiedadDTO>());
			if (propiedadesProducto != null && !propiedadesProducto.isEmpty() && productoDTO.getCategoriaPlantilla() != null) {
				for (PropiedadDTO propiedadDTO : propiedadesProducto) {
					if (propiedadDTO.getCampo().compareTo(productoDTO.getCategoriaPlantilla()) == 0)
						productoDTO.getPropiedades().add(propiedadDTO);
				}
			}
			if (productoDTO.getPropiedades().isEmpty() && productoDTO.getProductoBase() != null) {
				for (ProductoDTO iBase : bases) {
					if (iBase.getCategoriaPlantilla()!=null && productoDTO.getProductoBase().compareTo(iBase.getCategoriaPlantilla()) == 0) {
						productoDTO.setPropiedades(iBase.getPropiedades());
						break;
					}
				}
			}
			productoDTO.setTemplateFields(Propiedades.obtenerValor(productoDTO, Propiedades.TIPO_PRODUCTO_FORMULARIO_DETALLADO));
			if(productoDTO.getTemplateFields()!=null && productoDTO.getTemplateFields().isEmpty())
				productoDTO.setTemplateFields(null);
		}
		return result;
	}

	public DetallePedidoVentaDTO consultaCompleta(DetallePedidoVentaDTO dto, List<PropiedadDTO> tarifario,
			String tercero, String propiedadFuncionTarifario,
			List<PedidoVentaCaracteristicaDTO> parametrosFuncionTarifario, List<ProductoDTO> productosSimplificados,
			String token, String newOnlyFormProcess) throws ServerException {
		DetallePedidoVentaDTO result;
		if (dto.getProducto() == null)
			throw new ServerException("Para esta operacion se debe colocar el producto");
		ProductoDTO producto = null;
		for (ProductoDTO productoDTO : productosSimplificados) {
			if (productoDTO.getLlaveTabla().compareTo(dto.getProducto()) == 0) {
				producto = productoDTO;
				break;
			}
		}
		if (producto == null)
			throw new ServerException("Para esta operacion se debe colocar el producto con identificador valido");
		// String productoBase = producto.getProductoBase();//Cuando se configura
		// producto base se toma los valores y carcteristicas del base
		if (dto.getLlaveTabla() != null) {
			result = consultaXId(dto.getLlaveTabla());
			if (result.getProductoImagen() == null)result.setProductoImagen(producto.getImagen());
			if (result.getNombre() == null || result.getNombre().isEmpty())
				result.setNombre(producto.getNombre());
		} else {
			result = new DetallePedidoVentaDTO();
			result.setProducto(producto.getLlaveTabla());
			result.setProductoCodigo(producto.getCodigo());
			result.setProductoImagen(producto.getImagen());
			result.setProductoDocumento(producto.getDocumento());
			result.setNombre(producto.getNombre());
			result.setCantidad(BigDecimal.ONE);
			// Primero las promociones
			if (tercero != null) {
				UsuarioRolProductoFilterDTO fPromocion = new UsuarioRolProductoFilterDTO();
				fPromocion.setProducto(dto.getProducto());
				fPromocion.setDocumento(tercero);
				fPromocion.setEstado(SharedConstants.STATE_ACTIVE);
				UsuarioRolProductoDTO filterPromocion = usuarioRolProductoService.consultaUnica(fPromocion);
				if (filterPromocion == null)
					throw new ServerException("El producto en la plantilla no se encuentra configurado");
				result.setNombre((filterPromocion.getNombre() != null && !filterPromocion.getNombre().isEmpty())
						? filterPromocion.getNombre()
						: filterPromocion.getProductoNombre());
				result.setProductoTercero(filterPromocion.getLlaveTabla());
				result.setCantidadPromocion(filterPromocion.getCantidadPromocion());
				result.setCantidadPromocionBase(filterPromocion.getCantidadPromocionBase());
			}
		}
		result.setPlantillaDetalle(producto.getTemplateFields());
		result.setPropiedades(producto.getPropiedades());
		if (propiedadFuncionTarifario != null) {
			result.setTarifas(tarifaService.obtenerTarifaFuncion(propiedadFuncionTarifario, producto,
					parametrosFuncionTarifario));
		} else {
			// Consulto las tarifas
			if (tarifario != null) {
				result.setTarifas(consultarTarifas(tarifario, tercero, dto.getProducto()));
				if (producto.getProductoBase() != null
						&& (result.getTarifas() == null || result.getTarifas().isEmpty()))
					result.setTarifas(consultarTarifas(tarifario, tercero, producto.getProductoBase()));
			}
		}
		if (result.getTarifas() != null && !result.getTarifas().isEmpty() && dto.getLlaveTabla() == null) {
			TarifaDTO filter = result.getTarifas().get(0);
			result.setCantidad(BigDecimal.ONE);
			result.setValorUnitario(filter.getValor());
			result.setValorSubtotal(filter.getValor());
			result.setValorTotal(filter.getValor());
			result.setValorMaximo(filter.getValorMaximo());
			result.setValorMinimo(filter.getValorMinimo());
		}

		if (result.getCantidad() == null)
			result.setCantidad(BigDecimal.ZERO);
		if (result.getValorUnitario() == null)
			result.setValorUnitario(BigDecimal.ZERO);
		if(result.getDetalleId()!=null) {
			result.setDocumentoDetalle(documentoService.consultaCompleta(result.getDetalleId(), token));
		}
		createFieldsProduct(result, token, newOnlyFormProcess);
		return result;
	}

	
	public void createFieldsProduct(DetallePedidoVentaDTO field, String token, String newOnlyFormProcess) throws ServerException {
		
		
		
		if(field.getDocumentoDetalle() ==null) {
			field.setDocumentoDetalle(new PedidoVentaDTO());
			if(field.getPlantillaDetalle()!=null) {
				field.getDocumentoDetalle().setPlantilla(field.getPlantillaDetalle());
				documentoService.obtenerCamposCompletos(field.getDocumentoDetalle(), null);
				String usuario = null; //copia de ocumeto plantillapara no hacer una refecia circular
				if (token != null)
					usuario = getUserFlex(token);
				for (PedidoVentaCaracteristicaDTO iField : field.getDocumentoDetalle().getCaracteristicas()) {
					iField.getCampoDTO().setPropiedades(configuracionSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.CAMPO,
							iField.getCampoDTO().getLlaveTabla(), null, usuario));
				}
			}
		}
		
		
			
		if(field.getDocumentoDetalle().getCaracteristicas()==null)field.getDocumentoDetalle().setCaracteristicas(new ArrayList<PedidoVentaCaracteristicaDTO>());
		
		if(newOnlyFormProcess!=null && !newOnlyFormProcess.isEmpty()) return;
		if (field.getPropiedades() == null)
			field.setPropiedades(new ArrayList<PropiedadDTO>());
		DocumentoPlantillaCaracteristicaDTO baseValorUnitario = null;
		if (field.getTarifas() != null && !field.getTarifas().isEmpty()) {
			BigDecimal valorUnitario = null;
			if (field.getLlaveTabla() != null) {
				valorUnitario = field.getValorUnitario();
			} else {
				valorUnitario = getTarifaInitial(field);
			}
			PropiedadDTO pCampoUnitario = Propiedades.obtenerParametro(field,
					Propiedades.PRODUCTO_CAMPO_VALOR_UNITARIO);
			if (pCampoUnitario == null || pCampoUnitario.getValor() == null
					|| pCampoUnitario.getValor().compareTo("***UNIDAD") == 0) {// Sucede que como se copian las
																				// propiedades entonces de crean aveces
																				// esta propiedad pero el campo no
				// creo un campo con el valor unitario
				baseValorUnitario = new DocumentoPlantillaCaracteristicaDTO();
				baseValorUnitario.setCodigo("UNIDAD");
				baseValorUnitario.setFormato(DocumentoPlantillaCaracteristicaDTO.NUMERO);
				baseValorUnitario.setNombre("UNIDAD");
				baseValorUnitario.setOrden(3);
				baseValorUnitario.setLlaveTabla("***UNIDAD");
				baseValorUnitario.setPropiedades(new ArrayList<PropiedadDTO>());
				baseValorUnitario.getPropiedades().add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				 null, Propiedades.PERMISO_CAMPO_MODIFICABLE, Propiedades.TRUE, null));
				baseValorUnitario.getPropiedades().add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, null,
						Propiedades.NUMERO_MONEDA, Propiedades.TRUE, null));
				PedidoVentaCaracteristicaDTO cpValorUnitario = new PedidoVentaCaracteristicaDTO();
				cpValorUnitario.setDocumento(field.getLlaveTabla());
				cpValorUnitario.setValorNumero(valorUnitario);
				cpValorUnitario.setValorText(cpValorUnitario.getValorNumero().toString());
				cpValorUnitario.setCampoDTO(baseValorUnitario);
				cpValorUnitario.setCampo("***UNIDAD");
				field.getDocumentoDetalle().getCaracteristicas().add(0, cpValorUnitario);
				field.getPropiedades().add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, null,
						Propiedades.PRODUCTO_CAMPO_VALOR_UNITARIO, baseValorUnitario.getLlaveTabla(), null));
			} else {
				for (PedidoVentaCaracteristicaDTO iFieldValorUnitario : field.getDocumentoDetalle().getCaracteristicas()) {
					if (iFieldValorUnitario.getCampo().compareTo(pCampoUnitario.getValor()) == 0) {
						baseValorUnitario = iFieldValorUnitario.getCampoDTO();
						iFieldValorUnitario.setValorNumero(valorUnitario);
						iFieldValorUnitario.setValorText(iFieldValorUnitario.getValorNumero().toString());
						break;
					}
				}
			}
			if (baseValorUnitario == null)
				throw new ServerException(
						"Se ha presentado un problema consultando el campo de valor unitario" + field.getNombre());
		}

		PropiedadDTO pCampoCantidad = Propiedades.obtenerParametro(field, Propiedades.PRODUCTO_CAMPO_CANTIDAD);
		DocumentoPlantillaCaracteristicaDTO baseCantidad = null;
		if (pCampoCantidad == null || pCampoCantidad.getValor() == null
				|| pCampoCantidad.getValor().compareTo("***CANTIDAD") == 0) {// Sucede que como se copian las
																				// propiedades entonces de crean aveces
																				// esta propiedad pero el campo no
			// creo un campo con el valor unitario
			baseCantidad = new DocumentoPlantillaCaracteristicaDTO();
			baseCantidad.setCodigo("CANTIDAD");
			baseCantidad.setFormato(DocumentoPlantillaCaracteristicaDTO.NUMERO);
			baseCantidad.setNombre("CANTIDAD");
			baseCantidad.setOrden(2);
			baseCantidad.setLlaveTabla("***CANTIDAD");
			baseCantidad.setPropiedades(new ArrayList<PropiedadDTO>());
			baseCantidad.getPropiedades().add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, null,
					Propiedades.PERMISO_CAMPO_MODIFICABLE, Propiedades.TRUE, null));
			PedidoVentaCaracteristicaDTO cpCantidad = new PedidoVentaCaracteristicaDTO();
			cpCantidad.setCampoDTO(baseCantidad);
			cpCantidad.setDocumento(field.getLlaveTabla());
			cpCantidad.setValorNumero(field.getCantidad());
			if (field.getCantidad() != null)
				cpCantidad.setValorText(field.getCantidad().toString());
			cpCantidad.setCampo("***CANTIDAD");
			field.getDocumentoDetalle().getCaracteristicas().add(0, cpCantidad);
			field.getPropiedades().add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, null,
					Propiedades.PRODUCTO_CAMPO_CANTIDAD, baseCantidad.getLlaveTabla(), null));
			//
		} else {
			for (PedidoVentaCaracteristicaDTO iFieldCantidad : field.getDocumentoDetalle().getCaracteristicas()) {
				if (iFieldCantidad.getCampo().compareTo(pCampoCantidad.getValor()) == 0) {
					baseCantidad = iFieldCantidad.getCampoDTO();
					break;
				}
			}
		}
		if (baseCantidad == null)
			throw new ServerException(
					"Se ha presentado un problema consultando el campo de valor unitario" + field.getNombre());

		// creo un campo con el producto
		DocumentoPlantillaCaracteristicaDTO baseProducto = crearCampoBaseProducto();
		PedidoVentaCaracteristicaDTO cpProducto = new PedidoVentaCaracteristicaDTO();
		cpProducto.setCampoDTO(baseProducto);
		cpProducto.setDocumento(field.getLlaveTabla());
		cpProducto.setValorOpcion(field.getProductoDocumento());
		if (field.getProductoDocumento() != null)
			cpProducto.setPrincipal(documentoService.consultaXId(field.getProductoDocumento()));
		cpProducto.setCampo("***PRODUCTO");
		field.getDocumentoDetalle().getCaracteristicas().add(0, cpProducto);

		if (field.getTarifas() != null && !field.getTarifas().isEmpty()) {
			// Creo un campo con el valor subtotal
			PropiedadDTO pCampoTotal = Propiedades.obtenerParametro(field, Propiedades.PRODUCTO_CAMPO_TOTAL);
			DocumentoPlantillaCaracteristicaDTO baseTotal = null;
			if (pCampoTotal == null || pCampoTotal.getValor() == null
					|| pCampoTotal.getValor().compareTo("***TOTAL") == 0) {// Sucede que como se copian las propiedades
																			// entonces de crean aveces esta propiedad
																			// pero el campo no
				baseTotal = new DocumentoPlantillaCaracteristicaDTO();
				baseTotal.setCodigo("TOTAL");
				baseTotal.setFormato(DocumentoPlantillaCaracteristicaDTO.NUMERO);
				baseTotal.setNombre("TOTAL");
				baseTotal.setOrden(4);
				baseTotal.setLlaveTabla("***TOTAL");
				baseTotal.setPropiedades(new ArrayList<PropiedadDTO>());
				baseTotal.getPropiedades().add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, null,
						Propiedades.NUMERO_MONEDA, Propiedades.TRUE, null));
				baseTotal.getPropiedades().add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, null,
						Propiedades.PERMISO_CAMPO_BLOQUEAR, Propiedades.TRUE, null));
				// baseTotal.getPropiedades().add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				// null, Propiedades.MODIFICABLE, Propiedades.TRUE, null));
				baseTotal.getPropiedades()
						.add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, null,
								Propiedades.NUMERO_FORMULA,
								baseCantidad.getCodigo() + "*" + baseValorUnitario.getCodigo(), null));
				baseTotal.getPropiedades().add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, null,
						Propiedades.DEPENDE, baseValorUnitario.getLlaveTabla(), null));
				baseTotal.getPropiedades().add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, null,
						Propiedades.DEPENDE, baseCantidad.getLlaveTabla(), null));
				PedidoVentaCaracteristicaDTO cpTotal = new PedidoVentaCaracteristicaDTO();
				cpTotal.setDocumento(field.getLlaveTabla());
				cpTotal.setValorNumero(field.getValorTotal());
				if (cpTotal.getValorNumero() != null)
					cpTotal.setValorText(cpTotal.getValorNumero().toString());
				cpTotal.setCampoDTO(baseTotal);
				cpTotal.setCampo("***TOTAL");
				field.getDocumentoDetalle().getCaracteristicas().add(cpTotal);
				field.getPropiedades().add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, null,
						Propiedades.PRODUCTO_CAMPO_TOTAL, baseTotal.getLlaveTabla(), null));
				//
			} else {
				for (PedidoVentaCaracteristicaDTO iFieldTotal : field.getDocumentoDetalle().getCaracteristicas()) {
					if (iFieldTotal.getCampo().compareTo(pCampoTotal.getValor()) == 0) {
						baseTotal = iFieldTotal.getCampoDTO();
						if(baseTotal.getPropiedades()==null) {
							baseTotal.setPropiedades(new ArrayList<>());
						}
						baseTotal.getPropiedades().add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, null,
								Propiedades.DEPENDE, baseValorUnitario.getLlaveTabla(), null));
						baseTotal.getPropiedades().add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, null,
								Propiedades.DEPENDE, baseCantidad.getLlaveTabla(), null));
						break;
					}
				}
			}
			if (baseTotal == null)
				throw new ServerException(
						"Se ha presentado un problema consultando el campo de valor total" + field.getNombre());
		}

	}

	private BigDecimal getTarifaInitial(DetallePedidoVentaDTO field) {
		if (field.getTarifas() == null || field.getTarifas().isEmpty())
			return BigDecimal.ZERO;
		return field.getTarifas().get(0).getValor();
	}

	private List<TarifaDTO> consultarTarifas(List<PropiedadDTO> tarifario, String tercero, String producto)
			throws ServerException {
		List<TarifaDTO> result = null;
		if (tarifario != null && !tarifario.isEmpty()) {
			result = new ArrayList<TarifaDTO>();
			for (PropiedadDTO propiedadDTO : tarifario) {
				TarifaFilterDTO filter = new TarifaFilterDTO();
				filter.setTarifario(propiedadDTO.getValor());
				filter.setProducto(producto);
				filter.setRecurso(tercero);
				result.addAll(tarifaService.obtenerTarifa(filter));
			}
		}
		return result;
	}

	public DocumentoPlantillaCaracteristicaDTO crearCampoBaseProducto() {
		DocumentoPlantillaCaracteristicaDTO baseProducto = new DocumentoPlantillaCaracteristicaDTO();
		baseProducto.setCodigo("PRODUCTO");
		baseProducto.setFormato(DocumentoPlantillaCaracteristicaDTO.PROCESO);
		baseProducto.setNombre("PRODUCTO");
		baseProducto.setOrden(1);
		baseProducto.setLlaveTabla("***PRODUCTO");
		baseProducto.setPropiedades(new ArrayList<PropiedadDTO>());
		baseProducto.getPropiedades().add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, null,
				Propiedades.PERMISO_CAMPO_BLOQUEAR, Propiedades.TRUE, null));// NO se puede modificar
		return baseProducto;
	}

	public void definirPropiedad2Caracteristicas(DetallePedidoVentaDTO detail) throws ServerException {
		if (detail.getDocumentoDetalle() == null || detail.getDocumentoDetalle().getCaracteristicas() == null || detail.getDocumentoDetalle().getCaracteristicas().isEmpty())
			throw new ServerException("Ahora debes traer los campos basicos");
		if (detail.getPropiedades() == null)
			throw new ServerException("Por favor envia las propiedades");

		PropiedadDTO pCampoCantidad = Propiedades.obtenerParametro(detail, Propiedades.PRODUCTO_CAMPO_CANTIDAD);
		String keyCampoCantidad = "***CANTIDAD";
		if (pCampoCantidad != null)
			keyCampoCantidad = pCampoCantidad.getValor();
		PedidoVentaCaracteristicaDTO cpCantidad = CallDocumentCommons.obtenerValor(detail.getDocumentoDetalle().getCaracteristicas(),
				keyCampoCantidad);
		if (cpCantidad != null && cpCantidad.getValorNumero() != null)
			detail.setCantidad(cpCantidad.getValorNumero());// Al modificar no se actualizan estos campos

		PropiedadDTO pCampoUnitario = Propiedades.obtenerParametro(detail, Propiedades.PRODUCTO_CAMPO_VALOR_UNITARIO);
		String keyCampoUnitario = "***UNIDAD";
		if (pCampoUnitario != null)
			keyCampoUnitario = pCampoUnitario.getValor();
		PedidoVentaCaracteristicaDTO cpUnitario = CallDocumentCommons.obtenerValor(detail.getDocumentoDetalle().getCaracteristicas(),
				keyCampoUnitario);
		if (cpUnitario != null && cpUnitario.getValorNumero() != null)
			detail.setValorUnitario(cpUnitario.getValorNumero());// Cuando no tiene tarifario no van estos campos,
																	// deberia validar que si sean ciertos

		PropiedadDTO pCampoTotal = Propiedades.obtenerParametro(detail, Propiedades.PRODUCTO_CAMPO_TOTAL);
		String keyCampoTotal = "***TOTAL";
		if (pCampoTotal != null)
			keyCampoTotal = pCampoTotal.getValor();
		PedidoVentaCaracteristicaDTO cpTotal = CallDocumentCommons.obtenerValor(detail.getDocumentoDetalle().getCaracteristicas(),
				keyCampoTotal);
		// Sucede que en Universal el total no es igual al producto normal se hace pro
		// otra formula
		if (cpTotal != null && cpUnitario != null && cpUnitario.getValorNumero() != null) {
			// Cuando no tiene tarifario no van estos campos, deberia validar que si sean ciertos
			detail.setValorTotal(cpTotal.getValorNumero());
			if(detail.getValorTotal()==null) detail.setValorTotal(BigDecimal.ZERO);
			if (detail.getCantidad() != null && detail.getCantidad().multiply(detail.getValorUnitario())
					.add(detail.getValorTotal().negate()).abs().longValue() > 1) {
				if (Propiedades.obtenerParametro(cpTotal.getCampoDTO(), Propiedades.NUMERO_FORMULA) == null) {
					// throw new ServerException("El valor total (" +
					// SoftureUtil.formatMoney(detail.getValorTotal()) +") no concuerda con la
					// cantidad (" + SoftureUtil.formatNumber(detail.getCantidad()) +") x valor
					// unitario (" + SoftureUtil.formatMoney(detail.getValorUnitario()) +") =" +
					// SoftureUtil.formatMoney(detail.getCantidad().multiply(detail.getValorUnitario())));
				} else {
					detail.setValorUnitario(
							detail.getValorTotal().divide(detail.getCantidad(), 6, RoundingMode.CEILING));
				}
			}
		}
	}

	public void actualizarCamposNovedadParcial(DetallePedidoVentaDTO detail) throws ServerException {

		PropiedadDTO pCampoCantidad = Propiedades.obtenerParametro(detail, Propiedades.PRODUCTO_CAMPO_CANTIDAD);
		String keyCampoCantidad = "***CANTIDAD";
		if (pCampoCantidad != null)
			keyCampoCantidad = pCampoCantidad.getValor();
		PedidoVentaCaracteristicaDTO cpCantidad = CallDocumentCommons.obtenerValor(detail.getDocumentoDetalle().getCaracteristicas(),
				keyCampoCantidad);
		cpCantidad.setValorNumero(detail.getCantidad());

		PropiedadDTO pCampoUnitario = Propiedades.obtenerParametro(detail, Propiedades.PRODUCTO_CAMPO_VALOR_UNITARIO);
		String keyCampoUnitario = "***UNIDAD";
		if (pCampoUnitario != null)
			keyCampoUnitario = pCampoUnitario.getValor();
		PedidoVentaCaracteristicaDTO cpUnitario = CallDocumentCommons.obtenerValor(detail.getDocumentoDetalle().getCaracteristicas(),
				keyCampoUnitario);
		if (cpUnitario != null)
			cpUnitario.setValorNumero(detail.getValorUnitario());// Cuando no tiene tarifario no van estos campos,
																	// deberia validar que si sean ciertos

		PropiedadDTO pCampoTotal = Propiedades.obtenerParametro(detail, Propiedades.PRODUCTO_CAMPO_TOTAL);
		String keyCampoTotal = "***TOTAL";
		if (pCampoTotal != null)
			keyCampoTotal = pCampoTotal.getValor();
		PedidoVentaCaracteristicaDTO cpTotal = CallDocumentCommons.obtenerValor(detail.getDocumentoDetalle().getCaracteristicas(),
				keyCampoTotal);
		if (cpTotal != null)
			cpTotal.setValorNumero(detail.getValorTotal());// Cuando no tiene tarifario no van estos campos, deberia
															// validar que si sean ciertos

	}

	public List<ProductoDTO> getCompleteDetailFromProductId(String productId, String token) throws ServerException {
		DetallePedidoVentaDTO result = new DetallePedidoVentaDTO();
		result.setProducto(productId);
		List<ProductoDTO> productos = new ArrayList<>();
		ProductoDTO productFilter = new ProductoDTO();
		productFilter.setLlaveTabla(productId);
		productos.add(productFilter);
		productos = simplificarConsultaBDProductos(productos);
		for (ProductoDTO productoDTO : productos) {
			DetallePedidoVentaDTO filtroPlantilla = new DetallePedidoVentaDTO();
			filtroPlantilla.setProducto(productoDTO.getLlaveTabla());
			productoDTO
					.setDetallePlantilla(consultaCompleta(filtroPlantilla, null, null, null, null, productos, token, null));
		}
		return productos;
	}

	// END region aditionalMethods

}