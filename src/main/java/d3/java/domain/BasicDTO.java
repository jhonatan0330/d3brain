package d3.java.domain;

public abstract class BasicDTO {

	private String llaveTabla;
	private String estado;

	public String getLlaveTabla() {
		return llaveTabla;
	}

	public void setLlaveTabla(String llaveTabla) {
		this.llaveTabla = llaveTabla;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstado() {
		return estado;
	}

}
