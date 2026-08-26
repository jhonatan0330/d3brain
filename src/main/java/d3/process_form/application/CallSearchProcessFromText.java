package d3.process_form.application;

import java.util.List;

import org.springframework.stereotype.Service;

import d3.shared.domain.SharedConstants;
import d3.shared.domain.ServerException;
import d3.document_execution.application.CallDocumentListFromFieldProcess;
import d3.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import d3.document_execution.domain.PedidoVentaDTO;
import d3.java.services.D3Utils;
import d3.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import org.springframework.context.annotation.Lazy;

@Service
public class CallSearchProcessFromText {

	private final CallDocumentListFromFieldProcess listDocumentFromFieldProcessFunction;

	public CallSearchProcessFromText(@Lazy CallDocumentListFromFieldProcess listDocumentFromFieldProcessFunction) {
		this.listDocumentFromFieldProcessFunction = listDocumentFromFieldProcessFunction;
	}

	public String getValueOptionFromText(String token, String valueText,
			DocumentoPlantillaCaracteristicaDTO fieldTemplate) throws ServerException {
		String key = findOptionFromText(token, valueText, fieldTemplate);
		if (key == null)
			throw new ServerException(
					"Revisando el campo " + fieldTemplate.getNombre() + " No se encuentra el documento con codigo : "
							+ valueText + "\nRevisa que el usuario tenga permiso de visualizar el documento");
		return key;
	}

	public String findOptionFromText(String token, String valueText, DocumentoPlantillaCaracteristicaDTO fieldTemplate)
			throws ServerException {
		PedidoVentaCaracteristicaFilterDTO filter = new PedidoVentaCaracteristicaFilterDTO();
		filter.setCampo(fieldTemplate.getLlaveTabla());
		filter.setCampoDTO(fieldTemplate);
		filter.setSecurityToken(token);
		filter.setFiltroParametro(valueText);
		PedidoVentaCaracteristicaFilterDTO resultField = listDocumentFromFieldProcessFunction.execute(filter,
				fieldTemplate);
		if (resultField == null || resultField.getCampoDTO() == null
				|| resultField.getCampoDTO().getDocumentos() == null
				|| resultField.getCampoDTO().getDocumentos().isEmpty())
			return null;

		String keyOfDocument = getDocumentFromManyResults(valueText, resultField.getCampoDTO().getDocumentos());
		if (keyOfDocument == null && resultField.getCampoDTO().getDocumentos().size() > 1)
			throw new ServerException("El campo " + fieldTemplate.getNombre() + " obtiene "
					+ resultField.getCampoDTO().getDocumentos().size() + " resultados que concuerdan con el criterio : "
					+ valueText + " y ninguno tiene el mismo nombre");
		return keyOfDocument;
	}

	public String getDocumentFromManyResults(String valueText, List<PedidoVentaDTO> documents) throws ServerException {

		String keyOfDocument = null;
		if (documents.size() > 1) {
			String textToCompare = null;
			// Esto es porque el fitro trae muchos resultados ejemplo busco el 60 y me trae
			// el 601
			for (PedidoVentaDTO pedidoVentaDTO : documents) {
				if (pedidoVentaDTO.getNombre().compareTo(valueText.toUpperCase()) == 0) {
					keyOfDocument = pedidoVentaDTO.getLlaveTabla();
					break;
				}
				if (pedidoVentaDTO.getTextoFiltro() == null) {
					textToCompare = SharedConstants.COMA + pedidoVentaDTO.getNombre() + SharedConstants.COMA;
				} else {
					textToCompare = SharedConstants.COMA + pedidoVentaDTO.getTextoFiltro();
				}
				if (D3Utils.formatFunction(textToCompare).toUpperCase().contains(SharedConstants.COMA
						+ D3Utils.formatFunction(valueText).toUpperCase() + SharedConstants.COMA)) {
					keyOfDocument = pedidoVentaDTO.getLlaveTabla();
					break;
				}
			}
		} else {
			keyOfDocument = documents.get(0).getLlaveTabla();
		}
		return keyOfDocument;
	}

}
