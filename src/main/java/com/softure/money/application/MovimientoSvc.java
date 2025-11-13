package com.softure.money.application;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.authorization.application.RolAccesoSvc;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.money.domain.CuentaDTO;
import com.softure.money.domain.MovimientoDTO;
import com.softure.money.domain.MovimientoFilterDTO;
import com.softure.money.domain.TurnoDTO;
import com.softure.money.infrastructure.MovimientoMapper;

import jakarta.annotation.PostConstruct;

@Service("movimientoService")
public class MovimientoSvc extends BasicSvc<MovimientoDTO, MovimientoFilterDTO> {

	@Autowired
	@Lazy
	private MovimientoMapper movimientoMapper;

	@Autowired
	@Lazy
	private TurnoSvc turnoService;
	@Autowired
	@Lazy
	private CuentaSvc cuentaService;
	@Autowired
	@Lazy
	private RolAccesoSvc rolService;

	@Override
	public MovimientoDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Movimiento");
		MovimientoFilterDTO dto = new MovimientoFilterDTO();
		dto.setLlaveTabla(llave);
		return movimientoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = movimientoMapper;
	}

	@Override
	public MovimientoDTO activar(MovimientoDTO dto, String token) throws ServerException {
		throw new ServerException("Los movimientos de dinero no se pueden activar");
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MovimientoDTO actualizar(MovimientoDTO dto, String token) throws ServerException {
		// BEGIN Movimiento_actualizar
		MovimientoDTO movimiento = consultaXId(dto.getLlaveTabla());
//		movimiento.setAnterior(dto.getAnterior());
//		movimiento.setSiguiente(dto.getSiguiente());
//		movimiento.setSaldoInicial(dto.getSaldoInicial());
//		movimiento.setSaldoFinal(dto.getSaldoFinal());
		return super.actualizar(movimiento, token);
		// END Movimiento_actualizar
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MovimientoDTO inactivar(MovimientoDTO dto, String token) throws ServerException {
		// BEGIN Movimiento_inactivar
		MovimientoDTO movimiento = super.inactivar(dto, token);
		if (movimiento.getTipo().compareTo(MovimientoDTO.ENTRADA_INGRESO) == 0 && movimiento.getRelacionado() != null) {
			if (consultaXId(movimiento.getRelacionado()).getEstado().compareTo(SharedConstants.STATE_ACTIVE) == 0)
				throw new ServerException("Se debe anular el movimiento que origino la transferencia");
		}

		if (movimiento.getTurno() != null) {
			TurnoDTO turnoMovimiento = turnoService.consultaXId(movimiento.getTurno());
			if (turnoMovimiento.getEstado().compareTo(TurnoDTO.ESTADO_FINALIZADO) == 0)
				throw new ServerException(
						"Este movimiento se registro en un turno que ya se finalizo en este caso no podemos anular el movimiento, ya que descuadraria la caja");
		}

		MovimientoDTO siguiente = (movimiento.getSiguiente() == null) ? null : consultaXId(movimiento.getSiguiente());
		MovimientoDTO anterior = (movimiento.getAnterior() == null) ? null : consultaXId(movimiento.getAnterior());
		CuentaDTO cuenta = cuentaService.consultaXId(movimiento.getCuenta());
		BigDecimal sobregiro = cuentaService.sobregiro(cuenta.getLlaveTabla());
		if (cuenta.getFechaConciliacion() != null)
			if (cuenta.getFechaConciliacion().compareTo(movimiento.getFechaEvento()) >= 0)
				throw new ServerException(
						"No puede registrar un movimiento con fecha inferior a la fecha de conciliacion");
		if (anterior != null) {
			anterior.setSiguiente(movimiento.getSiguiente());
			anterior = super.actualizar(anterior, token);
		}
		if (siguiente != null) {
			siguiente.setAnterior(movimiento.getAnterior());
			if (anterior == null) {
				siguiente.setSaldoInicial(BigDecimal.ZERO);
			} else {
				siguiente.setSaldoInicial(anterior.getSaldoFinal());
			}
			siguiente.setSaldoFinal(siguiente.getSaldoInicial().add(siguiente.getMontoAplicado()));
			siguiente = super.actualizar(siguiente, token);
			siguiente = recalcular(siguiente, sobregiro, token);
		}
		// No se usa porque anula
		// if(dto.getSaldo().compareTo(dto.getSobregiro().negate())<0) throw new
		// ServerException("La cuenta sobrepasa el limite de sobregiro. Saldo:
		// "+cuenta.getSaldo() + ". Sobregiro:" + cuenta.getSobregiro() + ". Cuenta:" +
		// cuenta.getNombre());

		// Anulo los relacionados
		MovimientoFilterDTO relacionadoFilter = new MovimientoFilterDTO();
		relacionadoFilter.setRelacionado(movimiento.getLlaveTabla());
		relacionadoFilter.setEstado(SharedConstants.STATE_ACTIVE);
		List<MovimientoDTO> relacionados = listarConsulta(relacionadoFilter);
		if (relacionados != null && relacionados.size() != 0) {
			for (MovimientoDTO movimientoDTO : relacionados) {
				inactivar(movimientoDTO, token);
			}
		}
		movimiento.setAnterior(null);
		movimiento.setSiguiente(null);
		movimiento.setSaldoInicial(BigDecimal.ZERO);
		movimiento.setSaldoFinal(BigDecimal.ZERO);
		return super.actualizar(movimiento, token);
		// END Movimiento_inactivar
	}



	public List<MovimientoDTO> obtenerMovimientoAnteriorFecha(MovimientoFilterDTO dto) throws ServerException {
		dto.setPaginacionRegistroInicial(0);
		dto.setPaginacionRegistroFinal(1);
		dto.setEstado(SharedConstants.STATE_ACTIVE);
		paginar(dto);
		try {
			return movimientoMapper.obtenerMovimientoAnteriorFecha(dto);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public List<MovimientoDTO> obtenerMovimientoSiguienteFecha(MovimientoFilterDTO dto) throws ServerException {
		dto.setPaginacionRegistroInicial(0);
		dto.setPaginacionRegistroFinal(1);
		dto.setEstado(SharedConstants.STATE_ACTIVE);
		paginar(dto);
		try {
			return movimientoMapper.obtenerMovimientoSiguienteFecha(dto);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MovimientoDTO guardar(MovimientoDTO dto, String token) throws ServerException {
		if (dto.getFechaEvento() == null)
			throw new ServerException("Registra la fecha del movimiento");
		dto.setFechaRegistro(new Date());
		if (dto.getMonto() == null)
			throw new ServerException("El monto del movimiento no esta");
		if (dto.getMonto().compareTo(BigDecimal.ZERO) < 0) {
			throw new ServerException("Estas intentando registrar un movimiento con un monto negativo ("
					+ dto.getMonto().intValue() + "), lo cual no es permitido cuando estas gestionado dinero");
		}

		CuentaDTO cuenta = cuentaService.consultaXId(dto.getCuenta());
		BigDecimal sobregiro = BigDecimal.ZERO;
		if (dto.getTurno() == null) {
			// Consultar turno
			// retiro el usuario para poder identificar quien tiene activa la caja
			TurnoDTO turnoFilter = new TurnoDTO();
			turnoFilter.setCuenta(cuenta.getLlaveTabla());
			turnoFilter.setEstado(TurnoDTO.ESTADO_EJECUCION);
			TurnoDTO turno = turnoService.consultarTurnoActual(turnoFilter);
			if (turno != null) {
				// hay un usuario automatico que debo dejar que haga el registro del turno
				if (cuenta.getValidarTurno() && !rolService.usuarioPermisosCompletos(token)
						&& turno.getUsuario().compareTo(getUserFlex(token)) != 0)
					throw new ServerException("Esta cuenta " + cuenta.getNombre() + " se encuentra ocupada por "
							+ turno.getUsuarioNombre());
				dto.setTurno(turno.getLlaveTabla());
				if (dto.getFechaEvento().compareTo(turno.getFechaApertura()) < 0)
					throw new ServerException("Iniciaste turno de la cuenta en esta fecha "
							+ SoftureUtil.formatDateTime(turno.getFechaApertura())
							+ ", te agradecemos que cambies la fecha de tu documento a una fecha que sea mayor");
			} else {
				if (cuenta.getValidarTurno())
					throw new ServerException(
							"Es necesario tener un turno activo en esta caja para registrar un movimiento de dinero");
			}
		}
		/*
		 * }else{ if(dto.getTurno()==null) { //En los escenarios en donde se hacen pagos
		 * de otras cajas que no necesitan tener turno activo TurnoDTO turno = new
		 * TurnoDTO(); turno.setUsuario(getUserFlex(token)); turno =
		 * turnoService.consultarTurnoActual(turno);
		 * if(turno!=null)dto.setTurno(turno.getLlaveTabla()); } }
		 */

		// if(permiso.getValidarTurno()==true &&
		// dto.getTipo().compareTo(MovimientoDTO.TRANSFERENCIA)!=0 && turno==null) throw
		// new ServerException("Este cuenta solicita que se generen movimientos dentro
		// de un turno");
		// Validar que ningun otro usuario tenga esa cuenta en un turno
		// dto.setCuenta(permiso.getCuenta());
		// cuenta = cuentaService.consultaXId(dto.getCuenta());
		if (cuenta.getSaldo() == null)
			cuenta.setSaldo(BigDecimal.ZERO);
		if (cuenta.getEstado().compareTo(SharedConstants.STATE_INACTIVE) == 0)
			throw new ServerException("Esta cuenta no se encuentra disponible");
		if (cuenta.getFechaConciliacion() != null)
			if (cuenta.getFechaConciliacion().compareTo(dto.getFechaEvento()) >= 0)
				throw new ServerException(
						"No puede registrar un movimiento con fecha inferior a la fecha de conciliacion. "
								+ cuenta.getFechaConciliacion().toString());
		if (dto.getFechaEvento().compareTo(new Date()) > 0)
			throw new ServerException("No puede registrar un movimiento con fecha superior a la actual");
		// if(dto.getCategoria()==null && permiso.getCatalogo()!=null)
		// dto.setCategoria(permiso.getCatalogo());
		// CatalogoDTO tipo = catalogoService.consultaXId(dto.getCategoria());
		// if(tipo==null) throw new ServerException("El catalogo "+ dto.getCategoria() +
		// " no existe");
		// if(tipo.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0) throw
		// new ServerException("El rubro de "+ tipo.getNombre() + " no se encuentra
		// activo");
		// if(dto.getTipo()==null){
		// dto.setTipo(tipo.getTipo());
		// }
		dto.setMontoAplicado(dto.getMonto());

		sobregiro = cuentaService.sobregiro(cuenta.getDocumento());
		if (dto.getTipo().compareTo(MovimientoDTO.SALIDA_GASTO) == 0) {
			if (cuenta.getSaldo().add(dto.getMonto().negate()).compareTo(sobregiro.negate()) < 0)
				throw new ServerException("La cuenta " + cuenta.getNombre() + " sobrepasa el limite de sobregiro de ("
						+ SoftureUtil.formatMoney(sobregiro) + ") actualmente tiene un saldo de Saldo de ("
						+ SoftureUtil.formatMoney(cuenta.getSaldo()) + ") y quedaria con un valor final de "
						+ SoftureUtil.formatMoney(cuenta.getSaldo().add(dto.getMonto().negate())) + ". Cuenta:"
						+ cuenta.getNombre());
			dto.setMontoAplicado(dto.getMonto().negate());
		}

		// Como la contrapartida es para otro tercero no importa
		dto.setSaldoInicial(BigDecimal.ZERO);
		dto.setSaldoFinal(BigDecimal.ZERO);

		dto = super.guardar(dto, token);

		dto = reorganizar(dto, sobregiro, token);
		recalcular(dto, sobregiro, token);
		/*
		 * if(dto.getTipo().compareTo(MovimientoDTO.TRANSFERENCIA)==0){ //Debo crear
		 * otro movimiento para la cuenta destino con los valores contrarios
		 * MovimientoDTO contra = new MovimientoDTO();
		 * contra.setCuenta(dto.getCuentaDestino());
		 * contra.setCuentaDestino(dto.getCuenta());
		 * //contra.setDescripcion(dto.getDescripcion());
		 * contra.setFechaEvento(dto.getFechaEvento());
		 * contra.setFechaRegistro(dto.getFechaRegistro());
		 * contra.setMonto(dto.getMonto()); contra.setMontoAplicado(dto.getMonto());
		 * contra.setTipo(MovimientoDTO.ENTRADA_INGRESO);
		 * contra.setTurno(dto.getTurno()); contra.setRelacionado(dto.getLlaveTabla());
		 * 
		 * contra.setCuentaPermisoUsuario(dto.getCuentaPermisoUsuarioDestino());
		 * 
		 * CuentaDTO cuentaDestino =cuentaService.consultaXId(dto.getCuentaDestino());
		 * contra.setSaldoInicial(BigDecimal.ZERO);
		 * contra.setSaldoFinal(BigDecimal.ZERO);
		 * contra.setSecurityToken(dto.getSecurityToken());
		 * 
		 * contra = super.guardar(contra); contra = reorganizar(contra,
		 * cuentaDestino.getSobregiro()); recalcular(contra,
		 * cuentaDestino.getSobregiro()); }
		 */
		return dto;
	}

	private MovimientoDTO recalcular(MovimientoDTO inicial, BigDecimal valorMinimoCuenta, String token)
			throws ServerException {
		MovimientoDTO siguiente = null;
		boolean modificado = false;// Para evitar en la audotria muchos cambios
		BigDecimal valorFinal = null;
		while (inicial.getSiguiente() != null) {
			siguiente = consultaXId(inicial.getSiguiente());
			if (siguiente.getSaldoInicial().compareTo(inicial.getSaldoFinal()) != 0) {
				siguiente.setSaldoInicial(inicial.getSaldoFinal());
				modificado = true;
			}
			valorFinal = siguiente.getSaldoInicial().add(siguiente.getMontoAplicado());
			if (siguiente.getSaldoFinal().compareTo(valorFinal) != 0) {
				siguiente.setSaldoFinal(valorFinal);
				modificado = true;
			}
			if (modificado) {
				if (siguiente.getSaldoFinal().compareTo(valorMinimoCuenta.negate()) < 0)
					throw new ServerException("La cuenta sobrepasa el limite de sobregiro. Saldo: "
							+ siguiente.getSaldoFinal() + ". Sobregiro:" + valorMinimoCuenta + ". Fecha:"
							+ siguiente.getFechaEvento() + ". Cuenta:" + inicial.getCuenta());
				siguiente = super.actualizar(siguiente, token);
			}
			inicial = siguiente;
		}
		return inicial;
	}

	private MovimientoDTO reorganizar(MovimientoDTO inicial, BigDecimal valorMinimoCuenta, String token)
			throws ServerException {
		// Busco el ultimo movimiento de la empresa
		MovimientoFilterDTO anteriorFilter = new MovimientoFilterDTO();
		anteriorFilter.setCuenta(inicial.getCuenta());
		anteriorFilter.setFechaEventoMax(inicial.getFechaEvento());
		anteriorFilter.setLlaveTabla(inicial.getLlaveTabla());
		List<MovimientoDTO> ultimos = obtenerMovimientoAnteriorFecha(anteriorFilter);
		MovimientoDTO ultimo = null;
		if (ultimos == null || ultimos.size() == 0) {
			inicial.setSaldoInicial(BigDecimal.ZERO);
		} else {
			ultimo = ultimos.get(0);
			inicial.setSaldoInicial(ultimo.getSaldoFinal());
		}
		inicial.setSaldoFinal(inicial.getSaldoInicial().add(inicial.getMontoAplicado()));
		if (inicial.getSaldoFinal().compareTo(valorMinimoCuenta.negate()) < 0)
			throw new ServerException("La cuenta sobrepasa el limite de sobregiro. Saldo: "
					+ SoftureUtil.formatMoney(inicial.getSaldoFinal()) + ". Sobregiro:"
					+ SoftureUtil.formatMoney(valorMinimoCuenta) + ". Fecha:" + inicial.getFechaEvento() + ". Cuenta:"
					+ inicial.getCuenta());
		MovimientoDTO siguiente = null;
		if (ultimo != null) {
			inicial.setSiguiente(ultimo.getSiguiente());
			inicial.setAnterior(ultimo.getLlaveTabla());
			if (ultimo.getSiguiente() != null) {
				siguiente = consultaXId(ultimo.getSiguiente());
				siguiente.setAnterior(inicial.getLlaveTabla());
				siguiente = super.actualizar(siguiente, token);
			}
			ultimo.setSiguiente(inicial.getLlaveTabla());
			ultimo = super.actualizar(ultimo, token);
		} else {
			MovimientoFilterDTO inicialFilter = new MovimientoFilterDTO();
			inicialFilter.setCuenta(inicial.getCuenta());
			inicialFilter.setFechaEventoMin(inicial.getFechaEvento());
			inicialFilter.setLlaveTabla(inicial.getLlaveTabla());
			// Busco el primer movimiento que hay y lo vinculo como siguiente
			List<MovimientoDTO> siguientes = obtenerMovimientoSiguienteFecha(inicialFilter);

			if (siguientes == null || siguientes.size() == 0) {
			} else {
				siguiente = siguientes.get(0);
				siguiente.setAnterior(inicial.getLlaveTabla());
				inicial.setSiguiente(siguiente.getLlaveTabla());
				super.actualizar(siguiente, token);
			}
		}
		return super.actualizar(inicial, token);
	}

}