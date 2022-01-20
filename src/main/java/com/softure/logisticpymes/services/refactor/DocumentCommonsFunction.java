package com.softure.logisticpymes.services.refactor;

import java.util.List;

import com.softure.logisticpymes.dto.PedidoVentaCaracteristicaDTO;

public class DocumentCommonsFunction {

	public static PedidoVentaCaracteristicaDTO obtenerValor(List<PedidoVentaCaracteristicaDTO> caracteristicas, String campoValor) {
		if(caracteristicas==null || caracteristicas.size()==0) return null;
		for (PedidoVentaCaracteristicaDTO pvc : caracteristicas) {
			if(pvc.getCampo().compareTo(campoValor)==0){
				return pvc;
			}
		}
		return null;
	}
}
