package com.softure.process_designer.infrastructure;


// BEGIN region interImport  
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.process_designer.domain.ProcesoEstadoDTO;
import com.softure.process_designer.domain.ProcesoEstadoFilterDTO;

@SoftureSqlConnMapper("ProcesoEstadoMapper")
public interface ProcesoEstadoMapper extends IBasicMapper<ProcesoEstadoDTO, ProcesoEstadoFilterDTO>{
	

// BEGIN region aditionalMethods  
	void actualizarEstados(ProcesoEstadoDTO dto);
	List<ProcesoEstadoDTO> actualizarCierreResponsable(ProcesoEstadoDTO dto);
	List<ProcesoEstadoDTO> actualizarIngresoResponsable(ProcesoEstadoDTO dto);
	String funcionAsignacion(@Param("estado") String estado, @Param("documento") String documento, @Param("modificador") String modificador, @Param("token") String token);
	List<ProcesoEstadoDTO> getFullToSynchronize();
// END region aditionalMethods
}