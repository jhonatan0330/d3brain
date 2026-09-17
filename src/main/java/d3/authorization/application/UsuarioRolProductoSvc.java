package d3.authorization.application;

import java.util.List;

import d3.shared.domain.SharedConstants;
import d3.shared.domain.ServerException;
import d3.authorization.domain.UsuarioRolProductoDTO;
import d3.authorization.domain.UsuarioRolProductoFilterDTO;
import d3.authorization.infrastructure.UsuarioRolProductoMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.application.BasicSvc;
import d3.shared.application.SessionContext;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;

@Service("usuarioRolProductoService")
public class UsuarioRolProductoSvc extends BasicSvc<UsuarioRolProductoDTO, UsuarioRolProductoFilterDTO> {

	private final UsuarioRolProductoMapper usuarioRolProductoMapper;

	public UsuarioRolProductoSvc(
			@Lazy UsuarioRolProductoMapper usuarioRolProductoMapper) {
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
	public UsuarioRolProductoDTO activar(UsuarioRolProductoDTO dto) throws ServerException {
		return super.activar(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioRolProductoDTO actualizar(UsuarioRolProductoDTO dto) throws ServerException {
		dto.setCantidadPromocionBase(30);
		dto.setModificador(SessionContext.getCurrentUser());
		return super.actualizar(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioRolProductoDTO inactivar(UsuarioRolProductoDTO dto) throws ServerException {
		return super.inactivar(dto);
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
	public UsuarioRolProductoDTO guardar(UsuarioRolProductoDTO dto) throws ServerException {
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
		dto.setModificador(SessionContext.getCurrentUser());
		return super.guardar(dto);
	}


}