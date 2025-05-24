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
	public List<ResultMapDTO> getBalance(@PathVariable String catalog, @RequestHeader("Authorization") String token) throws ServerException {
		return getBalanceService.getBalance(catalog);
	}	
	
	@GetMapping("/account/{catalog}")
	public List<AccountDTO> getAccount(@PathVariable String catalog, @RequestHeader("Authorization") String token, @RequestParam(required = false) String filter) throws ServerException {
		return getAccountService.getActive(catalog, filter);
	}
	
	@GetMapping(value="/account/{catalog}/{id}")
	public AccountDTO getAccountById(@PathVariable String catalog, @PathVariable String id, @RequestHeader("Authorization") String token)  throws ServerException  {
		return getAccountService.getByCatalogAndId(catalog, id);
	}
	
	
	@GetMapping(value="/catalog/{id}")
	public CatalogDTO getCatalogById(@PathVariable String id, @RequestHeader("Authorization") String token)  throws ServerException  {
		return getCatalogService.getById(id);
	}
	
	@GetMapping("/catalog")
	public List<CatalogDTO> getCatalog(@RequestHeader("Authorization") String token) throws ServerException {
		return getCatalogService.getActive();
	}
	

}
