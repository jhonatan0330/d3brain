package com.softure.massiveload.application;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.java.dto.exception.ServerException;
import com.softure.massiveload.domain.MassiveMasterDTO;
import com.softure.massiveload.domain.MassiveMasterFilter;
import com.softure.massiveload.infrastructure.MassiveMasterMapper;
import com.softure.shared.application.SharedCRUDService;

@Service("CargaMasivaService")
public class MassiveCRUDMasterService extends SharedCRUDService<MassiveMasterDTO, MassiveMasterFilter> {

	@Autowired
	private MassiveMasterMapper taskMapper;

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = taskMapper;
	}

	@Override
	public MassiveMasterDTO findById(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Task");
		MassiveMasterFilter dto = new MassiveMasterFilter();
		dto.setId(llave);
		return taskMapper.selectOne(dto);
	}

}