package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import com.softure.java.cons.ConstantesGenerales;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.EncuestaOpcionRespuestaDTO;
import com.softure.logisticpymes.dto.filter.EncuestaOpcionRespuestaFilterDTO;
import com.softure.logisticpymes.persistence.EncuestaOpcionRespuestaMapper;

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
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public EncuestaOpcionRespuestaDTO actualizar( EncuestaOpcionRespuestaDTO dto, String token) throws ServerException {
		// BEGIN EncuestaOpcionRespuesta_actualizar
		return super.actualizar(dto, token);
		// END EncuestaOpcionRespuesta_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
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
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public EncuestaOpcionRespuestaDTO guardar(EncuestaOpcionRespuestaDTO dto, String token) throws ServerException {
		// BEGIN EncuestaOpcionRespuesta_guardar
		return super.guardar(dto, token);
		// END EncuestaOpcionRespuesta_guardar
	}

// BEGIN region aditionalMethods
	public List<EncuestaOpcionRespuestaDTO> getOptions(String id) throws ServerException {
		EncuestaOpcionRespuestaFilterDTO filter = new EncuestaOpcionRespuestaFilterDTO();
		filter.setPregunta(id);
		filter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		return listarConsulta(filter);
	}
// END region aditionalMethods

}