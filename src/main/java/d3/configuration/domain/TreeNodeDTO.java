package d3.configuration.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import d3.shared.domain.BasicParamDTO;

@JsonInclude(Include.NON_NULL)
public class TreeNodeDTO extends BasicParamDTO {

	public static final String ORGANIZACION = "ORGANIZACION";
	public static final String PROCESO_MACRO = "PROCESO_MACRO";
	public static final String PROCESO = "PROCESO";
	public static final String ESTADO = "ESTADO";
	public static final String TRANSICION = "TRANSICION";
	public static final String PLANTILLA = "PLANTILLA";
	public static final String PLANTILLA_MODIFICACION = "PLANTILLA_MODIFICACION";
	public static final String PLANTILLA_ANULACION = "PLANTILLA_ANULACION";
	public static final String PLANTILLA_ACTIVACION = "PLANTILLA_ACTIVACION";
	public static final String CAMPO = "CAMPO";
	public static final String REPORTE = "REPORTE";
	public static final String ROL = "ROL";
	public static final String API = "API";
	public static final String MENSAJE = "MENSAJE";

	private String nombre;
	private String codigo;
	private String imagen;
	private String tipo;
	private String camino;
	private Object dato;
	private List<TreeNodeDTO> hijos;

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getImagen() {
		return imagen;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setCamino(String camino) {
		this.camino = camino;
	}

	public String getCamino() {
		return camino;
	}

	public void setDato(Object dato) {
		this.dato = dato;
	}

	public Object getDato() {
		return dato;
	}

	public void setHijos(List<TreeNodeDTO> hijos) {
		this.hijos = hijos;
	}

	public List<TreeNodeDTO> getHijos() {
		return hijos;
	}

}