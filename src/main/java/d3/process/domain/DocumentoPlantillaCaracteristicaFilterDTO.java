package d3.process.domain;

import java.util.List;

import org.apache.ibatis.type.Alias;

import d3.document.domain.PedidoVentaDTO;
import d3.shared.domain.BasicNodoFilter;

@Alias("DocumentoPlantillaCaracteristicaFilterDTO")
public class DocumentoPlantillaCaracteristicaFilterDTO extends BasicNodoFilter {
	
	private List<PedidoVentaDTO> documentos;

	public void setDocumentos(List<PedidoVentaDTO> documentos) {
		this.documentos = documentos;
	}

	public List<PedidoVentaDTO> getDocumentos() {
		return documentos;
	}

}
