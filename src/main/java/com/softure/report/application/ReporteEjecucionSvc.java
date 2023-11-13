package com.softure.report.application;

import java.util.List;

// BEGIN region interImport
// END region interImport

import javax.annotation.PostConstruct;

import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.report.domain.ReporteEjecucionDTO;
import com.softure.report.domain.ReporteEjecucionFilterDTO;
import com.softure.report.infrastructure.ReporteEjecucionMapper;

@Service("reporteEjecucionService")
public class ReporteEjecucionSvc extends BasicSvc<ReporteEjecucionDTO, ReporteEjecucionFilterDTO> {
	
	@Autowired
	private ReporteEjecucionMapper reporteEjecucionMapper;
	
	// BEGIN region servicesReporteEjecucion
	// END region servicesReporteEjecucion

	@Override
	public ReporteEjecucionDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. ReporteEjecucion");
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
		// BEGIN ReporteEjecucion_activar
		return super.activar(dto, token);
		// END ReporteEjecucion_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ReporteEjecucionDTO actualizar( ReporteEjecucionDTO dto, String token) throws ServerException {
		// BEGIN ReporteEjecucion_actualizar
		return super.actualizar(dto, token);
		// END ReporteEjecucion_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ReporteEjecucionDTO inactivar(ReporteEjecucionDTO dto, String token) throws ServerException {
		// BEGIN ReporteEjecucion_inactivar
		return super.inactivar(dto, token);
		// END ReporteEjecucion_inactivar
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
	public List<ReporteEjecucionDTO> listarConsulta(ReporteEjecucionFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ReporteEjecucionDTO guardar(ReporteEjecucionDTO dto, String token) throws ServerException {
		// BEGIN ReporteEjecucion_guardar
		throw new ServerException("No se esta usando");
		// END ReporteEjecucion_guardar
	}

// BEGIN region aditionalMethods	
	@Override
	public ReporteEjecucionDTO save(ReporteEjecucionDTO dto) throws ServerException {
		throw new ServerException("No se esta usando");
	}

	public ReporteEjecucionDTO saveWithHistoric(ReporteEjecucionDTO dto, Integer historico) throws ServerException {
		if(historico==null || historico==0) {
			return super.save(dto);
		}else {
			dto.setLlaveTabla(generarLlave());
			try {
				reporteEjecucionMapper.insertarHistorico(dto); 
			}catch (BindingException ex) {
				throw new ServerException(ex.getMessage());
			}catch (Exception e) {
				throw new ServerException(e.getCause().getMessage());
			}
			return consultaXId(dto.getLlaveTabla());
		}
	}
// END region aditionalMethods

}