package d3.process_designer.domain;

import java.util.List;


import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import d3.logisticpymes.domain.BasicParamDTO;

@Alias("ProcesoEstadoDTO")
@JsonInclude(Include.NON_NULL)
public class ProcesoEstadoDTO extends BasicParamDTO {
	public static final String TIPO_ESTADO = "E";
	public static final String TIPO_DECISION = "D";
	public static final String TIPO_ITERADOR = "R";
	public static final String TIPO_API = "P";
	public static final String ACTIVO = "A";
	public static final String FINALIZADO = "C";
	public static final String INACTIVO = "I";

	private String tipo;
	private String estadoDocumento;
	private Integer avance;
	private String nombre;
	private String codigo;
	private String proceso;
	private String procesoNombre;
	private List<ProcesoTransicionDTO> transiciones;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getEstadoDocumento() {
		return estadoDocumento;
	}

	public void setEstadoDocumento(String estadoDocumento) {
		this.estadoDocumento = estadoDocumento;
	}

	public Integer getAvance() {
		return avance;
	}

	public void setAvance(Integer avance) {
		this.avance = avance;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getProceso() {
		return proceso;
	}

	public void setProceso(String proceso) {
		this.proceso = proceso;
	}

	public String getProcesoNombre() {
		return procesoNombre;
	}

	public void setProcesoNombre(String procesoNombre) {
		this.procesoNombre = procesoNombre;
	}

	public List<ProcesoTransicionDTO> getTransiciones() {
		return transiciones;
	}

	public void setTransiciones(List<ProcesoTransicionDTO> transiciones) {
		this.transiciones = transiciones;
	}

}