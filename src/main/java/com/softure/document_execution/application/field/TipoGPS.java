package com.softure.document_execution.application.field;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.java.dto.exception.ServerException;

@Component
public class TipoGPS {

	@Autowired private PedidoVentaCaracteristicaSvc campoService;

	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException{
		if(pCampo.getValorText()!=null && pCampo.getValorText().isEmpty()) pCampo.setValorText(null);
		if(Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL)==null 
				&& pCampo.getValorText()==null)
			throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre() + " Es obligatorio registrar el campo " + pCampo.getCampoDTO().getNombre());
		if(pCampo.getValorText()==null) return;
		Pattern pat = Pattern.compile("^((\\-?|\\+?)?\\d+(\\.\\d+)?),\\s*((\\-?|\\+?)?\\d+(\\.\\d+)?)$");
	    Matcher mat = pat.matcher(pCampo.getValorText());                                                                           
	    if (!mat.matches())
	    	throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre() + " El campo no cumple con el format de las coordenadas latitud y longitud");
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
