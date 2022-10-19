package com.softure.logisticpymes.infrastructure.mybatis.mapper;


// BEGIN region interImport  
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.ProcesoTransicionAutomaticaDTO;
import com.softure.logisticpymes.domain.filter.ProcesoTransicionAutomaticaFilterDTO;

public interface ProcesoTransicionAutomaticaMapper extends IBasicMapper<ProcesoTransicionAutomaticaDTO, ProcesoTransicionAutomaticaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<ProcesoTransicionAutomaticaDTO> consultarPendientes();
	String getFieldPlantilla(String propiedad);
	Date obtenerFechaUltimaEjecucion(String transicion);
	void inactivarPropiedad(String propiedad);
	int funcionPasarTablaHistoricos(@Param("plantilla") String plantilla, @Param("fechaCorte") Date fechaCorte);
// END region aditionalMethods
}