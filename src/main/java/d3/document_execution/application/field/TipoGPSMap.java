package d3.document_execution.application.field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import d3.shared.domain.ServerException;
import d3.document_execution.application.PedidoVentaCaracteristicaSvc;
import d3.document_execution.application.PedidoVentaSvc;
import d3.document_execution.domain.PedidoVentaCaracteristicaDTO;
import d3.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import d3.document_execution.domain.PedidoVentaDTO;
import d3.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import d3.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.property.domain.PropiedadDTO;
import org.springframework.context.annotation.Lazy;

@Component
public class TipoGPSMap {

	private final PedidoVentaCaracteristicaSvc campoService;
	private final DocumentoPlantillaCaracteristicaSvc caracteristicaService;
	private final PedidoVentaSvc pedidoVentaService;

	public TipoGPSMap(@Lazy PedidoVentaCaracteristicaSvc campoService,
			@Lazy DocumentoPlantillaCaracteristicaSvc caracteristicaService, @Lazy PedidoVentaSvc pedidoVentaService) {
		this.campoService = campoService;
		this.caracteristicaService = caracteristicaService;
		this.pedidoVentaService = pedidoVentaService;
	}

	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(PedidoVentaCaracteristicaFilterDTO pCampo)
			throws ServerException {
		DocumentoPlantillaCaracteristicaDTO pBase = caracteristicaService
				.consultaUnicaConComplementos(pCampo.getCampo(), pCampo.getSecurityToken());
		PropiedadDTO funcionConsulta = Propiedades.obtenerParametro(pBase, Propiedades.DISPONIBILIDAD_FUNCION_SQL);
		if (funcionConsulta != null) {
			HashMap<String, DocumentoPlantillaCaracteristicaDTO> hmap = new HashMap<String, DocumentoPlantillaCaracteristicaDTO>();
			campoService.validarDependientes(pBase, pCampo.getDependientes());
			List<PedidoVentaCaracteristicaDTO> ocupados = campoService.camposOcupadosCroquis(
					funcionConsulta.getLlaveTabla(), pCampo.getDocumento(), pCampo.getSecurityToken(),
					campoService.ordenarAlfabeticaDepende(pCampo.getDependientes()));
			if (ocupados != null && !ocupados.isEmpty()) {
				pCampo.setExpedientes(new ArrayList<>());
				for (PedidoVentaCaracteristicaDTO iOcupado : ocupados) {
					PedidoVentaDTO pv = pedidoVentaService.consultaXId(iOcupado.getDocumento());
					pv.setCaracteristicas(new ArrayList<>());
					if (!hmap.containsKey(iOcupado.getCampo())) {
						hmap.put(iOcupado.getCampo(), caracteristicaService.consultaXId(iOcupado.getCampo()));
					}
					iOcupado.setCampoDTO(hmap.get(iOcupado.getCampo()));
					pv.getCaracteristicas().add(iOcupado);
					pCampo.getExpedientes().add(pv);
				}
			}
		}
		pCampo.setCampoDTO(pBase);
		return pCampo;
	}
}
