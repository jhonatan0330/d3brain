package d3.mail.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import d3.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("MensajeDTO")
public class MensajeDTO extends BasicDTO {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private String titulo;
	private String usuario;
	private String documento;
	private String template;
	private String parametros;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date leido;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date correoEnviado;
	private String correoError;
	private String correo;
	private String reporte;
	private String adjuntoURL;
	private String transaccion;

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getDocumento() {
		return documento;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTemplate() {
		return template;
	}

	public void setParametros(String parametros) {
		this.parametros = parametros;
	}

	public String getParametros() {
		return parametros;
	}

	public void setLeido(Date leido) {
		this.leido = leido;
	}

	public Date getLeido() {
		return leido;
	}

	public void setCorreoEnviado(Date correoEnviado) {
		this.correoEnviado = correoEnviado;
	}

	public Date getCorreoEnviado() {
		return correoEnviado;
	}

	public void setCorreoError(String correoError) {
		this.correoError = correoError;
	}

	public String getCorreoError() {
		return correoError;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getCorreo() {
		return correo;
	}

	public String getAdjuntoURL() {
		return adjuntoURL;
	}

	public void setAdjuntoURL(String adjuntoURL) {
		this.adjuntoURL = adjuntoURL;
	}

	public void setReporte(String reporte) {
		this.reporte = reporte;
	}

	public String getReporte() {
		return reporte;
	}

	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}

	public String getTransaccion() {
		return transaccion;
	}

}