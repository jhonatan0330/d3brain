package d3.report.domain;


import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import d3.shared.domain.BasicParamDTO;

@Alias("ReporteBaseDTO")
@JsonInclude(Include.NON_NULL)
public class ReporteBaseDTO extends BasicParamDTO {
	private String plantilla;
	private String plantillaNombre;
	private String nombre;
	private String codigo;
	
	

	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}

	public String getPlantilla() {
		return plantilla;
	}

	public void setPlantillaNombre(String plantillaNombre) {
		this.plantillaNombre = plantillaNombre;
	}

	public String getPlantillaNombre() {
		return plantillaNombre;
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


}