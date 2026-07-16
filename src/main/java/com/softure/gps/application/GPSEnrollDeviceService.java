package com.softure.gps.application;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.shared.domain.SharedIdResponse;
import com.softure.gps.domain.GPSDispositivoDTO;
import org.springframework.context.annotation.Lazy;

@Service
public class GPSEnrollDeviceService {

	private final GPSDispositivoSvc deviceService;
	private final GPSGetDevicesByTokenService getDevicesByTokenService;

	public GPSEnrollDeviceService(@Lazy GPSDispositivoSvc deviceService,
			@Lazy GPSGetDevicesByTokenService getDevicesByTokenService) {
		this.deviceService = deviceService;
		this.getDevicesByTokenService = getDevicesByTokenService;
	}

	public SharedIdResponse call(String token) throws ServerException {
		GPSDispositivoDTO newDTO = getDevicesByTokenService.call(token);
		// if(name ==null || name.isEmpty()) throw new ServerException("El identificador
		// del dispositivo viene vacio");
		if (newDTO != null)
			return new SharedIdResponse(newDTO.getLlaveTabla());
		newDTO = new GPSDispositivoDTO();
		newDTO.setUsuario(deviceService.getUserFlex(token));
		newDTO.setAcercamiento(15);
		newDTO.setDistancia(10);
		newDTO.setIntervalo(1);
		newDTO.setNombre(token);
		newDTO.setUltimaConexion(new Date());
		return new SharedIdResponse(deviceService.guardar(newDTO, token).getLlaveTabla());
	}

}
