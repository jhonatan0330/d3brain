package com.accounting.voucher.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.api.domain.VoucherPrepareRequest;
import com.accounting.plan.application.base.TypeService;
import com.accounting.plan.domain.AccountConst;
import com.accounting.plan.domain.TypeDTO;
import com.accounting.plan.domain.TypeFilterDTO;
import com.accounting.voucher.application.base.VoucherService;
import com.accounting.voucher.domain.VoucherFilterDTO;
import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.shared.domain.SharedIdResponse;
import com.shared.domain.SharedToken;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_transaction.application.DocumentoTransaccionSvc;
import com.softure.webservice.application.WebServiceEjecucionSvc;
import com.softure.webservice.application.WebServiceExecuteAPI;
import com.softure.webservice.application.WebServiceSvc;
import com.softure.webservice.domain.WebServiceDTO;
import com.softure.webservice.domain.WebServiceEjecucionDTO;
import com.softure.webservice.domain.WebServiceEjecucionFilterDTO;

@Service
public class VoucherReCreateService {

	@Autowired
	@Lazy
	private PedidoVentaSvc pedidoVentaService;
	@Autowired
	@Lazy
	private WebServiceExecuteAPI apiService;
	@Autowired
	@Lazy
	private VoucherService voucherService;
	@Autowired
	@Lazy
	private WebServiceSvc webServiceSvc;
	@Autowired
	@Lazy
	private WebServiceEjecucionSvc webServiceEjecucionSvc;
	@Autowired @Lazy 
	private TypeService typeService;

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SharedIdResponse call(VoucherPrepareRequest pItem, SharedToken pToken) throws ServerException {

		if(pItem.getServiceId() == null)
			throw new ServerException("El servicio no puede ser nulo");
		
		TypeFilterDTO _typeFilter = new TypeFilterDTO();
		_typeFilter.setService(pItem.getServiceId());
		
		TypeDTO type = typeService.getOne(_typeFilter);
		if (type == null) {
			WebServiceDTO ws = webServiceSvc.getByIdFullProperties(pItem.getServiceId(), pToken.getToken());
			if(Propiedades.obtenerParametro(ws,Propiedades.API_ACCOUNT_CATALOG) == null) 	throw new ServerException("No se encontro un tipo de comprobante con ese identificador y el api no tiene un catalogo para crear el tipo");
			TypeDTO _type = new TypeDTO();
			_type.setService(pItem.getServiceId());
			_type.setName(ws.getNombre());
			_type.setCode(ws.getCodigo());
			_type.setPattern(AccountConst.TYPE_PATTERN_COMPROBANTE);
			_type.setCatalog(Propiedades.obtenerValor(ws,Propiedades.API_ACCOUNT_CATALOG));
			typeService.save(_type);
			type = _type;
			//Esto evita un null ya que el guardar no me vuelve a colocar el activo
			type.setState(SharedConstants.STATE_ACTIVE);
		}
		
		if(type.getState().compareTo(SharedConstants.STATE_ACTIVE)!=0)
			throw new ServerException("El tipo de comprobante no se encuentra activo");
			
		VoucherFilterDTO _filter = new VoucherFilterDTO();
		_filter.setDocument(pItem.getDocumentId());
		_filter.setType(type.getKey());
		_filter.setState(SharedConstants.STATE_ACTIVE);
		if (voucherService.count(_filter) != 0)
			throw new ServerException("Este documento ya tiene un comprobante");

		WebServiceEjecucionFilterDTO _serviceFilter = new WebServiceEjecucionFilterDTO();
		_serviceFilter.setServicio(pItem.getServiceId());
		_serviceFilter.setDocumento(pItem.getDocumentId());
		_serviceFilter.setSincrona(DocumentoTransaccionSvc.API_PREPARE_ASYNC);
		_serviceFilter.setEstado(SharedConstants.STATE_ACTIVE);
		WebServiceEjecucionDTO _service = webServiceEjecucionSvc.consultaUnica(_serviceFilter);
		if (_service != null)
			return new SharedIdResponse(pItem.getDocumentId(), null, null,
					apiService.applyScheduleToExecute(_service, pToken.getToken()));

		PedidoVentaDTO _document = pedidoVentaService.consultaXId(pItem.getDocumentId());
		return new SharedIdResponse(pItem.getDocumentId(), _document.getNombre(), _document.getEstadoNombre(),
				apiService.prepareApiToExecution(pItem.getServiceId(), _document, null, null, pToken.getToken(), null));

	}

}
