package com.softure.shared.infrastructure;

import java.util.List;

import com.softure.shared.domain.SharedDataObject;
import com.softure.shared.domain.SharedDataObjectFilter;


public interface SharedCRUDMapperMybatis<T extends SharedDataObject, TFilter extends SharedDataObjectFilter> {

	T insert(T dto);

	T update(T dto);
	
	int count(TFilter dto);
	
	T selectOne(TFilter dto);
	
	List<T> selectMany(TFilter dto);
	
	T selectOneSimple(TFilter dto);
	
	List<T> selectManySimple(TFilter dto);
}
