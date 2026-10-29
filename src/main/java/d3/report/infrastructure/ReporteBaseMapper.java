package d3.report.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.shared.domain.IBasicMapper;
import d3.report.domain.ReporteBaseDTO;
import d3.report.domain.ReporteBaseFilterDTO;

@D3SqlConnMapper(value = "ReporteBaseMapper")
public interface ReporteBaseMapper extends IBasicMapper<ReporteBaseDTO, ReporteBaseFilterDTO> {

	List<ReporteBaseDTO> listarMenu();

}