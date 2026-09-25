package d3.multitenancy.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.multitenancy.domain.TenantDTO;
import d3.multitenancy.domain.TenantFilterDTO;
import d3.multitenancy.domain.TenantPublicDTO;
import d3.multitenancy.domain.TenantRegistry;
import d3.multitenancy.infrastructure.TenantMapper;
import d3.authentication.application.OrganizacionSvc;
import d3.authentication.domain.OrganizacionDTO;
import d3.shared.domain.SharedConstants;

@Service("tenantCatalogService")
public class TenantCatalogService {

	private final TenantMapper tenantMapper;
	private final TenantRegistry tenantRegistry;
	private final OrganizacionSvc organizacionService;

	@Value("${tenant.default-name:Principal}")
	private String defaultName;

	public TenantCatalogService(@Lazy TenantMapper tenantMapper, @Lazy TenantRegistry tenantRegistry,
			@Lazy OrganizacionSvc organizacionService) {
		this.tenantMapper = tenantMapper;
		this.tenantRegistry = tenantRegistry;
		this.organizacionService = organizacionService;
	}

	/**
	 * Lista todos los tenants activos del catálogo maestro. La consulta siempre
	 * se ejecuta contra el tenant "default" (catálogo), sin importar el tenant
	 * del request, y garantiza incluir el tenant "default" aunque no exista
	 * fila. Sin caché.
	 */
	public List<TenantPublicDTO> listarTodos() {
		String previous = TenantContext.getCurrentTenant();
		try {
			TenantContext.setCurrentTenant("default");
			TenantFilterDTO filter = new TenantFilterDTO();
			filter.setState(SharedConstants.STATE_ACTIVE);
			List<TenantPublicDTO> tenants = new ArrayList<>();
			tenantMapper.getMany(filter).forEach(tenant -> {
				TenantPublicDTO dto = new TenantPublicDTO();
				dto.setKey(tenant.getKey());
				dto.setName(tenant.getName());
				dto.setImagen(tenant.getImagen());
				tenants.add(dto);
			});
			agregarPorDefecto(tenants);
			return tenants;
		} finally {
			TenantContext.setCurrentTenant(previous);
		}
	}

	/**
	 * Lista los tenants del alcance indicado: el propio tenant más sus hijos
	 * (filas de {@code tenant_ten} en la BD del tenant), garantizando siempre
	 * el tenant "default". Los hijos se devuelven con key compuesta
	 * ({@code alcance/hijo}). Si el alcance es inválido o es "default",
	 * retorna el catálogo completo. Sin caché.
	 */
	public List<TenantPublicDTO> listarAlcance(String alcance) {
		String normalizado = normalizarAlcance(alcance);
		if (normalizado.isEmpty() || "default".equals(normalizado)
				|| !tenantRegistry.isRegistered(normalizado)) {
			return listarTodos();
		}
		String previous = TenantContext.getCurrentTenant();
		try {
			List<TenantPublicDTO> tenants = new ArrayList<>();
			String segmento = normalizado.contains("/")
					? normalizado.substring(normalizado.lastIndexOf('/') + 1)
					: normalizado;
			String padre = normalizado.contains("/")
					? normalizado.substring(0, normalizado.lastIndexOf('/'))
					: "default";
			TenantContext.setCurrentTenant(padre);
			TenantFilterDTO propio = new TenantFilterDTO();
			propio.setKey(segmento);
			propio.setState(SharedConstants.STATE_ACTIVE);
			TenantDTO fila = tenantMapper.getOne(propio);
			TenantPublicDTO dto = new TenantPublicDTO();
			dto.setKey(normalizado);
			dto.setName(fila != null ? fila.getName() : segmento);
			dto.setImagen(fila != null ? fila.getImagen() : null);
			tenants.add(dto);
			TenantContext.setCurrentTenant(normalizado);
			TenantFilterDTO hijos = new TenantFilterDTO();
			hijos.setState(SharedConstants.STATE_ACTIVE);
			tenantMapper.getMany(hijos).forEach(hijo -> {
				TenantPublicDTO dtoHijo = new TenantPublicDTO();
				dtoHijo.setKey(normalizado + "/" + hijo.getKey());
				dtoHijo.setName(hijo.getName());
				dtoHijo.setImagen(hijo.getImagen());
				tenants.add(dtoHijo);
			});
			agregarPorDefecto(tenants);
			return tenants;
		} finally {
			TenantContext.setCurrentTenant(previous);
		}
	}

	private String normalizarAlcance(String alcance) {
		if (alcance == null) {
			return "";
		}
		return alcance.trim().replaceAll("/+", "/").replaceAll("^/|/$", "");
	}

	/**
	 * Lista los tenants asignados al usuario en la tabla tenantusuario_tnu.
	 * La consulta siempre se ejecuta contra el tenant "default" (catálogo), sin
	 * importar el tenant del request, y garantiza incluir el tenant "default"
	 * aunque no exista asignación. Sin caché: el resultado depende del usuario.
	 */
	public List<TenantPublicDTO> listarPorUsuario(String usuario) {
		String previous = TenantContext.getCurrentTenant();
		try {
			TenantContext.setCurrentTenant("default");
			List<TenantPublicDTO> tenants = new ArrayList<>(tenantMapper.listarPorUsuario(usuario));
			agregarPorDefecto(tenants);
			return tenants;
		} finally {
			TenantContext.setCurrentTenant(previous);
		}
	}

	private void agregarPorDefecto(List<TenantPublicDTO> tenants) {
		TenantPublicDTO defecto = tenantPorDefecto();
		if (tenants.stream().noneMatch(tenant -> defecto.getKey().equals(tenant.getKey()))) {
			tenants.add(defecto);
		}
	}

	/**
	 * Entrada sintética del tenant "default" con los datos de la organización
	 * principal del catálogo: key con la llaveTabla, name con el codigo e
	 * imagen con la imagen. Si no hay organización, respaldo estático con
	 * key "default".
	 */
	private TenantPublicDTO tenantPorDefecto() {
		TenantPublicDTO dto = new TenantPublicDTO();
		dto.setDefecto(true);
		try {
			OrganizacionDTO org = organizacionService.obtenerPrincipal();
			if (org != null && org.getLlaveTabla() != null) {
				dto.setKey(org.getLlaveTabla());
				dto.setName(org.getCodigo() != null ? org.getCodigo() : defaultName);
				dto.setImagen(org.getImagen());
				return dto;
			}
		} catch (Exception e) {
		}
		dto.setKey("default");
		dto.setName(defaultName);
		dto.setImagen(null);
		return dto;
	}

}