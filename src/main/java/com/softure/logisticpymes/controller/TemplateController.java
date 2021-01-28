package com.softure.logisticpymes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.DocumentoPlantillaDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaCaracteristicaFilterDTO;
import com.softure.logisticpymes.services.DocumentoPlantillaSvc;
import com.softure.logisticpymes.services.adapter.CampoAdaptador;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/template")
public class TemplateController {

	@Autowired private CampoAdaptador adaptador;
	@Autowired private DocumentoPlantillaSvc documentoplantillaService;
	
	@RequestMapping(value="/getFields", method=RequestMethod.GET)
	public DocumentoPlantillaDTO obtenerCampos(@RequestParam String id, @RequestHeader("Authorization") String token) throws ServerException {
		DocumentoPlantillaDTO filterTemplate = new DocumentoPlantillaDTO();
		filterTemplate.setLlaveTabla(id);
		return documentoplantillaService.obtenerCampos(filterTemplate, token);
	}
	
	@RequestMapping(value="/getFieldData", method=RequestMethod.POST)
	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(@RequestBody PedidoVentaCaracteristicaFilterDTO filterField, @RequestHeader("Authorization") String token)  throws ServerException  {
		return adaptador.consultarDatosBase(filterField);
	}
	
}
