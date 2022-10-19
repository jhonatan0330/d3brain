package com.softure.massiveload.application.service.adapter;

import java.util.List;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.java.application.ServiceGeneric;
import com.softure.java.dto.exception.ServerException;

import com.softure.massiveload.application.service.ICargaMasivaItemService;
import com.softure.massiveload.domain.dto.CargaMasivaItemDTO;
import com.softure.massiveload.domain.filter.CargaMasivaItemFilterDTO;
import com.softure.massiveload.domain.vo.CargaMasivaItem;
import com.softure.massiveload.infrastructure.mybatis.mapper.ICargaMasivaItemMapper;

@Service("cargaMasivaItemService")
public class CargaMasivaItemSvc extends ServiceGeneric<CargaMasivaItemDTO, CargaMasivaItemFilterDTO> implements ICargaMasivaItemService {
	
	@Autowired
	private ICargaMasivaItemMapper cargaMasivaItemMapper;

	@Override
	public CargaMasivaItem findById(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. CargaMasivaItem");
		var result = cargaMasivaItemMapper.selectById(llave);
		if(result != null) return result.toValueObject();
		return null;
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = cargaMasivaItemMapper;
	}
	
	@Override
	public List<CargaMasivaItem> find(String token, CargaMasivaItemFilterDTO filter) {
		return null;
	}

	@Override
	public CargaMasivaItem get(String token, CargaMasivaItemFilterDTO filter) throws ServerException {
		return transform(findUnique(token, filter));
	}

	@Override
	public CargaMasivaItem save(String token, CargaMasivaItem dto) throws ServerException {
		return transform(saveDB(token, dto.toModel()));
	}

	@Override
	public CargaMasivaItem update(String token, CargaMasivaItem dto, String id) throws ServerException {
		return transform(updateDB(token, dto.toModel()));
	}

	@Override
	public CargaMasivaItem activate(String token, String id) throws ServerException {
		return transform(activateDB(token, id));
	}

	@Override
	public CargaMasivaItem inactivate(String token, String id) throws ServerException {
		return transform(inactivateDB(token, id));
	}
	
	private CargaMasivaItem transform(CargaMasivaItemDTO dto) throws ServerException {
		if(dto == null) return null;
		return dto.toValueObject();
	}
}
