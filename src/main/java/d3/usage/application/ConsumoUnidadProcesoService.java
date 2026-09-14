package d3.usage.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.usage.domain.CompraConsumoDTO;
import d3.usage.domain.ConsumoUnidadConstantes;
import d3.usage.domain.MovimientoConsumoDTO;
import d3.usage.domain.SaldoConsumoDTO;
import d3.upload.domain.CargaArchivoFilterDTO;
import d3.upload.infrastructure.CargaArchivoMapper;
import d3.shared.domain.ServerException;

@Service("consumoUnidadProcesoService")
public class ConsumoUnidadProcesoService {

	private final SaldoConsumoSvc saldoConsumoSvc;
	private final MovimientoConsumoSvc movimientoConsumoSvc;
	private final CargaArchivoMapper cargaArchivoMapper;

	public ConsumoUnidadProcesoService(@Lazy SaldoConsumoSvc saldoConsumoSvc,
			@Lazy MovimientoConsumoSvc movimientoConsumoSvc, @Lazy CargaArchivoMapper cargaArchivoMapper) {
		this.saldoConsumoSvc = saldoConsumoSvc;
		this.movimientoConsumoSvc = movimientoConsumoSvc;
		this.cargaArchivoMapper = cargaArchivoMapper;
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

}