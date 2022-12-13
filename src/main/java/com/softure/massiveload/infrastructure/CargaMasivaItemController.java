package com.softure.massiveload.infrastructure;

import com.softure.java.domain.IdResponse;
import com.softure.java.dto.exception.ServerException;
import com.softure.massiveload.application.ICargaMasivaItemService;
import com.softure.massiveload.domain.CargaMasivaItem;
import com.softure.massiveload.domain.CargaMasivaItemFilterDTO;

import java.util.List;
// Start of user code importsModel
import java.util.Date;
// End of user code

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("massiveload/cargaMasivaItem")
public class CargaMasivaItemController {

	@Autowired
	private ICargaMasivaItemService cargaMasivaItemService;

	@GetMapping("")
	public List<CargaMasivaItem> find(@RequestHeader(name = "Authorization") String token, String cargaFilter, String documentoFilter, Date fechaSerializacionMin, Date fechaSerializacionMax, Date fechaSincronizacionMin, Date fechaSincronizacionMax, String modeloFilter, String nombreFilter, String progresoFilter, 
			@RequestParam(defaultValue = "A") String state, 
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "30") int size) throws ServerException {
		var filter = new CargaMasivaItemFilterDTO(state, page, size);
		filter.setCarga(cargaFilter); 
		filter.setDocumento(documentoFilter); 
		filter.setFechaSerializacionMax(fechaSerializacionMax);
		filter.setFechaSerializacionMin(fechaSerializacionMin); 
		filter.setFechaSincronizacionMax(fechaSincronizacionMax);
		filter.setFechaSincronizacionMin(fechaSincronizacionMin); 
		filter.setModelo(modeloFilter); 
		filter.setNombre(nombreFilter); 
		filter.setProgreso(progresoFilter); 
		return cargaMasivaItemService.find(token, filter);
	}

	@GetMapping("/get")
	public CargaMasivaItem get(@RequestHeader(name = "Authorization") String token, String cargaFilter, String documentoFilter, Date fechaSerializacionMin, Date fechaSerializacionMax, Date fechaSincronizacionMin, Date fechaSincronizacionMax, String modeloFilter, String nombreFilter, String progresoFilter, 
			@RequestParam(defaultValue = "A") String state) throws ServerException {
		var filter = new CargaMasivaItemFilterDTO();
		filter.setEstado(state);
		filter.setCarga(cargaFilter); 
		filter.setDocumento(documentoFilter); 
		filter.setFechaSerializacionMax(fechaSerializacionMax);
		filter.setFechaSerializacionMin(fechaSerializacionMin); 
		filter.setFechaSincronizacionMax(fechaSincronizacionMax);
		filter.setFechaSincronizacionMin(fechaSincronizacionMin); 
		filter.setModelo(modeloFilter); 
		filter.setNombre(nombreFilter); 
		filter.setProgreso(progresoFilter); 
		return cargaMasivaItemService.get(token, filter);
	}

	@GetMapping("/{id}")
	public CargaMasivaItem findById(@RequestHeader(name = "Authorization") String token
			, @PathVariable("id") String id) throws ServerException {
		return cargaMasivaItemService.findById(id);
	}

	@PostMapping("")
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public IdResponse save(@RequestHeader(name = "Authorization") String token
			, @RequestBody CargaMasivaItem body) throws ServerException {
		return new IdResponse(cargaMasivaItemService.save(token, body).getCargaMasivaItemId());
	}

	@PutMapping("/{id}")
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public IdResponse update(@RequestHeader(name = "Authorization") String token
			, @RequestBody CargaMasivaItem body, @PathVariable("id") String id) throws ServerException {
		return new IdResponse(cargaMasivaItemService.update(token, body, id).getCargaMasivaItemId());
	}
	
	@PutMapping("/activate/{id}")
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public void activate(@RequestHeader(name = "Authorization") String token
		, @PathVariable("id") String id) throws ServerException {
		cargaMasivaItemService.activate(token, id);
	}

	@PutMapping("/inactivate/{id}")
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public void inactivate(@RequestHeader(name = "Authorization") String token
		, @PathVariable("id") String id) throws ServerException {
		cargaMasivaItemService.inactivate(token, id);
	}

}
