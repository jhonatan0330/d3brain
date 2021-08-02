package com.softure.logisticpymes.services.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.services.PedidoVentaCaracteristicaSvc;

@Component
public class TipoArchivo {

	@Autowired private PedidoVentaCaracteristicaSvc campoService;
	
	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		if(Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL)==null && (pCampo.getValorText()==null || pCampo.getValorText().isEmpty()))
			throw new ServerException("Es obligatorio registrar el campo " + pCampo.getCampoDTO().getNombre());
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
