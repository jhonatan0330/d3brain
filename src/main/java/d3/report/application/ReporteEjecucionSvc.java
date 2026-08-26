package d3.report.application;

import java.util.List;

import org.apache.ibatis.binding.BindingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.domain.ServerException;
import d3.logisticpymes.application.BasicSvc;
import d3.report.domain.ReporteEjecucionDTO;
import d3.report.domain.ReporteEjecucionFilterDTO;
import d3.report.infrastructure.ReporteEjecucionMapper;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import d3.authentication.application.UsuarioSesionSvc;

@Service("reporteEjecucionService")
public class ReporteEjecucionSvc extends BasicSvc<ReporteEjecucionDTO, ReporteEjecucionFilterDTO> {

	private final ReporteEjecucionMapper reporteEjecucionMapper;

	public ReporteEjecucionSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy ReporteEjecucionMapper reporteEjecucionMapper) {
		super(usuarioSesionService);
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
	public ReporteEjecucionDTO activar(ReporteEjecucionDTO dto, String token) throws ServerException {
		return super.activar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ReporteEjecucionDTO actualizar(ReporteEjecucionDTO dto, String token) throws ServerException {
		return super.actualizar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ReporteEjecucionDTO inactivar(ReporteEjecucionDTO dto, String token) throws ServerException {
		return super.inactivar(dto, token);
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
	public ReporteEjecucionDTO guardar(ReporteEjecucionDTO dto, String token) throws ServerException {
		throw new ServerException("No se esta usando");
	}

	@Override
	public ReporteEjecucionDTO save(ReporteEjecucionDTO dto) throws ServerException {
		throw new ServerException("No se esta usando");
	}

	public ReporteEjecucionDTO saveWithHistoric(ReporteEjecucionDTO dto, Integer historico) throws ServerException {
		if (historico == null || historico == 0) {
			return super.save(dto);
		} else {
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

}