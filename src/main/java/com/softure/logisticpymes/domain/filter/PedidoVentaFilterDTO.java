package com.softure.logisticpymes.domain.filter;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicFilterDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaCaracteristicaDTO;

// BEGIN region interImport
import java.util.List;

import org.apache.ibatis.type.Alias;
@Alias("PedidoVentaFilterDTO")
public class PedidoVentaFilterDTO extends BasicFilterDTO
{

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaRegistroMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaRegistroMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMax;
	private String funcionario;
	private String funcionarioNombre;
	private String plantilla;
	private String nombre;
	private String imagen;
	private String descripcion;
	private String estadoExpediente;
	private String textoFiltro;
	private String estadoNombre;
	private Integer historico;
	private String transaccion;
	private List<PedidoVentaCaracteristicaDTO> caracteristicas;
	private String campoOrigen;
	private String campoPropiedad;

	
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
	
	
	public void setFechaMin(Date fechaMin) {
		this.fechaMin = fechaMin;
	}
	
	public Date getFechaMin() {
		return fechaMin;
	}
	
	public void setFechaMax(Date fechaMax) {
		this.fechaMax = fechaMax;
	}
	
	public Date getFechaMax() {
		return fechaMax;
	}
	
	
	public void setFuncionario(String funcionario) {
		this.funcionario = funcionario;
	}
	
	public String getFuncionario() {
		return funcionario;
	}
	
	
	public void setFuncionarioNombre(String funcionarioNombre) {
		this.funcionarioNombre = funcionarioNombre;
	}
	
	public String getFuncionarioNombre() {
		return funcionarioNombre;
	}
	
	
	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}
	
	public String getPlantilla() {
		return plantilla;
	}
	
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	public String getImagen() {
		return imagen;
	}
	
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	
	public void setEstadoExpediente(String estadoExpediente) {
		this.estadoExpediente = estadoExpediente;
	}
	
	public String getEstadoExpediente() {
		return estadoExpediente;
	}
	
	
	public void setTextoFiltro(String textoFiltro) {
		this.textoFiltro = textoFiltro;
	}
	
	public String getTextoFiltro() {
		return textoFiltro;
	}
	
	
	public void setEstadoNombre(String estadoNombre) {
		this.estadoNombre = estadoNombre;
	}
	
	public String getEstadoNombre() {
		return estadoNombre;
	}
	
	
	public void setHistorico(Integer historico) {
		this.historico = historico;
	}
	
	public Integer getHistorico() {
		return historico;
	}
	
	
	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}
	
	public String getTransaccion() {
		return transaccion;
	}
	
					
	
	public void setCaracteristicas(List<PedidoVentaCaracteristicaDTO> caracteristicas) {
		this.caracteristicas = caracteristicas;
	}
	
	public List<PedidoVentaCaracteristicaDTO> getCaracteristicas() {
		return caracteristicas;
	}
	
					
	
	public void setCampoOrigen(String campoOrigen) {
		this.campoOrigen = campoOrigen;
	}
	
	public String getCampoOrigen() {
		return campoOrigen;
	}
	
	
	public void setCampoPropiedad(String campoPropiedad) {
		this.campoPropiedad = campoPropiedad;
	}
	
	public String getCampoPropiedad() {
		return campoPropiedad;
	}
	

}