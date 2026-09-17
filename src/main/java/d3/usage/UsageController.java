package d3.usage;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import d3.usage.application.ConsumoUnidadProcesoService;
import d3.usage.application.MovimientoConsumoSvc;
import d3.usage.domain.CompraConsumoDTO;
import d3.usage.domain.MovimientoConsumoDTO;
import d3.usage.domain.MovimientoConsumoFilterDTO;
import d3.usage.domain.SaldoConsumoDTO;
import d3.shared.application.SessionContext;
import d3.shared.domain.ServerException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/consumo-units")
public class UsageController {

	private final ConsumoUnidadProcesoService consumoUnidadProcesoService;
	private final MovimientoConsumoSvc movimientoConsumoSvc;

	public UsageController(@Lazy ConsumoUnidadProcesoService consumoUnidadProcesoService,
			@Lazy MovimientoConsumoSvc movimientoConsumoSvc) {
		this.consumoUnidadProcesoService = consumoUnidadProcesoService;
		this.movimientoConsumoSvc = movimientoConsumoSvc;
	}

	@PostMapping(value = "/balance")
	public SaldoConsumoDTO balance() throws ServerException {
		SessionContext.getCurrentUser();
		return consumoUnidadProcesoService.consultarSaldo();
	}

	@PostMapping(value = "/movements")
	public List<MovimientoConsumoDTO> movements(
			@RequestBody(required = false) MovimientoConsumoFilterDTO filter) throws ServerException {
		SessionContext.getCurrentUser();
		return movimientoConsumoSvc.listarMovimientos(filter);
	}

	@PostMapping(value = "/purchase")
	public MovimientoConsumoDTO purchase(@RequestBody(required = false) CompraConsumoDTO compra)
			throws ServerException {
		SessionContext.getCurrentUser();
		return consumoUnidadProcesoService.comprar(compra);
	}

}