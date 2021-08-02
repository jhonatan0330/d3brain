package com.softure.logisticpymes.services.adapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.dto.PedidoVentaDTO;
import com.softure.logisticpymes.dto.PedidoVentaDineroDTO;
import com.softure.logisticpymes.dto.PropiedadDTO;
import com.softure.logisticpymes.dto.PuestoDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaCaracteristicaFilterDTO;
import com.softure.logisticpymes.dto.filter.PuestoFilterDTO;
import com.softure.logisticpymes.services.DocumentoPlantillaCaracteristicaSvc;
import com.softure.logisticpymes.services.PedidoVentaCaracteristicaSvc;
import com.softure.logisticpymes.services.PuestoSvc;

@Component
public class TipoDisponibilidad {
	
	@Autowired private DocumentoPlantillaCaracteristicaSvc baseService;
	@Autowired private PedidoVentaCaracteristicaSvc campoService;
	@Autowired private PuestoSvc puestoService;
	
	public PedidoVentaCaracteristicaDTO guardarCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException{
		PedidoVentaCaracteristicaDTO bd = campoService.buscarActivo(pCampo, pCampo.getPrincipal().getHistorico());
		if(bd!=null){
			if(pCampo.getValorText()==null){
				bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
				bd.setPrincipal(pCampo.getPrincipal());
				campoService.inactivar(bd, token);
				return pCampo;
			}else{
				if(pCampo.getValorText().compareTo(bd.getValorText())==0){
					return pCampo;
				}else{
					bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
					bd.setPrincipal(pCampo.getPrincipal());
					campoService.inactivar(bd, token);
				}
			}
		}
		if(pCampo.getValorText()==null){
			return pCampo;
		}else{
			pCampo = campoService.guardar(pCampo, token);
			return pCampo;
		}
	}

	public void cargarConsultaCampo(PedidoVentaCaracteristicaDTO pCampo) throws ServerException {
		if(pCampo.getValorOpcion()!=null && pCampo.getValorAuxiliar()!=null){
			pCampo.setDependientes(new ArrayList<PedidoVentaCaracteristicaDTO>());
			PedidoVentaCaracteristicaDTO vCampoViaje = new PedidoVentaCaracteristicaDTO();
			vCampoViaje.setValorOpcion(pCampo.getValorOpcion());
			vCampoViaje.setCampo(pCampo.getValorAuxiliar());
			pCampo.getDependientes().add(vCampoViaje);
			// consultarDatosBase(pCampo);  -Lo quito en la migracion de filters
			pCampo.setDependientes(null);
		}
	}

	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(PedidoVentaCaracteristicaFilterDTO pCampo) throws ServerException {
		DocumentoPlantillaCaracteristicaDTO pBase = baseService.consultaUnicaConComplementos(pCampo.getCampo(), pCampo.getSecurityToken());
		if(pCampo.getDependientes()==null || pCampo.getDependientes().isEmpty())throw new ServerException("Revise los dependientes. Tipo Disponibilidad");
		String estructura = Propiedades.obtenerValor(pBase, Propiedades.DISPONIBILIDAD_CROQUIS);
		if(estructura.isEmpty()) throw new ServerException("Es necesario colocar la caracteristica del Documento base que tiene el croquis. Tipo Disponibilidad");
		PedidoVentaCaracteristicaDTO dependienteCroquis = null;
		for (PedidoVentaCaracteristicaDTO iDependiente : pCampo.getDependientes()) {
			if(iDependiente.getCampo().compareTo(estructura)==0) {
				dependienteCroquis = iDependiente;
				break;
			}
		}
		
		if(dependienteCroquis==null) throw new ServerException("No se encontro en los dependientes la estructura del croquis");
		PedidoVentaCaracteristicaDTO vCroquis = campoService.consultarCampoCroquis(dependienteCroquis.getValorOpcion());
		if(vCroquis==null) throw new ServerException("La estructura no tiene un campo croquis que se encuentre activo");

		pBase.setImagen(vCroquis.getValorText());
		PuestoFilterDTO filtro = new PuestoFilterDTO();
		filtro.setCampo(vCroquis.getLlaveTabla());
		filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		List<PuestoDTO> componentesActuales = puestoService.listarConsulta(filtro);
		if(componentesActuales!=null && !componentesActuales.isEmpty()){
			pBase.setDocumentos(new ArrayList<PedidoVentaDTO>());
			for (PuestoDTO actual : componentesActuales){
				pBase.getDocumentos().add(convertirPuestoEnDocumento(actual));
			}
			PropiedadDTO funcion = Propiedades.obtenerParametro(pBase, Propiedades.DISPONIBILIDAD_FUNCION_SQL);
			if(funcion != null) {
				campoService.validarDependientes(pBase, pCampo.getDependientes());
				List<PedidoVentaCaracteristicaDTO> ocupados =  campoService.camposOcupadosCroquis(funcion.getLlaveTabla(), pCampo.getDocumento(), campoService.ordenarAlfabeticaDepende(pCampo.getDependientes()));
				if(ocupados!=null && !ocupados.isEmpty()) {
					for (PedidoVentaCaracteristicaDTO iOcupado : ocupados) {
						String[] pOcupados = iOcupado.getValorText().split("-");
						for (String iPuesto : pOcupados) {
							if(!iPuesto.isEmpty()) {
								for (PedidoVentaDTO iEspacio : pBase.getDocumentos()) {
									if(iEspacio.getNombre().compareTo(iPuesto)==0) {
										iEspacio.setLlaveTabla(iOcupado.getDocumento());
										iEspacio.setPlantilla(iOcupado.getValorAuxiliar());
										iEspacio.setEstadoExpediente(iOcupado.getEstado());
										iEspacio.setEstadoNombre(iOcupado.getTransaccionRegistro());
										break;
									}
								}
							}
						}
					}
				}
			}
		}

		pCampo.setCampoDTO(pBase);
		
		return pCampo;
	}
	
	private PedidoVentaDTO convertirPuestoEnDocumento(PuestoDTO actual) {
		PedidoVentaDTO componente = new PedidoVentaDTO();
		// componente.setLlaveTabla(actual.getLlaveTabla());
		componente.setNombre(actual.getNombre());
		componente.setDinero(new PedidoVentaDineroDTO());
		componente.getDinero().setValorTotal(new BigDecimal(actual.getFila()));
		componente.getDinero().setSaldo(new BigDecimal(actual.getColumna()));
		componente.setImagen(actual.getImagen());
		return componente;
	}

	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException{
		if(Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL)==null && (pCampo.getExpedientes()==null || pCampo.getExpedientes().isEmpty())) throw new ServerException("Es necesario registrar el campo " + pCampo.getCampoDTO().getNombre());
		if(pCampo.getExpedientes()!=null){
			PropiedadDTO funcion = Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.DISPONIBILIDAD_FUNCION_SQL);
			
			if(funcion != null) {
				campoService.validarDependientes(pCampo.getCampoDTO(), pCampo.getDependientes());
				List<PedidoVentaCaracteristicaDTO> ocupados =  campoService.camposOcupadosCroquis(funcion.getLlaveTabla(), pCampo.getLlaveTabla(), campoService.ordenarAlfabeticaDepende(pCampo.getDependientes()));
				if(ocupados!=null && !ocupados.isEmpty()) {
					for (PedidoVentaCaracteristicaDTO iOcupado : ocupados) {
						String[] pOcupados = iOcupado.getValorText().split("-");
						for (String iPuesto : pOcupados) {
							if(!iPuesto.isEmpty()) {
								for (PedidoVentaDTO actual : pCampo.getExpedientes()){
									if(actual.getNombre().compareTo(iPuesto)==0) {
										throw new ServerException("El "+ pCampo.getCampoDTO().getNombre() +" " + iPuesto + " ya se encuentra ocupado");
									}
								}
							}
						}
					}
				}
			}
			pCampo.setValorText("");
			for(PedidoVentaDTO componente : pCampo.getExpedientes()){
				pCampo.setValorText( pCampo.getValorText() + "-" + componente.getNombre());
			}
		}else{
			pCampo.setValorText(null);
		}
	}
	
}
