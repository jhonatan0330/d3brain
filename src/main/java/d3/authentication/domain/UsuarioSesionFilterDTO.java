package d3.authentication.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import d3.java.domain.BasicFilterDTO;

import org.apache.ibatis.type.Alias;

@Alias("UsuarioSesionFilterDTO")
public class UsuarioSesionFilterDTO extends BasicFilterDTO {

	private String usuario;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaCierreMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaCierreMax;
	private String ip;
	private Boolean privadaFilter = null;

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getUsuario() {
		return usuario;
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

	public void setFechaCierreMin(Date fechaCierreMin) {
		this.fechaCierreMin = fechaCierreMin;
	}

	public Date getFechaCierreMin() {
		return fechaCierreMin;
	}

	public void setFechaCierreMax(Date fechaCierreMax) {
		this.fechaCierreMax = fechaCierreMax;
	}

	public Date getFechaCierreMax() {
		return fechaCierreMax;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public void setPrivadaFilter(Boolean privadaFilter) {
		this.privadaFilter = privadaFilter;
	}

	public Boolean getPrivadaFilter() {
		return privadaFilter;
	}

}