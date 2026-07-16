package com.softure.massiveload.application;

import org.springframework.stereotype.Service;

import com.shared.application.SharedCRUDService;
import com.shared.domain.ServerException;
import com.softure.massiveload.domain.MassiveItemDTO;
import com.softure.massiveload.domain.MassiveItemFilter;
import com.softure.massiveload.infrastructure.MassiveItemMapper;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;

@Service("cargaMasivaItemService")
public class MassiveCRUDItemService extends SharedCRUDService<MassiveItemDTO, MassiveItemFilter> {

	private final MassiveItemMapper cargaMasivaItemMapper;

	public MassiveCRUDItemService(@Lazy MassiveItemMapper cargaMasivaItemMapper) {
		this.cargaMasivaItemMapper = cargaMasivaItemMapper;
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = cargaMasivaItemMapper;
	}

	@Override
	public MassiveItemDTO findById(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. CargaMasivaItem");
		MassiveItemFilter dto = new MassiveItemFilter();
		dto.setKey(llave);
		return cargaMasivaItemMapper.selectOne(dto);
	}

}
