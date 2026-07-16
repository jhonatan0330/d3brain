package com.shared.infrastructure;

import java.util.List;

import com.shared.domain.SharedDataObject;
import com.shared.domain.SharedDataObjectFilter;

public interface SharedCRUDMapperMybatis<T extends SharedDataObject, TFilter extends SharedDataObjectFilter> {

	T insert(T dto);

	T update(T dto);

	int count(TFilter dto);

	T selectOne(TFilter dto);

	List<T> selectMany(TFilter dto);

}
