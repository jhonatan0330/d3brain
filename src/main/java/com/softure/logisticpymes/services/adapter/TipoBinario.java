package com.softure.logisticpymes.services.adapter;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.domain.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.services.PedidoVentaCaracteristicaSvc;

@Component
public class TipoBinario {
	
	@Autowired private PedidoVentaCaracteristicaSvc campoService;
	
	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException{
		if(pCampo.getValorNumero()==null || pCampo.getValorNumero().compareTo(BigDecimal.ZERO)==0){
			String parametro = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.BINARIO_FALSO);
			if( parametro.isEmpty()){
				pCampo.setValorText("NO");
			}else{
				pCampo.setValorText(parametro);	
			}
		}else{
			String parametro = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.BINARIO_VERDADERO);
			if( parametro.isEmpty()){
				pCampo.setValorText("SI");
			}else{
				pCampo.setValorText(parametro);	
			}
			pCampo.setValorNumero(BigDecimal.ONE);
		}
			
	}
	
	public PedidoVentaCaracteristicaDTO guardarCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException{
		PedidoVentaCaracteristicaDTO bd = campoService.buscarActivo(pCampo, pCampo.getPrincipal().getHistorico());
		if(bd!=null){
			if(pCampo.getValorText()==null){
				bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
				bd.setPrincipal(pCampo.getPrincipal());
				campoService.inactivar(bd, token);
				return pCampo;
			}else{
				if(pCampo.getValorText().compareTo(bd.getValorText())==0){
					return pCampo;
				}else{
					bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
					bd.setPrincipal(pCampo.getPrincipal());
					campoService.inactivar(bd, token);
				}
			}
		}
		if(pCampo.getValorText()==null){
			return pCampo;
		}else{
			return campoService.guardar(pCampo, token);
		}
	}
		
}
