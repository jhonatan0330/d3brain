package d3.configuration.domain;


import org.apache.ibatis.type.Alias;

import d3.shared.domain.BasicFilterDTO;

@Alias("PropiedadValorDefinidoFilterDTO")
public class PropiedadValorDefinidoFilterDTO extends BasicFilterDTO {

	private String origen;
	private String origenCategoria;
	private String codigo;
	private String nombre;
	private String grupo;
	private Boolean textOcultoFilter = null;
	private Boolean necesitaDesarrolloFilter = null;
	private Boolean incluirPreloadOrigenFilter = null;
	private Boolean multipleFilter = null;
	private Boolean pideRolFilter = null;
	private Boolean pideTiempoBloqueoFilter = null;
	private Boolean propiedadBooleanFilter = null;
	private Boolean pideUsuarioFilter = null;
	private Boolean solicitaMotivoFilter = null;
	private Boolean pideFechasFilter = null;
	private Boolean privadaFilter = null;

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigenCategoria(String origenCategoria) {
		this.origenCategoria = origenCategoria;
	}

	public String getOrigenCategoria() {
		return origenCategoria;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setTextOcultoFilter(Boolean textOcultoFilter) {
		this.textOcultoFilter = textOcultoFilter;
	}

	public Boolean getTextOcultoFilter() {
		return textOcultoFilter;
	}

	public void setNecesitaDesarrolloFilter(Boolean necesitaDesarrolloFilter) {
		this.necesitaDesarrolloFilter = necesitaDesarrolloFilter;
	}

	public Boolean getNecesitaDesarrolloFilter() {
		return necesitaDesarrolloFilter;
	}

	public void setIncluirPreloadOrigenFilter(Boolean incluirPreloadOrigenFilter) {
		this.incluirPreloadOrigenFilter = incluirPreloadOrigenFilter;
	}

	public Boolean getIncluirPreloadOrigenFilter() {
		return incluirPreloadOrigenFilter;
	}

	public void setMultipleFilter(Boolean multipleFilter) {
		this.multipleFilter = multipleFilter;
	}

	public Boolean getMultipleFilter() {
		return multipleFilter;
	}

	public void setPideRolFilter(Boolean pideRolFilter) {
		this.pideRolFilter = pideRolFilter;
	}

	public Boolean getPideRolFilter() {
		return pideRolFilter;
	}

	public void setPideTiempoBloqueoFilter(Boolean pideTiempoBloqueoFilter) {
		this.pideTiempoBloqueoFilter = pideTiempoBloqueoFilter;
	}

	public Boolean getPideTiempoBloqueoFilter() {
		return pideTiempoBloqueoFilter;
	}

	public void setPropiedadBooleanFilter(Boolean propiedadBooleanFilter) {
		this.propiedadBooleanFilter = propiedadBooleanFilter;
	}

	public Boolean getPropiedadBooleanFilter() {
		return propiedadBooleanFilter;
	}

	public void setPideUsuarioFilter(Boolean pideUsuarioFilter) {
		this.pideUsuarioFilter = pideUsuarioFilter;
	}

	public Boolean getPideUsuarioFilter() {
		return pideUsuarioFilter;
	}

	public void setSolicitaMotivoFilter(Boolean solicitaMotivoFilter) {
		this.solicitaMotivoFilter = solicitaMotivoFilter;
	}

	public Boolean getSolicitaMotivoFilter() {
		return solicitaMotivoFilter;
	}

	public void setPideFechasFilter(Boolean pideFechasFilter) {
		this.pideFechasFilter = pideFechasFilter;
	}

	public Boolean getPideFechasFilter() {
		return pideFechasFilter;
	}

	public Boolean getPrivadaFilter() {
		return privadaFilter;
	}

	public void setPrivadaFilter(Boolean privadaFilter) {
		this.privadaFilter = privadaFilter;
	}

}