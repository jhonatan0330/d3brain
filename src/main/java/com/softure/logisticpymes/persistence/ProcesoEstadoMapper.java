package com.softure.logisticpymes.persistence;


// BEGIN region interImport  
import java.util.List;

import org.apache.ibatis.annotations.Param;
// END region interImport
import com.softure.logisticpymes.dto.ProcesoEstadoDTO;
import com.softure.logisticpymes.dto.filter.ProcesoEstadoFilterDTO;

public interface ProcesoEstadoMapper extends IBasicMapper<ProcesoEstadoDTO, ProcesoEstadoFilterDTO>{
	

// BEGIN region aditionalMethods  
	void actualizarEstados(ProcesoEstadoDTO dto);
	List<ProcesoEstadoDTO> actualizarCierreResponsable(ProcesoEstadoDTO dto);
	List<ProcesoEstadoDTO> actualizarIngresoResponsable(ProcesoEstadoDTO dto);
	String funcionAsignacion(@Param("estado") String estado, @Param("documento") String documento, @Param("modificador") String modificador, @Param("token") String token);
// END region aditionalMethods
}