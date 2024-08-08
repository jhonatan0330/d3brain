package com.softure.api.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.api.domain.DataFieldRequest;
import com.softure.api.domain.DataFieldResponse;
import com.softure.api.domain.DocumentResponse;
import com.softure.api.domain.FieldRequest;
import com.softure.document_execution.application.DetallePedidoVentaSvc;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import com.softure.inventory.application.ProductoSvc;
import com.softure.java.services.SoftureUtil;
import com.softure.process_form.application.CallSearchProcessFromText;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;

@Service
public class ApiGetFieldDataService {

	@Autowired @Lazy 
	private DocumentoPlantillaSvc templateService;
	@Autowired @Lazy 
	private PedidoVentaCaracteristicaSvc fieldService;

	@Autowired @Lazy 
	private DetallePedidoVentaSvc detallePedidoVentaService;
	@Autowired @Lazy 
	private ProductoSvc productoService;
	@Autowired @Lazy 
	private CallSearchProcessFromText searchProcessFromText;

	public DataFieldResponse call(String token, DataFieldRequest filter) throws ServerException {
		validateFilter(token, filter);
		DocumentoPlantillaDTO templateBD = findTemplate(filter.getTemplate(), token);
		DocumentoPlantillaCaracteristicaDTO fieldBD = findField(filter.getCode(), templateBD);

		PedidoVentaCaracteristicaFilterDTO fieldFilter = new PedidoVentaCaracteristicaFilterDTO();
		fieldFilter.setCampo(fieldBD.getLlaveTabla());
		fieldFilter.setCampoDTO(fieldBD);
		if (filter.getPreconditions() != null && !filter.getPreconditions().isEmpty()) {
			fieldFilter.setDependientes(new ArrayList<>());
			for (FieldRequest iPrecondition : filter.getPreconditions()) {
				DocumentoPlantillaCaracteristicaDTO fieldDependent = findField(iPrecondition.getField(), templateBD);
				PedidoVentaCaracteristicaDTO dependent = new PedidoVentaCaracteristicaDTO();
				dependent.setCampo(fieldDependent.getLlaveTabla());
				dependent.setCampoDTO(fieldDependent);
				ApiCommon.chooseValueToField(
						iPrecondition, dependent,
						productoService, detallePedidoVentaService);
				if (fieldDependent.getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.PROCESO) == 0)
					dependent.setValorOpcion(searchProcessFromText.getValueOptionFromText(token,iPrecondition.getValue(), fieldDependent));
				fieldFilter.getDependientes().add(dependent);
			}
		}
		fieldFilter.setSecurityToken(token);
		PedidoVentaCaracteristicaDTO fieldData = fieldService.completarDatosBase(fieldFilter);
		DataFieldResponse result = new DataFieldResponse();
		result.setField(fieldData.getCampoDTO().getCodigo());
		result.setInternalId(fieldData.getValorOpcion());
		result.setValue(fieldData.getValorText());
		if(result.getValue()==null && fieldData.getValorNumero()!=null ) result.setValue(SoftureUtil.formatNumber(fieldData.getValorNumero()));
		List<DocumentResponse> docs = new ArrayList<>();
		if(fieldData.getCampoDTO().getDocumentos()!=null && !fieldData.getCampoDTO().getDocumentos().isEmpty()) {
			//Para obtener los puestos de un pasaje no se llenaba plantilla
			DocumentoPlantillaDTO templateList = null;
			if(fieldData.getCampoDTO().getDocumentos().get(0).getPlantilla()!=null) {
				templateList = templateService.consultaXId(fieldData.getCampoDTO().getDocumentos().get(0).getPlantilla());
				templateList = templateService.obtenerCampos(templateList, token);	
			}
			docs = ApiCommon
			.transformPedidoVentaToDocument(token, fieldService, fieldData.getCampoDTO().getDocumentos(), templateList);
		}
		result.setDocuments(docs);
		return result;
	}

	private DocumentoPlantillaCaracteristicaDTO findField(String code, DocumentoPlantillaDTO templateBD)
			throws ServerException {
		if (templateBD.getCaracteristicas() != null && !templateBD.getCaracteristicas().isEmpty()) {
			for (DocumentoPlantillaCaracteristicaDTO iField : templateBD.getCaracteristicas()) {
				if (iField.getCodigo().compareTo(code) == 0)
					return iField;
			}
		}
		throw new ServerException(
				"No se identifica un campo de codigo " + code + " en la plantilla " + templateBD.getNombre());
	}

	private void validateFilter(String token, DataFieldRequest filter) throws ServerException {
		if (token == null || token.isEmpty())
			throw new ServerException("Es obligatorio enviar un token valido");
		if (filter == null)
			throw new ServerException("Revisa el Body del request esta llegando vacio");
		if (filter.getTemplate() == null || filter.getTemplate().isEmpty())
			throw new ServerException("El codigo de la plantilla es null, recuerda usar el campo template");
		if (filter.getCode() == null || filter.getCode().isEmpty())
			throw new ServerException(
					"El codigo del campo es null, recuerda usar el campo code para colocar el codigo del campo a consultar");
	}

	private DocumentoPlantillaDTO findTemplate(String template, String token) throws ServerException {
		DocumentoPlantillaDTO templateDTO = templateService.consultarPorCodigo(template);
		if (templateDTO == null)
			throw new ServerException("La plantilla no se encuentra por el codigo " + template);
		return templateService.obtenerCampos(templateDTO, token);
	}

}
