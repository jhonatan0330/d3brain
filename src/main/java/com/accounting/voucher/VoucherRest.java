package com.accounting.voucher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accounting.api.domain.VoucherPrepareRequest;
import com.accounting.voucher.application.VoucherCreateService;
import com.accounting.voucher.application.VoucherDeleteService;
import com.accounting.voucher.application.VoucherGetService;
import com.accounting.voucher.application.VoucherReCreateService;
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
	private VoucherDeleteService deleteService;
	@Autowired @Lazy 
	private VoucherGetService getVoucherService;
	@Autowired @Lazy 
	private VoucherReCreateService recreateService;

	@GetMapping("/{catalog}")
	public List<VoucherDTO> getVouchers(HttpServletRequest request, @RequestHeader("Authorization") String token,
			@PathVariable String catalog) throws ServerException {
		return getVoucherService.call(catalog);
	}
	
	@GetMapping("/one/{voucherId}")
	public Voucher getVoucher(HttpServletRequest request, @RequestHeader("Authorization") String token,
			 @PathVariable String voucherId) throws ServerException {
		return getVoucherService.getById( voucherId);
	}

	@PostMapping("/manual")
	public SharedIdResponse createManualVoucher(HttpServletRequest request,
			@RequestHeader("Authorization") String token, @RequestBody Voucher voucher) throws ServerException {
		return createService.call(voucher, tokenService.validate(token, request));
	}
	
	@DeleteMapping("/manual/{voucherId}")
	public SharedIdResponse deleteManualVoucher(HttpServletRequest request,
			@RequestHeader("Authorization") String token, @PathVariable String voucherId) throws ServerException {
		return deleteService.callById(voucherId, token);
	}
	

	@PostMapping("/generate-voucher")
	public SharedIdResponse generateVoucher(HttpServletRequest request, @RequestHeader("Authorization") String token, 
			@RequestBody VoucherPrepareRequest item
		) throws ServerException {
		return recreateService.call(item, tokenService.validate(token, request));
	}
	
	@PostMapping("/document")
	public SharedIdResponse getVoucherId(HttpServletRequest request, @RequestHeader("Authorization") String token,
			@RequestBody VoucherPrepareRequest item) throws ServerException {
		return getVoucherService.getByDocument( item, tokenService.validate(token, request));
	}

}
