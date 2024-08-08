package com.softure.logisticpymes.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.softure.logisticpymes.domain.PuestoDTO;
import com.softure.logisticpymes.domain.PuestoFilterDTO;
import com.softure.logisticpymes.infrastructure.PuestoMapper;

import jakarta.annotation.PostConstruct;

@Service("puestoService")
public class PuestoSvc extends BasicSvc<PuestoDTO, PuestoFilterDTO> {
	
	@Autowired @Lazy 
	private PuestoMapper puestoMapper;
	
	// BEGIN region servicesPuesto
	// END region servicesPuesto

	@Override
	public PuestoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. Puesto");
		PuestoFilterDTO dto = new PuestoFilterDTO();
		dto.setLlaveTabla(llave);
		return puestoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = puestoMapper;
	}
	
	@Override
	public PuestoDTO activar(PuestoDTO dto, String token) throws ServerException {
		// BEGIN Puesto_activar
		return super.activar(dto, token);
		// END Puesto_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PuestoDTO actualizar( PuestoDTO dto, String token) throws ServerException {
		// BEGIN Puesto_actualizar
		//dto = colocarInterno(dto);
		return super.actualizar(dto, token);
		// END Puesto_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PuestoDTO inactivar(PuestoDTO dto, String token) throws ServerException {
		// BEGIN Puesto_inactivar
		return super.inactivar(dto, token);
		// END Puesto_inactivar
	}
	
	@Override
	public PuestoDTO consultaUnica(PuestoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(PuestoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<PuestoDTO> listarConsulta(PuestoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PuestoDTO guardar(PuestoDTO dto, String token) throws ServerException {
		// BEGIN Puesto_guardar
		return super.guardar(dto, token);
		// END Puesto_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}