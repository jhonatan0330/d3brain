package com.softure.document_execution.application.field;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.shared.domain.ServerException;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.domain.PropiedadDTO;

@Component
public class TipoSeccion {

	@Autowired
	@Lazy
	private PedidoVentaCaracteristicaSvc campoService;
	@Autowired
	@Lazy
	private DocumentoPlantillaCaracteristicaSvc caracteristicaService;

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
				pCampo.setValorNumeroMax(
						campoService.calcularNumeroFuncion(funcionCalculo, pCampo.getDocumento(), pCampo.getSecurityToken(), newDependientes, pCampo.getCampoDTO()));
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
