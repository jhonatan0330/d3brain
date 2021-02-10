package com.softure.logisticpymes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.EncuestaDTO;
import com.softure.logisticpymes.dto.filter.EncuestaFilterDTO;
import com.softure.logisticpymes.services.EncuestaSvc;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/survey")
public class SurveyController {
	
	@Autowired private EncuestaSvc encuestaService;
	
	@RequestMapping(value="/getAvailable", method=RequestMethod.GET)
	public List<EncuestaDTO> obtenerCampos(@RequestHeader("Authorization") String token) throws ServerException {
		EncuestaFilterDTO filter = new EncuestaFilterDTO();
		filter.setSecurityToken(token);
		return encuestaService.listarDisponibles(filter);
	}
}
