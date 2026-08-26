package d3.document_execution.application.field;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.document_execution.application.CallDocumentCommons;
import d3.document_execution.application.CallDocumentListBySQLFunction;
import d3.document_execution.application.CallDocumentListFromFieldProcess;
import d3.document_execution.application.CallDocumentListWithFilters;
import d3.document_execution.application.DocumentoRelacionExpedienteSvc;
import d3.document_execution.application.PedidoVentaCaracteristicaSvc;
import d3.document_execution.application.PedidoVentaDineroSvc;
import d3.document_execution.application.PedidoVentaSvc;
import d3.document_execution.domain.DocumentoRelacionExpedienteDTO;
import d3.document_execution.domain.DocumentoRelacionExpedienteFilterDTO;
import d3.document_execution.domain.PedidoVentaCaracteristicaDTO;
import d3.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import d3.document_execution.domain.PedidoVentaDTO;
import d3.document_execution.domain.PedidoVentaDineroDTO;
import d3.document_execution.domain.PedidoVentaFilterDTO;
import d3.money.application.CuentaSvc;
import d3.money.application.MovimientoSvc;
import d3.money.application.TurnoSvc;
import d3.money.domain.CuentaDTO;
import d3.money.domain.CuentaFilterDTO;
import d3.money.domain.MovimientoDTO;
import d3.money.domain.MovimientoFilterDTO;
import d3.money.domain.TurnoDTO;
import d3.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import d3.process_form.application.DocumentoPlantillaSvc;
import d3.process_form.domain.DocumentoPlantillaDTO;
import d3.property.application.PropertyGetWithCacheService;
import d3.property.domain.PropiedadDTO;
import d3.property.domain.PropiedadValorDefinidoDTO;

@Component
public class TipoProceso {

	private final CuentaSvc cuentaService;
	private final PedidoVentaSvc pedidoService;
	private final CallDocumentListWithFilters listDocumentWithFiltersFunction;
	private final CallDocumentListFromFieldProcess listDocumentFromFieldProcessFunction;
	private final CallDocumentListBySQLFunction listDocumentBySQLFunction;
	private final PedidoVentaCaracteristicaSvc campoService;
	private final DocumentoPlantillaSvc plantillaService;
	private final DocumentoPlantillaCaracteristicaSvc caracteristicaService;
	private final DocumentoRelacionExpedienteSvc relacionExpedienteService;
	private final MovimientoSvc movimientoService;
	private final PropertyGetWithCacheService cacheService;
	private final PedidoVentaDineroSvc dineroService;
	private final TurnoSvc turnoService;
	private final AuxiliarProcesoBodega tipoBodega;
	private final CallUpdateInformativeField updateInformativeService;

	public TipoProceso(@Lazy CuentaSvc cuentaService, @Lazy PedidoVentaSvc pedidoService,
			@Lazy CallDocumentListWithFilters listDocumentWithFiltersFunction,
			@Lazy CallDocumentListFromFieldProcess listDocumentFromFieldProcessFunction,
			@Lazy CallDocumentListBySQLFunction listDocumentBySQLFunction,
			@Lazy PedidoVentaCaracteristicaSvc campoService, @Lazy DocumentoPlantillaSvc plantillaService,
			@Lazy DocumentoPlantillaCaracteristicaSvc caracteristicaService,
			@Lazy DocumentoRelacionExpedienteSvc relacionExpedienteService, @Lazy MovimientoSvc movimientoService,
			@Lazy PropertyGetWithCacheService cacheService, @Lazy PedidoVentaDineroSvc dineroService,
			@Lazy TurnoSvc turnoService, @Lazy AuxiliarProcesoBodega tipoBodega,
			@Lazy CallUpdateInformativeField updateInformativeService) {
		this.cuentaService = cuentaService;
		this.pedidoService = pedidoService;
		this.listDocumentWithFiltersFunction = listDocumentWithFiltersFunction;
		this.listDocumentFromFieldProcessFunction = listDocumentFromFieldProcessFunction;
		this.listDocumentBySQLFunction = listDocumentBySQLFunction;
		this.campoService = campoService;
		this.plantillaService = plantillaService;
		this.caracteristicaService = caracteristicaService;
		this.relacionExpedienteService = relacionExpedienteService;
		this.movimientoService = movimientoService;
		this.cacheService = cacheService;
		this.dineroService = dineroService;
		this.turnoService = turnoService;
		this.tipoBodega = tipoBodega;
		this.updateInformativeService = updateInformativeService;

	}

	public void cargarConsultaCampo(PedidoVentaCaracteristicaDTO pCampo) throws ServerException {
		if (pCampo.getValorOpcion() != null)
			pCampo.setPrincipal(pedidoService.consultaXId(pCampo.getValorOpcion()));// Consulto el Id por proceso
	}

	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token, boolean isUpdateAutomatic)
			throws ServerException {
		String campoHeredado1 = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.CAMPO_HEREDADO_1);
		if (campoHeredado1.isEmpty()) {// Los heredados trabajan solos
			// System.out.format("\n[%s - %s] Validando.....",
			// pCampo.getCampoDTO().getPlantillaNombre(), pCampo.getCampoDTO().getNombre());
			if (pCampo.getValorText() != null && pCampo.getValorText().isEmpty())
				pCampo.setValorText(null);
			String multiple = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.MULTIPLE);
			autosave(multiple, pCampo, token);
			if (!multiple.isEmpty()) {
				validarMultiple(pCampo, token);
			} else {
				// DEsde las automaticas vienen un listado pero si es unico entonces debo
				// agregarlo
				if (pCampo.getValorOpcion() == null) {
					if (pCampo.getExpedientes() != null && pCampo.getExpedientes().size() == 1) {
						pCampo.setValorOpcion(pCampo.getExpedientes().get(0).getLlaveTabla());
					} else {
						// Esto se hizo para las cargas masivas en caso que llegue un valor texto
						// intentamos consultarlo
						// especialmente se hizo para los dependientes
						// Esta cpopiado en varias partes miestras analizo como colocarlo en alguna
						// funcion
						if (pCampo.getValorText() != null) {
							PedidoVentaCaracteristicaFilterDTO filter = new PedidoVentaCaracteristicaFilterDTO();
							filter.setCampo(pCampo.getCampo());
							filter.setCampoDTO(pCampo.getCampoDTO());
							filter.setSecurityToken(token);
							filter.setDependientes(pCampo.getDependientes());
							filter.setFiltroParametro(pCampo.getValorText());
							PedidoVentaCaracteristicaFilterDTO result = listDocumentFromFieldProcessFunction
									.execute(filter, pCampo.getCampoDTO());
							if (result == null || result.getCampoDTO() == null
									|| result.getCampoDTO().getDocumentos() == null
									|| result.getCampoDTO().getDocumentos().isEmpty())
								throw new ServerException("Revisando el campo " + pCampo.getCampoDTO().getNombre()
										+ " No se encuentra el documento con codigo : " + pCampo.getValorText()
										+ ". Revisa permisos y el documento");
							if (result.getCampoDTO().getDocumentos().size() > 1) {
								for (PedidoVentaDTO iDocument : result.getCampoDTO().getDocumentos()) {
									if (iDocument.getNombre().compareTo(pCampo.getValorText()) == 0
											|| (iDocument.getDescripcion() != null && iDocument.getDescripcion()
													.compareTo(pCampo.getValorText()) == 0)) {
										pCampo.setValorOpcion(iDocument.getLlaveTabla());
										break;
									}
								}
							} else {
								pCampo.setValorOpcion(result.getCampoDTO().getDocumentos().get(0).getLlaveTabla());
							}
							if (pCampo.getValorOpcion() == null)
								throw new ServerException("El campo " + pCampo.getCampoDTO().getNombre()
										+ " de la plantilla " + pCampo.getCampoDTO().getPlantillaNombre() + " obtiene "
										+ result.getCampoDTO().getDocumentos().size()
										+ " resultados que concuerdan con el criterio : " + pCampo.getValorText()
										+ ", tu campo de busqueda debe ser la descripcion o el nombre");

						}
					}

				}
				String _defaultValue = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.DEFAULT);
				if (_defaultValue != null && !_defaultValue.isEmpty() && pCampo.getValorOpcion() == null) {
					PedidoVentaCaracteristicaFilterDTO _filter = toFilter(pCampo, token);
					_filter.setFiltroParametro(_defaultValue);
					_filter = consultarDatosBase(_filter);
					if (_filter.getCampoDTO().getDocumentos() != null
							&& _filter.getCampoDTO().getDocumentos().size() == 1)
						pCampo.setValorOpcion(_filter.getCampoDTO().getDocumentos().get(0).getLlaveTabla());
				}
				// Valido obligatoriedad
				if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL) == null
						&& pCampo.getValorOpcion() == null) {
					// En caso que no tome el default
					if (pCampo.getValorOpcion() == null) {
						if (isUpdateAutomatic) {
							CallDocumentCommons.addMessageError(pCampo.getPrincipal(),
									"Es necesario registrar el campo " + pCampo.getCampoDTO().getNombre()
											+ " de la plantilla " + pCampo.getCampoDTO().getPlantillaNombre());
						} else {
							throw new ServerException(
									"Es necesario registrar el campo " + pCampo.getCampoDTO().getNombre()
											+ " de la plantilla " + pCampo.getCampoDTO().getPlantillaNombre());
						}
					}
				}
				// Valido que el documento este activo y actualizo algunos valores
				if (pCampo.getValorOpcion() != null) {
					loadActualOptionToDocumentList(pCampo);

					if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.CUENTA_MOVIMIENTO) != null) {
						CuentaFilterDTO cajaFilter = new CuentaFilterDTO();
						cajaFilter.setDocumento(pCampo.getValorOpcion());
						CuentaDTO caja = cuentaService.consultaUnica(cajaFilter);
						if (caja == null) {
							PedidoVentaDTO cuentaDocumento = pedidoService.consultaXId(pCampo.getValorOpcion());
							PropiedadDTO propiedadCuenta = cacheService.obtenerPropiedad(
									PropiedadValorDefinidoDTO.PLANTILLA, cuentaDocumento.getPlantilla(),
									Propiedades.PLANTILLA_TIPO_CUENTA, null);
							if (propiedadCuenta == null) {
								DocumentoPlantillaDTO plantillaError = plantillaService
										.consultaXId(cuentaDocumento.getPlantilla());
								throw new ServerException("El documento " + cuentaDocumento.getNombre()
										+ " es de la plantilla " + plantillaError.getNombre()
										+ " y esta plantilla no tiene propiedad configurada la propiedad cuenta que le permite manejar un seguimiento a los movimientos");
							} else {
								caja = cuentaService.crearCuenta(cuentaDocumento, token);
								pCampo.setValorAuxiliar(caja.getLlaveTabla());
							}
						} else {
							if (caja.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
								throw new ServerException("La caja no esta activa");
							pCampo.setValorAuxiliar(caja.getLlaveTabla());
						}
					}
				}
			}
		} else {
			System.out.format("\n[%s - %s]  Campo heredado = %s, No se valida",
					pCampo.getCampoDTO().getPlantillaNombre(), pCampo.getCampoDTO().getNombre(), campoHeredado1);
		}
	}

	private void autosave(String multiple, PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		// Solo para los auload save
		if (pCampo.getValorOpcion() == null) {
			if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.AUTOLOAD_SAVE) != null) {

				if (!multiple.isEmpty()) {
					PropiedadDTO funcionConsulta = Propiedades.obtenerParametro(pCampo.getCampoDTO(),
							Propiedades.PROCESO_FUNCION_SQL);
					if (funcionConsulta == null)
						throw new ServerException("Se debe definir la funcion para obtener los datos del autosave");
					pCampo.setExpedientes(listDocumentBySQLFunction.execute(pCampo.getCampoDTO(), pCampo.getCampoDTO(),
							pCampo.getDependientes(), null, funcionConsulta, null, token));
					if (pCampo.getModificado()
							&& Propiedades.obtenerParametro(pCampo.getCampoDTO(),
									Propiedades.PERMISO_CAMPO_OPCIONAL) == null
							&& (pCampo.getExpedientes() == null || pCampo.getExpedientes().isEmpty()))
						throw new ServerException("Es necesario registrar el campo " + pCampo.getCampoDTO().getNombre()
								+ " de la plantilla " + pCampo.getCampoDTO().getPlantillaNombre());
				} else {
					PedidoVentaCaracteristicaFilterDTO filter = toFilter(pCampo, token);
					PedidoVentaCaracteristicaFilterDTO documentosFuncion = consultarDatosBase(filter);
					if (documentosFuncion.getCampoDTO().getDocumentos() != null
							&& !documentosFuncion.getCampoDTO().getDocumentos().isEmpty()) {
						pCampo.setValorOpcion(documentosFuncion.getCampoDTO().getDocumentos().get(0).getLlaveTabla());
					} else {
						if (pCampo.getModificado() && Propiedades.obtenerParametro(pCampo.getCampoDTO(),
								Propiedades.PERMISO_CAMPO_OPCIONAL) == null)
							throw new ServerException(
									"Es necesario registrar el campo " + pCampo.getCampoDTO().getNombre()
											+ " de la plantilla " + pCampo.getCampoDTO().getPlantillaNombre());
					}
				}

			}
		}
	}

	private PedidoVentaCaracteristicaFilterDTO toFilter(PedidoVentaCaracteristicaDTO pCampo, String token) {
		PedidoVentaCaracteristicaFilterDTO filter = new PedidoVentaCaracteristicaFilterDTO();
		filter.setCampo(pCampo.getCampo());
		filter.setCampoDTO(pCampo.getCampoDTO());
		filter.setDependientes(pCampo.getDependientes());
		filter.setDocumento(pCampo.getDocumento());
		filter.setEstado(pCampo.getEstado());
		filter.setExpedientes(pCampo.getExpedientes());
		filter.setLlaveTabla(pCampo.getLlaveTabla());
		filter.setSecurityToken(token);
		filter.setValorAuxiliar(pCampo.getValorAuxiliar());
		filter.setValorOpcion(pCampo.getValorOpcion());
		// filter.setValorText(pCampo.getValorText());
		return filter;
	}

	private void loadActualOptionToDocumentList(PedidoVentaCaracteristicaDTO pCampo) throws ServerException {
		if (pCampo.getValorOpcion() == null)
			return;
		PedidoVentaDTO vActual = pedidoService.consultaXIdConDinero(pCampo.getValorOpcion());
		if (vActual == null)
			throw new ServerException("El documento no existe");
		// if(vActual.getEstado()!=null &&
		// vActual.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO)==0) throw
		// new ServerException("El documento no se encuentra activo");
		pCampo.setValorText((vActual.getDescripcion() == null) ? vActual.getNombre() : vActual.getDescripcion());
		if (vActual.getDinero() != null) {
			String campoValor = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.PROCESO_VALOR);
			if (!campoValor.isEmpty() && campoValor.compareTo("1") == 0) {
				pCampo.setValorNumero(vActual.getDinero().getValorTotal());
			} else {
				pCampo.setValorNumero(vActual.getDinero().getSaldo());
			}
		}
		vActual.setEstado(null);// no activo para que lo procese gestionar
		pCampo.setExpedientes(new ArrayList<PedidoVentaDTO>());
		pCampo.getExpedientes().add(vActual);
	}

	private void validarMultiple(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		if (pCampo.getExpedientes() == null)
			pCampo.setExpedientes(new ArrayList<PedidoVentaDTO>());
		// Valido obligatoriedad
		if (pCampo.getModificado()
				&& Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL) == null
				&& pCampo.getExpedientes().isEmpty()
				&& Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.AUTOLOAD_SAVE) == null)
			throw new ServerException("Es necesario registrar el campo " + pCampo.getCampoDTO().getNombre()
					+ " de la plantilla " + pCampo.getCampoDTO().getPlantillaNombre());

		List<PedidoVentaDTO> procesosActuales = null;
		// Consulto los procesos que estan en BD
		if (pCampo.getDocumento() != null)
			procesosActuales = listDocumentWithFiltersFunction.listarExpedientesPertenecenCampo(pCampo.getLlaveTabla(),
					token, null);
		if (procesosActuales == null)
			procesosActuales = new ArrayList<PedidoVentaDTO>();
		// En caso que sea modificacion comparo que proceso estan retirandose, cambiando
		if (pCampo.getModificado()) {
			// Retiro de los actuales los que volvieron a enviar
			for (PedidoVentaDTO procesoDTO : pCampo.getExpedientes()) {
				// Para las cargas masivas como no tengo los dependientes lo hago ya en la
				// generacion del documento
				if (procesoDTO.getLlaveTabla() == null && procesoDTO.getNombre() != null) {
					PedidoVentaFilterDTO filterMultiple = new PedidoVentaFilterDTO();
					filterMultiple.setCampoOrigen(pCampo.getCampo());
					filterMultiple.setSecurityToken(token);
					filterMultiple.setFiltroParametro(procesoDTO.getNombre());
					if (pCampo.getDependientes() != null && !pCampo.getDependientes().isEmpty()) {
						filterMultiple.setLlaveTabla(pCampo.getDependientes().get(0).getValorOpcion());
						filterMultiple.setCaracteristicas(pCampo.getDependientes());
					}
					List<PedidoVentaDTO> resultListDocuments = listDocumentWithFiltersFunction
							.listarAvanzado(filterMultiple);
					if (resultListDocuments == null || resultListDocuments.isEmpty())
						throw new ServerException("Revisando el campo " + pCampo.getCampoDTO().getNombre()
								+ " No se encuentra el documento con codigo : " + procesoDTO.getNombre());
					if (resultListDocuments.size() > 1) {
						for (PedidoVentaDTO iDocument : resultListDocuments) {
							if (iDocument.getNombre().compareTo(pCampo.getValorText()) == 0) {
								pCampo.setValorOpcion(iDocument.getLlaveTabla());
								procesoDTO.setLlaveTabla(iDocument.getLlaveTabla());
								procesoDTO.setEstadoExpediente(iDocument.getEstadoExpediente());
								procesoDTO.setPlantilla(iDocument.getPlantilla());

								break;
							}
						}
					} else {
						pCampo.setValorOpcion(resultListDocuments.get(0).getLlaveTabla());
						procesoDTO.setLlaveTabla(resultListDocuments.get(0).getLlaveTabla());
						procesoDTO.setEstadoExpediente(resultListDocuments.get(0).getEstadoExpediente());
						procesoDTO.setPlantilla(resultListDocuments.get(0).getPlantilla());
					}
					if (pCampo.getValorOpcion() == null)
						throw new ServerException("El campo " + pCampo.getCampoDTO().getNombre() + " obtiene "
								+ resultListDocuments.size() + " resultados que concuerdan con el criterio : "
								+ procesoDTO.getNombre());
				}
				procesoDTO.setEstado(null);
				for (PedidoVentaDTO procesoActivo : procesosActuales) {
					if (procesoActivo.getLlaveTabla().compareTo(procesoDTO.getLlaveTabla()) == 0) {
						procesosActuales.remove(procesoActivo);
						procesoDTO.setEstado(SharedConstants.STATE_ACTIVE);
						break;
					}
				}
			}
			// Coloco estado de inactivo a los que no enviaron para borrarlos
			for (PedidoVentaDTO procesoInactivar : procesosActuales) {
				procesoInactivar.setEstado(SharedConstants.STATE_INACTIVE);
				pCampo.getExpedientes().add(procesoInactivar);
			}
		} else {
			pCampo.setExpedientes(procesosActuales);
		}

		pCampo.setValorOpcion(null);
		String campoValor = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.PROCESO_VALOR);
		if (!campoValor.isEmpty()) {
			// Quite esto pero para que lo tenia antes
			// Por aqui no calculo con el dinero sino con el campo
			// Esto es para validar que los valores sean los que estan en la base de datos.
			// Problema de concurrencia
			// Alguien cambia un valor de un expediente de un multiple y el valor no va a
			// ser el mimsmo en los reportes
			for (PedidoVentaDTO expediente : pCampo.getExpedientes()) {
				if (expediente.getEstado() == null
						|| expediente.getEstado().compareTo(SharedConstants.STATE_INACTIVE) != 0) {
					PedidoVentaDineroDTO valorActual = dineroService.consultaPorDocumento(expediente.getLlaveTabla(),
							expediente.getHistorico(), expediente.getNombre());
					if (valorActual == null) {
						if (expediente.getDinero() != null)
							throw new ServerException(
									"Revise porque el expediente tiene valor y en la base de datos no tiene. \nExpediente: "
											+ expediente.getNombre());
					} else {
						if (expediente.getDinero() == null)
							throw new ServerException(
									"Revise porque el expediente NO tiene valor y en la base de datos SI tiene. \nExpediente: "
											+ expediente.getNombre() + "\nValor actual:" + valorActual.getValorTotal());
						if (valorActual.getValorTotal().compareTo(expediente.getDinero().getValorTotal()) != 0)
							throw new ServerException("Revise porque los valores son diferentes. \nExpediente: "
									+ expediente.getNombre() + "\nValor actual:" + valorActual.getValorTotal()
									+ "\nValor enviado:" + expediente.getDinero().getValorTotal()
									+ ".\nRecomendacion actualice el documento posiblemente fue modificado.");
						if (valorActual.getSaldo().compareTo(expediente.getDinero().getSaldo()) != 0)
							throw new ServerException(
									"Revise porque los valores de los SALDOS son diferentes. \nExpediente: "
											+ expediente.getNombre() + "\nSaldo actual:" + valorActual.getSaldo()
											+ "\nValor enviado:" + expediente.getDinero().getSaldo()
											+ ".\nRecomendacion actualice el documento posiblemente fue modificado.");
					}
				}
			}
		}
		PedidoVentaCaracteristicaFilterDTO calculado = CallDocumentCommons
				.calcularValoresTotalesCampo(toFilter(pCampo, token), campoValor, relacionExpedienteService);
		pCampo.setValorText(calculado.getValorText());
		pCampo.setValorNumero(calculado.getValorNumeroMax());
	}

	public PedidoVentaCaracteristicaDTO guardarCampo(PedidoVentaCaracteristicaDTO pCampo, String token)
			throws ServerException {
		String campoHeredado1 = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.CAMPO_HEREDADO_1);
		boolean modificacion = false;
		if (campoHeredado1.isEmpty()) {
			PedidoVentaCaracteristicaDTO bd = campoService.buscarActivo(pCampo, pCampo.getPrincipal().getHistorico());
			String multiple = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.MULTIPLE);
			if (multiple.isEmpty()) {
				if (bd != null) {
					pCampo.setDifference(new PedidoVentaCaracteristicaDTO());
					pCampo.getDifference().setValorOpcion(bd.getValorOpcion());
					pCampo.getDifference().setValorText(bd.getValorText());
					bd.setCampoDTO(pCampo.getCampoDTO());
					if (pCampo.getValorOpcion() == null) {
						bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
						if (bd.getLlaveTabla() != null) {
							bd.setPrincipal(pCampo.getPrincipal());
							campoService.inactivar(bd, token);
						}
						updateInformativeService.call(pCampo, token);
						return inactivar(bd, null, token);// Se inactiva el anterior, toca revisar el inactivar
					} else {
						if (bd.getValorOpcion() != null
								&& pCampo.getValorOpcion().compareTo(bd.getValorOpcion()) == 0) {
							if (Propiedades.obtenerParametro(pCampo.getCampoDTO(),
									Propiedades.BODEGA_MOVIMIENTO) != null)
								tipoBodega.aplicarMovimientosBodega(pCampo, token);
							return pCampo;
						} else {
							bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
							if (bd.getLlaveTabla() != null) {
								bd.setPrincipal(pCampo.getPrincipal());
								campoService.inactivar(bd, token);
							}
							inactivar(bd, null, token);// comentario anterior
							modificacion = true;
						}
					}
				}
				if (pCampo.getValorOpcion() == null) {
					cerrarCaja(pCampo, token);
					updateInformativeService.call(pCampo, token);
					return pCampo;
				} else {
					// System.out.format("\n\n[%s (%s) - %s] START Guardando en bd %s ( %s )",
					// pCampo.getCampoDTO().getPlantillaNombre(), pCampo.getPrincipal().getNombre(),
					// pCampo.getCampoDTO().getNombre(), pCampo.getValorText(),
					// pCampo.getValorOpcion());
					bd = campoService.guardar(pCampo, token);
					pCampo.setLlaveTabla(bd.getLlaveTabla());

					// administrarExpedientes(pCampo, pCampo.getPrincipal(), modificacion, token);
					addDocumentToBPM(pCampo, pCampo.getPrincipal(), modificacion);
					if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.CUENTA_ABRIR_CAJA) != null) {
						TurnoDTO turno = new TurnoDTO();
						turno.setCuenta(pCampo.getValorOpcion());
						turno.setUsuario(campoService.getUserFlex(token));
						turno.setDocumento(pCampo.getDocumento());
						turno.setFechaApertura(pCampo.getPrincipal().getFecha());
						turno = turnoService.iniciarTurno(turno, token);
					}
					// relacionExternaDocumentos(pCampo, token);
					cerrarCaja(pCampo, token);
					generarPagos(pCampo, token);
					if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.BODEGA_MOVIMIENTO) != null)
						tipoBodega.aplicarMovimientosBodega(pCampo, token);
					// System.out.format("\n[%s (%s) - %s] END.. Guardando en bd %s ( %s )",
					// pCampo.getCampoDTO().getPlantillaNombre(), pCampo.getPrincipal().getNombre(),
					// pCampo.getCampoDTO().getNombre(), pCampo.getValorText(),
					// pCampo.getValorOpcion());
					// throw new ServerException("Probando");
					updateInformativeService.call(pCampo, token);
				}
			} else {
				System.out.format("\n[%s (%s) - %s] Campo Multiple] = %s", pCampo.getCampoDTO().getPlantillaNombre(),
						pCampo.getPrincipal().getNombre(), pCampo.getCampoDTO().getNombre(), multiple);
				if (bd == null) {
					bd = campoService.guardar(pCampo, token);
					pCampo.setLlaveTabla(bd.getLlaveTabla());
				} else {
					bd.setValorNumero(pCampo.getValorNumero());
					bd.setValorText(pCampo.getValorText());
					bd = campoService.actualizar(bd, token);
					modificacion = true;
				}
				relacionarExpedientes(pCampo, token);
				// administrarExpedientes(pCampo, pCampo.getPrincipal(), modificacion, token);
				addDocumentToBPM(pCampo, pCampo.getPrincipal(), modificacion);
				// relacionExternaDocumentos(pCampo, token);
			}
		}
		return pCampo;
	}

	private void addDocumentToBPM(PedidoVentaCaracteristicaDTO pCampo, PedidoVentaDTO principal, boolean modificacion) {
		if (pCampo.getCampoDTO().getPropiedades() == null || pCampo.getCampoDTO().getPropiedades().isEmpty())
			return;
		if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PROCESO_GESTIONAR_ESTADOS) == null
				&& Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.MODIFICAR_CAMPO) == null
				&& Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PROCESO_DIVISION) == null
				&& Propiedades.obtenerParametro(pCampo.getCampoDTO(),
						Propiedades.PROCESO_INCLUIR_TRAZA_PRINCIPAL) == null
				&& Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.VINCULO_MAKE_IN_OTHER_FORM) == null)
			return;
		pCampo.setDocumentsToBPM(principal);
		pCampo.setModificadoBPM(modificacion);
	}

	public PedidoVentaCaracteristicaDTO inactivar(PedidoVentaCaracteristicaDTO pCampo,
			PedidoVentaDTO documentoModificadorDTO, String token) throws ServerException {
		if (pCampo.getCampoDTO().getPropiedades() == null || pCampo.getCampoDTO().getPropiedades().isEmpty())
			pCampo.setCampoDTO(caracteristicaService.cargarComplementos(pCampo.getCampoDTO(), token));
		// if(pCampo.getLlaveTabla()!=null) campoService.inactivar(pCampo);
		// anularMovimiento(pCampo); //OJO esto qe como estoy haciendo para anular
		// movimeintos
		System.out.format("\n%s Inactivando", pCampo.getCampoDTO().getNombre());
		if (pCampo.getExpedientes() != null && pCampo.getExpedientes().size() != 0) {
			for (PedidoVentaDTO procesoInactivar : pCampo.getExpedientes()) {
				procesoInactivar.setEstado(SharedConstants.STATE_INACTIVE);
			}
			relacionarExpedientes(pCampo, token);
		} else {
			if (documentoModificadorDTO != null)
				loadActualOptionToDocumentList(pCampo);
		}
		addDocumentToBPM(pCampo, documentoModificadorDTO, true);
		// return administrarExpedientes(pCampo, documentoModificadorDTO, true, token);
		return pCampo;
	}

	private void relacionarExpedientes(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		if (pCampo.getExpedientes() == null || pCampo.getExpedientes().isEmpty())
			return;
		for (PedidoVentaDTO procesoDTO : pCampo.getExpedientes()) {
			if (procesoDTO.getEstado() != null
					&& procesoDTO.getEstado().compareTo(SharedConstants.STATE_INACTIVE) == 0) {
				retirarExpedienteDocumento(pCampo, procesoDTO,
						(pCampo.getPrincipal() == null) ? null : pCampo.getPrincipal().getLlaveTabla(), token);
			} else {
				BigDecimal _processValue = null;
				if (procesoDTO.getDinero() != null) {
					if (procesoDTO.getDinero().getValorCampo() != null
							&& procesoDTO.getDinero().getValorCampo().compareTo(BigDecimal.ZERO) != 0) {
						_processValue = procesoDTO.getDinero().getValorCampo();
					} else {
						_processValue = procesoDTO.getDinero().getSaldo();
					}
				}
				relacionExpedienteService.relacionarExpedienteDocumento(pCampo.getLlaveTabla(),
						procesoDTO.getLlaveTabla(), token, pCampo.getCampoDTO().getNombre(), _processValue,
						pCampo.getPrincipal().getLlaveTabla());
			}
		}
	}

	private boolean retirarExpedienteDocumento(PedidoVentaCaracteristicaDTO pCampo, PedidoVentaDTO procesoDTO,
			String pDocumentMainRetire, String token) throws ServerException {
		// Si es inactivo, busco la relacion del expediente y el campo
		DocumentoRelacionExpedienteFilterDTO filtroExpFilter = new DocumentoRelacionExpedienteFilterDTO();
		filtroExpFilter.setCampoMaestro(pCampo.getLlaveTabla());
		filtroExpFilter.setExpedienteDetalle(procesoDTO.getLlaveTabla());
		filtroExpFilter.setEstado(SharedConstants.STATE_ACTIVE);
		DocumentoRelacionExpedienteDTO filtroExp = relacionExpedienteService.consultaUnica(filtroExpFilter);
		if (filtroExp != null) {
			filtroExp.setDocumentoInactivo(pDocumentMainRetire);
			relacionExpedienteService.inactivar(filtroExp, token);
			return true;
		}
		return false;
	}

	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(PedidoVentaCaracteristicaFilterDTO pCampo)
			throws ServerException {
		return listDocumentFromFieldProcessFunction.execute(pCampo,
				caracteristicaService.consultaUnicaConComplementos(pCampo.getCampo(), pCampo.getSecurityToken()));
	}

	private void cerrarCaja(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		String catalogoCierre = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.CUENTA_CERRAR_CAJA);
		if (!catalogoCierre.isEmpty()) {
			TurnoDTO turno = new TurnoDTO();
			// Asi estaba antes para lograr que solo cerrar el mismo usuario, ahora esto se
			// hace por funcion
			// turno.setUsuario(campoService.getUserFlex(token));
			// turno.setDocumento(pCampo.getValorOpcion());
			// if (pCampo.getDependientes() == null || pCampo.getDependientes().isEmpty()
			// || pCampo.getDependientes().get(0) == null)
			// throw new ServerException(
			// "PAra el cierre de caja se debe teenr un dependiente que es el documento que
			// realizo la apertura");
			turno.setDocumento(pCampo.getValorOpcion());
			turno = turnoService.consultarTurnoActual(turno);
			if (turno == null)
				throw new ServerException("No se identifica el turno en ejecucion");
			CuentaDTO caja = cuentaService.consultaXId(turno.getCuenta());
			BigDecimal saldo = caja.getSaldo();
			if (saldo == null)
				saldo = BigDecimal.ZERO;
			if (pCampo.getPrincipal() != null & pCampo.getPrincipal().getDinero() != null
					&& pCampo.getPrincipal().getDinero().getValorTotal().compareTo(BigDecimal.ZERO) != 0) {
				if (pCampo.getValorOpcion() == null)
					throw new ServerException("Para este cierre con valor es neesario colocar la caja de destino");
				MovimientoDTO movimiento = new MovimientoDTO();
				movimiento.setFechaEvento(pCampo.getPrincipal().getFecha());
				movimiento.setTipo(MovimientoDTO.SALIDA_GASTO);
				movimiento.setCuenta(caja.getLlaveTabla());
				movimiento.setMonto(pCampo.getPrincipal().getDinero().getValorTotal());
				// movimiento.setCuentaPermisoUsuario(turno.getCuentaPermiso());
				movimiento.setDocumento(pCampo.getDocumento());
				movimiento.setTurno(turno.getLlaveTabla());
				movimiento = movimientoService.guardar(movimiento, token);
				saldo = saldo.add(movimiento.getMontoAplicado());
			}
			// if(permiso.getEstado().compareTo(TurnoDTO.ESTADO_ACTIVO)!=0) throw new
			// ServerException("No tiene permisos sobre esa cuenta");
			/*
			 * if(caja.getValidarTurno()){ if(caja.getCierreMaximo().compareTo(saldo)<0)
			 * throw new
			 * ServerException("Tiene como restriccion cerrar la caja con un valor maximo de "
			 * + caja.getCierreMaximo()); }
			 */
			turno.setFechaEntrega(new Date());
			turno.setEstado(TurnoDTO.ESTADO_FINALIZADO);
			turno.setMontoFinal(saldo);
			turno = turnoService.actualizar(turno, token);
			caja.setFechaConciliacion(turno.getFechaEntrega());
			caja = cuentaService.actualizar(caja, token);
		}
	}

	private void generarPagos(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		String catalogoMovimiento = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.CUENTA_MOVIMIENTO);
		if (!catalogoMovimiento.isEmpty()) {
			PedidoVentaDTO documento = pCampo.getPrincipal();
			if (documento.getDinero() == null)
				throw new ServerException("El campo " + pCampo.getCampoDTO().getNombre() + " de la plantilla "
						+ pCampo.getCampoDTO().getPlantillaNombre()
						+ " reporta Los formularios con campos tipo cuenta deben tener el valor");
			/*
			 * if(documento.getDinero()==null){
			 * if(plantillaService.consultaXId(documento.getPlantilla()).getTipo().compareTo
			 * (DocumentoPlantillaDTO.REPORTE)==0){ return; }else{ throw new
			 * ServerException("Los formularios con campos tipo cuenta deben tener el valor"
			 * ); } }
			 */
			if (documento.getDinero().getValorTotal().compareTo(BigDecimal.ZERO) != 0) {
				MovimientoDTO movimiento = new MovimientoDTO();
				movimiento.setFechaEvento(documento.getFecha());
				movimiento.setTipo(catalogoMovimiento);
				movimiento.setMonto(documento.getDinero().getValorTotal());
				movimiento.setCuenta(pCampo.getValorAuxiliar());
				movimiento.setDocumento(pCampo.getDocumento());
				movimiento = movimientoService.guardar(movimiento, token);
				// pedidoService.actualizarSaldo(pCampo.getDocumento(), movimiento.getMonto(),
				// pCampo.getSecurityToken());
				pCampo.setValorAuxiliar(movimiento.getLlaveTabla());
				pCampo.setValorFecha(movimiento.getFechaEvento());
				pCampo.setValorNumero(movimiento.getMonto());
			}
		}
		String anularMovimiento = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.CUENTA_ANULAR_MOVIMIENTO);
		if (anularMovimiento.isEmpty())
			return;
		MovimientoFilterDTO movimiento = new MovimientoFilterDTO();
		movimiento.setDocumento(pCampo.getValorOpcion());
		movimiento.setEstado(SharedConstants.STATE_ACTIVE);
		List<MovimientoDTO> movimientos = movimientoService.listarConsulta(movimiento);
		if (movimientos == null || movimientos.isEmpty())
			throw new ServerException("Estas anulando un movimiento y no se encuentra en la tabla de movimientos");
		for (MovimientoDTO movimientoDTO : movimientos) {
			movimientoService.inactivar(movimientoDTO, token);
		}

	}

}
