package com.accounting.voucher.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedIdResponse;
import com.shared.domain.SharedToken;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.webservice.application.WebServiceExecuteAPI;

@Service
public class VoucherReCreateService {

	@Autowired @Lazy
	private PedidoVentaSvc pedidoVentaService;
	@Autowired @Lazy
	private PropiedadSvc propertyService;
	@Autowired @Lazy 
	private WebServiceExecuteAPI apiService;
	
	
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<SharedIdResponse> call(String _documentKey, SharedToken token) throws ServerException {
		ArrayList<SharedIdResponse> responses = new ArrayList<>();
		PedidoVentaDTO pedidoVenta = pedidoVentaService.consultaCompleta(_documentKey, token.getToken());
		if(pedidoVenta == null) throw new ServerException("No se encontró el documento");
		List<PropiedadDTO> apis = propertyService.obtenerPropiedades(PropiedadValorDefinidoDTO.PLANTILLA, pedidoVenta.getPlantilla(), Propiedades.TEMPLATE_VOUCHER, token.getUser());		
		if (apis != null && !apis.isEmpty()) {
			for (PropiedadDTO api : apis) {
				responses.add(new SharedIdResponse(pedidoVenta.getLlaveTabla(), pedidoVenta.getNombre()
						,apiService.prepareApiToExecution(api.getValor(), pedidoVenta, pedidoVenta, token.getToken(), null)));
			}
		}else {
			throw new ServerException("No se encontraron APIs para la generación de comprobantes");
		}
		return responses;
	}



}
