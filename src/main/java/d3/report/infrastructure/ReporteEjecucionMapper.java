package d3.report.infrastructure;

import d3.D3SqlConnMapper;
import d3.java.domain.IBasicMapper;
import d3.report.domain.ReporteEjecucionDTO;
import d3.report.domain.ReporteEjecucionFilterDTO;

@D3SqlConnMapper(value = "ReporteEjecucionMapper")
public interface ReporteEjecucionMapper extends IBasicMapper<ReporteEjecucionDTO, ReporteEjecucionFilterDTO> {

	ReporteEjecucionDTO insertarHistorico(ReporteEjecucionDTO dto);
}