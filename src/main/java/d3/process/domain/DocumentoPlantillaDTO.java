package d3.process.domain;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import d3.shared.domain.BasicNodoDTO;

@Alias("DocumentoPlantillaDTO")
@JsonInclude(Include.NON_NULL)
public class DocumentoPlantillaDTO extends BasicNodoDTO
{

	public static final String PRINCIPAL = "P";
	public static final String REPORTE = "T";
	public static final String ROL = "R";
	public static final String MODIFICACION = "U";
	public static final String ANULACION = "I";
	public static final String ACTIVACION = "A";
	
	private String consecutivo;
	private String proceso;


	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getConsecutivo() {
		return consecutivo;
	}

	public void setProceso(String proceso) {
		this.proceso = proceso;
	}

	public String getProceso() {
		return proceso;
	}

}