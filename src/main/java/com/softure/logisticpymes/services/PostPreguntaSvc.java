package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.util.Date;
import com.softure.java.cons.ConstantesGenerales;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.PostPreguntaDTO;
import com.softure.logisticpymes.dto.filter.PostPreguntaFilterDTO;
import com.softure.logisticpymes.persistence.PostPreguntaMapper;

@Service("postPreguntaService")
public class PostPreguntaSvc extends BasicSvc<PostPreguntaDTO, PostPreguntaFilterDTO> {
	
	@Autowired
	private PostPreguntaMapper postPreguntaMapper;
	
	// BEGIN region servicesPostPregunta
	// END region servicesPostPregunta

	@Override
	public PostPreguntaDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. PostPregunta");
		PostPreguntaFilterDTO dto = new PostPreguntaFilterDTO();
		dto.setLlaveTabla(llave);
		return postPreguntaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = postPreguntaMapper;
	}
	
	@Override
	public PostPreguntaDTO activar(PostPreguntaDTO dto, String token) throws ServerException {
		// BEGIN PostPregunta_activar
		return super.activar(dto, token);
		// END PostPregunta_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PostPreguntaDTO actualizar( PostPreguntaDTO dto, String token) throws ServerException {
		// BEGIN PostPregunta_actualizar
		return super.actualizar(dto, token);
		// END PostPregunta_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PostPreguntaDTO inactivar(PostPreguntaDTO dto, String token) throws ServerException {
		// BEGIN PostPregunta_inactivar
		return super.inactivar(dto, token);
		// END PostPregunta_inactivar
	}
	
	@Override
	public PostPreguntaDTO consultaUnica(PostPreguntaFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(PostPreguntaFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<PostPreguntaDTO> listarConsulta(PostPreguntaFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	public List<PostPreguntaDTO> listarEnOrden(PostPreguntaFilterDTO dto)throws ServerException{
		// BEGIN region listarEnOrden
		paginar(dto);
		dto.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		return postPreguntaMapper.listarEnOrden(dto);
		// END region listarEnOrden
	}
	public List<PostPreguntaDTO> listarPreguntasSinRespuesta(PostPreguntaFilterDTO dto)throws ServerException{
		// BEGIN region listarPreguntasSinRespuesta
		paginar(dto);
		dto.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		return postPreguntaMapper.listarPreguntasSinRespuesta(dto);
		// END region listarPreguntasSinRespuesta
	}

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PostPreguntaDTO guardar(PostPreguntaDTO dto, String token) throws ServerException {
		// BEGIN PostPregunta_guardar
		dto.setFecha(new Date());
		dto.setAutor(getUserFlex(token));
		return super.guardar(dto, token);
		// END PostPregunta_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}