package d3.massiveload.application;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Service;

import d3.document.domain.PedidoVentaCaracteristicaDTO;
import d3.document.domain.PedidoVentaDTO;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.process.domain.TemplateDTO;

@Service
public class MassiveDocumentBuilderService {

	public PedidoVentaDTO build(Map<String, String> row, TemplateDTO template) {
		PedidoVentaDTO pedido = new PedidoVentaDTO();
		pedido.setPlantilla(template.getLlaveTabla());
		pedido.setCaracteristicas(new ArrayList<>());
		String updateColumn = "UPDATE_" + template.getCodigo();
		for (DocumentoPlantillaCaracteristicaDTO campoPlantilla : template.getCaracteristicas()) {
			if (DocumentoPlantillaCaracteristicaDTO.SECCION.equals(campoPlantilla.getFormato()))
				continue;
			PedidoVentaCaracteristicaDTO campo = new PedidoVentaCaracteristicaDTO();
			campo.setCampo(campoPlantilla.getLlaveTabla());
			campo.setCampoDTO(campoPlantilla);
			String headerName = MassiveFileParserService.formatStringXML(campoPlantilla.getNombre());
			String val = null;
			for (Map.Entry<String, String> e : row.entrySet()) {
				if (MassiveFileParserService.formatStringXML(e.getKey()).equals(headerName)) {
					val = e.getValue();
					break;
				}
			}
			if (val != null)
				val = val.trim();
			campo.setValorText(val);
			campo.setModificado(true);
			campo.setPrincipal(pedido);
			pedido.getCaracteristicas().add(campo);
		}
		for (Map.Entry<String, String> e : row.entrySet()) {
			if (MassiveFileParserService.formatStringXML(e.getKey())
					.equals(MassiveFileParserService.formatStringXML(updateColumn)) && e.getValue() != null
					&& !e.getValue().trim().isEmpty()) {
				pedido.setNombre(e.getValue().trim());
			}
		}
		return pedido;
	}
}
