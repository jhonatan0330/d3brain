package com.softure.tariff;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.softure.tariff.application.base.TarifaSvc;
import com.softure.tariff.application.base.TarifarioService;
import com.softure.tariff.domain.TarifaDTO;
import com.softure.tariff.domain.TarifaFilterDTO;
import com.softure.tariff.domain.TarifarioDTO;
import com.softure.tariff.domain.TarifarioFilterDTO;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/tariff")
public class TariffRest {

	@Autowired
	private TarifaSvc tarifaService;
	@Autowired
	private TarifarioService tariffService;

	@PostMapping(value = "/fees")
	public List<TarifaDTO> getFees(@RequestHeader("Authorization") String token, @RequestBody TarifaFilterDTO filter)
			throws ServerException {
		if (filter.getTarifario() == null)
			throw new ServerException("Se debe seleccionar un tarifario");
		TarifarioFilterDTO tariffFilter = new TarifarioFilterDTO();
		tariffFilter.setDocumento(filter.getTarifario());
		TarifarioDTO tariffDTO = tariffService.getOne(tariffFilter);
		if (tariffDTO == null)
			throw new ServerException("El tarifario no existe con ese identificador");
		filter.setTarifario(tariffDTO.getKey());
		if (filter.getEstado() == null)
			filter.setEstado(SharedConstants.STATE_ACTIVE);
		return tarifaService.listarConsulta(filter);
	}

	@PostMapping(value = "/fee")
	public TarifaDTO createFee(@RequestHeader("Authorization") String token, @RequestBody TarifaDTO tariff)
			throws ServerException {
		return tarifaService.guardar(tariff, token);
	}

	@GetMapping(value = "/fee")
	public TarifaDTO getFee(@RequestHeader("Authorization") String token, @RequestParam String id)
			throws ServerException {
		return tarifaService.consultaXId(id);
	}

	@PutMapping(value = "/fee")
	public TarifaDTO updateFee(@RequestHeader("Authorization") String token, @RequestBody TarifaDTO tariff)
			throws ServerException {
		return tarifaService.actualizar(tariff, token);
	}

}
