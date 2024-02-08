package com.softure.tariff.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.shared.domain.SharedDataObjectFilter;

@Alias("TarifarioFilterDTO")
public class TarifarioFilterDTO extends SharedDataObjectFilter {

	private String nombre;
	private String tipoRecurso;
	private String tipoRecursoNombre;
	private String tipoDimension2;
	private String tipoDimension2Nombre;
	private String tipoDimension3;
	private String tipoDimension3Nombre;
	private String tipoDimension4;
	private String tipoDimension4Nombre;
	private Boolean productoOpcionalFilter;
	private Boolean rangoValoresFilter;
	private Boolean rangoCantidadFilter;
	private Date fechaInicialMin;
	private Date fechaInicialMax;
	private Date fechaFinalMin;
	private Date fechaFinalMax;
	private String documento;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipoRecurso() {
		return tipoRecurso;
	}

	public void setTipoRecurso(String tipoRecurso) {
		this.tipoRecurso = tipoRecurso;
	}

	public String getTipoRecursoNombre() {
		return tipoRecursoNombre;
	}

	public void setTipoRecursoNombre(String tipoRecursoNombre) {
		this.tipoRecursoNombre = tipoRecursoNombre;
	}

	public String getTipoDimension2() {
		return tipoDimension2;
	}

	public void setTipoDimension2(String tipoDimension2) {
		this.tipoDimension2 = tipoDimension2;
	}

	public String getTipoDimension2Nombre() {
		return tipoDimension2Nombre;
	}

	public void setTipoDimension2Nombre(String tipoDimension2Nombre) {
		this.tipoDimension2Nombre = tipoDimension2Nombre;
	}

	public String getTipoDimension3() {
		return tipoDimension3;
	}

	public void setTipoDimension3(String tipoDimension3) {
		this.tipoDimension3 = tipoDimension3;
	}

	public String getTipoDimension3Nombre() {
		return tipoDimension3Nombre;
	}

	public void setTipoDimension3Nombre(String tipoDimension3Nombre) {
		this.tipoDimension3Nombre = tipoDimension3Nombre;
	}

	public String getTipoDimension4() {
		return tipoDimension4;
	}

	public void setTipoDimension4(String tipoDimension4) {
		this.tipoDimension4 = tipoDimension4;
	}

	public String getTipoDimension4Nombre() {
		return tipoDimension4Nombre;
	}

	public void setTipoDimension4Nombre(String tipoDimension4Nombre) {
		this.tipoDimension4Nombre = tipoDimension4Nombre;
	}

	public Boolean getProductoOpcionalFilter() {
		return productoOpcionalFilter;
	}

	public void setProductoOpcionalFilter(Boolean productoOpcionalFilter) {
		this.productoOpcionalFilter = productoOpcionalFilter;
	}

	public Boolean getRangoValoresFilter() {
		return rangoValoresFilter;
	}

	public void setRangoValoresFilter(Boolean rangoValoresFilter) {
		this.rangoValoresFilter = rangoValoresFilter;
	}

	public Boolean getRangoCantidadFilter() {
		return rangoCantidadFilter;
	}

	public void setRangoCantidadFilter(Boolean rangoCantidadFilter) {
		this.rangoCantidadFilter = rangoCantidadFilter;
	}

	public Date getFechaInicialMin() {
		return fechaInicialMin;
	}

	public void setFechaInicialMin(Date fechaInicialMin) {
		this.fechaInicialMin = fechaInicialMin;
	}

	public Date getFechaInicialMax() {
		return fechaInicialMax;
	}

	public void setFechaInicialMax(Date fechaInicialMax) {
		this.fechaInicialMax = fechaInicialMax;
	}

	public Date getFechaFinalMin() {
		return fechaFinalMin;
	}

	public void setFechaFinalMin(Date fechaFinalMin) {
		this.fechaFinalMin = fechaFinalMin;
	}

	public Date getFechaFinalMax() {
		return fechaFinalMax;
	}

	public void setFechaFinalMax(Date fechaFinalMax) {
		this.fechaFinalMax = fechaFinalMax;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

}