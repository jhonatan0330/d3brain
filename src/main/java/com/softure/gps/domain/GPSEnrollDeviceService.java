package com.softure.gps.domain;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.gps.application.GPSDispositivoSvc;
import com.softure.gps.application.GPSGetDevicesByTokenService;
import com.softure.java.domain.IdResponse;
import com.softure.java.dto.exception.ServerException;

@Service
public class GPSEnrollDeviceService {

	@Autowired private GPSDispositivoSvc deviceService;
	@Autowired private GPSGetDevicesByTokenService getDevicesByTokenService;
	
	public IdResponse call(String token, String name) throws ServerException {
		GPSDispositivoDTO newDTO = getDevicesByTokenService.call(token);
		//if(name ==null || name.isEmpty()) throw new ServerException("El identificador del dispositivo viene vacio");
		if(newDTO != null) return  new IdResponse(newDTO.getLlaveTabla());
		newDTO = new GPSDispositivoDTO();
		newDTO.setUsuario(deviceService.getUserFlex(token));
		newDTO.setAcercamiento(15);
		newDTO.setDistancia(10);
		newDTO.setIntervalo(1);
		newDTO.setNombre(token);
		newDTO.setUltimaConexion(new Date());
		return new IdResponse( deviceService.guardar(newDTO, token).getLlaveTabla());
	}

}
