package com.accounting.plan.application.base;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.ResultMapDTO;
import com.accounting.plan.domain.ResultMapFilterDTO;
import com.accounting.plan.infrastructure.ResultMapExtendMapper;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;

@Service("ResultMapExtendAccountingService")
public class ResultMapExtendService {

	@Autowired
	private ResultMapExtendMapper mapper;
	@Autowired
	private CatalogService catalogService;

	public ResultMapDTO getById(String id) throws ServerException {
		if (id == null)
			throw new ServerException("La llave del DTO se encuentra vacia. ResultMap");
		ResultMapFilterDTO dto = new ResultMapFilterDTO();
		dto.setKey(id);
		return mapper.getOne(dto);
	}

	public ResultMapDTO getOne(ResultMapFilterDTO dto) throws ServerException {
		try {
			return mapper.getOne(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public List<ResultMapDTO> getMany(ResultMapFilterDTO dto) throws ServerException {
		if (dto.getIndexStart() == null)
			dto.setIndexStart(0);
		if (dto.getIndexEnd() == null || dto.getIndexEnd() == 0)
			dto.setIndexEnd(200);
		try {
			return mapper.getMany(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public int count(ResultMapFilterDTO dto) throws ServerException {
		try {
			return mapper.count(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public ResultMapDTO save(ResultMapDTO dto, String token) throws ServerException {
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

	public ResultMapDTO update(ResultMapDTO dto, String token) throws ServerException {
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

	public ResultMapDTO delete(ResultMapDTO dto, String token) throws ServerException {
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
	
	public List<ResultMapDTO> getBalanceByCatalog(String catalogId) throws ServerException {
		if(catalogId ==null) throw new ServerException("Es necesario colcoar el Id del catalogo");
		CatalogDTO catalog = catalogService.getById(catalogId);
		if(catalog ==null) throw new ServerException("No se identifico un catalogo con el identificador " + catalogId);
		try {
			return mapper.getBalance(catalog.getKey(), catalog.getCode());
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

}