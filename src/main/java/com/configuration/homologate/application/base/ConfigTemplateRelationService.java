package com.configuration.homologate.application.base;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.binding.BindingException;
import org.springframework.stereotype.Service;

import com.configuration.homologate.domain.ConfigTemplateRelationDTO;
import com.configuration.homologate.domain.ConfigTemplateRelationFilterDTO;
import com.configuration.homologate.infrastructure.ConfigTemplateRelationMapper;
import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import org.springframework.context.annotation.Lazy;

@Service("ConfigTemplateRelationConfigurationService")
public class ConfigTemplateRelationService {

	private final ConfigTemplateRelationMapper mapper;

	public ConfigTemplateRelationService(@Lazy ConfigTemplateRelationMapper mapper) {
		this.mapper = mapper;
	}

	public ConfigTemplateRelationDTO getById(String id) throws ServerException {
		if (id == null)
			throw new ServerException("La llave del DTO se encuentra vacia. ConfigTemplateRelation");
		ConfigTemplateRelationFilterDTO dto = new ConfigTemplateRelationFilterDTO();
		dto.setKey(id);
		return mapper.getOne(dto);
	}

	public ConfigTemplateRelationDTO getOne(ConfigTemplateRelationFilterDTO dto) throws ServerException {
		try {
			return mapper.getOne(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public List<ConfigTemplateRelationDTO> getMany(ConfigTemplateRelationFilterDTO dto) throws ServerException {
		if (dto.getStartRow() == null)
			dto.setStartRow(0);
		if (dto.getEndRow() == null || dto.getEndRow() == 0)
			dto.setEndRow(200);
		try {
			return mapper.getMany(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public int count(ConfigTemplateRelationFilterDTO dto) throws ServerException {
		try {
			return mapper.count(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public void save(ConfigTemplateRelationDTO dto) throws ServerException {
		dto.setKey(UUID.randomUUID().toString().replaceAll("-", ""));
		try {
			mapper.insert(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public void update(ConfigTemplateRelationDTO dto) throws ServerException {
		try {
			mapper.update(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public ConfigTemplateRelationDTO delete(String id) throws ServerException {
		ConfigTemplateRelationDTO dto = getById(id);
		if (dto == null)
			throw new ServerException("No se identifica el objeto a inactivar");
		if (dto.getState().compareTo(SharedConstants.STATE_INACTIVE) == 0)
			throw new ServerException("Este objeto ya se encuentra inactivo");
		dto.setState(SharedConstants.STATE_INACTIVE);
		try {
			mapper.update(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}

}