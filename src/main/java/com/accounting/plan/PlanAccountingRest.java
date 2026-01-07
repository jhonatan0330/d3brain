package com.accounting.plan;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accounting.plan.application.PlanCreateAccountService;
import com.accounting.plan.application.PlanGetAccountService;
import com.accounting.plan.application.PlanGetBalanceService;
import com.accounting.plan.application.PlanGetCatalogService;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.ResultMapDTO;
import com.shared.domain.ServerException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/acc/plan")
public class PlanAccountingRest {
	
	@Autowired @Lazy 
	private PlanGetCatalogService getCatalogService;
	@Autowired @Lazy 
	private PlanCreateAccountService createAccountService;
	@Autowired @Lazy 
	private PlanGetAccountService getAccountService;
	@Autowired @Lazy
	private PlanGetBalanceService getBalanceService;
	
	@GetMapping("/balance/{catalog}")
	public List<ResultMapDTO> getBalance(@PathVariable(name="catalog") String pCatalog, @RequestHeader(name="Authorization") String token) throws ServerException {
		return getBalanceService.getBalance(pCatalog);
	}	
	
	@GetMapping("/account/{catalog}")
	public List<AccountDTO> getAccount(@PathVariable(name="catalog") String pCatalog, @RequestHeader(name="Authorization") String token, @RequestParam(name="filter", required = false) String pFilter) throws ServerException {
		return getAccountService.getActive(pCatalog, pFilter);
	}
	
	@GetMapping(value="/account/{catalog}/{id}")
	public AccountDTO getAccountById(@PathVariable(name="catalog") String pCatalog, @PathVariable(name="id") String pId, @RequestHeader(name="Authorization") String token)  throws ServerException  {
		return getAccountService.getByCatalogAndId(pCatalog, pId);
	}
	
	
	@GetMapping(value="/catalog/{id}")
	public CatalogDTO getCatalogById(@PathVariable(name="id") String pId, @RequestHeader(name="Authorization") String token)  throws ServerException  {
		return getCatalogService.getById(pId);
	}
	
	@GetMapping("/catalog")
	public List<CatalogDTO> getCatalog(@RequestHeader(name="Authorization") String token) throws ServerException {
		return getCatalogService.getActive();
	}
	

}
