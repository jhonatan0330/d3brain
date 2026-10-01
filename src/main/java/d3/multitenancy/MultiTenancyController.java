package d3.multitenancy;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import d3.multitenancy.application.PwaManifestSvc;
import d3.multitenancy.application.TenantAdminSvc;
import d3.multitenancy.application.TenantCatalogService;
import d3.multitenancy.application.TenantProvisioningSvc;
import d3.multitenancy.application.TenantResolver;
import d3.multitenancy.domain.PwaManifestDTO;
import d3.multitenancy.domain.TenantCrearDTO;
import d3.multitenancy.domain.TenantDTO;
import d3.multitenancy.domain.TenantPublicDTO;
import d3.multitenancy.domain.TenantResolveDTO;
import d3.multitenancy.domain.TenantUsuarioDTO;
import d3.shared.application.SessionContext;
import d3.shared.domain.ServerException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/multi-tenancy")
public class MultiTenancyController {

	private final TenantCatalogService tenantCatalogService;
	private final TenantResolver tenantResolver;
	private final PwaManifestSvc pwaManifestService;
	private final TenantProvisioningSvc tenantProvisioningService;
	private final TenantAdminSvc tenantAdminService;

	public MultiTenancyController(TenantCatalogService tenantCatalogService, TenantResolver tenantResolver,
			PwaManifestSvc pwaManifestService, TenantProvisioningSvc tenantProvisioningService,
			TenantAdminSvc tenantAdminService) {
		this.tenantCatalogService = tenantCatalogService;
		this.tenantResolver = tenantResolver;
		this.pwaManifestService = pwaManifestService;
		this.tenantProvisioningService = tenantProvisioningService;
		this.tenantAdminService = tenantAdminService;
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
	 * Crea un tenant hijo en el tenant actual (padre): registra la fila en
	 * {@code tenant_ten}, crea la base de datos produccion_{codigo} con su rol
	 * d3_{codigo}, ejecuta el script inicial y los deltas desde 2023-07.
	 * Sincrono. Sin reversion: ante fallo responde la fase con
	 * {@code [[PROVISION_*]]} y avisa al administrador por correo.
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public TenantPublicDTO crearTenant(@RequestBody TenantCrearDTO dto) throws ServerException {
		return tenantProvisioningService.crearTenant(dto);
	}

	@GetMapping("/mis-subtenants")
	public List<TenantDTO> misSubtenants() throws ServerException {
		return tenantAdminService.misSubtenants();
	}

	@GetMapping("/{key}")
	public TenantDTO detalleTenant(@PathVariable("key") String pKey) throws ServerException {
		return tenantAdminService.detalle(pKey);
	}

	@PutMapping(value = "/{key}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public TenantDTO actualizarTenant(@PathVariable("key") String pKey, @RequestBody TenantDTO dto)
			throws ServerException {
		return tenantAdminService.actualizar(pKey, dto);
	}

	@GetMapping("/{key}/usuarios")
	public List<TenantUsuarioDTO> listarUsuariosTenant(@PathVariable("key") String pkey) throws ServerException {
		return tenantAdminService.listarUsuarios(pkey);
	}

	@PostMapping(value = "/{key}/usuarios", consumes = MediaType.APPLICATION_JSON_VALUE)
	public TenantUsuarioDTO asignarUsuarioTenant(@PathVariable("key") String pkey,
			@RequestBody Map<String, String> body) throws ServerException {
		String usuario = body != null ? body.get("usuario") : null;
		return tenantAdminService.asignarUsuario(pkey, usuario);
	}

	@DeleteMapping("/{key}/usuarios/{usuarioId}")
	public TenantUsuarioDTO retirarUsuarioTenant(@PathVariable("key") String pkey,
			@PathVariable("usuarioId") String pusuarioId) throws ServerException {
		return tenantAdminService.retirarUsuario(pkey, pusuarioId);
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

	/**
	 * Manifest PWA dinámico por tenant (CU-MT-009). URL estable por tenant para
	 * que el navegador actualice la PWA instalada. Público.
	 */
	@GetMapping(value = "/manifest/{*tenantPath}", produces = "application/manifest+json")
	public ResponseEntity<PwaManifestDTO> manifestPorPath(
			@PathVariable("tenantPath") String pTenantPath,
			@RequestParam(value = "origin", required = false) String pOrigin) {
		return manifestRespuesta(pTenantPath, pOrigin);
	}

	/**
	 * Alias por query del manifest (mismo resultado; útil para el tenant
	 * {@code default} sin prefijo).
	 */
	@GetMapping(value = "/manifest", produces = "application/manifest+json")
	public ResponseEntity<PwaManifestDTO> manifestPorQuery(
			@RequestParam(value = "tenant", required = false) String pTenant,
			@RequestParam(value = "origin", required = false) String pOrigin) {
		return manifestRespuesta(pTenant, pOrigin);
	}

	/**
	 * Icono PWA por tenant con reescalado on-the-fly (CU-MT-010). Público.
	 */
	@GetMapping(value = "/icon/{size}/{*tenantPath}", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> iconoPorPath(@PathVariable("tenantPath") String pTenantPath,
			@PathVariable("size") int pSize) {
		return iconoRespuesta(pTenantPath, pSize);
	}

	/**
	 * Alias por query del icono.
	 */
	@GetMapping(value = "/icon", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> iconoPorQuery(
			@RequestParam(value = "tenant", required = false) String pTenant,
			@RequestParam(value = "size", required = false, defaultValue = "192") int pSize) {
		return iconoRespuesta(pTenant, pSize);
	}

	private ResponseEntity<PwaManifestDTO> manifestRespuesta(String tenant, String origin) {
		String base = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
		PwaManifestDTO dto = pwaManifestService.construirManifest(tenant, base, origin);
		return ResponseEntity.ok().cacheControl(CacheControl.maxAge(Duration.ofHours(1)).cachePublic())
				.body(dto);
	}

	private ResponseEntity<byte[]> iconoRespuesta(String tenant, int size) {
		byte[] png = pwaManifestService.icono(tenant, size);
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).contentLength(png.length)
				.cacheControl(CacheControl.maxAge(Duration.ofDays(1)).cachePublic()).body(png);
	}
}