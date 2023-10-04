package com.softure.report.infrastructure;


import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.report.domain.ReporteEjecucionDTO;
import com.softure.report.domain.ReporteEjecucionFilterDTO;

@SoftureSqlConnMapper("ReporteEjecucionMapper")
public interface ReporteEjecucionMapper extends IBasicMapper<ReporteEjecucionDTO, ReporteEjecucionFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}