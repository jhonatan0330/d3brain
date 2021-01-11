package com.softure.logisticpymes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.DocumentoPlantillaDTO;
import com.softure.logisticpymes.dto.filter.DocumentoPlantillaFilterDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaCaracteristicaFilterDTO;
import com.softure.logisticpymes.services.DocumentoPlantillaSvc;
import com.softure.logisticpymes.services.adapter.CampoAdaptador;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/template")
public class TemplateController {

	@Autowired private CampoAdaptador adaptador;
	@Autowired private DocumentoPlantillaSvc documentoplantillaService;
	
	@RequestMapping(value="/getFieldData", method=RequestMethod.POST)
	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(@RequestBody PedidoVentaCaracteristicaFilterDTO filterField)  throws ServerException  {
		return adaptador.consultarDatosBase(filterField);
	}
	
	@RequestMapping(value="/getTemplates", method=RequestMethod.POST)
	public List<DocumentoPlantillaDTO> listarPlantillas(@RequestBody DocumentoPlantillaFilterDTO filterTemplate) throws ServerException {
		return documentoplantillaService.consultaUsuario(filterTemplate);
	}
	
	@RequestMapping(value="/getUniqueTemplates", method=RequestMethod.POST)
	public DocumentoPlantillaDTO getUniqueTemplates(@RequestBody DocumentoPlantillaFilterDTO filterTemplate) throws ServerException {
		return documentoplantillaService.consultaUnica(filterTemplate);
	}
	
	@RequestMapping(value="/getTemplateWithFields", method=RequestMethod.POST)
	public DocumentoPlantillaDTO obtenerCampos(@RequestBody DocumentoPlantillaDTO filterTemplate, @RequestBody String token) throws ServerException {
		return documentoplantillaService.obtenerCampos(filterTemplate, token);
	}
	
}
