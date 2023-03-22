package com.softure.massiveload.infrastructure;

import com.softure.java.dto.exception.ServerException;
import com.softure.massiveload.application.MassiveItemSincronizeService;
import com.softure.massiveload.application.MassiveSincronizeService;
import com.softure.shared.domain.SharedIdResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// End of user code

@RestController
@RequestMapping("massiveload")
public class MassiveRest {


	@Autowired
	private MassiveItemSincronizeService cargaMasivaItemsincronizeService;

	@PostMapping("/sincronizeCargaMasivaItem")
	public SharedIdResponse sincronizeCargaMasivaItem(@RequestHeader(name = "Authorization") String token
			,@RequestBody String itemId
		) throws ServerException {
		return cargaMasivaItemsincronizeService.call(token, itemId);
	}

	@Autowired
	private MassiveSincronizeService cargaMasivasincronizeService;

	@PostMapping("/sincronizeCargaMasiva")
	public SharedIdResponse sincronizeCargaMasiva(@RequestHeader(name = "Authorization") String token
			,@RequestBody String fileUrl
			,@RequestBody String template
		) throws ServerException {
		return cargaMasivasincronizeService.call(token, fileUrl, template);
	}

}
