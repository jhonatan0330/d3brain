package com.softure.process_designer.domain;


import org.apache.ibatis.type.Alias;

import com.softure.logisticpymes.domain.BasicParamDTO;

@Alias("ProcesoTransicionDTO")
public class ProcesoTransicionDTO extends BasicParamDTO
{
	public static final String RESTANDO = "R";
	public static final String SUMANDO = "S";

	private String procesoNombre;
	private Integer estadoPartidaOrden;
	private Integer estadoLlegadaOrden;
	private String nombre;
	private String proceso;
	private String estadoPartida;
	private String estadoPartidaNombre;
	private String plantilla;
	private String plantillaNombre;
	private boolean documentador;
	private String afectaSaldo;
	private String imagen;
	private boolean rapida;
	private String estadoLLegada;
	private String estadoLlegadaNombre;
	private String estadoLlegadaTipo;
	private String codigo;

	public void setProcesoNombre(String procesoNombre) {
		this.procesoNombre = procesoNombre;
	}

	public String getProcesoNombre() {
		return procesoNombre;
	}

	public void setEstadoPartidaOrden(Integer estadoPartidaOrden) {
		this.estadoPartidaOrden = estadoPartidaOrden;
	}

	public Integer getEstadoPartidaOrden() {
		return estadoPartidaOrden;
	}

	public void setEstadoLlegadaOrden(Integer estadoLlegadaOrden) {
		this.estadoLlegadaOrden = estadoLlegadaOrden;
	}

	public Integer getEstadoLlegadaOrden() {
		return estadoLlegadaOrden;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setProceso(String proceso) {
		this.proceso = proceso;
	}

	public String getProceso() {
		return proceso;
	}

	public void setEstadoPartida(String estadoPartida) {
		this.estadoPartida = estadoPartida;
	}

	public String getEstadoPartida() {
		return estadoPartida;
	}

	public void setEstadoPartidaNombre(String estadoPartidaNombre) {
		this.estadoPartidaNombre = estadoPartidaNombre;
	}

	public String getEstadoPartidaNombre() {
		return estadoPartidaNombre;
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

	public void setDocumentador(boolean documentador) {
		this.documentador = documentador;
	}

	public boolean getDocumentador() {
		return documentador;
	}

	public void setAfectaSaldo(String afectaSaldo) {
		this.afectaSaldo = afectaSaldo;
	}

	public String getAfectaSaldo() {
		return afectaSaldo;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getImagen() {
		return imagen;
	}

	public void setRapida(boolean rapida) {
		this.rapida = rapida;
	}

	public boolean getRapida() {
		return rapida;
	}

	public void setEstadoLLegada(String estadoLLegada) {
		this.estadoLLegada = estadoLLegada;
	}

	public String getEstadoLLegada() {
		return estadoLLegada;
	}

	public void setEstadoLlegadaNombre(String estadoLlegadaNombre) {
		this.estadoLlegadaNombre = estadoLlegadaNombre;
	}

	public String getEstadoLlegadaNombre() {
		return estadoLlegadaNombre;
	}

	public void setEstadoLlegadaTipo(String estadoLlegadaTipo) {
		this.estadoLlegadaTipo = estadoLlegadaTipo;
	}

	public String getEstadoLlegadaTipo() {
		return estadoLlegadaTipo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

}