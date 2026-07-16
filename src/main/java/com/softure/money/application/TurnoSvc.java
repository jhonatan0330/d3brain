package com.softure.money.application;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.money.domain.CuentaDTO;
import com.softure.money.domain.CuentaFilterDTO;
import com.softure.money.domain.TurnoDTO;
import com.softure.money.domain.TurnoFilterDTO;
import com.softure.money.infrastructure.TurnoMapper;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import com.softure.authentication.application.UsuarioSesionSvc;

@Service("turnoService")
public class TurnoSvc extends BasicSvc<TurnoDTO, TurnoFilterDTO> {

	private final TurnoMapper turnoMapper;
	private final CuentaSvc cuentaService;

	public TurnoSvc(@Lazy UsuarioSesionSvc usuarioSesionService, @Lazy TurnoMapper turnoMapper,
			@Lazy CuentaSvc cuentaService) {
		super(usuarioSesionService);
		this.turnoMapper = turnoMapper;
		this.cuentaService = cuentaService;
	}

	@Override
	public TurnoDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Turno");
		TurnoFilterDTO dto = new TurnoFilterDTO();
		dto.setLlaveTabla(llave);
		return turnoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = turnoMapper;
	}

	public TurnoDTO consultarTurnoActual(TurnoDTO dto) throws ServerException {
		TurnoFilterDTO turnoDTO = new TurnoFilterDTO();
		turnoDTO.setUsuario(dto.getUsuario());
		turnoDTO.setCuenta(dto.getCuenta());
		turnoDTO.setDocumento(dto.getDocumento());
		turnoDTO.setEstado(TurnoDTO.ESTADO_EJECUCION);
		List<TurnoDTO> turnos = listarConsulta(turnoDTO);
		if (turnos == null || turnos.size() == 0)
			return null;
		if (turnos.size() == 1)
			return turnos.get(0);
		if (turnos.size() > 1)
			throw new ServerException("Se recomienda tener un solo turno asignado");
		return null;
	}

	public TurnoDTO iniciarTurno(TurnoDTO dto, String token) throws ServerException {
		CuentaFilterDTO cajaFilter = new CuentaFilterDTO();
		cajaFilter.setDocumento(dto.getCuenta());
		CuentaDTO caja = cuentaService.consultaUnica(cajaFilter);
		if (caja == null)
			throw new ServerException("No se identifica la caja que relaciona el turno");
		// Esto lo quite en autollanos
		// if(!caja.getValidarTurno())throw new ServerException("Esta cuenta no permite
		// iniciar turnos");

		if (!cuentaService.turnomultiple(caja.getLlaveTabla())) {
			TurnoFilterDTO turnoFilterDTO = new TurnoFilterDTO();
			turnoFilterDTO.setCuenta(caja.getLlaveTabla());
			turnoFilterDTO.setEstado(TurnoDTO.ESTADO_EJECUCION);
			List<TurnoDTO> turnos = listarConsulta(turnoFilterDTO);
			if (turnos != null && turnos.size() != 0)
				throw new ServerException("Esta caja " + caja.getNombre() + " ya tiene un turno asignado con "
						+ turnos.get(0).getUsuarioNombre());
		}

		dto.setCuenta(caja.getLlaveTabla());
		if (dto.getFechaApertura() == null) {
			dto.setFechaApertura(SoftureUtil.agregarMinutos(new Date(), -2));
		} else {
			dto.setFechaApertura(SoftureUtil.agregarMinutos(dto.getFechaApertura(), -2));
		}
		dto.setEstado(TurnoDTO.ESTADO_EJECUCION);
		dto.setMontoInicial(caja.getSaldo());
		if (dto.getLlaveTabla() == null) {
			dto = guardar(dto, token);
		} else {
			dto = actualizar(dto, token);
		}
		return dto;
	}

}