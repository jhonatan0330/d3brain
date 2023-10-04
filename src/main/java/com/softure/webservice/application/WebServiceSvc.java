package com.softure.webservice.application;

import java.util.List;

// BEGIN region interImport
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.webservice.domain.WebServiceDTO;
import com.softure.webservice.domain.WebServiceFilterDTO;
import com.softure.webservice.infrastructure.WebServiceMapper;

@Service("webServiceService")
public class WebServiceSvc extends BasicSvc<WebServiceDTO, WebServiceFilterDTO> {
	
	@Autowired
	private WebServiceMapper webServiceMapper;
	
	// BEGIN region servicesWebService
	// END region servicesWebService

	@Override
	public WebServiceDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. WebService");
		WebServiceFilterDTO dto = new WebServiceFilterDTO();
		dto.setLlaveTabla(llave);
		return webServiceMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = webServiceMapper;
	}
	
	@Override
	public WebServiceDTO activar(WebServiceDTO dto, String token) throws ServerException {
		// BEGIN WebService_activar
		return super.activar(dto, token);
		// END WebService_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public WebServiceDTO actualizar( WebServiceDTO dto, String token) throws ServerException {
		// BEGIN WebService_actualizar
		return super.actualizar(dto, token);
		// END WebService_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public WebServiceDTO inactivar(WebServiceDTO dto, String token) throws ServerException {
		// BEGIN WebService_inactivar
		return super.inactivar(dto, token);
		// END WebService_inactivar
	}
	
	@Override
	public WebServiceDTO consultaUnica(WebServiceFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(WebServiceFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<WebServiceDTO> listarConsulta(WebServiceFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public WebServiceDTO guardar(WebServiceDTO dto, String token) throws ServerException {
		// BEGIN WebService_guardar
		return super.guardar(dto, token);
		// END WebService_guardar
	}

	public List<WebServiceDTO> getFullToSynchronize() {
		return webServiceMapper.getFullToSynchronize();
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}