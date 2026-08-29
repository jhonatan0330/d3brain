package d3.massiveload.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import d3.document.application.field.CampoAdaptador;
import d3.document.application.field.Propiedades;
import d3.document.domain.DocumentMessage;
import d3.document.domain.PedidoVentaCaracteristicaDTO;
import d3.document.domain.PedidoVentaDTO;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.process.domain.DocumentoPlantillaDTO;
import d3.shared.domain.ServerException;

import org.springframework.context.annotation.Lazy;

@Service
public class MassiveValidationService {

	private final CampoAdaptador adaptador;

	public MassiveValidationService(@Lazy CampoAdaptador adaptador) {
		this.adaptador = adaptador;
	}

	public List<DocumentMessage> validate(PedidoVentaDTO document, DocumentoPlantillaDTO template, String token) {
		List<DocumentMessage> messages = new ArrayList<>();
		if (document.getCaracteristicas() == null) {
			messages.add(message("Es necesario registrar informacion adicional."));
			return messages;
		}
		for (DocumentoPlantillaCaracteristicaDTO campoPlantilla : template.getCaracteristicas()) {
			if (DocumentoPlantillaCaracteristicaDTO.SECCION.equals(campoPlantilla.getFormato()))
				continue;
			PedidoVentaCaracteristicaDTO campo = findField(document, campoPlantilla.getLlaveTabla());
			if (campo == null) {
				messages.add(message("Revisa porque el campo " + campoPlantilla.getNombre()
						+ " no viene registrado en el documento " + template.getNombre()));
				continue;
			}
			if (DocumentoPlantillaCaracteristicaDTO.PROCESO.equals(campoPlantilla.getFormato())
					|| DocumentoPlantillaCaracteristicaDTO.VINCULO.equals(campoPlantilla.getFormato())) {
				boolean optional = Propiedades.obtenerParametro(campoPlantilla,
						Propiedades.PERMISO_CAMPO_OPCIONAL) != null;
				if (!optional && (campo.getValorText() == null || campo.getValorText().isEmpty())
						&& campo.getValorOpcion() == null) {
					messages.add(message("Es necesario registrar el campo " + campoPlantilla.getNombre()
							+ " de la plantilla " + campoPlantilla.getPlantillaNombre()));
				}
				continue;
			}
			try {
				adaptador.validarPrepararCampo(campo, token, false);
			} catch (ServerException e) {
				messages.add(message("Campo " + campoPlantilla.getNombre() + ": " + e.getMessage()));
			}
		}
		return messages;
	}

	private PedidoVentaCaracteristicaDTO findField(PedidoVentaDTO document, String campo) {
		for (PedidoVentaCaracteristicaDTO c : document.getCaracteristicas())
			if (campo.equals(c.getCampo()))
				return c;
		return null;
	}

	private DocumentMessage message(String m) {
		DocumentMessage msg = new DocumentMessage();
		msg.setMessage(m);
		return msg;
	}
}
