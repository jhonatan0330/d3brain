package com.softure.logisticpymes.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

@Alias("ActividadDTO")
public class ActividadDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String responsable;
	private String responsableIdentificacion;
	private String responsableNombre;
	private String documento;
	private String documentoNombre;
	private String documentoDescripcion;
	private String plantilla;
	private String plantillaNombre;
	private String plantillaImagen;
	private String responsableFoto;
	private String comentario;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaArrancar;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaRegistro;
	private String usuarioRegistro;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInactivo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaTerminar;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaLimite;
	private String usuarioInactivo;
	private Integer duracion;
	private String actividadPrevia;
	private String actividadSiguiente;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaLeido;

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	
	public String getResponsable() {
		return responsable;
	}
	public void setResponsableIdentificacion(String responsableIdentificacion) {
		this.responsableIdentificacion = responsableIdentificacion;
	}
	
	public String getResponsableIdentificacion() {
		return responsableIdentificacion;
	}
	public void setResponsableNombre(String responsableNombre) {
		this.responsableNombre = responsableNombre;
	}
	
	public String getResponsableNombre() {
		return responsableNombre;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	public String getDocumento() {
		return documento;
	}
	public void setDocumentoNombre(String documentoNombre) {
		this.documentoNombre = documentoNombre;
	}
	
	public String getDocumentoNombre() {
		return documentoNombre;
	}
	public void setDocumentoDescripcion(String documentoDescripcion) {
		this.documentoDescripcion = documentoDescripcion;
	}
	
	public String getDocumentoDescripcion() {
		return documentoDescripcion;
	}
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
	public void setPlantillaImagen(String plantillaImagen) {
		this.plantillaImagen = plantillaImagen;
	}
	
	public String getPlantillaImagen() {
		return plantillaImagen;
	}
	public void setResponsableFoto(String responsableFoto) {
		this.responsableFoto = responsableFoto;
	}
	
	public String getResponsableFoto() {
		return responsableFoto;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
	public String getComentario() {
		return comentario;
	}
	public void setFechaArrancar(Date fechaArrancar) {
		this.fechaArrancar = fechaArrancar;
	}
	
	public Date getFechaArrancar() {
		return fechaArrancar;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}
	
	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}
	public void setFechaInactivo(Date fechaInactivo) {
		this.fechaInactivo = fechaInactivo;
	}
	
	public Date getFechaInactivo() {
		return fechaInactivo;
	}
	public void setFechaTerminar(Date fechaTerminar) {
		this.fechaTerminar = fechaTerminar;
	}
	
	public Date getFechaTerminar() {
		return fechaTerminar;
	}
	public void setFechaLimite(Date fechaLimite) {
		this.fechaLimite = fechaLimite;
	}
	
	public Date getFechaLimite() {
		return fechaLimite;
	}
	public void setUsuarioInactivo(String usuarioInactivo) {
		this.usuarioInactivo = usuarioInactivo;
	}
	
	public String getUsuarioInactivo() {
		return usuarioInactivo;
	}
	public void setDuracion(Integer duracion) {
		this.duracion = duracion;
	}
	
	public Integer getDuracion() {
		return duracion;
	}
	public void setActividadPrevia(String actividadPrevia) {
		this.actividadPrevia = actividadPrevia;
	}
	
	public String getActividadPrevia() {
		return actividadPrevia;
	}
	public void setActividadSiguiente(String actividadSiguiente) {
		this.actividadSiguiente = actividadSiguiente;
	}
	
	public String getActividadSiguiente() {
		return actividadSiguiente;
	}
	public void setFechaLeido(Date fechaLeido) {
		this.fechaLeido = fechaLeido;
	}
	
	public Date getFechaLeido() {
		return fechaLeido;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}