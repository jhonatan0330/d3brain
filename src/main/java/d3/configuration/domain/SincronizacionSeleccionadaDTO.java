package d3.configuration.domain;

import java.util.List;

public class SincronizacionSeleccionadaDTO {

	private TreeNodeDTO arbol;
	private List<SeleccionSincronizacionDTO> selecciones;

	public void setArbol(TreeNodeDTO arbol) {
		this.arbol = arbol;
	}

	public TreeNodeDTO getArbol() {
		return arbol;
	}

	public void setSelecciones(List<SeleccionSincronizacionDTO> selecciones) {
		this.selecciones = selecciones;
	}

	public List<SeleccionSincronizacionDTO> getSelecciones() {
		return selecciones;
	}

}