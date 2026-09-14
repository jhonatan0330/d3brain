package d3.usage.infrastructure;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import d3.usage.application.ConsumoUnidadProcesoService;
import d3.usage.application.MovimientoConsumoSvc;
import d3.usage.domain.CompraConsumoDTO;
import d3.usage.domain.MovimientoConsumoDTO;
import d3.usage.domain.MovimientoConsumoFilterDTO;
import d3.usage.domain.SaldoConsumoDTO;
import d3.shared.application.SharedAuthenticateService;
import d3.shared.domain.ServerException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/consumo-units")
public class ConsumoUnidadRest {

	private final SharedAuthenticateService tokenService;
	private final ConsumoUnidadProcesoService consumoUnidadProcesoService;
	private final MovimientoConsumoSvc movimientoConsumoSvc;

	public ConsumoUnidadRest(@Lazy SharedAuthenticateService tokenService,
			@Lazy ConsumoUnidadProcesoService consumoUnidadProcesoService,
			@Lazy MovimientoConsumoSvc movimientoConsumoSvc) {
		this.tokenService = tokenService;
		this.consumoUnidadProcesoService = consumoUnidadProcesoService;
		this.movimientoConsumoSvc = movimientoConsumoSvc;
	}

	@PostMapping(value = "/balance")
	public SaldoConsumoDTO balance(HttpServletRequest request, @RequestHeader("Authorization") String token)
			throws ServerException {
		tokenService.getUser(token, request);
		return consumoUnidadProcesoService.consultarSaldo();
	}

	@PostMapping(value = "/movements")
	public List<MovimientoConsumoDTO> movements(HttpServletRequest request,
			@RequestHeader("Authorization") String token,
			@RequestBody(required = false) MovimientoConsumoFilterDTO filter) throws ServerException {
		tokenService.getUser(token, request);
		return movimientoConsumoSvc.listarMovimientos(filter);
	}

	@PostMapping(value = "/purchase")
	public MovimientoConsumoDTO purchase(HttpServletRequest request,
			@RequestHeader("Authorization") String token, @RequestBody(required = false) CompraConsumoDTO compra)
			throws ServerException {
		tokenService.getUser(token, request);
		return consumoUnidadProcesoService.comprar(compra);
	}

}