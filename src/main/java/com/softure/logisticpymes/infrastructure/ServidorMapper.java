package com.softure.logisticpymes.infrastructure;


import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.ServidorDTO;
import com.softure.logisticpymes.domain.ServidorFilterDTO;

@SoftureSqlConnMapper("ServidorMapper")
public interface ServidorMapper extends IBasicMapper<ServidorDTO, ServidorFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}