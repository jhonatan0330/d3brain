package d3.multitenancy.application;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.multitenancy.domain.TenantDTO;
import d3.multitenancy.domain.TenantFilterDTO;
import d3.multitenancy.domain.TenantUsuarioDTO;
import d3.multitenancy.domain.TenantUsuarioFilterDTO;
import d3.multitenancy.infrastructure.TenantMapper;
import d3.multitenancy.infrastructure.TenantUsuarioMapper;
import d3.shared.application.D3Utils;
import d3.shared.application.SessionContext;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;

@Service("tenantAdminSvc")
public class TenantAdminSvc {

	private final TenantMapper tenantMapper;
	private final TenantUsuarioMapper tenantUsuarioMapper;

	public TenantAdminSvc(@Lazy TenantMapper tenantMapper, @Lazy TenantUsuarioMapper tenantUsuarioMapper) {
		this.tenantMapper = tenantMapper;
		this.tenantUsuarioMapper = tenantUsuarioMapper;
	}

	public List<TenantDTO> misSubtenants() throws ServerException {
		exigirSesion();
		TenantFilterDTO filter = new TenantFilterDTO();
		return tenantMapper.getMany(filter);
	}

	public TenantDTO detalle(String key) throws ServerException {
		exigirSesion();
		if (key == null || key.isBlank()) {
			throw new ServerException("La llave del subtenant se encuentra vacia");
		}
		TenantFilterDTO filter = new TenantFilterDTO();
		filter.setKey(key.trim());
		TenantDTO fila = tenantMapper.getOne(filter);
		if (fila == null) {
			throw new ServerException("El subtenant no existe o no pertenece a este tenant");
		}
		return fila;
	}

	public TenantDTO actualizar(String key, TenantDTO cambios) throws ServerException {
		TenantDTO fila = detalle(key);
		if (cambios != null) {
			if (cambios.getName() != null && !cambios.getName().isBlank()) {
				fila.setName(cambios.getName().trim());
			}
			if (cambios.getImagen() != null) {
				fila.setImagen(cambios.getImagen());
			}
			if (cambios.getState() != null && !cambios.getState().isBlank()) {
				if (!SharedConstants.STATE_ACTIVE.equals(cambios.getState())
						&& !SharedConstants.STATE_INACTIVE.equals(cambios.getState())) {
					throw new ServerException("El estado del subtenant debe ser A o I");
				}
				fila.setState(cambios.getState());
			}
			fila.setFechaValidez(cambios.getFechaValidez());
		}
		return tenantMapper.update(fila);
	}

	public List<TenantUsuarioDTO> listarUsuarios(String key) throws ServerException {
		detalle(key);
		String anterior = TenantContext.getCurrentTenant();
		try {
			TenantContext.setCurrentTenant("default");
			TenantUsuarioFilterDTO filter = new TenantUsuarioFilterDTO();
			filter.setTenant(key.trim());
			filter.setState(SharedConstants.STATE_ACTIVE);
			return tenantUsuarioMapper.getMany(filter);
		} finally {
			TenantContext.setCurrentTenant(anterior);
		}
	}

	public TenantUsuarioDTO asignarUsuario(String key, String usuario) throws ServerException {
		detalle(key);
		if (usuario == null || usuario.isBlank()) {
			throw new ServerException("El usuario a asignar es obligatorio");
		}
		String anterior = TenantContext.getCurrentTenant();
		try {
			TenantContext.setCurrentTenant("default");
			TenantUsuarioFilterDTO existente = new TenantUsuarioFilterDTO();
			existente.setUsuario(usuario.trim());
			existente.setTenant(key.trim());
			TenantUsuarioDTO fila = tenantUsuarioMapper.getOne(existente);
			if (fila != null) {
				if (SharedConstants.STATE_ACTIVE.equals(fila.getState())) {
					return fila;
				}
				fila.setState(SharedConstants.STATE_ACTIVE);
				return tenantUsuarioMapper.update(fila);
			}
			TenantUsuarioDTO dto = new TenantUsuarioDTO();
			dto.setKey(D3Utils.generarLlave());
			dto.setUsuario(usuario.trim());
			dto.setTenant(key.trim());
			dto.setState(SharedConstants.STATE_ACTIVE);
			return tenantUsuarioMapper.insert(dto);
		} finally {
			TenantContext.setCurrentTenant(anterior);
		}
	}

	public TenantUsuarioDTO retirarUsuario(String key, String usuarioId) throws ServerException {
		detalle(key);
		if (usuarioId == null || usuarioId.isBlank()) {
			throw new ServerException("El usuario a retirar es obligatorio");
		}
		String anterior = TenantContext.getCurrentTenant();
		try {
			TenantContext.setCurrentTenant("default");
			TenantUsuarioFilterDTO filter = new TenantUsuarioFilterDTO();
			filter.setUsuario(usuarioId.trim());
			filter.setTenant(key.trim());
			TenantUsuarioDTO fila = tenantUsuarioMapper.getOne(filter);
			if (fila == null) {
				throw new ServerException("El usuario no se encuentra asignado a este subtenant");
			}
			fila.setState(SharedConstants.STATE_INACTIVE);
			return tenantUsuarioMapper.update(fila);
		} finally {
			TenantContext.setCurrentTenant(anterior);
		}
	}

	public TenantDTO filaParaLogin(String composite) {
		if (composite == null || composite.isBlank() || !composite.contains("/")) {
			return null;
		}
		String padre = composite.substring(0, composite.lastIndexOf('/'));
		String hijo = composite.substring(composite.lastIndexOf('/') + 1);
		if (padre.isBlank() || hijo.isBlank()) {
			return null;
		}
		String anterior = TenantContext.getCurrentTenant();
		try {
			TenantContext.setCurrentTenant(padre);
			TenantFilterDTO filter = new TenantFilterDTO();
			filter.setKey(hijo);
			return tenantMapper.getOne(filter);
		} catch (Exception e) {
			return null;
		} finally {
			TenantContext.setCurrentTenant(anterior);
		}
	}

	public void validarVigencia(TenantDTO fila) throws ServerException {
		if (fila == null) {
			return;
		}
		if (!SharedConstants.STATE_ACTIVE.equals(fila.getState())) {
			throw new ServerException("Subtenant bloqueado por validez");
		}
		if (fila.getFechaValidez() != null) {
			LocalDate hoy = LocalDate.now(ZoneId.of("America/Bogota"));
			Date validez = fila.getFechaValidez();
			LocalDate limite = validez.toInstant().atZone(ZoneId.of("America/Bogota")).toLocalDate();
			if (limite.isBefore(hoy)) {
				throw new ServerException("Subtenant bloqueado por validez");
			}
		}
	}

	private void exigirSesion() throws ServerException {
		String usuario = SessionContext.getCurrentUserOrNull();
		if (usuario == null || usuario.isBlank()) {
			throw new ServerException("Se requiere un usuario autenticado para administrar tenants");
		}
	}

}