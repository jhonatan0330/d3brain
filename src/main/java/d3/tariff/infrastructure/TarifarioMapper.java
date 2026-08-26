package d3.tariff.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.tariff.domain.TarifarioDTO;
import d3.tariff.domain.TarifarioFilterDTO;

@D3SqlConnMapper(value = "TarifarioMapper")
public interface TarifarioMapper {

	TarifarioDTO insert(TarifarioDTO dto);

	TarifarioDTO update(TarifarioDTO dto);

	int count(TarifarioFilterDTO filter);

	TarifarioDTO getOne(TarifarioFilterDTO filter);

	List<TarifarioDTO> getMany(TarifarioFilterDTO filter);

}