package com.softure.authorization.application;

import java.util.List;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.authorization.domain.UsuarioRolProductoDTO;
import com.softure.authorization.domain.UsuarioRolProductoFilterDTO;
import com.softure.authorization.infrastructure.UsuarioRolProductoMapper;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.logisticpymes.application.BasicSvc;

import jakarta.annotation.PostConstruct;

@Service("usuarioRolProductoService")
public class UsuarioRolProductoSvc extends BasicSvc<UsuarioRolProductoDTO, UsuarioRolProductoFilterDTO> {
	
	@Autowired @Lazy 
	private UsuarioRolProductoMapper usuarioRolProductoMapper;
	
	// BEGIN region servicesUsuarioRolProducto
	// END region servicesUsuarioRolProducto

	@Override
	public UsuarioRolProductoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. UsuarioRolProducto");
		UsuarioRolProductoFilterDTO dto = new UsuarioRolProductoFilterDTO();
		dto.setLlaveTabla(llave);
		return usuarioRolProductoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = usuarioRolProductoMapper;
	}
	
	@Override
	public UsuarioRolProductoDTO activar(UsuarioRolProductoDTO dto, String token) throws ServerException {
		// BEGIN UsuarioRolProducto_activar
		return super.activar(dto, token);
		// END UsuarioRolProducto_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioRolProductoDTO actualizar( UsuarioRolProductoDTO dto, String token) throws ServerException {
		// BEGIN UsuarioRolProducto_actualizar
		dto.setCantidadPromocionBase(30);
		dto.setModificador(getUserFlex(token));
		return super.actualizar(dto, token);
		// END UsuarioRolProducto_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioRolProductoDTO inactivar(UsuarioRolProductoDTO dto, String token) throws ServerException {
		// BEGIN UsuarioRolProducto_inactivar
		return super.inactivar(dto, token);
		// END UsuarioRolProducto_inactivar
	}
	
	@Override
	public UsuarioRolProductoDTO consultaUnica(UsuarioRolProductoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(UsuarioRolProductoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<UsuarioRolProductoDTO> listarConsulta(UsuarioRolProductoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioRolProductoDTO guardar(UsuarioRolProductoDTO dto, String token) throws ServerException {
		// BEGIN UsuarioRolProducto_guardar
		UsuarioRolProductoFilterDTO existeFilter =  new UsuarioRolProductoFilterDTO();
		existeFilter.setProducto(dto.getProducto());
		existeFilter.setDocumento(dto.getDocumento());
		existeFilter.setEstado(SharedConstants.STATE_ACTIVE);
		UsuarioRolProductoDTO existe = consultaUnica(existeFilter);
		if(existe !=null) throw new ServerException("Este producto ya tiene promocion para este usuario. " + existe.getProductoNombre() );
		if(dto.getNombre()!=null && dto.getNombre().isEmpty()) dto.setNombre(null);
		dto.setModificador(getUserFlex(token));
		return super.guardar(dto, token);
		// END UsuarioRolProducto_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}