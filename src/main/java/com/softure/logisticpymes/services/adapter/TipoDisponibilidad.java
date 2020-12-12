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
import com.softure.logisticpymes.dto.filter.DocumentoPlantillaCaracteristicaFilterDTO;
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
	
	public PedidoVentaCaracteristicaDTO guardarCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException{
		PedidoVentaCaracteristicaDTO bd = campoService.buscarActivo(pCampo);
		if(bd!=null){
			if(pCampo.getValorText()==null){
				bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
				campoService.inactivar(bd, token);
				return pCampo;
			}else{
				if(pCampo.getValorText().compareTo(bd.getValorText())==0){
					return pCampo;
				}else{
					bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
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
		//if(pBase.getCodigoDepende()==null) throw new ServerException("Es necesario colocar la caracteristica dependiente que tendra el croquis. Tipo Disponibilidad");
		//if(pCampo.getDependientes()==null || pCampo.getDependientes().isEmpty())throw new ServerException("Revise los dependientes. Tipo Disponibilidad");
		//if(pCampo.getDependientes().size()!=1) throw new ServerException("Los tipo disponibilidad permiten solo un dependiente");
		if(Propiedades.obtenerValor(pBase, Propiedades.DISPONIBILIDAD_CROQUIS).isEmpty()) throw new ServerException("Es necesario colocar la caracteristica del Documento base que tiene el croquis. Tipo Disponibilidad");
		//if(CampoUtilidades.obtenerValor(pBase, CampoUtilidades.PLANTILLA_AUXILIAR).isEmpty()) throw new ServerException("Es necesario colocar la plantilla del Documento base que tiene el croquis. Tipo Disponibilidad");
		
		if(pCampo.getDependientes()!=null && pCampo.getDependientes().size()!=0) {
			PedidoVentaCaracteristicaDTO vCampoViaje = pCampo.getDependientes().get(0);
			if(vCampoViaje.getValorOpcion()==null) throw new ServerException("Esta consultando un valor vacio del dependiente. Tipo Disponibilidad");
			DocumentoPlantillaCaracteristicaDTO vBaseViaje = baseService.consultaUnicaConComplementos(vCampoViaje.getCampo(), pCampo.getSecurityToken());
			
			DocumentoPlantillaCaracteristicaFilterDTO vBaseVehiculoFilter = new DocumentoPlantillaCaracteristicaFilterDTO();
			vBaseVehiculoFilter.setPlantilla(Propiedades.obtenerValor(vBaseViaje, Propiedades.PLANTILLA_AUXILIAR));
			vBaseVehiculoFilter.setCodigo(Propiedades.obtenerValor(pBase, Propiedades.DISPONIBILIDAD_CROQUIS));
			vBaseVehiculoFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			DocumentoPlantillaCaracteristicaDTO vBaseVehiculo = baseService.consultaUnica(vBaseVehiculoFilter);
			if(vBaseVehiculo==null) throw new ServerException("No se identifica el campo " + Propiedades.obtenerValor(pBase, Propiedades.DISPONIBILIDAD_CROQUIS) +  " de " + vBaseViaje.getNombre());
			vBaseVehiculo = baseService.consultaUnicaConComplementos(vBaseVehiculo.getLlaveTabla(), pCampo.getSecurityToken());
			
			PedidoVentaCaracteristicaFilterDTO vVehiculoFilter = new PedidoVentaCaracteristicaFilterDTO();
			vVehiculoFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			vVehiculoFilter.setDocumento(vCampoViaje.getValorOpcion());
			vVehiculoFilter.setCampo(vBaseVehiculo.getLlaveTabla());
			PedidoVentaCaracteristicaDTO vVehiculo = campoService.consultaUnica(vVehiculoFilter);
			
			if(vVehiculo==null)  throw new ServerException("No se identifica el " + vBaseVehiculo.getNombre() );
		}
		
		PedidoVentaCaracteristicaDTO vCroquis = campoService.consultarCampoCroquis(Propiedades.obtenerValor(pBase, Propiedades.DISPONIBILIDAD_CROQUIS));
			
		if(vCroquis!=null){
			pBase.setImagen(vCroquis.getValorText());
			PuestoFilterDTO filtro = new PuestoFilterDTO();
			filtro.setCampo(vCroquis.getLlaveTabla());
			filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			List<PuestoDTO> componentesActuales = puestoService.listarConsulta(filtro);
			if(componentesActuales!=null && !componentesActuales.isEmpty()){
				pBase.setDocumentos(new ArrayList<PedidoVentaDTO>());
				for(PuestoDTO actual:componentesActuales){
					pBase.getDocumentos().add(convertirPuestoEnDocumento(actual));
				}
			}
		}else {
			throw new ServerException("No se identifica el campo croquis del documento con nombre" + Propiedades.obtenerValor(pBase, Propiedades.DISPONIBILIDAD_CROQUIS) );
		}
		pCampo.setCampoDTO(pBase);
		return pCampo;
	}

	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException{
		if(Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL)==null && (pCampo.getExpedientes()==null || pCampo.getExpedientes().isEmpty())) throw new ServerException("Es necesario registrar el campo " + pCampo.getCampoDTO().getNombre());
		List<PropiedadDTO> codigoDepende = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(), Propiedades.DEPENDE);
		if(codigoDepende!=null){
			if(pCampo.getDependientes()==null || pCampo.getDependientes().isEmpty())throw new ServerException("Revise los dependientes. Tipo Disponibilidad");
			if(pCampo.getDependientes().size()!=1) throw new ServerException("Los tipo disponibilidad permiten solo un dependiente");
			if(pCampo.getDependientes().get(0).getValorOpcion()==null) throw new ServerException("El dependieente de viaje debe incluir la llave");
			pCampo.setValorOpcion(pCampo.getDependientes().get(0).getValorOpcion());
			pCampo.setValorAuxiliar(pCampo.getDependientes().get(0).getCampo());
		}
		if(pCampo.getExpedientes()!=null){
			pCampo.setValorText("");
			for(PedidoVentaDTO componente : pCampo.getExpedientes()){
				pCampo.setValorText( pCampo.getValorText() + "-" + componente.getNombre());
			}
		}else{
			pCampo.setValorText(null);
		}
	}
	
}
