package d3.property.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import d3.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("PropiedadDTO")
@JsonInclude(Include.NON_NULL)
public class PropiedadDTO extends BasicDTO {

	private String propiedadValor;
	private String tipo;
	private String nombre;
	private String key;
	private String campo;
	private String valor;
	private String texto;
	private String rol;
	private String rolNombre;
	private String rolExcluyente;
	private String rolExcluyenteNombre;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicial;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFinal;
	private String usuario;
	private String usuarioNombre;
	private String usuarioExcluyente;
	private String usuarioExcluyenteNombre;
	private String motivo;
	private String bloqueo;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaDefinicion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEliminacion;
	private String usuarioCreacion;
	private String usuarioEliminacion;

	private Integer relaciones;

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

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getValor() {
		return valor;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getTexto() {
		return texto;
	}

	public void setFechaDefinicion(Date fechaDefinicion) {
		this.fechaDefinicion = fechaDefinicion;
	}

	public Date getFechaDefinicion() {
		return fechaDefinicion;
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

	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public Date getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public Date getFechaFinal() {
		return fechaFinal;
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

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setBloqueo(String bloqueo) {
		this.bloqueo = bloqueo;
	}

	public String getBloqueo() {
		return bloqueo;
	}

	public PropiedadDTO() {
		// Constructor vacío requerido por frameworks o para instanciar manualmente
	}

	// Este metodo lo lo uso para e cache de las propiedades, de esta forma yo evito
	// que se modifiquen las propiedades del cahce

	public PropiedadDTO(PropiedadDTO other) {

		this.propiedadValor = other.propiedadValor;
		this.tipo = other.tipo;
		this.nombre = other.nombre;
		this.key = other.key;
		this.campo = other.campo;
		this.valor = other.valor;
		this.texto = other.texto;
		this.fechaDefinicion = (other.fechaDefinicion != null) ? new Date(other.fechaDefinicion.getTime()) : null;
		this.fechaEliminacion = (other.fechaEliminacion != null) ? new Date(other.fechaEliminacion.getTime()) : null;
		this.usuarioCreacion = other.usuarioCreacion;
		this.usuarioEliminacion = other.usuarioEliminacion;
		this.rol = other.rol;
		this.rolNombre = other.rolNombre;
		this.rolExcluyente = other.rolExcluyente;
		this.rolExcluyenteNombre = other.rolExcluyenteNombre;
		this.fechaInicial = (other.fechaInicial != null) ? new Date(other.fechaInicial.getTime()) : null;
		this.fechaFinal = (other.fechaFinal != null) ? new Date(other.fechaFinal.getTime()) : null;
		this.usuario = other.usuario;
		this.usuarioNombre = other.usuarioNombre;
		this.usuarioExcluyente = other.usuarioExcluyente;
		this.usuarioExcluyenteNombre = other.usuarioExcluyenteNombre;
		this.motivo = other.motivo;
		this.bloqueo = other.bloqueo;
		this.relaciones = other.relaciones;
		this.setLlaveTabla(other.getLlaveTabla());
		this.setEstado(other.getEstado());
	}

	public Date getFechaEliminacion() {
		return fechaEliminacion;
	}

	public void setFechaEliminacion(Date fechaEliminacion) {
		this.fechaEliminacion = fechaEliminacion;
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

	public Integer getRelaciones() {
		return relaciones;
	}

	public void setRelaciones(Integer relaciones) {
		this.relaciones = relaciones;
	}

}