package com.learning.helpcenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learning.helpcenter.application.GetArticleService;
import com.learning.helpcenter.domain.ArticleDTO;
import com.shared.domain.ServerException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/help")
public class ArticleRest {
	
	@Autowired GetArticleService getArticleService;
	
	@GetMapping(value = "/article")
	public ArticleDTO getArticle(@RequestHeader("Authorization") String token, @RequestParam String type, @RequestParam String id)
			throws ServerException {
		return getArticleService.call(id, type, token);
	}
	
}
