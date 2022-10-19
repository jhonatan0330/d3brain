package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.services.SoftureUtil;
// END region interImport
import com.softure.logisticpymes.infrastructure.mybatis.mapper.ProcesoEstadoMapper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.domain.dto.ProcesoEstadoDTO;
import com.softure.logisticpymes.domain.dto.PropiedadDTO;
import com.softure.logisticpymes.domain.filter.ProcesoEstadoFilterDTO;
import com.softure.logisticpymes.domain.filter.ProcesoTransicionFilterDTO;

@Service("procesoEstadoService")
public class ProcesoEstadoSvc extends BasicSvc<ProcesoEstadoDTO, ProcesoEstadoFilterDTO> {
	
	@Autowired
	private ProcesoEstadoMapper procesoEstadoMapper;
	
	// BEGIN region servicesProcesoEstado
	@Autowired	private ProcesoTransicionSvc procesoTransicionService;
	// END region servicesProcesoEstado

	@Override
	public ProcesoEstadoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. ProcesoEstado");
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
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProcesoEstadoDTO actualizar( ProcesoEstadoDTO dto, String token) throws ServerException {
		// BEGIN ProcesoEstado_actualizar
		colocarSignoPregunta(dto);
		ProcesoEstadoDTO bd = consultaXId(dto.getLlaveTabla());
		if(bd.getEstadoDocumento().compareTo(dto.getEstadoDocumento())!=0){
			procesoEstadoMapper.actualizarEstados(dto);
		}
		return super.actualizar(dto, token);
		// END ProcesoEstado_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProcesoEstadoDTO inactivar(ProcesoEstadoDTO dto, String token) throws ServerException {
		// BEGIN ProcesoEstado_inactivar
		ProcesoTransicionFilterDTO transicion = new ProcesoTransicionFilterDTO();
		transicion.setEstadoLLegada(dto.getLlaveTabla());
		transicion.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		if(procesoTransicionService.contarResultados(transicion)!=0) throw new ServerException("Este estado es usada en varias transacciones activas");
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
	public List<ProcesoEstadoDTO> listarConsulta(ProcesoEstadoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProcesoEstadoDTO guardar(ProcesoEstadoDTO dto, String token) throws ServerException {
		// BEGIN ProcesoEstado_guardar
		colocarSignoPregunta(dto);
		return super.guardar(dto, token);
		// END ProcesoEstado_guardar
	}

// BEGIN region aditionalMethods
	
	private void colocarSignoPregunta(ProcesoEstadoDTO estado )throws ServerException {
		if(estado==null) throw new ServerException("No se puede colocar el signo porque el dto es nulo");
		if(estado.getTipo()==null) throw new ServerException("No se puede colocar el signo porque el tipo del estado es nulo");
		if(estado.getTipo().compareTo(ProcesoEstadoDTO.TIPO_DECISION)==0) {
			if(estado.getNombre()==null) throw new ServerException("No se puede colocar el signo porque el nombre del estado es nulo");
			if(!estado.getNombre().endsWith("?")) estado.setNombre(estado.getNombre() + "?"); 
		}
	}
	
	public String obtenerResponsable(PropiedadDTO propiedad, String documento, String modificador, String token) throws ServerException {
		String responsable = null;
		try {
			responsable = procesoEstadoMapper.funcionAsignacion(SoftureUtil.formatFunction(propiedad.getLlaveTabla()), documento, modificador, token);
		} catch (Exception e) {
			ProcesoEstadoDTO pes = consultaXId(propiedad.getCampo());
			throw new ServerException(e.getMessage(), "Proceso : " + pes.getProcesoNombre() + "  Estado :"+ pes.getNombre());
		}
		if(responsable==null) throw new ServerException("Revise porque la funcion de asignacion no trae ningun responsable");
		return responsable;
	}
// END region aditionalMethods

}