package d3.document.application.field;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import d3.document.application.PedidoVentaCaracteristicaSvc;
import d3.document.domain.PedidoVentaCaracteristicaDTO;
import d3.shared.domain.ServerException;

@Component
public class TipoArchivo {

	private final PedidoVentaCaracteristicaSvc campoService;

	public TipoArchivo(@Lazy PedidoVentaCaracteristicaSvc campoService) {
		this.campoService = campoService;
	}

	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo) throws ServerException {
		if (pCampo.getValorText() == null || pCampo.getValorText().isEmpty()) {
			if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL) == null)
				throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre()
						+ "Es obligatorio registrar el campo " + pCampo.getCampoDTO().getNombre() + "(codigo : "
						+ pCampo.getCampoDTO().getCodigo() + ")");
			if (pCampo.getValorText() != null && pCampo.getValorText().isEmpty())
				pCampo.setValorText(null);
		} else {
			if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.MULTIPLE_FILE) == null) {
				if (pCampo.getValorText().contains(";;"))
					throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre()
							+ " revisa el campo " + pCampo.getCampoDTO().getNombre()
							+ " el permite un solo adjunto y esta enviando varios adjuntos ");
			}
		}
	}

	public PedidoVentaCaracteristicaDTO guardarCampo(PedidoVentaCaracteristicaDTO pCampo) throws ServerException {
		PedidoVentaCaracteristicaDTO bd = campoService.buscarActivo(pCampo, pCampo.getPrincipal().getHistorico());
		if (bd != null) {
			if (pCampo.getValorText() == null) {
				bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
				bd.setPrincipal(pCampo.getPrincipal());
				campoService.inactivar(bd);
				return pCampo;
			}
			if (pCampo.getValorText().compareTo(bd.getValorText()) == 0) {
				return pCampo;
			}
			bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
			bd.setPrincipal(pCampo.getPrincipal());
			campoService.inactivar(bd);

		}
		if (pCampo.getValorText() == null) {
			return pCampo;
		}
		return campoService.guardar(pCampo);

	}

}
