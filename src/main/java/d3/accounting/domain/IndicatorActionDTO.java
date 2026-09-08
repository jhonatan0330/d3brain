package d3.accounting.domain;

import org.apache.ibatis.type.Alias;

@Alias("IndicatorAccionDTO")
public class IndicatorActionDTO {

	private String llaveTabla;
	private String nombre;
	private String imagen;
	private String plantilla;

	public String getLlaveTabla() {
		return llaveTabla;
	}

	public void setLlaveTabla(String llaveTabla) {
		this.llaveTabla = llaveTabla;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getPlantilla() {
		return plantilla;
	}

	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}

}