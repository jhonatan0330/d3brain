package d3.webservice.application;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.authentication.application.UsuarioSesionSvc;
import d3.authentication.domain.UsuarioSesionDTO;
import d3.document_transaction.application.DocumentoTransaccionSvc;
import d3.logisticpymes.application.BasicSvc;
import d3.webservice.domain.WebServiceDTO;
import d3.webservice.domain.WebServiceEjecucionDTO;
import d3.webservice.domain.WebServiceEjecucionFilterDTO;
import d3.webservice.infrastructure.WebServiceEjecucionMapper;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;

@Service("webServiceEjecucionService")
public class WebServiceEjecucionSvc extends BasicSvc<WebServiceEjecucionDTO, WebServiceEjecucionFilterDTO> {

	private final WebServiceEjecucionMapper webServiceEjecucionMapper;
	private final UsuarioSesionSvc autenticacionService;
	private final WebServiceExecuteAPI executeAPIFunction;
	private final WebServiceSvc webServiceSvc;

	public WebServiceEjecucionSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy WebServiceEjecucionMapper webServiceEjecucionMapper, @Lazy UsuarioSesionSvc autenticacionService,
			@Lazy WebServiceExecuteAPI executeAPIFunction, @Lazy WebServiceSvc webServiceSvc) {
		super(usuarioSesionService);
		this.webServiceEjecucionMapper = webServiceEjecucionMapper;
		this.autenticacionService = autenticacionService;
		this.executeAPIFunction = executeAPIFunction;
		this.webServiceSvc = webServiceSvc;
	}

	@Override
	public WebServiceEjecucionDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. WebServiceEjecucion");
		WebServiceEjecucionFilterDTO dto = new WebServiceEjecucionFilterDTO();
		dto.setLlaveTabla(llave);
		return webServiceEjecucionMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = webServiceEjecucionMapper;
	}

	public WebServiceEjecucionDTO ejecutarAPI(WebServiceEjecucionFilterDTO dto) throws ServerException {
		WebServiceEjecucionDTO bd = consultaXId(dto.getLlaveTabla());
		if (bd.getFechaEjecucion() != null)
			throw new ServerException("Este API ya fue ejecutado");
		if (bd.getSincrona() == null)
			throw new ServerException("Este API no es asincrono");
		if (bd.getSincrona().compareTo(DocumentoTransaccionSvc.API_PREPARE_ASYNC) == 0) {
			executeAPIFunction.applyScheduleToExecute(consultaXId(dto.getLlaveTabla()), dto.getSecurityToken());
		} else {
			WebServiceDTO service = webServiceSvc.consultaXId(bd.getServicio());
			executeAPIFunction.executeApi(service, bd, dto.getSecurityToken(), null, null, null);
		}
		return consultaXId(dto.getLlaveTabla());
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public WebServiceEjecucionDTO guardar(WebServiceEjecucionDTO dto, String token) throws ServerException {
		return super.guardar(dto, token);
	}

	public String apiToTransaction() throws ServerException {
		List<WebServiceEjecucionDTO> tareasPendientes = webServiceEjecucionMapper.apisTransaccion();

		if (tareasPendientes == null || tareasPendientes.isEmpty())
			return "*******APIS ASYNC (0) ****" + new Date().toString();
		UsuarioSesionDTO sessionAdmin = autenticacionService.generateAdministratorToken();
		for (WebServiceEjecucionDTO iMessage : tareasPendientes) {
			if (iMessage.getSincrona().compareTo(DocumentoTransaccionSvc.API_PREPARE_ASYNC) == 0) {
				executeAPIFunction.applyScheduleToExecute(iMessage, sessionAdmin.getLlaveTabla());
			} else {

				WebServiceDTO service = webServiceSvc.consultaXId(iMessage.getServicio());
				if (service == null)
					throw new ServerException("El id del servicio no se encuentra en la BD.");
				executeAPIFunction.executeApi(service, iMessage, sessionAdmin.getLlaveTabla(), null, null, null);
			}
		}
		return "*******APIS ASYNC (" + tareasPendientes.size() + ") ****" + new Date().toString();
	}

	public WebServiceEjecucionDTO getServiceVoucherActive(String pServiceId, String pDocumentId)
			throws ServerException {
		WebServiceEjecucionFilterDTO _serviceFilter = new WebServiceEjecucionFilterDTO();
		_serviceFilter.setServicio(pServiceId);
		_serviceFilter.setDocumento(pDocumentId);
		_serviceFilter.setSincrona(DocumentoTransaccionSvc.API_PREPARE_ASYNC);
		_serviceFilter.setEstado(SharedConstants.STATE_ACTIVE);
		return consultaUnica(_serviceFilter);
	}
}