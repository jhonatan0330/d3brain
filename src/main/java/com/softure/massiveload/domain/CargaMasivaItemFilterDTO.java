package com.softure.massiveload.domain;

import com.softure.java.domain.IDataObjectFilter;

// Start of user code importsModel
import java.util.Date;
// End of user code

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CargaMasivaItemFilterDTO implements IDataObjectFilter
{
	private int paginacionRegistroInicial;
	private int paginacionRegistroFinal;
	private String filtroParametro;
	private String llaveTabla;
	private String estado;
	private String securityToken;
	private String carga;
	private String documento;
	private Date fechaSerializacionMin;
	private Date fechaSerializacionMax;
	private Date fechaSincronizacionMin;
	private Date fechaSincronizacionMax;
	private String modelo;
	private String nombre;
	private String progreso;
	
	public CargaMasivaItemFilterDTO(String state, int page, int size) {
		this.estado = state;
		this.paginacionRegistroInicial = page*size;
		this.paginacionRegistroInicial = (page*size) + size - 1;
	}

}
