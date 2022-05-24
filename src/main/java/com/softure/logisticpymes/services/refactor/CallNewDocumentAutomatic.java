package com.softure.logisticpymes.services.refactor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.dto.DocumentoPlantillaDTO;
import com.softure.logisticpymes.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.dto.PedidoVentaDTO;
import com.softure.logisticpymes.dto.ProcesoTransicionDTO;
import com.softure.logisticpymes.dto.PropiedadDTO;
import com.softure.logisticpymes.dto.PropiedadValorDefinidoDTO;
import com.softure.logisticpymes.dto.RelacionInternaDTO;
import com.softure.logisticpymes.services.DocumentoPlantillaSvc;
import com.softure.logisticpymes.services.PedidoVentaCaracteristicaSvc;
import com.softure.logisticpymes.services.PropiedadSvc;
import com.softure.logisticpymes.services.RelacionInternaSvc;
import com.softure.logisticpymes.services.adapter.Propiedades;

@Component
public class CallNewDocumentAutomatic {

	@Autowired
	private RelacionInternaSvc relacionService;
	@Autowired
	private DocumentoPlantillaSvc plantillaService;
	@Autowired
	private PropiedadSvc propiedadService;
	@Autowired
	private CallCRUDDocument saveUpdateInactivateDocumentFunction;
	@Autowired
	private PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService;

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
			PedidoVentaDTO expedienteDTO, String transaccion, String token, int iterationNumber) throws ServerException {
		List<PedidoVentaCaracteristicaDTO> camposNuevos = new ArrayList<PedidoVentaCaracteristicaDTO>();
		// Por aqui voy cuando viene en una iteracion pero no se que otros caso pueda
		// intente refactor pero salio un aviso asi que deje quieto mientras
		if (transicion.getPlantilla() == null)
			return null;
		String user = getUserId(token);
		transicion.setPropiedades(propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.TRANSICION,
				transicion.getLlaveTabla(), null, user));
		String[] cars = {Propiedades.GENERA_DOCUMENTO_CAMPO, Propiedades.GENERA_DOCUMENTO_TEXTO, Propiedades.GENERA_DOCUMENTO_TEXTO};
		List<PropiedadDTO> camposGenerar = Propiedades.obtenerVariosParametro(transicion, cars);
		if (camposGenerar == null || camposGenerar.isEmpty())
			return null;
		for (PropiedadDTO iPropiedadDTO : camposGenerar) {
			List<RelacionInternaDTO> relaciones = relacionService.relacionesPropiedad(iPropiedadDTO.getLlaveTabla());
			if (relaciones == null || relaciones.isEmpty()) { // Este es un campo donde va principal
				PedidoVentaCaracteristicaDTO campoPrincipal = copyFieldDocument(null, iPropiedadDTO.getValor());
				if (documento != null) {
					campoPrincipal.setValorOpcion(documento.getLlaveTabla());
					if (documento.getDinero() != null)
						// Importante para que coja valor porque va a consultar po BD y no tiene
						campoPrincipal.setValorNumero(documento.getDinero().getValorTotal());
					campoPrincipal.setPrincipal(documento);
				} else {
					campoPrincipal.setValorOpcion(expedienteDTO.getLlaveTabla());
					if (expedienteDTO.getDinero() != null)
						campoPrincipal.setValorNumero(expedienteDTO.getDinero().getValorTotal());
					campoPrincipal.setPrincipal(expedienteDTO);
				}
				camposNuevos.add(campoPrincipal);
			} else {
				switch (iPropiedadDTO.getKey()) {
				case Propiedades.GENERA_DOCUMENTO_CAMPO:
					// Este campo debe sumarse
					for (RelacionInternaDTO iRelacion : relaciones) {
						if (documento != null && iRelacion.getPlantilla().compareTo(documento.getPlantilla()) == 0) {
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
					// Este campo debe sumarse
					PedidoVentaCaracteristicaDTO campoGenerado = pedidoVentaCaracteristicaService
							.consultarSQLCampoGenerarDocumento(iPropiedadDTO.getLlaveTabla(),
									(expedienteDTO != null) ? expedienteDTO.getLlaveTabla() : null,
									(documento != null) ? documento.getLlaveTabla() : null);
					camposNuevos.add(copyFieldDocument(campoGenerado, relaciones.get(0).getCampo()));
					break;
				case Propiedades.GENERA_DOCUMENTO_TEXTO:
					String textValueToNewField = iPropiedadDTO.getValor();
					PedidoVentaCaracteristicaDTO fieldNew = copyFieldDocument(null, relaciones.get(0).getCampo()); 
					if(textValueToNewField.compareTo("#NUMBER")==0) {
						fieldNew.setValorNumero(new BigDecimal(iterationNumber));
						fieldNew.setValorText(String.valueOf(iterationNumber) );
					}else {
						fieldNew.setValorText(textValueToNewField);
					}
					camposNuevos.add(fieldNew);	
					break;
				default:
					break;
				}
			}
		}

		return processNewFields(transicion, documento, transaccion, token, camposNuevos);
	}

	private PedidoVentaDTO processNewFields(ProcesoTransicionDTO transicion, PedidoVentaDTO documento,
			String transaccion, String token, List<PedidoVentaCaracteristicaDTO> camposNuevos) throws ServerException {
		if (!camposNuevos.isEmpty()) {
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
			return saveUpdateInactivateDocumentFunction.save(nuevo, token);
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
