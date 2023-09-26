package com.softure.property.application;

import java.util.Date;
import java.util.List;

// BEGIN region interImport
import com.softure.java.cons.ConstantesGenerales;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.logisticpymes.application.CambioSvc;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.domain.RelacionInternaDTO;
import com.softure.property.domain.RelacionInternaFilterDTO;
import com.softure.property.infrastructure.RelacionInternaMapper;

@Service("relacionInternaService")
public class RelacionInternaSvc extends BasicSvc<RelacionInternaDTO, RelacionInternaFilterDTO> {
	
	@Autowired
	private RelacionInternaMapper relacionInternaMapper;
	
	// BEGIN region servicesRelacionInterna
	@Autowired private DocumentoPlantillaCaracteristicaSvc campoService;
	@Autowired private CambioSvc cambioService;
	// END region servicesRelacionInterna

	@Override
	public RelacionInternaDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. RelacionInterna");
		RelacionInternaFilterDTO dto = new RelacionInternaFilterDTO();
		dto.setLlaveTabla(llave);
		return relacionInternaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = relacionInternaMapper;
	}
	
	@Override
	public RelacionInternaDTO activar(RelacionInternaDTO dto, String token) throws ServerException {
		// BEGIN RelacionInterna_activar
		return super.activar(dto, token);
		// END RelacionInterna_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public RelacionInternaDTO actualizar( RelacionInternaDTO dto, String token) throws ServerException {
		// BEGIN RelacionInterna_actualizar
		String llaveTabla = dto.getLlaveTabla();
		dto = guardar(dto, token);
		RelacionInternaDTO inactivo = new RelacionInternaDTO();
		inactivo.setLlaveTabla(llaveTabla);
		inactivar(inactivo, token);
		return dto;
		// END RelacionInterna_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public RelacionInternaDTO inactivar(RelacionInternaDTO dto, String token) throws ServerException {
		// BEGIN RelacionInterna_inactivar
		RelacionInternaDTO bd = consultaXId(dto.getLlaveTabla());
		bd.setCambioEliminacion(cambioService.obtenerCambioGrabando(token).getLlaveTabla());
		bd.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
		bd = super.update(bd);
		return bd;
		// END RelacionInterna_inactivar
	}
	
	@Override
	public RelacionInternaDTO consultaUnica(RelacionInternaFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(RelacionInternaFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<RelacionInternaDTO> listarConsulta(RelacionInternaFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	public List<RelacionInternaDTO> listarRelacion(RelacionInternaFilterDTO dto)throws ServerException{
		// BEGIN region listarRelacion
		// @generated
		// END region listarRelacion
		paginar(dto);
		try {
			return relacionInternaMapper.listarRelacion(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public RelacionInternaDTO guardar(RelacionInternaDTO dto, String token) throws ServerException {
		// BEGIN RelacionInterna_guardar
		if(dto.getAuxiliar()!=null && dto.getAuxiliar().length()==0) dto.setAuxiliar(null);
		RelacionInternaFilterDTO existeFilter = new RelacionInternaFilterDTO();
		existeFilter.setCampo(dto.getCampo());
		existeFilter.setPlantilla(dto.getPlantilla());
		existeFilter.setPropiedad(dto.getPropiedad());
		existeFilter.setAuxiliar(dto.getAuxiliar());
		existeFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		RelacionInternaDTO existe = consultaUnica(existeFilter);
		if(existe!=null) return existe;
		if (dto.getCambioCreacion()==null)dto.setCambioCreacion(cambioService.obtenerCambioGrabando(token).getLlaveTabla());
		DocumentoPlantillaCaracteristicaDTO campo = campoService.consultaXId(dto.getCampo());
		if(campo==null) return null;//En caso que sea un producto
		if(dto.getPlantilla()==null) {
			dto.setPlantilla(campo.getPlantilla());
		}else {
			if(dto.getPlantilla().compareTo(campo.getPlantilla())!=0) throw new ServerException("La plantilla no corresponde al campo escogido");
		}
		if(dto.getFechaInicio()==null)dto.setFechaInicio(new Date());
		return super.guardar(dto, token);
		// END RelacionInterna_guardar
	}

// BEGIN region aditionalMethods
	public List<RelacionInternaDTO> relacionesPropiedad(String propiedad)throws ServerException {
		RelacionInternaFilterDTO filtro = new RelacionInternaFilterDTO();
		filtro.setPropiedad(propiedad);
		filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		return super.listarConsulta(filtro);
	}
	
	public List<RelacionInternaDTO> getRelationsFullToSynchronize()throws ServerException {
		return relacionInternaMapper.getRelationsFullToSynchronize();
	}
// END region aditionalMethods

}