package d3.accounting.application;

import java.util.List;

import org.springframework.stereotype.Service;

import d3.accounting.application.base.ResultMapExtendService;
import d3.accounting.domain.ResultMapDTO;
import d3.shared.domain.ServerException;
import org.springframework.context.annotation.Lazy;

@Service("PlanGetBalanceAccountingService")
public class PlanGetBalanceService {

	private final ResultMapExtendService resultMapService;

	public PlanGetBalanceService(@Lazy ResultMapExtendService resultMapService) {
		this.resultMapService = resultMapService;
	}

	public List<ResultMapDTO> getBalance(String catalogId) throws ServerException {

		return resultMapService.getBalanceByCatalog(catalogId);
	}

}
