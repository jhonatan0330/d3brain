package d3.configuration.domain;

public class SeleccionSincronizacionDTO {

	public static final String CREAR = "CREAR";
	public static final String ACTUALIZAR = "ACTUALIZAR";
	public static final String OMITIR = "OMITIR";

	private String camino;
	private String accion;
	private Boolean incluirHijos;

	public void setCamino(String camino) {
		this.camino = camino;
	}

	public String getCamino() {
		return camino;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getAccion() {
		return accion;
	}

	public void setIncluirHijos(Boolean incluirHijos) {
		this.incluirHijos = incluirHijos;
	}

	public Boolean getIncluirHijos() {
		return incluirHijos;
	}

}