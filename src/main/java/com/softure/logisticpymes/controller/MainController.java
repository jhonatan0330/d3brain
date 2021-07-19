package com.softure.logisticpymes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.DocumentoPlantillaDTO;
import com.softure.logisticpymes.dto.OrganizacionDTO;
import com.softure.logisticpymes.dto.PedidoVentaDTO;
import com.softure.logisticpymes.dto.UsuarioAutenticacionDTO;
import com.softure.logisticpymes.dto.UsuarioSesionDTO;
import com.softure.logisticpymes.dto.filter.DocumentoPlantillaFilterDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaFilterDTO;
import com.softure.logisticpymes.dto.filter.UsuarioAutenticacionFilterDTO;
import com.softure.logisticpymes.services.DocumentoPlantillaSvc;
import com.softure.logisticpymes.services.OrganizacionSvc;
import com.softure.logisticpymes.services.PedidoVentaSvc;
import com.softure.logisticpymes.services.UsuarioAutenticacionSvc;
import com.softure.logisticpymes.services.UsuarioSesionSvc;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/main")
public class MainController {

	@Autowired private DocumentoPlantillaSvc plantillaService;
	@Autowired private UsuarioAutenticacionSvc usuarioAutenticacionService;
	@Autowired private UsuarioSesionSvc usuarioSessionService;
	@Autowired private PedidoVentaSvc pedidoVentaService;
	@Autowired private OrganizacionSvc organizacionService;
	
	@RequestMapping(value="/test", method=RequestMethod.GET)
	public String test() {
		return "OK";
	}
	
	@RequestMapping(value="/obtenerPrincipalOrganizacion", method=RequestMethod.GET)
	public OrganizacionDTO obtenerPrincipalOrganizacion() throws ServerException {
		// Este metodo se usa para obtener los datos de la organizacion pero despues se vuelve a utilizar para obtener las propiedades
		return organizacionService.obtenerPrincipal(null);
	}
	
	@RequestMapping(value="/autenticarUsuarioAutenticacion", method=RequestMethod.POST)
	public UsuarioAutenticacionDTO autenticarUsuarioAutenticacion(@RequestBody UsuarioAutenticacionFilterDTO filter) throws ServerException {
		return usuarioAutenticacionService.autenticar(filter);
	}
	
	@RequestMapping(value="/cambiarClave", method=RequestMethod.POST)
	public UsuarioAutenticacionDTO cambiarClave(@RequestHeader("Authorization") String token, @RequestBody UsuarioAutenticacionDTO filter) throws ServerException {
		return usuarioAutenticacionService.cambiarClave(filter, token);
	}
	
	@RequestMapping(value="/checkToken", method=RequestMethod.GET)
	public UsuarioSesionDTO checkToken(@RequestHeader("Authorization") String token) throws ServerException {
		return usuarioSessionService.checkToken(token);
	}
	
	@RequestMapping(value="/consultaUsuarioDocumentoPlantilla", method=RequestMethod.POST)
	public List<DocumentoPlantillaDTO> consultaUsuarioDocumentoPlantilla(@RequestBody DocumentoPlantillaFilterDTO filter)  throws ServerException  {
		return plantillaService.consultaUsuario(filter);	
	}
	
	@RequestMapping(value="/listarUsuarioPedidoVenta", method=RequestMethod.POST)
	public List<PedidoVentaDTO> listarUsuarioPedidoVenta(@RequestBody PedidoVentaFilterDTO dto)  throws ServerException  {
		return pedidoVentaService.listarUsuario(dto);
	}
	
	@RequestMapping(value="/getAdministratorTemplates", method=RequestMethod.GET)
	public List<DocumentoPlantillaDTO> consultaAdministrador(@RequestHeader("Authorization") String token)  throws ServerException  {
		DocumentoPlantillaFilterDTO filter = new DocumentoPlantillaFilterDTO();
		filter.setSecurityToken(token);
		return plantillaService.consultaAdministrador(filter);	
	}
}
