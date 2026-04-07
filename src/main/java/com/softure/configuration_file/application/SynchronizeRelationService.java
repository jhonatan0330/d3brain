package com.softure.configuration_file.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.configuration_file.domain.LogConfigurationDTO;
import com.softure.java.services.SoftureUtil;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaFilterDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.application.RelacionInternaSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.RelacionInternaDTO;

@Service
public class SynchronizeRelationService {

	@Autowired @Lazy 
	private PropiedadSvc propertiesService;
	@Autowired @Lazy 
	private RelacionInternaSvc relationsService;
	@Autowired @Lazy 
	private DocumentoPlantillaCaracteristicaSvc fieldsService;
	@Autowired @Lazy 
	private DocumentoPlantillaSvc templateService;

	public void call(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log, boolean compare)
			throws ServerException {
		if (hierarchy.getProperties() == null || hierarchy.getProperties().isEmpty())
			return;
		if (hierarchy.getRelations() == null || hierarchy.getRelations().isEmpty())
			return;
		String templateRoot = log.getRoot() + " sincronizando las relaciones";
		for (PropiedadDTO remoteProperty : hierarchy.getProperties()) {
			if (remoteProperty.getUsuarioEliminacion() != null
					&& remoteProperty.getUsuarioEliminacion().compareTo("YA") == 0) {
				switch (remoteProperty.getPropiedadValor()) {
				case "PROP_06": // BODEGA_MOVIMIENTO
				case "PROP_33": // CAMPO_HEREDADO
				case "PROP_36": // PROCESO_ACCIONES
				case "PROP_82": // PERMISO_PLANTILLA_CAMPO_FILTRO
				case "PROP_96": // CAMPO_HEREDADO
				case "PROP_117": // MENSAJE DESTINATARIO
				case "PROP_121": // MODIFICAR_CAMPO
				case "PROP_140": // TEMPORIZADOR
				case "PROP_151": // GENERA_DOCUMENTO_CAMPO
				case "PROP_152": // RELACIONAR_DOCUMENTOS
				case "PROP_153": // RETIRAR_DOCUMENTOS
				case "PROP_159": // GENERA_DOCUMENTO_FUNCION_SQL
				case "PROP_173": // REEMPLAZAR DEL TEMPLATE CODIGO FORMULARIO
				case "PROP_174": // REEMPLAZAR DEL TEMPLATE CODIGO REFERENCIADO
				case "PROP_175": // REEMPLAZAR DEL TEMPLATE CODIGO ESPECIAL
				case "PROP_177": // REEMPLAZAR DEL TEMPLATE CODIGO GENERA ACCION
				case "PROP_186": // ALERTAR AL SELECCIONAR
				case "PROP_189": // TEMPORIZADOR
				case "PROP_192": // API_EXTRACTION
				case "PROP_202": // GENERA_DOCUMENTO_TEXTO
				case "PROP_225": // RELACIONAR DOCUMENTO Y CAMPO
				case "PROP_231": // ACTUALIZAR CAMPO INFORMATIVO
				case "PROP_235": // API_EXTRACTION_NO_ERROR
				case "PROP_240": // MENSAJE_ADJUNTO_URL
				case "PROP_238": // REEMPLAZAR CODIGO REFERENCIADO TIPO LISTA
				case "PROP_241": // MENSAJE_ADJUNTO_URL
				case "PROP_282": // AGREGAR EL DOCUMENTO CREADO A UN CAMPO
				case "PROP_288": // RELACIONAR DOCUMENTO AL CAMPO DEL NUEVO FORMULARIO
				case "PROP_292": // FORMULARIO ELIMINACION VINCULO	
				{
					// List<PropiedadDTO> localPropertiesToErase =
					// propertiesService.obtenerPropiedades(remoteProperty.getTipo(),
					// remoteProperty.getCambioCreacion(), remoteProperty.getKey(), null);
					PropiedadDTO findProperty = propertiesService.getByIdWithType(remoteProperty.getUsuarioCreacion());
					String _msgError = findProperty.getTipo() + ".... Propiedad " + findProperty.getNombre() +"("+ findProperty.getPropiedadValor() + ") ";
					if(findProperty.getTexto()!=null) {
						_msgError = _msgError + " Con texto "  +findProperty.getTexto();
					}else {
						_msgError = _msgError + " Con valor "  + SoftureUtil.recortar(findProperty.getValor(), 20);
					}
					if(findProperty.getMotivo()!=null)_msgError = _msgError + " con el motivo : " + findProperty.getMotivo();
					log.setRoot(templateRoot + _msgError);
					synchronizeRelations(hierarchy, findProperty, remoteProperty, token, log, compare);
					break;
				}
				default:
					break;
				}
			}
		}
	}

	private void synchronizeRelations(HierarchyExporterDTO hierarchy, PropiedadDTO newProperty,
			PropiedadDTO remoteProperty, String token, LogConfigurationDTO log, boolean compare)
			throws ServerException {
		if (newProperty == null) {
			log.error("Propiedad no se encuentra");
			return;
		}
		List<RelacionInternaDTO> localRelationsToErase = relationsService
				.relacionesPropiedad(newProperty.getLlaveTabla());
		List<RelacionInternaDTO> relationsRemote = findRelationsInList(hierarchy.getRelations(),
				remoteProperty.getLlaveTabla());
		if (relationsRemote != null && !relationsRemote.isEmpty()) {
			for (RelacionInternaDTO remoteRelation : relationsRemote) {
				RelacionInternaDTO findRelation = findRelationInList(localRelationsToErase,
						remoteRelation.getPlantillaCodigo(), remoteRelation.getCampoCodigo(), remoteRelation.getAuxiliar());
				if (findRelation != null) {
					localRelationsToErase.remove(findRelation);
					log.info("EXIST RELATION " + remoteRelation.getPlantillaNombre() + ".." + remoteRelation.getCampoNombre());
				} else {
					if (compare) {
						log.error("COMPARE NOT EXIST RELATION " + remoteRelation.getPlantillaNombre() + ".."
								+ remoteRelation.getCampoNombre());
					} else {
						
						DocumentoPlantillaCaracteristicaDTO _field = searchField(remoteRelation.getPlantillaCodigo(), remoteRelation.getCampoCodigo());
						
						if(_field==null) {
							log.error("RELACION NO CREADA POR CAMPO NO EXISTENTE (Plantilla: " +
									 remoteRelation.getPlantillaNombre() +" - " + remoteRelation.getPlantillaCodigo() + ". Campo: " + remoteRelation.getCampoNombre() +" - " + remoteRelation.getCampoCodigo()+ ")");
						} else {
							RelacionInternaDTO newRelation = new RelacionInternaDTO();
							newRelation.setAuxiliar(remoteRelation.getAuxiliar());
							newRelation.setCampo(_field.getLlaveTabla());
							newRelation.setPlantilla(_field.getPlantilla());
							newRelation.setPropiedad(newProperty.getLlaveTabla());
							try {
								newRelation = relationsService.guardar(newRelation, token);
								if (newRelation == null) {
									log.error("RELACION NO CREADA -" + remoteRelation.getPropiedad() + " - "
											+ remoteRelation.getPlantillaNombre() + ".." + remoteRelation.getCampoNombre());
								} else {
									log.info("NEW RELATION " + newRelation.getPlantillaNombre() + ".."
											+ newRelation.getCampoNombre());
								}
							} catch (Exception e) {
								log.error(remoteRelation.getPlantillaNombre() + ".." + remoteRelation.getCampoNombre()
										+ " : " + e.getMessage());
							}	
						}
						
					}

				}
			}
		}
	}

	private DocumentoPlantillaCaracteristicaDTO searchField(String plantillaCodigo, String campoCodigo) throws ServerException {
		DocumentoPlantillaDTO _template = templateService.consultarPorCodigo(plantillaCodigo);
		if(_template==null)	return null;
		DocumentoPlantillaCaracteristicaFilterDTO _filter = new DocumentoPlantillaCaracteristicaFilterDTO();
		_filter.setEstado(SharedConstants.STATE_ACTIVE);
		_filter.setPlantilla(_template.getLlaveTabla());
		_filter.setCodigo(campoCodigo);
		return fieldsService.consultaUnica(_filter);
	}

	private List<RelacionInternaDTO> findRelationsInList(List<RelacionInternaDTO> array, String property) {
		if (array == null || array.isEmpty())
			return null;
		return array.stream().filter(x -> (x.getPropiedad().compareTo(property) == 0)).collect(Collectors.toList());
	}

	private RelacionInternaDTO findRelationInList(List<RelacionInternaDTO> array, String template, String field,
			String auxiliar) {
		for (RelacionInternaDTO relation : array) {
			if (template.compareTo(relation.getPlantillaCodigo()) == 0) {
				if (relation.getCampoCodigo().compareTo(field) == 0) {
					if ((relation.getAuxiliar() == null && auxiliar == null) || (auxiliar != null
							&& relation.getAuxiliar() != null && auxiliar.compareTo(relation.getAuxiliar()) == 0))
						return relation;
				}
			}
		}
		return null;
	}
}
