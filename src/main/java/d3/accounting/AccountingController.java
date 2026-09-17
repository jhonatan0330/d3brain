package d3.accounting;

import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import d3.accounting.application.ApiAccountVoucherService;
import d3.accounting.application.PlanGetAccountService;
import d3.accounting.application.PlanGetBalanceService;
import d3.accounting.application.PlanGetCatalogService;
import d3.accounting.application.StackAccountProccessService;
import d3.accounting.application.VoucherCreateService;
import d3.accounting.application.VoucherDeleteService;
import d3.accounting.application.VoucherGetService;
import d3.accounting.application.VoucherRangeService;
import d3.accounting.application.VoucherReCreateService;
import d3.accounting.domain.AccountDTO;
import d3.accounting.domain.CatalogDTO;
import d3.accounting.domain.ResultMapDTO;
import d3.accounting.domain.Voucher;
import d3.accounting.domain.VoucherDTO;
import d3.accounting.domain.VoucherPrepareRequest;
import d3.accounting.domain.VoucherRangeRequest;
import d3.accounting.domain.VoucherRequest;
import d3.api.application.ApiAuthorizeService;
import d3.authentication.application.UsuarioSesionSvc;
import d3.shared.application.SessionContext;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedIdResponse;
import d3.shared.domain.SharedToken;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/acc")
public class AccountingController {

	private final VoucherCreateService createService;
	private final VoucherDeleteService deleteService;
	private final VoucherGetService getVoucherService;
	private final VoucherReCreateService recreateService;
	private final VoucherRangeService range;
	private final PlanGetCatalogService getCatalogService;
	private final PlanGetAccountService getAccountService;
	private final PlanGetBalanceService getBalanceService;
	private final ApiAuthorizeService apiAuthorizeService;
	private final ApiAccountVoucherService voucherService;
	private final StackAccountProccessService accountService;
	private final UsuarioSesionSvc autenticacionService;

	public AccountingController(@Lazy VoucherCreateService createService, @Lazy VoucherDeleteService deleteService,
			@Lazy VoucherGetService getVoucherService, @Lazy VoucherReCreateService recreateService,
			@Lazy VoucherRangeService range, @Lazy PlanGetCatalogService getCatalogService,
			@Lazy PlanGetAccountService getAccountService, @Lazy PlanGetBalanceService getBalanceService,
			@Lazy ApiAuthorizeService apiAuthorizeService, @Lazy ApiAccountVoucherService voucherService,
			@Lazy StackAccountProccessService accountService, @Lazy UsuarioSesionSvc autenticacionService) {
		this.createService = createService;
		this.deleteService = deleteService;
		this.getVoucherService = getVoucherService;
		this.recreateService = recreateService;
		this.range = range;
		this.getCatalogService = getCatalogService;
		this.getAccountService = getAccountService;
		this.getBalanceService = getBalanceService;
		this.apiAuthorizeService = apiAuthorizeService;
		this.voucherService = voucherService;
		this.accountService = accountService;
		this.autenticacionService = autenticacionService;
	}

	// ==================== VOUCHER ENDPOINTS ====================

	@GetMapping("/voucher/{catalog}")
	public List<VoucherDTO> getVouchers(@PathVariable(name = "catalog") String pCatalog) throws ServerException {
		return getVoucherService.call(pCatalog);
	}

	@GetMapping("/voucher/one/{voucherId}")
	public Voucher getVoucher(@PathVariable(name = "voucherId") String pVoucherId) throws ServerException {
		return getVoucherService.getById(pVoucherId);
	}

	@PostMapping("/voucher/manual")
	public SharedIdResponse createManualVoucher(@RequestBody Voucher voucher) throws ServerException {
		return createService.call(voucher);
	}

	@DeleteMapping("/voucher/manual/{voucherId}")
	public SharedIdResponse deleteManualVoucher(@PathVariable(name = "voucherId") String pVoucherId)
			throws ServerException {
		return deleteService.callById(pVoucherId);
	}

	@PostMapping("/voucher/generate-voucher")
	public SharedIdResponse generateVoucher(@RequestBody VoucherPrepareRequest item) throws ServerException {
		return recreateService.call(item);
	}

	@PostMapping("/voucher/document")
	public SharedIdResponse getVoucherId(@RequestBody VoucherPrepareRequest item) throws ServerException {
		return getVoucherService.getByDocument(item);
	}

	@PostMapping("/voucher/range-clear-voucher")
	public SharedIdResponse rangeClearVoucher(@RequestBody VoucherRangeRequest item) throws ServerException {
		return range.clear(item);
	}

	@PostMapping("/voucher/range-create-voucher")
	public SharedIdResponse rangeCreateVoucher(@RequestBody VoucherRangeRequest item) throws ServerException {
		return range.create(item);
	}

	// ==================== PLAN ENDPOINTS ====================

	@GetMapping("/plan/balance/{catalog}")
	public List<ResultMapDTO> getBalance(@PathVariable(name = "catalog") String pCatalog) throws ServerException {
		return getBalanceService.getBalance(pCatalog);
	}

	@GetMapping("/plan/account/{catalog}")
	public List<AccountDTO> getAccount(@PathVariable(name = "catalog") String pCatalog,
			@RequestParam(name = "filter", required = false) String pFilter) throws ServerException {
		return getAccountService.getActive(pCatalog, pFilter);
	}

	@GetMapping(value = "/plan/account/{catalog}/{id}")
	public AccountDTO getAccountById(@PathVariable(name = "catalog") String pCatalog,
			@PathVariable(name = "id") String pId) throws ServerException {
		return getAccountService.getByCatalogAndId(pCatalog, pId);
	}

	@GetMapping(value = "/plan/catalog/{id}")
	public CatalogDTO getCatalogById(@PathVariable(name = "id") String pId) throws ServerException {
		return getCatalogService.getById(pId);
	}

	@GetMapping("/plan/catalog")
	public List<CatalogDTO> getCatalog() throws ServerException {
		return getCatalogService.getActive();
	}

	// ==================== API ENDPOINTS (antes api_account) ====================

	@PostMapping("/api/voucher")
	public SharedIdResponse send(@RequestHeader(name = "x-api-key") String apiKey, @RequestBody VoucherRequest item)
			throws ServerException {
		String token = autenticacionService.generateAdministratorToken().getLlaveTabla();
		SharedToken adminToken = autenticacionService.getUserToken(token);
		apiAuthorizeService.call(apiKey);
		SharedToken previous = SessionContext.getCurrent();
		try {
			SessionContext.setCurrent(adminToken);
			return voucherService.call(item);
		} finally {
			SessionContext.setCurrent(previous);
		}
	}

	@GetMapping("/api/ok")
	public String ok(@RequestHeader(name = "x-api-key") String apiKey) throws ServerException {
		apiAuthorizeService.call(apiKey);
		return "OK";
	}

	@GetMapping("/api/ping")
	public String ping() throws ServerException {
		return "PING ******* ACUMULADOR (" + accountService.call() + ") ***" + new Date().toString();
	}
}
