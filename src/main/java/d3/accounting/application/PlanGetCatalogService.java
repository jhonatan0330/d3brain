package d3.accounting.application;

import java.util.List;

import org.springframework.stereotype.Service;

import d3.accounting.application.base.CatalogService;
import d3.accounting.domain.CatalogDTO;
import d3.accounting.domain.CatalogFilterDTO;
import d3.shared.domain.SharedConstants;
import d3.shared.domain.ServerException;
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
