package d3.shared.infrastructure;

import java.util.List;

import d3.shared.domain.SharedDataObject;
import d3.shared.domain.SharedDataObjectFilter;

public interface SharedCRUDMapperMybatis<T extends SharedDataObject, TFilter extends SharedDataObjectFilter> {

	T insert(T dto);

	T update(T dto);

	int count(TFilter dto);

	T selectOne(TFilter dto);

	List<T> selectMany(TFilter dto);

}
