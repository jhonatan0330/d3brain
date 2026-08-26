package d3.multitenancy.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.multitenancy.domain.TenantDTO;
import d3.multitenancy.domain.TenantFilterDTO;

@D3SqlConnMapper(value = "TenantMapper")
public interface TenantMapper {

	TenantDTO insert(TenantDTO dto);

	TenantDTO update(TenantDTO dto);

	int count(TenantFilterDTO filter);

	TenantDTO getOne(TenantFilterDTO filter);

	List<TenantDTO> getMany(TenantFilterDTO filter);
}
