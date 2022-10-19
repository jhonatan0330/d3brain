package com.softure.massiveload.infrastructure.rest.controller;

import com.softure.java.domain.IdResponse;
import com.softure.java.dto.exception.ServerException;
import com.softure.massiveload.application.service.ICargaMasivaService;
import com.softure.massiveload.domain.vo.CargaMasiva;
import com.softure.massiveload.domain.filter.CargaMasivaFilterDTO;

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
@RequestMapping("massiveload/cargaMasiva")
public class CargaMasivaController {

	@Autowired
	private ICargaMasivaService cargaMasivaService;

	@GetMapping("")
	public List<CargaMasiva> find(@RequestHeader(name = "Authorization") String token, String archivoFilter, Date fechaMin, Date fechaMax, String mensajeFilter, String plantillaFilter, String progresoFilter, String usuarioFilter, 
			@RequestParam(defaultValue = "A") String state, 
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "30") int size) throws ServerException {
		var filter = new CargaMasivaFilterDTO(state, page, size);
		filter.setArchivo(archivoFilter); 
		filter.setFechaMax(fechaMax);
		filter.setFechaMin(fechaMin); 
		filter.setMensaje(mensajeFilter); 
		filter.setPlantilla(plantillaFilter); 
		filter.setProgreso(progresoFilter); 
		filter.setUsuario(usuarioFilter); 
		return cargaMasivaService.find(token, filter);
	}

	@GetMapping("/get")
	public CargaMasiva get(@RequestHeader(name = "Authorization") String token, String archivoFilter, Date fechaMin, Date fechaMax, String mensajeFilter, String plantillaFilter, String progresoFilter, String usuarioFilter, 
			@RequestParam(defaultValue = "A") String state) throws ServerException {
		var filter = new CargaMasivaFilterDTO();
		filter.setEstado(state);
		filter.setArchivo(archivoFilter); 
		filter.setFechaMax(fechaMax);
		filter.setFechaMin(fechaMin); 
		filter.setMensaje(mensajeFilter); 
		filter.setPlantilla(plantillaFilter); 
		filter.setProgreso(progresoFilter); 
		filter.setUsuario(usuarioFilter); 
		return cargaMasivaService.get(token, filter);
	}

	@GetMapping("/{id}")
	public CargaMasiva findById(@RequestHeader(name = "Authorization") String token
			, @PathVariable("id") String id) throws ServerException {
		return cargaMasivaService.findById(id);
	}

	@PostMapping("")
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public IdResponse save(@RequestHeader(name = "Authorization") String token
			, @RequestBody CargaMasiva body) throws ServerException {
		return new IdResponse(cargaMasivaService.save(token, body).getCargaMasivaId());
	}

	@PutMapping("/{id}")
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public IdResponse update(@RequestHeader(name = "Authorization") String token
			, @RequestBody CargaMasiva body, @PathVariable("id") String id) throws ServerException {
		return new IdResponse(cargaMasivaService.update(token, body, id).getCargaMasivaId());
	}
	
	@PutMapping("/activate/{id}")
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public void activate(@RequestHeader(name = "Authorization") String token
		, @PathVariable("id") String id) throws ServerException {
		cargaMasivaService.activate(token, id);
	}

	@PutMapping("/inactivate/{id}")
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public void inactivate(@RequestHeader(name = "Authorization") String token
		, @PathVariable("id") String id) throws ServerException {
		cargaMasivaService.inactivate(token, id);
	}

}
