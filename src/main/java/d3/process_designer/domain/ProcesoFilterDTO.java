package d3.process_designer.domain;


import org.apache.ibatis.type.Alias;

import d3.java.domain.BasicFilterDTO;

@Alias("ProcesoFilterDTO")
public class ProcesoFilterDTO extends BasicFilterDTO {

	private String tipo;
	private String imagen;
	private Integer prioridad;
	private String macroproceso;
	private String nombre;
	private String codigo;
	private String macroNombre;

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getImagen() {
		return imagen;
	}

	public void setPrioridad(Integer prioridad) {
		this.prioridad = prioridad;
	}

	public Integer getPrioridad() {
		return prioridad;
	}

	public void setMacroproceso(String macroproceso) {
		this.macroproceso = macroproceso;
	}

	public String getMacroproceso() {
		return macroproceso;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setMacroNombre(String macroNombre) {
		this.macroNombre = macroNombre;
	}

	public String getMacroNombre() {
		return macroNombre;
	}

}