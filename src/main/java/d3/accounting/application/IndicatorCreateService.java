package d3.accounting.application;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.accounting.application.base.IndicatorService;
import d3.accounting.application.base.IndicatorTargetService;
import d3.accounting.domain.IndicatorDTO;
import d3.accounting.domain.IndicatorFilterDTO;
import d3.shared.domain.ServerException;

@Service("IndicadorCreateAccountingService")
public class IndicatorCreateService {

	private final IndicatorService indicatorService;
	private final IndicatorTargetService targetService;

	public IndicatorCreateService(@Lazy IndicatorService indicatorService, @Lazy IndicatorTargetService targetService) {
		this.indicatorService = indicatorService;
		this.targetService = targetService;
	}

	public List<IndicatorDTO> getToDashboard() throws ServerException{
		return this.indicatorService.getMany(new IndicatorFilterDTO());
	}
		/*
	
	public ResultMapDTO getResultado(String indicadorId, TimeFrameDTO periodo) throws ServerException {
		IndicatorDTO indicador = getById(indicadorId);
		if (indicador == null)
			throw new ServerException("No se identifica el indicador: " + indicadorId);

		ResultMapDTO resultado = mapper.getResultadoByCodigo(indicador.getCodigo());
		if (resultado == null)
			resultado = new ResultMapDTO();
		resultado.setKey(indicadorId);
		resultado.setState(indicador.getEstado());
		return resultado;
	}

	public List<ResultMapDTO> getTabla(String indicadorId) throws ServerException {
		IndicatorDTO indicador = getById(indicadorId);
		if (indicador == null)
			throw new ServerException("No se identifica el indicador: " + indicadorId);
		List<ResultMapDTO> tabla = mapper.getTablaByCodigo(indicador.getCodigo());
		return tabla == null ? new ArrayList<>() : tabla;
	}

	private void cargarTargets(IndicatorDTO indicador) {
		if (indicador == null || indicador.getLlaveTabla() == null)
			return;
		indicador.setTargets(targetMapper.getByIndicador(indicador.getLlaveTabla()));
	}

	private void cargarTargets(List<IndicatorDTO> indicadores) {
		if (indicadores == null || indicadores.isEmpty())
			return;
		List<String> keys = indicadores.stream().map(IndicatorDTO::getLlaveTabla)
				.filter(key -> key != null).collect(Collectors.toList());
		if (keys.isEmpty())
			return;
		Map<String, List<IndicatorTargetDTO>> targets = targetMapper.getByIndicadores(keys).stream()
				.collect(Collectors.groupingBy(IndicatorTargetDTO::getIndicador));
		for (IndicatorDTO indicador : indicadores) {
			indicador.setTargets(targets.getOrDefault(indicador.getLlaveTabla(), new ArrayList<>()));
		}
	}

	private void guardarTargets(IndicatorDTO dto) {
		if (dto.getTargets() == null || dto.getTargets().isEmpty())
			return;
		List<IndicatorTargetDTO> targets = dto.getTargets().stream().filter(target -> target != null)
				.collect(Collectors.toList());
		if (targets.isEmpty())
			return;
		for (IndicatorTargetDTO target : targets) {
			target.setLlaveTabla(UUID.randomUUID().toString().replaceAll("-", ""));
			target.setIndicador(dto.getLlaveTabla());
		}
		targetMapper.insertMany(dto.getLlaveTabla(), targets);
	}
*/
}
