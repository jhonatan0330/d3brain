package com.softure.logisticpymes.dto.filter;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;
@Alias("ActividadFilterDTO")
public class ActividadFilterDTO extends BasicFilterDTO
{

	private String responsable;
	private String responsableIdentificacion;
	private String responsableNombre;
	private String documento;
	private String documentoNombre;
	private String documentoDescripcion;
	private String documentoEstado;
	private String plantilla;
	private String plantillaNombre;
	private String plantillaImagen;
	private String responsableFoto;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaArrancarMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaArrancarMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaRegistroMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaRegistroMax;
	private String usuarioRegistro;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInactivoMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInactivoMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaTerminarMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaTerminarMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaLimiteMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaLimiteMax;
	private String usuarioInactivo;
	private Integer duracion;
	private String actividadPrevia;
	private String actividadSiguiente;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaLeidoMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaLeidoMax;

	
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
	
	
	public void setDocumentoEstado(String documentoEstado) {
		this.documentoEstado = documentoEstado;
	}
	
	public String getDocumentoEstado() {
		return documentoEstado;
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
	
	
	public void setFechaArrancarMin(Date fechaArrancarMin) {
		this.fechaArrancarMin = fechaArrancarMin;
	}
	
	public Date getFechaArrancarMin() {
		return fechaArrancarMin;
	}
	
	public void setFechaArrancarMax(Date fechaArrancarMax) {
		this.fechaArrancarMax = fechaArrancarMax;
	}
	
	public Date getFechaArrancarMax() {
		return fechaArrancarMax;
	}
	
	
	public void setFechaRegistroMin(Date fechaRegistroMin) {
		this.fechaRegistroMin = fechaRegistroMin;
	}
	
	public Date getFechaRegistroMin() {
		return fechaRegistroMin;
	}
	
	public void setFechaRegistroMax(Date fechaRegistroMax) {
		this.fechaRegistroMax = fechaRegistroMax;
	}
	
	public Date getFechaRegistroMax() {
		return fechaRegistroMax;
	}
	
	
	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}
	
	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}
	
	
	public void setFechaInactivoMin(Date fechaInactivoMin) {
		this.fechaInactivoMin = fechaInactivoMin;
	}
	
	public Date getFechaInactivoMin() {
		return fechaInactivoMin;
	}
	
	public void setFechaInactivoMax(Date fechaInactivoMax) {
		this.fechaInactivoMax = fechaInactivoMax;
	}
	
	public Date getFechaInactivoMax() {
		return fechaInactivoMax;
	}
	
	
	public void setFechaTerminarMin(Date fechaTerminarMin) {
		this.fechaTerminarMin = fechaTerminarMin;
	}
	
	public Date getFechaTerminarMin() {
		return fechaTerminarMin;
	}
	
	public void setFechaTerminarMax(Date fechaTerminarMax) {
		this.fechaTerminarMax = fechaTerminarMax;
	}
	
	public Date getFechaTerminarMax() {
		return fechaTerminarMax;
	}
	
	
	public void setFechaLimiteMin(Date fechaLimiteMin) {
		this.fechaLimiteMin = fechaLimiteMin;
	}
	
	public Date getFechaLimiteMin() {
		return fechaLimiteMin;
	}
	
	public void setFechaLimiteMax(Date fechaLimiteMax) {
		this.fechaLimiteMax = fechaLimiteMax;
	}
	
	public Date getFechaLimiteMax() {
		return fechaLimiteMax;
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
	
	
	public void setFechaLeidoMin(Date fechaLeidoMin) {
		this.fechaLeidoMin = fechaLeidoMin;
	}
	
	public Date getFechaLeidoMin() {
		return fechaLeidoMin;
	}
	
	public void setFechaLeidoMax(Date fechaLeidoMax) {
		this.fechaLeidoMax = fechaLeidoMax;
	}
	
	public Date getFechaLeidoMax() {
		return fechaLeidoMax;
	}
	

}