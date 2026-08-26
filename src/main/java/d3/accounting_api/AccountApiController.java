package d3.accounting_api;

import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import d3.accounting_api.application.ApiAccountVoucherService;
import d3.accounting_api.domain.VoucherRequest;
import d3.accounting_plan.application.StackAccountProccessService;
import d3.shared.application.SharedAuthenticateService;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedIdResponse;
import d3.api.application.ApiAuthorizeService;
import d3.authentication.application.UsuarioSesionSvc;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Lazy;

@RestController
@RequestMapping("api_account")
public class AccountApiController {

	private final SharedAuthenticateService tokenService;
	private final ApiAuthorizeService apiAuthorizeService;
	private final ApiAccountVoucherService voucherService;
	private final StackAccountProccessService accountService;
	private final UsuarioSesionSvc autenticacionService;

	public AccountApiController(@Lazy SharedAuthenticateService tokenService, @Lazy ApiAuthorizeService apiAuthorizeService,
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
