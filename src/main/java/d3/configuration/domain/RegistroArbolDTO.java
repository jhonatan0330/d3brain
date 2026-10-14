package d3.configuration.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class RegistroArbolDTO {

	private String arbolId;
	private TreeNodeDTO arbol;
	private String tenantOrigen;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "America/Bogota")
	private Date fechaExport;
	private Integer totalNodos;

	public String getArbolId() {
		return arbolId;
	}

	public void setArbolId(String arbolId) {
		this.arbolId = arbolId;
	}

	public TreeNodeDTO getArbol() {
		return arbol;
	}

	public void setArbol(TreeNodeDTO arbol) {
		this.arbol = arbol;
	}

	public String getTenantOrigen() {
		return tenantOrigen;
	}

	public void setTenantOrigen(String tenantOrigen) {
		this.tenantOrigen = tenantOrigen;
	}

	public Date getFechaExport() {
		return fechaExport;
	}

	public void setFechaExport(Date fechaExport) {
		this.fechaExport = fechaExport;
	}

	public Integer getTotalNodos() {
		return totalNodos;
	}

	public void setTotalNodos(Integer totalNodos) {
		this.totalNodos = totalNodos;
	}

}
