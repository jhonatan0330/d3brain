package com.softure.configuration_file.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.configuration_file.domain.LogConfigurationDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.application.RelacionInternaSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.RelacionInternaDTO;

@Service
public class SynchronizeRelationService {

	@Autowired
	private PropiedadSvc propertiesService;
	@Autowired
	private RelacionInternaSvc relationsService;

	public void call(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log)
			throws ServerException {
		if(hierarchy.getProperties()==null || hierarchy.getProperties().isEmpty()) return;
		if(hierarchy.getRelations()==null || hierarchy.getRelations().isEmpty()) return;
		log.setRoot("SynchronizeRelationService");
		for (PropiedadDTO remoteProperty : hierarchy.getProperties()) {
			if(remoteProperty.getCambioEliminacion()!=null && remoteProperty.getCambioEliminacion().compareTo("YA")==0) {
				switch (remoteProperty.getPropiedadValor()) {
				case "PROP_33": //CAMPO_HEREDADO
				case "PROP_36": //PROCESO_ACCIONES
				case "PROP_82": //PERMISO_PLANTILLA_CAMPO_FILTRO
				case "PROP_96": //CAMPO_HEREDADO
				case "PROP_117": //MENSAJE DESTINATARIO
				case "PROP_121": //MODIFICAR_CAMPO
				case "PROP_140": //TEMPORIZADOR
				case "PROP_151": //GENERA_DOCUMENTO_CAMPO
				case "PROP_152": //RELACIONAR_DOCUMENTOS
				case "PROP_153": //RETIRAR_DOCUMENTOS
				case "PROP_159": //GENERA_DOCUMENTO_FUNCION_SQL
				case "PROP_173": //REEMPLAZAR DEL TEMPLATE CODIGO FORMULARIO
				case "PROP_174": //REEMPLAZAR DEL TEMPLATE CODIGO REFERENCIADO
				case "PROP_175": //REEMPLAZAR DEL TEMPLATE CODIGO ESPECIAL
				case "PROP_177": //REEMPLAZAR DEL TEMPLATE CODIGO GENERA ACCION
				case "PROP_186": //ALERTAR AL SELECCIONAR
				case "PROP_189": //TEMPORIZADOR
				case "PROP_192": //API_EXTRACTION
				case "PROP_202": //GENERA_DOCUMENTO_TEXTO
				case "PROP_225": //RELACIONAR DOCUMENTO Y CAMPO
				case "PROP_231": //ACTUALIZAR CAMPO INFORMATIVO
				case "PROP_235": //API_EXTRACTION_NO_ERROR
				case "PROP_240": //MENSAJE_ADJUNTO_URL
				case "PROP_238": //REEMPLAZAR CODIGO REFERENCIADO TIPO LISTA
				case "PROP_241": //MENSAJE_ADJUNTO_URL
				{
					//List<PropiedadDTO> localPropertiesToErase = propertiesService.obtenerPropiedades(remoteProperty.getTipo(), remoteProperty.getCambioCreacion(), remoteProperty.getKey(), null);
					PropiedadDTO findProperty = propertiesService.consultaXId(remoteProperty.getCambioCreacion());
					synchronizeRelations(hierarchy, findProperty, remoteProperty, token, log);
				break;
				}
				default:
					break;
				}
			}
		}
	}

	private void synchronizeRelations(HierarchyExporterDTO hierarchy, PropiedadDTO newProperty, PropiedadDTO remoteProperty,
			String token, LogConfigurationDTO log) throws ServerException {
		if(newProperty==null) {
			log.error("Propiedad no se encuentra");
			return;
		}
		List<RelacionInternaDTO> localRelationsToErase = relationsService.relacionesPropiedad(newProperty.getLlaveTabla());
		List<RelacionInternaDTO> relationsRemote = findRelationsInList(hierarchy.getRelations(), remoteProperty.getLlaveTabla());
		if (relationsRemote != null && !relationsRemote.isEmpty()) {
			for (RelacionInternaDTO remoteRelation : relationsRemote) {
				RelacionInternaDTO findRelation = findRelationInList(localRelationsToErase, remoteRelation.getPlantilla(), remoteRelation.getCampo(), remoteRelation.getAuxiliar());
				if(findRelation!= null) {
					localRelationsToErase.remove(findRelation);
					log.info("EXIST " + remoteRelation.getPlantillaNombre() + ".." +  remoteRelation.getCampoNombre());
				} else {
					RelacionInternaDTO newRelation = new RelacionInternaDTO();
					newRelation.setAuxiliar(remoteRelation.getAuxiliar());
					newRelation.setCampo(remoteRelation.getCampo());
					newRelation.setPlantilla(remoteRelation.getPlantilla());
					newRelation.setPropiedad(newProperty.getLlaveTabla());
					try {
						newRelation = relationsService.guardar(newRelation, token);
						if(newRelation == null) {
							log.error("RELACION NO CREADA -"+ remoteRelation.getPropiedad() + " - " + remoteRelation.getPlantillaNombre() + ".." +  remoteRelation.getCampoNombre());
						}else {
							log.info("NEW " + newRelation.getPlantillaNombre() + ".." +  newRelation.getCampoNombre());	
						}
					} catch (Exception e) {
						log.error(remoteRelation.getPlantillaNombre() + ".." +  remoteRelation.getCampoNombre()+ " : " + e.getMessage());
					}
					
				}
			}
		}
	}
	
	private List<RelacionInternaDTO> findRelationsInList(List<RelacionInternaDTO> array, String property) {
		if(array==null || array.isEmpty())return null;
		return array.stream()
			      .filter(x -> (x.getPropiedad().compareTo(property)==0))
			      .collect(Collectors.toList());
	}

	private RelacionInternaDTO findRelationInList(List<RelacionInternaDTO> array, String template, String field, String auxiliar) {
		for (RelacionInternaDTO relation : array) {
			if (template.compareTo(relation.getPlantilla()) == 0) {
				if(relation.getCampo().compareTo(field)==0) {
					if((relation.getAuxiliar()==null && auxiliar==null)|| (auxiliar!=null && relation.getAuxiliar()!=null && auxiliar.compareTo(relation.getAuxiliar())==0))
					return relation;
				}
			}	
		}
		return null;
	}
}
