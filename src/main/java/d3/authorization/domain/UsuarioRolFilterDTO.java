package d3.authorization.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import d3.shared.domain.BasicFilterDTO;

import org.apache.ibatis.type.Alias;

@Alias("UsuarioRolFilterDTO")
public class UsuarioRolFilterDTO extends BasicFilterDTO {

	private String usuario;
	private String usuarioIdentificacion;
	private String usuarioNombre;
	private String usuarioImagen;
	private String rolAcceso;
	private String rolNombre;
	private String documento;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicialMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicialMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFinalMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFinalMax;

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuarioIdentificacion(String usuarioIdentificacion) {
		this.usuarioIdentificacion = usuarioIdentificacion;
	}

	public String getUsuarioIdentificacion() {
		return usuarioIdentificacion;
	}

	public void setUsuarioNombre(String usuarioNombre) {
		this.usuarioNombre = usuarioNombre;
	}

	public String getUsuarioNombre() {
		return usuarioNombre;
	}

	public void setUsuarioImagen(String usuarioImagen) {
		this.usuarioImagen = usuarioImagen;
	}

	public String getUsuarioImagen() {
		return usuarioImagen;
	}

	public void setRolAcceso(String rolAcceso) {
		this.rolAcceso = rolAcceso;
	}

	public String getRolAcceso() {
		return rolAcceso;
	}

	public void setRolNombre(String rolNombre) {
		this.rolNombre = rolNombre;
	}

	public String getRolNombre() {
		return rolNombre;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getDocumento() {
		return documento;
	}

	public void setFechaInicialMin(Date fechaInicialMin) {
		this.fechaInicialMin = fechaInicialMin;
	}

	public Date getFechaInicialMin() {
		return fechaInicialMin;
	}

	public void setFechaInicialMax(Date fechaInicialMax) {
		this.fechaInicialMax = fechaInicialMax;
	}

	public Date getFechaInicialMax() {
		return fechaInicialMax;
	}

	public void setFechaFinalMin(Date fechaFinalMin) {
		this.fechaFinalMin = fechaFinalMin;
	}

	public Date getFechaFinalMin() {
		return fechaFinalMin;
	}

	public void setFechaFinalMax(Date fechaFinalMax) {
		this.fechaFinalMax = fechaFinalMax;
	}

	public Date getFechaFinalMax() {
		return fechaFinalMax;
	}

}