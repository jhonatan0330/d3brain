package com.softure.massiveload.infrastructure;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
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

import com.shared.domain.ServerException;
import com.shared.domain.SharedIdResponse;
import com.softure.massiveload.application.MassiveCRUDItemService;
import com.softure.massiveload.domain.MasivaItemRequest;
import com.softure.massiveload.domain.MassiveItemFilter;


@RestController
@RequestMapping("massiveload/cargaMasivaItem")
public class MassiveItemController {

	@Autowired @Lazy 
	private MassiveCRUDItemService cargaMasivaItemService;

	@GetMapping("")
	public List<MasivaItemRequest> find(@RequestHeader(name = "Authorization") String token, String cargaFilter, String documentoFilter, Date fechaSerializacionMin, Date fechaSerializacionMax, Date fechaSincronizacionMin, Date fechaSincronizacionMax, String modeloFilter, String nombreFilter, String progresoFilter, 
			@RequestParam(value="state",defaultValue = "A") String state, 
			@RequestParam(value="page",defaultValue = "0") int page,
			@RequestParam(value="size",defaultValue = "30") int size) throws ServerException {
		var filter = new MassiveItemFilter(state, page, size);
		filter.setCarga(cargaFilter); 
		filter.setDocumento(documentoFilter); 
		filter.setFechaSerializacionMax(fechaSerializacionMax);
		filter.setFechaSerializacionMin(fechaSerializacionMin); 
		filter.setFechaSincronizacionMax(fechaSincronizacionMax);
		filter.setFechaSincronizacionMin(fechaSincronizacionMin); 
		filter.setModelo(modeloFilter); 
		filter.setNombre(nombreFilter); 
		filter.setProgreso(progresoFilter); 
		// return cargaMasivaItemService.find(token, filter);
		return null;
	}

	@GetMapping("/get")
	public MasivaItemRequest get(@RequestHeader(name = "Authorization") String token, String cargaFilter, String documentoFilter, Date fechaSerializacionMin, Date fechaSerializacionMax, Date fechaSincronizacionMin, Date fechaSincronizacionMax, String modeloFilter, String nombreFilter, String progresoFilter, 
			@RequestParam(defaultValue = "A") String state) throws ServerException {
		var filter = new MassiveItemFilter();
		filter.setState(state);
		filter.setCarga(cargaFilter); 
		filter.setDocumento(documentoFilter); 
		filter.setFechaSerializacionMax(fechaSerializacionMax);
		filter.setFechaSerializacionMin(fechaSerializacionMin); 
		filter.setFechaSincronizacionMax(fechaSincronizacionMax);
		filter.setFechaSincronizacionMin(fechaSincronizacionMin); 
		filter.setModelo(modeloFilter); 
		filter.setNombre(nombreFilter); 
		filter.setProgreso(progresoFilter); 
		return null;
		//return cargaMasivaItemService.get(token, filter);
	}

	@GetMapping("/{id}")
	public MasivaItemRequest findById(@RequestHeader(name = "Authorization") String token
			, @PathVariable String id) throws ServerException {
		return null;
		// return cargaMasivaItemService.findById(id);
	}

	@PostMapping("")
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public SharedIdResponse save(@RequestHeader(name = "Authorization") String token
			, @RequestBody MasivaItemRequest body) throws ServerException {
		return null;
		// return new SharedIdResponse(cargaMasivaItemService.save(token, body).getCargaMasivaItemId());
	}

	@PutMapping("/{id}")
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public SharedIdResponse update(@RequestHeader(name = "Authorization") String token
			, @RequestBody MasivaItemRequest body, @PathVariable String id) throws ServerException {
		// return new SharedIdResponse(cargaMasivaItemService.update(token, body, id).getCargaMasivaItemId());
		return null;
	}
	
	@PutMapping("/activate/{id}")
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public void activate(@RequestHeader(name = "Authorization") String token
		, @PathVariable String id) throws ServerException {
		cargaMasivaItemService.restore(id, token);
	}

	@PutMapping("/inactivate/{id}")
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public void inactivate(@RequestHeader(name = "Authorization") String token
		, @PathVariable String id) throws ServerException {
		cargaMasivaItemService.delete(id, token);
	}

}
