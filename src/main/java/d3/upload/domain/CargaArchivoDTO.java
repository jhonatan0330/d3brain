package d3.upload.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import d3.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("CargaArchivoDTO")
public class CargaArchivoDTO extends BasicDTO
{

	private String servidor;
	private Integer size;
	private String url;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicio;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFin;
	private String error;
	private String usuario;

	public void setServidor(String servidor) {
		this.servidor = servidor;
	}

	public String getServidor() {
		return servidor;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getSize() {
		return size;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getUsuario() {
		return usuario;
	}

}