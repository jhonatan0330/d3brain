package com.softure.gps.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.gps.domain.GPSDispositivoDTO;
import com.softure.gps.domain.GPSDispositivoFilterDTO;

@Service
public class GPSGetDevicesByQueryService {

	@Autowired @Lazy  private GPSDispositivoSvc deviceService;
	
	public List<GPSDispositivoDTO> call(String query) throws ServerException {
		GPSDispositivoFilterDTO filter = new GPSDispositivoFilterDTO();
		filter.setNombre(query);
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		return deviceService.listarConsulta(filter);
	}
}
