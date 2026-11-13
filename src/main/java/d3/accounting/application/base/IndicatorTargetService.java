package d3.accounting.application.base;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.binding.BindingException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.accounting.domain.IndicatorTargetDTO;
import d3.accounting.domain.IndicatorTargetFilterDTO;
import d3.accounting.infrastructure.IndicatorTargetMapper;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;

@Service("IndicadorTargetAccountingService")
public class IndicatorTargetService {

	private final IndicatorTargetMapper mapper;

	public IndicatorTargetService(@Lazy IndicatorTargetMapper mapper) {
		this.mapper = mapper;
	}

	public IndicatorTargetDTO getById(String id) throws ServerException {
		if (id == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Indicator");
		IndicatorTargetFilterDTO dto = new IndicatorTargetFilterDTO();
		dto.setKey(id);
		return mapper.getOne(dto);
	}

	public IndicatorTargetDTO getOne(IndicatorTargetFilterDTO dto) throws ServerException {
		try {
			return mapper.getOne(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
		}
	}

	public List<IndicatorTargetDTO> getMany(IndicatorTargetFilterDTO dto) throws ServerException {
		if (dto.getStartRow() == null)
			dto.setStartRow(0);
		if (dto.getEndRow() == null || dto.getEndRow() == 0)
			dto.setEndRow(200);
		try {
			return mapper.getMany(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
		}
	}

	public void save(IndicatorTargetDTO dto) throws ServerException {
		dto.setLlaveTabla(UUID.randomUUID().toString().replaceAll("-", ""));
		try {
			mapper.insert(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
		}
	}

	public void update(IndicatorTargetDTO dto) throws ServerException {
		try {
			mapper.update(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
		}
	}

	public IndicatorTargetDTO delete(String id) throws ServerException {
		IndicatorTargetDTO dto = getById(id);
		if (dto == null)
			throw new ServerException("No se identifica el objeto a inactivar");
		if (dto.getEstado() != null && dto.getEstado().compareTo(SharedConstants.STATE_INACTIVE) == 0)
			throw new ServerException("Este objeto ya se encuentra inactivo");
		dto.setEstado(SharedConstants.STATE_INACTIVE);
		try {
			mapper.update(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
		}
		return dto;
	}
	
	public IndicatorTargetDTO activate(IndicatorTargetDTO dto) throws ServerException {
		dto = getById(dto.getLlaveTabla());
		if (dto == null)
			throw new ServerException("No se identifica el objeto a Activar");
		if (dto.getEstado().compareTo(SharedConstants.STATE_ACTIVE) == 0)
			throw new ServerException("Este objeto ya se encuentra Activo");
		dto.setEstado(SharedConstants.STATE_ACTIVE);
		try {
			mapper.update(dto);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}

}
