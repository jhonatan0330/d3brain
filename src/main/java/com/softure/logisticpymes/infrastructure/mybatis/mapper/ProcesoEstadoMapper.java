package com.softure.logisticpymes.infrastructure.mybatis.mapper;


// BEGIN region interImport  
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.ProcesoEstadoDTO;
import com.softure.logisticpymes.domain.filter.ProcesoEstadoFilterDTO;

public interface ProcesoEstadoMapper extends IBasicMapper<ProcesoEstadoDTO, ProcesoEstadoFilterDTO>{
	

// BEGIN region aditionalMethods  
	void actualizarEstados(ProcesoEstadoDTO dto);
	List<ProcesoEstadoDTO> actualizarCierreResponsable(ProcesoEstadoDTO dto);
	List<ProcesoEstadoDTO> actualizarIngresoResponsable(ProcesoEstadoDTO dto);
	String funcionAsignacion(@Param("estado") String estado, @Param("documento") String documento, @Param("modificador") String modificador, @Param("token") String token);
// END region aditionalMethods
}