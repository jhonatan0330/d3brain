package d3.configuration.domain;

import org.apache.ibatis.type.Alias;

import d3.shared.domain.BasicDTO;

@Alias("PropiedadValorDefinidoDTO")
public class PropiedadValorDefinidoDTO extends BasicDTO {
	public static final String PROCESO = "P";
	public static final String ESTADO = "A";
	public static final String TRANSICION = "T";
	public static final String PLANTILLA = "L";
	public static final String CAMPO = "C";
	public static final String REPORTE = "E";
	public static final String ORGANIZACION = "O";
	public static final String API_SERVICE = "W";

	public static final String CATALOG = "G";
	public static final String ACCOUNT = "K";

	private String origen;
	private String origenCategoria;
	private String codigo;
	private String nombre;
	private String grupo;
	private boolean textOculto;
	private boolean necesitaDesarrollo;
	private boolean incluirPreloadOrigen;
	private boolean multiple;
	private boolean pideRol;
	private boolean pideTiempoBloqueo;
	private boolean propiedadBoolean;
	private boolean pideUsuario;
	private boolean solicitaMotivo;
	private boolean pideFechas;
	private boolean privada;

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigenCategoria(String origenCategoria) {
		this.origenCategoria = origenCategoria;
	}

	public String getOrigenCategoria() {
		return origenCategoria;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setTextOculto(boolean textOculto) {
		this.textOculto = textOculto;
	}

	public boolean getTextOculto() {
		return textOculto;
	}

	public void setNecesitaDesarrollo(boolean necesitaDesarrollo) {
		this.necesitaDesarrollo = necesitaDesarrollo;
	}

	public boolean getNecesitaDesarrollo() {
		return necesitaDesarrollo;
	}

	public void setIncluirPreloadOrigen(boolean incluirPreloadOrigen) {
		this.incluirPreloadOrigen = incluirPreloadOrigen;
	}

	public boolean getIncluirPreloadOrigen() {
		return incluirPreloadOrigen;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	public boolean getMultiple() {
		return multiple;
	}

	public void setPideRol(boolean pideRol) {
		this.pideRol = pideRol;
	}

	public boolean getPideRol() {
		return pideRol;
	}

	public void setPideTiempoBloqueo(boolean pideTiempoBloqueo) {
		this.pideTiempoBloqueo = pideTiempoBloqueo;
	}

	public boolean getPideTiempoBloqueo() {
		return pideTiempoBloqueo;
	}

	public void setPropiedadBoolean(boolean propiedadBoolean) {
		this.propiedadBoolean = propiedadBoolean;
	}

	public boolean getPropiedadBoolean() {
		return propiedadBoolean;
	}

	public void setPideUsuario(boolean pideUsuario) {
		this.pideUsuario = pideUsuario;
	}

	public boolean getPideUsuario() {
		return pideUsuario;
	}

	public void setSolicitaMotivo(boolean solicitaMotivo) {
		this.solicitaMotivo = solicitaMotivo;
	}

	public boolean getSolicitaMotivo() {
		return solicitaMotivo;
	}

	public void setPideFechas(boolean pideFechas) {
		this.pideFechas = pideFechas;
	}

	public boolean getPideFechas() {
		return pideFechas;
	}

	public boolean getPrivada() {
		return privada;
	}

	public void setPrivada(boolean privada) {
		this.privada = privada;
	}

}