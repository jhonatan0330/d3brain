package com.softure.massiveload.domain;

import java.util.Date;

import com.softure.shared.domain.SharedDataObjectFilter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MassiveItemFilter extends SharedDataObjectFilter
{
	private String carga;
	private String documento;
	private Date fechaSerializacionMin;
	private Date fechaSerializacionMax;
	private Date fechaSincronizacionMin;
	private Date fechaSincronizacionMax;
	private String modelo;
	private String nombre;
	private String progreso;
	
	public MassiveItemFilter(String state, int page, int size) {
		this.setState(state);
		this.setStartRow( page*size );
		this.setEndRow( (page*size) + size - 1 );
	}

}
