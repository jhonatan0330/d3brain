package com.softure.configuration_file.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.configuration_file.domain.LogConfigurationDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadDTO;

@Service
public class SynchronizePropertiesService {

	@Autowired
	private PropiedadSvc propertiesService;

	public void call(HierarchyExporterDTO hierarchy, String entityRemote, String type, String entityLocal, String token, LogConfigurationDTO log)
			throws ServerException {
		List<PropiedadDTO> localPropertiesToErase = propertiesService.obtenerPropiedades(type, entityLocal, null, null);
		List<PropiedadDTO> propertiesRemote = filterPropertiesToTypeAndEntity(hierarchy.getProperties(), type, entityRemote);
		// Saco un listado de las propiedades nuevas
		// Saco un listado de las propiedades a borrar
		if (propertiesRemote != null && !propertiesRemote.isEmpty()) {
			for (PropiedadDTO remoteProperty : propertiesRemote) {
				PropiedadDTO findProperty = findPropertyInList(localPropertiesToErase, remoteProperty.getPropiedadValor(), remoteProperty.getValor(), remoteProperty.getTexto(), remoteProperty.getRol());
				//Creo la nueva propiedad
				if(findProperty!= null) {
					localPropertiesToErase.remove(findProperty);
					log.info("EXIST " + remoteProperty.getPropiedadValor());
					remoteProperty.setCambioEliminacion("YA");
					remoteProperty.setCambioCreacion(findProperty.getLlaveTabla());
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
						case "PROP_29":
						case "PROP_37":
						case "PROP_41":
						case "PROP_51":
						case "PROP_54":
						case "PROP_58":
						case "PROP_59":
						case "PROP_69":
						case "PROP_74":
						case "PROP_90":
						case "PROP_118":
						case "PROP_120":
						case "PROP_122":
						case "PROP_125":
						case "PROP_139":
						case "PROP_140":
						case "PROP_146":
						case "PROP_147":
						case "PROP_156":
						case "PROP_159":
						case "PROP_160":
						case "PROP_164":
						case "PROP_175":
						case "PROP_182":
						case "PROP_187":
						case "PROP_189":
						case "PROP_185":
						case "PROP_192":
						case "PROP_212": 
						case "PROP_235":
						case "PROP_237":
						case "PROP_224":
						{
							newProperty.setValor(remoteProperty.getValor());
							newProperty.setTexto(remoteProperty.getTexto());	
							break;
						}
						default:
							newProperty.setValor(remoteProperty.getTexto());
						}	
					}
					try {
						findProperty = propertiesService.guardar(newProperty, token);
						log.info("NEW " + remoteProperty.getPropiedadValor());
						remoteProperty.setCambioEliminacion("YA");
						remoteProperty.setCambioCreacion(findProperty.getLlaveTabla());
					} catch (Exception e) {
						log.error(remoteProperty.getPropiedadValor() + " - " + remoteProperty.getValor() + " : " + e.getMessage());
						if(remoteProperty.getPropiedadValor().compareTo("PROP_242")==0) {
							log.error("NUL");
						}
					}
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
	
	private PropiedadDTO findPropertyInList(List<PropiedadDTO> array, String code, String value, String text, String role) {
		for (PropiedadDTO property : array) {
			if((role == null && property.getRol()==null) || (property.getRol()!=null && role!=null && role.compareTo(property.getRol())==0)) {
				if (code.compareTo(property.getPropiedadValor()) == 0) {
					if(property.getTexto()!=null && text!= null && property.getTexto().compareTo(text)==0)
						return property;
					if(property.getValor()!=null && property.getValor().compareTo(value)==0)
						return property;
				}	
			}
		}
		return null;
	}
	
}
