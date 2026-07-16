package com.accounting.api;

import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accounting.api.application.ApiAccountVoucherService;
import com.accounting.api.domain.VoucherRequest;
import com.accounting.plan.application.StackAccountProccessService;
import com.shared.application.SharedAuthenticateService;
import com.shared.domain.ServerException;
import com.shared.domain.SharedIdResponse;
import com.softure.api.application.ApiAuthorizeService;
import com.softure.authentication.application.UsuarioSesionSvc;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Lazy;

@RestController
@RequestMapping("api_account")
public class AccountApiRest {

	private final SharedAuthenticateService tokenService;
	private final ApiAuthorizeService apiAuthorizeService;
	private final ApiAccountVoucherService voucherService;
	private final StackAccountProccessService accountService;
	private final UsuarioSesionSvc autenticacionService;

	public AccountApiRest(@Lazy SharedAuthenticateService tokenService, @Lazy ApiAuthorizeService apiAuthorizeService,
			@Lazy ApiAccountVoucherService voucherService, @Lazy StackAccountProccessService accountService,
			@Lazy UsuarioSesionSvc autenticacionService) {
		this.tokenService = tokenService;
		this.apiAuthorizeService = apiAuthorizeService;
		this.voucherService = voucherService;
		this.accountService = accountService;
		this.autenticacionService = autenticacionService;
	}

	@PostMapping("/voucher")
	public SharedIdResponse send(HttpServletRequest request, @RequestHeader(name = "x-api-key") String apiKey,
			@RequestBody VoucherRequest item) throws ServerException {
		String token = autenticacionService.generateAdministratorToken().getLlaveTabla();
		apiAuthorizeService.call(apiKey, token);
		return voucherService.call(tokenService.validate(token, request), item);
	}

	@GetMapping("/ok")
	public String ok(@RequestHeader(name = "x-api-key") String apiKey) throws ServerException {
		apiAuthorizeService.call(apiKey, null);
		return "OK";
	}

	@GetMapping("/ping")
	public String ping() throws ServerException {
		return "PING ******* ACUMULADOR (" + accountService.call() + ") ***" + new Date().toString();
	}
}
