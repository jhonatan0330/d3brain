package com.softure.inventory.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.softure.inventory.domain.ProductoCaracteristicaDTO;
import com.softure.inventory.domain.ProductoCaracteristicaFilterDTO;
import com.softure.inventory.domain.ProductoDTO;
import com.softure.inventory.infrastructure.ProductoCaracteristicaMapper;
import com.softure.logisticpymes.application.BasicSvc;

import jakarta.annotation.PostConstruct;

@Service("productoCaracteristicaService")
public class ProductoCaracteristicaSvc extends BasicSvc<ProductoCaracteristicaDTO, ProductoCaracteristicaFilterDTO> {
	
	@Autowired @Lazy 
	private ProductoCaracteristicaMapper productoCaracteristicaMapper;
	
	// BEGIN region servicesProductoCaracteristica
	// END region servicesProductoCaracteristica

	@Override
	public ProductoCaracteristicaDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. ProductoCaracteristica");
		ProductoCaracteristicaFilterDTO dto = new ProductoCaracteristicaFilterDTO();
		dto.setLlaveTabla(llave);
		return productoCaracteristicaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = productoCaracteristicaMapper;
	}
	
	@Override
	public ProductoCaracteristicaDTO activar(ProductoCaracteristicaDTO dto, String token) throws ServerException {
		// BEGIN ProductoCaracteristica_activar
		return super.activar(dto, token);
		// END ProductoCaracteristica_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProductoCaracteristicaDTO actualizar( ProductoCaracteristicaDTO dto, String token) throws ServerException {
		// BEGIN ProductoCaracteristica_actualizar
		return super.actualizar(dto, token);
		// END ProductoCaracteristica_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProductoCaracteristicaDTO inactivar(ProductoCaracteristicaDTO dto, String token) throws ServerException {
		// BEGIN ProductoCaracteristica_inactivar
		return super.inactivar(dto, token);
		// END ProductoCaracteristica_inactivar
	}
	
	@Override
	public ProductoCaracteristicaDTO consultaUnica(ProductoCaracteristicaFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(ProductoCaracteristicaFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<ProductoCaracteristicaDTO> listarConsulta(ProductoCaracteristicaFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProductoCaracteristicaDTO guardar(ProductoCaracteristicaDTO dto, String token) throws ServerException {
		// BEGIN ProductoCaracteristica_guardar
		return super.guardar(dto, token);
		// END ProductoCaracteristica_guardar
	}

// BEGIN region aditionalMethods
	public List<ProductoCaracteristicaDTO> listarProductoSimplificar(List<ProductoDTO> productos) throws ServerException{
		return productoCaracteristicaMapper.listarProductoSimplificado(productos);
	}
// END region aditionalMethods

}