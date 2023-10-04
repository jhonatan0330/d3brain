package com.softure.massiveload.infrastructure;

// Start of user code importsModel
import java.util.Date;
// End of user code
import java.util.List;

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

import com.softure.java.dto.exception.ServerException;
import com.softure.massiveload.application.MassiveCRUDMasterService;
import com.softure.massiveload.domain.MassiveMasterRequest;
import com.softure.massiveload.domain.MassiveMasterFilter;
import com.softure.shared.domain.SharedIdResponse;


@RestController
@RequestMapping("massiveload/cargaMasiva")
public class MassiveMasterController {

	@Autowired
	private MassiveCRUDMasterService cargaMasivaService;

	@GetMapping("")
	public List<MassiveMasterRequest> find(@RequestHeader(name = "Authorization") String token, String archivoFilter, Date fechaMin, Date fechaMax, String mensajeFilter, String plantillaFilter, String progresoFilter, String usuarioFilter, 
			@RequestParam(defaultValue = "A") String state, 
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "30") int size) throws ServerException {
		var filter = new MassiveMasterFilter(state, page, size);
		filter.setArchivo(archivoFilter); 
		filter.setFechaMax(fechaMax);
		filter.setFechaMin(fechaMin); 
		filter.setMensaje(mensajeFilter); 
		filter.setPlantilla(plantillaFilter); 
		filter.setProgreso(progresoFilter); 
		filter.setUsuario(usuarioFilter); 
		//return cargaMasivaService.findMany(filter);
		return null;
	}

	@GetMapping("/get")
	public MassiveMasterRequest get(@RequestHeader(name = "Authorization") String token, String archivoFilter, Date fechaMin, Date fechaMax, String mensajeFilter, String plantillaFilter, String progresoFilter, String usuarioFilter, 
			@RequestParam(defaultValue = "A") String state) throws ServerException {
		var filter = new MassiveMasterFilter();
		filter.setState(state);
		filter.setArchivo(archivoFilter); 
		filter.setFechaMax(fechaMax);
		filter.setFechaMin(fechaMin); 
		filter.setMensaje(mensajeFilter); 
		filter.setPlantilla(plantillaFilter); 
		filter.setProgreso(progresoFilter); 
		filter.setUsuario(usuarioFilter); 
		//return cargaMasivaService.findOne(filter);
		return null;
	}

	@GetMapping("/{id}")
	public MassiveMasterRequest findById(@RequestHeader(name = "Authorization") String token
			, @PathVariable("id") String id) throws ServerException {
		// return cargaMasivaService.findById(id);
		return null;
	}

	@PostMapping("")
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public SharedIdResponse save(@RequestHeader(name = "Authorization") String token
			, @RequestBody MassiveMasterRequest body) throws ServerException {
		//return new SharedIdResponse(cargaMasivaService.save(body).getCargaMasivaId());
		return null;
	}

	@PutMapping("/{id}")
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public SharedIdResponse update(@RequestHeader(name = "Authorization") String token
			, @RequestBody MassiveMasterRequest body, @PathVariable("id") String id) throws ServerException {
		// return new SharedIdResponse(cargaMasivaService.update(token, body, id).getCargaMasivaId());
		return null;
	}
	
	@PutMapping("/activate/{id}")
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public void activate(@RequestHeader(name = "Authorization") String token
		, @PathVariable("id") String id) throws ServerException {
		cargaMasivaService.restore(id, token);
	}

	@PutMapping("/inactivate/{id}")
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public void inactivate(@RequestHeader(name = "Authorization") String token
		, @PathVariable("id") String id) throws ServerException {
		cargaMasivaService.delete(id, token);
	}

}
