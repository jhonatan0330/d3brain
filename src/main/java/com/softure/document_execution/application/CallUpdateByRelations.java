package com.softure.document_execution.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.DocumentoRelacionExpedienteDTO;
import com.softure.document_execution.domain.DocumentoRelacionExpedienteFilterDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.application.RelacionInternaSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.RelacionInternaDTO;

@Service
public class CallUpdateByRelations {

	@Autowired
	@Lazy
	private PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService;

	@Autowired
	@Lazy
	private RelacionInternaSvc relacionService;

	@Autowired
	@Lazy
	private CallDocumentListWithFilters listDocumentWithFiltersFunction;
	@Autowired
	@Lazy
	private DocumentoPlantillaCaracteristicaSvc documentoPlantillaCaracteristicaService;
	@Autowired
	@Lazy
	private PedidoVentaSvc pedidoService;
	@Autowired @Lazy 
	private DocumentoRelacionExpedienteSvc relacionExpedienteService;

	private String[] props = { Propiedades.RELACIONAR_DOCUMENTOS, Propiedades.RETIRAR_DOCUMENTOS };

	public void call(CallDocumentCRUD pCrud, PedidoVentaDTO pDTO, String pToken) throws ServerException {

		if (pDTO.getCaracteristicas() == null)
			return;

		List<PedidoVentaCaracteristicaDTO> _fieldWithProperties = new ArrayList<PedidoVentaCaracteristicaDTO>();
		for (PedidoVentaCaracteristicaDTO _fieldOfDTO : pDTO.getCaracteristicas()) {
			if (Propiedades.obtenerVariosParametro(_fieldOfDTO.getCampoDTO(), props) != null) {
				_fieldWithProperties.add(_fieldOfDTO);
			}
		}

		if (_fieldWithProperties.isEmpty())
			return;

		Map<String, PedidoVentaDTO> _documentsToUpdate = new java.util.HashMap<String, PedidoVentaDTO>();
		for (PedidoVentaCaracteristicaDTO _fieldOfDTO : pDTO.getCaracteristicas()) {
			relacionExternaDocumentos(_fieldOfDTO, pToken, _documentsToUpdate);
		}

		for (PedidoVentaDTO updateDocument : _documentsToUpdate.values()) {
			pCrud.updateWithoutTransaction(updateDocument, pDTO.getLlaveTabla(), pToken, true);
		}

	}

	private void relacionExternaDocumentos(PedidoVentaCaracteristicaDTO pCampo, String token,
			Map<String, PedidoVentaDTO> pDocumentsToUpdate) throws ServerException {

		List<PropiedadDTO> relacionExternaAgregar = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(), props);
		if (relacionExternaAgregar == null)
			return;
		if (pCampo.getDependientes() == null)
			throw new ServerException(
					"relacionado o retirando documentos no esta relacionado el dependiente que contiene el campo proceso que vamos a afectar");
		pedidoVentaCaracteristicaService.validarDependientes(pCampo.getCampoDTO(), pCampo.getDependientes());
		for (PropiedadDTO propiedadDTO : relacionExternaAgregar) {
			List<RelacionInternaDTO> relaciones = relacionService
					.relacionesPropiedad(propiedadDTO.getLlaveTabla());
			if (relaciones == null || relaciones.isEmpty())
				throw new ServerException("Revisa las relaciones de la propiedad " + propiedadDTO.getNombre()
						+ " del campo " + pCampo.getCampoDTO().getNombre());
			for (PedidoVentaCaracteristicaDTO dependiente : pCampo.getDependientes()) {
				if (dependiente.getCampo().compareTo(propiedadDTO.getValor()) == 0) {
					for (RelacionInternaDTO iRelacion : relaciones) {
						List<PedidoVentaCaracteristicaDTO> _fieldsDestinyToreplace =  new ArrayList<>();
						// Es un solo documento el que tengo que relacionar
						if(dependiente.getValorOpcion()!=null) {
							_fieldsDestinyToreplace.add(getFieldToRelateItem(dependiente.getValorOpcion(), iRelacion.getCampo()));
						}else {
							//Son varios documentos a los que le voy a relacionar
							for (PedidoVentaDTO _iProcess : dependiente.getExpedientes()) {
								//Posiblemenre debo mejorar los tipos de campo
								_fieldsDestinyToreplace.add(getFieldToRelateItem(_iProcess.getLlaveTabla(), iRelacion.getCampo()));
							}
						}
						
						for (PedidoVentaCaracteristicaDTO _fieldToReplace : _fieldsDestinyToreplace) {
							// Aqui sucedio en colegios, la plantilla curso se creo sin campo estudiantes y
							// se creo un curso, este no se asociaba porque no existia el campo destino.
							// toca dejarlo asi porque hay casos donde se salta esta validacion.
							
								_fieldToReplace.setTransaccionRegistro(pCampo.getTransaccionRegistro());
								_fieldToReplace.setCampoDTO(
										documentoPlantillaCaracteristicaService.consultaXId(_fieldToReplace.getCampo()));
								_fieldToReplace.setCampoDTO(documentoPlantillaCaracteristicaService
										.cargarComplementos(_fieldToReplace.getCampoDTO(), token));
								String campoValor = Propiedades.obtenerValor(_fieldToReplace.getCampoDTO(),
										Propiedades.PROCESO_VALOR);
								
								// Identificar si el campo destino es Multiple o no
								if(!Propiedades.obtenerValor(_fieldToReplace.getCampoDTO(),Propiedades.MULTIPLE).isEmpty()) {
									
									_fieldToReplace.setExpedientes(new ArrayList<>());
									if(_fieldToReplace.getLlaveTabla()!=null) {
										List<PedidoVentaDTO> actualDocuments = listDocumentWithFiltersFunction
												.listarExpedientesPertenecenCampo(_fieldToReplace.getLlaveTabla(), token, campoValor);
										if (actualDocuments != null && !actualDocuments.isEmpty())
											_fieldToReplace.getExpedientes().addAll(actualDocuments);	
									} else {
										_fieldToReplace.setValorText("0");
										pedidoVentaCaracteristicaService.saveSimple(_fieldToReplace);
										_fieldToReplace.setEstado(SharedConstants.STATE_ACTIVE);
										//Puedo evitar que el retirar se llame
									}
									
									for (PedidoVentaDTO iDocumentoRelacionar : pCampo.getExpedientes()) {
										if (propiedadDTO.getKey().compareTo(Propiedades.RELACIONAR_DOCUMENTOS) == 0) {
											PedidoVentaDTO _same_document = (pCampo.getCampo().equals(propiedadDTO.getValor()))
													? pCampo.getPrincipal()	: iDocumentoRelacionar;
											_fieldToReplace.getExpedientes().add(_same_document);
											if (campoValor.isEmpty())
												relacionExpedienteService.relacionarExpedienteDocumento(
														_fieldToReplace.getLlaveTabla(), _same_document.getLlaveTabla(), token,
														_fieldToReplace.getCampoDTO().getNombre(),
														(_same_document.getDinero() == null) ? null
																: _same_document.getDinero().getSaldo(),
														pCampo.getPrincipal().getLlaveTabla());
										} else {
											// Si utilizo foreach falla por collection modified
											for (int i = _fieldToReplace.getExpedientes().size() - 1; i >= 0; i--) {
												PedidoVentaDTO iExpediente = _fieldToReplace.getExpedientes().get(i);
												if (iExpediente.getLlaveTabla()
														.compareTo(iDocumentoRelacionar.getLlaveTabla()) == 0) {
													_fieldToReplace.getExpedientes().remove(iExpediente);
													
													 if(campoValor.isEmpty()) retirarExpedienteDocumento(_fieldToReplace,
													  iDocumentoRelacionar, (pCampo.getPrincipal()==null)?null:
													  pCampo.getPrincipal().getLlaveTabla(), token); break;
													 
												}
											}
										}
									}
									if (campoValor.isEmpty()) {
										_fieldToReplace.setValorText(String.valueOf(_fieldToReplace.getExpedientes().size()));
										pedidoVentaCaracteristicaService.update(_fieldToReplace);
									} else {
										PedidoVentaDTO updateDocument = pDocumentsToUpdate.get(dependiente.getValorOpcion());
										if (updateDocument == null) {
											updateDocument = pedidoService.consultaCompleta(dependiente.getValorOpcion(), token);
										}

										for (PedidoVentaCaracteristicaDTO iFieldUpdateDocument : updateDocument.getCaracteristicas()) {
											if (iFieldUpdateDocument.getCampo().compareTo(_fieldToReplace.getCampo()) == 0) {
												iFieldUpdateDocument.setModificado(true);
												iFieldUpdateDocument.setExpedientes(_fieldToReplace.getExpedientes());
												break;
											}
										}
										organizeDependsNumberToUpdate(_fieldToReplace, updateDocument);

										pDocumentsToUpdate.put(dependiente.getValorOpcion(), updateDocument);
									}
								}else {

									
									
									// Para campos vinculo que no se realacionaron poruq se modifico la estructura
									// de la plantilla
									// Esto lo hice rapido creo que debe tener mas elaboracion
									//DocumentoPlantillaCaracteristicaDTO _field = documentoPlantillaCaracteristicaService
									//		.consultaXId(iRelacion.getCampo());
									
									// Esta validacion la coloque porque al retirar me mostraba error y la idea es evitar las consultas si no se cumple
									/*if (propiedadDTO.getKey().compareTo(Propiedades.RELACIONAR_DOCUMENTOS) == 0 
											&& _fieldToReplace.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.VINCULO) == 0) {
										throw new ServerException("El campo destino " + _fieldToReplace.getCampoDTO().getNombre()
												+ " es de tipo vinculo, ya tiene un vinculo por eso no se puede relacionar con documentos");
									}*/
									
									if (_fieldToReplace.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.VINCULO) == 0
											&& dependiente.getExpedientes() != null && dependiente.getExpedientes().size() == 1) {
										
										_fieldToReplace.setDocumento(dependiente.getExpedientes().get(0).getLlaveTabla());

										//El escenario era en trustmetrans se creaba exito del rnec y en ese momento si creaba la asociacion
										if(dependiente.getExpedientes().get(0).getLlaveTabla().compareTo(pCampo.getExpedientes().get(0).getLlaveTabla())==0) {
											//En el manifiesto se realaciona el mismo campo, porque se quiere asociar el manifiesto nuevo creado
											_fieldToReplace.setValorOpcion(dependiente.getPrincipal().getLlaveTabla());
											_fieldToReplace.setValorText(dependiente.getPrincipal().getNombre());
										}else {
											// En la remesa ya existia la remesa y se asocia uno existente
											_fieldToReplace.setValorOpcion(pCampo.getExpedientes().get(0).getLlaveTabla());
											_fieldToReplace.setValorText(pCampo.getExpedientes().get(0).getNombre());
										}
										if(_fieldToReplace.getLlaveTabla()==null) {
											_fieldToReplace.setTransaccionRegistro(pCampo.getTransaccionRegistro());
											pedidoVentaCaracteristicaService.saveSimple(_fieldToReplace);	
										}else {
											//_fieldToReplace.setTransaccionRegistro(pCampo.getTransaccionRegistro());
											pedidoVentaCaracteristicaService.update(_fieldToReplace);
										}
										
									}
									
									// Para trustmetrans al agregar un recibo a factura se relacione
									if (_fieldToReplace.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.PROCESO) == 0) {

										if(dependiente.getExpedientes().get(0).getLlaveTabla().compareTo(pCampo.getExpedientes().get(0).getLlaveTabla())==0) {
											_fieldToReplace.setValorOpcion(dependiente.getPrincipal().getLlaveTabla());
											_fieldToReplace.setValorText(dependiente.getPrincipal().getNombre());
										}else {
											_fieldToReplace.setValorOpcion(pCampo.getValorOpcion());
											_fieldToReplace.setValorText(pCampo.getValorText());
										}
											
											
											if(_fieldToReplace.getLlaveTabla()==null) {
												_fieldToReplace.setTransaccionRegistro(pCampo.getTransaccionRegistro());
												pedidoVentaCaracteristicaService.saveSimple(_fieldToReplace);	
											}else {
												//_fieldToReplace.setTransaccionRegistro(pCampo.getTransaccionRegistro());
												pedidoVentaCaracteristicaService.update(_fieldToReplace);
											}
										
									}
								}
								
								
								
							
						}
					}
					break;
				}
			}
		}

	}

	// OJO SE DUPLICO EN CallDocumentUpdateFromAutomatic
	private void organizeDependsNumberToUpdate(PedidoVentaCaracteristicaDTO campoDestino,
			PedidoVentaDTO updateDocument) {
		for (PedidoVentaCaracteristicaDTO iFieldUpdateDocument : updateDocument.getCaracteristicas()) {
			List<PropiedadDTO> dependents = Propiedades.obtenerVariosParametro(iFieldUpdateDocument.getCampoDTO(),
					Propiedades.DEPENDENT_PROPS);
			if (dependents != null && !dependents.isEmpty()) {
				for (PropiedadDTO iDependent : dependents) {
					if (iDependent.getValor().compareTo(campoDestino.getCampo()) == 0) {
						if (!iFieldUpdateDocument.getModificado()) {
							iFieldUpdateDocument.setValorNumero(null);
							iFieldUpdateDocument.setModificado(true);
							// Lo repirto para que se calculen los que dependen de estos
							organizeDependsNumberToUpdate(iFieldUpdateDocument, updateDocument);
						}
						break;
					}
				}

			}
		}

	}
	
	//copiado de tipo proceso
	private boolean retirarExpedienteDocumento(PedidoVentaCaracteristicaDTO pCampo, PedidoVentaDTO procesoDTO,
			String pDocumentMainRetire, String token) throws ServerException {
		// Si es inactivo, busco la relacion del expediente y el campo
		DocumentoRelacionExpedienteFilterDTO filtroExpFilter = new DocumentoRelacionExpedienteFilterDTO();
		filtroExpFilter.setCampoMaestro(pCampo.getLlaveTabla());
		filtroExpFilter.setExpedienteDetalle(procesoDTO.getLlaveTabla());
		filtroExpFilter.setEstado(SharedConstants.STATE_ACTIVE);
		DocumentoRelacionExpedienteDTO filtroExp = relacionExpedienteService.consultaUnica(filtroExpFilter);
		if (filtroExp != null) {
			filtroExp.setDocumentoInactivo(pDocumentMainRetire);
			relacionExpedienteService.inactivar(filtroExp, token);
			return true;
		}
		return false;
	}
	
	private PedidoVentaCaracteristicaDTO getFieldToRelateItem(String pDocumentId, String pFieldId ) throws ServerException {
		PedidoVentaCaracteristicaFilterDTO campoDestinoFilter = new PedidoVentaCaracteristicaFilterDTO();
		campoDestinoFilter.setDocumento(pDocumentId);
		campoDestinoFilter.setCampo(pFieldId);
		campoDestinoFilter.setEstado(SharedConstants.STATE_ACTIVE);
		List<PedidoVentaCaracteristicaDTO> _fieldsDestinyToreplace = pedidoVentaCaracteristicaService
				.listarConsulta(campoDestinoFilter);
		if(_fieldsDestinyToreplace==null || _fieldsDestinyToreplace.isEmpty()) {
			//Si es nulo o no existe tengo que crearlos
			PedidoVentaCaracteristicaDTO _newFieldToRelation = new PedidoVentaCaracteristicaDTO();
			_newFieldToRelation.setDocumento(pDocumentId);	
			_newFieldToRelation.setCampo(pFieldId);	
			return _newFieldToRelation;
		} else {
			//A veces no entiendo porque se creaban muchos campos u eso generaba un error dificil de ideintificar
			return _fieldsDestinyToreplace.get(0);	
		}	
	}
}
