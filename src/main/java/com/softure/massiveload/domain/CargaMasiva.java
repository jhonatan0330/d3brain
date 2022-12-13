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
public class CargaMasiva {
	
	private String cargaMasivaId;
	private String archivo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private String mensaje;
	private String plantilla;
	private String progreso;
	private String usuario;
	@JsonProperty(access = Access.READ_ONLY)
	private String estado;
	
	public CargaMasivaDTO toModel() {
		var cargaMasivaModel = new CargaMasivaDTO();
		cargaMasivaModel.setLlaveTabla(this.cargaMasivaId);
		cargaMasivaModel.setFecha(this.fecha);
		cargaMasivaModel.setUsuario(this.usuario);
		cargaMasivaModel.setArchivo(this.archivo);
		cargaMasivaModel.setProgreso(this.progreso);
		cargaMasivaModel.setMensaje(this.mensaje);
		cargaMasivaModel.setPlantilla(this.plantilla);
		return cargaMasivaModel;
	}

}
