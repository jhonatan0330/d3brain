package com.accounting.plan.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accounting.plan.application.base.CatalogService;
import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.CatalogFilterDTO;
import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;

@Service("PlanGetCatalogAccountingService")
public class PlanGetCatalogService {

	@Autowired
	private CatalogService catalogService;
	
	public List<CatalogDTO> getActive() throws ServerException{
		CatalogFilterDTO filter = new CatalogFilterDTO();
		filter.setState(SharedConstants.STATE_ACTIVE);
		return catalogService.getMany(filter);
	}

	public CatalogDTO getById(String id) throws ServerException {
		return catalogService.getById(id);
	}
}
