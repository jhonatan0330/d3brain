package d3.configuration.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.document.application.field.Propiedades;
import d3.process.application.DocumentoPlantillaCaracteristicaSvc;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.shared.domain.ServerException;

@Component
public class HomologateFaq {

	public void createFaqFields(String templateId, DocumentoPlantillaCaracteristicaSvc campoService,
			PropiedadSvc propertyService) throws ServerException {
		List<String> fieldsTemplate = new ArrayList<>();
		// Crear el campo Introduccion
		fieldsTemplate
				.add(campoService.createField(templateId, "PREGUNTA", DocumentoPlantillaCaracteristicaDTO.TEXTO, 1));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(0), Propiedades.PERMISO_CAMPO_MODIFICABLE, "1"));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA,
				templateId, Propiedades.DESCRIPCION, fieldsTemplate.get(0)));

		// Crear el campo Ayudas
		fieldsTemplate
				.add(campoService.createField(templateId, "IMAGEN", DocumentoPlantillaCaracteristicaDTO.ARCHIVO, 2));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(1), Propiedades.PERMISO_CAMPO_MODIFICABLE, "1"));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				fieldsTemplate.get(1), Propiedades.PERMISO_CAMPO_OPCIONAL, "1"));
	}
}
