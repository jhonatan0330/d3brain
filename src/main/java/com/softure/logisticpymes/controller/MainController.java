package com.softure.logisticpymes.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softure.authentication.application.OrganizacionSvc;
import com.softure.authentication.application.UsuarioAutenticacionSvc;
import com.softure.authentication.application.UsuarioOrganizacionSvc;
import com.softure.authentication.application.UsuarioSesionSvc;
import com.softure.authentication.domain.OrganizacionDTO;
import com.softure.authentication.domain.UsuarioAutenticacionDTO;
import com.softure.authentication.domain.UsuarioAutenticacionFilterDTO;
import com.softure.authentication.domain.UsuarioOrganizacionDTO;
import com.softure.authentication.domain.UsuarioSesionDTO;
import com.softure.document_execution.application.CallDocumentListWithFilters;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaFilterDTO;
import com.softure.java.dto.exception.ServerException;
import com.softure.java.services.HttpUtils;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.process_form.domain.DocumentoPlantillaFilterDTO;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/main")
public class MainController {

	@Autowired private DocumentoPlantillaSvc plantillaService;
	@Autowired private UsuarioAutenticacionSvc usuarioAutenticacionService;
	@Autowired private UsuarioSesionSvc usuarioSessionService;
	@Autowired private CallDocumentListWithFilters listDocumentWithFiltersFunction;
	@Autowired private OrganizacionSvc organizacionService;
	@Autowired private UsuarioOrganizacionSvc organizacionUsuarioService;
	
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
	public UsuarioAutenticacionDTO autenticarUsuarioAutenticacion(HttpServletRequest request, @RequestBody UsuarioAutenticacionFilterDTO filter) throws ServerException {
		filter.setIp(HttpUtils.getRequestIP(request));
		return usuarioAutenticacionService.autenticar(filter, (filter.getClaveAnterior()==null));
	}
	
	@RequestMapping(value="/cambiarClave", method=RequestMethod.POST)
	public UsuarioAutenticacionDTO cambiarClave(HttpServletRequest request, @RequestHeader(name="Authorization", required = false) String token, @RequestBody UsuarioAutenticacionDTO filter) throws ServerException {
		filter.setIp(HttpUtils.getRequestIP(request));
		return usuarioAutenticacionService.cambiarClave(filter, token);
	}
	
	@RequestMapping(value="/solicitarNuevaClave", method=RequestMethod.POST)
	public void solicitarNuevaClave(HttpServletRequest request, @RequestBody UsuarioAutenticacionDTO filter) throws ServerException {
		filter.setIp(HttpUtils.getRequestIP(request));
		usuarioAutenticacionService.solicitarNuevaClave(filter);
	}
	
	@RequestMapping(value="/cambiarClaveOtherSystem", method=RequestMethod.POST)
	public UsuarioOrganizacionDTO cambiarClaveOtherSystem(@RequestHeader("Authorization") String token, @RequestBody UsuarioOrganizacionDTO dto) throws ServerException {
		return organizacionUsuarioService.reloadPassword(dto, token);
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
		return listDocumentWithFiltersFunction.listarUsuario(dto);
	}
	
	@RequestMapping(value="/getAdministratorTemplates", method=RequestMethod.GET)
	public List<DocumentoPlantillaDTO> consultaAdministrador(@RequestHeader("Authorization") String token)  throws ServerException  {
		DocumentoPlantillaFilterDTO filter = new DocumentoPlantillaFilterDTO();
		filter.setSecurityToken(token);
		return plantillaService.consultaAdministrador(filter);	
	}
}
