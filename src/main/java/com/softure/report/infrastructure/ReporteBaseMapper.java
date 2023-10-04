package com.softure.report.infrastructure;


// BEGIN region interImport  
import java.util.List;

import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.report.domain.ReporteBaseDTO;
import com.softure.report.domain.ReporteBaseFilterDTO;

@SoftureSqlConnMapper("ReporteBaseMapper")
public interface ReporteBaseMapper extends IBasicMapper<ReporteBaseDTO, ReporteBaseFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<ReporteBaseDTO> listarMenu();
	List<ReporteBaseDTO> getFullToSynchronize();
// END region aditionalMethods

	
}