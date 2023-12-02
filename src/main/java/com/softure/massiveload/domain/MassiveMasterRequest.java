package com.softure.massiveload.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
// Start of user code imports
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
//End of user code

public class MassiveMasterRequest {
	
	private String cargaMasivaId;
	private String archivo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private String mensaje;
	private String plantilla;
	private String progreso;
	private String usuario;
	@JsonProperty(access = Access.READ_ONLY)
	private String estado;
	
	public MassiveMasterDTO toModel() {
		var cargaMasivaModel = new MassiveMasterDTO();
		cargaMasivaModel.setKey(this.cargaMasivaId);
		cargaMasivaModel.setFecha(this.fecha);
		cargaMasivaModel.setUsuario(this.usuario);
		cargaMasivaModel.setArchivo(this.archivo);
		cargaMasivaModel.setProgreso(this.progreso);
		cargaMasivaModel.setMensaje(this.mensaje);
		cargaMasivaModel.setPlantilla(this.plantilla);
		return cargaMasivaModel;
	}

	public String getCargaMasivaId() {
		return cargaMasivaId;
	}

	public void setCargaMasivaId(String cargaMasivaId) {
		this.cargaMasivaId = cargaMasivaId;
	}

	public String getArchivo() {
		return archivo;
	}

	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getPlantilla() {
		return plantilla;
	}

	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}

	public String getProgreso() {
		return progreso;
	}

	public void setProgreso(String progreso) {
		this.progreso = progreso;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

}
