package com.softure.authentication.application;

import java.util.List;

// BEGIN region interImport
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.authentication.domain.OrganizacionDTO;
import com.softure.authentication.domain.UsuarioAutenticacionDTO;
import com.softure.authentication.domain.UsuarioAutenticacionFilterDTO;
import com.softure.authentication.domain.UsuarioOrganizacionDTO;
import com.softure.authentication.domain.UsuarioOrganizacionFilterDTO;
import com.softure.authentication.infrastructure.UsuarioOrganizacionMapper;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.logisticpymes.application.UsuarioSvc;
import com.softure.logisticpymes.domain.UsuarioDTO;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("usuarioOrganizacionService")
public class UsuarioOrganizacionSvc extends BasicSvc<UsuarioOrganizacionDTO, UsuarioOrganizacionFilterDTO> {
	
	@Autowired
	private UsuarioOrganizacionMapper usuarioOrganizacionMapper;
	
	// BEGIN region servicesUsuarioOrganizacion
	@Autowired private OrganizacionSvc organizacionService;
	@Autowired private UsuarioSvc usuarioService;
	@Autowired private UsuarioAutenticacionSvc autenticacionService;
	// END region servicesUsuarioOrganizacion

	@Override
	public UsuarioOrganizacionDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. UsuarioOrganizacion");
		UsuarioOrganizacionFilterDTO dto = new UsuarioOrganizacionFilterDTO();
		dto.setLlaveTabla(llave);
		return usuarioOrganizacionMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = usuarioOrganizacionMapper;
	}
	
	@Override
	public UsuarioOrganizacionDTO activar(UsuarioOrganizacionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioOrganizacion_activar
		validateNotMainOrganization(dto.getOrganizacion());
		return super.activar(dto, token);
		// END UsuarioOrganizacion_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioOrganizacionDTO actualizar( UsuarioOrganizacionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioOrganizacion_actualizar
		validateNotMainOrganization(dto.getOrganizacion());
		return super.actualizar(dto, token);
		// END UsuarioOrganizacion_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioOrganizacionDTO inactivar(UsuarioOrganizacionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioOrganizacion_inactivar
		return super.inactivar(dto, token);
		// END UsuarioOrganizacion_inactivar
	}
	
	@Override
	public UsuarioOrganizacionDTO consultaUnica(UsuarioOrganizacionFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(UsuarioOrganizacionFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<UsuarioOrganizacionDTO> listarConsulta(UsuarioOrganizacionFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public List<UsuarioOrganizacionDTO> sincronizarUsuarios(UsuarioOrganizacionDTO dto, String token)throws ServerException{
		// BEGIN region sincronizarUsuarios
		OrganizacionDTO organizacion = organizacionService.consultaXId(dto.getOrganizacion());
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<UsuarioOrganizacionDTO[]> response =
				  restTemplate.getForEntity(
						  organizacion.getServidorUrl() + "/externos/connection/sincronizarUsuarios",
						  UsuarioOrganizacionDTO[].class);
		UsuarioOrganizacionDTO[] employees = response.getBody();
		if(employees==null) return null;
		for (UsuarioOrganizacionDTO iterador : employees) {
			UsuarioAutenticacionFilterDTO autenticacionLocalFilter = new UsuarioAutenticacionFilterDTO();
			autenticacionLocalFilter.setSesion(iterador.getTokenServer());
			autenticacionLocalFilter.setEstado(SharedConstants.STATE_ACTIVE);
			UsuarioAutenticacionDTO autenticacionLocal = autenticacionService.consultaUnica(autenticacionLocalFilter);
			if(autenticacionLocal==null) {
				UsuarioDTO usuarioNuevo = new UsuarioDTO();
				usuarioNuevo.setIdentificacion(iterador.getOrganizacion());
				usuarioNuevo.setNombre(iterador.getUsuario());
				usuarioNuevo = usuarioService.guardar(usuarioNuevo, token);
				autenticacionService.crearAutenticacion(usuarioNuevo.getLlaveTabla(), token);
			}else {
				UsuarioOrganizacionFilterDTO actualFilter = new UsuarioOrganizacionFilterDTO();
				actualFilter.setOrganizacion(organizacion.getLlaveTabla());
				actualFilter.setUsuario(autenticacionLocal.getUsuario());
				actualFilter.setEstado(SharedConstants.STATE_ACTIVE);
				UsuarioOrganizacionDTO actual = consultaUnica(actualFilter);
				
				if(actual ==null) {
					actual = new UsuarioOrganizacionDTO();
					actual.setOrganizacion(organizacion.getLlaveTabla());
					actual.setUsuario(autenticacionLocal.getUsuario());
					actual.setTokenServer(iterador.getLlaveTabla());
					actual = guardar(actual, token);
				}else {
					if(actual.getTokenServer().compareTo(iterador.getTokenServer())!=0) {
						actual.setTokenServer(iterador.getTokenServer());
						actualizar(actual, token);
					}
				}
			}
		}
		return null;
		// END region sincronizarUsuarios
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioOrganizacionDTO guardar(UsuarioOrganizacionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioOrganizacion_guardar
		validateNotMainOrganization(dto.getOrganizacion());
		return super.guardar(dto, token);
		// END UsuarioOrganizacion_guardar
	}

// BEGIN region aditionalMethods
	public List<UsuarioOrganizacionDTO> getHumanResource()throws ServerException{
		try {
			return usuarioOrganizacionMapper.sincronizarUsuarios(); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public UsuarioOrganizacionDTO reloadPassword(UsuarioOrganizacionDTO dto, String token)throws ServerException{
		if(dto.getTokenServer()==null) throw new ServerException("Es necesario incluir la nueva clave");
		UsuarioOrganizacionFilterDTO filter = new UsuarioOrganizacionFilterDTO();
		filter.setEstado(SharedConstants.STATE_ACTIVE);;
		filter.setOrganizacion(dto.getOrganizacion());
		filter.setUsuario(dto.getUsuario());
		UsuarioOrganizacionDTO unique =  consultaUnica(filter);
		if(unique!=null) {
			inactivar(unique, token);
		}
		dto.setLlaveTabla(null);
		return guardar(dto, token);
	}
	
	private void validateNotMainOrganization(String organizationId) throws ServerException {
		var org = organizacionService.consultaXId(organizationId);
		if(org==null) throw new ServerException("No se identifico la organizacion");
		if(org.getPrincipal()==null) throw new ServerException("No se pude ingresar usuario a la organizacion principal solo a las que dependen de ella");
	}
// END region aditionalMethods

}