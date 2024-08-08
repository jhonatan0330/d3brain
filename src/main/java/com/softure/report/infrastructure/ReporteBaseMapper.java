package com.softure.report.infrastructure;


// BEGIN region interImport  
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.report.domain.ReporteBaseDTO;
import com.softure.report.domain.ReporteBaseFilterDTO;

@SoftureSqlConnMapper(value = "ReporteBaseMapper")
public interface ReporteBaseMapper extends IBasicMapper<ReporteBaseDTO, ReporteBaseFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<ReporteBaseDTO> listarMenu();
	List<ReporteBaseDTO> getFullToSynchronize(@Param("process") List<String> process);
// END region aditionalMethods

	
}