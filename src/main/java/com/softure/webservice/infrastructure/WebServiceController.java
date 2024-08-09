package com.softure.webservice.infrastructure;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shared.domain.ServerException;
import com.shared.domain.SharedIdResponse;
import com.softure.webservice.application.WebServiceCopyAPI;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/webservice")
public class WebServiceController {
	
	@Autowired @Lazy 
	private WebServiceCopyAPI copyService;
	
	@PostMapping(value="/copy")
	public SharedIdResponse copy(@RequestHeader("Authorization") String token, @RequestParam("apiId") String apiId) throws ServerException {
		return copyService.call(apiId, token);
	}
	
}
