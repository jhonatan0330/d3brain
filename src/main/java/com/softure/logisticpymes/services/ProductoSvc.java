package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.util.ArrayList;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.services.SoftureUtil;
// END region interImport
import com.softure.logisticpymes.infrastructure.mybatis.mapper.ProductoMapper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.domain.dto.PedidoVentaDTO;
import com.softure.logisticpymes.domain.dto.ProductoDTO;
import com.softure.logisticpymes.domain.dto.UsuarioRolProductoDTO;
import com.softure.logisticpymes.domain.filter.ProductoFilterDTO;
import com.softure.logisticpymes.domain.filter.UsuarioRolProductoFilterDTO;

@Service("productoService")
public class ProductoSvc extends BasicSvc<ProductoDTO, ProductoFilterDTO> {
	
	@Autowired
	private ProductoMapper productoMapper;
	
	// BEGIN region servicesProducto
	@Autowired private UsuarioRolProductoSvc usuarioRolProductoSvc;
	@Autowired private CategoriaProductoSvc categoriaSvc;
	// END region servicesProducto

	@Override
	public ProductoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. Producto");
		ProductoFilterDTO dto = new ProductoFilterDTO();
		dto.setLlaveTabla(llave);
		return productoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = productoMapper;
	}
	
	@Override
	public ProductoDTO activar(ProductoDTO dto, String token) throws ServerException {
		// BEGIN Producto_activar
		return super.activar(dto, token);
		// END Producto_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProductoDTO actualizar( ProductoDTO dto, String token) throws ServerException {
		// BEGIN Producto_actualizar
		return super.actualizar(dto, token);
		// END Producto_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProductoDTO inactivar(ProductoDTO dto, String token) throws ServerException {
		// BEGIN Producto_inactivar
		dto = super.inactivar(dto, token);
		UsuarioRolProductoFilterDTO filtro = new UsuarioRolProductoFilterDTO();
		filtro.setProducto(dto.getLlaveTabla());
		filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		List<UsuarioRolProductoDTO> relacionados = usuarioRolProductoSvc.listarConsulta(filtro);
		if(relacionados!=null &&!relacionados.isEmpty()) {
			for(UsuarioRolProductoDTO iProducto : relacionados) {
				usuarioRolProductoSvc.inactivar(iProducto, token);
			}
		}
		return dto;
		// END Producto_inactivar
	}
	
	@Override
	public ProductoDTO consultaUnica(ProductoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(ProductoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<ProductoDTO> listarConsulta(ProductoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProductoDTO guardar(ProductoDTO dto, String token) throws ServerException {
		// BEGIN Producto_guardar
		dto = super.guardar(dto, token);
		categoriaSvc.organizarInventario();
		return dto;
		// END Producto_guardar
	}

// BEGIN region aditionalMethods
	
	public ProductoDTO crearDesdeDocumento(PedidoVentaDTO documento, String categoria) throws ServerException{
		ProductoFilterDTO newProductoFilter = new ProductoFilterDTO();
		newProductoFilter.setDocumento(documento.getLlaveTabla());
		ProductoDTO newProducto = consultaUnica(newProductoFilter);
		if (newProducto==null) {
			newProducto = new ProductoDTO();
			newProducto.setCategoria(categoria);
			newProducto.setCodigo(documento.getNombre());
			newProducto.setDocumento(documento.getLlaveTabla());
			newProducto.setNombre(documento.getDescripcion());
			newProducto = save(newProducto);
			categoriaSvc.organizarInventario();
		}else {
			if(documento.getEstado().compareTo(newProducto.getEstado())!=0){
				if(documento.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)==0) {
					newProducto.setEstado(ConstantesGenerales.ESTADO_ACTIVO);	
				}else {
					newProducto.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
				}
				newProducto = update(newProducto);
			}
		}
		return newProducto;
	}
	
	public List<ProductoDTO> listarProductoPlantillaResponsable(ProductoFilterDTO dto)throws ServerException{
		if(dto.getUsuarioRol()==null)
			throw new ServerException("Se necesita el usuariorol en la plantillaproducto");
		if(dto!=null && dto.getFiltroParametro()!=null && dto.getFiltroParametro().compareTo("*")==0)dto.setFiltroParametro(null);
		return productoMapper.listarProductoPlantillaResponsable(dto);
	}
	
	public List<ProductoDTO> listarProductoFuncion(String funcion, String documento, String filtro, String token)throws ServerException{
		return productoMapper.listarProductoFuncion(funcion, documento, filtro, token);
	} 
	
	public List<ProductoDTO> listarProductoCampo(String campo, String filtro)throws ServerException{
		if(filtro!=null) filtro = SoftureUtil.formatFunction(filtro).toUpperCase();
		return productoMapper.listarProductoCampo(campo, filtro);
	}
	
	public List<ProductoDTO> listarProductoSimplificar(List<ProductoDTO> productos)throws ServerException{
		if(productos==null || productos.isEmpty()) return new ArrayList<ProductoDTO>();
		return productoMapper.listarProductoSimplificado(productos);
	}
	
	public ProductoDTO getProduct2Document(String document)throws ServerException{
		ProductoFilterDTO p = new ProductoFilterDTO();
		p.setDocumento(document);
		p.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		ProductoDTO pr = consultaUnica(p);
		if(pr!=null && pr.getProductoBase()!=null) {
			ProductoDTO pb = consultaXId(pr.getProductoBase());
			pr.setBaseNombre(pb.getNombre());
		}
		return pr;
	}
	
	public List<ProductoDTO> getProducts2Filter(String filter)throws ServerException{
		ProductoFilterDTO p = new ProductoFilterDTO();
		p.setFiltroParametro(filter);
		p.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		return listarConsulta(p);
	}
	
	public ProductoDTO filtrarPorCodigo(String codigo)throws ServerException{
		return productoMapper.filtrarPorCodigo(codigo);
	}
// END region aditionalMethods

}