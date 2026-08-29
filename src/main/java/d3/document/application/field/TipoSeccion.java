package d3.document.application.field;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import d3.shared.domain.ServerException;
import d3.configuration.domain.PropiedadDTO;
import d3.document.application.PedidoVentaCaracteristicaSvc;
import d3.document.domain.PedidoVentaCaracteristicaDTO;
import d3.document.domain.PedidoVentaCaracteristicaFilterDTO;
import d3.process.application.DocumentoPlantillaCaracteristicaSvc;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;

@Component
public class TipoSeccion {

	private final PedidoVentaCaracteristicaSvc campoService;
	private final DocumentoPlantillaCaracteristicaSvc caracteristicaService;

	public TipoSeccion(@Lazy PedidoVentaCaracteristicaSvc campoService,
			@Lazy DocumentoPlantillaCaracteristicaSvc caracteristicaService) {
		this.campoService = campoService;
		this.caracteristicaService = caracteristicaService;
	}

	// Copiado de Tipo numero
	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(PedidoVentaCaracteristicaFilterDTO pCampo)
			throws ServerException {
		DocumentoPlantillaCaracteristicaDTO pBase = caracteristicaService
				.consultaUnicaConComplementos(pCampo.getCampo(), pCampo.getSecurityToken());
		PropiedadDTO funcionCalculo = Propiedades.obtenerParametro(pBase, Propiedades.SECCION_FUNCION_SQL);
		if (funcionCalculo != null) {
			campoService.validarDependientes(pBase, pCampo.getDependientes());
			// Este ordenar esta como repetido porque en calcularNumeroFuncion tambiens e
			// usa
			List<PedidoVentaCaracteristicaDTO> newDependientes = campoService
					.ordenarAlfabeticaDepende(pCampo.getDependientes());
			for (PedidoVentaCaracteristicaDTO iDep : newDependientes) {
				if (iDep.getValorOpcion() == null) {
					if (iDep.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.NUMERO) == 0) {
						iDep.setValorOpcion((iDep.getValorNumero() == null) ? "0" : iDep.getValorNumero().toString());
					}
				}
			}
			try {
				pCampo.setValorNumeroMax(campoService.calcularNumeroFuncion(funcionCalculo, pCampo.getDocumento(),
						pCampo.getSecurityToken(), newDependientes, pCampo.getCampoDTO()));
			} catch (ServerException e) {
				throw new ServerException(e.getMessage(), "Campo: " + pCampo.getCampoDTO().getNombre());
			}
			if (pCampo.getValorNumeroMax() == null)
				pCampo.setValorNumeroMax(BigDecimal.ZERO);
		}
		// Es apra evitar enviar mucha informacion en el response
		pCampo.setCampoDTO(null);
		pCampo.setDependientes(null);
		return pCampo;
	}
}
