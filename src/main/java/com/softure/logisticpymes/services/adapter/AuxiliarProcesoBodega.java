package com.softure.logisticpymes.services.adapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.BodegaDTO;
import com.softure.logisticpymes.dto.DeduccionProductoDTO;
import com.softure.logisticpymes.dto.DetallePedidoVentaDTO;
import com.softure.logisticpymes.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.dto.PedidoVentaDTO;
import com.softure.logisticpymes.dto.ProductoDTO;
import com.softure.logisticpymes.dto.ProductoInventarioDTO;
import com.softure.logisticpymes.dto.ProductoInventarioDescuentoDTO;
import com.softure.logisticpymes.dto.PropiedadDTO;
import com.softure.logisticpymes.dto.RelacionInternaDTO;
import com.softure.logisticpymes.dto.filter.BodegaFilterDTO;
import com.softure.logisticpymes.dto.filter.ProductoInventarioDescuentoFilterDTO;
import com.softure.logisticpymes.dto.filter.ProductoInventarioFilterDTO;
import com.softure.logisticpymes.services.BodegaSvc;
import com.softure.logisticpymes.services.DeduccionProductoSvc;
import com.softure.logisticpymes.services.DocumentoPlantillaCaracteristicaSvc;
import com.softure.logisticpymes.services.PedidoVentaSvc;
import com.softure.logisticpymes.services.ProductoInventarioDescuentoSvc;
import com.softure.logisticpymes.services.ProductoInventarioSvc;
import com.softure.logisticpymes.services.ProductoSvc;
import com.softure.logisticpymes.services.RelacionInternaSvc;
import com.softure.logisticpymes.services.refactor.CallListDocumentWithFilters;

@Component
public class AuxiliarProcesoBodega {

	@Autowired private BodegaSvc bodegaService;
	@Autowired private DeduccionProductoSvc deduccionProductoService;
	@Autowired private DocumentoPlantillaCaracteristicaSvc caracteristicaService;
	@Autowired private PedidoVentaSvc pedidoService;
	@Autowired private CallListDocumentWithFilters listDocumentWithFiltersFunction;
	@Autowired private ProductoSvc productoService;
	@Autowired private ProductoInventarioSvc productoInventarioService;
	@Autowired private ProductoInventarioDescuentoSvc productoInventarioDescuentoService;
	@Autowired private RelacionInternaSvc relacionService;
		
	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String bodega) throws ServerException{
		BodegaDTO bodegaDTO =  bodegaService.consultaXId(bodega);
		if(bodegaDTO==null) throw new ServerException("Revise el id de la bodega");
		pCampo.setValorText(bodegaDTO.getNombre());
		pCampo.setValorAuxiliar(bodegaDTO.getLlaveTabla());
		pCampo.setValorOpcion(bodegaDTO.getDocumento());
	}
	
	public void consultarBodegaDesdeDocumento(PedidoVentaCaracteristicaDTO pCampo) throws ServerException{
		BodegaFilterDTO bodegaFilterDTO = new BodegaFilterDTO();
		bodegaFilterDTO.setDocumento(pCampo.getValorOpcion());
		BodegaDTO bodegaDTO =  bodegaService.consultaUnica(bodegaFilterDTO);
		if(bodegaDTO==null) throw new ServerException("Revise el id de la bodega");
		pCampo.setValorText(bodegaDTO.getNombre());
		pCampo.setValorAuxiliar(bodegaDTO.getLlaveTabla());
		pCampo.setValorOpcion(bodegaDTO.getDocumento());
	}
	
	public String consultarBodegaBaseFija(String bodegaFijaId) throws ServerException{
		BodegaDTO bodegaDTO =  bodegaService.consultaXId(bodegaFijaId);
		if(bodegaDTO==null) throw new ServerException("Revise el id de la bodega");
		if(bodegaDTO.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0) throw new ServerException("La bodega "+bodegaDTO.getNombre()+" no tiene estado activo.");
		return bodegaDTO.getDocumento();
	}
	/*
	public PedidoVentaCaracteristicaDTO guardarCampo(PedidoVentaCaracteristicaDTO pCampo) throws ServerException{
		String bodega = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.BODEGA_FIJA);
		if(bodega.isEmpty()){
			PedidoVentaCaracteristicaDTO bd = campoService.buscarActivo(pCampo);
			if(bd!=null){
				bd.setSecurityToken(pCampo.getSecurityToken());
				if(pCampo.getValorOpcion()==null){
					bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
					campoService.inactivar(bd);
					return pCampo;
				}else{
					if(pCampo.getValorOpcion().compareTo(bd.getValorOpcion())==0){
						return pCampo;
					}else{
						bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
						campoService.inactivar(bd);
					}
				}
			}
		}
		if(pCampo.getValorOpcion()==null){
			return pCampo;
		}else{
			gestionarInventario(validarInventario(pCampo), pCampo.getSecurityToken());
			if(bodega.isEmpty()){
				pCampo = campoService.guardar(pCampo);
			}
			return pCampo;
		}
	}*/

	/*
	public PedidoVentaCaracteristicaDTO consultarDatosBase(PedidoVentaCaracteristicaDTO pCampo) throws ServerException {
		DocumentoPlantillaCaracteristicaDTO pBase = caracteristicaService.consultaUnicaConComplementos(pCampo.getCampo(), pCampo.getSecurityToken());
		BodegaDTO bodega = new BodegaDTO();
		bodega.setFiltroParametro(pCampo.getFiltroParametro());
		bodega.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		List<BodegaDTO> bodegas = bodegaService.listarConsulta(bodega);
		List<PedidoVentaDTO> resultados = new ArrayList<PedidoVentaDTO>();
		if(bodegas!=null && !bodegas.isEmpty()){
			for(BodegaDTO iBodega : bodegas){
				PedidoVentaDTO adaptado = cloneBodega(iBodega);
				resultados.add(adaptado);
			}
		}
		if(pBase!=null){
			pBase.setDocumentos(resultados);
			pCampo.setCampoDTO(pBase);
		}else{//Esto aplica para autoload de los productos con ocion de seleccion
			pCampo.getCampoDTO().setDocumentos(resultados);
		}
		return pCampo;
	}*/
	/*
	private PedidoVentaDTO cloneBodega(BodegaDTO bodega){
		PedidoVentaDTO adaptado = new PedidoVentaDTO();
		adaptado.setLlaveTabla(bodega.getLlaveTabla());
		adaptado.setNombre(bodega.getCodigo());
		adaptado.setDescripcion(bodega.getNombre());
		return adaptado;
	}*/
	
	public void aplicarMovimientosBodega(PedidoVentaCaracteristicaDTO pCampo, String token)throws ServerException {
		gestionarInventario(validarInventario(pCampo, token), token);
	}
	
	public List<DeduccionProductoDTO> validarInventario(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		
		if(pCampo.getValorAuxiliar()==null) throw new ServerException("Por favor revise la configuracion de inventarios");
		List<DeduccionProductoDTO> result = null;
		
		List<PropiedadDTO> movimientos = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(), Propiedades.BODEGA_MOVIMIENTO);
		if(movimientos== null) throw new ServerException("Revise la configuracion de la bodega ya que no tiene movimientos. Campo: " + pCampo.getCampoDTO().getNombre());;
		for (PropiedadDTO iParam : movimientos) {
			if(iParam.getTexto()==null) throw new ServerException("El texto de la propiedad movimiento de bodega debe tener el codigo del campo que vamos a gestionar. Campo: " + pCampo.getCampoDTO().getNombre());
			PedidoVentaCaracteristicaDTO dependiente = null;
			for (PedidoVentaCaracteristicaDTO iDep : pCampo.getDependientes()) {
				if(iDep.getCampoDTO().getCodigo().compareTo(iParam.getTexto())==0) {
					dependiente = iDep;
					break;
				}
			}
			if(dependiente==null) throw new ServerException("El campo bodega no logra obtener el campo dependiente." + iParam.getTexto());
			
			List<DeduccionProductoDTO> acumulado = inventarioDirecto(dependiente, iParam.getValor(), 
					pCampo.getValorAuxiliar(), pCampo.getDocumento(), relacionService.relacionesPropiedad(iParam.getLlaveTabla()), token);
			
			if(acumulado!=null && !acumulado.isEmpty()){
				if(result ==null){
					result = new ArrayList<DeduccionProductoDTO>();
					result.addAll(acumulado);
				}else{
					for(DeduccionProductoDTO iDeduccion : acumulado){
						result = adicionarDeduccion(result, iDeduccion);
					}
				}
			}
			
		}		
		return result;
	}
	
	
	public List<DeduccionProductoDTO> inventarioDirecto(
			PedidoVentaCaracteristicaDTO pCampo, 
			String operacion, 
			String recursoInventario, 
			String documentoInicial, 
			List<RelacionInternaDTO> relaciones,
			String token) throws ServerException {
		if(pCampo.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.PRODUCTO)==0){
			return inventariarDetalle(pCampo, operacion, recursoInventario, documentoInicial, token);
		}else{
			if(pCampo.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.PROCESO)==0){
				if(relaciones==null || relaciones.isEmpty()) throw new ServerException("Coloca el camino de profundidad de consulta de inventario en las relaciones de la propiedad"
						+ "\nPlantilla: " + pCampo.getCampoDTO().getPlantillaNombre()
						+ "\nCampo: " + pCampo.getCampoDTO().getNombre());
				return inventariarProceso(pCampo, operacion, recursoInventario, documentoInicial, relaciones, token);
			}else{
				if(pCampo.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.NUMERO)==0){
					List<PropiedadDTO> codigoDepende = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(), Propiedades.DEPENDE);
					if(codigoDepende!=null) {
						if(pCampo.getDependientes()==null || pCampo.getDependientes().isEmpty())
							 throw new ServerException("Por favor revise la configuracion del dependiente de tipo numero " + pCampo.getCampoDTO().getNombre());
						List<DeduccionProductoDTO> acumulado = inventarioDirecto(pCampo.getDependientes().get(0), operacion, recursoInventario, documentoInicial, relaciones, token);
						if(acumulado!=null && !acumulado.isEmpty()){
							for (DeduccionProductoDTO iDeduccion: acumulado){
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
	
	private void gestionarInventario(List<DeduccionProductoDTO> deduccionesFinales, String securityToken) throws ServerException {
		if(deduccionesFinales!=null && !deduccionesFinales.isEmpty()){
			for(DeduccionProductoDTO deduccion : deduccionesFinales){
				if(deduccion.getCantidad().compareTo(BigDecimal.ZERO)!=0){
					deduccion = deduccionProductoService.guardar(deduccion, securityToken);
				}
			}
		}
	}
	
	public List<DeduccionProductoDTO> inventariarProceso(
			PedidoVentaCaracteristicaDTO pCampo, 
			String operacion, 
			String recursoInventario, 
			String documentoInicial,
			List<RelacionInternaDTO> relaciones,
			String token) throws ServerException {
		if(pCampo.getExpedientes()!=null && !pCampo.getExpedientes().isEmpty()){
			List<DeduccionProductoDTO> result = new ArrayList<DeduccionProductoDTO>();
			List<DeduccionProductoDTO> acumulado = null;
			
			for(PedidoVentaDTO expediente: pCampo.getExpedientes()){
				//El tipo proceso cuando gestiona me lo envia vacio
				expediente = pedidoService.obtenerCamposCompletos(expediente, token);
				for(PedidoVentaCaracteristicaDTO campoExpediente : expediente.getCaracteristicas()){
					for(RelacionInternaDTO rit : relaciones) {
						if(campoExpediente.getCampo().compareTo(rit.getCampo())==0) {
							System.out.format("\n[%s (%s) - %s] Inventario anidado de documento operacion(%s) iniciando en campo interno ( %s )", pCampo.getCampoDTO().getPlantillaNombre(), expediente.getNombre(), pCampo.getCampoDTO().getNombre(), operacion, campoExpediente.getCampoDTO().getNombre());
							// La siguiente linea es redundante ya que lo consulte el campo completo //campo.setCampoDTO(caracteristicaService.consultaXId(campo.getCampo()));
							if(campoExpediente.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.PROCESO)==0 
									&& campoExpediente.getLlaveTabla()!=null){
								campoExpediente.setCampoDTO(caracteristicaService.cargarComplementos(campoExpediente.getCampoDTO(), token));
								if(Propiedades.obtenerParametro(campoExpediente.getCampoDTO(), Propiedades.MULTIPLE)==null){
									campoExpediente.setExpedientes( new ArrayList<PedidoVentaDTO>() );
									campoExpediente.getExpedientes().add(pedidoService.consultaXId(campoExpediente.getValorOpcion()));
								}else {
									campoExpediente.setExpedientes( listDocumentWithFiltersFunction.listarExpedientesPertenecenCampo(campoExpediente.getLlaveTabla(), token, null));														
								}
							}
							acumulado = inventarioDirecto(campoExpediente, operacion, recursoInventario, documentoInicial, relaciones, token);
							if(acumulado!=null){
								for(DeduccionProductoDTO iAcumulado : acumulado){
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
	
	public List<DeduccionProductoDTO> inventariarDetalle(
			PedidoVentaCaracteristicaDTO pCampo, 
			String operacion, 
			String recursoInventario, 
			String documentoInicial,
			String token) throws ServerException {
		if(operacion==null) throw new ServerException("La operacion de inventarios no puede ser vacia");
		BigDecimal factor = null;
		
		if (operacion.contains("E")) factor = BigDecimal.ONE;
		if (operacion.contains("S")) factor = BigDecimal.ONE.negate();
		if (operacion.compareTo("T")==0) factor = BigDecimal.ONE;
		
		if(factor ==null) throw new ServerException("Revise la operacion de la bodega, no se identifica el factor");
		
		List<DeduccionProductoDTO> result = null;
		System.out.format("\n[%s - %s] Gestionando inventario operacion", pCampo.getCampoDTO().getPlantillaNombre(),  pCampo.getCampoDTO().getNombre());
		
		if(pCampo.getDetalles()!=null && !pCampo.getDetalles().isEmpty()){
			for (DetallePedidoVentaDTO detalle : pCampo.getDetalles()) {
				//Si principal viene nulo viene de tipo proceso
				if(pCampo.getPrincipal()==null ||  detalle.getLlaveTabla()==null || detalle.getEstado()==null || detalle.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO)==0){
					boolean deducirComposicion = false;//En transformacion se hacen las 2 operaciones
					System.out.format("\n[%s - %s] Revisando producto %s", pCampo.getCampoDTO().getPlantillaNombre(),  pCampo.getCampoDTO().getNombre(), detalle.getNombre());
					if(!operacion.contains("C")){
						ProductoInventarioFilterDTO productoFilter = new ProductoInventarioFilterDTO();
						productoFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
						productoFilter.setProducto(detalle.getProducto());
						productoFilter.setBodega(recursoInventario);
						ProductoInventarioDTO producto = productoInventarioService.consultaUnica(productoFilter);
						if(producto!=null){
							DeduccionProductoDTO salida = new DeduccionProductoDTO();
							salida.setBodega(recursoInventario);
							salida.setCantidad(detalle.getCantidadTotal().multiply(factor));
							if(detalle.getEstado()!=null && detalle.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO)==0) salida.setCantidad(salida.getCantidad().negate());
							salida.setProducto(detalle.getProducto());
							salida.setDocumento(documentoInicial);
							result = adicionarDeduccion(result, salida);
							deducirComposicion = false;
							if (operacion.compareTo("T")==0) deducirComposicion = true;
						}else{
							if (operacion.compareTo("T")==0) throw new ServerException("Para una transformacion es necesario que el producto maneje inventarios." + detalle.getNombre());
							if(!operacion.contains("D"))deducirComposicion = true;
						}
					}else{
						deducirComposicion = true;
					}
					
					if(deducirComposicion){
						ProductoInventarioDescuentoFilterDTO filtro = new ProductoInventarioDescuentoFilterDTO();
						filtro.setProducto(detalle.getProducto());
						filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
						List<ProductoInventarioDescuentoDTO> descuentos = productoInventarioDescuentoService.listarConsulta(filtro);
						if(descuentos==null || descuentos.size()==0){
							ProductoDTO productoDTO = productoService.consultaXId(detalle.getProducto());
							if(productoDTO.getProductoBase()!=null) {
								filtro.setProducto(productoDTO.getProductoBase());
								descuentos = productoInventarioDescuentoService.listarConsulta(filtro);
								if(descuentos==null || descuentos.isEmpty()){//En caso que no tenda composicion miramos que sea el mismo
									ProductoInventarioFilterDTO inventarioPFilter = new ProductoInventarioFilterDTO();
									inventarioPFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
									inventarioPFilter.setProducto(productoDTO.getProductoBase());
									inventarioPFilter.setBodega(recursoInventario);
									ProductoInventarioDTO inventarioP = productoInventarioService.consultaUnica(inventarioPFilter);
									if(inventarioP!=null) {
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
						if(descuentos!=null && descuentos.size()!=0){
							if (operacion.compareTo("T")==0) factor = factor.negate();
							for (ProductoInventarioDescuentoDTO descuento : descuentos) {
								DeduccionProductoDTO salida = new DeduccionProductoDTO();
								salida.setProducto(descuento.getProductoDescontar());
								if(descuento.getCaracteristica()!=null){
									salida.setProducto(null);
									if(detalle.getCaracteristicas()!=null && !detalle.getCaracteristicas().isEmpty()){
										for(PedidoVentaCaracteristicaDTO iterador: detalle.getCaracteristicas()){
											if(iterador.getValorOpcion()!=null ){
												if(iterador.getValorOpcion().compareTo(descuento.getCaracteristica())==0){
													salida.setProducto(descuento.getProductoDescontar());
													break;
												}
											}
										}
									}
								}
								if(salida.getProducto()!=null){
									salida.setCantidad(descuento.getCantidadProductoDescontar().multiply(detalle.getCantidadTotal().multiply(factor)));
									if(detalle.getEstado()!=null && detalle.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO)==0) salida.setCantidad(salida.getCantidad().negate());
									salida.setBodega(recursoInventario);
									salida.setDocumento(documentoInicial);
									result = adicionarDeduccion(result, salida);
								}
							}
						}else{
							if (operacion.compareTo("T")==0) {
								ProductoDTO productoDTO = productoService.consultaXId(detalle.getProducto());
								if(productoDTO.getProductoBase()!=null) 
									productoDTO = productoService.consultaXId(productoDTO.getProductoBase());
								throw new ServerException("Para una transformacion es necesario que el producto maneje inventarios de composicion." + productoDTO.getNombre());
							}
						}
					}
					
				}
			}
		}
		return result;
	}
	
	public List<DeduccionProductoDTO> adicionarDeduccion(List<DeduccionProductoDTO> deduccionesFinales, DeduccionProductoDTO salida) {
		if(deduccionesFinales==null)deduccionesFinales = new ArrayList<DeduccionProductoDTO>(); 
		if(!deduccionesFinales.isEmpty()){
			for(DeduccionProductoDTO deduccion : deduccionesFinales){
				if(deduccion.getProducto().compareTo(salida.getProducto())==0){
					deduccion.setCantidad(deduccion.getCantidad().add(salida.getCantidad()));
					return deduccionesFinales;
				}
			}
		}
		deduccionesFinales.add(salida);
		return deduccionesFinales;
	}
}
