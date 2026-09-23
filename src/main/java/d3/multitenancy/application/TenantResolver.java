package d3.multitenancy.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import d3.multitenancy.domain.TenantDTO;
import d3.multitenancy.domain.TenantFilterDTO;
import d3.multitenancy.infrastructure.TenantMapper;
import d3.shared.domain.SharedConstants;

/**
 * Resuelve la cadena de tenants multi-nivel a partir de segmentos de path o del
 * header {@code X-Tenant-ID}. Cada nivel se busca en la base de datos del nivel
 * padre; el primer nivel se busca en el catálogo maestro ("default"). El id de
 * tenant resultante es el "composite" unido con "/" (ej: "a", "a/b", "a/b/c").
 */
@Component
public class TenantResolver {

	public static final String SEPARATOR = "/";
	private static final Pattern VALID_SEGMENT = Pattern.compile("[A-Za-z0-9_\\-]{1,32}");

	private final TenantMetadataProvider metadataProvider;
	private final TenantRegistry tenantRegistry;
	private final TenantMapper tenantMapper;

	public TenantResolver(TenantMetadataProvider metadataProvider, TenantRegistry tenantRegistry,
			@Lazy TenantMapper tenantMapper) {
		this.metadataProvider = metadataProvider;
		this.tenantRegistry = tenantRegistry;
		this.tenantMapper = tenantMapper;
	}

	/**
	 * Resuelve la cadena completa (todos los niveles deben existir). Devuelve el id
	 * composite final o vacío si algún nivel no resuelve.
	 */
	public Optional<String> resolveChain(List<String> levels) {
		List<String> resolved = resolveLongestPrefix(levels);
		if (resolved.size() != levels.size()) {
			return Optional.empty();
		}
		return Optional.of(String.join(SEPARATOR, resolved));
	}

	/**
	 * Recorre los segmentos resolviendo el prefijo de tenant más largo posible y
	 * registrando cada nivel como "composite". Los segmentos no consumidos quedan
	 * como ruta de la aplicación.
	 */
	public List<String> resolveLongestPrefix(List<String> segments) {
		List<String> resolved = new ArrayList<>();
		if (segments == null || segments.isEmpty()) {
			return resolved;
		}
		String previous = TenantContext.getCurrentTenant();
		try {
			String context = "default";
			for (String segment : segments) {
				if (!isValidSegment(segment)) {
					break;
				}
				TenantContext.setCurrentTenant(context);
				TenantFilterDTO filter = new TenantFilterDTO();
				filter.setKey(segment);
				filter.setState(SharedConstants.STATE_ACTIVE);
				TenantDTO tenant = tenantMapper.getOne(filter);
				if (tenant == null) {
					break;
				}
				resolved.add(segment);
				String composite = String.join(SEPARATOR, resolved);
				tenant.setKey(composite);
				register(composite, tenant);
				context = composite;
			}
		} catch (Exception ex) {
			System.err.println("No se pudo resolver el tenant desde el path: " + ex.getMessage());
			resolved.clear();
		} finally {
			TenantContext.setCurrentTenant(previous);
		}
		return resolved;
	}

	public Optional<String> resolveFromPath(String path) {
		List<String> segments = split(path);
		List<String> resolved = resolveLongestPrefix(segments);
		if (resolved.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(String.join(SEPARATOR, resolved));
	}

	public String resolveFromHeader(String raw) {
		Optional<String> resolved = resolveChain(split(raw));
		return resolved.orElse(raw.trim());
	}

	private void register(String composite, TenantDTO tenant) {
		if (metadataProvider instanceof DatabaseTenantMetadataProvider provider) {
			provider.register(tenant);
		}
		if (tenantRegistry instanceof DatabaseTenantRegistry registry) {
			registry.register(composite);
		}
	}

	private boolean isValidSegment(String segment) {
		return segment != null && VALID_SEGMENT.matcher(segment).matches();
	}

	public static List<String> split(String value) {
		List<String> result = new ArrayList<>();
		if (value == null) {
			return result;
		}
		for (String part : value.split(SEPARATOR)) {
			if (!part.isBlank()) {
				result.add(part.trim());
			}
		}
		return result;
	}
}