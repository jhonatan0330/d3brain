package com.softure.webservice.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.application.RelacionInternaSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.property.domain.RelacionInternaDTO;
import com.softure.shared.domain.SharedIdResponse;
import com.softure.webservice.domain.WebServiceDTO;

@Component
public class WebServiceCopyAPI {

	@Autowired
	private WebServiceSvc webServiceSvc;
	@Autowired
	private PropiedadSvc propiedadesSvc;
	@Autowired
	private RelacionInternaSvc relacionService;
	
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SharedIdResponse call(String serviceId, String token) throws ServerException {

		WebServiceDTO service = webServiceSvc.consultaXId(serviceId);
		if (service == null)
			throw new ServerException("El id del servicio no se encuentra en la BD." + serviceId);
		if (service.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO) != 0)
			throw new ServerException("El servicio " + service.getNombre() + " no se encuentra Activo." + serviceId);
		// Obtengo propiedades del servicio
		String userId = webServiceSvc.getUserFlex(token);
		service.setPropiedades(
				propiedadesSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.API_SERVICE, serviceId, null, userId));
		
		WebServiceDTO newAPi = new WebServiceDTO();
		newAPi.setCodigo(service.getCodigo() + "COPY");
		newAPi.setNombre(service.getNombre() + "COPY");
		newAPi.setTemplate(service.getTemplate());
		newAPi.setUrl(service.getUrl());
		
		newAPi = webServiceSvc.save(newAPi);
		if(service.getPropiedades()==null) return new SharedIdResponse(newAPi.getLlaveTabla());
		
		for (PropiedadDTO iProperty : service.getPropiedades()) {
			PropiedadDTO newProperty = new PropiedadDTO();
			newProperty.setBloqueo(iProperty.getBloqueo());
			newProperty.setCampo(newAPi.getLlaveTabla());
			//newProperty.setFechaDefinicion(new Date());
			newProperty.setKey(iProperty.getKey());
			newProperty.setMotivo(iProperty.getMotivo());
			newProperty.setNombre(iProperty.getNombre());
			newProperty.setPropiedadValor(iProperty.getPropiedadValor());
			newProperty.setRol(iProperty.getRol());
			newProperty.setRolExcluyente(iProperty.getRolExcluyente());
			newProperty.setTexto(iProperty.getTexto());
			newProperty.setTipo(iProperty.getTipo());
			newProperty.setUsuario(iProperty.getUsuario());
			newProperty.setUsuarioExcluyente(iProperty.getUsuarioExcluyente());
			newProperty.setValor(iProperty.getValor());
			newProperty = propiedadesSvc.guardar(newProperty, token);
			
			List<RelacionInternaDTO> relations = relacionService.relacionesPropiedad(iProperty.getLlaveTabla());
			if (relations != null && !relations.isEmpty()) {
				for (RelacionInternaDTO iRelation : relations) {
					RelacionInternaDTO newRelation = new RelacionInternaDTO();
					newRelation.setAuxiliar(iRelation.getAuxiliar());
					newRelation.setCampo(iRelation.getCampo());
					newRelation.setPlantilla(iRelation.getPlantilla());
					newRelation.setPropiedad(newProperty.getLlaveTabla());
					newRelation.setCambioCreacion(newProperty.getCambioCreacion());
					relacionService.save(newRelation);
				}
			}
		}

		return new SharedIdResponse(newAPi.getLlaveTabla());
	}

}
