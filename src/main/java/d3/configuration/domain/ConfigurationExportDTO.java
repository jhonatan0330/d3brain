package d3.configuration.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ConfigurationExportDTO {

	public static final String VERSION = "1.0";

	private String version = VERSION;
	private String tenantOrigen;
	private Date fechaExport;
	private List<String> modulos;
	private HierarchyExporterDTO hierarchy;
	private TreeNodeDTO arbol;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public List<String> getModulos() {
		return modulos;
	}

	public void setModulos(List<String> modulos) {
		this.modulos = modulos;
	}

	public HierarchyExporterDTO getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(HierarchyExporterDTO hierarchy) {
		this.hierarchy = hierarchy;
	}

	public TreeNodeDTO getArbol() {
		return arbol;
	}

	public void setArbol(TreeNodeDTO arbol) {
		this.arbol = arbol;
	}

}
