package com.softure.document_execution.application.field;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.java.dto.exception.ServerException;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.property.application.RelacionInternaSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.RelacionInternaDTO;

@Component
public class TipoInformativo {

	@Autowired
	private PedidoVentaCaracteristicaSvc campoService;
	@Autowired
	private DocumentoPlantillaCaracteristicaSvc caracteristicaService;
	@Autowired
	private DocumentoPlantillaSvc templateService;
	@Autowired
	private RelacionInternaSvc relacionService;
	@Autowired
	private PedidoVentaSvc documentService;

	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		PedidoVentaCaracteristicaFilterDTO filter = new PedidoVentaCaracteristicaFilterDTO();
		filter.setCampoDTO(pCampo.getCampoDTO());
		filter.setCampo(pCampo.getCampo());
		filter.setDependientes(pCampo.getDependientes());
		filter = consultarDatosBase(filter);
		pCampo.setValorAuxiliar(filter.getValorAuxiliar());
		pCampo.setValorFecha(filter.getValorFechaMin());
		pCampo.setValorNumero(filter.getValorNumeroMin());
		pCampo.setValorOpcion(filter.getValorOpcion());
		pCampo.setValorText(filter.getValorText());
		if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL) == null
				&& pCampo.getValorText() == null)
			throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre()
					+ " Es obligatorio registrar el campo " + pCampo.getCampoDTO().getNombre() + "(codigo : "
					+ pCampo.getCampoDTO().getCodigo() + ")");
	}



	public PedidoVentaCaracteristicaDTO guardarCampo(PedidoVentaCaracteristicaDTO pCampo, String token)
			throws ServerException {
		PedidoVentaCaracteristicaDTO bd = campoService.buscarActivo(pCampo, pCampo.getPrincipal().getHistorico());
		if (bd != null) {
			if (pCampo.getValorText() == null) {
				bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
				bd.setPrincipal(pCampo.getPrincipal());
				campoService.inactivar(bd, token);
				return pCampo;
			} else {
				if (pCampo.getValorText().compareTo(bd.getValorText()) == 0) {
					return pCampo;
				} else {
					bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
					bd.setPrincipal(pCampo.getPrincipal());
					campoService.inactivar(bd, token);
				}
			}
		}
		if (pCampo.getValorText() == null) {
			return pCampo;
		} else {
			return campoService.guardar(pCampo, token);
		}
	}

	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(PedidoVentaCaracteristicaFilterDTO pCampo)
			throws ServerException {
		DocumentoPlantillaCaracteristicaDTO pBase = caracteristicaService
				.consultaUnicaConComplementos(pCampo.getCampo(), pCampo.getSecurityToken());
		PropiedadDTO dataToGetInformation = Propiedades.obtenerParametro(pBase, Propiedades.INFORMATIVE_DATA);
		if (dataToGetInformation == null) throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre()
				+ " En el campo " + pCampo.getCampoDTO().getNombre() + " no se ha definido la propiedad de codigo INFORMATIVE_DATA");

		if( pCampo.getDependientes() ==null || pCampo.getDependientes().isEmpty()) {
			cleanFieldToResponse(pCampo);
			return pCampo;
		}
		PedidoVentaCaracteristicaDTO fieldToGetInformation = pCampo.getDependientes().get(0);
		if(fieldToGetInformation.getValorOpcion() ==null) {	
			cleanFieldToResponse(pCampo);
			return pCampo;
		}
		
		if(fieldToGetInformation.getCampoDTO()==null) throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre()
				+ " En el campo " + pCampo.getCampoDTO().getNombre() + " no se envio la información del campoDTO");
		
		if(fieldToGetInformation.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.PROCESO)!=0) throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre()
				+ " En el campo " + pCampo.getCampoDTO().getNombre() + " solo se puede relacionar campos proceso");
		
		
		PedidoVentaDTO documentToGet = documentService.consultaXId(fieldToGetInformation.getValorOpcion());
		
		PedidoVentaCaracteristicaDTO pCampoFilter = new PedidoVentaCaracteristicaDTO();
		pCampoFilter.setDocumento(documentToGet.getLlaveTabla());
		
		List<RelacionInternaDTO> relationsOfPropertyData = relacionService.relacionesPropiedad(dataToGetInformation.getLlaveTabla());
		
		if(relationsOfPropertyData==null || relationsOfPropertyData.isEmpty()) throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre()
				+ " En el campo " + pCampo.getCampoDTO().getNombre() + " no tiene relaciones para identificar cual es campo a consultar");
		pCampoFilter.setCampo(null);
		
		for (RelacionInternaDTO relacionInternaDTO : relationsOfPropertyData) {
			if(relacionInternaDTO.getPlantilla().compareTo(documentToGet.getPlantilla())==0) {
				pCampoFilter.setCampo(relacionInternaDTO.getCampo());
				break;
			}
		}
		
		if(pCampoFilter.getCampo()==null) {
			DocumentoPlantillaDTO templateError = templateService.consultaXId(documentToGet.getPlantilla()); 
			throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre()
					+ " En el campo " + pCampo.getCampoDTO().getNombre() + " no tiene una relacion relacionada con la plantilla "
					+ templateError.getNombre());
		}
				
		
		PedidoVentaCaracteristicaDTO bd = null;
		// En caso que sea una transaccion de aPI de un documento que se esta creando las caracteristicas ya se tienen en memoria
		if(documentToGet.getCaracteristicas()!=null) {
			for (PedidoVentaCaracteristicaDTO iField : documentToGet.getCaracteristicas()) {
				if(iField.getCampo().compareTo(pCampoFilter.getCampo())==0) {
					bd = iField;
					break;
				}
			}
		} else {
			bd = campoService.buscarActivo(pCampoFilter, documentToGet.getHistorico());	
		}
		if(bd ==null) {
			cleanFieldToResponse(pCampo);
		} else {
			pCampo.setValorAuxiliar(bd.getValorAuxiliar());
			pCampo.setValorFechaMin(bd.getValorFecha());
			pCampo.setValorNumeroMin(bd.getValorNumero());
			pCampo.setValorOpcion(bd.getValorOpcion());
			pCampo.setValorText(bd.getValorText());
		}
		
		pCampo.setCampoDTO(pBase);
		return pCampo;
	}



	private void cleanFieldToResponse(PedidoVentaCaracteristicaFilterDTO pCampo) {
		pCampo.setValorAuxiliar(null);
		pCampo.setValorFechaMin(null);
		pCampo.setValorNumeroMin(null);
		pCampo.setValorOpcion(null);
		pCampo.setValorText(null);
	}

}
