package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
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
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public WebServiceEjecucionDTO guardar(WebServiceEjecucionDTO dto, String token) throws ServerException {
		// BEGIN WebServiceEjecucion_guardar
		return super.guardar(dto, token);
		// END WebServiceEjecucion_guardar
	}

// BEGIN region aditionalMethods	
// END region aditionalMethods

}