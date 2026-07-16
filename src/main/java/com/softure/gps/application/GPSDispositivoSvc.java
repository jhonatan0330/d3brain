package com.softure.gps.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.softure.gps.domain.GPSDispositivoDTO;
import com.softure.gps.domain.GPSDispositivoFilterDTO;
import com.softure.gps.infrastructure.GPSDispositivoMapper;
import com.softure.logisticpymes.application.BasicSvc;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import com.softure.authentication.application.UsuarioSesionSvc;

@Service("gPSDispositivoService")
public class GPSDispositivoSvc extends BasicSvc<GPSDispositivoDTO, GPSDispositivoFilterDTO> {

	private final GPSDispositivoMapper gPSDispositivoMapper;

	public GPSDispositivoSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy GPSDispositivoMapper gPSDispositivoMapper) {
		super(usuarioSesionService);
		this.gPSDispositivoMapper = gPSDispositivoMapper;
	}

	@Override
	public GPSDispositivoDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. GPSDispositivo");
		GPSDispositivoFilterDTO dto = new GPSDispositivoFilterDTO();
		dto.setLlaveTabla(llave);
		return gPSDispositivoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = gPSDispositivoMapper;
	}

	@Override
	public GPSDispositivoDTO activar(GPSDispositivoDTO dto, String token) throws ServerException {
		return super.activar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public GPSDispositivoDTO actualizar(GPSDispositivoDTO dto, String token) throws ServerException {
		return super.actualizar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public GPSDispositivoDTO inactivar(GPSDispositivoDTO dto, String token) throws ServerException {
		return super.inactivar(dto, token);
	}

	@Override
	public GPSDispositivoDTO consultaUnica(GPSDispositivoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}

	@Override
	public int contarResultados(GPSDispositivoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}

	@Override
	public List<GPSDispositivoDTO> listarConsulta(GPSDispositivoFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public GPSDispositivoDTO guardar(GPSDispositivoDTO dto, String token) throws ServerException {
		return super.guardar(dto, token);
	}


}