package com.softure.gps.infrastructure;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shared.domain.SharedIdResponse;
import com.softure.gps.application.GPSEnrollDeviceService;
import com.softure.gps.application.GPSGetDevicesByQueryService;
import com.softure.gps.application.GPSGetDevicesByTokenService;
import com.softure.gps.application.GPSLocalizacionSvc;
import com.softure.gps.application.GPSReportLocationsService;
import com.softure.gps.domain.DeviceVO;
import com.softure.gps.domain.GPSDispositivoDTO;
import com.softure.gps.domain.GPSLocalizacionDTO;
import com.softure.gps.domain.GPSLocalizacionFilterDTO;
import com.softure.java.dto.exception.ServerException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/gps")
public class GPSRest {
	
	
	@Autowired private GPSLocalizacionSvc localizacionGPS;
	@Autowired private GPSGetDevicesByQueryService getDevicesByQueryService;
	@Autowired private GPSGetDevicesByTokenService getDevicesByTokenService;
	@Autowired private GPSEnrollDeviceService enrollDeviceService;
	@Autowired private GPSReportLocationsService reportLocationsService;
	
	@PostMapping(value="/enroll-device")
	public SharedIdResponse enroll(@RequestHeader("Authorization") String token, @RequestBody DeviceVO device)  throws ServerException  {
		return enrollDeviceService.call(token);
	}
	
	@GetMapping(value="/getGPS")
	public GPSDispositivoDTO getGPS(@RequestHeader("Authorization") String token)  throws ServerException  {
		return getDevicesByTokenService.call(token);
	}
	
	@GetMapping(value="/get-device/{query}")
	public List<GPSDispositivoDTO> getdevicesByQuery(@RequestHeader("Authorization") String token, @PathParam("query")String query)  throws ServerException  {
		return getDevicesByQueryService.call(query);
	}
	
	@PostMapping(value="/getGPSLocation")
	public List<GPSLocalizacionDTO> getGPSLocation(@RequestHeader("Authorization") String token, @RequestBody GPSLocalizacionFilterDTO location)  throws ServerException  {
		return 	localizacionGPS.listarFullByDay(location);
	}
	
	@PostMapping(value="/save-locations")
	public SharedIdResponse savesLocation(@RequestHeader("Authorization") String token, @RequestBody List<GPSLocalizacionDTO> locations)  throws ServerException  {
		return reportLocationsService.call(token, locations);
	}

}
