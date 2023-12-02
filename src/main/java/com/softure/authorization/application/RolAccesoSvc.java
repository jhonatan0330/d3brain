package com.softure.authorization.application;

import java.util.List;

import com.shared.domain.ServerException;
import com.softure.authorization.domain.RolAccesoDTO;
import com.softure.authorization.domain.RolAccesoFilterDTO;
import com.softure.authorization.domain.UsuarioRolFilterDTO;
import com.softure.authorization.infrastructure.RolAccesoMapper;
// BEGIN region interImport
import com.softure.java.cons.ConstantesGenerales;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.logisticpymes.application.BasicSvc;

@Service("rolAccesoService")
public class RolAccesoSvc extends BasicSvc<RolAccesoDTO, RolAccesoFilterDTO> {
	
	@Autowired
	private RolAccesoMapper rolAccesoMapper;
	
	// BEGIN region servicesRolAcceso
	@Autowired private UsuarioRolSvc usuarioRolService;
	// END region servicesRolAcceso

	@Override
	public RolAccesoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. RolAcceso");
		RolAccesoFilterDTO dto = new RolAccesoFilterDTO();
		dto.setLlaveTabla(llave);
		return rolAccesoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = rolAccesoMapper;
	}
	
	@Override
	public RolAccesoDTO activar(RolAccesoDTO dto, String token) throws ServerException {
		// BEGIN RolAcceso_activar
		return super.activar(dto, token);
		// END RolAcceso_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public RolAccesoDTO actualizar( RolAccesoDTO dto, String token) throws ServerException {
		// BEGIN RolAcceso_actualizar
		return super.actualizar(dto, token);
		// END RolAcceso_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public RolAccesoDTO inactivar(RolAccesoDTO dto, String token) throws ServerException {
		// BEGIN RolAcceso_inactivar
		dto = super.inactivar(dto, token);
		UsuarioRolFilterDTO filtro = new UsuarioRolFilterDTO();
		filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		filtro.setRolAcceso(dto.getLlaveTabla());
		int cont = usuarioRolService.contarResultados(filtro);
		if(cont!=0) throw new ServerException("No se puede inactivar el rol debido a que tiene usuarios activos. " + cont);
		return dto;
		// END RolAcceso_inactivar
	}
	
	@Override
	public RolAccesoDTO consultaUnica(RolAccesoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(RolAccesoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<RolAccesoDTO> listarConsulta(RolAccesoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	public List<RolAccesoDTO> consultaUsuarioDocumento(RolAccesoFilterDTO dto)throws ServerException{
		// BEGIN region consultaUsuarioDocumento
		if(dto.getSecurityToken()==null) throw new ServerException("No se puede consultar Roles sin token de seguridad");
		// END region consultaUsuarioDocumento
		paginar(dto);
		try {
			return rolAccesoMapper.consultaUsuarioDocumento(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public RolAccesoDTO guardar(RolAccesoDTO dto, String token) throws ServerException {
		// BEGIN RolAcceso_guardar
		dto = super.guardar(dto, token);
		return dto;
		// END RolAcceso_guardar
	}

// BEGIN region aditionalMethods
	
	public boolean usuarioPermisosCompletos(String token) throws ServerException{
		String user = getUserFlex(token);
		if(user.compareTo("PROCESS")==0) return true;
		if(rolAccesoMapper.permisosCompletos(user)!=0) return true;
		return false;
	}
// END region aditionalMethods

	public List<RolAccesoDTO> getFullToSynchronize() {
		return rolAccesoMapper.getFullToSynchronize();
	}

}