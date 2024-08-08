package com.softure.massiveload.application;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.shared.application.SharedCRUDService;
import com.shared.domain.ServerException;
import com.softure.massiveload.domain.MassiveMasterDTO;
import com.softure.massiveload.domain.MassiveMasterFilter;
import com.softure.massiveload.infrastructure.MassiveMasterMapper;

import jakarta.annotation.PostConstruct;

@Service("CargaMasivaService")
public class MassiveCRUDMasterService extends SharedCRUDService<MassiveMasterDTO, MassiveMasterFilter> {

	@Autowired @Lazy 
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
		dto.setKey(llave);
		return taskMapper.selectOne(dto);
	}

}