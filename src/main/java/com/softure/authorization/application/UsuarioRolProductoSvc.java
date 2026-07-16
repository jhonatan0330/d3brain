package com.softure.authorization.application;

import java.util.List;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.authorization.domain.UsuarioRolProductoDTO;
import com.softure.authorization.domain.UsuarioRolProductoFilterDTO;
import com.softure.authorization.infrastructure.UsuarioRolProductoMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.logisticpymes.application.BasicSvc;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import com.softure.authentication.application.UsuarioSesionSvc;

@Service("usuarioRolProductoService")
public class UsuarioRolProductoSvc extends BasicSvc<UsuarioRolProductoDTO, UsuarioRolProductoFilterDTO> {

	private final UsuarioRolProductoMapper usuarioRolProductoMapper;

	public UsuarioRolProductoSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy UsuarioRolProductoMapper usuarioRolProductoMapper) {
		super(usuarioSesionService);
		this.usuarioRolProductoMapper = usuarioRolProductoMapper;
	}

	@Override
	public UsuarioRolProductoDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. UsuarioRolProducto");
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
		return super.activar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioRolProductoDTO actualizar(UsuarioRolProductoDTO dto, String token) throws ServerException {
		dto.setCantidadPromocionBase(30);
		dto.setModificador(getUserFlex(token));
		return super.actualizar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioRolProductoDTO inactivar(UsuarioRolProductoDTO dto, String token) throws ServerException {
		return super.inactivar(dto, token);
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
	public List<UsuarioRolProductoDTO> listarConsulta(UsuarioRolProductoFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioRolProductoDTO guardar(UsuarioRolProductoDTO dto, String token) throws ServerException {
		UsuarioRolProductoFilterDTO existeFilter = new UsuarioRolProductoFilterDTO();
		existeFilter.setProducto(dto.getProducto());
		existeFilter.setDocumento(dto.getDocumento());
		existeFilter.setEstado(SharedConstants.STATE_ACTIVE);
		UsuarioRolProductoDTO existe = consultaUnica(existeFilter);
		if (existe != null)
			throw new ServerException(
					"Este producto ya tiene promocion para este usuario. " + existe.getProductoNombre());
		if (dto.getNombre() != null && dto.getNombre().isEmpty())
			dto.setNombre(null);
		dto.setModificador(getUserFlex(token));
		return super.guardar(dto, token);
	}


}