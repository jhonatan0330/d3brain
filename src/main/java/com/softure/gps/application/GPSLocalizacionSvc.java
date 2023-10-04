package com.softure.gps.application;

// BEGIN region interImport
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.gps.domain.GPSDispositivoDTO;
import com.softure.gps.domain.GPSLocalizacionDTO;
import com.softure.gps.domain.GPSLocalizacionFilterDTO;
import com.softure.gps.infrastructure.GPSLocalizacionMapper;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;

@Service("gPSLocalizacionService")
public class GPSLocalizacionSvc extends BasicSvc<GPSLocalizacionDTO, GPSLocalizacionFilterDTO> {
	
	@Autowired
	private GPSLocalizacionMapper gPSLocalizacionMapper;
	
	// BEGIN region servicesGPSLocalizacion
	@Autowired private GPSDispositivoSvc gpsDispositivoService;
	// END region servicesGPSLocalizacion

	@Override
	public GPSLocalizacionDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. GPSLocalizacion");
		GPSLocalizacionFilterDTO dto = new GPSLocalizacionFilterDTO();
		dto.setLlaveTabla(llave);
		return gPSLocalizacionMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = gPSLocalizacionMapper;
	}
	
	@Override
	public GPSLocalizacionDTO activar(GPSLocalizacionDTO dto, String token) throws ServerException {
		// BEGIN GPSLocalizacion_activar
		return super.activar(dto, token);
		// END GPSLocalizacion_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public GPSLocalizacionDTO actualizar( GPSLocalizacionDTO dto, String token) throws ServerException {
		// BEGIN GPSLocalizacion_actualizar
		return super.actualizar(dto, token);
		// END GPSLocalizacion_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public GPSLocalizacionDTO inactivar(GPSLocalizacionDTO dto, String token) throws ServerException {
		// BEGIN GPSLocalizacion_inactivar
		return super.inactivar(dto, token);
		// END GPSLocalizacion_inactivar
	}
	
	@Override
	public GPSLocalizacionDTO consultaUnica(GPSLocalizacionFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(GPSLocalizacionFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<GPSLocalizacionDTO> listarConsulta(GPSLocalizacionFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public GPSLocalizacionDTO guardar(GPSLocalizacionDTO dto, String token) throws ServerException {
		// BEGIN GPSLocalizacion_guardar
		if(dto==null) throw new ServerException("El objeto no puede ser vacio");
		if(dto.getFecha()==null) throw new ServerException("La ubicacion debe tener fecha");
		dto.setFechaReporte(new Date());
		GPSDispositivoDTO dispositivo = gpsDispositivoService.consultaXId(dto.getDispositivo());
		dispositivo.setUltimaConexion(dto.getFecha());
		gpsDispositivoService.save(dispositivo);
		dto = super.save(dto);
		return dto;
		// END GPSLocalizacion_guardar
	}

// BEGIN region aditionalMethods
	public List<GPSLocalizacionDTO> listarFullByDay(GPSLocalizacionFilterDTO dto)
			throws ServerException {
		if(dto.getFechaMin()==null) throw new ServerException("Es necesario la fecha de inicio");
		if(dto.getFechaMax()==null) throw new ServerException("Es necesario la fecha de fin");
		if(dto.getDispositivo()==null) throw new ServerException("Es necesario el dispositivo");
		return gPSLocalizacionMapper.listarFullByDay(dto);
	}
// END region aditionalMethods

}