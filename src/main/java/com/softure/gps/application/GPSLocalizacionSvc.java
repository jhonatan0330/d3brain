package com.softure.gps.application;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.softure.gps.domain.GPSDispositivoDTO;
import com.softure.gps.domain.GPSLocalizacionDTO;
import com.softure.gps.domain.GPSLocalizacionFilterDTO;
import com.softure.gps.infrastructure.GPSLocalizacionMapper;
import com.softure.logisticpymes.application.BasicSvc;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import com.softure.authentication.application.UsuarioSesionSvc;

@Service("gPSLocalizacionService")
public class GPSLocalizacionSvc extends BasicSvc<GPSLocalizacionDTO, GPSLocalizacionFilterDTO> {

	private final GPSLocalizacionMapper gPSLocalizacionMapper;

	public GPSLocalizacionSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy GPSLocalizacionMapper gPSLocalizacionMapper, @Lazy GPSDispositivoSvc gpsDispositivoService) {
		super(usuarioSesionService);
		this.gPSLocalizacionMapper = gPSLocalizacionMapper;
		this.gpsDispositivoService = gpsDispositivoService;
	}

	private final GPSDispositivoSvc gpsDispositivoService;

	@Override
	public GPSLocalizacionDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. GPSLocalizacion");
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
		return super.activar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public GPSLocalizacionDTO actualizar(GPSLocalizacionDTO dto, String token) throws ServerException {
		return super.actualizar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public GPSLocalizacionDTO inactivar(GPSLocalizacionDTO dto, String token) throws ServerException {
		return super.inactivar(dto, token);
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
	public List<GPSLocalizacionDTO> listarConsulta(GPSLocalizacionFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public GPSLocalizacionDTO guardar(GPSLocalizacionDTO dto, String token) throws ServerException {
		if (dto == null)
			throw new ServerException("El objeto no puede ser vacio");
		if (dto.getFecha() == null)
			throw new ServerException("La ubicacion debe tener fecha");
		dto.setFechaReporte(new Date());
		GPSDispositivoDTO dispositivo = gpsDispositivoService.consultaXId(dto.getDispositivo());
		dispositivo.setUltimaConexion(dto.getFecha());
		gpsDispositivoService.saveSimple(dispositivo);
		dto = super.save(dto);
		return dto;
	}

	public List<GPSLocalizacionDTO> listarFullByDay(GPSLocalizacionFilterDTO dto) throws ServerException {
		if (dto.getFechaMin() == null)
			throw new ServerException("Es necesario la fecha de inicio");
		if (dto.getFechaMax() == null)
			throw new ServerException("Es necesario la fecha de fin");
		if (dto.getDispositivo() == null)
			throw new ServerException("Es necesario el dispositivo");
		return gPSLocalizacionMapper.listarFullByDay(dto);
	}

}