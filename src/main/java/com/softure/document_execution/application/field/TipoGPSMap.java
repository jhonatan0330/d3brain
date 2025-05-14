package com.softure.document_execution.application.field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.shared.domain.ServerException;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.domain.PropiedadDTO;

@Component
public class TipoGPSMap {

	@Autowired @Lazy 
	private PedidoVentaCaracteristicaSvc campoService;
	@Autowired @Lazy 
	private DocumentoPlantillaCaracteristicaSvc caracteristicaService;
	
	@Autowired @Lazy  private PedidoVentaSvc pedidoVentaService;
	
	

	/*public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		if (pCampo.getValorText() != null && pCampo.getValorText().isEmpty())
			pCampo.setValorText(null);
		if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL) == null
				&& pCampo.getValorText() == null)
			throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre()
					+ " Es obligatorio registrar el campo " + pCampo.getCampoDTO().getNombre() + "(codigo : "
					+ pCampo.getCampoDTO().getCodigo() + ")");
		if (pCampo.getValorText() == null)
			return;
		Pattern pat = Pattern.compile(
				"^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$");
		Matcher mat = pat.matcher(pCampo.getValorText());
		if (!mat.matches())
			throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre()
					+ " El campo no cumple con el format de las coordenadas latitud y longitud");
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
			gpsReportLocationService.callByForm(token, pCampo.getValorText(), pCampo.getDocumento(),
					pCampo.getPrincipal().getNombre());
			return campoService.guardar(pCampo, token);
		}
	}*/

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
						hmap.put(iOcupado.getCampo(),
								caracteristicaService.consultaXId(iOcupado.getCampo()));
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
