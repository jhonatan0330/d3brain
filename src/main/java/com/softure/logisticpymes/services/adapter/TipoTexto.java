package com.softure.logisticpymes.services.adapter;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.domain.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.services.PedidoVentaCaracteristicaSvc;

@Component
public class TipoTexto {
	
	@Autowired private PedidoVentaCaracteristicaSvc campoService;
	
	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException{
		if(pCampo.getValorText()!=null && pCampo.getValorText().isEmpty()) pCampo.setValorText(null);
		if(pCampo.getLlaveTabla()==null && Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_BLOQUEAR)!=null
				&&  Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.TEXTO_FORMULA)!=null){
			calcularValorFormula(pCampo);
		}
		if(Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL)==null 
				&& pCampo.getValorText()==null)
			throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre() + " Es obligatorio registrar el campo " + pCampo.getCampoDTO().getNombre());
		if(pCampo.getValorText()==null) return;
		String formato = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.FORMATO);
		if(!formato.isEmpty()) {
			String[] registros = pCampo.getValorText().split(";");
			String emailRegex = "^[0-9]*$";
			if(formato.compareTo("E")==0) emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
			if(formato.compareTo("T")==0) emailRegex = "^3[0-9]{9}$";
			Pattern pat = Pattern.compile(emailRegex);
			for (String iRegistro : registros) {
				if(iRegistro!=null && !iRegistro.isEmpty()) {
					if(!pat.matcher(iRegistro).matches())
						throw new ServerException("Revisa el campo ya que no tiene un formato valido, " + iRegistro);	
				}
			}
		    pCampo.setValorText(pCampo.getValorText().toLowerCase());
		}else {
			String parametro = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.TEXTO_LARGO);
			if(parametro.isEmpty())	pCampo.setValorText(pCampo.getValorText().toUpperCase());
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
	
	private void calcularValorFormula(PedidoVentaCaracteristicaDTO pCampo) {
		String textoCalculado = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.TEXTO_FORMULA);
		if(pCampo.getDependientes()!=null && !pCampo.getDependientes().isEmpty()){
			for (PedidoVentaCaracteristicaDTO iDep : pCampo.getDependientes()) {
				textoCalculado = StringUtils.replace(textoCalculado, iDep.getCampoDTO().getCodigo(), (iDep.getValorText()==null)?"":iDep.getValorText());
			}
		}
		pCampo.setValorText(textoCalculado);
	}
		
}
