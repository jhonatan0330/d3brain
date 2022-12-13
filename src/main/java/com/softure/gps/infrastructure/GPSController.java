package com.softure.gps.infrastructure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softure.gps.application.GPSDispositivoSvc;
import com.softure.gps.application.GPSLocalizacionSvc;
import com.softure.gps.domain.GPSDispositivoDTO;
import com.softure.gps.domain.GPSDispositivoFilterDTO;
import com.softure.gps.domain.GPSLocalizacionDTO;
import com.softure.gps.domain.GPSLocalizacionFilterDTO;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.domain.UsuarioDTO;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/gps")
public class GPSController {
	
	@Autowired private GPSDispositivoSvc dispositivosGPS;
	@Autowired private GPSLocalizacionSvc localizacionGPS;
	
	@RequestMapping(value="/getGPS", method=RequestMethod.POST)
	public GPSDispositivoDTO getGPS(@RequestBody UsuarioDTO usuario)  throws ServerException  {
		GPSDispositivoFilterDTO filtro = new GPSDispositivoFilterDTO();
		filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		filtro.setUsuario(usuario.getLlaveTabla());
		List<GPSDispositivoDTO> dispositivos = dispositivosGPS.listarConsulta(filtro);
		if(dispositivos==null || dispositivos.isEmpty()) return null;
		if(dispositivos.size()> 1) throw new ServerException("Muchos dispoistivos");
		return 	dispositivos.get(0);
	}
	
	@RequestMapping(value="/getGPSLocation", method=RequestMethod.POST)
	public List<GPSLocalizacionDTO> getGPSLocation(@RequestBody GPSLocalizacionFilterDTO location)  throws ServerException  {
		return 	localizacionGPS.listarConsulta(location);
	}
	
	@RequestMapping(value="/saveGPSLocation", method=RequestMethod.POST)
	public GPSLocalizacionDTO saveGPSLocation(@RequestBody GPSLocalizacionDTO location)  throws ServerException  {
		return localizacionGPS.save(location);
	}

	@RequestMapping(value="/getGPSDocument", method=RequestMethod.POST)
	public List<GPSLocalizacionDTO> getGPSDocument(@RequestBody GPSLocalizacionFilterDTO location)  throws ServerException  {
		return 	localizacionGPS.listarDocumento(location);
	}
}
