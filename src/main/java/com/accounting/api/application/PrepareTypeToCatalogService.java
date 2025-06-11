package com.accounting.api.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.plan.application.base.TypeService;
import com.accounting.plan.domain.AccountConst;
import com.accounting.plan.domain.TypeDTO;
import com.accounting.plan.domain.TypeFilterDTO;
import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.shared.domain.SharedToken;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.webservice.application.WebServiceSvc;
import com.softure.webservice.domain.WebServiceDTO;

@Service
public class PrepareTypeToCatalogService {

	
	@Autowired
	@Lazy
	private WebServiceSvc webServiceSvc;
	
	@Autowired @Lazy 
	private TypeService typeService;

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public TypeDTO call(String pServiceId, SharedToken pToken) throws ServerException {

		if(pServiceId == null)
			throw new ServerException("El servicio no puede ser nulo");
		
		TypeFilterDTO _typeFilter = new TypeFilterDTO();
		_typeFilter.setService(pServiceId);
		
		TypeDTO _type = typeService.getOne(_typeFilter);
		if (_type == null) {
			WebServiceDTO ws = webServiceSvc.getByIdFullProperties(pServiceId, pToken.getToken());
			if(Propiedades.obtenerParametro(ws,Propiedades.API_ACCOUNT_CATALOG) == null)
				throw new ServerException("No se encontro un tipo de comprobante con ese identificador y el api no tiene un catalogo para crear el tipo");
			_type = new TypeDTO();
			_type.setService(pServiceId);
			_type.setName(ws.getNombre());
			_type.setCode(ws.getCodigo());
			_type.setPattern(AccountConst.TYPE_PATTERN_COMPROBANTE);
			_type.setCatalog(Propiedades.obtenerValor(ws,Propiedades.API_ACCOUNT_CATALOG));
			typeService.save(_type);
			//Esto evita un null ya que el guardar no me vuelve a colocar el activo
			_type.setState(SharedConstants.STATE_ACTIVE);
		}
		
		if(_type.getState().compareTo(SharedConstants.STATE_ACTIVE)!=0)
			throw new ServerException("El tipo de comprobante no se encuentra activo");
		
		return _type;

	}



}
