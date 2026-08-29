package d3.document.application.field;

import java.util.List;

import org.springframework.stereotype.Component;

import d3.shared.domain.SharedConstants;
import d3.shared.domain.ServerException;
import d3.configuration.application.RelacionInternaSvc;
import d3.configuration.domain.PropiedadDTO;
import d3.configuration.domain.RelacionInternaDTO;
import d3.document.application.CallDocumentListWithFilters;
import d3.document.domain.PedidoVentaCaracteristicaDTO;
import d3.document.domain.PedidoVentaDTO;
import d3.document.domain.PedidoVentaFilterDTO;
import d3.process.application.DocumentoPlantillaCaracteristicaSvc;

import org.springframework.context.annotation.Lazy;

@Component
public class CallUpdateInformativeField {

	private final TipoInformativo informativeField;
	private final RelacionInternaSvc relacionService;
	private final CallDocumentListWithFilters listDocumentWithFiltersFunction;
	private final DocumentoPlantillaCaracteristicaSvc fieldService;

	public CallUpdateInformativeField(@Lazy TipoInformativo informativeField, @Lazy RelacionInternaSvc relacionService,
			@Lazy CallDocumentListWithFilters listDocumentWithFiltersFunction,
			@Lazy DocumentoPlantillaCaracteristicaSvc fieldService) {
		this.informativeField = informativeField;
		this.relacionService = relacionService;
		this.listDocumentWithFiltersFunction = listDocumentWithFiltersFunction;
		this.fieldService = fieldService;
	}

	// EWsto lo hago por urgencia que modifico de una el valor del dependiente
	public void call(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		List<PropiedadDTO> updateProperties = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(),
				Propiedades.UPDATE_INFORMATIVE_FIELD);
		if (updateProperties == null)
			return;
		if (pCampo.getDependientes() == null)
			throw new ServerException("Error al consultar dependientes");
		for (PropiedadDTO propiedadDTO : updateProperties) {
			List<RelacionInternaDTO> relaciones = relacionService.relacionesPropiedad(propiedadDTO.getLlaveTabla());
			if (relaciones == null || relaciones.isEmpty())
				throw new ServerException("Revisa las relaciones de la propiedad " + propiedadDTO.getNombre()
						+ " del campo " + pCampo.getCampoDTO().getNombre());
			for (PedidoVentaCaracteristicaDTO dependiente : pCampo.getDependientes()) {
				if (dependiente.getCampo().compareTo(propiedadDTO.getValor()) == 0) {
					if (dependiente.getExpedientes() == null || dependiente.getExpedientes().isEmpty()) {
						PedidoVentaFilterDTO filter = new PedidoVentaFilterDTO();
						filter.setCampoOrigen(dependiente.getCampo());
						filter.setEstado(SharedConstants.STATE_ACTIVE);
						filter.setTextoFiltro(dependiente.getDocumento());
						filter.setSecurityToken(token);
						dependiente.setCampoDTO(fieldService.cargarComplementos(dependiente.getCampoDTO(), token));
						dependiente.setExpedientes(listDocumentWithFiltersFunction.listarAvanzado(filter));

						if (dependiente.getExpedientes() == null || dependiente.getExpedientes().isEmpty())
							break;
					}
					for (PedidoVentaDTO iDocument : dependiente.getExpedientes()) {
						for (RelacionInternaDTO iRelacion : relaciones) {
							if (iDocument.getPlantilla().compareTo(iRelacion.getPlantilla()) == 0) {
								PedidoVentaCaracteristicaDTO pInformativeField = new PedidoVentaCaracteristicaDTO();
								pInformativeField.setCampo(iRelacion.getCampo());
								pInformativeField.setDocumento(iDocument.getLlaveTabla());
								pInformativeField.setValorText(pCampo.getValorText());
								pInformativeField.setValorOpcion(pCampo.getValorOpcion());
								pInformativeField.setTransaccionRegistro(pCampo.getTransaccionRegistro());
								pInformativeField.setPrincipal(iDocument);
								// informativeField.validarPrepararCampo(pInformativeField, token);
								informativeField.guardarCampo(pInformativeField, token);
							}
						}
					}
					break;
				}
			}
		}
	}
}
