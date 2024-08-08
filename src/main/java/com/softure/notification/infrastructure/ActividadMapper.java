package com.softure.notification.infrastructure;


import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.notification.domain.ActividadDTO;
import com.softure.notification.domain.ActividadFilterDTO;

@SoftureSqlConnMapper(value = "ActividadMapper")
public interface ActividadMapper extends IBasicMapper<ActividadDTO, ActividadFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}