package d3.accounting.application;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.accounting.domain.VoucherRangeRequest;
import d3.accounting.infrastructure.VoucherExtendMapper;
import d3.configuration.application.PropertyGetWithCacheService;
import d3.configuration.domain.PropiedadDTO;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedIdResponse;
import d3.shared.domain.SharedToken;
import d3.document.application.field.Propiedades;
import d3.document.domain.PedidoVentaDTO;
import d3.webservice.application.WebServiceExecuteAPI;

@Service
public class VoucherRangeService {

	private final VoucherDeleteService deleteService;
	private final WebServiceExecuteAPI apiService;
	private final PropertyGetWithCacheService cacheService;
	private final VoucherExtendMapper voucherExtendMapper;

	public VoucherRangeService(@Lazy VoucherDeleteService deleteService, @Lazy WebServiceExecuteAPI apiService,
			@Lazy PropertyGetWithCacheService cacheService, @Lazy VoucherExtendMapper voucherExtendMapper) {
		this.deleteService = deleteService;
		this.apiService = apiService;
		this.cacheService = cacheService;
		this.voucherExtendMapper = voucherExtendMapper;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SharedIdResponse clear(VoucherRangeRequest pItem, SharedToken pToken) throws ServerException {

		List<PedidoVentaDTO> _documents = voucherExtendMapper.itemsToDeleteVoucher(pItem.getTemplateId(),
				pItem.getStartDate(), pItem.getEndDate());

		if (_documents == null)
			return null;

		for (PedidoVentaDTO _iDocument : _documents) {
			deleteService.callByDocument(_iDocument.getLlaveTabla(), _iDocument.getPlantilla(), pToken.getToken());
		}
		return new SharedIdResponse(pItem.getTemplateId());
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SharedIdResponse create(VoucherRangeRequest pItem, SharedToken pToken) throws ServerException {

		// Mejorar la consulta, mejorar la consultar del servicio
		List<PropiedadDTO> _prop = cacheService.getByValueWithoutField(PropiedadValorDefinidoDTO.API_SERVICE,
				Propiedades.TEMPLATE_VOUCHER, pItem.getTemplateId(), null);
		if (_prop == null || _prop.isEmpty())
			throw new ServerException("La plantilla no tiene una referencia de TEMPLATE VOUCHER");

		List<PedidoVentaDTO> _documents = voucherExtendMapper.itemsToRecreateVoucher(pItem.getTemplateId(),
				pItem.getStartDate(), pItem.getEndDate());

		if (_documents == null)
			return null;
		for (PropiedadDTO _iProp : _prop) {
			for (PedidoVentaDTO _iDocument : _documents) {
				apiService.programateExecution(_iProp.getCampo(), _iDocument.getLlaveTabla(), null, null,
						pToken.getToken());
			}
		}

		return new SharedIdResponse(pItem.getTemplateId());

	}

}
