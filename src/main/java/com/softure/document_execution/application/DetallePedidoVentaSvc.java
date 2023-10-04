package com.softure.document_execution.application;

import java.util.List;

// BEGIN region interImport
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.softure.authorization.application.UsuarioRolProductoSvc;
import com.softure.authorization.domain.UsuarioRolProductoDTO;
import com.softure.authorization.domain.UsuarioRolProductoFilterDTO;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.DetallePedidoVentaDTO;
import com.softure.document_execution.domain.DetallePedidoVentaFilterDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.infrastructure.DetallePedidoVentaMapper;
import com.softure.inventory.application.DetalleCaracteristicaProductoSvc;
import com.softure.inventory.application.ProductoCaracteristicaSvc;
import com.softure.inventory.application.ProductoSvc;
import com.softure.inventory.domain.DetalleCaracteristicaProductoDTO;
import com.softure.inventory.domain.DetalleCaracteristicaProductoFilterDTO;
import com.softure.inventory.domain.ProductoCaracteristicaDTO;
import com.softure.inventory.domain.ProductoDTO;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
// END region interImport
import com.softure.tariff.application.TarifaSvc;
import com.softure.tariff.domain.TarifaDTO;
import com.softure.tariff.domain.TarifaFilterDTO;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;

@Service("detallePedidoVentaService")
public class DetallePedidoVentaSvc extends BasicSvc<DetallePedidoVentaDTO, DetallePedidoVentaFilterDTO> {

	@Autowired
	private DetallePedidoVentaMapper detallePedidoVentaMapper;

	// BEGIN region servicesDetallePedidoVenta
	@Autowired
	private DetalleCaracteristicaProductoSvc detalleCaracteristicaProductoService;
	@Autowired
	private ProductoCaracteristicaSvc productoCaracteristicaService;
	@Autowired
	private ProductoSvc productoService;
	@Autowired
	private PedidoVentaSvc documentoService;
	@Autowired
	private UsuarioRolProductoSvc usuarioRolProductoService;
	@Autowired
	private TarifaSvc tarifaService;
	@Autowired
	private PropiedadSvc configuracionSvc;

	// END region servicesDetallePedidoVenta

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
		// BEGIN DetallePedidoVenta_actualizar
		if (dto.getCaracteristicas() != null && dto.getCaracteristicas().size() != 0) {
			for (PedidoVentaCaracteristicaDTO pvc : dto.getCaracteristicas()) {
				formatValidarCampo(pvc, dto);
				DetalleCaracteristicaProductoDTO caracteristica = new DetalleCaracteristicaProductoDTO();
				caracteristica.setCampo(pvc.getCampo());
				caracteristica.setEntidad(dto.getLlaveTabla());
				caracteristica.setEstado(pvc.getEstado());
				caracteristica.setTransaccionRegistro(pvc.getTransaccionRegistro());
				caracteristica.setValorFecha(pvc.getValorFecha());
				caracteristica.setValorNumero(pvc.getValorNumero());
				caracteristica.setLlaveTabla(pvc.getLlaveTabla());
				caracteristica.setValorOpcion(pvc.getValorOpcion());
				caracteristica.setValorText(pvc.getValorText());
				detalleCaracteristicaProductoService.actualizar(caracteristica, token);
			}
		}
		dto = update(dto);
		return dto;
		// END DetallePedidoVenta_actualizar
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DetallePedidoVentaDTO inactivar(DetallePedidoVentaDTO dto, String token) throws ServerException {
		// BEGIN DetallePedidoVenta_inactivar
		if (dto.getTransaccionInactivo() == null)
			throw new ServerException("Ingrese la transaccion de inactivar");
		DetallePedidoVentaDTO bd = consultaXId(dto.getLlaveTabla());
		bd.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
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
		// BEGIN DetallePedidoVenta_guardar
		List<PedidoVentaCaracteristicaDTO> caracteristicas = dto.getCaracteristicas();
		// if(dto.getCantidad().multiply(dto.getValorUnitario()).add(dto.getValorTotal().negate()).abs().longValue()
		// > 1)
		// throw new ServerException("El valor total (" +
		// SoftureUtil.formatMoney(dto.getValorTotal()) +") no concuerda con la cantidad
		// (" + SoftureUtil.formatNumber(dto.getCantidad()) +") x valor unitario (" +
		// SoftureUtil.formatMoney(dto.getValorUnitario()) +") =" +
		// SoftureUtil.formatMoney(dto.getCantidad().multiply(dto.getValorUnitario())));
		dto = save(dto);
		dto.setCaracteristicas(caracteristicas);
		if (dto.getCaracteristicas() != null && dto.getCaracteristicas().size() != 0) {
			for (int i = 0; i < dto.getCaracteristicas().size(); i++) {
				PedidoVentaCaracteristicaDTO pvc = dto.getCaracteristicas().get(i);
				if (!pvc.getCampo().startsWith("***")) {
					if (pvc.getCampoDTO() == null) {
						DocumentoPlantillaCaracteristicaDTO newP = new DocumentoPlantillaCaracteristicaDTO();
						newP.setFormato(productoCaracteristicaService.consultaXId(pvc.getCampo()).getFormato());
						pvc.setCampoDTO(newP);
					}
					formatValidarCampo(pvc, dto);
					DetalleCaracteristicaProductoDTO caracteristica = new DetalleCaracteristicaProductoDTO();
					// Copiado de actualizar, si cambio algo cambio en guardar
					caracteristica.setCampo(pvc.getCampo());
					caracteristica.setEntidad(dto.getLlaveTabla());
					caracteristica.setEstado(pvc.getEstado());
					caracteristica.setTransaccionRegistro(dto.getTransaccionRegistro());
					caracteristica.setValorFecha(pvc.getValorFecha());
					caracteristica.setValorNumero(pvc.getValorNumero());
					caracteristica.setValorOpcion(pvc.getValorOpcion());
					caracteristica.setValorText(pvc.getValorText());
					caracteristica = detalleCaracteristicaProductoService.guardar(caracteristica, token);
				}
			}
		}
		return dto;
		// END DetallePedidoVenta_guardar
	}

// BEGIN region aditionalMethods
	public List<DetallePedidoVentaDTO> listarCompleto(String documento, List<PropiedadDTO> tarifario,
			String propiedadFuncion, List<PedidoVentaCaracteristicaDTO> parametrosFuncionTarifario, String token)
			throws ServerException {
		List<DetallePedidoVentaDTO> base = detallePedidoVentaMapper.listar2Documento(documento);
		if (base == null || base.isEmpty())
			return new ArrayList<DetallePedidoVentaDTO>();
		List<DetallePedidoVentaDTO> result = new ArrayList<DetallePedidoVentaDTO>();
		List<ProductoDTO> productosSimplificados = new ArrayList<ProductoDTO>();
		for (DetallePedidoVentaDTO iDetalle : base) {
			ProductoDTO iProducto = new ProductoDTO();
			iProducto.setLlaveTabla(iDetalle.getProducto());
			// iProducto.setSecurityToken(null);
			productosSimplificados.add(iProducto);
		}
		productosSimplificados = simplificarConsultaBDProductos(productosSimplificados);
		for (DetallePedidoVentaDTO detallePedidoVentaDTO : base) {
			result.add(consultaCompleta(detallePedidoVentaDTO, tarifario, null, propiedadFuncion,
					parametrosFuncionTarifario, productosSimplificados, token));
		}
		return result;
	}

	public List<DetallePedidoVentaDTO> listar2Documento(String documento) throws ServerException {
		return detallePedidoVentaMapper.listar2Documento(documento);
	}

	private void formatValidarCampo(PedidoVentaCaracteristicaDTO dto, DetallePedidoVentaDTO campo)
			throws ServerException {
		switch (dto.getCampoDTO().getFormato()) {
		case DocumentoPlantillaCaracteristicaDTO.FECHA:
			if (dto.getValorFecha() != null) {
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				dto.setValorText(format.format(dto.getValorFecha()));
			} else {
				if (Propiedades.obtenerParametro(dto.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL) == null)
					throw new ServerException("Falta la opcion " + dto.getCampoDTO().getNombre() + " del producto "
							+ campo.getNombre() + ". Cant: " + campo.getCantidad());
			}
			break;
		case DocumentoPlantillaCaracteristicaDTO.NUMERO:
			if (dto.getValorNumero() != null)
				dto.setValorText(dto.getValorNumero().toPlainString());// Se presentaba un error al modificar una guia
																		// en universal
			break;
		case DocumentoPlantillaCaracteristicaDTO.PROCESO:
			if (dto.getValorOpcion() != null) {
				PedidoVentaDTO documento = documentoService.consultaXId(dto.getValorOpcion());
				if (documento == null)
					throw new ServerException("La opcion escogida no esta correctamente configurada." + dto.getCampo());

				if (documento.getDescripcion() != null) {
					dto.setValorText(documento.getDescripcion());
				} else {
					dto.setValorText(documento.getNombre());
				}
			} else {
				if (Propiedades.obtenerParametro(dto.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL) == null)
					throw new ServerException("Falta la opcion " + dto.getCampoDTO().getNombre() + " del producto "
							+ campo.getNombre() + ". Cant: " + campo.getCantidad());
			}
			break;
		case DocumentoPlantillaCaracteristicaDTO.PRODUCTO_LISTA:
		case DocumentoPlantillaCaracteristicaDTO.TEXTO:
			/*if (dto.getValorText() != null) {
				if (Propiedades.obtenerParametro(dto.getCampoDTO(), Propiedades.TEXTO_LARGO) == null)
					dto.setValorText(dto.getValorText().toUpperCase());
			}*/
			break;
		}
	}

	public List<ProductoDTO> simplificarConsultaBDProductos(List<ProductoDTO> productos) throws ServerException {
		List<ProductoDTO> result = productoService.listarProductoSimplificar(productos);
		List<ProductoDTO> bases = new ArrayList<ProductoDTO>();
		for (ProductoDTO productoDTO : result) {
			if (productoDTO.getProductoBase() != null) {
				ProductoDTO iBase = new ProductoDTO();
				iBase.setLlaveTabla(productoDTO.getProductoBase());
				bases.add(iBase);
			}
		}
		if (bases != null && bases.size() != 0) {
			List<PropiedadDTO> propiedadesBases = configuracionSvc.listarProductoSimplificar(bases);
			List<ProductoCaracteristicaDTO> camposBases = productoCaracteristicaService
					.listarProductoSimplificar(bases);
			for (ProductoDTO iProductoDTO : bases) {
				iProductoDTO.setPropiedades(new ArrayList<PropiedadDTO>());
				if (propiedadesBases != null && !propiedadesBases.isEmpty()) {
					for (PropiedadDTO propiedadDTO : propiedadesBases) {
						if (propiedadDTO.getCampo().compareTo(iProductoDTO.getLlaveTabla()) == 0)
							iProductoDTO.getPropiedades().add(propiedadDTO);
					}
				}
				iProductoDTO.setCampos(new ArrayList<ProductoCaracteristicaDTO>());
				if (camposBases != null && !camposBases.isEmpty()) {
					for (ProductoCaracteristicaDTO campoDTO : camposBases) {
						if (campoDTO.getBase().compareTo(iProductoDTO.getLlaveTabla()) == 0)
							iProductoDTO.getCampos().add(campoDTO);
					}
				}
			}
		}
		List<PropiedadDTO> propiedadesProducto = configuracionSvc.listarProductoSimplificar(productos);
		List<ProductoCaracteristicaDTO> camposProducto = productoCaracteristicaService
				.listarProductoSimplificar(productos);
		for (ProductoDTO productoDTO : result) {
			productoDTO.setPropiedades(new ArrayList<PropiedadDTO>());
			if (propiedadesProducto != null && !propiedadesProducto.isEmpty()) {
				for (PropiedadDTO propiedadDTO : propiedadesProducto) {
					if (propiedadDTO.getCampo().compareTo(productoDTO.getLlaveTabla()) == 0)
						productoDTO.getPropiedades().add(propiedadDTO);
				}
			}
			if (productoDTO.getPropiedades().isEmpty() && productoDTO.getProductoBase() != null) {
				for (ProductoDTO iBase : bases) {
					if (productoDTO.getProductoBase().compareTo(iBase.getLlaveTabla()) == 0) {
						productoDTO.setPropiedades(iBase.getPropiedades());
						break;
					}
				}
			}
			productoDTO.setCampos(new ArrayList<ProductoCaracteristicaDTO>());
			if (camposProducto != null && !camposProducto.isEmpty()) {
				for (ProductoCaracteristicaDTO campoDTO : camposProducto) {
					if (campoDTO.getBase().compareTo(productoDTO.getLlaveTabla()) == 0)
						productoDTO.getCampos().add(campoDTO);
				}
			}
			if (productoDTO.getCampos().isEmpty() && productoDTO.getProductoBase() != null) {
				for (ProductoDTO iBase : bases) {
					if (productoDTO.getProductoBase().compareTo(iBase.getLlaveTabla()) == 0) {
						productoDTO.setCampos(iBase.getCampos());
						break;
					}
				}
			}
		}
		return result;
	}

	public DetallePedidoVentaDTO consultaCompleta(DetallePedidoVentaDTO dto, List<PropiedadDTO> tarifario,
			String tercero, String propiedadFuncionTarifario,
			List<PedidoVentaCaracteristicaDTO> parametrosFuncionTarifario, List<ProductoDTO> productosSimplificados,
			String token) throws ServerException {
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
				fPromocion.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
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
			// result.setNombre(filter.getProductoNombre());
			result.setValorMaximo(filter.getValorMaximo());
			result.setValorMinimo(filter.getValorMinimo());
		}

		if (result.getCantidad() == null)
			result.setCantidad(BigDecimal.ZERO);
		if (result.getValorUnitario() == null)
			result.setValorUnitario(BigDecimal.ZERO);
		result.setCaracteristicas(new ArrayList<PedidoVentaCaracteristicaDTO>());
		// Consulto las caracteristicas
		List<ProductoCaracteristicaDTO> caracteristicas = producto.getCampos();
		if (caracteristicas != null && !caracteristicas.isEmpty()) {
			List<DetalleCaracteristicaProductoDTO> dcpList = null;

			if (dto.getLlaveTabla() != null) {
				DetalleCaracteristicaProductoFilterDTO filtro = new DetalleCaracteristicaProductoFilterDTO();
				filtro.setEntidad(dto.getLlaveTabla());
				filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
				dcpList = detalleCaracteristicaProductoService.listarConsulta(filtro);
			}
			for (ProductoCaracteristicaDTO productoCaracteristicaDTO : caracteristicas) {
				PedidoVentaCaracteristicaDTO nueva = null;
				if (dcpList != null && !dcpList.isEmpty()) {
					for (DetalleCaracteristicaProductoDTO dcpDTO : dcpList) {
						if (dcpDTO.getCampo().compareTo(productoCaracteristicaDTO.getLlaveTabla()) == 0) {
							nueva = new PedidoVentaCaracteristicaDTO();
							nueva.setDocumento(dcpDTO.getEntidad());
							nueva.setEstado(dcpDTO.getEstado());
							nueva.setTransaccionRegistro(dto.getTransaccionRegistro());
							nueva.setValorFecha(dcpDTO.getValorFecha());
							nueva.setValorNumero(dcpDTO.getValorNumero());
							nueva.setValorOpcion(dcpDTO.getValorOpcion());
							nueva.setValorText(dcpDTO.getValorText());
							nueva.setLlaveTabla(dcpDTO.getLlaveTabla());
							break;
						}
					}
				}
				DocumentoPlantillaCaracteristicaDTO nuevaCaracteristica = new DocumentoPlantillaCaracteristicaDTO();
				nuevaCaracteristica.setCodigo(productoCaracteristicaDTO.getCodigo());
				nuevaCaracteristica.setFormato(productoCaracteristicaDTO.getFormato());
				nuevaCaracteristica.setNombre(productoCaracteristicaDTO.getNombre());
				nuevaCaracteristica.setOrden(productoCaracteristicaDTO.getOrden() + 4);// Por los cuatro parametros
																						// inciales
				nuevaCaracteristica.setDocumentos(productoCaracteristicaDTO.getCaracteristicas());
				nuevaCaracteristica.setLlaveTabla(productoCaracteristicaDTO.getLlaveTabla());
				// nuevaCaracteristica.setSecurityToken(productoCaracteristicaDTO.getDocumentoAuxiliar());

				// Propiedades.obtenerParametro(dto.getCampoDTO(),
				// Propiedades.PERMISO_CAMPO_OBLIGATORIO)!=null

				List<PropiedadDTO> parametros = configuracionSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.CAMPO,
						productoCaracteristicaDTO.getLlaveTabla(), null, getUserFlex(token));

				parametros.add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, null,
						Propiedades.PERMISO_CAMPO_MODIFICABLE, Propiedades.TRUE, null));
				nuevaCaracteristica.setPropiedades(parametros);
				if (productoCaracteristicaDTO.getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.NUMERO) == 0) {
					if (Propiedades.obtenerParametro(nuevaCaracteristica, Propiedades.NUMERO_FUNCION_SQL) != null)
						nuevaCaracteristica.getPropiedades().add(Propiedades.crearParametro(
								PropiedadValorDefinidoDTO.CAMPO, null, Propiedades.DEPENDE, "***PRODUCTO", null));
					if (Propiedades.obtenerParametro(nuevaCaracteristica, Propiedades.NUMERO_FORMULA) != null)
						nuevaCaracteristica.getPropiedades().add(Propiedades.crearParametro(
								PropiedadValorDefinidoDTO.CAMPO, null, Propiedades.DEPENDE, "***TOTAL", null));
				}

				if (nueva == null)
					nueva = new PedidoVentaCaracteristicaDTO();
				nuevaCaracteristica.setPlantilla(dto.getPlantilla());
				nueva.setCampoDTO(nuevaCaracteristica);
				nueva.setCampo(nuevaCaracteristica.getLlaveTabla());
				result.getCaracteristicas().add(nueva);
			}
		}
		createFieldsProduct(result);
		return result;
	}

	public void createFieldsProduct(DetallePedidoVentaDTO field) throws ServerException {
		if (field.getCaracteristicas() == null)
			field.setCaracteristicas(new ArrayList<PedidoVentaCaracteristicaDTO>());
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
				// baseValorUnitario.getPropiedades().add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				// null, Propiedades.PERMISO_CAMPO_MODIFICABLE, Propiedades.TRUE, null));
				baseValorUnitario.getPropiedades().add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, null,
						Propiedades.NUMERO_MONEDA, Propiedades.TRUE, null));
				PedidoVentaCaracteristicaDTO cpValorUnitario = new PedidoVentaCaracteristicaDTO();
				cpValorUnitario.setDocumento(field.getLlaveTabla());
				cpValorUnitario.setValorNumero(valorUnitario);
				cpValorUnitario.setValorText(cpValorUnitario.getValorNumero().toString());
				cpValorUnitario.setCampoDTO(baseValorUnitario);
				cpValorUnitario.setCampo("***UNIDAD");
				field.getCaracteristicas().add(0, cpValorUnitario);
				field.getPropiedades().add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, null,
						Propiedades.PRODUCTO_CAMPO_VALOR_UNITARIO, baseValorUnitario.getLlaveTabla(), null));
			} else {
				for (PedidoVentaCaracteristicaDTO iFieldValorUnitario : field.getCaracteristicas()) {
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
			field.getCaracteristicas().add(0, cpCantidad);
			field.getPropiedades().add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, null,
					Propiedades.PRODUCTO_CAMPO_CANTIDAD, baseCantidad.getLlaveTabla(), null));
			//
		} else {
			for (PedidoVentaCaracteristicaDTO iFieldCantidad : field.getCaracteristicas()) {
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
		field.getCaracteristicas().add(0, cpProducto);

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
				field.getCaracteristicas().add(cpTotal);
				field.getPropiedades().add(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, null,
						Propiedades.PRODUCTO_CAMPO_TOTAL, baseTotal.getLlaveTabla(), null));
				//
			} else {
				for (PedidoVentaCaracteristicaDTO iFieldTotal : field.getCaracteristicas()) {
					if (iFieldTotal.getCampo().compareTo(pCampoTotal.getValor()) == 0) {
						baseTotal = iFieldTotal.getCampoDTO();
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
		if (detail.getCaracteristicas() == null || detail.getCaracteristicas().isEmpty())
			throw new ServerException("Ahora debes traer los campos basicos");
		if (detail.getPropiedades() == null)
			throw new ServerException("Por favor envia las propiedades");

		PropiedadDTO pCampoCantidad = Propiedades.obtenerParametro(detail, Propiedades.PRODUCTO_CAMPO_CANTIDAD);
		String keyCampoCantidad = "***CANTIDAD";
		if (pCampoCantidad != null)
			keyCampoCantidad = pCampoCantidad.getValor();
		PedidoVentaCaracteristicaDTO cpCantidad = CallDocumentCommons.obtenerValor(detail.getCaracteristicas(),
				keyCampoCantidad);
		if (cpCantidad != null && cpCantidad.getValorNumero() != null)
			detail.setCantidad(cpCantidad.getValorNumero());// Al modificar no se actualizan estos campos

		PropiedadDTO pCampoUnitario = Propiedades.obtenerParametro(detail, Propiedades.PRODUCTO_CAMPO_VALOR_UNITARIO);
		String keyCampoUnitario = "***UNIDAD";
		if (pCampoUnitario != null)
			keyCampoUnitario = pCampoUnitario.getValor();
		PedidoVentaCaracteristicaDTO cpUnitario = CallDocumentCommons.obtenerValor(detail.getCaracteristicas(),
				keyCampoUnitario);
		if (cpUnitario != null && cpUnitario.getValorNumero() != null)
			detail.setValorUnitario(cpUnitario.getValorNumero());// Cuando no tiene tarifario no van estos campos,
																	// deberia validar que si sean ciertos

		PropiedadDTO pCampoTotal = Propiedades.obtenerParametro(detail, Propiedades.PRODUCTO_CAMPO_TOTAL);
		String keyCampoTotal = "***TOTAL";
		if (pCampoTotal != null)
			keyCampoTotal = pCampoTotal.getValor();
		PedidoVentaCaracteristicaDTO cpTotal = CallDocumentCommons.obtenerValor(detail.getCaracteristicas(),
				keyCampoTotal);
		// Sucede que en Universal el total no es igual al producto normal se hace pro
		// otra formula
		if (cpTotal != null && cpUnitario.getValorNumero() != null) {
			detail.setValorTotal(cpTotal.getValorNumero());// Cuando no tiene tarifario no van estos campos, deberia
															// validar que si sean ciertos
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
		PedidoVentaCaracteristicaDTO cpCantidad = CallDocumentCommons.obtenerValor(detail.getCaracteristicas(),
				keyCampoCantidad);
		cpCantidad.setValorNumero(detail.getCantidad());

		PropiedadDTO pCampoUnitario = Propiedades.obtenerParametro(detail, Propiedades.PRODUCTO_CAMPO_VALOR_UNITARIO);
		String keyCampoUnitario = "***UNIDAD";
		if (pCampoUnitario != null)
			keyCampoUnitario = pCampoUnitario.getValor();
		PedidoVentaCaracteristicaDTO cpUnitario = CallDocumentCommons.obtenerValor(detail.getCaracteristicas(),
				keyCampoUnitario);
		if (cpUnitario != null)
			cpUnitario.setValorNumero(detail.getValorUnitario());// Cuando no tiene tarifario no van estos campos,
																	// deberia validar que si sean ciertos

		PropiedadDTO pCampoTotal = Propiedades.obtenerParametro(detail, Propiedades.PRODUCTO_CAMPO_TOTAL);
		String keyCampoTotal = "***TOTAL";
		if (pCampoTotal != null)
			keyCampoTotal = pCampoTotal.getValor();
		PedidoVentaCaracteristicaDTO cpTotal = CallDocumentCommons.obtenerValor(detail.getCaracteristicas(),
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
					.setDetallePlantilla(consultaCompleta(filtroPlantilla, null, null, null, null, productos, token));
		}
		return productos;
	}

	// END region aditionalMethods

}