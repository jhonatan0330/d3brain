package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import com.softure.java.cons.ConstantesGenerales;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.UsuarioRolProductoDTO;
import com.softure.logisticpymes.dto.filter.UsuarioRolProductoFilterDTO;
import com.softure.logisticpymes.persistence.UsuarioRolProductoMapper;

@Service("usuarioRolProductoService")
public class UsuarioRolProductoSvc extends BasicSvc<UsuarioRolProductoDTO, UsuarioRolProductoFilterDTO> {
	
	@Autowired
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
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioRolProductoDTO actualizar( UsuarioRolProductoDTO dto, String token) throws ServerException {
		// BEGIN UsuarioRolProducto_actualizar
		dto.setCantidadPromocionBase(30);
		return super.actualizar(dto, token);
		// END UsuarioRolProducto_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
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
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioRolProductoDTO guardar(UsuarioRolProductoDTO dto, String token) throws ServerException {
		// BEGIN UsuarioRolProducto_guardar
		UsuarioRolProductoFilterDTO existeFilter =  new UsuarioRolProductoFilterDTO();
		existeFilter.setProducto(dto.getProducto());
		existeFilter.setDocumento(dto.getDocumento());
		existeFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		UsuarioRolProductoDTO existe = consultaUnica(existeFilter);
		if(existe !=null) throw new ServerException("Este producto ya tiene promocion para este usuario. " + existe.getProductoNombre() );
		return super.guardar(dto, token);
		// END UsuarioRolProducto_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}