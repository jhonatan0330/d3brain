package d3.multitenancy.domain;

public class TenantCrearDTO {

	private String nombre;

	private String codigo;

	private String imagen;

	private String adminNombre;

	private String adminIdentificacion;

	private String adminCorreo;

	private String adminTelefono;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getAdminNombre() {
		return adminNombre;
	}

	public void setAdminNombre(String adminNombre) {
		this.adminNombre = adminNombre;
	}

	public String getAdminIdentificacion() {
		return adminIdentificacion;
	}

	public void setAdminIdentificacion(String adminIdentificacion) {
		this.adminIdentificacion = adminIdentificacion;
	}

	public String getAdminCorreo() {
		return adminCorreo;
	}

	public void setAdminCorreo(String adminCorreo) {
		this.adminCorreo = adminCorreo;
	}

	public String getAdminTelefono() {
		return adminTelefono;
	}

	public void setAdminTelefono(String adminTelefono) {
		this.adminTelefono = adminTelefono;
	}
}
