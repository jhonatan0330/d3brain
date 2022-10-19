package com.softure.massiveload.application.service.adapter;

import java.util.List;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.java.application.ServiceGeneric;
import com.softure.java.dto.exception.ServerException;

import com.softure.massiveload.application.service.ICargaMasivaService;
import com.softure.massiveload.domain.dto.CargaMasivaDTO;
import com.softure.massiveload.domain.filter.CargaMasivaFilterDTO;
import com.softure.massiveload.domain.vo.CargaMasiva;
import com.softure.massiveload.infrastructure.mybatis.mapper.ICargaMasivaMapper;

@Service("cargaMasivaService")
public class CargaMasivaSvc extends ServiceGeneric<CargaMasivaDTO, CargaMasivaFilterDTO> implements ICargaMasivaService {
	
	@Autowired
	private ICargaMasivaMapper cargaMasivaMapper;

	@Override
	public CargaMasiva findById(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. CargaMasiva");
		var result = cargaMasivaMapper.selectById(llave);
		if(result != null) return result.toValueObject();
		return null;
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = cargaMasivaMapper;
	}
	
	@Override
	public List<CargaMasiva> find(String token, CargaMasivaFilterDTO filter) {
		return null;
	}

	@Override
	public CargaMasiva get(String token, CargaMasivaFilterDTO filter) throws ServerException {
		return transform(findUnique(token, filter));
	}

	@Override
	public CargaMasiva save(String token, CargaMasiva dto) throws ServerException {
		return transform(saveDB(token, dto.toModel()));
	}

	@Override
	public CargaMasiva update(String token, CargaMasiva dto, String id) throws ServerException {
		return transform(updateDB(token, dto.toModel()));
	}

	@Override
	public CargaMasiva activate(String token, String id) throws ServerException {
		return transform(activateDB(token, id));
	}

	@Override
	public CargaMasiva inactivate(String token, String id) throws ServerException {
		return transform(inactivateDB(token, id));
	}
	
	private CargaMasiva transform(CargaMasivaDTO dto) throws ServerException {
		if(dto == null) return null;
		return dto.toValueObject();
	}
}
