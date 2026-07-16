package com.accounting.plan.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.accounting.plan.application.base.CatalogService;
import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.CatalogFilterDTO;
import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import org.springframework.context.annotation.Lazy;

@Service("PlanGetCatalogAccountingService")
public class PlanGetCatalogService {

	private final CatalogService catalogService;

	public PlanGetCatalogService(@Lazy CatalogService catalogService) {
		this.catalogService = catalogService;
	}

	public List<CatalogDTO> getActive() throws ServerException {
		CatalogFilterDTO filter = new CatalogFilterDTO();
		filter.setState(SharedConstants.STATE_ACTIVE);
		return catalogService.getMany(filter);
	}

	public CatalogDTO getById(String id) throws ServerException {
		return catalogService.getById(id);
	}
}
