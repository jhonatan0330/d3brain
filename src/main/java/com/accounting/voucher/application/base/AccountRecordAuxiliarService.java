package com.accounting.voucher.application.base;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.binding.BindingException;
import org.springframework.stereotype.Service;

import com.accounting.voucher.domain.AccountRecordAuxiliarDTO;
import com.accounting.voucher.domain.AccountRecordAuxiliarFilterDTO;
import com.accounting.voucher.infrastructure.AccountRecordAuxiliarMapper;
import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import org.springframework.context.annotation.Lazy;

@Service("AccountRecordAuxiliarAccountingService")
public class AccountRecordAuxiliarService {

	private final AccountRecordAuxiliarMapper mapper;

	public AccountRecordAuxiliarService(@Lazy AccountRecordAuxiliarMapper mapper) {
		this.mapper = mapper;
	}

	public AccountRecordAuxiliarDTO getById(String id) throws ServerException {
		if (id == null)
			throw new ServerException("La llave del DTO se encuentra vacia. AccountRecordAuxiliar");
		AccountRecordAuxiliarFilterDTO dto = new AccountRecordAuxiliarFilterDTO();
		dto.setKey(id);
		return mapper.getOne(dto);
	}

	public AccountRecordAuxiliarDTO getOne(AccountRecordAuxiliarFilterDTO dto) throws ServerException {
		try {
			return mapper.getOne(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public List<AccountRecordAuxiliarDTO> getMany(AccountRecordAuxiliarFilterDTO dto) throws ServerException {
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

	public int count(AccountRecordAuxiliarFilterDTO dto) throws ServerException {
		try {
			return mapper.count(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public void save(AccountRecordAuxiliarDTO dto) throws ServerException {
		dto.setKey(UUID.randomUUID().toString().replaceAll("-", ""));
		try {
			mapper.insert(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public void update(AccountRecordAuxiliarDTO dto) throws ServerException {
		try {
			mapper.update(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public AccountRecordAuxiliarDTO delete(String id) throws ServerException {
		AccountRecordAuxiliarDTO dto = getById(id);
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