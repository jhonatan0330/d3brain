package com.softure.property.application;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.property.domain.RelacionInternaDTO;
import com.softure.property.domain.RelacionInternaFilterDTO;
import com.softure.property.infrastructure.RelacionInternaMapper;

import jakarta.annotation.PostConstruct;

@Service("relacionInternaService")
public class RelacionInternaSvc extends BasicSvc<RelacionInternaDTO, RelacionInternaFilterDTO> {
	
	@Autowired @Lazy private RelacionInternaMapper relacionInternaMapper;
	
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
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public RelacionInternaDTO actualizar( RelacionInternaDTO dto, String token) throws ServerException {
		String llaveTabla = dto.getLlaveTabla();
		dto = guardar(dto, token);
		RelacionInternaDTO inactivo = new RelacionInternaDTO();
		inactivo.setLlaveTabla(llaveTabla);
		inactivar(inactivo, token);
		return dto;
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public RelacionInternaDTO inactivar(RelacionInternaDTO dto, String token) throws ServerException {
		RelacionInternaDTO bd = consultaXId(dto.getLlaveTabla());
		bd.setUsuarioEliminacion(getUserFlex(token));
		bd.setFechaEliminacion(new Date());
		bd.setEstado(SharedConstants.STATE_INACTIVE);
		return super.update(bd);
	}
	
	

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public RelacionInternaDTO guardar(RelacionInternaDTO dto, String token) throws ServerException {
		if(dto.getAuxiliar()!=null && dto.getAuxiliar().length()==0) dto.setAuxiliar(null);
		RelacionInternaFilterDTO existeFilter = new RelacionInternaFilterDTO();
		existeFilter.setCampo(dto.getCampo());
		existeFilter.setPlantilla(dto.getPlantilla());
		existeFilter.setPropiedad(dto.getPropiedad());
		existeFilter.setAuxiliar(dto.getAuxiliar());
		existeFilter.setEstado(SharedConstants.STATE_ACTIVE);
		RelacionInternaDTO existe = consultaUnica(existeFilter);
		if(existe!=null) return existe;
		if(dto.getUsuarioCreacion()==null)dto.setUsuarioCreacion(getUserFlex(token));
		if(dto.getFechaInicio()==null)dto.setFechaInicio(new Date());
		
		
		String _templateOfField = relacionInternaMapper.getTemplateOfField(dto.getCampo());
		if(_templateOfField==null) return null;//En caso que sea un producto
		if(dto.getPlantilla().compareTo(_templateOfField)!=0) throw new ServerException("La plantilla no corresponde al campo escogido");
		
		if(dto.getFechaInicio()==null)dto.setFechaInicio(new Date());
		return super.guardar(dto, token);
	}

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

	public void copyFromProperty(String propertyIdOld, String propertyIdNew, String token, String pUserCreation, boolean mantainDateInitial) throws ServerException {
		List<RelacionInternaDTO> relations = relacionesPropiedad(propertyIdOld);
		if (relations != null && !relations.isEmpty()) {
			for (RelacionInternaDTO iRelation : relations) {
				RelacionInternaDTO newRelation = new RelacionInternaDTO();
				newRelation.setAuxiliar(iRelation.getAuxiliar());
				newRelation.setCampo(iRelation.getCampo());
				newRelation.setPlantilla(iRelation.getPlantilla());
				newRelation.setPropiedad(propertyIdNew);
				if(mantainDateInitial) {
					newRelation.setFechaInicio(iRelation.getFechaInicio());
					newRelation.setUsuarioCreacion(pUserCreation);
				}
				guardar(newRelation, token);
			}
		}
	}

}