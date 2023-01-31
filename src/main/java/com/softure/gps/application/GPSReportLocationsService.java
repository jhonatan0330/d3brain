package com.softure.gps.application;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softure.gps.domain.GPSDispositivoDTO;
import com.softure.gps.domain.GPSEnrollDeviceService;
import com.softure.gps.domain.GPSLocalizacionDTO;
import com.softure.java.domain.IdResponse;
import com.softure.java.dto.exception.ServerException;

@Service
public class GPSReportLocationsService {
	
	@Autowired private GPSGetDevicesByTokenService getDevicesByTokenService;
	@Autowired private GPSLocalizacionSvc locationService;
	@Autowired private GPSEnrollDeviceService enrollDeviceService;

	@Transactional
	public IdResponse call(String token, List<GPSLocalizacionDTO> locations) throws ServerException {
		GPSDispositivoDTO device = getDevicesByTokenService.call(token);
		//if(name ==null || name.isEmpty()) throw new ServerException("El identificador del dispositivo viene vacio");
		if(device == null) {
			device = new GPSDispositivoDTO();
			device.setLlaveTabla( enrollDeviceService.call(token).getId());
		}
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
	
	public void callByForm(String token, String location, String documento, String codigo) throws ServerException {
		GPSDispositivoDTO device = getDevicesByTokenService.call(token);
		//if(name ==null || name.isEmpty()) throw new ServerException("El identificador del dispositivo viene vacio");
		if(device == null) return;
		GPSLocalizacionDTO gpsLocalizacionDTO = new GPSLocalizacionDTO();
		gpsLocalizacionDTO.setDispositivo(device.getLlaveTabla());
		gpsLocalizacionDTO.setDocumento(documento);
		gpsLocalizacionDTO.setCodigo(codigo);
		gpsLocalizacionDTO.setFecha(new Date());
		Pattern pat = Pattern.compile("^([-+])?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*([-+])?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$");
	    Matcher mat = pat.matcher(location);                                                                           
	    if (!mat.matches()) return;
	    
		gpsLocalizacionDTO.setLatitud(new BigDecimal(mat.group(2)));
		if(mat.group(1)!=null)gpsLocalizacionDTO.setLatitud(gpsLocalizacionDTO.getLatitud().negate());
		gpsLocalizacionDTO.setLongitud(new BigDecimal(mat.group(6)));
		if(mat.group(5)!=null)gpsLocalizacionDTO.setLongitud(gpsLocalizacionDTO.getLongitud().negate());
		locationService.save(gpsLocalizacionDTO);
		
	}
}
