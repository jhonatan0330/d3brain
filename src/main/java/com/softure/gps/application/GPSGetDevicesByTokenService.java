package com.softure.gps.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.gps.domain.GPSDispositivoDTO;
import com.softure.gps.domain.GPSDispositivoFilterDTO;

@Service
public class GPSGetDevicesByTokenService {

	@Autowired @Lazy  private GPSDispositivoSvc deviceService;
	
	public GPSDispositivoDTO call(String token) throws ServerException {
		GPSDispositivoFilterDTO filtro = new GPSDispositivoFilterDTO();
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		filtro.setUsuario(deviceService.getUserFlex(token));
		List<GPSDispositivoDTO> dispositivos = deviceService.listarConsulta(filtro);
		if(dispositivos==null || dispositivos.isEmpty()) return null;
		if(dispositivos.size()> 1) throw new ServerException("Muchos dispoistivos");
		return dispositivos.get(0);
	}
	
}
