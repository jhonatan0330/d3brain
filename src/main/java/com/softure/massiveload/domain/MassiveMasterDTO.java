package com.softure.massiveload.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.apache.ibatis.type.Alias;

import com.softure.shared.domain.SharedDataObject;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Alias("CargaMasivaDTO")
public class MassiveMasterDTO extends SharedDataObject
{
	public static final String ERROR = "E";
	public static final String FINALIZADA = "F";
	public static final String SERIALIZADA = "S";
	public static final String SINCRONIZANDO = "S";
	public static final String TERMINADA_CON_FALLAS = "T";

	private String archivo;
	private Date fecha;
	private String mensaje;
	private String plantilla;
	private String progreso;
	private String usuario;
    
	public MassiveMasterRequest toValueObject() {
		var cargaMasiva = new MassiveMasterRequest();
		cargaMasiva.setCargaMasivaId(this.getId());
		cargaMasiva.setFecha(this.fecha);
		cargaMasiva.setUsuario(this.usuario);
		cargaMasiva.setArchivo(this.archivo);
		cargaMasiva.setProgreso(this.progreso);
		cargaMasiva.setMensaje(this.mensaje);
		cargaMasiva.setPlantilla(this.plantilla);
		cargaMasiva.setEstado(this.getState());
		return cargaMasiva;
	}

}
