package d3.accounting_voucher;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import d3.accounting_api.domain.VoucherRangeRequest;
import d3.accounting_api.domain.VoucherPrepareRequest;
import d3.accounting_voucher.application.VoucherRangeService;
import d3.accounting_voucher.application.VoucherCreateService;
import d3.accounting_voucher.application.VoucherDeleteService;
import d3.accounting_voucher.application.VoucherGetService;
import d3.accounting_voucher.application.VoucherReCreateService;
import d3.accounting_voucher.domain.Voucher;
import d3.accounting_voucher.domain.VoucherDTO;
import d3.shared.application.SharedAuthenticateService;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedIdResponse;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Lazy;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("acc/voucher")
public class VoucherController {

	private final SharedAuthenticateService tokenService;
	private final VoucherCreateService createService;
	private final VoucherDeleteService deleteService;
	private final VoucherGetService getVoucherService;
	private final VoucherReCreateService recreateService;
	private final VoucherRangeService range;

	public VoucherController(@Lazy SharedAuthenticateService tokenService, @Lazy VoucherCreateService createService,
			@Lazy VoucherDeleteService deleteService, @Lazy VoucherGetService getVoucherService,
			@Lazy VoucherReCreateService recreateService, @Lazy VoucherRangeService range) {
		this.tokenService = tokenService;
		this.createService = createService;
		this.deleteService = deleteService;
		this.getVoucherService = getVoucherService;
		this.recreateService = recreateService;
		this.range = range;
	}

	@GetMapping("/{catalog}")
	public List<VoucherDTO> getVouchers(HttpServletRequest request, @RequestHeader("Authorization") String token,
			@PathVariable(name = "catalog") String pCatalog) throws ServerException {
		return getVoucherService.call(pCatalog);
	}

	@GetMapping("/one/{voucherId}")
	public Voucher getVoucher(HttpServletRequest request, @RequestHeader("Authorization") String token,
			@PathVariable(name = "voucherId") String pVoucherId) throws ServerException {
		return getVoucherService.getById(pVoucherId);
	}

	@PostMapping("/manual")
	public SharedIdResponse createManualVoucher(HttpServletRequest request,
			@RequestHeader("Authorization") String token, @RequestBody Voucher voucher) throws ServerException {
		return createService.call(voucher, tokenService.validate(token, request));
	}

	@DeleteMapping("/manual/{voucherId}")
	public SharedIdResponse deleteManualVoucher(HttpServletRequest request,
			@RequestHeader("Authorization") String token, @PathVariable(name = "voucherId") String pVoucherId)
			throws ServerException {
		return deleteService.callById(pVoucherId, token);
	}

	@PostMapping("/generate-voucher")
	public SharedIdResponse generateVoucher(HttpServletRequest request, @RequestHeader("Authorization") String token,
			@RequestBody VoucherPrepareRequest item) throws ServerException {
		return recreateService.call(item, tokenService.validate(token, request));
	}

	@PostMapping("/document")
	public SharedIdResponse getVoucherId(HttpServletRequest request, @RequestHeader("Authorization") String token,
			@RequestBody VoucherPrepareRequest item) throws ServerException {
		return getVoucherService.getByDocument(item, tokenService.validate(token, request));
	}

	@PostMapping("/range-clear-voucher")
	public SharedIdResponse rangeClearVoucher(HttpServletRequest request, @RequestHeader("Authorization") String token,
			@RequestBody VoucherRangeRequest item) throws ServerException {
		return range.clear(item, tokenService.validate(token, request));
	}

	@PostMapping("/range-create-voucher")
	public SharedIdResponse rangeCreateVoucher(HttpServletRequest request, @RequestHeader("Authorization") String token,
			@RequestBody VoucherRangeRequest item) throws ServerException {
		return range.create(item, tokenService.validate(token, request));
	}
}
