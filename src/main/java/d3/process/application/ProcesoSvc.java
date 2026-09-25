package d3.process.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.configuration.application.PropertyGetWithCacheService;
import d3.configuration.application.PropiedadSvc;
import d3.configuration.domain.PropiedadDTO;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.document.application.field.Propiedades;
import d3.process.domain.ProcesoDTO;
import d3.process.domain.ProcesoEstadoDTO;
import d3.process.domain.ProcesoEstadoFilterDTO;
import d3.process.domain.ProcesoFilterDTO;
import d3.process.domain.ProcesoTransicionDTO;
import d3.process.domain.ProcesoTransicionFilterDTO;
import d3.process.infrastructure.ProcesoMapper;
import d3.shared.application.BasicSvc;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import jakarta.annotation.PostConstruct;

@Service("procesoService")
public class ProcesoSvc extends BasicSvc<ProcesoDTO, ProcesoFilterDTO> {

	private final ProcesoMapper procesoMapper;
	private final ProcesoEstadoSvc estadoService;
	private final ProcesoTransicionSvc transicionService;
	private final PropiedadSvc propiedadService;
	private final PropiedadSvc paramService;
	private final PropertyGetWithCacheService cacheService;

	public ProcesoSvc(@Lazy ProcesoMapper procesoMapper,
			@Lazy ProcesoEstadoSvc estadoService, @Lazy ProcesoTransicionSvc transicionService,
			@Lazy PropiedadSvc propiedadService, @Lazy PropiedadSvc paramService,
			@Lazy PropertyGetWithCacheService cacheService) {
		this.procesoMapper = procesoMapper;
		this.estadoService = estadoService;
		this.transicionService = transicionService;
		this.propiedadService = propiedadService;
		this.paramService = paramService;
		this.cacheService = cacheService;
	}

	@Override
	public ProcesoDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Proceso");
		ProcesoFilterDTO dto = new ProcesoFilterDTO();
		dto.setLlaveTabla(llave);
		return procesoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = procesoMapper;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProcesoDTO actualizar(ProcesoDTO dto) throws ServerException {
		validarMacroproceso(dto.getMacroproceso());
		dto = super.actualizar(dto);
		organizar(dto);
		paramService.actualizarValorPropiedad(dto.getLlaveTabla(), dto.getNombre());
		return dto;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProcesoDTO inactivar(ProcesoDTO dto) throws ServerException {
		dto = super.inactivar(dto);
		organizar(dto);
		return dto;
	}

	public List<ProcesoDTO> consultarArbol(ProcesoFilterDTO dto) throws ServerException {
		boolean onlyOne2ShowClient = false;
		if (dto.getFiltroParametro() != null && dto.getFiltroParametro().compareTo("*") == 0) {
			onlyOne2ShowClient = true;
			dto.setFiltroParametro(null);
		}
		List<ProcesoDTO> result = listarConsulta(dto);
		for (ProcesoDTO procesoDTO : result) {
			if (procesoDTO.getTipo().compareTo(ProcesoDTO.EJECUTOR) == 0
					&& procesoDTO.getEstado().compareTo(SharedConstants.STATE_ACTIVE) == 0) {
				procesoDTO = completarProceso(procesoDTO);
			}
		}
		if (dto.getFiltroParametro() == null && !onlyOne2ShowClient) {
			ProcesoDTO resulDTO = ordenar(result);
			result = new ArrayList<ProcesoDTO>();
			result.add(resulDTO);
		}
		return result;
	}

	public ProcesoDTO obtenerProcesoParaGraficar(ProcesoFilterDTO dto) throws ServerException {
		ProcesoDTO bd = null;
		if (dto.getLlaveTabla() != null) {
			bd = consultaXId(dto.getLlaveTabla());
		} else {
			if (dto.getEstado() != null) {
				ProcesoEstadoDTO estado = estadoService.consultaXId(dto.getEstado());
				if (estado == null)
					throw new ServerException("El estado enviado no se identifica");
				bd = consultaXId(estado.getProceso());
			}
		}
		bd = completarProceso(bd);
		return bd;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProcesoDTO guardar(ProcesoDTO dto) throws ServerException {
		preConfigurar(dto);
		dto = super.guardar(dto);
		if (dto.getTipo().compareTo(ProcesoDTO.EJECUTOR) == 0)
			crearBasico(dto, null);
		return dto;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void organizar(ProcesoDTO dto) throws ServerException {
		// Consulto todas las caracteristicas del documento
		ProcesoFilterDTO filtro = new ProcesoFilterDTO();
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		List<ProcesoDTO> campos = listarConsulta(filtro);
		if (campos != null && !campos.isEmpty()) {
			int cont = 1;
			for (ProcesoDTO campo : campos) {
				if (campo.getLlaveTabla().compareTo(dto.getLlaveTabla()) != 0) {
					// asumo que hay dos iguales entonces debo saltar un espacio y el que modifique
					// lo dejo quieto
					if (campo.getPrioridad().compareTo(dto.getPrioridad()) == 0)
						cont++;
					if (campo.getPrioridad() != cont) {
						campo.setPrioridad(cont);
						super.actualizar(campo);
					}
					cont++;
				} else {
					if (cont == dto.getPrioridad())
						cont++;
				}
			}
		}
		// Debo validar que las dependencias si se puedan
	}

	private void crearBasico(ProcesoDTO dto, String plantillainicial) throws ServerException {
		ProcesoEstadoDTO estadoActivo = new ProcesoEstadoDTO();
		estadoActivo.setEstadoDocumento(SharedConstants.STATE_ACTIVE);
		estadoActivo.setTipo(ProcesoEstadoDTO.TIPO_ESTADO);
		estadoActivo.setProceso(dto.getLlaveTabla());
		estadoActivo.setNombre(dto.getNombre() + " ACTIVO");
		estadoActivo.setAvance(1);
		estadoActivo = estadoService.guardar(estadoActivo);

		PropiedadDTO propiedadModifcable = new PropiedadDTO();
		propiedadModifcable.setCampo(estadoActivo.getLlaveTabla());
		propiedadModifcable.setKey(Propiedades.MODIFICABLE);
		propiedadModifcable.setTipo(PropiedadValorDefinidoDTO.ESTADO);
		propiedadModifcable.setValor("T");
		propiedadModifcable.setMotivo("Permitir modificar los activos");
		propiedadService.guardar(propiedadModifcable);

		ProcesoEstadoDTO estadoInactivo = new ProcesoEstadoDTO();
		estadoInactivo.setEstadoDocumento(SharedConstants.STATE_INACTIVE);
		estadoInactivo.setTipo(ProcesoEstadoDTO.TIPO_ESTADO);
		estadoInactivo.setAvance(2);
		estadoInactivo.setProceso(dto.getLlaveTabla());
		estadoInactivo.setNombre(dto.getNombre() + "INACTIVO");
		estadoInactivo = estadoService.guardar(estadoInactivo);

		ProcesoTransicionDTO inicial = new ProcesoTransicionDTO();
		inicial.setEstadoLLegada(estadoActivo.getLlaveTabla());
		inicial.setNombre(dto.getNombre());
		inicial.setDocumentador(true);
		inicial.setProceso(dto.getLlaveTabla());
		transicionService.guardarConCodigo(inicial, dto.getCodigo(), plantillainicial);

		ProcesoTransicionDTO anular = new ProcesoTransicionDTO();
		anular.setEstadoPartida(estadoActivo.getLlaveTabla());
		anular.setEstadoLLegada(estadoInactivo.getLlaveTabla());
		anular.setNombre(dto.getNombre() + " - ANULAR");
		anular.setDocumentador(true);
		anular.setProceso(dto.getLlaveTabla());
		transicionService.guardarConCodigo(anular, "X" + dto.getCodigo(), null);

	}

	private ProcesoDTO ordenar(List<ProcesoDTO> procesos) throws ServerException {
		if (procesos == null)
			procesos = new ArrayList<ProcesoDTO>();
		ProcesoDTO nodoPrincipal = new ProcesoDTO();
		nodoPrincipal.setLlaveTabla("NODO1476");
		nodoPrincipal.setTipo(ProcesoDTO.AGRUPADOR);
		nodoPrincipal.setNombre("MAPA DE PROCESOS");
		nodoPrincipal.setCodigo("MAPA");
		procesos.add(0, nodoPrincipal);
		while (procesos.size() > 1) {
			ProcesoDTO ultimo = procesos.get(procesos.size() - 1);
			if (ultimo.getMacroproceso() == null)
				ultimo.setMacroproceso("NODO1476");
			ProcesoDTO padre = null;
			for (int i = procesos.size() - 2; i >= 0; i--) {
				padre = esPadre(procesos.get(i), ultimo.getMacroproceso());
				if (padre != null)
					break;
			}
			if (padre == null) {
				// En universal sucedio que se creo una llamada al mismo proceso y se generaba
				// un ciclo infinito
				if (ultimo.getLlaveTabla().compareTo(ultimo.getMacroproceso()) != 0) {
					ProcesoDTO categoria = consultaXId(ultimo.getMacroproceso());
					if (categoria == null)
						throw new ServerException(
								"No se encuentra la categoria principal. " + ultimo.getMacroproceso());
					procesos.add(categoria);
				} else {
					procesos.remove(ultimo);
				}

			} else {
				if (padre.getHijos() == null)
					padre.setHijos(new ArrayList<ProcesoDTO>());
				padre.getHijos().add(0, ultimo);
				procesos.remove(ultimo);
			}
		}
		return nodoPrincipal;
	}

	private ProcesoDTO esPadre(ProcesoDTO categoria, String llavePadre) {
		if (categoria.getLlaveTabla().compareTo(llavePadre) == 0) {
			return categoria;
		}
		if (categoria.getHijos() == null)
			return null;
		for (ProcesoDTO iCategoria : categoria.getHijos()) {
			ProcesoDTO busqueda = esPadre(iCategoria, llavePadre);
			if (busqueda != null)
				return busqueda;
		}

		return null;
	}

	public ProcesoDTO crearDesdePlantilla(String plantilla, String codigo, String nombre, String objetivo)
			throws ServerException {
		ProcesoFilterDTO filtroCantidad = new ProcesoFilterDTO();
		int cantidad = contarResultados(filtroCantidad);
		cantidad = cantidad + 1;
		ProcesoDTO dto = new ProcesoDTO();
		dto.setNombre(nombre);
		dto.setCodigo(codigo);
		dto.setTipo(ProcesoDTO.EJECUTOR);
		dto.setObjetivo(objetivo);
		dto.setPrioridad(cantidad);
		dto = super.guardar(dto);
		crearBasico(dto, plantilla);
		return dto;
	}

	private void validarMacroproceso(String macroproceso) throws ServerException {
		if (macroproceso == null)
			return;
		ProcesoDTO macro = consultaXId(macroproceso);
		if (macro == null)
			throw new ServerException("El macro proceso no se identifica");
		if (macro.getTipo().compareTo(ProcesoDTO.AGRUPADOR) != 0)
			throw new ServerException("El macroproceso no es agrupador");
	}

	private void preConfigurar(ProcesoDTO dto) throws ServerException {
		ProcesoFilterDTO filtroCantidad = new ProcesoFilterDTO();
		int cantidad = contarResultados(filtroCantidad);
		cantidad = cantidad + 1;
		dto.setPrioridad(cantidad);
		validarMacroproceso(dto.getMacroproceso());
	}

	private ProcesoDTO completarProceso(ProcesoDTO proceso) throws ServerException {
		ProcesoEstadoFilterDTO filtroEstadoDTO = new ProcesoEstadoFilterDTO();
		filtroEstadoDTO.setEstado(SharedConstants.STATE_ACTIVE);
		filtroEstadoDTO.setProceso(proceso.getLlaveTabla());
		proceso.setEstados(estadoService.listarConsulta(filtroEstadoDTO));
		for (ProcesoEstadoDTO iEstado : proceso.getEstados()) {
			iEstado.setPropiedades(cacheService.obtenerPropiedades(PropiedadValorDefinidoDTO.ESTADO,
					iEstado.getLlaveTabla(), null, null));
		}

		ProcesoTransicionFilterDTO filtroTransicionDTO = new ProcesoTransicionFilterDTO();
		filtroTransicionDTO.setEstado(SharedConstants.STATE_ACTIVE);
		filtroTransicionDTO.setProceso(proceso.getLlaveTabla());
		proceso.setTransiciones(transicionService.listarConsulta(filtroTransicionDTO));
		for (ProcesoTransicionDTO iTransicion : proceso.getTransiciones()) {
			iTransicion.setPropiedades(cacheService.obtenerPropiedades(PropiedadValorDefinidoDTO.TRANSICION,
					iTransicion.getLlaveTabla(), null, null));
			if (iTransicion.getPlantilla() != null) {
				iTransicion.getPropiedades().addAll(cacheService.obtenerPropiedades(PropiedadValorDefinidoDTO.PLANTILLA,
						iTransicion.getPlantilla(), null, null));
			}
		}

		return proceso;
	}

	public List<ProcesoDTO> getFullToSynchronize(List<String> process) {
		return procesoMapper.getFullToSynchronize(process);
	}

}