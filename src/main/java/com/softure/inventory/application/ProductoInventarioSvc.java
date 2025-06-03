package com.softure.inventory.application;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.inventory.domain.CategoriaProductoDTO;
import com.softure.inventory.domain.ProductoDTO;
import com.softure.inventory.domain.ProductoInventarioDTO;
import com.softure.inventory.domain.ProductoInventarioFilterDTO;
import com.softure.inventory.infrastructure.ProductoInventarioMapper;
import com.softure.logisticpymes.application.BasicSvc;

import jakarta.annotation.PostConstruct;

@Service("productoInventarioService")
public class ProductoInventarioSvc extends BasicSvc<ProductoInventarioDTO, ProductoInventarioFilterDTO> {
	
	@Autowired @Lazy 
	private ProductoInventarioMapper productoInventarioMapper;
	@Autowired @Lazy 
	private ProductoSvc productService;
	@Autowired @Lazy 
	private CategoriaProductoSvc categoryService;
	
	@Override
	public ProductoInventarioDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. ProductoInventario");
		ProductoInventarioFilterDTO dto = new ProductoInventarioFilterDTO();
		dto.setLlaveTabla(llave);
		return productoInventarioMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = productoInventarioMapper;
	}
	
	@Override
	public ProductoInventarioDTO activar(ProductoInventarioDTO dto, String token) throws ServerException {
		// BEGIN ProductoInventario_activar
		return super.activar(dto, token);
		// END ProductoInventario_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProductoInventarioDTO actualizar( ProductoInventarioDTO dto, String token) throws ServerException {
		// BEGIN ProductoInventario_actualizar
		ProductoInventarioDTO bd = consultaXId(dto.getLlaveTabla());
		if(dto.getCantidadModificar()!=null && dto.getCantidadModificar().compareTo(BigDecimal.ZERO)!=0){
			bd.setCantidadActual(bd.getCantidadActual().add(dto.getCantidadModificar()));
			return super.actualizar(bd, token);
		}else{
			if(dto.getCantidadMaxima().compareTo(bd.getCantidadMaxima())!=0 || dto.getCantidadMinima().compareTo(bd.getCantidadMinima())!=0){
				
				bd.setCantidadMaxima(dto.getCantidadMaxima());
				bd.setCantidadMinima(dto.getCantidadMinima());
				if(bd.getCantidadMinima().compareTo(bd.getCantidadMaxima())>0)
					throw new ServerException("La cantidad minima no puede ser mayor a la maxima");
				return super.actualizar(bd, token);
			}
		}
		return bd;
		// END ProductoInventario_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProductoInventarioDTO inactivar(ProductoInventarioDTO dto, String token) throws ServerException {
		// BEGIN ProductoInventario_inactivar
		return super.inactivar(dto, token);
		// END ProductoInventario_inactivar
	}
	
	@Override
	public ProductoInventarioDTO consultaUnica(ProductoInventarioFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(ProductoInventarioFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<ProductoInventarioDTO> listarConsulta(ProductoInventarioFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	public ProductoInventarioDTO guardar(ProductoInventarioDTO dto, String token) throws ServerException {
		// BEGIN ProductoInventario_guardar
		if(dto.getDocumento()==null)  throw new ServerException("Al crear un inventario de producto debe teenr un documento.");
		ProductoDTO product = productService.consultaXId(dto.getProducto());
		
		ProductoInventarioFilterDTO unicoFilter = new ProductoInventarioFilterDTO();
		unicoFilter.setBodega(dto.getBodega());
		unicoFilter.setProducto(dto.getProducto());
		ProductoInventarioDTO unico = consultaUnica(unicoFilter);
		if(unico!=null){
			if(unico.getEstado().compareTo(SharedConstants.STATE_ACTIVE)==0){
				throw new ServerException("Este producto " + product.getNombre() +" ya se encuentra referenciado para controlar en esta bodega ");
			}else{
				throw new ServerException("Este producto " + product.getNombre() +" se encuentra inactivo para manejo de inventarios en esta bodega ");
			}
		}
		if(dto.getCantidadMinima()==null) dto.setCantidadMinima(BigDecimal.ZERO);
		if(dto.getCantidadMaxima()==null) {
			CategoriaProductoDTO category = categoryService.consultaXId(product.getCategoria());
			dto.setCantidadMaxima(category.getCantidadMaxima());
			if(dto.getCantidadMaxima().compareTo(BigDecimal.ZERO) ==0)
				dto.setCantidadMaxima(BigDecimal.valueOf(1000));
		}
		if(dto.getCantidadMinima().compareTo(dto.getCantidadMaxima())>=0)
			throw new ServerException("La cantidad minima no puede ser mayor o igual a la maxima");
		
		dto.setCantidadActual(BigDecimal.ZERO);
		dto.setFechaInicial(new Date());
		return super.save(dto);
		// END ProductoInventario_guardar
	}

// BEGIN region aditionalMethods
	public List<ProductoInventarioDTO> getByProducto( String id)
			throws ServerException {
		ProductoInventarioFilterDTO filter = new ProductoInventarioFilterDTO();
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		filter.setProducto(id);
		return super.listarConsulta(filter);
	}
	
// END region aditionalMethods

}