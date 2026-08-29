package d3.configuration.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.authorization.application.UsuarioRolProductoSvc;
import d3.authorization.domain.UsuarioRolProductoDTO;
import d3.authorization.domain.UsuarioRolProductoFilterDTO;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.document.application.CallDocumentCommons;
import d3.document.application.field.Propiedades;
import d3.document.domain.PedidoVentaDTO;
import d3.inventory.application.ProductoSvc;
import d3.inventory.domain.ProductoDTO;
import d3.inventory.domain.ProductoFilterDTO;
import d3.process.application.DocumentoPlantillaCaracteristicaSvc;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;

@Component
public class HomologateProduct {

	private final ProductoSvc productService;
	private final UsuarioRolProductoSvc usuarioRolProductoSvc;

	public HomologateProduct(@Lazy ProductoSvc productService, @Lazy UsuarioRolProductoSvc usuarioRolProductoSvc) {
		this.productService = productService;
		this.usuarioRolProductoSvc = usuarioRolProductoSvc;
	}

	public void createProductFields(String templateId, String token, DocumentoPlantillaCaracteristicaSvc campoService,
			PropiedadSvc propertyService) throws ServerException {
		List<String> fieldsTemplate = new ArrayList<>();
		fieldsTemplate.add(
				campoService.createField(templateId, "NOMBRE", DocumentoPlantillaCaracteristicaDTO.TEXTO, 1, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(0), Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA,
				templateId, Propiedades.DESCRIPCION, fieldsTemplate.get(0), token), token);

		fieldsTemplate.add(campoService.createField(templateId, "DESCRIPCION",
				DocumentoPlantillaCaracteristicaDTO.TEXTO, 2, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(1), Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);

		fieldsTemplate.add(
				campoService.createField(templateId, "BASE", DocumentoPlantillaCaracteristicaDTO.PROCESO, 10, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(2), Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(2), Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);

	}

	public ProductoDTO crearDesdeDocumento(PedidoVentaDTO documento, String token) throws ServerException {
		ProductoFilterDTO newProductoFilter = new ProductoFilterDTO();
		newProductoFilter.setDocumento(documento.getLlaveTabla());
		ProductoDTO newProducto = productService.consultaUnica(newProductoFilter);
		if (newProducto == null) {
			newProducto = new ProductoDTO();
			newProducto.setCategoria(documento.getPlantilla());
			newProducto.setCodigo(documento.getNombre());
			newProducto.setDocumento(documento.getLlaveTabla());
			newProducto.setNombre(documento.getDescripcion());
			newProducto.setDescripcion(CallDocumentCommons.getValueText(documento, "DESCRIPCION"));
			newProducto.setProductoBase(getBase(CallDocumentCommons.getValueOption(documento, "BASE")));
			newProducto = productService.save(newProducto);
		} else {
			if (documento.getEstado().compareTo(SharedConstants.STATE_ACTIVE) == 0) {
				newProducto.setDescripcion(CallDocumentCommons.getValueText(documento, "DESCRIPCION"));
				newProducto.setProductoBase(getBase(CallDocumentCommons.getValueOption(documento, "BASE")));
				newProducto.setEstado(SharedConstants.STATE_ACTIVE);
			} else {
				newProducto.setEstado(SharedConstants.STATE_INACTIVE);
				inactivateUsuarioRolProduct(newProducto, token);
			}
			newProducto = productService.update(newProducto);

		}
		return newProducto;
	}

	private String getBase(String valueOption) throws ServerException {
		if (valueOption == null)
			return null;
		ProductoDTO prod = productService.getProduct2Document(valueOption);
		if (prod == null)
			return null;
		return prod.getLlaveTabla();
	}

	private void inactivateUsuarioRolProduct(ProductoDTO dto, String token) throws ServerException {

		UsuarioRolProductoFilterDTO filtro = new UsuarioRolProductoFilterDTO();
		filtro.setProducto(dto.getLlaveTabla());
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		List<UsuarioRolProductoDTO> relacionados = usuarioRolProductoSvc.listarConsulta(filtro);
		if (relacionados != null && !relacionados.isEmpty()) {
			for (UsuarioRolProductoDTO iProducto : relacionados) {
				usuarioRolProductoSvc.inactivar(iProducto, token);
			}
		}
	}
}
