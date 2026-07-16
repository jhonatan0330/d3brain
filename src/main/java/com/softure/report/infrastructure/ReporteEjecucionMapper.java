package com.softure.report.infrastructure;

import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.report.domain.ReporteEjecucionDTO;
import com.softure.report.domain.ReporteEjecucionFilterDTO;

@SoftureSqlConnMapper(value = "ReporteEjecucionMapper")
public interface ReporteEjecucionMapper extends IBasicMapper<ReporteEjecucionDTO, ReporteEjecucionFilterDTO> {

	ReporteEjecucionDTO insertarHistorico(ReporteEjecucionDTO dto);
}