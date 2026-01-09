package com.softure.process_designer.application;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.process_designer.domain.ProcesoEstadoDTO;
import com.softure.process_designer.domain.ProcesoEstadoFilterDTO;
import com.softure.process_designer.domain.ProcesoTransicionFilterDTO;
import com.softure.process_designer.infrastructure.ProcesoEstadoMapper;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

import jakarta.annotation.PostConstruct;

@Service("procesoEstadoService")
public class ProcesoEstadoSvc extends BasicSvc<ProcesoEstadoDTO, ProcesoEstadoFilterDTO> {

	@Autowired
	@Lazy
	private ProcesoEstadoMapper procesoEstadoMapper;

	@Autowired
	@Lazy
	private ProcesoTransicionSvc procesoTransicionService;

	@Autowired
	@Lazy
	private PropiedadSvc parametroService;

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
	public ProcesoEstadoDTO actualizar(ProcesoEstadoDTO dto, String token) throws ServerException {
		colocarSignoPregunta(dto);
		ProcesoEstadoDTO bd = consultaXId(dto.getLlaveTabla());
		if (bd.getEstadoDocumento().compareTo(dto.getEstadoDocumento()) != 0) {
			procesoEstadoMapper.actualizarEstados(dto);
		}
		dto = super.actualizar(dto, token);
		organizar(dto, token);
		return dto;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProcesoEstadoDTO inactivar(ProcesoEstadoDTO dto, String token) throws ServerException {
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

		dto = super.inactivar(dto, token);
		organizar(dto, token);
		return dto;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProcesoEstadoDTO guardar(ProcesoEstadoDTO dto, String token) throws ServerException {
		colocarSignoPregunta(dto);
		ProcesoEstadoFilterDTO filtroCantidad = new ProcesoEstadoFilterDTO();
		filtroCantidad.setProceso(dto.getProceso());
		filtroCantidad.setEstado(SharedConstants.STATE_ACTIVE);
		int cantidadCampos = contarResultados(filtroCantidad);
		if (dto.getAvance() != null && dto.getAvance().compareTo(0) != 0) {
			cantidadCampos = dto.getAvance();
		} else {
			cantidadCampos = cantidadCampos + 1;
		}
		dto.setAvance(cantidadCampos);
		ProcesoEstadoDTO result = super.guardar(dto, token);
		// Esto hace fallar el sincronizador
		/*
		 * if (dto.getTipo().compareTo(ProcesoEstadoDTO.TIPO_API) == 0) {
		 * createTransicionAPI(result, SharedConstants.OK, token);
		 * createTransicionAPI(result, SharedConstants.ERROR, token); }
		 */
		colorHexAleatorio(result, token);
		return result;
	}

	private void organizar(ProcesoEstadoDTO pDTO, String pToken) throws ServerException {
		// Consulto todas las caracteristicas del documento
		ProcesoEstadoFilterDTO _filtro = new ProcesoEstadoFilterDTO();
		_filtro.setEstado(SharedConstants.STATE_ACTIVE);
		_filtro.setProceso(pDTO.getProceso());
		_filtro.setPaginacionRegistroFinal(500);
		List<ProcesoEstadoDTO> _estados = listarConsulta(_filtro);
		if (_estados != null && !_estados.isEmpty()) {
			int cont = 1;
			for (ProcesoEstadoDTO _iEstado : _estados) {
				if (_iEstado.getLlaveTabla().compareTo(pDTO.getLlaveTabla()) != 0) {
					// asumo que hay dos iguales entonces debo saltar un espacio y el que modifique
					// lo dejo quieto
					if (_iEstado.getAvance().compareTo(pDTO.getAvance()) == 0)
						cont++;
					if (_iEstado.getAvance() != cont) {
						_iEstado.setAvance(cont);
						super.actualizar(_iEstado, pToken);
					}
					cont++;
				} else {
					if (cont == pDTO.getAvance())
						cont++;
				}
			}
		}
		// Debo validar que las dependencias si se puedan
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
		estado.setCodigo(SoftureUtil.formatFunction(estado.getCodigo()).toUpperCase());
	}

	public void colorHexAleatorio(ProcesoEstadoDTO pState, String pToken) throws ServerException {
		if (pState == null)
			throw new ServerException("No se puede colocar el signo porque el dto es nulo");
		if (pState.getTipo() == null)
			throw new ServerException("No se puede colocar el signo porque el tipo del estado es nulo");
		if (pState.getTipo().compareTo(ProcesoEstadoDTO.TIPO_ESTADO) != 0)
			return;
		Random _random = new Random();
		int color = _random.nextInt(0x1000000); // 0x1000000 = 16777216
		parametroService.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.ESTADO, pState.getLlaveTabla(),
				Propiedades.COLOR, String.format("#%06X", color), pToken), pToken);
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