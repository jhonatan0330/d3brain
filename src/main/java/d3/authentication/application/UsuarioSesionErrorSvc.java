package d3.authentication.application;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.domain.ServerException;
import d3.authentication.domain.UsuarioSesionErrorDTO;
import d3.authentication.domain.UsuarioSesionErrorFilterDTO;
import d3.authentication.infrastructure.UsuarioSesionErrorMapper;
import d3.shared.application.BasicSvc;

import jakarta.annotation.PostConstruct;

@Service("usuarioSesionErrorService")
public class UsuarioSesionErrorSvc extends BasicSvc<UsuarioSesionErrorDTO, UsuarioSesionErrorFilterDTO> {

	private final UsuarioSesionErrorMapper usuarioSesionErrorMapper;

	public UsuarioSesionErrorSvc(
			@Lazy UsuarioSesionErrorMapper usuarioSesionErrorMapper) {
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
	public UsuarioSesionErrorDTO activar(UsuarioSesionErrorDTO dto) throws ServerException {
		return super.activar(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioSesionErrorDTO actualizar(UsuarioSesionErrorDTO dto) throws ServerException {
		return super.actualizar(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioSesionErrorDTO inactivar(UsuarioSesionErrorDTO dto) throws ServerException {
		return super.inactivar(dto);
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
	public UsuarioSesionErrorDTO guardar(UsuarioSesionErrorDTO dto) throws ServerException {
		return super.guardar(dto);
	}


}