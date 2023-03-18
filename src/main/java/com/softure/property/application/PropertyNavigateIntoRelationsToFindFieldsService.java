package com.softure.property.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.document_execution.application.DocumentoRelacionExpedienteSvc;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.DocumentoRelacionExpedienteDTO;
import com.softure.document_execution.domain.DocumentoRelacionExpedienteFilterDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.domain.RelacionInternaDTO;

@Service
public class PropertyNavigateIntoRelationsToFindFieldsService {

	@Autowired
	private DocumentoRelacionExpedienteSvc documentRelationService;
	@Autowired
	private RelacionInternaSvc relationService;
	@Autowired
	private PedidoVentaCaracteristicaSvc fieldService;
	
	public List<PedidoVentaCaracteristicaDTO> call(String propertyId, List<PedidoVentaCaracteristicaDTO> fields)
			throws ServerException {
		List<RelacionInternaDTO> relations = relationService.relacionesPropiedad(propertyId);
		return searchFieldText(relations, fields);
	}
	
	private List<PedidoVentaCaracteristicaDTO> searchFieldText(List<RelacionInternaDTO> relations, List<PedidoVentaCaracteristicaDTO> fields)
			throws ServerException {
		if (relations == null || relations.isEmpty() || fields == null || fields.isEmpty())
			return null;
		List<PedidoVentaCaracteristicaDTO> valueOfFieldsToResult = null;
		List<PedidoVentaCaracteristicaDTO> internalFields = null;
		List<RelacionInternaDTO> validateRelations = new ArrayList<RelacionInternaDTO>();
		// relacionesSinRepetir.addAll(relaciones.);//SEparado al contructor creo que
		// para que funcione el remove
		for (RelacionInternaDTO iRelation : relations) {
			for (PedidoVentaCaracteristicaDTO iField : fields) {
				if (iRelation.getCampo().compareTo(iField.getCampo()) == 0) {
					validateRelations.add(iRelation);
					if (iField.getValorOpcion() != null) {
						if (internalFields == null)
							internalFields = new ArrayList<PedidoVentaCaracteristicaDTO>();
						internalFields.add(iField);
					} else {
						// En caso que sea multiple se debe evaluar todos los expedientes internos
						if (iField.getCampoDTO() != null
								&& DocumentoPlantillaCaracteristicaDTO.PROCESO
										.compareTo(iField.getCampoDTO().getFormato()) == 0
								&& Propiedades.obtenerValor(iField.getCampoDTO(), Propiedades.MULTIPLE) != null) {
							DocumentoRelacionExpedienteFilterDTO relacionExpedienteFilter = new DocumentoRelacionExpedienteFilterDTO();
							relacionExpedienteFilter.setCampoMaestro(iField.getLlaveTabla());
							relacionExpedienteFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
							List<DocumentoRelacionExpedienteDTO> anidateDocuments = documentRelationService
									.listarConsulta(relacionExpedienteFilter);
							if (anidateDocuments != null && !anidateDocuments.isEmpty()) {
								if (internalFields == null)
									internalFields = new ArrayList<PedidoVentaCaracteristicaDTO>();
								for (DocumentoRelacionExpedienteDTO iAnidado : anidateDocuments) {
									PedidoVentaCaracteristicaDTO newField = new PedidoVentaCaracteristicaDTO();
									newField.setValorOpcion(iAnidado.getExpedienteDetalle());
									internalFields.add(newField);
								}
							}
						} else {
							if (valueOfFieldsToResult == null)
								valueOfFieldsToResult = new ArrayList<PedidoVentaCaracteristicaDTO>();
							valueOfFieldsToResult.add(iField);
						}
					}
					// break; //Lo reitre para que valide todos los campos
				}
			}
		}
		if (internalFields != null) {
			// Esto me toco hacerlo porque se descuadranban los array al remove la relacion
			List<RelacionInternaDTO> unrepeatRelations = new ArrayList<RelacionInternaDTO>();
			unrepeatRelations.addAll(relations);
			for (RelacionInternaDTO iRelacion : validateRelations) {
				unrepeatRelations.remove(iRelacion);
			}
			internalFields = fieldService.listar2getMessageMailDestiny(internalFields, unrepeatRelations);
			List<PedidoVentaCaracteristicaDTO> mailInternal = searchFieldText(unrepeatRelations, internalFields);
			if (mailInternal != null) {
				if (valueOfFieldsToResult == null)
					valueOfFieldsToResult = new ArrayList<PedidoVentaCaracteristicaDTO>();
				valueOfFieldsToResult.addAll(mailInternal);
			}
		}
		return valueOfFieldsToResult;
	}
	
}
