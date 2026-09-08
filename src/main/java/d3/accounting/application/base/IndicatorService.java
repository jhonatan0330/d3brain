package d3.accounting.application.base;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.binding.BindingException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.accounting.domain.DatoTablaDTO;
import d3.accounting.domain.IndicatorDTO;
import d3.accounting.domain.IndicatorFilterDTO;
import d3.accounting.domain.IndicatorResultadoDTO;
import d3.accounting.domain.PeriodoDTO;
import d3.accounting.infrastructure.IndicatorMapper;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;

@Service("IndicadorAccountingService")
public class IndicatorService {

	private final IndicatorMapper mapper;

	public IndicatorService(@Lazy IndicatorMapper mapper) {
		this.mapper = mapper;
	}

	public IndicatorDTO getById(String id) throws ServerException {
		if (id == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Indicator");
		IndicatorFilterDTO dto = new IndicatorFilterDTO();
		dto.setKey(id);
		return mapper.getOne(dto);
	}

	public IndicatorDTO getOne(IndicatorFilterDTO dto) throws ServerException {
		try {
			return mapper.getOne(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
		}
	}

	public List<IndicatorDTO> getMany(IndicatorFilterDTO dto) throws ServerException {
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

	public int count(IndicatorFilterDTO dto) throws ServerException {
		try {
			return mapper.count(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
		}
	}

	public void save(IndicatorDTO dto) throws ServerException {
		dto.setKey(UUID.randomUUID().toString().replaceAll("-", ""));
		try {
			mapper.insert(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
		}
	}

	public void update(IndicatorDTO dto) throws ServerException {
		try {
			mapper.update(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
		}
	}

	public IndicatorDTO delete(String id) throws ServerException {
		IndicatorDTO dto = getById(id);
		if (dto == null)
			throw new ServerException("No se identifica el objeto a inactivar");
		if (dto.getState() != null && dto.getState().compareTo(SharedConstants.STATE_INACTIVE) == 0)
			throw new ServerException("Este objeto ya se encuentra inactivo");
		dto.setState(SharedConstants.STATE_INACTIVE);
		try {
			mapper.update(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
		}
		return dto;
	}

	public IndicatorResultadoDTO getResultado(String indicadorId, PeriodoDTO periodo) throws ServerException {
		IndicatorDTO indicador = getById(indicadorId);
		if (indicador == null)
			throw new ServerException("No se identifica el indicador: " + indicadorId);

		BigDecimal valor = new BigDecimal(valorIndicador(indicador.getCodigo()));

		BigDecimal valorAntes = valor.multiply(new BigDecimal("0.85")).setScale(2, RoundingMode.HALF_UP);
		BigDecimal valorDespues = valor.multiply(new BigDecimal("1.08")).setScale(2, RoundingMode.HALF_UP);

		IndicatorResultadoDTO resultado = new IndicatorResultadoDTO();
		resultado.setValor(valor);
		resultado.setPeriodo(periodo);
		resultado.setValorAntes(valorAntes);
		resultado.setValorDespues(valorDespues);
		return resultado;
	}

	private String valorIndicador(String codigo) {
		if (codigo == null)
			return "0";
		switch (codigo.trim().toLowerCase()) {
			case "ventas_totales":
				return "125000000";
			case "cantidad_ventas":
				return "3480";
			case "ticket_promedio":
				return "35977";
			case "crecimiento_ventas":
				return "18.5";
			case "cumplimiento_meta":
				return "92.4";
			default:
				return "0";
		}
	}

	public List<DatoTablaDTO> getTabla(String indicadorId) throws ServerException {
		IndicatorDTO indicador = getById(indicadorId);
		if (indicador == null)
			throw new ServerException("No se identifica el indicador: " + indicadorId);
		return new ArrayList<>();
	}

}
