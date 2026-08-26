package d3.process_designer.domain;

import java.util.List;


import org.apache.ibatis.type.Alias;

import d3.logisticpymes.domain.BasicParamDTO;
import d3.process_form.domain.DocumentoPlantillaDTO;

@Alias("ProcesoDTO")
public class ProcesoDTO extends BasicParamDTO
{
	public static final String AGRUPADOR = "A";
	public static final String EJECUTOR = "E";

	private String tipo;
	private String objetivo;
	private String imagen;
	private Integer prioridad;
	private String macroproceso;
	private String nombre;
	private String codigo;
	private String macroNombre;
	private List<ProcesoDTO> hijos;
	private List<ProcesoEstadoDTO> estados;
	private List<ProcesoTransicionDTO> transiciones;
	private List<DocumentoPlantillaDTO> plantillas;

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}

	public String getObjetivo() {
		return objetivo;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getImagen() {
		return imagen;
	}

	public void setPrioridad(Integer prioridad) {
		this.prioridad = prioridad;
	}

	public Integer getPrioridad() {
		return prioridad;
	}

	public void setMacroproceso(String macroproceso) {
		this.macroproceso = macroproceso;
	}

	public String getMacroproceso() {
		return macroproceso;
	}

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

	public void setMacroNombre(String macroNombre) {
		this.macroNombre = macroNombre;
	}

	public String getMacroNombre() {
		return macroNombre;
	}

	public void setHijos(List<ProcesoDTO> hijos) {
		this.hijos = hijos;
	}

	public List<ProcesoDTO> getHijos() {
		return hijos;
	}

	public void setEstados(List<ProcesoEstadoDTO> estados) {
		this.estados = estados;
	}

	public List<ProcesoEstadoDTO> getEstados() {
		return estados;
	}

	public void setTransiciones(List<ProcesoTransicionDTO> transiciones) {
		this.transiciones = transiciones;
	}

	public List<ProcesoTransicionDTO> getTransiciones() {
		return transiciones;
	}

	public void setPlantillas(List<DocumentoPlantillaDTO> plantillas) {
		this.plantillas = plantillas;
	}

	public List<DocumentoPlantillaDTO> getPlantillas() {
		return plantillas;
	}

}