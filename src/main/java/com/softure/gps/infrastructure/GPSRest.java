package com.softure.gps.infrastructure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shared.domain.ServerException;
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


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/gps")
public class GPSRest {
	
	
	@Autowired @Lazy  private GPSLocalizacionSvc localizacionGPS;
	@Autowired @Lazy  private GPSGetDevicesByQueryService getDevicesByQueryService;
	@Autowired @Lazy  private GPSGetDevicesByTokenService getDevicesByTokenService;
	@Autowired @Lazy  private GPSEnrollDeviceService enrollDeviceService;
	@Autowired @Lazy  private GPSReportLocationsService reportLocationsService;
	
	@PostMapping(value="/enroll-device")
	public SharedIdResponse enroll(@RequestHeader("Authorization") String token, @RequestBody DeviceVO device)  throws ServerException  {
		return enrollDeviceService.call(token);
	}
	
	@GetMapping(value="/getGPS")
	public GPSDispositivoDTO getGPS(@RequestHeader("Authorization") String token)  throws ServerException  {
		return getDevicesByTokenService.call(token);
	}
	
	@GetMapping(value="/get-device")
	public List<GPSDispositivoDTO> getdevicesByQuery(@RequestHeader("Authorization") String token)  throws ServerException  {
		return getDevicesByQueryService.call(null);
	}
	
	@GetMapping(value="/get-device/{query}")
	public List<GPSDispositivoDTO> getdevicesByQuery(@RequestHeader("Authorization") String token, @PathVariable(name= "query" ,required = false)String pQuery)  throws ServerException  {
		return getDevicesByQueryService.call(pQuery);
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
