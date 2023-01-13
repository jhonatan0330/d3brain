package com.softure.api.infrastructure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softure.api.application.ApiAuthorizeService;
import com.softure.api.application.ApiGetService;
import com.softure.api.application.ApiLoginService;
import com.softure.api.application.ApiSendService;
import com.softure.api.domain.DocumentVO;
import com.softure.api.domain.DocumentWithLoginVO;
import com.softure.api.domain.FilterDocumentVO;
import com.softure.api.domain.FilterWithLoginVO;
import com.softure.api.domain.LoginVO;
import com.softure.java.domain.IdResponse;
import com.softure.java.dto.exception.ServerException;

@RestController
@RequestMapping("api")
public class ApiRest {

	@Autowired ApiAuthorizeService apiAuthorizeService;
	@Autowired ApiGetService apiGetService;
	@Autowired ApiLoginService apiLoginService;
	@Autowired ApiSendService apiSendService;
	
	@PostMapping("/get")
	public List<DocumentVO> getFromApi(@RequestHeader(name = "Authorization") String token, @RequestHeader(name = "x-api-key") String apiKey
			,@RequestBody FilterDocumentVO filter
		) throws ServerException {
		apiAuthorizeService.call(apiKey,token);
		return apiGetService.call(token, filter);
	}
	
	@PostMapping("/getWithLogin")
	public List<DocumentVO> getFromWithLoginApi(@RequestHeader(name = "x-api-key") String apiKey
			,@RequestBody FilterWithLoginVO filter
		) throws ServerException {
		IdResponse token = apiLoginService.call(filter.getLogin());
		apiAuthorizeService.call(apiKey, token.getId());
		return apiGetService.call(token.getId(), filter.getDocument());
	}
	
	@PostMapping("/login")
	public IdResponse login(@RequestHeader(name = "x-api-key") String apiKey
			,@RequestBody LoginVO login
		) throws ServerException {
		apiAuthorizeService.call(apiKey, null);
		return apiLoginService.call(login);
	}
	
	@PostMapping("/send")
	public IdResponse send(@RequestHeader(name = "Authorization") String token, @RequestHeader(name = "x-api-key") String apiKey
			,@RequestBody DocumentVO item
		) throws ServerException {
		apiAuthorizeService.call(apiKey,token);
		return apiSendService.call(token, item);
	}
	
	@PostMapping("/sendWithLogin")
	public IdResponse sendWithLogin(@RequestHeader(name = "x-api-key") String apiKey
			,@RequestBody DocumentWithLoginVO item
		) throws ServerException {
		IdResponse token = apiLoginService.call(item.getLogin());
		apiAuthorizeService.call(apiKey, token.getId());
		return apiSendService.call(token.getId(), item.getDocument());
	}
	
	@GetMapping("/ok")
	public String ok(@RequestHeader(name = "x-api-key") String apiKey
		) throws ServerException {
		apiAuthorizeService.call(apiKey, null);
		return "OK";
	}
	
	@GetMapping("/ping")
	public String ping() throws ServerException {
		return "PING";
	}

}
