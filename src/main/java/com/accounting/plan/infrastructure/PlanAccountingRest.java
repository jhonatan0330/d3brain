package com.accounting.plan.infrastructure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accounting.plan.application.PlanCreateAccountService;
import com.accounting.plan.application.PlanGetAccountService;
import com.accounting.plan.application.PlanGetCatalogService;
import com.accounting.plan.application.base.IPlanCreateCatalogService;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.CatalogDTO;
import com.softure.java.dto.exception.ServerException;

@RestController
@RequestMapping("acc/plan")
public class PlanAccountingRest {
	
	@Autowired
	private IPlanCreateCatalogService createCatalogService;
	@Autowired
	private PlanGetCatalogService getCatalogService;
	@Autowired
	private PlanCreateAccountService createAccountService;
	@Autowired
	private PlanGetAccountService getAccountService;
	
	@PostMapping("/account")
	public AccountDTO createAccount(@RequestBody AccountDTO account, @RequestHeader("Authorization") String token) throws ServerException {
		return createAccountService.call(account, token);
	}
	
	@GetMapping("/account/{catalog}")
	public List<AccountDTO> getAccount(@PathVariable String catalog, @RequestHeader("Authorization") String token) throws ServerException {
		return getAccountService.getActive(catalog);
	}
	
	@GetMapping(value="/account/{catalog}/{id}")
	public AccountDTO getAccountById(@PathVariable String catalog, @PathVariable String id, @RequestHeader("Authorization") String token)  throws ServerException  {
		return getAccountService.getById(catalog, id);
	}
	
	@PostMapping("/catalog")
	public CatalogDTO createCatalog(@RequestBody CatalogDTO catalog, @RequestHeader("Authorization") String token) throws ServerException {
		return createCatalogService.call(catalog, token);
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
