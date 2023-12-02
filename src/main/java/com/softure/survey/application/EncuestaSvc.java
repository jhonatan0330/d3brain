package com.softure.survey.application;

import java.util.List;

// BEGIN region interImport
import java.util.Date;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.application.BasicSvc;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.survey.domain.EncuestaDTO;
import com.softure.survey.domain.EncuestaFilterDTO;
import com.softure.survey.domain.EncuestaGrupoDTO;
import com.softure.survey.domain.EncuestaGrupoFilterDTO;
import com.softure.survey.infrastructure.EncuestaMapper;

@Service("encuestaService")
public class EncuestaSvc extends BasicSvc<EncuestaDTO, EncuestaFilterDTO> {
	
	@Autowired
	private EncuestaMapper encuestaMapper;
	
	// BEGIN region servicesEncuesta
	@Autowired
	private EncuestaGrupoSvc encuestaGrupoSvc;

	// END region servicesEncuesta

	@Override
	public EncuestaDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. Encuesta");
		EncuestaFilterDTO dto = new EncuestaFilterDTO();
		dto.setLlaveTabla(llave);
		return encuestaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = encuestaMapper;
	}
	
	@Override
	public EncuestaDTO activar(EncuestaDTO dto, String token) throws ServerException {
		// BEGIN Encuesta_activar
		return super.activar(dto, token);
		// END Encuesta_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public EncuestaDTO actualizar( EncuestaDTO dto, String token) throws ServerException {
		// BEGIN Encuesta_actualizar
		SoftureUtil.validarFechaInicioFin(dto.getFechaInicio(), dto.getFechaFin());
		return super.actualizar(dto, token);
		// END Encuesta_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public EncuestaDTO inactivar(EncuestaDTO dto, String token) throws ServerException {
		// BEGIN Encuesta_inactivar
		return super.inactivar(dto, token);
		// END Encuesta_inactivar
	}
	
	@Override
	public EncuestaDTO consultaUnica(EncuestaFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(EncuestaFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<EncuestaDTO> listarConsulta(EncuestaFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public EncuestaDTO copiar(EncuestaDTO dto, String token)throws ServerException{
		// BEGIN region copiar
		String encuestaBase = dto.getLlaveTabla();
		dto.setLlaveTabla(null);
		dto  = guardar(dto, token);
		
		//Consultar los grupos,
		EncuestaGrupoFilterDTO filtroGrupo = new EncuestaGrupoFilterDTO();
		filtroGrupo.setEncuesta(encuestaBase);
		filtroGrupo.setEstado(SharedConstants.STATE_ACTIVE);
		List<EncuestaGrupoDTO> grupos = encuestaGrupoSvc.listarConsulta(filtroGrupo);
		for (EncuestaGrupoDTO encuestaGrupoDTO : grupos) {
			encuestaGrupoDTO.setEncuesta(dto.getLlaveTabla());
			encuestaGrupoDTO = encuestaGrupoSvc.copiar(encuestaGrupoDTO, token);
		}
		return dto;
		// END region copiar
	}
	public List<EncuestaDTO> listarDisponibles(EncuestaFilterDTO dto)throws ServerException{
		// BEGIN region listarDisponibles
		dto.setFechaInicioMax(new Date());
		dto.setFechaFinMin(new Date());
		dto.setCliente(getUserFlex(dto.getSecurityToken()));
		if(dto.getCliente()!=null){
			// Esto lo hagopor el generador de codigo
			paginar(dto);
			try {
				List<EncuestaDTO> result =encuestaMapper.listarDisponibles(dto);
				if(result!=null && !result.isEmpty()) {
					for (EncuestaDTO encuestaDTO : result) {
						encuestaDTO.setGrupos(encuestaGrupoSvc.getGroups(encuestaDTO.getLlaveTabla(), dto.getSecurityToken()));
					}
				}
				return  result;
			}catch (Exception e) {
				throw new ServerException(e.getCause().getMessage());
			}
		}
		// END region listarDisponibles
		paginar(dto);
		try {
			return encuestaMapper.listarDisponibles(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public EncuestaDTO guardar(EncuestaDTO dto, String token) throws ServerException {
		// BEGIN Encuesta_guardar
		SoftureUtil.validarFechaInicioFin(dto.getFechaInicio(), dto.getFechaFin(), null);
		dto.setFechaEjecucion(null);
		dto =  super.guardar(dto, token);
		return dto;
		// END Encuesta_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}