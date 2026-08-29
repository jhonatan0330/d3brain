package d3.webservice.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.domain.SharedConstants;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedIdResponse;
import d3.configuration.application.PropertyGetWithCacheService;
import d3.configuration.application.PropiedadSvc;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.webservice.domain.WebServiceDTO;
import org.springframework.context.annotation.Lazy;

@Component
public class WebServiceCopyAPI {

	private final WebServiceSvc webServiceSvc;
	private final PropiedadSvc propiedadesSvc;
	private final PropertyGetWithCacheService cacheService;

	public WebServiceCopyAPI(@Lazy WebServiceSvc webServiceSvc, @Lazy PropiedadSvc propiedadesSvc,
			@Lazy PropertyGetWithCacheService cacheService) {
		this.webServiceSvc = webServiceSvc;
		this.propiedadesSvc = propiedadesSvc;
		this.cacheService = cacheService;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SharedIdResponse call(String serviceId, String token) throws ServerException {

		WebServiceDTO service = webServiceSvc.consultaXId(serviceId);
		if (service == null)
			throw new ServerException("El id del servicio no se encuentra en la BD." + serviceId);
		if (service.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
			throw new ServerException("El servicio " + service.getNombre() + " no se encuentra Activo." + serviceId);
		// Obtengo propiedades del servicio
		String userId = webServiceSvc.getUserFlex(token);
		service.setPropiedades(
				cacheService.obtenerPropiedades(PropiedadValorDefinidoDTO.API_SERVICE, serviceId, null, userId));

		WebServiceDTO newAPi = new WebServiceDTO();
		newAPi.setCodigo(service.getCodigo() + "COPY");
		newAPi.setNombre(service.getNombre() + "COPY");
		newAPi.setProceso(service.getProceso());

		newAPi = webServiceSvc.save(newAPi);
		if (service.getPropiedades() == null)
			return new SharedIdResponse(newAPi.getLlaveTabla());

		newAPi.setPropiedades(
				propiedadesSvc.copiarPropiedades(service.getPropiedades(), newAPi.getLlaveTabla(), token));

		return new SharedIdResponse(newAPi.getLlaveTabla());
	}

}
