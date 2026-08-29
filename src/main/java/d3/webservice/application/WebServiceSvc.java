package d3.webservice.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.document.application.field.Propiedades;
import d3.shared.application.D3Utils;
import d3.shared.application.BasicSvc;
import d3.property.application.PropertyGetWithCacheService;
import d3.property.application.PropiedadSvc;
import d3.property.domain.PropiedadDTO;
import d3.property.domain.PropiedadValorDefinidoDTO;
import d3.webservice.domain.WebServiceDTO;
import d3.webservice.domain.WebServiceFilterDTO;
import d3.webservice.infrastructure.WebServiceMapper;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import d3.authentication.application.UsuarioSesionSvc;

@Service("webServiceService")
public class WebServiceSvc extends BasicSvc<WebServiceDTO, WebServiceFilterDTO> {

	private final WebServiceMapper webServiceMapper;
	private final PropiedadSvc paramService;
	private final PropertyGetWithCacheService cacheService;

	public WebServiceSvc(@Lazy UsuarioSesionSvc usuarioSesionService, @Lazy WebServiceMapper webServiceMapper,
			@Lazy PropiedadSvc paramService, @Lazy PropertyGetWithCacheService cacheService) {
		super(usuarioSesionService);
		this.webServiceMapper = webServiceMapper;
		this.paramService = paramService;
		this.cacheService = cacheService;
	}

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
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public WebServiceDTO actualizar(WebServiceDTO dto, String token) throws ServerException {
		paramService.actualizarValorPropiedad(dto.getLlaveTabla(), dto.getNombre());
		dto.setCodigo(D3Utils.formatFunction(dto.getCodigo()).toUpperCase());
		return super.actualizar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public WebServiceDTO guardar(WebServiceDTO dto, String token) throws ServerException {
		dto.setCodigo(D3Utils.formatFunction(dto.getCodigo()).toUpperCase());
		return super.guardar(dto, token);
	}

	public List<WebServiceDTO> getFullToSynchronize(List<String> process) throws ServerException {
		if (process == null || process.isEmpty()) {
			WebServiceFilterDTO filter = new WebServiceFilterDTO();
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
		if (pToken != null)
			userId = getUserFlex(pToken);
		_service.setPropiedades(cacheService.obtenerPropiedades(PropiedadValorDefinidoDTO.API_SERVICE,
				_service.getLlaveTabla(), null, userId));
		List<PropiedadDTO> properties = Propiedades.obtenerVariosParametro(_service, Propiedades.API_BASE);
		if (properties == null || properties.isEmpty())
			return _service;
		for (PropiedadDTO iProp : properties) {
			// esta parte se puede centralizar para evitar referencias circulares
			WebServiceDTO baseService = consultaXId(iProp.getValor());
			if (baseService == null)
				throw new ServerException("El id del servicio no se encuentra en la BD." + iProp.getValor());
			if (baseService.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
				throw new ServerException(
						"El servicio " + _service.getNombre() + " no se encuentra Activo." + iProp.getValor());
			// Obtengo propiedades del servicio
			_service.getPropiedades().addAll(cacheService.obtenerPropiedades(PropiedadValorDefinidoDTO.API_SERVICE,
					iProp.getValor(), null, userId));
		}
		return _service;
	}

}