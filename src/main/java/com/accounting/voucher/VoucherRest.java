package com.accounting.voucher;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accounting.voucher.application.VoucherCreateService;
import com.accounting.voucher.application.VoucherGetService;
import com.accounting.voucher.domain.Voucher;
import com.accounting.voucher.domain.VoucherDTO;
import com.shared.application.SharedAuthenticateService;
import com.shared.domain.ServerException;
import com.shared.domain.SharedIdResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("acc/voucher")
public class VoucherRest {

	@Autowired @Lazy 
	private SharedAuthenticateService tokenService;
	@Autowired @Lazy 
	private VoucherCreateService createService;
	@Autowired @Lazy 
	private VoucherGetService getVoucherService;

	@GetMapping("/{catalog}")
	public List<VoucherDTO> getVouchers(HttpServletRequest request, @RequestHeader("Authorization") String token,
			@PathVariable("catalog") String catalog) throws ServerException {
		return getVoucherService.call(catalog);
	}
	
	@GetMapping("/{catalog}/{voucherId}")
	public Voucher getVouchers(HttpServletRequest request, @RequestHeader("Authorization") String token,
			@PathVariable("catalog") String catalog, @PathVariable("voucherId") String voucherId) throws ServerException {
		return getVoucherService.getById(catalog, voucherId);
	}

	@PostMapping("/manual")
	public SharedIdResponse createManualVoucher(HttpServletRequest request,
			@RequestHeader("Authorization") String token, @RequestBody Voucher voucher) throws ServerException {
		return createService.call(voucher, tokenService.validate(token, request));
	}
	
	@PutMapping("/manual")
	public SharedIdResponse updateManualVoucher(HttpServletRequest request,
			@RequestHeader("Authorization") String token, @RequestBody Voucher voucher) throws ServerException {
		return createService.update(voucher, tokenService.validate(token, request));
	}
	
	@DeleteMapping("/manual/{voucherId}")
	public VoucherDTO deleteManualVoucher(HttpServletRequest request,
			@RequestHeader("Authorization") String token, @PathVariable("voucherId") String voucherId) throws ServerException {
		return createService.delete(voucherId);
	}
}
