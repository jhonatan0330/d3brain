package com.softure.webservice.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.property.application.PropertyGetWithCacheService;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.webservice.domain.WebServiceDTO;
import com.softure.webservice.domain.WebServiceFilterDTO;
import com.softure.webservice.infrastructure.WebServiceMapper;

import jakarta.annotation.PostConstruct;

@Service("webServiceService")
public class WebServiceSvc extends BasicSvc<WebServiceDTO, WebServiceFilterDTO> {

	@Autowired @Lazy 
	private WebServiceMapper webServiceMapper;
	@Autowired @Lazy 
	private PropiedadSvc paramService;
	@Autowired @Lazy 
	private PropertyGetWithCacheService cacheService;

	
	@Override
	public WebServiceDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. WebService");
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
		return super.activar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public WebServiceDTO actualizar(WebServiceDTO dto, String token) throws ServerException {
		paramService.actualizarValorPropiedad(dto.getLlaveTabla(), dto.getNombre());
		dto.setCodigo(SoftureUtil.formatFunction(dto.getCodigo()).toUpperCase());
		return super.actualizar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
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
	public List<WebServiceDTO> listarConsulta(WebServiceFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public WebServiceDTO guardar(WebServiceDTO dto, String token) throws ServerException {
		dto.setCodigo(SoftureUtil.formatFunction(dto.getCodigo()).toUpperCase());
		return super.guardar(dto, token);
	}

	public List<WebServiceDTO> getFullToSynchronize(List<String> process) throws ServerException {
		if(process ==null || process.isEmpty()) {
			WebServiceFilterDTO filter  = new WebServiceFilterDTO();
			filter.setEstado(SharedConstants.STATE_ACTIVE);
			return listarConsulta(filter);
		} 
		return webServiceMapper.getFullToSynchronize(process);
	}
	
	public WebServiceDTO getByIdFullProperties(String pKey, String pToken) throws ServerException {
		WebServiceDTO _service = consultaXId(pKey);
		if (_service == null)
			throw new ServerException("El id del servicio no se encuentra en la BD." + pKey);
		if (_service.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
			throw new ServerException("El servicio " + _service.getNombre() + " no se encuentra Activo." + pKey);
		String userId = null; 
		if(pToken != null) userId = getUserFlex(pToken);
		_service.setPropiedades(
				cacheService.obtenerPropiedades( PropiedadValorDefinidoDTO.API_SERVICE, _service.getLlaveTabla(), null, userId));
		List<PropiedadDTO> properties = Propiedades.obtenerVariosParametro(_service, Propiedades.API_BASE);
		if (properties == null || properties.isEmpty()) return _service; 
		for (PropiedadDTO iProp : properties) {
			//esta parte se puede centralizar para evitar referencias circulares
			WebServiceDTO baseService = consultaXId(iProp.getValor());
			if (baseService == null)
				throw new ServerException("El id del servicio no se encuentra en la BD." + iProp.getValor());
			if (baseService.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
				throw new ServerException("El servicio " + _service.getNombre() + " no se encuentra Activo." + iProp.getValor());
			// Obtengo propiedades del servicio
			_service.getPropiedades().addAll(
					cacheService.obtenerPropiedades( PropiedadValorDefinidoDTO.API_SERVICE, iProp.getValor(), null, userId));
		}
		return _service;
	}
	
	

	public WebServiceDTO createVocherTemplate(String pToken, String pName, String pCode) throws ServerException {
		WebServiceDTO ws = new WebServiceDTO();
		ws.setCodigo("CC_" + pCode);
		ws.setNombre("CONT " + pName);
		ws = guardar(ws, pToken);
		
		paramService.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.API_SERVICE, ws.getLlaveTabla(),
				Propiedades.API_BASE, "CONT_A", pToken), pToken);
		paramService.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.API_SERVICE, ws.getLlaveTabla(),
				Propiedades.API_URL, "http://localhost:8080/api_account/voucher", pToken), pToken);
		paramService.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.API_SERVICE, ws.getLlaveTabla(),
				Propiedades.API_TEMPLATE, "{\r\n"
						+ "	\"catalog\": \"{{R_CATALOGO_ACTUAL}}\",\r\n"
						+ "	\"concept\": \"DOCUMENTO {{E_CODE}}\",\r\n"
						+ "	\"factDate\": \"{{E_CODE_FECHA}}\",\r\n"
						+ "	\"document\": \"{{E_CODE_ID}}\",\r\n"
						+ "	\"type\": \"{{E_API_ID}}\",\r\n"
						+ "	\"value\": \"${R_BUSCA_EL_VALOR}\",\r\n"
						+ "	\"lines\": [\r\n"
						+ "		{\r\n"
						+ "			\"account\": \"\",\r\n"
						+ "			\"debit O credit\": \"\",\r\n"
						+ "			\"note\": \"\",\r\n"
						+ "			\"references\": [\r\n"
						+ "				{\r\n"
						+ "					\"auxiliar\": \"\",\r\n"
						+ "					\"code\": \"\",\r\n"
						+ "					\"documentId\": \"\",\r\n"
						+ "					\"name\": \"\"\r\n"
						+ "				}\r\n"
						+ "			]\r\n"
						+ "		}\r\n"
						+ "	]\r\n"
						+ "}", pToken), pToken);
		return ws;
	}


}