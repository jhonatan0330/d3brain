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

	@GetMapping
	public List<TenantPublicDTO> listarTenants() {
		return tenantCatalogService.listarActivos();
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