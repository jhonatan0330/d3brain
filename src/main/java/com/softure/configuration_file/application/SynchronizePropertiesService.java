package com.softure.configuration_file.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.java.dto.exception.ServerException;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadDTO;

@Service
public class SynchronizePropertiesService {

	@Autowired
	private PropiedadSvc propertiesService;

	public void call(List<PropiedadDTO> propertiesFull, String entityRemote, String type, String entityLocal, String token)
			throws ServerException {

		List<PropiedadDTO> localPropertiesToErase = propertiesService.obtenerPropiedades(type, entityLocal, null, null);
		List<PropiedadDTO> propertiesRemote = filterPropertiesToTypeAndEntity(propertiesFull, type, entityRemote);
		// Saco un listado de las propiedades nuevas
		// Saco un listado de las propiedades a borrar
		if (propertiesRemote != null && !propertiesRemote.isEmpty()) {
			for (PropiedadDTO remoteProperty : propertiesRemote) {
				PropiedadDTO findProperty = findPropertyInList(localPropertiesToErase, remoteProperty.getPropiedadValor(), remoteProperty.getValor(), remoteProperty.getTexto());
				//Creo la nueva propiedad
				if(findProperty!= null) {
					localPropertiesToErase.remove(findProperty);
				} else {
					PropiedadDTO newProperty = new PropiedadDTO();
					newProperty.setCampo(entityLocal);
					newProperty.setMotivo(remoteProperty.getMotivo());
					newProperty.setPropiedadValor(remoteProperty.getPropiedadValor());
					newProperty.setRol(remoteProperty.getRol());
					newProperty.setRolExcluyente(remoteProperty.getRolExcluyente());
					if(remoteProperty.getTexto()==null) {
						newProperty.setValor(remoteProperty.getValor());
					}else {
						switch (remoteProperty.getPropiedadValor()) {
						case "PROP_140":
						case "PROP_189":
						case "PROP_185":
						case "PROP_212": {
							newProperty.setValor(remoteProperty.getValor());
							newProperty.setTexto(remoteProperty.getTexto());	
							break;
						}
						default:
							newProperty.setValor(remoteProperty.getTexto());
						}	
					}
					propertiesService.guardar(newProperty, token);
				}
			}
		}
		// elimino las propiedades que no estaban en la sincronizacion y no tenian
		// usuario especifico
		//for (PropiedadDTO propiedadDTO : localPropertiesToErase) {
		//	propertiesService.inactivar(propiedadDTO, token);
		//}

	}

	private List<PropiedadDTO> filterPropertiesToTypeAndEntity(List<PropiedadDTO> propertiesFull, String type, String entity) {
		if(propertiesFull==null || propertiesFull.isEmpty())return null;
		return propertiesFull.stream()
			      .filter(property -> (property.getCampo().compareTo(entity)==0 && property.getTipo().compareTo(type)==0))
			      .collect(Collectors.toList());
	}
	
	private PropiedadDTO findPropertyInList(List<PropiedadDTO> array, String code, String value, String text) {
		for (PropiedadDTO property : array) {
			if (code.compareTo(property.getPropiedadValor()) == 0) {
				if(property.getTexto()!=null && text!= null && property.getTexto().compareTo(text)==0)
					return property;
				if(property.getValor()!=null && property.getValor().compareTo(value)==0)
					return property;
			}
		}
		return null;
	}

}
