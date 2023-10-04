package com.softure.gps.infrastructure;


import java.util.List;

import com.softure.SoftureSqlConnMapper;
import com.softure.gps.domain.GPSLocalizacionDTO;
import com.softure.gps.domain.GPSLocalizacionFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper("GPSLocalizacionMapper")
public interface GPSLocalizacionMapper extends IBasicMapper<GPSLocalizacionDTO, GPSLocalizacionFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<GPSLocalizacionDTO> listarFullByDay(GPSLocalizacionFilterDTO dto);
// END region aditionalMethods
}