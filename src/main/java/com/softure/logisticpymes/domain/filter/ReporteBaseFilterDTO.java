package com.softure.logisticpymes.domain.filter;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;
@Alias("ReporteBaseFilterDTO")
public class ReporteBaseFilterDTO extends BasicFilterDTO
{

	private String plantilla;
	private String plantillaNombre;
	private String nombre;
	private String codigo;
 	private Boolean soloExistenteFilter = null;
	private Integer version;
	private String servidor;
	private String multiplesId;
	private String servidorUrl;
 	private Boolean publicoFilter = null;

	
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
	
 	
 	public void setSoloExistenteFilter(Boolean soloExistenteFilter) {
		this.soloExistenteFilter = soloExistenteFilter;
	}
	
	public Boolean getSoloExistenteFilter() {
		return soloExistenteFilter;
	}
	
	
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public Integer getVersion() {
		return version;
	}
	
	
	public void setServidor(String servidor) {
		this.servidor = servidor;
	}
	
	public String getServidor() {
		return servidor;
	}
	
	
	public void setMultiplesId(String multiplesId) {
		this.multiplesId = multiplesId;
	}
	
	public String getMultiplesId() {
		return multiplesId;
	}
	
	
	public void setServidorUrl(String servidorUrl) {
		this.servidorUrl = servidorUrl;
	}
	
	public String getServidorUrl() {
		return servidorUrl;
	}
	
 	
 	public void setPublicoFilter(Boolean publicoFilter) {
		this.publicoFilter = publicoFilter;
	}
	
	public Boolean getPublicoFilter() {
		return publicoFilter;
	}
	

}