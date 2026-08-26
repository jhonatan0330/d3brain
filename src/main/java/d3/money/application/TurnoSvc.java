package d3.money.application;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import d3.shared.domain.ServerException;
import d3.java.services.D3Utils;
import d3.logisticpymes.application.BasicSvc;
import d3.money.domain.CuentaDTO;
import d3.money.domain.CuentaFilterDTO;
import d3.money.domain.TurnoDTO;
import d3.money.domain.TurnoFilterDTO;
import d3.money.infrastructure.TurnoMapper;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import d3.authentication.application.UsuarioSesionSvc;

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
			dto.setFechaApertura(D3Utils.agregarMinutos(new Date(), -2));
		} else {
			dto.setFechaApertura(D3Utils.agregarMinutos(dto.getFechaApertura(), -2));
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