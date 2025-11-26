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
			for (PedidoVentaCaracteristicaDTO dependiente : pCampo.getDependientes()) {
				if (dependiente.getCampo().compareTo(propiedadDTO.getValor()) == 0) {
					List<RelacionInternaDTO> relaciones = relacionService
							.relacionesPropiedad(propiedadDTO.getLlaveTabla());
					if (relaciones == null || relaciones.isEmpty())
						throw new ServerException("Revisa las relaciones de la propiedad " + propiedadDTO.getNombre()
								+ " del campo " + pCampo.getCampoDTO().getNombre());
					for (RelacionInternaDTO iRelacion : relaciones) {
						PedidoVentaCaracteristicaFilterDTO campoDestinoFilter = new PedidoVentaCaracteristicaFilterDTO();
						campoDestinoFilter.setDocumento(dependiente.getValorOpcion());
						campoDestinoFilter.setCampo(iRelacion.getCampo());
						PedidoVentaCaracteristicaDTO campoDestino = pedidoVentaCaracteristicaService
								.consultaUnica(campoDestinoFilter);
						// Aqui sucedio en colegios, la plantilla curso se creo sin campo estudiantes y
						// se creo un curso, este no se asociaba porque no existia el campo destino.
						// toca dejarlo asi porque hay casos donde se salta esta validacion.
						if (campoDestino != null) {
							campoDestino.setTransaccionRegistro(pCampo.getTransaccionRegistro());
							campoDestino.setCampoDTO(
									documentoPlantillaCaracteristicaService.consultaXId(campoDestino.getCampo()));
							if (campoDestino.getCampoDTO().getFormato()
									.compareTo(DocumentoPlantillaCaracteristicaDTO.VINCULO) == 0) {
								throw new ServerException("El campo destino " + campoDestino.getCampoDTO().getNombre()
										+ " es de tipo vinculo, ya tiene un vinculo por eso no se puede relacionar con documentos");
							}
							campoDestino.setCampoDTO(documentoPlantillaCaracteristicaService
									.cargarComplementos(campoDestino.getCampoDTO(), token));
							String campoValor = Propiedades.obtenerValor(campoDestino.getCampoDTO(),
									Propiedades.PROCESO_VALOR);
							campoDestino.setExpedientes(new ArrayList<>());
							List<PedidoVentaDTO> actualDocuments = listDocumentWithFiltersFunction
									.listarExpedientesPertenecenCampo(campoDestino.getLlaveTabla(), token, campoValor);
							if (actualDocuments != null && !actualDocuments.isEmpty())
								campoDestino.getExpedientes().addAll(actualDocuments);
							for (PedidoVentaDTO iDocumentoRelacionar : pCampo.getExpedientes()) {
								if (propiedadDTO.getKey().compareTo(Propiedades.RELACIONAR_DOCUMENTOS) == 0) {
									PedidoVentaDTO _same_document = (pCampo.getCampo().equals(propiedadDTO.getValor()))
											? pCampo.getPrincipal()
											: iDocumentoRelacionar;
									campoDestino.getExpedientes().add(_same_document);
									if (campoValor.isEmpty())
										relacionExpedienteService.relacionarExpedienteDocumento(
												campoDestino.getLlaveTabla(), _same_document.getLlaveTabla(), token,
												campoDestino.getCampoDTO().getNombre(),
												(_same_document.getDinero() == null) ? null
														: _same_document.getDinero().getSaldo(),
												pCampo.getPrincipal().getLlaveTabla());
								} else {
									// Si utilizo foreach falla por collection modified
									for (int i = campoDestino.getExpedientes().size() - 1; i >= 0; i--) {
										PedidoVentaDTO iExpediente = campoDestino.getExpedientes().get(i);
										if (iExpediente.getLlaveTabla()
												.compareTo(iDocumentoRelacionar.getLlaveTabla()) == 0) {
											campoDestino.getExpedientes().remove(iExpediente);
											
											 if(campoValor.isEmpty()) retirarExpedienteDocumento(campoDestino,
											  iDocumentoRelacionar, (pCampo.getPrincipal()==null)?null:
											  pCampo.getPrincipal().getLlaveTabla(), token); break;
											 
										}
									}
								}
							}
							if (campoValor.isEmpty()) {
								campoDestino.setValorText(String.valueOf(campoDestino.getExpedientes().size()));
								pedidoVentaCaracteristicaService.update(campoDestino);
							} else {
								PedidoVentaDTO updateDocument = pDocumentsToUpdate.get(dependiente.getValorOpcion());
								if (updateDocument == null) {
									updateDocument = pedidoService.consultaCompleta(dependiente.getValorOpcion(),
											token);

								}

								for (PedidoVentaCaracteristicaDTO iFieldUpdateDocument : updateDocument
										.getCaracteristicas()) {
									if (iFieldUpdateDocument.getCampo().compareTo(campoDestino.getCampo()) == 0) {
										iFieldUpdateDocument.setModificado(true);
										iFieldUpdateDocument.setExpedientes(campoDestino.getExpedientes());
										break;
									}
								}
								organizeDependsNumberToUpdate(campoDestino, updateDocument);

								pDocumentsToUpdate.put(dependiente.getValorOpcion(), updateDocument);
							}
						} else {
							// Para campos vinculo que no se realacionaron poruq se modifico la estructura
							// de la plantilla
							// Esto lo hice rapido creo que debe tener mas elaboracion
							DocumentoPlantillaCaracteristicaDTO _field = documentoPlantillaCaracteristicaService
									.consultaXId(iRelacion.getCampo());
							if (_field.getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.VINCULO) == 0
									&& dependiente.getExpedientes() != null && dependiente.getExpedientes().size() == 1
									&& _field.getPlantilla()
											.compareTo(dependiente.getExpedientes().get(0).getPlantilla()) == 0) {
								campoDestino = new PedidoVentaCaracteristicaDTO();
								campoDestino.setCampo(_field.getLlaveTabla());
								campoDestino.setDocumento(dependiente.getExpedientes().get(0).getLlaveTabla());
								campoDestino.setValorOpcion(dependiente.getPrincipal().getLlaveTabla());
								campoDestino.setValorText(dependiente.getPrincipal().getNombre());
								campoDestino.setTransaccionRegistro(pCampo.getTransaccionRegistro());
								pedidoVentaCaracteristicaService.saveSimple(campoDestino);
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
}
