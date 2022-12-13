package com.softure.massiveload.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
// Start of user code imports
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
//End of user code
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CargaMasivaItem {
	
	private String cargaMasivaItemId;
	private String carga;
	private String documento;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaSerializacion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaSincronizacion;
	private String modelo;
	private String nombre;
	private String progreso;
	@JsonProperty(access = Access.READ_ONLY)
	private String estado;
	
	public CargaMasivaItemDTO toModel() {
		var cargaMasivaItemModel = new CargaMasivaItemDTO();
		cargaMasivaItemModel.setLlaveTabla(this.cargaMasivaItemId);
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
