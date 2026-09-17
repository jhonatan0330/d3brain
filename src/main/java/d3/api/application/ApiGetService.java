package d3.api.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.api.domain.DocumentFilterRequest;
import d3.api.domain.DocumentResponse;
import d3.api.domain.FieldRequest;
import d3.document.application.CallDocumentListWithFilters;
import d3.document.application.PedidoVentaCaracteristicaSvc;
import d3.document.domain.PedidoVentaCaracteristicaFilterDTO;
import d3.document.domain.PedidoVentaDTO;
import d3.document.domain.PedidoVentaFilterDTO;
import d3.process.application.CallSearchProcessFromText;
import d3.process.application.DocumentoPlantillaSvc;
import d3.process.application.ProcesoEstadoSvc;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.process.domain.DocumentoPlantillaDTO;
import d3.process.domain.ProcesoEstadoDTO;
import d3.process.domain.ProcesoEstadoFilterDTO;
import d3.process.domain.TemplateDTO;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;

@Service
public class ApiGetService {

	private final CallDocumentListWithFilters listService;
	private final CallSearchProcessFromText searchProcessFromText;
	private final DocumentoPlantillaSvc templateService;
	private final PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService;
	private final ProcesoEstadoSvc stateService;

	public ApiGetService(@Lazy CallDocumentListWithFilters listService,
			@Lazy CallSearchProcessFromText searchProcessFromText, @Lazy DocumentoPlantillaSvc templateService,
			@Lazy PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService, @Lazy ProcesoEstadoSvc stateService) {
		this.listService = listService;
		this.searchProcessFromText = searchProcessFromText;
		this.templateService = templateService;
		this.pedidoVentaCaracteristicaService = pedidoVentaCaracteristicaService;
		this.stateService = stateService;
	}

	public List<DocumentResponse> call(DocumentFilterRequest filter) throws ServerException {

		// if (token == null || token.isEmpty())
		// throw new ServerException("Es obligatorio enviar un token valido");
		if (filter == null)
			throw new ServerException("Por el momento es necesario que envies el nodo de document :( ");
		DocumentoPlantillaDTO pTemplate = templateService.consultarPorCodigo(filter.getTemplate());
		if (pTemplate == null)
			throw new ServerException("No se encontro una plantilla con el codigo " + filter.getTemplate());
		TemplateDTO templateBD = TemplateDTO.fromDocumentoPlantilla(pTemplate);
		templateBD = templateService.obtenerCampos(templateBD, true);
		PedidoVentaFilterDTO filterDTO = new PedidoVentaFilterDTO();
		if (filter.getId() == null) {
			filterDTO.setPlantilla(templateBD.getLlaveTabla());
			filterDTO.setNombre(filter.getCode());
			if (filter.getActive() == null)
				filter.setActive(SharedConstants.STATE_ACTIVE);
			filterDTO.setEstado(filter.getActive());
			filterDTO.setPaginacionRegistroInicial(filter.getPage() * filter.getSize());
			filterDTO.setPaginacionRegistroFinal((filter.getPage() + 1) * filter.getSize());
			filterDTO.setFechaMin(filter.getDateMin());
			filterDTO.setFechaMax(filter.getDateMax());
			filterDTO.setFiltroParametro(filter.getFilterText());
			filterDTO.setFechaRegistroMin(filter.getCreationDateMin());
			filterDTO.setFechaRegistroMax(filter.getCreationDateMax());
			if (filter.getStates() != null && !filter.getStates().isEmpty()) {
				filterDTO.setEstadoExpediente("");
				for (String _iState : filter.getStates()) {
					ProcesoEstadoFilterDTO pesFilter = new ProcesoEstadoFilterDTO();
					pesFilter.setProceso(templateBD.getProceso());
					pesFilter.setCodigo(_iState);
					pesFilter.setEstado(SharedConstants.STATE_ACTIVE);
					ProcesoEstadoDTO _state = stateService.consultaUnica(pesFilter);
					if (_state != null)
						filterDTO.setEstadoExpediente(filterDTO.getEstadoExpediente() + ";" + _state.getLlaveTabla());
				}
				if (filterDTO.getEstadoExpediente().isEmpty())
					filterDTO.setEstadoExpediente(null);
			}
			if (filter.getFilters() != null && !filter.getFilters().isEmpty()) {
				filterDTO.setFiltersByFields(new ArrayList<>());
				for (FieldRequest iField : filter.getFilters()) {
					PedidoVentaCaracteristicaFilterDTO fieldValueToAdd = getFieldValue(iField, templateBD);
					if (fieldValueToAdd == null)
						throw new ServerException("Estas usando un filtro por el campo con codigo " + iField.getField()
								+ " pero este campo no hace parte de la plantilla de documentos "
								+ templateBD.getNombre());
					filterDTO.getFiltersByFields().add(fieldValueToAdd);
				}
			}
		} else {
			filterDTO.setLlaveTabla(filter.getId());
		}
		List<PedidoVentaDTO> results = listService.listarAvanzado(filterDTO);
		return ApiCommon.transformPedidoVentaToDocument(pedidoVentaCaracteristicaService, results, templateBD);
	}

	private PedidoVentaCaracteristicaFilterDTO getFieldValue(FieldRequest fieldRequest, TemplateDTO template)
			throws ServerException {
		if (fieldRequest.getField() == null || fieldRequest.getField().isEmpty())
			throw new ServerException("Existe un campo sin Field");
		if (fieldRequest.getValue() == null || fieldRequest.getValue().isEmpty())
			throw new ServerException("El campo " + fieldRequest.getField() + "no tienen value");
		for (DocumentoPlantillaCaracteristicaDTO fieldTemplate : template.getCaracteristicas()) {
			if (fieldRequest.getField().compareTo(fieldTemplate.getCodigo()) == 0) {
				PedidoVentaCaracteristicaFilterDTO result = new PedidoVentaCaracteristicaFilterDTO();
				result.setCampoDTO(fieldTemplate);
				result.setCampo(fieldTemplate.getLlaveTabla());
				if (fieldTemplate.getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.PROCESO) == 0)
					result.setValorOpcion(
							searchProcessFromText.getValueOptionFromText(fieldRequest.getValue(), fieldTemplate));
				return result;
			}
		}
		return null;
	}

}
