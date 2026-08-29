package d3.document.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import d3.shared.domain.ServerException;
import d3.configuration.domain.PropiedadDTO;
import d3.document.application.field.Propiedades;
import d3.document.domain.PedidoVentaCaracteristicaDTO;
import d3.document.domain.PedidoVentaDTO;
import d3.document.domain.PedidoVentaFilterDTO;
import d3.shared.application.D3Utils;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;

import org.springframework.context.annotation.Lazy;

@Component
public class CallDocumentListBySQLFunction {

	private final PedidoVentaCaracteristicaSvc campoService;
	private final CallDocumentListWithFilters listDocumentWithFiltersFunction;

	public CallDocumentListBySQLFunction(@Lazy PedidoVentaCaracteristicaSvc campoService,
			@Lazy CallDocumentListWithFilters listDocumentWithFiltersFunction) {
		this.campoService = campoService;
		this.listDocumentWithFiltersFunction = listDocumentWithFiltersFunction;
	}

	public List<PedidoVentaDTO> execute(DocumentoPlantillaCaracteristicaDTO pBase,
			DocumentoPlantillaCaracteristicaDTO campo, List<PedidoVentaCaracteristicaDTO> dependientes,
			PedidoVentaFilterDTO entityFilter, PropiedadDTO funcionConsulta, String campoValor, String token)
			throws ServerException {
		List<PedidoVentaDTO> result = executeWithoutDetailDocument(campo, dependientes, entityFilter, funcionConsulta);
		return listDocumentWithFiltersFunction.listadoCompleto(result, token, campoValor);
	}

	public List<PedidoVentaDTO> executeWithoutDetailDocument(DocumentoPlantillaCaracteristicaDTO campo,
			List<PedidoVentaCaracteristicaDTO> dependientes, PedidoVentaFilterDTO entityFilter,
			PropiedadDTO funcionConsulta) throws ServerException {
		// En caso que sea funcion y tenga una dependencia va a aenviar ese valor como
		// llave tabla
		List<PropiedadDTO> codigoDepende = Propiedades.obtenerVariosParametro(campo, Propiedades.DEPENDENT_PROPS);
		if (entityFilter == null)
			entityFilter = new PedidoVentaFilterDTO(); // en tipo proceos autoload no sabia que filtrar
		if (codigoDepende != null) {// Coloco las dependencias
			campoService.validarDependientes(campo, dependientes);
			if (dependientes != null) {
				dependientes = campoService.ordenarAlfabeticaDepende(dependientes);
				dependientes = campoService.removeDuplicateDepends(dependientes);
				if (dependientes.get(0).getValorOpcion() != null)// Se me perdia la referencia y no se porque
					entityFilter.setLlaveTabla(new String(dependientes.get(0).getValorOpcion()));
				List<PedidoVentaCaracteristicaDTO> expedientesMultiples = new ArrayList<PedidoVentaCaracteristicaDTO>();
				for (PedidoVentaCaracteristicaDTO iDependiente : dependientes) {
					if (iDependiente.getValorOpcion() == null) {
						if (iDependiente.getExpedientes() != null) {
							// Esto aplica para los campos multiples
							for (PedidoVentaDTO iExpediente : iDependiente.getExpedientes()) {
								PedidoVentaCaracteristicaDTO pd = new PedidoVentaCaracteristicaDTO();
								pd.setValorOpcion(iExpediente.getLlaveTabla());
								expedientesMultiples.add(pd);
							}
						}
						if (iDependiente.getValorText() == null) {
							if (iDependiente.getValorFecha() != null) {
								iDependiente.setValorText(D3Utils.formatDateTime(iDependiente.getValorFecha()));
							}
						}
					}
				}
				if (expedientesMultiples.size() != 0)
					dependientes.addAll(expedientesMultiples);
			}
		}
		// entityFilter.setDescripcion(funcionConsulta.getLlaveTabla());
		List<PedidoVentaDTO> result = listDocumentWithFiltersFunction.listarExpedientesDisponiblesDocumentoFuncion(
				entityFilter, funcionConsulta.getLlaveTabla(), dependientes);
		return result;
	}

}
