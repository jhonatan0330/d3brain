package d3.massiveload.application;

import org.springframework.stereotype.Service;

import d3.shared.application.SharedCRUDService;
import d3.shared.domain.ServerException;
import d3.massiveload.domain.MassiveMasterDTO;
import d3.massiveload.domain.MassiveMasterFilter;
import d3.massiveload.infrastructure.MassiveMasterMapper;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;

@Service("CargaMasivaService")
public class MassiveCRUDMasterService extends SharedCRUDService<MassiveMasterDTO, MassiveMasterFilter> {

	private final MassiveMasterMapper taskMapper;

	public MassiveCRUDMasterService(@Lazy MassiveMasterMapper taskMapper) {
		this.taskMapper = taskMapper;
	}

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