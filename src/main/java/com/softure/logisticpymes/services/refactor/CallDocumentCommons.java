package com.softure.logisticpymes.services.refactor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.domain.dto.DocumentoRelacionExpedienteDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaDTO;
import com.softure.logisticpymes.domain.filter.DocumentoRelacionExpedienteFilterDTO;
import com.softure.logisticpymes.domain.filter.PedidoVentaCaracteristicaFilterDTO;
import com.softure.logisticpymes.services.DocumentoRelacionExpedienteSvc;

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
			DocumentoRelacionExpedienteFilterDTO filtro = new DocumentoRelacionExpedienteFilterDTO();
			filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			filtro.setCampoMaestro(pCampo.getLlaveTabla());
			relaciones = relacionExpedienteService.listarConsulta(filtro);			
		}else {
			relaciones = new ArrayList<DocumentoRelacionExpedienteDTO>();	
		}
		boolean ValorNuevo = true;
		for (PedidoVentaDTO expediente : pCampo.getExpedientes()) {
			if((expediente.getEstado()==null || expediente.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO)!=0)) {
				cantidad ++;
				if(expediente.getDinero()!=null) {
					ValorNuevo = true;
					for (DocumentoRelacionExpedienteDTO iRelacion : relaciones) {
						if(iRelacion.getExpedienteDetalle().compareTo(expediente.getLlaveTabla())==0) {
							if(expediente.getDinero()!=null)expediente.getDinero().setValorCampo(iRelacion.getValor());
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
					}
					valor = valor.add(expediente.getDinero().getValorCampo());
				}
			}
		}
		pCampo.setValorText(String.valueOf(cantidad));
		pCampo.setValorNumeroMax(valor);
		return pCampo;
	}
}
