package d3.webservice.domain;


import org.apache.ibatis.type.Alias;

import d3.java.domain.BasicFilterDTO;

@Alias("WebServiceFilterDTO")
public class WebServiceFilterDTO extends BasicFilterDTO {

	private String nombre;
	private String codigo;
	private String proceso;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getProceso() {
		return proceso;
	}

	public void setProceso(String proceso) {
		this.proceso = proceso;
	}

}