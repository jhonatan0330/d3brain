package com.accounting.voucher;

import java.util.List;

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
import com.shared.domain.SharedIdResponse;
import com.softure.java.dto.exception.ServerException;

@RestController
@RequestMapping("acc/voucher")
public class VoucherRest {

	@Autowired
	private VoucherCreateService createService;
	@Autowired
	private VoucherGetService getVoucherService;
	
	@GetMapping("/{catalog}")
	public List<VoucherDTO> getVouchers(@RequestHeader("Authorization") String token, @PathVariable String catalog) throws ServerException {
		return getVoucherService.call(catalog);
	}
	
	@PostMapping("/manual")
	public SharedIdResponse createManualVoucher(@RequestHeader("Authorization") String token, @RequestBody Voucher voucher) throws ServerException {
		return createService.call(voucher, token);
	}
}
