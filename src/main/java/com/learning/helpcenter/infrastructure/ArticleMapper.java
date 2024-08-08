package com.learning.helpcenter.infrastructure;

import java.util.List;

import com.learning.helpcenter.domain.ArticleDTO;
import com.learning.helpcenter.domain.ArticleFilterDTO;
import com.softure.SoftureSqlConnMapper;

@SoftureSqlConnMapper(value = "ArticleLearningMapper")
public interface ArticleMapper {

	ArticleDTO insert(ArticleDTO dto);

	ArticleDTO update(ArticleDTO dto);

	int count(ArticleFilterDTO filter);
	
	ArticleDTO getOne(ArticleFilterDTO filter);

	List<ArticleDTO> getMany(ArticleFilterDTO filter);

}