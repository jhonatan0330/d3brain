package com.accounting.voucher.application.base;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accounting.voucher.domain.AccountRecordDTO;
import com.accounting.voucher.domain.AccountRecordFilterDTO;
import com.accounting.voucher.infrastructure.AccountRecordMapper;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;

@Service("AccountRecordAccountingService")
public class AccountRecordService {

	@Autowired
	private AccountRecordMapper mapper;

	public AccountRecordDTO getById(String id) throws ServerException {
		if (id == null)
			throw new ServerException("La llave del DTO se encuentra vacia. AccountRecord");
		AccountRecordFilterDTO dto = new AccountRecordFilterDTO();
		dto.setKey(id);
		return mapper.getOne(dto);
	}

	public AccountRecordDTO getOne(AccountRecordFilterDTO dto) throws ServerException {
		try {
			return mapper.getOne(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public List<AccountRecordDTO> getMany(AccountRecordFilterDTO dto) throws ServerException {
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

	public int count(AccountRecordFilterDTO dto) throws ServerException {
		try {
			return mapper.count(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public void save(AccountRecordDTO dto, String token) throws ServerException {
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

	public void update(AccountRecordDTO dto, String token) throws ServerException {
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

	public AccountRecordDTO delete(AccountRecordDTO dto, String token) throws ServerException {
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