package d3.process_designer.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import d3.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("ProcesoTransicionAutomaticaDTO")
public class ProcesoTransicionAutomaticaDTO extends BasicDTO
{

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private String transicion;
	private String plantilla;
	private String plantillaNombre;
	private String propiedad;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date ejecucion;
	private String mensaje;

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setTransicion(String transicion) {
		this.transicion = transicion;
	}

	public String getTransicion() {
		return transicion;
	}

	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}

	public String getPlantilla() {
		return plantilla;
	}

	public void setPlantillaNombre(String plantillaNombre) {
		this.plantillaNombre = plantillaNombre;
	}

	public String getPlantillaNombre() {
		return plantillaNombre;
	}

	public void setPropiedad(String propiedad) {
		this.propiedad = propiedad;
	}

	public String getPropiedad() {
		return propiedad;
	}

	public void setEjecucion(Date ejecucion) {
		this.ejecucion = ejecucion;
	}

	public Date getEjecucion() {
		return ejecucion;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

}