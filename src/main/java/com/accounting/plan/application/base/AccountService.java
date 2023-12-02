package com.accounting.plan.application.base;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.AccountFilterDTO;
import com.accounting.plan.infrastructure.AccountMapper;
import com.shared.domain.ServerException;
import com.softure.java.cons.ConstantesGenerales;

@Service("AccountAccountingService")
public class AccountService {

	@Autowired
	private AccountMapper mapper;

	public AccountDTO getById(String id) throws ServerException {
		if (id == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Account");
		AccountFilterDTO dto = new AccountFilterDTO();
		dto.setKey(id);
		return mapper.getOne(dto);
	}

	public AccountDTO getOne(AccountFilterDTO dto) throws ServerException {
		try {
			return mapper.getOne(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public List<AccountDTO> getMany(AccountFilterDTO dto) throws ServerException {
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

	public int count(AccountFilterDTO dto) throws ServerException {
		try {
			return mapper.count(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public void save(AccountDTO dto, String token) throws ServerException {
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
	}

	public void update(AccountDTO dto, String token) throws ServerException {
		if (token == null)
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		try {
			mapper.update(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public AccountDTO delete(AccountDTO dto, String token) throws ServerException {
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