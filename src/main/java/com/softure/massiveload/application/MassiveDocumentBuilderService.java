package com.softure.massiveload.application;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;

@Service
public class MassiveDocumentBuilderService {

	public PedidoVentaDTO build(Map<String, String> row, DocumentoPlantillaDTO template) throws ServerException {
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
