package d3.accounting;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import d3.accounting.application.PlanGetAccountService;
import d3.accounting.application.PlanGetBalanceService;
import d3.accounting.application.PlanGetCatalogService;
import d3.accounting.domain.AccountDTO;
import d3.accounting.domain.CatalogDTO;
import d3.accounting.domain.ResultMapDTO;
import d3.shared.domain.ServerException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/acc/plan")
public class PlanAccountingController {

	private final PlanGetCatalogService getCatalogService;
	private final PlanGetAccountService getAccountService;
	private final PlanGetBalanceService getBalanceService;

	public PlanAccountingController(@Lazy PlanGetCatalogService getCatalogService,
			@Lazy PlanGetAccountService getAccountService, @Lazy PlanGetBalanceService getBalanceService) {
		this.getCatalogService = getCatalogService;
		this.getAccountService = getAccountService;
		this.getBalanceService = getBalanceService;
	}

	@GetMapping("/balance/{catalog}")
	public List<ResultMapDTO> getBalance(@PathVariable(name = "catalog") String pCatalog,
			@RequestHeader(name = "Authorization") String token) throws ServerException {
		return getBalanceService.getBalance(pCatalog);
	}

	@GetMapping("/account/{catalog}")
	public List<AccountDTO> getAccount(@PathVariable(name = "catalog") String pCatalog,
			@RequestHeader(name = "Authorization") String token,
			@RequestParam(name = "filter", required = false) String pFilter) throws ServerException {
		return getAccountService.getActive(pCatalog, pFilter);
	}

	@GetMapping(value = "/account/{catalog}/{id}")
	public AccountDTO getAccountById(@PathVariable(name = "catalog") String pCatalog,
			@PathVariable(name = "id") String pId, @RequestHeader(name = "Authorization") String token)
			throws ServerException {
		return getAccountService.getByCatalogAndId(pCatalog, pId);
	}

	@GetMapping(value = "/catalog/{id}")
	public CatalogDTO getCatalogById(@PathVariable(name = "id") String pId,
			@RequestHeader(name = "Authorization") String token) throws ServerException {
		return getCatalogService.getById(pId);
	}

	@GetMapping("/catalog")
	public List<CatalogDTO> getCatalog(@RequestHeader(name = "Authorization") String token) throws ServerException {
		return getCatalogService.getActive();
	}

}
