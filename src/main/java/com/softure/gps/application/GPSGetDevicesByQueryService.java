package com.softure.gps.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.gps.domain.GPSDispositivoDTO;
import com.softure.gps.domain.GPSDispositivoFilterDTO;
import com.softure.java.cons.ConstantesGenerales;

@Service
public class GPSGetDevicesByQueryService {

	@Autowired private GPSDispositivoSvc deviceService;
	
	public List<GPSDispositivoDTO> call(String query) throws ServerException {
		GPSDispositivoFilterDTO filter = new GPSDispositivoFilterDTO();
		filter.setNombre(query);
		filter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		return deviceService.listarConsulta(filter);
	}
}
