package com.softure.document_execution.application.field;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shared.domain.ServerException;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.java.services.SoftureUtil;
import com.softure.property.domain.PropiedadDTO;

@Component
public class TipoFecha {

	@Autowired
	private PedidoVentaCaracteristicaSvc campoService;

	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		System.out.format("\n[%s - %s] Validando.....", pCampo.getCampoDTO().getPlantillaNombre(),
				pCampo.getCampoDTO().getNombre());
		if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL) == null
				&& pCampo.getValorFecha() == null)
			throw new ServerException("Es obligatorio colocar el campo " + pCampo.getCampoDTO().getNombre()
					+ " del formulario " + pCampo.getCampoDTO().getPlantillaNombre());
		if (pCampo.getValorFecha() != null) {
			String rango = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.FECHA_RANGO);
			if (!rango.isEmpty()) {
				if (pCampo.getValorAuxiliar() == null)
					throw new ServerException("El valor auxiliar debe indicar el rango usado");
				if (pCampo.getValorNumero() == null || pCampo.getValorNumero().compareTo(BigDecimal.ZERO) == 0)
					throw new ServerException("Debe colocar el numero de tiempo de la fecha final");

				// long rangoSeleccionadoLong = pCampo.getValorFecha().getTime() -
				// pCampo.getValorNumero().longValue();
				PropiedadDTO rangoMaximo = Propiedades.obtenerParametro(pCampo.getCampoDTO(),
						Propiedades.FECHA_RANGO_MAXIMO);
				if (rangoMaximo != null) {
					Calendar fechaInicial = Calendar.getInstance();
					fechaInicial.setTime(pCampo.getValorFecha());
					Calendar fechaFinal = Calendar.getInstance();
					fechaFinal.setTimeInMillis(pCampo.getValorFecha().getTime() + pCampo.getValorNumero().longValue());
					String difference = "";
					try {
						String[] periodos = rangoMaximo.getValor().split("T");
						if (periodos[0].length() > 1) {
							Period pr = Period.parse(periodos[0]);
							if (pr.getYears() != 0) {
								fechaInicial.add(Calendar.YEAR, pr.getYears());
								difference = difference + pr.getYears() + " años, ";
							}
							if (pr.getMonths() != 0) {
								fechaInicial.add(Calendar.MONTH, pr.getMonths());
								difference = difference + pr.getMonths() + " meses, ";
							}
							if (pr.getDays() != 0) {
								fechaInicial.add(Calendar.DATE, pr.getDays());
								difference = difference + pr.getDays() + " dias, ";
							}
						}
						if (periodos.length > 1) {
							Duration lt = Duration.parse("PT" + periodos[1]);
							if (lt.toHoursPart() != 0) {
								fechaInicial.add(Calendar.HOUR, lt.toHoursPart());
								difference = difference + lt.toHoursPart() + " horas, ";
							}

							if (lt.toMinutesPart() != 0) {
								fechaInicial.add(Calendar.MINUTE, (int) lt.toMinutesPart());
								difference = difference + lt.toMinutesPart() + " minutos, ";
							}
							if (lt.toSecondsPart() != 0) {
								fechaInicial.add(Calendar.SECOND, (int) lt.toSecondsPart());
								difference = difference + lt.toSecondsPart() + " segundos, ";
							}
						}
						if (fechaInicial.before(fechaFinal))
							throw new ServerException("La fecha " + pCampo.getCampoDTO().getNombre()
									+ " no debe superar un periodo de " + difference);// + );
					} catch (DateTimeParseException e) {
						throw new ServerException("La fecha " + pCampo.getCampoDTO().getNombre()
								+ " no tiene configurado correctamente el periodo a validar (FECHA_MAXIMO_RANGO) : "
								+ rangoMaximo.getValor() + " e= " + e.getMessage());
					}
				}
				switch (pCampo.getValorAuxiliar()) {
				case "D": {
					pCampo.setValorText(SoftureUtil.formatDate(pCampo.getValorFecha()));
					break;
				}
				case "M": {
					pCampo.setValorText(SoftureUtil.formatDateMonth(pCampo.getValorFecha()));
					break;
				}
				case "A": {
					pCampo.setValorText(SoftureUtil.formatDateYear(pCampo.getValorFecha()));
					break;
				}
				case "R": {
					pCampo.setValorText(
							DurationFormatUtils.formatDurationWords(pCampo.getValorNumero().longValue(), true, true));
					break;
				}
				}
			} else {
				if (Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.FECHA_CON_HORA).isEmpty()) {
					Calendar fecha = Calendar.getInstance();
					fecha.setTime(pCampo.getValorFecha());
					fecha.set(Calendar.HOUR_OF_DAY, 0);
					fecha.set(Calendar.MINUTE, 0);
					fecha.set(Calendar.SECOND, 0);
					fecha.set(Calendar.MILLISECOND, 0);
					pCampo.setValorFecha(fecha.getTime());
					pCampo.setValorText(SoftureUtil.formatDate(pCampo.getValorFecha()));
					if (Propiedades.obtenerParametro(pCampo.getCampoDTO(),
							Propiedades.PERMISO_CAMPO_BLOQUEAR) != null) {
						fecha.setTime(new Date());
						fecha.set(Calendar.HOUR_OF_DAY, 0);
						fecha.set(Calendar.MINUTE, 0);
						fecha.set(Calendar.SECOND, 0);
						fecha.set(Calendar.MILLISECOND, 0);
						if (pCampo.getValorFecha().getTime() != fecha.getTime().getTime())
							throw new ServerException("El campo " + pCampo.getCampoDTO().getNombre()
									+ " permite la fecha " + SoftureUtil.formatDate(fecha.getTime())
									+ ". Y la fecha recibida es " + SoftureUtil.formatDate(pCampo.getValorFecha()));
					}
				} else {
					Calendar hora = Calendar.getInstance();
					hora.setTime(pCampo.getValorFecha());
					hora.set(Calendar.SECOND, 0);
					hora.set(Calendar.MILLISECOND, 0);
					if (Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.FECHA_SIN_CALENDAR).isEmpty()) {
						pCampo.setValorFecha(hora.getTime());
						pCampo.setValorText(SoftureUtil.formatDateTime(pCampo.getValorFecha()));
						if (Propiedades.obtenerParametro(pCampo.getCampoDTO(),
								Propiedades.PERMISO_CAMPO_BLOQUEAR) != null) {
							hora.setTime(new Date());
							if (Math.abs(pCampo.getValorFecha().getTime() - hora.getTime().getTime()) > 900000)
								throw new ServerException("El campo " + pCampo.getCampoDTO().getNombre()
										+ " permite la fecha " + SoftureUtil.formatDateTime(hora.getTime())
										+ ". Y la fecha recibida es "
										+ SoftureUtil.formatDateTime(pCampo.getValorFecha()));
						}
					} else {
						hora.set(Calendar.YEAR, 0);
						hora.set(Calendar.MONTH, 0);
						hora.set(Calendar.DAY_OF_MONTH, 0);
						hora.set(Calendar.HOUR, 0);
						hora.set(Calendar.MINUTE, 0);
						if (pCampo.getValorNumero() != null
								&& pCampo.getValorNumero().compareTo(BigDecimal.ZERO) != 0) {
							int totalSecs = pCampo.getValorNumero().intValue();
							totalSecs = totalSecs / 1000;
							hora.set(Calendar.HOUR_OF_DAY, totalSecs / 3600);
							hora.set(Calendar.MINUTE, (totalSecs % 3600) / 60);
						}
						if (hora.get(Calendar.HOUR_OF_DAY) == 0 && hora.get(Calendar.MINUTE) == 0)
							throw new ServerException("Por favor escoja la cantidad de tiempo");
						pCampo.setValorNumero(BigDecimal.ZERO);
						pCampo.setValorFecha(hora.getTime());
						pCampo.setValorText(null);
						if (hora.get(Calendar.HOUR) != 0) {
							pCampo.setValorText(hora.get(Calendar.HOUR_OF_DAY) + " Horas");
							pCampo.setValorNumero(pCampo.getValorNumero()
									.add(new BigDecimal(hora.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000)));
						}
						if (hora.get(Calendar.MINUTE) != 0) {
							if (pCampo.getValorText() == null) {
								pCampo.setValorText(hora.get(Calendar.MINUTE) + " Minutos");
							} else {
								pCampo.setValorText(
										pCampo.getValorText() + "," + hora.get(Calendar.MINUTE) + " Minutos");
							}
							pCampo.setValorNumero(
									pCampo.getValorNumero().add(new BigDecimal(hora.get(Calendar.MINUTE) * 60 * 1000)));
						}
						// Esto indica que el campo de horas vienen 00:00, validamos si es obligatorio o
						// sino le borramos fecha para que no lo guarde
						if (pCampo.getValorText() == null) {
							if (Propiedades.obtenerParametro(pCampo.getCampoDTO(),
									Propiedades.PERMISO_CAMPO_OPCIONAL) == null) {
								throw new ServerException(
										"Es obligatorio colocar el campo " + pCampo.getCampoDTO().getNombre()
												+ " del formulario " + pCampo.getCampoDTO().getPlantillaNombre());
							} else {
								pCampo.setValorFecha(null);
							}
						}

					}
				}
				if (pCampo.getModificado()) {// en algunos casos se modifican datos de documentos viejos en donde se
												// deja la misma fecha
					String maximo = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.FECHA_MAXIMA);
					if (!maximo.isEmpty()) {
						try {
							long tiempoAdicional = Long.valueOf(maximo);
							Date fechaMaxima = new Date(new Date().getTime() + tiempoAdicional);
							if (pCampo.getValorFecha().compareTo(fechaMaxima) > 0)
								throw new ServerException("La fecha " + pCampo.getCampoDTO().getNombre()
										+ " debe ser menor a " + SoftureUtil.formatDateTime(fechaMaxima));
						} catch (NumberFormatException exNumber) {
							throw new ServerException("El campo " + pCampo.getCampoDTO().getNombre()
									+ " contiene un valor que no puede convertirse en numero como fecha maxima. "
									+ exNumber.getMessage());
						}

					}
					String minimo = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.FECHA_MINIMA);
					if (!minimo.isEmpty()) {

						try {
							long tiempoAdicional = Long.valueOf(minimo);
							Calendar fechaMinimaCalendar = Calendar.getInstance();
							if (Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.FECHA_CON_HORA).isEmpty()) {
								fechaMinimaCalendar.setTime(new Date(new Date().getTime() - tiempoAdicional));
								fechaMinimaCalendar.set(Calendar.HOUR_OF_DAY, 0);
								fechaMinimaCalendar.set(Calendar.MINUTE, 0);
								fechaMinimaCalendar.set(Calendar.SECOND, 0);
								fechaMinimaCalendar.set(Calendar.MILLISECOND, 0);
							}
							if (!Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.FECHA_SIN_CALENDAR)
									.isEmpty()) {
								fechaMinimaCalendar.setTime(new Date(new Date().getTime() - tiempoAdicional));
								fechaMinimaCalendar.set(Calendar.YEAR, 0);
								fechaMinimaCalendar.set(Calendar.MONTH, 0);
								fechaMinimaCalendar.set(Calendar.DAY_OF_MONTH, 0);
								fechaMinimaCalendar.set(Calendar.SECOND, 0);
								fechaMinimaCalendar.set(Calendar.MILLISECOND, 0);
							}
							if (pCampo.getValorFecha().compareTo(fechaMinimaCalendar.getTime()) < 0)
								throw new ServerException(
										"La fecha " + pCampo.getCampoDTO().getNombre() + " debe ser mayor a "
												+ SoftureUtil.formatDateTime(fechaMinimaCalendar.getTime()));
						} catch (NumberFormatException exNumber) {
							throw new ServerException("El campo " + pCampo.getCampoDTO().getNombre()
									+ " contiene un valor que no puede convertirse en numero como fecha minima. "
									+ exNumber.getMessage());
						}

					}
				}
			}
		}
	}

	public PedidoVentaCaracteristicaDTO guardarCampo(PedidoVentaCaracteristicaDTO pCampo, String token)
			throws ServerException {
		PedidoVentaCaracteristicaDTO bd = campoService.buscarActivo(pCampo, pCampo.getPrincipal().getHistorico());
		if (bd != null) {
			if (pCampo.getValorFecha() == null) {
				bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
				bd.setPrincipal(pCampo.getPrincipal());
				campoService.inactivar(bd, token);
				return pCampo;
			} else {
				if (pCampo.getValorFecha().compareTo(bd.getValorFecha()) == 0 && (pCampo.getValorNumero() == null
						|| pCampo.getValorNumero().compareTo(bd.getValorNumero()) == 0)) {
					return pCampo;
				} else {
					bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
					bd.setPrincipal(pCampo.getPrincipal());
					campoService.inactivar(bd, token);
				}
			}
		}
		if (pCampo.getValorFecha() == null) {
			return pCampo;
		} else {
			return campoService.guardar(pCampo, token);
		}
	}
}
