package d3.process.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.document.application.field.Propiedades;
import d3.shared.application.D3Utils;
import d3.shared.application.SessionContext;
import d3.shared.application.BasicSvc;
import d3.process.domain.ProcesoEstadoDTO;
import d3.process.domain.ProcesoEstadoFilterDTO;
import d3.process.domain.ProcesoTransicionFilterDTO;
import d3.process.infrastructure.ProcesoEstadoMapper;
import jakarta.annotation.PostConstruct;
import d3.configuration.application.PropiedadSvc;
import d3.configuration.domain.PropiedadDTO;
import d3.configuration.domain.PropiedadValorDefinidoDTO;

@Service("procesoEstadoService")
public class ProcesoEstadoSvc extends BasicSvc<ProcesoEstadoDTO, ProcesoEstadoFilterDTO> {

	private final ProcesoEstadoMapper procesoEstadoMapper;
	private final ProcesoTransicionSvc procesoTransicionService;
	private final PropiedadSvc parametroService;

	public ProcesoEstadoSvc(@Lazy ProcesoEstadoMapper procesoEstadoMapper,
			@Lazy ProcesoTransicionSvc procesoTransicionService, @Lazy PropiedadSvc parametroService) {
		this.procesoEstadoMapper = procesoEstadoMapper;
		this.procesoTransicionService = procesoTransicionService;
		this.parametroService = parametroService;
	}

	@Override
	public ProcesoEstadoDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. ProcesoEstado");
		ProcesoEstadoFilterDTO dto = new ProcesoEstadoFilterDTO();
		dto.setLlaveTabla(llave);
		return procesoEstadoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = procesoEstadoMapper;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProcesoEstadoDTO actualizar(ProcesoEstadoDTO dto) throws ServerException {
		colocarSignoPregunta(dto);
		ProcesoEstadoDTO bd = consultaXId(dto.getLlaveTabla());
		if (bd.getEstadoDocumento().compareTo(dto.getEstadoDocumento()) != 0) {
			procesoEstadoMapper.actualizarEstados(dto);
		}
		dto = super.actualizar(dto);
		organizar(dto);
		return dto;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProcesoEstadoDTO inactivar(ProcesoEstadoDTO dto) throws ServerException {
		ProcesoTransicionFilterDTO transicion = new ProcesoTransicionFilterDTO();
		transicion.setEstadoLLegada(dto.getLlaveTabla());
		transicion.setEstado(SharedConstants.STATE_ACTIVE);
		if (procesoTransicionService.contarResultados(transicion) != 0)
			throw new ServerException("Este estado es usada en varias transacciones activas como estado de llegada");

		transicion = new ProcesoTransicionFilterDTO();
		transicion.setEstadoPartida(dto.getLlaveTabla());
		transicion.setEstado(SharedConstants.STATE_ACTIVE);
		if (procesoTransicionService.contarResultados(transicion) != 0)
			throw new ServerException("Este estado es usada en varias transacciones activas como estado de partida");

		dto = super.inactivar(dto);
		organizar(dto);
		return dto;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProcesoEstadoDTO guardar(ProcesoEstadoDTO dto) throws ServerException {
		colocarSignoPregunta(dto);
		ProcesoEstadoFilterDTO filtroCantidad = new ProcesoEstadoFilterDTO();
		filtroCantidad.setNodoSuperior(dto.getNodoSuperior());
		filtroCantidad.setEstado(SharedConstants.STATE_ACTIVE);
		int cantidadCampos = contarResultados(filtroCantidad);
		if (dto.getPrioridad() != null && dto.getPrioridad().compareTo(0) != 0) {
			cantidadCampos = dto.getPrioridad();
		} else {
			cantidadCampos = cantidadCampos + 1;
		}
		dto.setPrioridad(cantidadCampos);
		ProcesoEstadoDTO result = super.guardar(dto);
		// Esto hace fallar el sincronizador
		/*
		 * if (dto.getTipo().compareTo(ProcesoEstadoDTO.TIPO_API) == 0) {
		 * createTransicionAPI(result, SharedConstants.OK, token);
		 * createTransicionAPI(result, SharedConstants.ERROR, token); }
		 */
		colorHexAleatorio(result);
		return result;
	}

	private void organizar(ProcesoEstadoDTO pDTO) throws ServerException {
		if (pDTO == null || pDTO.getNodoSuperior() == null || pDTO.getLlaveTabla() == null) {
			return;
		}
		ProcesoEstadoFilterDTO _filtro = new ProcesoEstadoFilterDTO();
		_filtro.setEstado(SharedConstants.STATE_ACTIVE);
		_filtro.setNodoSuperior(pDTO.getNodoSuperior());
		_filtro.setPaginacionRegistroFinal(500);
		List<ProcesoEstadoDTO> _estados = listarConsulta(_filtro);
		if (_estados == null || _estados.isEmpty()) {
			return;
		}

		ProcesoEstadoDTO _actualizado = null;
		List<ProcesoEstadoDTO> _resto = new ArrayList<>();
		for (ProcesoEstadoDTO _estado : _estados) {
			if (_estado.getLlaveTabla() != null && _estado.getLlaveTabla().compareTo(pDTO.getLlaveTabla()) == 0) {
				_actualizado = _estado;
			} else {
				_resto.add(_estado);
			}
		}

		int _total = _estados.size();
		int _objetivo;
		if (_actualizado == null) {
			_objetivo = -1;
		} else if (pDTO.getPrioridad() != null) {
			_objetivo = pDTO.getPrioridad();
		} else {
			_objetivo = _estados.indexOf(_actualizado) + 1;
		}
		if (_objetivo > _total) {
			_objetivo = _total;
		}
		if (_actualizado != null && _objetivo < 1) {
			_objetivo = 1;
		}

		int _cont = 1;
		for (ProcesoEstadoDTO _estado : _resto) {
			if (_objetivo > 0 && _cont == _objetivo) {
				_cont++;
			}
			Integer _prioridad = _estado.getPrioridad();
			if (_prioridad == null || _prioridad != _cont) {
				_estado.setPrioridad(_cont);
				super.actualizar(_estado);
			}
			_cont++;
		}

		if (_actualizado != null) {
			Integer _prioridad = _actualizado.getPrioridad();
			if (_prioridad == null || _prioridad != _objetivo) {
				_actualizado.setPrioridad(_objetivo);
				super.actualizar(_actualizado);
			}
		}
	}

	private void colocarSignoPregunta(ProcesoEstadoDTO estado) throws ServerException {
		if (estado == null)
			throw new ServerException("No se puede colocar el signo porque el dto es nulo");
		if (estado.getTipo() == null)
			throw new ServerException("No se puede colocar el signo porque el tipo del estado es nulo");
		if (estado.getTipo().compareTo(ProcesoEstadoDTO.TIPO_DECISION) == 0) {
			if (estado.getNombre() == null)
				throw new ServerException("No se puede colocar el signo porque el nombre del estado es nulo");
			if (!estado.getNombre().endsWith("?"))
				estado.setNombre(estado.getNombre() + "?");
		}
		if (estado.getCodigo() == null)
			estado.setCodigo(
					(estado.getNombre().length() > 50) ? estado.getNombre().substring(0, 49) : estado.getNombre());
		estado.setCodigo(D3Utils.formatFunction(estado.getCodigo()).toUpperCase());
	}

	public void colorHexAleatorio(ProcesoEstadoDTO pState) throws ServerException {
		if (pState == null)
			throw new ServerException("No se puede colocar el signo porque el dto es nulo");
		if (pState.getTipo() == null)
			throw new ServerException("No se puede colocar el signo porque el tipo del estado es nulo");
		if (pState.getTipo().compareTo(ProcesoEstadoDTO.TIPO_ESTADO) != 0)
			return;
		Random _random = new Random();
		int color = _random.nextInt(0x1000000); // 0x1000000 = 16777216
		parametroService.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.ESTADO, pState.getLlaveTabla(),
				Propiedades.COLOR, String.format("#%06X", color)));
	}

	public String obtenerResponsable(PropiedadDTO propiedad, String documento, String modificador)
			throws ServerException {
		String responsable = null;
		try {
			responsable = procesoEstadoMapper.funcionAsignacion(D3Utils.formatFunction(propiedad.getLlaveTabla()),
					documento, modificador, SessionContext.getCurrentToken());
		} catch (Exception e) {
			ProcesoEstadoDTO pes = consultaXId(propiedad.getCampo());
			throw new ServerException(e.getMessage(),
					"Proceso : " + pes.getNodoSuperiorNombre() + "  Estado :" + pes.getNombre());
		}
		// En Fiel a veces no me retornaba valor porque el campo es opcional
		return responsable;
	}

}