package com.softure.survey.infrastructure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shared.domain.ServerException;
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
	
	@Autowired @Lazy  private EncuestaSvc encuestaService;
	@Autowired @Lazy  private EncuestaGrupoSvc groupService;
	@Autowired @Lazy  private PostPreguntaSvc preguntaService;
	@Autowired @Lazy  private PostRespuestaSvc respuestaService;
	
	@GetMapping(value="/getAvailable")
	public List<EncuestaDTO> obtenerCampos(@RequestHeader("Authorization") String token) throws ServerException {
		EncuestaFilterDTO filter = new EncuestaFilterDTO();
		filter.setSecurityToken(token);
		return encuestaService.listarDisponibles(filter);
	}
	
	@PostMapping(value="/responseGroupSurvey")
	public EncuestaGrupoDTO responseSurvey(@RequestBody EncuestaGrupoDTO dto, @RequestHeader("Authorization") String token)  throws ServerException  {
		return groupService.responderEncuesta(dto, token);
	}
	
	@GetMapping(value="/getFAQ")
	public List<PostPreguntaDTO> obtenerPreguntas(@RequestHeader("Authorization") String token) throws ServerException {
		return preguntaService.listarEnOrden(new PostPreguntaFilterDTO());
	}
	
	@GetMapping(value="/getFAQResponse/{id}")
	public List<PostRespuestaDTO> obtenerREspuestas(@RequestHeader("Authorization") String token, @PathVariable String id) throws ServerException {
		PostRespuestaFilterDTO filter  = new PostRespuestaFilterDTO();
		filter.setPregunta(id);
		return respuestaService.listarEnOrden(filter);
	}
}
