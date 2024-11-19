package com.configuration.homologate.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.authorization.application.UsuarioRolProductoSvc;
import com.softure.authorization.domain.UsuarioRolProductoDTO;
import com.softure.authorization.domain.UsuarioRolProductoFilterDTO;
import com.softure.document_execution.application.CallDocumentCommons;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.inventory.application.CategoriaProductoSvc;
import com.softure.inventory.application.ProductoSvc;
import com.softure.inventory.domain.ProductoDTO;
import com.softure.inventory.domain.ProductoFilterDTO;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

@Component
public class HomologateProduct {

	@Autowired @Lazy private ProductoSvc productService;
	@Autowired @Lazy private CategoriaProductoSvc categoriaSvc;
	@Autowired @Lazy private UsuarioRolProductoSvc usuarioRolProductoSvc;
	
	public void createProductFields(String templateId, String token, DocumentoPlantillaCaracteristicaSvc campoService, PropiedadSvc propertyService) throws ServerException {
		List<String> fieldsTemplate = new ArrayList<>();
		fieldsTemplate.add(
				campoService.createField(templateId, "NOMBRE", DocumentoPlantillaCaracteristicaDTO.TEXTO, 1, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(0),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA, templateId,
				Propiedades.DESCRIPCION, fieldsTemplate.get(0), token), token);
		
		fieldsTemplate.add(
				campoService.createField(templateId, "DESCRIPCION",	DocumentoPlantillaCaracteristicaDTO.TEXTO, 2, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(1),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
	
		fieldsTemplate.add(
				campoService.createField(templateId, "BASE", DocumentoPlantillaCaracteristicaDTO.PROCESO, 10, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(2),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(2),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		
	}
		

	public ProductoDTO crearDesdeDocumento(PedidoVentaDTO documento, String categoria, String token) throws ServerException{
		ProductoFilterDTO newProductoFilter = new ProductoFilterDTO();
		newProductoFilter.setDocumento(documento.getLlaveTabla());
		ProductoDTO newProducto = productService.consultaUnica(newProductoFilter);
		if (newProducto==null) {
			newProducto = new ProductoDTO();
			newProducto.setCategoria(categoria);
			newProducto.setCodigo(documento.getNombre());
			newProducto.setDocumento(documento.getLlaveTabla());
			newProducto.setNombre(documento.getDescripcion());
			newProducto.setDescripcion(CallDocumentCommons.getValueText(documento, "DESCRIPCION"));
			newProducto.setProductoBase(getBase(CallDocumentCommons.getValueOption(documento, "BASE")));
			newProducto = productService.save(newProducto);
			// Al crear un producto, no puedo crear propiedades 
			/*CategoriaProductoDTO category = categoriaSvc.consultaXId(categoria);
			if(category.getInventarios()) {
				PropiedadDTO propiedadModifcable = new PropiedadDTO();
				propiedadModifcable.setCampo(newProducto.getLlaveTabla());
				propiedadModifcable.setKey(Propiedades.INVENTARIO_OBLIGATORIO);
				propiedadModifcable.setTipo(PropiedadValorDefinidoDTO.PLANTILLA);
				propiedadModifcable.setValor("1");
				propiedadService.guardar(propiedadModifcable, token);
			}*/
			categoriaSvc.organizarInventario();
		}else {
			if(documento.getEstado().compareTo(SharedConstants.STATE_ACTIVE)==0) {
				newProducto.setDescripcion(CallDocumentCommons.getValueText(documento, "DESCRIPCION"));
				newProducto.setProductoBase(getBase(CallDocumentCommons.getValueOption(documento, "BASE")));
				newProducto.setEstado(SharedConstants.STATE_ACTIVE);	
			}else {
				newProducto.setEstado(SharedConstants.STATE_INACTIVE);
				inactivateUsuarioRolProduct(newProducto, token);
			}	
			newProducto = productService.update(newProducto);
			
		}
		return newProducto;
	}
	

	private String getBase(String valueOption) throws ServerException {
		if(valueOption==null) return null;
		ProductoDTO prod = productService.getProduct2Document(valueOption);
		if(prod==null) return null;
		return prod.getLlaveTabla();
	}


	private void inactivateUsuarioRolProduct(ProductoDTO dto, String token) throws ServerException {

		UsuarioRolProductoFilterDTO filtro = new UsuarioRolProductoFilterDTO();
		filtro.setProducto(dto.getLlaveTabla());
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		List<UsuarioRolProductoDTO> relacionados = usuarioRolProductoSvc.listarConsulta(filtro);
		if(relacionados!=null &&!relacionados.isEmpty()) {
			for(UsuarioRolProductoDTO iProducto : relacionados) {
				usuarioRolProductoSvc.inactivar(iProducto, token);
			}
		}
	}
}
