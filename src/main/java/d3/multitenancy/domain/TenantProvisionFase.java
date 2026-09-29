package d3.multitenancy.domain;

import d3.shared.domain.ServerException;

public final class TenantProvisionFase {

	public static final String VALIDAR_CODIGO = "PROVISION_VALIDAR_CODIGO";
	public static final String VERIFICAR_DUPLICADOS = "PROVISION_VERIFICAR_DUPLICADOS";
	public static final String CREAR_ROL = "PROVISION_CREAR_ROL";
	public static final String CREAR_BASE_DATOS = "PROVISION_CREAR_BASE_DATOS";
	public static final String OTORGAR_PERMISOS = "PROVISION_OTORGAR_PERMISOS";
	public static final String SCRIPT_INICIAL = "PROVISION_SCRIPT_INICIAL";
	public static final String SELLO_VERSION = "PROVISION_SELLO_VERSION";
	public static final String SCRIPTS_DELTAS = "PROVISION_SCRIPTS_DELTAS";
	public static final String CREAR_ADMINISTRADOR = "PROVISION_CREAR_ADMINISTRADOR";
	public static final String REGISTRAR_CATALOGO = "PROVISION_REGISTRAR_CATALOGO";
	public static final String ASIGNAR_USUARIO = "PROVISION_ASIGNAR_USUARIO";
	public static final String NOTIFICAR_ACCESO = "PROVISION_NOTIFICAR_ACCESO";
	public static final String FALLO_GENERAL = "PROVISION";

	private TenantProvisionFase() {
	}

	public static ServerException fallar(String fase, String mensaje) {
		return new ServerException(mensaje, fase);
	}

	public static ServerException fallar(String fase, String mensaje, Throwable causa) {
		String detalle = causa != null && causa.getMessage() != null ? causa.getMessage() : "";
		String completo = mensaje;
		if (!detalle.isBlank()) {
			String recortado = detalle;
			if (recortado.contains("Where:")) {
				int inicio = recortado.contains("ERROR:") ? recortado.indexOf("ERROR:") : 0;
				recortado = recortado.substring(inicio, recortado.indexOf("Where:")).trim();
			}
			completo = mensaje + ": " + recortado;
		}
		return new ServerException(completo, fase);
	}
}
