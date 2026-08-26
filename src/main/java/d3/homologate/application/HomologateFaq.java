package d3.homologate.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import d3.shared.domain.ServerException;
import d3.document_execution.application.field.Propiedades;
import d3.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import d3.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.property.application.PropiedadSvc;
import d3.property.domain.PropiedadValorDefinidoDTO;

@Component
public class HomologateFaq {

	public void createFaqFields(String templateId, String token, DocumentoPlantillaCaracteristicaSvc campoService,
			PropiedadSvc propertyService) throws ServerException {
		List<String> fieldsTemplate = new ArrayList<>();
		// Crear el campo Introduccion
		fieldsTemplate.add(
				campoService.createField(templateId, "PREGUNTA", DocumentoPlantillaCaracteristicaDTO.TEXTO, 1, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(0), Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA,
				templateId, Propiedades.DESCRIPCION, fieldsTemplate.get(0), token), token);

		// Crear el campo Ayudas
		fieldsTemplate.add(
				campoService.createField(templateId, "IMAGEN", DocumentoPlantillaCaracteristicaDTO.ARCHIVO, 2, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(1), Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(1), Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
	}
}
