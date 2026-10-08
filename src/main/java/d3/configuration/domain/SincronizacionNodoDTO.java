package d3.configuration.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SincronizacionNodoDTO {

	public static final String CREAR = "CREAR";
	public static final String ACTUALIZAR = "ACTUALIZAR";
	public static final String OMITIR = "OMITIR";

	private TreeNodeDTO nodo;
	private String camino;
	private String accion;
	private Boolean incluirHijos;
	private Boolean crearAncestros;

	public TreeNodeDTO getNodo() {
		return nodo;
	}

	public void setNodo(TreeNodeDTO nodo) {
		this.nodo = nodo;
	}

	public String getCamino() {
		return camino;
	}

	public void setCamino(String camino) {
		this.camino = camino;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public Boolean getIncluirHijos() {
		return incluirHijos;
	}

	public void setIncluirHijos(Boolean incluirHijos) {
		this.incluirHijos = incluirHijos;
	}

	public Boolean getCrearAncestros() {
		return crearAncestros;
	}

	public void setCrearAncestros(Boolean crearAncestros) {
		this.crearAncestros = crearAncestros;
	}

}
