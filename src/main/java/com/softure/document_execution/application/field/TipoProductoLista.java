package com.softure.document_execution.application.field;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shared.domain.ServerException;
import com.softure.authorization.application.UsuarioRolProductoSvc;
import com.softure.authorization.domain.UsuarioRolProductoDTO;
import com.softure.authorization.domain.UsuarioRolProductoFilterDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import com.softure.inventory.application.CategoriaProductoSvc;
import com.softure.inventory.application.ProductoSvc;
import com.softure.inventory.domain.CategoriaProductoDTO;
import com.softure.inventory.domain.ProductoDTO;
import com.softure.inventory.domain.ProductoFilterDTO;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;

@Component
public class TipoProductoLista {

	@Autowired
	private DocumentoPlantillaCaracteristicaSvc caracteristicaService;
	@Autowired
	private ProductoSvc productoService;
	@Autowired
	private UsuarioRolProductoSvc usuarioRolProductoService;
	@Autowired
	private CategoriaProductoSvc categoriaProductoService;

	public void cargarConsultaCampo(PedidoVentaCaracteristicaDTO pCampo) throws ServerException {
		UsuarioRolProductoFilterDTO urp = new UsuarioRolProductoFilterDTO();
		urp.setDocumento(pCampo.getDocumento());
		urp.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		pCampo.setProductosExclusivos(usuarioRolProductoService.listarConsulta(urp));
	}

	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL) == null
				&& (pCampo.getProductosExclusivos() == null || pCampo.getProductosExclusivos().size() == 0))
			throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre()
					+ " Es obligatorio registrar el campo " + pCampo.getCampoDTO().getNombre() + "(codigo : "
					+ pCampo.getCampoDTO().getCodigo() + ")");
		List<UsuarioRolProductoDTO> productosActualesUsuario = null;
		if (pCampo.getDocumento() != null) {
			UsuarioRolProductoFilterDTO filtroProductosExistentes = new UsuarioRolProductoFilterDTO();
			filtroProductosExistentes.setDocumento(pCampo.getDocumento());
			filtroProductosExistentes.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			productosActualesUsuario = usuarioRolProductoService.listarConsulta(filtroProductosExistentes);
		}
		if (pCampo.getProductosExclusivos() != null) {
			for (UsuarioRolProductoDTO urp : pCampo.getProductosExclusivos()) {
				if (urp.getLlaveTabla() != null) {
					if (productosActualesUsuario != null && !productosActualesUsuario.isEmpty()) {
						for (UsuarioRolProductoDTO urActual : productosActualesUsuario) {
							if (urActual.getLlaveTabla().compareTo(urp.getLlaveTabla()) == 0) {
								productosActualesUsuario.remove(urActual);
								break;
							}
						}
					}
				} else {
					urp.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
					// Valido que en la lista no se ingrese 2 veces el mismo producto
					int ingresos = 0;
					for (UsuarioRolProductoDTO iProducto : pCampo.getProductosExclusivos()) {
						if (iProducto.getProducto().compareTo(urp.getProducto()) == 0)
							ingresos++;
					}
					if (ingresos != 1)
						throw new ServerException("Valida el producto " + urp.getProductoNombre()
								+ " aparece varias veces : " + ingresos);
				}
			}
		}
		if (productosActualesUsuario != null && !productosActualesUsuario.isEmpty()) {
			for (UsuarioRolProductoDTO urInactivar : productosActualesUsuario) {
				urInactivar.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
				pCampo.getProductosExclusivos().add(urInactivar);
			}
		}
	}

	public PedidoVentaCaracteristicaDTO guardarCampo(PedidoVentaCaracteristicaDTO pCampo, String token)
			throws ServerException {
		for (UsuarioRolProductoDTO producto : pCampo.getProductosExclusivos()) {
			if (producto.getLlaveTabla() == null) {
				producto.setDocumento(pCampo.getDocumento());
				producto = usuarioRolProductoService.guardar(producto, token);
			} else {
				if (producto.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO) == 0) {
					producto = usuarioRolProductoService.inactivar(producto, token);
				} else {
					producto = usuarioRolProductoService.actualizar(producto, token);
				}
			}
		}
		return pCampo;
	}

	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(PedidoVentaCaracteristicaFilterDTO pCampo)
			throws ServerException {
		if (pCampo == null || pCampo.getCampo() == null)
			throw new ServerException("Revise la parametro del metodo");
		DocumentoPlantillaCaracteristicaDTO pBase = caracteristicaService.consultaXId(pCampo.getCampo());
		if (pBase == null)
			throw new ServerException("Error en el identificador de la caracteristica");
		ProductoFilterDTO entityFilt = new ProductoFilterDTO();
		entityFilt.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		entityFilt.setFiltroParametro(pCampo.getFiltroParametro());
		pBase.setProductos(productoService.listarConsulta(entityFilt));

		if (pBase.getProductos() != null && !pBase.getProductos().isEmpty()) {
			List<CategoriaProductoDTO> categorias = new ArrayList<CategoriaProductoDTO>();
			for (ProductoDTO productoDTO : pBase.getProductos()) {
				boolean existeCategoria = false;
				for (CategoriaProductoDTO catPlantilla : categorias) {
					if (catPlantilla.getLlaveTabla().compareTo(productoDTO.getCategoria()) == 0) {
						productoDTO.setCantidadPromocionBase(catPlantilla.getPromocionBase());
						existeCategoria = true;
						break;
					}
				}
				if (!existeCategoria) {
					CategoriaProductoDTO categoria = categoriaProductoService.consultaXId(productoDTO.getCategoria());
					productoDTO.setCantidadPromocionBase(categoria.getPromocionBase());
					categorias.add(categoria);
				}
			}
		}
		pCampo.setCampoDTO(pBase);
		return pCampo;
	}
}
