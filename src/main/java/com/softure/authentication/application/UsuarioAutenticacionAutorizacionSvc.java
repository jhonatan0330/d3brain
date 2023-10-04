package com.softure.authentication.application;

import java.util.List;

// BEGIN region interImport
import java.util.Date;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.authentication.domain.UsuarioAutenticacionAutorizacionDTO;
import com.softure.authentication.domain.UsuarioAutenticacionAutorizacionFilterDTO;
import com.softure.authentication.infrastructure.UsuarioAutenticacionAutorizacionMapper;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.mail.application.MailRecoverPasswordService;

@Service("usuarioAutenticacionAutorizacionService")
public class UsuarioAutenticacionAutorizacionSvc extends BasicSvc<UsuarioAutenticacionAutorizacionDTO, UsuarioAutenticacionAutorizacionFilterDTO> {
	
	@Autowired
	private UsuarioAutenticacionAutorizacionMapper usuarioAutenticacionAutorizacionMapper;
	
	// BEGIN region servicesUsuarioAutenticacionAutorizacion
	@Autowired private MailRecoverPasswordService mailRecoverPasswordService;
	// END region servicesUsuarioAutenticacionAutorizacion

	@Override
	public UsuarioAutenticacionAutorizacionDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. UsuarioAutenticacionAutorizacion");
		UsuarioAutenticacionAutorizacionFilterDTO dto = new UsuarioAutenticacionAutorizacionFilterDTO();
		dto.setLlaveTabla(llave);
		return usuarioAutenticacionAutorizacionMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = usuarioAutenticacionAutorizacionMapper;
	}
	
	@Override
	public UsuarioAutenticacionAutorizacionDTO activar(UsuarioAutenticacionAutorizacionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioAutenticacionAutorizacion_activar
		return super.activar(dto, token);
		// END UsuarioAutenticacionAutorizacion_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioAutenticacionAutorizacionDTO actualizar( UsuarioAutenticacionAutorizacionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioAutenticacionAutorizacion_actualizar
		return super.actualizar(dto, token);
		// END UsuarioAutenticacionAutorizacion_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioAutenticacionAutorizacionDTO inactivar(UsuarioAutenticacionAutorizacionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioAutenticacionAutorizacion_inactivar
		return super.inactivar(dto, token);
		// END UsuarioAutenticacionAutorizacion_inactivar
	}
	
	@Override
	public UsuarioAutenticacionAutorizacionDTO consultaUnica(UsuarioAutenticacionAutorizacionFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(UsuarioAutenticacionAutorizacionFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<UsuarioAutenticacionAutorizacionDTO> listarConsulta(UsuarioAutenticacionAutorizacionFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioAutenticacionAutorizacionDTO guardar(UsuarioAutenticacionAutorizacionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioAutenticacionAutorizacion_guardar
		return super.guardar(dto, token);
		// END UsuarioAutenticacionAutorizacion_guardar
	}

// BEGIN region aditionalMethods
	public void generarAutorizacion(String usuario, String correo, String ip) throws ServerException {
		UsuarioAutenticacionAutorizacionDTO dto = new UsuarioAutenticacionAutorizacionDTO();
		dto.setCorreo(correo);
		dto.setUsuario(usuario);
		dto.setIpSolicitud(ip);
		dto.setFechaSolicitud(new Date());
		dto.setFechaMaxima(new Date(dto.getFechaSolicitud().getTime()+15*60*1000));
		dto.setCodigo(String.valueOf(Double.valueOf(Math.random()*1000000).intValue()));
		dto = save(dto);
		mailRecoverPasswordService.call(correo, dto.getLlaveTabla(), dto.getCodigo() );	
	}
	
	public UsuarioAutenticacionAutorizacionDTO validar(String llave, String code, String newKey, String ip) throws ServerException {
		UsuarioAutenticacionAutorizacionDTO dto = consultaXId(llave);
		if(dto == null) throw new ServerException("Token de validacion incorrecto"); 
		if(dto.getCodigo()!=null & dto.getCodigo().compareTo(code)!=0)
			throw new ServerException("Codigo de seguridad incorrecto");
		if(dto.getFechaMaxima().compareTo(new Date())<0)
			throw new ServerException("Se ha vencido el token, por favor genera un nuevo token");
		dto.setIpRedencion(ip);
		dto.setFechaRedencion(new Date());
		dto.setKey(newKey);
		return update(dto);
	}
	
// END region aditionalMethods

}