package d3.process_designer.application;

import java.util.List;

import java.util.ArrayList;

import d3.shared.domain.SharedConstants;
import d3.shared.domain.ServerException;
import d3.document_execution.application.PedidoVentaSvc;
import d3.document_execution.domain.PedidoVentaDTO;
import d3.document_execution.domain.PedidoVentaFilterDTO;
import d3.java.services.D3Utils;
import d3.process_designer.domain.ProcesoEstadoDTO;
import d3.process_designer.domain.ProcesoEstadoFilterDTO;
import d3.process_designer.domain.ProcesoTransicionDTO;
import d3.process_designer.domain.ProcesoTransicionFilterDTO;
import d3.process_designer.infrastructure.ProcesoTransicionMapper;
import d3.process_form.application.DocumentoPlantillaSvc;
import d3.process_form.domain.DocumentoPlantillaDTO;
import d3.property.domain.PropiedadDTO;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.logisticpymes.application.BasicSvc;
import org.springframework.context.annotation.Lazy;
import d3.authentication.application.UsuarioSesionSvc;

@Service("procesoTransicionService")
public class ProcesoTransicionSvc extends BasicSvc<ProcesoTransicionDTO, ProcesoTransicionFilterDTO> {

	private final ProcesoTransicionMapper procesoTransicionMapper;

	public ProcesoTransicionSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy ProcesoTransicionMapper procesoTransicionMapper, @Lazy PedidoVentaSvc pedidoService,
			@Lazy ProcesoEstadoSvc estadoService, @Lazy DocumentoPlantillaSvc plantillaService) {
		super(usuarioSesionService);
		this.procesoTransicionMapper = procesoTransicionMapper;
		this.pedidoService = pedidoService;
		this.estadoService = estadoService;
		this.plantillaService = plantillaService;
	}

	private final PedidoVentaSvc pedidoService;
	private final ProcesoEstadoSvc estadoService;
	private final DocumentoPlantillaSvc plantillaService;

	@Override
	public ProcesoTransicionDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. ProcesoTransicion");
		ProcesoTransicionFilterDTO dto = new ProcesoTransicionFilterDTO();
		dto.setLlaveTabla(llave);
		return procesoTransicionMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = procesoTransicionMapper;
	}

	@Override
	public ProcesoTransicionDTO activar(ProcesoTransicionDTO dto, String token) throws ServerException {
		return super.activar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProcesoTransicionDTO actualizar(ProcesoTransicionDTO dto, String token) throws ServerException {
		validarTransicion(dto);
		return super.update(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProcesoTransicionDTO inactivar(ProcesoTransicionDTO dto, String token) throws ServerException {
		ProcesoTransicionDTO bd = consultaXId(dto.getLlaveTabla());
		if (bd.getEstadoPartida() == null) {
			PedidoVentaFilterDTO contar = new PedidoVentaFilterDTO();
			contar.setEstado(SharedConstants.STATE_COMPLETE);
			contar.setPlantilla(bd.getPlantilla());
			int cantidad = pedidoService.contarResultados(contar);
			if (cantidad != 0) {
				DocumentoPlantillaDTO plantilla = plantillaService.consultaXId(bd.getPlantilla());
				throw new ServerException("Al intentar anular la transicion " + bd.getNombre()
						+ " encontramos que existen " + cantidad + " registros de la plantilla " + plantilla.getNombre()
						+ " que fueron completados, por ello no podemos asignarlos");
			}
			procesoTransicionMapper.clearStateOfDocumentsProcess(bd.getPlantilla());
		}
		return super.inactivar(dto, token);
	}

	@Override
	public ProcesoTransicionDTO consultaUnica(ProcesoTransicionFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}

	@Override
	public int contarResultados(ProcesoTransicionFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}

	@Override
	public List<ProcesoTransicionDTO> listarConsulta(ProcesoTransicionFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProcesoTransicionDTO guardar(ProcesoTransicionDTO dto, String token) throws ServerException {
		if (dto.getEstadoLLegada() == null)
			dto.setEstadoLLegada(dto.getEstadoPartida());
		if (dto.getPlantilla() == null && dto.getDocumentador()) {
			ProcesoEstadoDTO inicial = null;
			if (dto.getEstadoPartida() != null)
				inicial = estadoService.consultaXId(dto.getEstadoPartida());
			if (inicial == null || inicial.getTipo().compareTo(ProcesoEstadoDTO.TIPO_ESTADO) == 0)
				dto.setPlantilla(crearPlantilla(dto, null, token));
		}
		validarTransicion(dto);
		dto = super.guardar(dto, token);
		return dto;
	}

	public List<ProcesoTransicionDTO> listarTransicionesRol(ProcesoTransicionFilterDTO dto) throws ServerException {
		return procesoTransicionMapper.listarTransicionesRol(dto);
	}

	public String consultarProceso(String plantilla) throws ServerException {
		ProcesoTransicionDTO filtro = consultarTransaccionInicial(plantilla);
		if (filtro != null)
			return filtro.getProceso();
		return null;
	}

	public List<ProcesoTransicionDTO> listarTransaccionesIniciales(String plantilla, String proceso)
			throws ServerException {
		ProcesoTransicionFilterDTO filtro = new ProcesoTransicionFilterDTO();
		filtro.setPlantilla(plantilla);
		filtro.setProceso(proceso);
		return procesoTransicionMapper.listarTransaccionInicial(filtro);
	}

	public ProcesoTransicionDTO consultarTransaccionInicial(String plantilla) throws ServerException {
		List<ProcesoTransicionDTO> result = listarTransaccionesIniciales(plantilla, null);
		if (result != null && !result.isEmpty()) {
			if (result.size() == 1) {
				return result.get(0);
			} else {
				throw new ServerException(
						"Revisar porq esta plantilla genera varios procesos.\n" + result.get(0).getPlantillaNombre());
			}
		}
		return null;
	}

	private void validarTransicion(ProcesoTransicionDTO dto) throws ServerException {
		if (dto == null)
			throw new ServerException("Transicion nula");
		if (dto.getProceso() == null)
			throw new ServerException("Transicion sin maquina de estados");

		// YA no necesito estoi porque la llegada es obligatoria
		// if(dto.getEstadoPartida()==null && dto.getEstadoLLegada()==null &&
		// dto.getDecision()==null) throw new ServerException("Revise los estados de
		// inicio o fin");
		// if(dto.getEstadoLLegada()==null && dto.getDecision()==null)
		// dto.setEstadoLLegada(dto.getEstadoPartida());//Esto es para evitar un error
		// que no encuentra estado de llegada
		ProcesoEstadoDTO estado = estadoService.consultaXId(dto.getEstadoLLegada());
		if (estado.getProceso().compareTo(dto.getProceso()) != 0)
			throw new ServerException("La plantilla del estado de llegada debe ser de la misma maquina de estados");
		if (estado.getTipo() == null)
			throw new ServerException("La transicion no tiene tipo");
		if (estado.getTipo().compareTo(ProcesoEstadoDTO.TIPO_API) != 0
				&& estado.getTipo().compareTo(ProcesoEstadoDTO.TIPO_DECISION) != 0
				&& estado.getTipo().compareTo(ProcesoEstadoDTO.TIPO_ESTADO) != 0
				&& estado.getTipo().compareTo(ProcesoEstadoDTO.TIPO_ITERADOR) != 0)
			throw new ServerException("La transicion tiene un tipo no valido = " + estado.getTipo());
		if (estado.getEstadoDocumento() == null) {
			estado.setEstadoDocumento(ProcesoEstadoDTO.ACTIVO);
		} else {
			if (estado.getEstadoDocumento().compareTo(ProcesoEstadoDTO.ACTIVO) != 0
					&& estado.getEstadoDocumento().compareTo(ProcesoEstadoDTO.INACTIVO) != 0
					&& estado.getEstadoDocumento().compareTo(ProcesoEstadoDTO.FINALIZADO) != 0)
				throw new ServerException(
						"La transicion tiene un estado de documento no valido = " + estado.getEstadoDocumento());
		}
		if (dto.getEstadoPartida() != null) {
			estado = estadoService.consultaXId(dto.getEstadoPartida());
			if (estado.getProceso().compareTo(dto.getProceso()) != 0)
				throw new ServerException("La plantilla del estado de partida debe ser de la misma maquina de estados");
			if (dto.getPlantilla() == null && estado.getTipo().compareTo(ProcesoEstadoDTO.TIPO_ESTADO) == 0)
				throw new ServerException("Transicion sin formulario");
			if (estado.getTipo().compareTo(ProcesoEstadoDTO.TIPO_API) == 0) {
				if (dto.getNombre().compareTo(SharedConstants.OK) != 0
						&& dto.getNombre().compareTo(SharedConstants.ERROR) != 0
						&& dto.getNombre().compareTo(SharedConstants.INCOMPLETE) != 0) {
					throw new ServerException("Las opciones que puede tener un Iterador son OK, ERROR o INCOMPLETE");
				}
			}
		} else {
			if (dto.getPlantilla() == null)
				throw new ServerException("Transicion sin formulario");
			// Validar que la transicion de inicio no se use en 2 procesos como inicial
			ProcesoTransicionFilterDTO filtroValidacion = new ProcesoTransicionFilterDTO();
			filtroValidacion.setEstado(SharedConstants.STATE_ACTIVE);
			filtroValidacion.setPlantilla(dto.getPlantilla());
			List<ProcesoTransicionDTO> filtradas = listarConsulta(filtroValidacion);
			if (filtradas != null && !filtradas.isEmpty()) {
				for (ProcesoTransicionDTO iTransicion : filtradas) {
					if (iTransicion.getEstadoPartida() == null) {
						if (dto.getLlaveTabla() == null
								|| dto.getLlaveTabla().compareTo(iTransicion.getLlaveTabla()) != 0)
							throw new ServerException(
									"Esta plantilla esta siendo usada como inicio de un proceso diferente. "
											+ iTransicion.getProcesoNombre() + "\n Nombre : "
											+ iTransicion.getNombre());
					}
				}
			}
			//
			if (dto.getLlaveTabla() == null) {
				organizarEstadosNuevos(dto.getProceso(), dto.getPlantilla(), SharedConstants.STATE_ACTIVE,
						dto.getEstadoLLegada());
				organizarEstadosNuevos(dto.getProceso(), dto.getPlantilla(), SharedConstants.STATE_INACTIVE, null);
				organizarEstadosNuevos(dto.getProceso(), dto.getPlantilla(), SharedConstants.STATE_COMPLETE, null);
			}
			// dto.setDocumentador(true);
		}
		if (dto.getCodigo() == null)
			dto.setCodigo((dto.getNombre().length() > 50) ? dto.getNombre().substring(0, 49) : dto.getNombre());
		dto.setCodigo(D3Utils.formatFunction(dto.getCodigo()).toUpperCase());
	}

	private void organizarEstadosNuevos(String proceso, String plantilla, String estadoBase, String procesoEstado)
			throws ServerException {
		PedidoVentaDTO contador = new PedidoVentaDTO();
		contador.setPlantilla(plantilla);
		contador.setEstado(estadoBase);
		int cantidad = pedidoService.listarEstadosNuevoProceso(contador);
		if (cantidad > 0) {
			if (procesoEstado == null) {
				// Valido que si no tenia proceso le coloque valor a todos los documentos del
				// proceso
				ProcesoEstadoFilterDTO estadoFiltro = new ProcesoEstadoFilterDTO();
				estadoFiltro.setEstado(SharedConstants.STATE_ACTIVE);
				estadoFiltro.setProceso(proceso);
				estadoFiltro.setEstadoDocumento(estadoBase);
				List<ProcesoEstadoDTO> estados = estadoService.listarConsulta(estadoFiltro);
				if (estados == null || estados.isEmpty())
					throw new ServerException("El nuevo proceso debe tener estados para relacionar:" + estadoBase);
				if (estados.size() != 1)
					throw new ServerException(
							"Todavia no esta la funcionalidad de cambio de estados, diferente a 1. Active un solo estado del proceso para continuar");
				procesoEstado = estados.get(0).getLlaveTabla();
			}
			contador.setEstadoExpediente(procesoEstado);
			pedidoService.actualizarEstadosNuevoProceso(contador);
		}
	}

	private String crearPlantilla(ProcesoTransicionDTO dto, String codigoFormulario, String token)
			throws ServerException {
		DocumentoPlantillaDTO plantilla = new DocumentoPlantillaDTO();

		plantilla.setProceso(dto.getProceso());
		plantilla.setCodigo(codigoFormulario);
		plantilla.setNombre(dto.getNombre());
		if (dto.getEstadoPartida() == null)
			plantilla.setPropiedades(new ArrayList<PropiedadDTO>());// Esta es la estrategia para que se cree listable
																	// el formularios
		plantilla = plantillaService.guardar(plantilla, token);
		if (dto.getEstadoPartida() != null)
			plantillaService.crearCampoProcesos(plantilla.getLlaveTabla(), token);
		return plantilla.getLlaveTabla();
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProcesoTransicionDTO guardarConCodigo(ProcesoTransicionDTO dto, String codigoFormulario, String plantilla,
			String token) throws ServerException {
		if (plantilla == null) {
			dto.setPlantilla(crearPlantilla(dto, codigoFormulario, token));
		} else {
			dto.setPlantilla(plantilla);
		}
		validarTransicion(dto);
		return super.guardar(dto, token);
	}

	public List<ProcesoTransicionDTO> getFullToSynchronize(List<String> process) {
		return procesoTransicionMapper.getFullToSynchronize(process);
	}


}