package com.softure.gps.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.gps.domain.GPSDispositivoDTO;
import com.softure.gps.domain.GPSDispositivoFilterDTO;
import org.springframework.context.annotation.Lazy;

@Service
public class GPSGetDevicesByQueryService {

	private final GPSDispositivoSvc deviceService;

	public GPSGetDevicesByQueryService(@Lazy GPSDispositivoSvc deviceService) {
		this.deviceService = deviceService;
	}

	public List<GPSDispositivoDTO> call(String query) throws ServerException {
		GPSDispositivoFilterDTO filter = new GPSDispositivoFilterDTO();
		filter.setNombre(query);
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		return deviceService.listarConsulta(filter);
	}
}
