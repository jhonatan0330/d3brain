package d3.accounting.domain;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonProperty;

import d3.shared.domain.SharedDataObject;

@Alias("IndicatorDTO")
public class IndicatorDTO extends SharedDataObject {

	private String nombre;
	private String codigo;
	private String proceso;
	private String imagen;

	@Override
	@JsonProperty("llaveTabla")
	public String getKey() {
		return super.getKey();
	}

	@Override
	public void setKey(String key) {
		super.setKey(key);
	}

	@Override
	@JsonProperty("estado")
	public String getState() {
		return super.getState();
	}

	@Override
	public void setState(String state) {
		super.setState(state);
	}

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

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

}
