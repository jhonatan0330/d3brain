package com.softure.logisticpymes.persistence;


// BEGIN region interImport  
import java.util.Date;
import java.util.List;
// END region interImport
import com.softure.logisticpymes.dto.ProcesoTransicionAutomaticaDTO;
import com.softure.logisticpymes.dto.filter.ProcesoTransicionAutomaticaFilterDTO;

public interface ProcesoTransicionAutomaticaMapper extends IBasicMapper<ProcesoTransicionAutomaticaDTO, ProcesoTransicionAutomaticaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<ProcesoTransicionAutomaticaDTO> consultarPendientes();
	String getFieldPlantilla(String propiedad);
	Date obtenerFechaUltimaEjecucion(String transicion);
	void inactivarPropiedad(String propiedad);
// END region aditionalMethods
}