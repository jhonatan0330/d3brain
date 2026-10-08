package d3.configuration.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DiferenciaDTO {

	public static final String CREAR = "CREAR";
	public static final String ACTUALIZAR = "ACTUALIZAR";
	public static final String SIN_DIFERENCIA = "SIN_DIFERENCIA";
	public static final String LOCAL_SIN_REMOTO = "LOCAL_SIN_REMOTO";

	private String tipoDiferencia;
	private TreeNodeDTO nodo;
	private String camino;
	private List<String> camposDiferentes;
	private List<DetalleCampoDTO> detalles;
	private String codigoLocal;
	private String codigoRemoto;
	private String llaveLocal;
	private String llaveRemota;
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

	public List<DetalleCampoDTO> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleCampoDTO> detalles) {
		this.detalles = detalles;
	}

	public String getCodigoLocal() {
		return codigoLocal;
	}

	public void setCodigoLocal(String codigoLocal) {
		this.codigoLocal = codigoLocal;
	}

	public String getCodigoRemoto() {
		return codigoRemoto;
	}

	public void setCodigoRemoto(String codigoRemoto) {
		this.codigoRemoto = codigoRemoto;
	}

	public String getLlaveLocal() {
		return llaveLocal;
	}

	public void setLlaveLocal(String llaveLocal) {
		this.llaveLocal = llaveLocal;
	}

	public String getLlaveRemota() {
		return llaveRemota;
	}

	public void setLlaveRemota(String llaveRemota) {
		this.llaveRemota = llaveRemota;
	}

	public void setHijos(List<DiferenciaDTO> hijos) {
		this.hijos = hijos;
	}

	public List<DiferenciaDTO> getHijos() {
		return hijos;
	}

}
