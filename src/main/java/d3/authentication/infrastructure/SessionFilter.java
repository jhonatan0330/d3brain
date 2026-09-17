package d3.authentication.infrastructure;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import d3.shared.application.SessionContext;
import d3.shared.application.SharedTokenService;
import d3.shared.domain.ServerException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SessionFilter extends OncePerRequestFilter {

	private final SharedTokenService sharedTokenService;

	public SessionFilter(SharedTokenService sharedTokenService) {
		this.sharedTokenService = sharedTokenService;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		return path.startsWith("/static/") || path.startsWith("/error");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = resolveToken(request);
		if (token != null) {
			try {
				SessionContext.setCurrent(sharedTokenService.validate(token, request));
			} catch (ServerException e) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
				return;
			}
		}
		try {
			filterChain.doFilter(request, response);
		} finally {
			SessionContext.clear();
		}
	}

	private String resolveToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (header == null || header.isBlank())
			return null;
		String value = header;
		if (value.regionMatches(true, 0, "Bearer ", 0, 7))
			value = value.substring(7);
		value = value.trim();
		return value.isEmpty() ? null : value;
	}
}