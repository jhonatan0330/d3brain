package com.softure.logisticpymes.dto.filter;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;
@Alias("CuentaFilterDTO")
public class CuentaFilterDTO extends BasicFilterDTO
{

	private String codigo;
	private String nombre;
	private String documento;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaConciliacionMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaConciliacionMax;
 	private Boolean validarTurnoFilter = null;

	
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
	
	
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	public String getDocumento() {
		return documento;
	}
	
	
	public void setFechaConciliacionMin(Date fechaConciliacionMin) {
		this.fechaConciliacionMin = fechaConciliacionMin;
	}
	
	public Date getFechaConciliacionMin() {
		return fechaConciliacionMin;
	}
	
	public void setFechaConciliacionMax(Date fechaConciliacionMax) {
		this.fechaConciliacionMax = fechaConciliacionMax;
	}
	
	public Date getFechaConciliacionMax() {
		return fechaConciliacionMax;
	}
	
 	
 	public void setValidarTurnoFilter(Boolean validarTurnoFilter) {
		this.validarTurnoFilter = validarTurnoFilter;
	}
	
	public Boolean getValidarTurnoFilter() {
		return validarTurnoFilter;
	}
	

}