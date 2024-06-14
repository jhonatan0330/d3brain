package com.softure.process_designer.application;

import java.util.List;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.application.BasicSvc;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.process_designer.domain.ProcesoEstadoDTO;
import com.softure.process_designer.domain.ProcesoEstadoFilterDTO;
import com.softure.process_designer.domain.ProcesoTransicionFilterDTO;
import com.softure.process_designer.infrastructure.ProcesoEstadoMapper;
import com.softure.property.domain.PropiedadDTO;

@Service("procesoEstadoService")
public class ProcesoEstadoSvc extends BasicSvc<ProcesoEstadoDTO, ProcesoEstadoFilterDTO> {

	@Autowired
	private ProcesoEstadoMapper procesoEstadoMapper;

	// BEGIN region servicesProcesoEstado
	@Autowired
	private ProcesoTransicionSvc procesoTransicionService;
	// END region servicesProcesoEstado

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
	public ProcesoEstadoDTO activar(ProcesoEstadoDTO dto, String token) throws ServerException {
		// BEGIN ProcesoEstado_activar
		return super.activar(dto, token);
		// END ProcesoEstado_activar
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProcesoEstadoDTO actualizar(ProcesoEstadoDTO dto, String token) throws ServerException {
		// BEGIN ProcesoEstado_actualizar
		colocarSignoPregunta(dto);
		ProcesoEstadoDTO bd = consultaXId(dto.getLlaveTabla());
		if (bd.getEstadoDocumento().compareTo(dto.getEstadoDocumento()) != 0) {
			procesoEstadoMapper.actualizarEstados(dto);
		}
		return super.actualizar(dto, token);
		// END ProcesoEstado_actualizar
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProcesoEstadoDTO inactivar(ProcesoEstadoDTO dto, String token) throws ServerException {
		// BEGIN ProcesoEstado_inactivar
		ProcesoTransicionFilterDTO transicion = new ProcesoTransicionFilterDTO();
		transicion.setEstadoLLegada(dto.getLlaveTabla());
		transicion.setEstado(SharedConstants.STATE_ACTIVE);
		if (procesoTransicionService.contarResultados(transicion) != 0)
			throw new ServerException("Este estado es usada en varias transacciones activas");
		return super.inactivar(dto, token);
		// END ProcesoEstado_inactivar
	}

	@Override
	public ProcesoEstadoDTO consultaUnica(ProcesoEstadoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}

	@Override
	public int contarResultados(ProcesoEstadoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}

	@Override
	public List<ProcesoEstadoDTO> listarConsulta(ProcesoEstadoFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProcesoEstadoDTO guardar(ProcesoEstadoDTO dto, String token) throws ServerException {
		// BEGIN ProcesoEstado_guardar
		colocarSignoPregunta(dto);
		ProcesoEstadoDTO result = super.guardar(dto, token);
		//Esto hace fallar el sincronizador 
		/*if (dto.getTipo().compareTo(ProcesoEstadoDTO.TIPO_API) == 0) {
			createTransicionAPI(result, SharedConstants.OK, token);
			createTransicionAPI(result, SharedConstants.ERROR, token);
		}*/
		return result;
		// END ProcesoEstado_guardar
	}

	/*
	private void createTransicionAPI(ProcesoEstadoDTO result, String name, String token) throws ServerException {
		ProcesoTransicionDTO transition = new ProcesoTransicionDTO();
		transition.setEstadoPartida(result.getLlaveTabla());
		transition.setNombre(name);
		transition.setProceso(result.getProceso());
		procesoTransicionService.guardar(transition, token);
	}*/

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
			estado.setCodigo((estado.getNombre().length()>50)?estado.getNombre().substring(0,49):estado.getNombre());
		estado.setCodigo(SoftureUtil.formatFunction(estado.getCodigo()).toUpperCase());
	}

	public String obtenerResponsable(PropiedadDTO propiedad, String documento, String modificador, String token)
			throws ServerException {
		String responsable = null;
		try {
			responsable = procesoEstadoMapper.funcionAsignacion(SoftureUtil.formatFunction(propiedad.getLlaveTabla()),
					documento, modificador, token);
		} catch (Exception e) {
			ProcesoEstadoDTO pes = consultaXId(propiedad.getCampo());
			throw new ServerException(e.getMessage(),
					"Proceso : " + pes.getProcesoNombre() + "  Estado :" + pes.getNombre());
		}
		if (responsable == null) {
			ProcesoEstadoDTO pes = consultaXId(propiedad.getCampo());
			throw new ServerException("Revise porque la funcion de asignacion no trae ningun responsable",
					"Proceso : " + pes.getProcesoNombre() + "  Estado :" + pes.getNombre());
		}
		return responsable;
	}

	public List<ProcesoEstadoDTO> getFullToSynchronize(List<String> process) {
		return procesoEstadoMapper.getFullToSynchronize(process);
	}
// END region aditionalMethods

}