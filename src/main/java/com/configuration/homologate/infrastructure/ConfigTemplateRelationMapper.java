package com.configuration.homologate.infrastructure;

import java.util.List;

import com.configuration.homologate.domain.ConfigTemplateRelationDTO;
import com.configuration.homologate.domain.ConfigTemplateRelationFilterDTO;
import com.softure.SoftureSqlConnMapper;

@SoftureSqlConnMapper("ConfigTemplateRelationConfigurationMapper")
public interface ConfigTemplateRelationMapper {

	ConfigTemplateRelationDTO insert(ConfigTemplateRelationDTO dto);

	ConfigTemplateRelationDTO update(ConfigTemplateRelationDTO dto);

	int count(ConfigTemplateRelationFilterDTO filter);
	
	ConfigTemplateRelationDTO getOne(ConfigTemplateRelationFilterDTO filter);

	List<ConfigTemplateRelationDTO> getMany(ConfigTemplateRelationFilterDTO filter);

}