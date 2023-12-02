package com.softure.survey.application;

import java.util.List;

// BEGIN region interImport
import java.util.Date;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.survey.domain.PostCalificacionDTO;
import com.softure.survey.domain.PostCalificacionFilterDTO;
import com.softure.survey.infrastructure.PostCalificacionMapper;

@Service("postCalificacionService")
public class PostCalificacionSvc extends BasicSvc<PostCalificacionDTO, PostCalificacionFilterDTO> {
	
	@Autowired
	private PostCalificacionMapper postCalificacionMapper;
	
	// BEGIN region servicesPostCalificacion
	// END region servicesPostCalificacion

	@Override
	public PostCalificacionDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. PostCalificacion");
		PostCalificacionFilterDTO dto = new PostCalificacionFilterDTO();
		dto.setLlaveTabla(llave);
		return postCalificacionMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = postCalificacionMapper;
	}
	
	@Override
	public PostCalificacionDTO activar(PostCalificacionDTO dto, String token) throws ServerException {
		// BEGIN PostCalificacion_activar
		return super.activar(dto, token);
		// END PostCalificacion_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PostCalificacionDTO actualizar( PostCalificacionDTO dto, String token) throws ServerException {
		// BEGIN PostCalificacion_actualizar
		return super.actualizar(dto, token);
		// END PostCalificacion_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PostCalificacionDTO inactivar(PostCalificacionDTO dto, String token) throws ServerException {
		// BEGIN PostCalificacion_inactivar
		return super.inactivar(dto, token);
		// END PostCalificacion_inactivar
	}
	
	@Override
	public PostCalificacionDTO consultaUnica(PostCalificacionFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(PostCalificacionFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<PostCalificacionDTO> listarConsulta(PostCalificacionFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PostCalificacionDTO guardar(PostCalificacionDTO dto, String token) throws ServerException {
		// BEGIN PostCalificacion_guardar
		dto.setFecha(new Date());
		dto.setUsuario(getUserFlex(token));
		return super.guardar(dto, token);
		// END PostCalificacion_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}