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
public class MassiveMasterFilter extends SharedDataObjectFilter
{

	private String archivo;
	private Date fechaMin;
	private Date fechaMax;
	private String mensaje;
	private String plantilla;
	private String progreso;
	private String usuario;
	
	public MassiveMasterFilter(String state, int page, int size) {
		this.setState(state);
		this.setStartRow( page*size );
		this.setEndRow( (page*size) + size - 1 );
	}

}
