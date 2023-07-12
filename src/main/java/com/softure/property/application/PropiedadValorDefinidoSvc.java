package com.softure.property.application;

import java.util.List;

// BEGIN region interImport
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.property.domain.PropiedadValorDefinidoFilterDTO;
import com.softure.property.infrastructure.PropiedadValorDefinidoMapper;

@Service("propiedadValorDefinidoService")
public class PropiedadValorDefinidoSvc extends BasicSvc<PropiedadValorDefinidoDTO, PropiedadValorDefinidoFilterDTO> {
	
	@Autowired
	private PropiedadValorDefinidoMapper propiedadValorDefinidoMapper;
	
	// BEGIN region servicesPropiedadValorDefinido
	// END region servicesPropiedadValorDefinido

	@Override
	public PropiedadValorDefinidoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. PropiedadValorDefinido");
		PropiedadValorDefinidoFilterDTO dto = new PropiedadValorDefinidoFilterDTO();
		dto.setLlaveTabla(llave);
		return propiedadValorDefinidoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = propiedadValorDefinidoMapper;
	}
	
	@Override
	public PropiedadValorDefinidoDTO activar(PropiedadValorDefinidoDTO dto, String token) throws ServerException {
		// BEGIN PropiedadValorDefinido_activar
		return super.activar(dto, token);
		// END PropiedadValorDefinido_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PropiedadValorDefinidoDTO actualizar( PropiedadValorDefinidoDTO dto, String token) throws ServerException {
		// BEGIN PropiedadValorDefinido_actualizar
		dto = validarPropiedad(dto);
		return super.actualizar(dto, token);
		// END PropiedadValorDefinido_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PropiedadValorDefinidoDTO inactivar(PropiedadValorDefinidoDTO dto, String token) throws ServerException {
		// BEGIN PropiedadValorDefinido_inactivar
		return super.inactivar(dto, token);
		// END PropiedadValorDefinido_inactivar
	}
	
	@Override
	public PropiedadValorDefinidoDTO consultaUnica(PropiedadValorDefinidoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(PropiedadValorDefinidoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<PropiedadValorDefinidoDTO> listarConsulta(PropiedadValorDefinidoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	public List<PropiedadValorDefinidoDTO> listarPorOrigen(PropiedadValorDefinidoFilterDTO dto)throws ServerException{
		// BEGIN region listarPorOrigen
		return propiedadValorDefinidoMapper.listarPorOrigen(dto);
		// END region listarPorOrigen
	}

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PropiedadValorDefinidoDTO guardar(PropiedadValorDefinidoDTO dto, String token) throws ServerException {
		// BEGIN PropiedadValorDefinido_guardar
		dto = validarPropiedad(dto);
		return super.guardar(dto, token);
		// END PropiedadValorDefinido_guardar
	}

// BEGIN region aditionalMethods
	private PropiedadValorDefinidoDTO validarPropiedad(PropiedadValorDefinidoDTO dto) {
		if(dto.getOrigenCategoria()!=null && dto.getOrigenCategoria().isEmpty())dto.setOrigenCategoria(null);
		return dto;
	}
// END region aditionalMethods

	public List<PropiedadValorDefinidoDTO> getFullToSynchronize() {
		return propiedadValorDefinidoMapper.getFullToSynchronize();
	}

}