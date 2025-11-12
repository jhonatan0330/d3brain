package com.softure.document_execution.application.field;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.document_execution.application.CallDocumentCRUD;
import com.softure.document_execution.application.CallDocumentCommons;
import com.softure.document_execution.application.CallDocumentListBySQLFunction;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_transition.application.CallDocumentUpdateFromAutomatic;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.property.application.RelacionInternaSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.RelacionInternaDTO;

@Component
public class TipoVinculo {

	@Autowired
	@Lazy
	private PedidoVentaCaracteristicaSvc campoService;
	@Autowired
	@Lazy
	private DocumentoPlantillaSvc plantillaService;
	@Autowired
	@Lazy
	private RelacionInternaSvc relationService;
	@Autowired
	@Lazy
	private PedidoVentaSvc documentService;
	@Autowired
	@Lazy
	private CallDocumentUpdateFromAutomatic updateDocumentFunction;
	@Autowired
	@Lazy
	private CallDocumentCRUD crudService;
	@Autowired @Lazy 
	private CallDocumentListBySQLFunction sqlFunctionService;
	@Autowired @Lazy 
	private DocumentoPlantillaCaracteristicaSvc caracteristicaService;

	public PedidoVentaCaracteristicaDTO guardarCampo(PedidoVentaCaracteristicaDTO pCampo, String token)
			throws ServerException {
		PedidoVentaCaracteristicaDTO bd = campoService.buscarActivo(pCampo, pCampo.getPrincipal().getHistorico());
		if (bd != null) {
			if (pCampo.getValorOpcion() == null) {
				bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
				bd.setPrincipal(pCampo.getPrincipal());
				campoService.inactivar(bd, token);
				return pCampo;
			} else {
				if (pCampo.getValorOpcion().compareTo(bd.getValorOpcion()) == 0) {
					return pCampo;
				} else {
					bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
					bd.setPrincipal(pCampo.getPrincipal());
					campoService.inactivar(bd, token);
				}
			}
		}
		if (pCampo.getValorOpcion() == null) {
			return pCampo;
		} else {
			return campoService.guardar(pCampo, token);
		}
	}

	public void cargarConsultaCampo(PedidoVentaCaracteristicaDTO pCampo) throws ServerException {
		if (pCampo.getValorOpcion() == null)
			return;
		pCampo.setExpedientes(new ArrayList<>());
		pCampo.getExpedientes().add(documentService.consultaXIdConDinero(pCampo.getValorOpcion()));
	}

	public PedidoVentaDTO doDocumentVinculate(PedidoVentaCaracteristicaDTO pCampo, String ptoken)
			throws ServerException {
		
		PropiedadDTO _functionSQl = Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.VINCULO_GET_PREVIOUS_SQL);
		
		PropiedadDTO _templateId = Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.VINCULO_DATA);
	
		if(_functionSQl !=null) {
			List<PedidoVentaDTO> _optionToLink =  sqlFunctionService.executeWithoutDetailDocument(pCampo.getCampoDTO(), pCampo.getDependientes(), null, _functionSQl);
			if(_optionToLink!=null && _optionToLink.size()> 1) {
				throw new ServerException( "El campo " + pCampo.getCampoDTO().getNombre() + " de la plantilla " + pCampo.getCampoDTO().getPlantillaNombre() +
						"El campo vinculo esta consultando si exsite un documento al cual vincular, pero la funcion devuelve muchos resultados, por favor revisa la funcion");
			}
			if(_optionToLink!=null && _optionToLink.size()== 1) {
				return _optionToLink.get(0);
			}
		}
		
		if (_templateId == null || _templateId.getValor().isEmpty()) {
			if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL) != null) {
				return null; // Si el campo es opcional, no se genera un documento, para los update
			} else {
				throw new ServerException("El campo " + pCampo.getCampoDTO().getNombre() + " de la plantilla " + pCampo.getCampoDTO().getPlantillaNombre() +
						"No encontramos la plantilla de vinculo, por favor valide la configuracion del campo");
			}
		}
		return generateDocumentToVinculate(pCampo, ptoken, _templateId, pCampo.getPrincipal().getLlaveTabla());
		
	}

	public void updateDocumentVinculate(PedidoVentaCaracteristicaDTO pCampo, String ptoken) throws ServerException {
		if (pCampo.getValorOpcion() == null)
			return;
		if (!pCampo.getModificado())
			return;
		PedidoVentaDTO procesoDTO = documentService.consultaXId(pCampo.getValorOpcion());
		campoService.validarDependientes(pCampo.getCampoDTO(), pCampo.getDependientes());
		updateDocumentFunction.executeFromBPM(pCampo, procesoDTO, ptoken,
				Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(), Propiedades.DEPENDE));
	}

	

	private PedidoVentaDTO generateDocumentToVinculate(PedidoVentaCaracteristicaDTO pCampo, String ptoken,
			PropiedadDTO _templateId, String pDocumentToRelationMain) throws ServerException {
		

		DocumentoPlantillaDTO pPlantilla = new DocumentoPlantillaDTO();
		pPlantilla.setLlaveTabla(_templateId.getValor());
		pPlantilla = plantillaService.obtenerCampos(pPlantilla, ptoken, false);

		List<PedidoVentaCaracteristicaDTO> _newFields = new ArrayList<PedidoVentaCaracteristicaDTO>();
		
		RelacionInternaDTO _relation = relationService.getFirstRelation(_templateId.getLlaveTabla(), null);
		if (_relation != null) {
			PedidoVentaCaracteristicaDTO _newField = new PedidoVentaCaracteristicaDTO();
			_newField.setCampo(_relation.getCampo());
			_newField.setValorOpcion(pDocumentToRelationMain);
			//_newField.setValorText((pCampo.getPrincipal().getDescripcion() != null) ? pCampo.getPrincipal().getDescripcion()
			//		: pCampo.getPrincipal().getNombre());
			_newFields.add(_newField);	
		}
		
		if (pCampo.getDependientes() != null && !pCampo.getDependientes().isEmpty()) {
			List<PropiedadDTO> _dependentsProperties = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(),
					Propiedades.DEPENDE);
			for (PedidoVentaCaracteristicaDTO _iDependentValue : pCampo.getDependientes()) {
				for (PropiedadDTO _iDependentProp : _dependentsProperties) {
					if (_iDependentValue.getCampo().compareTo(_iDependentProp.getValor()) == 0) {
						RelacionInternaDTO _relationDependent = relationService
								.getFirstRelation(_iDependentProp.getLlaveTabla(), null);
						if (_relationDependent == null) {
							throw new ServerException( "El campo " + pCampo.getCampoDTO().getNombre() + " de la plantilla " + pCampo.getCampoDTO().getPlantillaNombre() + 
									"No encontramos la RELACION de un dependiente que identifica en campo del documento");
						}
						_newFields.add(
								CallDocumentCommons.copyFieldDocument(_iDependentValue, _relationDependent.getCampo()));
					}
				}
			}
		}

		List<PropiedadDTO> _sqlProperties = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(),
				Propiedades.VINCULO_FIELD_SQL);
		if (_sqlProperties != null && !_sqlProperties.isEmpty()) {
			for (PropiedadDTO _iSqlProperty : _sqlProperties) {
				RelacionInternaDTO _relationSql = relationService.getFirstRelation(_iSqlProperty.getLlaveTabla(), null);
				if (_relationSql == null) {
					throw new ServerException("El campo " + pCampo.getCampoDTO().getNombre() + " de la plantilla " + pCampo.getCampoDTO().getPlantillaNombre() +
							"No encontramos la RELACION de un SQL que identifica en campo del documento");
				}
				PedidoVentaCaracteristicaDTO _newFieldSql = CallDocumentCommons.copyFieldDocument(pCampo,
						_relationSql.getCampo());
				List<PedidoVentaDTO> _resultsFunction = documentService.listarExpedientesDisponiblesDocumentoFuncion(
						null, _iSqlProperty.getLlaveTabla(), pCampo.getDependientes());
				if (_resultsFunction != null && !_resultsFunction.isEmpty()) {
					if (_resultsFunction.size() > 1) {
						throw new ServerException("La funcion SQL: " + _iSqlProperty.getLlaveTabla()
								+ " debe retornar un solo resultado, revise la configuracion");
					}
					_newFieldSql.setValorOpcion(_resultsFunction.get(0).getLlaveTabla());
				}
				_newFields.add(_newFieldSql);
			}
		}
		return CallDocumentCommons.generateNewDocument(pPlantilla, pCampo.getTransaccionRegistro(), ptoken, _newFields,
				pCampo.getPrincipal().getFuncionario());
	}

	public PedidoVentaDTO deleteDocumentToVinculate(PedidoVentaCaracteristicaDTO pCampo, String token)
			throws ServerException {

		PedidoVentaCaracteristicaDTO bd = campoService.buscarActivo(pCampo, pCampo.getPrincipal().getHistorico());
		if (bd == null)
			return null;
		
		PedidoVentaDTO document = documentService.consultaXId(bd.getValorOpcion());
		if(document.getEstado().compareTo(SharedConstants.STATE_ACTIVE)==0)
			return null;
		
		if (pCampo.getCampoDTO().getPropiedades() == null || pCampo.getCampoDTO().getPropiedades().isEmpty())
			pCampo.setCampoDTO(caracteristicaService.cargarComplementos(pCampo.getCampoDTO(), token));
		PropiedadDTO _templateId = Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.VINCULO_DELETE);
		if (_templateId == null) {
			throw new ServerException( "En el campo " + pCampo.getCampoDTO().getNombre() + " de la plantilla " + pCampo.getCampoDTO().getPlantillaNombre()+
					"No encontramos la plantilla de ELIMINAR vinculo, por favor valide la configuracion del campo");
		}
		return generateDocumentToVinculate(pCampo, token, _templateId, bd.getValorOpcion());
	}
}
