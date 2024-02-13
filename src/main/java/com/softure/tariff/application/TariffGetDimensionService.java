package com.softure.tariff.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.tariff.domain.TarifarioDTO;
import com.softure.tariff.domain.TariffOptionDTO;
import com.softure.tariff.infrastructure.TarifaMapper;

@Service
public class TariffGetDimensionService {

	@Autowired
	private TarifaMapper tarifaMapper;

	public List<TariffOptionDTO> call(TarifarioDTO tariff, String dimension, String filter) throws ServerException {
		if(filter==null || filter.length() < 3 ) return null;
		String templatesToFilter = null;
		switch (dimension) {
		case "1": {
			if(tariff.getTipoRecurso()==null) throw new ServerException("El tarifario no tiene dimension 1");
			templatesToFilter=tariff.getTipoRecurso();
			break;
		}
		case "2": {
			if(tariff.getTipoDimension2()==null) throw new ServerException("El tarifario no tiene dimension 2");
			templatesToFilter=tariff.getTipoDimension2();
			break;
		}
		case "3": {
			if(tariff.getTipoDimension3()==null) throw new ServerException("El tarifario no tiene dimension 3");
			templatesToFilter=tariff.getTipoDimension3();
			break;
		}
		case "4": {
			if(tariff.getTipoDimension4()==null) throw new ServerException("El tarifario no tiene dimension 4");
			templatesToFilter=tariff.getTipoDimension4();
			break;
		}
		case "P": {
			if(tariff.getProductoOpcional()) throw new ServerException("El tarifario no tiene habilitado para productos");
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value to dimension tariff: " + dimension);
		}
		List<TariffOptionDTO> result = tarifaMapper.getDimentionOptions(templatesToFilter, filter.toUpperCase()); 
		return result;
	}

}
