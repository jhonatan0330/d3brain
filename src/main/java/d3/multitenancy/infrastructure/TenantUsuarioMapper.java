package d3.multitenancy.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.multitenancy.domain.TenantUsuarioDTO;
import d3.multitenancy.domain.TenantUsuarioFilterDTO;

@D3SqlConnMapper(value = "TenantUsuarioMapper")
public interface TenantUsuarioMapper {

	TenantUsuarioDTO insert(TenantUsuarioDTO dto);

	TenantUsuarioDTO update(TenantUsuarioDTO dto);

	int count(TenantUsuarioFilterDTO filter);

	TenantUsuarioDTO getOne(TenantUsuarioFilterDTO filter);

	List<TenantUsuarioDTO> getMany(TenantUsuarioFilterDTO filter);
}
