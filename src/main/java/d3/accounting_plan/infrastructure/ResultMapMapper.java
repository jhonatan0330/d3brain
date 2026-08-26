package d3.accounting_plan.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.accounting_plan.domain.ResultMapDTO;
import d3.accounting_plan.domain.ResultMapFilterDTO;

@D3SqlConnMapper(value = "ResultMapAccountingMapper")
public interface ResultMapMapper {

	ResultMapDTO insert(ResultMapDTO dto);

	ResultMapDTO update(ResultMapDTO dto);

	int count(ResultMapFilterDTO filter);

	ResultMapDTO getOne(ResultMapFilterDTO filter);

	List<ResultMapDTO> getMany(ResultMapFilterDTO filter);

}