package com.softure.logisticpymes.services.adapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.domain.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaDineroDTO;
import com.softure.logisticpymes.domain.dto.PuestoDTO;
import com.softure.logisticpymes.domain.filter.PedidoVentaCaracteristicaFilterDTO;
import com.softure.logisticpymes.domain.filter.PuestoFilterDTO;
import com.softure.logisticpymes.services.DocumentoPlantillaCaracteristicaSvc;
import com.softure.logisticpymes.services.PedidoVentaCaracteristicaSvc;
import com.softure.logisticpymes.services.PuestoSvc;

@Component
public class TipoCroquis {
	
	@Autowired private DocumentoPlantillaCaracteristicaSvc caracteristicaService;
	@Autowired private PedidoVentaCaracteristicaSvc campoService;
	@Autowired private PuestoSvc puestoService;
	
	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException{
		if(Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL)==null && (pCampo.getValorText()==null || pCampo.getValorText().isEmpty()))
			throw new ServerException("Es obligatorio registrar el campo " + pCampo.getCampoDTO().getNombre());
		if(pCampo.getExpedientes()!=null && !pCampo.getExpedientes().isEmpty()){
			List<PuestoDTO> componentesActuales = null;
			if(pCampo.getLlaveTabla()!=null){
				PuestoFilterDTO filtro = new PuestoFilterDTO();
				filtro.setCampo(pCampo.getLlaveTabla());
				filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
				componentesActuales = puestoService.listarConsulta(filtro);
			}
			for(PedidoVentaDTO componente: pCampo.getExpedientes()){
				if(componente.getDinero()==null) throw new ServerException("Revisa porque dinero viene vacio");
				if(componente.getDinero().getSaldo().compareTo(BigDecimal.ZERO)==0) throw new ServerException("Los campos no pueden tener coordenada 0 en x");
				if(componente.getDinero().getValorTotal().compareTo(BigDecimal.ZERO)==0) throw new ServerException("Los campos no pueden tener coordenada 0 en Y");
				if(componente.getNombre()==null) throw new ServerException("Los campos deben tener nombre");
				
				componente.setEstado(null);
				
				if(componente.getLlaveTabla()!=null){
					if(componentesActuales!=null && !componentesActuales.isEmpty()){
						for(PuestoDTO actual:componentesActuales){
							if(actual.getLlaveTabla().compareTo(componente.getLlaveTabla())==0){
								if(actual.getNombre().compareTo(componente.getNombre())==0  && actual.getColumna().compareTo(componente.getDinero().getSaldo().intValue())==0 
										&& actual.getFila().compareTo(componente.getDinero().getValorTotal().intValue())==0) componente.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
								if(componente.getEstado()!=null) componentesActuales.remove(actual);
								break;
							}
						}
					}
				}
			}
			if(componentesActuales!=null && !componentesActuales.isEmpty()){
				for(PuestoDTO actual:componentesActuales){
					PedidoVentaDTO adicionarI = new PedidoVentaDTO();
					adicionarI.setLlaveTabla(actual.getLlaveTabla());
					adicionarI.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
					pCampo.getExpedientes().add(adicionarI);
				}
			}
		}
	}
	
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
					actualizarExpedientes(pCampo, token);
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
			PedidoVentaCaracteristicaDTO result = campoService.guardar(pCampo, token);
			pCampo.setLlaveTabla(result.getLlaveTabla());
			actualizarExpedientes(pCampo, token);
			return result;
		}
	}
	
	private void actualizarExpedientes(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException{
		if(pCampo.getExpedientes()!=null){
			if(pCampo.getLlaveTabla()==null) throw new ServerException("Revise porque la llave del campo es nula. Tipo Croquis");
			for(PedidoVentaDTO componente: pCampo.getExpedientes()){
				if (componente.getEstado()==null){
					PuestoDTO nuevo = new  PuestoDTO();
					nuevo.setColumna(componente.getDinero().getSaldo().intValue());
					nuevo.setFila(componente.getDinero().getValorTotal().intValue());
					nuevo.setCampo(pCampo.getLlaveTabla());
					nuevo.setNombre(componente.getNombre());
					nuevo.setImagen(componente.getImagen());
					puestoService.guardar(nuevo, token);
				}else{
					if(componente.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO)==0){
						PuestoDTO inactivar = new  PuestoDTO();
						inactivar.setLlaveTabla(componente.getLlaveTabla());
						puestoService.inactivar(inactivar, token);
					}
				}
			}
		}
	}

	public void cargarConsultaCampo(PedidoVentaCaracteristicaDTO pCampo) throws ServerException {
		if(pCampo.getLlaveTabla()!=null){
			PuestoFilterDTO filtro = new PuestoFilterDTO();
			filtro.setCampo(pCampo.getLlaveTabla());
			filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			List<PuestoDTO> componentesActuales = puestoService.listarConsulta(filtro);
			if(componentesActuales!=null && !componentesActuales.isEmpty()){
				pCampo.setExpedientes(new ArrayList<PedidoVentaDTO>());
				for(PuestoDTO actual:componentesActuales){
					pCampo.getExpedientes().add(convertirPuestoEnDocumento(actual));
				}
			}
		}
	}
	
	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(PedidoVentaCaracteristicaFilterDTO pCampo) throws ServerException {
		if(pCampo==null || pCampo.getCampo()==null) throw new ServerException("Revise la parametro del metodo");
		DocumentoPlantillaCaracteristicaDTO pBase = caracteristicaService.consultaXId(pCampo.getCampo());
		if(pBase==null) throw new ServerException("Error en el identificador de la caracteristica");
		
		if(pCampo.getLlaveTabla()!=null){
			PuestoFilterDTO filtro = new PuestoFilterDTO();
			filtro.setCampo(pCampo.getLlaveTabla());
			filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			List<PuestoDTO> componentesActuales = puestoService.listarConsulta(filtro);
			if(componentesActuales!=null && !componentesActuales.isEmpty()){
				pCampo.setExpedientes(new ArrayList<PedidoVentaDTO>());
				for(PuestoDTO actual:componentesActuales){
					pCampo.getExpedientes().add(convertirPuestoEnDocumento(actual));
				}
			}
		}
		
		pCampo.setCampoDTO(pBase);
		
		return pCampo;
	}
	
	private PedidoVentaDTO convertirPuestoEnDocumento(PuestoDTO actual) {
		PedidoVentaDTO componente = new PedidoVentaDTO();
		componente.setLlaveTabla(actual.getLlaveTabla());
		componente.setNombre(actual.getNombre());
		componente.setDinero(new PedidoVentaDineroDTO());
		componente.getDinero().setValorTotal(new BigDecimal(actual.getFila()));
		componente.getDinero().setSaldo(new BigDecimal(actual.getColumna()));
		componente.setImagen(actual.getImagen());
		return componente;
	}
		
}
