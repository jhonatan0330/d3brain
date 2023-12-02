package com.softure.massiveload.application;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.application.SharedCRUDService;
import com.softure.java.dto.exception.ServerException;
import com.softure.massiveload.domain.MassiveItemDTO;
import com.softure.massiveload.domain.MassiveItemFilter;
import com.softure.massiveload.infrastructure.MassiveItemMapper;

@Service("cargaMasivaItemService")
public class MassiveCRUDItemService extends SharedCRUDService<MassiveItemDTO, MassiveItemFilter> {
	
	@Autowired
	private MassiveItemMapper cargaMasivaItemMapper;

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = cargaMasivaItemMapper;
	}
	
	@Override
	public MassiveItemDTO findById(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. CargaMasivaItem");
		MassiveItemFilter dto = new MassiveItemFilter();
		dto.setId(llave);
		return cargaMasivaItemMapper.selectOne(dto);
	}
	
}
