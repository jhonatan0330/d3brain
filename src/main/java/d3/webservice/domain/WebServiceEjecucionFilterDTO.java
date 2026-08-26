package d3.webservice.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import d3.java.domain.BasicFilterDTO;

import org.apache.ibatis.type.Alias;

@Alias("WebServiceEjecucionFilterDTO")
public class WebServiceEjecucionFilterDTO extends BasicFilterDTO {

	private String servicio;
	private String usuario;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMax;
	private String documento;
	private String modificador;
	private String transaccion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEjecucionMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEjecucionMax;
	private String entrada;
	private String salida;
	private String masivo;
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

	public Date getFechaEjecucionMin() {
		return fechaEjecucionMin;
	}

	public void setFechaEjecucionMin(Date fechaEjecucionMin) {
		this.fechaEjecucionMin = fechaEjecucionMin;
	}

	public Date getFechaEjecucionMax() {
		return fechaEjecucionMax;
	}

	public void setFechaEjecucionMax(Date fechaEjecucionMax) {
		this.fechaEjecucionMax = fechaEjecucionMax;
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

	public String getMasivo() {
		return masivo;
	}

	public void setMasivo(String masivo) {
		this.masivo = masivo;
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

}