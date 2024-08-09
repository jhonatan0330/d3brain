package com.softure.tariff;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.tariff.application.TariffGetByDocumentService;
import com.softure.tariff.application.TariffGetDimensionService;
import com.softure.tariff.application.base.TarifaSvc;
import com.softure.tariff.domain.TarifaDTO;
import com.softure.tariff.domain.TarifaFilterDTO;
import com.softure.tariff.domain.TariffOptionDTO;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/tariff")
public class TariffRest {

	@Autowired @Lazy 
	private TarifaSvc tarifaService;
	@Autowired @Lazy 
	private TariffGetByDocumentService tariffGetByDocumentService;
	@Autowired @Lazy 
	private TariffGetDimensionService tariffGetDimensionService;
	
	@PostMapping(value = "/fees")
	public List<TarifaDTO> getFees(@RequestHeader("Authorization") String token, @RequestBody TarifaFilterDTO filter)
			throws ServerException {
		filter.setTarifario(tariffGetByDocumentService.call(filter.getDocumento()).getKey());
		if (filter.getEstado() == null)
			filter.setEstado(SharedConstants.STATE_ACTIVE);
		return tarifaService.listarConsulta(filter);
	}

	@PostMapping(value = "/fee")
	public TarifaDTO createFee(@RequestHeader("Authorization") String token, @RequestBody TarifaDTO tariff)
			throws ServerException {
		tariff.setTarifario(tariffGetByDocumentService.call(tariff.getDocumento()).getKey());
		return tarifaService.guardar(tariff, token);
	}

	@GetMapping(value = "/fee")
	public TarifaDTO getFee(@RequestHeader("Authorization") String token, @RequestParam("id") String id)
			throws ServerException {
		return tarifaService.consultaXId(id);
	}

	@PutMapping(value = "/fee")
	public TarifaDTO updateFee(@RequestHeader("Authorization") String token, @RequestBody TarifaDTO tariff)
			throws ServerException {
		tariff.setTarifario(tariffGetByDocumentService.call(tariff.getDocumento()).getKey());
		return tarifaService.actualizar(tariff, token);
	}

	@GetMapping(value = "/dimension")
	public List<TariffOptionDTO> getDimensionToTariff(@RequestHeader("Authorization") String token, @RequestParam("tariff") String tariff, 
			@RequestParam("dimension") String dimension, @RequestParam("filter") String filter)
			throws ServerException {
		return tariffGetDimensionService.call(tariffGetByDocumentService.call(tariff), dimension, filter);
	}
	
	@DeleteMapping(value = "/fee")
	public void deleteFee(@RequestHeader("Authorization") String token, @RequestParam("id") String id)
			throws ServerException {
		tarifaService.inactivar(tarifaService.consultaXId(id), token);
	}
	
}
