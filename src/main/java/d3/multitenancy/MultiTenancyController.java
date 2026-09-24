package d3.multitenancy;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import d3.multitenancy.application.TenantCatalogService;
import d3.multitenancy.application.TenantResolver;
import d3.multitenancy.domain.TenantPublicDTO;
import d3.multitenancy.domain.TenantResolveDTO;
import d3.shared.application.SessionContext;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/multi-tenancy")
public class MultiTenancyController {

	private final TenantCatalogService tenantCatalogService;
	private final TenantResolver tenantResolver;

	public MultiTenancyController(TenantCatalogService tenantCatalogService, TenantResolver tenantResolver) {
		this.tenantCatalogService = tenantCatalogService;
		this.tenantResolver = tenantResolver;
	}

	/**
	 * Lista tenants. Con parámetro {@code alcance} retorna los tenants de ese
	 * tenant (el propio más sus hijos). Sin alcance: los visibles para el
	 * usuario autenticado según la tabla tenantusuario_tnu (siempre con
	 * "default"); sin autenticación retorna todos los activos del catálogo.
	 */
	@GetMapping
	public List<TenantPublicDTO> listarTenants(
			@RequestParam(value = "alcance", required = false) String pAlcance) {
		if (pAlcance != null && !pAlcance.isBlank()) {
			return tenantCatalogService.listarAlcance(pAlcance);
		}
		String usuario = SessionContext.getCurrentUserOrNull();
		if (usuario == null) {
			return tenantCatalogService.listarTodos();
		}
		return tenantCatalogService.listarPorUsuario(usuario);
	}

	/**
	 * Resuelve el prefijo de tenant del path (ej: {@code /bytec/pioexpress/main})
	 * en {@code { tenantId: "bytec/pioexpress", rest: "/main" }}. Usado por el SPA
	 * antes del bootstrap para fijar el prefijo persistente de la URL.
	 */
	@GetMapping("/resolve")
	public TenantResolveDTO resolve(@RequestParam("path") String pPath) {
		List<String> segments = TenantResolver.split(pPath);
		List<String> resolved = tenantResolver.resolveLongestPrefix(segments);
		TenantResolveDTO dto = new TenantResolveDTO();
		if (!resolved.isEmpty()) {
			dto.setTenantId(String.join(TenantResolver.SEPARATOR, resolved));
		}
		dto.setRest("/" + String.join(TenantResolver.SEPARATOR, segments.subList(resolved.size(), segments.size())));
		return dto;
	}
}