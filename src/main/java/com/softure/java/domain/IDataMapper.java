package com.softure.java.domain;

import java.util.List;


public interface IDataMapper<T extends IDataObject, TFilter extends IDataObjectFilter> {

	T insert(T dto);
	
	int delete(T dto);

	T update(T dto);
	
	int count(TFilter dto);
	
	T selectOne(TFilter dto);
	
	T selectById(String Id);
	
	List<T> selectMany(TFilter dto);
	
	T selectOnSimple(TFilter dto);
	
	List<T> selectManySimple(TFilter dto);
}
