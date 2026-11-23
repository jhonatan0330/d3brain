package d3.accounting.application;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.accounting.application.base.IndicatorService;
import d3.accounting.application.base.IndicatorTargetService;
import d3.accounting.domain.IndicatorDTO;
import d3.accounting.domain.IndicatorFilterDTO;
import d3.accounting.domain.IndicatorTargetFilterDTO;
import d3.shared.application.D3Utils;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;

@Service("IndicadorCreateAccountingService")
public class IndicatorCreateService {

	private static final int MAX_CODE_ATTEMPTS = 100;

	private final IndicatorService indicatorService;
	private final IndicatorTargetService targetService;

	public IndicatorCreateService(@Lazy IndicatorService indicatorService, @Lazy IndicatorTargetService targetService) {
		this.indicatorService = indicatorService;
		this.targetService = targetService;
	}

	public IndicatorDTO create(IndicatorDTO dto) throws ServerException {
		if (dto == null)
			throw new ServerException("No se puede crear un indicador sin datos");
		if (dto.getNombre() == null || dto.getNombre().trim().isEmpty())
			throw new ServerException("El nombre del indicador es obligatorio");

		dto.setCodigo(this.generarCodigo(dto));
		if (dto.getEstado() == null)
			dto.setEstado(SharedConstants.STATE_ACTIVE);

		this.indicatorService.save(dto);
		return dto;
	}

	private String generarCodigo(IndicatorDTO dto) throws ServerException {
		String base = (dto.getCodigo() != null && !dto.getCodigo().trim().isEmpty())
				? D3Utils.formatFunction(dto.getCodigo())
				: D3Utils.formatFunction(dto.getNombre());

		if (base == null || base.isEmpty())
			base = "indicador";

		String codigo = base;
		int consecutivo = 2;
		while (this.existeCodigo(codigo)) {
			if (consecutivo > MAX_CODE_ATTEMPTS)
				throw new ServerException("No se pudo generar un codigo unico para el indicador");
			codigo = base + "_" + consecutivo;
			consecutivo++;
		}
		return codigo;
	}

	private boolean existeCodigo(String codigo) throws ServerException {
		IndicatorFilterDTO filtro = new IndicatorFilterDTO();
		filtro.setCodigo(codigo);
		filtro.setStartRow(0);
		filtro.setEndRow(1);
		List<IndicatorDTO> encontrados = this.indicatorService.getMany(filtro);
		return encontrados != null && !encontrados.isEmpty();
	}

	public List<IndicatorDTO> getToDashboard() throws ServerException {
		List<IndicatorDTO> indicadores = this.indicatorService.getMany(new IndicatorFilterDTO());
		for (IndicatorDTO indicador : indicadores) {
			IndicatorTargetFilterDTO filtro = new IndicatorTargetFilterDTO();
			filtro.setIndicador(indicador.getLlaveTabla());
			indicador.setTargets(this.targetService.getMany(filtro));
		}
		return indicadores;
	}
}
