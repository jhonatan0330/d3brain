package com.softure.process_designer.infrastructure;


// BEGIN region interImport  
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.process_designer.domain.ProcesoTransicionAutomaticaDTO;
import com.softure.process_designer.domain.ProcesoTransicionAutomaticaFilterDTO;

@SoftureSqlConnMapper("ProcesoTransicionAutomaticaMapper")
public interface ProcesoTransicionAutomaticaMapper extends IBasicMapper<ProcesoTransicionAutomaticaDTO, ProcesoTransicionAutomaticaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<ProcesoTransicionAutomaticaDTO> consultarPendientes();
	String getFieldPlantilla(String propiedad);
	Date obtenerFechaUltimaEjecucion(String transicion);
	void inactivarPropiedad(String propiedad);
	int funcionPasarTablaHistoricos(@Param("plantilla") String plantilla, @Param("fechaCorte") Date fechaCorte);
	int countExecutionInLastMonth(@Param("transition") String transition);
// END region aditionalMethods
}