package com.softure.massiveload.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.shared.domain.SharedDataObject;

@Alias("CargaMasivaItemDTO")
public class MassiveItemDTO extends SharedDataObject
{
	
	public static final String ERROR = "E";
	public static final String FINALIZADA = "F";
	public static final String SERIALIZADA = "S";
	
	private String carga;
	private String documento;
	private Date fechaSerializacion;
	private Date fechaSincronizacion;
	private String modelo;
	private String nombre;
	private String progreso;



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



	public Date getFechaSerializacion() {
		return fechaSerializacion;
	}



	public void setFechaSerializacion(Date fechaSerializacion) {
		this.fechaSerializacion = fechaSerializacion;
	}



	public Date getFechaSincronizacion() {
		return fechaSincronizacion;
	}



	public void setFechaSincronizacion(Date fechaSincronizacion) {
		this.fechaSincronizacion = fechaSincronizacion;
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
	

	public MasivaItemRequest toValueObject() {
		var cargaMasivaItem = new MasivaItemRequest();
		cargaMasivaItem.setKey(this.getKey());
		cargaMasivaItem.setCarga(this.carga);
		cargaMasivaItem.setModelo(this.modelo);
		cargaMasivaItem.setProgreso(this.progreso);
		cargaMasivaItem.setFechaSerializacion(this.fechaSerializacion);
		cargaMasivaItem.setFechaSincronizacion(this.fechaSincronizacion);
		cargaMasivaItem.setDocumento(this.documento);
		cargaMasivaItem.setNombre(this.nombre);
		cargaMasivaItem.setState(this.getState());
		return cargaMasivaItem;
	}



}
