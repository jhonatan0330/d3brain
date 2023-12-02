package com.softure.survey.application;

import java.util.List;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.logisticpymes.application.BasicSvc;
import com.softure.survey.domain.EncuestaOpcionRespuestaDTO;
import com.softure.survey.domain.EncuestaOpcionRespuestaFilterDTO;
import com.softure.survey.infrastructure.EncuestaOpcionRespuestaMapper;

@Service("encuestaOpcionRespuestaService")
public class EncuestaOpcionRespuestaSvc extends BasicSvc<EncuestaOpcionRespuestaDTO, EncuestaOpcionRespuestaFilterDTO> {
	
	@Autowired
	private EncuestaOpcionRespuestaMapper encuestaOpcionRespuestaMapper;
	
	// BEGIN region servicesEncuestaOpcionRespuesta
	// END region servicesEncuestaOpcionRespuesta

	@Override
	public EncuestaOpcionRespuestaDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. EncuestaOpcionRespuesta");
		EncuestaOpcionRespuestaFilterDTO dto = new EncuestaOpcionRespuestaFilterDTO();
		dto.setLlaveTabla(llave);
		return encuestaOpcionRespuestaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = encuestaOpcionRespuestaMapper;
	}
	
	@Override
	public EncuestaOpcionRespuestaDTO activar(EncuestaOpcionRespuestaDTO dto, String token) throws ServerException {
		// BEGIN EncuestaOpcionRespuesta_activar
		return super.activar(dto, token);
		// END EncuestaOpcionRespuesta_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public EncuestaOpcionRespuestaDTO actualizar( EncuestaOpcionRespuestaDTO dto, String token) throws ServerException {
		// BEGIN EncuestaOpcionRespuesta_actualizar
		return super.actualizar(dto, token);
		// END EncuestaOpcionRespuesta_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public EncuestaOpcionRespuestaDTO inactivar(EncuestaOpcionRespuestaDTO dto, String token) throws ServerException {
		// BEGIN EncuestaOpcionRespuesta_inactivar
		return super.inactivar(dto, token);
		// END EncuestaOpcionRespuesta_inactivar
	}
	
	@Override
	public EncuestaOpcionRespuestaDTO consultaUnica(EncuestaOpcionRespuestaFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(EncuestaOpcionRespuestaFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<EncuestaOpcionRespuestaDTO> listarConsulta(EncuestaOpcionRespuestaFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public EncuestaOpcionRespuestaDTO guardar(EncuestaOpcionRespuestaDTO dto, String token) throws ServerException {
		// BEGIN EncuestaOpcionRespuesta_guardar
		return super.guardar(dto, token);
		// END EncuestaOpcionRespuesta_guardar
	}

// BEGIN region aditionalMethods
	public List<EncuestaOpcionRespuestaDTO> getOptions(String id) throws ServerException {
		EncuestaOpcionRespuestaFilterDTO filter = new EncuestaOpcionRespuestaFilterDTO();
		filter.setPregunta(id);
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		return listarConsulta(filter);
	}
// END region aditionalMethods

}