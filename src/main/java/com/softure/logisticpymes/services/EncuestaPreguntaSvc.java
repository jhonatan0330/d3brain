package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.domain.dto.EncuestaPreguntaDTO;
import com.softure.logisticpymes.domain.filter.EncuestaPreguntaFilterDTO;
import com.softure.logisticpymes.infrastructure.mybatis.mapper.EncuestaPreguntaMapper;

@Service("encuestaPreguntaService")
public class EncuestaPreguntaSvc extends BasicSvc<EncuestaPreguntaDTO, EncuestaPreguntaFilterDTO> {
	
	@Autowired
	private EncuestaPreguntaMapper encuestaPreguntaMapper;
	
	// BEGIN region servicesEncuestaPregunta
	@Autowired
	private EncuestaOpcionRespuestaSvc opcionesSvc;
	// END region servicesEncuestaPregunta

	@Override
	public EncuestaPreguntaDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. EncuestaPregunta");
		EncuestaPreguntaFilterDTO dto = new EncuestaPreguntaFilterDTO();
		dto.setLlaveTabla(llave);
		return encuestaPreguntaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = encuestaPreguntaMapper;
	}
	
	@Override
	public EncuestaPreguntaDTO activar(EncuestaPreguntaDTO dto, String token) throws ServerException {
		// BEGIN EncuestaPregunta_activar
		return super.activar(dto, token);
		// END EncuestaPregunta_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public EncuestaPreguntaDTO actualizar( EncuestaPreguntaDTO dto, String token) throws ServerException {
		// BEGIN EncuestaPregunta_actualizar
		return super.actualizar(dto, token);
		// END EncuestaPregunta_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public EncuestaPreguntaDTO inactivar(EncuestaPreguntaDTO dto, String token) throws ServerException {
		// BEGIN EncuestaPregunta_inactivar
		return super.inactivar(dto, token);
		// END EncuestaPregunta_inactivar
	}
	
	@Override
	public EncuestaPreguntaDTO consultaUnica(EncuestaPreguntaFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(EncuestaPreguntaFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<EncuestaPreguntaDTO> listarConsulta(EncuestaPreguntaFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	public List<EncuestaPreguntaDTO> listarPermitidas(EncuestaPreguntaFilterDTO dto)throws ServerException{
		// BEGIN region listarPermitidas
		// END region listarPermitidas
		paginar(dto);
		try {
			return encuestaPreguntaMapper.listarPermitidas(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public EncuestaPreguntaDTO guardar(EncuestaPreguntaDTO dto, String token) throws ServerException {
		// BEGIN EncuestaPregunta_guardar
		return super.guardar(dto, token);
		// END EncuestaPregunta_guardar
	}

// BEGIN region aditionalMethods
	public List<EncuestaPreguntaDTO> getQuestions(String grupoId, String token) throws ServerException {
		EncuestaPreguntaFilterDTO filter = new EncuestaPreguntaFilterDTO();
		filter.setGrupo(grupoId);
		filter.setSecurityToken(token);
		List<EncuestaPreguntaDTO> preguntas = listarPermitidas(filter);
		for (EncuestaPreguntaDTO iPregunta : preguntas) {
			iPregunta.setOpciones(opcionesSvc.getOptions(iPregunta.getLlaveTabla()));
		}
		return preguntas;
	}
// END region aditionalMethods

}