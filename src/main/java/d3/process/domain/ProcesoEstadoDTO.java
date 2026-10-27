package d3.process.domain;

import java.util.List;


import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import d3.shared.domain.BasicNodoDTO;

@Alias("ProcesoEstadoDTO")
@JsonInclude(Include.NON_NULL)
public class ProcesoEstadoDTO extends BasicNodoDTO {
	public static final String TIPO_ESTADO = "E";
	public static final String TIPO_DECISION = "D";
	public static final String TIPO_ITERADOR = "R";
	public static final String TIPO_API = "P";
	
	public static final String ACTIVO = "A";
	public static final String FINALIZADO = "C";
	public static final String INACTIVO = "I";

	private String estadoDocumento;
	
	private List<ProcesoTransicionDTO> transiciones;

	public String getEstadoDocumento() {
		return estadoDocumento;
	}

	public void setEstadoDocumento(String estadoDocumento) {
		this.estadoDocumento = estadoDocumento;
	}


	public List<ProcesoTransicionDTO> getTransiciones() {
		return transiciones;
	}

	public void setTransiciones(List<ProcesoTransicionDTO> transiciones) {
		this.transiciones = transiciones;
	}

}