package d3.multitenancy.domain;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

import d3.shared.domain.ServerException;

public final class TenantCodigoNormalizer {

	private static final Pattern VALIDO = Pattern.compile("^[a-z][a-z0-9_]{2,29}$");

	private TenantCodigoNormalizer() {
	}

	public static String normalizar(String codigoCrudo) throws ServerException {
		if (codigoCrudo == null || codigoCrudo.isBlank()) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.VALIDAR_CODIGO,
					"El codigo del tenant es obligatorio");
		}
		String base = Normalizer.normalize(codigoCrudo, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
		base = base.toLowerCase(Locale.ROOT).trim().replaceAll("[\\s\\-]+", "_")
				.replaceAll("[^a-z0-9_]", "").replaceAll("_+", "_").replaceAll("^_+|_+$", "");
		if (!VALIDO.matcher(base).matches()) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.VALIDAR_CODIGO,
					"El codigo \"" + codigoCrudo.trim() + "\" no es valido tras normalizar (quedo \""
							+ base + "\"): debe empezar con letra, solo minusculas, numeros y guion bajo, de 3 a 30 caracteres");
		}
		return base;
	}
}
