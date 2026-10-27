package d3.shared.domain;

public abstract class BasicNodoDTO extends BasicParamDTO {

	private String nodoSuperior;
	private String nombre;
	private String codigo;
	private String imagen;
	private Integer prioridad;
	private String tipo;
	private String nodoSuperiorNombre;

	public String getNodoSuperior() {
		return nodoSuperior;
	}

	public void setNodoSuperior(String nodoSuperior) {
		this.nodoSuperior = nodoSuperior;
	}

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

	public Integer getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(Integer prioridad) {
		this.prioridad = prioridad;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNodoSuperiorNombre() {
		return nodoSuperiorNombre;
	}

	public void setNodoSuperiorNombre(String nodoSuperiorNombre) {
		this.nodoSuperiorNombre = nodoSuperiorNombre;
	}

}
