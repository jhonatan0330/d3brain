package com.softure.document_transition.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shared.domain.ServerException;
import com.softure.authentication.application.UsuarioAutenticacionSvc;
import com.softure.document_execution.application.CallDocumentCRUD;
import com.softure.document_execution.application.CallDocumentCommons;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.logisticpymes.domain.UsuarioDTO;
import com.softure.process_designer.domain.ProcesoTransicionDTO;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.application.RelacionInternaSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.property.domain.RelacionInternaDTO;

@Component
public class CallDocumentNewFromAutomatic {

	@Autowired
	private RelacionInternaSvc relacionService;
	@Autowired
	private DocumentoPlantillaSvc plantillaService;
	@Autowired
	private PropiedadSvc propiedadService;
	@Autowired
	private CallDocumentCRUD saveUpdateInactivateDocumentFunction;
	@Autowired
	private PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService;
	@Autowired
	private UsuarioAutenticacionSvc autenticacionService;

	public PedidoVentaDTO generateDocumentsFromAutomaticTask(ProcesoTransicionDTO transicion, PedidoVentaDTO documento,
			PedidoVentaDTO expedienteDTO, String transaccion, String token,
			PedidoVentaCaracteristicaDTO vieneAutomatica) throws ServerException {
		List<PedidoVentaCaracteristicaDTO> camposNuevos = new ArrayList<PedidoVentaCaracteristicaDTO>();
		camposNuevos.add(vieneAutomatica);
		return processNewFields(transicion, documento, transaccion, token, camposNuevos);
	}

	/**
	 * 
	 * @param transicion
	 * @param documento       Documento modificador que realiza la accion sobre el
	 *                        documetnto base
	 * @param expedienteDTO   Documento base que se esta fafectando con el proceso
	 * @param transaccion     Como reuso esto en los temporizadores automaticos
	 *                        entonces no viene transaccion
	 * @param token
	 * @param vieneAutomatica Machetazo toca dividir 2 funcione
	 * @return
	 * @throws ServerException
	 */
	public PedidoVentaDTO generateDocuments(ProcesoTransicionDTO transicion, PedidoVentaDTO documento,
			PedidoVentaDTO expedienteDTO, String transaccion, String token, int iterationNumber)
			throws ServerException {
		List<PedidoVentaCaracteristicaDTO> camposNuevos = new ArrayList<PedidoVentaCaracteristicaDTO>();
		// Por aqui voy cuando viene en una iteracion pero no se que otros caso pueda
		// intente refactor pero salio un aviso asi que deje quieto mientras
		if (transicion.getPlantilla() == null)
			return null;
		String user = getUserId(token);
		transicion.setPropiedades(propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.TRANSICION,
				transicion.getLlaveTabla(), null, user));
		String[] cars = { Propiedades.GENERA_DOCUMENTO_CAMPO, Propiedades.GENERA_DOCUMENTO_TEXTO,
				Propiedades.GENERA_DOCUMENTO_FUNCION_SQL, Propiedades.GENERA_DOCUMENTO_CAMPO_FROM_EXPEDIENTE, Propiedades.GENERA_DOCUMENTO_CAMPO_FROM_GENERADOR
				, Propiedades.GENERA_DOCUMENTO_DEL_RESULTADO_ITERACION};
		List<PropiedadDTO> camposGenerar = Propiedades.obtenerVariosParametro(transicion, cars);
		if (camposGenerar == null || camposGenerar.isEmpty())
			return null;
		for (PropiedadDTO iPropiedadDTO : camposGenerar) {
			switch (iPropiedadDTO.getKey()) {
			case Propiedades.GENERA_DOCUMENTO_CAMPO_FROM_GENERADOR:
				if (documento != null) {
					PedidoVentaCaracteristicaDTO campoPrincipal = copyFieldDocument(null, iPropiedadDTO.getValor());
					campoPrincipal.setValorOpcion(documento.getLlaveTabla());
					if (documento.getDinero() != null)
						// Importante para que coja valor porque va a consultar po BD y no tiene
						campoPrincipal.setValorNumero(documento.getDinero().getValorTotal());
					campoPrincipal.setPrincipal(documento);
					camposNuevos.add(campoPrincipal);
				}
				break;
			case Propiedades.GENERA_DOCUMENTO_CAMPO_FROM_EXPEDIENTE:
				if (expedienteDTO != null) {
					PedidoVentaCaracteristicaDTO campoPrincipal = copyFieldDocument(null, iPropiedadDTO.getValor());
					campoPrincipal.setValorOpcion(expedienteDTO.getLlaveTabla());
					if (expedienteDTO.getDinero() != null)
						campoPrincipal.setValorNumero(expedienteDTO.getDinero().getValorTotal());
					campoPrincipal.setPrincipal(expedienteDTO);
					camposNuevos.add(campoPrincipal);
					break;	
				}
			case Propiedades.GENERA_DOCUMENTO_CAMPO:
				List<RelacionInternaDTO> relaciones = relacionService.relacionesPropiedad(iPropiedadDTO.getLlaveTabla());
				if (relaciones == null || relaciones.isEmpty()) throw new ServerException("La propiedad " + iPropiedadDTO.getNombre() + "No tiene relaciones, usa las relaciones para identificar que campo deseas copiar");
				for (RelacionInternaDTO iRelacion : relaciones) {
					if (documento != null && documento.getPlantilla()!=null && iRelacion.getPlantilla().compareTo(documento.getPlantilla()) == 0) {
						camposNuevos.add(copyFieldDocument(CallDocumentCommons.obtenerValor(
								documento.getCaracteristicas(), iRelacion.getCampo()), iPropiedadDTO.getValor()));
					} else {
						if (expedienteDTO != null && expedienteDTO.getPlantilla() != null
								&& iRelacion.getPlantilla().compareTo(expedienteDTO.getPlantilla()) == 0) {
							// Solo consulto el documento cuando en realidad lo necesito, en general no
							// veien las caracteristicas
							if (expedienteDTO.getCaracteristicas() == null)
								expedienteDTO.setCaracteristicas(pedidoVentaCaracteristicaService.listar2Documento(
										expedienteDTO.getLlaveTabla(), expedienteDTO.getHistorico()));
							camposNuevos.add(copyFieldDocument(CallDocumentCommons
									.obtenerValor(expedienteDTO.getCaracteristicas(), iRelacion.getCampo()),
									iPropiedadDTO.getValor()));
						}
					}
				}
				break;
			case Propiedades.GENERA_DOCUMENTO_FUNCION_SQL:
				PedidoVentaCaracteristicaDTO campoGenerado = pedidoVentaCaracteristicaService
						.consultarSQLCampoGenerarDocumento(iPropiedadDTO.getLlaveTabla(),
								(expedienteDTO != null) ? expedienteDTO.getLlaveTabla() : null,
								(documento != null) ? documento.getLlaveTabla() : null);
				if (campoGenerado !=null) {
					List<RelacionInternaDTO> relations = relacionService.relacionesPropiedad(iPropiedadDTO.getLlaveTabla());
					if (relations == null || relations.isEmpty()) throw new ServerException("La propiedad " + iPropiedadDTO.getNombre() + "No tiene relaciones, usa las relaciones para identificar que campo deseas copiar");
					for (RelacionInternaDTO iRelacion : relations) {
						if (documento != null && iRelacion.getPlantilla().compareTo(transicion.getPlantilla()) == 0) {
							camposNuevos.add(copyFieldDocument(campoGenerado, iRelacion.getCampo()));
							break;
						}
					}					
				}
				break;
			case Propiedades.GENERA_DOCUMENTO_DEL_RESULTADO_ITERACION:
				PedidoVentaCaracteristicaDTO fieldNewFromIteration = copyFieldDocument(null, iPropiedadDTO.getValor());
				fieldNewFromIteration.setValorOpcion(documento.getLlaveTabla());
				fieldNewFromIteration.setValorText(documento.getNombre());
				fieldNewFromIteration.setValorFecha(documento.getFecha());
				fieldNewFromIteration.setValorNumero(documento.getConsecutivo());
				camposNuevos.add(fieldNewFromIteration);
				break;
			case Propiedades.GENERA_DOCUMENTO_TEXTO:
				String textValueToNewField = iPropiedadDTO.getValor();
				
				List<RelacionInternaDTO> relacionesNumber = relacionService.relacionesPropiedad(iPropiedadDTO.getLlaveTabla());
				if (relacionesNumber == null || relacionesNumber.isEmpty()) throw new ServerException("La propiedad " + iPropiedadDTO.getNombre() + "No tiene relaciones, usa las relaciones para identificar que campo deseas que contenga el consecutivo");
				PedidoVentaCaracteristicaDTO fieldNew = copyFieldDocument(null, relacionesNumber.get(0).getCampo());
				if (textValueToNewField.compareTo("#NUMBER") == 0) {
					fieldNew.setValorNumero(new BigDecimal(iterationNumber));
					fieldNew.setValorText(String.valueOf(iterationNumber));
				} else {
					fieldNew.setValorText(textValueToNewField);
				}
				camposNuevos.add(fieldNew);
				break;
			default:
				break;
			}
		}
		return processNewFields(transicion, documento, transaccion, token, camposNuevos);
	}

	private PedidoVentaDTO processNewFields(ProcesoTransicionDTO transicion, PedidoVentaDTO documento,
			String transaccion, String token, List<PedidoVentaCaracteristicaDTO> camposNuevos) throws ServerException {
		if (!camposNuevos.isEmpty()) {
			UsuarioDTO userAdmin = autenticacionService.getUserSystem();
			if(userAdmin==null ) throw new ServerException("Es indispensable configurar el usuario administrador");
			PedidoVentaDTO nuevo = new PedidoVentaDTO();
			nuevo.setCaracteristicas(new ArrayList<PedidoVentaCaracteristicaDTO>());
			nuevo.setPlantilla(transicion.getPlantilla());
			DocumentoPlantillaDTO pPlantilla = new DocumentoPlantillaDTO();
			pPlantilla.setLlaveTabla(transicion.getPlantilla());
			pPlantilla = plantillaService.obtenerCampos(pPlantilla, token);
			if (documento != null)
				nuevo.setTransaccion(documento.getTransaccion());
			for (DocumentoPlantillaCaracteristicaDTO iCampo : pPlantilla.getCaracteristicas()) {
				boolean relacionExistente = false;
				for (PedidoVentaCaracteristicaDTO iCampoCopiar : camposNuevos) {
					if (iCampo.getLlaveTabla().compareTo(iCampoCopiar.getCampo()) == 0) {
						nuevo.getCaracteristicas().add(copyFieldDocument(iCampoCopiar, iCampoCopiar.getCampo()));
						relacionExistente = true;
						break;
					}
				}
				if (!relacionExistente)
					nuevo.getCaracteristicas().add(copyFieldDocument(null, iCampo.getLlaveTabla()));
			}

			nuevo.setLlaveTabla(null);
			nuevo.setTransaccion(transaccion);
			nuevo.setFuncionario(userAdmin.getLlaveTabla());
			return saveUpdateInactivateDocumentFunction.saveWithoutTransaction(nuevo, token, true);
		} else {
			return null;
		}
	}

	private PedidoVentaCaracteristicaDTO copyFieldDocument(PedidoVentaCaracteristicaDTO actual, String campoId) {
		PedidoVentaCaracteristicaDTO nueva = new PedidoVentaCaracteristicaDTO();
		nueva.setCampo(campoId);
		if (actual != null) {
			nueva.setValorAuxiliar(actual.getValorAuxiliar());
			nueva.setValorFecha(actual.getValorFecha());
			nueva.setValorNumero(actual.getValorNumero());
			nueva.setValorOpcion(actual.getValorOpcion());
			nueva.setValorText(actual.getValorText());
			nueva.setExpedientes(actual.getExpedientes());
		}
		return nueva;
	}

	private String getUserId(String token) throws ServerException {
		return plantillaService.getUserFlex(token);
	}
}
