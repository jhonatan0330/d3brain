package d3.configuration.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompararArbolRequestDTO {

	private TreeNodeDTO arbol;
	private ArbolConfiguracionFilterDTO filter;

	public TreeNodeDTO getArbol() {
		return arbol;
	}

	public void setArbol(TreeNodeDTO arbol) {
		this.arbol = arbol;
	}

	public ArbolConfiguracionFilterDTO getFilter() {
		return filter;
	}

	public void setFilter(ArbolConfiguracionFilterDTO filter) {
		this.filter = filter;
	}

}