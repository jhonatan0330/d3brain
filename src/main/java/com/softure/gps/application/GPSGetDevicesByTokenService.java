package com.softure.gps.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.gps.domain.GPSDispositivoDTO;
import com.softure.gps.domain.GPSDispositivoFilterDTO;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;

@Service
public class GPSGetDevicesByTokenService {

	@Autowired private GPSDispositivoSvc deviceService;
	
	public GPSDispositivoDTO call(String token) throws ServerException {
		GPSDispositivoFilterDTO filtro = new GPSDispositivoFilterDTO();
		filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		filtro.setUsuario(deviceService.getUserFlex(token));
		List<GPSDispositivoDTO> dispositivos = deviceService.listarConsulta(filtro);
		if(dispositivos==null || dispositivos.isEmpty()) return null;
		if(dispositivos.size()> 1) throw new ServerException("Muchos dispoistivos");
		return dispositivos.get(0);
	}
	
}
