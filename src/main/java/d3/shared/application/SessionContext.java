package d3.shared.application;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedToken;

public final class SessionContext {

	private static final ThreadLocal<SharedToken> CURRENT = new ThreadLocal<>();

	private SessionContext() {
	}

	public static void setCurrent(SharedToken token) {
		CURRENT.set(token);
	}

	public static SharedToken getCurrent() {
		return CURRENT.get();
	}

	public static String getCurrentUser() throws ServerException {
		SharedToken token = CURRENT.get();
		if (token == null)
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		return token.getUser();
	}

	public static String getCurrentUserOrNull() {
		SharedToken token = CURRENT.get();
		if (token == null)
			return null;
		return token.getUser();
	}

	public static String getCurrentToken() throws ServerException {
		SharedToken token = CURRENT.get();
		if (token == null)
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		return token.getToken();
	}

	public static String getCurrentTokenOrNull() {
		SharedToken token = CURRENT.get();
		if (token == null)
			return null;
		return token.getToken();
	}
	
	public static void clear() {
		CURRENT.remove();
	}
}