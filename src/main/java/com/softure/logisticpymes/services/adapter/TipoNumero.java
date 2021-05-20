package com.softure.logisticpymes.services.adapter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.java.services.CalculatorUtil;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.dto.DetallePedidoVentaDTO;
import com.softure.logisticpymes.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.dto.PropiedadDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaCaracteristicaFilterDTO;
import com.softure.logisticpymes.services.DocumentoPlantillaCaracteristicaSvc;
import com.softure.logisticpymes.services.PedidoVentaCaracteristicaSvc;

@Component
public class TipoNumero {
	
	@Autowired private PedidoVentaCaracteristicaSvc campoService;
	@Autowired private DocumentoPlantillaCaracteristicaSvc caracteristicaService;
	
	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException{		

		if(pCampo.getValorNumero()==null) {//Asumo que viene de automatico
			if(!Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.NUMERO_FORMULA).isEmpty()) {
				BigDecimal valorCalculado = calcular(pCampo);
				pCampo.setValorNumero(valorCalculado);
			}else {
				pCampo.setValorNumero(BigDecimal.ZERO);
			}
		}else {
			if(!Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.NUMERO_FORMULA).isEmpty()) {
				if(pCampo.getModificado()) {
					//Valido que del cliente este ien calculado
					if((pCampo.getLlaveTabla()==null && Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_BLOQUEAR)!=null)
							|| (pCampo.getLlaveTabla()!=null && Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_MODIFICABLE)!=null)){
						BigDecimal valorCalculado = calcular(pCampo);
						if(pCampo.getValorNumero().compareTo(valorCalculado)!=0) 
							throw new ServerException("El campo " + pCampo.getCampoDTO().getNombre() + " no se calculo correctamente, valor esperado : (" + SoftureUtil.formatMoney(valorCalculado) +  ") y se recibe (" + SoftureUtil.formatMoney(pCampo.getValorNumero()) +")");
					}
				}
			}
		}
		
		if(Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL)==null && pCampo.getValorNumero().compareTo(BigDecimal.ZERO)==0) throw new ServerException("Es obligatorio registrar el campo " + pCampo.getCampoDTO().getNombre());
		if(pCampo.getLlaveTabla()!=null && Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_MODIFICABLE)==null && pCampo.getModificado()){
			PedidoVentaCaracteristicaDTO bd = campoService.buscarActivo(pCampo);
			if(bd==null){
				if(pCampo.getValorNumero().compareTo(BigDecimal.ZERO)!=0)
					throw new ServerException("El campo " + pCampo.getCampoDTO().getNombre() + " no se puede modificar, valor esperado : " + SoftureUtil.formatMoney(BigDecimal.ZERO));
			}else{
				if(pCampo.getValorNumero().compareTo(bd.getValorNumero())!=0)
					throw new ServerException("El campo " + pCampo.getCampoDTO().getNombre() + " no se puede modificar, valor esperado : " + SoftureUtil.formatMoney(bd.getValorNumero()));
			}
		}
		formatText(pCampo);
	}

	private void formatText(PedidoVentaCaracteristicaDTO pCampo) {
		if(!Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.NUMERO_MONEDA).isEmpty()){
			pCampo.setValorText(SoftureUtil.formatMoney(pCampo.getValorNumero()));	
		}else{
			pCampo.setValorText(pCampo.getValorNumero().toPlainString());
			if(pCampo.getValorText().endsWith(".0")){
				pCampo.setValorText(pCampo.getValorText().substring(0, pCampo.getValorText().length()-2));
			}
		}
	}
	
	
	
	public PedidoVentaCaracteristicaDTO guardarCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException{
		//La idea es calcular unos campos al finalizar el docuemtno
		//eso si ya se debieron guardar todas las precondiciones o depende
		PropiedadDTO funcionCalculo = Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.NUMERO_FUNCION_SQL);
		if(Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_BLOQUEAR)!=null && funcionCalculo!=null){
			pCampo.setValorNumero(campoService.calcularNumeroFuncion(funcionCalculo.getLlaveTabla(), pCampo.getDocumento(), pCampo.getDependientes()));
			if(pCampo.getValorNumero()==null) pCampo.setValorNumero(BigDecimal.ZERO);
			formatText(pCampo);
		}
		PedidoVentaCaracteristicaDTO bd = campoService.buscarActivo(pCampo);
		if(bd!=null){
			if(pCampo.getValorNumero().compareTo(BigDecimal.ZERO)==0){
				bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
				campoService.inactivar(bd, token);
				return pCampo;
			}else{
				if(pCampo.getValorNumero().compareTo(bd.getValorNumero())==0){
					return pCampo;
				}else{
					bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
					campoService.inactivar(bd, token);
				}
			}
		}
		if(pCampo.getValorNumero().compareTo(BigDecimal.ZERO)==0){
			return pCampo;
		}else{
			
			return campoService.guardar(pCampo, token);
		}
	}
	
	private BigDecimal calcular(PedidoVentaCaracteristicaDTO pCampo) throws ServerException{
		String formula = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.NUMERO_FORMULA);
		List<PropiedadDTO> codigoDepende = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(), Propiedades.DEPENDE);
		if(codigoDepende!=null){
			if(pCampo.getDependientes()==null || pCampo.getDependientes().isEmpty())throw new ServerException("Revise los dependientes del campo " +  pCampo.getCampoDTO().getNombre());
			for (PedidoVentaCaracteristicaDTO iterable : pCampo.getDependientes()){
				if(iterable.getValorNumero()==null) iterable.setValorNumero(BigDecimal.ZERO);
				if(iterable.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.PRODUCTO)==0) {
					//Esta aprte fue para fenix para calcular valores internos de los productos
					if(iterable.getDetalles()!=null && !iterable.getDetalles().isEmpty()) {
						HashMap<String, BigDecimal> valoresDetallesCampo = new HashMap<String, BigDecimal>();
						for (DetallePedidoVentaDTO iDetalle : iterable.getDetalles()){
							
							if(iDetalle.getCaracteristicas()!=null && !iDetalle.getCaracteristicas().isEmpty()
									&& (iDetalle.getEstado()==null || iDetalle.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO)!=0)) {//Aveces vienen inactivos y esos no toca tenerlos en cuenta
								for (PedidoVentaCaracteristicaDTO iCaracteristica : iDetalle.getCaracteristicas()){
									if(iCaracteristica.getCampoDTO()==null) iCaracteristica.setCampoDTO(caracteristicaService.consultaXIdProducto(iCaracteristica.getCampo()));
									String code = iterable.getCampoDTO().getCodigo() + "_" + iCaracteristica.getCampoDTO().getCodigo();
									BigDecimal acumulado = valoresDetallesCampo.get(code);
									if(acumulado==null) {
										valoresDetallesCampo.put(code, iCaracteristica.getValorNumero());
									}else {
										valoresDetallesCampo.put(code, acumulado.add( iCaracteristica.getValorNumero()));
									}
								}	
							}
						}
						for (Map.Entry<String,BigDecimal> entry : valoresDetallesCampo.entrySet()) {
							if(entry.getValue()==null) entry.setValue(BigDecimal.ZERO);
							formula = StringUtils.replace(formula, entry.getKey(), entry.getValue().toPlainString() );							
						}
					} 
					formula = formula.replaceAll(iterable.getCampoDTO().getCodigo() + "_[A-Z]*" , "0");
				}
                formula = StringUtils.replace(formula, iterable.getCampoDTO().getCodigo(), iterable.getValorNumero().toPlainString());
			}
		}
		//Me aparecian errores porque los numeros incluian espacios
		if(formula!=null) formula = formula.replace(" ", "");
		return CalculatorUtil.calcular(formula);
	}
	
	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(PedidoVentaCaracteristicaFilterDTO pCampo) throws ServerException {		
		DocumentoPlantillaCaracteristicaDTO pBase = caracteristicaService.consultaUnicaConComplementos(pCampo.getCampo(), pCampo.getSecurityToken());
		PropiedadDTO funcionCalculo = Propiedades.obtenerParametro(pBase, Propiedades.NUMERO_FUNCION_SQL);
		if(funcionCalculo!=null){
			campoService.validarDependientes(pBase, pCampo.getDependientes());
			List<PedidoVentaCaracteristicaDTO> newDependientes =  campoService.ordenarAlfabeticaDepende(pCampo.getDependientes());
			for (PedidoVentaCaracteristicaDTO iDep : newDependientes) {
				if (iDep.getValorOpcion() == null) {
					if(iDep.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.NUMERO)==0) {
						iDep.setValorOpcion(iDep.getValorNumero().toString());
					}
				}
			}
			try {
				pCampo.setValorNumeroMax(campoService.calcularNumeroFuncion(funcionCalculo.getLlaveTabla(), pCampo.getDocumento(), newDependientes));			
			} catch (ServerException e) {
				throw new ServerException(e.getMessage(), "Funcion de Calculo : " + funcionCalculo.getLlaveTabla() + "\n Campo: " + pCampo.getCampoDTO().getNombre());
			}
			
		}
		pCampo.setCampoDTO(pBase);
		return pCampo;
	}
}
