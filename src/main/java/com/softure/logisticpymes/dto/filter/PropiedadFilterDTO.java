package com.softure.logisticpymes.dto.filter;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;
@Alias("PropiedadFilterDTO")
public class PropiedadFilterDTO extends BasicFilterDTO
{

	private String propiedadValor;
	private String tipo;
	private String nombre;
	private String key;
	private String campo;
	private String texto;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaDefinicionMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaDefinicionMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaImplementacionMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaImplementacionMax;
	private String cambioCreacion;
	private String cambioEliminacion;
	private String rol;
	private String rolNombre;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicialMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicialMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFinalMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFinalMax;
	private String usuarioNombre;
	private String usuario;
	private String bloqueo;

	
	public void setPropiedadValor(String propiedadValor) {
		this.propiedadValor = propiedadValor;
	}
	
	public String getPropiedadValor() {
		return propiedadValor;
	}
	
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
	
	public void setCampo(String campo) {
		this.campo = campo;
	}
	
	public String getCampo() {
		return campo;
	}
	
	
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public String getTexto() {
		return texto;
	}
	
	
	public void setFechaDefinicionMin(Date fechaDefinicionMin) {
		this.fechaDefinicionMin = fechaDefinicionMin;
	}
	
	public Date getFechaDefinicionMin() {
		return fechaDefinicionMin;
	}
	
	public void setFechaDefinicionMax(Date fechaDefinicionMax) {
		this.fechaDefinicionMax = fechaDefinicionMax;
	}
	
	public Date getFechaDefinicionMax() {
		return fechaDefinicionMax;
	}
	
	
	public void setFechaImplementacionMin(Date fechaImplementacionMin) {
		this.fechaImplementacionMin = fechaImplementacionMin;
	}
	
	public Date getFechaImplementacionMin() {
		return fechaImplementacionMin;
	}
	
	public void setFechaImplementacionMax(Date fechaImplementacionMax) {
		this.fechaImplementacionMax = fechaImplementacionMax;
	}
	
	public Date getFechaImplementacionMax() {
		return fechaImplementacionMax;
	}
	
	
	public void setCambioCreacion(String cambioCreacion) {
		this.cambioCreacion = cambioCreacion;
	}
	
	public String getCambioCreacion() {
		return cambioCreacion;
	}
	
	
	public void setCambioEliminacion(String cambioEliminacion) {
		this.cambioEliminacion = cambioEliminacion;
	}
	
	public String getCambioEliminacion() {
		return cambioEliminacion;
	}
	
	
	public void setRol(String rol) {
		this.rol = rol;
	}
	
	public String getRol() {
		return rol;
	}
	
	
	public void setRolNombre(String rolNombre) {
		this.rolNombre = rolNombre;
	}
	
	public String getRolNombre() {
		return rolNombre;
	}
	
	
	public void setFechaInicialMin(Date fechaInicialMin) {
		this.fechaInicialMin = fechaInicialMin;
	}
	
	public Date getFechaInicialMin() {
		return fechaInicialMin;
	}
	
	public void setFechaInicialMax(Date fechaInicialMax) {
		this.fechaInicialMax = fechaInicialMax;
	}
	
	public Date getFechaInicialMax() {
		return fechaInicialMax;
	}
	
	
	public void setFechaFinalMin(Date fechaFinalMin) {
		this.fechaFinalMin = fechaFinalMin;
	}
	
	public Date getFechaFinalMin() {
		return fechaFinalMin;
	}
	
	public void setFechaFinalMax(Date fechaFinalMax) {
		this.fechaFinalMax = fechaFinalMax;
	}
	
	public Date getFechaFinalMax() {
		return fechaFinalMax;
	}
	
	
	public void setUsuarioNombre(String usuarioNombre) {
		this.usuarioNombre = usuarioNombre;
	}
	
	public String getUsuarioNombre() {
		return usuarioNombre;
	}
	
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	
	public void setBloqueo(String bloqueo) {
		this.bloqueo = bloqueo;
	}
	
	public String getBloqueo() {
		return bloqueo;
	}
	

}