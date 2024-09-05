package com.softure.document_execution.application.field;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.document_execution.application.DetallePedidoVentaSvc;
import com.softure.document_execution.domain.DetallePedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.inventory.domain.ProductoDTO;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.property.domain.PropiedadDTO;

@Service
public class CallProductValidateAndSave {

	@Autowired @Lazy 
	private DetallePedidoVentaSvc detallePedidoVentaService;
	@Autowired @Lazy 
	private DocumentoPlantillaCaracteristicaSvc caracteristicaService;

	public List<DetallePedidoVentaDTO> orderToValidate(List<DetallePedidoVentaDTO> products) throws ServerException {

		List<DetallePedidoVentaDTO> agrupados = new ArrayList<DetallePedidoVentaDTO>();
		// Consulto las propiedades de los productos
		List<ProductoDTO> productos = new ArrayList<>();
		for (DetallePedidoVentaDTO detalle : products) {
			ProductoDTO prod = new ProductoDTO();
			prod.setLlaveTabla(detalle.getProducto());
			productos.add(prod);
		}
		productos = detallePedidoVentaService.simplificarConsultaBDProductos(productos);
		// Agrupo los detalles por producto
		for (DetallePedidoVentaDTO detalle : products) {
			for (ProductoDTO iProducto : productos) {
				if (iProducto.getLlaveTabla().compareTo(detalle.getProducto()) == 0) {
					detalle.setPropiedades(iProducto.getPropiedades());
					if(detalle.getNombre()==null) detalle.setNombre(iProducto.getNombre());
					detalle.setPlantillaDetalle(iProducto.getTemplateFields());
					break;
				}
			}
			detallePedidoVentaService.definirPropiedad2Caracteristicas(detalle);
			if (detalle.getCantidad() == null || detalle.getCantidad().compareTo(BigDecimal.ZERO) == 0)
				throw new ServerException("No se puede registrar un producto con cantidad CERO");
			boolean agregar = true;
			for (DetallePedidoVentaDTO detalleAgrupado : agrupados) {
				if (detalleAgrupado.getProducto().compareTo(detalle.getProducto()) == 0) {
					agregar = sonCaracteristicasIguales(detalleAgrupado, detalle);
					if (agregar) {
						detalleAgrupado.setCantidad(detalleAgrupado.getCantidad().add(detalle.getCantidad()));
						detalleAgrupado.setLlaveTabla(null);
						agregar = false;
					} else {
						// Esto se hace por el tema de validar caracteristicas
						agregar = true;
					}
					
					break;
				}
			}
			if (agregar)
				agrupados.add(detalle);
		}
		return agrupados;
	}
	
	public List<DetallePedidoVentaDTO> validateWithExistProducts(List<DetallePedidoVentaDTO> products, String document, List<PropiedadDTO> tarrifs, String token) throws ServerException {
		List<DetallePedidoVentaDTO> detallesActuales = detallePedidoVentaService
				.listarCompleto(document, tarrifs, null, null, token);

		if(products!=null) {
			for (DetallePedidoVentaDTO detalle : products) {
				if (detalle.getLlaveTabla() != null) {
					if (detallesActuales != null && !detallesActuales.isEmpty()) {
						for (DetallePedidoVentaDTO actual : detallesActuales) {
							if (detalle.getLlaveTabla().compareTo(actual.getLlaveTabla()) == 0) {
								//Coloco estos valores que debeiran venir de forn pero para evitar errores
								detalle.setDetalleId(actual.getDetalleId());
								detalle.setDocumento(actual.getDocumento());
								detalle.setPlantilla(actual.getPlantilla());
								detalle.setPlantillaDetalle(actual.getPlantillaDetalle());
								//
								BigDecimal diferencia = actual.getCantidad().subtract(detalle.getCantidad());
								if (diferencia.compareTo(BigDecimal.ZERO) != 0) {
									detalle.setLlaveTabla(null);
								} else {
									if (sonCaracteristicasIguales(detalle, actual)) {
										detallesActuales.remove(actual);
									} else {
										detalle.setLlaveTabla(null);
									}
								}
								break;
							}
						}
					}
				}
			}
		}
		
		if (detallesActuales != null && detallesActuales != null && !detallesActuales.isEmpty()) {
			if (products == null)
				products = new ArrayList<DetallePedidoVentaDTO>();
			for (DetallePedidoVentaDTO detalleEliminado : detallesActuales) {
				detalleEliminado.setEstado(SharedConstants.STATE_INACTIVE);
				products.add(detalleEliminado);
			}
		}
		
		return products;
	}

	public List<DetallePedidoVentaDTO> save(List<DetallePedidoVentaDTO> products, String token, String document,
			String template, String transaction, String fieldId) throws ServerException {

		List<DetallePedidoVentaDTO> result = new ArrayList<>();
		for (DetallePedidoVentaDTO detalle : products) {
			detalle.setDocumento(document);
			detalle.setPlantilla(template);
			detalle.setCampo(fieldId);
			detalle.setTransaccionRegistro(transaction);
			if (detalle.getLlaveTabla() == null) {
				detalle = detallePedidoVentaService.guardar(detalle, token);
				// En inventario se debe tener primero el campo bodega y evitar agregar
				//result.add(detalle);
			} else {
				if (detalle.getEstado() != null
						&& detalle.getEstado().compareTo(SharedConstants.STATE_INACTIVE) == 0) {
					detalle.setTransaccionInactivo(transaction);
					detallePedidoVentaService.inactivar(detalle, token);
				} else {
					// NO he entendido porque el estado llega null
					if (detalle.getEstado() == null)
						detalle.setEstado(SharedConstants.STATE_ACTIVE);
					// aqui seria bueno validar que el detalle si se el correcto

					detallePedidoVentaService.actualizar(detalle, token);
					result.add(detalle);
				}
			}
		}
		return result;
	}
	
	private boolean sonCaracteristicasIguales(DetallePedidoVentaDTO detalleAgrupado, DetallePedidoVentaDTO detalle)
			throws ServerException {
		// Valido qus las caracteristicas del proeudcto sean iguales para sumarlas
		if (detalleAgrupado.getDocumentoDetalle()!=null && detalleAgrupado.getDocumentoDetalle().getCaracteristicas() != null 
				&& !detalleAgrupado.getDocumentoDetalle().getCaracteristicas().isEmpty()) {
			if (detalle.getDocumentoDetalle().getCaracteristicas() == null)
				return true;
			if (detalle.getDocumentoDetalle().getCaracteristicas() == null)
				return true;
			if (detalle.getDocumentoDetalle().getCaracteristicas().isEmpty())
				return true;
			if (detalle.getDocumentoDetalle().getCaracteristicas().size() != detalleAgrupado.getDocumentoDetalle().getCaracteristicas().size())
				return true;

			for (PedidoVentaCaracteristicaDTO dcp : detalle.getDocumentoDetalle().getCaracteristicas()) {
				if (dcp.getCampoDTO() == null)
					dcp.setCampoDTO(caracteristicaService.consultaXIdProducto(dcp.getCampo()));
				for (PedidoVentaCaracteristicaDTO dcpa : detalleAgrupado.getDocumentoDetalle().getCaracteristicas()) {
					if (dcpa.getCampoDTO() == null)
						dcpa.setCampoDTO(caracteristicaService.consultaXIdProducto(dcpa.getCampo()));
					if (dcp.getCampoDTO().getCodigo().compareTo(dcpa.getCampoDTO().getCodigo()) == 0) {
						if (dcp.getValorOpcion() == null) {
							if (dcp.getValorText() != null) {
								if (dcpa.getValorText() != null) {
									if (dcp.getValorText().compareTo(dcpa.getValorText()) != 0) {
										// detalleAgrupado.setLlaveTabla(null);
										return false;
									}
								} else {
									// detalleAgrupado.setLlaveTabla(null);
									return false;
								}
							} else {
								if (dcpa.getValorText() != null) {
									// detalleAgrupado.setLlaveTabla(null);
									return false;
								}
							}
						} else {
							if (dcpa.getValorOpcion() != null
									&& dcp.getValorOpcion().compareTo(dcpa.getValorOpcion()) != 0) {
								// detalleAgrupado.setLlaveTabla(null);
								return false;
							}
						}

						break;
					}
				}
			}
		}
		return true;
	}
}
