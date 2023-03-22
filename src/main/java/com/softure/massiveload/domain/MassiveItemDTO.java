package com.softure.massiveload.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.softure.shared.domain.SharedDataObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Alias("CargaMasivaItemDTO")
public class MassiveItemDTO extends SharedDataObject
{
	
	public static final String ERROR = "E";
	public static final String FINALIZADA = "F";
	public static final String SERIALIZADA = "S";
	private String carga;
	private String documento;
	private Date fechaSerializacion;
	private Date fechaSincronizacion;
	private String modelo;
	private String nombre;
	private String progreso;


	public MasivaItemRequest toValueObject() {
		var cargaMasivaItem = new MasivaItemRequest();
		cargaMasivaItem.setId(this.getId());
		cargaMasivaItem.setCarga(this.carga);
		cargaMasivaItem.setModelo(this.modelo);
		cargaMasivaItem.setProgreso(this.progreso);
		cargaMasivaItem.setFechaSerializacion(this.fechaSerializacion);
		cargaMasivaItem.setFechaSincronizacion(this.fechaSincronizacion);
		cargaMasivaItem.setDocumento(this.documento);
		cargaMasivaItem.setNombre(this.nombre);
		cargaMasivaItem.setState(this.getState());
		return cargaMasivaItem;
	}

}
