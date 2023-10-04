package com.softure.money.infrastructure;


import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.money.domain.TurnoDTO;
import com.softure.money.domain.TurnoFilterDTO;

@SoftureSqlConnMapper("TurnoMapper")
public interface TurnoMapper extends IBasicMapper<TurnoDTO, TurnoFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}