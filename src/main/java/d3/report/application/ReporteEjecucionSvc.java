package d3.report.application;

import java.util.List;

import org.apache.ibatis.binding.BindingException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.report.domain.ReporteEjecucionDTO;
import d3.report.domain.ReporteEjecucionFilterDTO;
import d3.report.infrastructure.ReporteEjecucionMapper;
import d3.shared.application.BasicSvc;
import d3.shared.domain.ServerException;
import jakarta.annotation.PostConstruct;

@Service("reporteEjecucionService")
public class ReporteEjecucionSvc extends BasicSvc<ReporteEjecucionDTO, ReporteEjecucionFilterDTO> {

	private final ReporteEjecucionMapper reporteEjecucionMapper;

	public ReporteEjecucionSvc(
			@Lazy ReporteEjecucionMapper reporteEjecucionMapper) {
		this.reporteEjecucionMapper = reporteEjecucionMapper;
	}

	@Override
	public ReporteEjecucionDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. ReporteEjecucion");
		ReporteEjecucionFilterDTO dto = new ReporteEjecucionFilterDTO();
		dto.setLlaveTabla(llave);
		return reporteEjecucionMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = reporteEjecucionMapper;
	}

	@Override
	public ReporteEjecucionDTO activar(ReporteEjecucionDTO dto) throws ServerException {
		return super.activar(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ReporteEjecucionDTO actualizar(ReporteEjecucionDTO dto) throws ServerException {
		return super.actualizar(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ReporteEjecucionDTO inactivar(ReporteEjecucionDTO dto) throws ServerException {
		return super.inactivar(dto);
	}

	@Override
	public ReporteEjecucionDTO consultaUnica(ReporteEjecucionFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}

	@Override
	public int contarResultados(ReporteEjecucionFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}

	@Override
	public List<ReporteEjecucionDTO> listarConsulta(ReporteEjecucionFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ReporteEjecucionDTO guardar(ReporteEjecucionDTO dto) throws ServerException {
		throw new ServerException("No se esta usando");
	}

	@Override
	public ReporteEjecucionDTO save(ReporteEjecucionDTO dto) throws ServerException {
		throw new ServerException("No se esta usando");
	}

	public ReporteEjecucionDTO saveWithHistoric(ReporteEjecucionDTO dto, Integer historico) throws ServerException {
		if (historico == null || historico == 0) {
			return super.save(dto);
		}
		dto.setLlaveTabla(generarLlave());
		try {
			reporteEjecucionMapper.insertarHistorico(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return consultaXId(dto.getLlaveTabla());

	}

}