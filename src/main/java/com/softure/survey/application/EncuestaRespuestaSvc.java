package com.softure.survey.application;

import java.util.List;

// BEGIN region interImport
import java.util.Date;
import com.softure.java.cons.ConstantesGenerales;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.survey.domain.EncuestaOpcionRespuestaDTO;
import com.softure.survey.domain.EncuestaRespuestaDTO;
import com.softure.survey.domain.EncuestaRespuestaFilterDTO;
import com.softure.survey.infrastructure.EncuestaRespuestaMapper;

@Service("encuestaRespuestaService")
public class EncuestaRespuestaSvc extends BasicSvc<EncuestaRespuestaDTO, EncuestaRespuestaFilterDTO> {
	
	@Autowired
	private EncuestaRespuestaMapper encuestaRespuestaMapper;
	
	// BEGIN region servicesEncuestaRespuesta
	@Autowired
	public EncuestaOpcionRespuestaSvc encuestaOpcionRespuestaSvc;
	// END region servicesEncuestaRespuesta

	@Override
	public EncuestaRespuestaDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. EncuestaRespuesta");
		EncuestaRespuestaFilterDTO dto = new EncuestaRespuestaFilterDTO();
		dto.setLlaveTabla(llave);
		return encuestaRespuestaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = encuestaRespuestaMapper;
	}
	
	@Override
	public EncuestaRespuestaDTO activar(EncuestaRespuestaDTO dto, String token) throws ServerException {
		// BEGIN EncuestaRespuesta_activar
		return super.activar(dto, token);
		// END EncuestaRespuesta_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public EncuestaRespuestaDTO actualizar( EncuestaRespuestaDTO dto, String token) throws ServerException {
		// BEGIN EncuestaRespuesta_actualizar
		return super.actualizar(dto, token);
		// END EncuestaRespuesta_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public EncuestaRespuestaDTO inactivar(EncuestaRespuestaDTO dto, String token) throws ServerException {
		// BEGIN EncuestaRespuesta_inactivar
		return super.inactivar(dto, token);
		// END EncuestaRespuesta_inactivar
	}
	
	@Override
	public EncuestaRespuestaDTO consultaUnica(EncuestaRespuestaFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(EncuestaRespuestaFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<EncuestaRespuestaDTO> listarConsulta(EncuestaRespuestaFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public EncuestaRespuestaDTO guardar(EncuestaRespuestaDTO dto, String token) throws ServerException {
		// BEGIN EncuestaRespuesta_guardar
		dto.setFecha(new Date());
		dto.setUsuario(getUserFlex(token));
		if(dto.getRespuestaOpcion()!=null){
			EncuestaOpcionRespuestaDTO eor = encuestaOpcionRespuestaSvc.consultaXId(dto.getRespuestaOpcion());
			if(eor ==null) throw new ServerException("No se encontro esta opcion de respuesta en la BD");
			if(eor.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO) ==0 ) throw new ServerException("Esta opcion de respuesta esta inactiva");
			if(eor.getPregunta().compareTo(dto.getPregunta()) !=0 ) throw new ServerException("Esta opcion de respuesta no corresponde a la pregunta");
		}
		
		return super.guardar(dto, token);
		// END EncuestaRespuesta_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}