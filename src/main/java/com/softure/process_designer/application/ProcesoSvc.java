package com.softure.process_designer.application;

import java.util.List;

import java.util.ArrayList;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.process_designer.domain.ProcesoDTO;
import com.softure.process_designer.domain.ProcesoEstadoDTO;
import com.softure.process_designer.domain.ProcesoEstadoFilterDTO;
import com.softure.process_designer.domain.ProcesoFilterDTO;
import com.softure.process_designer.domain.ProcesoTransicionDTO;
import com.softure.process_designer.domain.ProcesoTransicionFilterDTO;
import com.softure.process_designer.infrastructure.ProcesoMapper;
import com.softure.property.application.PropertyGetWithCacheService;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.logisticpymes.application.BasicSvc;

@Service("procesoService")
public class ProcesoSvc extends BasicSvc<ProcesoDTO, ProcesoFilterDTO> {

	@Autowired
	@Lazy
	private ProcesoMapper procesoMapper;

	@Autowired
	@Lazy
	private ProcesoEstadoSvc estadoService;
	@Autowired
	@Lazy
	private ProcesoTransicionSvc transicionService;
	@Autowired
	@Lazy
	private PropiedadSvc propiedadService;
	@Autowired
	@Lazy
	private PropiedadSvc paramService;
	@Autowired
	@Lazy
	private PropertyGetWithCacheService cacheService;

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
	public ProcesoDTO actualizar(ProcesoDTO dto, String token) throws ServerException {
		// BEGIN Proceso_actualizar
		validarMacroproceso(dto.getMacroproceso());
		dto = super.actualizar(dto, token);
		organizar(dto, token);
		paramService.actualizarValorPropiedad(dto.getLlaveTabla(), dto.getNombre());
		return dto;
		// END Proceso_actualizar
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProcesoDTO inactivar(ProcesoDTO dto, String token) throws ServerException {
		// BEGIN Proceso_inactivar
		dto = super.inactivar(dto, token);
		organizar(dto, token);
		return dto;
		// END Proceso_inactivar
	}

	public List<ProcesoDTO> consultarArbol(ProcesoFilterDTO dto) throws ServerException {
		// BEGIN region consultarArbol
		boolean onlyOne2ShowClient = false;
		if (dto.getFiltroParametro() != null && dto.getFiltroParametro().compareTo("*") == 0) {
			onlyOne2ShowClient = true;
			dto.setFiltroParametro(null);
		}
		List<ProcesoDTO> result = listarConsulta(dto);
		for (ProcesoDTO procesoDTO : result) {
			if (procesoDTO.getTipo().compareTo(ProcesoDTO.EJECUTOR) == 0
					&& procesoDTO.getEstado().compareTo(SharedConstants.STATE_ACTIVE) == 0) {
				procesoDTO = completarProceso(procesoDTO, dto.getSecurityToken());
			}
		}
		if (dto.getFiltroParametro() == null && !onlyOne2ShowClient) {
			ProcesoDTO resulDTO = ordenar(result);
			result = new ArrayList<ProcesoDTO>();
			result.add(resulDTO);
		}
		return result;
		// END region consultarArbol
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
		bd = completarProceso(bd, dto.getSecurityToken());
		return bd;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProcesoDTO guardar(ProcesoDTO dto, String token) throws ServerException {
		preConfigurar(dto);
		dto = super.guardar(dto, token);
		if (dto.getTipo().compareTo(ProcesoDTO.EJECUTOR) == 0)
			crearBasico(dto, null, token);
		return dto;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void organizar(ProcesoDTO dto, String token) throws ServerException {
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
						super.actualizar(campo, token);
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

	private void crearBasico(ProcesoDTO dto, String plantillainicial, String token) throws ServerException {
		ProcesoEstadoDTO estadoActivo = new ProcesoEstadoDTO();
		estadoActivo.setEstadoDocumento(SharedConstants.STATE_ACTIVE);
		estadoActivo.setTipo(ProcesoEstadoDTO.TIPO_ESTADO);
		estadoActivo.setProceso(dto.getLlaveTabla());
		estadoActivo.setNombre(dto.getNombre() + " ACTIVO");
		estadoActivo.setAvance(1);
		estadoActivo = estadoService.guardar(estadoActivo, token);

		PropiedadDTO propiedadModifcable = new PropiedadDTO();
		propiedadModifcable.setCampo(estadoActivo.getLlaveTabla());
		propiedadModifcable.setKey(Propiedades.MODIFICABLE);
		propiedadModifcable.setTipo(PropiedadValorDefinidoDTO.ESTADO);
		propiedadModifcable.setValor("T");
		propiedadModifcable.setMotivo("Permitir modificar los activos");
		propiedadService.guardar(propiedadModifcable, token);

		ProcesoEstadoDTO estadoInactivo = new ProcesoEstadoDTO();
		estadoInactivo.setEstadoDocumento(SharedConstants.STATE_INACTIVE);
		estadoInactivo.setTipo(ProcesoEstadoDTO.TIPO_ESTADO);
		estadoInactivo.setAvance(2);
		estadoInactivo.setProceso(dto.getLlaveTabla());
		estadoInactivo.setNombre(dto.getNombre() + "INACTIVO");
		estadoInactivo = estadoService.guardar(estadoInactivo, token);

		ProcesoTransicionDTO inicial = new ProcesoTransicionDTO();
		inicial.setEstadoLLegada(estadoActivo.getLlaveTabla());
		inicial.setNombre(dto.getNombre());
		inicial.setDocumentador(true);
		inicial.setProceso(dto.getLlaveTabla());
		transicionService.guardarConCodigo(inicial, dto.getCodigo(), plantillainicial, token);

		ProcesoTransicionDTO anular = new ProcesoTransicionDTO();
		anular.setEstadoPartida(estadoActivo.getLlaveTabla());
		anular.setEstadoLLegada(estadoInactivo.getLlaveTabla());
		anular.setNombre(dto.getNombre() + " - ANULAR");
		anular.setDocumentador(true);
		anular.setProceso(dto.getLlaveTabla());
		transicionService.guardarConCodigo(anular, "X" + dto.getCodigo(), null, token);

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
				//En universal sucedio que se creo una llamada al mismo proceso y se generaba un ciclo infinito
				if(ultimo.getLlaveTabla().compareTo(ultimo.getMacroproceso())!=0) {
					ProcesoDTO categoria = consultaXId(ultimo.getMacroproceso());
					if (categoria == null)
						throw new ServerException("No se encuentra la categoria principal. " + ultimo.getMacroproceso());
					 procesos.add(categoria);	
				}else {
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
		} else {
			if (categoria.getHijos() == null)
				return null;
			for (ProcesoDTO iCategoria : categoria.getHijos()) {
				ProcesoDTO busqueda = esPadre(iCategoria, llavePadre);
				if (busqueda != null)
					return busqueda;
			}
		}
		return null;
	}

	public ProcesoDTO crearDesdePlantilla(String plantilla, String codigo, String nombre, String objetivo, String token)
			throws ServerException {
		// BEGIN Proceso_guardar
		ProcesoFilterDTO filtroCantidad = new ProcesoFilterDTO();
		int cantidad = contarResultados(filtroCantidad);
		cantidad = cantidad + 1;
		ProcesoDTO dto = new ProcesoDTO();
		dto.setNombre(nombre);
		dto.setCodigo(codigo);
		dto.setTipo(ProcesoDTO.EJECUTOR);
		dto.setObjetivo(objetivo);
		dto.setPrioridad(cantidad);
		dto = super.guardar(dto, token);
		crearBasico(dto, plantilla, token);
		return dto;
		// END Proceso_guardar
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
		if (dto.getImagen() == null)
			dto.setImagen(SharedConstants.LOGO);
		ProcesoFilterDTO filtroCantidad = new ProcesoFilterDTO();
		int cantidad = contarResultados(filtroCantidad);
		cantidad = cantidad + 1;
		dto.setPrioridad(cantidad);
		validarMacroproceso(dto.getMacroproceso());
	}

	private ProcesoDTO completarProceso(ProcesoDTO proceso, String token) throws ServerException {
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

	public List<ProcesoDTO> getFullToSynchronize(List<String> process) throws ServerException {
		return procesoMapper.getFullToSynchronize(process);
	}

}