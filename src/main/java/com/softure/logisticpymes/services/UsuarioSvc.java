package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import com.softure.java.cons.ConstantesGenerales;
import com.softure.logisticpymes.domain.dto.PedidoVentaDTO;
import com.softure.logisticpymes.domain.dto.UsuarioAutenticacionDTO;
import com.softure.logisticpymes.domain.dto.UsuarioDTO;
import com.softure.logisticpymes.domain.filter.ActividadFilterDTO;
import com.softure.logisticpymes.domain.filter.UsuarioAutenticacionFilterDTO;
import com.softure.logisticpymes.domain.filter.UsuarioFilterDTO;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.infrastructure.mybatis.mapper.UsuarioMapper;

@Service("usuarioService")
public class UsuarioSvc extends BasicSvc<UsuarioDTO, UsuarioFilterDTO> {
	
	@Autowired
	private UsuarioMapper usuarioMapper;
	
	// BEGIN region servicesUsuario
	@Autowired private ActividadSvc actividadSvc;
	@Autowired private PedidoVentaSvc documentoService;
	@Autowired private UsuarioAutenticacionSvc usuarioAutenticacionSvc;
	// END region servicesUsuario

	@Override
	public UsuarioDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. Usuario");
		UsuarioFilterDTO dto = new UsuarioFilterDTO();
		dto.setLlaveTabla(llave);
		return usuarioMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = usuarioMapper;
	}
	
	@Override
	public UsuarioDTO activar(UsuarioDTO dto, String token) throws ServerException {
		// BEGIN Usuario_activar
		return super.activar(dto, token);
		// END Usuario_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioDTO actualizar( UsuarioDTO dto, String token) throws ServerException {
		// BEGIN Usuario_actualizar
		UsuarioDTO bd = consultaXId(dto.getLlaveTabla());
		//Cambio la clave en caso que el rol tenga credenciales
		if(bd.getIdentificacion().compareTo(dto.getIdentificacion())!=0){
			UsuarioAutenticacionFilterDTO autenticacionFilter = new UsuarioAutenticacionFilterDTO();
			autenticacionFilter.setUsuario(dto.getLlaveTabla());
			autenticacionFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			UsuarioAutenticacionDTO autenticacion = usuarioAutenticacionSvc.consultaUnica(autenticacionFilter);
			if(autenticacion!=null){
				if(autenticacion.getClave().compareTo(autenticacion.getSesion())==0)autenticacion.setClave(dto.getIdentificacion());
				autenticacion.setSesion(dto.getIdentificacion());
				usuarioAutenticacionSvc.actualizar(autenticacion, token);
			}
		}
		if(dto.getCorreo()!=null) dto.setCorreo(dto.getCorreo().toLowerCase());
		return super.actualizar(dto, token);
		// END Usuario_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioDTO inactivar(UsuarioDTO dto, String token) throws ServerException {
		// BEGIN Usuario_inactivar
		dto = super.inactivar(dto, token);
		ActividadFilterDTO filtro = new ActividadFilterDTO();
		filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		filtro.setResponsable(dto.getLlaveTabla());
		int cont = actividadSvc.contarResultados(filtro);
		if(cont!=0) {
			List<PedidoVentaDTO> tareasActuales = documentoService.listarTareasOtroUsuario(dto.getLlaveTabla());
			String mensaje = "No se puede inactivar debido a que tiene asignaciones. " + cont ;
			for (PedidoVentaDTO iTarea : tareasActuales) {
				if(iTarea.getDescripcion()!=null) {
					mensaje = mensaje + "\n(" + iTarea.getNombre() + ") " + iTarea.getDescripcion();
				}else {
					mensaje = mensaje + "\n(" + iTarea.getNombre() + ") ";
				}
			}
			throw new ServerException(mensaje);
		}
		return dto;
		// END Usuario_inactivar
	}
	
	@Override
	public UsuarioDTO consultaUnica(UsuarioFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(UsuarioFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<UsuarioDTO> listarConsulta(UsuarioFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	public List<UsuarioDTO> listarRol(UsuarioFilterDTO dto)throws ServerException{
		// BEGIN region listarRol
		if(dto.getRol()==null) return listarConsulta(dto);
		// END region listarRol
		paginar(dto);
		try {
			return usuarioMapper.listarRol(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioDTO guardar(UsuarioDTO dto, String token) throws ServerException {
		// BEGIN Usuario_guardar
		UsuarioFilterDTO filtro  = new UsuarioFilterDTO();
		filtro.setIdentificacion(dto.getIdentificacion());
		filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		if(contarResultados(filtro)!=0) throw new ServerException("Ya existe ese ID en la BD y esta activo.\n Id : " + dto.getIdentificacion());
		if(dto.getImagen()==null) dto.setImagen(ConstantesGenerales.AVATAR);
		if(dto.getCorreo()!=null) dto.setCorreo(dto.getCorreo().toLowerCase());
		return super.guardar(dto, token);
		// END Usuario_guardar
	}

// BEGIN region aditionalMethods
	public List<UsuarioDTO> getUsersState(String state, String token)throws ServerException{
		return usuarioMapper.getUsersState(state, token);
	}
	
	public UsuarioDTO changePicture(String url, String token) throws ServerException {
		UsuarioDTO bd = consultaXId(getUserFlex(token));
		bd.setImagen(url);
		return update(bd);
	}
// END region aditionalMethods

}