package com.softure.document_execution.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.DocumentoRelacionExpedienteDTO;
import com.softure.document_execution.domain.DocumentoRelacionExpedienteFilterDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_transition.application.CallDocumentUpdateFromAutomatic;
import com.softure.document_transition.application.CallManageTransition;
import com.softure.document_transition.application.DocumentoRelacionGestorSvc;
import com.softure.document_transition.domain.DocumentoRelacionGestorDTO;
import com.softure.document_transition.domain.DocumentoRelacionGestorFilterDTO;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.process_designer.application.ProcesoEstadoSvc;
import com.softure.process_designer.application.ProcesoTransicionSvc;
import com.softure.process_designer.domain.ProcesoEstadoDTO;
import com.softure.process_designer.domain.ProcesoTransicionDTO;
import com.softure.process_designer.domain.ProcesoTransicionFilterDTO;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

@Component
public class CallBPM {

	@Autowired
	private CallDocumentCRUD saveUpdateInactivateDocumentFunction;
	@Autowired
	private DocumentoRelacionGestorSvc relacionGestorService;
	@Autowired
	private ProcesoEstadoSvc estadoService;
	@Autowired
	private ProcesoTransicionSvc expedienteTransicionService;
	@Autowired
	private CallManageTransition manageTransitionFunction;
	@Autowired
	private CallDocumentUpdateFromAutomatic updateDocumentFunction;
	@Autowired
	private DocumentoRelacionExpedienteSvc relacionExpedienteService;
	@Autowired
	private PedidoVentaCaracteristicaSvc campoService;
	@Autowired
	private PedidoVentaSvc pedidoService;
	@Autowired
	private DocumentoPlantillaSvc plantillaService;
	@Autowired
	private PropiedadSvc propiedadService;

	public void execute(PedidoVentaDTO document, String token) throws ServerException {
		if (document == null || document.getCaracteristicas() == null || document.getCaracteristicas().isEmpty())
			return;
		for (PedidoVentaCaracteristicaDTO iField : document.getCaracteristicas()) {
			if (iField.getDocumentsToBPM() != null) {
				administrarExpedientes(iField, iField.getDocumentsToBPM(), iField.isModificadoBPM(), token);
			}
		}
	}

	private boolean modificarDocumentoPrincipal(PedidoVentaCaracteristicaDTO pCampo, PedidoVentaDTO procesoDTO,
			String token) throws ServerException {
		// Modificar campos de plantilla principal
		List<PropiedadDTO> modificarCampo = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(),
				Propiedades.MODIFICAR_CAMPO);
		if (modificarCampo == null || modificarCampo.isEmpty())
			return false;
		System.out.format("\n%s (Modificando documento principal..... %s)", pCampo.getCampoDTO().getNombre(),
				procesoDTO.getNombre());
		campoService.validarDependientes(pCampo.getCampoDTO(), pCampo.getDependientes());
		updateDocumentFunction.executeFromBPM(pCampo, procesoDTO, token, modificarCampo);
		return true;
	}

	private List<String> getCaminos(PedidoVentaCaracteristicaDTO pCampo) {
		List<String> caminosGestionar = new ArrayList<String>();
		List<PropiedadDTO> caminos = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(),
				Propiedades.PROCESO_GESTIONAR_ESTADOS);
		if (caminos != null) {
			for (PropiedadDTO iCamino : caminos) {
				if (iCamino.getValor().compareTo("*") == 0) {
					caminosGestionar.add("*");
					System.out.format(" Camino (*)");
				} else {
					caminosGestionar.add(iCamino.getValor() + ";");
					System.out.format(", Camino (%s)", iCamino.getValor());
				}
			}
		}
		return caminosGestionar;
	}

	private void gestionarExpedienteDependientes(PedidoVentaDTO procesoDTO, PedidoVentaDTO documento,
			String securityToken, BigDecimal saldoDocumento, List<String> plantillasRevisadas,
			List<String> caminosGestionables, List<String> documentosGestionados, String transaccion,
			boolean primerLlamado) throws ServerException {
		if (caminosGestionables == null || caminosGestionables.isEmpty())
			return;
		if (caminosGestionables.size() == 1 && caminosGestionables.get(0).isEmpty())
			return;
		if (procesoDTO == null)
			return;
		List<String> caminosValidados = validarCamino(caminosGestionables, procesoDTO.getPlantilla());
		if (caminosValidados.size() == 0)
			return;
		PedidoVentaDTO expediente = pedidoService.consultaXId(procesoDTO.getLlaveTabla());
		if (expediente == null)
			throw new ServerException("No se identifico el expediente");

		System.out.format("\n[%s] Gestionando por accion en documento: %s", expediente.getNombre(),
				documento.getNombre());
		if (procesoDTO.getEstadoExpediente() != null) {
			ProcesoTransicionDTO transicion = consultarTransicion(documento.getPlantilla(),
					procesoDTO.getEstadoExpediente(), null);
			if (expediente.getEstadoExpediente() == null)
				throw new ServerException("Revise el estado del expediente que no es NULO : " + expediente.getNombre());
			if (expediente.getEstadoExpediente().compareTo(procesoDTO.getEstadoExpediente()) != 0)
				throw new ServerException(
						"Revise el expediente " + procesoDTO.getNombre() + " el cual tiene un estado desactualizado");
			// Manejo de los saldos de los procesos
			if (transicion != null) {
				manageTransitionFunction.execute(transicion, expediente.getLlaveTabla(), documento, saldoDocumento,
						null, null, securityToken, transaccion, null);
				if (documentosGestionados == null)
					documentosGestionados = new ArrayList<String>();// Para evitar que se generen ciclos validando los
																	// mismos documentos
				documentosGestionados.add(expediente.getLlaveTabla());
				saveUpdateInactivateDocumentFunction.saveRole(expediente, securityToken);
			} else {
				if (primerLlamado) {
					ProcesoEstadoDTO pState = estadoService.consultaXId(procesoDTO.getEstadoExpediente());
					DocumentoPlantillaDTO plantilla = plantillaService.consultaXId(documento.getPlantilla());
					String mensajeFault = "Revisa porque las plantillas " + plantilla.getNombre() + " ( Codigo = "
							+ plantilla.getCodigo() + " ) no generan ninguna transicion en el proceso "
							+ pState.getProcesoNombre();
					mensajeFault = mensajeFault + " desde el estado " + pState.getNombre() + " (Codigo = "
							+ pState.getCodigo() + ")";
					mensajeFault = mensajeFault + ", el campo lo solicita. ( Documento = " + procesoDTO.getNombre()
							+ " )";
					if (procesoDTO.getDescripcion() != null)
						mensajeFault = mensajeFault + procesoDTO.getDescripcion();

					throw new ServerException(mensajeFault);
				}
			}
		}
		plantillasRevisadas.add(procesoDTO.getPlantilla());
		List<PedidoVentaCaracteristicaDTO> gestionables = campoService.listarGestionables(expediente.getLlaveTabla());
		if (gestionables != null && !gestionables.isEmpty()) {
			System.out.format("\n[%s] Gestionando documentos que esten relacionados", expediente.getNombre(),
					documento.getNombre());
			for (PedidoVentaCaracteristicaDTO campo : gestionables) {
				System.out.format("\n[] Relacion %s ( %s )", campo.getCampo(), campo.getValorText());
				List<DocumentoRelacionExpedienteDTO> expedientesAnidados = null;
				DocumentoRelacionExpedienteDTO relacionExpediente = new DocumentoRelacionExpedienteDTO();
				if (campo.getValorOpcion() == null) {// En caso que sean multiples
					if (campo.getLlaveTabla().compareTo(Propiedades.CAMPO_HEREDADO_1) == 0) {
						// Consulto los campos que se relacionan y gestiono el estado de esos procesos
						expedientesAnidados = relacionExpedienteService.listarHeredados(campo.getValorAuxiliar(),
								campo.getValorText(), procesoDTO.getLlaveTabla(), documento.getPlantilla(),
								plantillasRevisadas);
					} else {
						// Esto creo que se podria optimizar algun día y solo ahce un llamado por todos
						DocumentoRelacionExpedienteFilterDTO relacionExpedienteFilter = new DocumentoRelacionExpedienteFilterDTO();
						relacionExpedienteFilter.setCampoMaestro(campo.getLlaveTabla());
						relacionExpedienteFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
						relacionExpedienteFilter.setPaginacionRegistroFinal(5000);// Esto es para poder listar todos los
																					// relacionados
						expedientesAnidados = relacionExpedienteService.listarConsulta(relacionExpedienteFilter);
					}
				} else {
					expedientesAnidados = new ArrayList<DocumentoRelacionExpedienteDTO>();
					relacionExpediente.setExpedienteDetalle(campo.getValorOpcion());
					relacionExpediente.setValor(saldoDocumento);
					expedientesAnidados.add(relacionExpediente);
				}
				if (expedientesAnidados != null && !expedientesAnidados.isEmpty()) {// Aqui cambie el calculo de los
																					// saldos y no se como cuadralos
					if (documentosGestionados == null)
						documentosGestionados = new ArrayList<String>();// Para evitar que se generen ciclos validando
																		// los mismos documentos
					boolean validadoPreviamente = false;
					String expedienteId = null;
					for (DocumentoRelacionExpedienteDTO iExpediente : expedientesAnidados) {
						expedienteId = iExpediente.getExpedienteDetalle();
						for (String iValidado : documentosGestionados) {// Para evitar que se generen ciclos validando
																		// los mismos documentos
							if (iValidado.compareTo(expedienteId) == 0) {
								validadoPreviamente = true;
								break;
							}
						}
						if (!validadoPreviamente) {
							PedidoVentaDTO expAnidado = pedidoService.consultaXId(expedienteId);
							System.out.format("\n[%s] INICIA Procesar documento anidado ( %s )", expediente.getNombre(),
									expAnidado.getNombre());
							documentosGestionados.add(expedienteId);
							gestionarExpedienteDependientes(expAnidado, documento, securityToken,
									iExpediente.getValor(), plantillasRevisadas, caminosValidados,
									documentosGestionados, transaccion, false);
							System.out.format("\n[%s] FIN Procesar documento anidado ( %s )", expediente.getNombre(),
									expAnidado.getNombre());
						}
					}
				}
			}
		}
	}

	private void relacionarGestor(PedidoVentaDTO anterior, PedidoVentaDTO nuevo, String motivo, String securityToken)
			throws ServerException {
		anterior = pedidoService.consultaXId(anterior.getLlaveTabla());
		if (motivo == null) {
			DocumentoPlantillaDTO plantillaNueva = plantillaService.consultaXId(nuevo.getPlantilla());
			motivo = plantillaNueva.getNombre();
		}
		System.out.format("\n(Colocar traza a documento...... %s)", anterior.getNombre());
		// Creo la relacion del documento Gestor
		relacionGestorService.trazar(anterior.getLlaveTabla(), nuevo.getLlaveTabla(), motivo,
				anterior.getEstadoExpediente(), anterior.getEstadoExpediente(), null, null, securityToken, null,
				anterior.getHistorico(), nuevo.getTransaccion());
	}

	private void revertirExpedienteDependiente(PedidoVentaDTO procesoDTO, PedidoVentaDTO documento,
			String securityToken, List<String> caminosGestionables, boolean primerLlamado) throws ServerException {
		// Consulto la relacion que genero el cambio de estado
		if (procesoDTO == null || procesoDTO.getEstadoExpediente() == null)
			return;
		List<String> caminosValidados = validarCamino(caminosGestionables, procesoDTO.getPlantilla());
		if (caminosValidados.size() == 0)
			return;
		DocumentoRelacionGestorFilterDTO filtroGestor = new DocumentoRelacionGestorFilterDTO();
		filtroGestor.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		filtroGestor.setEstadoFinal(procesoDTO.getEstadoExpediente());
		filtroGestor.setDocumentoModificador(documento.getLlaveTabla());
		filtroGestor.setDocumentoPrincipal(procesoDTO.getLlaveTabla());
		List<DocumentoRelacionGestorDTO> gestores = relacionGestorService.listarConsulta(filtroGestor);

		if (gestores == null || gestores.isEmpty()) {
			if (primerLlamado) {
				StringBuilder error = new StringBuilder("El documento ");
				error.append(procesoDTO.getNombre());
				error.append(" no tiene elementos gestores con el estado actual ");
				error.append(procesoDTO.getEstadoNombre());
				error.append(" que permitan revertir el proceso.");
				throw new ServerException(error.toString());
			}
		} else {
			DocumentoRelacionGestorDTO ultimoGestor = gestores.get(0); // El query trae desc, escojo el primero para que
																		// es el ultimo
			ProcesoTransicionDTO transicion = consultarTransicion(documento.getPlantilla(),
					ultimoGestor.getEstadoInicial(), procesoDTO.getEstadoExpediente());
			if (transicion == null)
				return;// throw new ServerException("Existen documentos sin transicion para gestionar."
						// + procesoDTO.getNombre());
			// Realizo validaciones de documento con estado
			PedidoVentaDTO expediente = pedidoService.consultaXId(procesoDTO.getLlaveTabla());
			if (expediente == null)
				throw new ServerException("No se identifico el expediente");
			if (expediente.getEstadoExpediente() == null)
				throw new ServerException("Revise el estado del expediente que no es NULO : " + expediente.getNombre());
			if (transicion.getEstadoLLegada().compareTo(procesoDTO.getEstadoExpediente()) != 0)
				throw new ServerException("Revise e estado del proceso que no es acorde a la transcision");
			if (expediente.getEstadoExpediente().compareTo(procesoDTO.getEstadoExpediente()) != 0)
				throw new ServerException(
						"Revise el expediente " + procesoDTO.getNombre() + " el cual tiene un estado desactualizado");
			manageTransitionFunction.gestionarTransicionReversa(transicion, expediente.getLlaveTabla(), documento,
					securityToken);
			saveUpdateInactivateDocumentFunction.saveRole(expediente, securityToken);
			List<PedidoVentaCaracteristicaDTO> gestionables = campoService
					.listarGestionables(expediente.getLlaveTabla());
			for (PedidoVentaCaracteristicaDTO campo : gestionables) {
				List<DocumentoRelacionExpedienteDTO> expedientesAnidados;
				if (campo.getValorOpcion() == null) {// En caso que sean multiples
					DocumentoRelacionExpedienteFilterDTO relacionExpediente = new DocumentoRelacionExpedienteFilterDTO();
					relacionExpediente.setCampoMaestro(campo.getLlaveTabla());
					relacionExpediente.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
					expedientesAnidados = relacionExpedienteService.listarConsulta(relacionExpediente);
				} else {
					expedientesAnidados = new ArrayList<DocumentoRelacionExpedienteDTO>();
					DocumentoRelacionExpedienteDTO relacionExpediente = new DocumentoRelacionExpedienteDTO();
					relacionExpediente.setExpedienteDetalle(campo.getValorOpcion());
					expedientesAnidados.add(relacionExpediente);
				}
				if (expedientesAnidados != null && !expedientesAnidados.isEmpty()) {
					// BigDecimal saldoDoc = saldoDocumento;
					for (DocumentoRelacionExpedienteDTO iExpediente : expedientesAnidados) {
						PedidoVentaDTO expAnidado = pedidoService.consultaXId(iExpediente.getExpedienteDetalle());
						revertirExpedienteDependiente(expAnidado, documento, securityToken, caminosValidados, false);
					}
					// if(saldoDoc!=null && (saldoDoc.compareTo(documento.getDinero().getSaldo())!=0
					// && saldoDoc.compareTo(BigDecimal.ZERO)<0))
					// throw new ServerException("Revise el proceso porque el saldo no puede ser
					// negativo");//. (" + expediente.getNombre() + ")" +
					// SoftureUtil.formatMoney(dinero.getValorTotal()) + " + (" +
					// documento.getNombre() + ") " + SoftureUtil.formatMoney(saldoDocumento) + " =
					// " + SoftureUtil.formatMoney(nuevo.getSaldo()));
				}
			}
		}

		return;
	}

	private List<String> validarCamino(List<String> caminosGestionables, String plantilla) throws ServerException {
		List<String> caminosValidados = new ArrayList<String>();
		String codigoDocumento = plantillaService.consultaXId(plantilla).getCodigo();
		for (String camino : caminosGestionables) {
			if (camino.compareTo("*") == 0) {
				caminosValidados.add(camino);
			} else {
				if ((camino + ";").startsWith(codigoDocumento + ";")) {
					caminosValidados.add(camino.replaceFirst(codigoDocumento + ";", ""));
				}
			}
		}
		return caminosValidados;
	}

	private ProcesoTransicionDTO consultarTransicion(String plantilla, String estadoPartida, String estadoLlegada)
			throws ServerException {
		// Consulto la transicion del documento
		ProcesoTransicionFilterDTO transicion = new ProcesoTransicionFilterDTO();
		transicion.setPlantilla(plantilla);
		transicion.setEstadoPartida(estadoPartida);
		transicion.setEstadoLLegada(estadoLlegada);
		transicion.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		List<ProcesoTransicionDTO> transiciones = expedienteTransicionService.listarConsulta(transicion);
		if (transiciones.size() == 0)
			return null;// throw new ServerException("Existen documentos sin transicion para gestionar."
						// + procesoDTO.getNombre());
		if (transiciones.size() > 1) {
			String message = "Existen muchas transiciones que cumplen con las condiciones del expediente.\n";
			message = message.concat("Plantilla : " + transiciones.get(0).getPlantillaNombre() + "\n");
			if (estadoPartida == null) {
				message = message.concat("Estado Partida : NULL");
			} else {
				message = message.concat("Estado Partida : " + transiciones.get(0).getEstadoPartidaNombre());
			}
			message = message.concat((estadoLlegada == null) ? "Estado Llegada : NULL"
					: "Estado Llegada : " + transiciones.get(0).getEstadoLlegadaNombre());
			throw new ServerException(message);
		}
		return transiciones.get(0);
	}

	private PedidoVentaCaracteristicaDTO administrarExpedientes(PedidoVentaCaracteristicaDTO pCampo,
			PedidoVentaDTO updaterDTO, boolean modificacion, String token) throws ServerException {
		if (pCampo.getExpedientes() != null && !pCampo.getExpedientes().isEmpty()) {
			List<PedidoVentaDTO> activos = new ArrayList<PedidoVentaDTO>();
			HashMap<String, String> hmap = new HashMap<String, String>();
			String maquinaEstados;
			for (PedidoVentaDTO procesoDTO : pCampo.getExpedientes()) {
				System.out.format("\n[%s (%s) - %s] INICIO Procesar expediente %s ( %s )",
						pCampo.getCampoDTO().getPlantillaNombre(), pCampo.getPrincipal().getNombre(),
						pCampo.getCampoDTO().getNombre(), procesoDTO.getNombre(), procesoDTO.getLlaveTabla());
				if (!hmap.containsKey(procesoDTO.getPlantilla())) {
					hmap.put(procesoDTO.getPlantilla(),
							expedienteTransicionService.consultarProceso(procesoDTO.getPlantilla()));
				}
				maquinaEstados = hmap.get(procesoDTO.getPlantilla());
				if (procesoDTO.getEstado() == null) {
					// Esto lo tuve que hacer en logimax para un cilo que se generaba de
					modificacion = modificarDocumentoPrincipal(pCampo, procesoDTO, token);
					if (maquinaEstados != null) {
						if (Propiedades.obtenerParametro(pCampo.getCampoDTO(),
								Propiedades.PROCESO_GESTIONAR_ESTADOS) != null) {
							System.out.format("\n[%s (%s) - %s] Maquina de estados BPM ( %s ) plantilla  ( %s )",
									pCampo.getCampoDTO().getPlantillaNombre(), pCampo.getPrincipal().getNombre(),
									pCampo.getCampoDTO().getNombre(), procesoDTO.getNombre(), maquinaEstados);
							System.out.format("\n[%s (%s) - %s] Calculando caminos BPM",
									pCampo.getCampoDTO().getPlantillaNombre(), pCampo.getPrincipal().getNombre(),
									pCampo.getCampoDTO().getNombre());
							List<String> caminosGestionar = getCaminos(pCampo);
							List<String> documentosGestionados = new ArrayList<String>();
							documentosGestionados.add(pCampo.getDocumento());
							BigDecimal saldoDoc = null;
							// Me sucedio el probelma de validar lso saldos de un documento cuando son
							// multiples
							if (pCampo.getExpedientes().size() > 1) {
								if (procesoDTO.getDinero() != null)
									saldoDoc = procesoDTO.getDinero().getSaldo();
							} else {
								if (updaterDTO.getDinero() != null)
									saldoDoc = updaterDTO.getDinero().getValorTotal();
							}

							gestionarExpedienteDependientes(procesoDTO, updaterDTO, token, saldoDoc,
									new ArrayList<String>(), caminosGestionar, documentosGestionados,
									pCampo.getTransaccionRegistro(), !modificacion);
						} else {
							// Esto algun día lo voy a unir con el modificar
							if (Propiedades.obtenerParametro(pCampo.getCampoDTO(),
									Propiedades.PROCESO_DIVISION) != null) {
								System.out.format("\n[%s (%s) - %s] Dividir documento...... %s",
										pCampo.getCampoDTO().getPlantillaNombre(), pCampo.getPrincipal().getNombre(),
										pCampo.getCampoDTO().getNombre(), procesoDTO.getNombre());
								dividirDocumento(procesoDTO, updaterDTO, token, pCampo.getTransaccionRegistro());
								// Lo coloco aqui porque se relacionaba todo
								relacionarGestor(procesoDTO, updaterDTO, "Dividir documento", token);
							}
						}
					} else {
						if (Propiedades.obtenerParametro(pCampo.getCampoDTO(),
								Propiedades.PROCESO_GESTIONAR_ESTADOS) != null) {
							String usuarioToken = (token == null) ? null : propiedadService.getUserFlex(token);
							PropiedadDTO prop = propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.PLANTILLA,
									procesoDTO.getPlantilla(), Propiedades.PLANTILLA_ANULAR, usuarioToken);
							if (prop != null && updaterDTO.getPlantilla().compareTo(prop.getValor()) == 0) {
								procesoDTO.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
								saveUpdateInactivateDocumentFunction.inactivateDocumentWithProcess(procesoDTO,
										updaterDTO, token);
								relacionarGestor(procesoDTO, updaterDTO, "ANULAR DOCUMENTO", token);
							}
						}
					}
					if (Propiedades.obtenerParametro(pCampo.getCampoDTO(),
							Propiedades.PROCESO_GESTIONAR_ESTADOS) == null) {
						if (Propiedades.obtenerParametro(pCampo.getCampoDTO(),
								Propiedades.PROCESO_INCLUIR_TRAZA_PRINCIPAL) != null) {
							System.out.format("\n[%s (%s) - %s] Incluir traza..... %s",
									pCampo.getCampoDTO().getPlantillaNombre(), pCampo.getPrincipal().getNombre(),
									pCampo.getCampoDTO().getNombre(), procesoDTO.getNombre());
							relacionarGestor(procesoDTO, updaterDTO, null, token);
						}
					}

					activos.add(procesoDTO);
				} else {
					if (procesoDTO.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO) == 0) {
						// Si tenia permisos, inactivo esos permisos
						if (maquinaEstados != null) {
							// BigDecimal saldoDoc = null;
							// if(updaterDTO.getDinero()!=null) saldoDoc =
							// updaterDTO.getDinero().getSaldo();
							List<String> caminosGestionar = getCaminos(pCampo);
							revertirExpedienteDependiente(procesoDTO, updaterDTO, token, caminosGestionar, true);
						}
					} else {
						activos.add(procesoDTO);
					}
				}
				System.out.format("\n[%s (%s) - %s] FIN... Procesar expediente %s ( %s )",
						pCampo.getCampoDTO().getPlantillaNombre(), pCampo.getPrincipal().getNombre(),
						pCampo.getCampoDTO().getNombre(), procesoDTO.getNombre(), procesoDTO.getLlaveTabla());
			}
		}
		return pCampo;
	}

	public void dividirDocumento(PedidoVentaDTO anterior, PedidoVentaDTO nuevo, String securityToken,
			String transaccion) throws ServerException {
		// Se encarga de incluir el documento en los padres
		DocumentoRelacionExpedienteFilterDTO dre = new DocumentoRelacionExpedienteFilterDTO();
		dre.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		dre.setExpedienteDetalle(anterior.getLlaveTabla());
		List<DocumentoRelacionExpedienteDTO> cargues = relacionExpedienteService.listarConsulta(dre);
		if (cargues != null && !cargues.isEmpty()) {
			for (DocumentoRelacionExpedienteDTO relacion : cargues) {
				DocumentoRelacionExpedienteDTO relacionCargueNuevo = new DocumentoRelacionExpedienteDTO();
				relacionCargueNuevo.setCampoMaestro(relacion.getCampoMaestro());
				relacionCargueNuevo.setExpedienteDetalle(nuevo.getLlaveTabla());
				relacionCargueNuevo.setTransaccionRegistro(transaccion);
				String valorTomar = campoService
						.valueFieldProcessMultipleToPartialDivideDocument(relacion.getCampoMaestro());
				if (valorTomar != null) {
					if (valorTomar.compareTo("2") == 0) {
						if (nuevo.getDinero() != null)
							relacionCargueNuevo.setValor(nuevo.getDinero().getSaldo());
						if (nuevo.getDinero() != null) {
							relacion.setValor(anterior.getDinero().getSaldo());
							relacionExpedienteService.update(relacion);
						}
					} else {// Aqui falta que lo tome de la caracteristica
						if (nuevo.getDinero() != null)
							relacionCargueNuevo.setValor(nuevo.getDinero().getValorTotal());
						if (nuevo.getDinero() != null) {
							relacion.setValor(anterior.getDinero().getValorTotal());
							relacionExpedienteService.update(relacion);
						}
					}
				}
				relacionExpedienteService.guardar(relacionCargueNuevo, securityToken);
			}
		}
	}

}
