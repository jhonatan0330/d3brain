package d3.massiveload.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import d3.shared.domain.SharedDataObject;

@Alias("MassiveMasterDTO")
public class MassiveMasterDTO extends SharedDataObject {
	public static final String ERROR = "E";
	public static final String FINALIZADA = "F";
	public static final String SERIALIZADA = "S";
	public static final String SINCRONIZANDO = "S";
	public static final String TERMINADA_CON_FALLAS = "T";
	public static final String CARGANDO = "C";
	public static final String VALIDADO = "V";
	public static final String PENDIENTE = "P";

	private String archivo;
	private Date fecha;
	private String mensaje;
	private String plantilla;
	private String progreso;
	private String usuario;

	public MassiveMasterRequest toValueObject() {
		var cargaMasiva = new MassiveMasterRequest();
		cargaMasiva.setKey(this.getKey());
		cargaMasiva.setFecha(this.fecha);
		cargaMasiva.setUsuario(this.usuario);
		cargaMasiva.setArchivo(this.archivo);
		cargaMasiva.setProgreso(this.progreso);
		cargaMasiva.setMensaje(this.mensaje);
		cargaMasiva.setPlantilla(this.plantilla);
		cargaMasiva.setEstado(this.getState());
		return cargaMasiva;
	}

	public String getArchivo() {
		return archivo;
	}

	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getPlantilla() {
		return plantilla;
	}

	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}

	public String getProgreso() {
		return progreso;
	}

	public void setProgreso(String progreso) {
		this.progreso = progreso;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}
