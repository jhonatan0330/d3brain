package com.softure.process_designer.domain;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("ProcesoEstadoFilterDTO")
public class ProcesoEstadoFilterDTO extends BasicFilterDTO
{

	private String tipo;
	private String estadoDocumento;
	private Integer avance;
	private String nombre;
	private String codigo;
	private String proceso;
	private String procesoNombre;	

}