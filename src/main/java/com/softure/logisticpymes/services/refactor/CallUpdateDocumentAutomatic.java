package com.softure.logisticpymes.services.refactor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softure.java.dto.exception.ServerException;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.domain.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaDTO;
import com.softure.logisticpymes.domain.dto.PropiedadDTO;
import com.softure.logisticpymes.domain.dto.RelacionInternaDTO;
import com.softure.logisticpymes.services.DocumentoPlantillaCaracteristicaSvc;
import com.softure.logisticpymes.services.DocumentoRelacionGestorSvc;
import com.softure.logisticpymes.services.PedidoVentaCaracteristicaSvc;
import com.softure.logisticpymes.services.PedidoVentaSvc;
import com.softure.logisticpymes.services.RelacionInternaSvc;

@Component
public class CallUpdateDocumentAutomatic {

	@Autowired
	private PedidoVentaSvc pedidoService;
	@Autowired
	private DocumentoPlantillaCaracteristicaSvc caracteristicaService;
	@Autowired
	private PedidoVentaCaracteristicaSvc campoService;
	@Autowired
	private RelacionInternaSvc relacionService;
	@Autowired
	private DocumentoRelacionGestorSvc relacionGestorService;
	@Autowired
	private CallCRUDDocument saveUpdateInactivateDocumentFunction;

	/*
	 * aqui tengo mil cosas mal, deberia priero armar lso campos y despues mandarlos
	 * con als relaciones la propiedad le modifico el valor porque esta pegado la
	 * implementacion del proceso Por el momento solo saca una extraccion , creo?
	 * Logimax guia Blue
	 */

	/**
	 * Mientras que idenrfico para que hago lo de las relaciones me toco este coso raro
	 * @param documentId
	 * @param updaterDocumentId
	 * @param propertiesToSearchFieldDestiny
	 * @param token
	 * @param transaction
	 * @param extractionText 
	 * @throws ServerException
	 */
	public void executeFromAPIExtraction(PedidoVentaDTO modificador,
			List<PropiedadDTO> propertiesToSearchFieldDestiny, String token, String extractionText)
			throws ServerException {
		//Cuando son servicios asincronos no hay un documento modificador?? de pronto afecte las extracciones
		if(modificador==null) return;
		//PedidoVentaDTO processDTO = pedidoService.consultaXId(documentId);
		Map<String, Object> extractionMap = SoftureUtil.createMaptoString(extractionText);
		// Necesito crear los campos para que se cargue
		List<PedidoVentaCaracteristicaDTO> generateFieldsFromProperty = new ArrayList<PedidoVentaCaracteristicaDTO>();
		for (PropiedadDTO propiedadDTO : propertiesToSearchFieldDestiny) {
			PedidoVentaCaracteristicaDTO newField = new PedidoVentaCaracteristicaDTO();
			if(extractionMap.get(propiedadDTO.getLlaveTabla()).getClass().getName().compareTo("java.lang.String")==0) {				
				newField.setValorText((String) extractionMap.get(propiedadDTO.getLlaveTabla()));
			}
			// campo
			List<RelacionInternaDTO> relations = relacionService.relacionesPropiedad(propiedadDTO.getLlaveTabla());
			for (RelacionInternaDTO iRelation : relations) {
				if (iRelation.getPlantilla().compareTo(modificador.getPlantilla()) == 0) {
					newField.setCampo(iRelation.getCampo());
					propiedadDTO.setValor(iRelation.getCampo()); // Para que hago esto??
				}
			}
			newField.setModificado(true);
			generateFieldsFromProperty.add(newField);
		}
		execute(generateFieldsFromProperty, modificador.getLlaveTabla(), modificador.getTransaccion(), modificador, token,
				propertiesToSearchFieldDestiny);
	}

	/*
	 * TEngo que buscar de donde viene esta funcion, creo que de generar reuniones o
	 * tambien de las facturas al aprobarlas en Sw42
	 */
	public void executeFromBPM(PedidoVentaCaracteristicaDTO pCampo, PedidoVentaDTO procesoDTO, String token,
			List<PropiedadDTO> modificarCampo) throws ServerException {
		execute(pCampo.getDependientes(), pCampo.getDocumento(), pCampo.getTransaccionRegistro(), procesoDTO, token,
				modificarCampo);
	}

	/**
	 * Se encarga de crear el documento de modificacion para que se aplique los
	 * cambios al documento principal
	 * 
	 * @param fieldsNewToInclude
	 * @param updaterDocumentId
	 * @param transaction
	 * @param procesoDTO
	 * @param token
	 * @param propertiesToSearchFieldDestiny Contiene las propiedades que generaron
	 *                                       la actualizacion deben tener llave,
	 *                                       valor donde llave es @see
	 *                                       {@link #getNewValues(List, String, List, List, List)}
	 * @throws ServerException
	 */
	private void execute(List<PedidoVentaCaracteristicaDTO> fieldsNewToInclude, String updaterDocumentId,
			String transaction, PedidoVentaDTO procesoDTO, String token,
			List<PropiedadDTO> propertiesToSearchFieldDestiny) throws ServerException {

		// hay un escenario en el que se modifica un campo del mismo formulario, ver logimax con guias blu
		// Lo que hago es borra los que tienen modificado false asi solo hago estas funciones cuando es necesario
		List<PedidoVentaCaracteristicaDTO> fieldsNewToIncludeActiveModify = new ArrayList<>();
		for (PedidoVentaCaracteristicaDTO pedidoVentaCaracteristicaDTO : fieldsNewToInclude) {
			if(pedidoVentaCaracteristicaDTO.getModificado())fieldsNewToIncludeActiveModify.add(pedidoVentaCaracteristicaDTO);
		}
		if(fieldsNewToIncludeActiveModify.isEmpty())return;
		
		PedidoVentaDTO updateDocument = new PedidoVentaDTO();
		updateDocument.setLlaveTabla(procesoDTO.getLlaveTabla());
		updateDocument.setEstadoExpediente(procesoDTO.getEstadoExpediente());

		List<DocumentoPlantillaCaracteristicaDTO> camposPlantilla = caracteristicaService
				.listarCamposPlantillaConComplementos(procesoDTO.getPlantilla(), token);

		List<PedidoVentaCaracteristicaDTO> currentFields = campoService.readCompleteFields(procesoDTO.getLlaveTabla(),
				camposPlantilla, procesoDTO.getHistorico());

		List<PedidoVentaCaracteristicaDTO> newFields = getNewValues(fieldsNewToIncludeActiveModify, procesoDTO,
				propertiesToSearchFieldDestiny, camposPlantilla, currentFields);

		if (hasChanges(currentFields, newFields)) {
			updateDocument.setTransaccion(transaction);
			updateDocument.setCaracteristicas(newFields);

			PedidoVentaDTO pedidoActualizado = saveUpdateInactivateDocumentFunction.updateWithoutTransaction(updateDocument,
					updaterDocumentId, token, true);
			procesoDTO.setNombre(pedidoActualizado.getNombre());
			// Cambie pCampo.getPrincipal().getLlaveTabla() x el que esta modificadndo creo
			// que eso funciona
			relacionarGestor(procesoDTO, updaterDocumentId, token, transaction);
		}
	}

	/**
	 * 
	 * @param dependientes
	 * @param processId
	 * @param generatorProperties Tiene las propiedades que generaron la
	 *                            modificacion, cada propiedad debe tener llave y
	 *                            valor donde el valor es el key del campo,
	 * @param templateFields      trae los campos actuales del documento
	 * @param currentFields
	 * @return
	 * @throws ServerException
	 */
	private List<PedidoVentaCaracteristicaDTO> getNewValues(List<PedidoVentaCaracteristicaDTO> dependientes,
			PedidoVentaDTO process, List<PropiedadDTO> generatorProperties,
			List<DocumentoPlantillaCaracteristicaDTO> templateFields, List<PedidoVentaCaracteristicaDTO> currentFields)
			throws ServerException {

		List<PedidoVentaCaracteristicaDTO> result = new ArrayList<PedidoVentaCaracteristicaDTO>();

		for (DocumentoPlantillaCaracteristicaDTO camposActualesDTO : templateFields) {
			PedidoVentaCaracteristicaDTO newField = null;
			for (PropiedadDTO codigo : generatorProperties) {
				List<RelacionInternaDTO> relaciones = relacionService.relacionesPropiedad(codigo.getLlaveTabla());
				DocumentoPlantillaCaracteristicaDTO campoCoincide = null;
				for (RelacionInternaDTO iRelacion : relaciones) {
					if (iRelacion.getCampo().compareTo(camposActualesDTO.getLlaveTabla()) == 0) {
						campoCoincide = camposActualesDTO;
						generatorProperties.remove(codigo);
						break;
					}
				}
				if (campoCoincide != null) {
					if (dependientes == null || dependientes.isEmpty())
						throw new ServerException("Tiene que registrar dependientes del campo "); // +
																									// pCampo.getCampoDTO().getNombre());
					// Aqui tengo un problema deberia antes mucho antes procesar lso campos que
					// quiero como
					// la otra funcion execute y despues enviarlo para homologar los metodos

					for (PedidoVentaCaracteristicaDTO iDependiente : dependientes) {
						if (iDependiente.getCampo().compareTo(codigo.getValor()) == 0) {
							newField = new PedidoVentaCaracteristicaDTO();
							newField.setCampo(camposActualesDTO.getLlaveTabla());
							newField.setCampoDTO(camposActualesDTO);
							newField.setDocumento(process.getLlaveTabla());
							newField.setValorAuxiliar(iDependiente.getValorAuxiliar());
							newField.setValorFecha(iDependiente.getValorFecha());
							newField.setValorNumero(iDependiente.getValorNumero());
							newField.setValorText(iDependiente.getValorText());
							newField.setValorOpcion(iDependiente.getValorOpcion());
							// Despues valido si se modifica o sigue igual en la funcionalidad de cada campo
							// por el momento debe tener permisos el usuario
							newField.setModificado(true);
							
							// Tengo que actualizar el modificador por un tema en el api que no guarda los cambios de los campos
							if(process.getCaracteristicas()!=null && !process.getCaracteristicas().isEmpty()) {
								for (PedidoVentaCaracteristicaDTO iCampoModificador : process.getCaracteristicas()) {
									if(iCampoModificador.getCampo().compareTo(camposActualesDTO.getLlaveTabla())==0) {
										//Por el momento solo texto
										iCampoModificador.setValorText(iDependiente.getValorText());
										break;
									}
								}
							}
							break;
						}
					}
					break;
				}
			}
			if (newField == null) {// Copio caracteristicas que existen
				for (PedidoVentaCaracteristicaDTO iActual : currentFields) {
					if (iActual.getCampo().compareTo(camposActualesDTO.getLlaveTabla()) == 0) {
						newField = iActual;
						break;
					}
				}
			}
			if (newField == null) {// En caso que no exista anteriormente la creo
				newField = new PedidoVentaCaracteristicaDTO();
				newField.setCampo(camposActualesDTO.getLlaveTabla());
				newField.setCampoDTO(camposActualesDTO);
				newField.setDocumento(process.getLlaveTabla());
			}
			result.add(newField);
		}
		return result;
	}

	// Esto deberia ir en save
	private void relacionarGestor(PedidoVentaDTO anterior, String updaterDocumentId, String securityToken,
			String transaction) throws ServerException {
		anterior = pedidoService.consultaXId(anterior.getLlaveTabla());
		System.out.format("\n(Colocar traza a documento...... %s)", anterior.getNombre());
		// Creo la relacion del documento Gestor
		relacionGestorService.trazar(anterior.getLlaveTabla(), updaterDocumentId, "Modificar Campos",
				anterior.getEstadoExpediente(), anterior.getEstadoExpediente(), null, null, securityToken, null,
				anterior.getHistorico(), transaction);
	}

	private boolean hasChanges(List<PedidoVentaCaracteristicaDTO> caracteristicasActuales,
			List<PedidoVentaCaracteristicaDTO> caracteristicasModificadas) {
		for (PedidoVentaCaracteristicaDTO iCampoModificado : caracteristicasModificadas) {
			if (iCampoModificado.getModificado()) {
				PedidoVentaCaracteristicaDTO campoComparar = null;
				for (PedidoVentaCaracteristicaDTO iCampoActual : caracteristicasActuales) {
					if (iCampoActual.getCampo().compareTo(iCampoModificado.getCampo()) == 0) {
						campoComparar = iCampoActual;
						break;
					}
				}
				if (campoComparar == null) {
					return true;
				}
				if ((iCampoModificado.getValorText()==null && campoComparar.getValorText()!=null)|| iCampoModificado.getValorText().compareTo(campoComparar.getValorText()) != 0) {
					return true;
				}
				// se que una funcion reusaria el codigo peor no se como hacerlo apra 3 tipos de
				// datos diferentes
				if (iCampoModificado.getValorOpcion() == null) {
					if (campoComparar.getValorOpcion() != null) {
						return true;
					}
				} else {
					if (campoComparar.getValorOpcion() == null) {
						return true;
					} else {
						if (iCampoModificado.getValorOpcion().compareTo(campoComparar.getValorOpcion()) != 0) {
							return true;
						}
					}
				}
				if (iCampoModificado.getValorNumero() == null) {
					if (campoComparar.getValorNumero() != null) {
						return true;
					}
				} else {
					if (campoComparar.getValorNumero() == null) {
						return true;
					} else {
						if (iCampoModificado.getValorNumero().compareTo(campoComparar.getValorNumero()) != 0) {
							return true;
						}
					}
				}
				if (iCampoModificado.getValorFecha() == null) {
					if (campoComparar.getValorFecha() != null) {
						return true;
					}
				} else {
					if (campoComparar.getValorFecha() == null) {
						return true;
					} else {
						if (iCampoModificado.getValorFecha().compareTo(campoComparar.getValorFecha()) != 0) {
							return true;
						}
					}
				}
				if (iCampoModificado.getValorAuxiliar() == null) {
					if (campoComparar.getValorAuxiliar() != null) {
						return true;
					}
				} else {
					if (campoComparar.getValorAuxiliar() == null) {
						return true;
					} else {
						if (iCampoModificado.getValorAuxiliar().compareTo(campoComparar.getValorAuxiliar()) != 0) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
