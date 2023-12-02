package com.softure.process_form.application;

import java.util.List;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.logisticpymes.application.BasicSvc;
import com.softure.process_form.domain.PlantillaConsecutivoDTO;
import com.softure.process_form.domain.PlantillaConsecutivoFilterDTO;
import com.softure.process_form.infrastructure.PlantillaConsecutivoMapper;

@Service("plantillaConsecutivoService")
public class PlantillaConsecutivoSvc extends BasicSvc<PlantillaConsecutivoDTO, PlantillaConsecutivoFilterDTO> {
	
	@Autowired
	private PlantillaConsecutivoMapper plantillaConsecutivoMapper;
	
	// BEGIN region servicesPlantillaConsecutivo
	// END region servicesPlantillaConsecutivo

	@Override
	public PlantillaConsecutivoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. PlantillaConsecutivo");
		PlantillaConsecutivoFilterDTO dto = new PlantillaConsecutivoFilterDTO();
		dto.setLlaveTabla(llave);
		return plantillaConsecutivoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = plantillaConsecutivoMapper;
	}
	
	@Override
	public PlantillaConsecutivoDTO activar(PlantillaConsecutivoDTO dto, String token) throws ServerException {
		// BEGIN PlantillaConsecutivo_activar
		return super.activar(dto, token);
		// END PlantillaConsecutivo_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PlantillaConsecutivoDTO actualizar( PlantillaConsecutivoDTO dto, String token) throws ServerException {
		// BEGIN PlantillaConsecutivo_actualizar
		return super.actualizar(dto, token);
		// END PlantillaConsecutivo_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PlantillaConsecutivoDTO inactivar(PlantillaConsecutivoDTO dto, String token) throws ServerException {
		// BEGIN PlantillaConsecutivo_inactivar
		return super.inactivar(dto, token);
		// END PlantillaConsecutivo_inactivar
	}
	
	@Override
	public PlantillaConsecutivoDTO consultaUnica(PlantillaConsecutivoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(PlantillaConsecutivoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<PlantillaConsecutivoDTO> listarConsulta(PlantillaConsecutivoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PlantillaConsecutivoDTO guardar(PlantillaConsecutivoDTO dto, String token) throws ServerException {
		// BEGIN PlantillaConsecutivo_guardar
		PlantillaConsecutivoFilterDTO bdFilter = new PlantillaConsecutivoFilterDTO();
		bdFilter.setCaracteristica(dto.getCaracteristica());
		bdFilter.setValorOpcion(dto.getValorOpcion());
		bdFilter.setEstado(SharedConstants.STATE_ACTIVE);
		PlantillaConsecutivoDTO bd = consultaUnica(bdFilter);
		if(bd!=null) throw new ServerException("Ya existe una relacion entre caracteristica y opcion");
		return super.guardar(dto, token);
		// END PlantillaConsecutivo_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}