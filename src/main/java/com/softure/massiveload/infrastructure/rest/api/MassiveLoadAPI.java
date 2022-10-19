package com.softure.massiveload.infrastructure.rest.api;

import com.softure.java.dto.exception.ServerException;
import com.softure.massiveload.application.ICargaMasivaItemSincronizeService;
import com.softure.massiveload.application.ICargaMasivaSincronizeService;

// Start of user code importsModel
import com.softure.java.domain.IdResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// End of user code

@RestController
@RequestMapping("massiveload")
public class MassiveLoadAPI {


	@Autowired
	private ICargaMasivaItemSincronizeService cargaMasivaItemsincronizeService;

	@PostMapping("/sincronizeCargaMasivaItem")
	public IdResponse sincronizeCargaMasivaItem(@RequestHeader(name = "Authorization") String token
			,@RequestBody String itemId
		) throws ServerException {
		return cargaMasivaItemsincronizeService.call(token, itemId);
	}

	@Autowired
	private ICargaMasivaSincronizeService cargaMasivasincronizeService;

	@PostMapping("/sincronizeCargaMasiva")
	public IdResponse sincronizeCargaMasiva(@RequestHeader(name = "Authorization") String token
			,@RequestBody String fileUrl
			,@RequestBody String template
		) throws ServerException {
		return cargaMasivasincronizeService.call(token, fileUrl, template);
	}

}
