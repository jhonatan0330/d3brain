package com.softure.document_execution.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.document_execution.domain.DocumentMessage;
import com.softure.document_execution.domain.DocumentoRelacionExpedienteDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;

public class CallDocumentCommons {

	public static PedidoVentaCaracteristicaDTO obtenerValor(List<PedidoVentaCaracteristicaDTO> caracteristicas, String campoValor) {
		if(caracteristicas==null || caracteristicas.size()==0) return null;
		for (PedidoVentaCaracteristicaDTO pvc : caracteristicas) {
			if(pvc.getCampo().compareTo(campoValor)==0){
				return pvc;
			}
		}
		return null;
	}
	
	public static PedidoVentaCaracteristicaFilterDTO calcularValoresTotalesCampo(PedidoVentaCaracteristicaFilterDTO pCampo, String valorTomar, DocumentoRelacionExpedienteSvc relacionExpedienteService) throws ServerException{
		if(pCampo.getExpedientes()==null) pCampo.setExpedientes(new ArrayList<PedidoVentaDTO>());
		//Calculo el valor de los expedientes y la cantidad
		int cantidad = 0;
		//Esto llena los valores de la tabla relacion expediente
		BigDecimal valor =BigDecimal.ZERO;
		List<DocumentoRelacionExpedienteDTO> relaciones;
		if(pCampo.getLlaveTabla()!=null) {
			relaciones = relacionExpedienteService.listByField(pCampo.getLlaveTabla());			
		}else {
			relaciones = new ArrayList<DocumentoRelacionExpedienteDTO>();	
		}
		boolean ValorNuevo = true;
		for (PedidoVentaDTO expediente : pCampo.getExpedientes()) {
			if((expediente.getEstado()==null || expediente.getEstado().compareTo(SharedConstants.STATE_INACTIVE)!=0)) {
				cantidad ++;
				if(expediente.getDinero()!=null) {
					ValorNuevo = true;
					for (DocumentoRelacionExpedienteDTO iRelacion : relaciones) {
						if(iRelacion.getExpedienteDetalle().compareTo(expediente.getLlaveTabla())==0) {
							if(expediente.getDinero()!=null)expediente.getDinero().setValorCampo(iRelacion.getValor());
							valor = valor.add(iRelacion.getValor());
							ValorNuevo = false;
							break;
						}
					}
					if(ValorNuevo && valorTomar!=null && expediente.getDinero()!=null){
						if(valorTomar.compareTo("2")==0) {
							expediente.getDinero().setValorCampo( expediente.getDinero().getSaldo());
						}else {//Aqui falta que lo tome de la caracteristica
							expediente.getDinero().setValorCampo( expediente.getDinero().getValorTotal());
						}
						valor = valor.add(expediente.getDinero().getValorCampo());
					}
				}
			}
		}
		pCampo.setValorText(String.valueOf(cantidad));
		pCampo.setValorNumeroMax(valor);
		return pCampo;
	}
	
	public static void addMessageError(PedidoVentaDTO document, String message) {
		if(document==null) return;
		if(document.getMessages()==null) document.setMessages(new ArrayList<>());
		DocumentMessage msg = new DocumentMessage();
		msg.setDate(new Date());
		msg.setType("ERROR");
		msg.setMessage(message);
		document.getMessages().add(msg);
	}
}
