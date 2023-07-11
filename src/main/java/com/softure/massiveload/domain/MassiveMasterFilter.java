package com.softure.massiveload.domain;

import java.util.Date;

import com.softure.shared.domain.SharedDataObjectFilter;


public class MassiveMasterFilter extends SharedDataObjectFilter
{

	private String archivo;
	private Date fechaMin;
	private Date fechaMax;
	private String mensaje;
	private String plantilla;
	private String progreso;
	private String usuario;
	
	public MassiveMasterFilter(String state, int page, int size) {
		this.setState(state);
		this.setStartRow( page*size );
		this.setEndRow( (page*size) + size - 1 );
	}

	public MassiveMasterFilter() {
	}

	public String getArchivo() {
		return archivo;
	}

	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	public Date getFechaMin() {
		return fechaMin;
	}

	public void setFechaMin(Date fechaMin) {
		this.fechaMin = fechaMin;
	}

	public Date getFechaMax() {
		return fechaMax;
	}

	public void setFechaMax(Date fechaMax) {
		this.fechaMax = fechaMax;
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

}
