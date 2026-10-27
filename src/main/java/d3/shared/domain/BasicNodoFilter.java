package d3.shared.domain;

public class BasicNodoFilter extends BasicFilterDTO {

	private String nodoSuperior;
	private String nombre;
	private String codigo;
	private String tipo;

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


	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
