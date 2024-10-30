package com.configuration.homologate.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.learning.helpcenter.application.base.ArticleService;
import com.learning.helpcenter.domain.ArticleDTO;
import com.learning.helpcenter.domain.ArticleFilterDTO;
import com.shared.domain.ServerException;
import com.softure.document_execution.application.CallDocumentCommons;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

@Component
public class HomologateArticle {
	
	@Autowired
	@Lazy
	private ArticleService articleService;

	public void updateArticle(PedidoVentaDTO document) throws ServerException {
		ArticleFilterDTO filter = new ArticleFilterDTO();
		filter.setDocument(document.getLlaveTabla());
		ArticleDTO updateArticle = articleService.getOne(filter);
		if (updateArticle == null)
			return;
		updateArticle.setIntroduction(CallDocumentCommons.getValueText(document, "INTRODUCCION"));
		updateArticle.setHelp(CallDocumentCommons.getValueText(document, "AYUDA_EXTRA"));
		updateArticle.setImage(CallDocumentCommons.getValueText(document, "IMAGEN"));
		articleService.update(updateArticle);
	}
	
	public void createArticleFields(String templateId, String token, DocumentoPlantillaCaracteristicaSvc campoService, PropiedadSvc propertyService) throws ServerException {
		List<String> fieldsTemplate = new ArrayList<>();

		// Crear el campo Introduccion
		fieldsTemplate.add(campoService.createField(templateId, "INTRODUCCION",
				DocumentoPlantillaCaracteristicaDTO.TEXTO, 1, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(0),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(0),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA, templateId,
				Propiedades.DESCRIPCION, fieldsTemplate.get(0), token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(0),
				Propiedades.TEXTO_LARGO, "1", token), token);

		// Crear el campo Ayudas
		fieldsTemplate.add(campoService.createField(templateId, "AYUDA EXTRA",
				DocumentoPlantillaCaracteristicaDTO.TEXTO, 2, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(1),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(1),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(1),
				Propiedades.TEXTO_LARGO, "1", token), token);

		// Crear el campo Ayudas
		fieldsTemplate.add(
				campoService.createField(templateId, "IMAGEN", DocumentoPlantillaCaracteristicaDTO.ARCHIVO, 3, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(2),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(2),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA, templateId,
				Propiedades.CAMPO_EVIDENCIA, fieldsTemplate.get(2), token), token);

	}
}
