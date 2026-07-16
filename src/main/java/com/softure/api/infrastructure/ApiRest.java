package com.softure.api.infrastructure;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shared.domain.ServerException;
import com.shared.domain.SharedIdResponse;
import com.softure.api.application.ApiAuthorizeService;
import com.softure.api.application.ApiGetFieldDataService;
import com.softure.api.application.ApiGetReportService;
import com.softure.api.application.ApiGetService;
import com.softure.api.application.ApiLoginService;
import com.softure.api.application.ApiSendService;
import com.softure.api.application.TransactionLogger;
import com.softure.api.domain.DataFieldResponse;
import com.softure.api.domain.DataFieldWithLoginRequest;
import com.softure.api.domain.DocumentFilterRequest;
import com.softure.api.domain.DocumentFilterWithLoginRequest;
import com.softure.api.domain.DocumentRequest;
import com.softure.api.domain.DocumentResponse;
import com.softure.api.domain.DocumentWithLoginRequest;
import com.softure.api.domain.LoginRequest;
import com.softure.api.domain.ReportRequest;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api")
public class ApiRest {

	private final TransactionLogger transactionLogger;
	private final ApiAuthorizeService apiAuthorizeService;
	private final ApiGetService apiGetService;
	private final ApiGetReportService apiGetReportService;
	private final ApiGetFieldDataService apiGetFieldDataService;
	private final ApiLoginService apiLoginService;
	private final ApiSendService apiSendService;

	public ApiRest(@Lazy TransactionLogger transactionLogger, @Lazy ApiAuthorizeService apiAuthorizeService,
			@Lazy ApiGetService apiGetService, @Lazy ApiGetReportService apiGetReportService,
			@Lazy ApiGetFieldDataService apiGetFieldDataService, @Lazy ApiLoginService apiLoginService,
			@Lazy ApiSendService apiSendService) {
		this.transactionLogger = transactionLogger;
		this.apiAuthorizeService = apiAuthorizeService;
		this.apiGetService = apiGetService;
		this.apiGetReportService = apiGetReportService;
		this.apiGetFieldDataService = apiGetFieldDataService;
		this.apiLoginService = apiLoginService;
		this.apiSendService = apiSendService;
	}

	@PostMapping("/get")
	public List<DocumentResponse> getDocumentFromApi(@RequestHeader(name = "Authorization") String token,
			@RequestHeader(name = "x-api-key") String apiKey, @RequestBody DocumentFilterRequest filter)
			throws ServerException {
		apiAuthorizeService.call(apiKey, token);

		return transactionLogger.executeWithLogging(token, filter, () -> apiGetService.call(token, filter));

	}

	@PostMapping("/getReport")
	public SharedIdResponse getReportFromApi(@RequestHeader(name = "Authorization") String token,
			@RequestHeader(name = "x-api-key") String apiKey, @RequestBody ReportRequest filter)
			throws ServerException {
		apiAuthorizeService.call(apiKey, token);
		return apiGetReportService.call(token, filter);
	}

	@PostMapping("/getWithLogin")
	public List<DocumentResponse> getDocumentFromWithLoginApi(HttpServletRequest request,
			@RequestHeader(name = "x-api-key") String apiKey, @RequestBody DocumentFilterWithLoginRequest filter)
			throws ServerException {
		SharedIdResponse token = apiLoginService.call(filter.getLogin(), request);
		apiAuthorizeService.call(apiKey, token.getId());

		return transactionLogger.executeWithLogging(token.getId(), filter.getDocument(),
				() -> apiGetService.call(token.getId(), filter.getDocument()));

	}

	@PostMapping("/getDataFieldWithLogin")
	public DataFieldResponse getDataFieldFromWithLoginApi(HttpServletRequest request,
			@RequestHeader(name = "x-api-key") String apiKey, @RequestBody DataFieldWithLoginRequest filter)
			throws ServerException {

		SharedIdResponse token = apiLoginService.call(filter.getLogin(), request);
		apiAuthorizeService.call(apiKey, token.getId());

		return transactionLogger.executeWithLogging(token.getId(), filter.getField(),
				() -> apiGetFieldDataService.call(token.getId(), filter.getField()));

	}

	@PostMapping("/login")
	public SharedIdResponse login(HttpServletRequest request, @RequestHeader(name = "x-api-key") String apiKey,
			@RequestBody LoginRequest login) throws ServerException {
		apiAuthorizeService.call(apiKey, null);
		return apiLoginService.call(login, request);
	}

	@PostMapping("/send")
	public SharedIdResponse send(@RequestHeader(name = "Authorization") String token,
			@RequestHeader(name = "x-api-key") String apiKey, @RequestBody DocumentRequest item)
			throws ServerException {
		apiAuthorizeService.call(apiKey, token);

		return transactionLogger.executeWithLogging(token, item, () -> apiSendService.call(token, item));
	}

	@PostMapping("/sendWithLogin")
	public SharedIdResponse sendWithLogin(HttpServletRequest request, @RequestHeader(name = "x-api-key") String apiKey,
			@RequestBody DocumentWithLoginRequest item) throws ServerException {
		SharedIdResponse token = apiLoginService.call(item.getLogin(), request);
		apiAuthorizeService.call(apiKey, token.getId());

		return transactionLogger.executeWithLogging(token.getId(), item.getDocument(),
				() -> apiSendService.call(token.getId(), item.getDocument()));
	}

	@GetMapping("/ok")
	public String ok(@RequestHeader(name = "x-api-key") String apiKey) throws ServerException {
		apiAuthorizeService.call(apiKey, null);
		return "OK";
	}

	@GetMapping("/ping")
	public String ping() throws ServerException {
		return "PING";
	}

}
