package d3.property.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import d3.java.domain.BasicFilterDTO;

import org.apache.ibatis.type.Alias;

@Alias("PropiedadFilterDTO")
public class PropiedadFilterDTO extends BasicFilterDTO {

	private String propiedadValor;
	private String tipo;
	private String nombre;
	private String key;
	private String campo;
	private String texto;
	private String rol;
	private String rolNombre;
	private String rolExcluyente;
	private String rolExcluyenteNombre;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicialMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicialMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFinalMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFinalMax;
	private String usuario;
	private String usuarioNombre;
	private String usuarioExcluyente;
	private String usuarioExcluyenteNombre;
	private String motivo;
	private String valor;
	private String bloqueo;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaDefinicionMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaDefinicionMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEliminacionMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEliminacionMax;
	private String usuarioCreacion;
	private String usuarioEliminacion;

	public void setPropiedadValor(String propiedadValor) {
		this.propiedadValor = propiedadValor;
	}

	public String getPropiedadValor() {
		return propiedadValor;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public String getCampo() {
		return campo;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getTexto() {
		return texto;
	}

	public void setFechaDefinicionMin(Date fechaDefinicionMin) {
		this.fechaDefinicionMin = fechaDefinicionMin;
	}

	public Date getFechaDefinicionMin() {
		return fechaDefinicionMin;
	}

	public void setFechaDefinicionMax(Date fechaDefinicionMax) {
		this.fechaDefinicionMax = fechaDefinicionMax;
	}

	public Date getFechaDefinicionMax() {
		return fechaDefinicionMax;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getRol() {
		return rol;
	}

	public void setRolNombre(String rolNombre) {
		this.rolNombre = rolNombre;
	}

	public String getRolNombre() {
		return rolNombre;
	}

	public void setRolExcluyente(String rolExcluyente) {
		this.rolExcluyente = rolExcluyente;
	}

	public String getRolExcluyente() {
		return rolExcluyente;
	}

	public void setRolExcluyenteNombre(String rolExcluyenteNombre) {
		this.rolExcluyenteNombre = rolExcluyenteNombre;
	}

	public String getRolExcluyenteNombre() {
		return rolExcluyenteNombre;
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

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuarioNombre(String usuarioNombre) {
		this.usuarioNombre = usuarioNombre;
	}

	public String getUsuarioNombre() {
		return usuarioNombre;
	}

	public void setUsuarioExcluyente(String usuarioExcluyente) {
		this.usuarioExcluyente = usuarioExcluyente;
	}

	public String getUsuarioExcluyente() {
		return usuarioExcluyente;
	}

	public void setUsuarioExcluyenteNombre(String usuarioExcluyenteNombre) {
		this.usuarioExcluyenteNombre = usuarioExcluyenteNombre;
	}

	public String getUsuarioExcluyenteNombre() {
		return usuarioExcluyenteNombre;
	}

	public void setBloqueo(String bloqueo) {
		this.bloqueo = bloqueo;
	}

	public String getBloqueo() {
		return bloqueo;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public Date getFechaEliminacionMin() {
		return fechaEliminacionMin;
	}

	public void setFechaEliminacionMin(Date fechaEliminacionMin) {
		this.fechaEliminacionMin = fechaEliminacionMin;
	}

	public Date getFechaEliminacionMax() {
		return fechaEliminacionMax;
	}

	public void setFechaEliminacionMax(Date fechaEliminacionMax) {
		this.fechaEliminacionMax = fechaEliminacionMax;
	}

	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}

	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}

	public String getUsuarioEliminacion() {
		return usuarioEliminacion;
	}

	public void setUsuarioEliminacion(String usuarioEliminacion) {
		this.usuarioEliminacion = usuarioEliminacion;
	}

}