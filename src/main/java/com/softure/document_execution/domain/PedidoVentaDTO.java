package com.softure.document_execution.domain;

import java.util.List;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.softure.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("PedidoVentaDTO")
@JsonInclude(Include.NON_NULL)
public class PedidoVentaDTO extends BasicDTO {
	public static final String ESTADO_ACTIVO = "A";
	public static final String ESTADO_INACTIVO = "I";
	public static final String ESTADO_FINALIZADO = "C";

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaRegistro;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private String funcionario;
	private String funcionarioNombre;
	private String plantilla;
	private BigDecimal consecutivo;
	private String nombre;
	private String imagen;
	private String descripcion;
	private String estadoExpediente;
	private String textoFiltro;
	private String estadoNombre;
	private Integer historico;
	private String transaccion;
	private PedidoVentaDineroDTO dinero;
	private List<PedidoVentaCaracteristicaDTO> caracteristicas;
	private String campoOrigen;
	private String campoPropiedad;
	private List<DocumentMessage> messages;

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getFecha() {
		return fecha;
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

	public void setConsecutivo(BigDecimal consecutivo) {
		this.consecutivo = consecutivo;
	}

	public BigDecimal getConsecutivo() {
		return consecutivo;
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

	public void setDinero(PedidoVentaDineroDTO dinero) {
		this.dinero = dinero;
	}

	public PedidoVentaDineroDTO getDinero() {
		return dinero;
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

	public List<DocumentMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<DocumentMessage> messages) {
		this.messages = messages;
	}

}