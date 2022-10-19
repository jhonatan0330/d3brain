package com.softure.massiveload.domain.filter;

import com.softure.java.domain.IDataObjectFilter;

// Start of user code importsModel
import java.util.Date;
//End of user code

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CargaMasivaFilterDTO implements IDataObjectFilter
{
	private int paginacionRegistroInicial;
	private int paginacionRegistroFinal;
	private String filtroParametro;
	private String llaveTabla;
	private String estado;
	private String securityToken;
	private String archivo;
	private Date fechaMin;
	private Date fechaMax;
	private String mensaje;
	private String plantilla;
	private String progreso;
	private String usuario;
	
	public CargaMasivaFilterDTO(String state, int page, int size) {
		this.estado = state;
		this.paginacionRegistroInicial = page*size;
		this.paginacionRegistroInicial = (page*size) + size - 1;
	}

}
