package d3.multitenancy.application;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLConnection;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import d3.authentication.application.OrganizacionSvc;
import d3.authentication.domain.OrganizacionDTO;
import d3.multitenancy.domain.PwaManifestDTO;
import d3.multitenancy.domain.PwaManifestIconDTO;
import d3.multitenancy.domain.TenantDTO;
import d3.multitenancy.domain.TenantFilterDTO;
import d3.multitenancy.domain.TenantRegistry;
import d3.multitenancy.infrastructure.TenantMapper;
import d3.shared.domain.SharedConstants;
import d3.users.application.ServidorSvc;

@Service("pwaManifestService")
public class PwaManifestSvc {

	public static final String THEME_COLOR = "#4f46e5";

	public static final String BACKGROUND_COLOR = "#ffffff";

	public static final List<Integer> ICON_SIZES = Arrays.asList(192, 512);

	private static final int MAX_SOURCE_BYTES = 10 * 1024 * 1024;

	private final TenantMapper tenantMapper;

	private final TenantRegistry tenantRegistry;

	private final TenantResolver tenantResolver;

	private final OrganizacionSvc organizacionService;

	private final ServidorSvc servidorService;

	@Value("${tenant.default-name:Principal}")
	private String defaultName;

	private final ConcurrentHashMap<String, byte[]> iconCache = new ConcurrentHashMap<>();

	public PwaManifestSvc(@Lazy TenantMapper tenantMapper, @Lazy TenantRegistry tenantRegistry,
			TenantResolver tenantResolver, @Lazy OrganizacionSvc organizacionService,
			@Lazy ServidorSvc servidorService) {
		this.tenantMapper = tenantMapper;
		this.tenantRegistry = tenantRegistry;
		this.tenantResolver = tenantResolver;
		this.organizacionService = organizacionService;
		this.servidorService = servidorService;
	}

	public PwaManifestDTO construirManifest(String rawTenant, String baseUrl) {
		return construirManifest(rawTenant, baseUrl, null);
	}

	public PwaManifestDTO construirManifest(String rawTenant, String baseUrl, String frontendOrigin) {
		Branding branding = resolverBranding(rawTenant);
		String base = baseUrl == null || baseUrl.isBlank() ? "" : baseUrl.replaceAll("/+$", "");
		String site = normalizarOrigin(frontendOrigin);
		String ruta = branding.isDefault || branding.slug.isBlank() ? "/" : "/" + branding.slug + "/";
		String scope = site + ruta;
		PwaManifestDTO dto = new PwaManifestDTO();
		dto.setName(branding.displayName);
		String compacto = branding.displayName == null ? "D3apps"
				: branding.displayName.replaceAll("\\s+", " ").trim();
		dto.setShortName(compacto.length() > 12 ? compacto.substring(0, 12) : compacto);
		dto.setDescription(branding.description);
		dto.setScope(scope);
		dto.setStartUrl(scope + "?source=pwa");
		dto.setId(scope);
		dto.setDisplay("standalone");
		dto.setOrientation("portrait");
		dto.setThemeColor(THEME_COLOR);
		dto.setBackgroundColor(BACKGROUND_COLOR);
		dto.setIcons(Arrays.asList(
				new PwaManifestIconDTO(iconUrl(base, branding.tenantId, 192), "192x192", "image/png", "any"),
				new PwaManifestIconDTO(iconUrl(base, branding.tenantId, 512), "512x512", "image/png",
						"any maskable")));
		return dto;
	}

	public byte[] icono(String rawTenant, int size) {
		if (!ICON_SIZES.contains(size)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "size no soportado: " + size);
		}
		Branding branding = resolverBranding(rawTenant);
		String llave = branding.tenantId + ":" + size + ":"
				+ (branding.imagen == null ? "-" : String.valueOf(branding.imagen.hashCode()));
		byte[] cached = iconCache.get(llave);
		if (cached != null) {
			return cached;
		}
		byte[] bytes;
		try {
			byte[] fuente = cargarFuente(branding.imagen);
			bytes = fuente == null ? placeholder(size, branding.displayName)
					: redimensionarCuadrado(fuente, size, branding.displayName);
		} catch (Exception e) {
			bytes = placeholder(size, branding.displayName);
		}
		iconCache.put(llave, bytes);
		return bytes;
	}

	private String iconUrl(String base, String tenantId, int size) {
		return base + "/multi-tenancy/icon/" + size + "/" + tenantId;
	}

	static String normalizarOrigin(String origin) {
		if (origin == null || origin.isBlank()) {
			return "";
		}
		String limpio = origin.trim().replaceAll("/+$", "");
		if (!limpio.matches("https?://[^/\\s?#@]+")) {
			return "";
		}
		return limpio;
	}

	private Branding resolverBranding(String rawTenant) {
		String previous = TenantContext.getCurrentTenant();
		try {
			String normalizado = normalizar(rawTenant);
			if (esDefault(normalizado)) {
				TenantContext.setCurrentTenant("default");
				OrganizacionDTO org = obtenerPrincipalSilencioso();
				String nombre = nombreOrg(org, defaultName);
				String imagen = org == null ? null : org.getImagen();
				String descripcion = org == null || org.getSlogan() == null || org.getSlogan().isBlank() ? nombre
						: org.getSlogan();
				return new Branding("default", nombre, descripcion, imagen, "", true);
			}
			List<String> niveles = TenantResolver.split(normalizado);
			Optional<String> cadena = tenantResolver.resolveChain(niveles);
			if (cadena.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown tenant: " + rawTenant);
			}
			String composite = cadena.get();
			if (!tenantRegistry.isRegistered(composite)) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown tenant: " + rawTenant);
			}
			TenantDTO fila = buscarFila(niveles);
			String ultimo = niveles.get(niveles.size() - 1);
			TenantContext.setCurrentTenant(composite);
			OrganizacionDTO org = obtenerPrincipalSilencioso();
			String nombre = org != null && org.getNombre() != null && !org.getNombre().isBlank() ? org.getNombre()
					: fila != null && fila.getName() != null && !fila.getName().isBlank() ? fila.getName() : ultimo;
			String imagen = fila != null && fila.getImagen() != null && !fila.getImagen().isBlank()
					? fila.getImagen()
					: org == null ? null : org.getImagen();
			String descripcion = org != null && org.getSlogan() != null && !org.getSlogan().isBlank()
					? org.getSlogan()
					: nombre;
			String slug = slugify(nombre);
			if (slug.isBlank()) {
				slug = slugify(ultimo);
			}
			return new Branding(composite, nombre, descripcion, imagen, slug, false);
		} finally {
			TenantContext.setCurrentTenant(previous);
		}
	}

	private TenantDTO buscarFila(List<String> niveles) {
		TenantDTO fila = null;
		String contexto = "default";
		StringBuilder compuesto = new StringBuilder();
		for (String nivel : niveles) {
			TenantContext.setCurrentTenant(contexto);
			TenantFilterDTO porId = new TenantFilterDTO();
			porId.setKey(nivel);
			porId.setState(SharedConstants.STATE_ACTIVE);
			TenantDTO encontrado = consultaSilenciosa(porId);
			if (encontrado == null) {
				TenantFilterDTO porCodigo = new TenantFilterDTO();
				porCodigo.setCodigo(nivel);
				porCodigo.setState(SharedConstants.STATE_ACTIVE);
				encontrado = consultaSilenciosa(porCodigo);
			}
			if (encontrado == null) {
				return fila;
			}
			fila = encontrado;
			if (compuesto.length() > 0) {
				compuesto.append("/");
			}
			compuesto.append(nivel);
			contexto = compuesto.toString();
		}
		return fila;
	}

	private TenantDTO consultaSilenciosa(TenantFilterDTO filtro) {
		try {
			return tenantMapper.getOne(filtro);
		} catch (Exception e) {
			return null;
		}
	}

	private OrganizacionDTO obtenerPrincipalSilencioso() {
		try {
			return organizacionService.obtenerPrincipal();
		} catch (Exception e) {
			return null;
		}
	}

	private String nombreOrg(OrganizacionDTO org, String respaldo) {
		if (org == null) {
			return respaldo;
		}
		if (org.getNombre() != null && !org.getNombre().isBlank()) {
			return org.getNombre();
		}
		if (org.getCodigo() != null && !org.getCodigo().isBlank()) {
			return org.getCodigo();
		}
		return respaldo;
	}

	private byte[] cargarFuente(String imagen) throws IOException {
		if (imagen == null || imagen.isBlank()) {
			return null;
		}
		String ruta = imagen.trim();
		if (ruta.startsWith("http://") || ruta.startsWith("https://")) {
			return descargar(ruta);
		}
		String relativo = ruta.replaceAll("^[\\\\/]+", "").replace('/', File.separatorChar).replace('\\',
				File.separatorChar);
		String base;
		try {
			base = servidorService.resolveLocalServer().getBase();
		} catch (Exception e) {
			return null;
		}
		if (base == null || base.isBlank()) {
			return null;
		}
		File archivo = new File(base, relativo);
		if (!archivo.exists() || !archivo.isFile() || archivo.length() == 0
				|| archivo.length() > MAX_SOURCE_BYTES) {
			return null;
		}
		return java.nio.file.Files.readAllBytes(archivo.toPath());
	}

	private byte[] descargar(String url) throws IOException {
		URLConnection conexion = URI.create(url).toURL().openConnection();
		conexion.setConnectTimeout(5000);
		conexion.setReadTimeout(5000);
		try (java.io.InputStream entrada = conexion.getInputStream()) {
			byte[] bytes = entrada.readAllBytes();
			if (bytes.length == 0 || bytes.length > MAX_SOURCE_BYTES) {
				return null;
			}
			return bytes;
		}
	}

	private byte[] redimensionarCuadrado(byte[] fuente, int size, String nombre) throws IOException {
		BufferedImage original;
		try (ByteArrayInputStream entrada = new ByteArrayInputStream(fuente)) {
			original = ImageIO.read(entrada);
		}
		if (original == null) {
			return placeholder(size, nombre);
		}
		int lado = Math.min(original.getWidth(), original.getHeight());
		if (lado <= 0) {
			return placeholder(size, nombre);
		}
		int x = (original.getWidth() - lado) / 2;
		int y = (original.getHeight() - lado) / 2;
		BufferedImage recorte = original.getSubimage(x, y, lado, lado);
		BufferedImage destino = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D lienzo = destino.createGraphics();
		lienzo.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		lienzo.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		lienzo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		lienzo.setColor(Color.WHITE);
		lienzo.fillRect(0, 0, size, size);
		lienzo.drawImage(recorte, 0, 0, size, size, null);
		lienzo.dispose();
		return aPng(destino);
	}

	private byte[] placeholder(int size, String nombre) {
		BufferedImage destino = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D lienzo = destino.createGraphics();
		lienzo.setColor(new Color(0x4f46e5));
		lienzo.fillRect(0, 0, size, size);
		String inicial = "D";
		if (nombre != null && !nombre.trim().isEmpty()) {
			int codePoint = nombre.trim().codePointAt(0);
			if (Character.isLetterOrDigit(codePoint)) {
				inicial = new String(Character.toChars(Character.toUpperCase(codePoint)));
			}
		}
		lienzo.setColor(Color.WHITE);
		lienzo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, size / 2));
		lienzo.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		FontMetrics metricas = lienzo.getFontMetrics();
		int ancho = metricas.stringWidth(inicial);
		int alto = metricas.getAscent() - metricas.getDescent();
		lienzo.drawString(inicial, (size - ancho) / 2, (size + alto) / 2);
		lienzo.dispose();
		try {
			return aPng(destino);
		} catch (IOException e) {
			return new byte[0];
		}
	}

	private byte[] aPng(BufferedImage imagen) throws IOException {
		try (ByteArrayOutputStream salida = new ByteArrayOutputStream()) {
			ImageIO.write(imagen, "png", salida);
			return salida.toByteArray();
		}
	}

	static String normalizar(String raw) {
		if (raw == null) {
			return "";
		}
		return raw.trim().replace('\\', '/').replaceAll("/+", "/").replaceAll("^/|/$", "");
	}

	static boolean esDefault(String normalizado) {
		return normalizado.isEmpty() || "default".equalsIgnoreCase(normalizado);
	}

	static String slugify(String nombre) {
		if (nombre == null) {
			return "";
		}
		String base = Normalizer.normalize(nombre, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
		base = base.toLowerCase(Locale.ROOT).trim().replaceAll("[\\s_]+", "-").replaceAll("[^a-z0-9-]", "")
				.replaceAll("-+", "-").replaceAll("^-+|-+$", "");
		return base;
	}

	static class Branding {
		final String tenantId;
		final String displayName;
		final String description;
		final String imagen;
		final String slug;
		final boolean isDefault;

		Branding(String tenantId, String displayName, String description, String imagen, String slug,
				boolean isDefault) {
			this.tenantId = tenantId;
			this.displayName = displayName == null || displayName.isBlank() ? "D3apps" : displayName.trim();
			this.description = description == null || description.isBlank() ? this.displayName
					: description.trim();
			this.imagen = imagen;
			this.slug = slug == null ? "" : slug;
			this.isDefault = isDefault;
		}
	}

}
