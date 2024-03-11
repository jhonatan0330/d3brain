package com.learning.helpcenter.application;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.helpcenter.application.base.ArticleService;
import com.learning.helpcenter.domain.ArticleDTO;
import com.learning.helpcenter.domain.ArticleFilterDTO;
import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.document_execution.application.CallDocumentCRUD;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

@Service("GetArticleLearningService")
public class GetArticleService {

	@Autowired ArticleService articleService;
	@Autowired
	private CallDocumentCRUD crudService;
	@Autowired
	private DocumentoPlantillaSvc templateService;
	
	public ArticleDTO call(String entityId, String entityType, String token)
			throws ServerException {
	
		ArticleDTO article = validateIfExistArticle(entityId, entityType);
		if(article!=null) return article;
		
		DocumentoPlantillaDTO templateArticles = templateService.getTemplateConfiguration("ARTICLE", token);
		if(templateArticles==null) throw new ServerException("Comunicate con tu desarrollador necesitas una plantilla para los articulos");
		
		article = getArticleFromObject(entityId, entityType);
		if(article==null) throw new ServerException("No se encontro un objeto con ese identificador activo");
		
		return createNewArticle(article, templateArticles, token);
		
	}
	
	private ArticleDTO validateIfExistArticle(String entityId, String entityType) throws ServerException {
		ArticleFilterDTO filter = new ArticleFilterDTO();
		filter.setState(SharedConstants.STATE_ACTIVE);
		filter.setEntityType(entityType);
		filter.setEntity(entityId);
		return articleService.getOne(filter);
	}
	
	private ArticleDTO getArticleFromObject(String entityId, String entityType) throws ServerException {
		ArticleDTO article = new ArticleDTO();
		article.setEntity(entityId);
		article.setEntityType(entityType);
		switch (entityType.toUpperCase()) {
		case PropiedadValorDefinidoDTO.PLANTILLA: {
			DocumentoPlantillaDTO template = templateService.consultaXId(entityId);
			if(template == null) throw new ServerException("No existe una plantilla con este identificador");
			article.setTitle(template.getNombre());
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + entityType);
		}
		return article;
	}

	private ArticleDTO createNewArticle(ArticleDTO article, DocumentoPlantillaDTO template, String token) throws ServerException {
		PedidoVentaDTO document = new PedidoVentaDTO();
		document.setPlantilla(template.getLlaveTabla());
		document.setCaracteristicas(new ArrayList<>());
		for (DocumentoPlantillaCaracteristicaDTO iCampo : template.getCaracteristicas()) {
			PedidoVentaCaracteristicaDTO nueva = new PedidoVentaCaracteristicaDTO();
			nueva.setCampo(iCampo.getLlaveTabla());
			document.getCaracteristicas().add(nueva);
		}
		document.setFuncionario(templateService.getUserFlex(token));
		document = crudService.saveWithoutTransaction(document, token, true);
		article.setDocument(document.getLlaveTabla());
		articleService.save(article);
		return articleService.getById(article.getKey());
	}


}