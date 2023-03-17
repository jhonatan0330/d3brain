package com.softure.report.domain;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.softure.logisticpymes.domain.BasicParamDTO;

@Alias("ReporteBaseDTO")
@JsonInclude(Include.NON_NULL)
public class ReporteBaseDTO extends BasicParamDTO
{
	private String plantilla;
	private String plantillaNombre;
	private String nombre;
	private String codigo;
	private boolean soloExistente;
	private String variables;
	private Integer version;
	private String descripcion;
	private String servidor;
	private String multiplesId;
	private String servidorUrl;
	private boolean publico;

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
	public void setSoloExistente(boolean soloExistente) {
		this.soloExistente = soloExistente;
	}
	
	public boolean getSoloExistente() {
		return soloExistente;
	}
	public void setVariables(String variables) {
		this.variables = variables;
	}
	
	public String getVariables() {
		return variables;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public Integer getVersion() {
		return version;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getDescripcion() {
		return descripcion;
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
	public void setPublico(boolean publico) {
		this.publico = publico;
	}
	
	public boolean getPublico() {
		return publico;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}