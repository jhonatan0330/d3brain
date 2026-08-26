package d3.webservice.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonFormat;
import d3.java.domain.BasicDTO;

@Alias("WebServiceEjecucionDTO")
public class WebServiceEjecucionDTO extends BasicDTO {

	private String servicio;
	private String usuario;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private String documento;
	private String modificador;
	private String transaccion;
	private String parametros;
	private String parametersInexecution;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEjecucion;
	private String entrada;
	private String salida;
	private String error;
	private String masivo;
	private String extracciones;
	private String textoRespuesta;
	private String sincrona;

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getModificador() {
		return modificador;
	}

	public void setModificador(String modificador) {
		this.modificador = modificador;
	}

	public String getTransaccion() {
		return transaccion;
	}

	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}

	public String getParametros() {
		return parametros;
	}

	public void setParametros(String parametros) {
		this.parametros = parametros;
	}

	public Date getFechaEjecucion() {
		return fechaEjecucion;
	}

	public void setFechaEjecucion(Date fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}

	public String getEntrada() {
		return entrada;
	}

	public void setEntrada(String entrada) {
		this.entrada = entrada;
	}

	public String getSalida() {
		return salida;
	}

	public void setSalida(String salida) {
		this.salida = salida;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMasivo() {
		return masivo;
	}

	public void setMasivo(String masivo) {
		this.masivo = masivo;
	}

	public String getExtracciones() {
		return extracciones;
	}

	public void setExtracciones(String extracciones) {
		this.extracciones = extracciones;
	}

	public String getTextoRespuesta() {
		return textoRespuesta;
	}

	public void setTextoRespuesta(String textoRespuesta) {
		this.textoRespuesta = textoRespuesta;
	}

	public String getSincrona() {
		return sincrona;
	}

	public void setSincrona(String sincrona) {
		this.sincrona = sincrona;
	}

	public String getParametersInexecution() {
		return parametersInexecution;
	}

	public void setParametersInexecution(String parametersInexecution) {
		this.parametersInexecution = parametersInexecution;
	}

}