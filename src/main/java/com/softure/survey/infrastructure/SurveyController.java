package com.softure.survey.infrastructure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softure.java.dto.exception.ServerException;
import com.softure.survey.application.EncuestaGrupoSvc;
import com.softure.survey.application.EncuestaSvc;
import com.softure.survey.application.PostPreguntaSvc;
import com.softure.survey.application.PostRespuestaSvc;
import com.softure.survey.domain.EncuestaDTO;
import com.softure.survey.domain.EncuestaFilterDTO;
import com.softure.survey.domain.EncuestaGrupoDTO;
import com.softure.survey.domain.PostPreguntaDTO;
import com.softure.survey.domain.PostPreguntaFilterDTO;
import com.softure.survey.domain.PostRespuestaDTO;
import com.softure.survey.domain.PostRespuestaFilterDTO;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/survey")
public class SurveyController {
	
	@Autowired private EncuestaSvc encuestaService;
	@Autowired private EncuestaGrupoSvc groupService;
	@Autowired private PostPreguntaSvc preguntaService;
	@Autowired private PostRespuestaSvc respuestaService;
	
	@RequestMapping(value="/getAvailable", method=RequestMethod.GET)
	public List<EncuestaDTO> obtenerCampos(@RequestHeader("Authorization") String token) throws ServerException {
		EncuestaFilterDTO filter = new EncuestaFilterDTO();
		filter.setSecurityToken(token);
		return encuestaService.listarDisponibles(filter);
	}
	
	@RequestMapping(value="/responseGroupSurvey", method=RequestMethod.POST)
	public EncuestaGrupoDTO responseSurvey(@RequestBody EncuestaGrupoDTO dto, @RequestHeader("Authorization") String token)  throws ServerException  {
		return groupService.responderEncuesta(dto, token);
	}
	
	@RequestMapping(value="/getFAQ", method=RequestMethod.GET)
	public List<PostPreguntaDTO> obtenerPreguntas(@RequestHeader("Authorization") String token) throws ServerException {
		return preguntaService.listarEnOrden(new PostPreguntaFilterDTO());
	}
	
	@RequestMapping(value="/getFAQResponse/{id}", method=RequestMethod.GET)
	public List<PostRespuestaDTO> obtenerREspuestas(@RequestHeader("Authorization") String token, @PathVariable String id) throws ServerException {
		PostRespuestaFilterDTO filter  = new PostRespuestaFilterDTO();
		filter.setPregunta(id);
		return respuestaService.listarEnOrden(filter);
	}
}
