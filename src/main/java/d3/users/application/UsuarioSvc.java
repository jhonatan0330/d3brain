package d3.users.application;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.authentication.application.UsuarioAutenticacionSvc;
import d3.authentication.domain.UsuarioAutenticacionDTO;
import d3.authentication.domain.UsuarioAutenticacionFilterDTO;
import d3.configuration.application.PropertyCRUDSvc;
import d3.notification.application.ActividadSvc;
import d3.shared.application.BasicSvc;
import d3.shared.application.SessionContext;
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

	public UsuarioSvc(@Lazy UsuarioMapper usuarioMapper,
			@Lazy ActividadSvc actividadSvc, @Lazy UsuarioAutenticacionSvc usuarioAutenticacionSvc,
			@Lazy PropertyCRUDSvc propertySvc) {
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
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioDTO actualizar(UsuarioDTO dto) throws ServerException {
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
				usuarioAutenticacionSvc.actualizar(autenticacion);
			}
		}
		if (dto.getCorreo() != null)
			dto.setCorreo(dto.getCorreo().toLowerCase());
		return super.actualizar(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioDTO inactivar(UsuarioDTO dto) throws ServerException {
		dto = super.inactivar(dto);
		actividadSvc.validateActivitiesToInactivateUser(dto.getLlaveTabla());
		propertySvc.inactivateAllPropertiesOfUser(dto.getLlaveTabla());
		UsuarioAutenticacionFilterDTO autenticacionFilter = new UsuarioAutenticacionFilterDTO();
		autenticacionFilter.setUsuario(dto.getLlaveTabla());
		autenticacionFilter.setEstado(SharedConstants.STATE_ACTIVE);
		List<UsuarioAutenticacionDTO> autenticaciones = usuarioAutenticacionSvc.listarConsulta(autenticacionFilter);
		for (UsuarioAutenticacionDTO autenticacion : autenticaciones) {
			autenticacion.setEstado(SharedConstants.STATE_INACTIVE);
			usuarioAutenticacionSvc.inactivar(autenticacion);
		}
		return dto;
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
	public UsuarioDTO guardar(UsuarioDTO dto) throws ServerException {
		UsuarioFilterDTO filtro = new UsuarioFilterDTO();
		filtro.setIdentificacion(dto.getIdentificacion());
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		if (contarResultados(filtro) != 0)
			throw new ServerException("Ya existe ese ID en la BD y esta activo.\n Id : " + dto.getIdentificacion());
		if (dto.getCorreo() != null)
			dto.setCorreo(dto.getCorreo().toLowerCase());
		return super.guardar(dto);
	}

	public List<UsuarioDTO> getUsersState(String document)  {
		return usuarioMapper.getUsersState(document);
	}

	public UsuarioDTO changePicture(String url) throws ServerException {
		UsuarioDTO bd = consultaXId(SessionContext.getCurrentUser());
		bd.setImagen(url);
		return update(bd);
	}

	public UsuarioDTO getUserByDocument(String pDocument) {
		return usuarioMapper.getUserByDocument(pDocument);
	}

}