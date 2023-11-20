package com.softure.process_form.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.document_execution.application.CallDocumentListFromFieldProcess;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;

@Service
public class CallSearchProcessFromText {

	@Autowired
	private CallDocumentListFromFieldProcess listDocumentFromFieldProcessFunction;

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

		String keyOfDocument = null;
		if (resultField.getCampoDTO().getDocumentos().size() > 1) {
			String textToCompare = null;
			// Esto es porque el fitro trae muchos resultados ejemplo busco el 60 y me trae
			// el 601
			for (PedidoVentaDTO pedidoVentaDTO : resultField.getCampoDTO().getDocumentos()) {
				if (pedidoVentaDTO.getNombre().compareTo(valueText.toUpperCase()) == 0) {
					keyOfDocument = pedidoVentaDTO.getLlaveTabla();
					break;
				}
				if (pedidoVentaDTO.getTextoFiltro() == null) {
					textToCompare = ConstantesGenerales.COMA + pedidoVentaDTO.getNombre() + ConstantesGenerales.COMA;
				} else {
					textToCompare = ConstantesGenerales.COMA + pedidoVentaDTO.getTextoFiltro();
				}
				if (textToCompare
						.contains(ConstantesGenerales.COMA + valueText.toUpperCase() + ConstantesGenerales.COMA)) {
					keyOfDocument = pedidoVentaDTO.getLlaveTabla();
					break;
				}

			}
		} else {
			keyOfDocument = resultField.getCampoDTO().getDocumentos().get(0).getLlaveTabla();
		}
		if (keyOfDocument == null && resultField.getCampoDTO().getDocumentos().size() > 1)
			throw new ServerException("El campo " + fieldTemplate.getNombre() + " obtiene "
					+ resultField.getCampoDTO().getDocumentos().size() + " resultados que concuerdan con el criterio : "
					+ valueText + " y ninguno tiene el mismo nombre");
		return keyOfDocument;
	}

}
