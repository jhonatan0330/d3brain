package com.softure.massiveload.infrastructure;

import com.shared.domain.ServerException;
import com.shared.domain.SharedIdResponse;
import com.softure.massiveload.application.MassiveItemSincronizeService;
import com.softure.massiveload.application.MassiveSincronizeService;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// End of user code

@RestController
@RequestMapping("massiveload")
public class MassiveRest {


	@Autowired @Lazy 
	private MassiveItemSincronizeService cargaMasivaItemsincronizeService;

	@PostMapping("/sincronizeCargaMasivaItem")
	public SharedIdResponse sincronizeCargaMasivaItem(@RequestHeader(name = "Authorization") String token
			,@RequestBody String itemId
		) throws ServerException {
		return cargaMasivaItemsincronizeService.call(token, itemId);
	}

	@Autowired @Lazy 
	private MassiveSincronizeService cargaMasivasincronizeService;

	@PostMapping("/sincronizeCargaMasiva")
	public SharedIdResponse sincronizeCargaMasiva(@RequestHeader(name = "Authorization") String token
			,@RequestBody String fileUrl
			,@RequestBody String template
		) throws ServerException {
		return cargaMasivasincronizeService.call(token, fileUrl, template);
	}

}
