package d3.java.services;

import jakarta.servlet.http.HttpServletRequest;

public class HttpUtils {

	private static final String[] IP_HEADERS = { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
			"HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
			"HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };

	private HttpUtils() {
	}

	public static String getRequestIP(HttpServletRequest request) {
		for (String headerIp : IP_HEADERS) {
			String value = request.getHeader(headerIp);
			if (value == null || value.isEmpty()) {
				continue;
			}
			String[] parts = value.split("\\s*,\\s*");
			return parts[0];
		}
		return request.getRemoteAddr();
	}
}
