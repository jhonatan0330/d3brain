package com.softure.survey.application;

import java.util.List;

// BEGIN region interImport
import java.util.Date;

import com.shared.domain.ServerException;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.application.BasicSvc;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.survey.domain.EncuestaDTO;
import com.softure.survey.domain.EncuestaGrupoDTO;
import com.softure.survey.domain.EncuestaGrupoFilterDTO;
import com.softure.survey.domain.EncuestaOpcionRespuestaDTO;
import com.softure.survey.domain.EncuestaOpcionRespuestaFilterDTO;
import com.softure.survey.domain.EncuestaPreguntaDTO;
import com.softure.survey.domain.EncuestaPreguntaFilterDTO;
import com.softure.survey.domain.EncuestaRespuestaDTO;
import com.softure.survey.infrastructure.EncuestaGrupoMapper;

@Service("encuestaGrupoService")
public class EncuestaGrupoSvc extends BasicSvc<EncuestaGrupoDTO, EncuestaGrupoFilterDTO> {
	
	@Autowired
	private EncuestaGrupoMapper encuestaGrupoMapper;
	
	// BEGIN region servicesEncuestaGrupo
	@Autowired
	private EncuestaRespuestaSvc encuestaRespuestaService;
	@Autowired
	private EncuestaSvc encuestaSvc;
	@Autowired
	private EncuestaPreguntaSvc encuestaPreguntaSvc;
	@Autowired
	private EncuestaOpcionRespuestaSvc encuestaOpcionRespuestaSvc;
	// END region servicesEncuestaGrupo

	@Override
	public EncuestaGrupoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. EncuestaGrupo");
		EncuestaGrupoFilterDTO dto = new EncuestaGrupoFilterDTO();
		dto.setLlaveTabla(llave);
		return encuestaGrupoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = encuestaGrupoMapper;
	}
	
	@Override
	public EncuestaGrupoDTO activar(EncuestaGrupoDTO dto, String token) throws ServerException {
		// BEGIN EncuestaGrupo_activar
		return super.activar(dto, token);
		// END EncuestaGrupo_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public EncuestaGrupoDTO actualizar( EncuestaGrupoDTO dto, String token) throws ServerException {
		// BEGIN EncuestaGrupo_actualizar
		return super.actualizar(dto, token);
		// END EncuestaGrupo_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public EncuestaGrupoDTO inactivar(EncuestaGrupoDTO dto, String token) throws ServerException {
		// BEGIN EncuestaGrupo_inactivar
		return super.inactivar(dto, token);
		// END EncuestaGrupo_inactivar
	}
	
	@Override
	public EncuestaGrupoDTO consultaUnica(EncuestaGrupoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(EncuestaGrupoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<EncuestaGrupoDTO> listarConsulta(EncuestaGrupoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public EncuestaGrupoDTO responderEncuesta(EncuestaGrupoDTO dto, String token)throws ServerException{
		// BEGIN region responderEncuesta
		List<EncuestaRespuestaDTO>  respuestas = dto.getRespuestas();
		if(respuestas==null || respuestas.size() ==0) throw new ServerException("respuestas no tiene formato correcto");
		
		EncuestaGrupoFilterDTO bdFilter = new EncuestaGrupoFilterDTO();
		bdFilter.setLlaveTabla(dto.getLlaveTabla());
		bdFilter.setUsuario(getUserFlex(token));
		EncuestaGrupoDTO bd = consultaUnica(bdFilter);
		
		
		EncuestaDTO encuesta = encuestaSvc.consultaXId(bd.getEncuesta());
		try {
			SoftureUtil.validarFechaInicioFin(new Date(), new Date(), encuesta.getFechaInicio(), encuesta.getFechaFin());
		} catch (ServerException e) {
			throw new ServerException("se encuentra por fuera del tiempo de la encuesta.\n Inicio: " + encuesta.getFechaInicio().toString() + "\nFechaFin : " + encuesta.getFechaFin().toString());
		}
		EncuestaPreguntaFilterDTO filtroPreguntas = new EncuestaPreguntaFilterDTO();
		filtroPreguntas.setGrupo(bd.getLlaveTabla());
		filtroPreguntas.setSecurityToken(token);
		List <EncuestaPreguntaDTO> preguntas = encuestaPreguntaSvc.listarPermitidas(filtroPreguntas);
		
		if (bd.getNumeroRespuestasUsuario()!=0)
			throw new ServerException("Este grupo de preguntas ya se han respondido");
		if(respuestas.size()!=preguntas.size())
			throw new ServerException("La cantidad de preguntas que se respondieron no corresponde a las solicitadas");
		
		for (EncuestaRespuestaDTO respondio : respuestas) {
			encuestaRespuestaService.guardar(respondio, token);
		}
		return bd;
		// END region responderEncuesta
	}
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public EncuestaGrupoDTO copiar(EncuestaGrupoDTO dto, String token)throws ServerException{
		// BEGIN region copiar
		String grupoBase =dto.getLlaveTabla();
		dto.setLlaveTabla(null);
		dto = guardar(dto, token);
		
		String preguntaBase;
		EncuestaPreguntaFilterDTO filtroPregunta;
		EncuestaOpcionRespuestaFilterDTO filtroOpcion;
		
		filtroPregunta = new EncuestaPreguntaFilterDTO();
		filtroPregunta.setGrupo(grupoBase);
		filtroPregunta.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		List<EncuestaPreguntaDTO> preguntas = encuestaPreguntaSvc.listarConsulta(filtroPregunta);
		for (EncuestaPreguntaDTO encuestaPreguntaDTO : preguntas) {
			preguntaBase = encuestaPreguntaDTO.getLlaveTabla();
			encuestaPreguntaDTO.setLlaveTabla(null);
			encuestaPreguntaDTO.setGrupo(dto.getLlaveTabla());
			encuestaPreguntaDTO = encuestaPreguntaSvc.guardar(encuestaPreguntaDTO, token);

			filtroOpcion = new EncuestaOpcionRespuestaFilterDTO();
			filtroOpcion.setPregunta(preguntaBase);
			filtroOpcion.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			List<EncuestaOpcionRespuestaDTO> opciones = encuestaOpcionRespuestaSvc.listarConsulta(filtroOpcion);
			for (EncuestaOpcionRespuestaDTO encuestaOpcionRespuestaDTO : opciones) {
				encuestaOpcionRespuestaDTO.setLlaveTabla(null);
				encuestaOpcionRespuestaDTO.setPregunta(encuestaPreguntaDTO.getLlaveTabla());
				encuestaOpcionRespuestaDTO = encuestaOpcionRespuestaSvc.guardar(encuestaOpcionRespuestaDTO, token);
			}
			
		}
		return dto;
		// END region copiar
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public EncuestaGrupoDTO guardar(EncuestaGrupoDTO dto, String token) throws ServerException {
		// BEGIN EncuestaGrupo_guardar
		return super.guardar(dto, token);
		// END EncuestaGrupo_guardar
	}

// BEGIN region aditionalMethods
	public List<EncuestaGrupoDTO> getGroups(String surveyId, String token) throws ServerException {
		EncuestaGrupoFilterDTO filtroGrupo = new EncuestaGrupoFilterDTO();
		filtroGrupo.setEncuesta(surveyId);
		filtroGrupo.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		List<EncuestaGrupoDTO> grupos = listarConsulta(filtroGrupo);
		for (EncuestaGrupoDTO encuestaGrupoDTO : grupos) {
			encuestaGrupoDTO.setPreguntas(encuestaPreguntaSvc.getQuestions(encuestaGrupoDTO.getLlaveTabla(), token));
		}
		return grupos;
	}
// END region aditionalMethods

}