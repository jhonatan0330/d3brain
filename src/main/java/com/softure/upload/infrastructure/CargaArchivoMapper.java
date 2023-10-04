package com.softure.upload.infrastructure;


import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.upload.domain.CargaArchivoDTO;
import com.softure.upload.domain.CargaArchivoFilterDTO;

@SoftureSqlConnMapper("CargaArchivoMapper")
public interface CargaArchivoMapper extends IBasicMapper<CargaArchivoDTO, CargaArchivoFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}