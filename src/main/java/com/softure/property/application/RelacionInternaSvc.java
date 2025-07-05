package com.softure.property.application;

import java.util.Date;
import java.util.List;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.logisticpymes.application.BasicSvc;
import com.softure.logisticpymes.application.CambioSvc;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.domain.RelacionInternaDTO;
import com.softure.property.domain.RelacionInternaFilterDTO;
import com.softure.property.infrastructure.RelacionInternaMapper;

import jakarta.annotation.PostConstruct;

@Service("relacionInternaService")
public class RelacionInternaSvc extends BasicSvc<RelacionInternaDTO, RelacionInternaFilterDTO> {
	
	@Autowired @Lazy 
	private RelacionInternaMapper relacionInternaMapper;
	
	// BEGIN region servicesRelacionInterna
	@Autowired @Lazy  private DocumentoPlantillaCaracteristicaSvc campoService;
	@Autowired @Lazy  private CambioSvc cambioService;
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
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
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
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public RelacionInternaDTO inactivar(RelacionInternaDTO dto, String token) throws ServerException {
		// BEGIN RelacionInterna_inactivar
		RelacionInternaDTO bd = consultaXId(dto.getLlaveTabla());
		bd.setCambioEliminacion(cambioService.obtenerCambioGrabando(token).getLlaveTabla());
		bd.setEstado(SharedConstants.STATE_INACTIVE);
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
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public RelacionInternaDTO guardar(RelacionInternaDTO dto, String token) throws ServerException {
		// BEGIN RelacionInterna_guardar
		if(dto.getAuxiliar()!=null && dto.getAuxiliar().length()==0) dto.setAuxiliar(null);
		RelacionInternaFilterDTO existeFilter = new RelacionInternaFilterDTO();
		existeFilter.setCampo(dto.getCampo());
		existeFilter.setPlantilla(dto.getPlantilla());
		existeFilter.setPropiedad(dto.getPropiedad());
		existeFilter.setAuxiliar(dto.getAuxiliar());
		existeFilter.setEstado(SharedConstants.STATE_ACTIVE);
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
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		return super.listarConsulta(filtro);
	}
	
	public RelacionInternaDTO getFirstRelation(String pProperty, String pOptionalTemplateId)throws ServerException {
		List<RelacionInternaDTO> _relations = relacionesPropiedad(pProperty);
		if (_relations == null || _relations.isEmpty()) {
			return null;
		}
		
		if (pOptionalTemplateId == null || pOptionalTemplateId.isEmpty()) {
			return _relations.get(0);
		} else {
			for (RelacionInternaDTO iRelation : _relations) {
				if (iRelation.getPlantilla().compareTo(pOptionalTemplateId) == 0) {
					return iRelation;
				}
			}
		}
		return null;
	}
	
	public List<RelacionInternaDTO> getRelationsFullToSynchronize()throws ServerException {
		return relacionInternaMapper.getRelationsFullToSynchronize();
	}

	public void copyFromProperty(String propertyIdOld, String propertyIdNew, String token, String creationChangeId, boolean mantainDateInitial) throws ServerException {
		List<RelacionInternaDTO> relations = relacionesPropiedad(propertyIdOld);
		if (relations != null && !relations.isEmpty()) {
			for (RelacionInternaDTO iRelation : relations) {
				RelacionInternaDTO newRelation = new RelacionInternaDTO();
				newRelation.setAuxiliar(iRelation.getAuxiliar());
				newRelation.setCampo(iRelation.getCampo());
				newRelation.setPlantilla(iRelation.getPlantilla());
				newRelation.setPropiedad(propertyIdNew);
				if(!mantainDateInitial) newRelation.setFechaInicio(iRelation.getFechaInicio());
				newRelation.setCambioCreacion(creationChangeId);
				guardar(newRelation, token);
			}
		}
	}

}