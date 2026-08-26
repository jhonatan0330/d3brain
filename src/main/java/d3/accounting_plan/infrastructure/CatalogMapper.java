package d3.accounting_plan.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.accounting_plan.domain.CatalogDTO;
import d3.accounting_plan.domain.CatalogFilterDTO;

@D3SqlConnMapper(value = "CatalogAccountingMapper")
public interface CatalogMapper {

	CatalogDTO insert(CatalogDTO dto);

	CatalogDTO update(CatalogDTO dto);

	int count(CatalogFilterDTO filter);

	CatalogDTO getOne(CatalogFilterDTO filter);

	List<CatalogDTO> getMany(CatalogFilterDTO filter);

}