package d3.property.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import d3.shared.domain.SharedConstants;
import d3.shared.domain.ServerException;
import d3.document.application.DocumentoRelacionExpedienteSvc;
import d3.document.application.PedidoVentaCaracteristicaSvc;
import d3.document.application.field.Propiedades;
import d3.document.domain.DocumentoRelacionExpedienteDTO;
import d3.document.domain.DocumentoRelacionExpedienteFilterDTO;
import d3.document.domain.PedidoVentaCaracteristicaDTO;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.property.domain.RelacionInternaDTO;
import org.springframework.context.annotation.Lazy;

@Service
public class PropertyNavigateIntoRelationsToFindFieldsService {

	private final DocumentoRelacionExpedienteSvc documentRelationService;
	private final RelacionInternaSvc relationService;
	private final PedidoVentaCaracteristicaSvc fieldService;

	public PropertyNavigateIntoRelationsToFindFieldsService(
			@Lazy DocumentoRelacionExpedienteSvc documentRelationService, @Lazy RelacionInternaSvc relationService,
			@Lazy PedidoVentaCaracteristicaSvc fieldService) {
		this.documentRelationService = documentRelationService;
		this.relationService = relationService;
		this.fieldService = fieldService;
	}

	public List<PedidoVentaCaracteristicaDTO> call(String propertyId, List<PedidoVentaCaracteristicaDTO> fields)
			throws ServerException {
		List<RelacionInternaDTO> relations = relationService.relacionesPropiedad(propertyId);
		return searchFieldText(relations, fields);
	}

	private List<PedidoVentaCaracteristicaDTO> searchFieldText(List<RelacionInternaDTO> relations,
			List<PedidoVentaCaracteristicaDTO> fields) throws ServerException {
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
							relacionExpedienteFilter.setEstado(SharedConstants.STATE_ACTIVE);
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
