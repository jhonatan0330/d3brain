package com.accounting.plan.application.base;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.binding.BindingException;
import org.springframework.stereotype.Service;

import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.CatalogFilterDTO;
import com.accounting.plan.infrastructure.CatalogMapper;
import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import org.springframework.context.annotation.Lazy;

@Service("CatalogAccountingService")
public class CatalogService {

	private final CatalogMapper mapper;

	public CatalogService(@Lazy CatalogMapper mapper) {
		this.mapper = mapper;
	}

	public CatalogDTO getById(String id) throws ServerException {
		if (id == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Catalog");
		CatalogFilterDTO dto = new CatalogFilterDTO();
		dto.setKey(id);
		return mapper.getOne(dto);
	}

	public CatalogDTO getOne(CatalogFilterDTO dto) throws ServerException {
		try {
			return mapper.getOne(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public List<CatalogDTO> getMany(CatalogFilterDTO dto) throws ServerException {
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

	public int count(CatalogFilterDTO dto) throws ServerException {
		try {
			return mapper.count(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public void save(CatalogDTO dto) throws ServerException {
		dto.setKey(UUID.randomUUID().toString().replaceAll("-", ""));
		try {
			mapper.insert(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public void update(CatalogDTO dto) throws ServerException {
		try {
			mapper.update(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public CatalogDTO delete(String id) throws ServerException {
		CatalogDTO dto = getById(id);
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