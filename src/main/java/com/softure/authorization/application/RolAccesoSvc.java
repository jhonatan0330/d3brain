package com.softure.authorization.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.CacheManager;
import com.softure.authentication.application.OrganizacionSvc;
import com.softure.authorization.domain.RolAccesoDTO;
import com.softure.authorization.domain.RolAccesoFilterDTO;
import com.softure.authorization.domain.UsuarioRolFilterDTO;
import com.softure.authorization.infrastructure.RolAccesoMapper;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.property.application.PropertyCRUDSvc;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import com.softure.authentication.application.UsuarioSesionSvc;

@Service("rolAccesoService")
public class RolAccesoSvc extends BasicSvc<RolAccesoDTO, RolAccesoFilterDTO> {

	private final RolAccesoMapper rolAccesoMapper;
	private final UsuarioRolSvc usuarioRolService;
	private final PropertyCRUDSvc propertySvc;
	private final OrganizacionSvc organizationSvc;
	private final CacheManager cacheManager;

	public RolAccesoSvc(@Lazy UsuarioSesionSvc usuarioSesionService, @Lazy RolAccesoMapper rolAccesoMapper,
			@Lazy UsuarioRolSvc usuarioRolService, @Lazy PropertyCRUDSvc propertySvc,
			@Lazy OrganizacionSvc organizationSvc, @Lazy CacheManager cacheManager) {
		super(usuarioSesionService);
		this.rolAccesoMapper = rolAccesoMapper;
		this.usuarioRolService = usuarioRolService;
		this.propertySvc = propertySvc;
		this.organizationSvc = organizationSvc;
		this.cacheManager = cacheManager;
	}

	@Override
	public RolAccesoDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. RolAcceso");
		RolAccesoDTO _db = cacheManager.getRole(llave);
		if (_db != null)
			return _db;

		RolAccesoFilterDTO dto = new RolAccesoFilterDTO();
		dto.setLlaveTabla(llave);
		_db = rolAccesoMapper.consultar(dto);
		cacheManager.putRole(llave, _db);
		return _db;
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = rolAccesoMapper;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public RolAccesoDTO inactivar(RolAccesoDTO dto, String token) throws ServerException {
		dto = super.inactivar(dto, token);
		UsuarioRolFilterDTO filtro = new UsuarioRolFilterDTO();
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		filtro.setRolAcceso(dto.getLlaveTabla());
		int cont = usuarioRolService.contarResultados(filtro);
		if (cont != 0)
			throw new ServerException("No se puede inactivar el rol debido a que tiene usuarios activos. " + cont);
		propertySvc.inactivateAllPropertiesOfRol(dto.getLlaveTabla(), token);
		cacheManager.clearRolesMap();
		return dto;
	}

	public List<RolAccesoDTO> consultaUsuarioDocumento(String userId) throws ServerException {
		try {
			return rolAccesoMapper.consultaUsuarioDocumento(userId);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public boolean usuarioPermisosCompletos(String token) throws ServerException {
		String user = getUserFlex(token);
		if (user.compareTo("PROCESS") == 0)
			return true;
		return organizationSvc.permisosCompletos(user);
	}

	public boolean usuarioPermisosAuditor(String token) throws ServerException {
		String user = getUserFlex(token);
		if (user.compareTo("PROCESS") == 0)
			return true;
		return organizationSvc.permisosAuditor(user);
	}

	public List<RolAccesoDTO> getFullToSynchronize(List<String> process) {
		return rolAccesoMapper.getFullToSynchronize(process);
	}

}