package d3.configuration.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import d3.shared.domain.BasicNodoDTO;

@JsonInclude(Include.NON_NULL)
public class NodoArbolDTO extends BasicNodoDTO {

	private String categoria;
	private String camino;
	private Boolean tieneHijos;
	private Integer totalHijos;
	private List<NodoArbolDTO> hijos;

	private String att1;
	private String att2;
	private String att3;

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getCamino() {
		return camino;
	}

	public void setCamino(String camino) {
		this.camino = camino;
	}

	public Boolean getTieneHijos() {
		return tieneHijos;
	}

	public void setTieneHijos(Boolean tieneHijos) {
		this.tieneHijos = tieneHijos;
	}

	public Integer getTotalHijos() {
		return totalHijos;
	}

	public void setTotalHijos(Integer totalHijos) {
		this.totalHijos = totalHijos;
	}

	public List<NodoArbolDTO> getHijos() {
		return hijos;
	}

	public void setHijos(List<NodoArbolDTO> hijos) {
		this.hijos = hijos;
	}

	public String getAtt1() {
		return att1;
	}

	public void setAtt1(String att1) {
		this.att1 = att1;
	}

	public String getAtt2() {
		return att2;
	}

	public void setAtt2(String att2) {
		this.att2 = att2;
	}

	public String getAtt3() {
		return att3;
	}

	public void setAtt3(String att3) {
		this.att3 = att3;
	}

}
