package com.softure.massiveload.domain;

import java.util.Date;

import com.shared.domain.SharedDataObjectFilter;

public class MassiveItemFilter extends SharedDataObjectFilter {
	private String carga;
	private String documento;
	private Date fechaSerializacionMin;
	private Date fechaSerializacionMax;
	private Date fechaSincronizacionMin;
	private Date fechaSincronizacionMax;
	private String modelo;
	private String nombre;
	private String progreso;

	public MassiveItemFilter(String state, int page, int size) {
		this.setState(state);
		this.setStartRow(page * size);
		this.setEndRow((page * size) + size - 1);
	}

	public MassiveItemFilter() {
	}

	public String getCarga() {
		return carga;
	}

	public void setCarga(String carga) {
		this.carga = carga;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public Date getFechaSerializacionMin() {
		return fechaSerializacionMin;
	}

	public void setFechaSerializacionMin(Date fechaSerializacionMin) {
		this.fechaSerializacionMin = fechaSerializacionMin;
	}

	public Date getFechaSerializacionMax() {
		return fechaSerializacionMax;
	}

	public void setFechaSerializacionMax(Date fechaSerializacionMax) {
		this.fechaSerializacionMax = fechaSerializacionMax;
	}

	public Date getFechaSincronizacionMin() {
		return fechaSincronizacionMin;
	}

	public void setFechaSincronizacionMin(Date fechaSincronizacionMin) {
		this.fechaSincronizacionMin = fechaSincronizacionMin;
	}

	public Date getFechaSincronizacionMax() {
		return fechaSincronizacionMax;
	}

	public void setFechaSincronizacionMax(Date fechaSincronizacionMax) {
		this.fechaSincronizacionMax = fechaSincronizacionMax;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getProgreso() {
		return progreso;
	}

	public void setProgreso(String progreso) {
		this.progreso = progreso;
	}

}
