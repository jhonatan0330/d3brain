package com.softure.webservice.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.property.application.PropiedadSvc;
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

	public WebServiceDTO createVocherTemplate(String pToken, String pName, String pCode) throws ServerException {
		WebServiceDTO ws = new WebServiceDTO();
		ws.setCodigo(pCode);
		ws.setNombre(pName);
		ws.setTemplate("{\r\n"
				+ "	\"catalog\": \"PUC2025\",\r\n"
				+ "	\"concept\": \"\",\r\n"
				+ "	\"factDate\": \"\",\r\n"
				+ "	\"value\": \"\",\r\n"
				+ "	\"document\": \"\",\r\n"
				+ "	\"type\": \"\",\r\n"
				+ "	\"lines\": [\r\n"
				+ "			\"account\": \"\",\r\n"
				+ "			\"debit\": \"\",\r\n"
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
				+ "}");
		ws.setUrl("http://localhost:8080/api_account/voucher");
		ws = guardar(ws, pToken);
		return ws;
	}


}