package d3.shared.application;

import java.util.List;

import org.apache.ibatis.binding.BindingException;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.authentication.application.UsuarioSesionSvc;
import d3.shared.domain.BasicDTO;
import d3.shared.domain.BasicFilterDTO;
import d3.shared.domain.IBasicMapper;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;

public class BasicSvc<T extends BasicDTO, TFilter extends BasicFilterDTO> {

	protected IBasicMapper<T, TFilter> mapper;

	private final UsuarioSesionSvc usuarioSesionService;

	public BasicSvc(@Lazy UsuarioSesionSvc usuarioSesionService) {
		this.usuarioSesionService = usuarioSesionService;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public T actualizar(T dto, String token) throws ServerException {
		usuarioSesionService.getUserFlex(token);
		try {
			mapper.actualizar(dto);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}

	public T consultaUnica(TFilter dto) throws ServerException {
		T result = null;
		try {
			result = mapper.consultar(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return result;
	}

	public T consultaXId(String llave) throws ServerException {
		throw new ServerException("Este metodo debe ser sobreescrito en cada servicio");
	}

	public int contarResultados(TFilter dto) throws ServerException {
		try {
			return mapper.cantidadRegistros(dto);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public T inactivar(T dto, String token) throws ServerException {
		usuarioSesionService.getUserFlex(token);
		dto = consultaXId(dto.getLlaveTabla());
		if (dto == null)
			throw new ServerException("No se identifica el objeto a inactivar");
		if (dto.getEstado().compareTo(SharedConstants.STATE_INACTIVE) == 0)
			throw new ServerException("Este objeto ya se encuentra inactivo");
		dto.setEstado(SharedConstants.STATE_INACTIVE);
		try {
			mapper.actualizar(dto);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}

	public T activar(T dto, String token) throws ServerException {
		usuarioSesionService.getUserFlex(token);
		dto = consultaXId(dto.getLlaveTabla());
		if (dto == null)
			throw new ServerException("No se identifica el objeto a Activar");
		if (dto.getEstado().compareTo(SharedConstants.STATE_ACTIVE) == 0)
			throw new ServerException("Este objeto ya se encuentra Activo");
		dto.setEstado(SharedConstants.STATE_ACTIVE);
		try {
			mapper.actualizar(dto);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}

	public T guardar(T dto, String token) throws ServerException {
		usuarioSesionService.getUserFlex(token);
		dto.setLlaveTabla(generarLlave());
		try {
			mapper.insertar(dto);
		} catch (DuplicateKeyException e) {
			throw e;
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		dto = consultaXId(dto.getLlaveTabla());
		return dto;
	}

	public List<T> listarConsulta(TFilter dto) throws ServerException {
		paginar(dto);
		try {
			return mapper.listar(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public void paginar(TFilter dto) {
		if (dto.getPaginacionRegistroInicial() == null)
			dto.setPaginacionRegistroInicial(0);
		if (dto.getPaginacionRegistroFinal() == null || dto.getPaginacionRegistroFinal() == 0)
			dto.setPaginacionRegistroFinal(200);
	}

	public String generarLlave() {
		return D3Utils.generarLlave();
	}

	public T inactivate(T dto) throws ServerException {
		dto = consultaXId(dto.getLlaveTabla());
		if (dto == null)
			throw new ServerException("No se identifica el objeto a inactivar");
		if (dto.getEstado().compareTo(SharedConstants.STATE_INACTIVE) == 0)
			throw new ServerException("Este objeto ya se encuentra inactivo");
		dto.setEstado(SharedConstants.STATE_INACTIVE);
		try {
			mapper.actualizar(dto);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}

	public T activate(T dto) throws ServerException {
		dto = consultaXId(dto.getLlaveTabla());
		if (dto == null)
			throw new ServerException("No se identifica el objeto a Activar");
		if (dto.getEstado().compareTo(SharedConstants.STATE_ACTIVE) == 0)
			throw new ServerException("Este objeto ya se encuentra Activo");
		dto.setEstado(SharedConstants.STATE_ACTIVE);
		try {
			mapper.actualizar(dto);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}

	public T save(T dto) throws ServerException {
		dto.setLlaveTabla(generarLlave());
		try {
			mapper.insertar(dto);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		dto = consultaXId(dto.getLlaveTabla());
		return dto;
	}

	public T saveSimple(T dto) throws ServerException {
		dto.setLlaveTabla(generarLlave());
		try {
			mapper.insertar(dto);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}

	public T update(T dto) throws ServerException {
		try {
			mapper.actualizar(dto);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}

	public String getUserFlex(String token) throws ServerException {
		return usuarioSesionService.getUserFlex(token);
	}

	public boolean isPublicToken(String token) throws ServerException {
		return usuarioSesionService.isPublicToken(token);
	}
}