package com.softure.tariff.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.tariff.application.base.TarifarioService;
import com.softure.tariff.domain.TarifarioDTO;
import com.softure.tariff.domain.TarifarioFilterDTO;

@Service("tariffGetByDocumentService")
public class TariffGetByDocumentService {

	@Autowired
	private TarifarioService tariffService;
	
	public TarifarioDTO call(String documentId) throws ServerException {
		if (documentId == null)
			throw new ServerException("Se debe seleccionar un tarifario");
		TarifarioFilterDTO tariffFilter = new TarifarioFilterDTO();
		tariffFilter.setDocumento(documentId);
		TarifarioDTO tariffDTO = tariffService.getOne(tariffFilter);
		if (tariffDTO == null)
			throw new ServerException("El tarifario no existe con ese identificador");
		return tariffDTO;
	}
}
