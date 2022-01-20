package com.softure.logisticpymes.services.refactor;

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
public class DocumentAutomaticNewFunction {

	@Autowired private RelacionInternaSvc relacionService;
	@Autowired private DocumentoPlantillaSvc plantillaService;
	@Autowired private PropiedadSvc propiedadService;
	@Autowired private DocumentNewSaveUpdateInactivateFunction saveUpdateInactivateDocumentFunction;
	@Autowired private PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService;
	
	public PedidoVentaDTO generateDocuments(
			ProcesoTransicionDTO transicion, 
			PedidoVentaDTO documento, 		// Documento modificador que realiza la accion sobre el documetnto base
			PedidoVentaDTO expedienteDTO, 	// Documento base que se esta fafectando con el proceso
			String transaccion,				// Como reuso esto en los temporizadores automaticos entonces no viene transaccion
			String token,
			PedidoVentaCaracteristicaDTO vieneAutomatica)throws ServerException{ // Machetazo 
		List<PedidoVentaCaracteristicaDTO> camposNuevos = new ArrayList<PedidoVentaCaracteristicaDTO>();
		if (vieneAutomatica !=null) {
			camposNuevos.add(vieneAutomatica);
		}else {
			if(transicion.getPlantilla()==null) return null;
			String user = getUserId(token);
			List<PropiedadDTO>camposGenerar = propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.TRANSICION, transicion.getLlaveTabla(), Propiedades.GENERA_DOCUMENTO_CAMPO, user);
			if(camposGenerar==null) camposGenerar = new ArrayList<>();
			camposGenerar.addAll(propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.TRANSICION, transicion.getLlaveTabla(), Propiedades.GENERA_DOCUMENTO_FUNCION_SQL, user));
			if(camposGenerar==null || camposGenerar.isEmpty())	return null;
			//tengo que revisar cada propiedad y ver el campo que pide
			for (PropiedadDTO iPropiedadDTO : camposGenerar) {
				List<RelacionInternaDTO> relaciones = relacionService.relacionesPropiedad(iPropiedadDTO.getLlaveTabla());
				if(relaciones==null || relaciones.isEmpty()) {	//Este es un campo donde va principal
					PedidoVentaCaracteristicaDTO campoPrincipal = copyFieldDocument( null ,iPropiedadDTO.getValor());
					if(documento!=null) {
						campoPrincipal.setValorOpcion(documento.getLlaveTabla());
						if(documento.getDinero()!=null)campoPrincipal.setValorNumero(documento.getDinero().getValorTotal());//Importante para que coja valor porque va a consultar po BD y no tiene
						campoPrincipal.setPrincipal(documento);
					}else {
						campoPrincipal.setValorOpcion(expedienteDTO.getLlaveTabla());
						if(expedienteDTO.getDinero()!=null)campoPrincipal.setValorNumero(expedienteDTO.getDinero().getValorTotal());//Importante para que coja valor porque va a consultar po BD y no tiene
						campoPrincipal.setPrincipal(expedienteDTO);
					}
					camposNuevos.add(campoPrincipal);
				}else {
					if(iPropiedadDTO.getKey().compareTo(Propiedades.GENERA_DOCUMENTO_CAMPO)==0) {
						//Este campo debe sumarse
						for (RelacionInternaDTO iRelacion : relaciones) {
							if(documento!=null && iRelacion.getPlantilla().compareTo(documento.getPlantilla())==0) {
								camposNuevos.add(copyFieldDocument( DocumentCommonsFunction.obtenerValor(documento.getCaracteristicas(), iRelacion.getCampo()), iPropiedadDTO.getValor()));
							} else {
								if(expedienteDTO!=null && expedienteDTO.getPlantilla() != null && iRelacion.getPlantilla().compareTo(expedienteDTO.getPlantilla())==0) {
									// Solo consulto el documento cuando en realidad lo necesito, en general no veien las caracteristicas
									if(expedienteDTO.getCaracteristicas()==null) expedienteDTO.setCaracteristicas(pedidoVentaCaracteristicaService.listar2Documento(expedienteDTO.getLlaveTabla(), expedienteDTO.getHistorico()));
									camposNuevos.add(copyFieldDocument( DocumentCommonsFunction.obtenerValor(expedienteDTO.getCaracteristicas(), iRelacion.getCampo()), iPropiedadDTO.getValor()));
								}
							}
						}	
					}else {
						PedidoVentaCaracteristicaDTO campoGenerado = pedidoVentaCaracteristicaService.consultarSQLCampoGenerarDocumento(iPropiedadDTO.getLlaveTabla(), (expedienteDTO!=null)?expedienteDTO.getLlaveTabla():null, (documento!=null)?documento.getLlaveTabla():null);
						camposNuevos.add(copyFieldDocument(campoGenerado, relaciones.get(0).getCampo()));
					}
				}
			}
		}
		
		if(!camposNuevos.isEmpty()) {
			PedidoVentaDTO nuevo = new PedidoVentaDTO();
			nuevo.setCaracteristicas(new ArrayList<PedidoVentaCaracteristicaDTO>());
			nuevo.setPlantilla(transicion.getPlantilla());
			DocumentoPlantillaDTO pPlantilla =  new DocumentoPlantillaDTO();
			pPlantilla.setLlaveTabla(transicion.getPlantilla());
			pPlantilla = plantillaService.obtenerCampos(pPlantilla, token);
			if(documento!=null)nuevo.setTransaccion(documento.getTransaccion());
			for (DocumentoPlantillaCaracteristicaDTO iCampo : pPlantilla.getCaracteristicas()) {
				boolean relacionExistente = false;
				for (PedidoVentaCaracteristicaDTO iCampoCopiar : camposNuevos) {
					if(iCampo.getLlaveTabla().compareTo(iCampoCopiar.getCampo())==0) {
						nuevo.getCaracteristicas().add(copyFieldDocument(iCampoCopiar, iCampoCopiar.getCampo()));
						relacionExistente = true;
						break;
					}
				}
				if(!relacionExistente)nuevo.getCaracteristicas().add(copyFieldDocument(null, iCampo.getLlaveTabla()));
			}
			
			nuevo.setLlaveTabla(null);
			nuevo.setTransaccion(transaccion);
			return saveUpdateInactivateDocumentFunction.save(nuevo, token);
		}else {
			return null;
		}
	}
	
	private PedidoVentaCaracteristicaDTO copyFieldDocument(PedidoVentaCaracteristicaDTO actual, String campoId) {
		PedidoVentaCaracteristicaDTO nueva = new PedidoVentaCaracteristicaDTO();
		nueva.setCampo(campoId);
		if(actual!=null) {
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
