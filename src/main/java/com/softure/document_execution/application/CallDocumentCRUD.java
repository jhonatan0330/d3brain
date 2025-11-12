
package com.softure.document_execution.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.voucher.application.VoucherDeleteService;
import com.configuration.homologate.application.HomologateAdapterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.authorization.application.RolAccesoSvc;
import com.softure.authorization.application.UsuarioRolSvc;
import com.softure.authorization.domain.RolAccesoDTO;
import com.softure.authorization.domain.RolAccesoFilterDTO;
import com.softure.authorization.domain.UsuarioRolDTO;
import com.softure.authorization.domain.UsuarioRolFilterDTO;
import com.softure.document_execution.application.field.CampoAdaptador;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.application.field.TipoVinculo;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaDineroDTO;
import com.softure.document_execution.domain.PedidoVentaFilterDTO;
import com.softure.document_transaction.application.DocumentoTransaccionSvc;
import com.softure.document_transaction.application.TransaccionErrorSvc;
import com.softure.document_transaction.application.TransaccionLogSvc;
import com.softure.document_transaction.domain.DocumentoTransaccionDTO;
import com.softure.document_transaction.domain.TransaccionLogFilterDTO;
import com.softure.document_transition.application.CallManageTransition;
import com.softure.document_transition.application.DocumentoRelacionGestorSvc;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.application.UsuarioSvc;
import com.softure.logisticpymes.domain.UsuarioDTO;
import com.softure.logisticpymes.domain.UsuarioFilterDTO;
import com.softure.mail.application.MailGenerateMessageService;
import com.softure.process_designer.application.ProcesoEstadoSvc;
import com.softure.process_designer.application.ProcesoTransicionSvc;
import com.softure.process_designer.domain.ProcesoTransicionDTO;
import com.softure.process_form.application.ConsecutivoSvc;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.application.PlantillaConsecutivoSvc;
import com.softure.process_form.domain.ConsecutivoDTO;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.process_form.domain.DocumentoPlantillaFilterDTO;
import com.softure.process_form.domain.PlantillaConsecutivoDTO;
import com.softure.process_form.domain.PlantillaConsecutivoFilterDTO;
import com.softure.property.application.PropertyGetWithCacheService;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.webservice.application.WebServiceExecuteAPI;

@Component
public class CallDocumentCRUD {

	@Autowired
	@Lazy
	private CampoAdaptador adaptador;
	@Autowired
	@Lazy
	private PedidoVentaSvc pedidoService;
	@Autowired
	@Lazy
	private ProcesoEstadoSvc estadoService;
	@Autowired
	@Lazy
	private DocumentoTransaccionSvc transaccionSvc;
	@Autowired
	@Lazy
	private TransaccionLogSvc logSvc;
	@Autowired
	@Lazy
	private TransaccionErrorSvc errorSvc;
	@Autowired
	@Lazy
	private DocumentoRelacionGestorSvc relacionGestorService;
	@Autowired
	@Lazy
	private MailGenerateMessageService generateMessageService;
	@Autowired
	@Lazy
	private CallManageTransition manageTransitionFunction;
	@Autowired
	@Lazy
	private WebServiceExecuteAPI apiService;
	@Autowired
	@Lazy
	private ConsecutivoSvc consecutivoService;
	@Autowired
	@Lazy
	private PlantillaConsecutivoSvc plantillaConsecutivoSvc;
	@Autowired
	@Lazy
	private ProcesoTransicionSvc transicionService;
	@Autowired
	@Lazy
	private DocumentoPlantillaSvc documentoPlantillaService;
	@Autowired
	@Lazy
	private DocumentoPlantillaCaracteristicaSvc documentoPlantillaCaracteristicaService;
	@Autowired
	@Lazy
	private PropiedadSvc propiedadService;
	@Autowired @Lazy 
	private PropertyGetWithCacheService cacheService;
	@Autowired
	@Lazy
	private UsuarioSvc usuarioService;
	@Autowired
	@Lazy
	private UsuarioRolSvc usuarioRolService;
	@Autowired
	@Lazy
	private RolAccesoSvc rolService;
	@Autowired
	@Lazy
	private PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService;
	@Autowired
	@Lazy
	private PedidoVentaDineroSvc dineroService;
	@Autowired
	@Lazy
	private CallBPM bpmService;
	@Autowired
	@Lazy
	private HomologateAdapterService homologateService;
	@Autowired
	@Lazy
	private VoucherDeleteService voucherDeleteService;
	@Autowired
	@Lazy
	private TipoVinculo tipoVinculoService;

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaDTO massive(PedidoVentaDTO pDocument, String pToken, String pSession) throws ServerException {
		//Este metodo es igual al de guardar pero debi colocar una logica del modificar
		// La idea es despues mejorar las cargas masivas 
		// Para almacenar el archivo y crear los registros desde el back
		if(pDocument.getNombre()==null) return save(pDocument, pToken, pSession);
		//Camino del Update
		PedidoVentaDTO bd = pedidoService.findByCode(pDocument.getNombre(), pDocument.getPlantilla());
		if(bd==null) throw new ServerException("No se encontro el documento con nombre " + pDocument.getNombre() + " para la plantilla " + pDocument.getPlantilla());
		bd = pedidoService.obtenerCamposCompletos(bd, pToken);
		for (PedidoVentaCaracteristicaDTO iterador : pDocument.getCaracteristicas()) {
			if(iterador.getValorText()!=null || iterador.getValorOpcion()!=null || iterador.getValorFecha()!=null || iterador.getValorNumero()!=null) {
				for (PedidoVentaCaracteristicaDTO bdField : bd.getCaracteristicas()) {
					if(bdField.getCampo().compareTo(iterador.getCampo())==0) {
						if(iterador.getValorText()!=null && iterador.getValorText().equals("NULL_SPACE")) {
							bdField.setValorText(null);
							bdField.setValorNumero(null);
							bdField.setValorFecha(null);
							bdField.setValorOpcion(null);
						}else {
							bdField.setValorText(iterador.getValorText());
							bdField.setValorNumero(iterador.getValorNumero());
							bdField.setValorFecha(iterador.getValorFecha());
							bdField.setValorOpcion(iterador.getValorOpcion());
						}
						bdField.setModificado(true);
						break;
					}
				}
			}
		}
		return update(bd, null, pToken);
	}
	
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaDTO save(PedidoVentaDTO dto, String token, String session) throws ServerException {
		String userId = getUserID(token);
		if (session != null) {
			TransaccionLogFilterDTO validateDuplicate = new TransaccionLogFilterDTO();
			validateDuplicate.setSesion(session + "-" + userId);
			if (logSvc.listarConsulta(validateDuplicate).size() != 0)
				throw new ServerException(
						"Identificamos que esta informacion ya esta almacenada por favor valida si ya se guardo el registro o sino cierra el formulario y vuelve a registrar. Gracias por tu comprension");
		}
		DocumentoTransaccionDTO tran = transaccionSvc.crear(token);
		dto.setTransaccion(tran.getLlaveTabla());
		dto.setFuncionario(userId);
		String dtoToJson = null;
		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			dtoToJson = ow.writeValueAsString(dto);
		} catch (JsonProcessingException e1) {
		}
		try {
			PedidoVentaDTO result = saveWithoutTransaction(dto, token, false);
			logSvc.finalizar(tran.getFecha(), dto.getTransaccion(), session + "-" + userId);
			return result;
		} catch (Exception e) {
			 errorSvc.finalizar(tran.getFecha(), e.getMessage(), tran.getUsuario(), dtoToJson, token);
			throw new ServerException(e.getMessage(), false);
		}
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaDTO inactivateDocumentWithProcess(PedidoVentaDTO documentDTO, PedidoVentaDTO updaterDTO,
			String token) throws ServerException {
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
		deleteVinculateDocument(documentDTO, token);
		voucherDeleteService.callByDocument(bd.getLlaveTabla(),  bd.getPlantilla(), token);
		return documentDTO;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaDTO activateDocument(PedidoVentaDTO documentDTO, PedidoVentaDTO updaterDTO, String token)
			throws ServerException {
		PedidoVentaDTO bd = pedidoService.consultaXId(documentDTO.getLlaveTabla());
		if (bd.getEstadoExpediente() != null)
			throw new ServerException("Para activar el expediente se debe usar un documento de transicion de estado");
		documentDTO = pedidoService.obtenerCamposCompletos(documentDTO, token);

		DocumentoPlantillaFilterDTO plantillaFilter = new DocumentoPlantillaFilterDTO();
		plantillaFilter.setLlaveTabla(documentDTO.getPlantilla());
		plantillaFilter.setSecurityToken(token);

		DocumentoPlantillaDTO plantilla = documentoPlantillaService.obtenerConfiguracionSinCampos(plantillaFilter,
				rolService.usuarioPermisosCompletos(token));
		plantilla = documentoPlantillaService.obtenerCampos(plantilla, token, false);

		// if (!isUpdateAutomatic)
		propiedadService.prevalidate(plantilla, documentDTO.getCaracteristicas(), documentDTO.getLlaveTabla(), token);

		documentDTO = pedidoService.activate(documentDTO);
		propiedadService.validarFuncionConsultandoPropiedad(plantilla, documentDTO.getLlaveTabla(), null,
				documentDTO.getFuncionario(), token);
		manageTemplateTypes(documentDTO, null, token);
		List<PropiedadDTO> _PropertyListToAPis = Propiedades.obtenerVariosParametro(plantilla, Propiedades.API);
		if (_PropertyListToAPis != null && !_PropertyListToAPis.isEmpty()) {
			for (PropiedadDTO _iApi : _PropertyListToAPis) {
				apiService.prepareApiToExecution(_iApi.getValor(), documentDTO, null, null, token, null);
			}
		}
		voucherCreate(bd, token);
		makeVinculateDocument(bd, token);
		generateNotifications(documentDTO, token, plantilla, documentDTO);
		// Para los tipo cuenta al actualizar no estoy mirando los sobregiros
		// if (crearTraza)
		relacionGestorService.trazar(documentDTO.getLlaveTabla(), null, plantilla.getNombre(),
				documentDTO.getEstadoExpediente(), documentDTO.getEstadoExpediente(),
				(documentDTO.getDinero() == null) ? null : documentDTO.getDinero().getLlaveTabla(), token, null,
				documentDTO.getHistorico(), documentDTO.getTransaccion(), true);
		return documentDTO;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaDTO update(PedidoVentaDTO dto, String modificadorId, String token) throws ServerException {
		return updateWithoutTransaction(dto, modificadorId, token, false);
	}

	public PedidoVentaDTO updateWithoutTransaction(PedidoVentaDTO dto, String modificadorId, String token,
			boolean isUpdateAutomatic) throws ServerException {
		PedidoVentaDTO bd = pedidoService.consultaXId(dto.getLlaveTabla());
		dto.setHistorico(bd.getHistorico()); // para evitatr errores en el calculo de valores
		dto.setPlantilla(bd.getPlantilla());
		// if(dto.getTransaccion()!=null &&
		// dto.getTransaccion().compareTo(bd.getTransaccion())==0)
		// dto.setTransaccion(null);
		DocumentoPlantillaFilterDTO plantillaFilter = new DocumentoPlantillaFilterDTO();
		plantillaFilter.setLlaveTabla(dto.getPlantilla());
		plantillaFilter.setSecurityToken(token);
		DocumentoPlantillaDTO plantilla = documentoPlantillaService.obtenerConfiguracionSinCampos(plantillaFilter,
				rolService.usuarioPermisosCompletos(token));
		plantilla = documentoPlantillaService.obtenerCampos(plantilla, token, false);
		if (!isUpdateAutomatic
				&& Propiedades.obtenerValor(plantilla, Propiedades.PERMISO_PLANTILLA_MODIFICAR).isEmpty())
			throw new ServerException("El usuario no tiene permisos para modificar un " + plantilla.getNombre());
		if (Propiedades.obtenerParametro(plantilla, Propiedades.PLANTILLA_DIFERENCIAS) == null)
			throw new ServerException(
					"Por favor configura la plantilla de registrar las diferencias. " + plantilla.getNombre());
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
		validateFields(dto, plantilla, token, isUpdateAutomatic);

		if (!isUpdateAutomatic)
			propiedadService.prevalidate(plantilla, dto.getCaracteristicas(), dto.getLlaveTabla(), token);

		if (dto.getNombre() == null) {
			dto.setNombre(bd.getNombre());// Cuando envio modificar lo envio vacio
			dto.setConsecutivo(bd.getConsecutivo());
		}
		// Descripcion para los roles
		String campoDescripcion = Propiedades.obtenerValor(plantilla, Propiedades.DESCRIPCION);
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
		// Cuando un formulario modifica otro no debo crear traza ya que esta la del
		// proceso
		boolean crearTraza = false;
		// Si son diferetnes vienen de otro proceso
		if (transaccion == null || bd.getTransaccion().compareTo(transaccion) == 0) {
			transaccion = transaccionSvc.crear(token).getLlaveTabla();
			crearTraza = true;
		}
		if (dto.getEstado() == null) {
			if (dto.getEstadoExpediente() == null) {
				dto.setEstado(SharedConstants.STATE_ACTIVE);// Viene de tipo proceso que lo coloca nulo
			} else {
				dto.setEstado(estadoService.consultaXId(dto.getEstadoExpediente()).getEstadoDocumento());
			}
		}
		dto.setFechaRegistro(bd.getFechaRegistro());// Siempre tiene que mantenerse la fecha de registro
		dto.setTransaccion(bd.getTransaccion());// Siempre tiene que mantenerse la transaccion de registro
		dto.setFuncionario(bd.getFuncionario());// Siempre tiene que mantenerse la funcionario de registro
		dto.setHistorico(bd.getHistorico());
		bd = pedidoService.update(dto);
		bd.setDinero(saveBalance(dto, token));
		for (PedidoVentaCaracteristicaDTO iterador : dto.getCaracteristicas()) {
			iterador.setTransaccionRegistro(transaccion);// Le quite el igual a null asumo que va a modificar los nuevos
			iterador.setPrincipal(bd);
		}
		dto.setCaracteristicas(saveInternalFields(dto, token));
		propiedadService.validarFuncionConsultandoPropiedad(plantilla, dto.getLlaveTabla(), modificadorId,
				dto.getFuncionario(), token);
		bpmService.execute(dto, token, null);
		manageTemplateTypes(dto, plantilla, token);
		PedidoVentaDTO updateDocument = generateUpdateDocument(plantilla, dto, transaccion, token);
		voucherDeleteService.callByDocument(dto.getLlaveTabla(), plantilla.getLlaveTabla(),  token);
		voucherCreate(dto, token);
		updateVinculateDocument(dto, token);
		generateNotifications(dto, token, plantilla, dto);
		// Para los tipo cuenta al actualizar no estoy mirando los sobregiros
		if (crearTraza)
			relacionGestorService.trazar(dto.getLlaveTabla(),
					(updateDocument == null) ? null : updateDocument.getLlaveTabla(), plantilla.getNombre(),
					dto.getEstadoExpediente(), dto.getEstadoExpediente(),
					(dto.getDinero() == null) ? null : dto.getDinero().getLlaveTabla(), token, null, dto.getHistorico(),
					transaccion, true);
		dto.setCaracteristicas(null);// Por error al serializar
		return dto;
	}

	private PedidoVentaDTO generateUpdateDocument(DocumentoPlantillaDTO template, PedidoVentaDTO dto,
			String transaccion, String token) throws ServerException {
		if (dto == null || dto.getCaracteristicas() == null || dto.getCaracteristicas().isEmpty())
			return null;
		PropiedadDTO propertyDiference = Propiedades.obtenerParametro(template, Propiedades.PLANTILLA_DIFERENCIAS);
		// if(propertyDiference==null) se supone que esto se valida antes
		List<PedidoVentaCaracteristicaDTO> fieldsDifference = new ArrayList<>();
		for (PedidoVentaCaracteristicaDTO iField : dto.getCaracteristicas()) {
			if (iField.getDifference() != null
					&& iField.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.VINCULO) != 0) {
				PropiedadDTO fieldDifference = Propiedades.obtenerParametro(iField.getCampoDTO(),
						Propiedades.CAMPO_DIFERENCIAS);
				// Esto no se puede hacer porque pide un cambio autorizado
				// if (fieldDifference == null)
				// fieldDifference =
				// documentoPlantillaCaracteristicaService.createFieldDifference(iField.getCampoDTO(),
				// propertyDiference.getValor(), token);
				if (fieldDifference == null)
					throw new ServerException("El campo " + iField.getCampoDTO().getNombre()
							+ " no tiene la propiedad de campo diferencia");
				iField.getDifference().setCampo(fieldDifference.getValor());
				fieldsDifference.add(iField.getDifference());
			}
		}
		if (fieldsDifference.isEmpty())
			return null;

		DocumentoPlantillaDTO updateTemplate = new DocumentoPlantillaDTO();
		updateTemplate.setLlaveTabla(propertyDiference.getValor());
		updateTemplate.setCaracteristicas(
				documentoPlantillaCaracteristicaService.listarCamposPlantilla(updateTemplate.getLlaveTabla(), token));

		PedidoVentaDTO updateDocument = new PedidoVentaDTO();
		updateDocument.setCaracteristicas(new ArrayList<PedidoVentaCaracteristicaDTO>());
		updateDocument.setPlantilla(updateTemplate.getLlaveTabla());
		updateDocument.setTransaccion(transaccion);
		updateDocument.setFuncionario(getUserID(token));
		String documentId = dto.getLlaveTabla();
		for (DocumentoPlantillaCaracteristicaDTO iField : updateTemplate.getCaracteristicas()) {
			PedidoVentaCaracteristicaDTO newField = null;
			for (PedidoVentaCaracteristicaDTO iDifference : fieldsDifference) {
				if (iDifference.getCampo().compareTo(iField.getLlaveTabla()) == 0) {
					newField = iDifference;
					fieldsDifference.remove(newField);
					break;
				}
			}
			if (newField == null) {
				newField = new PedidoVentaCaracteristicaDTO();
				newField.setCampo(iField.getLlaveTabla());
				// Esto es para que la plantilla update tenga el id del documento debe ser el
				// primer campo
				if (documentId != null) {
					newField.setValorOpcion(dto.getLlaveTabla());
					documentId = null;
				}
			}
			updateDocument.getCaracteristicas().add(newField);
		}
		return saveWithoutTransaction(updateDocument, token, true);
	}

	public PedidoVentaDTO saveWithoutTransaction(PedidoVentaDTO dto, String token, boolean isAutomatic)
			throws ServerException {
		return saveWithoutTransaction(dto, token, isAutomatic, null);
	}

	public PedidoVentaDTO saveWithoutTransaction(PedidoVentaDTO dto, String token, boolean isAutomatic,
			PedidoVentaDTO pGenerator) throws ServerException {
		if (dto.getLlaveTabla() != null)
			throw new ServerException("Envio un pedido a guardar con llave existente");
		if (dto.getFuncionario() == null)
			throw new ServerException("Para crear el documento debes enviar el funcionario");
		DocumentoPlantillaFilterDTO plantillaFilter = new DocumentoPlantillaFilterDTO();
		plantillaFilter.setLlaveTabla(dto.getPlantilla());
		plantillaFilter.setSecurityToken(token);
		DocumentoPlantillaDTO plantilla = documentoPlantillaService.obtenerConfiguracionSinCampos(plantillaFilter,
				(isAutomatic) ? true : rolService.usuarioPermisosCompletos(token));
		if (Propiedades.obtenerValor(plantilla, Propiedades.PERMISO_PLANTILLA_CREAR).isEmpty())
			throw new ServerException("El usuario no tiene permisos para crear un " + plantilla.getNombre());

		// Hay que optimizar el tema de los token para que no se consulte tantas veces
		// la base de datos
		if (pedidoService.isPublicToken(token)
				&& Propiedades.obtenerValor(plantilla, Propiedades.PLANTILLA_PERMISO_PUBLICO).isEmpty())
			throw new ServerException("Usuario perdio autenticacion.\nCODE:private_user");

		plantilla = documentoPlantillaService.obtenerCampos(plantilla, token, false);
		validateFields(dto, plantilla, token, false);

		propiedadService.prevalidate(plantilla, dto.getCaracteristicas(), null, token);

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
		// Al crear un documento que va a un API se estaba ejecutando el api y despues
		// decia que fallaba :(
		propiedadService.validarFuncionConsultandoPropiedad(plantilla, dto.getLlaveTabla(), null, dto.getFuncionario(),
				token);
		bpmService.execute(pedido, token, pGenerator);
		manageState(pedido, plantilla.getNombre(), token, dto.getTransaccion());
		// Aqui envio el pedido porque necesito saber si es un documento incial de
		// estado o simple para el tema de los roles
		manageTemplateTypes(pedido, plantilla, token);
		List<PropiedadDTO> _PropertyListToAPis = Propiedades.obtenerVariosParametro(plantilla, Propiedades.API);
		if (_PropertyListToAPis != null && !_PropertyListToAPis.isEmpty()) {
			for (PropiedadDTO _iApi : _PropertyListToAPis) {
				apiService.prepareApiToExecution(_iApi.getValor(), dto, null, null, token, null);
			}
		}
		voucherCreate(dto, token);
		makeVinculateDocument(dto, token);
		generateNotifications(dto, token, plantilla, pedido);
		dto.setCaracteristicas(null);// Por error al serializar
		return pedido;
	}

	// Porque lo hago hasta el final
	private void makeVinculateDocument(PedidoVentaDTO pDTO, String pToken) throws ServerException {
		if (pDTO.getCaracteristicas() == null)
			return;
		for (PedidoVentaCaracteristicaDTO _iField : pDTO.getCaracteristicas()) {
			if (_iField.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.VINCULO) == 0
					&& Propiedades.obtenerParametro(_iField.getCampoDTO(), Propiedades.PERMISO_CAMPO_BLOQUEAR)==null) {
				createDocumentOfVinculateField(pToken, _iField);
			}
		}
	}

	public void createDocumentOfVinculateField(String pToken, PedidoVentaCaracteristicaDTO _iField)
			throws ServerException {
		
		PedidoVentaDTO _vinculateDocument = tipoVinculoService.doDocumentVinculate(_iField, pToken);
		if (_vinculateDocument != null) {
			if(_vinculateDocument.getLlaveTabla()==null) _vinculateDocument = saveWithoutTransaction(_vinculateDocument, pToken, true);
			_iField.setValorOpcion(_vinculateDocument.getLlaveTabla());
			_iField.setValorText(_vinculateDocument.getNombre());
			tipoVinculoService.guardarCampo(_iField, pToken);
		}
	}

	private void updateVinculateDocument(PedidoVentaDTO pDTO, String pToken) throws ServerException {
		if (pDTO.getCaracteristicas() == null)
			return;
		for (PedidoVentaCaracteristicaDTO _iField : pDTO.getCaracteristicas()) {
			if (_iField.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.VINCULO) == 0) {
				tipoVinculoService.updateDocumentVinculate(_iField, pToken);
			}
		}
	}

	// Porque lo hago hasta el final
	public void deleteVinculateDocument(PedidoVentaDTO pDTO, String pToken) throws ServerException {
		if (pDTO.getCaracteristicas() == null) {
			if(documentoPlantillaCaracteristicaService.countFieldsVinculo(pDTO.getPlantilla())!=0){
				pDTO.setCaracteristicas(
						pedidoVentaCaracteristicaService.readCompleteFields(pDTO.getLlaveTabla(),
								documentoPlantillaCaracteristicaService
										.listarCamposPlantillaConComplementos(pDTO.getPlantilla(), pToken, false),
								pDTO.getHistorico(), pToken));
			} else {
				return; // No hay campos de vinculo
			}
		}
		for (PedidoVentaCaracteristicaDTO _iField : pDTO.getCaracteristicas()) {
			if (_iField.getCampoDTO()!= null  &&_iField.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.VINCULO) == 0) {
				_iField.setPrincipal(pDTO);// PAra evitar errores en lafuncion de delete vinculo
				PedidoVentaDTO _vinculateDocument = tipoVinculoService.deleteDocumentToVinculate(_iField, pToken);
				if (_vinculateDocument != null) {
					_vinculateDocument = saveWithoutTransaction(_vinculateDocument, pToken, true);
				}
			}
		}
	}

	private void voucherCreate(PedidoVentaDTO dto, String token)
			throws ServerException {
		List<PropiedadDTO> _PropertyListToAPis = cacheService.getByValueWithoutField(PropiedadValorDefinidoDTO.API_SERVICE, Propiedades.TEMPLATE_VOUCHER, dto.getPlantilla(), getUserID(token));
		if (_PropertyListToAPis == null || _PropertyListToAPis.isEmpty())
			return;

		for (PropiedadDTO _iVoucher : _PropertyListToAPis) {
			apiService.programateExecution(_iVoucher.getCampo(), dto.getLlaveTabla(), null, dto.getTransaccion(),
					token);
		}
	}

	private void generateNotifications(PedidoVentaDTO dto, String token, DocumentoPlantillaDTO plantilla,
			PedidoVentaDTO pedido) throws ServerException {
		List<PropiedadDTO> _propertyListToNotify = Propiedades.obtenerVariosParametro(plantilla,
				Propiedades.TEMPLATE_MESSAGE_SQL);
		if (_propertyListToNotify == null || _propertyListToNotify.isEmpty())
			return;
		for (PropiedadDTO _iValidation : _propertyListToNotify) {
			CallDocumentCommons.addMessageError(pedido, propiedadService.templateNotifications(
					_iValidation.getLlaveTabla(), dto.getCaracteristicas(), token, dto.getLlaveTabla()));
		}
	}

	private void manageState(PedidoVentaDTO pedido, String plantillaNombre, String token, String transaccion)
			throws ServerException {
		ProcesoTransicionDTO inicial = transicionService.consultarTransaccionInicial(pedido.getPlantilla());
		if (inicial != null) {
			manageTransitionFunction.execute(inicial, pedido.getLlaveTabla(), pedido,
					(pedido.getDinero() == null) ? null : pedido.getDinero().getValorTotal(), pedido.getDinero(), null,
					token, transaccion, null, null);
		} else {// Cuando son transacciones que no inician un proceso (aqui traza del documento
				// en tipo proceso traza al proceso)
				// cundo son solo documetnos sin transciones se envian mensajes
			generateMessageService.call(pedido, null, usuarioService.consultaXId(pedido.getFuncionario()), pedido,
					token);
			// Pase aqui la traza ya que debo integrar
			relacionGestorService.trazar(pedido.getLlaveTabla(), null, plantillaNombre, null,
					pedido.getEstadoExpediente(),
					(pedido.getDinero() == null) ? null : pedido.getDinero().getLlaveTabla(), token, null,
					pedido.getHistorico(), transaccion, false);
		}
		// return inicial;
	}

	private void validateBalance(PedidoVentaDTO pedido, DocumentoPlantillaDTO plantilla) throws ServerException {
		PropiedadDTO total = Propiedades.obtenerParametro(plantilla, Propiedades.TOTAL);
		if (total != null) {
			PedidoVentaCaracteristicaDTO campoValor = CallDocumentCommons.obtenerValor(pedido.getCaracteristicas(),
					total.getValor());
			if (campoValor == null)
				throw new ServerException("En la plantilla " + plantilla.getNombre()
						+ " se registro la propiedad TOTAL pero el campo no se encuentra, revisa que el campo del propiedad total no este inactivo");
			// En roa me sucdeio que automaticamente modifciaban una guia y despues
			// modficaban el recibo pero el valor quedaba mal porque tomaba el valor viejo
			if (campoValor.getModificado()) {
				PedidoVentaDineroDTO dineroCalculado = new PedidoVentaDineroDTO();
				dineroCalculado.setValorTotal(campoValor.getValorNumero());
				dineroCalculado.setSaldo(BigDecimal.ZERO);
				// Si es modificar debo actualizar el saldo, solo tienen saldo los que son de
				// proceso
				if (pedido.getLlaveTabla() != null && pedido.getEstadoExpediente() != null) {
					PedidoVentaDineroDTO anterior = dineroService.consultaPorDocumento(pedido.getLlaveTabla(),
							pedido.getHistorico(), pedido.getNombre());
					if (anterior != null) {
						dineroCalculado.setSaldo(anterior.getSaldo());
						if (campoValor.getModificado() && anterior.getControlarSaldo()) {
							BigDecimal diferencia = campoValor.getValorNumero().subtract(anterior.getValorTotal());
							dineroCalculado.setSaldo(dineroCalculado.getSaldo().add(diferencia));
							/*
							 * // En el proceso de facturacion se softure el saldo sube en el momento que se
							 * // aprueba la factura y desde el inicial no es afecta saldo if (anterior !=
							 * null) { BigDecimal diferencia =
							 * campoValor.getValorNumero().subtract(anterior.getValorTotal());
							 * dineroCalculado.setSaldo(dineroCalculado.getSaldo().add(diferencia));
							 * 
							 * } else { ProcesoTransicionDTO inicial = transicionService
							 * .consultarTransaccionInicial(pedido.getPlantilla()); if (inicial != null &&
							 * inicial.getAfectaSaldo() != null)
							 * dineroCalculado.setSaldo(campoValor.getValorNumero()); }
							 */
						}
					}

				}
				pedido.setDinero(dineroCalculado);
			} else {
				if (pedido.getLlaveTabla() != null && pedido.getEstadoExpediente() != null) {
					pedido.setDinero(dineroService.consultaPorDocumento(pedido.getLlaveTabla(), pedido.getHistorico(),
							pedido.getNombre()));
				}
			}

		} else {
			pedido.setDinero(null);
		}
	}

	private void validateFields(PedidoVentaDTO dto, DocumentoPlantillaDTO plantilla, String token,
			boolean isUpdateAutomatic) throws ServerException {
		if (plantilla != null && plantilla.getCaracteristicas() != null && !plantilla.getCaracteristicas().isEmpty()) {
			String filtroTexto = "";
			String propDescription = Propiedades.obtenerValor(plantilla, Propiedades.DESCRIPCION);
			if (dto.getCaracteristicas() == null)
				throw new ServerException("Es necesesario registrar informacion adicional.");
			// En casos como generacion automatica vienen en desorden
			List<PedidoVentaCaracteristicaDTO> ordenadas = new ArrayList<PedidoVentaCaracteristicaDTO>();
			for (DocumentoPlantillaCaracteristicaDTO campoPlantilla : plantilla.getCaracteristicas()) {
				boolean campoEncontrado = false;
				// 1 Coloco los campos DTO
				for (PedidoVentaCaracteristicaDTO campoDocumento : dto.getCaracteristicas()) {
					if (campoDocumento.getCampo() != null
							&& campoDocumento.getCampo().compareTo(campoPlantilla.getLlaveTabla()) == 0) {
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
							+ " no viene registrado en el documento " + plantilla.getNombre() + "\nCODE:caud_usuario");
			}
			dto.setCaracteristicas(ordenadas);
			if (!isUpdateAutomatic && dto.getLlaveTabla() != null) {
				// Valido para actualizar que el campo si se pueda modiifcar
				if (!plantilla.getCaracteristicas().isEmpty()) {
					boolean iContadorModificadas = false;
					for (PedidoVentaCaracteristicaDTO iCampoDocumento : dto.getCaracteristicas()) {
						if (iCampoDocumento.getModificado()) {
							if (Propiedades.obtenerParametro(iCampoDocumento.getCampoDTO(),
									Propiedades.PERMISO_CAMPO_MODIFICABLE) == null
									&& Propiedades.obtenerParametro(iCampoDocumento.getCampoDTO(),
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

				organizeDepends(dto.getCaracteristicas(), campoDocumento);
			}
			// 3. valido cada campo
			for (PedidoVentaCaracteristicaDTO campoDocumento : dto.getCaracteristicas()) {
				adaptador.validarPrepararCampo(campoDocumento, token, isUpdateAutomatic);
				// Como no es el mismo documento y no quiero forzarlo a que sea el mimso le
				// copioe los mensajes
				CallDocumentCommons.copyMessages(campoDocumento.getPrincipal(), dto);
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
				if (!propDescription.isEmpty() && propDescription.compareTo(campoDocumento.getCampo()) == 0) {
					dto.setDescripcion(campoDocumento.getValorText());
				}
			}
			if (filtroTexto.compareTo("") != 0) {
				dto.setTextoFiltro(SoftureUtil.formatSimpleFunction(filtroTexto).toUpperCase());
			} else {
				dto.setTextoFiltro(null);
			}
		}
	}

	public void organizeDepends(List<PedidoVentaCaracteristicaDTO> pFieldsDTO, PedidoVentaCaracteristicaDTO pField) {
		List<PropiedadDTO> _propDepend = Propiedades.obtenerVariosParametro(pField.getCampoDTO(),
				Propiedades.DEPENDENT_PROPS);
		if (_propDepend != null) {
			for (PropiedadDTO _iProp : _propDepend) {
				for (PedidoVentaCaracteristicaDTO fieldExpediente : pFieldsDTO) {
					if (_iProp.getValor().compareTo(fieldExpediente.getCampo()) == 0) {
						if (pField.getDependientes() == null)
							pField.setDependientes(new ArrayList<PedidoVentaCaracteristicaDTO>());
						if (fieldExpediente.getModificado())
							pField.setModificado(true);
						pField.getDependientes().add(fieldExpediente);
						break;
					}
				}
			}
			// Esto es muy riesgoso hacerlo toca despues con calma hacer pruebas
			// campoDocumento.setDependientes(pedidoVentaCaracteristicaService.ordenarAlfabeticaDepende(campoDocumento.getDependientes()));
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
			PedidoVentaCaracteristicaDTO fieldFirstValueConsecutive = null;
			// aqui obtengo el valor porque tengo varios campos propiedad
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
					relacionConsecutivoFilter.setEstado(SharedConstants.STATE_ACTIVE);
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

		ConsecutivoDTO consecutivoManual = null;
		if (codigoNuevo == null) {// Lo hace para los automaticos y manuales de numero

			if (plantilla.getConsecutivo() == null)
				throw new ServerException("La plantilla no tiene consecutivo asignado");

			consecutivoManual = consecutivoService.consultaXId(plantilla.getConsecutivo());
			if (consecutivoManual.getManual() || (!consecutivoManual.getManual() && pedido.getLlaveTabla() == null)) {
				codigoNuevo = asignateConsecutive(pedido, plantilla.getConsecutivo(), token);
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

		if (pedido.getLlaveTabla() == null || pedido.getNombre().compareTo(codigoNuevo) != 0)
			pedido.setNombre(validateDoubleCodeIdActive(pedido, codigoNuevo, consecutivoManual));

	}

	private String asignateConsecutive(PedidoVentaDTO pedido, String consecutiveId, String token)
			throws ServerException {

		ConsecutivoDTO consecutivo = new ConsecutivoDTO();
		consecutivo.setLlaveTabla(consecutiveId);
		consecutivo.setNumeroActual(pedido.getConsecutivo());
		consecutivo = consecutivoService.asignarConsecutivo(consecutivo, token);
		pedido.setConsecutivo(consecutivo.getNumeroActual());
		if (pedido.getConsecutivo().compareTo(new BigDecimal(9999999999999999.0)) > 0)
			throw new ServerException("Se excedio del numero maximo para el consecutivo 1exp16");
		return consecutivo.getConsecutivoActual();
	}

	private String validateDoubleCodeIdActive(PedidoVentaDTO pedido, String codigoNuevo, ConsecutivoDTO consecutive)
			throws ServerException {
		PedidoVentaFilterDTO filtroNombreFilter = new PedidoVentaFilterDTO();
		// Valido que no existan documentos con el mismo nombre ni cerrados ni activos
		filtroNombreFilter.setNombre(codigoNuevo);
		filtroNombreFilter.setPlantilla(pedido.getPlantilla());
		List<PedidoVentaDTO> mismoNombre = pedidoService.listarConsulta(filtroNombreFilter);
		if (mismoNombre == null || mismoNombre.isEmpty())
			return codigoNuevo;
		for (PedidoVentaDTO igualNombre : mismoNombre) {
			if (pedido.getLlaveTabla() == null || pedido.getLlaveTabla().compareTo(igualNombre.getLlaveTabla()) != 0) {
				if (igualNombre.getEstado().compareTo(SharedConstants.STATE_INACTIVE) != 0) {
					// Se hace para evitar el error de concurrencia con los consecutivos automaticos
					if (consecutive != null && !consecutive.getManual()) {
						codigoNuevo = asignateConsecutive(pedido, consecutive.getLlaveTabla(), null);
						return validateDoubleCodeIdActive(pedido, codigoNuevo, consecutive);
					} else {
						DocumentoPlantillaDTO plantilla = documentoPlantillaService.consultaXId(pedido.getPlantilla());
						throw new ServerException("Ya existe un " + plantilla.getNombre() + " con el mismo codigo ("
								+ igualNombre.getNombre() + "). Creado el "
								+ SoftureUtil.formatDateTime(igualNombre.getFechaRegistro()) + " con estado "
								+ igualNombre.getEstado());
					}
				}
			}
		}
		return codigoNuevo;
	}

	private List<PedidoVentaCaracteristicaDTO> saveInternalFields(PedidoVentaDTO dto, String token)
			throws ServerException {
		if (dto.getCaracteristicas() == null)
			return null;
		List<PedidoVentaCaracteristicaDTO> result = new ArrayList<PedidoVentaCaracteristicaDTO>();
		for (PedidoVentaCaracteristicaDTO iterable : dto.getCaracteristicas()) {
			if (iterable.getModificado()) {
				iterable.setDocumento(dto.getLlaveTabla());
				result.add(adaptador.guardarCampo(iterable, token));
			} else {// Antes solo devolvia las que iteraba pero no se porque
				result.add(iterable);
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
			PedidoVentaCaracteristicaDTO campoFecha = CallDocumentCommons.obtenerValor(pedido.getCaracteristicas(),
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
					documento.getHistorico(), documento.getNombre());
			if (anterior != null) {
				// En box sucedia que se guardaba y se hacia una tercera modificacion y el saldo
				// no se controlaba
				documento.getDinero().setControlarSaldo(anterior.getControlarSaldo());
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
			plantilla.setPropiedades(cacheService.obtenerPropiedades( PropiedadValorDefinidoDTO.PLANTILLA,
					dto.getPlantilla(), null, null));
		}
		// Sucede que en los estados tambien se llama esta funcion, y cuando son
		// procesos de inicio se duplicaba y generaba error
		if (dto.getEstadoExpediente() == null)
			saveRole(dto, token);

		if (Propiedades.obtenerParametro(plantilla, Propiedades.PLANTILLA_TIPO_PRODUCTO) != null)
			homologateService.crearProducto(dto, token);
		if (Propiedades.obtenerParametro(plantilla, Propiedades.PLANTILLA_TIPO_CUENTA) != null)
			homologateService.crearCuenta(dto, token);
		// Queda pendiente que las cuentas contables se activen En cuenta auxiliar
		if (Propiedades.obtenerParametro(plantilla, Propiedades.PLANTILLA_TIPO_CONFIGURATION) != null)
			homologateService.createFromDocument(dto,
					Propiedades.obtenerParametro(plantilla, Propiedades.PLANTILLA_TIPO_CONFIGURATION).getValor(),
					token);
	}

	@Transactional(value = "transactionManager", propagation = Propagation.REQUIRES_NEW, noRollbackFor = DuplicateKeyException.class)
	public void saveRole(PedidoVentaDTO dto, String token) throws ServerException {
		// Valido que tenga relacion de plantilla
		RolAccesoFilterDTO dpiRolFilter = new RolAccesoFilterDTO();
		dpiRolFilter.setPlantilla(dto.getPlantilla());
		dpiRolFilter.setEstado(SharedConstants.STATE_ACTIVE);
		RolAccesoDTO dpiRol = rolService.consultaUnica(dpiRolFilter);
		if (dpiRol == null)
			return;

		if (dto.getEstado() == null || dto.getEstado().compareTo(SharedConstants.STATE_ACTIVE) == 0) {
			// Obtengo los valores de Id y nombre
			String usrNombre = dto.getDescripcion();
			if (usrNombre == null)
				throw new ServerException("revise la configuracion del nombre del recurso");
			String usrId = null;
			String usrMail = null;
			String usrPhone = null;

			String campoCorreo = cacheService.obtenerUnica( PropiedadValorDefinidoDTO.PLANTILLA, dto.getPlantilla(),
					Propiedades.CORREO_ROL, getUserID(token));
			String campoCelular = cacheService.obtenerUnica(PropiedadValorDefinidoDTO.PLANTILLA, dto.getPlantilla(),
					Propiedades.CELULAR_ROL, getUserID(token));

			// En casos que el mismo usuario se coloque varias veces en un mismo formulario
			// x ejemplo contactos de varios proyectos
			String campoConsecutivo = cacheService.obtenerUnica( PropiedadValorDefinidoDTO.PLANTILLA,
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
				if (usrId == null && pvc.getCampo().compareTo(campoConsecutivo) == 0) {
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
				}
				if (usrMail == null && campoCorreo != null && pvc.getCampo().compareTo(campoCorreo) == 0) {
					usrMail = pvc.getValorText();
				}
				if (usrPhone == null && campoCelular != null && pvc.getCampo().compareTo(campoCelular) == 0) {
					usrPhone = pvc.getValorText();
				}
			}
			if (usrId == null)
				throw new ServerException("revise la configuracion del id del recurso");
			// Consulto si el documento ya tiene una relacion con un rol
			UsuarioDTO usr = null;
			UsuarioRolFilterDTO urFilter = new UsuarioRolFilterDTO();
			urFilter.setDocumento(dto.getLlaveTabla());
			urFilter.setEstado(SharedConstants.STATE_ACTIVE);
			UsuarioRolDTO ur = usuarioRolService.consultaUnica(urFilter);
			// Cuando se modifica un contacto que tenia mal el id salia un errro de forgin
			// key
			if (ur != null && ur.getUsuarioIdentificacion().compareTo(usrId) != 0) {
				inactivateRolOfDocument(ur.getDocumento(), token);
				ur = null;
			}
			if (ur == null) {
				// Si no tengo relacion, busco usuario y creo relacion
				UsuarioFilterDTO usrFilter = new UsuarioFilterDTO();
				usrFilter.setIdentificacion(usrId);
				usr = usuarioService.consultaUnica(usrFilter);
				if (usr == null) {
					usr = new UsuarioDTO();
					usr.setIdentificacion(usrId);
					usr.setNombre(usrNombre);
					usr.setCorreo(usrMail);
					usr.setTelefono(usrPhone);
					usr.setEstado(SharedConstants.STATE_ACTIVE);
					try {
						usr = usuarioService.guardar(usr, token);	
					}  catch (DuplicateKeyException e) {
						return; // Si dos procesos intentan crear el mismo usuario esto sucede en cisrtos casos remotos de apis
					}catch (Exception e) {
						throw new ServerException(e.getMessage());
					}
				} else {
					if (usr.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0) {
						usr.setCorreo(usrMail);
						usr.setTelefono(usrPhone);
						usr.setEstado(SharedConstants.STATE_ACTIVE);
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
			if (usr.getNombre().compareTo(usrNombre) != 0 || usr.getIdentificacion().compareTo(usrId) != 0
					|| (usrMail != null && (usr.getCorreo() == null || usr.getCorreo().compareTo(usrMail) != 0))
					|| (usrPhone != null
							&& (usr.getTelefono() == null || usr.getTelefono().compareTo(usrPhone) != 0))) {
				UsuarioDTO usrActualizar = new UsuarioDTO();
				usrActualizar.setEstado(usr.getEstado());
				usrActualizar.setIdentificacion(usrId);
				usrActualizar.setNombre(usrNombre);
				if (usrMail != null) {
					usrActualizar.setCorreo(usrMail);
				} else {
					usrActualizar.setCorreo(usr.getCorreo());
				}
				if (usrPhone != null) {
					usrActualizar.setTelefono(usrPhone);
				} else {
					usrActualizar.setTelefono(usr.getTelefono());
				}
				usrActualizar.setLlaveTabla(usr.getLlaveTabla());
				usrActualizar.setImagen(usr.getImagen());
				usuarioService.actualizar(usrActualizar, token);
			}
		} else {
			inactivateRolOfDocument(dto.getLlaveTabla(), token);
		}
	}

	private void inactivateRolOfDocument(String document, String token) throws ServerException {
		UsuarioRolFilterDTO rolFilter = new UsuarioRolFilterDTO();
		rolFilter.setDocumento(document);
		rolFilter.setEstado(SharedConstants.STATE_ACTIVE);
		UsuarioRolDTO rol = usuarioRolService.consultaUnica(rolFilter);
		if (rol != null) {
			usuarioRolService.inactivar(rol, token);
		}
	}

	private String getUserID(String token) throws ServerException {
		return pedidoService.getUserFlex(token);
	}

}
