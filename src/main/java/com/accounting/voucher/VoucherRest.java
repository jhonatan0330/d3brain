package com.accounting.voucher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

@RestController
@RequestMapping("acc/voucher")
public class VoucherRest {

	@Autowired
	private SharedAuthenticateService tokenService;
	@Autowired
	private VoucherCreateService createService;
	@Autowired
	private VoucherGetService getVoucherService;

	@GetMapping("/{catalog}")
	public List<VoucherDTO> getVouchers(HttpServletRequest request, @RequestHeader("Authorization") String token,
			@PathVariable String catalog) throws ServerException {
		return getVoucherService.call(catalog);
	}

	@PostMapping("/manual")
	public SharedIdResponse createManualVoucher(HttpServletRequest request,
			@RequestHeader("Authorization") String token, @RequestBody Voucher voucher) throws ServerException {
		return createService.call(voucher, tokenService.validate(token, request));
	}
}
