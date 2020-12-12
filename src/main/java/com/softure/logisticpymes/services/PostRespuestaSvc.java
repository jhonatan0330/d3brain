package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.util.Date;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.PostRespuestaDTO;
import com.softure.logisticpymes.dto.filter.PostRespuestaFilterDTO;
import com.softure.logisticpymes.persistence.PostRespuestaMapper;

@Service("postRespuestaService")
public class PostRespuestaSvc extends BasicSvc<PostRespuestaDTO, PostRespuestaFilterDTO> {
	
	@Autowired
	private PostRespuestaMapper postRespuestaMapper;
	
	// BEGIN region servicesPostRespuesta
	// END region servicesPostRespuesta

	@Override
	public PostRespuestaDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. PostRespuesta");
		PostRespuestaFilterDTO dto = new PostRespuestaFilterDTO();
		dto.setLlaveTabla(llave);
		return postRespuestaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = postRespuestaMapper;
	}
	
	@Override
	public PostRespuestaDTO activar(PostRespuestaDTO dto, String token) throws ServerException {
		// BEGIN PostRespuesta_activar
		return super.activar(dto, token);
		// END PostRespuesta_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PostRespuestaDTO actualizar( PostRespuestaDTO dto, String token) throws ServerException {
		// BEGIN PostRespuesta_actualizar
		return super.actualizar(dto, token);
		// END PostRespuesta_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PostRespuestaDTO inactivar(PostRespuestaDTO dto, String token) throws ServerException {
		// BEGIN PostRespuesta_inactivar
		return super.inactivar(dto, token);
		// END PostRespuesta_inactivar
	}
	
	@Override
	public PostRespuestaDTO consultaUnica(PostRespuestaFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(PostRespuestaFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<PostRespuestaDTO> listarConsulta(PostRespuestaFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PostRespuestaDTO guardar(PostRespuestaDTO dto, String token) throws ServerException {
		// BEGIN PostRespuesta_guardar
		dto.setFecha(new Date());
		dto.setAutor(getUserFlex(token));
		return super.guardar(dto, token);
		// END PostRespuesta_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}