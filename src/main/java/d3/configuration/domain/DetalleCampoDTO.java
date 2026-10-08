package d3.configuration.domain;

public class DetalleCampoDTO {

	private String campo;
	private Object valorLocal;
	private Object valorRemoto;

	public DetalleCampoDTO() {
	}

	public DetalleCampoDTO(String campo, Object valorLocal, Object valorRemoto) {
		this.campo = campo;
		this.valorLocal = valorLocal;
		this.valorRemoto = valorRemoto;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public Object getValorLocal() {
		return valorLocal;
	}

	public void setValorLocal(Object valorLocal) {
		this.valorLocal = valorLocal;
	}

	public Object getValorRemoto() {
		return valorRemoto;
	}

	public void setValorRemoto(Object valorRemoto) {
		this.valorRemoto = valorRemoto;
	}

}
