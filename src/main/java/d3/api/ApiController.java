package d3.api;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import d3.api.application.ApiAuthorizeService;
import d3.api.application.ApiGetFieldDataService;
import d3.api.application.ApiGetReportService;
import d3.api.application.ApiGetService;
import d3.api.application.ApiLoginService;
import d3.api.application.ApiSendService;
import d3.api.application.TransactionLogger;
import d3.api.domain.DataFieldResponse;
import d3.api.domain.DataFieldWithLoginRequest;
import d3.api.domain.DocumentFilterRequest;
import d3.api.domain.DocumentFilterWithLoginRequest;
import d3.api.domain.DocumentRequest;
import d3.api.domain.DocumentResponse;
import d3.api.domain.DocumentWithLoginRequest;
import d3.api.domain.LoginRequest;
import d3.api.domain.ReportRequest;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedIdResponse;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api")
public class ApiController {

	private final TransactionLogger transactionLogger;
	private final ApiAuthorizeService apiAuthorizeService;
	private final ApiGetService apiGetService;
	private final ApiGetReportService apiGetReportService;
	private final ApiGetFieldDataService apiGetFieldDataService;
	private final ApiLoginService apiLoginService;
	private final ApiSendService apiSendService;

	public ApiController(@Lazy TransactionLogger transactionLogger, @Lazy ApiAuthorizeService apiAuthorizeService,
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
	public List<DocumentResponse> getDocumentFromApi(@RequestHeader(name = "x-api-key") String apiKey,
			@RequestBody DocumentFilterRequest filter) throws ServerException {
		apiAuthorizeService.call(apiKey);
		return transactionLogger.executeWithLogging(filter, () -> apiGetService.call(filter));

	}

	@PostMapping("/getReport")
	public SharedIdResponse getReportFromApi(@RequestHeader(name = "x-api-key") String apiKey,
			@RequestBody ReportRequest filter) throws ServerException {
		apiAuthorizeService.call(apiKey);
		return apiGetReportService.call(filter);
	}

	@PostMapping("/getWithLogin")
	public List<DocumentResponse> getDocumentFromWithLoginApi(@RequestHeader(name = "x-api-key") String apiKey,
			@RequestBody DocumentFilterWithLoginRequest filter) throws ServerException {
		apiAuthorizeService.call(apiKey);
		return transactionLogger.executeWithLogging(filter.getDocument(),
				() -> apiGetService.call(filter.getDocument()));

	}

	@PostMapping("/getDataFieldWithLogin")
	public DataFieldResponse getDataFieldFromWithLoginApi(@RequestHeader(name = "x-api-key") String apiKey,
			@RequestBody DataFieldWithLoginRequest filter) throws ServerException {
		apiAuthorizeService.call(apiKey);
		return transactionLogger.executeWithLogging(filter.getField(),
				() -> apiGetFieldDataService.call(filter.getField()));

	}

	@PostMapping("/login")
	public SharedIdResponse login(HttpServletRequest request, @RequestHeader(name = "x-api-key") String apiKey,
			@RequestBody LoginRequest login) throws ServerException {
		apiAuthorizeService.call(apiKey);
		return apiLoginService.call(login, request);
	}

	@PostMapping("/send")
	public SharedIdResponse send(@RequestHeader(name = "x-api-key") String apiKey, @RequestBody DocumentRequest item)
			throws ServerException {
		apiAuthorizeService.call(apiKey);
		return transactionLogger.executeWithLogging(item, () -> apiSendService.call(item));
	}

	@PostMapping("/sendWithLogin")
	public SharedIdResponse sendWithLogin(@RequestHeader(name = "x-api-key") String apiKey,
			@RequestBody DocumentWithLoginRequest item) throws ServerException {
		apiAuthorizeService.call(apiKey);
		return transactionLogger.executeWithLogging(item.getDocument(), () -> apiSendService.call(item.getDocument()));
	}

	@GetMapping("/ok")
	public String ok(@RequestHeader(name = "x-api-key") String apiKey) throws ServerException {
		apiAuthorizeService.call(apiKey);
		return "OK";
	}

	@GetMapping("/ping")
	public String ping() {
		return "PING";
	}

}
