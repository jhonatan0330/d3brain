package com.softure.massiveload.domain;

import com.softure.java.domain.IDataObject;

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
@Alias("CargaMasivaItemDTO")
public class CargaMasivaItemDTO implements IDataObject
{
	private String llaveTabla;
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
	private String estado;

	public CargaMasivaItem toValueObject() {
		var cargaMasivaItem = new CargaMasivaItem();
		cargaMasivaItem.setCargaMasivaItemId(this.llaveTabla);
		cargaMasivaItem.setCarga(this.carga);
		cargaMasivaItem.setModelo(this.modelo);
		cargaMasivaItem.setProgreso(this.progreso);
		cargaMasivaItem.setFechaSerializacion(this.fechaSerializacion);
		cargaMasivaItem.setFechaSincronizacion(this.fechaSincronizacion);
		cargaMasivaItem.setDocumento(this.documento);
		cargaMasivaItem.setNombre(this.nombre);
		cargaMasivaItem.setEstado(this.estado);
		return cargaMasivaItem;
	}

}
