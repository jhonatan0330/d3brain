package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import com.softure.logisticpymes.services.refactor.ExecuteAPIFunction;
import com.softure.logisticpymes.dto.UsuarioSesionDTO;
import com.softure.logisticpymes.dto.WebServiceDTO;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.WebServiceEjecucionDTO;
import com.softure.logisticpymes.dto.filter.WebServiceEjecucionFilterDTO;
import com.softure.logisticpymes.persistence.WebServiceEjecucionMapper;

@Service("webServiceEjecucionService")
public class WebServiceEjecucionSvc extends BasicSvc<WebServiceEjecucionDTO, WebServiceEjecucionFilterDTO> {
	
	@Autowired
	private WebServiceEjecucionMapper webServiceEjecucionMapper;
	
	// BEGIN region servicesWebServiceEjecucion
	@Autowired private UsuarioAutenticacionSvc autenticacionService;
	@Autowired private ExecuteAPIFunction executeAPIFunction;
	@Autowired private WebServiceSvc webServiceSvc;
	// END region servicesWebServiceEjecucion

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
		// BEGIN WebServiceEjecucion_activar
		return super.activar(dto, token);
		// END WebServiceEjecucion_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public WebServiceEjecucionDTO actualizar( WebServiceEjecucionDTO dto, String token) throws ServerException {
		// BEGIN WebServiceEjecucion_actualizar
		return super.actualizar(dto, token);
		// END WebServiceEjecucion_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public WebServiceEjecucionDTO inactivar(WebServiceEjecucionDTO dto, String token) throws ServerException {
		// BEGIN WebServiceEjecucion_inactivar
		return super.inactivar(dto, token);
		// END WebServiceEjecucion_inactivar
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
		// BEGIN region ejecutarAPI
		WebServiceEjecucionDTO bd = consultaXId(dto.getLlaveTabla());
		if(bd.getFechaEjecucion()!=null) throw new ServerException("Este API ya fue ejecutado");
		if(bd.getSincrona()==null) throw new ServerException("Este API no es asincrono");
		WebServiceDTO service = webServiceSvc.consultaXId(bd.getServicio());
		executeAPIFunction.executeApi(service, bd, dto.getSecurityToken());
		return consultaXId(dto.getLlaveTabla());
		// END region ejecutarAPI
	}

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public WebServiceEjecucionDTO guardar(WebServiceEjecucionDTO dto, String token) throws ServerException {
		// BEGIN WebServiceEjecucion_guardar
		return super.guardar(dto, token);
		// END WebServiceEjecucion_guardar
	}

// BEGIN region aditionalMethods
	public void apiToTransaction() throws ServerException {
		List<WebServiceEjecucionDTO> tareasPendientes = webServiceEjecucionMapper.apisTransaccion();
	 	if(tareasPendientes!=null && tareasPendientes.size()>0){
	 		UsuarioSesionDTO sessionAdmin = autenticacionService.generateAdministratorToken();
	 		for (WebServiceEjecucionDTO iMessage : tareasPendientes) {
	 			WebServiceDTO service = webServiceSvc.consultaXId(iMessage.getServicio());
	 			if (service == null)
	 				throw new ServerException("El id del servicio no se encuentra en la BD.");
	 			executeAPIFunction.executeApi(service, iMessage, sessionAdmin.getLlaveTabla());
			}
	 	}
	}
	
	public boolean hasPropertiesAsync() {
		return webServiceEjecucionMapper.hasPropertiesAsync()!=0;
	}
// END region aditionalMethods

}