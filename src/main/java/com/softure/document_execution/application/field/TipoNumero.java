package com.softure.document_execution.application.field;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.domain.DetallePedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.java.services.CalculatorUtil;
import com.softure.java.services.SoftureUtil;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.domain.PropiedadDTO;

@Component
public class TipoNumero {

	@Autowired @Lazy 
	private PedidoVentaCaracteristicaSvc campoService;
	@Autowired @Lazy 
	private DocumentoPlantillaCaracteristicaSvc caracteristicaService;

	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		System.out.format("\n[%s - %s] Validando.....", pCampo.getCampoDTO().getPlantillaNombre(),
				pCampo.getCampoDTO().getNombre());
		PropiedadDTO bloqProperty = Propiedades.obtenerParametro(pCampo.getCampoDTO(),
				Propiedades.PERMISO_CAMPO_BLOQUEAR);
		String formula = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.NUMERO_FORMULA);
		if (pCampo.getValorNumero() == null) {// Asumo que viene de automatico
			if (!formula.isEmpty()) {
				BigDecimal valorCalculado = calcular(pCampo, formula);
				pCampo.setValorNumero(valorCalculado);
			} else {
				pCampo.setValorNumero(BigDecimal.ZERO);
			}
		} else {
			if (!formula.isEmpty()) {
				if (pCampo.getModificado()) {
					// Valido que del cliente este ien calculado
					if ((pCampo.getLlaveTabla() == null && bloqProperty != null)
							|| (pCampo.getLlaveTabla() != null && Propiedades.obtenerParametro(pCampo.getCampoDTO(),
									Propiedades.PERMISO_CAMPO_MODIFICABLE) == null)) {
						// En tcm se tienen permisos de modificar el seguro
						BigDecimal valorCalculado = calcular(pCampo, formula);
						BigDecimal diferencia = pCampo.getValorNumero().abs().add(valorCalculado.abs().negate());
						if (diferencia.abs().longValue() > 1)
							throw new ServerException("El campo " + pCampo.getCampoDTO().getNombre() + " de la plantilla " + pCampo.getCampoDTO().getPlantillaNombre()
									+ " no se calculo correctamente, valor esperado : ("
									+ SoftureUtil.formatMoney(valorCalculado) + ") y se recibe ("
									+ SoftureUtil.formatMoney(pCampo.getValorNumero()) + ")");
					} /*
						 * else { if (!formula.isEmpty()) { BigDecimal valorCalculado = calcular(pCampo,
						 * formula); pCampo.setValorNumero(valorCalculado); } }
						 */
				}
			}
			// Valido que se calcule bien la funcion
			PropiedadDTO funcionCalculo = Propiedades.obtenerParametro(pCampo.getCampoDTO(),
					Propiedades.NUMERO_FUNCION_SQL);
			if (bloqProperty != null && funcionCalculo != null) {
				// Dividi el tema del depende ya que siempre tengo que calcular el valor sin
				// necesidad del depende
				if (Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(), Propiedades.DEPENDE) != null) {
					if (pCampo.getModificado()) {
						// Valido que del cliente este ien calculado
						if (pCampo.getLlaveTabla() == null
								|| (pCampo.getLlaveTabla() != null && Propiedades.obtenerParametro(pCampo.getCampoDTO(),
										Propiedades.PERMISO_CAMPO_MODIFICABLE) == null)) {
							BigDecimal valorCalculado = campoService.calcularNumeroFuncion(
									funcionCalculo.getLlaveTabla(), pCampo.getDocumento(), pCampo.getDependientes());
							// Algunas funciones no traen el valor del cero
							if (valorCalculado == null)
								valorCalculado = BigDecimal.ZERO;
							BigDecimal diferencia = pCampo.getValorNumero().abs().add(valorCalculado.abs().negate());
							if (diferencia.abs().longValue() > 1)
								throw new ServerException("El campo " + pCampo.getCampoDTO().getNombre()
										+ " no se calculo correctamente, valor esperado : ("
										+ SoftureUtil.formatMoney(valorCalculado) + ") y se recibe ("
										+ SoftureUtil.formatMoney(pCampo.getValorNumero()) + ")");
						}
					}
				} else {
					// En box hay un campo que calcula el area, en guardar todo bien pero al
					// modificar no se guardaba
					// lo arreglo aqui para que si guarde
					pCampo.setModificado(true);
				}
			}
		}

		// Revisar que es opcional
		if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL) == null
				&& pCampo.getValorNumero().compareTo(BigDecimal.ZERO) == 0)
			throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre()
					+ " Es obligatorio registrar el campo " + pCampo.getCampoDTO().getNombre() + "(codigo : "
					+ pCampo.getCampoDTO().getCodigo() + ")");
		// Solo para modificaciones
		if (pCampo.getLlaveTabla() != null
				&& Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_MODIFICABLE) == null
				&& bloqProperty == null && pCampo.getModificado()) {
			PedidoVentaCaracteristicaDTO bd = campoService.buscarActivo(pCampo, pCampo.getPrincipal().getHistorico());
			if (bd == null) {
				if (pCampo.getValorNumero().compareTo(BigDecimal.ZERO) != 0)
					throw new ServerException("El campo " + pCampo.getCampoDTO().getNombre()
							+ " no se puede modificar, valor esperado : " + SoftureUtil.formatMoney(BigDecimal.ZERO));
			} else {
				if (pCampo.getValorNumero().compareTo(bd.getValorNumero()) != 0)
					throw new ServerException(
							"El campo " + pCampo.getCampoDTO().getNombre() + " no se puede modificar, valor esperado : "
									+ SoftureUtil.formatMoney(bd.getValorNumero()));
			}
		}

		try {
			int roundInt = 0;
			String roundNumber = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.NUMERO_REDONDEO);
			if (!roundNumber.isEmpty())
				roundInt = Integer.parseInt(roundNumber);
			pCampo.setValorNumero(pCampo.getValorNumero().setScale(roundInt, RoundingMode.HALF_UP));
		} catch (NumberFormatException e) {
			throw new ServerException(e.getMessage());
		}

		// Validar minimo y maximo
		String minimum = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.NUMERO_MINIMO);
		if (!minimum.isEmpty()) {
			BigDecimal valueMinimum = calcular(pCampo, minimum);
			if (valueMinimum.compareTo(pCampo.getValorNumero()) > 0)
				throw new ServerException("El campo " + pCampo.getCampoDTO().getNombre()
						+ " tiene valor minimo permitido es " + valueMinimum.toString());
		}
		String maximum = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.NUMERO_MAXIMO);
		if (!maximum.isEmpty()) {
			BigDecimal valueMaximum = calcular(pCampo, maximum);
			if (valueMaximum.compareTo(pCampo.getValorNumero()) < 0)
				throw new ServerException("El campo " + pCampo.getCampoDTO().getNombre()
						+ " tiene valor maximo permitido es " + valueMaximum.toString());
		}
		formatText(pCampo);
	}

	private void formatText(PedidoVentaCaracteristicaDTO pCampo) {
		if (!Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.NUMERO_MONEDA).isEmpty()) {
			pCampo.setValorText(SoftureUtil.formatMoney(pCampo.getValorNumero()));
		} else {
			String formato = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.FORMATO);
			if (formato.isEmpty()) {
				pCampo.setValorText(pCampo.getValorNumero().toPlainString());
				if (pCampo.getValorText().endsWith(".0")) {
					pCampo.setValorText(pCampo.getValorText().substring(0, pCampo.getValorText().length() - 2));
				}
			} else {
				pCampo.setValorText(SoftureUtil.formatNumberPattern(pCampo.getValorNumero(), formato));
			}

		}
	}

	public PedidoVentaCaracteristicaDTO guardarCampo(PedidoVentaCaracteristicaDTO pCampo, String token)
			throws ServerException {
		// La idea es calcular unos campos al finalizar el docuemtno
		// eso si ya se debieron guardar todas las precondiciones o depende
		// Coloque null los dependientes solo con esa condicion se calculan al final
		PropiedadDTO funcionCalculo = Propiedades.obtenerParametro(pCampo.getCampoDTO(),
				Propiedades.NUMERO_FUNCION_SQL);
		if (funcionCalculo != null
				&& Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_BLOQUEAR) != null
				&& (Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(), Propiedades.DEPENDE) == null
						|| Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(),
								Propiedades.FUNCION_NUMBER_ALL_CALCULATE_SAVE) != null)) {
			if(pCampo.getDependientes()==null) pCampo.setDependientes(new ArrayList<PedidoVentaCaracteristicaDTO>());
			pCampo.setValorNumero(campoService.calcularNumeroFuncion(funcionCalculo.getLlaveTabla(),
					pCampo.getDocumento(), pCampo.getDependientes()));
			if (pCampo.getValorNumero() == null)
				pCampo.setValorNumero(BigDecimal.ZERO);
			formatText(pCampo);
		}
		PedidoVentaCaracteristicaDTO bd = campoService.buscarActivo(pCampo, pCampo.getPrincipal().getHistorico());
		if (bd != null) {
			if (pCampo.getValorNumero().compareTo(BigDecimal.ZERO) == 0) {
				bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
				bd.setPrincipal(pCampo.getPrincipal());
				campoService.inactivar(bd, token);
				pCampo.setDifference(new PedidoVentaCaracteristicaDTO());
				pCampo.getDifference().setValorNumero(bd.getValorNumero().negate());
				return pCampo;
			} else {
				if (bd.getValorNumero() != null && pCampo.getValorNumero().compareTo(bd.getValorNumero()) == 0) {
					return pCampo;
				} else {
					bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
					bd.setPrincipal(pCampo.getPrincipal());
					campoService.inactivar(bd, token);
					pCampo.setDifference(new PedidoVentaCaracteristicaDTO());
					if (bd.getValorNumero() == null) {
						pCampo.getDifference().setValorNumero(pCampo.getValorNumero());
					} else {
						pCampo.getDifference()
								.setValorNumero(pCampo.getValorNumero().add(bd.getValorNumero().negate()));
					}
				}
			}
		}
		if (pCampo.getValorNumero().compareTo(BigDecimal.ZERO) == 0) {
			return pCampo;
		} else {
			if (pCampo.getDifference() == null) {
				pCampo.setDifference(new PedidoVentaCaracteristicaDTO());
				pCampo.getDifference().setValorNumero(pCampo.getValorNumero());
			}
			return campoService.guardar(pCampo, token);
		}
	}

	private BigDecimal calcular(PedidoVentaCaracteristicaDTO pCampo, String formula) throws ServerException {
		List<PropiedadDTO> codigoDepende = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(),
				Propiedades.DEPENDE);
		if (codigoDepende != null) {
			if (pCampo.getDependientes() == null || pCampo.getDependientes().isEmpty())
				throw new ServerException("Revise los dependientes del campo " + pCampo.getCampoDTO().getNombre());
			for (PedidoVentaCaracteristicaDTO iterable : pCampo.getDependientes()) {
				if (iterable.getValorNumero() == null)
					iterable.setValorNumero(BigDecimal.ZERO);
				if (iterable.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.PRODUCTO) == 0) {
					// Esta aprte fue para fenix para calcular valores internos de los productos
					if (iterable.getDetalles() != null && !iterable.getDetalles().isEmpty()) {
						HashMap<String, BigDecimal> valoresDetallesCampo = new HashMap<String, BigDecimal>();
						for (DetallePedidoVentaDTO iDetalle : iterable.getDetalles()) {
							// Aveces vienen inactivos y esos no toca tenerlos en cuenta
							if (iDetalle.getCaracteristicas() != null && !iDetalle.getCaracteristicas().isEmpty()
									&& (iDetalle.getEstado() == null || iDetalle.getEstado()
											.compareTo(SharedConstants.STATE_INACTIVE) != 0)) {
								for (PedidoVentaCaracteristicaDTO iCaracteristica : iDetalle.getCaracteristicas()) {
									if (iCaracteristica.getCampoDTO() == null)
										iCaracteristica.setCampoDTO(
												caracteristicaService.consultaXIdProducto(iCaracteristica.getCampo()));
									String code = iterable.getCampoDTO().getCodigo() + "_"
											+ iCaracteristica.getCampoDTO().getCodigo();
									BigDecimal acumulado = valoresDetallesCampo.get(code);
									if (acumulado == null) {
										valoresDetallesCampo.put(code, iCaracteristica.getValorNumero());
									} else {
										valoresDetallesCampo.put(code, acumulado.add(iCaracteristica.getValorNumero()));
									}
								}
							}
						}
						for (Map.Entry<String, BigDecimal> entry : valoresDetallesCampo.entrySet()) {
							if (entry.getValue() == null)
								entry.setValue(BigDecimal.ZERO);
							formula = StringUtils.replace(formula, entry.getKey(), entry.getValue().toPlainString());
						}
					}
					formula = formula.replaceAll(iterable.getCampoDTO().getCodigo() + "_[A-Z]*", "0");
				}
				if (iterable.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.PROCESO) == 0) {
					if (iterable.getExpedientes() != null && !iterable.getExpedientes().isEmpty()) {
						HashMap<String, BigDecimal> valoresDetallesCampo = new HashMap<String, BigDecimal>();
						for (PedidoVentaDTO iDetalle : iterable.getExpedientes()) {
							// Aveces vienen inactivos y esos no toca tenerlos en cuenta
							if (iDetalle.getCaracteristicas() != null && !iDetalle.getCaracteristicas().isEmpty()
									&&iDetalle.getDinero()!=null && (iDetalle.getEstado() == null || iDetalle.getEstado()
											.compareTo(SharedConstants.STATE_INACTIVE) != 0)) {
								for (PedidoVentaCaracteristicaDTO iCaracteristica : iDetalle.getCaracteristicas()) {
									if(iCaracteristica.getValorText()!=null) {
										if (iCaracteristica.getCampoDTO() == null)
											iCaracteristica.setCampoDTO(
													caracteristicaService.consultaXId(iCaracteristica.getCampo()));
										String code = iterable.getCampoDTO().getCodigo() + "_";
										if (iCaracteristica.getCampoDTO().getCodigo()==null) {
											code = code	+ SoftureUtil.formatFunction(iCaracteristica.getCampo()) + "_";	
										}else {
											code = code	+ SoftureUtil.formatFunction(iCaracteristica.getCampoDTO().getCodigo()) + "_" ;
										}
										code = code	+ SoftureUtil.formatFunction( iCaracteristica.getValorText()).toUpperCase();
										BigDecimal acumulado = valoresDetallesCampo.get(code);
										if (acumulado == null) {
											valoresDetallesCampo.put(code, iDetalle.getDinero().getValorTotal());
										} else {
											valoresDetallesCampo.put(code, acumulado.add(iDetalle.getDinero().getValorTotal()));
										}	
									}
								}
							}
						}
						for (Map.Entry<String, BigDecimal> entry : valoresDetallesCampo.entrySet()) {
							if (entry.getValue() == null)
								entry.setValue(BigDecimal.ZERO);
							formula = StringUtils.replace(formula, entry.getKey(), entry.getValue().toPlainString());
						}
						formula = formula.replaceAll(iterable.getCampoDTO().getCodigo() + "_[A-Z\\_]*", "0");
					}
				}
				formula = StringUtils.replace(formula, iterable.getCampoDTO().getCodigo(),
						iterable.getValorNumero().toPlainString());
			}
		}
		// Me aparecian errores porque los numeros incluian espacios
		if (formula != null)
			formula = formula.replace(" ", "");
		return CalculatorUtil.calcular(formula);
	}

	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(PedidoVentaCaracteristicaFilterDTO pCampo)
			throws ServerException {
		DocumentoPlantillaCaracteristicaDTO pBase = caracteristicaService
				.consultaUnicaConComplementos(pCampo.getCampo(), pCampo.getSecurityToken());
		PropiedadDTO funcionCalculo = Propiedades.obtenerParametro(pBase, Propiedades.NUMERO_FUNCION_SQL);
		if (funcionCalculo != null) {
			campoService.validarDependientes(pBase, pCampo.getDependientes());
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
				pCampo.setValorNumeroMax(campoService.calcularNumeroFuncion(funcionCalculo.getLlaveTabla(),
						pCampo.getDocumento(), newDependientes));
			} catch (ServerException e) {
				throw new ServerException(e.getMessage(), "Campo: " + pCampo.getCampoDTO().getNombre());
			}

		}
		pCampo.setCampoDTO(pBase);
		return pCampo;
	}
}
