package com.softure.webservice.application;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.authentication.application.UsuarioAutenticacionSvc;
import com.softure.authentication.domain.UsuarioSesionDTO;
import com.softure.document_transaction.application.DocumentoTransaccionSvc;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.webservice.domain.WebServiceDTO;
import com.softure.webservice.domain.WebServiceEjecucionDTO;
import com.softure.webservice.domain.WebServiceEjecucionFilterDTO;
import com.softure.webservice.infrastructure.WebServiceEjecucionMapper;

import jakarta.annotation.PostConstruct;

@Service("webServiceEjecucionService")
public class WebServiceEjecucionSvc extends BasicSvc<WebServiceEjecucionDTO, WebServiceEjecucionFilterDTO> {
	
	@Autowired @Lazy 
	private WebServiceEjecucionMapper webServiceEjecucionMapper;
	
	@Autowired @Lazy  private UsuarioAutenticacionSvc autenticacionService;
	@Autowired @Lazy  private WebServiceExecuteAPI executeAPIFunction;
	@Autowired @Lazy  private WebServiceSvc webServiceSvc;

	@Override
	public WebServiceEjecucionDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. WebServiceEjecucion");
		WebServiceEjecucionFilterDTO dto = new WebServiceEjecucionFilterDTO();
		dto.setLlaveTabla(llave);
		return webServiceEjecucionMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = webServiceEjecucionMapper;
	}
	
	@Override
	public WebServiceEjecucionDTO activar(WebServiceEjecucionDTO dto, String token) throws ServerException {
		return super.activar(dto, token);
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public WebServiceEjecucionDTO actualizar( WebServiceEjecucionDTO dto, String token) throws ServerException {
		return super.actualizar(dto, token);
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public WebServiceEjecucionDTO inactivar(WebServiceEjecucionDTO dto, String token) throws ServerException {
		return super.inactivar(dto, token);
	}
	
	@Override
	public WebServiceEjecucionDTO consultaUnica(WebServiceEjecucionFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(WebServiceEjecucionFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<WebServiceEjecucionDTO> listarConsulta(WebServiceEjecucionFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	public WebServiceEjecucionDTO ejecutarAPI(WebServiceEjecucionFilterDTO dto)throws ServerException{
		WebServiceEjecucionDTO bd = consultaXId(dto.getLlaveTabla());
		if(bd.getFechaEjecucion()!=null) throw new ServerException("Este API ya fue ejecutado");
		if(bd.getSincrona()==null) throw new ServerException("Este API no es asincrono");
		if(bd.getSincrona().compareTo(DocumentoTransaccionSvc.API_PREPARE_ASYNC)==0) {
			executeAPIFunction.applyScheduleToExecute(consultaXId(dto.getLlaveTabla()), dto.getSecurityToken());
		} else {
			WebServiceDTO service = webServiceSvc.consultaXId(bd.getServicio());
			executeAPIFunction.executeApi(service, bd, dto.getSecurityToken(), null, null, null);	
		}
		
		return consultaXId(dto.getLlaveTabla());
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public WebServiceEjecucionDTO guardar(WebServiceEjecucionDTO dto, String token) throws ServerException {
		return super.guardar(dto, token);
	}

	public String apiToTransaction() throws ServerException {
		List<WebServiceEjecucionDTO> tareasPendientes = webServiceEjecucionMapper.apisTransaccion();
		
	 	if(tareasPendientes==null || tareasPendientes.isEmpty())
	 		return "*******APIS ASYNC (0) ****" + new Date().toString();
	 	UsuarioSesionDTO sessionAdmin = autenticacionService.generateAdministratorToken();
 		for (WebServiceEjecucionDTO iMessage : tareasPendientes) {
 			if(iMessage.getSincrona().compareTo(DocumentoTransaccionSvc.API_PREPARE_ASYNC)==0) {
 				executeAPIFunction.applyScheduleToExecute(iMessage, sessionAdmin.getLlaveTabla());
 			} else {
 				
 				WebServiceDTO service = webServiceSvc.consultaXId(iMessage.getServicio());
 				if (service == null)
 					throw new ServerException("El id del servicio no se encuentra en la BD.");
 				executeAPIFunction.executeApi(service, iMessage, sessionAdmin.getLlaveTabla(), null, null, null);
 			}
		}
	 	return "*******APIS ASYNC ("+tareasPendientes.size()+") ****" + new Date().toString();
	}
	
	public boolean hasPropertiesAsync() {
		return webServiceEjecucionMapper.hasPropertiesAsync()!=0;
	}

	public WebServiceEjecucionDTO getServiceVoucherActive(String pServiceId, String pDocumentId) throws ServerException {
		WebServiceEjecucionFilterDTO _serviceFilter = new WebServiceEjecucionFilterDTO();
		_serviceFilter.setServicio(pServiceId);
		_serviceFilter.setDocumento(pDocumentId);
		_serviceFilter.setSincrona(DocumentoTransaccionSvc.API_PREPARE_ASYNC);
		_serviceFilter.setEstado(SharedConstants.STATE_ACTIVE);
		return consultaUnica(_serviceFilter);
	}
}