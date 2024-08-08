package com.softure.inventory.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.softure.inventory.domain.DetalleCaracteristicaProductoDTO;
import com.softure.inventory.domain.DetalleCaracteristicaProductoFilterDTO;
import com.softure.inventory.infrastructure.DetalleCaracteristicaProductoMapper;
import com.softure.logisticpymes.application.BasicSvc;

import jakarta.annotation.PostConstruct;

@Service("detalleCaracteristicaProductoService")
public class DetalleCaracteristicaProductoSvc extends BasicSvc<DetalleCaracteristicaProductoDTO, DetalleCaracteristicaProductoFilterDTO> {
	
	@Autowired @Lazy 
	private DetalleCaracteristicaProductoMapper detalleCaracteristicaProductoMapper;
	
	// BEGIN region servicesDetalleCaracteristicaProducto
	// END region servicesDetalleCaracteristicaProducto

	@Override
	public DetalleCaracteristicaProductoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. DetalleCaracteristicaProducto");
		DetalleCaracteristicaProductoFilterDTO dto = new DetalleCaracteristicaProductoFilterDTO();
		dto.setLlaveTabla(llave);
		return detalleCaracteristicaProductoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = detalleCaracteristicaProductoMapper;
	}
	
	@Override
	public DetalleCaracteristicaProductoDTO activar(DetalleCaracteristicaProductoDTO dto, String token) throws ServerException {
		// BEGIN DetalleCaracteristicaProducto_activar
		return super.activate(dto);
		// END DetalleCaracteristicaProducto_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DetalleCaracteristicaProductoDTO actualizar( DetalleCaracteristicaProductoDTO dto, String token) throws ServerException {
		// BEGIN DetalleCaracteristicaProducto_actualizar
		return update(dto);
		// END DetalleCaracteristicaProducto_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DetalleCaracteristicaProductoDTO inactivar(DetalleCaracteristicaProductoDTO dto, String token) throws ServerException {
		// BEGIN DetalleCaracteristicaProducto_inactivar
		if(dto.getTransaccionInactivo()==null) throw new ServerException("Es necesario registrar la transaccion que anula");
		return inactivate(dto);
		// END DetalleCaracteristicaProducto_inactivar
	}
	
	@Override
	public DetalleCaracteristicaProductoDTO consultaUnica(DetalleCaracteristicaProductoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(DetalleCaracteristicaProductoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<DetalleCaracteristicaProductoDTO> listarConsulta(DetalleCaracteristicaProductoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DetalleCaracteristicaProductoDTO guardar(DetalleCaracteristicaProductoDTO dto, String token) throws ServerException {
		// BEGIN DetalleCaracteristicaProducto_guardar
		return save(dto);
		// END DetalleCaracteristicaProducto_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}