package com.accounting.plan.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.accounting.plan.application.base.ResultMapExtendService;
import com.accounting.plan.domain.ResultMapDTO;
import com.shared.domain.ServerException;
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
