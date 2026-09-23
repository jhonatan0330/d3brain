package d3.multitenancy.domain;

/**
 * Información pública de un tenant para la pantalla de selección.
 * No expone credenciales ni configuración de datasource.
 */
public class TenantPublicDTO {

	private String key;

	private String name;

	private String imagen;


	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

}