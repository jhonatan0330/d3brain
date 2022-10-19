package com.softure.massiveload.domain.dto;

import com.softure.java.domain.IDataObject;
import com.softure.massiveload.domain.vo.CargaMasiva;

// Start of user code importsModel
import java.util.Date;
// End of user code

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.ibatis.type.Alias;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("CargaMasivaDTO")
public class CargaMasivaDTO implements IDataObject
{
	private String llaveTabla;
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
	private String estado;

	public CargaMasiva toValueObject() {
		var cargaMasiva = new CargaMasiva();
		cargaMasiva.setCargaMasivaId(this.llaveTabla);
		cargaMasiva.setFecha(this.fecha);
		cargaMasiva.setUsuario(this.usuario);
		cargaMasiva.setArchivo(this.archivo);
		cargaMasiva.setProgreso(this.progreso);
		cargaMasiva.setMensaje(this.mensaje);
		cargaMasiva.setPlantilla(this.plantilla);
		cargaMasiva.setEstado(this.estado);
		return cargaMasiva;
	}

}
