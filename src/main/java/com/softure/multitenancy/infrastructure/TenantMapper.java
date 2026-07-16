package com.softure.multitenancy.infrastructure;

import java.util.List;

import com.softure.SoftureSqlConnMapper;
import com.softure.multitenancy.domain.TenantDTO;
import com.softure.multitenancy.domain.TenantFilterDTO;

@SoftureSqlConnMapper(value = "TenantMapper")
public interface TenantMapper {

	TenantDTO insert(TenantDTO dto);

	TenantDTO update(TenantDTO dto);

	int count(TenantFilterDTO filter);

	TenantDTO getOne(TenantFilterDTO filter);

	List<TenantDTO> getMany(TenantFilterDTO filter);
}
