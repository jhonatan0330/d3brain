package com.softure.authentication.application;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.softure.authentication.domain.UsuarioSesionErrorDTO;
import com.softure.authentication.domain.UsuarioSesionErrorFilterDTO;
import com.softure.authentication.infrastructure.UsuarioSesionErrorMapper;
import com.softure.logisticpymes.application.BasicSvc;

import jakarta.annotation.PostConstruct;

@Service("usuarioSesionErrorService")
public class UsuarioSesionErrorSvc extends BasicSvc<UsuarioSesionErrorDTO, UsuarioSesionErrorFilterDTO> {

	private final UsuarioSesionErrorMapper usuarioSesionErrorMapper;

	public UsuarioSesionErrorSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy UsuarioSesionErrorMapper usuarioSesionErrorMapper) {
		super(usuarioSesionService);
		this.usuarioSesionErrorMapper = usuarioSesionErrorMapper;
	}

	@Override
	public UsuarioSesionErrorDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. UsuarioSesionError");
		UsuarioSesionErrorFilterDTO dto = new UsuarioSesionErrorFilterDTO();
		dto.setLlaveTabla(llave);
		return usuarioSesionErrorMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = usuarioSesionErrorMapper;
	}

	@Override
	public UsuarioSesionErrorDTO activar(UsuarioSesionErrorDTO dto, String token) throws ServerException {
		return super.activar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioSesionErrorDTO actualizar(UsuarioSesionErrorDTO dto, String token) throws ServerException {
		return super.actualizar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioSesionErrorDTO inactivar(UsuarioSesionErrorDTO dto, String token) throws ServerException {
		return super.inactivar(dto, token);
	}

	@Override
	public UsuarioSesionErrorDTO consultaUnica(UsuarioSesionErrorFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}

	@Override
	public int contarResultados(UsuarioSesionErrorFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}

	@Override
	public List<UsuarioSesionErrorDTO> listarConsulta(UsuarioSesionErrorFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioSesionErrorDTO guardar(UsuarioSesionErrorDTO dto, String token) throws ServerException {
		return super.guardar(dto, token);
	}


}