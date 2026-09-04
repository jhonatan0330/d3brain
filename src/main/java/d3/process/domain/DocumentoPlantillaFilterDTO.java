package d3.process.domain;


import org.apache.ibatis.type.Alias;

import d3.shared.domain.BasicFilterDTO;

@Alias("DocumentoPlantillaFilterDTO")
public class DocumentoPlantillaFilterDTO extends BasicFilterDTO {

	private String nombre;
	private String consecutivo;
	private String imagen;
	private String codigo;
	private String proceso;
	private String tipo;
	private String padre;

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getConsecutivo() {
		return consecutivo;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getImagen() {
		return imagen;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setProceso(String proceso) {
		this.proceso = proceso;
	}

	public String getProceso() {
		return proceso;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setPadre(String padre) {
		this.padre = padre;
	}

	public String getPadre() {
		return padre;
	}

}