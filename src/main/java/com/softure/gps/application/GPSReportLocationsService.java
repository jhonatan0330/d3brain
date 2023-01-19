package com.softure.gps.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softure.gps.domain.GPSDispositivoDTO;
import com.softure.gps.domain.GPSLocalizacionDTO;
import com.softure.java.domain.IdResponse;
import com.softure.java.dto.exception.ServerException;

@Service
public class GPSReportLocationsService {
	
	@Autowired private GPSGetDevicesByTokenService getDevicesByTokenService;
	@Autowired private GPSLocalizacionSvc locationService;

	@Transactional
	public IdResponse call(String token, List<GPSLocalizacionDTO> locations) throws ServerException {
		GPSDispositivoDTO device = getDevicesByTokenService.call(token);
		//if(name ==null || name.isEmpty()) throw new ServerException("El identificador del dispositivo viene vacio");
		if(device == null) throw new ServerException("No se identifico el dispositivo por el token");
		for (GPSLocalizacionDTO gpsLocalizacionDTO : locations) {
			gpsLocalizacionDTO.setDispositivo(device.getLlaveTabla());
			gpsLocalizacionDTO.setDocumento(null);
			gpsLocalizacionDTO.setEstado(null);
			locationService.save(gpsLocalizacionDTO);
		}
		// No tengo nada que retornar pero para que no salga vacia
		// algun dia guardare las transacciones de envio
		return new IdResponse(device.getLlaveTabla());
	}
}
