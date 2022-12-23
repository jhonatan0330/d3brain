package com.softure.process_designer.domain;

import java.util.List;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.logisticpymes.domain.BasicParamDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("ProcesoEstadoDTO")
public class ProcesoEstadoDTO extends BasicParamDTO
// BEGIN region interfaces  
// END region interfaces
{
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


}