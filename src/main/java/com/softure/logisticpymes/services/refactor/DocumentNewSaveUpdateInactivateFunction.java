package com.softure.logisticpymes.services.refactor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.dto.ConsecutivoDTO;
import com.softure.logisticpymes.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.dto.DocumentoPlantillaDTO;
import com.softure.logisticpymes.dto.DocumentoTransaccionDTO;
import com.softure.logisticpymes.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.dto.PedidoVentaDTO;
import com.softure.logisticpymes.dto.PedidoVentaDineroDTO;
import com.softure.logisticpymes.dto.PlantillaConsecutivoDTO;
import com.softure.logisticpymes.dto.ProcesoTransicionDTO;
import com.softure.logisticpymes.dto.PropiedadDTO;
import com.softure.logisticpymes.dto.PropiedadValorDefinidoDTO;
import com.softure.logisticpymes.dto.RolAccesoDTO;
import com.softure.logisticpymes.dto.UsuarioDTO;
import com.softure.logisticpymes.dto.UsuarioRolDTO;
import com.softure.logisticpymes.dto.filter.DocumentoPlantillaFilterDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaFilterDTO;
import com.softure.logisticpymes.dto.filter.PlantillaConsecutivoFilterDTO;
import com.softure.logisticpymes.dto.filter.RolAccesoFilterDTO;
import com.softure.logisticpymes.dto.filter.UsuarioFilterDTO;
import com.softure.logisticpymes.dto.filter.UsuarioRolFilterDTO;
import com.softure.logisticpymes.services.BodegaSvc;
import com.softure.logisticpymes.services.ConsecutivoSvc;
import com.softure.logisticpymes.services.DocumentoPlantillaCaracteristicaSvc;
import com.softure.logisticpymes.services.DocumentoPlantillaSvc;
import com.softure.logisticpymes.services.DocumentoRelacionGestorSvc;
import com.softure.logisticpymes.services.DocumentoTransaccionSvc;
import com.softure.logisticpymes.services.MensajeSvc;
import com.softure.logisticpymes.services.PedidoVentaCaracteristicaSvc;
import com.softure.logisticpymes.services.PedidoVentaDineroSvc;
import com.softure.logisticpymes.services.PedidoVentaSvc;
import com.softure.logisticpymes.services.PlantillaConsecutivoSvc;
import com.softure.logisticpymes.services.ProcesoEstadoSvc;
import com.softure.logisticpymes.services.ProcesoTransicionSvc;
import com.softure.logisticpymes.services.ProductoSvc;
import com.softure.logisticpymes.services.PropiedadSvc;
import com.softure.logisticpymes.services.RolAccesoSvc;
import com.softure.logisticpymes.services.TransaccionErrorSvc;
import com.softure.logisticpymes.services.TransaccionLogSvc;
import com.softure.logisticpymes.services.UsuarioRolSvc;
import com.softure.logisticpymes.services.UsuarioSvc;
import com.softure.logisticpymes.services.adapter.CampoAdaptador;
import com.softure.logisticpymes.services.adapter.Propiedades;

@Component
public class DocumentNewSaveUpdateInactivateFunction {

	@Autowired
	private CampoAdaptador adaptador;
	@Autowired
	private PedidoVentaSvc pedidoService;

	@Autowired
	private ProcesoEstadoSvc estadoService;
	@Autowired
	private DocumentoTransaccionSvc transaccionSvc;
	@Autowired
	private TransaccionLogSvc logSvc;
	@Autowired
	private TransaccionErrorSvc errorSvc;
	@Autowired
	private DocumentoRelacionGestorSvc relacionGestorService;
	@Autowired
	private MensajeSvc mensajeSvc;
	@Autowired
	private ManageTransitionFunction manageTransitionFunction;
	@Autowired
	private ExecuteAPIFunction apiService;
	@Autowired
	private ConsecutivoSvc consecutivoService;
	@Autowired
	private PlantillaConsecutivoSvc plantillaConsecutivoSvc;
	@Autowired
	private ProcesoTransicionSvc transicionService;
	@Autowired
	private BodegaSvc bodegaService;
	@Autowired
	private ProductoSvc productoService;
	@Autowired
	private DocumentoPlantillaSvc documentoPlantillaService;
	@Autowired
	private DocumentoPlantillaCaracteristicaSvc documentoPlantillaCaracteristicaService;
	@Autowired
	private PropiedadSvc propiedadService;
	@Autowired
	private UsuarioSvc usuarioService;
	@Autowired
	private UsuarioRolSvc usuarioRolService;
	@Autowired
	private RolAccesoSvc rolService;
	@Autowired
	private PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService;
	@Autowired
	private PedidoVentaDineroSvc dineroService;

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaDTO save(PedidoVentaDTO dto, String token) throws ServerException {
		DocumentoTransaccionDTO tran = transaccionSvc.crear(token);
		dto.setTransaccion(tran.getLlaveTabla());
		try {
			PedidoVentaDTO result = saveAfterIdentifyTransaction(dto, token);
			logSvc.finalizar(tran.getFecha(), dto.getTransaccion());
			return result;
		} catch (Exception e) {
			errorSvc.finalizar(tran.getFecha(), e.getMessage(), tran.getUsuario());
			throw new ServerException(e.getMessage());
		} 
	}

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaDTO inactivateDocumentWithProcess(PedidoVentaDTO documentDTO, PedidoVentaDTO updaterDTO,
			String token) throws ServerException {
		// BEGIN PedidoVenta_inactivar
		PedidoVentaDTO bd = pedidoService.consultaXId(documentDTO.getLlaveTabla());
		if (bd.getEstadoExpediente() != null)
			throw new ServerException("Para inactivar el expediente se debe usar un documento de transicion de estado");
		documentDTO = pedidoService.obtenerCamposCompletos(documentDTO, token);
		String transaccion = documentDTO.getTransaccion();
		if (transaccion == null)
			transaccion = transaccionSvc.crear(token).getLlaveTabla();
		for (PedidoVentaCaracteristicaDTO iterador : documentDTO.getCaracteristicas()) {
			if (iterador.getCampoDTO() == null)
				iterador.setCampoDTO(documentoPlantillaCaracteristicaService.consultaXId(iterador.getCampo()));
			iterador.setTransaccionInactivo(transaccion);
			adaptador.inactivar(iterador, updaterDTO, token);
		} // El inactivar va e intenta gestionar los productos y proceso ()

		documentDTO = pedidoService.inactivate(documentDTO);
		manageTemplateTypes(documentDTO, null, token);
		/*
		 * if(Propiedades.obtenerParametro(dto.getPlantilla(),
		 * Propiedades.PLANTILLA_TIPO_CUENTA) !=null) {
		 * cuentaService.inactivarDocumento(dto); }
		 */
		return documentDTO;
		// END PedidoVenta_inactivar
	}

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaDTO update(PedidoVentaDTO dto, String modificadorId, String token) throws ServerException {
		DocumentoPlantillaFilterDTO plantillaFilter = new DocumentoPlantillaFilterDTO();
		PedidoVentaDTO bd = pedidoService.consultaXId(dto.getLlaveTabla());
		dto.setHistorico(bd.getHistorico()); // para evitatr errores en el calculo de valores
		dto.setPlantilla(bd.getPlantilla());
		// if(dto.getTransaccion()!=null &&
		// dto.getTransaccion().compareTo(bd.getTransaccion())==0)
		// dto.setTransaccion(null);
		plantillaFilter.setLlaveTabla(dto.getPlantilla());
		plantillaFilter.setSecurityToken(token);
		DocumentoPlantillaDTO plantilla = documentoPlantillaService.obtenerConfiguracionSinCampos(plantillaFilter,
				rolService.usuarioPermisosCompletos(token));
		plantilla = documentoPlantillaService.obtenerCampos(plantilla, token);
		if (Propiedades.obtenerValor(plantilla, Propiedades.PERMISO_PLANTILLA_MODIFICAR).isEmpty())
			throw new ServerException("El usuario no tiene permisos para modificar un " + plantilla.getNombre());
		// PedidoVentaDTO pdv = consultaXId(dto.getLlaveTabla());
		if (bd.getEstadoExpediente() != null) {
			if (dto.getEstadoExpediente() == null)
				throw new ServerException("El documento a actualizar debe traer el estado del expediente");
			if (bd.getEstadoExpediente().compareTo(dto.getEstadoExpediente()) != 0)
				throw new ServerException("Revise porque el documento tiene un estado diferente.\nDocumento: "
						+ bd.getNombre() + "\nEstado actual: " + bd.getEstadoNombre());
		}
		// Me aparecio un hz desde el historico, porque no tienen principal y nose como
		// pasarlo pero tengo mis dudas con el guardar
		for (PedidoVentaCaracteristicaDTO iterador : dto.getCaracteristicas()) {
			iterador.setPrincipal(bd);
		}
		validateFields(dto, plantilla, token);
		if (dto.getNombre() == null) {
			dto.setNombre(bd.getNombre());// Cuando envio modificar lo envio vacio
			dto.setConsecutivo(bd.getConsecutivo());
		}
		String campoDescripcion = Propiedades.obtenerValor(plantilla, Propiedades.DESCRIPCION);// Descripcion para los
																								// roles
		if (!campoDescripcion.isEmpty()) {
			for (PedidoVentaCaracteristicaDTO iterador : dto.getCaracteristicas()) {
				if (campoDescripcion.compareTo(iterador.getCampo()) == 0) {
					dto.setDescripcion(iterador.getValorText());
					break;
				}
			}
		}
		validateConsecutiveNumber(dto, plantilla, token);
		dto.setFecha(bd.getFecha()); // Copio la fecha para que no me la modifiquen desde el cliente sin un campo
		validateDates(dto, Propiedades.obtenerValor(plantilla, Propiedades.FECHA));
		validateBalance(dto, plantilla);
		String transaccion = dto.getTransaccion();
		boolean crearTraza = false;// Cuando un formulario modifica otro no debo crear traza ya que esta la del
									// proceso
		if (transaccion == null || bd.getTransaccion().compareTo(transaccion) == 0) {// Si son diferetnes vienen de otro
																						// proceso
			transaccion = transaccionSvc.crear(token).getLlaveTabla();
			crearTraza = true;
		}
		if (dto.getEstado() == null) {
			if (dto.getEstadoExpediente() == null) {
				dto.setEstado(ConstantesGenerales.ESTADO_ACTIVO);// Viene de tipo proceso que lo coloca nulo
			} else {
				dto.setEstado(estadoService.consultaXId(dto.getEstadoExpediente()).getEstadoDocumento());
			}
		}
		dto.setFechaRegistro(bd.getFechaRegistro());// Siempre tiene que mantenerse la fecha de registro
		dto.setTransaccion(bd.getTransaccion());// Siempre tiene que mantenerse la transaccion de registro
		dto.setFuncionario(bd.getFuncionario());// Siempre tiene que mantenerse la funcionario de registro
		dto.setHistorico(bd.getHistorico());
		bd = pedidoService.update(dto);
		saveBalance(dto, token);
		for (PedidoVentaCaracteristicaDTO iterador : dto.getCaracteristicas()) {
			iterador.setTransaccionRegistro(transaccion);// Le quite el igual a null asumo que va a modificar los nuevos
			iterador.setPrincipal(bd);
		}
		dto.setCaracteristicas(saveInternalFields(dto, token));
		manageTemplateTypes(dto, plantilla, token);
		// Para los tipo cuenta al actualizar no estoy mirando los sobregiros
		if (crearTraza)
			relacionGestorService.trazar(dto.getLlaveTabla(), null, plantilla.getNombre(), dto.getEstadoExpediente(),
					dto.getEstadoExpediente(), (dto.getDinero() == null) ? null : dto.getDinero().getLlaveTabla(), null,
					token, null, dto.getHistorico(), transaccion);
		propiedadService.validarFuncionConsultandoPropiedad(plantilla, dto.getLlaveTabla(), modificadorId,
				dto.getFuncionario(), token);
		dto.setCaracteristicas(null);// Por error al serializar
		return dto;
	}

	private PedidoVentaDTO saveAfterIdentifyTransaction(PedidoVentaDTO dto, String token) throws ServerException {
		if (dto.getLlaveTabla() != null)
			throw new ServerException("Envio un pedido a guardar con llave existente");
		dto.setFuncionario(getUserID(token));

		DocumentoPlantillaFilterDTO plantillaFilter = new DocumentoPlantillaFilterDTO();
		plantillaFilter.setLlaveTabla(dto.getPlantilla());
		plantillaFilter.setSecurityToken(token);
		DocumentoPlantillaDTO plantilla = documentoPlantillaService.obtenerConfiguracionSinCampos(plantillaFilter,
				rolService.usuarioPermisosCompletos(token));
		plantilla = documentoPlantillaService.obtenerCampos(plantilla, token);
		if (Propiedades.obtenerValor(plantilla, Propiedades.PERMISO_PLANTILLA_CREAR).isEmpty())
			throw new ServerException("El usuario no tiene permisos para crear un " + plantilla.getNombre());

		validateFields(dto, plantilla, token);
		validateConsecutiveNumber(dto, plantilla, token);
		validateDates(dto, Propiedades.obtenerValor(plantilla, Propiedades.FECHA));
		validateBalance(dto, plantilla);
		if (dto.getTransaccion() == null)
			dto.setTransaccion(transaccionSvc.crear(token).getLlaveTabla());
		
		dto.setFechaRegistro(new Date());
		dto.setHistorico(null);
		PedidoVentaDTO pedido = pedidoService.save(dto);
		dto.setLlaveTabla(pedido.getLlaveTabla());
		saveBalance(dto, token);
		pedido.setDinero(dto.getDinero());
		String campoDescripcion = Propiedades.obtenerValor(plantilla, Propiedades.DESCRIPCION);
		for (PedidoVentaCaracteristicaDTO iterador : dto.getCaracteristicas()) {
			// Principal se usa en inventarios, debe ir lleno
			iterador.setPrincipal(dto);
			iterador.setDocumento(pedido.getLlaveTabla());
			iterador.setTransaccionRegistro(pedido.getTransaccion());
			// Descripcion para los roles
			if (!campoDescripcion.isEmpty() && campoDescripcion.compareTo(iterador.getCampo()) == 0) {
				dto.setDescripcion(iterador.getValorText());
				pedido.setDescripcion(iterador.getValorText());
			}
		}
		pedido.setCaracteristicas(saveInternalFields(dto, token));
		if (dto.getDinero() != null && pedido.getDinero() == null)
			pedido.setDinero(dto.getDinero());// Error al generar documentos en la iteracion que se borra
		manageState(pedido, plantilla.getNombre(), token, dto.getTransaccion());
		manageTemplateTypes(dto, plantilla, token);
		propiedadService.validarFuncionConsultandoPropiedad(plantilla, dto.getLlaveTabla(), null, dto.getFuncionario(),
				token);
		String api = Propiedades.obtenerValor(plantilla, Propiedades.API);
		if (!api.isEmpty())
			apiService.prepareApiToExecution(api, dto, null, token);
		dto.setCaracteristicas(null);// Por error al serializar
		return pedido;
	}

	private void manageState(PedidoVentaDTO pedido, String plantillaNombre, String token, String transaccion)
			throws ServerException {
		ProcesoTransicionDTO inicial = transicionService.consultarTransaccionInicial(pedido.getPlantilla());
		if (inicial != null) {
			manageTransitionFunction.execute(inicial, pedido.getLlaveTabla(), pedido,
					(pedido.getDinero() == null) ? null : pedido.getDinero().getValorTotal(), pedido.getDinero(), null,
					token, transaccion);
		} else {// Cuando son transacciones que no inician un proceso (aqui traza del documento
				// en tipo proceso traza al proceso)
				// cundo son solo documetnos sin transciones se envian mensajes
			mensajeSvc.gestionarMensajes(pedido, null, usuarioService.consultaXId(pedido.getFuncionario()), pedido,
					token);
			// Pase aqui la traza ya que debo integrar
			relacionGestorService.trazar(pedido.getLlaveTabla(), null, plantillaNombre, null,
					pedido.getEstadoExpediente(),
					(pedido.getDinero() == null) ? null : pedido.getDinero().getLlaveTabla(), null, token, null,
					pedido.getHistorico(), transaccion);
		}
		// return inicial;
	}

	private void validateBalance(PedidoVentaDTO pedido, DocumentoPlantillaDTO plantilla) throws ServerException {
		String total = Propiedades.obtenerValor(plantilla, Propiedades.TOTAL);
		if (!total.isEmpty()) {
			PedidoVentaCaracteristicaDTO campoValor = DocumentCommonsFunction.obtenerValor(pedido.getCaracteristicas(),
					total);
			if (campoValor == null)
				throw new ServerException("Se debe colocar la caracteristica de valor TOTAL");
			PedidoVentaDineroDTO dineroCalculado = new PedidoVentaDineroDTO();
			dineroCalculado.setValorTotal(campoValor.getValorNumero());
			dineroCalculado.setSaldo(BigDecimal.ZERO);
			if (pedido.getLlaveTabla() != null && pedido.getEstadoExpediente() != null) {// Si es modificar debo
																							// actualizar el saldo//solo
																							// tienen saldo los que son
																							// de proceso
				PedidoVentaDineroDTO anterior = dineroService.consultaPorDocumento(pedido.getLlaveTabla(),
						pedido.getHistorico());
				if (anterior != null)
					dineroCalculado.setSaldo(anterior.getSaldo());
				if (campoValor.getModificado()) {
					ProcesoTransicionDTO inicial = transicionService.consultarTransaccionInicial(pedido.getPlantilla());
					if (inicial != null && inicial.getAfectaSaldo() != null) {
						if (anterior != null) {
							BigDecimal diferencia = campoValor.getValorNumero().subtract(anterior.getValorTotal());
							dineroCalculado.setSaldo(dineroCalculado.getSaldo().add(diferencia));
						} else {
							dineroCalculado.setSaldo(campoValor.getValorNumero());
						}
					}
				}
			}
			pedido.setDinero(dineroCalculado);
		} else {
			pedido.setDinero(null);
		}
	}

	private void validateFields(PedidoVentaDTO dto, DocumentoPlantillaDTO plantilla, String token)
			throws ServerException {
		if (plantilla != null && plantilla.getCaracteristicas() != null && !plantilla.getCaracteristicas().isEmpty()) {
			String filtroTexto = "";
			if (dto.getCaracteristicas() == null)
				throw new ServerException("Es necesesario registrar informacion adicional.");
			List<PedidoVentaCaracteristicaDTO> ordenadas = new ArrayList<PedidoVentaCaracteristicaDTO>();// En casos
																											// como
																											// generacion
																											// automatica
																											// vienen en
																											// desorden
			for (DocumentoPlantillaCaracteristicaDTO campoPlantilla : plantilla.getCaracteristicas()) {
				boolean campoEncontrado = false;
				// 1 Coloco los campos DTO
				for (PedidoVentaCaracteristicaDTO campoDocumento : dto.getCaracteristicas()) {
					if (campoDocumento.getCampo().compareTo(campoPlantilla.getLlaveTabla()) == 0) {
						campoDocumento.setCampoDTO(campoPlantilla);
						campoDocumento.setCampo(campoPlantilla.getLlaveTabla());
						campoDocumento.setDocumento(dto.getLlaveTabla());
						if (campoDocumento.getDocumento() == null)
							campoDocumento.setModificado(true);
						campoDocumento.setDependientes(null);
						ordenadas.add(campoDocumento);
						campoEncontrado = true;
						break;
					}
				}
				if (!campoEncontrado
						&& campoPlantilla.getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.SECCION) != 0)
					throw new ServerException("Revisa porque el campo " + campoPlantilla.getNombre()
							+ " no viene registrado en el documento " + plantilla.getNombre());
			}
			dto.setCaracteristicas(ordenadas);
			if (dto.getLlaveTabla() != null) {// Valido para actualizar que el campo si se pueda modiifcar
				if (!plantilla.getCaracteristicas().isEmpty()) {
					boolean iContadorModificadas = false;
					for (PedidoVentaCaracteristicaDTO iCampoDocumento : dto.getCaracteristicas()) {
						if (iCampoDocumento.getModificado()) {
							if (Propiedades.obtenerParametro(iCampoDocumento.getCampoDTO(),
									Propiedades.PERMISO_CAMPO_MODIFICABLE) == null && Propiedades.obtenerParametro(iCampoDocumento.getCampoDTO(),
											Propiedades.PERMISO_CAMPO_BLOQUEAR) == null) {
								String mensajeError = "El campo " + iCampoDocumento.getCampoDTO().getNombre();
								mensajeError = mensajeError + " de la plantilla "
										+ iCampoDocumento.getCampoDTO().getPlantillaNombre()
										+ " se envia a modificar pero el usuario ";
								mensajeError = mensajeError + usuarioService.consultaXId(getUserID(token)).getNombre()
										+ " no tiene permisos de modificar ese campo";
								throw new ServerException(mensajeError);
							}
							iContadorModificadas = true;
						}
					}
					if (!iContadorModificadas)
						throw new ServerException("Se envia a modificar un documento sin cambios");
				}
			}
			// 2. Coloco los dependientes//Actualizar dependencias despues de los camps para
			// que queden completas asi el campo este despues en orden
			for (PedidoVentaCaracteristicaDTO campoDocumento : dto.getCaracteristicas()) {
				List<PropiedadDTO> codigoDepende = Propiedades.obtenerVariosParametro(campoDocumento.getCampoDTO(),
						Propiedades.DEPENDE);
				List<PropiedadDTO> modificarCampo = Propiedades.obtenerVariosParametro(campoDocumento.getCampoDTO(),
						Propiedades.MODIFICAR_CAMPO);
				if (codigoDepende != null || modificarCampo != null) {
					List<PropiedadDTO> dependencias = new ArrayList<PropiedadDTO>();
					if (codigoDepende != null)
						dependencias.addAll(codigoDepende);
					if (modificarCampo != null)
						dependencias.addAll(modificarCampo);
					for (PropiedadDTO codigo : dependencias) {
						for (PedidoVentaCaracteristicaDTO fieldExpediente : dto.getCaracteristicas()) {
							if (codigo.getValor().compareTo(fieldExpediente.getCampo()) == 0) {
								if (campoDocumento.getDependientes() == null)
									campoDocumento.setDependientes(new ArrayList<PedidoVentaCaracteristicaDTO>());
								if (fieldExpediente.getModificado())
									campoDocumento.setModificado(true);
								campoDocumento.getDependientes().add(fieldExpediente);
								break;
							}
						}
					}
				}
			}
			// 3. valido cada campo
			for (PedidoVentaCaracteristicaDTO campoDocumento : dto.getCaracteristicas()) {
				adaptador.validarPrepararCampo(campoDocumento, token);
				if (campoDocumento.getValorText() != null) {
					String filtro = Propiedades.obtenerValor(campoDocumento.getCampoDTO(), Propiedades.FILTRO);
					if (!filtro.isEmpty())
						filtroTexto = filtroTexto + campoDocumento.getValorText() + ",";
					PropiedadDTO unique = Propiedades.obtenerParametro(campoDocumento.getCampoDTO(),
							Propiedades.UNIQUE);
					if (unique != null) {
						String conicidencia = pedidoVentaCaracteristicaService.validarUnique(campoDocumento);
						if (conicidencia != null) {
							PedidoVentaDTO coincidenciaDTO = pedidoService.consultaXId(conicidencia);
							throw new ServerException("Ya existe un documento que contiene en el campo "
									+ campoDocumento.getCampoDTO().getNombre() + " el valor "
									+ campoDocumento.getValorText() + "\n\n Documento: " + coincidenciaDTO.getNombre()
									+ "\nFecha: " + coincidenciaDTO.getFecha());
						}
					}
				}
			}
			if (filtroTexto.compareTo("") != 0) {
				dto.setTextoFiltro(SoftureUtil.formatSimpleFunction(filtroTexto).toUpperCase());
			} else {
				dto.setTextoFiltro(null);
			}
		}
	}

	private void validateConsecutiveNumber(PedidoVentaDTO pedido, DocumentoPlantillaDTO plantilla, String token)
			throws ServerException {
		String codigoNuevo = null;
		List<PropiedadDTO> fieldsConsecutive = Propiedades.obtenerVariosParametro(plantilla, Propiedades.CONSECUTIVO);

		// String campoConsecutivo = Propiedades.obtenerValor(plantilla,
		// Propiedades.CONSECUTIVO);
		if (fieldsConsecutive != null && !fieldsConsecutive.isEmpty()) {
			if (pedido.getCaracteristicas() == null || pedido.getCaracteristicas().size() == 0)
				throw new ServerException("Se debe colocar la caracteristica nombre del documento");
			PedidoVentaCaracteristicaDTO fieldFirstValueConsecutive = null; // aqui obtengo el valor porque tengo varios
																			// campos propiedad
			for (PedidoVentaCaracteristicaDTO iField : pedido.getCaracteristicas()) {
				if (fieldFirstValueConsecutive != null)
					break;
				for (PropiedadDTO iPropertyConsecutive : fieldsConsecutive) {
					if (iField.getCampo().compareTo(iPropertyConsecutive.getValor()) == 0) {
						switch (iField.getCampoDTO().getFormato()) {
						case DocumentoPlantillaCaracteristicaDTO.TEXTO:
							if (iField.getValorText() != null)
								fieldFirstValueConsecutive = iField;
							break;
						case DocumentoPlantillaCaracteristicaDTO.NUMERO:
							if (iField.getValorNumero().compareTo(BigDecimal.ZERO) != 0)
								fieldFirstValueConsecutive = iField;
							break;
						case DocumentoPlantillaCaracteristicaDTO.PROCESO:
							if (iField.getValorOpcion() != null)
								fieldFirstValueConsecutive = iField;
							break;
						default:
							if (iField.getValorText() != null)
								fieldFirstValueConsecutive = iField;
							break;
						}
						break;
					}

				}
			}
			if (fieldFirstValueConsecutive != null) {
				switch (fieldFirstValueConsecutive.getCampoDTO().getFormato()) {
				case DocumentoPlantillaCaracteristicaDTO.NUMERO:
					pedido.setConsecutivo(fieldFirstValueConsecutive.getValorNumero());
					if (pedido.getConsecutivo().compareTo(BigDecimal.ZERO) == 0)
						throw new ServerException("Se debe colocar el numero del documento");
					if (plantilla.getConsecutivo() == null) {
						pedido.setNombre(String.valueOf(pedido.getConsecutivo().longValue()));
						codigoNuevo = pedido.getNombre();
					}
					break;
				case DocumentoPlantillaCaracteristicaDTO.TEXTO:
					codigoNuevo = fieldFirstValueConsecutive.getValorText();
					break;
				case DocumentoPlantillaCaracteristicaDTO.PROCESO:
					if (fieldFirstValueConsecutive.getValorOpcion() == null)
						throw new ServerException("El valor no puede ser nulo, para asignar un consecutivo."
								+ fieldFirstValueConsecutive.getCampoDTO().getNombre());
					PlantillaConsecutivoFilterDTO relacionConsecutivoFilter = new PlantillaConsecutivoFilterDTO();
					relacionConsecutivoFilter.setCaracteristica(fieldFirstValueConsecutive.getCampo());
					relacionConsecutivoFilter.setValorOpcion(fieldFirstValueConsecutive.getValorOpcion());
					relacionConsecutivoFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
					PlantillaConsecutivoDTO relacionConsecutivo = plantillaConsecutivoSvc
							.consultaUnica(relacionConsecutivoFilter);
					if (relacionConsecutivo == null) {
						if (plantilla.getConsecutivo() == null) {
							throw new ServerException(
									"No es posible crear el consecutivo, dado que no tenemos un consecutivo base para generar en el formulario, coloca el consecutivo base. "
											+ plantilla.getNombre());
						} else {
							ConsecutivoDTO nuevo = consecutivoService.crear2Opcion(plantilla.getConsecutivo(),
									fieldFirstValueConsecutive.getCampo(), fieldFirstValueConsecutive.getValorOpcion(),
									token);
							relacionConsecutivo = new PlantillaConsecutivoDTO();
							relacionConsecutivo.setCaracteristica(fieldFirstValueConsecutive.getCampo());
							relacionConsecutivo.setValorOpcion(fieldFirstValueConsecutive.getValorOpcion());
							relacionConsecutivo.setConsecutivo(nuevo.getLlaveTabla());
							plantillaConsecutivoSvc.guardar(relacionConsecutivo, token);

							plantilla.setConsecutivo(nuevo.getLlaveTabla());
						}
					} else {
						plantilla.setConsecutivo(relacionConsecutivo.getConsecutivo());
					}
					break;
				default:
					throw new ServerException("El componente no es tipo texto o numero");
				}
			}
		} else {
			// Creo el consecutivo y se lo asigno a la plantilla, si es rol no cuadro
			// consecutivo (Ahora si)
			if (plantilla.getConsecutivo() == null) {
				// PropiedadDTO consecProperty =
				// propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.PLANTILLA,
				// plantilla.getLlaveTabla(), Propiedades.PLANTILLA_TIPO_ROL,
				// getUserFlex(token));
				// if(consecProperty ==null)
				consecutivoService.crear(plantilla, token);
			}

		}

		if (codigoNuevo == null) {// Lo hace para los automaticos y manuales de numero

			if (plantilla.getConsecutivo() == null)
				throw new ServerException("La plantilla no tiene consecutivo asignado");

			ConsecutivoDTO consecutivoManual = consecutivoService.consultaXId(plantilla.getConsecutivo());
			if (consecutivoManual.getManual() || (!consecutivoManual.getManual() && pedido.getLlaveTabla() == null)) {
				ConsecutivoDTO consecutivo = new ConsecutivoDTO();
				consecutivo.setLlaveTabla(plantilla.getConsecutivo());
				consecutivo.setNumeroActual(pedido.getConsecutivo());
				consecutivo = consecutivoService.asignarConsecutivo(consecutivo, token);
				pedido.setConsecutivo(consecutivo.getNumeroActual());
				if (pedido.getConsecutivo().compareTo(new BigDecimal(9999999999999999.0)) > 0)
					throw new ServerException("Se excedio del numero maximo para el consecutivo 1exp16");
				codigoNuevo = consecutivo.getConsecutivoActual();
			} else {
				codigoNuevo = pedido.getNombre();
			}
			if (pedido.getLlaveTabla() != null) {
				if (!consecutivoManual.getManual()) {
					if (pedido.getNombre() == null || pedido.getNombre().compareTo(codigoNuevo) != 0)
						throw new ServerException("El consecutivo no puede ser modificado para automaticos");
				}
			}
			if (codigoNuevo == null)
				throw new ServerException("Se debe colocar el nombre del documento");
		}

		if (pedido.getLlaveTabla() == null || pedido.getNombre().compareTo(codigoNuevo) != 0) {
			validateDoubleCodeIdActive(pedido, codigoNuevo);
			pedido.setNombre(codigoNuevo);
		}
	}

	private void validateDoubleCodeIdActive(PedidoVentaDTO pedido, String codigoNuevo) throws ServerException {
		PedidoVentaFilterDTO filtroNombreFilter = new PedidoVentaFilterDTO();// Valido que no existan documentos con el
																				// mismo nombre ni cerrados ni activos
		filtroNombreFilter.setNombre(codigoNuevo);
		filtroNombreFilter.setPlantilla(pedido.getPlantilla());
		List<PedidoVentaDTO> mismoNombre = pedidoService.listarConsulta(filtroNombreFilter);
		if (mismoNombre != null && !mismoNombre.isEmpty()) {
			for (PedidoVentaDTO igualNombre : mismoNombre) {
				if (pedido.getLlaveTabla() == null
						|| pedido.getLlaveTabla().compareTo(igualNombre.getLlaveTabla()) != 0) {
					if (igualNombre.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO) != 0) {
						DocumentoPlantillaDTO plantilla = documentoPlantillaService.consultaXId(pedido.getPlantilla());
						throw new ServerException("Ya existe un " + plantilla.getNombre() + " con el mismo codigo ("
								+ igualNombre.getNombre() + "). Creado el "
								+ SoftureUtil.formatDateTime(igualNombre.getFechaRegistro()) + " con estado "
								+ igualNombre.getEstado());
					}
				}
			}
		}
	}

	private List<PedidoVentaCaracteristicaDTO> saveInternalFields(PedidoVentaDTO dto, String token)
			throws ServerException {
		List<PedidoVentaCaracteristicaDTO> result = null;
		if (dto.getCaracteristicas() != null) {
			result = new ArrayList<PedidoVentaCaracteristicaDTO>();
			for (PedidoVentaCaracteristicaDTO iterable : dto.getCaracteristicas()) {
				// iterable.getModificado()!=null &&
				if (iterable.getModificado()) {
					iterable.setDocumento(dto.getLlaveTabla());
					result.add(adaptador.guardarCampo(iterable, token));
				} else {// Antes solo devolvia las que iteraba pero no se porque
					result.add(iterable);
				}
			}
		}
		return result;
	}

	private void validateDates(PedidoVentaDTO pedido, String caracteristicaFecha) throws ServerException {
		if (caracteristicaFecha.isEmpty()) {
			if (pedido.getLlaveTabla() == null) {
				pedido.setFecha(new Date());
			}
		} else {
			PedidoVentaCaracteristicaDTO campoFecha = DocumentCommonsFunction.obtenerValor(pedido.getCaracteristicas(),
					caracteristicaFecha);
			if (campoFecha == null)
				throw new ServerException("Se debe colocar la caracteristica de fecha fecha");
			if (campoFecha.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.FECHA) != 0)
				throw new ServerException("El componente visual no es tipo fecha");
			pedido.setFecha(campoFecha.getValorFecha());
			if (pedido.getFecha() == null)
				throw new ServerException("Se debe colocar la fecha");
		}
	}

	private PedidoVentaDineroDTO saveBalance(PedidoVentaDTO documento, String token) throws ServerException {
		if (documento != null && documento.getDinero() != null) {
			PedidoVentaDineroDTO anterior = dineroService.consultaPorDocumento(documento.getLlaveTabla(),
					documento.getHistorico());
			if (anterior != null) {
				// Si todo es igual lo dejo quieto
				if (documento.getDinero().getValorTotal().compareTo(anterior.getValorTotal()) == 0
						&& documento.getDinero().getSaldo().compareTo(anterior.getSaldo()) == 0)
					return null;
				anterior = dineroService.inactivarConHistorial(anterior, documento.getHistorico());
			}
			documento.getDinero().setDocumento(documento.getLlaveTabla());
			documento.setDinero(dineroService.guardarConHistorial(documento.getDinero(), documento.getHistorico()));
			return documento.getDinero();
		}
		return null;
	}

	public void manageTemplateTypes(PedidoVentaDTO dto, DocumentoPlantillaDTO plantilla, String token)
			throws ServerException {
		// Viene de inactivar
		if (plantilla == null) {
			plantilla = new DocumentoPlantillaDTO();
			plantilla.setPropiedades(propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.PLANTILLA,
					dto.getPlantilla(), null, null));
		}
		saveRole(dto, token);

		PropiedadDTO categoria = Propiedades.obtenerParametro(plantilla, Propiedades.PLANTILLA_TIPO_PRODUCTO);
		if (categoria != null)
			productoService.crearDesdeDocumento(dto, categoria.getValor());
		if (Propiedades.obtenerParametro(plantilla, Propiedades.PLANTILLA_TIPO_BODEGA) != null)
			bodegaService.crearDesdeDocumento(dto);
		// Queda pendiente que las cuentas contables se activen En cuenta auxiliar
	}

	public void saveRole(PedidoVentaDTO dto, String token) throws ServerException {
		// Valido que tenga relacion de plantilla
		RolAccesoFilterDTO dpiRolFilter = new RolAccesoFilterDTO();
		dpiRolFilter.setPlantilla(dto.getPlantilla());
		dpiRolFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		RolAccesoDTO dpiRol = rolService.consultaUnica(dpiRolFilter);
		if (dpiRol == null)
			return;

		if (dto.getEstado() == null || dto.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO) == 0) {
			// Obtengo los valores de Id y nombre
			String usrNombre = dto.getDescripcion();
			if (usrNombre == null)
				throw new ServerException("revise la configuracion del nombre del recurso");
			String usrId = null;
			// En casos que el mismo usuario se coloque varias veces en un mismo formulario
			// x ejemplo contactos de varios proyectos
			String campoConsecutivo = propiedadService.obtenerUnica(PropiedadValorDefinidoDTO.PLANTILLA,
					dto.getPlantilla(), Propiedades.CONSECUTIVO, getUserID(token));
			if (campoConsecutivo == null)
				throw new ServerException("Se debe configurar la propiedad consecutivo para obtener el id del usuario");
			// Cuando se gestiona el proceso para activar el usuario pasa que no vienen las
			// caracteristicas
			if (dto.getCaracteristicas() == null)
				dto.setCaracteristicas(
						pedidoVentaCaracteristicaService.listar2Documento(dto.getLlaveTabla(), dto.getHistorico()));
			if (dto.getCaracteristicas().size() == 0)
				throw new ServerException("Se debe colocar la caracteristica nombre del documento");
			for (PedidoVentaCaracteristicaDTO pvc : dto.getCaracteristicas()) {
				if (pvc.getCampo().compareTo(campoConsecutivo) == 0) {
					if (pvc.getCampoDTO() == null)
						pvc.setCampoDTO(documentoPlantillaCaracteristicaService.consultaXId(pvc.getCampo()));
					switch (pvc.getCampoDTO().getFormato()) {
					case DocumentoPlantillaCaracteristicaDTO.NUMERO:
						usrId = String.valueOf(pvc.getValorNumero().longValue());
						break;
					case DocumentoPlantillaCaracteristicaDTO.TEXTO:
						usrId = pvc.getValorText();
						break;
					default:
						throw new ServerException("El componente no es tipo texto o numero");
					}
					break;
				}
			}
			if (usrId == null)
				throw new ServerException("revise la configuracion del id del recurso");
			// Consulto si el documento ya tiene una relacion con un rol
			UsuarioDTO usr = null;
			UsuarioRolFilterDTO urFilter = new UsuarioRolFilterDTO();
			urFilter.setDocumento(dto.getLlaveTabla());
			urFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			UsuarioRolDTO ur = usuarioRolService.consultaUnica(urFilter);
			if (ur == null) {
				// Si no tengo relacion, busco usuario y creo relacion
				UsuarioFilterDTO usrFilter = new UsuarioFilterDTO();
				usrFilter.setIdentificacion(usrId);
				usr = usuarioService.consultaUnica(usrFilter);
				if (usr == null) {
					usr = new UsuarioDTO();
					usr.setIdentificacion(usrId);
					usr.setNombre(usrNombre);
					usr.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
					usr = usuarioService.guardar(usr, token);
				} else {
					if (usr.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO) != 0) {
						usr.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
						usr = usuarioService.actualizar(usr, token);
					}
				}
				// Creo la reacion del rol con el documento
				ur = new UsuarioRolDTO();
				ur.setUsuario(usr.getLlaveTabla());
				ur.setRolAcceso(dpiRol.getLlaveTabla());
				ur.setDocumento(dto.getLlaveTabla());
				ur = usuarioRolService.guardar(ur, token);
			} else {
				usr = usuarioService.consultaXId(ur.getUsuario());
			}

			// 3. actualizo nombre y el id
			if (usr.getNombre().compareTo(usrNombre) != 0 || usr.getIdentificacion().compareTo(usrId) != 0) {
				UsuarioDTO usrActualizar = new UsuarioDTO();
				usrActualizar.setEstado(usr.getEstado());
				usrActualizar.setIdentificacion(usrId);
				usrActualizar.setNombre(usrNombre);
				usrActualizar.setLlaveTabla(usr.getLlaveTabla());
				usrActualizar.setImagen(usr.getImagen());
				usuarioService.actualizar(usrActualizar, token);
			}
		} else {
			UsuarioRolFilterDTO rolFilter = new UsuarioRolFilterDTO();
			rolFilter.setDocumento(dto.getLlaveTabla());
			rolFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			UsuarioRolDTO rol = usuarioRolService.consultaUnica(rolFilter);
			if (rol != null) {
				usuarioRolService.inactivar(rol, token);
			}
		}
	}

	private String getUserID(String token) throws ServerException {
		return pedidoService.getUserFlex(token);
	}

}
