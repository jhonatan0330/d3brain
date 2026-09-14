package d3.usage.application;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;

import d3.authentication.application.UsuarioSesionSvc;
import d3.usage.domain.ConsumoUnidadConstantes;
import d3.usage.domain.SaldoConsumoDTO;
import d3.usage.domain.SaldoConsumoFilterDTO;
import d3.usage.infrastructure.SaldoConsumoMapper;
import d3.shared.application.BasicSvc;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;

@Service("saldoConsumoService")
public class SaldoConsumoSvc extends BasicSvc<SaldoConsumoDTO, SaldoConsumoFilterDTO> {

	private final SaldoConsumoMapper saldoConsumoMapper;
	private final MovimientoConsumoSvc movimientoConsumoService;

	public SaldoConsumoSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy SaldoConsumoMapper saldoConsumoMapper, @Lazy MovimientoConsumoSvc movimientoConsumoService) {
		super(usuarioSesionService);
		this.saldoConsumoMapper = saldoConsumoMapper;
		this.movimientoConsumoService = movimientoConsumoService;
	}

	@Override
	public SaldoConsumoDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Saldo de consumo");
		SaldoConsumoFilterDTO dto = new SaldoConsumoFilterDTO();
		dto.setLlaveTabla(llave);
		return saldoConsumoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = saldoConsumoMapper;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SaldoConsumoDTO getSaldoActual() throws ServerException {
		SaldoConsumoFilterDTO filter = new SaldoConsumoFilterDTO();
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		SaldoConsumoDTO saldo = saldoConsumoMapper.obtenerSaldoActivo(filter);
		if (saldo == null) {
			saldo = new SaldoConsumoDTO();
			saldo.setSaldo(BigDecimal.valueOf(ConsumoUnidadConstantes.SALDO_INICIAL_MB));
			saldo.setFechaActualizacion(new Date());
			saldo.setEstado(SharedConstants.STATE_ACTIVE);
			saldo = saveSimple(saldo);
			movimientoConsumoService.registrarMovimiento(ConsumoUnidadConstantes.TIPO_INICIAL, saldo.getSaldo(),
					BigDecimal.ZERO, saldo.getSaldo(), new Date(), null);
		}
		return saldo;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SaldoConsumoDTO actualizarSaldo(SaldoConsumoDTO saldo) throws ServerException {
		return update(saldo);
	}

}