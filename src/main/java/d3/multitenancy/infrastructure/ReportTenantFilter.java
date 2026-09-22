package d3.multitenancy.infrastructure;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.filter.OncePerRequestFilter;

import d3.multitenancy.TenantContext;
import d3.multitenancy.application.TenantResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Resuelve el tenant para los reportes públicos. El reporte viaja como
 * prefijo de path: {@code /<nivel1>[/<nivel2>...]/reporte|r/...}. Se resuelve la
 * cadena de niveles, se fija {@link TenantContext} y se reenvía al servlet de
 * reportes ({@code /reporte}) con {@code RequestDispatcher#forward}, que
 * conserva el query string original.
 */
public class ReportTenantFilter extends OncePerRequestFilter {

	private static final Pattern REPORT_PATH = Pattern.compile("^/(.+)/(reporte|r)(/.*)?$");

	private final TenantResolver tenantResolver;

	public ReportTenantFilter(TenantResolver tenantResolver) {
		this.tenantResolver = tenantResolver;
	}

	public static boolean isReportPath(String path) {
		return path != null && REPORT_PATH.matcher(path).matches();
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String path = request.getRequestURI();
		if (!"GET".equalsIgnoreCase(request.getMethod())) {
			filterChain.doFilter(request, response);
			return;
		}
		Matcher matcher = REPORT_PATH.matcher(path);
		if (!matcher.matches()) {
			filterChain.doFilter(request, response);
			return;
		}
		List<String> levels = TenantResolver.split(matcher.group(1));
		List<String> resolved = tenantResolver.resolveLongestPrefix(levels);
		if (resolved.size() != levels.size()) {
			filterChain.doFilter(request, response);
			return;
		}
		String tenantId = String.join(TenantResolver.SEPARATOR, resolved);
		String forwardPath = "/" + matcher.group(2) + (matcher.group(3) != null ? matcher.group(3) : "");
		TenantContext.setCurrentTenant(tenantId);
		try {
			request.getRequestDispatcher(forwardPath).forward(request, response);
		} finally {
			TenantContext.clear();
		}
	}
}