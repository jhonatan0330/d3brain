package com.accounting.plan;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import com.accounting.plan.application.base.IPlanCreateCatalogService;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.ResultMapDTO;
import com.shared.domain.ServerException;

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
	private PlanUploadAccountService uploadAccountService;
	@Autowired
	private PlanGetAccountService getAccountService;
	@Autowired
	private PlanGetBalanceService getBalanceService;
	
	
	@GetMapping("/balance/{catalog}")
	public List<ResultMapDTO> getBalance(@PathVariable String catalog, @RequestHeader("Authorization") String token) throws ServerException {
		return getBalanceService.getBalance(catalog);
	}
	
	@PostMapping("/upload/{catalog}")
	public void uploadAccount(@PathVariable String catalog, @RequestHeader("Authorization") String token, MultipartFile file) throws ServerException {
		uploadAccountService.call(catalog, file);
	}
	
	@PostMapping("/account")
	public AccountDTO createAccount(@RequestBody AccountDTO account, @RequestHeader("Authorization") String token) throws ServerException {
		return createAccountService.call(account);
	}
	
	@GetMapping("/account/{catalog}")
	public List<AccountDTO> getAccount(@PathVariable String catalog, @RequestHeader("Authorization") String token, @RequestParam(required = false) String filter) throws ServerException {
		return getAccountService.getActive(catalog, filter);
	}
	
	@GetMapping(value="/account/{catalog}/{id}")
	public AccountDTO getAccountById(@PathVariable String catalog, @PathVariable String id, @RequestHeader("Authorization") String token)  throws ServerException  {
		return getAccountService.getById(catalog, id);
	}
	
	@PostMapping("/catalog")
	public CatalogDTO createCatalog(@RequestBody CatalogDTO catalog, @RequestHeader("Authorization") String token) throws ServerException {
		return createCatalogService.call(catalog);
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
