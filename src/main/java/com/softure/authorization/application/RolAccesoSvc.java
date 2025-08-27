package com.softure.authorization.application;

import java.util.List;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.authorization.domain.RolAccesoDTO;
import com.softure.authorization.domain.RolAccesoFilterDTO;
import com.softure.authorization.domain.UsuarioRolFilterDTO;
import com.softure.authorization.infrastructure.RolAccesoMapper;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.logisticpymes.application.BasicSvc;
import com.softure.property.application.PropertyCRUDSvc;

import jakarta.annotation.PostConstruct;

@Service("rolAccesoService")
public class RolAccesoSvc extends BasicSvc<RolAccesoDTO, RolAccesoFilterDTO> {
	
	@Autowired @Lazy 
	private RolAccesoMapper rolAccesoMapper;
	
	@Autowired @Lazy  private UsuarioRolSvc usuarioRolService;
	@Autowired @Lazy  private PropertyCRUDSvc propertySvc;

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
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		filtro.setRolAcceso(dto.getLlaveTabla());
		int cont = usuarioRolService.contarResultados(filtro);
		if(cont!=0) throw new ServerException("No se puede inactivar el rol debido a que tiene usuarios activos. " + cont);
		propertySvc.inactivateAllPropertiesOfRol(dto.getLlaveTabla(), token);
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
	
	public List<RolAccesoDTO> consultaUsuarioDocumento(String userId)throws ServerException{
		try {
			return rolAccesoMapper.consultaUsuarioDocumento(userId); 
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


	public boolean usuarioPermisosCompletos(String token) throws ServerException{
		String user = getUserFlex(token);
		if(user.compareTo("PROCESS")==0) return true;
		if(rolAccesoMapper.permisosCompletos(user)!=0) return true;
		return false;
	}

	public List<RolAccesoDTO> getFullToSynchronize(List<String> process) {
		return rolAccesoMapper.getFullToSynchronize(process);
	}

}