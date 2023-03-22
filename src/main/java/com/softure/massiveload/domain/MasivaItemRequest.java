package com.softure.massiveload.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.softure.shared.domain.SharedDataObject;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MasivaItemRequest extends SharedDataObject {
	
	private String carga;
	private String documento;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaSerializacion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaSincronizacion;
	private String modelo;
	private String nombre;
	private String progreso;
    private Date createdAt;
    private String createdUser;
    private Date updatedAt;
    private String updatedUser;
	@JsonProperty(access = Access.READ_ONLY)
	private String state;
	
	public MassiveItemDTO toModel() {
		var cargaMasivaItemModel = new MassiveItemDTO();
		cargaMasivaItemModel.setId(this.getId());
		cargaMasivaItemModel.setCarga(this.carga);
		cargaMasivaItemModel.setModelo(this.modelo);
		cargaMasivaItemModel.setProgreso(this.progreso);
		cargaMasivaItemModel.setFechaSerializacion(this.fechaSerializacion);
		cargaMasivaItemModel.setFechaSincronizacion(this.fechaSincronizacion);
		cargaMasivaItemModel.setDocumento(this.documento);
		cargaMasivaItemModel.setNombre(this.nombre);
		return cargaMasivaItemModel;
	}

}
