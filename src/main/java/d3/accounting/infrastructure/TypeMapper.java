package d3.accounting.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.accounting.domain.TypeDTO;
import d3.accounting.domain.TypeFilterDTO;

@D3SqlConnMapper(value = "TypeAccountingMapper")
public interface TypeMapper {

	TypeDTO insert(TypeDTO dto);

	TypeDTO update(TypeDTO dto);

	int count(TypeFilterDTO filter);

	TypeDTO getOne(TypeFilterDTO filter);

	List<TypeDTO> getMany(TypeFilterDTO filter);

}