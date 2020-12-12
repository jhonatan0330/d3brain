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
import com.softure.logisticpymes.dto.OrganizacionDTO;
import com.softure.logisticpymes.dto.PedidoVentaDTO;
import com.softure.logisticpymes.dto.UsuarioAutenticacionDTO;
import com.softure.logisticpymes.dto.filter.DocumentoPlantillaFilterDTO;
import com.softure.logisticpymes.dto.filter.OrganizacionFilterDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaFilterDTO;
import com.softure.logisticpymes.dto.filter.UsuarioAutenticacionFilterDTO;
import com.softure.logisticpymes.services.DocumentoPlantillaSvc;
import com.softure.logisticpymes.services.OrganizacionSvc;
import com.softure.logisticpymes.services.PedidoVentaSvc;
import com.softure.logisticpymes.services.UsuarioAutenticacionSvc;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/main")
public class MainController {

	@Autowired private DocumentoPlantillaSvc plantillaService;
	@Autowired private UsuarioAutenticacionSvc usuarioAutenticacionService;
	@Autowired private PedidoVentaSvc pedidoVentaService;
	@Autowired private OrganizacionSvc organizacionService;
	
	@RequestMapping(value="/test", method=RequestMethod.GET)
	public String test() {
		return "OK";
	}
	
	@RequestMapping(value="/obtenerPrincipalOrganizacion", method=RequestMethod.POST)
	public OrganizacionDTO obtenerPrincipalOrganizacion(@RequestBody OrganizacionFilterDTO filter) throws ServerException {
		return organizacionService.obtenerPrincipal(filter);
	}
	
	@RequestMapping(value="/autenticarUsuarioAutenticacion", method=RequestMethod.POST)
	public UsuarioAutenticacionDTO autenticarUsuarioAutenticacion(@RequestBody UsuarioAutenticacionFilterDTO filter) throws ServerException {
		return usuarioAutenticacionService.autenticar(filter);
	}
	
	@RequestMapping(value="/consultaUsuarioDocumentoPlantilla", method=RequestMethod.POST)
	public List<DocumentoPlantillaDTO> consultaUsuarioDocumentoPlantilla(@RequestBody DocumentoPlantillaFilterDTO filter)  throws ServerException  {
		return plantillaService.consultaUsuario(filter);	
	}
	
	@RequestMapping(value="/listarUsuarioPedidoVenta", method=RequestMethod.POST)
	public List<PedidoVentaDTO> listarUsuarioPedidoVenta(@RequestBody PedidoVentaFilterDTO dto)  throws ServerException  {
		return pedidoVentaService.listarUsuario(dto);
	}
}
