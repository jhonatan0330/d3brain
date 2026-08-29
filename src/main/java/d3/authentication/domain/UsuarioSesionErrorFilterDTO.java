package d3.authentication.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import d3.shared.domain.BasicFilterDTO;

import org.apache.ibatis.type.Alias;

@Alias("UsuarioSesionErrorFilterDTO")
public class UsuarioSesionErrorFilterDTO extends BasicFilterDTO {

	private String sesion;
	private String clave;
	private String ip;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMax;

	public void setSesion(String sesion) {
		this.sesion = sesion;
	}

	public String getSesion() {
		return sesion;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getClave() {
		return clave;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
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

}