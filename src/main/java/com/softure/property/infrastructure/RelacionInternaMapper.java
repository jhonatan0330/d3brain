package com.softure.property.infrastructure;

import java.util.List;

import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.property.domain.RelacionInternaDTO;
import com.softure.property.domain.RelacionInternaFilterDTO;

@SoftureSqlConnMapper(value = "RelacionInternaMapper")
public interface RelacionInternaMapper extends IBasicMapper<RelacionInternaDTO, RelacionInternaFilterDTO> {

	List<RelacionInternaDTO> getRelationsFullToSynchronize();

	String getTemplateOfField(String pFieldId);

	void updatePropertyRelations(String pProperty);
}