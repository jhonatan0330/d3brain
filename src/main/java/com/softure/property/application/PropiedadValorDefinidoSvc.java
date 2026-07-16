package com.softure.property.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.property.domain.PropiedadValorDefinidoFilterDTO;
import com.softure.property.infrastructure.PropiedadValorDefinidoMapper;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import com.softure.authentication.application.UsuarioSesionSvc;

@Service("propiedadValorDefinidoService")
public class PropiedadValorDefinidoSvc extends BasicSvc<PropiedadValorDefinidoDTO, PropiedadValorDefinidoFilterDTO> {

	private final PropiedadValorDefinidoMapper propiedadValorDefinidoMapper;

	public PropiedadValorDefinidoSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy PropiedadValorDefinidoMapper propiedadValorDefinidoMapper) {
		super(usuarioSesionService);
		this.propiedadValorDefinidoMapper = propiedadValorDefinidoMapper;
	}

	@Override
	public PropiedadValorDefinidoDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. PropiedadValorDefinido");
		PropiedadValorDefinidoFilterDTO dto = new PropiedadValorDefinidoFilterDTO();
		dto.setLlaveTabla(llave);
		return propiedadValorDefinidoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = propiedadValorDefinidoMapper;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PropiedadValorDefinidoDTO actualizar(PropiedadValorDefinidoDTO dto, String token) throws ServerException {
		dto = validarPropiedad(dto);
		return super.actualizar(dto, token);
	}

	public List<PropiedadValorDefinidoDTO> listarPorOrigen(PropiedadValorDefinidoFilterDTO dto) throws ServerException {
		return propiedadValorDefinidoMapper.listarPorOrigen(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PropiedadValorDefinidoDTO guardar(PropiedadValorDefinidoDTO dto, String token) throws ServerException {
		dto = validarPropiedad(dto);
		return super.guardar(dto, token);
	}

	private PropiedadValorDefinidoDTO validarPropiedad(PropiedadValorDefinidoDTO dto) {
		if (dto.getOrigenCategoria() != null && dto.getOrigenCategoria().isEmpty())
			dto.setOrigenCategoria(null);
		return dto;
	}

	public List<PropiedadValorDefinidoDTO> getFullToSynchronize() {
		return propiedadValorDefinidoMapper.getFullToSynchronize();
	}

}