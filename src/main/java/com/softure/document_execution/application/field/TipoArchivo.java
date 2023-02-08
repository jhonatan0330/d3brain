package com.softure.document_execution.application.field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.java.dto.exception.ServerException;

@Component
public class TipoArchivo {

	@Autowired
	private PedidoVentaCaracteristicaSvc campoService;

	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL) == null
				&& (pCampo.getValorText() == null || pCampo.getValorText().isEmpty()))
			throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre()
					+ "Es obligatorio registrar el campo " + pCampo.getCampoDTO().getNombre() + "(codigo : "
					+ pCampo.getCampoDTO().getCodigo() + ")");
	}

	public PedidoVentaCaracteristicaDTO guardarCampo(PedidoVentaCaracteristicaDTO pCampo, String token)
			throws ServerException {
		PedidoVentaCaracteristicaDTO bd = campoService.buscarActivo(pCampo, pCampo.getPrincipal().getHistorico());
		if (bd != null) {
			if (pCampo.getValorText() == null) {
				bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
				bd.setPrincipal(pCampo.getPrincipal());
				campoService.inactivar(bd, token);
				return pCampo;
			} else {
				if (pCampo.getValorText().compareTo(bd.getValorText()) == 0) {
					return pCampo;
				} else {
					bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
					bd.setPrincipal(pCampo.getPrincipal());
					campoService.inactivar(bd, token);
				}
			}
		}
		if (pCampo.getValorText() == null) {
			return pCampo;
		} else {
			return campoService.guardar(pCampo, token);
		}
	}

}
