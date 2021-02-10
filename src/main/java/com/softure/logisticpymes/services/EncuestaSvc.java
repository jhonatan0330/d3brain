package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.util.Date;

import com.softure.logisticpymes.dto.EncuestaGrupoDTO;
import com.softure.logisticpymes.dto.filter.EncuestaGrupoFilterDTO;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.services.SoftureUtil;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.EncuestaDTO;
import com.softure.logisticpymes.dto.filter.EncuestaFilterDTO;
import com.softure.logisticpymes.persistence.EncuestaMapper;

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
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public EncuestaDTO actualizar( EncuestaDTO dto, String token) throws ServerException {
		// BEGIN Encuesta_actualizar
		SoftureUtil.validarFechaInicioFin(dto.getFechaInicio(), dto.getFechaFin());
		return super.actualizar(dto, token);
		// END Encuesta_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
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
	
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public EncuestaDTO copiar(EncuestaDTO dto, String token)throws ServerException{
		// BEGIN region copiar
		String encuestaBase = dto.getLlaveTabla();
		dto.setLlaveTabla(null);
		dto  = guardar(dto, token);
		
		//Consultar los grupos,
		EncuestaGrupoFilterDTO filtroGrupo = new EncuestaGrupoFilterDTO();
		filtroGrupo.setEncuesta(encuestaBase);
		filtroGrupo.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
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
		// END region listarDisponibles
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

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
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