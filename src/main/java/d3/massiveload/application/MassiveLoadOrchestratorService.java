package d3.massiveload.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import d3.shared.domain.ServerException;
import d3.authorization.application.RolAccesoSvc;
import d3.document_execution.application.CallDocumentCRUD;
import d3.document_execution.application.PedidoVentaSvc;
import d3.document_execution.domain.DocumentMessage;
import d3.document_execution.domain.PedidoVentaDTO;
import d3.massiveload.domain.MasivaItemRequest;
import d3.massiveload.domain.MassiveItemDTO;
import d3.massiveload.domain.MassiveItemFilter;
import d3.massiveload.domain.MassiveMasterDTO;
import d3.massiveload.domain.MassiveMasterRequest;
import d3.process_form.application.DocumentoPlantillaSvc;
import d3.process_form.domain.DocumentoPlantillaDTO;
import d3.process_form.domain.DocumentoPlantillaFilterDTO;

@Service
public class MassiveLoadOrchestratorService {

	private static final int MAX_DOCUMENTS = 20000;

	private final MassiveFileParserService parserService;
	private final MassiveDocumentBuilderService builderService;
	private final MassiveValidationService validationService;
	private final MassiveCRUDMasterService cargaMasivaService;
	private final MassiveCRUDItemService cargaMasivaItemService;
	private final DocumentoPlantillaSvc plantillaService;
	private final RolAccesoSvc rolService;
	private final CallDocumentCRUD callDocumentCRUD;
	private final PedidoVentaSvc pedidoService;

	private final ObjectMapper mapper = new ObjectMapper();

	public MassiveLoadOrchestratorService(@Lazy MassiveFileParserService parserService,
			@Lazy MassiveDocumentBuilderService builderService, @Lazy MassiveValidationService validationService,
			@Lazy MassiveCRUDMasterService cargaMasivaService, @Lazy MassiveCRUDItemService cargaMasivaItemService,
			@Lazy DocumentoPlantillaSvc plantillaService, @Lazy RolAccesoSvc rolService,
			@Lazy CallDocumentCRUD callDocumentCRUD,
			@Lazy PedidoVentaSvc pedidoService) {
		this.parserService = parserService;
		this.builderService = builderService;
		this.validationService = validationService;
		this.cargaMasivaService = cargaMasivaService;
		this.cargaMasivaItemService = cargaMasivaItemService;
		this.plantillaService = plantillaService;
		this.rolService = rolService;
		this.callDocumentCRUD = callDocumentCRUD;
		this.pedidoService = pedidoService;
	}

	public MassiveMasterRequest uploadFile(MultipartFile file, String templateId, String token) throws ServerException {
		DocumentoPlantillaDTO plantilla = obtenerPlantilla(templateId, token);
		MassiveMasterDTO master = new MassiveMasterDTO();
		master.setArchivo(file.getOriginalFilename());
		master.setPlantilla(templateId);
		master.setUsuario(pedidoService.getUserFlex(token));
		master.setFecha(new Date());
		master.setState(MassiveMasterDTO.CARGANDO);
		master = cargaMasivaService.saveAndFindById(master);

		List<MassiveItemDTO> items = new ArrayList<>();
		try {
			List<java.util.Map<String, String>> rows = parserService.parse(file, plantilla);
			if (rows == null || rows.isEmpty()) {
				master.setState(MassiveMasterDTO.ERROR);
				master.setMensaje("No se generaron registros a partir del archivo");
				cargaMasivaService.update(master);
				return master.toValueObject();
			}
			if (rows.size() > MAX_DOCUMENTS) {
				master.setState(MassiveMasterDTO.ERROR);
				master.setMensaje("El maximo de documentos a cargar son " + MAX_DOCUMENTS);
				cargaMasivaService.update(master);
				return master.toValueObject();
			}
			int order = 0;
			for (java.util.Map<String, String> row : rows) {
				order++;
				PedidoVentaDTO pedido = builderService.build(row, plantilla);
				pedido.setTextoFiltro(String.valueOf(order));
				MassiveItemDTO item = new MassiveItemDTO();
				item.setCarga(master.getKey());
				item.setFechaSerializacion(new Date());
				item.setNombre(pedido.getNombre());
				try {
					item.setModelo(mapper.writeValueAsString(pedido));
					item.setState(MassiveItemDTO.SERIALIZADA);
					item.setProgreso("Serializado");
				} catch (JsonProcessingException e) {
					item.setNombre(master.getKey());
					item.setState(MassiveItemDTO.ERROR);
					item.setProgreso("Error serializando");
				}
				items.add(item);
			}
			for (MassiveItemDTO item : items) {
				cargaMasivaItemService.save(item);
			}
			master.setState(MassiveMasterDTO.SERIALIZADA);
			master.setProgreso(items.size() + " registros serializados");
		} catch (ServerException e) {
			master.setState(MassiveMasterDTO.ERROR);
			master.setMensaje(e.getMessage());
			cargaMasivaService.update(master);
			return master.toValueObject();
		}
		cargaMasivaService.update(master);
		return master.toValueObject();
	}

	public MassiveMasterRequest validateLoad(String loadId, String token) throws ServerException {
		MassiveMasterDTO master = cargaMasivaService.findById(loadId);
		if (MassiveMasterDTO.FINALIZADA.equals(master.getState())
				|| MassiveMasterDTO.TERMINADA_CON_FALLAS.equals(master.getState()))
			throw new ServerException("La carga masiva ya fue ejecutada, no se puede validar nuevamente");
		DocumentoPlantillaDTO plantilla = obtenerPlantilla(master.getPlantilla(), token);
		int validados = 0;
		int conError = 0;
		for (MassiveItemDTO item : listItems(loadId)) {
			if (!MassiveItemDTO.SERIALIZADA.equals(item.getState()))
				continue;
			PedidoVentaDTO pedido = deserialize(item.getModelo());
			List<DocumentMessage> messages = validationService.validate(pedido, plantilla, token);
			if (messages.isEmpty()) {
				item.setState(MassiveItemDTO.VALIDADO);
				validados++;
			} else {
				pedido.setMessages(messages);
				try {
					item.setModelo(mapper.writeValueAsString(pedido));
				} catch (JsonProcessingException e) {
				}
				item.setState(MassiveItemDTO.ERROR);
				conError++;
			}
			cargaMasivaItemService.update(item);
		}
		if (conError == 0 && validados > 0) {
			master.setState(MassiveMasterDTO.VALIDADO);
			master.setProgreso(validados + " registros validados correctamente");
		} else {
			master.setState(MassiveMasterDTO.PENDIENTE);
			master.setProgreso(validados + " validados, " + conError + " con errores");
		}
		cargaMasivaService.update(master);
		return master.toValueObject();
	}

	public MassiveMasterRequest executeLoad(String loadId, String token) throws ServerException {
		MassiveMasterDTO master = cargaMasivaService.findById(loadId);
		if (!MassiveMasterDTO.VALIDADO.equals(master.getState()))
			throw new ServerException(
					"Para ejecutar la carga masiva todos los registros deben estar validados. Estado actual: "
							+ master.getState());
		int finalizados = 0;
		int conError = 0;
		for (MassiveItemDTO item : listItems(loadId)) {
			if (!MassiveItemDTO.VALIDADO.equals(item.getState()))
				continue;
			PedidoVentaDTO pedido = deserialize(item.getModelo());
			try {
				PedidoVentaDTO result = callDocumentCRUD.massive(pedido, token, null);
				item.setDocumento(result.getLlaveTabla());
				item.setNombre(result.getNombre());
				item.setFechaSincronizacion(new Date());
				item.setState(MassiveItemDTO.FINALIZADA);
				finalizados++;
			} catch (ServerException e) {
				PedidoVentaDTO conErrorMsg = deserialize(item.getModelo());
				List<DocumentMessage> messages = new ArrayList<>();
				DocumentMessage msg = new DocumentMessage();
				msg.setMessage(e.getMessage());
				messages.add(msg);
				conErrorMsg.setMessages(messages);
				try {
					item.setModelo(mapper.writeValueAsString(conErrorMsg));
				} catch (JsonProcessingException ex) {
				}
				item.setFechaSincronizacion(new Date());
				item.setState(MassiveItemDTO.ERROR);
				conError++;
			}
			cargaMasivaItemService.update(item);
		}
		if (conError == 0) {
			master.setState(MassiveMasterDTO.FINALIZADA);
			master.setProgreso(finalizados + " registros guardados");
		} else {
			master.setState(MassiveMasterDTO.TERMINADA_CON_FALLAS);
			master.setProgreso(finalizados + " guardados, " + conError + " con errores");
		}
		cargaMasivaService.update(master);
		return master.toValueObject();
	}

	public MassiveMasterRequest getLoad(String loadId, String token) throws ServerException {
		return cargaMasivaService.findById(loadId).toValueObject();
	}

	public List<MasivaItemRequest> getItems(String loadId, String token) throws ServerException {
		List<MasivaItemRequest> result = new ArrayList<>();
		for (MassiveItemDTO item : listItems(loadId)) {
			result.add(item.toValueObject());
		}
		return result;
	}

	private List<MassiveItemDTO> listItems(String loadId) throws ServerException {
		MassiveItemFilter filter = new MassiveItemFilter(null, 0, MAX_DOCUMENTS + 10);
		filter.setCarga(loadId);
		return cargaMasivaItemService.findMany(filter);
	}

	private DocumentoPlantillaDTO obtenerPlantilla(String templateId, String token) throws ServerException {
		DocumentoPlantillaFilterDTO plantillaFilter = new DocumentoPlantillaFilterDTO();
		plantillaFilter.setLlaveTabla(templateId);
		plantillaFilter.setSecurityToken(token);
		DocumentoPlantillaDTO plantilla = plantillaService.obtenerConfiguracionSinCampos(plantillaFilter,
				rolService.usuarioPermisosCompletos(token));
		plantilla = plantillaService.obtenerCampos(plantilla, token, false);
		return plantilla;
	}

	private PedidoVentaDTO deserialize(String json) throws ServerException {
		try {
			return mapper.readValue(json, PedidoVentaDTO.class);
		} catch (JsonProcessingException e) {
			throw new ServerException("Error deserializando el registro: " + e.getMessage());
		}
	}
}
