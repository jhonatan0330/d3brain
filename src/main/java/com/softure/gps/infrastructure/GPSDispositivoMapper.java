package com.softure.gps.infrastructure;

import com.softure.SoftureSqlConnMapper;
import com.softure.gps.domain.GPSDispositivoDTO;
import com.softure.gps.domain.GPSDispositivoFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper(value = "GPSDispositivoMapper")
public interface GPSDispositivoMapper extends IBasicMapper<GPSDispositivoDTO, GPSDispositivoFilterDTO> {

}