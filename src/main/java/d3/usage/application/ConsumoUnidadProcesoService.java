package d3.usage.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.multitenancy.application.TenantAdminSvc;
import d3.multitenancy.application.TenantContext;
import d3.multitenancy.domain.TenantDTO;
import d3.usage.domain.CompraConsumoDTO;
import d3.usage.domain.ConsumoUnidadConstantes;
import d3.usage.domain.MovimientoConsumoDTO;
import d3.usage.domain.MovimientoConsumoFilterDTO;
import d3.usage.domain.SaldoConsumoDTO;
import d3.usage.domain.TransferenciaConsumoDTO;
import d3.upload.domain.CargaArchivoFilterDTO;
import d3.upload.infrastructure.CargaArchivoMapper;
import d3.shared.application.SessionContext;
import d3.shared.domain.ServerException;

@Service("consumoUnidadProcesoService")
public class ConsumoUnidadProcesoService {

	private final SaldoConsumoSvc saldoConsumoSvc;
	private final MovimientoConsumoSvc movimientoConsumoSvc;
	private final CargaArchivoMapper cargaArchivoMapper;
	private final TenantAdminSvc tenantAdminService;

	public ConsumoUnidadProcesoService(@Lazy SaldoConsumoSvc saldoConsumoSvc,
			@Lazy MovimientoConsumoSvc movimientoConsumoSvc, @Lazy CargaArchivoMapper cargaArchivoMapper,
			@Lazy TenantAdminSvc tenantAdminService) {
		this.saldoConsumoSvc = saldoConsumoSvc;
		this.movimientoConsumoSvc = movimientoConsumoSvc;
		this.cargaArchivoMapper = cargaArchivoMapper;
		this.tenantAdminService = tenantAdminService;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String procesarConsumoHorario() throws ServerException {
		LocalDateTime fin = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
		LocalDateTime inicio = fin.minusHours(1);
		Date fechaInicio = Date.from(inicio.atZone(ZoneId.systemDefault()).toInstant());
		Date fechaFin = Date.from(fin.atZone(ZoneId.systemDefault()).toInstant());

		CargaArchivoFilterDTO filter = new CargaArchivoFilterDTO();
		filter.setFechaInicioMin(fechaInicio);
		filter.setFechaInicioMax(fechaFin);
		Long totalBytes = cargaArchivoMapper.sumarSizeEntreFechas(filter);
		if (totalBytes == null)
			totalBytes = 0L;
		BigDecimal consumoMb = BigDecimal.valueOf(totalBytes)
				.divide(BigDecimal.valueOf(ConsumoUnidadConstantes.MB_BYTES), 6, RoundingMode.HALF_UP);

		SaldoConsumoDTO saldo = saldoConsumoSvc.getSaldoActual();
		BigDecimal saldoInicial = saldo.getSaldo();
		BigDecimal saldoFinal = saldoInicial.subtract(consumoMb);
		saldo.setSaldo(saldoFinal);
		saldo.setFechaActualizacion(new Date());
		saldoConsumoSvc.actualizarSaldo(saldo);
		movimientoConsumoSvc.registrarMovimiento(ConsumoUnidadConstantes.TIPO_CONSUMO, consumoMb, saldoInicial,
				saldoFinal, fechaFin, null);
		return "consumoMb=" + consumoMb + " saldo=" + saldoFinal;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String procesarIncrementoDiario() throws ServerException {
		BigDecimal incremento = BigDecimal.valueOf(ConsumoUnidadConstantes.INCREMENTO_DIARIO_MB);

		SaldoConsumoDTO saldo = saldoConsumoSvc.getSaldoActual();
		BigDecimal saldoInicial = saldo.getSaldo();
		BigDecimal saldoFinal = saldoInicial.add(incremento);
		saldo.setSaldo(saldoFinal);
		saldo.setFechaActualizacion(new Date());
		saldoConsumoSvc.actualizarSaldo(saldo);
		movimientoConsumoSvc.registrarMovimiento(ConsumoUnidadConstantes.TIPO_DIARIO, incremento, saldoInicial,
				saldoFinal, new Date(), null);
		return "incrementoMb=" + incremento + " saldo=" + saldoFinal;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MovimientoConsumoDTO comprar(CompraConsumoDTO compra) throws ServerException {
		if (compra == null || compra.getCantidad() == null
				|| compra.getCantidad().compareTo(BigDecimal.ZERO) <= 0)
			throw new ServerException("La cantidad de unidades a comprar debe ser mayor a cero");
		BigDecimal cantidadMb;
		if (ConsumoUnidadConstantes.UNIDAD_GB.equalsIgnoreCase(compra.getUnidad())) {
			cantidadMb = compra.getCantidad().multiply(BigDecimal.valueOf(ConsumoUnidadConstantes.MB_POR_GB));
		} else if (ConsumoUnidadConstantes.UNIDAD_MB.equalsIgnoreCase(compra.getUnidad())) {
			cantidadMb = compra.getCantidad();
		} else {
			throw new ServerException("La unidad debe ser MB o GB");
		}

		SaldoConsumoDTO saldo = saldoConsumoSvc.getSaldoActual();
		BigDecimal saldoInicial = saldo.getSaldo();
		BigDecimal saldoFinal = saldoInicial.add(cantidadMb);
		saldo.setSaldo(saldoFinal);
		saldo.setFechaActualizacion(new Date());
		saldoConsumoSvc.actualizarSaldo(saldo);
		return movimientoConsumoSvc.registrarMovimiento(ConsumoUnidadConstantes.TIPO_COMPRA, cantidadMb, saldoInicial,
				saldoFinal, new Date(), compra.getReferencia());
	}

	public SaldoConsumoDTO consultarSaldo() throws ServerException {
		return saldoConsumoSvc.getSaldoActual();
	}

	public MovimientoConsumoDTO transferir(TransferenciaConsumoDTO dto) throws ServerException {
		SessionContext.getCurrentUser();
		if (dto == null || dto.getTenantDestino() == null || dto.getTenantDestino().isBlank()) {
			throw new ServerException("El subtenant destino es obligatorio");
		}
		String padre = TenantContext.getCurrentTenant();
		if (padre == null || padre.isBlank()) {
			padre = "default";
		}
		TenantDTO hijo = tenantAdminService.detalle(dto.getTenantDestino().trim());
		tenantAdminService.validarVigencia(hijo);
		BigDecimal cantidadMb = aMegabytes(dto.getCantidad(), dto.getUnidad());
		String referencia = dto.getReferencia();
		if (referencia == null || referencia.isBlank()) {
			referencia = "TRF-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
		} else {
			referencia = referencia.trim();
			if (referencia.length() > 32) {
				referencia = referencia.substring(0, 32);
			}
		}
		MovimientoConsumoFilterDTO idem = new MovimientoConsumoFilterDTO();
		idem.setReferencia(referencia);
		List<MovimientoConsumoDTO> previos = movimientoConsumoSvc.listarMovimientos(idem);
		if (previos != null && !previos.isEmpty()) {
			return previos.get(0);
		}
		SaldoConsumoDTO saldoPadre = saldoConsumoSvc.getSaldoActual();
		BigDecimal inicialPadre = saldoPadre.getSaldo();
		if (inicialPadre.compareTo(cantidadMb) < 0) {
			throw new ServerException("El saldo del tenant es insuficiente para la transferencia");
		}
		BigDecimal finalPadre = inicialPadre.subtract(cantidadMb);
		saldoPadre.setSaldo(finalPadre);
		saldoPadre.setFechaActualizacion(new Date());
		saldoConsumoSvc.actualizarSaldo(saldoPadre);
		Date evento = new Date();
		MovimientoConsumoDTO salida = movimientoConsumoSvc.registrarMovimiento(
				ConsumoUnidadConstantes.TIPO_TRANSFERENCIA_ENVIADA, cantidadMb, inicialPadre, finalPadre, evento,
				referencia);
		String compuestoHijo = "default".equals(padre) ? hijo.getKey() : padre + "/" + hijo.getKey();
		String anterior = TenantContext.getCurrentTenant();
		try {
			TenantContext.setCurrentTenant(compuestoHijo);
			SaldoConsumoDTO saldoHijo = saldoConsumoSvc.getSaldoActual();
			BigDecimal inicialHijo = saldoHijo.getSaldo();
			BigDecimal finalHijo = inicialHijo.add(cantidadMb);
			saldoHijo.setSaldo(finalHijo);
			saldoHijo.setFechaActualizacion(new Date());
			saldoConsumoSvc.actualizarSaldo(saldoHijo);
			movimientoConsumoSvc.registrarMovimiento(ConsumoUnidadConstantes.TIPO_TRANSFERENCIA_RECIBIDA,
					cantidadMb, inicialHijo, finalHijo, evento, referencia);
		} catch (Exception e) {
			TenantContext.setCurrentTenant(padre);
			compensarPadre(cantidadMb, referencia);
			if (e instanceof ServerException) {
				throw (ServerException) e;
			}
			throw new ServerException("No se pudo acreditar el usage al subtenant, la salida fue revertida");
		} finally {
			TenantContext.setCurrentTenant(anterior);
		}
		return salida;
	}

	public SaldoConsumoDTO consultarSaldoHijo(String tenantKey) throws ServerException {
		SessionContext.getCurrentUser();
		TenantDTO hijo = tenantAdminService.detalle(tenantKey);
		String anterior = TenantContext.getCurrentTenant();
		try {
			TenantContext.setCurrentTenant(compuestoHijo(hijo.getKey()));
			return saldoConsumoSvc.getSaldoActual();
		} finally {
			TenantContext.setCurrentTenant(anterior);
		}
	}

	public List<MovimientoConsumoDTO> listarMovimientosHijo(String tenantKey, MovimientoConsumoFilterDTO filter)
			throws ServerException {
		SessionContext.getCurrentUser();
		TenantDTO hijo = tenantAdminService.detalle(tenantKey);
		String anterior = TenantContext.getCurrentTenant();
		try {
			TenantContext.setCurrentTenant(compuestoHijo(hijo.getKey()));
			return movimientoConsumoSvc.listarMovimientos(filter);
		} finally {
			TenantContext.setCurrentTenant(anterior);
		}
	}

	private String compuestoHijo(String keyHijo) {
		String padre = TenantContext.getCurrentTenant();
		if (padre == null || padre.isBlank()) {
			padre = "default";
		}
		return "default".equals(padre) ? keyHijo : padre + "/" + keyHijo;
	}

	private void compensarPadre(BigDecimal cantidadMb, String referencia) {
		try {
			SaldoConsumoDTO saldo = saldoConsumoSvc.getSaldoActual();
			BigDecimal inicial = saldo.getSaldo();
			BigDecimal fin = inicial.add(cantidadMb);
			saldo.setSaldo(fin);
			saldo.setFechaActualizacion(new Date());
			saldoConsumoSvc.actualizarSaldo(saldo);
			String base = referencia.length() > 28 ? referencia.substring(0, 28) : referencia;
			movimientoConsumoSvc.registrarMovimiento(ConsumoUnidadConstantes.TIPO_TRANSFERENCIA_RECIBIDA,
					cantidadMb, inicial, fin, new Date(), base + "-REV");
		} catch (Exception e) {
			System.err.println("No se pudo compensar la transferencia " + referencia + ": " + e.getMessage());
		}
	}

	private BigDecimal aMegabytes(BigDecimal cantidad, String unidad) throws ServerException {
		if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ServerException("La cantidad de unidades debe ser mayor a cero");
		}
		if (ConsumoUnidadConstantes.UNIDAD_GB.equalsIgnoreCase(unidad)) {
			return cantidad.multiply(BigDecimal.valueOf(ConsumoUnidadConstantes.MB_POR_GB));
		}
		if (ConsumoUnidadConstantes.UNIDAD_MB.equalsIgnoreCase(unidad)) {
			return cantidad;
		}
		throw new ServerException("La unidad debe ser MB o GB");
	}

}