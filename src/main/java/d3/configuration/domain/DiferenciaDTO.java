package d3.configuration.domain;

import java.util.List;

public class DiferenciaDTO {

	public static final String CREAR = "CREAR";
	public static final String ACTUALIZAR = "ACTUALIZAR";
	public static final String SIN_DIFERENCIA = "SIN_DIFERENCIA";

	private String tipoDiferencia;
	private TreeNodeDTO nodo;
	private String camino;
	private List<String> camposDiferentes;
	private List<DiferenciaDTO> hijos;

	public void setTipoDiferencia(String tipoDiferencia) {
		this.tipoDiferencia = tipoDiferencia;
	}

	public String getTipoDiferencia() {
		return tipoDiferencia;
	}

	public void setNodo(TreeNodeDTO nodo) {
		this.nodo = nodo;
	}

	public TreeNodeDTO getNodo() {
		return nodo;
	}

	public void setCamino(String camino) {
		this.camino = camino;
	}

	public String getCamino() {
		return camino;
	}

	public void setCamposDiferentes(List<String> camposDiferentes) {
		this.camposDiferentes = camposDiferentes;
	}

	public List<String> getCamposDiferentes() {
		return camposDiferentes;
	}

	public void setHijos(List<DiferenciaDTO> hijos) {
		this.hijos = hijos;
	}

	public List<DiferenciaDTO> getHijos() {
		return hijos;
	}

}