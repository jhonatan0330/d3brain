package com.accounting.voucher.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accounting.plan.application.base.CatalogService;
import com.accounting.plan.domain.CatalogDTO;
import com.accounting.voucher.application.base.VoucherService;
import com.accounting.voucher.domain.VoucherDTO;
import com.accounting.voucher.domain.VoucherFilterDTO;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;

@Service
public class VoucherGetService {
	
	@Autowired
	private VoucherService voucherService;
	@Autowired
	private CatalogService catalogService;

	public List<VoucherDTO> call(String catalogId) throws ServerException {
		CatalogDTO catalog = getCatalog(catalogId);
		VoucherFilterDTO filter = new VoucherFilterDTO();
		filter.setCatalog(catalogId);
		filter.setCatalogCode(catalog.getCode());
		filter.setState(ConstantesGenerales.ESTADO_ACTIVO);
		return voucherService.getMany(filter);
	}
	
	private CatalogDTO getCatalog(String catalogId) throws ServerException {
		if (catalogId == null)
			throw new ServerException("Es importante identificar el catalogo para guardar el comprobante");
		CatalogDTO catalogDTO = catalogService.getById(catalogId);
		if (catalogDTO == null)
			throw new ServerException("No se encontro un catalogo con ese identificador");
		return catalogDTO;
	}

}
