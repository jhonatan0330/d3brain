package com.accounting.plan;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.accounting.plan.application.PlanCreateAccountService;
import com.accounting.plan.application.PlanGetAccountService;
import com.accounting.plan.application.PlanGetBalanceService;
import com.accounting.plan.application.PlanGetCatalogService;
import com.accounting.plan.application.PlanUploadAccountService;
import com.accounting.plan.application.PlanCreateCatalogService;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.ResultMapDTO;
import com.shared.domain.ServerException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/acc/plan")
public class PlanAccountingRest {
	
	@Autowired @Lazy 
	private PlanCreateCatalogService createCatalogService;
	@Autowired @Lazy 
	private PlanGetCatalogService getCatalogService;
	@Autowired @Lazy 
	private PlanCreateAccountService createAccountService;
	@Autowired @Lazy 
	private PlanUploadAccountService uploadAccountService;
	@Autowired @Lazy 
	private PlanGetAccountService getAccountService;
	@Autowired @Lazy
	private PlanGetBalanceService getBalanceService;
	
	@GetMapping("/balance/{catalog}")
	public List<ResultMapDTO> getBalance(@PathVariable("catalog") String catalog, @RequestHeader("Authorization") String token) throws ServerException {
		return getBalanceService.getBalance(catalog);
	}
	
	@PostMapping("/upload/{catalog}")
	public void uploadAccount(@PathVariable("catalog") String catalog, @RequestHeader("Authorization") String token, MultipartFile file) throws ServerException {
		uploadAccountService.call(catalog, file);
	}
	
	@PostMapping("/account")
	public AccountDTO createAccount(@RequestBody AccountDTO account, @RequestHeader("Authorization") String token) throws ServerException {
		return createAccountService.call(account);
	}
	
	@PutMapping("/account")
	public AccountDTO updateAccount(@RequestHeader("Authorization") String token, @RequestBody AccountDTO account) throws ServerException {
		return createAccountService.callUpdate(account);
	}
	
	@DeleteMapping("/account/{accountId}")
	public AccountDTO deleteAccount(@PathVariable("acountId") String accountId, @RequestHeader("Authorization") String token) throws ServerException {
		return createAccountService.callDelete(accountId);
	}
	
	@GetMapping("/account/{catalog}")
	public List<AccountDTO> getAccount(@PathVariable("catalog") String catalog, @RequestHeader("Authorization") String token, @RequestParam(value="filter", required = false) String filter) throws ServerException {
		return getAccountService.getActive(catalog, filter);
	}
	
	@GetMapping(value="/account/{catalog}/{id}")
	public AccountDTO getAccountById(@PathVariable("catalog") String catalog, @PathVariable("id") String id, @RequestHeader("Authorization") String token)  throws ServerException  {
		return getAccountService.getById(catalog, id);
	}
	
	@PostMapping("/catalog")
	public CatalogDTO createCatalog(@RequestBody CatalogDTO catalog, @RequestHeader("Authorization") String token) throws ServerException {
		return createCatalogService.call(catalog);
	}
	
	@GetMapping(value="/catalog/{id}")
	public CatalogDTO getCatalogById(@PathVariable("id") String id, @RequestHeader("Authorization") String token)  throws ServerException  {
		return getCatalogService.getById(id);
	}
	
	@GetMapping("/catalog")
	public List<CatalogDTO> getCatalog(@RequestHeader("Authorization") String token) throws ServerException {
		return getCatalogService.getActive();
	}
	
	@PutMapping("/catalog")
	public CatalogDTO updateCatalog(@RequestHeader("Authorization") String token, @RequestBody CatalogDTO catalog) throws ServerException {
		return createCatalogService.call(catalog);
	}
	
	@DeleteMapping("/catalog/{catalogId}")
	public CatalogDTO inactivateCatalog(@RequestHeader("Authorization") String token, @PathVariable("catalogId") String catalogId) throws ServerException {
		return createCatalogService.callDelete(catalogId);
	}

}
