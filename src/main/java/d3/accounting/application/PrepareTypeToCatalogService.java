package d3.accounting.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.accounting.application.base.TypeService;
import d3.accounting.domain.AccountConst;
import d3.accounting.domain.TypeDTO;
import d3.accounting.domain.TypeFilterDTO;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.shared.domain.SharedToken;
import d3.document.application.field.Propiedades;
import d3.webservice.application.WebServiceSvc;
import d3.webservice.domain.WebServiceDTO;
import org.springframework.context.annotation.Lazy;

@Service
public class PrepareTypeToCatalogService {

	private final WebServiceSvc webServiceSvc;
	private final TypeService typeService;

	public PrepareTypeToCatalogService(@Lazy WebServiceSvc webServiceSvc, @Lazy TypeService typeService) {
		this.webServiceSvc = webServiceSvc;
		this.typeService = typeService;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public TypeDTO call(String pServiceId, String pCatalogId, SharedToken pToken) throws ServerException {

		// Aqui creo un type por defecto para la parametrizacion en la organizacion
		if (pServiceId == null) {
			if (pCatalogId != null) {
				TypeFilterDTO _typeDefaultFilter = new TypeFilterDTO();
				_typeDefaultFilter.setCatalog(pCatalogId);
				_typeDefaultFilter.setState(SharedConstants.STATE_ACTIVE);
				_typeDefaultFilter.setPattern(AccountConst.TYPE_PATTERN_INDICATOR);
				TypeDTO _default = typeService.getOne(_typeDefaultFilter);
				if (_default == null)
					throw new ServerException("El catalogo no tiene un tipo por defecto que elpatron sea indicador");
				return _default;
			} else {
				throw new ServerException("El servicio no puede ser nulo");
			}
		}

		TypeFilterDTO _typeFilter = new TypeFilterDTO();
		_typeFilter.setService(pServiceId);

		TypeDTO _type = typeService.getOne(_typeFilter);
		if (_type == null) {
			WebServiceDTO ws = webServiceSvc.getByIdFullProperties(pServiceId, pToken.getToken());
			if (Propiedades.obtenerParametro(ws, Propiedades.API_ACCOUNT_CATALOG) == null)
				throw new ServerException(
						"No se encontro un tipo de comprobante con ese identificador y el api no tiene un catalogo para crear el tipo");
			_type = new TypeDTO();
			_type.setService(pServiceId);
			_type.setName(ws.getNombre());
			_type.setCode(ws.getCodigo());
			_type.setPattern(AccountConst.TYPE_PATTERN_COMPROBANTE);
			_type.setCatalog(Propiedades.obtenerValor(ws, Propiedades.API_ACCOUNT_CATALOG));
			typeService.save(_type);
			// Esto evita un null ya que el guardar no me vuelve a colocar el activo
			_type.setState(SharedConstants.STATE_ACTIVE);
		}

		if (_type.getState().compareTo(SharedConstants.STATE_ACTIVE) != 0)
			throw new ServerException("El tipo de comprobante no se encuentra activo");

		return _type;

	}

}
