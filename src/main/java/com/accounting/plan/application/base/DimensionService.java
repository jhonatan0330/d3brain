package com.accounting.plan.application.base;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accounting.plan.domain.DimensionDTO;
import com.accounting.plan.domain.DimensionFilterDTO;
import com.accounting.plan.infrastructure.DimensionMapper;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;

@Service("DimensionAccountingService")
public class DimensionService {

	@Autowired
	private DimensionMapper mapper;

	public DimensionDTO getById(String id) throws ServerException {
		if (id == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Dimension");
		DimensionFilterDTO dto = new DimensionFilterDTO();
		dto.setKey(id);
		return mapper.getOne(dto);
	}

	public DimensionDTO getOne(DimensionFilterDTO dto) throws ServerException {
		try {
			return mapper.getOne(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public List<DimensionDTO> getMany(DimensionFilterDTO dto) throws ServerException {
		if (dto.getStartIndex() == null)
			dto.setStartIndex(0);
		if (dto.getEndIndex() == null || dto.getEndIndex() == 0)
			dto.setEndIndex(200);
		try {
			return mapper.getMany(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public int count(DimensionFilterDTO dto) throws ServerException {
		try {
			return mapper.count(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public DimensionDTO save(DimensionDTO dto, String token) throws ServerException {
		if (token == null)
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		dto.setKey(UUID.randomUUID().toString().replaceAll("-", ""));
		try {
			mapper.insert(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}

	public DimensionDTO update(DimensionDTO dto, String token) throws ServerException {
		if (token == null)
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		try {
			return mapper.update(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public DimensionDTO delete(DimensionDTO dto, String token) throws ServerException {
		if (token == null)
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		dto = getById(dto.getKey());
		if (dto == null)
			throw new ServerException("No se identifica el objeto a inactivar");
		if (dto.getState().compareTo(ConstantesGenerales.ESTADO_INACTIVO) == 0)
			throw new ServerException("Este objeto ya se encuentra inactivo");
		dto.setState(ConstantesGenerales.ESTADO_INACTIVO);
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