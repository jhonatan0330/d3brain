package com.accounting.plan.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.accounting.plan.application.base.ResultMapExtendService;
import com.accounting.plan.domain.ResultMapDTO;
import com.shared.domain.ServerException;

@Service("PlanGetBalanceAccountingService")
public class PlanGetBalanceService {

	@Autowired @Lazy 
	private ResultMapExtendService resultMapService;

	public List<ResultMapDTO> getBalance(String catalogId) throws ServerException{
		
		return resultMapService.getBalanceByCatalog(catalogId);
	}

}
