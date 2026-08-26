package d3.authorization.application;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.CacheManager;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.authentication.application.UsuarioAutenticacionSvc;
import d3.authorization.domain.UsuarioRolDTO;
import d3.authorization.domain.UsuarioRolFilterDTO;
import d3.authorization.infrastructure.UsuarioRolMapper;
import d3.logisticpymes.application.BasicSvc;
import d3.logisticpymes.application.UsuarioSvc;
import d3.logisticpymes.domain.UsuarioDTO;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import d3.authentication.application.UsuarioSesionSvc;

@Service("usuarioRolService")
public class UsuarioRolSvc extends BasicSvc<UsuarioRolDTO, UsuarioRolFilterDTO> {

	private final UsuarioRolMapper usuarioRolMapper;
	private final UsuarioSvc usuarioService;
	private final UsuarioAutenticacionSvc autenticacionService;
	private final CacheManager cacheService;

	public UsuarioRolSvc(@Lazy UsuarioSesionSvc usuarioSesionService, @Lazy UsuarioRolMapper usuarioRolMapper,
			@Lazy UsuarioSvc usuarioService, @Lazy UsuarioAutenticacionSvc autenticacionService,
			@Lazy CacheManager cacheService) {
		super(usuarioSesionService);
		this.usuarioRolMapper = usuarioRolMapper;
		this.usuarioService = usuarioService;
		this.autenticacionService = autenticacionService;
		this.cacheService = cacheService;
	}

	@Override
	public UsuarioRolDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. UsuarioRol");
		UsuarioRolFilterDTO dto = new UsuarioRolFilterDTO();
		dto.setLlaveTabla(llave);
		return usuarioRolMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = usuarioRolMapper;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioRolDTO actualizar(UsuarioRolDTO dto, String token) throws ServerException {
		throw new ServerException("Lo que se debe actualizar es el documento");
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioRolDTO inactivar(UsuarioRolDTO dto, String token) throws ServerException {
		dto.setFechaFinal(new Date());
		dto.setEstado(SharedConstants.STATE_INACTIVE);
		dto = super.update(dto);
		UsuarioRolFilterDTO filtro = new UsuarioRolFilterDTO();
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		filtro.setUsuario(dto.getUsuario());
		int cont = contarResultados(filtro);
		if (cont == 0) {
			UsuarioDTO usuario = usuarioService.consultaXId(dto.getUsuario());
			if (usuario.getEstado().compareTo(SharedConstants.STATE_INACTIVE) != 0)
				usuarioService.inactivar(usuario, token);
		}
		cacheService.clearUserRoleMap();
		return dto;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioRolDTO guardar(UsuarioRolDTO dto, String token) throws ServerException {
		dto.setFechaInicial(new Date());
		dto = super.guardar(dto, token);
		autenticacionService.crearAutenticacion(dto.getUsuario(), token);
		cacheService.clearUserRoleMap();
		return dto;
	}

}