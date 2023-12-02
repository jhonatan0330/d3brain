package com.softure.document_execution.application.field;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shared.domain.ServerException;
import com.softure.document_execution.application.CallDocumentListWithFilters;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaFilterDTO;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.property.application.RelacionInternaSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.RelacionInternaDTO;

@Component
public class CallUpdateInformativeField {

	@Autowired
	private TipoInformativo informativeField;
	@Autowired
	private RelacionInternaSvc relacionService;
	@Autowired 
	private CallDocumentListWithFilters listDocumentWithFiltersFunction;
	@Autowired 
	private DocumentoPlantillaCaracteristicaSvc fieldService;
	
	
	//EWsto lo hago por urgencia que modifico de una el valor del dependiente
	public void call(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		List<PropiedadDTO> updateProperties = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(),
				Propiedades.UPDATE_INFORMATIVE_FIELD);
		if (updateProperties == null) return;
		if (pCampo.getDependientes() == null)
			throw new ServerException(
					"Error al consultar dependientes");
		for (PropiedadDTO propiedadDTO : updateProperties) {
			List<RelacionInternaDTO> relaciones = relacionService
					.relacionesPropiedad(propiedadDTO.getLlaveTabla());
			if (relaciones == null || relaciones.isEmpty())
				throw new ServerException("Revisa las relaciones de la propiedad " + propiedadDTO.getNombre()
						+ " del campo " + pCampo.getCampoDTO().getNombre());
			for (PedidoVentaCaracteristicaDTO dependiente : pCampo.getDependientes()) {
				if (dependiente.getCampo().compareTo(propiedadDTO.getValor()) == 0) {
					if(dependiente.getExpedientes()==null || dependiente.getExpedientes().isEmpty()) {
						PedidoVentaFilterDTO filter = new PedidoVentaFilterDTO();
						filter.setCampoOrigen(dependiente.getCampo());
						filter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
						filter.setTextoFiltro(dependiente.getDocumento());
						filter.setSecurityToken(token);
						dependiente.setCampoDTO(fieldService.cargarComplementos(dependiente.getCampoDTO(), token));
						dependiente.setExpedientes(listDocumentWithFiltersFunction.listarAvanzado(filter));
						
						if(dependiente.getExpedientes()==null || dependiente.getExpedientes().isEmpty())
							break;
					}
					for (PedidoVentaDTO iDocument : dependiente.getExpedientes()) {
						for (RelacionInternaDTO iRelacion : relaciones) {
							if(iDocument.getPlantilla().compareTo(iRelacion.getPlantilla())==0) {
								PedidoVentaCaracteristicaDTO pInformativeField = new PedidoVentaCaracteristicaDTO();
								pInformativeField.setCampo(iRelacion.getCampo());
								pInformativeField.setDocumento(iDocument.getLlaveTabla());
								pInformativeField.setValorText(pCampo.getValorText());
								pInformativeField.setValorOpcion(pCampo.getValorOpcion());
								pInformativeField.setTransaccionRegistro(pCampo.getTransaccionRegistro());
								pInformativeField.setPrincipal(iDocument);
								//informativeField.validarPrepararCampo(pInformativeField, token);
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
