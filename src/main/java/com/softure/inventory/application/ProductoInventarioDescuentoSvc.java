package com.softure.inventory.application;

import java.util.List;

// BEGIN region interImport
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.inventory.domain.ProductoInventarioDescuentoDTO;
import com.softure.inventory.domain.ProductoInventarioDescuentoFilterDTO;
import com.softure.inventory.infrastructure.ProductoInventarioDescuentoMapper;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;

@Service("productoInventarioDescuentoService")
public class ProductoInventarioDescuentoSvc extends BasicSvc<ProductoInventarioDescuentoDTO, ProductoInventarioDescuentoFilterDTO> {
	
	@Autowired
	private ProductoInventarioDescuentoMapper productoInventarioDescuentoMapper;
	
	// BEGIN region servicesProductoInventarioDescuento
	// END region servicesProductoInventarioDescuento

	@Override
	public ProductoInventarioDescuentoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. ProductoInventarioDescuento");
		ProductoInventarioDescuentoFilterDTO dto = new ProductoInventarioDescuentoFilterDTO();
		dto.setLlaveTabla(llave);
		return productoInventarioDescuentoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = productoInventarioDescuentoMapper;
	}
	
	@Override
	public ProductoInventarioDescuentoDTO activar(ProductoInventarioDescuentoDTO dto, String token) throws ServerException {
		// BEGIN ProductoInventarioDescuento_activar
		return super.activar(dto, token);
		// END ProductoInventarioDescuento_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProductoInventarioDescuentoDTO actualizar( ProductoInventarioDescuentoDTO dto, String token) throws ServerException {
		// BEGIN ProductoInventarioDescuento_actualizar
		if(dto.getProducto().compareTo(dto.getProductoDescontar())==0) throw new ServerException("No se puede generar una composición del mismo producto");
		return super.actualizar(dto, token);
		// END ProductoInventarioDescuento_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProductoInventarioDescuentoDTO inactivar(ProductoInventarioDescuentoDTO dto, String token) throws ServerException {
		// BEGIN ProductoInventarioDescuento_inactivar
		return super.inactivar(dto, token);
		// END ProductoInventarioDescuento_inactivar
	}
	
	@Override
	public ProductoInventarioDescuentoDTO consultaUnica(ProductoInventarioDescuentoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(ProductoInventarioDescuentoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<ProductoInventarioDescuentoDTO> listarConsulta(ProductoInventarioDescuentoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProductoInventarioDescuentoDTO guardar(ProductoInventarioDescuentoDTO dto, String token) throws ServerException {
		// BEGIN ProductoInventarioDescuento_guardar
		dto = super.guardar(dto, token);
		if(dto.getProducto().compareTo(dto.getProductoDescontar())==0) throw new ServerException("No se puede generar una composición del mismo producto");
		return dto;
		// END ProductoInventarioDescuento_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}