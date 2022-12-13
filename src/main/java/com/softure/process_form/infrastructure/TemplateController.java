package com.softure.process_form.infrastructure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.application.field.CampoAdaptador;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import com.softure.document_transition.application.DocumentoRelacionGestorSvc;
import com.softure.document_transition.domain.DocumentoRelacionGestorDTO;
import com.softure.document_transition.domain.DocumentoRelacionGestorFilterDTO;
import com.softure.java.dto.exception.ServerException;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaFilterDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.process_form.domain.DocumentoPlantillaFilterDTO;
import com.softure.property.application.RelacionInternaSvc;
import com.softure.property.domain.RelacionInternaDTO;
import com.softure.property.domain.RelacionInternaFilterDTO;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/template")
public class TemplateController {

	@Autowired private CampoAdaptador adaptador;
	@Autowired private DocumentoPlantillaSvc documentoplantillaService;
	@Autowired private DocumentoPlantillaCaracteristicaSvc campoService;
	@Autowired private DocumentoRelacionGestorSvc gestionService;
	@Autowired private RelacionInternaSvc relacionesService;
	@Autowired private PedidoVentaCaracteristicaSvc fieldsService;
	
	
	@GetMapping(value="/getTemplates")
	public List<DocumentoPlantillaDTO> consultaUsuarioDocumentoPlantilla(@RequestHeader("Authorization") String token)  throws ServerException  {
		DocumentoPlantillaFilterDTO filter = new DocumentoPlantillaFilterDTO();
		filter.setSecurityToken(token);
		return documentoplantillaService.consultaUsuario(filter);	
	}
	
	@RequestMapping(value="/getFields", method=RequestMethod.GET)
	public DocumentoPlantillaDTO obtenerCampos(@RequestParam String id, @RequestHeader("Authorization") String token) throws ServerException {
		DocumentoPlantillaDTO filterTemplate = new DocumentoPlantillaDTO();
		filterTemplate.setLlaveTabla(id);
		return documentoplantillaService.obtenerCampos(filterTemplate, token);
	}
	
	@RequestMapping(value="/getFieldData", method=RequestMethod.POST)
	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(@RequestBody PedidoVentaCaracteristicaFilterDTO filterField, @RequestHeader("Authorization") String token)  throws ServerException  {
		filterField.setSecurityToken(token);
		return adaptador.consultarDatosBase(filterField);
	}
	
	@RequestMapping(value="/getTrace", method=RequestMethod.POST)
	public List<DocumentoRelacionGestorDTO> getTrace(@RequestBody DocumentoRelacionGestorFilterDTO filterField, @RequestHeader("Authorization") String token)  throws ServerException  {
		filterField.setSecurityToken(token);
		return gestionService.listarExpedientesGestionadores(filterField);
	}
	
	@RequestMapping(value="/getTraceFields/{documentId}/{transaction}", method=RequestMethod.GET)
	public List<PedidoVentaCaracteristicaDTO> getTraceFields(@PathVariable String documentId, @PathVariable String transaction, @RequestHeader("Authorization") String token)  throws ServerException  {
		return fieldsService.listar2Gestor(documentId, transaction);
	}
	
	@RequestMapping(value="/getPropertyRelations", method=RequestMethod.POST)
	public List<RelacionInternaDTO> getPropertyRelations(@RequestBody RelacionInternaFilterDTO filter, @RequestHeader("Authorization") String token)  throws ServerException  {
		filter.setSecurityToken(token);
		return relacionesService.listarConsulta(filter);
	}
	
	@RequestMapping(value="/validateLoad", method=RequestMethod.POST)
	public DocumentoPlantillaCaracteristicaDTO validateLoad(@RequestBody DocumentoPlantillaCaracteristicaFilterDTO filter, @RequestHeader("Authorization") String token)  throws ServerException  {
		filter.setSecurityToken(token);
		return campoService.listarCarga(filter);
	}
}
