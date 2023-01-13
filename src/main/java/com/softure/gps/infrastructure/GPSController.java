package com.softure.gps.infrastructure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softure.gps.application.GPSDispositivoSvc;
import com.softure.gps.application.GPSLocalizacionSvc;
import com.softure.gps.domain.GPSDispositivoDTO;
import com.softure.gps.domain.GPSLocalizacionDTO;
import com.softure.gps.domain.GPSLocalizacionFilterDTO;
import com.softure.java.dto.exception.ServerException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/gps")
public class GPSController {
	
	@Autowired private GPSDispositivoSvc dispositivosGPS;
	@Autowired private GPSLocalizacionSvc localizacionGPS;
	
	@GetMapping(value="/getGPS")
	public GPSDispositivoDTO getGPS(@RequestHeader("Authorization") String token)  throws ServerException  {
		return dispositivosGPS.getGPSFromToken(token);
	}
	
	@PostMapping(value="/getGPSLocation")
	public List<GPSLocalizacionDTO> getGPSLocation(@RequestHeader("Authorization") String token, @RequestBody GPSLocalizacionFilterDTO location)  throws ServerException  {
		return 	localizacionGPS.listarConsulta(location);
	}
	
	@RequestMapping(value="/saveGPSLocation", method=RequestMethod.POST)
	public GPSLocalizacionDTO saveGPSLocation(@RequestHeader("Authorization") String token, @RequestBody GPSLocalizacionDTO location)  throws ServerException  {
		return localizacionGPS.save(location);
	}

	@RequestMapping(value="/getGPSDocument", method=RequestMethod.POST)
	public List<GPSLocalizacionDTO> getGPSDocument(@RequestHeader("Authorization") String token, @RequestBody GPSLocalizacionFilterDTO location)  throws ServerException  {
		return 	localizacionGPS.listarDocumento(location);
	}
}
