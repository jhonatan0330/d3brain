package d3.users.application;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.authentication.application.UsuarioAutenticacionSvc;
import d3.authentication.application.UsuarioSesionSvc;
import d3.authentication.domain.UsuarioAutenticacionDTO;
import d3.authentication.domain.UsuarioAutenticacionFilterDTO;
import d3.notification.application.ActividadSvc;
import d3.property.application.PropertyCRUDSvc;
import d3.shared.application.BasicSvc;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.users.domain.UsuarioDTO;
import d3.users.domain.UsuarioFilterDTO;
import d3.users.infrastructure.UsuarioMapper;
import jakarta.annotation.PostConstruct;

@Service("usuarioService")
public class UsuarioSvc extends BasicSvc<UsuarioDTO, UsuarioFilterDTO> {

	private final UsuarioMapper usuarioMapper;
	private final ActividadSvc actividadSvc;
	private final UsuarioAutenticacionSvc usuarioAutenticacionSvc;
	private final PropertyCRUDSvc propertySvc;

	public UsuarioSvc(@Lazy UsuarioSesionSvc usuarioSesionService, @Lazy UsuarioMapper usuarioMapper,
			@Lazy ActividadSvc actividadSvc, @Lazy UsuarioAutenticacionSvc usuarioAutenticacionSvc,
			@Lazy PropertyCRUDSvc propertySvc) {
		super(usuarioSesionService);
		this.usuarioMapper = usuarioMapper;
		this.actividadSvc = actividadSvc;
		this.usuarioAutenticacionSvc = usuarioAutenticacionSvc;
		this.propertySvc = propertySvc;
	}

	@Override
	public UsuarioDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Usuario");
		UsuarioFilterDTO dto = new UsuarioFilterDTO();
		dto.setLlaveTabla(llave);
		return usuarioMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = usuarioMapper;
	}

	@Override
	public UsuarioDTO activar(UsuarioDTO dto, String token) throws ServerException {
		return super.activar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioDTO actualizar(UsuarioDTO dto, String token) throws ServerException {
		UsuarioDTO bd = consultaXId(dto.getLlaveTabla());
		// Cambio la clave en caso que el rol tenga credenciales
		if (bd.getIdentificacion().compareTo(dto.getIdentificacion()) != 0) {
			UsuarioAutenticacionFilterDTO autenticacionFilter = new UsuarioAutenticacionFilterDTO();
			autenticacionFilter.setUsuario(dto.getLlaveTabla());
			autenticacionFilter.setEstado(SharedConstants.STATE_ACTIVE);
			UsuarioAutenticacionDTO autenticacion = usuarioAutenticacionSvc.consultaUnica(autenticacionFilter);
			if (autenticacion != null) {
				if (autenticacion.getClave().compareTo(autenticacion.getSesion()) == 0)
					autenticacion.setClave(dto.getIdentificacion());
				autenticacion.setSesion(dto.getIdentificacion());
				usuarioAutenticacionSvc.actualizar(autenticacion, token);
			}
		}
		if (dto.getCorreo() != null)
			dto.setCorreo(dto.getCorreo().toLowerCase());
		return super.actualizar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioDTO inactivar(UsuarioDTO dto, String token) throws ServerException {
		dto = super.inactivar(dto, token);
		actividadSvc.validateActivitiesToInactivateUser(dto.getLlaveTabla());
		propertySvc.inactivateAllPropertiesOfUser(dto.getLlaveTabla(), token);
		UsuarioAutenticacionFilterDTO autenticacionFilter = new UsuarioAutenticacionFilterDTO();
		autenticacionFilter.setUsuario(dto.getLlaveTabla());
		autenticacionFilter.setEstado(SharedConstants.STATE_ACTIVE);
		List<UsuarioAutenticacionDTO> autenticaciones = usuarioAutenticacionSvc.listarConsulta(autenticacionFilter);
		for (UsuarioAutenticacionDTO autenticacion : autenticaciones) {
			autenticacion.setEstado(SharedConstants.STATE_INACTIVE);
			usuarioAutenticacionSvc.inactivar(autenticacion, token);
		}
		return dto;
	}

	@Override
	public UsuarioDTO consultaUnica(UsuarioFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}

	@Override
	public int contarResultados(UsuarioFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}

	@Override
	public List<UsuarioDTO> listarConsulta(UsuarioFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	public List<UsuarioDTO> listarRol(UsuarioFilterDTO dto) throws ServerException {
		if (dto.getRol() == null) {
			if (dto.getEstado() == null)
				dto.setEstado(SharedConstants.STATE_ACTIVE);
			return listarConsulta(dto);
		}
		paginar(dto);
		try {
			return usuarioMapper.listarRol(dto);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	@Override
	public UsuarioDTO guardar(UsuarioDTO dto, String token) throws ServerException {
		UsuarioFilterDTO filtro = new UsuarioFilterDTO();
		filtro.setIdentificacion(dto.getIdentificacion());
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		if (contarResultados(filtro) != 0)
			throw new ServerException("Ya existe ese ID en la BD y esta activo.\n Id : " + dto.getIdentificacion());
		if (dto.getImagen() == null)
			dto.setImagen(SharedConstants.AVATAR);
		if (dto.getCorreo() != null)
			dto.setCorreo(dto.getCorreo().toLowerCase());
		return super.guardar(dto, token);
	}

	public List<UsuarioDTO> getUsersState(String document) throws ServerException {
		return usuarioMapper.getUsersState(document);
	}

	public UsuarioDTO changePicture(String url, String token) throws ServerException {
		UsuarioDTO bd = consultaXId(getUserFlex(token));
		bd.setImagen(url);
		return update(bd);
	}

	public UsuarioDTO getUserByDocument(String pDocument) throws ServerException {
		return usuarioMapper.getUserByDocument(pDocument);
	}

}