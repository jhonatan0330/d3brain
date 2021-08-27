package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.softure.java.cons.ConstantesGenerales;
import com.softure.logisticpymes.dto.OrganizacionDTO;
import com.softure.logisticpymes.dto.UsuarioDTO;
import com.softure.logisticpymes.dto.UsuarioSesionDTO;
import com.softure.logisticpymes.dto.filter.ModuloContratadoFilterDTO;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.UsuarioAutenticacionDTO;
import com.softure.logisticpymes.dto.filter.UsuarioAutenticacionFilterDTO;
import com.softure.logisticpymes.persistence.UsuarioAutenticacionMapper;

@Service("usuarioAutenticacionService")
public class UsuarioAutenticacionSvc extends BasicSvc<UsuarioAutenticacionDTO, UsuarioAutenticacionFilterDTO> {
	
	@Autowired
	private UsuarioAutenticacionMapper usuarioAutenticacionMapper;
	
	// BEGIN region servicesUsuarioAutenticacion
	@Autowired private ModuloContratadoSvc modulosService;
	@Autowired private OrganizacionSvc organizacionService;
	@Autowired private UsuarioSesionSvc usuarioSesionService;
	@Autowired private UsuarioSvc usuarioService;
	// END region servicesUsuarioAutenticacion

	@Override
	public UsuarioAutenticacionDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. UsuarioAutenticacion");
		UsuarioAutenticacionFilterDTO dto = new UsuarioAutenticacionFilterDTO();
		dto.setLlaveTabla(llave);
		return usuarioAutenticacionMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = usuarioAutenticacionMapper;
	}
	
	@Override
	public UsuarioAutenticacionDTO activar(UsuarioAutenticacionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioAutenticacion_activar
		return super.activar(dto, token);
		// END UsuarioAutenticacion_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioAutenticacionDTO actualizar( UsuarioAutenticacionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioAutenticacion_actualizar
		return super.actualizar(dto, token);
		// END UsuarioAutenticacion_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioAutenticacionDTO inactivar(UsuarioAutenticacionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioAutenticacion_inactivar
		return super.inactivar(dto, token);
		// END UsuarioAutenticacion_inactivar
	}
	
	@Override
	public UsuarioAutenticacionDTO consultaUnica(UsuarioAutenticacionFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(UsuarioAutenticacionFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<UsuarioAutenticacionDTO> listarConsulta(UsuarioAutenticacionFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	public UsuarioAutenticacionDTO autenticar(UsuarioAutenticacionFilterDTO dto)throws ServerException{
		// BEGIN region autenticar
		if(dto.getClaveAnterior()==null) throw new ServerException("Por favor actualice su version de software");
		String fechaMinima = null;
		try {
			fechaMinima = usuarioAutenticacionMapper.fechaMinima();
		}catch (Exception e) {
			throw new ServerException(e.getMessage());
		}
		if(fechaMinima!=null){
			int cliente = Integer.parseInt(dto.getClaveAnterior().replace(".", ""));
			int servidor = Integer.parseInt(fechaMinima.replace(".", ""));
			if(cliente < servidor) throw new ServerException("Por favor actualice su version de software (Limpie cache o descargue una nueva app).\nCliente: " + String.valueOf(cliente) + "\nServidor:" + String.valueOf(servidor));
		}
		String fechaTrial = usuarioAutenticacionMapper.consultarValidez();
		if(fechaTrial==null) throw new ServerException("El sistema no tiene configurada la fecha de la licencia");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		long diasVigencia = 0;
		try {
			Date date = formatter.parse(fechaTrial);
			diasVigencia =(date.getTime() - new Date().getTime())/ (24 * 3600000);
			if(diasVigencia < 0)
				throw new ServerException("Se ha vencido la licencia del sistema. " + fechaTrial);
		} catch (ParseException e) {
			throw new  ServerException("El formato de la fecha de licencia esta incorrecto");
		}
		UsuarioAutenticacionDTO autenticacion =null;
		UsuarioSesionDTO sesion  = null;
		if(dto.getSecurityToken()!=null && dto.getClave()==null){
			sesion = usuarioSesionService.consultaXId(dto.getSecurityToken());
			if(sesion == null) throw new ServerException("Autenticacion incorrecta");
			if(sesion.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0)
				throw new ServerException("Se encuentra inactiva la sesion");
			autenticacion = new UsuarioAutenticacionDTO();
			autenticacion.setUsuario(sesion.getUsuario());
		}else{
			if(dto.getClave()!=null && dto.getSesion()==null && dto.getSecurityToken()!=null) {
				autenticacion = consultaXId(dto.getClave());
			}
			if(autenticacion== null) {
				UsuarioAutenticacionFilterDTO autenticacionF = new UsuarioAutenticacionFilterDTO();
				if(dto.getSesion()==null) throw new ServerException("La sesion no puede estar vacia");
				autenticacionF.setSesion(dto.getSesion());
				if(dto.getClave()==null) throw new ServerException("La clave no puede estar vacia");
				autenticacionF.setClave(dto.getClave());
				autenticacion = consultaUnica(autenticacionF);
			}
			if(autenticacion == null) throw new ServerException("Autenticacion incorrecta");
			if(autenticacion.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0) throw new ServerException("Autenticacion no se enecuentra activa");			
		}
		
		UsuarioDTO usuario = usuarioService.consultaXId(autenticacion.getUsuario());
		if(usuario.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0)throw new ServerException("El usuario no se encuentra activo");
		autenticacion.setUsuarioDTO(usuario);
		
		autenticacion.setOrganizacion(organizacionService.obtenerPrincipalPropiedades(usuario.getLlaveTabla()));
		autenticacion.setOrganizaciones(organizacionService.obtenerUsuario(autenticacion.getUsuario()));
		
		if(sesion==null) {
			sesion = new UsuarioSesionDTO();
			sesion.setFecha(new Date());
			sesion.setFechaCierre(usuarioSesionService.getFechaCierre(autenticacion.getUsuario()));
			sesion.setUsuario(autenticacion.getUsuario());
			sesion = usuarioSesionService.guardar(sesion, dto.getSecurityToken());
		}
		autenticacion.setToken(sesion.getLlaveTabla());
		if(diasVigencia < 14 && usuarioAutenticacionMapper.ocultarLicencia(autenticacion.getUsuario())==0) autenticacion.setMensaje("Quedan "+ (diasVigencia +1) +" dias para que se cumpla el periodo de su licencia");
		autenticacion.setTableroControl(usuarioAutenticacionMapper.cantidadAsignaciones(autenticacion.getUsuario()));
		
		ModuloContratadoFilterDTO filterMod = new ModuloContratadoFilterDTO();
		filterMod.setSecurityToken(sesion.getLlaveTabla());
		autenticacion.setModulos(modulosService.modulosUsuario(filterMod));
		
		return autenticacion;
		// END region autenticar
	}
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioAutenticacionDTO cambiarClave(UsuarioAutenticacionDTO dto, String token)throws ServerException{
		// BEGIN region cambiarClave
		
		UsuarioAutenticacionDTO user = null;
		// Se envia por el administrador
		if(dto.getClaveAnterior().compareTo("ADMIN$123")==0){
			if(dto.getLlaveTabla()!=null){
				user = consultaXId(dto.getLlaveTabla());
				if(user== null) throw new ServerException("El usuario no tiene una autenticacion");
				if(user.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0) throw new ServerException("Por favor consulte con su administrador, sus credenciales se encuentran inactivas");
				if(user.getClave().compareTo(dto.getClaveAnterior())!=0) throw new ServerException("No concuerda la clave anterior");
			}
		} else {
			UsuarioAutenticacionFilterDTO filtro = new UsuarioAutenticacionFilterDTO();
			filtro.setUsuario(getUserFlex(token));
			filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			user = consultaUnica(filtro);
			if(user.getClave().compareTo(dto.getClaveAnterior())!=0) throw new ServerException("No concuerda la clave anterior");
		}
		
		if(user== null){
			user = new UsuarioAutenticacionDTO();
			user.setUsuario(dto.getUsuario());
			user.setClave(dto.getClave());
			user.setSesion(dto.getSesion());
			user = guardar(user, token);
		}else{
			if(dto.getSesion()!=null) user.setSesion(dto.getSesion());
			user.setClave(dto.getClave());
			user = actualizar(user, token);
		}
		return user;
		// END region cambiarClave
	}

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioAutenticacionDTO guardar(UsuarioAutenticacionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioAutenticacion_guardar
		return super.guardar(dto, token);
		// END UsuarioAutenticacion_guardar
	}

// BEGIN region aditionalMethods
	public void crearAutenticacion(String usuario, String token) throws ServerException{
		UsuarioAutenticacionFilterDTO filtro1 = new UsuarioAutenticacionFilterDTO();
		filtro1.setUsuario(usuario);
		if(consultaUnica(filtro1) == null) {
			UsuarioDTO user = usuarioService.consultaXId(usuario);
			UsuarioAutenticacionDTO aut = new UsuarioAutenticacionDTO();
			aut.setUsuario(usuario);
			aut.setClave(user.getIdentificacion());
			aut.setSesion(user.getIdentificacion());
			guardar(aut, token);
		}
	}
	
	public UsuarioDTO getUserSystem() throws ServerException{
		OrganizacionDTO principal = organizacionService.obtenerPrincipal(null);
		if(principal==null ) throw new ServerException("No se tiene organizacion principal");
		if(principal.getUsuarioSystem()==null) throw new ServerException("No se tiene configurado el usuario sistem en la organizacion principal");
		UsuarioDTO usuarioSystem = usuarioService.consultaXId(principal.getUsuarioSystem());
		if(usuarioSystem==null || usuarioSystem.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0) throw new ServerException("El usuario sistema no exisste o esta inactivo");
		return usuarioSystem;
	}
	
	public UsuarioSesionDTO generateAdministratorToken() throws ServerException{
		UsuarioDTO usuarioSystem = getUserSystem();
		UsuarioSesionDTO sesion = new UsuarioSesionDTO();
		sesion.setFecha(new Date());
		//sesion.setFechaCierre(usuarioSesionService.getFechaCierre(usuarioSystem.getLlaveTabla()));
		sesion.setUsuario(usuarioSystem.getLlaveTabla());
		sesion = usuarioSesionService.guardar(sesion, null);
		return sesion;
	}
	
	public void logout(String token)throws ServerException{
		UsuarioSesionDTO sesion = usuarioSesionService.consultaXId(token);
		if(sesion == null) throw new ServerException("Token incorrecto");
		if(sesion.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)==0) throw new ServerException("Se encuentra inactiva la sesion");
		usuarioSesionService.inactivar(sesion, null);
	}
	
	public String getFechaActualizacion() {
		return usuarioAutenticacionMapper.versionActual();
	}
	
// END region aditionalMethods

}