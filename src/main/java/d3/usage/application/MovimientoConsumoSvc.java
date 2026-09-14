package d3.usage.application;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;

import d3.authentication.application.UsuarioSesionSvc;
import d3.usage.domain.MovimientoConsumoDTO;
import d3.usage.domain.MovimientoConsumoFilterDTO;
import d3.usage.infrastructure.MovimientoConsumoMapper;
import d3.shared.application.BasicSvc;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;

@Service("movimientoConsumoService")
public class MovimientoConsumoSvc extends BasicSvc<MovimientoConsumoDTO, MovimientoConsumoFilterDTO> {

	private final MovimientoConsumoMapper movimientoConsumoMapper;

	public MovimientoConsumoSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy MovimientoConsumoMapper movimientoConsumoMapper) {
		super(usuarioSesionService);
		this.movimientoConsumoMapper = movimientoConsumoMapper;
	}

	@Override
	public MovimientoConsumoDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Movimiento de consumo");
		MovimientoConsumoFilterDTO dto = new MovimientoConsumoFilterDTO();
		dto.setLlaveTabla(llave);
		return movimientoConsumoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = movimientoConsumoMapper;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MovimientoConsumoDTO registrarMovimiento(String tipo, BigDecimal cantidad, BigDecimal saldoInicial,
			BigDecimal saldoFinal, Date fechaEvento, String referencia) throws ServerException {
		MovimientoConsumoDTO movimiento = new MovimientoConsumoDTO();
		movimiento.setTipo(tipo);
		movimiento.setCantidad(cantidad);
		movimiento.setSaldoInicial(saldoInicial);
		movimiento.setSaldoFinal(saldoFinal);
		movimiento.setFechaEvento(fechaEvento);
		movimiento.setFechaRegistro(new Date());
		movimiento.setReferencia(referencia);
		movimiento.setEstado(SharedConstants.STATE_ACTIVE);
		return saveSimple(movimiento);
	}

	public List<MovimientoConsumoDTO> listarMovimientos(MovimientoConsumoFilterDTO filter) throws ServerException {
		if (filter == null)
			filter = new MovimientoConsumoFilterDTO();
		return listarConsulta(filter);
	}

}